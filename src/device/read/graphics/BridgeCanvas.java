package device.read.graphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BridgeCanvas extends JPanel {
	private static final long serialVersionUID = 8695090553402617092L;
	private int angle = 0;
	private int moving = 0;
	private int gyroAngle = 0;	
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.black);
		
		g.drawString(angle + " d. / " + gyroAngle, 5, this.getHeight() - 10);

		switch(moving) {
		case 0:
			g.drawOval(this.getWidth() - 10, 10, 5, 5);
			break;
		case 1:
			g.drawLine(this.getWidth() - 10, 2, this.getWidth() - 10, 10);
			g.drawLine(this.getWidth() - 10, 2, this.getWidth() - 13, 5);
			g.drawLine(this.getWidth() - 10, 2, this.getWidth() - 7, 5);
			break;
		case 2:
			g.drawLine(this.getWidth() - 10, 10, this.getWidth() - 10, 2);
			g.drawLine(this.getWidth() - 10, 10, this.getWidth() - 13, 7);
			g.drawLine(this.getWidth() - 10, 10, this.getWidth() - 7, 7);
			break;
		}
		
		if (angle > 5) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.green);
		}
	

		int length = min(this.getWidth(), this.getHeight()) - 5;
		double angleRad = Math.toRadians(angle);
		int width = (int) (Math.cos(angleRad) * length);
		int height = (int) (Math.sin(angleRad) * length);
		int startY = this.getHeight() - 1;
		int startX = 1;
		
		g.drawLine(startX, startY, startX + width, startY - height);

	}

	private int min(int a, int b) {
		if (a > b) {
			return b;
		} else {
			return a;
		}
	}

	public void setStatus(int angle, int moving, int gyroAngle) {
		this.angle = angle;
		this.moving = moving;
		this.gyroAngle = gyroAngle;
		this.repaint();
	}	
}
