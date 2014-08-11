package com.mobfox.adsdk.nativeads;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arcadeoftheabsurd.absurdengine.Sprite;
import com.arcadeoftheabsurd.absurdengine.WebUtils;
import com.mobfox.adsdk.nativeads.NativeAd.Tracker;

public class NativeAdView extends FrameLayout
{
	private Context context;
	private NativeBanner bannerView;
	private boolean impressionReported = false;
	private List<Tracker> trackers;

	class NativeBanner extends RelativeLayout 
	{
		Sprite sprite;
		TextView descriptionView;
		LayoutParams descriptionLayout; 
		
		public NativeBanner(Context context, Sprite sprite, String description) {
			super(context);
			this.sprite = sprite;
			
			descriptionView = new TextView(context);
			descriptionView.setText(description);
			descriptionView.setTextColor(Color.WHITE);
			descriptionLayout = new LayoutParams(NativeAdView.this.getWidth() - sprite.getWidth(), NativeAdView.this.getHeight());
			descriptionLayout.addRule(CENTER_VERTICAL);
			descriptionLayout.addRule(ALIGN_PARENT_RIGHT);
			
			addView(descriptionView, descriptionLayout);
		}
		
		@Override
		protected void dispatchDraw(Canvas canvas) {
			super.dispatchDraw(canvas);
			sprite.setLocation(0, -20);
			sprite.draw(canvas);
						
			System.out.println("banner width: " + getWidth() + " banner height: " + getHeight());
			System.out.println("sprite width: " + sprite.getWidth() + " sprite height: " + sprite.getHeight());
		}
	}

	public NativeAdView(Context context) {
		super(context);
		this.context = context;
	}
	
	@SuppressWarnings("deprecation")
	public void setAd(NativeAd ad) {
		if (bannerView != null) {
			this.removeView(bannerView);
		}
		trackers = ad.getTrackers();
		bannerView = new NativeBanner(context, ad.getImageAsset("icon").sprite, ad.getTextAsset("description"));
		addView(bannerView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	
		invalidate();
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (trackers != null) {
			if (!impressionReported) {
				impressionReported = true;
								
				for (Tracker t : trackers) {
					if (t.type.equals("impression")) {
						try {
							System.out.println("tracking impression...");
							System.out.println(WebUtils.restRequest(t.url));
						} catch (IOException e) {
							System.out.println("impression failed");
						}
					}
				}
			}
		}
		super.dispatchDraw(canvas);
	}
}
