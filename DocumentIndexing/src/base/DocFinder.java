package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

import javax.swing.SwingWorker;

public class DocFinder extends SwingWorker<Integer,Integer> {

	private String path;
	private int count = 0;
	private boolean pause = false;
	private DocHelper docHelper;
	private List<File> files;
	private static Hashtable<String, List<String>> docIndex = new Hashtable<String, List<String>>();
	
	public synchronized void pause(){
		pause = true;
	}
	
	public synchronized void resume(){
		pause = false;
		this.notify();
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {
		
		Runtime runtime = Runtime.getRuntime();
		int npa = runtime.availableProcessors();
		System.out.println("Processori disponibili sulla macchina: " + npa + "\r\n");
		Indexer[] workers = new Indexer[npa];
		
		for(int i=0;i<workers.length;i++){
			workers[i]=new Indexer("worker"+i, docIndex);
			System.out.println("Creato : worker"+ i + "\r\n");
		}
		
		files = docHelper.GetFiles();
		System.out.println("Trovati: " + files.size() + " file nel percorso dato");
		for(int i=0; i<files.size(); i++){
        	workers[i%(npa-1)].AddFile(files.get(i));
        }
		System.out.println("Assegnati i file trovati ai vari processi\r\n");
		
		for(int i=0;i<workers.length;i++){
			System.out.println("Start : worker"+ i + "\r\n");
			workers[i].start();
		}
		
		/*
		for(int i=0;i<100;i++){
			if(!pause){
				Thread.sleep(200);
				count ++;
				setProgress(count);
			}else{
				while(pause){
					Thread.sleep(500);//Polling solution
				}
			}
		}*/
		
		return count;
	}
	
	protected void done(){
		if(isCancelled()){
			System.out.println("Task cancelled! :(");
		}else{
			System.out.println("Task Done! :)");
		}
	}
	
	public DocFinder(String path)
	{
		this.path = path;
		docHelper = new DocHelper(path);
	}
	

}
