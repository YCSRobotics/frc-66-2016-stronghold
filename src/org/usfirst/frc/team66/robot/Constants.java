package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;

public class Constants {
	// Controllers
	
	public static final Joystick DRIVE_CONTROLLER = new Joystick(0);
	public static final Joystick SHOOT_CONTROLLER = new Joystick(1);

	// Drivetrain Motors
	
	public static final Victor LEFT_MOTOR = new Victor(0);
	public static final double LEFT_MOTOR_SCALER = -0.5;
	public static final Victor RIGHT_MOTOR = new Victor(1);
	public static final double RIGHT_MOTOR_SCALER = 0.5;
	
	// Shooter Motor
	
	public static final TalonSRX SHOOT_MOTOR = new TalonSRX(2);
	
	//  Intake Motor
	
	public static final Victor FEED_MOTOR = new Victor(3);
	public static final double FEED_MOTOR_SCALER = -1.00;
	
}
