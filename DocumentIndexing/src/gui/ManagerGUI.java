package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;  //notice javax
import base.DocIndexing;
import base.Reader;
import base.Worker;
import java.util.List;

public class ManagerGUI extends JFrame implements ActionListener {
	JPanel panel = new JPanel();
	JTextField pathFld = new JTextField(20);
	JButton startBtn = new JButton("Start Indexing");
	JTextArea txtArea = new JTextArea(50,100);
	JScrollPane resultPnl = new JScrollPane(txtArea);
	JLabel answer = new JLabel("");
	public ManagerGUI(){
	    super("GUI"); 
	}
	
	public void GenerateGUI()
	{
		setBounds(100,50,1400,1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); 
	    startBtn.addActionListener(this); 
	    con.add(panel); 
	    panel.add(pathFld);
	    panel.add(startBtn);
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
			DocIndexing docIndx = new DocIndexing();
			//Ricavo il numero di file
			int nFiles = docIndx.GetNumberOfFiles(pathFld.getText());
			List<String> filesName = docIndx.GetFilesName(pathFld.getText());
			Runtime runtime = Runtime.getRuntime();
			int npa = runtime.availableProcessors();
			Worker[] workers = new Worker[npa];
			for(int i=0;i<workers.length;i++){
				workers[i]=new Worker("worker"+i);
			}
			for(int i=0; !filesName.isEmpty(); i++){
	        	workers[i%(npa-1)].TakeFile(filesName.get(i));
	        }
			for(int i=0;i<workers.length;i++){
				workers[i].start();
			}
		}
	}
}
