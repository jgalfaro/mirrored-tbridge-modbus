package device.define;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.automatak.dnp3.Channel;
import com.automatak.dnp3.ChannelState;
import com.automatak.dnp3.ChannelStateListener;
import com.automatak.dnp3.DNP3Manager;
import com.automatak.dnp3.DatabaseConfig;
import com.automatak.dnp3.LogLevel;
import com.automatak.dnp3.Outstation;
import com.automatak.dnp3.OutstationStackConfig;
import com.automatak.dnp3.StaticAnalogResponse;
import com.automatak.dnp3.impl.DNP3ManagerFactory;
import com.automatak.dnp3.mock.PrintingLogSubscriber;
import com.automatak.dnp3.mock.SuccessCommandHandler;

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

	
	public DNP3Manager dnp3Manager = null;
	public Channel dnp3Channel = null;
	public OutstationStackConfig dnp3Config = null;
	public Outstation dnp3Outstation = null;
	private boolean dnp3Active = false;
	private int dnp3Port = 20000;
	private int dnp3NbThread = 2;
	public int dnp3UnitId = 1;

	public Device(String deviceAddr, Boolean modbusActive, int modbusPort, int modbusUnitId, Boolean dnp3Active, int dnp3Port, int dnp3UnitId) {
		this.deviceAddr = deviceAddr;

		this.setModbusActivated(modbusActive);		
		this.modbusPort = modbusPort;
		this.modbusUnitId = modbusUnitId;

		this.setDnp3Activated(dnp3Active);		
		this.dnp3Port = dnp3Port;
		this.dnp3UnitId = dnp3UnitId;

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
	 * DNP3 initialisation
	 */
	public void initDnp3() {
        // create the root class with a thread pool size of 1
        dnp3Manager = DNP3ManagerFactory.createManager(dnp3NbThread);

        // You can send the log messages anywhere you want
        // but PrintingLogSubscriber just prints them to the console
        dnp3Manager.addLogSubscriber(PrintingLogSubscriber.getInstance());

        // Create a TCP channel class that will connect to the address
        dnp3Channel = dnp3Manager.addTCPServer("client", LogLevel.INFO, 5000, deviceAddr, dnp3Port);

        
        // You can optionally add a listener to receive state changes on the channel
        dnp3Channel.addStateListener(new ChannelStateListener() {
            @Override
            public void onStateChange(ChannelState state) {
                System.out.println("server state: " + state);
            }
        });

        initDnp3Config();

        // Create an Outstation instance, pass in a simple a command handler that responds successfully to everything
        dnp3Outstation = dnp3Channel.addOutstation("outstation", LogLevel.INTERPRET, SuccessCommandHandler.getInstance(), dnp3Config);

        /*
        // You can optionally add a listener to receive state changes on the stack
        outstation.addStateListener(new StackStateListener() {
            @Override
            public void onStateChange(StackState state) {
                System.out.println("Outstation state: " + state);
            }
        });

        // This sub-interface allows us to load data into the outstation
        DataObserver data = outstation.getDataObserver();
		*/
		
/* To modify data
        // all this stuff just to read a line of text in Java. Oh the humanity.
        String line = "";
        InputStreamReader converter = new InputStreamReader(System.in);
        BufferedReader in = new BufferedReader(converter);

        int i = 0;
        while (true) {
            System.out.println("Enter something to update a counter or type <quit> to exit");
            line = in.readLine();
            if(line.equals("quit")) break;
            else {
                data.start();
                data.update(new Counter(i, CounterInputQuality.ONLINE.toByte(), 0), 0);
                data.end();
                ++i;
            }
        }
*/
	}

	/*
	 * DNP3 unload
	 */
	public void stopDnp3() {
		dnp3Manager.shutdown();
	}

	public void setDnp3Activated( boolean status) {
		this.dnp3Active = status;
	}
	
	public boolean getDnp3Activated( ) {
		return this.dnp3Active;
	}

	abstract public void initDnp3Config();
	
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