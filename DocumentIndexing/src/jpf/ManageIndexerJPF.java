package jpf;

import java.util.concurrent.Executors;

public class ManageIndexerJPF {
	public static void main(String[] args) throws InterruptedException
	{
		BlackboardJPF.exec = Executors.newFixedThreadPool(BlackboardJPF.NTHREADS);
		//Creazione workers
		IndexerJPF[] workers = new IndexerJPF[BlackboardJPF.NTHREADS];
		for(int i=0;i<workers.length;i++)
			workers[i]=new IndexerJPF("worker"+i);
		
		//Start loader e indexers
		BlackboardJPF.exec.execute(new FileLoaderJPF("Loader"));
		BlackboardJPF.StartToLoad.acquire();
		for(int i=0;i<(BlackboardJPF.NTHREADS);i++)
			BlackboardJPF.exec.execute(workers[i]);
		
	}
}
