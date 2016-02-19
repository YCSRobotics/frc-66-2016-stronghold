package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;

public class Shooter {
	static Joystick controller = new Joystick(0);
	static TalonSRX shootMotor = new TalonSRX(2);
//	static Victor indexerMotor = new Victor(3);

	public Shooter() {
		
	}
	
	public static void updateShooter() {
		if (controller.getRawAxis(3) >= 0.9) {
			shootMotor.set(1.0);
//			if (controller.getRawAxis(4) >= 0.9) {
//				indexerMotor.set(1.0);
//			} else {
//				indexerMotor.set(0.0);
//			}
		} else {
			shootMotor.set(0.0);
//			indexerMotor.set(0.0);
		}
	}
}
