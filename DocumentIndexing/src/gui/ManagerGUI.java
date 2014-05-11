package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;  //notice javax

public class ManagerGUI extends JFrame implements ActionListener {
	JPanel pane = new JPanel();
	JButton pressme = new JButton("Press Me");
	JLabel answer = new JLabel("");
	public ManagerGUI(){
	    super("Principal GUI"); 
	}
	
	public void GenerateGUI()
	{
		setBounds(100,100,300,100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); // inherit main frame
	    pressme.setMnemonic('P');
	    pressme.addActionListener(this);  //Associo il listener dell'azione
	    con.add(pane); // add the panel to frame
	    // customize panel here
	    pane.add(pressme); 
	    pane.add(answer);
	    pressme.requestFocus();
	    setVisible(true); // display this frame
	}
	
	// here is the basic event handler
	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		if (source == pressme)
		{
			answer.setText("Button pressed!");
			JOptionPane.showMessageDialog(null,"I hear you!","Message Dialog", JOptionPane.PLAIN_MESSAGE); 
			setVisible(true);  // show something
		}
	}
}
