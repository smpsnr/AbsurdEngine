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

import java.util.Observable;

import com.arcadeoftheabsurd.j_utils.Delegate;

/**
 * Schedules a delegate in a GameView to be executed on a timer
 * @author sam
 */

abstract class Timer extends Observable
{    
	public static int curId = 0;
	
	public Delegate method;
	
	protected GameView context;
    private boolean running;
    private float delay;
        
    private final int id;
    private final float time;
    private final float delayInc = (float) 1 / RunnerThread.FPS;
    
    protected abstract void addToContext();
    protected abstract void removeFromContext();
    
    public Timer(float time, GameView context, Delegate method) {
        this.time = time;
        this.context = context;
        this.method = method;

        id = curId++;
        delay = time;  
    }
    
    public void tic() {
        if (running) {
            delay -= delayInc;
            
            if (delay <= 0) {
                setChanged();
                notifyObservers(id);
                delay = time;
            }
        }
    }
   
    public void start() {
        running = true;
        addObserver(context);
        addToContext();  
    }
    
    public void end() {
        running = false;
        this.deleteObservers();
        removeFromContext();
    }
    
    public void resume() {
        running = true;
    }
    
    public void pause() {
        running = false;
    }
    
    public static void reset() {
        curId = 0;
    }
    
    public boolean isRunning() {
    	return running;
    }
}
