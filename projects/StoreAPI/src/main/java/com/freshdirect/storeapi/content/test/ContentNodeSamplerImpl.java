package com.freshdirect.storeapi.content.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.framework.util.DiscreteRandomSamplerWithReplacement;
import com.freshdirect.storeapi.CmsLegacy;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;

@CmsLegacy
public class ContentNodeSamplerImpl implements ContentNodeSampler {

	protected ContentType type;

	protected ContentFactory fact = ContentFactory.getInstance();

	/** known content keys. keys that are not in this map will not be generated */
	protected Map knownContentKeys = new HashMap();
	protected DiscreteRandomSamplerWithReplacement sampler = new DiscreteRandomSamplerWithReplacement();

	public ContentNodeSamplerImpl(ContentType type, Collection contentKeys) {
		this.type = type;
		addContentKeys(contentKeys);
	}

	public ContentNodeSamplerImpl(ContentType type) {
		this(type,CmsManager.getInstance().getContentKeysByType(type));
	}

	@Override
    public void addContentKeys(Collection contentKeys) {
		for(Iterator i = contentKeys.iterator(); i.hasNext(); ) {
			ContentKey key = (ContentKey)i.next();
			if (!key.getType().equals(type)) continue;
			knownContentKeys.put(key.getId(), key);
		}

	}

	@Override
    public void setFrequency(String id, int frequency) {
		if (!knownContentKeys.containsKey(id))
			throw new NoSuchElementException("Content node with id " + id + " is not in known content key set");
		sampler.setItemFrequency(id, frequency);
	}

	@Override
    public void resetFrequencies() {
		sampler.clear();
	}

	@Override
    public Collection getContentKeys() {
		return knownContentKeys.values();
	}

	@Override
    public long getFrequency(String id) {
		return sampler.getItemFrequency(id);
	}

	@Override
    public void addFrequenciesFromMap(Map frequencies) {
		for(Iterator i = frequencies.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry)i.next();
			String id = (String)entry.getKey();
			if (!knownContentKeys.containsKey(id)) continue;
			sampler.addValue(id,((Integer)entry.getValue()).intValue());
		}

	}

	@Override
    public void setFrequenciesFromMap(Map frequencies) {
		sampler.clear();
		addFrequenciesFromMap(frequencies);
	}

	@Override
    public List getSampleContentKeys(int n, Random R) {
		List keys = new ArrayList();
		if (sampler.getTotalFrequency() == 0) return keys;
		for(int i=0; i< n; ) {
			String id = (String)sampler.getRandomItem(R);
			if (id == null) continue;
			ContentKey key = (ContentKey)knownContentKeys.get(id);
			if (key == null) continue;
			++i;
			keys.add(key);
		}
		return keys;
	}

	@Override
    public ContentKey next(Random R) {
		return (ContentKey)knownContentKeys.get(sampler.getRandomItem(R));
	}

}