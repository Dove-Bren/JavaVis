package com.smanzana.JavaVis.Visualization;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.collections15.Transformer;

import com.smanzana.JavaVis.Driver;
import com.smanzana.JavaVis.Parser.ClassDeclaration;
import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Representation.DataRepresentation;
import com.smanzana.JavaVis.Representation.DataRepresentation.RepresentationType;
import com.smanzana.JavaVis.Representation.Graph.DirectedGraph;
import com.smanzana.JavaVis.Representation.Tree.Tree;
import com.smanzana.JavaVis.Util.WeightedPair;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

public class JungVisualization {
	
	private enum colors {
		Class_Color,
		Interface_Color,
		Enumeration_Color,
		Node_Hightlight,
		Edge_Color,
		Edge_Highlight,
		Background_Color;
	}
	
	private Map<colors, Color> colorMap;
	
//	static final Paint classColor = new Color(0, 20, 255);
//	
//	private static final Paint interfaceColor = new Color(0, 60, 60);
//	
//	private static final Paint enumColor = new Color(120, 0, 60);
	
	
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
	
	private static final class FileSelectAction extends AbstractAction {
		
		private static final long serialVersionUID = 1L;
		
		//private JungVisualization vis;
		
		public FileSelectAction() {
			//this.vis = vis;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JFileChooser fc = new JFileChooser("Open A System");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
			
			int result = fc.showDialog(JungVisualization.window, "Select Directory");
			
			if (result == JFileChooser.APPROVE_OPTION) {
	            File file = fc.getSelectedFile();
	            Driver.main(new String[] {file.getAbsolutePath()});
	        }
		}
	}
	
	private static final class ColorSelectAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private static class ColorChangeAction extends AbstractAction {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			private colors key;
			
			private JungVisualization vis;
			
			ColorSelectAction act;
			
			public ColorChangeAction(ColorSelectAction act, JungVisualization vis, colors colorKey) {
				this.key = colorKey;
				this.vis = vis;
				this.act = act;
			}
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Color color = JColorChooser.showDialog(JungVisualization.window, "Class Color", vis.colorMap.get(key));
				
				if (color == null) {
					color = Color.BLUE;
				}
				
				vis.setColor(key, color);
				
				act.reloadColors();
			    vis.vv.setBackground(vis.colorMap.get(colors.Background_Color));
				
				JungVisualization.window.repaint();
			}
			
		}
		
		private JungVisualization vis;
		
		private Map<colors, JButton> buttons;
		
		public ColorSelectAction(JungVisualization vis) {
			this.vis = vis;
			buttons = new HashMap<colors, JButton>();
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JDialog dialog = new JDialog(JungVisualization.window, "Color Selection");
			JPanel pane = new JPanel();
			dialog.add(pane);
			pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
//			JPanel buttonPanel = new JPanel();
//			JPanel labelPanel = new JPanel();
//			buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
//			labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));
//			pane.add(labelPanel);
//			pane.add(buttonPanel);
			
			
			//4 colors to choose: One for each type of 'class' and one for background color. Oh
			//crap also 1 for highlight color, 1 for edge color, 1 for highlighted edge color!
			//lets go with the origin 3 first!
			
			for (colors c : JungVisualization.colors.values()) {
				JPanel butPanel = new JPanel();
				pane.add(butPanel);
				butPanel.setLayout(new BoxLayout(butPanel, BoxLayout.LINE_AXIS));
				JButton but = new JButton();
				but.setAction(new ColorChangeAction(this, vis, c));
				but.setIcon(new ImageIcon(makeImage(new Rectangle(30, 30), vis.colorMap.get(c))));
				buttons.put(c, but);
				JLabel lab = new JLabel();
				lab.setText(c.name());
				lab.setHorizontalTextPosition(JLabel.RIGHT);
				butPanel.add(lab);
				butPanel.add(Box.createHorizontalGlue());
				butPanel.add(but);
			}
			
//			Image classS = makeImage(new Rectangle(30, 30), vis.colorMap.get(colors.classColor));
//			dialog.setLayout(new BorderLayout());
//			JButton classColorButton = new JButton();
//			classColorButton.setAction(new ColorChangeAction(vis, colors.classColor));
//			classColorButton.setIcon(new ImageIcon(classS));
//			dialog.add(classColorButton);
			
			
			dialog.setMinimumSize(new Dimension(200, 10));
			dialog.pack();
			dialog.setVisible(true);
		}
		
		protected void reloadColors() {
			for (Entry<colors, JButton> e : buttons.entrySet()) {
				e.getValue().setIcon(new ImageIcon(makeImage(new Rectangle(30, 30), vis.colorMap.get(e.getKey()))));
			}
		}
		
		public Image makeImage(Shape s, Color fillColor) {
		    Rectangle r = s.getBounds();
		    BufferedImage image = new BufferedImage(r.width, r.height, BufferedImage.TYPE_INT_RGB);
		    Graphics2D gr = image.createGraphics();
		    // move the shape in the region of the image
		    gr.translate(-r.x, -r.y);
		    gr.setColor(fillColor);
		    gr.fill(s);
		    gr.dispose();
		    return image;
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
			 
			for (JCheckBoxMenuItem but : vis.customButtons) {
				but.setEnabled(type == RepresentationType.custom);
			}
			
			if (type == RepresentationType.custom) {
				DirectedGraph graph = new DirectedGraph();
				for (RepresentationType type : vis.customEnabledTypes) {
					graph.mergeFrom(vis.map.get(type));
				}
				
				vis.map.put(RepresentationType.custom, graph);

			}

			if (vis.layout instanceof CircleLayout<?, ?>) {
				vis.layout = null;
			}
			
			vis.Visualize(type);
		}
		
	}
	
	private static final class IncludeObjectAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 852235214270217247L;

		private JungVisualization vis;
		
		private AbstractButton but;
		
		public IncludeObjectAction(JungVisualization vis, AbstractButton but) {
			this.vis = vis;
			this.but = but;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (but.isSelected()) {
				vis.includeObject = true;
			} else {
				vis.includeObject = false;
			}
			vis.Visualize(vis.lastType);
		}
		
	}
	
	private static final class EdgeWeightAction extends AbstractAction {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 852235214270217247L;

		private JungVisualization vis;
		
		private AbstractButton but;
		
		public EdgeWeightAction(JungVisualization vis, AbstractButton but) {
			this.vis = vis;
			this.but = but;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if (but.isSelected()) {
				vis.edgeWeight = true;
			} else {
				vis.edgeWeight = false;
			}
			vis.Visualize(vis.lastType);
		}
		
	}
	
	private static final class DeadNodeAction extends AbstractAction {
		private JungVisualization vis;
		
		private AbstractButton but;
		
		public DeadNodeAction(JungVisualization vis, AbstractButton but) {
			this.vis = vis;
			this.but = but;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (but.isSelected())  {
				vis.deadNodes = true;
			} else {
				vis.deadNodes = false;
			}
			vis.Visualize(vis.lastType);
		}
	}
	
	private static final class LayoutAction extends AbstractAction{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		JungVisualization vis;
		
		Class<? extends Layout<Cclass, WeightedPair<Cclass, Cclass>>> layoutClass;
		
		@SuppressWarnings("unchecked")
		public LayoutAction(JungVisualization vis, @SuppressWarnings("rawtypes") Class<? extends Layout> layoutClass) {
			this.vis = vis;
			this.layoutClass = (Class<Layout<Cclass, WeightedPair<Cclass, Cclass>>>)layoutClass;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				vis.layout = layoutClass.getConstructor( (Class<Graph<Cclass, WeightedPair<Cclass, Cclass>>>)(Class<?>)Graph.class  ).newInstance(vis.layout.getGraph());
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//vis.vv.setLayout(layout);
			vis.vv.setGraphLayout(vis.layout);
		}
		
	}
	
	private static final class CustomOptionAction extends AbstractAction{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JungVisualization vis;
		
		private RepresentationType type;
		
		private boolean activated = true; //default true
		
		public CustomOptionAction(JungVisualization vis, DataRepresentation.RepresentationType type) {
			this.vis = vis;
			this.type = type;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (activated) {
				//was activated. turning off
				activated = false;
				vis.customEnabledTypes.remove(this.type);
				
			} else {
				activated = true;
				vis.customEnabledTypes.add(type);
			}
			
			//regardless of if it's on or off, recreated visualization and display it
			DirectedGraph graph = new DirectedGraph();
			for (RepresentationType type : vis.customEnabledTypes) {
				graph.mergeFrom(vis.map.get(type));
			}
			
			vis.map.put(RepresentationType.custom, graph);

			vis.Visualize(RepresentationType.custom);
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
		
		public InfoPanel(JungVisualization vis) {
			super();
			
			this.title = new JTextField(18);
			this.packageInfo = new JTextField(18);
			this.statInfo = new JTextArea(18,50);
			statInfo.setLineWrap(true);
			statInfo.setWrapStyleWord(true);
			
			title.setEditable(false);
			title.setMaximumSize(new Dimension(300, 75));
			packageInfo.setEditable(false);
			packageInfo.setMaximumSize(new Dimension(300, 50));
			statInfo.setEditable(false);
			statInfo.setMaximumSize(new Dimension(300, 600));
			

		    setBorder(BorderFactory.createRaisedBevelBorder());
		    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		    
		    add(title);
		    add(packageInfo);
		    add(statInfo);
		    

		     if (vis.map.keySet().contains(RepresentationType.all)) {
			     TreePanel packageTreePanel = new TreePanel(vis);
			     add(packageTreePanel);
			     packageTreePanel.CreateTree(vis.map.get(RepresentationType.all).getClasses());
			     packageTreePanel.setMinimumSize(new Dimension(300, 100));
			     packageTreePanel.setVisible(true);
		     }
		    
		    add(Box.createVerticalGlue());

		    setPreferredSize(new Dimension(300,700));
		    
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

	private static final class TreePanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JungVisualization vis;
		
		private static class HighlightSelectionListener implements ListSelectionListener {
			
			private JungVisualization vis;
			
			public HighlightSelectionListener(JungVisualization vis) {
				this.vis = vis;
			}
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				
				if (arg0.getValueIsAdjusting()) {
					return;
				}
				
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) arg0.getSource();
				
				if (list.getSelectedIndex() == -1) {
					vis.infoPanel.setPackageInfo("");
					vis.infoPanel.setTitle("");
					vis.infoPanel.setStatInfo("Pick a node on the right to see more information");
					return;
				}
				PickedState<Cclass> info = vis.vv.getPickedVertexState();
				info.clear();
				
				ListModel<String> model = list.getModel();
								
				for (int i = arg0.getFirstIndex(); i <= arg0.getLastIndex(); i++) {
					
					if (!list.isSelectedIndex(i)) {
						continue;
					}
					//we want to do one of two things:
					//if wehave selected a package, highlight ALL classes in that package
					
					//if we select a class, just select that class
					String line = model.getElementAt(i).trim();
					
					if (line.startsWith("[")) {
						String packName = line.substring(1, line.length() - 1);
						
						for (Cclass node : vis.layout.getGraph().getVertices()) {
							
							if (node.getPackageName().startsWith(packName)) {
								info.pick(node, true);
							}
							
						}
					} else {
					
						for (Cclass node : vis.layout.getGraph().getVertices()) {
							
							if (node.getName().equals(model.getElementAt(i).trim())) {
								info.pick(node, true);
								break;
							}
						}
					}
				}
				
				vis.updateInfo();
			}
			
		}
		
		private static final class PackageWrapper {
			//lol holds a list of classes in this package
			
			private String name;
			
			private Set<Cclass> classes;
			
			public PackageWrapper(String name) {
				this.name = name;
				this.classes = new HashSet<Cclass>();
			}
			
			public void addClass(Cclass cclass) {
				classes.add(cclass);
			}
			
			@SuppressWarnings("unused")
			public boolean contains(Cclass cclass) {
				return classes.contains(cclass);
			}
			
			@Override
			public boolean equals(Object o) {
				return this.toString().equals(o.toString());
			}
			
			@Override
			public String toString() {
				return "PackageWrapper[" + this.name + "]";
			}
		}
		
		
		private Tree<PackageWrapper> packageTree;
		
		public TreePanel(JungVisualization vis) {
			this.vis = vis;

		}
		
		/**
		 * Works with the passed classes to reverse-engineer a package tree D:
		 * @param classes
		 */
		public void CreateTree(Set<Cclass> classes) {
			//k what am I gonna have to do? Go through each class and construct a package tree
			
			//lol FIRST we get a list of all package names! Make this easier
			List<String> packageNames = new LinkedList<String>();
			for (Cclass c : classes) {
				if (!packageNames.contains(c.getPackageName())) {
					packageNames.add(c.getPackageName());
				}
			}
			
			java.util.Collections.sort(packageNames);
			packageTree = new Tree<PackageWrapper>(new PackageWrapper("NEVER MATCHS LULZ OMG"));
			
			for (String name : packageNames) {
				//System.out.println(name);
				Tree<PackageWrapper> tree = getPackageWrapper(packageTree, name);
				
				if (tree == null) {
					packageTree.addChild(new Tree<PackageWrapper>(new PackageWrapper(name)));
				} else if (!tree.getCclass().name.equals(name)) {
					//found it's new parent
					tree.addChild(new Tree<PackageWrapper>(new PackageWrapper(name)));
				}
				
			}
			
			//finally have a tree of the packages!
			
			//now go through classes and match them to their packages
			PackageWrapper wrap;
			Tree<PackageWrapper> subTree;
			for (Cclass c : classes) {
				subTree = getPackageWrapper(packageTree, c.getPackageName());
				wrap = subTree.getCclass();
				
				if (wrap.name.equals(c.getPackageName())) {
					wrap.addClass(c);
				} else {
					System.out.println("Error encountered when constructing package tree on class [" + "]");
				}
			}
			
			
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			String treeString = "";
			
			for (Tree<PackageWrapper> t : packageTree.getChildren()) {
				treeString += treeSummary(t, 0);
			}
			
			Scanner parser = new Scanner(treeString);
			String line; 
			while (parser.hasNextLine()) {
				line = parser.nextLine();
				if (line.trim().isEmpty()) {
					continue;
				}
				
				listModel.addElement(line);
			}
			parser.close();
			
			JList<String> list = new JList<String>(listModel);
			
			//format list
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			list.setLayoutOrientation(JList.VERTICAL);
			( (DefaultListCellRenderer)  list.getCellRenderer()).setHorizontalAlignment(JLabel.LEFT);
			Font oldFont = list.getFont();
			Font newFont = new Font(oldFont.getName(), oldFont.getStyle(), 10);
			//( (DefaultListCellRenderer)  list.getCellRenderer())
			list.setFont(newFont);
			list.setVisible(true);
			list.addListSelectionListener(new HighlightSelectionListener(vis));
//			list.setMinimumSize(new Dimension(300, 100));
//			list.setPreferredSize(new Dimension(300, 400));
			
			JScrollPane pane = new JScrollPane(list);
			pane.setVisible(true);
			pane.setMinimumSize(new Dimension(300, 100));
			pane.setPreferredSize(new Dimension(300, 400));
			
			add(pane);
			
		}
		
		/**
		 * Returns EITHER the package itself or the closest it got. This is so you (I) can create
		 * a child right off from the paren tif the names don't match! It's hacky, but oh well :S
		 * @param tree
		 * @param packName
		 * @return
		 */
		private Tree<PackageWrapper> getPackageWrapper(Tree<PackageWrapper> tree, String packName) {
			if (tree == null) {
				return null;
			}
			if (tree.getCclass().name.equals(packName)) {
				return tree;
			}
			
			//recurve search!
			if (tree.getChildren().isEmpty()) {
				return tree;
			}
			
			for (Tree<PackageWrapper> subTree : tree.getChildren()) {
				if (packName.startsWith(subTree.getCclass().name)) {
					return getPackageWrapper(subTree, packName);
				}
			}
			
			return tree;
		}
		
//		private Tree<PackageWrapper> getPackageParent(Tree<PackageWrapper> tree, String packName) {
//			
//		}
		
		private String treeSummary(Tree<PackageWrapper> tree, int depth) {
			String buildString = "";
			String tabLength = "";
			
			if (tree == null) {
				return buildString;
			}
			
			//construct/cache tabLength
			for (int i = 0; i < depth; i++) {
				tabLength += "  ";
			}
			
			
			buildString += tabLength + "[" + tree.getCclass().name + "]" + System.getProperty("line.separator");
			//we need to print out the subtrees AND sub classes!
			
			//subtrees first!
			if (!tree.getChildren().isEmpty()) {
				for (Tree<PackageWrapper> t : tree.getChildren()) {
					buildString += treeSummary(t, depth + 1);
					//we add "  " for space to indicate nesting!
				}
			}
			
			//now classes
			if (tree.getCclass().classes.size() != 0) {
				for (Cclass c: tree.getCclass().classes) {
					buildString += tabLength + "  " + c.getName() + System.getProperty("line.separator");
				}
			}
			
			return buildString;
		}
		
		
		
	}
	
	private static JFrame window;
	
	private String searchTerm;
	
	private VisualizationViewer<Cclass, WeightedPair<Cclass, Cclass>> vv;
	
	private RepresentationType lastType;
	
	private InfoPanel infoPanel;
	
	private Set<JCheckBoxMenuItem> customButtons;
	
	private Layout<Cclass, WeightedPair<Cclass, Cclass>> layout;
	
	private DefaultModalGraphMouse<Cclass, WeightedPair<Cclass, Cclass>> mouse;
	
	private Map<DataRepresentation.RepresentationType, DataRepresentation> map;
	
	private boolean includeObject;
	
	private boolean edgeWeight;
	
	private boolean deadNodes;
	
	private Set<RepresentationType> customEnabledTypes;
	
	public JungVisualization() {
		map = new TreeMap<DataRepresentation.RepresentationType, DataRepresentation>();
		includeObject = true;
		edgeWeight = true;
		deadNodes = false;
		customEnabledTypes = new HashSet<RepresentationType>();
		customButtons = new HashSet<JCheckBoxMenuItem>();
		colorMap = new HashMap<colors, Color>();
		
		initColors();
		
		if (JungVisualization.window == null) {
			JungVisualization.window = new JFrame("JavaVis Code Explorer");
		}
	}
	
	private void initColors() {
		colorMap.put(colors.Class_Color, new Color(0, 102, 153));
		colorMap.put(colors.Interface_Color, new Color(0, 60, 60));
		colorMap.put(colors.Enumeration_Color, new Color(120, 00, 60));
		colorMap.put(colors.Edge_Color, new Color(255, 255, 255));
		colorMap.put(colors.Edge_Highlight, new Color(0, 255, 0));
		colorMap.put(colors.Background_Color, new Color(0, 0, 0));
		colorMap.put(colors.Node_Hightlight, new Color(0, 255, 0));
	}
	
	public void setColor(colors type, Color c) {
		colorMap.put(type, c);
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
			if (type == RepresentationType.custom || type == RepresentationType.all) {
				Iterator<RepresentationType> it = map.keySet().iterator();
				
				while (it.hasNext() && (type == RepresentationType.custom || type == RepresentationType.all)) {
					type = it.next();
				}
			}
		}
		
		lastType = type;
		
		DataRepresentation dataRep = map.get(type);
		
		edu.uci.ics.jung.graph.Graph<Cclass, WeightedPair<Cclass, Cclass>> visGraph = new edu.uci.ics.jung.graph.DirectedSparseMultigraph<Cclass, WeightedPair<Cclass, Cclass>>();
		
		
		if (includeObject) {
			if (deadNodes) {
				for (Cclass node : dataRep.getClasses()) {
					visGraph.addVertex(node);
				}
			}
			for (WeightedPair<Cclass, Cclass> e : dataRep.getWeightedPairs()) {
				visGraph.addEdge(e, e.getLeft(), e.getRight());
			}
		} else {
			if (deadNodes) {
				for (Cclass node: dataRep.getClasses()) {
					if (!(node.getPackageName() + "." + node.getName()).equals("java.lang.Object")) {
						visGraph.addVertex(node);
					}
				}
			}
			for (WeightedPair<Cclass, Cclass> e : dataRep.getWeightedPairs()) {
				if ((e.getLeft().getPackageName() + "." + e.getLeft().getName()).equals("java.lang.Object") || (e.getRight().getPackageName() + "." + e.getRight().getName()).equals("java.lang.Object")) {
					continue;
				}
					visGraph.addEdge(e, e.getLeft(), e.getRight());
			}
		}
	
	    //create layout if we don't alrady have one
		if (layout == null) {
		    layout = new CircleLayout<Cclass, WeightedPair<Cclass, Cclass>>(visGraph);
		    layout.setSize(new Dimension(600,600));
		} else {
			layout.setGraph(visGraph);
		}
	    
	    //create a visualizatoin viewer if we don't have one, or update current with new type
	    if (vv == null) {
	    	vv = new VisualizationViewer<Cclass, WeightedPair<Cclass, Cclass>>(layout);
		    vv.setPreferredSize(new Dimension(700,700)); //Sets the viewing area size
		    
		    Transformer<Cclass,Paint> vertexPaint = new Transformer<Cclass, Paint>() {
		         public Paint transform(Cclass node) {
		        	 if (node == null) {
		        		 return Color.GRAY;
		        	 }
		             switch (node.getType()) {
		             case CLASS:
		            	 return colorMap.get(colors.Class_Color);
		             case INTERFACE:
		            	 return colorMap.get(colors.Interface_Color);
		             case ENUM:
		             default:
		            	 return colorMap.get(colors.Enumeration_Color);
		             }
		         }
		     };  
		     
		     Transformer<WeightedPair<Cclass, Cclass>, Paint> edgePaint = new Transformer<WeightedPair<Cclass, Cclass>, Paint>() {

				@Override
				public Paint transform(WeightedPair<Cclass, Cclass> arg0) {
					updateInfo();
					if (vv.getPickedEdgeState().isPicked(arg0)) {
						return colorMap.get(colors.Edge_Highlight);
					} else {
						return colorMap.get(colors.Edge_Color);
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
		    			
		    			updateInfo();
		    		}
		    		//Point2D point = layout.transform(node);
		    		//return new Ellipse2D.Double(point.getX() - size/2, point.getY() - size/2, size, size);
		    		return new Ellipse2D.Double(-size/2, -size/2, size, size);
		    	}
		     };
		     
		     Transformer<Cclass, Paint> vertexOutline = new Transformer<Cclass, Paint>() {
		    	 public Paint transform(Cclass node) {
		    		if (vv.getPickedVertexState().isPicked(node)) {
		    			return colorMap.get(colors.Node_Hightlight);
		    		} else {
		    			return Color.BLACK;
		    		}
		    	 }
		     };
		     
		     Transformer<WeightedPair<Cclass, Cclass>, Stroke> edgeStroke = new Transformer<WeightedPair<Cclass, Cclass>, Stroke>() {
		    	 public Stroke transform(WeightedPair<Cclass, Cclass> node) {
		    		 if (!edgeWeight) {
		    			 return new BasicStroke(1.0f);
		    		 }
		    		return new BasicStroke((float) node.getWeight());
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
		     DefaultVertexLabelRenderer rend = new DefaultVertexLabelRenderer(new Color(180, 255, 180));
		     rend.setHorizontalAlignment(SwingConstants.RIGHT);
		     rend.setHorizontalTextPosition(SwingConstants.RIGHT);
		     
		     vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		     vv.setVertexToolTipTransformer(new ToStringLabeller<Cclass>());
		     vv.getRenderContext().setVertexLabelTransformer(vertexLabel);
		     vv.getRenderContext().setVertexShapeTransformer(vertexShape);
		     vv.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
		     vv.getRenderContext().setArrowDrawPaintTransformer(edgePaint);
		     vv.getRenderContext().setVertexDrawPaintTransformer(vertexOutline);
		     vv.getRenderContext().setEdgeStrokeTransformer(edgeStroke);
		     
		     vv.getRenderContext().setVertexLabelRenderer(rend);
		     
		     
		     //create mouse
		     mouse = new DefaultModalGraphMouse<Cclass, WeightedPair<Cclass, Cclass>>();
		     //mouse.add(new SelectMousePlugin(this));
		     //mouse.add(new BrushPlugin(this));
		     vv.addKeyListener(mouse.getModeKeyListener());
		     vv.setGraphMouse(mouse);
		     mouse.setMode(Mode.PICKING);
		     
		     //vv.setPickSupport(new PickTatle(this));
		     
		     
		     //window = new JFrame("JavaVis Code Explorer");
		     window.getContentPane().removeAll();
		     window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		     
		     JPanel content = new JPanel();
		     content.setLayout(new BorderLayout());
		     content.setBorder(BorderFactory.createLoweredBevelBorder());
		     infoPanel = new InfoPanel(this);
		     
		     //set up two nested frames
		     window.getContentPane().setLayout(new BorderLayout());
		     window.getContentPane().add(content, BorderLayout.EAST);
		     window.getContentPane().add(infoPanel, BorderLayout.WEST);
		     

		     JToolBar toolbar = new JToolBar();
		     toolbar.add(new SearchAction(this));
		     content.add(toolbar, BorderLayout.PAGE_START);
		     vv.setBackground(colorMap.get(colors.Background_Color));
		     content.add(vv, BorderLayout.CENTER); 
		     

		     JMenuBar menuBar = new JMenuBar();
		     
		     //create file menu
		     JMenu fileMenu = new JMenu();
		     fileMenu.setText("File");
		     JMenuItem open = new JMenuItem();
		     open.setAction(new FileSelectAction());
		     open.setText("Open");
		     fileMenu.add(open);
		     
		     menuBar.add(fileMenu);

		     //Create menu for different views
		     JMenu viewMenu = new JMenu();
		     viewMenu.setText("View");
		     viewMenu.setIcon(null);
		     //viewMenu.add(a)
		     
//		     //quickly sort our map so the order is the same
//		     Comparator<Entry<RepresentationType, DataRepresentation> >entryCompare = new Comparator<Entry<RepresentationType, DataRepresentation>>() {
//
//				@Override
//				public int compare(
//						Entry<RepresentationType, DataRepresentation> arg0,
//						Entry<RepresentationType, DataRepresentation> arg1) {
//					return (arg0.getKey().getTitle().compareTo(arg1.getKey().getTitle()));
//				}
//		    	 
//		     };
//		     List<Entry<RepresentationType, DataRepresentation>> dataList = 
//		    		 new LinkedList<Entry<RepresentationType, DataRepresentation>> (map.entrySet());
//		     Collections.sort(dataList, entryCompare);
//		     
//		     map.clear();
//		     for (Entry<RepresentationType, DataRepresentation> entry : dataList) {
//		    	 map.put(entry.getKey(), entry.getValue());
//		     }
//		     
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
		     mouse.setMode(Mode.PICKING);
		     
		     JMenu layoutMenu = new JMenu();
		     layoutMenu.setText("Layout");
		     layoutMenu.setIcon(null);
		     JRadioButtonMenuItem layoutBut;
		     ButtonGroup group = new ButtonGroup();
		     	layoutBut = new JRadioButtonMenuItem("Circle");
		     	layoutBut.setSelected(true);
		     	layoutBut.setAction(new LayoutAction(this, CircleLayout.class));
		     	layoutBut.setIcon(null);
		     	layoutBut.setText("Circle");
		     	group.add(layoutBut);
		     	layoutMenu.add(layoutBut);
		     	
		     	layoutBut = new JRadioButtonMenuItem("Force Directed");
		     	layoutBut.setSelected(false);
		     	layoutBut.setAction(new LayoutAction(this, FRLayout.class));
		     	group.add(layoutBut);
		     	layoutMenu.add(layoutBut);
		     	layoutBut.setText("Force Directed");
		     	
		     	layoutBut = new JRadioButtonMenuItem("Spring");
		     	layoutBut.setSelected(false);
		     	layoutBut.setAction(new LayoutAction(this, SpringLayout.class));
		     	group.add(layoutBut);
		     	layoutMenu.add(layoutBut);
		     	layoutBut.setText("Spring");
		     
		     menuBar.add(layoutMenu);
		     
		     JMenu optionMenu = new JMenu();
		     optionMenu.setText("Options");
		     optionMenu.setIcon(null);
		     //JButton colorBut = new JButton("Select Colors");
		     JMenuItem colorBut = new JMenuItem();
		     colorBut.setAction(new ColorSelectAction(this));
		     colorBut.setText("Select Colors");
		     optionMenu.add(colorBut);
		     
			     JCheckBoxMenuItem includeObject = new JCheckBoxMenuItem("Include java.lang.Object");
			     includeObject.setSelected(true);
			     includeObject.setAction(new IncludeObjectAction(this, includeObject));
			     includeObject.setText("Include java.lang.Object");
			     optionMenu.add(includeObject);
			     JCheckBoxMenuItem edgeWeight = new JCheckBoxMenuItem("Visualize Edge Weights");
			     edgeWeight.setSelected(true);
			     edgeWeight.setAction(new EdgeWeightAction(this, edgeWeight));
			     edgeWeight.setText("Visualize Edge Weights");
			     optionMenu.add(edgeWeight);
			     JCheckBoxMenuItem deadNodes = new JCheckBoxMenuItem();
			     deadNodes.setAction(new DeadNodeAction(this, deadNodes));
			     deadNodes.setSelected(false);
			     deadNodes.setText("Display Dead Nodes");
			     optionMenu.add(deadNodes);
			     optionMenu.addSeparator();
			     	for (RepresentationType enableType : map.keySet()) {
			     		if (enableType == RepresentationType.custom || enableType == RepresentationType.all) {
			     			continue;
			     		}
					    JCheckBoxMenuItem CustomButton = new JCheckBoxMenuItem();
			     		CustomButton.setAction(new CustomOptionAction(this, enableType));
			     		CustomButton.setSelected(true);
			     		CustomButton.setText(enableType.getTitle());
			     		CustomButton.setEnabled(false);
			     		optionMenu.add(CustomButton);
			     		customEnabledTypes.add(enableType);
			     		customButtons.add(CustomButton);
			     	}
		     menuBar.add(optionMenu);
		     
		     window.setJMenuBar(menuBar);
		     
		     
		     window.pack();
		     window.setVisible(true);   
	    } else {
	    	vv.setGraphLayout(layout);
	    }
	     
	      
	}
	
//	public void Visualize(Tree tree) {
//		// TODO Auto-generated method stub
//		
//	}
	
	
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
	
	private void updateInfo() {
		if (!(vv.getPickedVertexState() == null) && !vv.getPickedVertexState().getPicked().isEmpty()) {
			updateVertices();
		} else if (!(vv.getPickedEdgeState() == null) && !vv.getPickedEdgeState().getPicked().isEmpty()) {
			updateEdges();
		}
		
			

	}
	
	private void updateVertices() {
		PickedState<Cclass> picks = vv.getPickedVertexState();
		
		if (picks == null || picks.getPicked() == null || picks.getPicked().isEmpty()) {
			return;
		}
		
		if (picks.getSelectedObjects().length == 1) {
			//only 1 thing selected
			Cclass c = (Cclass) picks.getSelectedObjects()[0];
			infoPanel.setTitle(c.getName());
			infoPanel.setPackageInfo(c.getPackageName());
			
			//gather some info first
			//does this class extend anything?
			ClassDeclaration decl = c.getDeclaration();
			if (decl == null) {
				return;
			}
			int extendCount = (decl.getExtends() == null ? 1 : 0);
			int implementCount = decl.getImplements().size();
			infoPanel.setStatInfo("<" + decl.getType().toString() + ">\n " +
			"Inherits from: " + (extendCount + implementCount) + (extendCount == 1 ? ", 1 of which it extends." : "") +
			"\nContains " + c.getMethods().size() + " methods\n"
					+ "");
			return;
		}
		
		String packageName = null;
		int methodCount = 0;
		
		for (Cclass c : picks.getPicked()) {
			if (packageName == null) {
				packageName = c.getPackageName();
			} else if (packageName.equals("") || !packageName.contains(".")) {
				//wait until end
			} else {
				//get most common element
				String newPack = "";
				String[] parts = packageName.split("\\."), otherParts = c.getPackageName().split("\\.");
				int i;
				for (i = 0; i < parts.length; i++) {
					if (i >= otherParts.length) {
						//too far!
						break;
					}
					if (parts[i].equals(otherParts[i])) {
						if (i > 0) {
							//not first matching part
							newPack += ".";
						}
						newPack += parts[i];
					} else {
						break;
					}
				}
				packageName = newPack;
			}
			
			methodCount += (c.getMethods() == null ? 0 : c.getMethods().size());
		}

		
		//after all package name processing
		//if it's "" we have no info
		if (packageName == null || packageName.equals("")) {
			packageName = "No Common Package!";
		}
		
		infoPanel.setPackageInfo(packageName);
		infoPanel.setTitle("Selection");
		infoPanel.setStatInfo("Selection contains:\n"
				+ methodCount + " methods");
	}
	
	
	
	
	
	
	
	private void updateEdges() {
		PickedState<WeightedPair<Cclass, Cclass>> picks = vv.getPickedEdgeState();
		
		if (picks == null || picks.getPicked() == null || picks.getPicked().isEmpty()) {
			return;
		}
		
		if (picks.getPicked().size() == 1) {
			//only 1 thing selected
			WeightedPair<Cclass, Cclass> edge = picks.getPicked().iterator().next();
			infoPanel.setTitle(edge.getLeft().getName() + " -> " + edge.getRight().getName());
			infoPanel.setPackageInfo("");
			
			//what information do we want from the edges?
			//TODO edge information
			//maybe how many method calls in both direction. How many references in either direction.
			infoPanel.setStatInfo(edge.getLeft().getName() + " holds " 
					+ edge.getLeft().getFormalReferenceCount(edge.getRight()) + " references to "
					+ edge.getRight().getName()
					+"\n"
					+ edge.getRight().getName() + " holds " 
					+ edge.getRight().getFormalReferenceCount(edge.getLeft()) + " references to "
					+ edge.getLeft().getName()					
					);
			return;
		}
		
//		String packageName = null;
//		int methodCount = 0;
//		
//		for (WeightedPair<Cclass, Cclass> pair : picks.getPicked()) {
//						
//			//methodCount += (c.getMethods() == null ? 0 : c.getMethods().size());
//		}
		
		infoPanel.setPackageInfo("");
		infoPanel.setTitle("Selection");
	}

}
