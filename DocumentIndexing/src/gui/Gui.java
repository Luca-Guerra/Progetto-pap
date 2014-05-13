package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;  //notice javax

import base.DocIndexing;
import base.Reader;
import base.Worker;

import java.util.Hashtable;
import java.util.List;

public class Gui extends JFrame implements ActionListener {
	JPanel panel = new JPanel();
	JTextField pathFld = new JTextField(20);
	JButton startBtn = new JButton("Start Indexing");
	JButton stopBtn = new JButton("Stop Indexing");
	JButton pauseBtn = new JButton("Pause Indexing");
	JTextArea txtArea = new JTextArea(25,50);
	JScrollPane resultPnl = new JScrollPane(txtArea);
	JLabel answer = new JLabel("");
	private Hashtable<String, List<String>> docIndex;
	public Gui(Hashtable<String, List<String>> docIndex){
	    super("GUI"); 
	    this.docIndex = docIndex;
	}
	
	public void GenerateGUI()
	{
		setBounds(100,50,700,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); 
	    con.add(panel); 
	    panel.add(pathFld);
	    panel.add(startBtn);
	    startBtn.addActionListener(this); 
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
			DocIndexing docIndx = new DocIndexing();
			int nFiles = docIndx.GetNumberOfFiles(pathFld.getText());
			txtArea.append("File txt trovati: " + nFiles + "\r\n");
			List<File> files = docIndx.GetFiles(pathFld.getText());
			Runtime runtime = Runtime.getRuntime();
			int npa = runtime.availableProcessors();
			txtArea.append("Processori disponibili sulla macchina: " + npa + "\r\n");
			Worker[] workers = new Worker[npa];
			for(int i=0;i<workers.length;i++){
				workers[i]=new Worker("worker"+i, docIndex, txtArea);
				txtArea.append("Creato : worker"+ i + "\r\n");
			}
			
			for(int i=0; i<files.size(); i++){
	        	workers[i%(npa-1)].AddFile(files.get(i));
	        }
			txtArea.append("Assegnati i file trovati ai vari processi\r\n");
			
			
			for(int i=0;i<workers.length;i++){
				workers[i].start();
			}
		}
	}
}
