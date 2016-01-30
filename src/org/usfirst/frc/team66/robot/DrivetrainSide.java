package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class DrivetrainSide {
	private static final VictorSP LEFT_MOTOR1 = new VictorSP(0);
	//private static final Victor LEFT_MOTOR2 = new Victor(1);
	private static final double LEFT_MOTOR1_SCALER = 1.0;
	//private static final double LEFT_MOTOR2_SCALER = 1.0;
	private static final VictorSP RIGHT_MOTOR1 = new VictorSP(8);
	//private static final Victor RIGHT_MOTOR2 = new Victor(9);
	private static final double RIGHT_MOTOR1_SCALER = 1.0;
	//private static final double RIGHT_MOTOR2_SCALER = 1.0;
	
	private boolean isLeftSide = false;
	
	public DrivetrainSide(boolean leftSide) {
		isLeftSide = leftSide;
	}
	
	public void set(double speed) {
		if (isLeftSide) {
			LEFT_MOTOR1.set(speed * LEFT_MOTOR1_SCALER);
			//LEFT_MOTOR2.set(speed * LEFT_MOTOR2_SCALER);
		} else {
			RIGHT_MOTOR1.set(speed * RIGHT_MOTOR1_SCALER);
			//RIGHT_MOTOR2.set(speed * RIGHT_MOTOR2_SCALER);
		}
	}
}
