package com.smanzana.JavaVis.Parser.Wrappers;

public class Method {
	
	private String name;
	
	private String[] codeLines;
	
	public Method(String name, String[] codeLines) {
		this.name = name;
		this.codeLines = codeLines;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the codeLines
	 */
	public String[] getCodeLines() {
		return codeLines;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
}
