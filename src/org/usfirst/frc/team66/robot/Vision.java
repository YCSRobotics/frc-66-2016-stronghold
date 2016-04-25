package org.usfirst.frc.team66.robot;

import java.util.Comparator;
import java.util.Vector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.AxisCamera;

public class Vision {
	
	private static Joystick controller;
	
	int session;
	static Image rawFrame;
	static Image binaryFrame;
	static int imaqError;
	
	static AxisCamera axisCamera;
	
	static double distanceToVisionTarget;
	static double visionTargetXAxis;
	static boolean isTargetDetected;
	static int counter = 0;
	
	//A structure to hold measurements of a particle
	public static class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>
	{
		double PercentAreaToImageArea;
		double Area;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;
		double CenterOfMassX;
					
		public int compareTo(ParticleReport r)
		{
			return (int)(r.Area - this.Area);
		}
					
		public int compare(ParticleReport r1, ParticleReport r2)
		{
			return (int)(r1.Area - r2.Area);
		}
	};
		
	//Structure to represent the scores for the various tests used for target identification
	public static class Scores {
		double Area;
		double Aspect;
	};
		
	static double AREA_MINIMUM = 0.05; //Default Area minimum for particle as a percentage of total image area
	static double ASPECT_SCORE_MIN = 15.0;  //Minimum aspect score to be considered a target
	static double ASPECT_SCORE_MAX = 75.0;  //Minimum aspect score to be considered a target
	static double AREA_SCORE_MIN = 15.0;  //Minimum aspect score to be considered a target
	static double AREA_SCORE_MAX = 40.0;  //Minimum aspect score to be considered a target
	static double VIEW_ANGLE = 64; //View angle fo camera, set to Axis m1011 by default, 64 for m1013, 51.7 for 206, 52 for HD3000 square, 60 for HD3000 640x480
	static double IMAGE_WIDTH = 320;
	
	static NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	static NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
	static Scores scores = new Scores();
	
	//Constants
	static NIVision.Range TARGET_HUE_RANGE = new NIVision.Range(85, 135);	//Default hue range for yellow tote
	static NIVision.Range TARGET_SAT_RANGE = new NIVision.Range(125, 255);	//Default saturation range for yellow tote
	static NIVision.Range TARGET_VAL_RANGE = new NIVision.Range(60, 255);	//Default value range for yellow tote
	
	Vision(){
		Vision.controller = Constants.DRIVE_CONTROLLER;
		
		//Create images
		rawFrame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		binaryFrame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
		
		axisCamera = new AxisCamera("axis-camera.local");//Need camera IP
		
		criteria[0] = new NIVision.ParticleFilterCriteria2(NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA, AREA_MINIMUM, 100.0, 0, 0);
		
		SmartDashboard.putNumber("HUE Range Min",TARGET_HUE_RANGE.minValue);
		SmartDashboard.putNumber("HUE Range Max",TARGET_HUE_RANGE.maxValue);
		SmartDashboard.putNumber("SAT Range Min",TARGET_SAT_RANGE.minValue);
		SmartDashboard.putNumber("SAT Range Max",TARGET_SAT_RANGE.maxValue);
		SmartDashboard.putNumber("VAL Value Min",TARGET_VAL_RANGE.minValue);
		SmartDashboard.putNumber("VAL Value Max",TARGET_VAL_RANGE.maxValue);
	}
	
	public static void initCamera(){
		axisCamera.writeResolution(AxisCamera.Resolution.k320x240);
		axisCamera.writeRotation(AxisCamera.Rotation.k180);
		axisCamera.writeWhiteBalance(AxisCamera.WhiteBalance.kHold);
		axisCamera.writeExposureControl(AxisCamera.ExposureControl.kHold);
	}
	
	public static void updateFilterValues(){
		TARGET_HUE_RANGE.minValue = (int)SmartDashboard.getNumber("HUE Range Min",TARGET_HUE_RANGE.minValue);
		TARGET_HUE_RANGE.maxValue = (int)SmartDashboard.getNumber("HUE Range Max",TARGET_HUE_RANGE.maxValue);
		TARGET_SAT_RANGE.minValue = (int)SmartDashboard.getNumber("SAT Range Min",TARGET_SAT_RANGE.minValue);
		TARGET_SAT_RANGE.maxValue = (int)SmartDashboard.getNumber("SAT Range Max",TARGET_SAT_RANGE.maxValue);
		TARGET_VAL_RANGE.minValue = (int)SmartDashboard.getNumber("VAL Range Min",TARGET_VAL_RANGE.minValue);
		TARGET_VAL_RANGE.maxValue = (int)SmartDashboard.getNumber("VAL Range Max",TARGET_VAL_RANGE.maxValue);
	}
	
	public static boolean processImage(){
		if(counter >= 5){
		int numParticles = 0;
		
		//Set "Target Detected" and "Image Processed" flags to false initially in case image capture fails
		//boolean isTargetDetected = false;
		
		try{
			axisCamera.getImage(rawFrame);
		
			//Threshold the frame looking for green (reflected LED color)
			NIVision.imaqColorThreshold(binaryFrame, rawFrame, 255, NIVision.ColorMode.HSV, 
					TARGET_HUE_RANGE, TARGET_SAT_RANGE, TARGET_VAL_RANGE);
		
			//numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
			//SmartDashboard.putNumber("Masked particles", numParticles);
		
			CameraServer.getInstance().setImage(binaryFrame);

			//Send particle count after filtering to dashboard
			criteria[0].lower = (float)AREA_MINIMUM;
			imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, filterOptions, null);
		
			numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
			//SmartDashboard.putNumber("Filtered particles", numParticles);
		
			if(numParticles > 0)
			{
				//Measure particles and sort by particle size
				Vector<ParticleReport> particles = new Vector<ParticleReport>();
				for(int particleIndex = 0; particleIndex < numParticles; particleIndex++)
				{
					ParticleReport par = new ParticleReport();
					par.PercentAreaToImageArea = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA_BY_IMAGE_AREA);
					par.Area = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_AREA);
					par.BoundingRectTop = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_TOP);
					par.BoundingRectLeft = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_LEFT);
					par.BoundingRectBottom = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_BOTTOM);
					par.BoundingRectRight = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_BOUNDING_RECT_RIGHT);
					par.CenterOfMassX = NIVision.imaqMeasureParticle(binaryFrame, particleIndex, 0, NIVision.MeasurementType.MT_CENTER_OF_MASS_X);
					particles.add(par);
				}
				particles.sort(null);

				//This example only scores the largest particle. Extending to score all particles and choosing the desired one is left as an exercise
				//for the reader. Note that this scores and reports information about a single particle (single L shaped target). To get accurate information 
				//about the location of the tote (not just the distance) you will need to correlate two adjacent targets in order to find the true center of the tote.
				scores.Aspect = AspectScore(particles.elementAt(0));
				SmartDashboard.putNumber("Aspect Score", scores.Aspect);
				scores.Area = AreaScore(particles.elementAt(0));
				SmartDashboard.putNumber("Area Score", scores.Area);
				
				/*if (((scores.Aspect > ASPECT_SCORE_MIN)&&
					 (scores.Aspect < ASPECT_SCORE_MAX))&&
					((scores.Area > AREA_SCORE_MIN)&&
					((scores.Area < AREA_SCORE_MAX))))*/
				if ((scores.Aspect > ASPECT_SCORE_MIN)&&
					(scores.Area > AREA_SCORE_MIN))
				{
					isTargetDetected = true;
					distanceToVisionTarget = computeDistance(binaryFrame, particles.elementAt(0));
					visionTargetXAxis = particles.elementAt(0).CenterOfMassX;
				}
				else
				{
					isTargetDetected = false;
				}

				
			} 
			else 
			{
				//Image processed but no target candidates detected
				isTargetDetected = false;
			}
			
		}
		catch(Exception e)
		{
			
		}
		
		//Send target status, distance and target center to dashboard.
		//SmartDashboard.putBoolean("Is Target Detected", isTargetDetected);
		//SmartDashboard.putNumber("Distance", distanceToVisionTarget);
		//SmartDashboard.putNumber("CenterOfVisionTarget", visionTargetXAxis);
		//SmartDashboard.putNumber("Target Angle", getAngleToVisionTarget());
		counter = 0;
		}
		else
		{
			counter++;
		}
		return(isTargetDetected);
	}
	
	static double getDistanceToTarget(){
		return(distanceToVisionTarget);
	}
	
	static double getAngleToVisionTarget(){
		double offset;
		double degreesFromCenter;
		
		offset = ((getVisionTargetXAxis()+10) - (IMAGE_WIDTH/2));
		degreesFromCenter = offset * (VIEW_ANGLE/IMAGE_WIDTH);
		
		return(degreesFromCenter);
	}
	
	static double getVisionTargetXAxis(){
		return(visionTargetXAxis);
	}
	static double ratioToScore(double ratio)
	{
		return (Math.max(0, Math.min(100*(1-Math.abs(1-ratio)), 100)));
	}
	
	static double AreaScore(ParticleReport report)
	{
		double boundingArea = (report.BoundingRectBottom - report.BoundingRectTop) * (report.BoundingRectRight - report.BoundingRectLeft);
		//Tape is 13"x20" edge so 66" bounding rect. With 2" wide tape it covers 84"^2 of the rect.
		//SmartDashboard.putNumber("Bounding Area", boundingArea);
		//SmartDashboard.putNumber("TargetArea", report.Area);
		return ratioToScore((84/66)*report.Area/boundingArea);
	}

	/**
	 * Method to score if the aspect ratio of the particle appears to match the retro-reflective target. Target is 7"x7" so aspect should be 1
	*/
	static double  AspectScore(ParticleReport report)
	{
		return ratioToScore(((report.BoundingRectRight-report.BoundingRectLeft)/(report.BoundingRectBottom-report.BoundingRectTop)));
	}
	
	static double computeDistance (Image image, ParticleReport report) 
	{
		double normalizedWidth, targetWidth;
		NIVision.GetImageSizeResult size;

		size = NIVision.imaqGetImageSize(image);
		normalizedWidth = 2*(report.BoundingRectRight - report.BoundingRectLeft)/size.width;
		targetWidth = 20;
		
		//SmartDashboard.putNumber("Target Width", (report.BoundingRectRight - report.BoundingRectLeft));
		//SmartDashboard.putNumber("Target Height",(report.BoundingRectBottom - report.BoundingRectTop));
		//SmartDashboard.putNumber("Image Width", size.width);

		return  targetWidth/(normalizedWidth*12*Math.tan(VIEW_ANGLE*Math.PI/(180*2)));
	}

}
