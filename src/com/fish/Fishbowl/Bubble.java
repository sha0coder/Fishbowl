package com.fish.Fishbowl;

import android.content.Context;
import android.graphics.Canvas;

import com.games.Sprites.Pic;
import com.games.Sprites.Sprite;



public class Bubble extends Sprite  {
	public static final int SPEED = 2;
	public static final int SZ_SMALL = 15;
	public static final int SZ_MEDIUM = 25;
	public static final int SZ_BIG = 35;
	
	//private SecureRandom sr;
	private Pic pic;
	
	
	public Bubble(Context ctx, double x, double y, int sz) {
		super();
		
		this.x = x;
		this.y = y;
		this.targetX = x;
		this.targetY = GameThread.SURFACE;
		pic = new Pic(ctx);
		pic.load(R.drawable.bubble);
		pic.scale(sz, sz);
		setSpeed(Bubble.SPEED);
		startMoving();
	}	

	@Override
	public int getWidth() {
		return pic.getWidth();
	}

	@Override
	public int getHeight() {
		return pic.getHeight();
	}

	@Override
	public void doDraw(Canvas c) {
		pic.doDraw(c, x, y);
	}

	@Override
	public void onBorderTouch(int c) {
		
	}

	@Override
	public void onCollision(int c) {
		
	}
	

}
