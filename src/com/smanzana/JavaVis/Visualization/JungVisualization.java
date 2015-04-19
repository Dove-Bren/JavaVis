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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.apache.commons.collections15.Transformer;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Representation.DataRepresentation;
import com.smanzana.JavaVis.Representation.Tree.Tree;
import com.smanzana.JavaVis.Util.Pair;

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
	
	private static final class ViewAction extends AbstractAction{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JungVisualization vis;
		
		private DataRepresentation.RepresentationType type;
		
		public ViewAction(JungVisualization vis, DataRepresentation.RepresentationType type) {
			super(type.getTitle(), null);
			this.vis = vis;
			this.type = type;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (vis == null || vis.vv == null) {
				return;
			}
			
			vis.Visualize(type);
		}
		
	}
	
	private static final class InfoPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JTextField title;
		
		private JTextField packageInfo;
		
		private JTextArea statInfo;
		
		public InfoPanel() {
			super();
			this.title = new JTextField();
			this.packageInfo = new JTextField();
			this.statInfo = new JTextArea();
			
			title.setEditable(false);
			title.setMaximumSize(new Dimension(200, 75));
			packageInfo.setEditable(false);
			packageInfo.setMaximumSize(new Dimension(200, 50));
			statInfo.setEditable(false);
			statInfo.setMaximumSize(new Dimension(200, 600));
			

		    setBorder(BorderFactory.createRaisedBevelBorder());
		    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		    
		    add(title);
		    add(packageInfo);
		    add(statInfo);
		    add(Box.createVerticalGlue());

		    setPreferredSize(new Dimension(200,700));
		    
		    setTitle("");
		    setPackageInfo("");
		    setStatInfo("Pick a node on the right to see more information");
		}
		
		public void setTitle(String title) {
			this.title.setText(title);
		}
		
		public void setPackageInfo(String info) {
			this.packageInfo.setText(info);
		}
		
		public void setStatInfo(String info) {
			statInfo.setText(info);
		}
	}
	
	private JFrame window;
	
	private String searchTerm;
	
	private VisualizationViewer<Cclass, Pair<Cclass, Cclass>> vv;
	
	private Layout<Cclass, Pair<Cclass, Cclass>> layout;
	
	private DefaultModalGraphMouse<Cclass, Pair<Cclass, Cclass>> mouse;
	
	private Map<DataRepresentation.RepresentationType, DataRepresentation> map;
	
	public JungVisualization() {
		map = new HashMap<DataRepresentation.RepresentationType, DataRepresentation>();
	}
	
	public void provideRepresentation(DataRepresentation.RepresentationType type, DataRepresentation data) {
		map.put(type, data);
	}
	
	public void Visualize() {
		Visualize( (DataRepresentation.RepresentationType) null);
	}
	
	public void Visualize(DataRepresentation.RepresentationType type) {
		
		if (map.isEmpty()) {
			System.out.println("Unable to start visualization because there is no data to visualize!");
			return;
		}
		
		if (type == null) {
			type = map.keySet().iterator().next();
		}
		
		DataRepresentation dataRep = map.get(type);
		
		edu.uci.ics.jung.graph.Graph<Cclass, Pair<Cclass, Cclass>> visGraph = new DirectedSparseGraph<Cclass, Pair<Cclass, Cclass>>();
		
		
		
		for (Cclass node : dataRep.getClasses()) {
			visGraph.addVertex(node);
		}
		for (Pair<Cclass, Cclass> e : dataRep.getPairs()) {
			visGraph.addEdge(e, e.getLeft(), e.getRight());
		}
	
	    //create layout based on current loaded visualization
	    layout = new CircleLayout<Cclass, Pair<Cclass, Cclass>>(visGraph);
	    layout.setSize(new Dimension(600,600));
	    
	    //create a visualizatoin viewer if we don't have one, or update current with new type
	    if (vv == null) {
	    	vv = new VisualizationViewer<Cclass, Pair<Cclass, Cclass>>(layout);
		    vv.setPreferredSize(new Dimension(700,700)); //Sets the viewing area size
		    
		    Transformer<Cclass,Paint> vertexPaint = new Transformer<Cclass, Paint>() {
		         public Paint transform(Cclass node) {
		        	 if (node == null) {
		        		 return Color.DARK_GRAY;
		        	 }
		             switch (node.getType()) {
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
		     
		     Transformer<Cclass, String> vertexLabel = new Transformer<Cclass, String>() {
		    	 public String transform(Cclass node) {
		    		 if (node == null) {
		    			 return "";
		    		 }
		    		 return node.getName();
		    	 }
		     };
		     
		     Transformer<Cclass, Shape> vertexShape = new Transformer<Cclass, Shape>() {
		    	public Shape transform(Cclass node) {
		    		if (node == null) {
		    			return null;
		    		}
		    		
		    		double size = 30;
		    		
		    		if (vv.getPickedVertexState().isPicked(node)) {
		    			size = size * 1.25;
		    		}
		    		
		    		//Point2D point = layout.transform(node);
		    		//return new Ellipse2D.Double(point.getX() - size/2, point.getY() - size/2, size, size);
		    		return new Ellipse2D.Double(-size/2, -size/2, size, size);
		    	}
		     };
		     
//		     Transformer<GraphNode, String> vertexTooltip = new Transformer<GraphNode, String>(){
//		    	public String transform(GraphNode node) {
//		    		if (node == null || node.getCclass() == null) {
//		    			return "Invalid Description!";
//		    		}
//		    		
//		    		String str = "";
//		    		
//		    		str = node.getName() + node.getCclass().getType().name() + "\n" +
//		    				node.getCclass().info();
//		    		
//		    		return str;
//		    	}
//		     };
		     
		     vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		     vv.setVertexToolTipTransformer(new ToStringLabeller<Cclass>());
		     vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
		     vv.getRenderContext().setVertexShapeTransformer(vertexShape);
		     //vv.getRenderContext().setVer(vertexTooltip);
		     
		     
		     //create mouse
		     mouse = new DefaultModalGraphMouse<Cclass, Pair<Cclass, Cclass>>();
		     mouse.setMode(Mode.PICKING);
		     //mouse.add(new BrushPlugin(this));
		     vv.addKeyListener(mouse.getModeKeyListener());
		     vv.setGraphMouse(mouse);
		     
		     window = new JFrame("JavaVis Code Explorer");
		     window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		     
		     JPanel content = new JPanel();
		     content.setLayout(new BorderLayout());
		     content.setBorder(BorderFactory.createLoweredBevelBorder());
		     JPanel navigation = new InfoPanel();
		     
		     //set up two nested frames
		     window.getContentPane().setLayout(new BorderLayout());
		     window.getContentPane().add(content, BorderLayout.EAST);
		     window.getContentPane().add(navigation, BorderLayout.WEST);
		     

		     JToolBar toolbar = new JToolBar();
		     toolbar.add(new SearchAction(this));
		     content.add(toolbar, BorderLayout.PAGE_START);
		     
		     content.add(vv, BorderLayout.CENTER); 
		     

		     JMenuBar menuBar = new JMenuBar();

		     //Create menu for different views
		     JMenu viewMenu = new JMenu();
		     viewMenu.setText("View");
		     viewMenu.setIcon(null);
		     //viewMenu.add(a)
		     for (DataRepresentation.RepresentationType rt : map.keySet()) {
		    	 viewMenu.add(new ViewAction(this, rt));
		     }
		     
		     menuBar.add(viewMenu);
		     
		     //code from JUNG interaction tutorial
		     JMenu modeMenu = mouse.getModeMenu(); // Obtain mode menu from the mouse
		     modeMenu.setText("Mouse Mode");
		     modeMenu.setIcon(null); // I'm using this in a main menu
		     modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size 
		     menuBar.add(modeMenu);
		     
		     window.setJMenuBar(menuBar);
		     
		     
		     window.pack();
		     window.setVisible(true);   
	    } else {
	    	vv.setGraphLayout(layout);
	    }
	     
	      
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
		PickedState<Cclass> info = vv.getPickedVertexState();
		for (Cclass node : layout.getGraph().getVertices()) {
			if (node.getName().startsWith(searchTerm)) {
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
		
		for (Cclass node : layout.getGraph().getVertices()) {
			if (node.getName().toLowerCase().startsWith(searchTerm.toLowerCase())) {
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
		for (Cclass node : layout.getGraph().getVertices()) {
			if (node.getName().contains(searchTerm)) {
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
				for (Cclass node : layout.getGraph().getVertices()) {
					if (node.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
						info.pick(node, true);
						gotResults = true;
					} else {
						info.pick(node, false);
					}
				}
	}

}
