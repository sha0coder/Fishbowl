package com.fish.Fishbowl;

import java.security.SecureRandom;

import android.content.Context;
import android.graphics.Canvas;

import com.games.Base.Collisions;
import com.games.Sprites.Animation;
import com.games.Sprites.Sprite;

public class Shark extends Sprite {
	private final static int NADANDO_R = 0;
	private final static int NADANDO_L = 1;
	private final static int COMIENDO_R = 2;
	private final static int COMIENDO_L = 3;
	
	private Animation nadar_r;
	private Animation nadar_l;
	private Animation comer_r;
	private Animation comer_l;
	
	private int estado;


	private SecureRandom sr;
	
	public Shark(Context ctx, int canvasW, int canvasH) {
		super();
		this.canvasH = canvasH;
		this.canvasW = canvasW;
		sr = new SecureRandom();
		
		nadar_r = new Animation(ctx);
		nadar_l = new Animation(ctx);
		comer_r = new Animation(ctx);
		comer_l = new Animation(ctx);
		
		//comer_l.add(R.drawable.piranhab);
		comer_l.add(R.drawable.sharkc);
		comer_l.add(R.drawable.sharkc);
		comer_l.setCiclesPerAnimation(15);
		comer_l.scale1();
		comer_l.setPhase(1);
		comer_l.setLoop(false);
		comer_l.setNumLoops(1);
		comer_l.setDebug(true);
		comer_l.startAnimation();
		
		
		
		//comer_r.add(R.drawable.piranhab);
		comer_r.add(R.drawable.sharkc);
		comer_r.add(R.drawable.sharkc);
		comer_r.setCiclesPerAnimation(15);
		comer_r.scale1();
		comer_r.setLoop(false);
		comer_r.setNumLoops(1);
		comer_r.setPhase(1);
		comer_r.setDebug(true);
		comer_r.startAnimation();
		comer_r.flipH();
		
		
		nadar_r.add(R.drawable.sharka);
		nadar_r.add(R.drawable.sharkb);
		nadar_r.setCiclesPerAnimation(10);
		nadar_r.setPhase(1);
		nadar_r.startAnimation();
		nadar_r.flipH();
		
		nadar_l.add(R.drawable.sharka);
		nadar_l.add(R.drawable.sharkb);
		nadar_l.setCiclesPerAnimation(10);
		nadar_l.setPhase(1);
		nadar_l.startAnimation();
		nadar_l.scale1();
		
		//Por que lado de la pantalla aparece?
		if (sr.nextInt(2) == 0) {
			//Sale por la Izquierda
			x = getWidth();
			targetX = canvasW+getWidth();
			setEstado(NADANDO_R,false);
		} else {
			//Sale por la derecha
			x  = canvasW + getWidth();
			targetX = -getWidth();
			setEstado(NADANDO_L,false);
		}
		
		//A que altura sale?
		targetY = y = sr.nextInt(canvasH-getHeight()-70)+70;
		
		setSpeed(2);
		startMoving();
	}
	
	public int getEstado() {
		return estado;
	}

	private void setEstado(int stat, boolean priority) {
		//optimizar con los casos m��s probables primero
		
		Animation anim = getAnimation();
		
		if (!priority && (estado == COMIENDO_R || estado == COMIENDO_L)) {
			if (anim.isRunning())
				return;
		}
		
		anim.restart();
		//Log.d("FLUJO","cambiando estado "+estado+" --> "+stat);
		estado = stat;
	}
	
	public Animation getAnimation()  {
		switch (estado) {
		
			case NADANDO_R:
				return nadar_r;
				
			case NADANDO_L:
				return nadar_l;
				
			case COMIENDO_R:
				return comer_r;
				
			case COMIENDO_L:
				return comer_l;
			
			default:
				return null;
		}
	}
	
	
	
	public boolean isDerecha() {
		return (targetX>x);
	}
	
	public void doNadar(boolean priority) {
		setEstado((isDerecha())?NADANDO_R:NADANDO_L, priority);
	}
	
	public void doComer() {
		setEstado((isDerecha())?COMIENDO_R:COMIENDO_L, true);
	}
	
	private boolean isComiendo() {
		return (estado == COMIENDO_R || estado == COMIENDO_L);
	}
	
	public void turn() {
		if (targetX < 0)
			targetX = canvasW+10;
		else
			targetX = -10;
		
		//if (isComiendo())
		//	doComer(); 
		
		switch (estado) {
			case COMIENDO_R:
				setEstado(COMIENDO_L, true);
				break;
			case COMIENDO_L:
				setEstado(COMIENDO_R, true);
				break;
			default:
				doNadar(false);
		}

	}
	
	@Override
	public void doStep() {
		
		//doNadar(false);
		
		//Avanzar hacia el rumbo fijado previamente
		super.doStep();
		
		
		//Variar Rumbo
		switch(sr.nextInt(GameThread.SHARK_ENTROPY)) {
			
			case 0:	//Girar
				turn();
				break;
				
			case 2: //Subir
				if (y > getHeight()+GameThread.PIRANHA_DIRECTION_VARIATION)
					targetY -= GameThread.SHARK_DIRECTION_VARIATION;
				break;
				
			case 1: //Bajar
				if (y<canvasH-getHeight())
					targetY += GameThread.SHARK_DIRECTION_VARIATION;
				break;
				
			default: //No variar rumbo
				return;
		}
		
		
		//if (targetY < GameThread.PIRANHA_SURFACE)
		//	targetY = GameThread.PIRANHA_SURFACE;
		
	}
	
	
	@Override
	public int getWidth() {
		return nadar_r.getWidth();
	}
	
	@Override
	public int getHeight() {
		return nadar_r.getHeight();
	}
	
	@Override
	public void doDraw(Canvas c) {
		Animation anim = getAnimation();
		
		anim.doDraw(c, x, y);
		
		if (!anim.isRunning()) 
			doNadar(true);
			
	}
	
	@Override
	public void onBorderTouch(int c) {
		if (c == Collisions.COLLISION_LEFT && targetX<0)
			turn();
		else if(c == Collisions.COLLISION_RIGHT && targetX > canvasW)
			turn();
	}
	
	@Override
	public void onCollision(int c) {
		doComer();
	}
	
	
}
