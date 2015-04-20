package com.smanzana.JavaVis.Parser;

import java.util.HashSet;
import java.util.Set;

public final class JavaLangDictionary {
	
	public static Set<String> langDictionary;
	
	private static void init()
	{
		langDictionary = new HashSet<String>(); 
		langDictionary.add("Appendable");//96
		langDictionary.add("AutoCloseable");
		langDictionary.add("CharSequence");
		langDictionary.add("Cloneable");
		langDictionary.add("Comparable");
		langDictionary.add("Iterable");
		langDictionary.add("Readable");
		langDictionary.add("Runnable");
		langDictionary.add("Boolean");
		langDictionary.add("Byte");
		langDictionary.add("Character");
		langDictionary.add("Class");
		langDictionary.add("ClassLoader");
		langDictionary.add("ClassValue");
		langDictionary.add("Compiler");
		langDictionary.add("Double");
		langDictionary.add("Enum");
		langDictionary.add("Float");
		langDictionary.add("InheritableThreadLocal");
		langDictionary.add("Integer");
		langDictionary.add("Long");
		langDictionary.add("Math");
		langDictionary.add("Number");
		langDictionary.add("Object");
		langDictionary.add("Package");
		langDictionary.add("Process");
		langDictionary.add("ProcessBuilder");
		langDictionary.add("Runtime");
		langDictionary.add("RuntimePermission");
		langDictionary.add("SecurityManager");
		langDictionary.add("Short");
		langDictionary.add("StackTraceElement");
		langDictionary.add("StrictMath");
		langDictionary.add("String");
		langDictionary.add("StringBuffer");
		langDictionary.add("StringBuilder");
		langDictionary.add("System");
		langDictionary.add("Thread");
		langDictionary.add("ThreadGroup");
		langDictionary.add("ThreadLocal");
		langDictionary.add("Throwable");
		langDictionary.add("Void");
		langDictionary.add("int");
		langDictionary.add("double");
		langDictionary.add("float");
		langDictionary.add("char");
	}
	
	/**
	 * Checks whether the passed string is the name of a class included in java.lang
	 * @return
	 */
	public static boolean isJavaLang(String className) {
		if (langDictionary == null) {
			init();
		}
		return langDictionary.contains(className);
	}
}
