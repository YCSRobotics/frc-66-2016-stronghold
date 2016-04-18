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
				particles.add(par);
			}
			particles.sort(null);

			//This example only scores the largest particle. Extending to score all particles and choosing the desired one is left as an exercise
			//for the reader. Note that this scores and reports information about a single particle (single L shaped target). To get accurate information 
			//about the location of the tote (not just the distance) you will need to correlate two adjacent targets in order to find the true center of the tote.
			scores.Aspect = AspectScore(particles.elementAt(0));
			SmartDashboard.putNumber("Aspect", scores.Aspect);
			scores.Area = AreaScore(particles.elementAt(0));
			SmartDashboard.putNumber("Area", scores.Area);
			boolean isTote = scores.Aspect > SCORE_MIN && scores.Area > SCORE_MIN;
		} 
		else 
		{
			SmartDashboard.putBoolean("IsTote", false);
		}
	}
		
	//Comparator function for sorting particles. Returns true if particle 1 is larger
	static boolean CompareParticleSizes(ParticleReport particle1, ParticleReport particle2)
	{
		//we want descending sort order
		return particle1.PercentAreaToImageArea > particle2.PercentAreaToImageArea;
	}

	/**
	* Converts a ratio with ideal value of 1 to a score. The resulting function is piecewise
	* linear going from (0,0) to (1,100) to (2,0) and is 0 for all inputs outside the range 0-2
	*/
	double ratioToScore(double ratio)
	{
		return (Math.max(0, Math.min(100*(1-Math.abs(1-ratio)), 100)));
	}

	double AreaScore(ParticleReport report)
	{
		double boundingArea = (report.BoundingRectBottom - report.BoundingRectTop) * (report.BoundingRectRight - report.BoundingRectLeft);
		//Tape is 7" edge so 49" bounding rect. With 2" wide tape it covers 24" of the rect.
		return ratioToScore((49/24)*report.Area/boundingArea);
	}

	/**
	 * Method to score if the aspect ratio of the particle appears to match the retro-reflective target. Target is 7"x7" so aspect should be 1
	*/
	double AspectScore(ParticleReport report)
	{
		return ratioToScore(((report.BoundingRectRight-report.BoundingRectLeft)/(report.BoundingRectBottom-report.BoundingRectTop)));
	}

	/**
	 * Computes the estimated distance to a target using the width of the particle in the image. For more information and graphics
	 * showing the math behind this approach see the Vision Processing section of the ScreenStepsLive documentation.
	 *
	 * @param image The image to use for measuring the particle estimated rectangle
	 * @param report The Particle Analysis Report for the particle
	 * @param isLong Boolean indicating if the target is believed to be the long side of a tote
	 * @return The estimated distance to the target in feet.
	 */
	 double computeDistance (Image image, ParticleReport report) 
	 {
		 double normalizedWidth, targetWidth;
		 NIVision.GetImageSizeResult size;

		 size = NIVision.imaqGetImageSize(image);
		 normalizedWidth = 2*(report.BoundingRectRight - report.BoundingRectLeft)/size.width;
		 targetWidth = 7;

		return  targetWidth/(normalizedWidth*12*Math.tan(VIEW_ANGLE*Math.PI/(180*2)));
	}
	}

