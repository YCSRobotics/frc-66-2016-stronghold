package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer.StaticInterface;
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
	
	public Drivetrain(Joystick joy, Victor left, double leftScaler, Victor right, double rightScaler, Encoder leftEncoder, Encoder rightEncoder){
		Drivetrain.controller = joy;
		
		Drivetrain.LEFT_MOTOR = left;
		Drivetrain.LEFT_MOTOR_SCALER = leftScaler;
		Drivetrain.RIGHT_MOTOR = right;
		Drivetrain.RIGHT_MOTOR_SCALER = rightScaler;
		
		Drivetrain.LEFT_ENCODER = leftEncoder;
		Drivetrain.RIGHT_ENCODER = rightEncoder;
		
		Drivetrain.leftSide = new DrivetrainSide(LEFT_MOTOR, LEFT_MOTOR_SCALER);
		Drivetrain.rightSide = new DrivetrainSide(RIGHT_MOTOR, RIGHT_MOTOR_SCALER);
	}
	
	public void updateDrivetrain() {
		if(controller.getRawButton(5)) {
			leftSide.set(controller.getRawAxis(1) * TURBO_SCALER);
			rightSide.set(controller.getRawAxis(5) * TURBO_SCALER);
		} else {
			leftSide.set(controller.getRawAxis(1));
			rightSide.set(controller.getRawAxis(5));
		}
		
		SmartDashboard.putNumber("Left Encoder", LEFT_ENCODER.getDistance());
		SmartDashboard.putNumber("Right Encoder", RIGHT_ENCODER.getDistance());
	}
}
