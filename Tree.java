package com.smanzana.Exploratory2.Tree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Basic tree data structure with an undetermined number of children
 * Designed to be recursively defined
 * @author Skyler
 *
 */
public class Tree {
	
	private Tree parent;
	
	private Set<Tree> children;
	
	private int depth;
	
	/**
	 * This represents how WIDE the tree is. 
	 */
	private int hDepth;
	
	
	public Tree() {
		this.parent = null;
		this.children = new HashSet<Tree>();
		this.depth = 0;
		this.hDepth = 0;
	}


	/**
	 * @return the parent
	 */
	public Tree getParent() {
		return parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setParent(Tree parent) {
		this.parent = parent;
	}


	/**
	 * @return the children
	 */
	public Set<Tree> getChildren() {
		return children;
	}


	/**
	 * @param children the children to set
	 */
	public void setChildren(Set<Tree> children) {
		this.children = children;
	}


	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}


	/**
	 * @return the hDepth
	 */
	public int gethDepth() {
		return hDepth;
	}
		
	
	
	
	public void addChild(Tree child) {
		
		if (child == null) {
			System.out.println("Null tree being added...");
			return;
		}
		
		if (children.add(child)) {
			//update depth to new largest depth
			depth = Math.max(depth, child.getDepth() + 1);
			
			//update hDepth
			//just tack on the new hDepth to current, as we'll have one class per vert. line
			hDepth = hDepth + child.gethDepth();
			
			child.setParent(this);
		}
	}
	
	public void removeChild(Tree child) {
		
		if (child == null) {
			System.out.println("Attempt to remove a null child...");
			return;
		}
		
		if (children.remove(child)) {
			//get new depth :/
			
			depth = 0;
			if (!children.isEmpty()) {
				depth = 0;
				Iterator<Tree> it = children.iterator();
				int tempDepth;
				while (it.hasNext()) {
					tempDepth = it.next().getDepth();
					if (tempDepth > depth) {
						depth = tempDepth;
					}
				}
			}
			
			//now hdepth
			hDepth -= child.hDepth;
			//TODO if hDepth changes, change here
			
			//finally remove link as parent
			child.setParent(null);
		}
	}
	
	
}
