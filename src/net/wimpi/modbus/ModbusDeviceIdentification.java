package net.wimpi.modbus;

import java.util.ArrayList;

import net.wimpi.modbus.procimg.IllegalAddressException;

public class ModbusDeviceIdentification {
	private final static short MAX_OBJECT_IDENTIFICATION = 256;
	public final static int BASIC = 1;
	public final static int REGULAR = 2;
	public final static int EXTENDED = 3;
	public final static int SPECIFIC = 4;
	private String objects[];
	
	public ModbusDeviceIdentification() {
		objects = new String[MAX_OBJECT_IDENTIFICATION];
	}

	public void addIdentification(int objectId, String value) throws IllegalAddressException {
		if (objectId > MAX_OBJECT_IDENTIFICATION) {
			throw new IllegalAddressException();
		}
		objects[objectId] = value;
	}

	public void setIdentification(int objectId, String value) throws IllegalAddressException {
		if (objectId > MAX_OBJECT_IDENTIFICATION) {
			throw new IllegalAddressException();
		}
		objects[objectId] = value;
	}
	
	public String getIdentification(int objectId) {
		String value = "null";
		
		return value;
	}
	
	public ArrayList<ModbusDeviceItem> getExtract(int extractType, int objectId) throws IllegalAddressException {
		ArrayList<ModbusDeviceItem> extract = new ArrayList<ModbusDeviceItem>();
		ModbusDeviceItem item;
		short i = 0;
		
		switch (extractType) {
		case BASIC:
			for (i = 0; i< 3; i++) {
				item = new ModbusDeviceItem(i, objects[i]);
				extract.add(item);
			}			
			break;
		case REGULAR:
			for (i = 3; i< 128; i++) {
				item = new ModbusDeviceItem(i, objects[i]);
				extract.add(item);
			}			
			break;
		case EXTENDED:
			for (i = 128; i< 256; i++) {
				item = new ModbusDeviceItem(i, objects[i]);
				extract.add(item);
			}						
			break;
		case SPECIFIC:
			item = new ModbusDeviceItem(i, objects[i]);
			extract.add(item);
			break;
		default:
			throw new IllegalAddressException();
		}
		
		return extract;
	}
	
	public int getLength() {
		return objects.length;
	}
	
	public void print() {
		int i ;
		for (i = 0; i < 30; i++) {
			System.out.println("Id : " + i + " Value : " + objects[i]);
		}
	}
}
