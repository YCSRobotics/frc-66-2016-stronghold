package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

public class Drivetrain {
	
	
	private static Joystick controller;
	
	private static Victor LEFT_MOTOR;
	private static double LEFT_MOTOR_SCALER;
	private static Victor RIGHT_MOTOR;
	private static double RIGHT_MOTOR_SCALER;
	
	private static Encoder LEFT_ENCODER;
	private static Encoder RIGHT_ENCODER;
	
	private static AnalogGyro GYRO;
	
	static DrivetrainSide leftSide;
	static DrivetrainSide rightSide;
	
	public static double fwdThrottle;
	public static double targetTurnRate;
	public static double targetLeftSpeed;
	public static double targetRightSpeed;
	
	public static double targetDistance;
	public static double targetAngle;
	
	public static boolean isMovingDistance = false;
	public static boolean isTurningDistance = false;
	
	public static boolean isGyroZeroed = false;
	
	public Drivetrain() {
		Drivetrain.controller = Constants.DRIVE_CONTROLLER;
		
		Drivetrain.LEFT_MOTOR = Constants.LEFT_MOTOR;
		Drivetrain.LEFT_MOTOR_SCALER = Constants.LEFT_MOTOR_SCALER;
		Drivetrain.RIGHT_MOTOR = Constants.RIGHT_MOTOR;
		Drivetrain.RIGHT_MOTOR_SCALER = Constants.RIGHT_MOTOR_SCALER;
		
		Drivetrain.LEFT_ENCODER = Constants.LEFT_ENCODER;
		LEFT_ENCODER.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_COUNT);
		
		Drivetrain.RIGHT_ENCODER = Constants.RIGHT_ENCODER;
		RIGHT_ENCODER.setDistancePerPulse(Constants.ENCODER_DISTANCE_PER_COUNT);
		
		Drivetrain.leftSide = new DrivetrainSide(LEFT_MOTOR, LEFT_MOTOR_SCALER);
		Drivetrain.rightSide = new DrivetrainSide(RIGHT_MOTOR, RIGHT_MOTOR_SCALER);
		
		Drivetrain.GYRO = Constants.GYRO;
	}
	
	public void updateDrivetrainAuton(){
		double distanceError;
		double turnError;
		
		if(isMovingDistance)
		{
			distanceError = targetDistance - getAverageDistance();
			
			if(Math.abs(distanceError) <= Constants.TARGET_DISTANCE_THRESHOLD){
				isMovingDistance = false;
				fwdThrottle = 0.0;
			}
			
			targetTurnRate = -1*(GYRO.getAngle()/10);
		}
		else if (isTurningDistance){
			turnError = targetAngle - GYRO.getAngle();
			fwdThrottle = 0.0;
			
			if(Math.abs(turnError) <= Constants.TARGET_ANGLE_THRESHOLD){
				//Inside turn threshold, so stop turning
				isTurningDistance = false;
    			targetTurnRate = 0.0;
			}
			else{
				//Not done turning, so turn rate is proportional to how far we are from target
				targetTurnRate = Constants.AUTON_TURN_GAIN * turnError;
				
				if(targetAngle > 0){
					targetTurnRate = Math.max(Constants.AUTON_MIN_TURN_RATE, targetTurnRate);
				}
				else{
					targetTurnRate = Math.min(-Constants.AUTON_MIN_TURN_RATE, targetTurnRate);
				}
			}	
		}
		
		setTargetSpeeds(fwdThrottle, targetTurnRate);
		
		leftSide.set(targetLeftSpeed);
		rightSide.set(targetRightSpeed);
		
		updateDrivetrainDashboard();
	}
	
	public void updateDrivetrainTeleop() {
		double driveGain;
		
		if(controller.getRawButton(5)){
			//Turbo Mode
			driveGain = Constants.TURBO_SCALER;
		}
		else
		{
			//Default to Finesse Mode
			driveGain = Constants.FINESSE_SCALER;
		}
		
		
		if (controller.getRawButton(6)) 
		{
			//Drive Straight Mode is active
			if(!isGyroZeroed){
				GYRO.reset();
				isGyroZeroed = true;
			}
			
			goStraight(driveGain);
		} 
		else 
		{
			isGyroZeroed = false;
				
			leftSide.set(controller.getRawAxis(1) * driveGain);
			rightSide.set(controller.getRawAxis(5) * driveGain);
		}
		
		updateDrivetrainDashboard();
		
	}
	
    public void setTargetSpeeds(double fwdThrottle, double turnRate)
    {
    	/*************************************************************************
    	 * This method calculates the necessary left and right motor outputs given
    	 * throttle and turn rates
    	 *************************************************************************/
    	double t_left;
    	double t_right;
    	
    	turnRate = turnRate * Constants.TURN_GAIN;
    	
    	t_left = fwdThrottle - turnRate;
    	t_right = fwdThrottle + turnRate;
    	
    	targetLeftSpeed = t_left + skim(t_right);
    	targetRightSpeed = t_right + skim(t_left);
    }
   
	
	private void goStraight(double SCALER) {
		double throttle;
		double turn;
    	
		if (Math.abs(controller.getRawAxis(1)) >= Math.abs(controller.getRawAxis(5))) {
			//leftSide.set(controller.getRawAxis(1) * SCALER);
			//rightSide.set(controller.getRawAxis(1) * SCALER);
			
			throttle = controller.getRawAxis(1) * SCALER;
			
		} else {
			//leftSide.set(controller.getRawAxis(5) * SCALER);
			//rightSide.set(controller.getRawAxis(5) * SCALER);
			
			throttle = controller.getRawAxis(5) * SCALER;
		}
		
		/* Initial angle zeroed when move distance was commanded, and we want to maintain 0 degree 
		 * heading, so turn rate is proportional to current angle.  Invert result so turn direction 
		 * is back toward 0 degrees.    
		 */
		turn = -1*(GYRO.getAngle()/10);
		
		setTargetSpeeds(throttle, turn);
		
		leftSide.set(targetLeftSpeed);
		rightSide.set(targetRightSpeed);
	}
	
	private double skim(double v){
        /* skim any speed value greater than 1 or less than -1 and apply a gain */ 
        if (v > 1.0){
            return -((v-1.0)*Constants.SKIM_GAIN);
        }
        else if (v <-1.0){
            return -((v+1.0)*Constants.SKIM_GAIN);
        }
        else{
            return 0;
        }
    }
	
	private double getAverageDistance(){
		double averageDistance;
		
		averageDistance = ((LEFT_ENCODER.getDistance() + RIGHT_ENCODER.getDistance())/2);
		return averageDistance;
	}
	
	public static void moveDistance(double distance, double speed){
		
		LEFT_ENCODER.reset();
		RIGHT_ENCODER.reset();
		
		targetDistance = distance;
		
		if(Math.abs(distance) > Constants.TARGET_DISTANCE_THRESHOLD){
			isMovingDistance = true;
			fwdThrottle = -speed;

		}
		else{
			isMovingDistance = false;
			fwdThrottle = 0;
		}
		
	}
	
	public static void turnDistance(double turnAngle){
		
		GYRO.reset();
    	
    	targetAngle = turnAngle;
    	
    	if(Math.abs(turnAngle) > Constants.TARGET_ANGLE_THRESHOLD)
    	{
    		isTurningDistance = true;
    	}
    	else
    	{
    		isTurningDistance = false;
    	}
	}
	
	private void updateDrivetrainDashboard(){
		
		SmartDashboard.putNumber("Left Encoder", LEFT_ENCODER.getDistance());
		SmartDashboard.putNumber("Right Encoder", RIGHT_ENCODER.getDistance());
		SmartDashboard.putNumber("Right Motor Output", RIGHT_MOTOR.getSpeed());
		SmartDashboard.putNumber("Left Motor Output", LEFT_MOTOR.getSpeed());
		SmartDashboard.putNumber("Gyro Angle", GYRO.getAngle());
		SmartDashboard.putNumber("Gyro Rate", GYRO.getRate());
		SmartDashboard.putBoolean("Is Moving Distance", isMovingDistance);
		SmartDashboard.putNumber("Target Distance", targetDistance);
	}
	
	public void initDrivetrain(){
		
	}
	
	public static boolean isMoveComplete(){
		return(!isMovingDistance);
	}
}
