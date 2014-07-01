package device.define;

//Modbus imports
import device.util.DetectedObjectListener;
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.procimg.SimpleProcessImage;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
//Lego EV3 imports
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RangeFinderAdaptor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.objectdetection.FeatureListener;
import lejos.robotics.objectdetection.RangeFeatureDetector;
import lejos.utility.Delay;

/**
 * Toll management
 * @author Ken LE PRADO
 * @version 1.0
 * http://www.rapidpm.org/2014/01/lego-mindstorms-ev3-components-infrared.html
 */
public class Rover extends Device {
	public Rover(String modbusAddr, int modbusPort, int modbusUnitId) {
		super(modbusAddr, modbusPort, modbusUnitId);
	}
	
	public EV3 ev3 = null;

	private RegulatedMotor wheelMotor1 = null;
	private RegulatedMotor wheelMotor2 = null;
	private DifferentialPilot wheelPilot = null;
	
	private EV3IRSensor IRSensor = null;

    private final static double WHEEL_DIAMETER = 30.0f;
    private final static double DISTANCE_BETWEEN_WHEELS = 50.0;
    protected final static double PILOT_SPEED = 400.0;
    protected final static double PILOT_SPEED_ROT = 50000.0;
    protected final static int PILOT_ACCELERATION = 20;
    protected final static float MAX_DISTANCE = 100.0f;
    protected final static int INTERVAL = 500;

    
	/*
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
		*/

	/*
	 * Modbus initialisation
	 */
	public void initSpi() {		
		this.spi = new SimpleProcessImage();

		/*
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
*/
	}
	
	public void initMbIdentification() {
		this.mbIdent = new ModbusDeviceIdentification();
		
		this.mbIdent.setIdentification(0, "TELECOM SUD PARIS");
		this.mbIdent.setIdentification(1, "LEGO ROVER");
		this.mbIdent.setIdentification(2, "0.1");
		this.mbIdent.setIdentification(3, "http://www.telecom-sudparis.eu");
		this.mbIdent.setIdentification(4, "LEGO ROVER");
		this.mbIdent.setIdentification(5, "LEGO ROVER SMALL EDITION");
		this.mbIdent.setIdentification(6, "LEGO ROVER LEJOS");
		this.mbIdent.setIdentification(130, "NICE Comment");
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
		
		wheelMotor1 = new EV3MediumRegulatedMotor(MotorPort.B);
		wheelMotor2 = new EV3MediumRegulatedMotor(MotorPort.C);
		
	    wheelPilot = new DifferentialPilot(WHEEL_DIAMETER, DISTANCE_BETWEEN_WHEELS, wheelMotor1, wheelMotor2);
	    this.configurePilot(wheelPilot);
		IRSensor = new EV3IRSensor(SensorPort.S1);
		this.configureInfraredSensor(IRSensor, wheelPilot);
	}


    private void configureInfraredSensor(final EV3IRSensor infraredSensor, final DifferentialPilot pilot) {

    }
 
    private void configurePilot(final DifferentialPilot pilot) {
        pilot.setAcceleration(PILOT_ACCELERATION);
        pilot.setRotateSpeed(PILOT_SPEED_ROT);
        pilot.setTravelSpeed(PILOT_SPEED);
    }
	
	/*
	 * EV3 Close
	 */
	public void stopEV3() {
		wheelMotor1.close();
		wheelMotor2.close();
			
		IRSensor.close();
	}
	
	/*
	 * Thread to manage the device
	 */
	public void run() {
		int command = 0;
		//float[] distances = new float[IRSensor.sampleSize()];
		
		while (Button.ESCAPE.isUp()) {
			command = IRSensor.getRemoteCommand(0);
            //IRSensor.fetchSample(distances, 0);			
		
		/*
		    1 TOP-LEFT
			2 BOTTOM-LEFT
			3 TOP-RIGHT
			4 BOTTOM-RIGHT
			5 TOP-LEFT + TOP-RIGHT
			6 TOP-LEFT + BOTTOM-RIGHT
			7 BOTTOM-LEFT + TOP-RIGHT
			8 BOTTOM-LEFT + BOTTOM-RIGHT
			9 CENTRE/BEACON
			10 BOTTOM-LEFT + TOP-LEFT
			11 TOP-RIGHT + BOTTOM-RIGHT
		 */
            if (command == 1) {
            	System.out.println("Avance");
                wheelPilot.forward();
            } 
            if (command == 2) {
            	System.out.println("Recule");
                wheelPilot.backward();
            }
            if (command == 3) {
            	System.out.println("Gauche");
                wheelPilot.rotate(+90);
            }
            if (command == 4) {
            	System.out.println("Droite");
                wheelPilot.rotate(-90);
            }
            
            if (command == 9 || command == 0) {
            	System.out.println("Stop");
                wheelPilot.stop();
            }
            
            
            
		}
		/*
		drawScreen();
        wheelPilot.forward();
		Delay.msDelay(5000);
        wheelPilot.rotate(90);
//        Button.waitForAnyPress();
		*/
	}

	/*
	 * draw screen
	 */
	public void drawScreen() {
		LCD.clearDisplay();
		LCD.drawString("ROVER", 4, 0);
		LCD.drawString("ESC Exit", 7, 6);
	}
	
	/*
	 * Sounds a beep
	 */
	public void beep() {
		Audio audio = this.ev3.getAudio();
		audio.systemSound(0);
	}

}