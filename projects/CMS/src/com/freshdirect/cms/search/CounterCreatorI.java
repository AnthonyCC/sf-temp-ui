package com.freshdirect.cms.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import com.freshdirect.cms.search.AutocompleteService.HitCounter;

public interface CounterCreatorI {

	void createCounters(HashMap<String, HitCounter> counters, String fullname);

	TreeSet<HitCounter> initWords(Collection<String> words);
}
