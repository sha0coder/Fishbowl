package com.games.Base;


import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public abstract class BaseGameThread extends Thread {
	
	protected Context ctx;
	protected SurfaceHolder sh;
	protected boolean isRunning;
	protected boolean isDrawing;
	protected boolean isProcessing;
	protected float tick;
	public boolean isReloading;
	
	
	public BaseGameThread(SurfaceHolder sh, Context ctx) {
		this.ctx = ctx;
		this.sh = sh;
		isRunning = false;
		isDrawing = true;
		isProcessing = true;
		isReloading = false;
		tick = 0;
	}
	
	
	public void setSurfaceSize(int width, int height) {
		if (!isReloading)
			preInitializeGame();
		
		setCanvasSize(width,height);
		initializeGame();
		startEngine();
	}
	


	abstract protected void setCanvasSize(int width, int height);
	
	public void startEngine() {
		setRunning(true);
	}
	
	public void setRunning(boolean running) {
		this.isRunning = running;
	}
	
	public void run() {
		while (isRunning) {
			Canvas c = null;
			try {
				c = sh.lockCanvas(null);
				synchronized (sh) {
					if (isProcessing) {
						doIA();
						doPhysics();
					}
					
					if (isDrawing)
						doDraw(c);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				
			} finally {
				if (c != null) {
					sh.unlockCanvasAndPost(c);
				}
			}
		}
		
	}

	//Engine:
	abstract public void preInitializeGame(); //No se ejecuta al onReload()
	abstract public void initializeGame();
	abstract public void pauseEngine();
	abstract public void doIA();
	abstract public void doPhysics();
	abstract public void doDraw(Canvas c);

}
