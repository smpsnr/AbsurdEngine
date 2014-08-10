package com.mobfox.adsdk.nativeads;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arcadeoftheabsurd.absurdengine.Sprite;
import com.mobfox.adsdk.nativeads.NativeAd.Tracker;

public class NativeAdView extends FrameLayout 
{
	private Context context;
	private boolean impressionReported;
	private NativeBanner bannerView;
	private List<Tracker> trackers;
	
	public class NativeBanner extends RelativeLayout 
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
			sprite.draw(canvas);
		}
		
		@Override
		protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
			this.invalidate();
		}
	}

	public NativeAdView(Context context) {
		super(context);
		this.context = context;
	}
	
	public void setAd(NativeAd ad) {
		if (bannerView != null) {
			this.removeView(bannerView);
		}
		bannerView = new NativeBanner(context, ad.getImageAsset("icon").sprite, ad.getTextAsset("description"));
		this.addView(bannerView);
	}

	/*@Override
	protected void dispatchDraw(Canvas canvas) {
		if (trackers != null) {
			if (!impressionReported) {
				impressionReported = true;

				for (Tracker t : trackers) {
					if (t.type.equals("impression")) {
						trackImpression(t.url);
					}
				}
			}
		}
		super.dispatchDraw(canvas);
	}*/

	/*private void notifyImpression() {
		if (listener != null) {
			handler.post(new Runnable() {
				public void run() {
					listener.impression();
				}
			});
		}
	}*/

	/*private void trackImpression(final String url) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet request = new HttpGet();
					request.setHeader("User-Agent", System.getProperty("http.agent"));
					request.setURI(new URI(url));
					client.execute(request);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		task.execute();
	}*/
}
