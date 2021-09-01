package com.fish.Fishbowl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import com.games.Base.BaseGameThread;

public class MenuThread  extends BaseGameThread {

	private Paint red;
	
	public MenuThread(SurfaceHolder sh, Context ctx) {
		super(sh, ctx);
	}

	@Override
	protected void setCanvasSize(int width, int height) {
		//canvasW = width;
		//canvasH = height;
	}

	@Override
	public void initializeGame() {
		red = new Paint();
		red.setAntiAlias(true);
		red.setARGB(255, 255, 0, 0);
		red.setTextSize(30);
	}

	@Override
	public void pauseEngine() {

		
	}

	@Override
	public void doIA() {

		
	}

	@Override
	public void doPhysics() {

		
	}

	@Override
	public void doDraw(Canvas c) {
		c.drawText("Center tablet, and tap the screen", 100, 100, red);
		
	}

	@Override
	public void preInitializeGame() {
		
		
	}

}
