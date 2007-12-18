package com.freshdirect.rules.ui;

import java.util.Comparator;

import com.freshdirect.rules.Rule;

public class RulesComparator implements Comparator {

	public static final Comparator INSTANCE = new RulesComparator();

	public int compare(Object o1, Object o2) {
		Rule r1 = (Rule) o1;
		Rule r2 = (Rule) o2;
		if (r1.getOutcome() == null && r2.getOutcome() != null) {
			return -1;
		}
		if (r1.getOutcome() != null && r2.getOutcome() == null) {
			return 1;
		}
		return r1.getName().compareTo(r2.getName());
	}

}
