package device.define;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.SimpleProcessImage;

import lejos.hardware.Audio;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;

/**
 * Defines a device
 * @author Ken LE PRADO
 * @version 1.0
 */
abstract class Device  {
	public EV3 ev3 = null;
	
	public SimpleProcessImage spi = null;
	public ModbusDeviceIdentification mbIdent = null;
	public ModbusTCPListener listener = null;

	private String modbusAddr = "";
	private int modbusPort = 502;
	private int modbusNbThread = 2;
	public int modbusUnitId = 1;

	public Device(String modbusAddr, int modbusPort, int modbusUnitId) {
		this.modbusAddr = modbusAddr;
		this.modbusPort = modbusPort;
		this.modbusUnitId = modbusUnitId;
	}
	
	/*
	 * Modbus initialisation
	 */
	public void initModbus() {
		this.initSpi();
		this.initMbIdentification();

		ModbusCoupler.getReference().setProcessImage(this.spi);
		ModbusCoupler.getReference().setIdentification(this.mbIdent);
		ModbusCoupler.getReference().setMaster(false);
		ModbusCoupler.getReference().setUnitID(this.modbusUnitId);		
		
		this.listener = new ModbusTCPListener(this.modbusNbThread);
		if (modbusAddr != "") {
			try {
				this.listener.setAddress(InetAddress.getByName(this.modbusAddr));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		this.listener.setPort(this.modbusPort);
		this.listener.start(); 
	}
	
	abstract public void initSpi();
	abstract public void initMbIdentification();
	
	
	/*
	 * Modbus unload
	 */
	public void stopModbus() {
		if (this.listener.isListening()) {
			this.listener.stop();
		}
	}
	
	/*
	 * EV3 Initialisation
	 */
	public void initEV3() {		
		this.ev3 = (EV3) BrickFinder.getDefault();
		this.loadEV3();
	}
	
	abstract public void loadEV3();

	/*
	 * EV3 Close
	 */
	abstract public void stopEV3();
	
			/*
	 * Thread to manage the device
	 */
	abstract public void run();
	
	/*
	 * Sounds a beep
	 */
	public void beep() {
		Audio audio = this.ev3.getAudio();
		audio.systemSound(0);
	}

}