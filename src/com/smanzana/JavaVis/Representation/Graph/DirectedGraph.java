package com.smanzana.JavaVis.Representation.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic graph data structure.
 * @author Skyler
 *
 */
public class DirectedGraph extends Graph {
	
	private Set<DirectedGraphNode> nodes;
	
	public DirectedGraph() {
		nodes = new HashSet<DirectedGraphNode>();
	}

	public Set<DirectedGraphNode> getNodes() {
		return nodes;
	}
	
	public boolean addNode(DirectedGraphNode node) {
		return nodes.add(node);
	}
	
	//TODO get remove stuffs
	
	@Override
	public String toString() {
		String out = "Directed Graph:\n";
		if (nodes != null && !nodes.isEmpty()) {
			out += "Nodes:\n";
			for (DirectedGraphNode node : nodes) {
				out += "  " + node.toString() + "\n";
			}
		}
		
		
		return out;
	}
	
}
