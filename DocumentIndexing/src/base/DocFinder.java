package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.SwingWorker;

public class DocFinder extends SwingWorker<Integer,Integer> {

	private int count = 0;
	private boolean pause = false;
	
	public synchronized void pause(){
		pause = true;
	}
	
	public synchronized void resume(){
		pause = false;
		this.notify();
	}
	
	
	@Override
	protected Integer doInBackground() throws Exception {
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
		}
		
		return count;
	}
	
	protected void done(){
		if(isCancelled()){
			System.out.println("Task cancelled! :(");
		}else{
			System.out.println("Task Done! :)");
		}
	}

}
