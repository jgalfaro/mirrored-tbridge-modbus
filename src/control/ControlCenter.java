package control;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.wimpi.modbus.ModbusDeviceIdentification;

import device.read.Bridge;
import device.read.Toll;

public class ControlCenter {
	public static Bridge bridge1 = null;
	public static HomeFrame window;
	public static List<Toll> tolls = new ArrayList<Toll>();
	public static List<Bridge> bridges = new ArrayList<Bridge>();
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
    	
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				window = new HomeFrame();
				window.setVisible(true);
			}
		});

    	boolean statusOpened;
    	boolean statusFree;
    	boolean statusBarrier;
    	boolean statusBarrierMode;
    	int nbCars;
    	int nbCoins;
    	boolean waitingBoat;
    	int statusMoving;

    	for (;;) {
    		TimeUnit.SECONDS.sleep(1);
    		
    		//Refresh Tolls Status
    		Iterator<Toll> iter = tolls.iterator();
    	    while (iter.hasNext()) {
    	    	Toll toll = (Toll) iter.next();
    	    	if (toll.isConnected()) {
        			statusBarrier = toll.getStatusBarrier();
    	    		statusOpened = toll.getStatusOpened();
    	    		statusFree = toll.getStatusWaitingPayment();
    	    		nbCoins = toll.getStatusNbCoins();
    	    		nbCars =  toll.getStatusNbCars();
    	    	
    	    		toll.getPanel().getStatusCars().setText(Integer.toString(nbCars));
    	    		toll.getPanel().getStatusCoins().setText(Integer.toString(nbCoins));
    	    		toll.getPanel().showTollBarrier(statusBarrier);
    	    		toll.getPanel().showTollMode(statusOpened, statusFree);
    	    	}
    	    }
    		
    		//Refresh Bridge Status
    		Iterator<Bridge> iterB = bridges.iterator();
    	    while (iterB.hasNext()) {
    	    	Bridge bridge = (Bridge) iterB.next();
	    		if (bridge.isConnected()) {
	    			statusBarrier = bridge.getStatusBarrier();
	    			statusBarrierMode = bridge.getStatusBarrierOpen();
		    		nbCars = bridge.getStatusNbCars();
		    		waitingBoat = bridge.getStatusWaitingBoat();
	    			statusMoving = bridge.getBridgeMoveDirection();
		    		
		    		Integer bridgeAngle = bridge.getBridgeAngle();
		    			
		    		bridge.getPanel().showBridgeStatus(bridgeAngle, statusMoving);
		    		
		    		bridge.getPanel().showBridgeActivation(bridge.getStatusActivate());
		    		bridge.getPanel().showBridgeMode(bridge.getStatusBridgeMove(), bridge.getStatusBridgeRaise());
		    		
		    		bridge.getPanel().showBridgeBarrierMode(statusBarrierMode);
		    		bridge.getPanel().showWaitingBoat(waitingBoat);
		    		bridge.getPanel().showBridgeBarrier(statusBarrier);
		    		bridge.getPanel().getStatusBridgeCars().setText(Integer.toString(nbCars));
	    		}
    	    }
    	}
	}	
	
	public static void exit() {
		//Closing all tolls connexion
		Iterator<Toll> iter = tolls.iterator();
	    while (iter.hasNext()) {
	    	delToll(iter.next());
	    }
	    		
		//Closing all bridges connexion
		Iterator<Bridge> iterB = bridges.iterator();
	    while (iterB.hasNext()) {
	    	delBridge(iterB.next());
	    }
		
		System.exit(0);
	}
	
	public static void addToll() {
		Toll myToll = null;
		
		//We connect
		myToll = actionConnectNewToll();
		if (myToll == null) {
			return;
		}
		
		//We add to the graphics
		int y = tolls.size() * 150 + 10;
		myToll.initPanel(window.getContent(), 10, y);
		
		//We add to the collection
		tolls.add(myToll);
		
		refreshDevices();
	}
	
	public static void delToll(Toll myToll) {

		//Del graphics
		myToll.getPanel().getContent().removeAll();
		myToll.getPanel().getContent().setVisible(false);
		
		//disconnect
		myToll.close();
		
		//Remove reference
		tolls.remove(myToll);
		
		//refresh others Toll
		refreshDevices();
	}

	public static void refreshDevices() {
		int y = 10;
		Iterator<Toll> iter = tolls.iterator();
	    while (iter.hasNext()) {
	    	Toll toll = (Toll) iter.next();
	    	toll.getPanel().updatePanel(10, y);
	    	y += 150;
	    }
	    
		y = 10;
		Iterator<Bridge> iterB = bridges.iterator();
	    while (iterB.hasNext()) {
	    	Bridge bridge = (Bridge) iterB.next();
	    	bridge.getPanel().updatePanel(250, y);
	    	y += 250;
	    }
	    
		window.repaint();
	}
	
	public static void addBridge() {
		Bridge myBridge = null;
		
		//We connect
		myBridge = actionConnectNewBridge();
		if (myBridge == null) {
			return;
		}
		
		//We add to the graphics
		int y = bridges.size() * 250 + 10;
		myBridge.initPanel(window.getContent(), 250, y);
		
		//We add to the collection
		bridges.add(myBridge);
		
		refreshDevices();

	}
	
	public static void delBridge(Bridge myBridge) {

		//Del graphics
		myBridge.getPanel().getContent().removeAll();
		myBridge.getPanel().getContent().setVisible(false);
		
		//disconnect
		myBridge.close();
		
		//Remove reference
		bridges.remove(myBridge);
		
		//refresh others Toll
		refreshDevices();

	}
	
	/*
	 * connect to a Toll
	 */
	private static Toll actionConnectNewToll() {
		Toll myToll = null;
		
		String ipField = JOptionPane.showInputDialog("Toll IP Address:");
		
		
		try {
			myToll = new Toll(ipField);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(window, "Impossible to set IP");
			return null;
		}

		//			ControlCenter.toll1.setPort(1502);
					
		try {
			if (!myToll.connect()) {
				return null;
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(window, "Impossible to establish connection to " + ipField);
			return null;
		}
		
		//Test if the DEVICE_TYPE is good
		ModbusDeviceIdentification ident = myToll.getDeviceIdentification(1, 0);
	
		if (!ident.getIdentification(1).equals("LEGO TOLL") & !ident.getIdentification(1).equals("LEGO TOLL SIM")) {
			JOptionPane.showMessageDialog(window, "Unattended device");
			return null;			
		}
		
		return myToll;
	}
	
	
	private static Bridge actionConnectNewBridge() {
		Bridge myDevice = null;
		
		String ipField = JOptionPane.showInputDialog("Bridge IP Address:");

		try {
			myDevice = new Bridge(ipField);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(window, "Impossible to set IP");
			return null;
		}
		
		try {
			if (!myDevice.connect()) {
				return null;
			}
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(window, "Impossible to establish connection to " + ipField);
			return null;
		}

		//Test if the DEVICE_TYPE is good
		ModbusDeviceIdentification ident = myDevice.getDeviceIdentification(1, 0);
		
		if (!ident.getIdentification(1).equals("LEGO BRIDGE")) {
			JOptionPane.showMessageDialog(window, "Unattended device");
			return null;			
		}
		
		return myDevice;

	}
	
}
