package com.games.Base;

import java.util.HashMap;


public class Scheduler {

	private double currTick = 0;
	private HashMap<Integer,Double> timeouts = new HashMap<Integer,Double>(); //TODO: optimizar con SparseArrary
	private HashMap<Integer,Double> intervals = new HashMap<Integer,Double>();
	private HashMap<Integer,Double> intervalsDefault = new HashMap<Integer,Double>();
	
	
	public void setTimeout(double tick, int code) {
		timeouts.put(code,currTick+tick);
	}
	
	public void setInterval(double tick, int code) {
		intervals.put(code,tick+currTick);
		intervals.put(code,tick);
	}
	
	public void doTick() {
		currTick++;
	}
	
	public boolean isTimeout(int code) {
		boolean veredict;
		
		try {
			veredict = (currTick >= timeouts.get(code));
			timeouts.remove(code);
			
		} catch (Exception e) {
			veredict = false;
		} 
		
		return veredict;
	}
	
	public boolean isInterval(int code) {
		
		try {
			
			if (currTick < intervals.get(code))
				return false;
			
			intervals.put(code, currTick + intervalsDefault.get(code));
			return true;
			
		} catch (Exception e) {
			return false;
		}

	}
	

}
