package com.smanzana.JavaVis.Representation.Tree;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Representation.DataRepresentation;
import com.smanzana.JavaVis.Util.Pair;
import com.smanzana.JavaVis.Util.WeightedPair;

/**
 * Basic tree data structure with an undetermined number of children
 * Designed to be recursively defined
 * @author Skyler
 *
 */
public class Tree<T> extends DataRepresentation {
	
	private Tree<T> parent;
	
	private Set<Tree<T>> children;
	
	private int depth;
	
	/**
	 * This represents how WIDE the tree is. 
	 */
	private int hDepth;
	
	private String name = "EMPTY NAME FOR TREE LULZ";
	
	private T node;
	
	
	public Tree(T node) {
		this.parent = null;
		this.children = new HashSet<Tree<T>>();
		this.depth = 0;
		this.hDepth = 0;
		this.node = node;
		//this.name = cclass.getName();
	}


	/**
	 * @return the parent
	 */
	public Tree<T> getParent() {
		return parent;
	}


	/**
	 * @param parent the parent to set
	 */
	public void setParent(Tree<T> parent) {
		this.parent = parent;
		parent.addChild(this);
	}


	/**
	 * @return the children
	 */
	public Set<Tree<T>> getChildren() {
		return children;
	}


	/**
	 * @param children the children to set
	 */
	public void setChildren(Set<Tree<T>> children) {
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
		
	
	
	
	public void addChild(Tree<T> child) {
		
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
	
	public void removeChild(Tree<T> child) {
		
		if (child == null) {
			System.out.println("Attempt to remove a null child...");
			return;
		}
		
		if (children.remove(child)) {
			//get new depth :/
			
			depth = 0;
			if (!children.isEmpty()) {
				depth = 0;
				Iterator<Tree<T>> it = children.iterator();
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
			for (Tree<T> t : children) {
				out += "  " + t.getName() + "\n";
			}
		}
		
		return out;
	}
	
	public T getCclass() {
		return node;
	}
	
	public Set<T> getNodes() {
		Set<T> c = new HashSet<T>();
		
		c.add(node);
		
		if (children.isEmpty()) {
			return c;
		}
		
		for (Tree<T> child : children) {
			c.addAll(child.getNodes());
		}
		
		return c;
	}
	
	/**
	 * EXTREMELY UNSAFE OMG
	 */
	@Override
	public Set<Cclass> getClasses() {
		Set<Cclass> c = new HashSet<Cclass>();
		
		c.add((Cclass) node);
		
		if (children.isEmpty()) {
			return c;
		}
		
		for (Tree<T> child : children) {
			c.addAll(child.getClasses());
		}
		
		return c;
	}
	
	@Override
	public Set<Pair<Cclass, Cclass>> getPairs() {
		Set<Pair<Cclass, Cclass>> pairs = new HashSet<Pair<Cclass, Cclass>>();
		
		if (children.isEmpty()) {
			return pairs;
		}
		
		for (Tree<T> child : children) {
			pairs.add(new Pair<Cclass, Cclass>((Cclass) node, (Cclass) child.node));
			pairs.addAll(child.getPairs());
		}
		
		return pairs;
	}
	
	@Override
	public Set<WeightedPair<Cclass, Cclass>> getWeightedPairs() {

		Set<WeightedPair<Cclass, Cclass>> pairs = new HashSet<WeightedPair<Cclass, Cclass>>();
		
		if (children.isEmpty()) {
			return pairs;
		}
		
		for (Tree<T> child : children) {
			pairs.add(new WeightedPair<Cclass, Cclass>((Cclass) node, (Cclass) child.node, 1.0));
			pairs.addAll(child.getWeightedPairs());
		}
		
		return pairs;
	}
	
}
