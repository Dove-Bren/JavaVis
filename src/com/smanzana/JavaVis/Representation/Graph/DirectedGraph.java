package com.smanzana.JavaVis.Representation.Graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.List;

import com.smanzana.JavaVis.Representation.DataRepresentation;

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
	
	/**
	 * Takes the passed data representation and attempts to add all stored information into this graph.<br />
	 * When passed undirected graphs, this method takes point A of the edge pair and assigns it as the
	 * source of the edge, with B being the destination.
	 * @param data
	 */
	public void mergeFrom(DataRepresentation data) {
		if (data instanceof DirectedGraph) {
			//easier if we can use member functions
			DirectedGraph otherGraph = (DirectedGraph) data;
			DirectedGraphNode cacheNode;
			for (DirectedGraphNode node : otherGraph.nodes) {
				
				//iteratorate over it :/
				ListIterator<DirectedGraphNode> it = (new LinkedList<DirectedGraphNode>(nodes)).listIterator();
				boolean foundone = false;
				while (it.hasNext()) {
					cacheNode = it.next();
					if (cacheNode.getCclass().equals(node.getCclass())) {
						//combine
						foundone = true;
						
						for (DirectedWeightedEdge e : node.getEdges()) {
							cacheNode.addEdge((DirectedGraphNode) e.getDestination(), e.getWeight());
						}
					}
				}
				
				if (!foundone) {
					//add new
					nodes.add(node);
				}
				
			}
			
		}
	}
	
}
