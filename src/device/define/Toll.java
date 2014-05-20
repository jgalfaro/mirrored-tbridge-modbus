package device.define;

import device.util.ButtonSensorRegisterIn;
import device.util.ColorSensorRegisterIn;
import device.util.TouchSensorRegisterIn;

//Modbus imports
import net.wimpi.modbus.ModbusDeviceIdentification;
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
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Toll management
 * @author Ken LE PRADO
 * @version 1.0
 */
public class Toll extends Device {
	public Toll(String modbusAddr, int modbusPort, int modbusUnitId) {
		super(modbusAddr, modbusPort, modbusUnitId);
	}

	private RegulatedMotor barrierMotor = null;
	private RegulatedMotor coinMotor = null;
	public EV3ColorSensor coinColorSensor = null;
	public EV3TouchSensor passageTouchSensor = null;

	private static int BARRIER_ANGLE = 80;
	private static int COIN_ANGLE = 120;
	
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
		

	/*
	 * Modbus initialisation
	 */
	public void initSpi() {		
		this.spi = new SimpleProcessImage();

		this.spi.addInputRegister(new ColorSensorRegisterIn(this.coinColorSensor)); //0 STATUS_COIN_COLOR
		this.spi.addInputRegister(new TouchSensorRegisterIn(this.passageTouchSensor)); //1 STATUS_CAR_PASSAGE
		this.spi.addInputRegister(new ButtonSensorRegisterIn()); //2 STATUS_KEY_PRESS

		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //0 STATUS_ACTIVE
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //1 STATUS_FREE

		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //0 STATUS_BARRIER
		
		this.spi.addRegister(new SimpleRegister(this.modbusUnitId)); //0 Name Id
		this.spi.addRegister(new SimpleRegister(0)); //1 Nb coins
		this.spi.addRegister(new SimpleRegister(0)); //2 NB cars
	}
	
	public void initMbIdentification() {
		this.mbIdent = new ModbusDeviceIdentification();
		
		this.mbIdent.setIdentification(0, "TELECOM SUD PARIS");
		this.mbIdent.setIdentification(1, "LEGO TOLL");
		this.mbIdent.setIdentification(2, "0.5");
		this.mbIdent.setIdentification(3, "http://www.telecom-sudparis.eu");
		this.mbIdent.setIdentification(4, "LEGO TOLL");
		this.mbIdent.setIdentification(5, "LEGO TOLL SMALL EDITION");
		this.mbIdent.setIdentification(6, "LEGO TOLL LEJOS");
		this.mbIdent.setIdentification(130, "NICE Comment");
	}
		
	/*
	 * EV3 Initialisation
	 */
	public void loadEV3() {
		
		barrierMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		barrierMotor.setSpeed(60);
		
		coinMotor = new EV3MediumRegulatedMotor(MotorPort.A);
		coinMotor.setSpeed(60);
		
		coinColorSensor = new EV3ColorSensor(SensorPort.S1);
		passageTouchSensor = new EV3TouchSensor(SensorPort.S2);
		
	}

	/*
	 * EV3 Close
	 */
	public void stopEV3() {
		
		barrierMotor.close();
		coinMotor.close();
		coinColorSensor.close();
		passageTouchSensor.close();
		
	}
	
	/*
	 * Thread to manage the device
	 */
	public void run() {
		int refColorId = this.spi.getInputRegister(STATUS_COIN_COLOR).getValue();
		int coinColorId = 0;
		
		while (this.spi.getInputRegister(STATUS_KEY_PRESS).getValue() != Button.ID_ESCAPE) {
			
			drawScreen();
			Delay.msDelay(200);
			
			//Is the toll activated ?
			if (this.spi.getDigitalOut(STATUS_ACTIVE).isSet() == true) {
				
				//Is the toll opened freely ?
				if (this.spi.getDigitalOut(STATUS_FREE).isSet() == true) {
					Button.LEDPattern(1); //Green
					barrierUp();
					//Increase the car counter
					if (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 1) {
						addCar();
					}
				} else {
					Button.LEDPattern(3); //Orange
					//Is a coin inserted and waiting for action ?
					coinColorId = this.spi.getInputRegister(STATUS_COIN_COLOR).getValue();
					if (refColorId != coinColorId) {
						//System.out.println("New coin");
						if (isValidCoin(coinColorId)) {
							//System.out.println("Coin accepted");
							eatCoin();
							barrierUp();
							
							//Wait a car to low the barrier
							while (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 0);
							addCar();
							//some time for the car to go away
							Delay.msDelay(500);
													
							barrierDown();
						}  else {
							//System.out.println("Coin rejected");
							rejectCoin();
						}
					} else {
						barrierDown();
					}
				}
			} else {
				Button.LEDPattern(2); //Red
				//if barrier is up, we low it
				barrierDown();
			}
		}
        Button.LEDPattern(0); //None
		barrierDown();
	}

	/*
	 * draw screen
	 */
	public void drawScreen() {
		LCD.clearDisplay();
		LCD.drawString("TOLL " + this.spi.getRegister(STATUS_NAME_ID).getValue(), 4, 0);
		LCD.drawString("Coins inside : " + this.spi.getRegister(STATUS_NB_COINS).getValue(), 1, 2);
		LCD.drawString("Cars viewed  : " + this.spi.getRegister(STATUS_NB_CARS).getValue(), 1, 3);
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
	 * Return if the coin inserted is valid to pass
	 * @param coinColorId Color index of the coin inserted
	 * @return true if coin is accepted, else false 
	 */
	public boolean isValidCoin(int coinColorId) {
		if (coinColorId == 1)
			return true;
		else 
			return false;
	}
	
/*
 * Activate motors to eat inserted coin (and increase counter associated)
 */
	public void eatCoin() {
		int nbCoins = this.spi.getRegister(STATUS_NB_COINS).getValue();
		this.spi.setRegister(STATUS_NB_COINS, new SimpleRegister(nbCoins + 1));

		coinMotor.rotate(- COIN_ANGLE);
	}
	
	/*
	 * Activate motors to reject inserted coin
	 */
	public void rejectCoin() {
		coinMotor.rotate(+ Toll.COIN_ANGLE);
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