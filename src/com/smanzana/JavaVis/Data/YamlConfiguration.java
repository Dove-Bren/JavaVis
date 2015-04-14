package com.smanzana.JavaVis.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * YAML implementation of configuration:<br />
 * Stores key/value pairs using<br /><br />
 * key:
 * <br />
 * [tab]value
 * @author Skyler
 *
 */
public class YamlConfiguration extends Configuration {
	
	
	public void load(File file) throws FileNotFoundException {
		Scanner input = new Scanner(file);
		load(input, 0);
	}
	
	/**
	 * Secret method for loading in stored information from a yaml config file.<br />
	 * Uses offset to know when the current tree is finished.
	 * @param file
	 * @param tabOffset
	 */
	private void load(Scanner input, int tabOffset) {
		
	}
	
	private void loadPath(String key) {
		map.put(key, null); //TODO
	}
	
}
