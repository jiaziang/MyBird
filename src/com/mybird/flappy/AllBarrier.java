package com.mybird.flappy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.integer;
import android.util.Log;

public class AllBarrier {
	static final int speedX = 132;
	static final int speedY = 30;
	Random random ;
	public List<Barrier> barriers = new ArrayList<Barrier>();
	public List<Barrier> copyBarriers = new ArrayList<Barrier>();	
	public AllBarrier(){
		random = new Random();
	}
	
	public void newBarrier(int x,int y){
		int rad = random.nextInt(2);
		int direction;
		if(rad==0){
			direction = 1;
		}
		else{
			direction = -1;
		}
		copyBarriers.clear();
		Barrier downBarrier = new Barrier(x, y,Barrier.DOWN,direction);
		copyBarriers.add(downBarrier);
		Barrier upBarrier = new Barrier(x, y, Barrier.UP,direction);
		copyBarriers.add(upBarrier);
		barriers.addAll(copyBarriers);
	}
	
	public void update(float deltaTime){
		copyBarriers.clear();
		Float deltaXMove = new Float(speedX*deltaTime);
		float deltaYMove = speedY*deltaTime;
		for(Barrier theBarrier:barriers){		
			if(theBarrier.x<(0-Barrier.WIDTH)){
				copyBarriers.add(theBarrier);	
			}			
			else{
				theBarrier.x-=deltaXMove;
				if(theBarrier.type == Barrier.UP){
					theBarrier.y+=deltaYMove*theBarrier.direction;
					theBarrier.height-=deltaYMove*theBarrier.direction;
					if(theBarrier.y>theBarrier.y_flag+Barrier.Y_MOVE_MAX||theBarrier.y<theBarrier.y_flag-Barrier.Y_MOVE_MAX){
						theBarrier.direction*=(-1);
					}
				}
				else{
					theBarrier.height+=deltaYMove*theBarrier.direction;
					if(theBarrier.height>theBarrier.height_flag+Barrier.Y_MOVE_MAX||theBarrier.height<theBarrier.height_flag-Barrier.Y_MOVE_MAX){
						theBarrier.direction*=(-1);
					}
				}
			}
		}
		if(copyBarriers.size()>0){
			barriers.removeAll(copyBarriers);
		}
			
		
	}

}
