package device.util;

import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

public final class GyroSensorRegisterIn extends AbstractSensorRegisterIn {
	  private EV3GyroSensor sensor;
	  private float[] angle = { 0 };
	  private SampleProvider angleProvider;
	  
	  public GyroSensorRegisterIn(EV3GyroSensor mySensor) {
		  this.sensor = mySensor;

		  //Reset the angle
		  this.sensor.reset();
		  
		  //Object to get the angle
		  this.angleProvider = sensor.getAngleMode();
	  }

	public int getSensorValue() {
		
		angleProvider.fetchSample(angle, 0);
		
		int value = (int)angle[0] % 360; 
		
		//to prevent a negative value (% 360  in java can produce negative numbers)
		if (value < 0) {
			value = value + 360;
		}
		
		return (value);
	}
}