package com.mobfox.adsdk.nativeads;

import java.util.List;
import java.util.Random;

import com.mobfox.adsdk.Const;
import com.mobfox.adsdk.Gender;
import com.arcadeoftheabsurd.absurdengine.DeviceUtility;

public class NativeAdRequest 
{
	private static final String REQUEST_TYPE = "native";
	private static final String RESPONSE_TYPE = "json";
	private static final String IMAGE_TYPES = "icon,main";
	private static final String TEXT_TYPES = "headline,description,cta,advertiser,rating";
	private static final String REQUEST_TYPE_ANDROID = "android_app";
	private static final String REQUEST_TYPE_IPHONE = "iphone_app";
	private  String request_url;
	private List<String> adTypes;
	private String publisherId;
	private String userAgent;
	private String adId;
	private String protocolVersion;

	private double longitude = 0.0;
	private double latitude = 0.0;

	private Gender gender;
	private int userAge;
	private List<String> keywords;

	public String toString() {
		final StringBuilder b = new StringBuilder(request_url);
		
		Random r = new Random();
		int random = r.nextInt(50000);
		
		if (DeviceUtility.isIOS()) {
			b.append("?rt=" + REQUEST_TYPE_IPHONE);
		} else {
			b.append("?rt=" + REQUEST_TYPE_ANDROID);
		}
		b.append("&r_type=" + REQUEST_TYPE);
		b.append("&r_resp=" + RESPONSE_TYPE);
		b.append("&n_img=" + IMAGE_TYPES);
		b.append("&n_txt=" + TEXT_TYPES);
		
		if (adTypes != null) {
			b.append("&n_type=");
			for (int i = 0; i < adTypes.size(); i++) {
				b.append(i < adTypes.size() - 1 ? adTypes.get(i) + "," : adTypes.get(i));
			}
		}
		b.append("&s=" + this.getPublisherId());
		b.append("&u=" + this.getUserAgent());
		b.append("&r_random=" + Integer.toString(random));

		if (DeviceUtility.isIOS()) {
			b.append("&o_iosadvid=" + adId);
		} else {
			b.append("&o_andadvid=" + adId);
		}
		b.append("&v=" + this.getProtocolVersion());

		if (userAge != 0) {
			b.append("&demo.age=" + Integer.toString(userAge));
		}
		if (gender != null) {
			b.append("&demo.gender=" + gender.getServerParam());
		}
		if (keywords != null) {
			b.append("&demo.keywords=");
			for (int i = 0; i < keywords.size(); i++) {
				b.append(i < keywords.size() - 1 ? keywords.get(i) + "," : keywords.get(i));
			}
		}
		b.append("&u_wv=" + this.getUserAgent());
		b.append("&u_br=" + this.getUserAgent());
		if (longitude != 0 && latitude != 0) {
			b.append("&longitude=" + Double.toString(longitude));
			b.append("&latitude=" + Double.toString(latitude));
		}
		return b.toString();
	}

	public List<String> getAdTypes() {
		return adTypes;
	}

	public void setAdTypes(List<String> adTypes) {
		this.adTypes = adTypes;
	}

	public String getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}

	public String getUserAgent() {
		if (this.userAgent == null)
			return "";
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	
	public String getAdId() {
		return adId;
	}
	
	public void setAdId(String adId) {
		this.adId = adId;
	}

	public String getProtocolVersion() {
		if (this.protocolVersion == null)
			return Const.VERSION;
		else
			return this.protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public int getUserAge() {
		return userAge;
	}

	public void setUserAge(int userAge) {
		this.userAge = userAge;
	}

	public List<String> getKeywords() {
		return keywords;
	}
	
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	
	public void setRequestUrl (String url) {
		this.request_url = url;
	}
}
