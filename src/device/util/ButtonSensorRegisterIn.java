package device.util;

import lejos.hardware.Button;
import net.wimpi.modbus.procimg.*;

public final class ButtonSensorRegisterIn extends SynchronizedAbstractRegister implements InputRegister {

  public ButtonSensorRegisterIn() {
  }

  public final int getValue() {
  //TODO Change to support multiple key press 
	if (Button.UP.isDown()) {
    	return Button.ID_UP;
    }
    
    if (Button.ENTER.isDown()) {
    	return Button.ID_ENTER;
    }
    
    if (Button.DOWN.isDown()) {
    	return Button.ID_DOWN;
    }
    
    if (Button.RIGHT.isDown()) {
    	return Button.ID_RIGHT;
    }

    if (Button.LEFT.isDown()) {
    	return Button.ID_LEFT;
    }
    
    if (Button.ESCAPE.isDown()) {
    	return Button.ID_ESCAPE;
    }

    return 0;
  }

  public final boolean isValid() {
    return true;
  }
}