package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.*;  //notice javax

import base.DocFinder;
import base.DocHelper;
import base.Reader;
import base.Indexer;

import java.util.Hashtable;
import java.util.List;

public class Gui extends JFrame {
	private boolean pause = false;
	JPanel panel = new JPanel();
	JTextField pathFld = new JTextField(20);
	JButton startBtn = new JButton("Start");
	JButton stopBtn = new JButton("Stop");
	JButton pauseBtn = new JButton("Pause");
	JTextArea txtArea = new JTextArea(15,25);
	JScrollPane resultPnl = new JScrollPane(txtArea);
	JLabel rootPath = new JLabel("path");
	JProgressBar progressBar = new JProgressBar(0,100);
	private Hashtable<String, List<String>> docIndex;
	DocFinder finderTask = new DocFinder();
	public Gui(Hashtable<String, List<String>> docIndex){
	    super("Document Indexing"); 
	    this.docIndex = docIndex;
	}
	
	public void GenerateGUI()
	{
	    startBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(!pause){
	    			finderTask = new DocFinder();
	    			finderTask.addPropertyChangeListener(new PropertyChangeListener(){
	    				public void propertyChange(PropertyChangeEvent evt) {
	    					if("progress".equals(evt.getPropertyName()))
	    					progressBar.setValue((Integer) evt.getNewValue());
	    				}
	    		    });
	    			finderTask.execute();
	    			pause = false;
	    		}else{
	    			finderTask.resume();
	    			pause = false;
	    		}
	    	}
	    }); 
	    pauseBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		finderTask.pause();
	    		pause = true;
	    	}
	    });
	    stopBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		finderTask.cancel(true);
	    	}
	    });
	    
	    setBounds(100,50,700,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); 
	    con.add(panel);
	    panel.add(rootPath);
	    panel.add(pathFld);
	    panel.add(startBtn);
	    panel.add(pauseBtn);
	    panel.add(stopBtn);
	    panel.add(progressBar);
	    panel.add(resultPnl);
	    
	    txtArea.setEditable(false);
	    setVisible(true); 
	}
	/*
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == startBtn)
		{
			txtArea.append("===========Inzio indicizzazione=========\r\n");
			DocHelper docHelper = new DocHelper(pathFld.getText());
			
			List<File> files = docHelper.GetFiles();
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
	*/
}
