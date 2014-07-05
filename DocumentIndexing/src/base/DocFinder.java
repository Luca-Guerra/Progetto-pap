package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import java.util.List;

import javax.swing.SwingWorker;

public class DocFinder extends SwingWorker<Integer,Integer> {

	public static class Blackboard{
		public static int progress = 0;
		public static boolean enableSearch = false;
		public static Hashtable<String, List<String>> docIndex = new Hashtable<String, List<String>>();
	}
	
	private String path;
	private int count = 0;
	private boolean pause = false;
	private DocHelper docHelper;
	private List<File> files;
	
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
			workers[i]=new Indexer("worker"+i);
			System.out.println("Creato : worker"+ i + "\r\n");
		}
		
		files = docHelper.GetFiles();
		System.out.println("Trovati: " + files.size() + " file nel percorso dato");
		int nw = docHelper.GetNumWords();
		System.out.println("Parole contate: " + nw);
		
		
		
		for(int i=0; i<files.size(); i++){
        	workers[i%(npa-1)].AddFile(files.get(i));
        }
		System.out.println("Assegnati i file trovati ai vari processi\r\n");
		for(int i=0;i<workers.length;i++){
			System.out.println("Start : worker"+ i + "\r\n");
			workers[i].start();
		}
		int inc=0;
		boolean alive = true;
		while(alive){
			if(!pause){
				Thread.sleep(100);
				if(Blackboard.progress != 0)
					inc = Math.round(Blackboard.progress*100/nw);
				setProgress(inc);
			}else
				while(pause)
					Thread.sleep(500);//Polling solution
			
			alive=false;
			for(int i=0;i<workers.length;i++)
				if(workers[i].isAlive())
					alive=true;
		}
				
		System.out.println("Parole computate: " + Blackboard.progress);
		for(int i=0;i<workers.length;i++){
			workers[i].join();
		}
		
		return count;
	}
	
	protected void done(){
		if(isCancelled()){
			System.out.println("Task cancelled! :(");
		}else{
			Blackboard.enableSearch = true;
			System.out.println("Task Done! :)");
		}
	}
	
	public DocFinder(String path)
	{
		this.path = path;
		docHelper = new DocHelper(path);
	}
	

}
