package com.arcadeoftheabsurd.absurdengine;

import com.arcadeoftheabsurd.absurdengine.GameView.GameLoadListener;
import com.arcadeoftheabsurd.absurdengine.RunnerThread.UpdateListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

/**
 * Entry point for Absurd Engine games. Maintains a RunnerThread that calls updateGame FPS times per second,
 * executing its GameView's logic on the runner thread and rendering it on the main (UI) thread
 * @author sam
 */

public abstract class GameActivity extends Activity implements GameLoadListener, UpdateListener
{    
    private RunnerThread gameRunner;
    private Handler gameHandler;
    private GameView game;
    
    protected abstract GameView initializeGame();
    protected abstract View initializeContentView();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        // implicitly bind handler to UI thread's message queue (explicit binding does not work on iOS)
        gameHandler = new Handler();
        gameRunner = new RunnerThread(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (gameRunner.paused()) {
            gameRunner.unpause();
        } else {
            Timer.reset();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        if (isFinishing()) {
            gameRunner.finish();
        } else {
            gameRunner.pause();
        }
    }
    
    protected final void loadContent() {
    	gameHandler.post (
            new Runnable() {
                public void run() {
                	// post to UI thread
                 	game = initializeGame();
                 	setContentView(initializeContentView());
                }
            }
        );
    }
    
    @Override
    public void gameLoaded() {
    	gameRunner.start();
    }
    
    @Override
    public void update() 
    {
        // run in update thread
        game.update();
        gameHandler.post (
            new Runnable() {
                public void run() {
                    // post to UI thread
                    game.invalidate();  
                }
            }
        );   
    }  
}
