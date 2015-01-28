package com.mybird.frameword;

import com.mybird.frameword.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();
	
	public int getHeight();
	
	public PixmapFormat getFormat();
	
	public void dispose();
	
	public Pixmap rotateUp_45(int degree);
}
