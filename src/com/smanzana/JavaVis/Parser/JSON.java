package com.smanzana.JavaVis.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import com.smanzana.JavaVis.Representation.Graph.DirectedGraph;
import com.smanzana.JavaVis.Representation.Graph.DirectedGraphNode;
import com.smanzana.JavaVis.Representation.Graph.DirectedWeightedEdge;
import com.smanzana.JavaVis.Representation.Tree.Tree;

public final class JSON {
	
	private static boolean prepOutputFile(File outfile) {
		if (outfile == null || (outfile.exists() && outfile.isDirectory())) {
			System.err.println("Cannot save json to file, as it either is null or a directory!");
			return false;
		}
		
		if (outfile.exists()) {
			System.out.println("Overwriting " + outfile.getAbsolutePath());
			return true;
		} else {
			try {
				outfile.createNewFile();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}
	
	public static void toJSON(File outfile, Tree root) {
		if (!prepOutputFile(outfile)) {
			return;
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outfile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		printTree(writer, root);
		
		writer.flush();
		
		writer.close();
		
		
	}
	
	private static void printTree(PrintWriter writer, Tree tree) {
		if (tree == null) {
			return;
		}
		writer.print("{ ");
		writer.print("\"id\": \"" + tree.getName() + "\", ");
		writer.print("\"name\": \"" + tree.getName().substring(tree.getName().lastIndexOf(".") + 1) + "\", ");
		writer.print("\"data\": {\"package\": \"" + tree.getName().substring(0, tree.getName().lastIndexOf("."))     + "\"}, ");
		writer.print("\"children\": [");
//		
//		//go through all children and print them too
//		if (tree.getChildren() != null && !tree.getChildren().isEmpty()) {
//			for (Tree child : tree.getChildren()) {
//				printTree(writer, child);
//			}
//		}
		Iterator<Tree> childrenIt = tree.getChildren().iterator();
		while (childrenIt.hasNext()) {
			printTree(writer, childrenIt.next());
			if (childrenIt.hasNext()) {
				writer.print(" , ");
			}
		}
		
		writer.print("]");
		writer.print("}");
	}
	
	public static void toJSON(File outfile, DirectedGraph graph) {
		if (graph == null) {
			return;
		}

		if (!prepOutputFile(outfile)) {
			return;
		}

		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outfile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//no nifty recusion here D:
		writer.print("[ ");
		
		Iterator<DirectedGraphNode> nodeIt = graph.getNodes().iterator();
		DirectedGraphNode node;
		while (nodeIt.hasNext()) {
			node = nodeIt.next();
			writer.print("{ ");
			writer.print("\"id\": \"" +
						  node.getUniqueKey() + 
						  "\", ");
			writer.print("\"name\": \"" +
						  node.getName() + 
						  "\", ");
			writer.print("\"data\": { }, ");
			writer.print("\"adjacencies\": [");
			
			//go over all edges
			Iterator<DirectedWeightedEdge> edgeIt = node.getEdges().iterator();
			DirectedWeightedEdge edge;
			while (edgeIt.hasNext()) {
				edge = edgeIt.next();
				writer.print("{ ");
				writer.print("\"nodeTo\": \"" +
							  edge.getDestination().getUniqueKey() +
							  "\", ");
				writer.print("\"data\": {" +
							  "\"weight\": \"" +
							  edge.getWeight() +
							  "\" }");
				
				writer.print(" }");
				if (edgeIt.hasNext()) {
					writer.print(", ");
				}
			}
			
			
			writer.print("]");
			
			
			
			writer.print("}");
			
			if (nodeIt.hasNext()) {
				writer.print(", ");
			}
		}
		
		
		writer.print(" ]");
		
		writer.flush();
		
		writer.close();
		
		
	}
	
}
