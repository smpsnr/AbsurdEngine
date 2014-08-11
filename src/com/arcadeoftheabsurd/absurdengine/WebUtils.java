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

/**
 * Provides device-independent network methods
 * @author sam
 */

public class WebUtils 
{
	//static final String IP_API = "http://ipinfo.io/ip";
	
	// downloads a file to the current process's private storage space
	public static String downloadFile(Context context, String fileUrl, String fileName) throws IOException {
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
	
	public static String restRequest(String request) throws IOException {
		URL url = new URL(request);
		URLConnection conn = url.openConnection();
		conn.setRequestProperty("User-Agent", DeviceUtility.getUserAgent());
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

		StringBuilder result = new StringBuilder();
		String line = "";
		while((line = in.readLine()) != null) {
			result.append(line);
		}
		in.close();
		return result.toString();
	}
}
