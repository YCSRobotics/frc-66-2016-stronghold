package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Arm {
	public static Talon ARM_MOTOR;
	public static double ARM_MOTOR_SCALER;
	private static Joystick CONTROLLER;
	
	public Arm() {
		Arm.ARM_MOTOR = Constants.ARM_MOTOR;
		Arm.ARM_MOTOR_SCALER = Constants.ARM_MOTOR_SCALER_DOWN;
		Arm.CONTROLLER = Constants.SHOOT_CONTROLLER;
	}
	
	public void updateArm() {
		double speed = CONTROLLER.getRawAxis(5);
		
		if (speed >= 0) {
			speed = - (speed * speed) * Constants.ARM_MOTOR_SCALER_UP;
		} else {
			speed = (speed * speed) * Constants.ARM_MOTOR_SCALER_DOWN;
		}
		
		if (Math.abs(speed) >= 0.1) {
			ARM_MOTOR.set(speed);
		} else {
			ARM_MOTOR.set(0);
		}
	}
	
}
