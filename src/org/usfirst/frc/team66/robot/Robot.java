package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    int autoSelected;
	
    public static Autonomous AUTONOMOUS;
    public static Drivetrain DRIVETRAIN;
    public static Shooter SHOOTER;
    public static Intake INTAKE;
	public static Arm ARM;
	private static Camera CAMERA;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        SHOOTER = new Shooter();
        INTAKE = new Intake();
        ARM = new Arm();
        DRIVETRAIN = new Drivetrain();
        AUTONOMOUS = new Autonomous();
        CAMERA = new Camera();
        
        //TODO: Need to tie this to the limit switches, for now zero sensor on init
        ARM.zeroSensor();

        SmartDashboard.putNumber("Auto Mode", AUTONOMOUS.AUTON_MODE_DO_NOTHING);
    }
    public void disabledInit() {
        SmartDashboard.putNumber("Auto Mode", AUTONOMOUS.AUTON_MODE_DO_NOTHING);
    }
    
    public void disabledPeriodic() {
		autoSelected = (int) SmartDashboard.getNumber("Auto Mode", AUTONOMOUS.AUTON_MODE_DO_NOTHING);
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	//AUTONOMOUS.setAutonomousMode(AUTONOMOUS.AUTON_MODE_LOW_BAR);
    	autoSelected = (int) SmartDashboard.getNumber("Auto Mode", AUTONOMOUS.AUTON_MODE_DO_NOTHING);
    	AUTONOMOUS.setAutonomousMode(autoSelected);
    	DRIVETRAIN.GYRO.reset();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	AUTONOMOUS.updateAutonomous();
    	DRIVETRAIN.updateDrivetrainAuton();
    	ARM.updateArmAuton();
    }

    public void teleopInit(){
    	//TODO: Need to tie this to the limit switches, for now zero sensor on init
        //ARM.zeroSensor();
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	DRIVETRAIN.updateDrivetrainTeleop();
    	CAMERA.updateCamera();
        SHOOTER.updateShooter();
        INTAKE.updateIntake();
        ARM.updateArmTeleop();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
