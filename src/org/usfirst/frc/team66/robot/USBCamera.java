package org.usfirst.frc.team66.robot;

import java.util.Comparator;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;

public class USBCamera {
	
	static Image frame;
	static Image binaryFrame;
	static int imaqError;
	
	static int currentSession;
	static int sessionArm;
	
	static int centerPointX = 320;
	static int centerPointY = 240;
	
	static NIVision.Rect rect;
	static NIVision.Point vertStartPoint;
	static NIVision.Point vertEndPoint;
	static NIVision.Point horzStartPoint;
	static NIVision.Point horzEndPoint;
	
	//A structure to hold measurements of a particle
	public class ParticleReport implements Comparator<ParticleReport>, Comparable<ParticleReport>{
		double PercentAreaToImageArea;
		double Area;
		double BoundingRectLeft;
		double BoundingRectTop;
		double BoundingRectRight;
		double BoundingRectBottom;
				
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
	public class Scores {
		double Area;
		double Aspect;
	};
	
	//Constants
	NIVision.Range TARGET_HUE_RANGE = new NIVision.Range(101, 64);	//Default hue range for green retro target
	NIVision.Range TARGET_SAT_RANGE = new NIVision.Range(88, 255);	//Default saturation range for green retro target
	NIVision.Range TARGET_VAL_RANGE = new NIVision.Range(134, 255);	//Default value range for green retro target
	
	double AREA_MINIMUM = 0.5; //Default Area minimum for particle as a percentage of total image area
	double LONG_RATIO = 2.22; //Tote long side = 26.9 / Tote height = 12.1 = 2.22
	double SHORT_RATIO = 1.4; //Tote short side = 16.9 / Tote height = 12.1 = 1.4
	double SCORE_MIN = 75.0;  //Minimum score to be considered a tote
	double VIEW_ANGLE = 49.4; //View angle fo camera, set to Axis m1011 by default, 64 for m1013, 51.7 for 206, 52 for HD3000 square, 60 for HD3000 640x480
	NIVision.ParticleFilterCriteria2 criteria[] = new NIVision.ParticleFilterCriteria2[1];
	NIVision.ParticleFilterOptions2 filterOptions = new NIVision.ParticleFilterOptions2(0,0,1,1);
	Scores scores = new Scores();
	
	static int frameCount = 6;
	
	public USBCamera(){
		try {	
			//create image
			frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
			binaryFrame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_U8, 0);
			
			sessionArm = NIVision.IMAQdxOpenCamera(Constants.CAMERA_ARM_NAME, NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			currentSession = sessionArm;
			NIVision.IMAQdxConfigureGrab(currentSession);
		
		
			//Setup overlay
			rect = new NIVision.Rect((centerPointY-100),(centerPointX-100),200,200);
			vertStartPoint = new NIVision.Point(centerPointX, (centerPointY-110));
			vertEndPoint = new NIVision.Point(centerPointX, (centerPointY+110));
			horzStartPoint = new NIVision.Point((centerPointX-110), centerPointY);
			horzEndPoint = new NIVision.Point((centerPointX+110), centerPointY);
		}
		catch(Error e) {
			
		}
	}
	
	public void updateUsbCamera(){
		
		if(frameCount >= 6)
		{
			try {
				NIVision.IMAQdxGrab(currentSession, frame, 1);
        
				NIVision.imaqDrawShapeOnImage(frame, frame, rect,
					DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 255.0f); 
				NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, 
					vertStartPoint, vertEndPoint, 255.0f);
				NIVision.imaqDrawLineOnImage(frame, frame, DrawMode.DRAW_VALUE, 
					horzStartPoint, horzEndPoint, 255.0f);
        
				CameraServer.getInstance().setImage(frame);
			}
			catch(Error e) {
				
			}
			
			frameCount = 0;
		}
		else
		{
			frameCount++;
		}
	}
	
	public void processUsbImage(){
		int numParticles;
		//Threshold the frame looking for green (reflected LED color)
		NIVision.imaqColorThreshold(binaryFrame, frame, 255, NIVision.ColorMode.HSV, 
				TARGET_HUE_RANGE, TARGET_SAT_RANGE, TARGET_HUE_RANGE);
		
		//Filter small particles
		criteria[0].lower = (float)AREA_MINIMUM;
		imaqError = NIVision.imaqParticleFilter4(binaryFrame, binaryFrame, criteria, filterOptions, null);
		
		//Count detected particles
		numParticles = NIVision.imaqCountParticles(binaryFrame, 1);
		
		//
		
	}
	

}
