package base;

import gui.Gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DocIndexing {

	private Hashtable<String, List<String>> DocIndex = new Hashtable<String, List<String>>();
	
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
	
	public List<File> GetFiles(String path_origin){
		File[] files = new File("C:/TestingUniversita").listFiles();
		return GetFiles(files);
	}
	
	public List<File> GetFiles(File[] files){
		List<File> txtFiles = new ArrayList<File>();
		for (File file : files) {
			if(file.isDirectory()){
				txtFiles.addAll(GetFiles(file.listFiles()));
			}else if(file.getName().contains(".txt")){
				txtFiles.add(file);
			}
	    }
		return txtFiles;
	}
	
	public static void main(String[] args)
	{
		new Gui().GenerateGUI();
	} 
	
}
	