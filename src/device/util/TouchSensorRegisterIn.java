package device.util;

import lejos.hardware.sensor.EV3TouchSensor;

public final class TouchSensorRegisterIn extends AbstractSensorRegisterIn {
	private EV3TouchSensor sensor;
	  
	  public TouchSensorRegisterIn(EV3TouchSensor mySensor) {
		  this.sensor = mySensor;

	  }

	public int getSensorValue() {
		float[] touchSample = new float[this.sensor.sampleSize()];
		this.sensor.fetchSample(touchSample, 0);
		return (int) touchSample[0]; 
	}
}