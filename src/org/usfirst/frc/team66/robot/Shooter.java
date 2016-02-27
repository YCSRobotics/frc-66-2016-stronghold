package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class Shooter {
	static Joystick controller;
	static CANTalon shootMotor;
	static double speed;
	
	public Shooter() {
		Shooter.shootMotor = Constants.SHOOT_MOTOR;
		Shooter.controller = Constants.SHOOT_CONTROLLER;
		
		shootMotor.enableBrakeMode(false);
		shootMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
    	shootMotor.configEncoderCodesPerRev(Constants.SHOOT_ENCODER_COUNTS_PER_REV);
    	shootMotor.configNominalOutputVoltage(Constants.SHOOT_NOMINAL_VOLTAGE, -Constants.SHOOT_NOMINAL_VOLTAGE);
    	shootMotor.configPeakOutputVoltage(Constants.SHOOT_PEAK_VOLTAGE, -Constants.SHOOT_PEAK_VOLTAGE);
    	shootMotor.setP(Constants.SHOOT_PID_P);
    	shootMotor.setI(Constants.SHOOT_PID_I);
    	shootMotor.setD(Constants.SHOOT_PID_D);
    	shootMotor.setF(Constants.SHOOT_PID_F);
    	shootMotor.setProfile(Constants.SHOOT_PID_PROFILE);
	}
	
	public void updateShooter() {
		speed = Constants.DASHBOARD_VALUES.getDouble("Shoot Motor RPM", 5340);
		if (controller.getRawAxis(2) >= 0.9) {
			shootMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
			shootMotor.set(speed);
		} else {
			shootMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			shootMotor.set(0.0);
		}
		
		SmartDashboard.putNumber("Shooter Encoder Velocity: ", shootMotor.getEncVelocity());
	}
}
