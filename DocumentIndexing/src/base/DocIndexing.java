package base;

import gui.ManagerGUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DocIndexing {

	private Hashtable<String, String[]> DocIndex = new Hashtable<String, String[]>();
	
	public int GetNumberOfFiles(String path_origin){
		File[] files = new File("C:/TestingUniversita").listFiles();
		return GetNumberOfFiles(files);
	}
	
	public int GetNumberOfFiles(File[] files){
		int numFiles = 0;
		for (File file : files) {
			if(file.isDirectory()){
				numFiles += GetNumberOfFiles(file.listFiles());
			}else if(file.getName().contains(".txt")){
				numFiles++;
			}
	    }
		return numFiles;
	}
	
	public List<String> GetFilesName(String path_origin){
		File[] files = new File("C:/TestingUniversita").listFiles();
		return GetFilesName(files);
	}
	
	public List<String> GetFilesName(File[] files){
		List<String> filesName = new ArrayList<String>();
		for (File file : files) {
			if(file.isDirectory()){
				filesName.addAll(GetFilesName(file.listFiles()));
			}else if(file.getName().contains(".txt")){
				filesName.add(file.getName());
			}
	    }
		return filesName;
	}
	
	public static void main(String[] args)
	{
		new ManagerGUI().GenerateGUI();
	} 
	
}
	