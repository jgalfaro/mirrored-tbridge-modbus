package device.read;

import java.net.UnknownHostException;

public class Bridge extends Device {

	//Discrete Input
	private static final int STATUS_BARRIER = 0;
	private static final int STATUS_WAITING_BOAT = 1;
	private static final int STATUS_BRIDGE_UP = 2;
	//Discrete Output
	private static final int STATUS_ACTIVE = 0;	
	private static final int STATUS_BRIDGE_MOVE = 1;
	private static final int STATUS_BRIDGE_RAISE = 2;
	private static final int STATUS_BARRIER_OPENED = 3;
	
	//Input Register
	private static final int STATUS_CAR_PASSAGE = 0;
	private static final int STATUS_KEY_PRESS = 1;
	private static final int STATUS_BRIDGE_ANGLE = 2;
	private static final int STATUS_BOAT_PASSAGE = 3;
		
	//Holding Register	
	private static final int STATUS_NAME_ID = 0;
	private static final int STATUS_NB_CARS = 1;		
	
	
	public Bridge (String address, int port) throws UnknownHostException {
    	super(address, port);
	}

	public Bridge (String address) throws UnknownHostException {
    	super(address);
	}

	public Bridge () {
		super();
	}

	public boolean getStatusBarrier() {
		return this.getBoolRO(Bridge.STATUS_BARRIER);
	}

	public boolean getStatusWaitingBoat() {
		return this.getBoolRO(Bridge.STATUS_WAITING_BOAT);
	}

	public boolean getStatusBridgeUp() {
		return this.getBoolRO(Bridge.STATUS_BRIDGE_UP);
	}

	
	public int getStatusCarPassage() {
		return this.getIntRO(Bridge.STATUS_CAR_PASSAGE);
	}
	
	public int getStatusKeyPress() {
		return this.getIntRO(Bridge.STATUS_KEY_PRESS);
	}
	
	public int getStatusBridgeAngle() {
		return this.getIntRO(Bridge.STATUS_BRIDGE_ANGLE);
	}	
	
	public int getStatusBoatWaiting() {
		return this.getIntRO(Bridge.STATUS_BOAT_PASSAGE);
	}	
	
	public boolean getStatusActivate() {
		return this.getBoolRW(Bridge.STATUS_ACTIVE);
	}
	
	public void setStatusActivate(boolean value) {
		this.setBool(Bridge.STATUS_ACTIVE, value);
	}
	
	public boolean getStatusBridgeMove() {
		return this.getBoolRW(Bridge.STATUS_BRIDGE_MOVE);
	}
	
	public void setStatusBridgeMove(boolean value) {
		this.setBool(Bridge.STATUS_BRIDGE_MOVE, value);
	}
	
	public boolean getStatusBridgeRaise() {
		return this.getBoolRW(Bridge.STATUS_BRIDGE_RAISE);
	}
	
	public void setStatusBridgeRaise(boolean value) {
		this.setBool(Bridge.STATUS_BRIDGE_RAISE, value);
	}
	
	public boolean getStatusBarrierOpen() {
		return this.getBoolRW(Bridge.STATUS_BARRIER_OPENED);
	}
	
	public void setStatusBarrierOpen(boolean value) {
		this.setBool(Bridge.STATUS_BARRIER_OPENED, value);
	}

	
	
	public int getStatusNbCars() {
		return this.getIntRW(Bridge.STATUS_NB_CARS);
	}

	public void setStatusNbCars(int value) {
		this.setInt(Bridge.STATUS_NB_CARS, value);
	}
	
	public int getStatusNameId() {
		return this.getIntRW(Bridge.STATUS_NAME_ID);
	}

	public void setStatusNameId(int value) {
		this.setInt(Bridge.STATUS_NAME_ID, value);
	}

}
