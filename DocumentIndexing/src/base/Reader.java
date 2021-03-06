package base;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
/*
 * Classe helper per lettura file
 */
public class Reader {

	private String path;

	public Reader(String file_path){
		path = file_path;
	}

    //Ritorno la lista delle righe presenti nel file
	public List<String> GetLines() throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		List<String> textData = new ArrayList<String>();
		String line="";
		while((line = textReader.readLine()) != null){
			textData.add(line);
		}
		textReader.close();
		return textData;
	}

}