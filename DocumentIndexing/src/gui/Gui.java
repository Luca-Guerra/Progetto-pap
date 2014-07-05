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
	JLabel rootPath = new JLabel("path");
	JProgressBar progressBar = new JProgressBar(0,100);
	DocFinder finderTask = null;
	public Gui(){
	    super("Document Indexing"); 
	}
	
	public void GenerateGUI()
	{
	    startBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(!pause && (finderTask == null || finderTask.isCancelled() || finderTask.isDone())){
	    			finderTask = new DocFinder(pathFld.getText());
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
	    
	    setVisible(true); 
	}
	
	public static void main(String[] args)
	{
		new Gui().GenerateGUI();
	}
}
