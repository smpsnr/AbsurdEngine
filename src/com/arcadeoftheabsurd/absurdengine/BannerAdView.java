package com.arcadeoftheabsurd.absurdengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.TextView;

import com.adsdk.sdk.nativeads.NativeAdView;

/**
 * View that displays a NativeAd as a horizontal banner: [ad icon] [textMarginLeft] [ad description] [textMarginRight]
 * @author sam
 */

public class BannerAdView extends NativeAdView
{
	private Sprite sprite;
	private TextView spacerView;
	private TextView descriptionView;
	
	private int textSize;
	private int textMarginLeft;
	private int textMarginRight;
		
	private boolean readyToWrite = false;
	private int descriptionWidth;
	private char[] descriptionChars;
	private StringBuilder descriptionBuffer;
	private int curChar;
	
	public BannerAdView(Context context, int textSize, int textMarginLeft, int textMarginRight) {
		super(context);
		this.textSize = textSize;
		this.textMarginLeft = textMarginLeft;
		this.textMarginRight = textMarginRight;
		
		setOrientation(HORIZONTAL);
		spacerView = new TextView(context);
		descriptionView = new TextView(context);
	}
	
	protected void setAssets(Sprite sprite, String description) {
		this.sprite = sprite;
		this.sprite.setLocation(0, 0);
		descriptionChars = description.toCharArray();
		
		descriptionWidth = (this.getWidth() - sprite.getWidth()) - (textMarginLeft + textMarginRight);
		descriptionBuffer = new StringBuilder();
		curChar = 0;
		
		descriptionBuffer.append(descriptionChars[curChar++]);
		
		descriptionView.setText(descriptionBuffer.toString());
		descriptionView.setTextColor(Color.WHITE);
		descriptionView.setTextSize(textSize);
		
		readyToWrite = true;
		
		addView(spacerView, new LayoutParams(sprite.getWidth() + textMarginLeft, sprite.getHeight()));
		addView(descriptionView, new LayoutParams(descriptionWidth, sprite.getHeight()));		
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (sprite != null) {
			sprite.draw(canvas);
		}		
		super.dispatchDraw(canvas);
		
		if (readyToWrite) {
			if (curChar < descriptionChars.length) {
				descriptionBuffer.append(descriptionChars[curChar++]);
				
				if (descriptionView.getWidth() > descriptionWidth) {
					descriptionBuffer.insert(curChar, " ");
				}
				descriptionView.setText(descriptionBuffer.toString());
				dispatchDraw(canvas);
			} else {
				readyToWrite = false;
			}
		}
	}
}
