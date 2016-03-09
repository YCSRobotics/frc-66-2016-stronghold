package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera {
	private NetworkTable table;
	private double imageHeight;
	private double imageWidth;
	private double cameraAngle;   // radians
	private double vertFOV;       // vert fov in radians
	private double targetHeight; 
	
	public Camera() {
		table = NetworkTable.getTable("SmartDashboard");
		imageHeight = table.getNumber("IMAGE_HEIGHT", 480);
		imageWidth= table.getNumber("IMAGE_WIDTH", 640);
		cameraAngle = 0.0;
		vertFOV = Math.toRadians(35.5);  
		targetHeight = 13.5;
	}
	
	public void updateCamera() {
		SmartDashboard.putString("Bounding Coordinates", boundingToString());
		SmartDashboard.putString("Distance", distanceToTarget()+"");
	}
	
	// {p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y}
	// top left, top right, bottom right, bottom left
	private double[] getBoundingCoordinates() {
		double[] noTargetCoordinates = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		
		try
		{
			return table.getNumberArray("BOUNDING_COORDINATES", noTargetCoordinates);
		}
		catch (TableKeyNotDefinedException ex)
		{
			return noTargetCoordinates;
		}
	}
	
	public double distanceToTarget(){
		double[] coordsArray = getBoundingCoordinates();
		if(coordsArray.length < 8){
			return 0.0;
		}
		double py = Math.max(coordsArray[1], coordsArray[3]);
		double dtheta = vertFOV / imageHeight;
		double theta = cameraAngle + (dtheta * (py - imageHeight/2));
		double dist = targetHeight / Math.tan(theta);
		if(dist < 0.0){
			return 0.0;
		}
		return dist;
	}
	
	public String boundingToString() {
		double[] coordsArray = getBoundingCoordinates();
		if(coordsArray.length < 8) {
			return "NO TARGET";
		}
		String output = "";
		output = output + "(" + coordsArray[0] + ", " + coordsArray[1] + "), ";
		output = output + "(" + coordsArray[2] + ", " + coordsArray[3] + "), ";
		output = output + "(" + coordsArray[4] + ", " + coordsArray[5] + "), ";
		output = output + "(" + coordsArray[6] + ", " + coordsArray[7] + ")";
		return output;
	}
	
	private double getTargetWidth() {
		double[] targetCoordinates = getBoundingCoordinates();
		return targetCoordinates[0] - targetCoordinates[2];
	}
	
	private double getTargetHeight() {
		double[] targetCoordinates = getBoundingCoordinates();
		return targetCoordinates[1] - targetCoordinates[5];
	}
	
	public boolean isTargetInRange() {
		double minimumWidth = 0.0;
		double maximumWidth = 320.0;
		return (getTargetWidth() > minimumWidth && getTargetWidth() < maximumWidth);
	}
}
