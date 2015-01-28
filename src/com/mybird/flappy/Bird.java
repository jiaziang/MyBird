package com.mybird.flappy;

import android.R.integer;

public class Bird {
	static final int WIDTH = 34;
	static final int HEIGHT = 25;
	static final int JUMP_SPEED=-220;
	
	public int x;
	public int y;
	public float speedY=0;
	public static int accelerationSpeed = 725; 
	public boolean isGameOver = false;
	
	
	public Bird(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void jump(){
		speedY =JUMP_SPEED;
	}
	
	public void move(float deltaTime){
		y+=deltaTime*speedY;
		speedY+=accelerationSpeed*deltaTime;
		if(y>=370-HEIGHT){
			y=370-HEIGHT;
			isGameOver =true;
		}
		if(y<=0){
			speedY = 80;
		}
	}

}
