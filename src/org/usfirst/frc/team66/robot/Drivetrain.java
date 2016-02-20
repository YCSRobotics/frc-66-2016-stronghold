package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer.StaticInterface;
import edu.wpi.first.wpilibj.Victor;

public class Drivetrain {
	private static double TURBO_SCALER = 2.0;
	
	private static Joystick controller = new Joystick(0);
	
	private static Victor LEFT_MOTOR;
	private static double LEFT_MOTOR_SCALER;
	private static Victor RIGHT_MOTOR;
	private static double RIGHT_MOTOR_SCALER;
	
	private static DrivetrainSide LEFT_SIDE;
	private static DrivetrainSide RIGHT_SIDE;
	
	
	public Drivetrain() {
		
	}
	
	public Drivetrain(Joystick joy, Victor left, double leftScaler, Victor right, double rightScaler){
		Drivetrain.controller = joy;
		Drivetrain.LEFT_MOTOR = left;
		Drivetrain.LEFT_MOTOR_SCALER = leftScaler;
		Drivetrain.RIGHT_MOTOR = right;
		Drivetrain.RIGHT_MOTOR_SCALER = rightScaler;
		
		LEFT_SIDE = new DrivetrainSide(LEFT_MOTOR, LEFT_MOTOR_SCALER);
		RIGHT_SIDE = new DrivetrainSide(RIGHT_MOTOR, RIGHT_MOTOR_SCALER);
	}
	
	public void updateDrivetrain() {
		if(controller.getRawButton(5)) {
			if (controller.getRawButton(6)) {
				goStraight(TURBO_SCALER);
			} else {
				LEFT_SIDE.set(controller.getRawAxis(1) * TURBO_SCALER);
				RIGHT_SIDE.set(controller.getRawAxis(5) * TURBO_SCALER);
			}
		} else {
			if (controller.getRawButton(6)) {
				goStraight(1.0);
			} else {
				LEFT_SIDE.set(controller.getRawAxis(1));
				RIGHT_SIDE.set(controller.getRawAxis(5));
			}
		}
	}
	
	private void goStraight(double SCALER) {
		if (Math.abs(controller.getRawAxis(1)) >= Math.abs(controller.getRawAxis(5))) {
			LEFT_SIDE.set(controller.getRawAxis(1) * SCALER);
			RIGHT_SIDE.set(controller.getRawAxis(1) * SCALER);
		} else {
			LEFT_SIDE.set(controller.getRawAxis(5) * SCALER);
			RIGHT_SIDE.set(controller.getRawAxis(5) * SCALER);
		}
	}
}
