package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Victor;

public class Constants {
	// Drivetrain Motors
	public static final Joystick DRIVE_CONTROLLER = new Joystick(0);
	public static final Joystick SHOOT_CONTROLLER = new Joystick(1);
	
	public static final Victor LEFT_MOTOR = new Victor(0);
	public static final double LEFT_MOTOR_SCALER = -0.5;
	public static final Victor RIGHT_MOTOR = new Victor(1);
	public static final double RIGHT_MOTOR_SCALER = 0.5;
	
	public static final TalonSRX SHOOT_MOTOR = new TalonSRX(2);
	
	
}
