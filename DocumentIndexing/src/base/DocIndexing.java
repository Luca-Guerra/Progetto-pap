package base;

import gui.ManagerGUI;

import java.util.Hashtable;

public class DocIndexing {

	private Hashtable<String, String[]> DocIndex = new Hashtable<String, String[]>();
	
	public static void main(String[] args)
	{
		new ManagerGUI();
		//Ciclo le varie directory
		//per ogni file trovato aggiorno la hashtable
		
		//date le parole eseguo le query per determinare in quali documenti sono presenti le varie parole passate
	} 
	
}
