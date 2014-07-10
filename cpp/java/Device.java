//Modbus imports
import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.net.ModbusTCPListener;
import net.wimpi.modbus.procimg.SimpleProcessImage;

public abstract class Device  {

	public SimpleProcessImage spi = null;
   	public ModbusDeviceIdentification mbIdent = null;
	public ModbusTCPListener listener = null;

	private String modbusAddr = "127.0.0.1";
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
		//this.spi.setLocked(true);

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
	abstract public void initEV3();

	abstract public void loadEV3();

	/*
	 * EV3 Close
	 */
	abstract public void stopEV3();

	/*
	 * Thread to manage the device
	 */
	abstract public void run(SingleThreadedServer server);

	/*
	 * Sounds a beep
	 */
	abstract public void beep();

}