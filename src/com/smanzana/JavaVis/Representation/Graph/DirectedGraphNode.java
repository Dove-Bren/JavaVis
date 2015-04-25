package com.smanzana.JavaVis.Representation.Graph;

import java.util.Set;
import java.util.TreeSet;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;

public class DirectedGraphNode extends GraphNode{
	
	private String name;
	
	/**
	 * A unique id.<br />
	 * This is going to be <i>parentPackage</i>.<i>name</i><br />
	 * Where <i>parentPackage</i> is the name of the domain exactly <i>one</i> level up.
	 */
	private String uniqueKey; 
	
	private Set<DirectedWeightedEdge> edges;
	
	
	public DirectedGraphNode(Cclass cl) {
		this.name = cl.getName();
		this.edges = new TreeSet<DirectedWeightedEdge>();
		this.uniqueKey = (cl.getPackageName().substring(cl.getPackageName().lastIndexOf(".") + 1)) + "." + this.name;
		this.cclass = cl;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the uniqueKey
	 */
	public String getUniqueKey() {
		return uniqueKey;
	}
	
	public Set<DirectedWeightedEdge> getEdges() {
		return edges;
	}
	
	public boolean addEdge(DirectedGraphNode to) {
		return addEdge(to, 1.0);
	}
	
	public boolean addEdge(DirectedGraphNode to, double weight) {
		
		
		DirectedWeightedEdge edge = new DirectedWeightedEdge(this, to, weight);
		
		return edges.add(edge);
	}
	
	
	
	@Override
	public String toString() {
		String out = uniqueKey;
		
		if (edges != null && !edges.isEmpty()) {
			out += "\nEdges: \n[\n";
			for (DirectedWeightedEdge edge : edges) {
				out += "  " + edge + "\n";
			}
			out += "]";
		}
		
		
		
		return out;
	}
	
	@Override
	public boolean equals(Object o) {
		return (o.toString().equals(this.toString()));
	}
}
