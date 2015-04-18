package com.smanzana.JavaVis.Visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.apache.commons.collections15.Transformer;

import com.smanzana.JavaVis.Representation.DataRepresentation;
import com.smanzana.JavaVis.Representation.Graph.Graph;
import com.smanzana.JavaVis.Representation.Graph.GraphNode;
import com.smanzana.JavaVis.Representation.Graph.UndirectedWeightedEdge;
import com.smanzana.JavaVis.Representation.Tree.Tree;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class JungVisualization {
	
	private static final Paint classColor = Color.GREEN;
	
	private static final Paint interfaceColor = Color.BLUE;
	
	private static final Paint enumColor = Color.RED;
	
//	private static final class BrushPlugin extends PickingGraphMousePlugin<GraphNode, DirectedWeightedEdge> {
//		
//		JungVisualization vis;
//		
//		public BrushPlugin(JungVisualization vis) {
//			this.vis = vis;
//		}
//		
////		@Override
////		public void mousePressed(MouseEvent ev) {
////			super.mousePressed(ev);
////			if (vertex == null && edge == null) {
////				vis.vv.getPickedVertexState().clear();
////				vis.vv.getPickedEdgeState().clear();
////			}
////		}
//		
////		@Override
////		public void mouseReleased(MouseEvent ev) {
////			super.mouseReleased(ev);
////			if (vertex == null && edge == null) {
////				vis.vv.getPickedVertexState().clear();
////				vis.vv.getPickedEdgeState().clear();
////			}
////		}
//		
//	}
	
	
	
	private static final class SearchAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JungVisualization vis;
		
		//boolean enabled;
		
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
	
	private VisualizationViewer<GraphNode, UndirectedWeightedEdge> vv;
	
	private Layout<GraphNode, UndirectedWeightedEdge> layout;
	
	private DefaultModalGraphMouse<GraphNode, UndirectedWeightedEdge> mouse;
	
	private Map<DataRepresentation.RepresentationType, DataRepresentation> map;
	
	public JungVisualization() {
		map = new HashMap<DataRepresentation.RepresentationType, DataRepresentation>();
	}
	
	public void provideRepresentation(DataRepresentation.RepresentationType type, DataRepresentation data) {
		map.put(type, data);
	}
	
	public void Visualize(Graph graph) {
		edu.uci.ics.jung.graph.Graph<GraphNode, UndirectedWeightedEdge> visGraph = new DirectedSparseGraph<GraphNode, UndirectedWeightedEdge>();
		
		for (GraphNode node : graph.getNodes()) {
			visGraph.addVertex(node);
		}
		for (UndirectedWeightedEdge e : graph.getEdges()) {
			visGraph.addEdge(e, e.getEnds().getLeft(), e.getEnds().getRight());
		}
	
	    // The Layout<V, E> is parameterized by the vertex and edge types
	    layout = new CircleLayout<GraphNode, UndirectedWeightedEdge>(visGraph);
	    layout.setSize(new Dimension(600,600)); // sets the initial size of the space
	     // The BasicVisualizationServer<V,E> is parameterized by the edge types
	     vv = new VisualizationViewer<GraphNode, UndirectedWeightedEdge>(layout);
	     vv.setPreferredSize(new Dimension(700,700)); //Sets the viewing area size
	     
	     Transformer<GraphNode,Paint> vertexPaint = new Transformer<GraphNode, Paint>() {
	         public Paint transform(GraphNode node) {
	        	 if (node == null || node.getCclass() == null) {
	        		 return Color.DARK_GRAY;
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
	     
	     Transformer<GraphNode, Shape> vertexShape = new Transformer<GraphNode, Shape>() {
	    	public Shape transform(GraphNode node) {
	    		if (node == null) {
	    			return null;
	    		}
	    		
	    		double size = 30;
	    		
	    		if (node.getCclass() == null) {
	    			size = size / 2;
	    		} else if (vv.getPickedVertexState().isPicked(node)) {
	    			size = size * 1.25;
	    		}
	    		
	    		//Point2D point = layout.transform(node);
	    		//return new Ellipse2D.Double(point.getX() - size/2, point.getY() - size/2, size, size);
	    		return new Ellipse2D.Double(-size/2, -size/2, size, size);
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
	     vv.getRenderContext().setVertexShapeTransformer(vertexShape);
	     //vv.getRenderContext().setVer(vertexTooltip);
	     
	     
	     //create mouse
	     mouse = new DefaultModalGraphMouse<GraphNode, UndirectedWeightedEdge>();
	     mouse.setMode(Mode.TRANSFORMING);
	     //mouse.add(new BrushPlugin(this));
	     vv.addKeyListener(mouse.getModeKeyListener());
	     vv.setGraphMouse(mouse);
	     
	     window = new JFrame("JavaVis Code Explorer");
	     window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     
	     JPanel content = new JPanel();
	     content.setLayout(new BorderLayout());
	     content.setBorder(BorderFactory.createLoweredBevelBorder());
	     JPanel navigation = new JPanel();
	     navigation.setBorder(BorderFactory.createRaisedBevelBorder());
	     navigation.setLayout(new BorderLayout());
	     navigation.setPreferredSize(new Dimension(200,700));
	     
	     //set up two nested frames
	     window.getContentPane().setLayout(new BorderLayout());
	     window.getContentPane().add(content, BorderLayout.EAST);
	     window.getContentPane().add(navigation, BorderLayout.WEST);
	     

	     JToolBar toolbar = new JToolBar();
	     toolbar.add(new SearchAction(this));
	     content.add(toolbar, BorderLayout.PAGE_START);
	     
	     content.add(vv, BorderLayout.CENTER); 
	     

	     
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
		searchTerm = (String) JOptionPane.showInputDialog(window, "Search for objects that begin with:", searchTerm);
		
		if (searchTerm == null) {
			return;
		}
		
		mouse.setMode(Mode.PICKING);
		
		boolean gotResults = false;
		PickedState<GraphNode> info = vv.getPickedVertexState();
		for (GraphNode node : layout.getGraph().getVertices()) {
			if (node.getCclass().getName().startsWith(searchTerm)) {
				info.pick(node, true);
				gotResults = true;
			} else {
				info.pick(node, false);
			}
		}
		
		if (gotResults) {
			return;
		}
		//there were no matches. Let's try again with a no-case search
		
		for (GraphNode node : layout.getGraph().getVertices()) {
			if (node.getCclass().getName().toLowerCase().startsWith(searchTerm.toLowerCase())) {
				info.pick(node, true);
				gotResults = true;
			} else {
				info.pick(node, false);
			}
		}
		
		if (gotResults) {
			return;
		}
		
		//still no results. Let's search for a 'contains'
		for (GraphNode node : layout.getGraph().getVertices()) {
			if (node.getCclass().getName().contains(searchTerm)) {
				info.pick(node, true);
				gotResults = true;
			} else {
				info.pick(node, false);
			}
		}
		
		if (gotResults) {
			return;
		}
		
		//no results. finally a contains not-case-sensitive
		//still no results. Let's search for a 'contains'
				for (GraphNode node : layout.getGraph().getVertices()) {
					if (node.getCclass().getName().toLowerCase().contains(searchTerm.toLowerCase())) {
						info.pick(node, true);
						gotResults = true;
					} else {
						info.pick(node, false);
					}
				}
	}

}
