package com.smanzana.JavaVis;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.smanzana.JavaVis.Parser.ClassDeclaration;
import com.smanzana.JavaVis.Parser.FileParser;
import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Parser.Wrappers.Import;
import com.smanzana.JavaVis.Representation.DataRepresentation.RepresentationType;
import com.smanzana.JavaVis.Representation.Graph.DirectedGraph;
import com.smanzana.JavaVis.Representation.Graph.DirectedGraphNode;
import com.smanzana.JavaVis.Representation.Tree.Tree;
import com.smanzana.JavaVis.Visualization.JungVisualization;

/**
 * This class is the driver for the visualization. 
 * @author Skyler
 *
 */
public final class Driver {

	public static FileParser fileP;
	
	public static Set<Cclass> classes;
	
	
	public static void main(String[] args) {
		
		
		if (args.length == 0) {
			//printUsage();
			JungVisualization jvis = new JungVisualization();
			DirectedGraph graph = new DirectedGraph(); //TODO do undirected not work with getedges?!
			DirectedGraphNode a, b, c;
			a = new DirectedGraphNode(new Cclass("a", "com.package.name"));
			b = new DirectedGraphNode(new Cclass("b", "com.package.name"));
			c = new DirectedGraphNode(new Cclass("c", "com.package.name.subpackage"));
			
			a.addEdge(b);
			b.addEdge(c);
			
			graph.addNode(a);
			graph.addNode(b);
			graph.addNode(c);
			
			jvis.provideRepresentation(RepresentationType.extend, graph);
			
			jvis.Visualize();
			
			return;
		}
		
		File file = new File(args[0]);
		
		if (!file.exists()) {
			System.out.println("Unable to find the passed file: " + file.getAbsolutePath());
			return;
		}
		fileP = new FileParser();
		classes = new TreeSet<Cclass>();
	
		processFile(file);
		
		if (classes.isEmpty()) {
			System.out.println("Unable to locate any .java files!");
			return;
		}
		
		System.out.println("Got a total of: " + classes.size() + " classes!");
		
//		for (Cclass c : classes) {
//			System.out.println(c.info());
//		}
		
		//Tree extendTree = classesAsExtendsTree(classes);
		
		//System.out.println(extendTree);
		
		DirectedGraph implementGraph = classesAsImplementsGraph(classes);
		Tree<Cclass> extendTree = classesAsExtendsTree(classes);
		//TODO TEST
		
		DirectedGraph referenceGraph = classesAsReferenceGraph(classes);
		
		DirectedGraph allGraph = classesAsImplementsGraph(classes);
		allGraph.mergeFrom(extendTree);
		allGraph.mergeFrom(referenceGraph);
		
		//WebVisualization webViz = new WebVisualization();
		//webViz.Visualize(VisualizationType.IMPLEMENTS, implementGraph);
		
		JungVisualization jvis = new JungVisualization();
		jvis.provideRepresentation(RepresentationType.implement, implementGraph);
		jvis.provideRepresentation(RepresentationType.extend, extendTree);
		jvis.provideRepresentation(RepresentationType.reference, referenceGraph);
		jvis.provideRepresentation(RepresentationType.all, allGraph);
		jvis.provideRepresentation(RepresentationType.custom, null);
		
		jvis.Visualize();
	}
	
	
	
	@SuppressWarnings("unused")
	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("java -jar [jar name] [dir/file]");
	}
	
	private static void processFile(File file) {
		if (file == null || !file.exists()) {
			System.err.println("Invalid file selection in Driver class: " + file);
			return;
		}
		
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				processFile(f);
			}
		} else if (file.getName().endsWith(".java")) {
			//we have an actual file. Process is
			try {
				fileP.setFile(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}
	
			Cclass cl = fileP.getcClass();
			if (cl == null) {
				System.out.println("Extreme error! Got error class!");
			} else {
				classes.add(cl);
			}

			
		}
	}
	
	private static DirectedGraph classesAsImplementsGraph(Set<Cclass> classes) {
		
		if (classes == null || classes.isEmpty()) {
			return null;
		}
		
		//also a naive approach, add all classes as nodes and then do edges
		Map<Cclass, DirectedGraphNode> nodeMap = new TreeMap<Cclass, DirectedGraphNode>();
		DirectedGraph graph = new DirectedGraph();
		
		for (Cclass c : classes) {
			//go through and create and add each class/vertex to the graph
			DirectedGraphNode n = new DirectedGraphNode(c);
			graph.addNode(n);
			nodeMap.put(c, n);
		}
		
		
		
		
		//now, parse through and add edges. This is an implements graph, so add edge FROM the class
		//that is implemented TO the current class
		for (Cclass c : classes) {
			//each class has an array of implementations 
			if (c.getDeclaration() == null || c.getDeclaration().getImplements() == null || c.getDeclaration().getImplements().isEmpty()) {
				continue;
			}
			
			//convenience
			Set<String> impleSet = c.getDeclaration().getImplements();
			for (String impl : impleSet) {
				//for each, add self as edge to
				{
					//has a parent
					Import im = c.getImport(impl);
					Cclass parent;
					if (im != null) {
						//we found an import that matched! e.g. we have it's package name!
						parent = getClass(nodeMap.keySet(), im.getPackageName() + "." + impl);
						
						//if parent == null for any of these, it means we haven't created a tree for the class
						//yet. This probably means we don't have the source code for that class, and need
						//to make up an imaginary class
						if (parent == null) {
							parent = new Cclass(impl, im.getPackageName());
						}
					} else {
						//no matching import, meaning it has the same package
						//OR it means there was a generic import: java.util.*
						//which we can't recover from :(
						
						//our last shot is that the whole package was specific inline. We test for this
						//by seeing if there exists a . in the extend line :(
						if (impl.contains(".")) {
							//whole thing is there already!
							parent = getClass(nodeMap.keySet(), impl);
							
							//if parent is null, create ghost class
							//extends org.smanzana.imanorg.BestClass
							if (parent == null) {
								String pack, parName;
								pack = impl.substring(0, impl.lastIndexOf("."));
								parName = impl.substring(impl.lastIndexOf(".") + 1);
								parent = new Cclass(parName, pack);
							}
						} else {
							//assume it's the same package as this class
							parent = getClass(nodeMap.keySet(), c.getPackageName() + "." + impl);
							
							if (parent == null) {
								parent = new Cclass(impl, c.getPackageName());
							}
						}
						
					}
					
					//finally got a parent. It's either a ghost or a flesh class hehe
					
					//check to see if it's in our map. If not it's a ghost and needs a tree created for it
					if (!nodeMap.containsKey(parent)) {
						nodeMap.put(parent, new DirectedGraphNode(parent));
					}
					
					nodeMap.get(parent).addEdge(nodeMap.get(c));
				}
			}
			
			
		}
		
		return graph;
		
	}
	
	private static Tree<Cclass> classesAsExtendsTree(Set<Cclass> classes) {
		
		if (classes == null || classes.isEmpty()) {
			return null;
		}
		
		//naive approach - not optimized
		Map<Cclass, Tree<Cclass>> treeMap = new TreeMap<Cclass, Tree<Cclass>>();
		
		for (Cclass c : classes) {
			//go through and create a tree for each class
			treeMap.put(c, new Tree<Cclass>(c));
		}
		

		ClassDeclaration decl;		
		//have to do this after all trees have been created
		for (Cclass c : classes) {
			decl = c.getDeclaration();
			if (decl == null) {
				continue;
			}
			if (decl.getExtends() != null ) {
				//has a parent
				Import im = c.getImport(decl.getExtends());
				Cclass parent;
				if (im != null) {
					//we found an import that matched! e.g. we have it's package name!
					parent = getClass(treeMap.keySet(), im.getPackageName() + "." + decl.getExtends());
					
					//if parent == null for any of these, it means we haven't created a tree for the class
					//yet. This probably means we don't have the source code for that class, and need
					//to make up an imaginary class
					if (parent == null) {
						parent = new Cclass(decl.getExtends(), im.getPackageName());
						System.out.println("DEBUG: new ghost class: " + parent.getName());
					}
				} else {
					//no matching import, meaning it has the same package
					//OR it means there was a generic import: java.util.*
					//which we can't recover from :(
					
					//our last shot is that the whole package was specific inline. We test for this
					//by seeing if there exists a . in the extend line :(
					if (decl.getExtends().contains(".")) {
						//whole thing is there already!
						parent = getClass(treeMap.keySet(), decl.getExtends());
						
						//if parent is null, create ghost class
						//extends org.smanzana.imanorg.BestClass
						if (parent == null) {
							String pack, parName;
							pack = decl.getExtends().substring(0, decl.getExtends().lastIndexOf("."));
							parName = decl.getExtends().substring(decl.getExtends().lastIndexOf(".") + 1);
							parent = new Cclass(parName, pack);
						}
					} else {
						//assume it's the same package as this class
						parent = getClass(treeMap.keySet(), c.getPackageName() + "." + decl.getExtends());
						
						if (parent == null) {
							parent = new Cclass(decl.getExtends(), c.getPackageName());
						}
					}
					
				}
				
				//finally got a parent. It's either a ghost or a flesh class hehe
				
				//check to see if it's in our map. If not it's a ghost and needs a tree created for it
				if (!treeMap.containsKey(parent)) {
					treeMap.put(parent, new Tree<Cclass>(parent));
				}
				
				treeMap.get(c).setParent(treeMap.get(parent));
			}
			
			
		}
		
		//add a ghost class for the Object class
		Cclass ob = new Cclass("Object", "java.lang");
		Tree<Cclass> obTree = new Tree<Cclass>(ob);
		
		//finally go and link all classes that don't have a parent to OBJECT, our root
		for (Tree<Cclass> tree : treeMap.values()) {
			if (tree.getParent() == null) {
				tree.setParent(obTree);
			}
		}
		
		
		return obTree;
	}
	
	private static DirectedGraph classesAsReferenceGraph(Set<Cclass> classes) {
		
		if (classes == null || classes.isEmpty()) {
			return null;
		}
		
		//also a naive approach, add all classes as nodes and then do edges
		Map<Cclass, DirectedGraphNode> nodeMap = new TreeMap<Cclass, DirectedGraphNode>();
		DirectedGraph graph = new DirectedGraph();
		
		for (Cclass c : classes) {
			//go through and create and add each class/vertex to the graph
			DirectedGraphNode n = new DirectedGraphNode(c);
			graph.addNode(n);
			if (!nodeMap.containsKey(c)) {
				System.out.println("Adding new class: " + c.getName());
				nodeMap.put(c, n);
			}
			
			for (Cclass refClass : c.getFormalReferenceMap().keySet()) {
				DirectedGraphNode rN;
				rN = null;
				for (Cclass cet : nodeMap.keySet()) {
					if (cet.equals(refClass)) {
						rN = nodeMap.get(cet);
						break;
					}
				}
				if (rN == null) {
					rN = new DirectedGraphNode(refClass);
					System.out.println("Adding new class as extension: " + refClass.getName());
					nodeMap.put(refClass, rN);
				}
				n.addEdge(rN, c.getFormalReferenceCount(refClass));
			}
		}
		
		
		return graph;
	}
	
	/**
	 * helper to a helper method, this method looks in the passed set and returns the Cclass object
	 * that matches the passed object (string in the case of a Cclass)<br />
	 * For more info, see {@link com.smanzana.JavaVis.Parser.Wrappers.Cclass Cclass}.
	 * {@link com.smanzana.JavaVis.Parser.Wrappers.Cclass#equals(Object) equals(Object)} 
	 * @param classes
	 * @return
	 */
	private static Cclass getClass(Set<Cclass> classes, Object o) {
		for (Cclass c : classes) {
			if (c.equals(o)) {
				return c;
			}
		}
		
		return null;
	}


}
