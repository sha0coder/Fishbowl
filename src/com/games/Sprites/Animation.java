package com.games.Sprites;

import java.util.ArrayList;

import com.games.Base.Physics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;


/* Animation v0.2
 * 
 * Sprite -> MobileSprite -> AnimatedSprite
 * 
 * 
 *  ciclos -> entre foto y foto pasan n ciclos.
 *  fases -> entre la primera foto y la ultima foto, cada foto es una fase.
 *  toLoop -> cuando llegas a la última foto, volver a empezar de forma iterativa?
 *  loop -> en el caso de toLoop, cuantas iteraciones? cero una n o por defecto infinitas que es el valor -1
 *  
 */


public class Animation {
	private int currentPhase = 0;
	private int currentCicle = 0;
	private int ciclesPerAnimation = 1;
	private boolean isRunning;
	private boolean toLoop;
	private int iteration_limit = -1;
	private int loops = -1;
	private boolean debug = false;
	
	private ArrayList<Drawable> draw = new ArrayList<Drawable>();
	protected Context ctx;
	
	private int width = 0;
	private int height = 0;
	
	
	
	public Animation(Context ctx) {
		super();
		this.ctx = ctx;
		isRunning  = false;
		toLoop = true;
	}
	
	public void setDebug(boolean d) {
		debug = d;
	}
	
	public void setNumLoops(int l) {
		iteration_limit = loops = l;
	}
	
	public void setLoop(boolean loop) {
		toLoop = loop;
	}
	
	public boolean getLoop() {
		return toLoop;
	}
	
	public void setCiclesPerAnimation(int c) {
		this.ciclesPerAnimation = c;
	}
	
	public void setPhase(int c) {
		this.currentPhase = c;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void add(int pic) { //escalar a 1 ?
		this.draw.add(this.ctx.getResources().getDrawable(pic));
	}
	
	
	// Se llama muchisimas veces, ha de retornar algo precalculado
	public int getWidth() {
		if (currentPhase >= this.draw.size())
			return width;
		return this.draw.get(currentPhase).getIntrinsicWidth();
		
		//return width;
		
		/*
		try {
			return this.draw.get(currentPhase).getIntrinsicWidth();  //OPTIMIZATION: anchura fija para toda la anim
		
		} catch (Exception e) {
			Log.d("getHeight()","currentPhase: "+currentPhase);
			Log.d("getHeight()","draw.size(): "+draw.size());
			e.printStackTrace();
			return 0;
		}*/
	}
	
	// Se llama muchisimas veces, ha de retornar algo precalculado
	public int getHeight() {
		if (currentPhase >= this.draw.size())
			return height;
		
		return this.draw.get(currentPhase).getIntrinsicHeight();
					
		//return height;
		
		/*
		try {
			return this.draw.get(currentPhase).getIntrinsicHeight();
		
		} catch (Exception e) {
			Log.d("getHeight()","currentPhase: "+currentPhase);
			Log.d("getHeight()","draw.size(): "+draw.size());
			e.printStackTrace();
			return 0;
		}*/
	}
	

	public void rotate(float angle) {
		for (int i=0; i<draw.size(); i++)
			draw.set(i,Physics.rotateBMP(((BitmapDrawable)draw.get(i)).getBitmap(), angle));
		precalcSize();
	}
	

	public void flipV() {
		for (int i=0; i<draw.size(); i++)
			draw.set(i,Physics.flipVBMP(((BitmapDrawable)draw.get(i)).getBitmap()));
		precalcSize();
	}
	

	public void flipH() {
		for (int i=0; i<draw.size(); i++)
			draw.set(i,Physics.flipHBMP(((BitmapDrawable)draw.get(i)).getBitmap()));
		precalcSize();
	}
	

	public void flipVH() {
		for (int i=0; i<draw.size(); i++)
			draw.set(i,Physics.flipBMP(((BitmapDrawable)draw.get(i)).getBitmap()));
		precalcSize();
	}
	

	public void scale(int w, int h) {
		for (int i=0; i<draw.size(); i++)
			draw.set(i,Physics.scaleBMP(((BitmapDrawable)draw.get(i)).getBitmap(),w,h));
		precalcSize();
	}
	
	public void scale1() {
		for (int i=0; i<draw.size(); i++)
			draw.set(i,Physics.rescale(((BitmapDrawable)draw.get(i)).getBitmap(),1,1));
		precalcSize();
	}


	
	

	
	public void doDraw(Canvas c, double x, double y) {
		//Esta función se ejecuta muchas veces, ha de ser muy rápida
		//optimización: quitar try/catch?
		//TODO: optimización: fusionar ciclo con phase, 0.0 es fase 0, 0.5 es fase 0, pero 1.1 es fase 1 
		
		try {        
			
				
    		draw.get(currentPhase).setBounds((int)x,(int)y,(int)x+getWidth(),(int)y+getHeight());
    		draw.get(currentPhase).draw(c);
			
    		
    		if (isRunning) {
    			
				if (currentCicle == ciclesPerAnimation) {
					currentCicle = 0;
					
					
					if (currentPhase == draw.size()-1) {
						
						
						if (toLoop) {
							currentPhase = 0;
							
				
							if (loops >0) {
								loops--;
							} else if (loops == 0) {
								isRunning = false; 
							}
							
						} else {
							isRunning = false;
						}
					} else
						currentPhase ++;
					
				} else
					currentCicle ++;
    		}
        		
        } catch (Exception e) {
        		System.out.println("Animation loop "+e.toString());
        }
	}
	
	public void resetAnimation() {
		currentPhase = 0;
		currentCicle = 0;
	}
	
	public void restart() {
		resetAnimation();
		startAnimation();
	}
	
	public void startAnimation() {
		isRunning = true;
		loops = iteration_limit;
		
		precalcSize();
	}
	
	private void precalcSize() {
		//Precalculo para que vaya mas rapida el getWidth() y getHeight()
		width = this.draw.get(0).getIntrinsicWidth();
		height = this.draw.get(0).getIntrinsicHeight();
	}
	
	public void stopAnimation() {
		isRunning = false;
	}
	
	
}
