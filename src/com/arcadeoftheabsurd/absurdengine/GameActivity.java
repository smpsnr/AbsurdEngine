package com.arcadeoftheabsurd.absurdengine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

/**
 * Entry point for Absurd Engine games. Maintains a RunnerThread that calls updateGame FPS times per second,
 * executing its GameView's logic on the runner thread and rendering it on the main (UI) thread
 * @author sam
 */

public abstract class GameActivity extends Activity
{    
    private RunnerThread gameRunner;
    private GameView game;  
    private Handler gameHandler;
    
    protected abstract GameView initializeGame();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        // get an instance of the GameView subclass implemented in the program
        game = initializeGame();
        
        // implicitly bind handler to UI thread's message queue (explicit binding does not work on iOS)
        gameHandler = new Handler();
        gameRunner = new RunnerThread(this);
        gameRunner.start();
        
        setContentView(game);
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
    
    void updateGame() 
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
