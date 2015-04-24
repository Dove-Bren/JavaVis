package com.smanzana.JavaVis.Visualization;

import java.io.File;
import java.io.IOException;

import com.smanzana.JavaVis.Parser.JSON;
import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Representation.DataRepresentation;
import com.smanzana.JavaVis.Representation.Graph.DirectedGraph;
import com.smanzana.JavaVis.Representation.Tree.Tree;

import java.awt.Desktop;

public class WebVisualization {
	
	public enum VisualizationType {
		EXTENDS,
		IMPLEMENTS;
	}
	
	public static final File resourceFolder = new File("web", "resources");
	
	public static final File implementsGraph = new File(resourceFolder, "extendsGraph.json");
	
	public static final File extendsTree = new File(resourceFolder, "implementsTree.json");
	
	public WebVisualization() {
		if (!resourceFolder.exists()) {
			resourceFolder.mkdirs();
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void Visualize(VisualizationType type, DataRepresentation data) {
		switch (type) {
		case IMPLEMENTS:
			if (!(data instanceof DirectedGraph)) {
				break;
			}
			JSON.toJSON(implementsGraph, (DirectedGraph) data);
			break;
		case EXTENDS:
			if (!(data instanceof Tree)) {
				break;
			}
			JSON.toJSON(extendsTree, (Tree<Cclass>) data);
			break;
		}
		
		
		
		
		
		
		

		// ...

		if(Desktop.isDesktopSupported())
		{
		  try {
			Desktop.getDesktop().browse(new File("web", "index.html").toURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	

}
