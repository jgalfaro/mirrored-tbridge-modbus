package device.util;

import lejos.hardware.sensor.EV3TouchSensor;
import net.wimpi.modbus.procimg.*;

public final class TouchSensorRegisterIn extends SynchronizedAbstractRegister implements InputRegister {
	private EV3TouchSensor sensor;

	public TouchSensorRegisterIn(EV3TouchSensor mySensor) {
		this.sensor = mySensor;
	}

	public final int getValue() {
		float[] touchSample = new float[this.sensor.sampleSize()];
		this.sensor.fetchSample(touchSample, 0);
		return (int) touchSample[0]; 
	}

	public final boolean isValid() {
		if (this.sensor == null) {
			return false;
		}
		return true;
	}
}