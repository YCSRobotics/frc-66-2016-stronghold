package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	
	//Auton Modes
	public static final int AUTON_MODE_DO_NOTHING           = 0;
	public static final int AUTON_MODE_FORWARD_SLOW         = 1;
	public static final int AUTON_MODE_FORWARD_FAST         = 2;
	public static final int AUTON_MODE_REVERSE_SLOW         = 3;
	public static final int AUTON_MODE_REVERSE_FAST         = 4;
	public static final int AUTON_MODE_LOW_BAR              = 5;
	public static final int AUTON_MODE_SHORT				= 6;

	
	//Auton States
	private static final int AUTON_STATE_START                = 0;
	private static final int AUTON_STATE_LOWER_ARM            = 1;
	private static final int AUTON_STATE_MOVE_DISTANCE        = 2;
	private static final int AUTON_STATE_MOVE_ARM_TO_POSITION = 3;
	private static final int AUTON_STATE_MOVE_AND_LOWER_ARM   = 4;
	private static final int AUTON_STATE_TURN_DISTANCE        = 5;
	private static final int AUTON_STATE_STOP                 = 6;
	

	private static int currentAutoState;
	private static int selectedAutoMode;
	
	public Autonomous(){
		//Initialize selected mode to do nothing
		selectedAutoMode = AUTON_MODE_DO_NOTHING;
		
		//Initialize autonomous state to the "Start" state
		currentAutoState = AUTON_STATE_START;
	}
	
	public void updateAutonomous(){
		switch(currentAutoState) {
		case AUTON_STATE_START:
			stateActionStart();
			break;
			
		case AUTON_STATE_LOWER_ARM:
			stateActionLowerArm();
			break;
			
		case AUTON_STATE_MOVE_DISTANCE:
			stateActionMoveDistance();
			break;
			
		case AUTON_STATE_MOVE_ARM_TO_POSITION:
			stateActionMoveArmToPosition();
			break;
			
		case AUTON_STATE_MOVE_AND_LOWER_ARM:
			stateActionMoveAndLowerArm();
			break;
			
		case AUTON_STATE_TURN_DISTANCE:
			stateActionTurnAngle();
			break;
			
		case AUTON_STATE_STOP:
			stateActionStop();
			break;
		}
		
		updateAutonDashboard();	
	}
	
	public void setAutonomousMode(int mode){
		//Function provided to disabled init/disabled periodic to set auton mode from smart dashboard
		selectedAutoMode = mode;
	}
	
	private void stateActionStop(){
		Drivetrain.moveDistance(0, 0);
	}
	
	private void stateActionStart()
	{
		if(selectedAutoMode != AUTON_MODE_DO_NOTHING)
		{
			if(selectedAutoMode == AUTON_MODE_LOW_BAR)
			{
				//Lower arm before moving
				Arm.enableClosedLoop(Constants.ARM_LOAD_POSITION);
				currentAutoState = AUTON_STATE_LOWER_ARM;
				//currentAutoState = AUTON_STATE_STOP;
			}
			else if( (selectedAutoMode == AUTON_MODE_FORWARD_SLOW) ||
					 (selectedAutoMode == AUTON_MODE_FORWARD_FAST))
			{
				Arm.enableClosedLoop(Constants.ARM_LOW_POSITION);
				currentAutoState = AUTON_STATE_MOVE_ARM_TO_POSITION;
			}
			else if((selectedAutoMode == AUTON_MODE_REVERSE_SLOW) ||
					(selectedAutoMode == AUTON_MODE_REVERSE_FAST))
			{
				//Start lowering arm while moving in reverse
				Arm.enableClosedLoop(Constants.ARM_LOAD_POSITION);
				Drivetrain.moveDistance(-12.0, -0.5);
				currentAutoState = AUTON_STATE_MOVE_AND_LOWER_ARM;
			}
			else if(selectedAutoMode == AUTON_MODE_SHORT)
			{
				Drivetrain.moveDistance(60.0, 0.5);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
		} 
		else 
		{
			currentAutoState = AUTON_STATE_STOP;
		}
			
	}
	
	private void stateActionLowerArm()
	{
		if(Arm.isArmFwdLimitActive()){
			if(selectedAutoMode == AUTON_MODE_LOW_BAR){
				Drivetrain.moveDistance(150.0, 0.6);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
			else{
				//Should never get here, but protect if we do
				currentAutoState = AUTON_STATE_STOP;
			}
		}
		else{
			//Wait for arm to be lowered
		}
	}
	
	private void stateActionMoveArmToPosition()
	{
		if(Arm.isArmInPosition())
		{
			if (selectedAutoMode == AUTON_MODE_FORWARD_SLOW)
			{
				Drivetrain.moveDistance(150.0, 0.6);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
			else if (selectedAutoMode == AUTON_MODE_FORWARD_FAST)
			{
				Drivetrain.moveDistance(150.0, 1.0);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
			
			else
			{
				//Should never get here, but protect if we do
				currentAutoState = AUTON_STATE_STOP;
			}
		} 
		else 
		{
			//Wait for arm to be lowered
		}
	}
	
	private void stateActionMoveAndLowerArm()
	{
		if((Drivetrain.isMoveComplete()) &&
		   (Arm.isArmFwdLimitActive()))
		{
			if (selectedAutoMode == AUTON_MODE_REVERSE_SLOW)
			{
				Drivetrain.moveDistance(-130.0, -0.6);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
			else if (selectedAutoMode == AUTON_MODE_REVERSE_FAST)
			{
				Drivetrain.moveDistance(-130.0, -1.0);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
		}
		else
		{
			//Wait in this state
		}
	}
	
	private void stateActionMoveDistance(){
		if(Drivetrain.isMoveComplete()){
			if((selectedAutoMode == AUTON_MODE_FORWARD_SLOW) ||
			   (selectedAutoMode == AUTON_MODE_FORWARD_FAST) ||
			   (selectedAutoMode == AUTON_MODE_REVERSE_SLOW) ||
			   (selectedAutoMode == AUTON_MODE_REVERSE_FAST))
			{
				currentAutoState = AUTON_STATE_STOP;
			}
			else if(selectedAutoMode == AUTON_MODE_LOW_BAR)
			{
				Drivetrain.turnDistance(35);
				currentAutoState = AUTON_STATE_TURN_DISTANCE;
			}
			else
			{
				//Should never get here, but protect if we do
				currentAutoState = AUTON_STATE_STOP;
			}
			
		} else {
			//Wait for move to complete
		}
	}
	
	private void stateActionTurnAngle(){
		if(Drivetrain.isTurnComplete())
		{
			currentAutoState = AUTON_STATE_STOP;
		} else {
			//Wait for move to complete
		}
	}
	
	public void updateAutonDashboard(){
		SmartDashboard.putNumber("Auton State", currentAutoState);
	}
	
}
