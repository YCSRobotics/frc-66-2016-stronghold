package org.usfirst.frc.team66.robot;

<<<<<<< HEAD
import edu.wpi.first.wpilibj.CANTalon;
=======
import edu.wpi.first.wpilibj.Encoder;
>>>>>>> Drivetrain
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

	public static final Encoder LEFT_ENCODER = new Encoder(0, 0, false);
	public static final Encoder RIGHT_ENCODER = new Encoder(1, 1, true);
	
	public static final CANTalon SHOOT_MOTOR = new CANTalon(0);
	public static final int SHOOT_ENCODER_COUNTS_PER_REV = 1024; // Need to change
	public static final double SHOOT_PID_P = 0.125; // Need to change
	public static final double SHOOT_PID_I = 0; // Need to change
	public static final double SHOOT_PID_D = 0; // Need to change
	public static final double SHOOT_PEAK_VOLTAGE = 12; // Need to change
	public static final double SHOOT_NOMINAL_VOLTAGE = 0; // Need to change	
}
