package base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;

import gui.Gui;

public class BootstrapSystem implements ActionListener {

	private static Hashtable<String, List<String>> docIndex = new Hashtable<String, List<String>>();

	
	public static void main(String[] args)
	{
		new Gui(docIndex).GenerateGUI();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	} 
	
}
