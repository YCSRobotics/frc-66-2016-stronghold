package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer.StaticInterface;

public class Drivetrain {
	static Joystick leftJoystick = new Joystick(0);
	static Joystick rightJoystick = new Joystick(1);
	
	static DrivetrainSide leftSide = new DrivetrainSide(true);
	static DrivetrainSide rightSide = new DrivetrainSide(false);
	
	static double leftSideSpeed = 0;
	static double rightSideSpeed = 0;
	
	public Drivetrain() {
		
	}
	
	public static void updateDrivetrain() {
		leftSide.set(smoothValue(leftJoystick.getY(), leftSideSpeed, true));
		rightSide.set(smoothValue(rightJoystick.getY(), rightSideSpeed, false));
	}
	
	private static double smoothValue(double newJoy, double oldJoy, boolean isLeftSide) {
		double smoothedSpeed = 0.0;
		smoothedSpeed = (newJoy + oldJoy)/2;
		if (isLeftSide) {
			leftSideSpeed = smoothedSpeed;
		} else {
			rightSideSpeed = smoothedSpeed;
		}
		return smoothedSpeed;
	}
}
