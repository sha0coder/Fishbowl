package com.fish.Fishbowl;


import com.fish.Fishbowl.R;
import com.games.Base.SurfaceGame;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;


public class FishbowlActivity extends Activity implements SensorEventListener {
	private GameThread game;
	private SensorManager sensorManager;
	private Sensor sensor;
	private int callibration = 1;


	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startGame();
    }
    
    private void startGame() {
        try {
	        setContentView(R.layout.arena);
	        
	        setFullScreen();
	        enableSensor();
	        initEngine();
	        
        } catch (Exception e) {
        	e.printStackTrace();
        	sensorManager.unregisterListener(this);
        }
    }
    
    private void setFullScreen()  {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }
    
    private void enableSensor() {
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    private void initEngine() {
        SurfaceGame surface = (SurfaceGame) findViewById(R.id.arena);
        game = (GameThread) surface.thread;
    }
    
    /*********** finalizacion **************************/
    
    /*
    public void onDetachedFromWindow() {
    	Log.d("fishbowl","onDetachedFromWindow()");
    	game.pauseEngine();
    	super.onDetachedFromWindow();
    }*/
    
    
    /*
    protected void onDestroy() {
    	game.pauseEngine();
    }*/
    
    protected void onPause() {
    	super.onPause();
    	//Log.d("fishbowl","onPause()");
    	game.pauseEngine();
    	//game.stop();
    	//finalize();
        
    }
    
    protected void onRestart() {
    	Log.d("fishbowl","onRestart()");

    	
    	startGame();
    	game.isReloading = true;
        
    	super.onRestart();
    	/*
    	enableSensor();
    	setFullScreen();
    	initEngine();
    	game.startEngine();
    	super.onRestart();*/
    }
    
    /*
    protected void onResume() {
    	Log.d("fishbowl","onResume()");
    	initEngine();
    	game.startEngine();
    	super.onResume();
    }*/
    
	 @Override
	 public void onBackPressed() {
 		if (game.exit())
 			super.onBackPressed();
	 }
    
	/*
    @Override
    public void finalize() {
    	
    	try {
    		game.pauseEngine();
    		//game.stop();
    		
    	} finally {
				try {
					super.finalize();
				} catch (Throwable e) {
					e.printStackTrace();
				}
    	}
	
    	
    }*/

    
    /******************** eventos *******************************/
    
    
    // BOTON DOWN
    public boolean onTouchEvent(MotionEvent event) {
    	
    	
		
    	switch(event.getAction()) {
    	
	    	case MotionEvent.ACTION_DOWN:
	    		
	    		if (game.isControllingGato) 
	    			game.onTapDown((int)event.getX(), (int)event.getY());
	    		
	    		switch (game.screen) {
	    		
		    		case GameThread.SCREEN_INIT:
		    			game.setScreen(GameThread.SCREEN_CALIBRATING);
		    			break;
		    			
		    		case GameThread.SCREEN_TUTORIAL_OK:
		    			game.setScreen(GameThread.SCREEN_TUTORIAL_FISHES);
		    			break;
		    			
		    		case GameThread.SCREEN_GAMEOVER:
		    		case GameThread.SCREEN_TUTORIAL_FISHES:
		    			game.setScreen(GameThread.SCREEN_GAME_READY);
		    			break;
		    			
		    		case GameThread.SCREEN_GAME_READY:
		    			game.setScreen(GameThread.SCREEN_GAME);
		    			break;

	    		}
	    	
	    		break;
	    		
	    	case MotionEvent.ACTION_UP:
	    		
	    		if (game.isControllingGato) 
	    			game.onTapUp();
	    		
	    		
	    		break;
    	}
	    
    
    
    	return super.onTouchEvent(event);
    }





	// BALANCEO DEL TELEFONO - TIMON
	public void onSensorChanged(SensorEvent event) {
		//azimuth, pich, roll
		
		if (game.isControllingGato) {
			game.turn(event.values[callibration]);
			return;
		}
		
		
			
		if (game.screen == GameThread.SCREEN_CALIBRATING) {
			
			if (Math.abs(event.values[0]) < 10)
				callibration = 0;
			else if (Math.abs(event.values[1]) < 10)
				callibration = 1;
			else if (Math.abs(event.values[2]) < 10)
				callibration = 2;
		
			game.setScreen(GameThread.SCREEN_TUTORIAL_DOWN);
		}

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// o_O ??
	}


	//	 GESTURES

}