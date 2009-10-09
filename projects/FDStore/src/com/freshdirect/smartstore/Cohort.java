package com.freshdirect.smartstore;

import java.io.Serializable;

public class Cohort implements Serializable, Comparable<Cohort> {
	private static final long serialVersionUID = 5964417578763182053L;

	private final String id;
	
	private final int weight;
	
	public Cohort(String id, int weight) {
		super();
		this.id = id;
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public int compareTo(Cohort cohort) {
		String s1 = id;
		String s2 = cohort.id;
		if (s1 == null) {
			if (s2 == null)
				return 0;
			else
				return -1;
		} else {
			if (s2 == null)
				return 1;
			else {
				String prefix, candidate;
				prefix = candidate = "";
				while (true) {
					if (s1.length() <= candidate.length())
						break;
					else
						candidate = s1.substring(0,
								candidate.length() + 1);

					if (s2.startsWith(candidate)) {
						prefix = candidate;
						continue;
					} else
						break;
				}
				int pLen = prefix.length();
				if (pLen != 0) {
					s1 = s1.substring(pLen);
					s2 = s2.substring(pLen);
					try {
						int i1 = Integer.parseInt(s1);
						int i2 = Integer.parseInt(s2);
						return i1 - i2;
					} catch (NumberFormatException e) {
						return s1.compareTo(s2);
					}
				} else
					return s1.compareTo(s2);
			}
		}
	}
}
