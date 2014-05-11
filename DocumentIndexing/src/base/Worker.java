package base;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Worker extends Thread {

	private List<String> listOfFiles = new ArrayList<String>();
	
	public Worker(String name, Hashtable<String,String[]> sharedData){
		super(name);
	}
	
	public Worker(String name){
		super(name);
	}
	
	public void TakeFile(String file){
		listOfFiles.add(file);
	}
	
	public void run(){
		
		//Reader rdr = new Reader(pathFld.getText());
		/*try{
			for(String line : rdr.OpenFile()){
				txtArea.append(line+"\r\n");
			}
		}catch(Exception ex)
		{
			txtArea.append("====READING PROBLEMS===="+ ex.getMessage());
		}*/
	}
}
