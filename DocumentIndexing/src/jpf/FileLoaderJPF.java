package jpf;

import gov.nasa.jpf.vm.Verify;
import java.util.Random;


public class FileLoaderJPF extends Thread {

	public FileLoaderJPF(String name){
		super(name);
	}
	
	public void GetFiles(int[] files) throws InterruptedException{
		if(files.length <= 0)
			return;
		for (int i=0;i <files.length ; i++){
			BlackboardJPF.filesQueue.put(files[i]);
		}
	}
	
	public int GetFileNumWords(int file)
	{
		Random r = new Random();
		return r.nextInt(100);
	}
	
	public void run()
	{
		BlackboardJPF.StartToLoad.release();
		Verify.beginAtomic();
		int[] files = new int[4];
		for(int i = 0; i < files.length; i++)
			files[i] = i;
		Verify.endAtomic();
		try {
			GetFiles(files);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			BlackboardJPF.filesQueue.put(-1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//BlackboardJPF.FinishToLoad.release();
	}
}
