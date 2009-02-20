package com.freshdirect.smartstore;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.Set;

import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;
import com.freshdirect.smartstore.impl.FavoritesRecommendationService;
import com.freshdirect.smartstore.impl.ScriptedRecommendationService;
import com.freshdirect.smartstore.sampling.ListSampler;

// import edu.emory.mathcs.backport.java.util.Collections;


/**
 * Recommendation service configuration.
 * 
 * @author istvan
 *
 */
public class RecommendationServiceConfig implements Serializable {

    public static final String PIP_DEFAULT_LABEL = "YOUR FAVORITES";
    public static final String PIP_DEFAULT_INNERTEXT = "These are some of the items you've purchased most often.";
    public static final String FI_DEFAULT_TITLE = "Our Favorites";
    
    
	/** Id of the variant. */
	protected String variantId;
	
	/** Service type. */
	protected RecommendationServiceType type;

	protected Map params;
	
	/**
	 * Constructor.
	 * @param name
	 */
	public RecommendationServiceConfig(String variantId, RecommendationServiceType type) {
		this.variantId = variantId;
		this.type = type;
	}

	// auto generated serial version id
	private static final long serialVersionUID = -5942360122361860134L;
	
	/**
	 * Get the hash code.
	 * @see #equals(Object)
	 * @return {@link #getVariantId()}.{@link String#hashCode()}
	 */
	public int hashCode() {
		return getVariantId().hashCode();
	}
	
	/**
	 * Equality.
	 * Two configurations are the same if they have the same name.
	 * Configurations have id's in the corresponding database table.
	 * @return if the two configs have the same name
	 */
	public boolean equals(Object o) {
		if (!(o instanceof RecommendationServiceConfig)) return false;
		return ((RecommendationServiceConfig)o).getVariantId().equals(getVariantId());
	}
	
	/**
	 * Get config name.
	 * @return name
	 */
	public String getVariantId() {
		return variantId;
	}
	
	/**
	 * Get config type.
	 * @return config type
	 */
	public RecommendationServiceType getType() {
		return type;
	}
	
	/** RecommendationServiceConfig
	 * As string.
	 * @return String representation
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("{variant:" + variantId + ", ");
		buf.append("type:" + type.getName() + ", ");
		buf.append("config:{");

		if (params != null) {
			for (Iterator it=params.keySet().iterator(); it.hasNext();) {
				String key = (String) it.next();
				buf.append(key + ":"+params.get(key));
				if (it.hasNext())
					buf.append(", ");
			}
		}
		buf.append("}}");
		
		return buf.toString();
	}


	public String get(String key) {
	    return (String) (params!=null ? params.get(key) : null);
	}

	public String get(String key, String defaultValue) {
		String value = (String) (params!=null ? params.get(key) : defaultValue);
		if (value==null) {
		    return defaultValue;
		}
		return value;
	}
	
	public String getFILabel() {
		return get(AbstractRecommendationService.CKEY_FI_LABEL, FI_DEFAULT_TITLE);
	}

	public String getPresentationTitle() {
		return get(AbstractRecommendationService.CKEY_PREZ_TITLE, PIP_DEFAULT_LABEL);
	}

	public String getPresentationDescription() {
			return get(AbstractRecommendationService.CKEY_PREZ_DESC,  PIP_DEFAULT_INNERTEXT);
	}
	

	public RecommendationServiceConfig set(String key, String value) {
	    if (params==null) {
	        params = new HashMap();
	    }
	    params.put(key, value);
	    return this;
	}
	
	public Set keys() {
		return params != null ? params.keySet() : Collections.EMPTY_SET;
	}



	
	public class ConfigEntryInfo {
		String description;
		String type;
		String defaultValue;
		
		public ConfigEntryInfo(String description, String type, String defaultValue) {
			this.description = description;
			this.type = type;
			this.defaultValue = defaultValue;
		}

		public String getDescription() {
			return description;
		}

		public String getType() {
			return type;
		}

		public String getDefaultValue() {
			return defaultValue;
		}
	}


	public class ConfigEntryStatus {
		public static final int OK = 0;
		public static final int NOT_CONFIGURED = 1;
		public static final int INVALID = 2;
		
		String keyName;
		int	status;
		ConfigEntryInfo info;
		
		public ConfigEntryStatus(String keyName, int status, ConfigEntryInfo info) {
			this.keyName = keyName;
			this.status = status;
			this.info = info;
		}

		public ConfigEntryStatus(String keyName, int status) {
			this(keyName, status, null);
		}

		public String getKeyName() {
			return this.keyName;
		}
		
		public String getKeyDesc() {
			if (this.info != null && this.info.getDescription() != null)
				return this.info.getDescription();
			return this.keyName;
		}
		
		public int getStatus() {
			return this.status;
		}

		public ConfigEntryInfo getInfo() {
			return this.info;
		}


		public boolean isValid()		{ return status == OK; }
		public boolean isUnconfigured()	{ return status == NOT_CONFIGURED; }
		public boolean isInvalid()		{ return status == INVALID; }
	}


	public class ConfigStatus {
		// List<ConfigEntryStatus>
		List entries;
		
		public ConfigStatus(List entries) {
			this.entries = entries;
		}
		
		public boolean isValid() {
			for (Iterator it=entries.iterator(); it.hasNext();) {
				ConfigEntryStatus ces = (ConfigEntryStatus) it.next();
				if (!ces.isInvalid())
					return false;
			}
			return true;
		}
		
		public ConfigEntryStatus getStatus(String keyName) {
			if (keyName != null) {
				for (Iterator it=entries.iterator(); it.hasNext();) {
					ConfigEntryStatus ces = (ConfigEntryStatus) it.next();
					if (keyName.equalsIgnoreCase(ces.keyName))
						return ces;
				}
			}
			return null;
		}

		public Iterator iterator() {
			return entries.iterator();
		}

		
		/**
		 * Returns the config status for the given key
		 * @param key Configuration key
		 * @return Configuration Entry Status object
		 */
		public ConfigEntryStatus get(String key) {
			for (Iterator it=entries.iterator(); it.hasNext();) {
				ConfigEntryStatus ces = (ConfigEntryStatus) it.next();
				if (key.equalsIgnoreCase(ces.keyName))
					return ces;
			}
			return null;
		}



		
		public String toString() {
			StringBuffer buf = new StringBuffer();
			List l;
			
			l = find(ConfigEntryStatus.OK);
			if (l.size() > 0) {
				buf.append("OK:");
				for (Iterator it=l.iterator(); it.hasNext();) {
					ConfigEntryStatus ces = (ConfigEntryStatus) it.next();
					buf.append(ces.keyName);
					buf.append(" ");
				}
			}
			
			l = find(ConfigEntryStatus.NOT_CONFIGURED);
			if (l.size() > 0) {
				buf.append("NC:");
				for (Iterator it=l.iterator(); it.hasNext();) {
					ConfigEntryStatus ces = (ConfigEntryStatus) it.next();
					buf.append(ces.keyName);
					buf.append(" ");
				}
			}

			l = find(ConfigEntryStatus.INVALID);
			if (l.size() > 0) {
				buf.append("INV:");
				for (Iterator it=l.iterator(); it.hasNext();) {
					ConfigEntryStatus ces = (ConfigEntryStatus) it.next();
					buf.append(ces.keyName);
					buf.append(" ");
				}
			}

			return buf.toString();
		}

		private List find(int status) {
			List result = new ArrayList();
			for (Iterator it=entries.iterator(); it.hasNext();) {
				ConfigEntryStatus ces = (ConfigEntryStatus) it.next();
				if (ces.status == status)
					result.add(ces);
			}
			return result;
		}
	}

	
	// Valid sampler names
	public static final String SAMPLERS[] = {"deterministic", "uniform", "linear", "quadratic", "cubic", "harmonic", "sqrt", "power", "complicated"};
	
	
	/**
	 * Validates a variant configuration
	 * @return
	 */
	public ConfigStatus validate(Variant variant) {
		PropertyResourceBundle b = null;
		try {
			b = new PropertyResourceBundle(getClass().getResourceAsStream("VariantConfig.properties"));
		} catch (IOException e) {}


		List keys = getConfigKeys(variant);

		// check config items
		List checkedEntries = new ArrayList();
		Set p = params == null ? Collections.EMPTY_SET : params.keySet();

		for (Iterator it = p.iterator(); it.hasNext();) {
			String configKey = (String) it.next();

			ConfigEntryInfo info = null;
			try {
				if (b.getString(configKey + ".desc") != null) {
					String iDesc = b.getString(configKey + ".desc");
					String iType = b.getString(configKey + ".type");
					String iDefaultValue = b.getString(configKey + ".default");
					info = new ConfigEntryInfo(
						iDesc, iType, "<none>".equals(iDefaultValue) ? null : iDefaultValue
					);
				}
			} catch (MissingResourceException e) {
				
			}

			ConfigEntryStatus ces = new ConfigEntryStatus(configKey, (keys.contains(configKey) ? ConfigEntryStatus.OK : ConfigEntryStatus.INVALID), info);
			checkedEntries.add(ces);
		}
		
		for (Iterator it = keys.iterator(); it.hasNext(); ) {
			String configKey = (String) it.next();
			if (!p.contains(configKey)) {
				ConfigEntryInfo info = null;
				try {
					if (b.getString(configKey + ".desc") != null) {
						String iDesc = b.getString(configKey + ".desc");
						String iType = b.getString(configKey + ".type");
						String iDefaultValue = b.getString(configKey + ".default");
						info = new ConfigEntryInfo(
							iDesc, iType, "<none>".equals(iDefaultValue) ? null : iDefaultValue
						);
					}
				} catch (MissingResourceException e) {
					
				}
				
				checkedEntries.add(new ConfigEntryStatus(configKey, ConfigEntryStatus.NOT_CONFIGURED, info));
			}
		}


		return new ConfigStatus(checkedEntries);
	}
	

	public static boolean isPresentationIncluded(Variant variant) {
		return EnumSiteFeature.DYF.equals(variant.getSiteFeature()) || EnumSiteFeature.FAVORITES.equals(variant.getSiteFeature());
	}
	
	
	public List getConfigKeys(Variant variant) {
		if (RecommendationServiceType.NIL.equals( variant.getServiceConfig().getType()) ) {
			return Collections.EMPTY_LIST;
		}
		
		EnumSiteFeature feature = variant.getSiteFeature();
		
		List keys = new ArrayList();
		
		if (RecommendationServiceConfig.isPresentationIncluded(variant)) {
			// sampler keys

			keys.add(AbstractRecommendationService.CKEY_PREZ_TITLE);
			keys.add(AbstractRecommendationService.CKEY_PREZ_DESC);
		}
		
		if (variant.getSiteFeature().equals(EnumSiteFeature.FEATURED_ITEMS)) {
			keys.add(AbstractRecommendationService.CKEY_FI_LABEL);
		}
		
		
		if (EnumSiteFeature.FAVORITES.equals(feature)) {
			keys.add(FavoritesRecommendationService.CKEY_FAVORITE_LIST_ID);
		}


		keys.add(AbstractRecommendationService.CKEY_SAMPLING_STRATEGY);
		keys.add(AbstractRecommendationService.CKEY_CAT_AGGR);
		keys.add(AbstractRecommendationService.CKEY_INCLUDE_CART_ITEMS);
		keys.add(AbstractRecommendationService.CKEY_TOP_N);
		keys.add(AbstractRecommendationService.CKEY_TOP_PERC);
		
		if ("power".equalsIgnoreCase(get(AbstractRecommendationService.CKEY_SAMPLING_STRATEGY))) {
			keys.add(ListSampler.PowerCDF.CKEY_EXPONENT);
		}

		if (RecommendationServiceType.SCRIPTED.equals(variant.getServiceConfig().getType()) ) {
			keys.add(ScriptedRecommendationService.CKEY_GENERATOR);
			keys.add(ScriptedRecommendationService.CKEY_SCORING);
		}

		return keys;
	}
}
