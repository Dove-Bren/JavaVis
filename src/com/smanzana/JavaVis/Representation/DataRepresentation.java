package com.smanzana.JavaVis.Representation;

import java.util.Set;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Util.Pair;

public abstract class DataRepresentation {
	
	public enum RepresentationType {
		extend,
		implement,
		referencedby,
		referenceto,
		reference,
		calledby,
		callto,
		call,
		all;
	}
	
	public abstract Set<Cclass> getClasses();
	
	public abstract Set<Pair<Cclass, Cclass>> getPairs();
	
}
