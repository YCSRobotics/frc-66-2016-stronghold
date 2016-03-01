package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

public class Arm {
	public static CANTalon masterMotor;
	public static CANTalon slaveMotor;
	private static Joystick controller;
	
	public static double ARM_MOTOR_SCALER;
	
	public Arm() {
		Arm.masterMotor = Constants.ARM_MOTOR_MASTER;
		Arm.slaveMotor = Constants.ARM_MOTOR_SLAVE;
		Arm.controller = Constants.SHOOT_CONTROLLER;
		
		Arm.ARM_MOTOR_SCALER = Constants.ARM_MOTOR_SCALER_DOWN;
		
		masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		slaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		slaveMotor.set(masterMotor.getDeviceID());
		slaveMotor.reverseOutput(true);
		
	}
	
	public void updateArm() {
		double speed = controller.getRawAxis(5);
		
		if (speed >= 0) {
			speed = - (speed * speed) * Constants.ARM_MOTOR_SCALER_UP;
		} else {
			speed = (speed * speed) * Constants.ARM_MOTOR_SCALER_DOWN;
		}
		
		if (Math.abs(speed) >= 0.1) {
			masterMotor.set(speed);
		} else {
			masterMotor.set(0);
		}
	}
	
}
