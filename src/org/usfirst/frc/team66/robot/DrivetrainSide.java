package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Victor;

public class DrivetrainSide {
	private static final Victor LEFT_MOTOR1 = new Victor(0);
	//private static final Victor LEFT_MOTOR2 = new Victor(1);
	private static final double LEFT_MOTOR1_SCALER = -0.5;
	//private static final double LEFT_MOTOR2_SCALER = 1.0;
	private static final Victor RIGHT_MOTOR1 = new Victor(1);
	//private static final Victor RIGHT_MOTOR2 = new Victor(9);
	private static final double RIGHT_MOTOR1_SCALER = 0.5;
	//private static final double RIGHT_MOTOR2_SCALER = 1.0;
	
	public DrivetrainSide() {
		
	}
	
	private boolean isLeftSide = false;
	private double currentSpeed = 0.0;
	
	public DrivetrainSide(boolean leftSide) {
		isLeftSide = leftSide;
	}
	
	public void set(double speed) {
		if (Math.abs(speed) < 0.25) {
			speed = 0;
		}
		if (speed > 0) {
			speed = speed * speed;
		} else if (speed < 0) {
			speed = -(speed * speed);
		} else {
			speed = 0;
		}
		if (isLeftSide) {
			LEFT_MOTOR1.set(speed * LEFT_MOTOR1_SCALER);
		} else {
			RIGHT_MOTOR1.set(speed * RIGHT_MOTOR1_SCALER);
		}
	}
}
