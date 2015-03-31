package com.smanzana.JavaVis.Parser.Wrappers;

public class Import {
	
	private String importSpec;
	
	private String packageName;

	
	public Import(String importLine) {
		
		importLine = importLine.trim();
		importLine = importLine.substring(7); //'import ' is 7 chars
		if (importLine.isEmpty()) {
			importSpec = null;
			packageName = null;
		}
		//#import org.blah.bluh.*; or org.blah.bluh.class
		int finalIndex = importLine.lastIndexOf(".");
		
		packageName = importLine.substring(0, finalIndex);
		importSpec = importLine.substring(finalIndex + 1);
		importSpec = importSpec.substring(0, importSpec.length() -1);
	}
	
	@Override
	public String toString() {
		return "[Package Name: " + this.packageName + "] [spec: " + this.importSpec + "]";
	}
	
	
	/**
	 * @return the importSpec
	 */
	public String getImportSpec() {
		return importSpec;
	}

	/**
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}
	
	
	
	
	
	
}
