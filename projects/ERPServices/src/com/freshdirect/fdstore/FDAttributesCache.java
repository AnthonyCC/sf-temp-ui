package com.freshdirect.fdstore;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.content.attributes.AttributesFacade;
import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.content.attributes.ErpsAttributes;
import com.freshdirect.content.attributes.ErpsAttributesKey;
import com.freshdirect.fdstore.cache.FDAbstractCache;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDAttributesCache extends FDAbstractCache<ErpsAttributesKey, Date, ErpsAttributes> {
	private static final Logger LOGGER = LoggerFactory.getInstance(FDAttributesCache.class); 
	
	public static final ErpsAttributesKey NULL_KEY = new ErpsAttributesKey(null, null, null);

	private static final ErpsAttributes NULL_VALUE = new ErpsAttributes(NULL_KEY, new Date(Long.MAX_VALUE)) {
		private static final long serialVersionUID = 1060089542102745785L;

		@Override
		public void setAttributeValue(EnumAttributeName attr, Object value, Date date) {
			throw new UnsupportedOperationException();
		}
	};

	private static FDAttributesCache instance;
	
    public synchronized static FDAttributesCache getInstance() {
        if (instance == null) {
            instance = new FDAttributesCache();
            instance.startRefresher();
        }
        return instance;
    }

    public synchronized static void mockInstance() {
        if (instance == null) {
            instance = new FDAttributesCache(true);
        }
    }

	private FDAttributesCache() {
		super(DateUtil.MINUTE * FDStoreProperties.getAttributesRefreshPeriod());
		put(NULL_KEY, NULL_VALUE);
	}

	private FDAttributesCache(boolean mock) {
		super(mock);
		put(NULL_KEY, NULL_VALUE);
	}

	@Override
	protected Map<ErpsAttributesKey, ErpsAttributes> loadData(Date lastVersion) {
		try {
			LOGGER.info("REFRESHING");
			Map<ErpsAttributesKey, ErpsAttributes> data = AttributesFacade.loadAttributes(lastVersion == null ? new Date(0) : lastVersion);
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (FDResourceException e) {
			LOGGER.error("REFRESH FAILED: error while loading attributes data", e);
			throw new FDRuntimeException(e);
		}
	}
	
	public ErpsAttributes getAttributes(ErpsAttributesKey key) {
		// TODO handle null keys properly
		if (key == null)
			key = NULL_KEY;
		ErpsAttributes value = super.get(key);
		return value == null ? new ErpsAttributes(key, new Date()) : value;
	}

	public void saveAttributes(ErpsAttributes attributes) {
		try {
			if (attributes.getKey() == null)
				throw new FDRuntimeException("invalid attribute key (null)");
			AttributesFacade.storeAttributes(attributes);
			super.put(attributes.getKey(), attributes);
		} catch (FDResourceException e) {
			LOGGER.error("error storing attributes (" + attributes.getKey() + ")", e);
			throw new FDRuntimeException(e);
		}
	}

	public void saveAttributes(Collection<ErpsAttributes> attributes) {
		try {
			AttributesFacade.storeAttributes(attributes);
			for (ErpsAttributes item : attributes)
				super.put(item.getKey(), item);
		} catch (FDResourceException e) {
			LOGGER.error("error storing collection of attributes", e);
			throw new FDRuntimeException(e);
		}
	}
}
