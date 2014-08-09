package com.arcadeoftheabsurd.absurdengine;

import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
* Holds a Bitmap created from a temporary file; deletes that file once the bitmap is initialized
* @author sam
*/

public class BitmapTempFileHolder extends BitmapHolder
{
	private static Random random = new Random();
	private String tempFileName;
	private Context context;
	
	public static BitmapTempFileHolder fromUrl(String url, int initialWidth, int initialHeight, Context context) throws IOException {
		String tempFileName = "temp" + random.nextInt();
		String filePath = WebUtils.downloadFile(url, tempFileName, context);
		return new BitmapTempFileHolder(
    			((BitmapDrawable) BitmapDrawable.createFromPath(filePath)).getBitmap(),
    			initialWidth, initialHeight, tempFileName, context);
	}
	
	BitmapTempFileHolder(Bitmap bitmap, int initialWidth, int initialHeight, String tempFileName, Context context) {
		super(bitmap, initialWidth, initialHeight);

		this.tempFileName = tempFileName;
		this.context = context;
	}

	@Override
	public void initialize() {
		super.initialize();
		context.deleteFile(tempFileName);
	}
}
