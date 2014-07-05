package base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import base.DocFinder.Blackboard;


public class Indexer extends Thread {

	private List<File> listOfFiles = new ArrayList<File>();
	
	public Indexer(String name){
		super(name);
	}
	
	public void AddFile(File file){
		listOfFiles.add(file);
	}
	
	public void run(){
		if(listOfFiles.size() <= 0)
			return;
		
		System.out.println(super.getName() + ":Inizio il lavoro :)");
		
		for(File file:listOfFiles){
			Reader rdr = new Reader(file.getAbsolutePath());
			System.out.println(super.getName() + ":Aperto file:" + file.getName());
			try{
				for(String line : rdr.OpenFile()){
					//Per ogni linea devo prendere ogni parola
					String[] words = line.split(" ");
					for(String word:words)
						synchronized(Blackboard.docIndex)
						{
							List<String> record = Blackboard.docIndex.get(word);
							if(record == null)
								record = new ArrayList<String>();
							record.add(file.getName());
							Blackboard.docIndex.put(word,record);
							Blackboard.progress++;
						}
				}
			}catch(Exception ex){
				System.out.println(super.getName() + ": :( problema con il file:" + file.getName());
			}
		}
	}
}
