package device.read;

import java.awt.Container;
import java.net.UnknownHostException;

import device.read.graphics.TollPanel;

public class Toll extends Device {
	//Discrete Input
	private static final int STATUS_BARRIER = 0; 
	
	//Discrete Output
	private static final int STATUS_ACTIVE = 0;	
	private static final int STATUS_FREE = 1;
	
	//Holding Register	
	private static final int STATUS_NB_CARS = 0;
	private static final int STATUS_NB_COINS = 1;

	//Input Register
	private static final int STATUS_UNIT_ID = 0;
	private static final int STATUS_COIN_COLOR = 1;
	private static final int STATUS_CAR_PASSAGE = 2;
	private static final int STATUS_KEY_PRESS = 3;
	private static final int STATUS_CAR_PRESENTING = 4;

	
	public TollPanel panel;
	
	public Toll (String address, int port) throws UnknownHostException {
    	super(address, port);
	}

	public Toll (String address) throws UnknownHostException {
    	super(address);
	}

	public Toll () {
		super();
	}

	public void initPanel(Container container, int x, int y) {
		panel = new TollPanel(this, container, x, y);
	}
	
	public TollPanel getPanel() {
		return panel;
	}

	public int getUnitId() {
		return this.getIntRO(Toll.STATUS_UNIT_ID);
	}

	public boolean getStatusBarrier() {
		return this.getBoolRO(Toll.STATUS_BARRIER);
	}

	public boolean getStatusOpened() {
		return this.getBoolRW(Toll.STATUS_ACTIVE);
	}
	
	public void setStatusOpened(boolean value) {
		this.setBool(Toll.STATUS_ACTIVE, value);
	}
	
	public boolean getStatusFree() {
		return this.getBoolRW(Toll.STATUS_FREE);
	}
	
	public void setStatusFree(boolean value) {
		this.setBool(Toll.STATUS_FREE, value);
	}
	
	public boolean getStatusWaitingPayment() {
		return this.getBoolRW(Toll.STATUS_FREE);
	}
	
	public int getStatusCoinColor() {
		return this.getIntRO(Toll.STATUS_COIN_COLOR);
	}
	
	public int getStatusCarPassage() {
		return this.getIntRO(Toll.STATUS_CAR_PASSAGE);
	}
	
	public int getStatusCarPresenting() {
		return this.getIntRO(Toll.STATUS_CAR_PRESENTING);
	}
	
	public int getStatusKeyPress() {
		return this.getIntRO(Toll.STATUS_KEY_PRESS);
	}

	public int getStatusNbCars() {
		return this.getIntRW(Toll.STATUS_NB_CARS);
	}

	public void setStatusNbCars(int value) {
		this.setInt(Toll.STATUS_NB_CARS, value);
	}
	
	public int getStatusNbCoins() {
		return this.getIntRW(Toll.STATUS_NB_COINS);
	}
	
	public void setStatusNbCoins(int value) {
		this.setInt(Toll.STATUS_NB_COINS, value);
	}
	
}
