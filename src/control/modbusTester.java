package control;

import java.net.UnknownHostException;

import device.read.Toll;
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.MaskWriteRegisterRequest;
import net.wimpi.modbus.msg.MaskWriteRegisterResponse;

public class modbusTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Toll myToll = null;
		String ipField = "10.0.0.20";
	
		//System.out.println(args[0]);
		/*
		if (args.length > 1) {
			ipField = args[1];
		}*/
		
		try {
			myToll = new Toll(ipField);
		} catch (UnknownHostException e1) {
			System.err.println( "Impossible to set IP");
			return;
		}
					
		try {
			if (!myToll.connect()) {
				return;
			}
		} catch (Exception e1) {
			System.err.println( "Impossible to establish connection to " + ipField);
			return ;
		}
		
		//Test if the DEVICE_TYPE is good
		ModbusDeviceIdentification ident = myToll.getDeviceIdentification(1, 0);
	
		if (!ident.getIdentification(1).equals("LEGO TOLL") & !ident.getIdentification(1).equals("LEGO TOLL SIM")) {
			System.err.println("Unattended device");
			return ;			
		}

		
		//Test the ReadWriteMultipleRegistersRequest
/*		
		ModbusTCPTransaction trans = new ModbusTCPTransaction(myToll.getConnection());

		Register[] registers_W = new Register[2];
		registers_W[0] = new SimpleRegister(2);
		registers_W[1] = new SimpleRegister(5);
		
		ReadWriteMultipleRegistersRequest request = new ReadWriteMultipleRegistersRequest(0, 2, 0, registers_W);
		
		ReadWriteMultipleRegistersResponse result = null;
    	trans.setRequest(request);
    	try {
    		trans.execute();
    		result = (ReadWriteMultipleRegistersResponse) trans.getResponse();

    		System.out.println("Nb registers  read : " + result.getWordCountR());
    		System.out.println("Registers : " + result.getRegistersR().toString());

    	} catch (Exception e) {		
    		e.printStackTrace();
    		System.err.println("Read Write failed");
    	}

		*/
		

		
		//Test the MaskWriteRegisterRequest
	
		ModbusTCPTransaction trans = new ModbusTCPTransaction(myToll.getConnection());

		int ref = 1;
		int andMask = 0xf2;
		int orMask = 0x25;
		
		MaskWriteRegisterRequest request = new MaskWriteRegisterRequest(ref, andMask, orMask);
		
		MaskWriteRegisterResponse result = null;
    	trans.setRequest(request);
    	try {
    		trans.execute();
    		result = (MaskWriteRegisterResponse) trans.getResponse();

    		System.out.println("Ref : " + result.getReference());
    		System.out.println("And : " + result.getAndMask());
    		System.out.println("Or : " + result.getOrMask());

    	} catch (Exception e) {		
    		e.printStackTrace();
    		System.err.println("Mask Write failed");
    	}

		
		
		return ;
		
		

	}

}
