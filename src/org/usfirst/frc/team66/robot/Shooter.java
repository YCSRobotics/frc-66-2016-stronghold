package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Shooter {
	static Joystick driveController, shootController;
	static CANTalon shootMotor;
	static CANTalon shootMotorSlave;
	static Solenoid shootSolenoid;
	static Talon feedMotor;
	static double intakeSpeed = Constants.INTAKE_SPEED,
			rollerSpeed = Constants.ROLLER_SPEED,
			shootSpeed = Constants.SHOOT_SPEED,
			slowEjectSpeed = Constants.SLOW_EJECT_SPEED;
	static int shootControlCount = 0;
	static boolean isShootingAuton = false, isIntakingAuton = false,
			isShooting = false;
	
	public Shooter() {
		Shooter.shootMotor = Constants.SHOOT_MOTOR;
		Shooter.shootMotorSlave = Constants.SHOOT_MOTOR_SLAVE;
		Shooter.shootController = Constants.SHOOT_CONTROLLER;
		Shooter.driveController = Constants.DRIVE_CONTROLLER;
		
		Shooter.feedMotor = Constants.FEED_MOTOR;
		
		Shooter.shootSolenoid = Constants.SHOOT_SOLENOID;
		
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
		if(isIntakingAuton) isIntakingAuton = false;
		
		// Start shooting with right trigger
		if (shootController.getRawAxis(3) >= 0.9) {
			isShooting = true;
		}
		
		// Shooting Timing Process
		if (isShooting) {
			if (shootControlCount < 200) {
				shootControlCount = shootControlCount++;
			} else {
				shootSolenoid.set(true);
				isShooting = false;
			}
		} else {
			shootControlCount = 0;
		}
		
		// Eject
		if (shootController.getRawButton(1)) {
			shootSolenoid.set(true);
			shootMotor.set(slowEjectSpeed);
		} else {
			if (isShooting) { // spin up shooter
				shootMotor.set(shootSpeed);
			} else if (driveController.getRawAxis(3) >= 0.9 || 
					(!(driveController.getRawAxis(3) >= 0.9) &&
							shootController.getRawAxis(2) >= 0.9)) { // intake
				feedMotor.set(rollerSpeed);
				shootMotor.set(intakeSpeed);
			} else { // stop
				shootMotor.set(0);
				shootSolenoid.set(false);
			}
		}
	}
	
	/* Auton Methods */
	
	// Sets the shooter to full on, or turns it off. Toggle switch
	public void toggleShooterAuton() {
		isShootingAuton = !isShootingAuton;
	}
	
	public void toggleIntakeAuton() {
		isIntakingAuton = !isIntakingAuton;
	}
	
	public void updateAuton() {
		if (isShootingAuton) {
			shootMotor.set(0.0);
		} else {
			shootMotor.set(shootSpeed);
		}
		
		if (isIntakingAuton) {
			shootMotor.set(0.0);
			// ADD ROLLER
		} else {
			shootMotor.set(intakeSpeed);
			// ADD ROLLER
		}
	}
}
