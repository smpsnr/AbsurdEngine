/*
 * Adapted from the MobFox Android SDK (https://github.com/mobfox/MobFox-Android-SDK)
 * under the MoPub Client License (/3rdparty-license/adsdk-LICENSE.txt)
 */

package com.adsdk.sdk.nativeads;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;

import com.adsdk.sdk.Gender;
import com.adsdk.sdk.RequestException;
import com.arcadeoftheabsurd.absurdengine.DeviceUtility;
import com.arcadeoftheabsurd.j_utils.Vector2d;

public class NativeAdManager 
{
	private Context context;
	private NativeAdListener listener;
	private Handler handler;
	private NativeAdRequest request;
	
	private NativeAd nativeAd;
	private String publisherId;
	private Gender userGender;
	private int userAge;
	private List<String> keywords;
	private List<String> adTypes;
	private Map<String, Vector2d> imageAssets;
	
	public NativeAdManager(Context context, NativeAdListener listener, String publisherId, List<String> adTypes, Map<String, Vector2d> imageAssets) {
		if ((publisherId == null) || (publisherId.length() == 0)) {
			throw new IllegalArgumentException("Publisher ID cannot be null or empty");
		}
		this.context = context;
		this.publisherId = publisherId;
		this.listener = listener;
		this.adTypes = adTypes;
		this.imageAssets = imageAssets;
		
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
				} catch (final RequestException e) {
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
		request.setAdTypes(adTypes);
		request.setGender(userGender);
		request.setUserAge(userAge);
		request.setAdTypes(adTypes);
		request.setKeywords(keywords);
		request.setImageAssets(imageAssets);

		return this.request;
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