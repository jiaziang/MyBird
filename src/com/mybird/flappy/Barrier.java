package com.mybird.flappy;

import java.util.Random;

import android.R.integer;

public class Barrier {
	static final int UP = 0;
	static final int DOWN = 1;
	static final int WIDTH = 53; 
	static final int Y_MOVE_MAX=20;
	int direction;  //障碍移动方向 1下  -1上
	
	public float x, y;
	public float y_flag;
	public float height;
	public float height_flag;
	int type;
	
	public Barrier(int x,int y,int type,int direction){
		this.x = x;		
		this.type = type;
		this.direction = direction;
		if(type==UP){
			this.y = y;
			this.height= 370-y;
		}
		else{
			this.y=0;
			this.height= y-90; 
		}
		y_flag = this.y;
		height_flag = this.height;
	}
}
