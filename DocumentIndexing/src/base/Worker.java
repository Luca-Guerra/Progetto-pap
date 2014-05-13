package base;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JTextArea;

public class Worker extends Thread {

	private List<File> listOfFiles = new ArrayList<File>();
	private Hashtable<String, List<String>> sharedData;
	private JTextArea txtArea;
	public Worker(String name, Hashtable<String,List<String>> sharedData, JTextArea txtArea){
		super(name);
		this.sharedData = sharedData;
		this.txtArea = txtArea;
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
						
						synchronized(sharedData)
						{
							List<String> record=sharedData.get(word);
							record.add(file.getName());
							sharedData.put(word,record);
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
