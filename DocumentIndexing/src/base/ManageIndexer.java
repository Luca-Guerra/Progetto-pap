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

	private int count = 0;
	private String _path="";
	private static final int NTHREADS = Runtime.getRuntime().availableProcessors() + 1;

	public synchronized void pause(){
		Blackboard.pause = true;
	}
	
	public synchronized void resume(){
		Blackboard.pause = false;
		this.notify();
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		Blackboard.exec = Executors.newFixedThreadPool(NTHREADS);
		//Creazione workers
		Indexer[] workers = new Indexer[NTHREADS];
		for(int i=0;i<workers.length;i++){
			workers[i]=new Indexer("worker"+i);
			System.out.println("Creato: worker"+ i + "\r\n");
		}
		//Start loader e indexers
		Blackboard.exec.execute(new FileLoader("Loader",_path));
		//Garantisco che il primo processo a partire sia il Loader 
		//(altrimenti potevano partire tutti i worker e lui rimanere fuori dall'esecuzione) fixed con JPF
		Blackboard.StartToLoad.acquire();
		for(int i=0;i<workers.length;i++)
			Blackboard.exec.execute(workers[i]);
		
		//Ora posso inizare a mostrare l'avanzamento nella barra
		//prima non potevo perchè non avevo il numero di parole totali
		Blackboard.FinishToLoad.acquire();
		
		int inc=0;
		while(!Blackboard.exec.isTerminated()){
			if(!Blackboard.pause){
				Thread.sleep(100);
				if(Blackboard.totalWords != 0)
					inc = Math.round(Blackboard.progress * 100/Blackboard.totalWords);
				setProgress(inc);//Comunico la nuova progressione all'evt
			}else
				while(Blackboard.pause)
					Thread.sleep(500);//Polling solution
		}
		//Attendo il termine dell'executor
		Blackboard.exec.awaitTermination(Long.MAX_VALUE,TimeUnit.SECONDS);
		System.out.println("Parole computate: " + Blackboard.progress);
		return count;
	}
	
	protected void done(){
		if(isCancelled()){
			//Spendo bruscamente il pool
			Blackboard.exec.shutdownNow();
			try {
				Blackboard.exec.awaitTermination(Long.MAX_VALUE,TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//Resetto la Blackboard per una nuova esecuzione
			Blackboard.Reset();
			System.out.println("Indicizzazione cancellata! :(");
		}else{
			Blackboard.Reset();
			//Comunico che è possibile d'ora cercare le parole nell'indicizzazione
			Blackboard.enableSearch = true;
			System.out.println("Fine indicizzazione.");
		}
	}
	
	public ManageIndexer(String path)
	{
		_path = path;
	}
	
}
