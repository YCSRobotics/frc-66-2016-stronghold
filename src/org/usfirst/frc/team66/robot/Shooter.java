package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;

public class Shooter {
	static Joystick controller;
	static CANTalon shootMotorMaster;
	static CANTalon shootMotorSlave;
	static Solenoid shootPlunger;
	static double speed;
	static boolean isShooting = false, isReleased = true;
	static int plungerLoopCount;
	static boolean plungerState = false;
	static boolean isShootButtonReleased = false;
	
	public Shooter() {
		Shooter.shootMotorMaster = Constants.SHOOT_MOTOR_MASTER;
		Shooter.shootMotorSlave = Constants.SHOOT_MOTOR_SLAVE;
		Shooter.controller = Constants.SHOOT_CONTROLLER;
		Shooter.shootPlunger = Constants.SHOOT_PLUNGER;
		
		//Configure master motor (left)
		shootMotorMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		shootMotorMaster.enableBrakeMode(false);
		
		//Configure slave motor (right) to mirror master motor
		shootMotorSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		shootMotorSlave.set(shootMotorMaster.getDeviceID());
		shootMotorSlave.enableBrakeMode(false);
		shootMotorSlave.reverseOutput(true);
		
		
		/*try {
			shootMotor.configEncoderCodesPerRev(Constants.SHOOT_ENCODER_COUNTS_PER_REV);
			shootMotor.configNominalOutputVoltage(Constants.SHOOT_NOMINAL_VOLTAGE, -Constants.SHOOT_NOMINAL_VOLTAGE);
			shootMotor.configPeakOutputVoltage(Constants.SHOOT_PEAK_VOLTAGE, -Constants.SHOOT_PEAK_VOLTAGE);
			shootMotor.setP(Constants.SHOOT_PID_P);
			shootMotor.setI(Constants.SHOOT_PID_I);
			shootMotor.setD(Constants.SHOOT_PID_D);
			shootMotor.setF(Constants.SHOOT_PID_F);
			shootMotor.setProfile(Constants.SHOOT_PID_PROFILE);
		} catch(Error e) {
			
		}*/
	}
	
	public void updateShooter() {
		speed = Constants.DASHBOARD_VALUES.getDouble("Shoot Motor RPM", -0.52);
		
		/*if (controller.getRawAxis(2) >= 0.9) {
			toggleShooter();
			isReleased = false;
		} else {
			isReleased = true;
		}*/
		
		//Eject ball
		if (controller.getRawButton(6)) {
			shootMotorMaster.set(-0.5);
		} 
		//Intake ball
		else if (controller.getRawAxis(3) >= 0.9)
		{
			shootMotorMaster.set(0.75);
		}
		//Slow intake while raising arm
		else if (controller.getRawAxis(3) >= Constants.ARM_CONTROLLER_UPPER_DEADZONE)
		{
			shootMotorMaster.set(0.25);
		}
		else if(controller.getRawAxis(2) >= 0.9)
		{
			shootMotorMaster.set(speed);
		}
		else
		{
			shootMotorMaster.set(0.0);
		}
		
		if(plungerLoopCount <= Constants.PLUNGER_DELAY_COUNT)
		{
			plungerLoopCount++;
		}
		else
		{
			plungerState = false;
			shootPlunger.set(false);
		}
		
		if((controller.getRawButton(5)) &&
		   (isShootButtonReleased))
		{
			isShootButtonReleased = false;
			plungerLoopCount = 0;
			shootPlunger.set(true);
			plungerState = true;
			plungerLoopCount = 0;
		}
		else{
			if(plungerLoopCount >= Constants.PLUNGER_DELAY_COUNT){
				isShootButtonReleased = true;
			}
		}
		
		SmartDashboard.putNumber("Shooter Motor Output", shootMotorMaster.getOutputVoltage()/shootMotorMaster.getBusVoltage());
	}
	
	public void toggleShooter() {
		if (isReleased) {
			if (isShooting) {
				shootMotorMaster.set(0.0);
			} else {
				shootMotorMaster.set(speed);
			}
			isShooting = !isShooting;
		}
	}
	
}
