package device.read.graphics;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import control.DiagFrame;

import device.read.Device;

public abstract class DevicePanel {
	protected int X;
	protected int Y;
	protected Device myDevice;
	
	protected JButton btnClose;
	protected JButton btnDiag;
	protected JPanel content;
	protected Container container;

	public DevicePanel (Device dev, Container con, int x, int y) {
		super();
		container = con;
		myDevice = dev;
		X = x;
		Y = y;
		
		initPanel();
	}
	
	abstract void initPanel();

	abstract void removePanel();

	
	public void updatePanel(int x, int y) {
		X = x;
		Y = y;
		removePanel();
		initPanel();
	}
	
	public JPanel getContent() {
		return content;
	}	

	protected void displayDiagFrame() {
    	JFrame winDiag = new DiagFrame(myDevice);
		winDiag.setVisible(true);
	}
		

}
