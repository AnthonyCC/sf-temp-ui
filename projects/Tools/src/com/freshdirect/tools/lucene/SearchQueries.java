package com.freshdirect.tools.lucene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class SearchQueries {
	public static class QueryFrequencies {
		private List queryFreqs = new ArrayList(3);
		
		private int maxFreq = 0;
		
		public void add(QueryFrequency qf) {
			if (qf.getFrequency() > maxFreq) maxFreq = qf.getFrequency();
			for(Iterator i = queryFreqs.iterator(); i.hasNext();) {
				QueryFrequency of = (QueryFrequency)i.next();
				if (of.getValue().equals(qf.getValue())) {
					of.update(qf.getFrequency());
					return;
				}
			}
			queryFreqs.add(qf);
		}
		
		public QueryFrequency get(int i) {
			return (QueryFrequency)queryFreqs.get(i);
		}
		
		public int getMaxFrequency() {
			return maxFreq;
		}
		
		public int size() {
			return queryFreqs.size();
		}
		
		public String toString() {
			StringBuffer buff = new StringBuffer();
			for(Iterator i = queryFreqs.iterator(); i.hasNext();) {
				QueryFrequency qf = (QueryFrequency)i.next();
				buff.append(qf.getValue()).append(':').append(qf.getFrequency());
				if (i.hasNext()) buff.append(", ");
			}
			return buff.toString();
		}
	}
	
	public static class QueryFrequency {
		
		private String value;
		private int freq;
		
		public QueryFrequency(String value, int freq) {
			this.value = value;
			this.freq = freq;
		}
		
		public int getFrequency() {
			return freq;
		}
		
		public String getValue() {
			return value;
		}
		
		public void update(int freq) {
			if (this.freq < freq) this.freq = freq;
		}
		
		public String toString() {
			return value + ":" + freq;
		}
	}
	
	public static class QueryMap extends HashMap {
		
		private static final long serialVersionUID = -6594543582862291480L;

		public Object put(Object key, Object value) {
			QueryFrequencies qf = (QueryFrequencies)get(key);
			if (qf == null) {
				qf = new QueryFrequencies();
				super.put(key, qf);
			}
			qf.add((QueryFrequency)value);
			return null;
		}
	}
}
