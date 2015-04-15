package com.smanzana.JavaVis.Visualization;

import java.io.File;

import com.smanzana.JavaVis.Parser.JSON;
import com.smanzana.JavaVis.Representation.DataRepresentation;
import com.smanzana.JavaVis.Representation.Graph.DirectedGraph;
import com.smanzana.JavaVis.Representation.Tree.Tree;

public class WebVisualization {
	
	public enum VisualizationType {
		EXTENDS,
		IMPLEMENTS;
	}
	
	public static final File resourceFolder = new File("resources");
	
	public static final File extendsGraph = new File("resources", "extendsGraph.json");
	
	public static final File implementsTree = new File("resources", "implementsTree.json");
	
	public WebVisualization() {
		if (!resourceFolder.exists()) {
			resourceFolder.mkdirs();
		}
		
		
	}
	
	public void Visualize(VisualizationType type, DataRepresentation data) {
		switch (type) {
		case EXTENDS:
			if (!(data instanceof DirectedGraph)) {
				break;
			}
			JSON.toJSON(extendsGraph, (DirectedGraph) data);
			break;
		case IMPLEMENTS:
			if (!(data instanceof Tree)) {
				break;
			}
			JSON.toJSON(implementsTree, (Tree) data);
			break;
		}
	}
	
	

}
