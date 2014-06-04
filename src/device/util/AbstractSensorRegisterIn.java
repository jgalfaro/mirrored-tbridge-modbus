package device.util;

import net.wimpi.modbus.procimg.*;

public abstract class AbstractSensorRegisterIn implements InputRegister {
	  protected byte[] m_Register = new byte[2];
	  
	public abstract int getSensorValue() ;
		
	public void updateValue() {
		setValue(getSensorValue());
	}

	  public int getValue() {
		  updateValue();
		  return ((m_Register[0] & 0xff) << 8 | (m_Register[1] & 0xff));
	  }//getValue

	  public final int toUnsignedShort() {
		  updateValue();
	    return ((m_Register[0] & 0xff) << 8 | (m_Register[1] & 0xff));
	  }//toUnsignedShort

	  public final synchronized void setValue(int v) {
	    m_Register[0] = (byte) (0xff & (v >> 8));
	    m_Register[1] = (byte) (0xff & v);
	  }//setValue

	  public final short toShort() {
		  updateValue();
	    return (short) ((m_Register[0] << 8) | (m_Register[1] & 0xff));
	  }//toShort

	  public final synchronized void setValue(short s) {
	    m_Register[0] = (byte) (0xff & (s >> 8));
	    m_Register[1] = (byte) (0xff & s);
	  }//setValue

	  public final synchronized void setValue(byte[] bytes) {
	    if (bytes.length < 2) {
	      throw new IllegalArgumentException();
	    } else {
	      m_Register[0] = bytes[0];
	      m_Register[1] = bytes[1];
	    }
	  }//setValue

	  public byte[] toBytes() {
		  updateValue();
	    return m_Register;
	  }//toBytes
		
}