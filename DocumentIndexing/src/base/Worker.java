package base;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Worker extends Thread {

	private List<File> listOfFiles = new ArrayList<File>();
	private Hashtable<String, List<String>> sharedData = new Hashtable<String, List<String>>();
	
	public Worker(String name, Hashtable<String,String[]> sharedData){
		super(name);
	}
	
	public Worker(String name){
		super(name);
	}
	
	public void AddFile(File file){
		listOfFiles.add(file);
	}
	
	public void run(){
		for(File file:listOfFiles){
			Reader rdr = new Reader(file.getAbsolutePath());
			try{
				for(String line : rdr.OpenFile()){
					//Per ogni linea devo prendere ogni parola
					String[] words = line.split(" ");
					for(String word:words){
						List<String> record=sharedData.get(word);
						if(record == null){
							record.add(file.getName());
							sharedData.put(word,record);
						}else{
							record.add(file.getName());
							sharedData.put(word, record);
						}
					}
				}
			}catch(Exception ex)
			{
				//Do something
			}
		}
	}
}
