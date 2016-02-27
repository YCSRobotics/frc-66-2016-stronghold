package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;

public class Shooter {
	static Joystick controller;
	static CANTalon shootMotor;
	static double speed;
	
	public Shooter() {
		Shooter.shootMotor = Constants.SHOOT_MOTOR;
		Shooter.controller = Constants.SHOOT_CONTROLLER;
		
		speed = Constants.DASHBOARD_VALUES.getDouble("Shoot Motor Speed", 1.0);
		
		shootMotor.enableBrakeMode(true);
		shootMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	shootMotor.configEncoderCodesPerRev(Constants.SHOOT_ENCODER_COUNTS_PER_REV);
    	shootMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	shootMotor.configNominalOutputVoltage(-Constants.SHOOT_NOMINAL_VOLTAGE, Constants.SHOOT_NOMINAL_VOLTAGE);
    	shootMotor.configPeakOutputVoltage(-Constants.SHOOT_PEAK_VOLTAGE, Constants.SHOOT_PEAK_VOLTAGE);
    	shootMotor.setPID(Constants.SHOOT_PID_P, Constants.SHOOT_PID_I, Constants.SHOOT_PID_D, Constants.SHOOT_PID_F,
    			Constants.SHOOT_PID_IZONE, Constants.SHOOT_PID_CLOSE_LOOP_RAMP_RATE, Constants.SHOOT_PID_PROFILE);
	}
	
	public void updateShooter() {
		if (controller.getRawAxis(2) >= 0.9) {
			shootMotor.set(speed);
		} else {
			shootMotor.set(0.0);
		}
		
		SmartDashboard.putNumber("Shooter Encoder Velocity: ", shootMotor.getEncVelocity());
	}
}
