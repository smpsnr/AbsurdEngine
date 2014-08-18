package com.arcadeoftheabsurd.absurdengine;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

public class SoundManager 
{
	private static boolean initialized = false;
	private static int channels;
	private static AssetManager assetManager;
	private static MediaPlayer[] mediaChannels;
	private static boolean[] paused;
	
	public static void initializeSound(AssetManager assetManager, int channels) {
		SoundManager.assetManager = assetManager;
		SoundManager.channels = channels;
		mediaChannels = new MediaPlayer[channels];
		paused = new boolean[channels];
		
		for (int i = 0; i < channels; i++) {
			mediaChannels[i] = new MediaPlayer();
		}
		initialized = true;
	}
	
	public static void loadSound(String assetName, int channel) throws IOException {
		AssetFileDescriptor asset = assetManager.openFd(assetName);
        mediaChannels[channel].setDataSource(asset.getFileDescriptor(), asset.getStartOffset(), asset.getLength());
        mediaChannels[channel].prepare();
	}
	
	public static int numChannels() {
		return channels;
	}
	
	public static boolean isInitialized() {
		return initialized;
	}
	
	public static void pauseAll() {
		for (int c = 0; c < channels; c++) {
    		if (isPlaying(c)) {
    			pauseSound(c);
    		}
    	}
	}
	
	public static void resumeAll() {
		for (int c = 0; c < channels; c++) {
    		if (isPaused(c)) {
    			playSound(c);
    		}
    	}
	}
	
	public static void releaseAll() {
		for (MediaPlayer m : mediaChannels) {
			m.release();
		}
	}
	
	public static void playSound(int channel) {
		mediaChannels[channel].start();
		paused[channel] = false;
	}
	
	public static void pauseSound(int channel) {
		mediaChannels[channel].pause();
		paused[channel] = true;
	}
	
	public static boolean isPlaying(int channel) {
		return mediaChannels[channel].isPlaying();
	}
	
	public static boolean isPaused(int channel) {
		return paused[channel];
	}
}
