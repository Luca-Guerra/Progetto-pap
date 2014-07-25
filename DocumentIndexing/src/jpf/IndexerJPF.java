package jpf;

import gov.nasa.jpf.vm.Verify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class IndexerJPF extends Thread {

	public IndexerJPF(String name){
		super(name);
	}

	public void run(){
		System.out.println("parto");
		while(true){
			try {
				int file;
				file = BlackboardJPF.filesQueue.take();
				if(file == -1)
				{
					BlackboardJPF.filesQueue.add(-1);
					break;
				}
				synchronized(BlackboardJPF.docIndex)
				{
					Verify.beginAtomic();
					List<Integer> files = BlackboardJPF.docIndex.get(file);
					if(files == null)
						files = new ArrayList<Integer>();
					if(!files.contains(file)){
						files.add(file);
						BlackboardJPF.docIndex.put(file,files);
					}
					BlackboardJPF.progress++;
					Verify.endAtomic();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			BlackboardJPF.indexersBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		System.out.println("finito");
	}
}
