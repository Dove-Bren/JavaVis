package com.smanzana.JavaVis.Parser.Wrappers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.smanzana.JavaVis.Parser.ClassDeclaration;
import com.smanzana.JavaVis.Parser.JavaLangDictionary;

/**
 * Represents a class lulz 
 * @author Skyler
 *
 */
public class Cclass implements Comparable<Cclass> {
	
	public int compareTo(Cclass o1) {

		if (this.equals(o1)) {
			return 0;
		}
			
		return ((packageName + "." + name).compareTo(o1.packageName + "." + o1.name));
	}
	
	public enum Type {
		CLASS,
		INTERFACE,
		ENUM;
	}
	
	
	private java.lang.String name;
	
	private Type type;
	
	private String packageName;
	
	private List<Method> methods;
	
	private List<Import> imports;
	
	private ClassDeclaration declaration;
	
	private Map<Cclass, Integer> formalReferenceMap;
	
	public Cclass(String name, Type type, ClassDeclaration decl, String packageName, List<Method> methods) {
		
		this.declaration = decl;
		this.type = type;
		this.name = name.trim();
		this.packageName = packageName;
		
		this.methods = methods;
		
		if (methods == null) {
			methods = new LinkedList<Method>();
		}
		
		imports = new LinkedList<Import>();
		formalReferenceMap = new HashMap<Cclass, Integer>();
		
		//name cleanup
		if (name.endsWith(";")) {
			name = name.substring(0, name.length() -1).trim();
		}
		if (name.endsWith("{")) {
			name = name.substring(0, name.length() -1).trim();
		}
				
	}
	
	public Cclass (ClassDeclaration decl, String packageName, List<Method> methods) {
		this(decl.getClassName(), decl.getType(), decl, packageName, methods);
	}
	
	public Cclass(String name, String packageName, List<Method> methods) {
		this(name, Type.CLASS, null, packageName, methods);
	}
	
	public Cclass(String name, String packageName) {
		this(name, Type.CLASS, null, packageName, null);
	}
	
	public Cclass(String name) {
		this(name, Type.CLASS, null, null, null);
	}
	
	public Cclass(ClassDeclaration decl) {
		this(decl.getClassName(), decl.getType(), decl, null, null);
	}
	
	
	public void setDeclaration(ClassDeclaration decl) {
		this.declaration = decl;
	}
	
	public ClassDeclaration getDeclaration() {
		return this.declaration;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPackageName() {
		return this.packageName;
	}
	
	public List<Method> getMethods() {
		return this.methods;
	}
	
	public void addMethod(Method meth) {
		//not even once, kids
		methods.add(meth);
	}
	
	public void setFormalReferences(Map<Cclass, Integer> refs) {
		this.formalReferenceMap = refs;
	}
	
	/**
	 * increment count of references from the current class to the passed class
	 */
	public void addFormalReference(Cclass cclass) {
		if (formalReferenceMap.containsKey(cclass)) {
			formalReferenceMap.put(cclass, formalReferenceMap.get(cclass) + 1);
		} else {
			formalReferenceMap.put(cclass, 1);
		}
	}
	
	public int getFormalReferenceCount(Cclass cclass) {
		
		for (Cclass cs : formalReferenceMap.keySet()) {
			if (cs.equals(cclass)) {
				return formalReferenceMap.get(cs);
			}
		} 
		
		return 0;
	}
	
	public Map<Cclass, Integer> getFormalReferenceMap() {
		return formalReferenceMap;
	}
	
	
	
	/**
	 * @return the imports
	 */
	public List<Import> getImports() {
		return imports;
	}

	/**
	 * @param imports the imports to set
	 */
	public void setImports(List<Import> imports) {
		this.imports = imports;
	}

	/**
	 * Uses the passed class name (one that is not prefixed by the package) to return the
	 * import statement used to import the class's reference
	 * @param localName
	 * @return
	 */
	public Import getImport(String localName) {
		for (Import im : imports) {
			//look at import statements for the passed spec.
			if (im.getImportSpec().equals(localName)) {
				return im;
			}
			//we also need to be wary of import java.util.*'s
			//TODO figure out if there's any way to do this... darn it!
			if (JavaLangDictionary.isJavaLang(localName)) {
				return new Import("import java.lang." + localName);
			}
			
		}
		return null;
	}
	
	public Type getType() {
		return type;
	}
	
	
	
	/**
	 * Prints out detailed information about the class.<br />
	 * In many ways, this is toString++
	 * @return
	 */
	public String info() {

		String out =  type.name() + " [" + this.packageName + "." + this.name +"]:\n";
		if (!imports.isEmpty()) {
			out += "Import statements: \n";
			for (Import im : imports) {
				out += " -" + im + "\n";
			}
		}
		if (declaration != null && declaration.getExtends() != null) {
			out += "Extends: " + declaration.getExtends() + "\n";
		}
		if (declaration != null && declaration.getImplements() != null && !declaration.getImplements().isEmpty()) {
			out += "Implements: " + declaration.getImplements() + "\n";
		}
		for (Method m : methods) {
			out += "  " + m.toString() + "\n";
		}
		
		
		return out;
	}
	
	@Override
	public String toString() {
		return this.packageName + "." + this.name;
	}
	
	@Override
	public boolean equals(Object o) {
//		if (toString().startsWith("java.lang")) {
//			System.out.println("Compare [" + toString() + "] " + (toString().equals(o.toString()) ? "= " : "fails ") + "[" + o.toString() + "]");
//		}
		return toString().equals(o.toString());
	}
	
}
