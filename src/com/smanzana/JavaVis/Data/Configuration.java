package com.smanzana.JavaVis.Data;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * General configuration class.
 * Configurations provide basic access and modification. 
 * @author Skyler
 *
 */
public abstract class Configuration {
	
	private Map<String, Object> map;
	
	/**
	 * Construct an empty configuration.
	 */
	public Configuration() {
		this.map = new HashMap<String, Object>();
	}
	
	/**
	 * Return all configurations contained within this one.
	 * @return
	 */
	public Collection<Object> values() {
		return map.values();	
	}
	
	/**
	 * Returns all configurations nested inside of this configuration.<br />
	 * If there are no configurations under this one, an empty collection is returned instead.
	 * @return A collection containing all Configurations nested within this one.
	 */
	public Collection<Configuration> subConfigurations() {
		Set<Configuration> subs = new HashSet<Configuration>();
		
		for (Object o : map.values()) {
			if (o instanceof Configuration) {
				subs.add((Configuration) o);
			}
		}
		
		return subs;
	}
	
	/**
	 * Return the sub configuration associated with the passed key.
	 * @param address The {@link java.lang.String String} to look up
	 * @return The Configuration associated with the key, or null if none exists (or what's associated
	 * is not a Configuration).
	 */
	public Configuration getSubConfiguration(String address) {
		Object o = map.get(address);
		if (o instanceof Configuration) {
			return (Configuration) o;
		}
		
		return null;
	}
	
	/**
	 * Inserts the passed value at the specified address, overwriting any that may already exist
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value) {
		map.put(key, value);
	}
	
}
