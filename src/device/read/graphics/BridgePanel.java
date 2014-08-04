package device.read.graphics;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import control.ControlCenter;

import device.read.Bridge;
import device.read.Device;

public class BridgePanel extends DevicePanel implements ActionListener {
	public BridgePanel(Device dev, Container con, int x, int y) {
		super(dev, con, x, y);
	}

	private JLabel picWaitingBoat;
	private JLabel jlbBdgBoatWaiting;
	private JLabel jlbStatusBridgeCars;
	private JLabel jlbBdgName;
	private JButton btnBridgeBarrierOff;
	private JButton btnBridgeBarrierOn;
	private JButton btnBridgeActivateOn;
	private JButton btnBridgeActivateOff;
	private JButton btnBridgeMoveStop;
	private JButton btnBridgeMoveUp;
	private JButton btnBridgeMoveDown;
	private BridgeCanvas bdgCanvas;

	private JLabel picLabelBarrierBridgeOpen;
	private JLabel picLabelBarrierBridgeClose;

	
	public void initPanel() {
		content = new JPanel();
		content.setLayout(null);
		content.setBackground(Color.white);
		content.setBorder(BorderFactory.createLineBorder(Color.black));
		

		//Bridge Name
		jlbBdgName = new JLabel(myDevice.getLabel());
		jlbBdgName.setBounds(5, 5, 130, 15);
		content.add(jlbBdgName);
				
		//Bridge status
		bdgCanvas = new BridgeCanvas();
		bdgCanvas.setBounds(100, 100, 130, 130);
		content.add(bdgCanvas);
		
		//Button Activation
		btnBridgeActivateOn = new JButton("On");
		btnBridgeActivateOn.addActionListener(this);
		btnBridgeActivateOn.setBounds(110, 10, 60, 20);
		content.add(btnBridgeActivateOn);
		btnBridgeActivateOff = new JButton("Off");
		btnBridgeActivateOff.addActionListener(this);
		btnBridgeActivateOff.setBounds(110, 30, 60, 20);
		content.add(btnBridgeActivateOff);
				
		//Button Bridge Moving			
		btnBridgeMoveUp = new JButton("Up");
		btnBridgeMoveUp.addActionListener(this);
		btnBridgeMoveUp.setBounds(245, 150, 75, 20);
		content.add(btnBridgeMoveUp);
		btnBridgeMoveStop = new JButton("Stop");
		btnBridgeMoveStop.addActionListener(this);
		btnBridgeMoveStop.setBounds(245, 170, 75, 20);
		content.add(btnBridgeMoveStop);
		btnBridgeMoveDown = new JButton("Down");
		btnBridgeMoveDown.addActionListener(this);
		btnBridgeMoveDown.setBounds(245, 190, 75, 20);
		content.add(btnBridgeMoveDown);
				
		//Waiting boat
	    picWaitingBoat = new JLabel( new ImageIcon( "/home/user/projects/tollbridge/src/control/boat.png"));
	    picWaitingBoat.setBounds(250, 10, 50, 50);
	    content.add(picWaitingBoat);
		jlbBdgBoatWaiting = new JLabel();
		jlbBdgBoatWaiting.setBounds(230, 65, 150, 15);
		content.add(jlbBdgBoatWaiting);

		
		//Barrier up/down
	    picLabelBarrierBridgeOpen = new JLabel( new ImageIcon( "/home/user/projects/tollbridge/src/control/open_barrier.png"));
		picLabelBarrierBridgeOpen.setBounds(10, 100, 60, 60);
		content.add(picLabelBarrierBridgeOpen);
		picLabelBarrierBridgeClose = new JLabel( new ImageIcon( "/home/user/projects/tollbridge/src/control/close_barrier.png"));
		picLabelBarrierBridgeClose.setBounds(10, 100, 60, 60);
		content.add(picLabelBarrierBridgeClose);		
		
		//Buttons barrier
		btnBridgeBarrierOn = new JButton("Up");
		btnBridgeBarrierOn.addActionListener(this);
		btnBridgeBarrierOn.setBounds(10, 170, 75, 20);
		content.add(btnBridgeBarrierOn);
		btnBridgeBarrierOff = new JButton("Down");
		btnBridgeBarrierOff.addActionListener(this);
		btnBridgeBarrierOff.setBounds(10, 190, 75, 20);
		content.add(btnBridgeBarrierOff);
		
		//cars
		JLabel picLabelBridgeCars = new JLabel( new ImageIcon( "/home/user/projects/tollbridge/src/control/cars.png"));
		picLabelBridgeCars.setBounds(10, 220, 40, 25);
		content.add(picLabelBridgeCars);

		jlbStatusBridgeCars = new JLabel("xx");
		jlbStatusBridgeCars.setBounds(60, 220, 30, 10);
		content.add(jlbStatusBridgeCars);				

		
		//Btn Disconnect
		btnClose = new JButton("Close");
		btnClose.setBounds(10, 250, 80, 20);
		btnClose.addActionListener(this);
		content.add(btnClose);
		
		//Btn Diag
		btnDiag = new JButton("Ident.");
		btnDiag.setBounds(95, 250, 80, 20);
		btnDiag.addActionListener(this);
		content.add(btnDiag);

		
		//Adds the content
		content.setBounds(X, Y, 330, 280);
		content.setVisible(true);
		container.add(content);
				
	}
		
	public void removePanel() {
		content.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
 		
		if (source == btnBridgeBarrierOff) {
			((Bridge) myDevice).setStatusBarrierOpen(false);
		} else if (source == btnBridgeBarrierOn) {
			((Bridge) myDevice).setStatusBarrierOpen(true);
		} else if (source == btnBridgeActivateOff) {
			((Bridge) myDevice).setStatusActivate(false);
		} else if (source == btnBridgeActivateOn) {
			((Bridge) myDevice).setStatusActivate(true);
		} else if (source == btnBridgeMoveStop) {
			((Bridge) myDevice).setStatusBridgeRaise(true);
			((Bridge) myDevice).setStatusBridgeMove(false);
		} else if (source == btnBridgeMoveUp) {
			((Bridge) myDevice).setStatusBarrierOpen(false);
			((Bridge) myDevice).setStatusBridgeRaise(true);
			((Bridge) myDevice).setStatusBridgeMove(true);
		} else if (source == btnBridgeMoveDown) {
			((Bridge) myDevice).setStatusBarrierOpen(false);
			((Bridge) myDevice).setStatusBridgeRaise(false);
			((Bridge) myDevice).setStatusBridgeMove(true);
		} else if(source == btnClose){
			ControlCenter.delBridge((Bridge) myDevice);			
		} else if (source == btnDiag) {
			displayDiagFrame();
		}
	}
	
	public JLabel getStatusBridgeCars(){
		return jlbStatusBridgeCars;
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
	
		bdgCanvas.setStatus(0, 0, 0);
	}
	
	public void showBridgeBarrierMode(boolean statusBarrier) {
		
		if (statusBarrier == true) {
			btnBridgeBarrierOn.setBackground(Color.green);
			btnBridgeBarrierOff.setBackground(Color.gray);
		} else {
			btnBridgeBarrierOn.setBackground(Color.gray);
			btnBridgeBarrierOff.setBackground(Color.green);
		}
	}
	

	public void showBridgeActivation(boolean statusBridge) {
		
		if (statusBridge == true) {
			btnBridgeActivateOn.setBackground(Color.green);
			btnBridgeActivateOff.setBackground(Color.gray);
		} else {
			btnBridgeActivateOn.setBackground(Color.gray);
			btnBridgeActivateOff.setBackground(Color.green);
		}
	}

	
	public void showBridgeMode(boolean statusMove, boolean statusRaise) {
		if (statusMove == false) {
			btnBridgeMoveStop.setBackground(Color.green);
			btnBridgeMoveDown.setBackground(Color.gray);
			btnBridgeMoveUp.setBackground(Color.gray);
		} else {
			if (statusRaise == true) {
				btnBridgeMoveStop.setBackground(Color.gray);
				btnBridgeMoveUp.setBackground(Color.green);
				btnBridgeMoveDown.setBackground(Color.gray);
				
			} else {
				btnBridgeMoveStop.setBackground(Color.gray);
				btnBridgeMoveUp.setBackground(Color.gray);
				btnBridgeMoveDown.setBackground(Color.green);
				
			}
			
		}
	}
	
	public void showBridgeStatus(int bridgeAngle, int moveDirection, int gyroAngle) {
		bdgCanvas.setStatus(bridgeAngle, moveDirection, gyroAngle);

	}
	
	public void showWaitingBoat(boolean boatWaiting) {
		picWaitingBoat.setVisible(true);
/*		
		if (boatWaiting == true) {
			picWaitingBoat.setVisible(true);
			jlbBdgBoatWaiting.setVisible(true);
		} else {
			picWaitingBoat.setVisible(false);
			jlbBdgBoatWaiting.setVisible(false);
		}
	*/	
	}	

	public void setNbBoatWaiting(int nbBoat) {
		if (nbBoat == 0) {
			this.jlbBdgBoatWaiting.setText("No boat" );			
		} else {
			this.jlbBdgBoatWaiting.setText(nbBoat + " is waiting" );
		}
	}
	
}
