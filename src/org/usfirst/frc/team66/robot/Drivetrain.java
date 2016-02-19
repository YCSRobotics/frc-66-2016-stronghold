package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer.StaticInterface;
import edu.wpi.first.wpilibj.Victor;

public class Drivetrain {
	private static double TURBO_SCALER = 2.0;
	
	private static Joystick controller = new Joystick(0);
	
	private static final Victor LEFT_MOTOR = new Victor(0);
	private static final double LEFT_MOTOR_SCALER = -0.5;
	private static final Victor RIGHT_MOTOR = new Victor(1);
	private static final double RIGHT_MOTOR_SCALER = 0.5;
	
	static DrivetrainSide leftSide = new DrivetrainSide(LEFT_MOTOR, LEFT_MOTOR_SCALER);
	static DrivetrainSide rightSide = new DrivetrainSide(RIGHT_MOTOR, RIGHT_MOTOR_SCALER);
	
	public Drivetrain() {
		
	}
	
	public Drivetrain(Joystick joy, Victor left, double leftScaler, Victor right, double rightScaler){
		
	}
	
	public void updateDrivetrain() {
		if(controller.getRawButton(5)) {
			leftSide.set(controller.getRawAxis(1) * TURBO_SCALER);
			rightSide.set(controller.getRawAxis(5) * TURBO_SCALER);
		} else {
			leftSide.set(controller.getRawAxis(1));
			rightSide.set(controller.getRawAxis(5));
		}
	}
}
