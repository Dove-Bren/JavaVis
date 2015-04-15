package com.smanzana.JavaVis;

import org.yaml.snakeyaml.Yaml;

public class SettingsManager {
	
	/**
	 * 
	 */
	
	private final static String configName = "config.yml";
	
	//TODO put settings here
	
	public SettingsManager() {
		Yaml settings;
		settings = new Yaml();
		settings.load(configName);
		
		
	}
	
}
