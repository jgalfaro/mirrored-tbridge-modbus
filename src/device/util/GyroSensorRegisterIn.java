package device.util;

import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;
import net.wimpi.modbus.procimg.*;

public final class GyroSensorRegisterIn extends SynchronizedAbstractRegister implements InputRegister {
	  private EV3GyroSensor sensor;
	  private float[] angle = { 0 };


  public GyroSensorRegisterIn(EV3GyroSensor mySensor) {
	    this.sensor = mySensor;
  }

  public final int getValue() {
	 //Read sensor
	  SampleProvider angleProvider ;
	  
	  angleProvider = sensor.getAngleMode();
	  angleProvider.fetchSample(angle, 0);
	  
	  return (int)angle[0];
  }

  public final boolean isValid() {
    if (sensor == null) {
    	return false;
    }
    return true;
  }
}