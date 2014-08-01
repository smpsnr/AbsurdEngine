package com.arcadeoftheabsurd.absurdengine;

/**
 * Thread running game logic at the appropriate pace
 * @author sam
 */

public class RunnerThread extends Thread
{    
	public static final int FPS = 30;
	
	private final Object pauseLock = new Object();
    private final int SKIP_MILLISECONDS = 1000 / FPS;
	
	private boolean threadRunning = false;
    private boolean threadPaused = false;
    
    private GameActivity activity;
    
    private long sleepTime = 0;
    private long gameTime;
    
    public RunnerThread(GameActivity activity) {
        this.activity = activity;
        this.setName("update thread");
    }
    
    public void finish() {
        boolean retry = true;

        threadRunning = false;

        // wait for this thread to close before completing activity destruction
        while (retry) {
            try {
                join();
                retry = false;
            } catch (InterruptedException e) {}
        }
    }
    
    public void pause() {
        threadPaused = true;
    }
    
    public boolean paused() {
        return threadPaused;
    }
    
    public void unpause() {
        // monitor pauseLock
        synchronized (pauseLock) {
            threadPaused = false;
            gameTime = System.currentTimeMillis();
            // wake up the thread waiting on pauseLock (this one)
            pauseLock.notifyAll();
        }
    }
    
    // the main loop of the game
    @Override
    public void run() {    
        while (threadRunning) {
            synchronized (pauseLock) {
                while (threadPaused) {
                    try {
                        // force this thread to wait on pauseLock's monitor
                        pauseLock.wait();
                    } catch (InterruptedException e) {}               
                }
            }    
            
            // tell the gameview thread (android's UI thread) to execute game logic and redraw its view
            activity.updateGame();
            
            // if the game is running faster than the specified FPS, sleep for the extra time
            gameTime += SKIP_MILLISECONDS;
            sleepTime = gameTime - System.currentTimeMillis();
            
            if (sleepTime >= 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {}          
            }  
        }
    }
    
    @Override
    public void start() {
        threadRunning = true;
        gameTime = System.currentTimeMillis();
        super.start();
    }
}
