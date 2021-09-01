package com.fish.Fishbowl;

/*
 *  Sprite -> MobileSprite -> Gato <- Animation
 * 
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.games.Sprites.Animation;
import com.games.Sprites.Sprite;

public class Gato extends Sprite {
	public final static int NADANDO = 0;
	public final static int COMIENDO = 1;
	public final static int COMIENDO_ROJO = 2;
	public final static int AHOGANDOSE = 3;  // Nadando_rojo
	public final static int MUERTO = 4;
	public final static int SUPERGUERRERO_DURACION = 620;
	
	private int speed_normal = 3;
	private int speed_superguerrero = 5;
	
	
	private Animation nadar;
	private Animation ahogandose;
	private Animation comer;
	private Animation comerRojo;
	private Animation muerto;
	private int stat;
	private int superguerrero;
	//private boolean bVisible = true;
	
	private int air = 0;
	private int precalculedHeight = 0;

	public Gato(Context ctx, int canvasW, int canvasH) {
		super();
		this.canvasH = canvasH;
		this.canvasW = canvasW;
		
		superguerrero = 0;
		
		stat = Gato.NADANDO;
		
		nadar = new Animation(ctx);
		ahogandose = new Animation(ctx);
		comer = new Animation(ctx);
		comerRojo = new Animation(ctx);
		muerto = new Animation(ctx);
		
		nadar.add(R.drawable.gatonadandol);
		nadar.add(R.drawable.gatonadandor);
		nadar.setCiclesPerAnimation(15);
		nadar.setPhase(0);
		nadar.startAnimation();
		
		ahogandose.add(R.drawable.gatoahogandosel);
		ahogandose.add(R.drawable.gatoahogandoser);
		ahogandose.setCiclesPerAnimation(15);
		ahogandose.setPhase(0);
		ahogandose.startAnimation();
		
		comer.add(R.drawable.gatocomiendo);
		comer.add(R.drawable.gatocomiendol);
		comer.add(R.drawable.gatocomiendor);
		comer.add(R.drawable.gatocomiendol);
		comer.add(R.drawable.gatocomiendor);
		comer.setCiclesPerAnimation(20);
		comer.setPhase(0);
		comer.setNumLoops(0);
		comer.startAnimation();
		
		comerRojo.add(R.drawable.gatocomiendorojo);
		comerRojo.add(R.drawable.gatocomiendorojol);
		comerRojo.add(R.drawable.gatocomiendorojor);
		comerRojo.add(R.drawable.gatocomiendorojol);
		comerRojo.add(R.drawable.gatocomiendorojor);
		comerRojo.setCiclesPerAnimation(15);
		comerRojo.setPhase(0);
		comerRojo.setNumLoops(0);
		comerRojo.startAnimation();
		
		muerto.add(R.drawable.gatomuerto);
		muerto.setPhase(0);
		
		doRespirar();
		
		this.speed_normal = speed_normal;
		this.speed_superguerrero = speed_superguerrero;
		
		setSpeed(speed_normal);
		startMoving();
		
		initialPosition();
		precalculedHeight = this.getHeight();
	}
	
	public void setSpeeds(int normal, int superguerrero) {
		this.speed_normal = normal;
		this.speed_superguerrero = superguerrero;
	}
	
	public void decAir() {
		air--;
	}
	
	public void globoAir() {
		air+=GameThread.PEZGLOBO_AIR;
	}
	
	public int getAir() {
		return air;
	}
	
	public void doRespirar() {
		air = GameThread.MAX_AIR;
	}
	/*
	public boolean isVisible() {
		return this.bVisible;
	}
	
	public void setVisible() {
		this.bVisible = true;
	}
	
	public void setInvisible() {
		this.bVisible = false;
	}
	
	public void hide() {
		this.bVisible = false;
	}*/
	
	public void setStat(int st) {
		getAnimation().stopAnimation();
		getAnimation().resetAnimation();
		stat = st;
		getAnimation().restart();
	}

	@Deprecated
	public void setStatOld(int st) {
		if (stat == st)													//Optimización del caso más probable.
			return;
		
		if (st == MUERTO) {												//Estado prioritario
			superguerrero = 0;
			stat = st;
		}
		
		if (stat == COMIENDO || stat == COMIENDO_ROJO) {					//Si la animación del gato comiendo no ha acabado no interrumpir	
			if (getAnimation().isRunning()) {
				if (st == COMIENDO || st == COMIENDO_ROJO)
					getAnimation().restart();
				else 
					return;
			} else {
				if (st == COMIENDO || st == COMIENDO_ROJO)
					getAnimation().restart();
					
			}
		}
						
		
		if (st == COMIENDO) { 						//El thread se desentiende de si ha de lanzar un comiendo o
			if (stat == AHOGANDOSE)	
				stat = COMIENDO_ROJO;											//un comienodo_rojo
			else
				stat = COMIENDO;
			getAnimation().restart();
			
		} else 
			stat = st;
	}
	
	public int getStat() {
		return stat;
	}
	
	public void setSuperGuerrero() {
		superguerrero = SUPERGUERRERO_DURACION;
		this.setSpeed(speed_superguerrero);
	}
	
	public boolean isSuperGuerrero() {
		return (superguerrero > 0);
	}
	
	public boolean isSubiendoRecto() {
		return (x == targetX && y > targetY);
	}
	
	public Animation getAnimation()  {
		switch (stat) {
		
			case Gato.NADANDO:
				return nadar;
				
			case Gato.COMIENDO:
				return comer;
				
			case Gato.COMIENDO_ROJO:
				return comerRojo;
				
			case Gato.AHOGANDOSE:
				return ahogandose;
				
			case Gato.MUERTO:
				return muerto;
				
			default:
				Log.d("PhishBowl", "getAnimation() of stat"+stat);
				return null;
		}
	}
	
	public void initialPosition() {
		this.x = canvasW/2-this.getWidth()/2;
		this.y = GameThread.SURFACE;
		targetX = x;
		targetY = y;
	}

	public void goDown() {
		targetY = canvasH-precalculedHeight;
		startMoving();
	}
	
	public void goUp() {
		targetY = GameThread.SURFACE;
		startMoving();
	}
	
	public void timon(float pitch) {
		
		if (pitch < -10) {
			//derecha
			targetX = canvasW-getWidth();
			startMoving();
			
		} else if (pitch > 10) {
			//izquierda
			targetX = 0;
			startMoving();
			
		} else {
			//centro
			targetX = x;
		}
		
	}



	public void doDraw(Canvas c) {
		if (this.visible)
			getAnimation().doDraw(c, x, y);
	}
	
	public int getWidth() {
		return getAnimation().getWidth();
	}
	
	public int getHeight() {
		return getAnimation().getHeight();
	}
	
	
	public void doStep() {
		super.doStep();
	
		
		if (superguerrero > 0)
			superguerrero --;
		else
			setSpeed(speed_normal);
		
		
	
		
		// Finaliza la animación, (la de nadar es infinita, con lo que ha finalizado la de comer)
		if (!getAnimation().isRunning()) {
			
			if (stat == Gato.COMIENDO)
				setStat(Gato.NADANDO);
			
			if (stat == Gato.COMIENDO_ROJO)
				setStat(Gato.AHOGANDOSE);
		}
	
		
	}
	
	public boolean isComiendo() {
		return (stat == Gato.COMIENDO || stat == Gato.COMIENDO_ROJO);
	}
	
	public boolean isNadando() {
		return (stat == Gato.NADANDO || stat == Gato.AHOGANDOSE);
	}
	
	public boolean isOnTheBottom() {
		return (y == targetY && y > canvasH/2);
		//return (y > canvasH/2);
	}
	

	@Override
	public void onBorderTouch(int c) {

	}

	@Override
	public void onCollision(int c) {

	}

}
