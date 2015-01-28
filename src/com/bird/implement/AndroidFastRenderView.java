package com.bird.implement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable{
	AndroidGame game;
	Bitmap frambuffer ;
	Thread renderThread;
	SurfaceHolder holder;
	volatile boolean running = false;
	
	public AndroidFastRenderView(AndroidGame game,Bitmap frambuffer){
		super(game);
		this.game = game ;
		this.frambuffer = frambuffer;
		this.holder = getHolder();
	}
	
    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }  
    
    public void run(){
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        try {
            while(running){
            	if(!holder.getSurface().isValid()){
            		continue;       	
            	}
            	float deltaTime = (System.nanoTime()-startTime)/ 1000000000.0f;
            	startTime = System.nanoTime();
            	
                game.getCurrentScreen().update(deltaTime);
                game.getCurrentScreen().present(deltaTime);
                
                Canvas canvas = holder.lockCanvas();
                canvas.getClipBounds(dstRect);
    			canvas.drawBitmap(frambuffer, null, dstRect, null);
                holder.unlockCanvasAndPost(canvas);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

    }
    
    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
    } 
	
}
