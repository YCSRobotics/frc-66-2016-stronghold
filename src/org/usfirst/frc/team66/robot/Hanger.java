package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;

public class Hanger {
	
	public static Talon winchMotor;
	public static Encoder winchEncoder;
	public static Solenoid hangerRelease;
	private static Joystick shootController;
	
	private static boolean isHangerReleased = false;
	
	public Hanger(){
		Hanger.winchMotor = Constants.WINCH_MOTOR;
		
		Hanger.winchEncoder = Constants.WINCH_ENCODER;
		winchEncoder.setDistancePerPulse(Constants.WINCH_DISTANCE_PER_PULSE);
		winchEncoder.setReverseDirection(Constants.INVERT_WINCH_DIRECTION);
		
	}
	
	public void updateHanger(){
		double winchPosition = winchEncoder.getDistance(); 
		
		//Release hanger hooks if arm raised high enough and release button pressed
		if((shootController.getRawButton(button)) &&
		   (Arm.getArmPosition() < Constants.HANGER_RELEASE_THRESHOLD)){
			hangerRelease.set(true);
			isHangerReleased = true;
		}
		else{
			hangerRelease.set(false);
		}
		
		if((shootController.getRawButton(button)) &&
		   (isHangerReleased))
		{
			Hanger.winchMotor.set(1.0);
			Arm.enableClosedLoop(lookupArmPosition(winchPosition));
		}
		   
	}

	public double lookupArmPosition(double position){
		int index;
		int tableSize = Constants.ARM_POSITION_LOOKUP_TABLE.length;
		
		//round position to the closest integer
		index = (int)Math.round(position);

		//Make sure we don't index outside of the array!
		if(index < 0){
			index = 0;
		}
		else if(index >= Constants.ARM_POSITION_LOOKUP_TABLE.length)
		{
			index = tableSize-1;
		}
		
		return(Constants.ARM_POSITION_LOOKUP_TABLE[index]);
	}
}
