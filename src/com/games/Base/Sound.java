package com.games.Base;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
	
	private MediaPlayer mp;
	
	public Sound(Context ctx, int resource, float volume, boolean looping) {
		mp = MediaPlayer.create(ctx, resource);
		setVolume(volume);
	
		
		try {
        	mp.seekTo(0);
        	mp.setLooping(looping);
        	mp.setVolume(1f,1f);
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setVolume (float vol) {
		mp.setVolume(vol, vol);
	}
	
	public void setVolume(float i, float d) {
		mp.setVolume(i, d);
	}
	
	public void play() {
		if (mp.isLooping())
			mp.seekTo(0);
		mp.start();
	}
	
	public void stop() {
		try {
			
			if (mp.isPlaying()) {
				if (mp.isLooping()) {
					mp.seekTo(0);
					mp.pause();
				}else {
					mp.stop();
				}
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

}
