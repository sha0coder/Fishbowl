package com.fish.Fishbowl;

import java.security.SecureRandom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.games.Base.Collisions;
import com.games.Sprites.Animation;
import com.games.Sprites.Sprite;

public class Piranha extends Sprite {
	private final static int NADANDO_R = 0;
	private final static int NADANDO_L = 1;
	private final static int COMIENDO_R = 2;
	private final static int COMIENDO_L = 3;
	
	
	
	private Animation nadar_r;
	private Animation nadar_l;
	private Animation comer_r;
	private Animation comer_l;
	private Animation comer_gato_r;
	private Animation comer_gato_l;
	
	private int estado;
	private boolean bComiendoGato = false;

	private SecureRandom sr;
	
	public Piranha(Context ctx, int canvasW, int canvasH) {
		super();
		this.canvasH = canvasH;
		this.canvasW = canvasW;
		sr = new SecureRandom();
		
		nadar_r = new Animation(ctx);
		nadar_l = new Animation(ctx);
		comer_r = new Animation(ctx);
		comer_l = new Animation(ctx);
		comer_gato_r = new Animation(ctx);
		comer_gato_l = new Animation(ctx);
		
		//comer_l.add(R.drawable.piranhab);
		comer_l.add(R.drawable.piranham1);
		comer_l.add(R.drawable.piranham2);
		comer_l.add(R.drawable.piranham1);
		comer_l.add(R.drawable.piranhab);
		comer_l.setCiclesPerAnimation(15);
		comer_l.scale1();
		comer_l.setPhase(1);
		comer_l.setLoop(false);
		comer_l.setNumLoops(1);
		comer_l.setDebug(true);
		comer_l.startAnimation();
		comer_l.flipH();
		
		
		//comer_r.add(R.drawable.piranhab);
		comer_r.add(R.drawable.piranham1);
		comer_r.add(R.drawable.piranham2);
		comer_r.add(R.drawable.piranham1);
		comer_r.add(R.drawable.piranhab);
		comer_r.setCiclesPerAnimation(15);
		comer_r.scale1();
		comer_r.setLoop(false);
		comer_r.setNumLoops(1);
		comer_r.setPhase(1);
		comer_r.setDebug(true);
		comer_r.startAnimation();
		
		//comer_l.add(R.drawable.piranhab);
		comer_gato_l.add(R.drawable.piranhacomegato);
		comer_gato_l.add(R.drawable.piranhacomegato);
		comer_gato_l.add(R.drawable.piranhacomegato);
		comer_gato_l.add(R.drawable.piranham1);
		comer_gato_l.add(R.drawable.piranham2);
		comer_gato_l.add(R.drawable.piranham1);
		comer_gato_l.add(R.drawable.piranhab);
		comer_gato_l.setCiclesPerAnimation(20);
		comer_gato_l.scale1();
		comer_gato_l.setPhase(1);
		comer_gato_l.setLoop(false);
		comer_gato_l.setNumLoops(0);
		comer_gato_l.setDebug(true);
		comer_gato_l.startAnimation();
		comer_gato_l.flipH();
		
		
		//comer_r.add(R.drawable.piranhab);
		comer_gato_r.add(R.drawable.piranhacomegato);
		comer_gato_r.add(R.drawable.piranhacomegato);
		comer_gato_r.add(R.drawable.piranhacomegato);
		comer_gato_r.add(R.drawable.piranham1);
		comer_gato_r.add(R.drawable.piranham2);
		comer_gato_r.add(R.drawable.piranham1);
		comer_gato_r.add(R.drawable.piranhab);
		comer_gato_r.setCiclesPerAnimation(20);
		comer_gato_r.scale1();
		comer_gato_r.setLoop(false);
		comer_gato_r.setNumLoops(1);
		comer_gato_r.setPhase(0);
		comer_gato_r.setDebug(true);
		comer_gato_r.startAnimation();
		
		
		nadar_r.add(R.drawable.piranhaa);
		nadar_r.add(R.drawable.piranhab);
		nadar_r.add(R.drawable.piranhac);
		nadar_r.add(R.drawable.piranhab);
		nadar_r.setCiclesPerAnimation(6);
		nadar_r.setPhase(1);
		nadar_r.startAnimation();
		nadar_r.scale1();
		
		nadar_l.add(R.drawable.piranhaa);
		nadar_l.add(R.drawable.piranhab);
		nadar_l.add(R.drawable.piranhac);
		nadar_l.add(R.drawable.piranhab);
		nadar_l.setCiclesPerAnimation(6);
		nadar_l.setPhase(1);
		nadar_l.startAnimation();
		nadar_l.flipH();
		
		//Por que lado de la pantalla aparece?
		if (sr.nextInt(2) == 0) {
			//Sale por la Izquierda
			x = -10;
			targetX = canvasW+10;
			setEstado(NADANDO_R,false);
		} else {
			//Sale por la derecha
			x  = canvasW + 10;
			targetX = -10;
			setEstado(NADANDO_L,false);
		}
		
		//A que altura sale?
		targetY = y = sr.nextInt(canvasH-GameThread.PIRANHA_SURFACE-GameThread.PIRANHA_DOWN_MARGIN-getHeight())+GameThread.PIRANHA_SURFACE;
		
		setSpeed(2);
		startMoving();
	}
	
	public int getEstado() {
		return estado;
	}

	private void setEstado(int stat, boolean priority) {
		//optimizar con los casos más probables primero
		
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
				return (bComiendoGato?comer_gato_r:comer_r);
				
			case COMIENDO_L:
				return (bComiendoGato?comer_gato_l:comer_l);
			
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
	
	public void doComer(boolean gato) {
		bComiendoGato = gato;
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
				//setEstado(COMIENDO_L, true);
				setEstado(NADANDO_L, true);
				break;
			case COMIENDO_L:
				setEstado(NADANDO_R, true);
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
		switch(sr.nextInt(GameThread.PIRANHA_ENTROPY)) {
		//switch(2) {
			
			case 0:	//Girar
				turn();
				break;
				
			case 2: //Subir
				//Log.d("Piranha","y:"+y+" surface:"+(GameThread.PIRANHA_SURFACE+GameThread.PIRANHA_DIRECTION_VARIATION));
				if (targetY > (GameThread.PIRANHA_SURFACE+GameThread.PIRANHA_DIRECTION_VARIATION+getHeight()))
					targetY -= GameThread.PIRANHA_DIRECTION_VARIATION;
				break;
				
			case 1: //Bajar
				if (targetY < (canvasH-GameThread.PIRANHA_DOWN_MARGIN-GameThread.PIRANHA_DIRECTION_VARIATION-getHeight()))
					targetY += GameThread.PIRANHA_DIRECTION_VARIATION;
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
		//doComer();
	}
	
	
}
