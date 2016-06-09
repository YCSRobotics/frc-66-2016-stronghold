package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Victor;

public class DrivetrainSide {
	
	private Victor motor;
	private double motorScaler;
	
	private double speed = 0.0;
	private int rampingFactor = Constants.DRIVETRAIN_RAMPING_FACTOR;
	
	public DrivetrainSide(Victor motor, double motorScaler) {
		this.motor = motor;
		this.motorScaler = motorScaler;
	}

	public void set(double input) {
		if (Math.abs(input) < 0.25) {
			input = 0;
		}
		if (input > 0) {
			speedRamping(input * input);
		} else if (input < 0) {
			speedRamping(-(input * input));
		} else {
			speedRamping(0);
		}
		motor.set(speed * motorScaler);
	}
	
	private void speedRamping(double input) {
		double tempSpeed = ((speed * (rampingFactor - 1)) + input) / rampingFactor;
		if (Math.abs(tempSpeed) < 0.01) {
			tempSpeed = 0;
		}
		speed = tempSpeed;
	}
}
