package com.games.Base;

import android.content.Context;
import android.media.MediaPlayer;

abstract public class GameSound {
	
	abstract protected void load();
	abstract protected void stopSounds();
	abstract protected void stopMusics();
	
	protected Context ctx;
	
	public GameSound(Context ctx) {
		this.ctx = ctx;
	}
	
	public void finalize() {
		stopSounds();
		stopMusics();
	}

}
