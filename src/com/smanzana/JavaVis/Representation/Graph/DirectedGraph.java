package com.smanzana.JavaVis.Representation.Graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Representation.DataRepresentation;
import com.smanzana.JavaVis.Util.Pair;

/**
 * Basic graph data structure.
 * @author Skyler
 *
 */
public class DirectedGraph extends Graph {
	
	private Set<DirectedGraphNode> nodes;
	
	public DirectedGraph() {
		nodes = new TreeSet<DirectedGraphNode>();
	}

	public Set<GraphNode> getNodes() {
		return new TreeSet<GraphNode>(nodes);
	}
	
	public Set<DirectedGraphNode> getDirectedNodes() {
		return nodes;
	}
	
	public Set<UndirectedWeightedEdge> getEdges() {
		Set<UndirectedWeightedEdge> edges = new TreeSet<UndirectedWeightedEdge>();
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
						break;
					}
				}
				
				if (!foundone) {
					//add new
					System.out.println("Adding new node in merge: " + node.getUniqueKey());
					nodes.add(node);
				}
				
			}
			return;
		}
		
		//not a directed graph. how about a tree?
		//if (data instanceof Tree) {
			//in luck, cause trees are directed!
			//Tree otherTree = (Tree) data;
			//grab verticies, throw them in
			for (Pair<Cclass, Cclass> pair : data.getPairs()) {
				Cclass src = pair.getLeft();
				Cclass dest = pair.getRight();
				DirectedGraphNode srcNode = null, destNode = null;
				
				Iterator<DirectedGraphNode> it = nodes.iterator();
				DirectedGraphNode curNode;
				
				while (it.hasNext()) {
					curNode = it.next();
					
					if (curNode.getCclass().equals(src)) {
						srcNode = curNode;
					}
					if (curNode.getCclass().equals(dest)) {
						destNode = curNode;
					}
				}
				
				if (srcNode == null) {
					srcNode = new DirectedGraphNode(src);
					nodes.add(srcNode);
				}
				if (destNode == null) {
					destNode = new DirectedGraphNode(dest);
					nodes.add(destNode);
				}
				
				//now made the edges from src to dest
				srcNode.addEdge(destNode);
			}
			
			
		
		//not a tree or a directed graph! Darn it! We'll go generic here.
		
	}
	
}
