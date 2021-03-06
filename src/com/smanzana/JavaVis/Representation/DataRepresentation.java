package com.smanzana.JavaVis.Representation;

import java.util.Set;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Util.Pair;
import com.smanzana.JavaVis.Util.WeightedPair;

public abstract class DataRepresentation {
	
	public enum RepresentationType {
		extend("Direct Inheritence"),
		implement("Interface Inheritence"),
		reference("Reference"),
		all("All"),
		custom("Custom");
		
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
	
	public abstract Set<WeightedPair<Cclass, Cclass>> getWeightedPairs();
	
}
