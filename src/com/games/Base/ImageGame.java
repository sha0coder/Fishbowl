package com.games.Base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

abstract public class ImageGame {
	protected Context ctx;
	
	public Bitmap bg = null;
	
	public ImageGame(Context ctx) {
		this.ctx = ctx;
	}
	
	abstract void load();
	abstract public void loadLevelBackground(int level);
	
	
	

}
