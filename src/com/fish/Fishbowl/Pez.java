package com.fish.Fishbowl;

import java.security.SecureRandom;
import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.games.Base.Collisions;
import com.games.Sprites.Animation;
import com.games.Sprites.Pic;
import com.games.Sprites.Sprite;

public class Pez extends Sprite {
	//public static final int FISH_PIRANHA = 1;
	public static final int FISH_PAYASO = 2;
	public static final int FISH_GLOBO = 3;
	public static final int FISH_STAR = 4;
	
	public static final int DIR_LEFT = 0;
	public static final int DIR_RIGHT = 1;
	private int fishtype;


	private int dir;
	private Animation anim_r;
	private Animation anim_l;
	private SecureRandom sr;
	
	public Pez(Context ctx, int type, int canvasWidth, int canvasHeight) {
		super();
	
		fishtype = type;
		canvasW = canvasWidth;
		canvasH = canvasHeight;
		sr = new SecureRandom();
		
		anim_r = new Animation(ctx);
		anim_l = new Animation(ctx);
		
		//Configurar animación y pecularidades de cada especie
		
		switch (fishtype) {
			case FISH_PAYASO:
				anim_r.add(R.drawable.payasoa);
				anim_r.add(R.drawable.payasob);
				anim_r.add(R.drawable.payasoc);
				anim_r.add(R.drawable.payasob);
				
				anim_l.add(R.drawable.payasoa);
				anim_l.add(R.drawable.payasob);
				anim_l.add(R.drawable.payasoc);
				anim_l.add(R.drawable.payasob);
				
				setSpeed(1);
				break;
				
			case FISH_GLOBO:
				anim_r.add(R.drawable.pezgloboa);
				anim_r.add(R.drawable.pezglobob);
				anim_l.add(R.drawable.pezgloboa);
				anim_l.add(R.drawable.pezglobob);
				setSpeed(2);
				break;
				
			case FISH_STAR:
				anim_l.add(R.drawable.estrellaa);
				anim_l.add(R.drawable.estrellab);
				anim_l.add(R.drawable.estrellac);
				anim_l.add(R.drawable.estrellad);
				anim_l.add(R.drawable.estrellae);
				anim_l.add(R.drawable.estrellaf);
				anim_l.add(R.drawable.estrellag);
				anim_l.add(R.drawable.estrellah);
				anim_l.add(R.drawable.estrellai);
				anim_l.add(R.drawable.estrellaj);
				anim_l.add(R.drawable.estrellak);
				anim_l.add(R.drawable.estrellal);
				anim_l.add(R.drawable.estrellam);
				anim_l.add(R.drawable.estrellan);
				anim_l.add(R.drawable.estrellao);
				anim_l.add(R.drawable.estrellap);
				anim_l.add(R.drawable.estrellaq);
				anim_l.add(R.drawable.estrellar);
				anim_l.add(R.drawable.estrellas);
				anim_l.add(R.drawable.estrellat);
				anim_l.add(R.drawable.estrellau);
				anim_l.add(R.drawable.estrellav);
				anim_l.add(R.drawable.estrellaw);
				
				anim_l.setCiclesPerAnimation(15);
				anim_l.setPhase(1);
				anim_l.scale1();
				anim_l.startAnimation();
				
				anim_r.add(R.drawable.estrellaw);
				anim_r.add(R.drawable.estrellav);
				anim_r.add(R.drawable.estrellau);
				anim_r.add(R.drawable.estrellat);
				anim_r.add(R.drawable.estrellas);
				anim_r.add(R.drawable.estrellar);
				anim_r.add(R.drawable.estrellaq);
				anim_r.add(R.drawable.estrellap);
				anim_r.add(R.drawable.estrellao);
				anim_r.add(R.drawable.estrellan);
				anim_r.add(R.drawable.estrellam);
				anim_r.add(R.drawable.estrellal);
				anim_r.add(R.drawable.estrellak);
				anim_r.add(R.drawable.estrellaj);
				anim_r.add(R.drawable.estrellai);
				anim_r.add(R.drawable.estrellah);
				anim_r.add(R.drawable.estrellag);
				anim_r.add(R.drawable.estrellaf);
				anim_r.add(R.drawable.estrellae);
				anim_r.add(R.drawable.estrellad);
				anim_r.add(R.drawable.estrellac);
				anim_r.add(R.drawable.estrellab);
				anim_r.add(R.drawable.estrellaa);
				
				anim_r.setCiclesPerAnimation(15);
				anim_r.setPhase(1);
				anim_r.scale1();
				anim_r.startAnimation();
				
				setSpeed(3);
				break;
		}
		
		if (fishtype != FISH_STAR) {
			anim_r.setCiclesPerAnimation(5);
			anim_r.setPhase(1);
			anim_r.startAnimation();
			anim_r.scale1();
			
			anim_l.setCiclesPerAnimation(5);
			anim_l.setPhase(1);
			anim_l.flipH();
			anim_l.startAnimation();
		}
		
		
		if (sr.nextInt(2) == 0) {
			//Sale por la Izquierda
			x = -10;
			turnRight();
		} else {
			//Sale por la derecha
			x  = canvasWidth + 10;
			turnLeft();
		}
		
		
		//altura a la que sale el pez
		targetY = y = sr.nextInt(canvasH-GameThread.FISH_SURFACE-GameThread.FISH_DOWN_MARGIN-getHeight())+GameThread.FISH_SURFACE;

		startMoving();
		
	}

	public int getFishtype() {
		return fishtype;
	}
	
	private void turnLeft() {
		targetX = -10;
		dir = DIR_LEFT;
	}
	
	private void turnRight() {
		targetX = canvasW+10;
		dir = DIR_RIGHT;
	}
	
	public void turn() {
		if (dir == DIR_LEFT)
			turnRight();
		else
			turnLeft();
	}
	

	public void doDraw(Canvas c) {
		
		
		if (dir == DIR_LEFT)    // ??? o_O
			anim_r.doDraw(c, x, y);
		else
			anim_l.doDraw(c, x, y);
	}
	

	@Override
	public void onBorderTouch(int c) {
		if (dir == DIR_LEFT) {
			if (c == Collisions.COLLISION_LEFT)
				turnRight();
		} else {
			if(c == Collisions.COLLISION_RIGHT)
				turnLeft();
		}
	}

	@Override
	public void onCollision(int c) {
		//turn();
	}

	@Override
	
	public int getWidth() {
		return anim_r.getWidth();  //OPTIMIZATION: anchura fija para toda la anim
	}

	@Override
	public int getHeight() {
		return anim_r.getHeight();
	}

}

