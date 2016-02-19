package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class Intake {
	
	private static Victor FEED_MOTOR;
	private static double FEED_MOTOR_SCALER;
	private static Joystick CONTROLLER;
	
	public Intake(Joystick controller, Victor FEED_MOTOR, double FEED_MOTOR_SCALER) {
		Intake.FEED_MOTOR = FEED_MOTOR;
		Intake.FEED_MOTOR_SCALER = FEED_MOTOR_SCALER;
		Intake.CONTROLLER = controller;
	}

	public void updateIntake() {
		if (CONTROLLER.getRawAxis(3) >= 0.9) {
			FEED_MOTOR.set(1.0);
		} else {
			FEED_MOTOR.set(0);
		}
	}
}