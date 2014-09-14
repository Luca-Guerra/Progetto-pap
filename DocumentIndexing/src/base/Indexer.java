package base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import gov.nasa.jpf.vm.Verify;


public class Indexer extends Thread {

	public Indexer(String name){
		super(name);
	}

	public void run(){
		System.out.println(super.getName() + ":Inizio il lavoro :)");
		while(true){
			if(Blackboard.pause)
				try {
					System.out.println(super.getName() + ":Entro in pausa :)");
					Blackboard.restartSignal.await();
					System.out.println(super.getName() + ":Riparto :)");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			
			try {
				File file;
				if(Blackboard.filesQueue.isEmpty())
					break;
				file = Blackboard.filesQueue.take();
				if(file.getName().startsWith("Finish") && file.getName().endsWith("end"))
				{
					try {
						Blackboard.filesQueue.put(File.createTempFile("Finish", "end"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				Reader rdr = new Reader(file.getAbsolutePath());
				System.out.println(super.getName() + ":Aperto file:" + file.getName());
				try {
					for(String line : rdr.OpenFile()){
						//Per ogni linea devo prendere ogni parola
						String[] words = line.split(" ");
						for(String word:words)
							synchronized(Blackboard.docIndex)
							{
								Verify.beginAtomic();
								List<String> record = Blackboard.docIndex.get(word);
								if(record == null)
									record = new ArrayList<String>();
								if(!record.contains(file.getName())){
									record.add(file.getName());
									Blackboard.docIndex.put(word,record);
								}
								Blackboard.progress++;
								Verify.endAtomic();
							}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Blackboard.indexersBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
		
		System.out.println(super.getName() + ":lavoro finito :)");
	}
}
