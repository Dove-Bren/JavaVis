package com.smanzana.JavaVis.Parser;

import java.util.HashSet;
import java.util.Set;

import com.smanzana.JavaVis.Parser.Wrappers.Cclass;
import com.smanzana.JavaVis.Parser.Wrappers.Cclass.Type;


/**
 * Represent a class declaration.<br />
 * make this pretty later:<br />
 * public class [name] {extends class} {implements class, class}
 * @author Skyler
 * 
 */
public class ClassDeclaration {
	
	
	private String declarationLine;
	
	private String name;
	
	private String extendString;
	
	private Set<String> implementStrings;
	
	private Type type;
	
	public ClassDeclaration(String declaration) {
		this.declarationLine = declaration.trim();
		
		if (declarationLine.isEmpty()) {
			return;
		}
		
//		if (declarationLine.endsWith("{")) {
//			declarationLine = declarationLine.substring(0, declarationLine.length() - 2);
//		}
		
		String[] tokens = declarationLine.split(" ");
		int i = 0;
		
		name = null;
		extendString = null;
		implementStrings = new HashSet<String>();
		
		while (i < tokens.length) {
			//ignore all modifiers before class keyword
			if (isModifier(tokens[i])) {
				i++;
				continue;
			}
			
			//tokens[i] is not a modifier. Hopefully it's class!
			if (tokens[i].equals("class") || tokens[i].equals("interface") || tokens[i].equals("@interface") || tokens[i].equals("enum")) {
				this.name = tokens[i + 1];
				if (name.contains("<")) {
					this.name += tokens[i + 2].substring(0, tokens[i+2].indexOf(">") + 1);
							
				}
				if (tokens[i].equalsIgnoreCase("class")) {
					type = Cclass.Type.CLASS;
				} else if (tokens[i].equalsIgnoreCase("interface") || tokens[i].equalsIgnoreCase("@interface")) {
					type = Cclass.Type.INTERFACE;
				} else {
					type = Cclass.Type.ENUM;
				}
			}
			else {
				System.out.println("Failure of assumption [class]: " + tokens[i]);
				return;
			}
			
			i = i + 2;
			break;
		}
		
		if (name == null) {
			//no name was found, exhausted file
			System.out.println("Unable to find class name in declaration!");
			return;
		}
		
		//next is extends or implements. Either can be first
		while (i < tokens.length) {
			if (tokens[i].equals("extends")) {
				//next token is what it extends!
				this.extendString =tokens[i + 1].trim();
				
				//check if parameterization is in next string
				if (extendString.contains("<") && tokens.length >= i+3) {
					if (tokens[i + 2].contains(">")) {
						extendString = extendString + tokens[i + 2];
					}
				}
				
//				//check and make sure it doesn't include trailing {
//				if (this.extendString.endsWith("{")) {
//					extendString = extendString.substring(0, this.extendString.length() - 2);
//				}
				
				i = i+2;
				continue;
			}
			
			if (tokens[i].equals("implements")) {
				//next token is a class. If that token has a comma, expect another class
				do {
					i++;
					implementStrings.add(tokens[i].replace("," , " ").trim());
				} while (tokens[i].contains(","));
			}
			i++;
		}
		
		//name cleanup
		if (extendString == null) {
				if (name.endsWith(";")) {
					name = name.substring(0, name.length() -1).trim();
				}
				if (name.endsWith("{")) {
					name = name.substring(0, name.length() -1).trim();
					System.out.println("{ -> " + name);
				}
		} else {
				if (extendString.endsWith(";")) {
					extendString = extendString.substring(0, extendString.length() -1).trim();
				}
				if (extendString.endsWith("{")) {
					extendString = extendString.substring(0, extendString.length() -1).trim();
					System.out.println("{ -> " + extendString);
				}
		}
				
		System.out.println(name + " | " + extendString);
	}
	
	public static boolean isModifier(String str) {
		switch (str) {
		case "public":
		case "private":
		case "protected":
		case "abstract":
		case "final":
		case "static":
		case "volatile":
			return true;
		default:
			return false;
		}
	}
	
	public String getClassName() {
		return name;
	}
	
	public String getExtends() {
		return extendString;
	}
	
	public Set<String> getImplements() {
		return implementStrings;
	}
	
	public Type getType() {
		return type;
	}
}
