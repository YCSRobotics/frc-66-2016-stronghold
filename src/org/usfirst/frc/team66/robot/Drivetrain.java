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
		leftSide.set(controller.getRawAxis(1));
		rightSide.set(controller.getRawAxis(5));
	}
}
