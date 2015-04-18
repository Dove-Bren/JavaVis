package com.smanzana.JavaVis.Representation.Graph;

import java.util.Set;

import com.smanzana.JavaVis.Representation.DataRepresentation;


/**
 * Basic graph data structure.
 * @author Skyler
 *
 */
public abstract class Graph extends DataRepresentation {
//	
//	private Set<GraphNode> nodes;
//	
//	private int depth;
//	
//	/**
//	 * This represents how WIDE the tree is. 
//	 */
//	private int hDepth;
//	
//	
//	public Graph() {
//		this.parent = null;
//		this.children = new HashSet<Graph>();
//		this.depth = 0;
//		this.hDepth = 0;
//	}
//
//
//	/**
//	 * @return the parent
//	 */
//	public Graph getParent() {
//		return parent;
//	}
//
//
//	/**
//	 * @param parent the parent to set
//	 */
//	public void setParent(Graph parent) {
//		this.parent = parent;
//	}
//
//
//	/**
//	 * @return the children
//	 */
//	public Set<Graph> getChildren() {
//		return children;
//	}
//
//
//	/**
//	 * @param children the children to set
//	 */
//	public void setChildren(Set<Graph> children) {
//		this.children = children;
//	}
//
//
//	/**
//	 * @return the depth
//	 */
//	public int getDepth() {
//		return depth;
//	}
//
//
//	/**
//	 * @return the hDepth
//	 */
//	public int gethDepth() {
//		return hDepth;
//	}
//		
//	
//	
//	
//	public void addChild(Graph child) {
//		
//		if (child == null) {
//			System.out.println("Null tree being added...");
//			return;
//		}
//		
//		if (children.add(child)) {
//			//update depth to new largest depth
//			depth = Math.max(depth, child.getDepth() + 1);
//			
//			//update hDepth
//			//just tack on the new hDepth to current, as we'll have one class per vert. line
//			hDepth = hDepth + child.gethDepth();
//			
//			child.setParent(this);
//		}
//	}
//	
//	public void removeChild(Graph child) {
//		
//		if (child == null) {
//			System.out.println("Attempt to remove a null child...");
//			return;
//		}
//		
//		if (children.remove(child)) {
//			//get new depth :/
//			
//			depth = 0;
//			if (!children.isEmpty()) {
//				depth = 0;
//				Iterator<Graph> it = children.iterator();
//				int tempDepth;
//				while (it.hasNext()) {
//					tempDepth = it.next().getDepth();
//					if (tempDepth > depth) {
//						depth = tempDepth;
//					}
//				}
//			}
//			
//			//now hdepth
//			hDepth -= child.hDepth;
//			//TODO if hDepth changes, change here
//			
//			//finally remove link as parent
//			child.setParent(null);
//		}
//	}
//	
	
	public abstract Set<UndirectedWeightedEdge> getEdges();
	
	public abstract Set<GraphNode> getNodes();
	
}
