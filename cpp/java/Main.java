import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Main {
	private static String modbusAddr = "127.0.0.1";
	private static int modbusPort = 2048;
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

            if (deviceType.equals("TOLL_SIM")) {
                device = new TollSim(modbusAddr, modbusPort, modbusUnitId);
            } else {
                System.err.println("Device type ("+deviceType+") unknown");
            }

            System.out.println("Starting GUI at http://localhost:9090");

            SingleThreadedServer server = new SingleThreadedServer(9090, device);
            new Thread(server).start();

            if (device != null) {
			device.initModbus();

			//device.beep();
			device.run(server);
			//device.beep();

			device.stopModbus();
            }

            System.out.println("Stopping Server");
            server.stop();

            System.exit(0);

	}//end main

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




