package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {
	static Joystick controller = new Joystick(0);
	static Victor shootMotor = new Victor(2);
	static Victor feedMotor = new Victor(3);

	public static void updateShooter() {
		if (controller.getRawAxis(3) >= 0.9) {
			shootMotor.set(1.0);
			if (controller.getRawAxis(4) >= 0.9) {
				feedMotor.set(1.0);
			} else {
				feedMotor.set(0.0);
			}
		} else {
			shootMotor.set(0.0);
			feedMotor.set(0.0);
		}
	}
}
