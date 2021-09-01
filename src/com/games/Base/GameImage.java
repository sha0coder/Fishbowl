package com.games.Base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

abstract public class GameImage {
	protected Context ctx;
	public int canvasWidth = 0;
	public int canvasHeight = 0;
	public int canvasCenterWidth = 0;
	public int canvasCenterHeight = 0;
	
	public void setCanvas(int width, int height) {
		canvasWidth = width;
		canvasHeight = height;
		canvasCenterWidth = width/2;
		canvasCenterHeight = height/2;
	}
	
	
	
	public Bitmap bg = null;
	
	public GameImage(Context ctx) {
		this.ctx = ctx;
	}
	

	abstract public void selectBackground(int level);
	abstract public void loadBackgrounds();
	
	

}
