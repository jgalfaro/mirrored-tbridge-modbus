package device.define;


import device.util.ButtonSensorRegisterIn;
import device.util.GyroSensorRegisterIn;
import device.util.MotorPoseSensorRegisterIn;
import device.util.TouchSensorRegisterIn;
import net.wimpi.modbus.ModbusDeviceIdentification;

import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

/**
 * Bridge
 * @author Ken LE PRADO
 * @version 1.0
 */
public class Bridge extends Device {
	public Bridge(String modbusAddr, int modbusPort, int modbusUnitId) {
		super(modbusAddr, modbusPort, modbusUnitId);
	}
	public EV3 ev3 = null;
	private DifferentialPilot bridgeMotor = null;
	private RegulatedMotor bridgeMotor1 = null;
	private RegulatedMotor bridgeMotor2 = null;
	private RegulatedMotor barrierMotor = null;
	public EV3TouchSensor passageTouchSensor = null;
	public EV3TouchSensor boatTouchSensor = null;
	public EV3GyroSensor bridgeAngleSensor = null;
	
	private static int BARRIER_ANGLE = 80;
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
	
	/*
	 * Modbus initialisation
	 */
	public void initSpi() {	
		this.spi = new SimpleProcessImage();

		//Discrete Input
		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //0 STATUS_BARRIER
		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //1 STATUS_WAITING_BOAT

		//Discrete Output
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //0 STATUS_ACTIVE
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //1 STATUS_BRIDGE_MOVE
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //2 STATUS_BRIDGE_RAISE
		this.spi.addDigitalOut(new SimpleDigitalOut(false));  //3 STATUS_BARRIER_OPENED
		
		//Digital Input
		this.spi.addRegister(new SimpleRegister(0)); //0 NB cars

		//Digital Output
		this.spi.addInputRegister(new SimpleRegister(this.modbusUnitId)); //0 STATUS_UNIT_ID
		this.spi.addInputRegister(new ButtonSensorRegisterIn());				// 0 STATUS_SENSOR_BUTTON
		this.spi.addInputRegister(new GyroSensorRegisterIn(bridgeAngleSensor)); // 1 STATUS_SENSOR_GYRO
		this.spi.addInputRegister(new TouchSensorRegisterIn(passageTouchSensor)); // 2 STATUS_SENSOR_PASSAGE
		this.spi.addInputRegister(new TouchSensorRegisterIn(boatTouchSensor));    // 3 STATUS_SENSOR_BOAT
		this.spi.addInputRegister(new SimpleInputRegister(0));    					// 4 STATUS_SENSOR_MOVE
		this.spi.addInputRegister(new MotorPoseSensorRegisterIn(bridgeMotor));    // 5 STATUS_SENSOR_ANGLE
		
	}
	
	public void initMbIdentification() {
		this.mbIdent = new ModbusDeviceIdentification();
		
		this.mbIdent.setIdentification(0, "TELECOM SUD PARIS");
		this.mbIdent.setIdentification(1, "LEGO BRIDGE");
		this.mbIdent.setIdentification(2, "0.5");
		this.mbIdent.setIdentification(3, "http://www.telecom-sudparis.eu");
		this.mbIdent.setIdentification(4, "LEGO BRIDGE");
		this.mbIdent.setIdentification(5, "LEGO BRIDGE SMALL EDITION");
		this.mbIdent.setIdentification(6, "LEGO BRIDGE LEJOS");
		this.mbIdent.setIdentification(130, "NICE Comment for a bridge");
		this.mbIdent.setIdentification(131, "New comment for a bigger bridge");
	}
		
	/*
	 * EV3 Initialisation
	 */
	public void initEV3() {		
		this.ev3 = (EV3) BrickFinder.getDefault();
		this.loadEV3();
	}

	/*
	 * EV3 Initialisation
	 */
	public void loadEV3() {
		//Bridge motors and navigation
		bridgeMotor1 = new EV3LargeRegulatedMotor(MotorPort.A);
		bridgeMotor2 = new EV3LargeRegulatedMotor(MotorPort.B);

		bridgeMotor = new DifferentialPilot(5.6, 9.25, bridgeMotor1, bridgeMotor2); 
		bridgeMotor.setTravelSpeed(1);
		
	
        //barrier
		barrierMotor = new EV3MediumRegulatedMotor(MotorPort.C);
		barrierMotor.setSpeed(60);
		
		//sensors
		passageTouchSensor = new EV3TouchSensor(SensorPort.S1);

		boatTouchSensor = new EV3TouchSensor(SensorPort.S2);
		
		bridgeAngleSensor = new EV3GyroSensor(SensorPort.S4);
	}

	
	/*
	 * EV3 Close
	 */
	public void stopEV3() {
		
		bridgeMotor1.close();
		bridgeMotor2.close();
		
		barrierMotor.close();
		
		boatTouchSensor.close();
		passageTouchSensor.close();
		bridgeAngleSensor.close();
	}

	
	/*
	 * Thread to manage the device
	 */
	public void run() {
		
		while (((int)this.spi.getInputRegister(STATUS_SENSOR_BUTTON).getValue() & Button.ID_ESCAPE) == 0) {		
			drawScreen();
			Delay.msDelay(1000);
			
			/*
			System.err.println("Digital In : " + this.spi.getDigitalIn(0).isSet() + "/"  + this.spi.getDigitalIn(1).isSet());
			System.err.println("Digital Out : " + this.spi.getDigitalOut(0).isSet() + "/"  + this.spi.getDigitalOut(1).isSet() + "/"  + this.spi.getDigitalOut(2).isSet() + "/"  + this.spi.getDigitalOut(3).isSet());
			System.err.println("Input Registers : " + this.spi.getInputRegister(0).getValue() + "/" + this.spi.getInputRegister(1).getValue() + "/" + this.spi.getInputRegister(2).getValue() + "/" + this.spi.getInputRegister(3).getValue() + "/" + this.spi.getInputRegister(4).getValue() + "/" + this.spi.getInputRegister(5).getValue());
			System.err.println("Output Registers : " + this.spi.getRegister(0).getValue());
			*/

			//Update moving status
			if (bridgeMotor.isMoving() == false) {
				this.spi.setInputRegister(STATUS_SENSOR_MOVE, new SimpleInputRegister(0));
			}
			
			//Local control (buttons)
			//Raise
			if (((int)this.spi.getInputRegister(STATUS_SENSOR_BUTTON).getValue() & Button.ID_UP) != 0) {
				this.spi.setDigitalOut(STATUS_BRIDGE_RAISE, new SimpleDigitalOut(true)); 
			}
			//Low
			if (((int)this.spi.getInputRegister(STATUS_SENSOR_BUTTON).getValue() & Button.ID_DOWN) != 0) {
				this.spi.setDigitalOut(STATUS_BRIDGE_RAISE, new SimpleDigitalOut(false)); 
			}
			//Move
			if (((int)this.spi.getInputRegister(STATUS_SENSOR_BUTTON).getValue() & Button.ID_ENTER) != 0) {
				if (this.spi.getDigitalOut(STATUS_BRIDGE_MOVE).isSet() == true) {
					this.spi.setDigitalOut(STATUS_BRIDGE_MOVE, new SimpleDigitalOut(false)); 
				} else {
					this.spi.setDigitalOut(STATUS_BRIDGE_MOVE, new SimpleDigitalOut(true)); 
				}
			}
			//Active
			if (((int)this.spi.getInputRegister(STATUS_SENSOR_BUTTON).getValue() & Button.ID_LEFT) != 0) {
				if (this.spi.getDigitalOut(STATUS_ACTIVE).isSet() == true) {
					this.spi.setDigitalOut(STATUS_ACTIVE, new SimpleDigitalOut(false)); 
				} else {
					this.spi.setDigitalOut(STATUS_ACTIVE, new SimpleDigitalOut(true)); 
				}
			}
			
			//Barrier
			if (((int)this.spi.getInputRegister(STATUS_SENSOR_BUTTON).getValue() & Button.ID_RIGHT) != 0) {
				if (this.spi.getDigitalOut(STATUS_BARRIER_OPENED).isSet() == true) {
					this.spi.setDigitalOut(STATUS_BARRIER_OPENED, new SimpleDigitalOut(false)); 
				} else {
					this.spi.setDigitalOut(STATUS_BARRIER_OPENED, new SimpleDigitalOut(true)); 
				}
			}
		
			//Is the bridge activated ?
			if (this.spi.getDigitalOut(STATUS_ACTIVE).isSet() == true) {
				//Need to open the barrier ?
				if (this.spi.getDigitalOut(STATUS_BARRIER_OPENED).isSet() == true) {
					Button.LEDPattern(1); //Green
					barrierUp();
				} else {
					Button.LEDPattern(2); //Red
					barrierDown();
				}
	
				//Need to move the bridge ?
				if (this.spi.getDigitalOut(STATUS_BRIDGE_MOVE).isSet() == true) {
					
					if (this.spi.getDigitalOut(STATUS_BRIDGE_RAISE).isSet() == true) {
						bridgeUp();
					} else {
						bridgeDown();
					}
				} else {
					stopBridge();			
				}

				//Account the car passage
				if (this.spi.getDigitalOut(STATUS_BARRIER).isSet() == true) {
					if (this.spi.getInputRegister(STATUS_SENSOR_PASSAGE).getValue() == 1) {
						addCar();
					}				
				}
						
				//Detect waiting boat if bridge is up
				if (this.spi.getInputRegister(STATUS_SENSOR_BOAT).getValue() == 1) {
					if (bridgeIsDown()) {
						this.spi.setDigitalIn(STATUS_WAITING_BOAT, new SimpleDigitalIn(true));	
					}
				}
				//Reset boat status if bridge is up
				if (bridgeIsUp()) {
					this.spi.setDigitalIn(STATUS_WAITING_BOAT, new SimpleDigitalIn(false));	
				}
				
			//Bridge unactivated
//			} else {
//				bridgeDown();
//				barrierDown();
			}
			
		}
		
		barrierDown();
		bridgeDown();
        Button.LEDPattern(0); //None
	}

	/*
	 * draw screen
	 */
	public void drawScreen() {
		LCD.clearDisplay();
		LCD.drawString("BRIDGE " + this.spi.getInputRegister(STATUS_UNIT_ID).getValue(), 4, 0);
		LCD.drawString("Bridge Path : " + this.spi.getInputRegister(STATUS_SENSOR_ANGLE).getValue(), 0, 2);
		LCD.drawString("Bridge Gyro : " + this.spi.getInputRegister(STATUS_SENSOR_GYRO).getValue(), 0, 3);
		LCD.drawString("Cars viewed  : " + this.spi.getRegister(STATUS_NB_CARS).getValue(), 0, 4);
		LCD.drawString("Boat waiting : " + this.spi.getDigitalIn(STATUS_WAITING_BOAT).isSet(), 0, 5);
		LCD.drawString("ESC Exit", 7, 6);
	}
	
	/*
	 * Raise the barrier
	 */
	public void barrierUp() {
		if (this.spi.getDigitalIn(STATUS_BARRIER).isSet() == false) {
			barrierMotor.rotate(+ BARRIER_ANGLE);
			this.spi.setDigitalIn(STATUS_BARRIER, new SimpleDigitalIn(true));
		}
	}

	/*
	 * Low the barrier
	 */
	public void barrierDown() {
		if (this.spi.getDigitalIn(STATUS_BARRIER).isSet() == true) {
			barrierMotor.rotate(- BARRIER_ANGLE);		
			this.spi.setDigitalIn(STATUS_BARRIER, new SimpleDigitalIn(false));
		}
	}
		
	/*
	 * Raise the bridge
	 */
	public void bridgeUp() {

		//If already at top => ignoring
		if (bridgeIsUp() == true) {
			return;
		}

		//If moving to low => stop
		if (this.spi.getInputRegister(STATUS_SENSOR_MOVE).getValue() == 2) {
			stopBridge();
		}
		
		//If moving to top => ignoring
		if (this.spi.getInputRegister(STATUS_SENSOR_MOVE).getValue() == 1) {
			return;
		}
			
		//If not moving => let's go !
	//	barrierDown();
		this.spi.setInputRegister(STATUS_SENSOR_MOVE, new SimpleInputRegister(1));

		bridgeMotor.travel(BRIDGE_ANGLE - this.spi.getInputRegister(STATUS_SENSOR_ANGLE).getValue(), true);

	}
	
	/*
	 * Return in degres the inclinaison of the bridge
	 */
	public int getBridgeAngle() {
		int value = (int) this.spi.getInputRegister(STATUS_SENSOR_ANGLE).getValue()* 90 / BRIDGE_ANGLE;
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

	/*
	 * Low the bridge
	 */
	public void bridgeDown() {

		//If already at top => ignoring
		if (bridgeIsDown() == true) {
			return;
		}
		
		//If moving to top => stop
		if (this.spi.getInputRegister(STATUS_SENSOR_MOVE).getValue() == 1) {
			stopBridge();
		}
			
		//If moving to low => ignoring
		if (this.spi.getInputRegister(STATUS_SENSOR_MOVE).getValue() == 2) {
			return;
		}
		
		//If not moving => let's go !
	//	barrierDown();
		this.spi.setInputRegister(STATUS_SENSOR_MOVE, new SimpleInputRegister(2));

		bridgeMotor.travel(0 - this.spi.getInputRegister(STATUS_SENSOR_ANGLE).getValue(), true);

	}
	
	/*
	 * Stop the bridge
	 */
	public void stopBridge() {
		if (bridgeMotor.isMoving()) {
			
			this.spi.setInputRegister(STATUS_SENSOR_MOVE, new SimpleInputRegister(0));
			bridgeMotor.stop();
		}
	}	
	
	/*
	 * Increase counter if a car has gone 
	 */
	public void addCar() {
		int nbCars = this.spi.getRegister(STATUS_NB_CARS).getValue();
		this.spi.setRegister(STATUS_NB_CARS, new SimpleRegister(nbCars + 1));
		// car_passage == 0 : wait for car to free the sensor
		while (this.spi.getInputRegister(STATUS_SENSOR_PASSAGE).getValue() == 1);
	}

	/*
	 * Sounds a beep
	 */
	public void beep() {
		Audio audio = this.ev3.getAudio();
		audio.systemSound(0);
	}

}