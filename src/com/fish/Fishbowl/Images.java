package com.fish.Fishbowl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.games.Base.GameImage;


public class Images extends GameImage {
	
	public Images(Context ctx) {
		super(ctx);
	}
	
	public Bitmap fondo = null;
	public Bitmap help = null;
	
	
	
	public void loadBackgrounds() {
		//Load
		fondo = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.fondo);
		help = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.help);
		
		//Scale
		fondo = fondo.createScaledBitmap(fondo, this.canvasWidth, this.canvasHeight, true);
		help = help.createScaledBitmap(help, this.canvasWidth, this.canvasHeight, true);
	}

	@Override
	public void selectBackground(int screen) {
		
		
		switch (screen) {
			case GameThread.SCREEN_INIT:
			case GameThread.SCREEN_CALIBRATING:
				bg = fondo;
				break;
			case GameThread.SCREEN_TUTORIAL_DOWN:
			case GameThread.SCREEN_TUTORIAL_LATERAL:
			case GameThread.SCREEN_TUTORIAL_OK:
				bg = fondo;
				break;
			case GameThread.SCREEN_TUTORIAL_FISHES:
				bg = help;
				break;
			case GameThread.SCREEN_GAME_READY:
			case GameThread.SCREEN_GAME:
			case GameThread.SCREEN_GAMEOVER:
				bg = fondo;
				break;		
		}
		
		
	}
	
	

}
