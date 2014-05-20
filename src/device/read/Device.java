package device.read;


import java.net.InetAddress;
import java.net.UnknownHostException;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusDeviceIdentification;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadDeviceIdentificationRequest;
import net.wimpi.modbus.msg.ReadDeviceIdentificationResponse;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;

abstract class Device {

	private InetAddress address = null;
	private int port = 0;
	private TCPMasterConnection con = null;
	
	public Device (String address, int port) throws UnknownHostException {
    	this.setIp(address);
    	this.setPort(port);
	}

	public Device (String address) throws UnknownHostException {
    	this.setIp(address);
		this.setPort(Modbus.DEFAULT_PORT);
	}

	public Device () {
		this.setPort(Modbus.DEFAULT_PORT);
	}

	
	public void setIp (String address) throws UnknownHostException {
		this.address = InetAddress.getByName(address);
	}

	public void setPort (int port) {
		this.port = port;
	}

	public boolean isConnected() {
		if (con == null) {
			return false;
		} else {
			return con.isConnected();
		}
	}
	
	abstract int getStatusNameId() ;

	abstract void setStatusNameId(int value);
	
	
	public boolean getBoolRW(int registerRef) {
		int nbValues = 1;
		boolean returnedValue;
		ModbusTCPTransaction trans = new ModbusTCPTransaction(this.con);

		ReadCoilsRequest request = new ReadCoilsRequest(registerRef, nbValues);
		ReadCoilsResponse result = null;
    	trans.setRequest(request);
		//System.err.println("Trying to read register " + registerRef);
    	try {
    		trans.execute();
    		result = (ReadCoilsResponse) trans.getResponse();
    		returnedValue = result.getCoilStatus(0);
    		//System.err.println("Value returned=" + returnedValue);
    		return returnedValue;

    	} catch (Exception e) {		    		
    		System.err.println("Read failed");
    	}
    	return false;
	}	
	public boolean getBoolRO(int registerRef) {
		int nbValues = 1;
		boolean returnedValue;
		ModbusTCPTransaction trans = new ModbusTCPTransaction(this.con);

		ReadInputDiscretesRequest request = new ReadInputDiscretesRequest(registerRef, nbValues);
		ReadInputDiscretesResponse result = null;
    	trans.setRequest(request);
		//System.err.println("Trying to read register " + registerRef);
    	try {
    		trans.execute();
    		result = (ReadInputDiscretesResponse) trans.getResponse();
    		returnedValue = result.getDiscretes().getBit(0);
    		//System.err.println("Value returned=" + returnedValue);
    		return returnedValue;

    	} catch (Exception e) {		    		
    		System.err.println("Read failed");
    	}
    	return false;
	}

	public int getIntRW(int registerRef) {
		int nbValues = 1;
		int returnedValue;
		ModbusTCPTransaction trans = new ModbusTCPTransaction(this.con);

		ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(registerRef, nbValues);
		ReadMultipleRegistersResponse result = null;
    	trans.setRequest(request);
		//System.err.println("Trying to read register " + registerRef);
    	try {
    		trans.execute();
    		result = (ReadMultipleRegistersResponse) trans.getResponse();
    		returnedValue = result.getRegisterValue(0);
    		//System.err.println("Value returned=" + returnedValue);
    		return returnedValue;
    	} catch (Exception e) {		    		
    		System.err.println("Read failed");
    	}
    	return 0;
	}

	public int getIntRO(int registerRef) {
		int nbValues = 1;
		int returnedValue;
		ModbusTCPTransaction trans = new ModbusTCPTransaction(this.con);

		ReadInputRegistersRequest request = new ReadInputRegistersRequest(registerRef, nbValues);
		ReadInputRegistersResponse result = null;
    	trans.setRequest(request);
    	try {
    		trans.execute();
    		result = (ReadInputRegistersResponse) trans.getResponse();
    		returnedValue = result.getRegisterValue(0);
    		//System.err.println("Value returned=" + returnedValue);
    		return returnedValue;
    	} catch (Exception e) {		    		
    		System.err.println("Read failed");
    	}
    	return 0;
	}

	public void setBool(int registerRef, boolean value) {
		ModbusTCPTransaction trans = new ModbusTCPTransaction(this.con);
		
	 	WriteCoilRequest request = new WriteCoilRequest(registerRef, value);
	 	trans.setRequest(request);
    	try {
    		trans.execute();
    		trans.getResponse();
    	} catch (Exception e) {		    		
    		System.err.println("Write failed");
    	}
	}
		
	public void setInt(int registerRef, int value) {
		ModbusTCPTransaction trans = new ModbusTCPTransaction(this.con);
		
	 	WriteSingleRegisterRequest request = new WriteSingleRegisterRequest(registerRef, new SimpleInputRegister(value));
	 	trans.setRequest(request);
    	try {
    		trans.execute();
    		trans.getResponse();
    	} catch (Exception e) {		    		
    		System.err.println("Write failed");
    	}
	}
	
	public ModbusDeviceIdentification getDeviceIdentification(int readType, int objectId) {
		ModbusDeviceIdentification returnedValue = new ModbusDeviceIdentification();
		ModbusTCPTransaction trans = new ModbusTCPTransaction(this.con);

		ReadDeviceIdentificationRequest request = new ReadDeviceIdentificationRequest(readType, objectId);
		ReadDeviceIdentificationResponse result = null;
    	trans.setRequest(request);
		System.out.println("Trying to read device Id " + readType + " " + objectId);
    	try {
    		trans.execute();
    		result = (ReadDeviceIdentificationResponse) trans.getResponse();
    		returnedValue = result.getDeviceIdent();
    	} catch (Exception e) {		    		
    		System.err.println("Read device identification failed");
    	}
		return returnedValue;
	}
	
	public boolean connect() throws Exception {
		con = new TCPMasterConnection(this.address);
    	con.setPort(port);
    	try {
    		con.connect();
    	} catch (Exception e) {
    		System.err.println("Connection error");
    		e.printStackTrace();
    		return false;
    	}
		return true;
	}
	
	public void close() {
		if(con != null) {
			if (con.isConnected()) {
				con.close();
			}
		}
	}
	
}
