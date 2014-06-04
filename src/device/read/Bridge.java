package device.read;

import java.awt.Container;
import java.net.UnknownHostException;

import device.read.graphics.BridgePanel;

public class Bridge extends Device {

	private static int BRIDGE_ANGLE = 12;
	
	//Discrete Input
	private static final int STATUS_BARRIER = 0;
	private static final int STATUS_WAITING_BOAT = 1;

	//Discrete Output
	private static final int STATUS_ACTIVE = 0;	
	private static final int STATUS_BRIDGE_MOVE = 1;
	private static final int STATUS_BRIDGE_RAISE = 2;
	private static final int STATUS_BARRIER_OPENED = 3;
	
	//Holding Register	
	private static final int STATUS_NB_CARS = 0;		
	
	//Input registers
	private static final int STATUS_UNIT_ID = 0;
	private static final int STATUS_SENSOR_BUTTON = 1;
	private static final int STATUS_SENSOR_GYRO = 2;
	private static final int STATUS_SENSOR_PASSAGE = 3;
	private static final int STATUS_SENSOR_BOAT = 4;
	private static final int STATUS_SENSOR_MOVE = 5;
	private static final int STATUS_SENSOR_ANGLE = 6;

	
	public BridgePanel panel;

	public Bridge (String address, int port) throws UnknownHostException {
    	super(address, port);
	}

	public Bridge (String address) throws UnknownHostException {
    	super(address);
	}

	public Bridge () {
		super();
	}

	public void initPanel(Container container, int x, int y) {
		panel = new BridgePanel(this, container, x, y);
	}
	
	public BridgePanel getPanel() {
		return panel;
	}
	
	public int getUnitId() {
		return this.getIntRO(Bridge.STATUS_UNIT_ID);
	}

	/*
	 * Return in degres the inclinaison of the bridge
	 */
	public int getBridgeAngle() {
		int path = getStatusBridgeAngle();
		int value = (int) path * 90 / BRIDGE_ANGLE;
		return value;
	}

	/*
	 * Return true if bridge is up
	 */	
	public boolean bridgeIsUp() {
		if (getBridgeAngle() > 75) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Return true if bridge is down
	 */	
	public boolean bridgeIsDown() {
		if (getBridgeAngle() < 5) {
			return true;
		} else {
			return false;
		}
	}	
	
	//Access to coils (Read / Write)
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

	//Read Discrete Inputs
	public boolean getStatusBarrier() {
		return this.getBoolRO(Bridge.STATUS_BARRIER);
	}

	public boolean getStatusWaitingBoat() {
		return this.getBoolRO(Bridge.STATUS_WAITING_BOAT);
	}
	
	
	//Input register
	public int getStatusKeyPress() {
		return this.getIntRO(Bridge.STATUS_SENSOR_BUTTON);
	}
	public int getStatusBridgeGyro() {
		return this.getIntRO(Bridge.STATUS_SENSOR_GYRO);		
	}	
	public int getStatusCarPassage() {
		return this.getIntRO(Bridge.STATUS_SENSOR_PASSAGE);
	}
	public int getStatusBoatWaiting() {
		return this.getIntRO(Bridge.STATUS_SENSOR_BOAT);
	}		
	/*
	 *  0 : stopped
	 * +1 : go up
	 * +2 : go bottom
	 */
	public int getBridgeMoveDirection() {
		return this.getIntRO(Bridge.STATUS_SENSOR_MOVE);
	}	
	
	public int getStatusBridgeAngle() {
		return this.getIntRO(Bridge.STATUS_SENSOR_ANGLE);		
	}	
	
	//Register
	public int getStatusNbCars() {
		return this.getIntRW(Bridge.STATUS_NB_CARS);
	}

	public void setStatusNbCars(int value) {
		this.setInt(Bridge.STATUS_NB_CARS, value);
	}
	
}
