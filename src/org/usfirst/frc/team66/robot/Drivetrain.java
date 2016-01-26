package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Drivetrain {
	static Joystick leftJoystick = new Joystick(0);
	static Joystick rightJoystick = new Joystick(1);
	
	static DrivetrainSide leftSide = new DrivetrainSide(true);
	static DrivetrainSide rightSide = new DrivetrainSide(false);
	
	public static void updateDrivetrain() {
		leftSide.set(leftJoystick.getY());
		rightSide.set(rightJoystick.getY());
	}
}
