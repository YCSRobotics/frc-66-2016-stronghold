package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TalonSRX;

public class Shooter {
	static Joystick controller;
	static CANTalon shootMotor;
	
	public Shooter(Joystick controller, CANTalon motor) {
		Shooter.shootMotor = motor;
		Shooter.controller = controller;
		
		//shootMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    	//shootMotor.configEncoderCodesPerRev(Constants.SHOOT_ENCODER_COUNTS_PER_REV);
    	shootMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    	shootMotor.configNominalOutputVoltage(-Constants.SHOOT_NOMINAL_VOLTAGE, Constants.SHOOT_NOMINAL_VOLTAGE);
    	shootMotor.configPeakOutputVoltage(-Constants.SHOOT_PEAK_VOLTAGE, Constants.SHOOT_PEAK_VOLTAGE);
    	//shootMotor.setPID(Constants.SHOOT_PID_P, Constants.SHOOT_PID_I, Constants.SHOOT_PID_D);
	}
	
	public void updateShooter() {
		if (controller.getRawAxis(2) >= 0.9) {
			shootMotor.set(1.0);
		} else {
			shootMotor.set(0.0);
		}
	}
}
