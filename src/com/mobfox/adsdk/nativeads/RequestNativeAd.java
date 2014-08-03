package com.mobfox.adsdk.nativeads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

import com.arcadeoftheabsurd.absurdengine.BitmapTempFileHolder;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.eclipsesource.json.ParseException;
import com.mobfox.adsdk.Const;
import com.mobfox.adsdk.RequestException;
import com.mobfox.adsdk.nativeads.NativeAd.ImageAsset;
import com.mobfox.adsdk.nativeads.NativeAd.Tracker;

public class RequestNativeAd 
{
	private Context context;
	
	public RequestNativeAd(Context context) {
		this.context = context;
	}
	
	public NativeAd sendRequest(NativeAdRequest request) throws RequestException {
		try {
			URL url = new URL(request.toString());
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(Const.CONNECTION_TIMEOUT);
			conn.setRequestProperty("User-Agent", request.getUserAgent());
			return parse(conn.getInputStream());	
		} catch (IOException e) {

		}
		throw new RequestException("done goofed");
	}

	protected NativeAd parse(final InputStream inputStream) throws RequestException {
		final NativeAd response = new NativeAd();
		try {
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			String result = sb.toString();
						
			JsonObject mainObject = JsonObject.readFrom(result);
			JsonValue imageAssetsValue = mainObject.get("imageassets");
			
			if (imageAssetsValue != null) {
				JsonObject imageAssetsObject = imageAssetsValue.asObject();

				for (String type : imageAssetsObject.names()) {					
					ImageAsset asset = new ImageAsset();
					
					JsonObject assetObject = imageAssetsObject.get(type).asObject();
					String url = assetObject.get("url").asString();
					
					asset.url = url;
					asset.width = Integer.parseInt(assetObject.get("width").asString());
					asset.height = Integer.parseInt(assetObject.get("height").asString());
					asset.bitmapHolder = BitmapTempFileHolder.fromUrl(url, asset.width, asset.height, context);
					
					response.addImageAsset(type, asset);
				}
			}
			JsonValue textAssetsValue = mainObject.get("textassets");
			
			if (textAssetsValue != null) {
				JsonObject textAssetsObject = textAssetsValue.asObject();
				
				for (String type : textAssetsObject.names()) {					
					String text = textAssetsObject.get(type).asString();
					response.addTextAsset(type, text);
				}
			}
			response.setClickUrl(mainObject.get("click_url").asString());

			JsonValue trackersValue = mainObject.get("trackers");
			
			if (trackersValue != null) {
				JsonArray trackersArray = trackersValue.asArray();
				
				for (int i = 0; i < trackersArray.size(); i++) {
					JsonValue trackerValue = trackersArray.get(i);
					
					if(trackerValue != null) {
						JsonObject trackerObject = trackerValue.asObject();
						Tracker tracker = new Tracker();
						tracker.type = trackerObject.get("type").asString();
						tracker.url = trackerObject.get("url").asString();
						response.getTrackers().add(tracker);
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RequestException("Cannot parse Response", e);
		} catch (IOException e) {
			throw new RequestException("Cannot parse Response", e);
		} catch (ParseException e) {
			throw new RequestException("Cannot parse Response", e);
		}
		return response;
	}
}
