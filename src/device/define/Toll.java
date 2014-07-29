package device.define;

import com.automatak.dnp3.DatabaseConfig;

import device.util.ButtonSensorRegisterIn;
import device.util.ColorSensorRegisterIn;
import device.util.TouchSensorRegisterIn;
import device.util.UltrasonicSensorRegisterIn;

//Modbus imports
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
//Lego EV3 imports
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Toll management
 * @author Ken LE PRADO
 * @version 1.0
 */
public class Toll extends Device {
	public Toll(String deviceAddr, Boolean modbusActive, int modbusPort, int modbusUnitId, Boolean dnp3Active, int dnp3Port, int dnp3UnitId) {
		super(deviceAddr, modbusActive, modbusPort, modbusUnitId, dnp3Active, dnp3Port, dnp3UnitId);
	}
	public EV3 ev3 = null;

	private RegulatedMotor barrierMotor = null;
	private RegulatedMotor coinMotor = null;
	private EV3ColorSensor coinColorSensor = null;
	private EV3TouchSensor passageTouchSensor = null;
	private EV3UltrasonicSensor distanceUSSensor = null;
	
	private static int BARRIER_ANGLE = 80;
	private static int COIN_ANGLE = 2200;
	
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
		

	/*
	 * Modbus initialisation
	 */
	public void initModbusSpi() {		
		this.spi = new SimpleProcessImage();

		//Discrete output
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //0 STATUS_ACTIVE
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //1 STATUS_FREE

		//Discrete input
		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //0 STATUS_BARRIER
		
		//Holding registers
		this.spi.addRegister(new SimpleRegister(0)); //0 Nb coins
		this.spi.addRegister(new SimpleRegister(0)); //1 NB cars

		//Input register
		this.spi.addInputRegister(new SimpleInputRegister(this.modbusUnitId)); //0 STATUS_UNIT_ID
		this.spi.addInputRegister(new ColorSensorRegisterIn(this.coinColorSensor)); //1 STATUS_COIN_COLOR
		this.spi.addInputRegister(new TouchSensorRegisterIn(this.passageTouchSensor)); //2 STATUS_CAR_PASSAGE
		this.spi.addInputRegister(new ButtonSensorRegisterIn()); //3 STATUS_KEY_PRESS
		this.spi.addInputRegister(new UltrasonicSensorRegisterIn(this.distanceUSSensor)); //4 STATUS_CAR_PRESENTING
	}
	
	public void initModbusIdentification() {
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
	
	@Override
	public void initDnp3Config() {
//TODO: Complete this device db
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
		
		barrierMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		barrierMotor.setSpeed(60);
		
		coinMotor = new EV3MediumRegulatedMotor(MotorPort.A);
		coinMotor.setSpeed(2000);
		
		coinColorSensor = new EV3ColorSensor(SensorPort.S1);
		passageTouchSensor = new EV3TouchSensor(SensorPort.S2);
		distanceUSSensor = new EV3UltrasonicSensor(SensorPort.S3);
	}

	/*
	 * EV3 Close
	 */
	public void stopEV3() {
		
		barrierMotor.close();
		coinMotor.close();
		coinColorSensor.close();
		passageTouchSensor.close();
		distanceUSSensor.close();
	}
	
	/*
	 * Thread to manage the device
	 */
	public void run() {
		int refColorId = this.spi.getInputRegister(STATUS_COIN_COLOR).getValue();
		int coinColorId = 0;
		int dist = 0;
		
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
					dist = this.spi.getInputRegister(STATUS_CAR_PRESENTING).getValue();
					if (refColorId != coinColorId && dist < 15) {
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
		LCD.drawString("TOLL " + this.spi.getRegister(STATUS_UNIT_ID).getValue(), 4, 0);
		LCD.drawString("Coins inside : " + this.spi.getRegister(STATUS_NB_COINS).getValue(), 1, 2);
		LCD.drawString("Cars viewed  : " + this.spi.getRegister(STATUS_NB_CARS).getValue(), 1, 3);
		LCD.drawString("Car Dist (cm): " + this.spi.getInputRegister(STATUS_CAR_PRESENTING).getValue(), 1, 4);
		
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

		coinMotor.rotate(+ Toll.COIN_ANGLE);
		coinMotor.rotate(- Toll.COIN_ANGLE);
	}
	
	/*
	 * Activate motors to reject inserted coin
	 */
	public void rejectCoin() {
		coinMotor.rotate(- Toll.COIN_ANGLE);
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
	/*
	 * Sounds a beep
	 */
	public void beep() {
		Audio audio = this.ev3.getAudio();
		audio.systemSound(0);
	}

}