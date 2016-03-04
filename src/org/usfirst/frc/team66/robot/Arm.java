package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
	public static CANTalon masterMotor;
	public static CANTalon slaveMotor;
	private static Joystick shootController;
	private static Joystick driveController;
	
	private boolean isSensorZeroed = false;
	private boolean isHoldingPosition = false;
	
	private double targetPosition;
	
	public Arm() {
		Arm.masterMotor = Constants.ARM_MOTOR_MASTER;
		Arm.slaveMotor = Constants.ARM_MOTOR_SLAVE;
		Arm.shootController = Constants.SHOOT_CONTROLLER;
		Arm.driveController = Constants.DRIVE_CONTROLLER;
		
		//Configure Master Motor
		masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		
		//Configure Feedback Device
		masterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		
		//Configure Slave Motor to mirror Master Motor output
		slaveMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		slaveMotor.set(masterMotor.getDeviceID());
		slaveMotor.reverseOutput(true);	
		
		//Configure Master Motor PID parameters.
		masterMotor.setP(Constants.ARM_PID_P);
		masterMotor.setI(Constants.ARM_PID_I);
		masterMotor.setD(Constants.ARM_PID_D);
	}
	
	public void zeroSensor(){
		//Closed loop control based on relative position sensor, start at zero
		masterMotor.setPosition(0);
		isSensorZeroed = true;
		targetPosition = 0;
	}
	
	public void enableClosedLoop(double setValue){
		masterMotor.changeControlMode(CANTalon.TalonControlMode.Position);
		masterMotor.set(setValue);
		isHoldingPosition = true;
	}
	
	public void updateArm() {
		double speed = shootController.getRawAxis(5);
		
		SmartDashboard.putNumber("Arm Relative Position", masterMotor.getPosition());
		SmartDashboard.putNumber("Target Arm Position", targetPosition);
		SmartDashboard.putNumber("Arm Motor Output", (masterMotor.getOutputVoltage()/masterMotor.getBusVoltage()));
		SmartDashboard.putBoolean("Holding Position", isHoldingPosition);
		
		if(driveController.getRawButton(6)){
			//Set load position of the arm
			targetPosition = Constants.ARM_LOAD_POSITION;
			enableClosedLoop(targetPosition);
		}
		else{
			//Handle manual operation of the arm
			if (speed >= Constants.ARM_CONTROLLER_UPPER_DEADZONE){
				//Operator requesting manual movement of the arm, disable closed-loop control
				masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				isHoldingPosition = false;
				//speed = - (speed * speed) * Constants.ARM_MOTOR_SCALER_UP;
				masterMotor.set(-(speed*speed) * Constants.ARM_MOTOR_SCALER_UP);
			}
			else if (speed <= Constants.ARM_CONTROLLER_LOWER_DEADZONE){
				masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				isHoldingPosition = false;
				//speed = (speed * speed) * Constants.ARM_MOTOR_SCALER_DOWN;
				masterMotor.set((speed*speed) * Constants.ARM_MOTOR_SCALER_DOWN);
			}
			else if(shootController.getRawButton(2)){
				//Set load position of the arm
				targetPosition = Constants.ARM_LOAD_POSITION;
				enableClosedLoop(targetPosition);
			}
			else if (shootController.getRawButton(3)){
				//Set unload position of the arm
				targetPosition = Constants.ARM_UNLOAD_POSITION;
				enableClosedLoop(targetPosition);
			}
			else if (shootController.getRawButton(4)){
				//Set shoot position of the arm
				targetPosition = Constants.ARM_SHOOT_POSITION;
				enableClosedLoop(targetPosition);
			}
			else{
				//No throttle input, and no button pressed
				if(isSensorZeroed){
					/**************************************************************************
					 * Check to see if already holding position. If not, throttle was just 
					 * released so enable closed loop control on current position
					 **************************************************************************/
					if(!isHoldingPosition){
						targetPosition = masterMotor.getPosition();
						enableClosedLoop(targetPosition);
					}
					else{
						//Do nothing
					}
					
				}
				else{
					//No throttle input but sensor has not yet been zeroed so cannot closed loop
					masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
					masterMotor.set(0.0);
				}
			}
		}
	}
	
}
