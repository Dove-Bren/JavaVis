package com.smanzana.JavaVis.Representation;

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
	
}
