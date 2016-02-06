package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer.StaticInterface;

public class Drivetrain {
	private static double TURBO_SCALER = 2.0;
	
	static Joystick controller = new Joystick(0);
	
	static DrivetrainSide leftSide = new DrivetrainSide(true);
	static DrivetrainSide rightSide = new DrivetrainSide(false);
	
	static double leftSideSpeed = 0;
	static double rightSideSpeed = 0;
	
	public static void updateDrivetrain() {
		if(controller.getRawButton(5)) {
			leftSide.set(controller.getRawAxis(1) * TURBO_SCALER);
			rightSide.set(controller.getRawAxis(5) * TURBO_SCALER);
		} else {
			leftSide.set(controller.getRawAxis(1));
			rightSide.set(controller.getRawAxis(5));
		}
	}
}
