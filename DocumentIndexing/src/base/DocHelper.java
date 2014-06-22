package base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocHelper {

	private String path;
	
	public List<File> GetFiles(){
		File[] files = new File(path).listFiles();
		return GetFiles(files);
	}
	
	public List<File> GetFiles(File[] files){
		List<File> docFiles = new ArrayList<File>();
		if(docFiles.size() <= 0){
			return new ArrayList<File>();
		}
		for (File file : files) {
			if(file.isDirectory()){
				docFiles.addAll(GetFiles(file.listFiles()));
			}else if(file.getName().contains(".txt")){
				docFiles.add(file);
			}
	    }
		return docFiles;
	}
	
	public DocHelper(String path)
	{
		this.path = path;
	}
}
	