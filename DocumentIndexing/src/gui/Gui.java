package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;  //notice javax

import base.Blackboard;
import base.ManageIndexer;

import java.util.ArrayList;
import java.util.List;

public class Gui extends JFrame {
	JPanel 		panel 	 = new JPanel();
	JTextField 	pathFld  = new JTextField(20);
	JTextField 	wordFld  = new JTextField(20);
	JButton 	startBtn = new JButton("Start");
	JButton 	stopBtn  = new JButton("Stop");
	JButton 	pauseBtn = new JButton("Pause");
	JButton 	findBtn  = new JButton("Find");
	JLabel 		pathlbl  = new JLabel("path");
	JLabel 		wordlbl  = new JLabel("find");
	JTable 		table;
	JScrollPane scrollPanel;
	JProgressBar progressBar = new JProgressBar(0,100);
	ManageIndexer finderTask = null;
	
	public Gui(){
	    super("Document Indexing"); 
	}
	
	public void GenerateGUI()
	{
		pathFld.setText("C:\\TestingUniversita");
	    startBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(!Blackboard.pause && (finderTask == null || finderTask.isCancelled() || finderTask.isDone())){
	    			assert Blackboard.pause = true;
	    			finderTask = new ManageIndexer(pathFld.getText());
	    			finderTask.addPropertyChangeListener(new PropertyChangeListener(){
	    				public void propertyChange(PropertyChangeEvent evt) {
	    					if("progress".equals(evt.getPropertyName()))
	    						progressBar.setValue((Integer) evt.getNewValue());
	    				}
	    		    });
	    			finderTask.execute();
	    			Blackboard.pause = false;
	    			
	    		}else{
	    			finderTask.resume();
	    			Blackboard.pause = false;
	    			Blackboard.restartSignal.countDown();
	    		}
	    	}
	    }); 
	    pauseBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		finderTask.pause();
	    		Blackboard.pause = true;
	    	}
	    });
	    stopBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		finderTask.cancel(true);
	    	}
	    });
	    
	    findBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(Blackboard.enableSearch)
	    		{
	    			String[] colNames = {"File"};
	    			Object [] [] data;
	    			String[] words = wordFld.getText().split(",");
	    			
	    			List<String> res = new ArrayList<String>();
	    			for(String word:words){
	    				List<String> files = Blackboard.docIndex.get(word);
	    				if(files!=null)
	    					for(String file:files)
	    						res.add(file);
	    			}
	    			if(res.size() == 0)
	    				JOptionPane.showMessageDialog(null, "Non è stata trovata nessuna occorrenza per le parole date");
	    			else{
	    				data=new Object[res.size()][1];
	    				if(scrollPanel != null)
	    					panel.remove(scrollPanel);
	    				for(int i =0;i<res.size();i++)
	    					data[i][0]=res.get(i);
	    				
	    				table = new JTable(data, colNames);
		    			scrollPanel = new JScrollPane(table);
	    				table.setFillsViewportHeight(true);
		    			panel.add(scrollPanel);
	    			}	    			
	    			panel.revalidate();
	    			validate();
	    		}
	    	}
	    });
	    
	    setBounds(100,50,500,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); 
	    con.add(panel);
	    panel.add(pathlbl);
	    panel.add(pathFld);
	    panel.add(startBtn);
	    panel.add(pauseBtn);
	    panel.add(stopBtn);
	    panel.add(progressBar);
	    panel.add(wordlbl);
	    panel.add(wordFld);
	    panel.add(findBtn);
	    setVisible(true); 
	}
	
	public static void main(String[] args)
	{
		new Gui().GenerateGUI();
	}
}
