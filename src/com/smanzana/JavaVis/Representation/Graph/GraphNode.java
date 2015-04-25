package com.smanzana.JavaVis.Representation.Graph;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;

public class GraphNode implements Comparable<GraphNode>{
	
	private String name;
	
	protected Cclass cclass;
	
	/**
	 * A unique id.<br />
	 * This is going to be <i>parentPackage</i>.<i>name</i><br />
	 * Where <i>parentPackage</i> is the name of the domain exactly <i>one</i> level up.
	 */
	private String uniqueKey; 
	
	public GraphNode() {
		this.name = "Invalid Node";
		this.uniqueKey = "Invalid." + this.name;
	}
	
	
	public GraphNode(Cclass cl) {
		this.name = cl.getName();
		this.uniqueKey = (cl.getPackageName().substring(cl.getPackageName().lastIndexOf(".") + 1)) + "." + this.name;
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
	
	public Cclass getCclass() {
		return cclass;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GraphNode)) {
			return false;
		}
		
		GraphNode n = (GraphNode) o;
		
		if (n.uniqueKey == uniqueKey) {
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(GraphNode node) {
		return cclass.compareTo(node.cclass);
	}
	
}
