package com.arcadeoftheabsurd.absurdengine;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;

/*{{ ANDROIDONLY*/
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
/*}}

/**
 * Provides device specific utilities
 * @author sam
 */

public class DeviceUtility 
{
	private static Context context;
	private static Activity activity;
	
	public static void setDeviceContext(Context context, Activity activity) {
		DeviceUtility.context = context;
		DeviceUtility.activity = activity;
	}
	
	/*{{ ANDROIDONLY*/
	public static String getAdId() {
		Info adInfo = null;
		String result = null;
		
		try {
			adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
		} catch (IOException e) {
			return result;
		} catch (GooglePlayServicesRepairableException e) {
			return result;
		} catch (GooglePlayServicesNotAvailableException e) {
			return result;
		}
		final String id = adInfo.getId();

		if (!adInfo.isLimitAdTrackingEnabled()) {
			result = id;
		}
		return result;
	}
	/*}}*/
	
	/*{{ ANDROIDONLY*/
	public static void testPlayServices() {
		int error = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
    	if (error != ConnectionResult.SUCCESS) {
    		System.out.println("google play services not working, showing dialog...");
    		GooglePlayServicesUtil.getErrorDialog(error, activity, 0).show();
    	} else {
    		System.out.println("google play services working!");
    	}
	}
	/*}}*/
	
	/*{{ IOSONLY
	public static native String getAdId();
	/*}}*/
	
	public static boolean isAndroid() {
		/*{{ ANDROIDONLY*/
		return true;
		/*}}*/
		
		/*{{ IOSONLY
		return false;
		/*}}*/
	}
	
	public static boolean isIOS() {
		/*{{ ANDROIDONLY*/
		return false;
		/*}}*/
		
		/*{{ IOSONLY
		return true;
		/*}}*/
	}
}
