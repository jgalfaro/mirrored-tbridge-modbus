package device.util;

import lejos.hardware.sensor.EV3ColorSensor;
import net.wimpi.modbus.procimg.*;

public final class ColorSensorRegisterIn extends SynchronizedAbstractRegister implements InputRegister {
  private EV3ColorSensor sensor;

  public ColorSensorRegisterIn(EV3ColorSensor mySensor) {
    this.sensor = mySensor;
  }

  public final int getValue() {	  
	 return sensor.getColorID();
  }

  public final boolean isValid() {
    if (sensor == null) {
    	return false;
    }
    return true;
  }
}