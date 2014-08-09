package com.mobfox.adsdk.nativeads;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arcadeoftheabsurd.absurdengine.BitmapTempFileHolder;
import com.arcadeoftheabsurd.j_utils.Vector2d;
import com.mobfox.adsdk.nativeads.NativeAd.Tracker;

public class NativeAdView extends FrameLayout 
{
	private Context context;
	private boolean impressionReported;
	private NativeBanner bannerView;
	private List<Tracker> trackers;
	
	public class NativeBanner extends RelativeLayout 
	{
		ImageView iconView;
		TextView descriptionView;
		LayoutParams iconLayout, descriptionLayout; 
		
		public NativeBanner(Context context, Bitmap bitmap, String description) {
			super(context);

			iconView = new ImageView(context);
			iconView.setImageBitmap(bitmap);
			iconLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			iconLayout.addRule(ALIGN_PARENT_LEFT);
			
			descriptionView = new TextView(context);
			descriptionView.setText(description);
			descriptionView.setTextColor(Color.WHITE);
			descriptionLayout = new LayoutParams(NativeAdView.this.getWidth() - bitmap.getWidth(), NativeAdView.this.getHeight());
			descriptionLayout.addRule(CENTER_VERTICAL);
			descriptionLayout.addRule(ALIGN_PARENT_RIGHT);
			
			addView(iconView, iconLayout);
			addView(descriptionView, descriptionLayout);
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
		BitmapTempFileHolder bitmapHolder = ad.getImageAsset("icon").bitmapHolder;
		bitmapHolder.initialize();
		
		bannerView = new NativeBanner(context, bitmapHolder.bitmap, ad.getTextAsset("description"));
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

	private void trackImpression(final String url) {
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
	}
}
