package base;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

public class ManageIndexer extends SwingWorker<Integer,Integer> {

	public static class Blackboard{
		public static boolean 				LoaderFinished 	= 	false;
		public static Semaphore 			FinishToLoad 	= 	new Semaphore(0,true);
		public static boolean 				pause 			= 	false;
		public static int 					progress 		= 	0;
		public static boolean 				enableSearch 	= 	false;
		public static int 					totalWords		=	0;
		public static BlockingQueue<File> 	filesQueue 		= 	new LinkedBlockingDeque<File>();
		public static CyclicBarrier 		indexersBarrier = 	new CyclicBarrier(NTHREADS, new Runnable() {
																								public void run() { 
																									exec.shutdown();
																								}
																  		  					});
		public static CountDownLatch 		restartSignal 	= 	new CountDownLatch(1);
		public static Hashtable<String, List<String>> docIndex = new Hashtable<String, List<String>>();
		
		
		public static void Reset(){
			LoaderFinished=false;
			FinishToLoad 	= 	new Semaphore(0,true);
			pause 			= 	false;
			progress 		= 	0;
			enableSearch 	= 	false;
			totalWords		=	0;
			filesQueue 		= 	new LinkedBlockingDeque<File>();
			indexersBarrier = 	new CyclicBarrier(NTHREADS, new Runnable() {
																public void run() { 
																	exec.shutdown();
																}
															});
			restartSignal 	= 	new CountDownLatch(1);
			docIndex = new Hashtable<String, List<String>>();
		}
	}
	
	private int count = 0;
	private String _path="";
	private static final int NTHREADS = Runtime.getRuntime().availableProcessors() + 1;
	private static ExecutorService exec;
	
	public synchronized void pause(){
		Blackboard.pause = true;
	}
	
	public synchronized void resume(){
		Blackboard.pause = false;
		this.notify();
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		exec = Executors.newFixedThreadPool(NTHREADS);
		//Creazione workers
		Indexer[] workers = new Indexer[NTHREADS];
		for(int i=0;i<workers.length;i++){
			workers[i]=new Indexer("worker"+i);
			System.out.println("Creato : worker"+ i + "\r\n");
		}
		//Start loader e indexers
		exec.execute(new FileLoader("Loader",_path));
		for(int i=0;i<workers.length;i++)
			exec.execute(workers[i]);
		
		Blackboard.FinishToLoad.acquire();
		
		int inc=0;
		while(!exec.isTerminated()){
			if(!Blackboard.pause){
				Thread.sleep(100);
				if(Blackboard.totalWords != 0)
					inc = Math.round(Blackboard.progress * 100/Blackboard.totalWords);
				setProgress(inc);
			}else
				while(Blackboard.pause)
					Thread.sleep(500);//Polling solution
		}
		exec.awaitTermination(Long.MAX_VALUE,TimeUnit.SECONDS);
		System.out.println("Parole computate: " + Blackboard.progress);
		return count;
	}
	
	protected void done(){
		if(isCancelled()){
			System.out.println("Indicizzazione cancellata! :(");
		}else{
			Blackboard.Reset();
			System.out.println("Fine indicizzazione.");
		}
	}
	
	public ManageIndexer(String path)
	{
		_path = path;
	}
	
}
