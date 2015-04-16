package com.smanzana.JavaVis.Visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

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
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;

public class JungVisualization {
	
	private static final Paint classColor = Color.GREEN;
	
	private static final Paint interfaceColor = Color.BLUE;
	
	private static final Paint enumColor = Color.RED;
	
	private static final class PopupPlugin extends AbstractPopupGraphMousePlugin {

		@Override
		protected void handlePopup(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("omg");
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				handlePopup(e);
			}
//			else {
//				return;
//			}
		}
		
	}

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
	     VisualizationViewer<GraphNode, DirectedWeightedEdge> vv = 
	              new VisualizationViewer<GraphNode, DirectedWeightedEdge>(layout);
	     vv.setPreferredSize(new Dimension(350,350)); //Sets the viewing area size
	     
	     Transformer<GraphNode,Paint> vertexPaint = new Transformer<GraphNode, Paint>() {
	         public Paint transform(GraphNode node) {
	        	 if (node == null || node.getCclass() == null) {
	        		 return classColor;
	        	 }
	             switch (node.getCclass().getType()) {
	             case CLASS:
	            	 return classColor;
	             case INTERFACE:
	            	 return interfaceColor;
	             case ENUM:
	             default:
	            	 return enumColor;
	             }
	         }
	     };  
	     
//	     Transformer<GraphNode, String> vertexTooltip = new Transformer<GraphNode, String>(){
//	    	public String transform(GraphNode node) {
//	    		if (node == null || node.getCclass() == null) {
//	    			return "Invalid Description!";
//	    		}
//	    		
//	    		String str = "";
//	    		
//	    		str = node.getName() + node.getCclass().getType().name() + "\n" +
//	    				node.getCclass().info();
//	    		
//	    		return str;
//	    	}
//	     };
	     
	     vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
	     //vv.getRenderContext().setVer(vertexTooltip);
	     
	     
	     //create mouse
	     DefaultModalGraphMouse mouse = new DefaultModalGraphMouse();
	     mouse.setMode(Mode.TRANSFORMING);
	     mouse.add(new PopupPlugin());
	     vv.addKeyListener(mouse.getModeKeyListener());
	     vv.setGraphMouse(mouse);
	     
	     JFrame frame = new JFrame("Simple Graph View");
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     frame.getContentPane().add(vv); 
	     

	     
	     //code from JUNG interaction tutorial
	     JMenuBar menuBar = new JMenuBar();
	     JMenu modeMenu = mouse.getModeMenu(); // Obtain mode menu from the mouse
	     modeMenu.setText("Mouse Mode");
	     modeMenu.setIcon(null); // I'm using this in a main menu
	     modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size 
	     menuBar.add(modeMenu);
	     frame.setJMenuBar(menuBar);
	     
	     
	     frame.pack();
	     frame.setVisible(true);     
	}
	
	public void Visualize(Tree tree) {
		// TODO Auto-generated method stub
		
	}

}
