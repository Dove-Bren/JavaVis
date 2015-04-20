package com.smanzana.JavaVis.Util;

public class WeightedPair<a, b> extends Pair<a, b> {

	double weight;
	
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
