package com.mybird.flappy;

import java.text.BreakIterator;
import java.util.Random;

import android.R.bool;

public class World {

	static final float TIME_INTERVAL = 1.5f;
	static final int INTERVAL = 90;
	static final int min_height = 40;
	static final int max_height = 240;

	public Bird bird;
	public AllBarrier allBarrier;
	public boolean gameOver = false;
	public int score = 0;
	public float min_x =320;
	public float time = 0;
	public Barrier flagBarrier;
	Random random = new Random();
	protected boolean isSoundOn ;

	public World() {
		bird = new Bird(100, 200);
		allBarrier = new AllBarrier();
	}

	public void update(float deltaTime) {
		if (gameOver)
			return;
		bird.move(deltaTime);

		// 碰撞判断
		if (isCrashed()||bird.isGameOver == true) {
			if(isSoundOn==true){
				Assets.hit.play(1);	
				Assets.die.play(1);
			}

			gameOver = true;
			return;
		}				
		//新增障碍
		time += deltaTime;
		while (time > TIME_INTERVAL) {
			time -= TIME_INTERVAL;
			allBarrier.newBarrier(320,
					INTERVAL + min_height + random.nextInt(200));
		}
		
		//移动障碍
		allBarrier.update(deltaTime);
		
		//得分判断
		if(flagBarrier!=null&&flagBarrier.x<=100+Bird.WIDTH/2-Barrier.WIDTH/2){
			score++;
			if(isSoundOn==true){
				Assets.point.play(1);
			}

		}

	}
	
	public void birdFall(float deltaTime){
		bird.move(deltaTime);
	}
	
	//碰撞判断
	public boolean isCrashed() {
		min_x=320;
		for (Barrier theBarrier : allBarrier.barriers) {
			if(theBarrier.type == Barrier.DOWN){
				if(bird.x>theBarrier.x-Bird.WIDTH&&bird.x<theBarrier.x+Barrier.WIDTH&&bird.y<theBarrier.height){
					return true;
				}
			}			
			if(theBarrier.type == Barrier.UP){
				if((bird.x>theBarrier.x - Bird.WIDTH&&bird.x<theBarrier.x&&bird.y>theBarrier.y-Bird.HEIGHT&&bird.y<480-Bird.HEIGHT)||(bird.x>theBarrier.x-Bird.WIDTH&&bird.x<theBarrier.x+Barrier.WIDTH&&bird.y>theBarrier.y-Bird.HEIGHT&&bird.y<theBarrier.y)){
					return true;
				}
			}			
			//得到在小鸟右边且最靠近小鸟的障碍
			if(theBarrier.x<min_x&&theBarrier.x>100+Bird.WIDTH/2-Barrier.WIDTH/2){
				min_x = theBarrier.x;
				flagBarrier = theBarrier;
			}
		}	
			return false;
		
	}
}
