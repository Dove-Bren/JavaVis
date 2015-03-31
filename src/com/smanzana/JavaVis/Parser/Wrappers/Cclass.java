package com.smanzana.JavaVis.Parser.Wrappers;

import java.util.LinkedList;
import java.util.List;

import com.smanzana.JavaVis.Parser.ClassDeclaration;

/**
 * Represents a class lulz 
 * @author Skyler
 *
 */
public class Cclass {
	
	
	private String name;
	
	private String packageName;
	
	private List<Method> methods;
	
	private List<Import> imports;
	
	private ClassDeclaration declaration;
	
	public Cclass(String name, ClassDeclaration decl, String packageName, List<Method> methods) {
		
		this.declaration = decl;
		this.name = name;
		this.packageName = packageName;
		
		this.methods = methods;
		
		if (methods == null) {
			methods = new LinkedList<Method>();
		}
		
		imports = new LinkedList<Import>();
				
	}
	
	public Cclass (ClassDeclaration decl, String packageName, List<Method> methods) {
		this(decl.getClassName(), decl, packageName, methods);
	}
	
	public Cclass(String name, String packageName, List<Method> methods) {
		this(name, null, packageName, methods);
	}
	
	public Cclass(String name, String packageName) {
		this(name, null, packageName, null);
	}
	
	public Cclass(String name) {
		this(name, null, null, null);
	}
	
	public Cclass(ClassDeclaration decl) {
		this(decl.getClassName(), decl, null, null);
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
			
		}
		return null;
	}
	
	
	
	/**
	 * Prints out detailed information about the class.<br />
	 * In many ways, this is toString++
	 * @return
	 */
	public String info() {

		String out =  "[" + this.packageName + "." + this.name +"]:\n";
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
		return toString().equals(o.toString());
	}
	
}
