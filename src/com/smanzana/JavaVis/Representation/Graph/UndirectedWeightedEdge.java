package com.smanzana.JavaVis.Representation.Graph;

import com.smanzana.JavaVis.Util.Pair;
import com.smanzana.JavaVis.Util.WeightedPair;

public class UndirectedWeightedEdge implements Comparable<UndirectedWeightedEdge> {
	
	//protected double weight;
	
	protected WeightedPair<GraphNode, GraphNode> ends;
	
	public UndirectedWeightedEdge(double weight, GraphNode end1, GraphNode end2) {
		//this.weight = weight;
		this.ends = new WeightedPair<GraphNode, GraphNode>(end1, end2, weight);
	}
	
	public UndirectedWeightedEdge(GraphNode end1, GraphNode end2) {
		this(1.0, end1, end2);
	}
	
	public UndirectedWeightedEdge(double weight) {
		this(weight, null, null);
	}
	
	public UndirectedWeightedEdge() {
		this(1.0);
	}
	
	public Pair<GraphNode, GraphNode> getEnds() {
		return this.ends;
	}
	
	public double getWeight() {
		return ends.getWeight();
	}
	
	public void setWeight(double weight) {
		ends.setWeight(weight);
	}
	
	
	@Override
	public String toString() {
		return "<" + ends.getLeft().getUniqueKey() + ", " + ends.getRight().getUniqueKey() + ">(" + ends.getWeight() + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		return o.toString().equals(this.toString());
	}

	@Override
	public int compareTo(UndirectedWeightedEdge o) {
		return ends.getLeft().cclass.compareTo(o.ends.getLeft().cclass);
	}
	
}
