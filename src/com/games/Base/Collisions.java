package com.games.Base;

import java.util.Iterator;
import java.util.Vector;

import com.games.Sprites.Button;
import com.games.Sprites.Pic;
import com.games.Sprites.Sprite;


public class Collisions {
	public static final int COLLISION_MARGIN = 15;
	public static final int SCREEN_BORDER = 2;
	
	public static final int COLLISION_NONE = 0;
	public static final int COLLISION_UP = 1;
	public static final int COLLISION_DOWN = 2;
	public static final int COLLISION_LEFT = 3;
	public static final int COLLISION_RIGHT = 4;
	public static final int COLLISION_UPLEFT = 5;
	public static final int COLLISION_DOWNLEFT = 6;
	public static final int COLLISION_UPRIGHT = 7;
	public static final int COLLISION_DOWNRIGHT = 8;
	public static final int COLLISION_MULTIPLE = 9;

	
	public static void insideScreen(Sprite spr, double canvasWidth, double canvasHeight) {
		
		if (spr.x < SCREEN_BORDER)
			spr.onBorderTouch(COLLISION_LEFT);
		
		else if (spr.x+spr.getWidth() > canvasWidth- SCREEN_BORDER)
			spr.onBorderTouch(COLLISION_RIGHT);
		
		else if (spr.y  < SCREEN_BORDER)
			spr.onBorderTouch(COLLISION_UP);
		
		if (spr.y + spr.getHeight() >= canvasHeight - SCREEN_BORDER)
			spr.onBorderTouch(COLLISION_DOWN);
		
	}
	
	public static boolean colisionPuntoPlano(double x, double y, Button sh, boolean margen) {
    	if (margen)
    		return (x >= sh.x-COLLISION_MARGIN && x <= sh.x+sh.getWidth()+COLLISION_MARGIN && y >= sh.y-COLLISION_MARGIN && y <= sh.y+sh.getHeight()+COLLISION_MARGIN);
    	else
    		return (x >= sh.x && x <= sh.x+sh.getWidth() && y >= sh.y && y <= sh.y+sh.getHeight());
    }
	
	
	public static boolean colisionPuntoPlano(double x, double y, Sprite sh, boolean margen) {
    	if (margen)
    		return (x >= sh.x-COLLISION_MARGIN && x <= sh.x+sh.getWidth()+COLLISION_MARGIN && y >= sh.y-COLLISION_MARGIN && y <= sh.y+sh.getHeight()+COLLISION_MARGIN);
    	else
    		return (x >= sh.x && x <= sh.x+sh.getWidth() && y >= sh.y && y <= sh.y+sh.getHeight());
    }
	
    public static int colisionPlanoPlano(Sprite spr1, Sprite spr2) {
    	
    	if (colisionPuntoPlano(spr2.x, spr2.y, spr1, false))
    		return COLLISION_UPLEFT;
    	
    	if (colisionPuntoPlano(spr2.x+spr2.getWidth(), spr2.y, spr1, false))
    		return COLLISION_UPRIGHT;
    	
    	if (colisionPuntoPlano(spr2.x, spr2.y+spr2.getHeight(), spr1, false))
    		return COLLISION_DOWNLEFT;
    	
    	if (colisionPuntoPlano(spr2.x+spr2.getWidth(), spr2.y+spr2.getHeight(), spr1, false))
    		return COLLISION_DOWNRIGHT;
    	  	
    	boolean upleft = colisionPuntoPlano(spr1.x, spr1.y, spr2, false);
    	boolean downleft = colisionPuntoPlano(spr1.x, spr1.y+spr1.getHeight(), spr2, false);
    	boolean upright = colisionPuntoPlano(spr1.x+spr1.getWidth(), spr1.y, spr2, false);
    	boolean downright = colisionPuntoPlano(spr1.x+spr1.getWidth(), spr1.y+spr1.getHeight(), spr2, false);
    	
    	if ((downright || downleft) && !upright && !upleft)
    		return COLLISION_UP;
    	
    	if ((upright || upleft) && !downright && !downleft)
    		return COLLISION_DOWN;
    	
    	if ((downleft || upleft) && !upright && !downright)
    		return COLLISION_RIGHT;
    	
    	if ((upright || downright) && !downleft && !upleft)
    		return COLLISION_LEFT;
      
   
    	return COLLISION_NONE;
    }

    public static int colisionPlanoPlanos(Sprite sh1, Vector<Sprite> shs2) {
    	//java.util.Iterator<Sprite> it = shs2.iterator();
    	int ret = COLLISION_NONE;
    	int r;
    	int colisiones = 0;
    	for (Sprite sh2 : shs2) {
    		if (sh1.id != sh2.id) {
    			if ((r = colisionPlanoPlano(sh1, sh2)) != COLLISION_NONE)  {
    				ret = r;
    				if (++colisiones > 1)  
    					return COLLISION_MULTIPLE;
    			}
    		}
    	}
    	return ret;
    }
    
    public static Sprite whoIsHere(double x, double y, Vector<Sprite> vehicle) {
    	Iterator<Sprite> it = vehicle.iterator();
    	while (it.hasNext()) {
    		Sprite s = it.next();
    		if (colisionPuntoPlano(x, y, s, true))
    			return s;
    	}
    	return null;
    }
    
    public static int angle2quadrant(double angle) {
    	if (angle >= 0 && angle < 90)
    		return 4;
    	if (angle >= 90 && angle < 180)
    		return 3;
    	if (angle >=180 && angle < 270)
    		return 2;
    	if (angle >= 270 && angle < 360)
    		return 1;
    	
    	
    	return 0;
    	
    }
	
    public static double realAngle(double diffX, double diffY) {
    	double angle =  Math.atan2(diffY,diffX)*180/Math.PI+180;
    	
    	if (angle >= 360)
    		angle -= 360;
    	if (angle <= -360)
    		angle += 360;
    	
    	return angle;
    	
    }
    

	/*
    public boolean collision(double xx, double yy) {
    	return (xx >= x && xx <= x+getWidth() && yy >= y && yy <= y+getHeight());
    }*/
    
}
