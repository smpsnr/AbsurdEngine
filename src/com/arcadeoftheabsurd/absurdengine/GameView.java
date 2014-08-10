package com.arcadeoftheabsurd.absurdengine;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.arcadeoftheabsurd.j_utils.Pair;
import com.arcadeoftheabsurd.j_utils.Vector2d;

/**
 * Encapsulates game logic and rendering methods
 * @author sam
 */

public abstract class GameView extends View implements Observer
{    	
    protected ArrayList<Timer> timers = new ArrayList<Timer>();
    protected int bitmapsInitialized = 0;
    
    private GameLoadListener loadListener;
    private ArrayList<BitmapHolder> bitmapStorage = new ArrayList<BitmapHolder>();
    
    protected abstract void startGame();
    protected abstract void setupGame(int width, int height);
    protected abstract void updateGame();
    
    public interface GameLoadListener
    {
    	void gameLoaded();
    }
    
    public GameView(Context context, GameLoadListener loadListener) {
        super(context);
        this.loadListener = loadListener;
    }
    
    @Override
	public void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
    	setupGame(newWidth, newHeight);
    	loadListener.gameLoaded();
    	startGame();
    }
    
    // called when timers fire
    public void update(Observable timer, Object id) {
        timers.get((Integer)id).method.function();
    }
        
    // called RunnerThread.FPS times per second.
    void update() {
        for (Timer t : timers) {
            t.tic();
        }        
        updateGame();
    }       
    
    protected Sprite makeSprite(int bitmapId, int x, int y) {
    	return new Sprite(bitmapStorage.get(bitmapId), x, y);
    }
    
	protected ArrayList<Integer> loadBitmapResources(Pair<Integer, Vector2d>... args) {
    	ArrayList<Integer> bitmapIds = new ArrayList<Integer>();
		for (int i = 0; i < args.length; i++) {
			bitmapIds.add(bitmapStorage.size());
			bitmapStorage.add(new BitmapHolder(BitmapFactory.decodeResource(getResources(), args[i].a), args[i].b.x, args[i].b.y));
		}
		return bitmapIds;
	}
    
    protected int loadBitmapResource(int resourceId, Vector2d initialSize) {
    	int bitmapId = bitmapStorage.size();
    	bitmapStorage.add(new BitmapHolder(BitmapFactory.decodeResource(getResources(), resourceId), initialSize.x, initialSize.y));
    	return bitmapId;
    }
    
    protected int loadBitmapHolder(BitmapHolder holder) {
    	int bitmapId = bitmapStorage.size();
    	bitmapStorage.add(holder);
    	return bitmapId;
    }
    
    protected int loadTempBitmapFile(String filePath, String fileName, Vector2d initialSize) {
    	int bitmapId = bitmapStorage.size();
    	bitmapStorage.add(new BitmapTempFileHolder(
    			((BitmapDrawable) BitmapDrawable.createFromPath(filePath)).getBitmap(),
    			initialSize.x, initialSize.y, fileName, getContext()));
    	return bitmapId;
    }
}
