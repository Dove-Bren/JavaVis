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

	public Set<GraphNode> getNodes() {
		return new HashSet<GraphNode>(nodes);
	}
	
	public Set<DirectedGraphNode> getDirectedNodes() {
		return nodes;
	}
	
	public Set<UndirectedWeightedEdge> getEdges() {
		Set<UndirectedWeightedEdge> edges = new HashSet<UndirectedWeightedEdge>();
		for (DirectedGraphNode node : nodes) {
			edges.addAll(node.getEdges());
		}
		
		return edges;
	}
	
	public Set<DirectedWeightedEdge> getWeightedEdges() {
		Set<DirectedWeightedEdge> edges = new HashSet<DirectedWeightedEdge>();
		for (DirectedGraphNode node : nodes) {
			edges.addAll(node.getEdges());
		}
		
		return edges;
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
