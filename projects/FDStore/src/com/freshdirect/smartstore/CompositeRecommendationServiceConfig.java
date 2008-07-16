package com.freshdirect.smartstore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.fdstore.FDRuntimeException;

/**
 * Compostite Smart Store Configuration.
 * 
 * A composite service is made up of several other services.
 * This instance describes their configurations (recursively).
 * 
 * The class is abstract. There will be singleton instances only.
 * @author istvan
 *
 */
public abstract class CompositeRecommendationServiceConfig extends
		RecommendationServiceConfig {

	// Map<RecommendationServiceConfig,Integer>
	private Map parts = new HashMap();
	
	// hashCode, calculated as the object is created
	private int hashCode = super.hashCode();
	
	/**
	 * Constructor.
	 * 
	 * Will call init.
	 * @see #init()
	 * @param name
	 */
	protected CompositeRecommendationServiceConfig(String name) {
		super(name,RecommendationServiceType.COMPOSITE);
		init();
	}
	
	/**
	 * Add a part service.
	 * @param partConfig
	 * @param frequency relative frequency of selecting this service
	 */
	protected void addPart(RecommendationServiceConfig partConfig, int frequency) {
		if (frequency <= 0) return;
		if (parts.containsKey(partConfig)) {
			throw new FDRuntimeException("In " + getClass() + ", " + partConfig + " is already part of " + this);
		}
		parts.put(partConfig,new Integer(frequency));
		hashCode ^= partConfig.hashCode() + frequency;
	}
	
	/**
	 * Initialization of parts.
	 * The subclass must implement this!
	 */
	protected abstract void init();
	
	/**
	 * Get the frequency of the service.
	 * @param config
	 * @return frequency (or 0 if not part)
	 */
	public int getFrequency(RecommendationServiceConfig config) {
		Integer freq = (Integer)parts.get(config);
		if (freq == null) return 0;
		else return freq.intValue();
	}
	
	/**
	 * Get part configurations.
	 * 
	 * @return part configurations {@link Collection}<{@link RecommendationServiceConfig}>
	 */
	public Collection getPartConfigs() {
		return parts.keySet();
	}
	
	/**
	 * Equality.
	 * Two configurations are equal if they have the same parts with the same frequency.
	 * This is a relatively costly calculation (which will only be performed when
	 * the {@link #hashCode hash codes} are equal.
	 *  
	 * @param o other
	 * @return if same composite service config
	 */
	public boolean equals(Object o) {
		if (hashCode() != o.hashCode()) return false;
		if (!(o instanceof CompositeRecommendationServiceConfig)) return false;
		CompositeRecommendationServiceConfig ct = (CompositeRecommendationServiceConfig)o;
		if (ct.getName() != getName()) return false;
		if (ct.getPartConfigs().size() != getPartConfigs().size()) return false;
		for(Iterator i = parts.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry)i.next();
			int f = ct.getFrequency((RecommendationServiceConfig)e.getKey());
			if (f != ((Integer)e.getValue()).intValue()) return false;
		}
		return true;
		
	}
	
	/**
	 * Get hash code.
	 * The hash code is calculated when the object is created.
	 * @return hashcode
	 */
	public int hashCode() {
		return hashCode;
	}
	
	/**
	 * Write config with parts.
	 * @return rep.
	 */
	public String toString() {

		StringBuffer buffer = new StringBuffer(256);
		buffer.
			append(getName()).
			append('{');

		for(Iterator i = parts.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry)i.next();
			buffer.
				append(e.getKey()).
				append(':').
				append(e.getValue());
			if (i.hasNext()) buffer.append(',');
		}
		buffer.append('}');
		return buffer.toString();
	}

}
