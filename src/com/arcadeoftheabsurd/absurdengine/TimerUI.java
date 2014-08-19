package com.arcadeoftheabsurd.absurdengine;

import com.arcadeoftheabsurd.j_utils.Delegate;

public class TimerUI extends Timer
{
	public TimerUI(float time, GameView context, Delegate method) {
		super(time, context, method);
	}
	
	@Override
	protected void addToContext() {
		context.uiTimers.add(this);   
	}

	@Override
	protected void removeFromContext() {
		context.uiTimers.remove(this);  
	}
}
