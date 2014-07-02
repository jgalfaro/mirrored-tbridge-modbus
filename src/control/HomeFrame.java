package control;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;

public class HomeFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private Container content;

	private JTextArea messageLog;
	
	HomeFrame() {
		super();
		build();
	}
	
	private void build(){
		//Window properties
		this.setTitle("Bridge Control Center"); 
		this.setSize(700,400);
		this.setLocationRelativeTo(null); 				//Center
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.buildMenu();
		this.buildContentPanel();
		
	}
	
	private void buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		 
		JMenu menu1 = new JMenu("Menu");
  
	    JMenuItem mnuAddToll = new JMenuItem("Add toll");
	    mnuAddToll.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	        	ControlCenter.addToll();
	          }
	        });
		menu1.add(mnuAddToll);

	    JMenuItem mnuAddBridge = new JMenuItem("Add bridge");
	    mnuAddBridge.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	        	ControlCenter.addBridge();
	          }
	        });
		menu1.add(mnuAddBridge);

		
	    JMenuItem mnuExit = new JMenuItem("Exit");
	    mnuExit.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	        	ControlCenter.exit();
	          }
	        });
		menu1.add(mnuExit);

		menuBar.add(menu1);		
		
		JMenuItem menuAbout = new JMenuItem("?");
		menuBar.add(menuAbout);

		setJMenuBar(menuBar);	
				
	}
	
	private void buildContentPanel () {
		content = getContentPane();

		content.setLayout(null);
		//content.setBackground(Color.WHITE);
		
		messageLog = new JTextArea();
		messageLog.setBounds(5, 5, 100, 300);
		messageLog.setBackground(Color.white);
		messageLog.setEditable(false);
		content.add(messageLog);

	}
	
	public Container getContent() {
		return content;
	}

	public void addMessage(String message) {
		
		String currentText = messageLog.getText();
		if (currentText.length() > 200) {
			currentText = currentText.substring(0, 200);
		}
		messageLog.setText(message + "\n" + currentText); 
		
	}
}