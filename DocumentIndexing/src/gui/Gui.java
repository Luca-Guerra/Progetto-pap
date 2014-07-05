package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;  //notice javax
import base.DocFinder;
import base.DocFinder.Blackboard;

import java.util.List;

public class Gui extends JFrame {
	private boolean pause = false;
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
	
	JProgressBar progressBar = new JProgressBar(0,100);
	DocFinder finderTask = null;
	
	public Gui(){
	    super("Document Indexing"); 
	}
	
	public void GenerateGUI()
	{
		pathFld.setText("C:\\TestingUniversita");
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
	    
	    findBtn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		if(Blackboard.enableSearch)
	    		{
	    			String[] colNames = {"File", "Occorrenze"};
	    			Object [] [] data;
	    			String word = wordFld.getText();
	    			List<String> res = Blackboard.docIndex.get(word);
	    			data=new Object[res.size()][2];
	    			if(res == null)
	    				JOptionPane.showMessageDialog(null, "Non è stata trovata nessuna parola! :(");
	    			else
	    				for(int i =0;i<res.size();i++)
	    				{
	    					data[i][0]=(res.get(i) + "\r\n");
	    					data[i][1]="1";
	    				}
	    			
	    			 table = new JTable(data, colNames);
	    			 JScrollPane scrollPane = new JScrollPane(table);
    				 table.setFillsViewportHeight(true);
	    			 panel.add(scrollPane);
	    			 panel.add(new JButton("prova"));
	    			 panel.revalidate();
	    			 validate();
	    		}
	    	}
	    });
	    
	    setBounds(100,50,500,500);
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
