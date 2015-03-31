package com.smanzana.JavaVis.Representation.Graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic graph data structure.
 * @author Skyler
 *
 */
public class UndirectedGraph extends Graph {
	
	private Set<GraphNode> nodes;
	
	private Set<UndirectedWeightedEdge> edges;
	
	
	public UndirectedGraph() {
		nodes = new HashSet<GraphNode>();
		edges = new HashSet<UndirectedWeightedEdge>();
	}


	/**
	 * @return the nodes
	 */
	public Set<GraphNode> getNodes() {
		return nodes;
	}


	/**
	 * @return the edges
	 */
	public Set<UndirectedWeightedEdge> getEdges() {
		return edges;
	}

	public boolean addNode(GraphNode node) {
		return nodes.add(node);
	}
	
	public boolean addEdge(GraphNode node, GraphNode otherNode) {
		UndirectedWeightedEdge edge = new UndirectedWeightedEdge(node, otherNode);
		return edges.add(edge);
	}
	
}
