package com.smanzana.JavaVis.Representation;

import java.util.Set;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Util.Pair;

public abstract class DataRepresentation {
	
	public enum RepresentationType {
		extend("Direct Inheritence"),
		implement("Interface Inheritence"),
		referencedby("Referenced By"),
		referenceto("References To"),
		reference("Reference"),
		calledby("Called By"),
		callto("Calls On"),
		call("Calls"),
		all("All");
		
		private String title;
		
		private RepresentationType(String e) {
			title = e;
		}
		
		public String getTitle() {
			return title;
		}
	}
	
	public abstract Set<Cclass> getClasses();
	
	public abstract Set<Pair<Cclass, Cclass>> getPairs();
	
}
