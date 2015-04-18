package com.smanzana.JavaVis.Visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

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
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedInfo;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class JungVisualization {
	
	private static final Paint classColor = Color.GREEN;
	
	private static final Paint interfaceColor = Color.BLUE;
	
	private static final Paint enumColor = Color.RED;
	
	private static final class SearchPlugin extends AbstractPopupGraphMousePlugin {

		@Override
		protected void handlePopup(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//JFrame popup = new JPopupMenu("Search");
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				handlePopup(e);
			}
//			else {
//				return;
//			}
		}
		
	}
	
	private static final class SearchAction extends AbstractAction {
		
		private JungVisualization vis;
		
		boolean enabled;
		
		public SearchAction(JungVisualization vis) {
			super("Search", null);
			this.vis = vis;
			enabled = true;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			vis.performSearch();
		}

		
	}
	
	private JFrame window;
	
	private String searchTerm;
	
	private VisualizationViewer<GraphNode, DirectedWeightedEdge> vv;
	
	private Layout<GraphNode, DirectedWeightedEdge> layout;

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
	    layout = new CircleLayout<GraphNode, DirectedWeightedEdge>(visGraph);
	    layout.setSize(new Dimension(600,600)); // sets the initial size of the space
	     // The BasicVisualizationServer<V,E> is parameterized by the edge types
	     vv = new VisualizationViewer<GraphNode, DirectedWeightedEdge>(layout);
	     vv.setPreferredSize(new Dimension(700,700)); //Sets the viewing area size
	     
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
	     
	     Transformer<GraphNode, String> vertexLabel = new Transformer<GraphNode, String>() {
	    	 public String transform(GraphNode node) {
	    		 if (node == null || node.getCclass() == null) {
	    			 return "";
	    		 }
	    		 return node.getCclass().getName();
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
	     vv.setVertexToolTipTransformer(new ToStringLabeller<GraphNode>());
	     vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
	     //vv.getRenderContext().setVer(vertexTooltip);
	     
	     
	     //create mouse
	     DefaultModalGraphMouse mouse = new DefaultModalGraphMouse();
	     mouse.setMode(Mode.TRANSFORMING);
	     mouse.add(new SearchPlugin());
	     vv.addKeyListener(mouse.getModeKeyListener());
	     vv.setGraphMouse(mouse);
	     
	     window = new JFrame("Simple Graph View");
	     window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     

	     JToolBar toolbar = new JToolBar();
	     toolbar.add(new SearchAction(this));
	     window.getContentPane().setLayout(new BorderLayout());
	     window.getContentPane().add(toolbar, BorderLayout.PAGE_START);
	     
	     window.getContentPane().add(vv, BorderLayout.CENTER); 
	     

	     
	     //code from JUNG interaction tutorial
	     JMenuBar menuBar = new JMenuBar();
	     JMenu modeMenu = mouse.getModeMenu(); // Obtain mode menu from the mouse
	     modeMenu.setText("Mouse Mode");
	     modeMenu.setIcon(null); // I'm using this in a main menu
	     modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size 
	     menuBar.add(modeMenu);
	     
	     window.setJMenuBar(menuBar);
	     
	     
	     window.pack();
	     window.setVisible(true);     
	}
	
	public void Visualize(Tree tree) {
		// TODO Auto-generated method stub
		
	}
	
	
	private void performSearch() {
		if (window == null) {
			return;
		}
		if (searchTerm == null) {
			searchTerm = "";
		}
		searchTerm = (String) JOptionPane.showInputDialog(window, "Search for:", searchTerm);
		
		if (searchTerm == null) {
			return;
		}
		
		GraphNode result = null;
		PickedState<GraphNode> info = vv.getPickedVertexState();
		for (GraphNode node : layout.getGraph().getVertices()) {
			if (node.getCclass().getName().startsWith(searchTerm)) {
				info.pick(node, true);
			} else {
				info.pick(node, false);
			}
		}
	}

}
