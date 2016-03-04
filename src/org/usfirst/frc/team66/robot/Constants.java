package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Constants {
	
	public static final Preferences DASHBOARD_VALUES = Preferences.getInstance();
	
	// Controllers
	
	public static final Joystick DRIVE_CONTROLLER = new Joystick(0);
	public static final Joystick SHOOT_CONTROLLER = new Joystick(1);

	// Drivetrain Motors
	
	public static final Victor LEFT_MOTOR = new Victor(0);
	public static final double LEFT_MOTOR_SCALER = -0.65;
	public static final Victor RIGHT_MOTOR = new Victor(1);
	public static final double RIGHT_MOTOR_SCALER = 0.65;

	public static final Encoder LEFT_ENCODER = new Encoder(0, 1, false);
	public static final Encoder RIGHT_ENCODER = new Encoder(2, 3, true);
	public static final AnalogGyro GYRO = new AnalogGyro(0);
	
	public static final double SKIM_GAIN  = 1.0;
    public static final double TURN_GAIN  = 1.5;
	
	// Shooter Motor
	
	public static final CANTalon SHOOT_MOTOR = new CANTalon(0);
	public static final int SHOOT_ENCODER_COUNTS_PER_REV = 1024; // Need to change
	
	/******************************************************************************
	 * Calculate F term (from CTRE user manual)
	 * 5080 RPM at 100% output (determined empirically)
	 * 4096 native units per rev (4 pulses/cycle * 1024 cycles/rev = 4096)
	 * 5080 rev/min x 1 min/60 sec x 1 sec/10 Tvelmeas x 4096 native units/rev = 34979
	 * F=(100%*1023)/34679 = 0.0295
	 *******************************************************************************/
	public static final double SHOOT_PID_F = 0.025;
	
	//public static final double SHOOT_PID_P = 0.125; // Need to change
	public static final double SHOOT_PID_P = 0.114;
	
	public static final double SHOOT_PID_I = 0; // Need to change
	public static final double SHOOT_PID_D = 0; // Need to change
	
	public static final int SHOOT_PID_PROFILE = 0; // Uses in-code PID, not WebUI
	public static final double SHOOT_PEAK_VOLTAGE = 12; // Need to change
	public static final double SHOOT_NOMINAL_VOLTAGE = 0; // Need to change	
	
	// Intake Motor
	
	public static final Talon FEED_MOTOR = new Talon(3);
	public static final double FEED_MOTOR_SCALER = 1.00;
	
	// Arm Motor

	//public static final Talon ARM_MOTOR = new Talon(2);
	public static final CANTalon ARM_MOTOR_MASTER = new CANTalon(1);
	public static final CANTalon ARM_MOTOR_SLAVE = new CANTalon(2);
	
	public static final double ARM_CONTROLLER_UPPER_DEADZONE = 0.1;
	public static final double ARM_CONTROLLER_LOWER_DEADZONE =-0.1;
	public static final double ARM_MOTOR_SCALER_UP = 0.3;
	public static final double ARM_MOTOR_SCALER_DOWN = 0.1;
	
	public static final double ARM_PID_P = 0.25;
	public static final double ARM_PID_I = 0;
	public static final double ARM_PID_D = 0.002;
	
	public static final double ARM_LOAD_POSITION = 0.62;
	public static final double ARM_SHOOT_POSITION =0.5;
	public static final double ARM_UNLOAD_POSITION = 0.2;
	
}
