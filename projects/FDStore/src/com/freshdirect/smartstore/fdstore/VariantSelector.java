package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import com.freshdirect.smartstore.RecommendationService;

/**
 * Variant selection based on user id.
 * 
 * Variants (aka particular {@link RecommendationService recommendation services}) are assigned based on
 * user ids (cohorts). The only public method is {@link #select(String)}. This works the following way.
 * 
 * There are k cohorts. For each site feature these cohorts can be assigned differently to the n variants.
 * This class first identifies which cohort the user belongs to and then returns the corresponding
 * variant.
 * 
 * This class is abstract, subclasses must implement the {@link #init()} method.
 * 
 * @author istvan
 */
public abstract class VariantSelector {
	
	/**
	 * Stores a cohort, the corresponding variant, frequency and mass.
	 * 
	 */
	protected class CohortAssignment {
		
		private String cohort; // id
		private RecommendationService variant;
		private int frequency; // cohort freq
		private int mass; // the cumulative mass "to the left"
		
		/**
		 * Constructor.
		 * @param cohort id
		 * @param variant service
		 * @param frequency cohort's 
		 */
		protected CohortAssignment(String cohort, RecommendationService variant, int frequency) {
			this.cohort = cohort;
			this.variant = variant;
			this.frequency = frequency;
			this.mass = 0;
		}
		
		/**
		 * Get assigned variant.
		 * @return assigned variant.
		 */
		public RecommendationService getVariant() {
			return variant;
		}
		
		/**
		 * Get cohort.
		 * @return cohort id
		 */
		public String getCohort() {
			return cohort;
		}
		
		/**
		 * Get mass.
		 * Return the cumulative mass of cohorts "to the left"; that is
		 * all cohorts that precede this, plus this frequency.
		 * @return mass "to the left"
		 */
		public int getMass() {
			return mass;
		}	
		
		public String toString() {
			return new StringBuffer()
				.append("[mass = ").append(mass)
				.append(" freq = ").append(frequency)
				.append(" cohort = ").append(cohort).append("] ->")
				.append(variant.getVariant().getId()).toString();
		}
	}
	
	/**
	 * The Cumulative Distribution of cohorts.
	 * The actual sorted list of variants with cumulative masses in cohort order. 
	 */
	protected List cdf = new ArrayList();
	
	/**
	 * Total mass in all cohorts.
	 */
	protected int total = 0;
	
	/**
	 * Compare variants by cohort.
	 */
	protected Comparator variantComparator = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((CohortAssignment)o1).getCohort().compareTo(((CohortAssignment)o2).getCohort());
		}
		
	};
	
	/**
	 * Compare variants by mass.
	 * Used explicitly to (binary) search the cohort the user belongs to.
	 */
	protected Comparator massComparator = new Comparator() {
		
		// get mass as integer
		private int getMass(Object o) {
			if (o instanceof Integer) return ((Integer)o).intValue();
			else return ((CohortAssignment)o).getMass();
		}

		public int compare(Object o1, Object o2) {
			
			return getMass(o1) - getMass(o2);
		}
	};

	/**
	 * Add a cohort group definition.
	 * 
	 * @param cohort id
	 * @param variant service assigned to cohort
	 * @param frequency relative frequency of cohort
	 */
	protected void addCohort(String cohort, RecommendationService variant, int frequency) {
		CohortAssignment vf = new CohortAssignment(cohort, variant,frequency);
		int p = Collections.binarySearch(cdf,vf,variantComparator);
		if (p < 0) { // new
			p = -p - 1;
			// shift
			cdf.add(null); 
			for(int i = cdf.size() -1 ; i > p; --i) cdf.set(i, cdf.get(i-1));
			// fill 
			cdf.set(p, vf);
			if (p > 0) vf.mass = ((CohortAssignment)cdf.get(p-1)).getMass();
		} else { // exists
			// adjust
			((CohortAssignment)cdf.get(p)).frequency += frequency;
		}
		
		for(int i=p; i< cdf.size(); ++i) {
			((CohortAssignment)cdf.get(i)).mass += frequency;
		}
		total += frequency;
		
	}
	
	/**
	 * Randomize user id.
	 * 
	 * The algorithm requires randomly distributed keys. Since raw user
	 * ids aren't sufficiently random, this function maps them to a more random set.
	 * The requirements are the following.
	 * <ul>
	 * <li>if u1 == u2, then randomize(u1) == randomize(u2)</li>
	 * <li>the distribution of (randomize(u) % m) is approximately U(0, 1, ..., m-1) for small m</li>
	 * </ul>
	 * 
	 * This implementation uses {@link Math#abs Math.abs}({@link String#hashCode()}), but subclasses can provide
	 * smarter algorithms. Ensure to return a natural number!
	 * 
	 * @param erpUserId user id
	 * @return randomized user id
	 */
	protected int randomize(String erpUserId) {
		return Math.abs(erpUserId.hashCode());
	}
	
	/**
	 * Initialization of distribution.
	 */
	protected abstract void init();
	
	/**
	 * Select a variant based on user id.
	 * @param erpUserId user id
	 * @return variant
	 */
	public RecommendationService select(String erpUserId) {
		Integer v = new Integer(randomize(erpUserId) % total);
		int p = Collections.binarySearch(cdf,v,massComparator);
		if (p < 0) p = -p -1; else ++p;
		return ((CohortAssignment)cdf.get(p)).getVariant();
	}
	
}
