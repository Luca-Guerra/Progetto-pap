package base;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JTextArea;

public class Indexer extends Thread {

	private List<File> listOfFiles = new ArrayList<File>();
	private Hashtable<String, List<String>> sharedData;
	public Indexer(String name, Hashtable<String,List<String>> sharedData){
		super(name);
		this.sharedData = sharedData;
	}
	
	public Indexer(String name){
		super(name);
	}
	
	public void AddFile(File file){
		listOfFiles.add(file);
	}
	
	public void run(){
		System.out.println(super.getName() + ":Inizio il lavoro :)");
		if(listOfFiles.size() <= 0)
			return;
		
		for(File file:listOfFiles){
			System.out.println(super.getName() + ":Aperto file:" + file.getName());
			Reader rdr = new Reader(file.getAbsolutePath());
			try{
				for(String line : rdr.OpenFile()){
					//Per ogni linea devo prendere ogni parola
					String[] words = line.split(" ");
					for(String word:words)
						synchronized(sharedData)
						{
							List<String> record=sharedData.get(word);
							record.add(file.getName());
							sharedData.put(word,record);
						}
				}
			}catch(Exception ex){
				//Do something
			}
		}
	}
}
