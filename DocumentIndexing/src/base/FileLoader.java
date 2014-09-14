package base;

import java.io.File;
import java.io.IOException;


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
				Blackboard.filesQueue.put(file);
				Blackboard.totalWords += GetFileNumWords(file);
			}
	}
	
	public int GetFileNumWords(File file)
	{
		int n=0;
		Reader rdr = new Reader(file.getAbsolutePath());
		try{
			for(String line : rdr.OpenFile()){
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
		Blackboard.StartToLoad.release();
		File[] files = new File(_path).listFiles();
		try {
			GetFiles(files);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Blackboard.filesQueue.put(File.createTempFile("Finish", "end"));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Blackboard.FinishToLoad.release();
		System.out.println(super.getName() + "Fine caricamento files");
	}
}
	