package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Autonomous {
	
	//Auton Modes
	public static final int AUTON_MODE_DO_NOTHING      = 0;
	public static final int AUTON_MODE_PASSIVE_DEFENSE_SLOW = 1;
	public static final int AUTON_MODE_PASSIVE_DEFENSE_FAST = 2;
	public static final int AUTON_MODE_LOW_BAR              = 3;
	
	//Auton States
	private static final int AUTON_STATE_START         = 0;
	private static final int AUTON_STATE_LOWER_ARM     = 1;
	private static final int AUTON_STATE_MOVE_DISTANCE = 2;
	private static final int AUTON_STATE_STOP          = 3;

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
		Arm.disableClosedLoop();
		Drivetrain.moveDistance(0, 0);
	}
	
	private void stateActionStart()
	{
		if(selectedAutoMode != AUTON_MODE_DO_NOTHING){
			if(selectedAutoMode == AUTON_MODE_LOW_BAR)
			{
				Arm.enableClosedLoop(Constants.ARM_LOAD_POSITION);
				currentAutoState = AUTON_STATE_LOWER_ARM;
				//currentAutoState = AUTON_STATE_STOP;
			}
			else if(selectedAutoMode == AUTON_MODE_PASSIVE_DEFENSE_SLOW)
			{
				Drivetrain.moveDistance(-150.0, 0.8);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
			else if(selectedAutoMode == AUTON_MODE_PASSIVE_DEFENSE_FAST)
			{
				Drivetrain.moveDistance(-150.0, 1.0);
				currentAutoState = AUTON_STATE_MOVE_DISTANCE;
			}
		} 
		else 
		{
			currentAutoState = AUTON_STATE_STOP;
		}
			
	}
	
	private void stateActionLowerArm(){
		
		if(Arm.isArmInPosition()){
			//Arm.disableClosedLoop();
			Drivetrain.moveDistance(-150.0, 0.6);
			currentAutoState = AUTON_STATE_MOVE_DISTANCE;
		} else {
			//Wait for arm to be lowered
		}
	}
	
	private void stateActionMoveDistance(){
		if(Drivetrain.isMoveComplete()){
			currentAutoState = AUTON_STATE_STOP;
		} else {
			//Wait for move to complete
		}
	}
	
	public void updateAutonDashboard(){
		SmartDashboard.putNumber("Auton State", currentAutoState);
	}
	
}
