package control;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class HomeFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private Container content;

	
	HomeFrame() {
		super();
		build();
	}
	
	private void build(){
		//Window properties
		this.setTitle("Bridge Control Center"); 
		this.setSize(600,400);
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
		
	}
	


	
	public Container getContent() {
		return content;
	}

	
}