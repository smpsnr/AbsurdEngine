/*
 * AbsurdEngine (https://bitbucket.org/smpsnr/absurdengine/) 
 * (c) by Sam Posner (http://www.arcadeoftheabsurd.com/)
 *
 * AbsurdEngine is licensed under a
 * Creative Commons Attribution 4.0 International License
 *
 * You should have received a copy of the license along with this
 * work. If not, see http://creativecommons.org/licenses/by/4.0/ 
 */

package com.arcadeoftheabsurd.absurdengine;

import android.app.Activity;
import android.content.Context;

/*{{ ANDROIDONLY*/
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.OpenUDID.OpenUDID_manager;

import android.content.DialogInterface;
import android.webkit.WebView;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
/*}}*/

/**
 * Provides device specific utilities. 
 * Initialize localIp, userAgent, and adId with set() before get()ting them
 * @author sam
 */

public class DeviceUtility 
{	
	private static Context context;
	
	private static String localIp;
	private static String userAgent;
	private static String adId;
	
	private static final String DEFAULT_AD_ID = "00000000-0000-0000-0000-000000000000";
	
	/*{{ ANDROIDONLY*/
	private static final int PLAY_DIALOG_CODE = 1;
	private static final long UDID_TIMEOUT = 5000;
	private static final long UDID_WAIT_INC = 20;
	
	private static boolean playServiceFailed = false;
	private static boolean openUDIDServiceFailed = false;
	/*}}*/
		
	public static void setDeviceContext(Context context) {
		DeviceUtility.context = context;
	}
	
	public static void setAdId() throws InterruptedException {
		adId = getAdIdImpl();
	}
	
	public static void setLocalIp() {
		localIp = getLocalIpImpl();
	}
	
	public static void setUserAgent() {
		userAgent = getUserAgentImpl();
	}
	
	public static String getAdId() {
		return adId;
	}
	
	public static String getLocalIp() {
		return localIp;
	}
	
	public static String getUserAgent() {
		return userAgent;
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
    			GooglePlayServicesUtil.getErrorDialog(error, activity, PLAY_DIALOG_CODE, new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						playServiceFailed = true;
					}
				}).show();
    		} else {
    			playServiceFailed = true;
    		}
    	}
    	/*}}*/
    	/*{{ IOSONLY
    	//nop
    	return;
    	/*}}*/
	}
	
	/*{{ ANDROIDONLY*/	
	private static void initOpenUDID() {
		OpenUDID_manager.sync(context);
		try {
			postBlocking(new Runnable() {
				@Override
				public void run() {
					long waitTimer = UDID_TIMEOUT;
					while (!OpenUDID_manager.isInitialized() && waitTimer > 0) {
						try {
							Thread.sleep(UDID_WAIT_INC);
							waitTimer -= UDID_WAIT_INC;
						} catch (InterruptedException e) {}
					}
				}
			});
		} catch (InterruptedException e) {}
		if (!OpenUDID_manager.isInitialized()) {
			openUDIDServiceFailed = true;
		}
	}
	/*}}*/
	
	static void postBlocking(Runnable r) throws InterruptedException {
		Thread thread = new Thread(r);
		thread.start();
		thread.join();
	}
	
	/*{{ ANDROIDONLY*/
	private static String getAdIdImpl() {
		if (!playServiceFailed) {
			Info adInfo = null;		
			try {
				adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
			} catch (Exception e) {
				initOpenUDID();
			}
			if (adInfo.getId() != null) {
				return adInfo.getId();
			} else {
				initOpenUDID();
			}
		}
		if (!openUDIDServiceFailed) {
			if (!(OpenUDID_manager.getOpenUDID() == null)) {
				return OpenUDID_manager.getOpenUDID();
			}
		}
		return DEFAULT_AD_ID;
	}
	/*}}*/
	
	/*{{ IOSONLY
	private static native String getAdIdImpl();
	/*}}*/
	
	/*{{ ANDROIDONLY*/
	private static String getLocalIpImpl() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {}
		return null;
	}
	/*}}*/
	
	/*{{ IOSONLY
	private static native String getLocalIpImpl();
	/*}}*/
	
	/*{{ ANDROIDONLY*/
	private static String getUserAgentImpl() {
		return new WebView(context).getSettings().getUserAgentString();
	}
	/*}}*/
	
	/*{{ IOSONLY
	private static native String getUserAgentImpl();
	/*}}*/
}
