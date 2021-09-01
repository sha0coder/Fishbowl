package com.games.Sprites;


import com.games.Base.Physics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;


public class Pic {

	
	protected Drawable draw;
	protected Context ctx;
	protected int width, height;

	public Pic(Context ctx) {
		super();
		this.ctx = ctx;
	}
	
	public void load(int pic) {
		draw = this.ctx.getResources().getDrawable(pic);
		width = this.draw.getIntrinsicWidth();
		height = this.draw.getIntrinsicHeight();
	}
	
	public Drawable getDrawable() {
		return draw;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public void rotate(float angle) {
		draw = Physics.rotateBMP(((BitmapDrawable)draw).getBitmap(), angle);
	}

	public void flipV() {
		draw = Physics.flipVBMP(((BitmapDrawable)draw).getBitmap());
	}

	public void flipH() {
		draw = Physics.flipHBMP(((BitmapDrawable)draw).getBitmap());
	}
	

	public void flipVH() {
		draw = Physics.flipBMP(((BitmapDrawable)draw).getBitmap());
	}

	public void scale(int w, int h) {
		draw = Physics.scaleBMP(((BitmapDrawable)draw).getBitmap(),w,h);
	}
	
	public void scale1() {
		draw = Physics.rescale(((BitmapDrawable)draw).getBitmap(),1,1);
	}

	public void doDraw(Canvas c, double x, double y) {
		//Esta funci��n se ejecuta muchas veces, ha de ser muy r��pida
		//optimizaci��n: quitar try/catch?
		//TODO: optimizaci��n: fusionar ciclo con phase, 0.0 es fase 0, 0.5 es fase 0, pero 1.1 es fase 1 
		
		try {        				
    		draw.setBounds((int)x,(int)y,(int)x+getWidth(),(int)y+getHeight());
    		draw.draw(c);
        		
        } catch (Exception e) {
        		System.out.println(e.toString());
        }
	}

}
