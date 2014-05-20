package net.wimpi.modbus;

public class ModbusDeviceItem {
	private int m_Id;
	private String m_Value;
	
	public ModbusDeviceItem() {
		
	}
	public ModbusDeviceItem(int id, String value) {
		this.m_Id = id;
		this.m_Value = value;
	}
	
	public String getValue () {
		return m_Value;
	}

	public int getId () {
		return m_Id;
	}

	public int getValueLength() {
		return m_Value.length();
	}
}
