package device.util;

import lejos.hardware.Button;

public final class ButtonSensorRegisterIn extends AbstractSensorRegisterIn {

	public ButtonSensorRegisterIn() {
	}//constructor

	public int getSensorValue() {
		int buttons = 0;

		if (Button.UP.isDown()) {
			buttons += Button.ID_UP;
		}

		if (Button.ENTER.isDown()) {
			buttons += Button.ID_ENTER;
		}

		if (Button.DOWN.isDown()) {
			buttons += Button.ID_DOWN;
		}

		if (Button.RIGHT.isDown()) {
			buttons += Button.ID_RIGHT;

		}

		if (Button.LEFT.isDown()) {
			buttons += Button.ID_LEFT;
		}

		if (Button.ESCAPE.isDown()) {
			buttons += Button.ID_ESCAPE;
		}
		return buttons;
	}

		
}