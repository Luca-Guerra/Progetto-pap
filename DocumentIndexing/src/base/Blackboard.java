package base;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class Blackboard {
	public static ExecutorService exec;
	public static int NTHREADS = Runtime.getRuntime().availableProcessors() + 1;
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
		LoaderFinished	=	false;
		FinishToLoad 	= 	new Semaphore(0,true);
		pause 			= 	false;
		progress 		= 	0;
		totalWords		=	0;
		filesQueue 		= 	new LinkedBlockingDeque<File>();
		indexersBarrier = 	new CyclicBarrier(NTHREADS, new Runnable() {
															public void run() { 
																exec.shutdown();
															}
														});
		restartSignal 	= 	new CountDownLatch(1);
	}
}
