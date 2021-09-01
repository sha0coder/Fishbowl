package com.games.Base;

/*
 *  (BaseGameThread)
 *        |
 *   (GameThread) <--------- (SurfaceGame) <------- FishbowlActivity
 * 
 */



import com.fish.Fishbowl.GameThread;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public  class SurfaceGame extends SurfaceView implements SurfaceHolder.Callback {
	
	public GameThread thread;
	
	public SurfaceGame(Context context, AttributeSet attrs) {
		super(context,attrs);
		
		try {
			// register our interest in hearing about changes to our surface
			SurfaceHolder holder = getHolder();
			holder.addCallback(this);
			
			this.initThread(context, holder);
			setFocusable(true); //get key events
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void initThread(Context ctx, SurfaceHolder holder) {
		thread = new GameThread(holder, ctx);
	}
	



	public void surfaceChanged(SurfaceHolder sh, int format, int width, int height) {
		try {
			thread.setSurfaceSize(width, height);
			thread.start();
		} catch(Exception e) {
			//engine ya arancado 
			e.printStackTrace();
		}
	}

	public void surfaceCreated(SurfaceHolder arg0) {	
		//Log.d("surfacegame","created");
	}


	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		thread.setRunning(false);

		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
