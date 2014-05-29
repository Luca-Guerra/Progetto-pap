package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;  //notice javax

import base.DocFinder;
import base.DocHelper;
import base.Reader;
import base.Indexer;

import java.util.Hashtable;
import java.util.List;

public class Gui extends JFrame implements ActionListener {
	JPanel panel = new JPanel();
	JTextField pathFld = new JTextField(20);
	JButton startBtn = new JButton("Start");
	JButton stopBtn = new JButton("Stop");
	JButton pauseBtn = new JButton("Pause");
	JTextArea txtArea = new JTextArea(25,50);
	JScrollPane resultPnl = new JScrollPane(txtArea);
	JLabel rootPath = new JLabel("path");
	private Hashtable<String, List<String>> docIndex;
	
	public Gui(Hashtable<String, List<String>> docIndex){
	    super("Document Indexing"); 
	    this.docIndex = docIndex;
	}
	
	public void GenerateGUI(DocFinder startListener)
	{
		setBounds(100,50,700,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); 
	    con.add(panel);
	    panel.add(rootPath);
	    panel.add(pathFld);
	    panel.add(startBtn);
	    startBtn.addActionListener(startListener); 
	    panel.add(pauseBtn);
	    panel.add(stopBtn);
	    
	    panel.add(resultPnl);
	    
	    txtArea.setEditable(false);
	    setVisible(true); 
	}
	
	// here is the basic event handler
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == startBtn)
		{
			txtArea.append("===========Inzio indicizzazione=========\r\n");
			DocHelper docIndx = new DocHelper();
			int nFiles = docIndx.GetNumberOfFiles(pathFld.getText());
			txtArea.append("File txt trovati: " + nFiles + "\r\n");
			List<File> files = docIndx.GetFiles(pathFld.getText());
			Runtime runtime = Runtime.getRuntime();
			int npa = runtime.availableProcessors();
			txtArea.append("Processori disponibili sulla macchina: " + npa + "\r\n");
			Indexer[] workers = new Indexer[npa];
			for(int i=0;i<workers.length;i++){
				workers[i]=new Indexer("worker"+i, docIndex, txtArea);
				txtArea.append("Creato : worker"+ i + "\r\n");
			}
			
			for(int i=0; i<files.size(); i++){
	        	workers[i%(npa-1)].AddFile(files.get(i));
	        }
			txtArea.append("Assegnati i file trovati ai vari processi\r\n");
			
			for(int i=0;i<workers.length;i++){
				txtArea.append("Start : worker"+ i + "\r\n");
				workers[i].start();
			}
		}
		if(source == stopBtn)
		{
			txtArea.append("===========Stop=========\r\n");
		}
	}
}
