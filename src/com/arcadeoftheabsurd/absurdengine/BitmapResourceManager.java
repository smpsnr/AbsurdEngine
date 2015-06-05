package com.arcadeoftheabsurd.absurdengine;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapResourceManager 
{
	private Bitmap bitmaps[];
	private int index = 0;
	private HashMap<Integer, Integer> names = new HashMap<Integer, Integer>();
	private Resources res;
		
	public BitmapResourceManager(Resources res, int size) {
		this.res = res;
		bitmaps = new Bitmap[size];
	}
	
	public void loadBitmap(int id) {
		bitmaps[index] = BitmapFactory.decodeResource(res, id);
		names.put(id, index);
		index++;
	}
	
	public Bitmap getBitmap(int id) {
		return bitmaps[names.get(id)];
	}
}
