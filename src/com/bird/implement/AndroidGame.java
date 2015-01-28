package com.bird.implement;

import cn.jpush.android.api.JPushInterface;

import com.mybird.frameword.Audio;
import com.mybird.frameword.FileIO;
import com.mybird.frameword.Game;
import com.mybird.frameword.Graphics;
import com.mybird.frameword.Input;
import com.mybird.frameword.Screen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public abstract class AndroidGame extends Activity  implements Game{
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 480 : 320;
		int frameBufferHeight = isLandscape ? 320 : 480;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565); // Important!	
		float scaleX = (float) frameBufferWidth
				/ getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) frameBufferHeight
				/ getWindowManager().getDefaultDisplay().getHeight();
		
		renderView = new AndroidFastRenderView (this,frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(this);
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView); // Important!
		

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"GLGame");					
		init();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
		JPushInterface.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		JPushInterface.onPause(this);

		if (isFinishing())
			screen.dispose();
	}

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public Audio getAudio() {
		return audio;
	}
	
	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}
	
	public Screen getCurrentScreen(){
		return screen;
	}
	
	public void share(String message){
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.putExtra(Intent.EXTRA_TEXT, message);
		shareIntent.setType("text/plain");
		startActivity(Intent.createChooser(shareIntent, "分享到以下内容~"));
	}
	
	private void init(){
		 JPushInterface.init(getApplicationContext());
	} 
}
