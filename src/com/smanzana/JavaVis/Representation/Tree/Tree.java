package com.smanzana.JavaVis.Representation.Tree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.smanzana.JavaVis.Representation.DataRepresentation;

/**
 * Basic tree data structure with an undetermined number of children
 * Designed to be recursively defined
 * @author Skyler
 *
 */
public class Tree implements DataRepresentation {
	
	private Tree parent;
	
	private Set<Tree> children;
	
	private int depth;
	
	/**
	 * This represents how WIDE the tree is. 
	 */
	private int hDepth;
	
	private String name;
	
	
	public Tree(String name) {
		this.parent = null;
		this.children = new HashSet<Tree>();
		this.depth = 0;
		this.hDepth = 0;
		this.name = name;
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
		parent.addChild(this);
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
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
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
			hDepth = hDepth + Math.max(child.gethDepth(), 1);
			
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
	
	
	@Override
	public String toString() {
		String out =  "Name: " + name + 
					  "\nDepth: " + depth + "  hDepth: " + hDepth + "\n";
		if (parent != null) {
			out += "Parent name: " + parent.getName() + "\n";
		}
		if (!children.isEmpty()) {
			out += "Children:\n";
			for (Tree t : children) {
				out += "  " + t.getName() + "\n";
			}
		}
		
		return out;
	}
	
}
