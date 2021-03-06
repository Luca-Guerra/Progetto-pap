package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileLoader extends Thread {

	private String _path;
	
	public FileLoader(String name, String path)
	{
		super(name);
		_path = path;
	}
	
	public FileLoader(String name){
		super(name);
	}
	
	public void GetFiles(File[] files) throws InterruptedException{
		if(files.length <= 0)
			return;
		for (File file : files) 
			if(file.isDirectory())
				GetFiles(file.listFiles());
			else if(file.getName().contains(".txt")){
				//Inserisco il file in queue
				Blackboard.filesQueue.put(file);
				//Calcolo e inserisco in blackboard il nuomero di parole del file
				Blackboard.totalWords += GetFileNumWords(file);
			}
	}
	
	public int GetFileNumWords(File file)
	{
		int n=0;
		Reader rdr = new Reader(file.getAbsolutePath());
		try{
			//per ogni riga splitto le parole e le conto
			for(String line : rdr.GetLines()){
				String[] words = line.split(" ");
				n += words.length;	
			}
		}catch(Exception ex){
			System.out.println(super.getName() + "problema con il file:" + file.getName());
		}
		return n;
	}
	
	public void run()
	{
		System.out.println(super.getName() + "Inizio caricamento files.");
		//Comunico l'inizio dell'indicizzazione(ovvero l'executor mi ha dato la parola)
		Blackboard.StartToLoad.release();
		//Ottengo la lista di file
		File[] files = new File(_path).listFiles();
		try {
			//Carico i file nella queue
			GetFiles(files);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			//Inserisco in queue la poison pill
			Blackboard.filesQueue.put(File.createTempFile("Finish", "end"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Comunico il termine del caricamento
		Blackboard.FinishToLoad.release();
		System.out.println(super.getName() + "Fine caricamento files");
	}
}
	