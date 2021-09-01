package com.fish.Fishbowl;

import android.content.Context;
import android.media.MediaPlayer;

import com.games.Base.GameSound;
import com.games.Base.Sound;

public class Sounds extends GameSound {
	
    private Sound sndBG;
    private Sound sndGameOver;
    public Sound sndComer;
    public Sound sndMorir;
    public Sound sndRespirar;
    public Sound sndBubbles;


	public Sounds(Context ctx) {
		super(ctx);
	}
	
	public void playBackground() {
		stopMusics();
		stopSounds();
		
		sndBG.play();
		//mpBackground.seekTo(0);
		//mpBackground.start();
	}
	
	public void playGameOver() {
		stopSounds();
		stopMusics();
		sndGameOver.play();
	}
	

	@Override
	protected void load() {
        sndBG = new Sound(ctx, R.raw.gokuh, 1f, true);
        sndGameOver = new Sound(ctx, R.raw.gameover, 1f, true);
        sndComer = new Sound(ctx, R.raw.comer, 0.6f, false);
        sndMorir = new Sound(ctx, R.raw.dead, 1f, false);
        sndRespirar = new Sound(ctx, R.raw.respirar, 0.6f, false);
        sndBubbles = new Sound(ctx, R.raw.bubbles, 0.3f, true);
	}

	@Override
	protected void stopSounds() {
		sndComer.stop();
		sndMorir.stop();
		sndRespirar.stop();
		sndBubbles.stop();
	}

	@Override
	protected void stopMusics() {
		sndBG.stop();
		sndGameOver.stop();
		
		
		
		/*
		try {
			
	        if (mpBackground.isPlaying()) { // || mpBackground.isLooping()) {
	        	mpBackground.pause();
	        	mpBackground.seekTo(0);
	        }
	        
	        if (mpGameOver.isPlaying()) { // || mpGameOver.isLooping()) {
	        	mpGameOver.pause();
	        	mpGameOver.seekTo(0);
	        }
			
			
		} catch (Exception e) {	}*/
	}
	
}
