package jpf;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class BlackboardJPF {
	public static ExecutorService exec;
	public static int NTHREADS = 2;
	public static boolean 				LoaderFinished 	= 	false;
	public static Semaphore 			StartToLoad 	=	new Semaphore(0,true);
	public static Semaphore 			FinishToLoad 	= 	new Semaphore(0,true);
	public static boolean 				pause 			= 	false;
	public static int 					progress 		= 	0;
	public static boolean 				enableSearch 	= 	false;
	public static int 					totalWords		=	0;
	public static LinkedBlockingDeque<Integer>filesQueue 		= 	new LinkedBlockingDeque<Integer>();
	public static CyclicBarrier 		indexersBarrier = 	new CyclicBarrier(NTHREADS, new Runnable() {
																							public void run() { 
																								exec.shutdown();
																								/*
																								 * Verifico che il numero di file controllati sia
																								 * effettivamente uguale al numero effettivo dei file, 
																								 * in questa simulazione 4 
																								 */
																								assert progress == 4;
																							}
															  		  					});
	public static CountDownLatch 		restartSignal 	= 	new CountDownLatch(1);
	public static Hashtable<Integer, List<Integer>> docIndex = new Hashtable<Integer, List<Integer>>();
	
}
