package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class Shooter {
	static Joystick controller;
	static CANTalon shootMotor;
	static CANTalon shootMotorSlave;
	static double intakeSpeed = Constants.INTAKE_SPEED,
			shootSpeed = Constants.SHOOT_SPEED,
			slowEjectSpeed = Constants.SLOW_EJECT_SPEED;
	static int shootControlCount = 0;
	static boolean isShootingAuton = false, isIntakingAuton = false,
			isShooting = false;
	
	public Shooter() {
		Shooter.shootMotor = Constants.SHOOT_MOTOR;
		Shooter.shootMotorSlave = Constants.SHOOT_MOTOR_SLAVE;
		Shooter.controller = Constants.SHOOT_CONTROLLER;
		
		shootMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		
		shootMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		shootMotorSlave.set(shootMotor.getDeviceID());
		shootMotorSlave.reverseOutput(true);	
		
		shootMotor.enableBrakeMode(false);
		shootMotorSlave.enableBrakeMode(false);
	}
	
	// Tele-OP method
	public void updateShooter() {
		// disable auton shooting at beginning of teleop
		if(isShootingAuton) isShootingAuton = false;
		
		// Start shooting with right trigger
		if (controller.getRawAxis(2) >= 0.9) {
			isShooting = true;
		}
		
		// Shooting Timing Process
		if (isShooting) {
			if (shootControlCount < 200) {
				shootControlCount = shootControlCount++;
			} else {
	/* ******* // ADD BALL ACTUATION HERE!!!!! ********* */
				isShooting = false;
			}
		} else {
			shootControlCount = 0;
		}
		
		// Eject
		if (controller.getRawButton(1)) {
			shootMotor.set(slowEjectSpeed);
		} else {
			if (isShooting) { // spin up shooter
				shootMotor.set(shootSpeed);
			} else if (controller.getRawAxis(3) >= 0.9) { // intake
				// ********** ADD ROLLER **********
				shootMotor.set(intakeSpeed);
			} else { // stop
				shootMotor.set(0);
			}
		}
	}
	
	// Sets the shooter to full on, or turns it off. Toggle switch
	public void toggleShooter() {
		if (isShootingAuton) {
			shootMotor.set(0.0);
		} else {
			shootMotor.set(shootSpeed);
		}
		isShootingAuton = !isShootingAuton;
	}
	
	public void toggleIntake() {
		if (isIntakingAuton) {
			shootMotor.set(0.0);
			// ADD ROLLER
		} else {
			shootMotor.set(intakeSpeed);
			// ADD ROLLER
		}
		isIntakingAuton = !isIntakingAuton;
	}
	
	public void rollerUp() {
		// MAKE ROLLER MOVE UP
	}
	
	public void rollerDown() {
		// MAKE ROLLER MOVE DOWN
	}
}
