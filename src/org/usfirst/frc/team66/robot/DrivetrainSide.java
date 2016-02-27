package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Victor;

public class DrivetrainSide {
	
	private Victor motor;
	private double motorScaler;
	
	public DrivetrainSide(Victor motor, double motorScaler) {
		this.motor = motor;
		this.motorScaler = motorScaler;
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
		motor.set(speed * motorScaler);
	}
}
