package control;

import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import device.read.Bridge;
import device.read.Toll;

public class ControlCenter {
	public static Toll toll1 = null;
	public static Bridge bridge1 = null;
	public static HomeFrame window;
	public static Bridge[] bridges = null;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
    	toll1 = new Toll();
    	bridge1 = new Bridge();
   
    	
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

    	for (;;) {
    		TimeUnit.SECONDS.sleep(1);

    		//Refresh Toll Status
    		if (toll1.isConnected()) {
    			statusBarrier = toll1.getStatusBarrier();
	    		statusOpened = toll1.getStatusOpened();
	    		statusFree = toll1.getStatusWaitingPayment();
	    		nbCoins = toll1.getStatusNbCoins();
	    		nbCars =  toll1.getStatusNbCars();
	    	
	    		window.getStatusCars().setText(Integer.toString(nbCars));
	    		window.getStatusCoins().setText(Integer.toString(nbCoins));
	    		window.showTollBarrier(statusBarrier);
	    		window.showTollMode(statusOpened, statusFree);
    		}
    		
    		//Refresh Bridge Status
    		if (bridge1.isConnected()) {
    			statusBarrier = bridge1.getStatusBarrier();
    			statusBarrierMode = bridge1.getStatusBarrierOpen();
	    		nbCars = bridge1.getStatusNbCars();
	    		waitingBoat = bridge1.getStatusWaitingBoat();
	    		
	    		Integer bridgeAngle = bridge1.getStatusBridgeAngle();
	    		boolean bridgeUp = bridge1.getStatusBridgeUp();
	    		

	    		//TODO Temporary way to display
	    		window.showBridgeStatus(bridgeUp, bridgeAngle);
	    		
	    		
	    		window.showBridgeActivation(bridge1.getStatusActivate());
	    		window.showBridgeMode(bridge1.getStatusBridgeMove(), bridge1.getStatusBridgeRaise());
	    		
	    		window.showBridgeBarrierMode(statusBarrierMode);
	    		window.showWaitingBoat(waitingBoat);
	    		window.showBridgeBarrier(statusBarrier);
	    		window.getStatusBridgeCars().setText(Integer.toString(nbCars));
    		}
    		
    	}
	}	
	
	public static void exit() {
		if (toll1.isConnected()) {
			toll1.close();
		}

		if (bridge1.isConnected()) {
			bridge1.close();
		}
		
		System.exit(0);
	}
	
}
