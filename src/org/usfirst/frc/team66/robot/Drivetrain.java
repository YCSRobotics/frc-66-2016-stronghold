package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer.StaticInterface;

public class Drivetrain {
	static Joystick controller = new Joystick(0);
	
	static DrivetrainSide leftSide = new DrivetrainSide(true);
	static DrivetrainSide rightSide = new DrivetrainSide(false);
	
	static double leftSideSpeed = 0;
	static double rightSideSpeed = 0;
	
	public static void updateDrivetrain() {
		leftSide.set(smoothValue(controller.getRawAxis(2), leftSideSpeed, true));
		rightSide.set(smoothValue(controller.getRawAxis(5), rightSideSpeed, false));
	}
	
	private static double smoothValue(double newJoy, double oldJoy, boolean isLeftSide) {
		double smoothedSpeed = 0.0;
		smoothedSpeed = (newJoy + (2*oldJoy))/3;
		if (isLeftSide) {
			leftSideSpeed = smoothedSpeed;
		} else {
			rightSideSpeed = smoothedSpeed;
		}
		return smoothedSpeed;
	}
}
