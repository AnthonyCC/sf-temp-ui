package com.freshdirect.storeapi.autocomplete.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import com.freshdirect.storeapi.autocomplete.domain.HitCounter;

public interface CounterCreatorI {

    void createCounters(HashMap<String, HitCounter> counters, String fullname);

    TreeSet<HitCounter> initWords(Collection<String> words);
}
