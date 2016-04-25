package org.usfirst.frc.team66.robot;

import java.util.Comparator;
import java.util.Vector;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	
	static int frameCount = 6;
	
	public USBCamera(){
		try {	
			//create image
			frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
			
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
		catch(Exception e) {
			
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
			catch(Exception e) {
				
			}
			
			frameCount = 0;
		}
		else
		{
			frameCount++;
		}
	}
	
}

