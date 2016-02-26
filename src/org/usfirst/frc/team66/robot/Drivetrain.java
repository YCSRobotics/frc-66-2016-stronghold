package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class Drivetrain {
	private static double TURBO_SCALER = (1 / Constants.RIGHT_MOTOR_SCALER);
	
	private static Joystick controller;
	
	private static Victor LEFT_MOTOR;
	private static double LEFT_MOTOR_SCALER;
	private static Victor RIGHT_MOTOR;
	private static double RIGHT_MOTOR_SCALER;
	
	private static Encoder LEFT_ENCODER;
	private static Encoder RIGHT_ENCODER;
	
	static DrivetrainSide leftSide;
	static DrivetrainSide rightSide;
	
	public Drivetrain() {
		Drivetrain.controller = Constants.DRIVE_CONTROLLER;
		
		Drivetrain.LEFT_MOTOR = Constants.LEFT_MOTOR;
		Drivetrain.LEFT_MOTOR_SCALER = Constants.LEFT_MOTOR_SCALER;
		Drivetrain.RIGHT_MOTOR = Constants.RIGHT_MOTOR;
		Drivetrain.RIGHT_MOTOR_SCALER = Constants.RIGHT_MOTOR_SCALER;
		
		Drivetrain.LEFT_ENCODER = Constants.LEFT_ENCODER;
		Drivetrain.RIGHT_ENCODER = Constants.RIGHT_ENCODER;
		
		Drivetrain.leftSide = new DrivetrainSide(LEFT_MOTOR, LEFT_MOTOR_SCALER);
		Drivetrain.rightSide = new DrivetrainSide(RIGHT_MOTOR, RIGHT_MOTOR_SCALER);
	}
	
	public void updateDrivetrain() {
		if(controller.getRawButton(5)) {
			if (controller.getRawButton(6)) {
				goStraight(TURBO_SCALER);
			} else {
				leftSide.set(controller.getRawAxis(1) * TURBO_SCALER);
				rightSide.set(controller.getRawAxis(5) * TURBO_SCALER);
			}
		} else {
			if (controller.getRawButton(6)) {
				goStraight(1.0);
			} else {
				leftSide.set(controller.getRawAxis(1));
				rightSide.set(controller.getRawAxis(5));
			}
		}
		
		SmartDashboard.putNumber("Left Encoder", LEFT_ENCODER.getDistance());
		SmartDashboard.putNumber("Right Encoder", RIGHT_ENCODER.getDistance());
	}
	
	private void goStraight(double SCALER) {
		if (Math.abs(controller.getRawAxis(1)) >= Math.abs(controller.getRawAxis(5))) {
			leftSide.set(controller.getRawAxis(1) * SCALER);
			rightSide.set(controller.getRawAxis(1) * SCALER);
		} else {
			leftSide.set(controller.getRawAxis(1));
			rightSide.set(controller.getRawAxis(5));
		}
	}
}
