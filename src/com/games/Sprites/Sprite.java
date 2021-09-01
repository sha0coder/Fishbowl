package com.games.Sprites;

import java.security.SecureRandom;

import android.content.Context;
import android.graphics.Canvas;


public abstract class Sprite  {
	public double id;
	public double x = 0;
	public double y = 0;
	public boolean isMoving;
	protected double inc_x;
	protected double inc_y;
	protected double oldX = 0;
	protected double oldY = 0;
	protected double targetX;
	protected double targetY;
	protected int canvasW;
	protected int canvasH;
	protected Context ctx;
	private float speed = 0.1F;
	private float acceleration = 0;
	private float deceleration = 0;
	protected boolean visible = true;
	
	public Sprite() {
		__genId();
		isMoving=false;
	}
	
	private void __genId() {
    	SecureRandom sr1 = new SecureRandom(); //optimizable: crear objeto random para el juego
    	byte[] seed = new byte[40];
    	sr1.nextBytes(seed);
    	SecureRandom sr2 = new SecureRandom(seed);
    	this.id = sr2.nextDouble();
	}
	
	public void hide() {
		visible = false;
	}
	
	public void show() {
		visible = true;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void goTo(double x, double y) {
    	this.targetX = x;
    	this.targetY = y;
    }
    
	public void stopMoving() {
		this.isMoving = false;
	}
	
	public void startMoving() {
		this.isMoving = true;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
	public int getCenterX() {
		return (int)(x+getWidth()/2);
	}
	
	public int getCenterY() {
		return (int)(y+getHeight()/2);
	}
	
	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
	}
	
	
    public void doStep() {
    	if (!this.isMoving)
    		return;
    	
    	//si ha llegado a la meta, parar
    	if ((int)x == (int)targetX && (int)y == (int)targetY) {
    		this.stopMoving();
    		return;
		}
    	
    	//backup de la posici��n actual, por si hay que tirar atras.
    	oldX = x;
    	oldY = y;
    	
    	
    	
    	//aceleraci��n y freno
     	speed += acceleration;
    	speed -= deceleration;
    	if (speed < 0)
    		speed = 1;
    	
    	
    	// Velocidad
		// TODO: si vamos en diagonal, hay que ir un poco m��s
		// despacio porque es avance doble
		// El incremento es cero, si una de las dos dimensioens ya
		// coincide con el destino
   
		inc_x = inc_y = speed;
		if ((int) x == (int)targetX)
			inc_x = 0;
		if ((int) y == (int)targetY)
			inc_y = 0;
    	
 
		// Direcci��n
		// los incrementos ser��n negativos si queremos llegar a un
		// punto menor
		if (x > targetX)
			inc_x = -1 * inc_x;
		if (y > targetY)
			inc_y = -1 * inc_y;
		
		
		// Si la distancia con el destino, es menor que el incremento,
		// para no pasarnos, llegamos directamente.
		if (Math.abs(x - targetX) <= inc_x) {
			x = targetX;
			inc_x = 0;
		}
		if (Math.abs(y - targetY) <= inc_y) {
			y = targetY;
			inc_y = 0;
		}
		
		//step
		x += inc_x;
		y += inc_y;
	
    }
    
	public abstract int getWidth();
	public abstract int getHeight();  
	public abstract void doDraw(Canvas c);
	public abstract void onBorderTouch(int c);
	public abstract void onCollision(int c);
	
}
