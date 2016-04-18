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
	private static boolean allowClosedLoop   = true;
	private static boolean isHoldingPosition = false;
	
	private static double targetPosition;
	private static double positionError;
	
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
		masterMotor.configNominalOutputVoltage(0, 0);
		masterMotor.configPeakOutputVoltage(3.0, -6.0);
		masterMotor.setP(Constants.ARM_PID_P_DEFAULT);
		masterMotor.setI(Constants.ARM_PID_I);
		masterMotor.setD(Constants.ARM_PID_D);
		masterMotor.setF(Constants.ARM_PID_F);
		
	}
	
	public void initArm()
	{
		//TODO
	}
	
	public void zeroSensor(){
		//Closed loop control based on relative position sensor, start at zero
		masterMotor.setPosition(0);
		isSensorZeroed = true;
		targetPosition = 0;
	}
	
	public static void enableClosedLoop(double setValue){
		if(allowClosedLoop){
			targetPosition = setValue;
			masterMotor.changeControlMode(CANTalon.TalonControlMode.Position);
			masterMotor.set(setValue);
			isHoldingPosition = true;
		}
		else
		{
			//Do Nothing
		}
		
	}
	
	public static void disableClosedLoop(){
		masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		masterMotor.set(0.0);
		allowClosedLoop = false;
		isHoldingPosition = false;
	}
	
	public void updateArmAuton(){
		
		if(masterMotor.isFwdLimitSwitchClosed()){
			allowClosedLoop = false;
			masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			masterMotor.set(0.0);
		}
		else
		{
			//Wait for user input to reenable PID
		}
		
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
		
		updateArmDashboard();
	}
	
	public void updateArmTeleop() {
		 double speed = shootController.getRawAxis(5);
		double gain;
		
		if(masterMotor.isRevLimitSwitchClosed())
		{
			zeroSensor();
		}
		
		if(masterMotor.isFwdLimitSwitchClosed()){
			allowClosedLoop = false;
			masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
			masterMotor.set(0.0);
		}
		else
		{
			//Wait for user input to reenable PID
		}
		
		gain = getMotorGain(speed);
		
		
		if(driveController.getRawAxis(3)>= 0.9){
			//Set load position of the arm
			targetPosition = Constants.ARM_LOAD_POSITION;
			enableClosedLoop(targetPosition);
		}
		else{
			//Handle manual operation of the arm
			if (speed >= Constants.ARM_CONTROLLER_UPPER_DEADZONE)
			{
				//Operator requesting manual movement of the arm, disable closed-loop control
				masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				isHoldingPosition = false;
				//speed = - (speed * speed) * Constants.ARM_MOTOR_SCALER_UP;
				masterMotor.set(-(speed*speed) * gain);
				allowClosedLoop = true;
			} 
			else if (speed <= Constants.ARM_CONTROLLER_LOWER_DEADZONE)
			{
				masterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
				isHoldingPosition = false;				//speed = (speed * speed) * Constants.ARM_MOTOR_SCALER_DOWN;
				masterMotor.set((speed*speed) * gain);
				allowClosedLoop = true;
			}
			else if(shootController.getRawButton(3))
			{
				//Set load position of the arm
				//targetPosition = Constants.ARM_LOAD_POSITION;
				allowClosedLoop = true;
				enableClosedLoop(Constants.ARM_LOW_POSITION);
			}
			else if (shootController.getRawButton(2))
			{
				//Set shoot position of the arm
				//targetPosition = Constants.ARM_HIGH_POSITION;
				allowClosedLoop = true;
				enableClosedLoop(Constants.ARM_HIGH_POSITION);
			} 
			else 
			{
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
		
		updateArmDashboard();
	}
	
	public static boolean isArmInPosition(){
		boolean isInPosition;
		
		positionError = (masterMotor.getPosition() - targetPosition);
		
		if(Math.abs(positionError) <= Constants.ARM_ERROR_THRESHOLD)
		{
			isInPosition = true;
		}
		else
		{
			isInPosition = false;
		}
		
		return isInPosition;
	}
	
	private double getMotorGain(double speedCommand)
	{
		double gain;
		double position;
		
		position = masterMotor.getPosition();
		
		if((position >= Constants.ARM_LOW_KNEE_POSITION)&&
		   (speedCommand >= Constants.ARM_CONTROLLER_UPPER_DEADZONE))
		{
			//Arm below knee point and being raised, use fast up gain
			gain = Constants.ARM_MOTOR_SCALER_UP_FAST;
		}
		else if ((position < Constants.ARM_LOW_KNEE_POSITION)&&
				 (speedCommand >= Constants.ARM_CONTROLLER_UPPER_DEADZONE))
		{
			//Arm raised above knee point and being raised, use slow up gain
			gain = Constants.ARM_MOTOR_SCALER_UP_SLOW;
		}
		else if ((position <= Constants.ARM_HIGH_KNEE_POSITION)&&
				 (speedCommand >= Constants.ARM_CONTROLLER_UPPER_DEADZONE))
		{
			//Arm raised above knee point and being lowered, use fast down gain
			gain = Constants.ARM_MOTOR_SCALER_FAST_DOWN;
		}
		else
		{
		    gain = Constants.ARM_MOTOR_SCALER_SLOW_DOWN;
		}
		
		return(gain);
	}

	public static double getArmPosition(){
		return masterMotor.getPosition();
	}
	
	public static boolean isArmFwdLimitActive(){
		return(masterMotor.isFwdLimitSwitchClosed());
	}
	
	private void updateArmDashboard(){
		SmartDashboard.putNumber("Arm Relative Position", masterMotor.getPosition());
		SmartDashboard.putNumber("Target Arm Position", targetPosition);
		SmartDashboard.putNumber("Arm Error", positionError);
		SmartDashboard.putNumber("Arm Motor Output", (masterMotor.getOutputVoltage()/masterMotor.getBusVoltage()));
		SmartDashboard.putBoolean("Holding Position", isHoldingPosition);
		SmartDashboard.putBoolean("Is Arm In Position", isArmInPosition());
		SmartDashboard.putBoolean("Lower Limit Active", masterMotor.isFwdLimitSwitchClosed());
		SmartDashboard.putBoolean("Upper Limit Active", masterMotor.isRevLimitSwitchClosed());
		SmartDashboard.putBoolean("PID Control Allowed", allowClosedLoop);
	}
	
}
