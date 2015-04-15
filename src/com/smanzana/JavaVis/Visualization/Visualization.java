package com.smanzana.JavaVis.Visualization;

import com.smanzana.JavaVis.Representation.Graph.Graph;
import com.smanzana.JavaVis.Representation.Tree.Tree;

/**
 * Visualization interface class
 * @author Skyler
 *
 */
public interface Visualization {
	
	/**
	 * Visualize the provided graph
	 * @param graph
	 */
	public void Visualize(Graph graph);
	
	/**
	 * Visualize the provided tree
	 * @param tree
	 */
	public void Visualize(Tree tree);
	
}
