package com.mobfox.adsdk.nativeads;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobfox.adsdk.Gender;

import com.arcadeoftheabsurd.absurdengine.DeviceUtility;

public class NativeAdManager 
{
	private NativeAd nativeAd;
	private String publisherId;
	private Gender userGender;
	private int userAge;
	private List<String> keywords;

	private NativeAdListener listener;

	private Context context;
	private NativeAdRequest request;

	private String requestUrl;
	private Handler handler;

	private List<String> adTypes;

	public NativeAdManager(Context context, String requestUrl, String publisherId, NativeAdListener listener, List<String> adTypes) {
		if ((publisherId == null) || (publisherId.length() == 0)) {
			throw new IllegalArgumentException("Publisher ID cannot be null or empty");
		}
		this.context = context;
		this.requestUrl = requestUrl;
		this.publisherId = publisherId;
		this.listener = listener;
		this.adTypes = adTypes;
		
		handler = new Handler();
	}

	public void requestAd() {
		Thread requestThread = new Thread(new Runnable() {
			public void run() {
				final RequestNativeAd requestAd;
				requestAd = new RequestNativeAd(context);
				try {
					nativeAd = requestAd.sendRequest(NativeAdManager.this.getRequest());
					if (nativeAd != null) {
						notifyAdLoaded(nativeAd);
					} else {
						notifyAdFailed();
					}
				} catch (final Throwable e) {
					notifyAdFailed();
				}
			}
		});
		requestThread.start();
	}

	private NativeAdRequest getRequest() {
		if (this.request == null) {
			this.request = new NativeAdRequest();
			this.request.setPublisherId(publisherId);
			this.request.setAdId(DeviceUtility.getAdId());
			this.request.setUserAgent(DeviceUtility.getUserAgent());
		}
		request.setRequestUrl(requestUrl);
		request.setAdTypes(adTypes);
		request.setGender(userGender);
		request.setUserAge(userAge);
		request.setAdTypes(adTypes);
		request.setKeywords(keywords);

		return this.request;
	}

	/*public NativeAdView getNativeAdView(NativeAd ad, NativeViewBinder binder) {
		NativeAdView view = new NativeAdView(context, ad, binder, listener);
		if (ad != null) {
			view.setOnClickListener(createOnNativeAdClickListener(ad.getClickUrl()));
		}
		return view;
	}*/

	private OnClickListener createOnNativeAdClickListener(final String clickUrl) {
		OnClickListener clickListener = new OnClickListener() {
			public void onClick(View v) {
				notifyAdClicked();
				if (clickUrl != null && !clickUrl.equals("")) {
					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
					context.startActivity(intent);
				}
			}
		};
		return clickListener;
	}

	private void notifyAdLoaded(final NativeAd ad) {
		if (listener != null) {
			handler.post(new Runnable() {
				public void run() {
					listener.adLoaded(ad);
				}
			});
		}
	}

	private void notifyAdFailed() {
		if (listener != null) {
			handler.post(new Runnable() {
				public void run() {
					listener.adFailedToLoad();
				}
			});
		}
	}

	private void notifyAdClicked() {
		if (listener != null) {
			handler.post(new Runnable() {
				public void run() {
					listener.adClicked();
				}
			});
		}
	}

	public void setUserGender(Gender userGender) {
		this.userGender = userGender;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
}
