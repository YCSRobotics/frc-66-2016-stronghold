package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Intake {
	
	private static Talon FEED_MOTOR;
	private static double FEED_MOTOR_SCALER;
	private static Joystick CONTROLLER;
	
	public Intake(Joystick controller, Talon FEED_MOTOR, double FEED_MOTOR_SCALER) {
		Intake.FEED_MOTOR = FEED_MOTOR;
		Intake.FEED_MOTOR_SCALER = FEED_MOTOR_SCALER;
		Intake.CONTROLLER = controller;
	}

	public void updateIntake() {
		double speed = 0.0;
		if (CONTROLLER.getRawAxis(2) >= 0.9) {
			speed = 0.5;
		} else {
			speed = 1.0;
		}
		
		if (CONTROLLER.getRawAxis(3) >= 0.9) {
			FEED_MOTOR.set(speed);
		} else {
			FEED_MOTOR.set(0);
		}
	}
}