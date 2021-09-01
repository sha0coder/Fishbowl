package com.games.Base;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public  class Physics {

	
	// MANIPULACION DE IMAGENES
	
	public static Drawable rescale(Bitmap bmp, int scaleX, int scaleY) {
		Matrix matrix = new Matrix();
		matrix.preScale(scaleX,scaleY);
		return new BitmapDrawable(Bitmap.createBitmap(bmp, 0, 0, (int) bmp.getWidth(), (int)bmp.getHeight(), matrix, true));
	}
	
	public static Drawable rotateBMP(Bitmap bmp, float angle) {
    	Matrix matrix = new Matrix();
 
    	matrix.postScale(1,1);
    	matrix.postRotate(angle * -1);
    	
    	Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, (int) bmp.getWidth(), (int)bmp.getHeight(), matrix, true);
    	resizedBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
    	return  new BitmapDrawable(resizedBitmap);
    }
		
	public static Drawable shadowRotateBMP(Bitmap bmp, float angle) {
    	Matrix matrix = new Matrix();
    	
    	if (angle == 180)
    		matrix.postScale(-1, 1);
    	else if (angle == 180+90) {
    		matrix.postRotate(-1*(180+90));
    		matrix.postScale(-1, 1);
    	} else if (angle == 180+45) {
    		matrix.postRotate(-1*(90+45));
    		matrix.postScale(1, -1);
    	} else if (angle == 90+45){
    		matrix.postRotate(-45);
    		matrix.postScale(-1, 1);
    	} else {
    		matrix.postScale(1,1);
    		matrix.postRotate(angle * -1);
    	}
    	
    	Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, (int) bmp.getWidth(), (int)bmp.getHeight(), matrix, true);
    	resizedBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
    	return  new BitmapDrawable(resizedBitmap);
    }
	
	public static Drawable flipVBMP(Bitmap bmp) {
		Matrix mirrorMatrix = new Matrix();
		mirrorMatrix.preScale(1, -1);
		return new BitmapDrawable(Bitmap.createBitmap(bmp, 0, 0, (int) bmp.getWidth(), (int)bmp.getHeight(), mirrorMatrix, true));
	}
	
	public static Drawable flipHBMP(Bitmap bmp) {
		Matrix mirrorMatrix = new Matrix();
		mirrorMatrix.preScale(-1, 1);
		return new BitmapDrawable(Bitmap.createBitmap(bmp, 0, 0, (int) bmp.getWidth(), (int)bmp.getHeight(), mirrorMatrix, true));
	}
	
	public static Drawable flipBMP(Bitmap bmp) {
		Matrix mirrorMatrix = new Matrix();
		mirrorMatrix.preScale(-1, -1);
		return new BitmapDrawable(Bitmap.createBitmap(bmp, 0, 0, (int) bmp.getWidth(), (int)bmp.getHeight(), mirrorMatrix, true));
	}
	
	public static Drawable scaleBMP(Bitmap bmp, int w, int h) {
        return new BitmapDrawable(Bitmap.createScaledBitmap(bmp, w, h, true));
	}
	
	//POSICIONAMIENTO RELATIVO
	
	public static double centerXToScreen(double width, double canvasW) {
    	return (canvasW/2-width/2);
    }
	
	public double centerYoScreen(double height, double canvasH) {
    	return canvasH/2-height/2;
    }
	
	
	

}
