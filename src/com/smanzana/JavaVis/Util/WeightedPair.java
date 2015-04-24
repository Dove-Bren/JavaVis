package com.smanzana.JavaVis.Util;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;

public class WeightedPair<a, b> extends Pair<a, b> {

	private double weight;
	
	private Cclass bestCLASS;
	
	public WeightedPair(a valueA, b valueB, double weight) {
		super(valueA, valueB);

		this.weight = weight;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}

}
