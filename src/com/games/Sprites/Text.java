package com.games.Sprites;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Text {
	public static final int TEXT_FIXED = -5;
	public static final int TEXT_FAST = 90;
	public static final int TEXT_SLOW = 130;
	
	private int x, y;
	private Vector<String> msg = new Vector<String>();
	private int id = 0;
	private int lines = 0;
	private Paint paint;
	private int sz = 30;
	private int duration = 0; //ticks
	private boolean enabled;
	
	
	public Text(int x, int y) {
		this.x = x;
		this.y = y;
		paint = new Paint();
		paint.setAntiAlias(true);
	}
	
	public void addText(String str) {
		msg.add(str);
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setColor(int r, int g, int b) {
		paint.setARGB(255, r, g, b);
	}
	
	public void setDuration(int ticks) {
		duration = ticks;
	}
	
	public void setTextSize(int sz) {
		paint.setTextSize(sz);
		this.sz = sz;
	}
	
	public void select(int id) {
		this.id = id;
		this.lines = 1;
	}
	
	public void setLines(int lines) {
		this.lines = lines;
	}
	
	public void start(int duration, int id, int lines) {
		this.enabled = true;
		this.duration = duration;
		this.id = id;
		this.lines = lines;
	}
	
	public int getSelected() {
		return id;
	}

	
	public void doDraw(Canvas c) {

		if (duration == 0) {
			enabled = false;
			return;
		}

		if (duration != TEXT_FIXED)
			duration --;
		
		
		for (int i=0; i<lines; i++) {
			c.drawText(msg.get(id+i), x, y+i*(sz+6), paint);
		}
		
	}
	
}
