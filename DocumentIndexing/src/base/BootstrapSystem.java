package base;

import java.util.Hashtable;
import java.util.List;

import gui.Gui;

public class BootstrapSystem {

	private static Hashtable<String, List<String>> docIndex = new Hashtable<String, List<String>>();
	private static DocFinder finder = new DocFinder();
	public static void main(String[] args)
	{
		new Gui(docIndex).GenerateGUI(finder);
	} 
	
}
