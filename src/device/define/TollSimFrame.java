package device.define;

import java.awt.Container;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TollSimFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = -3707693590333333107L;
	private TollSim myDevice;
	private Container content;
	private JButton btnCoinValid;
	private JButton btnCoinInvalid;
	private JButton btnCarPassage;
	private JButton btnExit;
	private JTextArea txtStatus;
		
	private JLabel picLabelBarrierOpen;
	private JLabel picLabelBarrierClose;

	TollSimFrame(TollSim myDevice) {
		super();
		this.myDevice = myDevice;
		build();		
	}
	
	/**
	 * Build window
	 */
	private void build(){
		//Window properties
		this.setTitle("Toll simulator"); 

		this.setSize(450,300);
		this.setLocationRelativeTo(null); 				//Center
		this.setResizable(false);
		this.buildContentPanel();
		
	}
	
	/**
	 * Generate content panel
	 */
	private void buildContentPanel() {
		content = getContentPane();

		content.setLayout(null);

		//Barrier up/down
	    picLabelBarrierOpen = new JLabel( new ImageIcon( "/home/user/projects/tollbridge/src/control/open_barrier.png"));
		picLabelBarrierOpen.setBounds(10, 10, 60, 60);
		picLabelBarrierOpen.setVisible(false);
		content.add(picLabelBarrierOpen);
		picLabelBarrierClose = new JLabel( new ImageIcon( "/home/user/projects/tollbridge/src/control/close_barrier.png"));
		picLabelBarrierClose.setBounds(10, 10, 60, 60);
		picLabelBarrierClose.setVisible(false);
		content.add(picLabelBarrierClose);		

		//Button Valid coin
		btnCoinValid = new JButton("Valid coin");
		btnCoinValid.setBounds(10, 80, 130, 30);
		btnCoinValid.addActionListener(this);
		content.add(btnCoinValid);	
		
		//Button invalid coin
		btnCoinInvalid = new JButton("Invalid coin");
		btnCoinInvalid.setBounds(150, 80, 130, 30);
		btnCoinInvalid.addActionListener(this);
		content.add(btnCoinInvalid);	

		//Button car passage
		btnCarPassage = new JButton("Car passing");
		btnCarPassage.setBounds(10, 120, 100, 30);
		btnCarPassage.addActionListener(this);
		content.add(btnCarPassage);	

		//Button exit
		btnExit = new JButton("Exit");
		btnExit.setBounds(10, 160, 100, 30);
		btnExit.addActionListener(this);
		content.add(btnExit);	

		txtStatus = new JTextArea();
		txtStatus.setBounds(290, 10, 150, 250);
		content.add(txtStatus);	

	}
	
	public JTextArea getStatus() {
		return txtStatus;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == btnCoinValid){
			myDevice.setCoin(1);
		} else if(source == btnCoinInvalid){
			myDevice.setCoin(2);
		} else if(source == btnCarPassage){
			myDevice.setNewCar();
		} else if(source == btnExit){
			myDevice.setExit();
		}
	}
	
	public void setBarrierStatus(boolean status) {
		if (status == true) {
			picLabelBarrierClose.setVisible(false);
			picLabelBarrierOpen.setVisible(true);
		} else {
			picLabelBarrierClose.setVisible(true);
			picLabelBarrierOpen.setVisible(false);

		}
		
	}

	public void beep() {
	     Toolkit.getDefaultToolkit().beep();     
	}
}
