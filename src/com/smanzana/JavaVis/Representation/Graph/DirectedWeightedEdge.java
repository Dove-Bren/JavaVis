package com.smanzana.JavaVis.Representation.Graph;

import com.smanzana.JavaVis.Util.Pair;

public class DirectedWeightedEdge extends UndirectedWeightedEdge{
	
	public DirectedWeightedEdge(DirectedGraphNode source, DirectedGraphNode dest) {
		this.weight = 1.0;
		this.ends = new Pair<GraphNode, GraphNode>(source, dest);
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
