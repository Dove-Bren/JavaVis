package com.smanzana.JavaVis.Representation.Graph;

import com.smanzana.JavaVis.Util.Pair;

public class UndirectedWeightedEdge {
	
	protected double weight;
	
	protected Pair<GraphNode, GraphNode> ends;
	
	public UndirectedWeightedEdge(double weight, GraphNode end1, GraphNode end2) {
		this.weight = weight;
		this.ends = new Pair<GraphNode, GraphNode>(end1, end2);
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
		return this.weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	@Override
	public String toString() {
		return "<" + ends.getLeft().getUniqueKey() + ", " + ends.getRight().getUniqueKey() + ">";
	}
	
	@Override
	public boolean equals(Object o) {
		return o.toString().equals(this.toString());
	}
	
}
