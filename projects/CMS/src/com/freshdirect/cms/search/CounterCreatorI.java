package com.freshdirect.cms.search;

import java.util.HashMap;

import com.freshdirect.cms.search.AutocompleteService.HitCounter;

public interface CounterCreatorI {

	void createCounters(HashMap<String, HitCounter> counters, String fullname);

}
