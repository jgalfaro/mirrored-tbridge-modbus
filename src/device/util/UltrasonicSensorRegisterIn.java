package device.util;

import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public final class UltrasonicSensorRegisterIn extends AbstractSensorRegisterIn {
	  private EV3UltrasonicSensor sensor;
	  private float[] distance = { 0 };
	  private SampleProvider distanceProvider;
	  
	  public UltrasonicSensorRegisterIn(EV3UltrasonicSensor mySensor) {
		  this.sensor = mySensor;
		  
		  //Object to get the angle
		  this.distanceProvider = sensor.getDistanceMode();
	  }

	/*
	 * Return the distance in cm (to be an integer)
	 */
	public int getSensorValue() {
		
		distanceProvider.fetchSample(distance, 0);
		
		int value = (int)(distance[0] * 100); 

		//System.out.println(distance[0]);
		return (value);
	}
}