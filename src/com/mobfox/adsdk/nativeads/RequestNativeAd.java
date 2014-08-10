package com.mobfox.adsdk.nativeads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

import com.arcadeoftheabsurd.absurdengine.Sprite;
import com.arcadeoftheabsurd.j_utils.Vector2d;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
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
			System.out.println(request.toString());
			URL url = new URL(request.toString());
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", request.getUserAgent());
			System.out.println("sending ad request");
			return parse(conn.getInputStream(), request);	
		} catch (IOException e) {
			throw new RequestException("Error sending ad request", e);
		}
	}

	protected NativeAd parse(final InputStream inputStream, final NativeAdRequest request) throws RequestException {
		final NativeAd response = new NativeAd();
		try {
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(inputStream, Const.ENCODING), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			String result = sb.toString();
			
			System.out.println("parsing result");
						
			JsonObject mainObject = JsonObject.readFrom(result);
			JsonValue imageAssetsValue = mainObject.get("imageassets");
			
			if (imageAssetsValue != null) {
				JsonObject imageAssetsObject = imageAssetsValue.asObject();

				for (String type : imageAssetsObject.names()) {	
					if (request.getImageAssets().containsKey(type)) {
						ImageAsset asset = new ImageAsset();
						
						JsonObject assetObject = imageAssetsObject.get(type).asObject();
						String url = assetObject.get("url").asString();
						
						asset.url = url;
						
						asset.width = Integer.parseInt(assetObject.get("width").asString());
						asset.height = Integer.parseInt(assetObject.get("height").asString());
						
						if(request.getImageAssets() != null) {
							Vector2d assetSize = request.getImageAssets().get(type);
							asset.sprite = Sprite.fromUrl(context, url, assetSize.x, assetSize.y);
						} else {
							asset.sprite = Sprite.fromUrl(context, url, asset.width, asset.height);
						}
						
						response.addImageAsset(type, asset);
					}
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
		} catch (IOException e) {
			throw new RequestException("Cannot parse Response", e);
		}
		return response;
	}
}
