package com.arcadeoftheabsurd.j_utils;

/**
 * Generic 2-tuple
 * @author sam
 */

public class Pair<A, B> 
{
	public final A a;
	public final B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) {
			return false;
		}
		Pair test = (Pair) o;

		return (a.equals(test.a)) && (b.equals(test.b));
	}
	
	@Override
	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}
}
