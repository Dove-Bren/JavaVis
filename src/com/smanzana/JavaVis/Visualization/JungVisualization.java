package com.smanzana.JavaVis.Visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import com.smanzana.JavaVis.Representation.Graph.DirectedGraph;
import com.smanzana.JavaVis.Representation.Graph.DirectedGraphNode;
import com.smanzana.JavaVis.Representation.Graph.DirectedWeightedEdge;
import com.smanzana.JavaVis.Representation.Graph.Graph;
import com.smanzana.JavaVis.Representation.Graph.GraphNode;
import com.smanzana.JavaVis.Representation.Tree.Tree;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class JungVisualization {

	public void Visualize(Graph graph) {
		// TODO Auto-generated method stub
		
				
		
	}
	
	public void Visualize(DirectedGraph graph) {
		edu.uci.ics.jung.graph.Graph<GraphNode, DirectedWeightedEdge> visGraph = new DirectedSparseGraph<GraphNode, DirectedWeightedEdge>();
		
		for (GraphNode node : graph.getNodes()) {
			visGraph.addVertex(node);
		}
		for (DirectedGraphNode node : graph.getNodes())
		for (DirectedWeightedEdge e : node.getEdges()) {
			visGraph.addEdge(e, e.getSource(), e.getDestination());
		}
	
	    // The Layout<V, E> is parameterized by the vertex and edge types
	    Layout<GraphNode, DirectedWeightedEdge> layout = new CircleLayout<GraphNode, DirectedWeightedEdge>(visGraph);
	    layout.setSize(new Dimension(300,300)); // sets the initial size of the space
	     // The BasicVisualizationServer<V,E> is parameterized by the edge types
	     BasicVisualizationServer<GraphNode, DirectedWeightedEdge> vv = 
	              new BasicVisualizationServer<GraphNode, DirectedWeightedEdge>(layout);
	     vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
	     
	     Transformer<GraphNode,Paint> vertexPaint = new Transformer<Integer,Paint>() {
	         public Paint transform(GraphNode node) {
	             node.getCclass().getType(); //switch on this!
	         }
	     };  
	     
	     JFrame frame = new JFrame("Simple Graph View");
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame.getContentPane().add(vv); 
	     frame.pack();
	     frame.setVisible(true);       
	}
	
	public void Visualize(Tree tree) {
		// TODO Auto-generated method stub
		
	}

}
