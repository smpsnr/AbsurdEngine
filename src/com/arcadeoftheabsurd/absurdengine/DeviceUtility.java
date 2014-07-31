package com.arcadeoftheabsurd.absurdengine;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;

/*{{ ANDROIDONLY*/
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
/*}}

/**
 * Provides device specific utilities
 * @author sam
 */

public class DeviceUtility 
{
	public static final int PLAY_DIALOG_REQUEST_CODE = 1;
	
	private static Context context;
	
	private static String localIp;
	private static String userAgent;
	private static String adId;
		
	public static void setDeviceContext(Context context) {
		DeviceUtility.context = context;
	}
	
	public static void setLocalIp() throws InterruptedException {
		postBlocking(new Runnable() {
			public void run() {
				try {
					localIp = WebUtils.getLocalIpAddress();
				} catch (IOException e) {
					localIp = null;
				}
			}
		});
	}
	
	public static void setUserAgent() {
		userAgent = WebUtils.getUserAgent(context);
	}
	
	public static void setAdId() throws InterruptedException {
		postBlocking(new Runnable() {
			public void run() {
				adId = getAdIdImpl();
			}
		});
	}
	
	public static String getLocalIp() {
		return localIp;
	}
	
	public static String getUserAgent() {
		return userAgent;
	}
	
	public static String getAdId() {
		return adId;
	}
	
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
	
	
	public static void requireAdService(final Activity activity) {
		/*{{ ANDROIDONLY*/
		int error = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
    	if (error != ConnectionResult.SUCCESS) {
    		if (GooglePlayServicesUtil.isUserRecoverableError(error)) {
    			GooglePlayServicesUtil.getErrorDialog(error, activity, PLAY_DIALOG_REQUEST_CODE, new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						Toast.makeText(context, "Google Play Services must be installed.", Toast.LENGTH_SHORT).show();
						activity.finish();
					}
				}).show();
    		} else {
    			Toast.makeText(context, "Google Play Services reported the following error: " + GooglePlayServicesUtil.getErrorString(error), Toast.LENGTH_SHORT).show();
    			activity.finish();
    		}
    	}
    	/*{{ IOSONLY
    	private static native String getAdIdImpl();
    	/*}}*/
	}
	
	static void postBlocking(Runnable r) throws InterruptedException {
		Thread thread = new Thread(r);
		thread.start();
		thread.join();
	}
	
	/*{{ ANDROIDONLY*/
	private static String getAdIdImpl() {
		Info adInfo = null;		
		try {
			adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
		} catch (Exception e) {
			return null;
		}
		return adInfo.getId();
	}
	/*}}*/
	
	/*{{ IOSONLY
	private static native String getAdIdImpl();
	/*}}*/
}
