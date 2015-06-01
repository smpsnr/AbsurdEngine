package com.arcadeoftheabsurd.absurdengine;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmapResourceManager 
{
	private Bitmap bitmaps[];
	private int index = 0;
	private HashMap<String, Integer> names = new HashMap<String, Integer>();
		
	public BitmapResourceManager(int size) {
		bitmaps = new Bitmap[size];
	}
	
	public void loadBitmap(Resources res, int id) {
		bitmaps[index] = BitmapFactory.decodeResource(res, id);
		names.put(res.getString(id), index);
		index++;
	}
	
	public Bitmap getBitmap(String name) {
		return bitmaps[names.get(name)];
	}
}
