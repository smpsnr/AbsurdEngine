package com.mobfox.adsdk.nativeads;

public interface NativeAdListener
{
	public void adLoaded(NativeAd ad);

	public void adFailedToLoad();
}
