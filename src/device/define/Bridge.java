package device.define;

import device.util.ButtonSensorRegisterIn;
import device.util.GyroSensorRegisterIn;
import device.util.TouchSensorRegisterIn;

import net.wimpi.modbus.ModbusDeviceIdentification;
//Modbus imports
import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

//Lego EV3 imports
import lejos.hardware.Button;
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
	private DifferentialPilot bridgeMotor = null;
	private RegulatedMotor bridgeMotor1 = null;
	private RegulatedMotor bridgeMotor2 = null;
	private RegulatedMotor barrierMotor = null;
	public EV3TouchSensor passageTouchSensor = null;
	public EV3TouchSensor boatTouchSensor = null;
	public EV3GyroSensor bridgeAngleSensor = null;

	private static int BARRIER_ANGLE = 80;
	private static int BRIDGE_ANGLE = 15;
	
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

	/*
	 * Modbus initialisation
	 */
	public void initSpi() {	
		this.spi = new SimpleProcessImage();

		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //0 STATUS_BARRIER
		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //1 STATUS_WAITING_BOAT
		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //2 STATUS_BRIDGE_UP

		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //0 STATUS_ACTIVE
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //1 STATUS_BRIDGE_MOVE
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //2 STATUS_BRIDGE_RAISE
		this.spi.addDigitalOut(new SimpleDigitalOut(true)); //3 STATUS_BARRIER_OPENED
		
		this.spi.addInputRegister(new TouchSensorRegisterIn(this.passageTouchSensor)); //0 STATUS_CAR_PASSAGE
		this.spi.addInputRegister(new ButtonSensorRegisterIn()); //1 STATUS_KEY_PRESS
		this.spi.addInputRegister(new GyroSensorRegisterIn(this.bridgeAngleSensor)); //2 STATUS_BRIDGE_ANGLE
		this.spi.addInputRegister(new TouchSensorRegisterIn(this.boatTouchSensor)); //3 STATUS_BOAT_PASSAGE

		this.spi.addRegister(new SimpleRegister(this.modbusUnitId)); //0 Name Id
		this.spi.addRegister(new SimpleRegister(0)); //1 NB cars

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
	}
		
	/*
	 * EV3 Initialisation
	 */
	public void loadEV3() {
	
		bridgeMotor1 = new EV3LargeRegulatedMotor(MotorPort.A);
		bridgeMotor2 = new EV3LargeRegulatedMotor(MotorPort.B);
		
		bridgeMotor = new DifferentialPilot(5.6, 9.25, bridgeMotor1, bridgeMotor2); 
		bridgeMotor.setRotateSpeed(10);  
		bridgeMotor.setTravelSpeed(1);  
				
		barrierMotor = new EV3MediumRegulatedMotor(MotorPort.C);
		barrierMotor.setSpeed(60);
		
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
	
		//Reset the angle
		bridgeAngleSensor.reset();
		
		while (this.spi.getInputRegister(STATUS_KEY_PRESS).getValue() != Button.ID_ESCAPE) {
			
			drawScreen();
			Delay.msDelay(200);

			//Is the bridge activated ?
			if (this.spi.getDigitalOut(STATUS_ACTIVE).isSet() == true) {
				//Need to open the barrier ?
				if (this.spi.getDigitalOut(STATUS_BARRIER_OPENED).isSet() == true) {
					Button.LEDPattern(1); //Green
					barrierUp();
				} else {
					Button.LEDPattern(0); //Red
					barrierDown();
				}
	
				//Need to move the bridge ?
				if (this.spi.getDigitalOut(STATUS_BRIDGE_MOVE).isSet() == true) {
					//barrierDown();
					if (this.spi.getDigitalOut(STATUS_BRIDGE_RAISE).isSet() == true) {
						bridgeUp();
					} else {
						bridgeDown();
					//	barrierUp();
					}
				} else {
					//Good question :)			
				}

				//Account the car passage
				if (this.spi.getDigitalIn(STATUS_BARRIER).isSet() == true) {
					if (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 1) {
						addCar();
					}				
				}
						
				//Detect waiting boat
				if (this.spi.getInputRegister(STATUS_BOAT_PASSAGE).getValue() == 1) {
					if (this.spi.getDigitalIn(STATUS_BRIDGE_UP).isSet() == false) {
						this.spi.setDigitalIn(STATUS_WAITING_BOAT, new SimpleDigitalIn(true));	
					}
				}
			} else {
				//Good question :)
			}
			
		}
		barrierDown();
        Button.LEDPattern(0); //None
	}

	/*
	 * draw screen
	 */
	public void drawScreen() {
		LCD.clearDisplay();
		LCD.drawString("BRIDGE " + this.spi.getRegister(STATUS_NAME_ID).getValue(), 4, 0);
		LCD.drawString("Bridge Angle : " + this.spi.getInputRegister(STATUS_BRIDGE_ANGLE).getValue(), 0, 2);
		LCD.drawString("Cars viewed  : " + this.spi.getRegister(STATUS_NB_CARS).getValue(), 0, 3);
		LCD.drawString("Boat waiting : " + this.spi.getDigitalIn(STATUS_WAITING_BOAT).isSet(), 0, 4);
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
		if (this.spi.getDigitalIn(STATUS_BRIDGE_UP).isSet() == false) {
			this.spi.setDigitalIn(STATUS_BRIDGE_UP, new SimpleDigitalIn(true));
			bridgeMotor.travel(BRIDGE_ANGLE);

			//Clear the boat who is waiting
			this.spi.setDigitalIn(STATUS_WAITING_BOAT, new SimpleDigitalIn(false));

			//End of move
			this.spi.setDigitalIn(STATUS_BRIDGE_MOVE, new SimpleDigitalIn(false));
		}

	}

	/*
	 * Low the bridge
	 */
	public void bridgeDown() {
		if (this.spi.getDigitalIn(STATUS_BRIDGE_UP).isSet() == true) {
			this.spi.setDigitalIn(STATUS_BRIDGE_UP, new SimpleDigitalIn(false));
			bridgeMotor.travel(- BRIDGE_ANGLE);
		
			//End of move
			this.spi.setDigitalIn(STATUS_BRIDGE_MOVE, new SimpleDigitalIn(false));
		}
	}
	
	/*
	 * Increase counter if a car has gone 
	 */
	public void addCar() {
		int nbCars = this.spi.getRegister(STATUS_NB_CARS).getValue();
		this.spi.setRegister(STATUS_NB_CARS, new SimpleRegister(nbCars + 1));
		// car_passage == 0 : wait for car to free the sensor
		while (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 1);
	}

}