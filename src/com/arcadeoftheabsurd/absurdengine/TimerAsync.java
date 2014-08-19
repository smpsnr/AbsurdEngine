package com.arcadeoftheabsurd.absurdengine;

import com.arcadeoftheabsurd.j_utils.Delegate;

public class TimerAsync extends Timer
{
	public TimerAsync(float time, GameView context, Delegate method) {
		super(time, context, method);
	}

	@Override
	protected void addToContext() {
		context.asyncTimers.add(this);   
	}

	@Override
	protected void removeFromContext() {
		context.asyncTimers.remove(this);
	}
}
