package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class Shooter {
	static Joystick controller;
	static CANTalon shootMotor;
	static double speed;
	static boolean isShooting = false, isReleased = true;
	
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
			toggleShooter();
			isReleased = false;
		} else {
			isReleased = true;
		}
		
		if (controller.getRawButton(1)) {
			shootMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			shootMotor.set(-1.0);
		} else {
			if (!isShooting) {
				shootMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				shootMotor.set(0.0);
			} else {
				shootMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
				shootMotor.set(speed);
			}
		}
		
		//SmartDashboard.putNumber("Shooter Encoder Velocity: ", shootMotor.getEncVelocity());
		SmartDashboard.putNumber("Shooter Encoder Velocity: ", shootMotor.getSpeed());
		SmartDashboard.putNumber("Shooter Motor Output", shootMotor.getOutputVoltage()/shootMotor.getBusVoltage());
		SmartDashboard.putNumber("Shooter Error", shootMotor.getError());
	}
	
	public void toggleShooter() {
		if (isReleased) {
			if (isShooting) {
				shootMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				shootMotor.set(0.0);
			} else {
				shootMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
				shootMotor.set(speed);
			}
			isShooting = !isShooting;
		}
	}
}
