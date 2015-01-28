package com.mybird.flappy;

import java.security.PublicKey;

import android.util.Log;

import com.mybird.frameword.Game;
import com.mybird.frameword.Graphics;
import com.mybird.frameword.Graphics.PixmapFormat;
import com.mybird.frameword.Screen;

public class LoadingScreen extends Screen{
    public LoadingScreen(Game game) {
        super(game);
    }
    
    public void update(float deltaTime){
        Graphics g = game.getGraphics();
        Assets.background = g.newPixmap("background.png", PixmapFormat.RGB565);
        Assets.bird = g.newPixmap("bird.png", PixmapFormat.ARGB8888);
        Assets.bird_1 = g.newPixmap("bird_1.png", PixmapFormat.ARGB8888);
        Assets.bird_2 = g.newPixmap("bird_2.png", PixmapFormat.ARGB8888);
        Assets.bird_3 = g.newPixmap("bird_3.png", PixmapFormat.ARGB8888);
        Assets.atlas = g.newPixmap("atlas.png", PixmapFormat.ARGB8888);
        Assets.grass = g.newPixmap("grass.png", PixmapFormat.RGB565);
        Assets.gameover = g.newPixmap("gameover.png", PixmapFormat.ARGB8888);
        Assets.lastscore = g.newPixmap("lastscore.png", PixmapFormat.ARGB8888);
        Assets.start = g.newPixmap("start.png", PixmapFormat.ARGB8888);
        Assets.rate = g.newPixmap("rate.png", PixmapFormat.ARGB8888);
        Assets.ready = g.newPixmap("ready.png", PixmapFormat.ARGB8888);
        Assets.number_0 = g.newPixmap("number0.png", PixmapFormat.ARGB8888);
        Assets.number_1 = g.newPixmap("number1.png", PixmapFormat.ARGB8888);
        Assets.number_2 = g.newPixmap("number2.png", PixmapFormat.ARGB8888);
        Assets.number_3 = g.newPixmap("number3.png", PixmapFormat.ARGB8888);
        Assets.number_4 = g.newPixmap("number4.png", PixmapFormat.ARGB8888);
        Assets.number_5 = g.newPixmap("number5.png", PixmapFormat.ARGB8888);
        Assets.number_6 = g.newPixmap("number6.png", PixmapFormat.ARGB8888);
        Assets.number_7 = g.newPixmap("number7.png", PixmapFormat.ARGB8888);
        Assets.number_8 = g.newPixmap("number8.png", PixmapFormat.ARGB8888);
        Assets.number_9 = g.newPixmap("number9.png", PixmapFormat.ARGB8888);
        Assets.white = g.newPixmap("white.png", PixmapFormat.ARGB8888);
        Assets.sound_off = g.newPixmap("sound_off.png", PixmapFormat.ARGB8888);
        Assets.sound_on = g.newPixmap("sound_on.png", PixmapFormat.ARGB8888);
        
        try {
            Assets.die = game.getAudio().newSound("sfx_die.ogg");
            Assets.hit = game.getAudio().newSound("sfx_hit.ogg");
            Assets.point = game.getAudio().newSound("sfx_point.ogg");
            Assets.swooshing = game .getAudio().newSound("sfx_swooshing.ogg");
            Assets.wing = game.getAudio().newSound("sfx_wing.ogg");
		} catch (Exception e) {
			e.printStackTrace();
		}


        game.setScreen(new GameScreen(game));
    }


    
    public void present(float deltaTime) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void dispose() {

    }

}
