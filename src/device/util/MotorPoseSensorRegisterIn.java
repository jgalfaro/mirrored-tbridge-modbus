package device.util;

import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Pose;

public final class MotorPoseSensorRegisterIn extends AbstractSensorRegisterIn {
	private OdometryPoseProvider bridgePP = null;

	public MotorPoseSensorRegisterIn(DifferentialPilot mySensor) {
		
		bridgePP = new OdometryPoseProvider(mySensor); 

	}

	public int getSensorValue() {
		Pose pose = bridgePP.getPose();        
		float value = pose.getX();

		return (int) value; 
	}

}