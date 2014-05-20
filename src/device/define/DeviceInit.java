package device.define;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DeviceInit {
	private static String modbusAddr = "";
	private static int modbusPort = 502;
//	private static int modbusNbThread = 2;
	private static int modbusUnitId = 1;
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
		    device = new Toll(modbusAddr, modbusPort, modbusUnitId);
		} else if (deviceType.equals("BRIDGE")) {
		    device = new Bridge(modbusAddr, modbusPort, modbusUnitId);
		} else {
			System.err.println("Device type ("+deviceType+") unknown");
		}
		if (device != null) {
			device.initEV3();
			device.initModbus();
			
			device.beep();
			device.run();
			device.beep();
			
			device.stopModbus();
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
		modbusAddr = p.getProperty("DEVICE_IP");
		modbusPort = Integer.parseInt(p.getProperty("DEVICE_PORT"));
		modbusUnitId = Integer.parseInt(p.getProperty("DEVICE_ID"));
		deviceType = p.getProperty("DEVICE_TYPE");
	}
}
