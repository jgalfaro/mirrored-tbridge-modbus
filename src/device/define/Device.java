package device.define;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.SimpleProcessImage;

/**
 * Defines a device
 * @author Ken LE PRADO
 * @version 1.0
 */
abstract class Device  {
	
	public SimpleProcessImage spi = null;
	public ModbusDeviceIdentification mbIdent = null;
	public ModbusTCPListener listener = null;

	private String deviceAddr = "";
	private boolean modbusActive = false;
	private int modbusPort = 502;
	private int modbusNbThread = 2;
	public int modbusUnitId = 1;

	

	public Device(String deviceAddr, Boolean modbusActive, int modbusPort, int modbusUnitId) {
		this.deviceAddr = deviceAddr;

		this.setModbusActivated(modbusActive);		
		this.modbusPort = modbusPort;
		this.modbusUnitId = modbusUnitId;

	}
	
	/*
	 * Modbus initialisation
	 */
	public void initModbus() {
		this.initModbusSpi();
		//this.spi.setLocked(true);

		this.initModbusIdentification();

		ModbusCoupler.getReference().setProcessImage(this.spi);
		ModbusCoupler.getReference().setIdentification(this.mbIdent);
		ModbusCoupler.getReference().setMaster(false);
		ModbusCoupler.getReference().setUnitID(this.modbusUnitId);		
		
		this.listener = new ModbusTCPListener(this.modbusNbThread);
		if (deviceAddr != "") {
			try {
				this.listener.setAddress(InetAddress.getByName(this.deviceAddr));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		this.listener.setPort(this.modbusPort);
		this.listener.start(); 
	}

	/*
	 * Modbus unload
	 */
	public void stopModbus() {
		if (this.listener.isListening()) {
			this.listener.stop();
		}
	}
	public void setModbusActivated( boolean status) {
		this.modbusActive = status;
	}
	
	public boolean getModbusActivated( ) {
		return this.modbusActive;
	}
	
	abstract public void initModbusSpi();
	abstract public void initModbusIdentification();
	

	/*
	 * EV3 Initialisation
	 */
	abstract public void initEV3();
	
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
	abstract public void beep();

}