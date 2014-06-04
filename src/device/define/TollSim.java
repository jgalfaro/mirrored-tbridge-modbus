package device.define;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

//Modbus imports
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.procimg.SimpleDigitalIn;
import net.wimpi.modbus.procimg.SimpleDigitalOut;
import net.wimpi.modbus.procimg.SimpleInputRegister;
import net.wimpi.modbus.procimg.SimpleProcessImage;
import net.wimpi.modbus.procimg.SimpleRegister;

/**
 * Toll management
 * @author Ken LE PRADO
 * @version 1.0
 */
public class TollSim extends Device {
	public TollSim(String modbusAddr, int modbusPort, int modbusUnitId) {
		super(modbusAddr, modbusPort, modbusUnitId);
	}
	public static TollSimFrame window;

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
		

	/*
	 * Modbus initialisation
	 */
	public void initSpi() {		
		this.spi = new SimpleProcessImage();

		//Discrete output
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //0 STATUS_ACTIVE
		this.spi.addDigitalOut(new SimpleDigitalOut(false)); //1 STATUS_FREE

		//Discrete input
		this.spi.addDigitalIn(new SimpleDigitalIn(false)); //0 STATUS_BARRIER
		
		//Holding register
		this.spi.addRegister(new SimpleRegister(0)); //1 Nb coins
		this.spi.addRegister(new SimpleRegister(0)); //2 NB cars
	
		//Input register
		this.spi.addInputRegister(new SimpleInputRegister(this.modbusUnitId)); //0 STATUS_UNIT_ID
		this.spi.addInputRegister(new SimpleInputRegister(0)); //1 STATUS_COIN_COLOR
		this.spi.addInputRegister(new SimpleInputRegister(0)); //2 STATUS_CAR_PASSAGE
		this.spi.addInputRegister(new SimpleInputRegister(0)); //3 STATUS_KEY_PRESS
		
	}
	
	public void initMbIdentification() {
		this.mbIdent = new ModbusDeviceIdentification();
		
		this.mbIdent.setIdentification(0, "TELECOM SUD PARIS");
		this.mbIdent.setIdentification(1, "LEGO TOLL SIM");
		this.mbIdent.setIdentification(2, "0.1");
		this.mbIdent.setIdentification(3, "http://www.telecom-sudparis.eu");
		this.mbIdent.setIdentification(4, "LEGO TOLL SIM");
		this.mbIdent.setIdentification(5, "LEGO TOLL SIM SMALL EDITION");
		this.mbIdent.setIdentification(6, "LEGO TOLL SIM LEJOS");
		this.mbIdent.setIdentification(130, "NICE Comment for a simulated toll");
		this.mbIdent.setIdentification(131, "NICE Comment for a simulgfdated toll");
		this.mbIdent.setIdentification(132, "NICE Comment for a simulagsdsfgfted toll");
		this.mbIdent.setIdentification(133, "NdfsgICE Comment for a simulated toll");
		this.mbIdent.setIdentification(134, "NICE fgffdsgddComment for a simulated toll");
		this.mbIdent.setIdentification(135, "NICE Commegnt for a simulated toll");
		this.mbIdent.setIdentification(136, "NICE Commefdnt fofdgr a simulated toll");
		this.mbIdent.setIdentification(137, "NICE Commenggdsfsdgfdft for a simulated toll");
		this.mbIdent.setIdentification(138, "NICEg Comment for a simulated toll");
		this.mbIdent.setIdentification(139, "NICEfd Comment for a simulated toll");
		this.mbIdent.setIdentification(140, "NICE gfComment for a simulated toll");
		this.mbIdent.setIdentification(141, "NICE Cdsgomment for a simulated toll");
		this.mbIdent.setIdentification(142, "NICE Comfdsment for a simulated toll");
		this.mbIdent.setIdentification(143, "NICE Commegnt for a simulated toll");
		this.mbIdent.setIdentification(144, "NICE Commefdsnt for a simulated toll");
		this.mbIdent.setIdentification(145, "NICE Commentg for a simulated toll");
		this.mbIdent.setIdentification(146, "NICE Commentfds for a simulated toll");
		this.mbIdent.setIdentification(147, "NICE Comment fgor a simulated toll");
		this.mbIdent.setIdentification(148, "NICE Comment ffdsor a simulated toll");
		this.mbIdent.setIdentification(149, "NICE Comment forgf a simulated toll");
		this.mbIdent.setIdentification(150, "NICE Comment for sda simulated toll");
		this.mbIdent.setIdentification(151, "NICE Comment for agsd simulated toll");
		this.mbIdent.setIdentification(152, "NICE Comment for a ssffsdgdgfimulated toll");
		this.mbIdent.setIdentification(153, "NICE Comment for a simulfsfsgdgdgated toll");
		this.mbIdent.setIdentification(154, "NICE Comment for a simulatedfgffdsgdd toll");
		this.mbIdent.setIdentification(155, "NICE Comment for a simulated tolgsfdgsl");
	}
	
	@Override
	public void initEV3() {
		window = new TollSimFrame(this);
		window.setVisible(true);
	}
	
	/*
	 * EV3 Initialisation
	 */
	public void loadEV3() {
	}

	/*
	 * EV3 Close
	 */
	public void stopEV3() {
		//Nothing to do
	}
	
	/*
	 * Thread to manage the device
	 */
	public void run() {
		int refColorId = 0;
		int coinColorId = 0;
		
        setDisplayColor(0); //None
        while (this.spi.getInputRegister(STATUS_KEY_PRESS).getValue() != 1) {
			
			drawScreen();
			
    		try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Is the toll activated ?
			if (this.spi.getDigitalOut(STATUS_ACTIVE).isSet() == true) {
				
				//Is the toll opened freely ?
				if (this.spi.getDigitalOut(STATUS_FREE).isSet() == true) {
					setDisplayColor(1); //Green
					barrierUp();
					//Increase the car counter
					if (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 1) {
						addCar();
					}
				} else {
					setDisplayColor(3); //Orange
					//Is a coin inserted and waiting for action ?
					coinColorId = this.spi.getInputRegister(STATUS_COIN_COLOR).getValue();
					
					if (refColorId != coinColorId) {
						System.out.println("New coin inserted");
						if (isValidCoin(coinColorId)) {
							System.out.println("Coin accepted, waiting a car...");

							eatCoin();
							barrierUp();
							drawScreen();
							
							//Wait a car to low the barrier
							while (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 0);
							addCar();
													
							barrierDown();
						}  else {
							System.out.println("Coin rejected");
							rejectCoin();
						}
					} else {
						barrierDown();
					}
				}
			} else {
				setDisplayColor(2); //Red
				//if barrier is up, we low it
				barrierDown();
			}
		}
        setDisplayColor(0); //None
		barrierDown();
	}

	/*
	 * draw screen
	 */
	public void drawScreen() {

		window.setBarrierStatus(this.spi.getDigitalIn(STATUS_BARRIER).isSet());

		String status = "";
		status += "Coins inside : " + this.spi.getRegister(STATUS_NB_COINS).getValue() + "\n";
		status += "Cars viewed : " + this.spi.getRegister(STATUS_NB_CARS).getValue() + "\n";
		status += "-------------\n";
		status += "TOLL : " + this.spi.getInputRegister(STATUS_UNIT_ID).getValue() + "\n";
		status += "Coin color : " + this.spi.getInputRegister(STATUS_COIN_COLOR).getValue() + "\n";
		status += "Car passage : " + this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() + "\n";
		status += "Key : " + this.spi.getInputRegister(STATUS_KEY_PRESS).getValue() + "\n";
		status += "-------------\n";
		status += "Active : " + this.spi.getDigitalOut(STATUS_ACTIVE).isSet() + "\n";
		status += "Free : " + this.spi.getDigitalOut(STATUS_FREE).isSet() + "\n";
		status += "-------------\n";
		status += "Barrier up : " + this.spi.getDigitalIn(STATUS_BARRIER).isSet() + "\n";
		
		window.getStatus().setText(status);
		
		
	}
	
	/*
	 * Raise the barrier
	 */
	public void barrierUp() {
		if (this.spi.getDigitalIn(STATUS_BARRIER).isSet() == false) {
//			barrierMotor.rotate(+ BARRIER_ANGLE);
			this.spi.setDigitalIn(STATUS_BARRIER, new SimpleDigitalIn(true));
		}
	}

	/*
	 * Low the barrier
	 */
	public void barrierDown() {
		if (this.spi.getDigitalIn(STATUS_BARRIER).isSet() == true) {
//			barrierMotor.rotate(- BARRIER_ANGLE);		
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

		setCoin(0);
	}
	
	/*
	 * Activate motors to reject inserted coin
	 */
	public void rejectCoin() {
		setCoin(0);
	}
	
	/*
	 * Increase counter if a car has gone 
	 */
	public void addCar() {
		int nbCars = this.spi.getRegister(STATUS_NB_CARS).getValue();
		this.spi.setRegister(STATUS_NB_CARS, new SimpleRegister(nbCars + 1));
		// car_passage == 0 : wait for car to free the sensor
//		while (this.spi.getInputRegister(STATUS_CAR_PASSAGE).getValue() == 1);
		this.spi.setInputRegister(STATUS_CAR_PASSAGE, new SimpleInputRegister(0));
	}



	@Override
	public void beep() {
		System.out.println("Beep !");
		window.beep();
	}
	
	public void setDisplayColor(int colorId) {
		switch (colorId) {
		case 0 :
			window.getStatus().setBackground(Color.WHITE);
			break;
		case 1 :
			window.getStatus().setBackground(Color.GREEN);
			break;
		case 2 :
			window.getStatus().setBackground(Color.RED);
			break;
		case 3 :
			window.getStatus().setBackground(Color.ORANGE);
			break;
		}
	}
	
	public void setNewCar() {
		System.out.println("Car is on");
		this.spi.setInputRegister(STATUS_CAR_PASSAGE, new SimpleInputRegister(1));
	}

	public void setCoin(int coinValue) {
		System.out.println("New coin " + coinValue);
		this.spi.setInputRegister(STATUS_COIN_COLOR, new SimpleInputRegister(coinValue));
	}

	public void setExit() {
		System.out.println("Exit");
		this.spi.setInputRegister(STATUS_KEY_PRESS, new SimpleInputRegister(1));
	}
	
}