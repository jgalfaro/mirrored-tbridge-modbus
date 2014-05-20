package control;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import net.wimpi.modbus.ModbusDeviceIdentification;

public class HomeFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JLabel picWaitingBoat;
	private JLabel jlbBdgBoatWaiting;
	private JLabel jlbStatusCars;
	private JLabel jlbStatusBridgeCars;
	private JLabel jlbStatusCoins;
	private JLabel jlbBdgStatus;
	private JButton btnTollOff;
	private JButton btnTollFree;
	private JButton btnTollPay;
	private JButton btnBridgeBarrierOff;
	private JButton btnBridgeBarrierOn;
	private JButton btnBridgeActivateOn;
	private JButton btnBridgeActivateOff;
	private JButton btnBridgeMoveStop;
	private JButton btnBridgeMoveUp;
	private JButton btnBridgeMoveDown;

	private JLabel picLabelBarrierOpen;
	private JLabel picLabelBarrierClose;
	private JLabel picLabelBarrierBridgeOpen;
	private JLabel picLabelBarrierBridgeClose;
	public static final String[] listContent = {"Bridge", "Toll"};
	private Container content;

	
	HomeFrame() {
		super();
		build();
	}
	
	private void build(){
		//Window properties
		this.setTitle("Bridge Control Center"); 
		this.setSize(600,300);
		this.setLocationRelativeTo(null); 				//Center
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.buildMenu();
		this.buildContentPanel();
		
	}
	
	private void buildMenu() {
		JMenuBar menuBar = new JMenuBar();
		 
		JMenu menu1 = new JMenu("Menu");
  

	    JMenuItem mnuExit = new JMenuItem("Exit");
	    mnuExit.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	        	ControlCenter.exit();
	          }
	        });

		
		menu1.add(mnuExit);

		menuBar.add(menu1);

		JMenu menuToll1 = new JMenu("Toll 1");
	    JMenuItem menuToll1Connect = new JMenuItem("Connexion");
	    menuToll1Connect.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){	        	
	        	actionConnectToll();
	        	}
	        });
		menuToll1.add(menuToll1Connect);

	    JMenuItem menuToll1Diag = new JMenuItem("Diagnostic");
	    menuToll1Diag.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){	        	
	        	System.out.println("Showing diagnostic");
	        	ModbusDeviceIdentification ident = ControlCenter.toll1.getDeviceIdentification(1, 0);
	        	ident.print();
	        	
	        	}
	        });
		menuToll1.add(menuToll1Diag);

		menuBar.add(menuToll1);
		

		JMenu menuBridge = new JMenu("Bridge");
	    JMenuItem menuBridgeConnect = new JMenuItem("Connexion");
	    menuBridgeConnect.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){
	        	actionConnectBridge();
	        	}
	        });
		menuBridge.add(menuBridgeConnect);

	    JMenuItem menuBridgeDiag = new JMenuItem("Diagnostic");
	    menuBridgeDiag.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent event){	        	
	        	System.out.println("Showing diagnostic");
	        	ModbusDeviceIdentification ident = ControlCenter.bridge1.getDeviceIdentification(1, 0);
	        	ident.print();
	        	
	        	}
	        });
		menuBridge.add(menuBridgeDiag);

		menuBar.add(menuBridge);

		
		JMenuItem menuAbout = new JMenuItem("?");
		menuBar.add(menuAbout);

		
		setJMenuBar(menuBar);	
		
		
	}
	
	private void buildContentPanel () {
		content = getContentPane();

		content.setLayout(null);
		content.setBackground(Color.WHITE);
		
	//Bridge Panel
		Integer bridgeX = 250;
		Integer bridgeY = 0;
		
		//Bridge Name
		JLabel jlbBdgName = new JLabel("Bridge");
		jlbBdgName.setBounds(bridgeX + 10, bridgeY + 10, 100, 30);
		content.add(jlbBdgName);
				
		//Bridge status
		//TODO : to be replaced by graphic
		jlbBdgStatus = new JLabel("");
		jlbBdgStatus.setBounds(bridgeX + 100, bridgeY + 100, 200, 30);
		content.add(jlbBdgStatus);
		
		//Button Activation
		btnBridgeActivateOn = new JButton("On");
		btnBridgeActivateOn.addActionListener(this);
		btnBridgeActivateOn.setBounds(bridgeX + 150, bridgeY + 10, 60, 20);
		content.add(btnBridgeActivateOn);
		btnBridgeActivateOff = new JButton("Off");
		btnBridgeActivateOff.addActionListener(this);
		btnBridgeActivateOff.setBounds(bridgeX + 150, bridgeY + 30, 60, 20);
		content.add(btnBridgeActivateOff);
				
		//Button Bridge Moving			
		btnBridgeMoveUp = new JButton("Up");
		btnBridgeMoveUp.addActionListener(this);
		btnBridgeMoveUp.setBounds(bridgeX + 250, bridgeY + 150, 60, 20);
		content.add(btnBridgeMoveUp);
		btnBridgeMoveStop = new JButton("Stop");
		btnBridgeMoveStop.addActionListener(this);
		btnBridgeMoveStop.setBounds(bridgeX + 250, bridgeY + 170, 60, 20);
		content.add(btnBridgeMoveStop);
		btnBridgeMoveDown = new JButton("Down");
		btnBridgeMoveDown.addActionListener(this);
		btnBridgeMoveDown.setBounds(bridgeX + 250, bridgeY + 190, 60, 20);
		content.add(btnBridgeMoveDown);
				
		//Waiting boat
	    picWaitingBoat = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/boat.png"));
	    picWaitingBoat.setBounds(bridgeX + 140, bridgeY + 300, 60, 60);
	    content.add(picWaitingBoat);
		jlbBdgBoatWaiting = new JLabel("Boat waiting");
		jlbBdgBoatWaiting.setBounds(bridgeX + 140, bridgeY + 355, 200, 50);
		content.add(jlbBdgBoatWaiting);

		
		//Barrier up/down
	    picLabelBarrierBridgeOpen = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/open_barrier.png"));
		picLabelBarrierBridgeOpen.setBounds(bridgeX + 10, bridgeY + 100, 60, 60);
		content.add(picLabelBarrierBridgeOpen);
		picLabelBarrierBridgeClose = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/close_barrier.png"));
		picLabelBarrierBridgeClose.setBounds(bridgeX + 10, bridgeY + 100, 60, 60);
		content.add(picLabelBarrierBridgeClose);		
		
		//Buttons barrier
		btnBridgeBarrierOn = new JButton("Up");
		btnBridgeBarrierOn.addActionListener(this);
		btnBridgeBarrierOn.setBounds(bridgeX + 10, bridgeY + 170, 70, 20);
		content.add(btnBridgeBarrierOn);
		btnBridgeBarrierOff = new JButton("Down");
		btnBridgeBarrierOff.addActionListener(this);
		btnBridgeBarrierOff.setBounds(bridgeX + 10, bridgeY + 190, 70, 20);
		content.add(btnBridgeBarrierOff);
		
		//cars
		JLabel picLabelBridgeCars = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/cars.png"));
		picLabelBridgeCars.setBounds(bridgeX + 10, bridgeY + 220, 40, 25);
		content.add(picLabelBridgeCars);

		jlbStatusBridgeCars = new JLabel("xx");
		jlbStatusBridgeCars.setBounds(bridgeX + 60, bridgeY + 220, 30, 10);
		content.add(jlbStatusBridgeCars);		
		
	//Toll Panel
		Integer tollX = 0;
		Integer tollY = 0;
		
		
		//Toll Name
		JLabel jlbToll1Name = new JLabel("Toll 1");
		jlbToll1Name.setBounds(tollX+10, tollY +10, 100, 20);
		content.add(jlbToll1Name);
		
		//Button status
		btnTollOff = new JButton("Off");
		btnTollOff.addActionListener(this);
		btnTollOff.setBounds(tollX+10, tollY +100, 70, 20);
		content.add(btnTollOff);
		btnTollFree = new JButton("Free");
		btnTollFree.addActionListener(this);
		btnTollFree.setBounds(tollX +10 , tollY + 120, 70, 20);
		content.add(btnTollFree);
		btnTollPay = new JButton("Pay");
		btnTollPay.addActionListener(this);
		btnTollPay.setBounds(tollX +10, tollY +140, 70, 20);
		content.add(btnTollPay);
		
		//Barrier up/down
	    picLabelBarrierOpen = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/open_barrier.png"));
		picLabelBarrierOpen.setBounds(tollX +10, tollY + 40, 60, 60);
		content.add(picLabelBarrierOpen);
		picLabelBarrierClose = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/close_barrier.png"));
		picLabelBarrierClose.setBounds(tollX +10, tollY +40, 60, 60);
		content.add(picLabelBarrierClose);		
		
		//coins
		JLabel picLabelCoins = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/coins.png"));
		picLabelCoins.setBounds(tollX + 95, tollY +10, 24, 24);
		content.add(picLabelCoins);

		jlbStatusCoins = new JLabel("");
		jlbStatusCoins.setBounds(tollX + 140, tollY +10, 30, 10);
		content.add(jlbStatusCoins);

		
		//cars
		JLabel picLabelCars = new JLabel( new ImageIcon( "/home/user/projects/ControlCenter/src/control/cars.png"));
		picLabelCars.setBounds(tollX + 90, tollY + 120, 40, 25);
		content.add(picLabelCars);

		jlbStatusCars = new JLabel("");
		jlbStatusCars.setBounds(tollX + 140, tollY +120, 30, 10);
		content.add(jlbStatusCars);

		clearBridgeStatus();
		clearTollStatus();
	}
	
	public void showBridgeBarrier(boolean open) {
		if (open == true) {
			picLabelBarrierBridgeOpen.setVisible(true);
			picLabelBarrierBridgeClose.setVisible(false);
		} else {
			picLabelBarrierBridgeOpen.setVisible(false);
			picLabelBarrierBridgeClose.setVisible(true);			
		}
	}

	public void clearTollStatus() {
		btnTollOff.setBackground(null);
		btnTollFree.setBackground(null);
		btnTollPay.setBackground(null);

		picLabelBarrierOpen.setVisible(false);
		picLabelBarrierClose.setVisible(false);

		jlbStatusCoins.setText("");
		jlbStatusCars.setText("");
	}

	public void clearBridgeStatus() {
		btnBridgeActivateOff.setBackground(null);
		btnBridgeActivateOn.setBackground(null);
		btnBridgeBarrierOff.setBackground(null);
		btnBridgeBarrierOn.setBackground(null);
		btnBridgeMoveDown.setBackground(null);
		btnBridgeMoveStop.setBackground(null);
		btnBridgeMoveUp.setBackground(null);
		
		picLabelBarrierBridgeClose.setVisible(false);
		picLabelBarrierBridgeOpen.setVisible(false);

		jlbStatusBridgeCars.setText("");

		picWaitingBoat.setVisible(false);
		jlbBdgBoatWaiting.setVisible(false);
	
		//TODO Remove to replace graphics
		jlbBdgStatus.setText("");
	}

	
	public void showTollBarrier(boolean open) {
		if (open == true) {
			picLabelBarrierOpen.setVisible(true);
			picLabelBarrierClose.setVisible(false);
		} else {
			picLabelBarrierOpen.setVisible(false);
			picLabelBarrierClose.setVisible(true);			
		}
	}
	public void showBridgeBarrierMode(boolean statusBarrier) {
		
		if (statusBarrier == true) {
			btnBridgeBarrierOn.setBackground(Color.green);
			btnBridgeBarrierOff.setBackground(Color.red);
		} else {
			btnBridgeBarrierOn.setBackground(Color.red);
			btnBridgeBarrierOff.setBackground(Color.green);
		}
	}
	

	public void showBridgeActivation(boolean statusBridge) {
		
		if (statusBridge == true) {
			btnBridgeActivateOn.setBackground(Color.green);
			btnBridgeActivateOff.setBackground(Color.red);
		} else {
			btnBridgeActivateOn.setBackground(Color.red);
			btnBridgeActivateOff.setBackground(Color.green);
		}
	}

	
	public void showBridgeMode(boolean statusMove, boolean statusRaise) {
		if (statusMove == false) {
			btnBridgeMoveStop.setBackground(Color.green);
			btnBridgeMoveDown.setBackground(Color.red);
			btnBridgeMoveUp.setBackground(Color.red);
		} else {
			if (statusRaise == true) {
				btnBridgeMoveStop.setBackground(Color.red);
				btnBridgeMoveUp.setBackground(Color.green);
				btnBridgeMoveDown.setBackground(Color.red);
				
			} else {
				btnBridgeMoveStop.setBackground(Color.red);
				btnBridgeMoveUp.setBackground(Color.red);
				btnBridgeMoveDown.setBackground(Color.green);
				
			}
			
		}
	}
	
	public void showBridgeStatus(boolean bridgeUp, Integer bridgeAngle) {
		String text = "Bridge : Up (" + bridgeUp + ") Angle : " + bridgeAngle;
		jlbBdgStatus.setText(text);
	}

	public void showTollMode(boolean statusOpened, boolean statusFree) {
		if (statusOpened == false) {
			btnTollOff.setBackground(Color.green);
			btnTollFree.setBackground(Color.red);
			btnTollPay.setBackground(Color.red);
		} else {
			if (statusFree == true) {
				btnTollOff.setBackground(Color.red);
				btnTollFree.setBackground(Color.green);
				btnTollPay.setBackground(Color.red);
				
				
			} else {
				btnTollOff.setBackground(Color.red);
				btnTollFree.setBackground(Color.red);
				btnTollPay.setBackground(Color.green);
				
			}
			
		}
	}
	
	public void showWaitingBoat(boolean boatWaiting) {
		if (boatWaiting == true) {
			picWaitingBoat.setVisible(true);
			jlbBdgBoatWaiting.setVisible(true);
		} else {
			picWaitingBoat.setVisible(false);
			jlbBdgBoatWaiting.setVisible(false);
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 
		//System.out.println("Click " + source.toString());
		
		if(source == btnTollOff){
			actionSetTollParam("Off");
		} else if(source == btnTollFree){
			actionSetTollParam("Free");
		} else if(source == btnTollPay){
			actionSetTollParam("Pay");			
		} else if (source == btnBridgeBarrierOff) {
			ControlCenter.bridge1.setStatusBarrierOpen(false);
		} else if (source == btnBridgeBarrierOn) {
			ControlCenter.bridge1.setStatusBarrierOpen(true);
		} else if (source == btnBridgeActivateOff) {
			ControlCenter.bridge1.setStatusActivate(false);
		} else if (source == btnBridgeActivateOn) {
			ControlCenter.bridge1.setStatusActivate(true);
		} else if (source == btnBridgeMoveStop) {
			ControlCenter.bridge1.setStatusBridgeRaise(true);
			ControlCenter.bridge1.setStatusBridgeMove(false);
		} else if (source == btnBridgeMoveUp) {
			ControlCenter.bridge1.setStatusBridgeRaise(true);
			ControlCenter.bridge1.setStatusBridgeMove(true);
		} else if (source == btnBridgeMoveDown) {
			ControlCenter.bridge1.setStatusBridgeRaise(false);
			ControlCenter.bridge1.setStatusBridgeMove(true);
		} else {
			System.out.println("Clic " + source.toString());	
		}
	}

	private void actionSetTollParam(String mode) {
		if (ControlCenter.toll1.isConnected()) {
			if (mode == "Off") {
				ControlCenter.toll1.setStatusOpened(false);
				ControlCenter.toll1.setStatusFree(false);
			} else if (mode == "Free") {
				ControlCenter.toll1.setStatusOpened(true);
				ControlCenter.toll1.setStatusFree(true);
			} else if (mode == "Pay") {
				ControlCenter.toll1.setStatusOpened(true);
				ControlCenter.toll1.setStatusFree(false);
			}
		}
	}

	/*
	 * connect to a Toll
	 */
	private void actionConnectToll() {

		if (ControlCenter.toll1.isConnected()) {
			ControlCenter.toll1.close();
		} else {
			String ipField = JOptionPane.showInputDialog("IP Address:");
			 
			try {
				ControlCenter.toll1.setIp(ipField);
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(this, "Impossible to set IP");
				return;
			}
						
			try {
				ControlCenter.toll1.connect();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Impossible to establish connection");
				return;
			}
		}
		clearTollStatus();

	}
	
	private void actionConnectBridge() {
		if (ControlCenter.bridge1.isConnected()) {
			ControlCenter.bridge1.close();
			jlbBdgStatus.setText("");			
		} else {
			String ipField = JOptionPane.showInputDialog("IP Address:");

			try {
				ControlCenter.bridge1.setIp(ipField);
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(this, "Impossible to set IP");
				return;
			}
			
			try {
				ControlCenter.bridge1.connect();
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Impossible to establish connection");
				return;
			}
			
		}
		clearBridgeStatus();

	
	}
	
	public JLabel getStatusCars(){
		return jlbStatusCars;
	}

	public JLabel getStatusBridgeCars(){
		return jlbStatusBridgeCars;
	}	
	
	public JLabel getStatusCoins(){
		return jlbStatusCoins;
	}
	
	public JLabel getBdgStatusLabel(){
		return jlbBdgStatus;
	}
	
	
	
	

}