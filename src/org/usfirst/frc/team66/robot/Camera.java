package org.usfirst.frc.team66.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class Camera {
	private NetworkTable server = NetworkTable.getTable("SmartDashboard");
	
	//{p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y}
	private double[] getBoundingCoordinates() {
		double[] noTargetCoordinates = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
		
		try
		{
			return server.getNumberArray("BOUNDING_COORDINATES", noTargetCoordinates);
		}
		catch (TableKeyNotDefinedException ex)
		{
			return noTargetCoordinates;
		}
	}
	
	private double getTargetWidth() {
		double[] targetCoordinates = getBoundingCoordinates();
		return targetCoordinates[0] - targetCoordinates[2];
	}
	
	private double getTargetHeight() {
		double[] targetCoordinates = getBoundingCoordinates();
		return targetCoordinates[1] - targetCoordinates[5];
	}
	
	public boolean isInRange() {
		double minimumWidth = 0.0;
		double maximumWidth = 320.0;
		return (getTargetWidth() > minimumWidth && getTargetWidth() < maximumWidth);
	}
}
