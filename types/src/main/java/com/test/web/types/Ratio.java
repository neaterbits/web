package com.test.web.types;

public final class Ratio {

	private final int antecendent;
	private final int consequent;
	
	public Ratio(int antecendent, int consequent) {
		this.antecendent = antecendent;
		this.consequent = consequent;
	}

	public int getAntecendent() {
		return antecendent;
	}

	public int getConsequent() {
		return consequent;
	}
}
