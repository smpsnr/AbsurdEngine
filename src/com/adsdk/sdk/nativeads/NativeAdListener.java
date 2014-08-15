/*
 * Adapted from the MobFox Android SDK (https://github.com/mobfox/MobFox-Android-SDK)
 * under the MoPub Client License (/3rdparty-license/adsdk-LICENSE.txt)
 */

package com.adsdk.sdk.nativeads;

public interface NativeAdListener
{
	public void adLoaded(NativeAd ad);

	public void adFailedToLoad();
}
