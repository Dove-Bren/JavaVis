package com.smanzana.JavaVis.Representation.Graph;

import com.smanzana.JavaVis.Util.WeightedPair;

public class DirectedWeightedEdge extends UndirectedWeightedEdge{
	
	public DirectedWeightedEdge(DirectedGraphNode source, DirectedGraphNode dest) {
		this(source, dest, 1.0);
	}
	
	/**
	 * 
	 * @param source
	 * @param dest
	 * @param weight
	 */
	public DirectedWeightedEdge(DirectedGraphNode source, DirectedGraphNode dest, double weight) {
		//this.weight = weight;
		this.ends = new WeightedPair<GraphNode, GraphNode>(source, dest, weight);
	}
	
	//DEFINE LEFT: SOURCE
	//RIGHT: DEST
	public GraphNode getSource() {
		return this.ends.getLeft();
	}
	
	public GraphNode getDestination() {
		return this.ends.getRight();
	}
	
}
