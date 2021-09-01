package com.games.Sprites;

import com.games.Base.Collisions;
import android.content.Context;
import android.graphics.Canvas;


public class Button extends Pic {
	public int x,y;
	private boolean isVisible;

	public Button(Context ctx, int x, int y) {
		super(ctx);
		this.x = x;
		this.y = y;
		isVisible = true;
	}
	
	public void hide() {
		this.isVisible = false;
	}
	
	public void show() {
		this.isVisible = true;
	}
	
	public boolean isVisible() {
		return isVisible;
	}

	
	public boolean isTapped(int x, int y) {
		return (Collisions.colisionPuntoPlano(x, y, this, true));
	}
	
	public void doDraw(Canvas c) {
		if (isVisible) {
			try {        				
	    		draw.setBounds(x,y,x+getWidth(),y+getHeight());
	    		draw.draw(c);
	        		
	        } catch (Exception e) {
	        		System.out.println(e.toString());
	        }
		}
	}

}
