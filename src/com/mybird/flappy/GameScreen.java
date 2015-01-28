package com.mybird.flappy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.R.integer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.IntentSender.SendIntentException;
import android.net.NetworkInfo.DetailedState;
import android.util.Log;

import com.mybird.frameword.Game;
import com.mybird.frameword.Graphics;
import com.mybird.frameword.Pixmap;
import com.mybird.frameword.Input.TouchEvent;
import com.mybird.frameword.Screen;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver
	}

	private static final float SWING_TIME = 0.1f;
	private static final int SCORE_WIDTH = 30;
	private static final int SCORE_HEIGHT = 36;
	public boolean isSoundOn;
	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;
	GameState state = GameState.Ready;
	World world;
	int birdFlag = 1;
	float time = 0;
	float gameoverTimeFlag = 0.0f;
	int gameoverFlag = 0;

	public GameScreen(Game game) {
		super(game);
		world = new World();
		try {
			sharedPreferences = game.getFileIO().getPreferences();
			editor = sharedPreferences.edit();
			boolean isSound = sharedPreferences.getBoolean("isSound", true);
			this.isSoundOn = isSound;
			world.isSoundOn = isSound;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();

		if (state == GameState.Ready) {
			updateReady(touchEvents);
		}
		if (state == GameState.Running) {
			updateRunning(touchEvents, deltaTime);
		}
		if (state == GameState.Paused)
			updatePaused(touchEvents);
		if (state == GameState.GameOver)
			updateGameOver(touchEvents, deltaTime);

	}

	private void updateReady(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent touchEvent = touchEvents.get(i);
			if (touchEvent.type == TouchEvent.TOUCH_UP) {
				if (touchEvent.x >= 108 && touchEvent.x < 212
						&& touchEvent.y >= 320 && touchEvent.y < 378) {
					state = GameState.Running;
					world.bird.jump();
					if(isSoundOn==true){
						Assets.wing.play(1);
					}
				}
				if(touchEvent.x>=0&&touchEvent.x<64&&touchEvent.y>=0&&touchEvent.y<64){
					if(isSoundOn ==true){
						isSoundOn = false;		
						world.isSoundOn = false;
					}
					else {
						isSoundOn = true;
						world.isSoundOn = true;
					}
					editor.putBoolean("isSound", isSoundOn);
					editor.commit();
				}
			}
		}

	}

	public void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		gameoverFlag = 0;
		gameoverTimeFlag = 0;
		// 响应触控事件 小鸟往上飞
		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent touchEvent = touchEvents.get(i);
			if (touchEvent.type == TouchEvent.TOUCH_DOWN) {
				world.bird.jump();
				if(isSoundOn==true){
					Assets.wing.play(1);
				}

			}
		}

		world.update(deltaTime);
		// 若游戏结束 则储存最高分
		if (world.gameOver == true) {
			try {
				int hightestScore = sharedPreferences.getInt("score", 0);
				if (world.score > hightestScore) {
					editor.putInt("score", world.score);
					editor.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			state = GameState.GameOver;
			// world.allBarrier.barriers.clear();

		}
		// 改变小鸟翅膀状态
		time += deltaTime;
		while (time > SWING_TIME) {
			time -= SWING_TIME;
			if (birdFlag == 1) {
				birdFlag = 2;
				continue;
			}
			if (birdFlag == 2) {
				birdFlag = 3;
				continue;
			}
			if (birdFlag == 3) {
				birdFlag = 1;
				continue;
			}
		}
	}

	private void updatePaused(List<TouchEvent> touchEvents) {

	}

	private void updateGameOver(List<TouchEvent> touchEvents, float deltaTime) {
		int len = touchEvents.size();
		world.birdFall(deltaTime);
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_UP) {
				// 继续游戏
				if (gameoverFlag == 1 && event.x >= 25 && event.x < 129
						&& event.y >= 320 && event.y < 378) {
					game.setScreen(new GameScreen(game));
					return;
				}
				if (gameoverFlag == 1 && event.x >= 191 && event.x < 295
						&& event.y >= 320 && event.y < 378) {
					game.share("我在Flappy Bird变态版里得了" + world.score + "分，小伙伴们快来围观吧~");
				}

			}
		}
		if (gameoverTimeFlag < 2.0) {
			gameoverTimeFlag += deltaTime;
		}
		if (gameoverTimeFlag >= 2.0) {
			gameoverFlag = 1;
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.background, 0, 0);
		drawWorld(world);
		if (state == GameState.Ready)
			drawReadyUI();
		if (state == GameState.Running)
			drawRunningUI();
		if (state == GameState.Paused)
			drawPausedUI();
		if (state == GameState.GameOver)
			drawGameOverUI();
	}

	private void drawWorld(World world) {
		Graphics g = game.getGraphics();
		Bird bird = world.bird;
		List<Barrier> barriers = world.allBarrier.barriers;
		g.drawPixmap(Assets.grass, 0, 370);
		Pixmap barrierPixmap = Assets.atlas;

		// 绘制障碍
		for (Barrier theBarrier : barriers) {
			if (theBarrier.type == Barrier.DOWN) {
				g.drawPixmap(barrierPixmap, floatToInt(theBarrier.x),
						floatToInt(theBarrier.y), 112,
						floatToInt(1024 - theBarrier.height - 58),
						Barrier.WIDTH, floatToInt(theBarrier.height));
			}
			if (theBarrier.type == Barrier.UP) {
				g.drawPixmap(barrierPixmap, floatToInt(theBarrier.x),
						floatToInt(theBarrier.y), 0, 646, Barrier.WIDTH,
						floatToInt(theBarrier.height));
			}
		}

		// 绘制小鸟
		int x = bird.x;
		int y = bird.y;
		switch (birdFlag) {
		case 1:		
			g.drawPixmap(Assets.bird_1.rotateUp_45(rotateDegree(bird.speedY)), x, y);
			break;
		case 2:	
			g.drawPixmap(Assets.bird_2.rotateUp_45(rotateDegree(bird.speedY)), x, y);
			break;
		case 3:		
			g.drawPixmap(Assets.bird_3.rotateUp_45(rotateDegree(bird.speedY)), x, y);
			break;
		default:
			break;
		}
		
		// 绘制分数
		String line = String.valueOf(world.score);
		if (state != GameState.GameOver) {
			drawText(g, line, g.getWidth() / 2 - line.length() * SCORE_WIDTH
					/ 2, 10);
		}
	}

	private void drawReadyUI() {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.start, 108, 320);
		g.drawPixmap(Assets.ready, 67, 80);
		if(isSoundOn == true){
			g.drawPixmap(Assets.sound_on, 0, 0);
		}
		else {
			g.drawPixmap(Assets.sound_off, 0, 0);
		}
	}

	private void drawRunningUI() {
		
	}

	private void drawPausedUI() {
		
	}

	private void drawGameOverUI() {
		Graphics g = game.getGraphics();
		if(gameoverTimeFlag<0.1){
			g.drawPixmap(Assets.white, 0, 0, gameoverTimeFlag*10);
		}
		if(gameoverTimeFlag>=0.1&&gameoverTimeFlag<0.1){
			g.drawPixmap(Assets.white, 0, 0, 2-gameoverTimeFlag*10);
		}
		if (gameoverFlag == 1) {
			
			g.drawPixmap(Assets.gameover, 64, 90);			

			g.drawPixmap(Assets.lastscore, 47, 174);
			g.drawPixmap(Assets.start, 25, 320);
			g.drawPixmap(Assets.rate, 191, 320);
			try {
				sharedPreferences = game.getFileIO().getPreferences();
			} catch (Exception e) {
				e.printStackTrace();
			}
			int hightestScore = sharedPreferences.getInt("score", 0);
			String scoreString = String.valueOf(hightestScore);
			drawText(g, scoreString,
					g.getWidth() / 2 + 12 - scoreString.length() * SCORE_WIDTH
							/ 2, 230);
			String theString = String.valueOf(world.score);
			drawText(g, theString, g.getWidth() / 2 + 12 - theString.length()
					* SCORE_WIDTH / 2, 182);
		}

	}

	public void drawScore(Graphics g, String score) {
		g.drawString(140, 50, score);
	}

	public void drawText(Graphics g, String line, int x, int y) {
		int len = line.length();
		for (int i = 0; i < len; i++) {
			char character = line.charAt(i);
			switch (character) {
			case '0':
				g.drawPixmap(Assets.number_0, x, y);
				break;
			case '1':
				g.drawPixmap(Assets.number_1, x, y);
				break;
			case '2':
				g.drawPixmap(Assets.number_2, x, y);
				break;
			case '3':
				g.drawPixmap(Assets.number_3, x, y);
				break;
			case '4':
				g.drawPixmap(Assets.number_4, x, y);
				break;
			case '5':
				g.drawPixmap(Assets.number_5, x, y);
				break;
			case '6':
				g.drawPixmap(Assets.number_6, x, y);
				break;
			case '7':
				g.drawPixmap(Assets.number_7, x, y);
				break;
			case '8':
				g.drawPixmap(Assets.number_8, x, y);
				break;
			case '9':
				g.drawPixmap(Assets.number_9, x, y);
				break;

			default:
				break;
			}
			x += SCORE_WIDTH;
		}
	}

	@Override
	public void pause() {
		if (state == GameState.Running)
			state = GameState.Paused;
	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
	
	private static int rotateDegree(float speedY){
		if(speedY<0){
			return floatToInt((-Bird.JUMP_SPEED+speedY)/205*15*(-1));
		}
		if(speedY>=0&&speedY<205){
			return floatToInt((-Bird.JUMP_SPEED-speedY)/205*15*(-1));
		}
		if(speedY>=205&&speedY<615){
			return floatToInt((speedY+Bird.JUMP_SPEED)/410*90);
		}
		else {
			return 90;
		}
	} 

	public static int floatToInt(float f) {
		String a = new BigDecimal(String.valueOf(f)).setScale(0,
				BigDecimal.ROUND_HALF_UP).toString();
		return Integer.parseInt(a);
	}

}
