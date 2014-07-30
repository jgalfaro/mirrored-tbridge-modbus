package device.define;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DeviceInit {
	//Network address (127.0.0.1, etc.)
	private static String deviceAddr = "";
	
	//Modbus params
	private static boolean modbusActive = false;
	private static int modbusPort = 502;
	private static int modbusUnitId = 1;
	
	//Type of device (Toll, Bridge, TollSim, Rover)
	private static String deviceType = "";
	
	
	/**
	 * Toll run
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) {
		Device device = null;
		
		//Init config file
		read_init("Device.ini");
	    
		if (deviceType.equals("TOLL")) {
		    device = new Toll(deviceAddr, modbusActive, modbusPort, modbusUnitId);
		} else if (deviceType.equals("TOLL_SIM")) {
		    device = new TollSim(deviceAddr, modbusActive, modbusPort, modbusUnitId);
		} else if (deviceType.equals("BRIDGE")) {
		    device = new Bridge(deviceAddr, modbusActive, modbusPort, modbusUnitId);
		} else if (deviceType.equals("ROVER")) {
		    device = new Rover(deviceAddr, modbusActive, modbusPort, modbusUnitId);
		} else {
			System.err.println("Device type ("+deviceType+") unknown");
		}
		
		if (device != null) {
			device.initEV3();
			if (device.getModbusActivated()) {
				device.initModbus();
			}
			
			device.beep();
			device.run();
			device.beep();
			
			if (device.getModbusActivated()) {
				device.stopModbus();
			}
			device.stopEV3();
		}
		
		System.exit(0);
	}
	
	public static void read_init(String fileName) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open ini file");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Cannot read ini file");
			e.printStackTrace();
		}
		deviceAddr = p.getProperty("DEVICE_IP");
		modbusActive = Boolean.parseBoolean(p.getProperty("DEVICE_MOD_ACTIVE"));
		modbusUnitId = Integer.parseInt(p.getProperty("DEVICE_MOD_ID"));
		modbusPort = Integer.parseInt(p.getProperty("DEVICE_MOD_PORT"));

		deviceType = p.getProperty("DEVICE_TYPE");
	}
}
