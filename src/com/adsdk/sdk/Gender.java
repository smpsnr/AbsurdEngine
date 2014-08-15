/*
 * Adapted from the MobFox Android SDK (https://github.com/mobfox/MobFox-Android-SDK)
 * under the MoPub Client License (/3rdparty-license/adsdk-LICENSE.txt)
 */

package com.adsdk.sdk;

public enum Gender 
{
	MALE("m"),
	FEMALE("f");
	
	private String param;
	
	Gender(String param) {
		this.param = param;
	}
	
	public String getServerParam() {
		return this.param;
	}
}
