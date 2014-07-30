package com.arcadeoftheabsurd.absurdengine;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

import android.webkit.WebView;

/**
 * Provides device-independent network methods
 * @author sam
 */

public class WebUtils 
{
	static final String IP_API = "http://ipinfo.io/ip";
	
	// gets the device's IP address by querying a third party server. this is actually the most reliable way to do it.
	public static String getLocalIpAddress() throws IOException {
		URL url = new URL(IP_API);
		URLConnection conn = url.openConnection();		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		String line = "";
		while((line = in.readLine()) != null) {
			return line;
		}
		in.close();
		
		return null;
	}
	
	// gets the device's user agent by starting the default web browser 
	public static String getUserAgent(Context context) {
		return new WebView(context).getSettings().getUserAgentString();
	}
	
	// downloads a file to the current process's private storage space
	public static String downloadFile(String fileUrl, String fileName, Context context) throws IOException {
		URL url = new URL(fileUrl);
		URLConnection conn = url.openConnection();
		InputStream in = new BufferedInputStream(conn.getInputStream());
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int byteIn = 0;
		
		while ((byteIn = in.read(buffer)) != -1) {
		   out.write(buffer, 0, byteIn);
		}
		out.close();
		in.close();
								
		context.openFileOutput(fileName, 0).write(out.toByteArray());		
		String filePath = context.getFileStreamPath(fileName).getAbsolutePath();
		
		return filePath;
	}
}
