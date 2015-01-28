package com.bird.implement;

import android.graphics.Bitmap;

import com.mybird.frameword.Pixmap;
import com.mybird.frameword.Graphics.PixmapFormat;

public class AndroidPixmap implements Pixmap{
	Bitmap bitmap;
	PixmapFormat pixmapFormat;
	
	public AndroidPixmap(Bitmap bitmap,PixmapFormat pixmapFormat){
		this.bitmap = bitmap;
		this.pixmapFormat = pixmapFormat;
	}
	
    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public PixmapFormat getFormat() {
        return pixmapFormat;
    }
    
    public void dispose() {
        bitmap.recycle();
    }   
    
    public AndroidPixmap rotateUp_45(int degree){
    	Bitmap bitmap = this.bitmap;
    	return new AndroidPixmap(AndroidGraphics.rotate(bitmap, degree), this.pixmapFormat);
    }
}
