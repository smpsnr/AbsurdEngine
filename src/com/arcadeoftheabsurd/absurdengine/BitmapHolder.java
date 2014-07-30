package com.arcadeoftheabsurd.absurdengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.arcadeoftheabsurd.j_utils.Vector2d;

/**
* Holds a Bitmap and information relevant to GameView, provides convenience methods
* @author sam
*/

class BitmapHolder
{
	Bitmap bitmap;
	boolean initialized = false;
	
	private Vector2d initialSize;
	
	BitmapHolder(Bitmap bitmap, int initialWidth, int initialHeight) {
		this.bitmap = bitmap;
		initialSize = new Vector2d(initialWidth, initialHeight);
	}
	
	Bitmap scaleCopy(int width, int height) {
		Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

    	Canvas canvas = new Canvas();
    	canvas.setBitmap(newBitmap);
    	canvas.scale((float) width / bitmap.getWidth(), (float) height / bitmap.getHeight());
    	
    	canvas.drawBitmap(bitmap, 0, 0, null);
    	
    	return newBitmap;
	}
	
	void initialize() {
		scaleInitial();
		initialized = true;
	}
	
	void scaleInitial() {    	
    	bitmap = scaleCopy(initialSize.x, initialSize.y);
	}
	
	int getInitialWidth() {
		return initialSize.x;
	}
	
	int getInitialHeight() {
		return initialSize.y;
	}
}
