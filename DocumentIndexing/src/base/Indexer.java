package base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import base.ManageIndexer.Blackboard;


public class Indexer extends Thread {

	public Indexer(String name){
		super(name);
	}

	public void run(){
	
		System.out.println(super.getName() + ":Inizio il lavoro :)");
		
		while(!(Blackboard.loaderFinish && Blackboard.filesQueue.isEmpty()))
			try {
				File file;
				file = Blackboard.filesQueue.take();
				Reader rdr = new Reader(file.getAbsolutePath());
				System.out.println(super.getName() + ":Aperto file:" + file.getName());
				try {
					for(String line : rdr.OpenFile()){
						//Per ogni linea devo prendere ogni parola
						String[] words = line.split(" ");
						for(String word:words)
							synchronized(Blackboard.docIndex)
							{
								List<String> record = Blackboard.docIndex.get(word);
								if(record == null)
									record = new ArrayList<String>();
								if(!record.contains(file.getName())){
									record.add(file.getName());
									Blackboard.docIndex.put(word,record);
								}
								Blackboard.progress++;
							}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		try {
			System.out.println(super.getName() + ":Terminato il lavoro :)");
			Blackboard.indexersBarrier.await();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
