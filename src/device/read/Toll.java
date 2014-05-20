package device.read;

import java.net.UnknownHostException;

public class Toll extends Device {

	//Discrete Input
	private static final int STATUS_BARRIER = 0; 
	//Discrete Output
	private static final int STATUS_ACTIVE = 0;	
	private static final int STATUS_FREE = 1;
	//Input Register
	private static final int STATUS_COIN_COLOR = 0;
	private static final int STATUS_CAR_PASSAGE = 1;
	private static final int STATUS_KEY_PRESS = 2;
	//Holding Register	
	private static final int STATUS_NAME_ID = 0;
	private static final int STATUS_NB_CARS = 1;
	private static final int STATUS_NB_COINS = 2;
	
	
	public Toll (String address, int port) throws UnknownHostException {
    	super(address, port);
	}

	public Toll (String address) throws UnknownHostException {
    	super(address);
	}

	public Toll () {
		super();
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
	
	public int getStatusKeyPress() {
		return this.getIntRO(Toll.STATUS_KEY_PRESS);
	}

	public int getStatusNbCars() {
		return this.getIntRW(Toll.STATUS_NB_CARS);
	}

	public void setStatusNbCars(int value) {
		this.setInt(Toll.STATUS_NB_CARS, value);
	}
	
	public int getStatusNameId() {
		return this.getIntRW(Toll.STATUS_NAME_ID);
	}

	public void setStatusNameId(int value) {
		this.setInt(Toll.STATUS_NAME_ID, value);
	}
	
	public int getStatusNbCoins() {
		return this.getIntRW(Toll.STATUS_NB_COINS);
	}
	
	public void setStatusNbCoins(int value) {
		this.setInt(Toll.STATUS_NB_COINS, value);
	}
	
}
