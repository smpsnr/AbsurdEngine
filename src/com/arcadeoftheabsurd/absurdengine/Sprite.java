package com.arcadeoftheabsurd.absurdengine;

import com.arcadeoftheabsurd.j_utils.Vector2d;

import android.graphics.Rect;

/**
 * Represents a bitmap on the screen
 * @author sam
 */

public class Sprite
{   
	BitmapHolder bitmapHolder;
	
    private boolean resized = false;
    private Rect bounds;
    private Vector2d pos;   
    
	Sprite(BitmapHolder bitmapHolder, int x, int y) {    
		this.bitmapHolder = bitmapHolder;
		pos = new Vector2d(x, y);
    	bounds = new Rect();
    	bounds.set(x, y, x + bitmapHolder.getInitialWidth(), y + bitmapHolder.getInitialHeight());
    }
    
    public void setLocation(int x, int y) {
    	pos.set(x, y);
    	bounds.set(pos.x, pos.y, pos.x + bounds.width(), pos.y + bounds.height());
    }
    
    public void translate(int dx, int dy) {
    	pos.offset(dx, dy);
    	bounds.set(pos.x, pos.y, pos.x + bounds.width(), pos.y + bounds.height());
    }
    
    public void resize(int width, int height) {
    	if (width == bitmapHolder.getInitialWidth() && height == bitmapHolder.getInitialHeight()) {
    		resized = false;
    	} else {
    		resized = true;
    	}
    	bounds.set(pos.x, pos.y, pos.x + width, pos.y + height);
    }
    
    public boolean isResized() {
    	return resized;
    }
    
    public int getX() {
    	return pos.x;
    }
    
    public int getY() {
    	return pos.y;
    }
    
    public int getWidth() {
    	return bounds.width();
    }
    
    public int getHeight() {
    	return bounds.height();
    }
    
    public Rect getBounds() {
		return bounds;
	}
}
