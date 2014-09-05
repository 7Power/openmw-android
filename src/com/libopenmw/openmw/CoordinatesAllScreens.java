package com.libopenmw.openmw;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;




public class CoordinatesAllScreens {
	
	public static	int height;
	public static	int width  ;
	private static CoordinatesAllScreens _instance = null;
	private final int STANDARD_WIDTH = 1024;
	private final int STANDARD_HEIGHT = 768;
	private float scaleRatio_y = 0;
	private float scaleRatio_x = 0;
	private int marginX = 0;
	private int marginY = 0;

	private CoordinatesAllScreens() {
	
		float x = (float) width/STANDARD_WIDTH;
		float y = (float) height / STANDARD_HEIGHT;
		scaleRatio_x =(float)	Math.max((float)  x,  (float)y);
		scaleRatio_y =(float)	Math.min((float)  x,  (float)y);
		marginX = (width - (int) ((float) STANDARD_WIDTH * scaleRatio_x)) / 2;
		marginY = (height - (int) ((float) STANDARD_HEIGHT * scaleRatio_y)) /2;
	}

	public static CoordinatesAllScreens getInstance() {
		if (_instance == null)
			_instance = new CoordinatesAllScreens();
		return _instance;
	}

	public int getScaledFontSize(int coordinate) {
		int result = (int) ((float) coordinate * scaleRatio_y);
		return result;
	}
	
	public int getScaledCoordinateX(int coordinate) {
		float scaled = (float) coordinate * scaleRatio_x;
		int result = marginX + (int) (scaled < 1 ? coordinate : scaled);
		return result;		
	}

	public int getScaledCoordinateY(int coordinate) {
		float scaled = (float) coordinate * scaleRatio_y;
		int result = marginY + (int) (scaled < 1 ? coordinate : scaled);
		return result;		
	}

	
}

