package com.mybird.flappy;

import com.bird.implement.AndroidGame;
import com.mybird.frameword.Screen;

public class FlappyBird extends AndroidGame{
	public Screen getStartScreen(){
		return new LoadingScreen(this);
	}
}
