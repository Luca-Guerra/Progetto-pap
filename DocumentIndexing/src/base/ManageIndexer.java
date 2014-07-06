package base;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

public class ManageIndexer extends SwingWorker<Integer,Integer> {

	public static class Blackboard{
		public static int 					progress 		= 	0;
		public static boolean 				loaderFinish	=	false;
		public static boolean 				enableSearch 	= 	false;
		public static int 					totalfile		=	0;
		public static BlockingQueue<File> 	filesQueue 		= 	new LinkedBlockingDeque<File>();
		public static CyclicBarrier 		indexersBarrier = 	new CyclicBarrier(NTHREADS, new Runnable() {
																								public void run() { 
																									exec.shutdown();
																								}
																  		  					});
		public static Hashtable<String, List<String>> docIndex = new Hashtable<String, List<String>>();
	}
	
	private int count = 0;
	private String _path="";
	private boolean pause = false;
	private static final int NTHREADS = Runtime.getRuntime().availableProcessors() + 1;
	private static final ExecutorService exec = Executors.newFixedThreadPool(NTHREADS);
	
	public synchronized void pause(){
		pause = true;
	}
	
	public synchronized void resume(){
		pause = false;
		this.notify();
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		//Creazione workers
		Indexer[] workers = new Indexer[NTHREADS];
		for(int i=0;i<workers.length;i++){
			workers[i]=new Indexer("worker"+i);
			System.out.println("Creato : worker"+ i + "\r\n");
		}
		//Esecuzione FileLoader
		exec.execute(new FileLoader("Loader",_path));
		//Es
		for(int i=0;i<workers.length;i++)
			exec.execute(workers[i]);
		
		while(!Blackboard.loaderFinish)
			Thread.sleep(100); //Polling solution it's not the best.
		
		int nw = Blackboard.totalfile;
		int inc=0;
		while(!exec.isTerminated()){
			if(!pause){
				Thread.sleep(100);
				if(nw != 0)
					inc = Math.round(Blackboard.progress*100/nw);
				setProgress(inc);
			}else
				while(pause)
					Thread.sleep(500);//Polling solution
		}
				
		System.out.println("Parole computate: " + Blackboard.progress);
		exec.awaitTermination(Long.MAX_VALUE,TimeUnit.SECONDS);
		return count;
	}
	
	protected void done(){
		if(isCancelled()){
			System.out.println("Indicizzazione cancellata! :(");
		}else{
			Blackboard.progress = 0;
			Blackboard.loaderFinish = false;
			Blackboard.enableSearch = true;
			System.out.println("Fine indicizzazione.");
		}
	}
	
	public ManageIndexer(String path)
	{
		_path = path;
	}
	
}
