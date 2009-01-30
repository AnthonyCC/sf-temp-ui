package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class contains the implementation of the cohort selection algorithm based on cohort weight, and user ID randomization. The main user of this class is the VariantSelector,
 * which decides which recommendation service associated with which cohort in the different site features.
 * 
 * @see VariantSelectorFactory
 * @see VariantSelector
 * @author zsombor
 *
 */
public class CohortSelector {

    private static Map            cohorts     = null;
    private static int            cohortSum   = 0;
    private static List           cohortNames = null;

    private static CohortSelector instance;

    /**
     * Stores a cohort, the corresponding variant, frequency and mass.
     * 
     */
    protected class CohortAssignment {

        private String cohort;   // id
        private int    frequency; // cohort freq
        private int    mass;     // the cumulative mass

        // "to the left"

        /**
         * Constructor.
         * 
         * @param cohort
         *            id
         * @param variant
         *            service
         * @param frequency
         *            cohort's
         */
        protected CohortAssignment(String cohort, int frequency) {
            this.cohort = cohort;
            this.frequency = frequency;
            this.mass = 0;
        }

        /**
         * Get cohort.
         * 
         * @return cohort id
         */
        public String getCohort() {
            return cohort;
        }

        /**
         * Get mass. Return the cumulative mass of cohorts "to the left"; that
         * is all cohorts that precede this, plus this frequency.
         * 
         * @return mass "to the left"
         */
        public int getMass() {
            return mass;
        }

        public String toString() {
            return new StringBuffer().append("[mass = ").append(mass).append(" freq = ").append(frequency).append(" cohort = ").append(cohort).append("]")
                    .toString();
        }
    }

    /**
     * The Cumulative Distribution of cohorts. The actual sorted list of
     * variants with cumulative masses in cohort order.
     */
    protected List       cdf               = new ArrayList();

    /**
     * Total mass in all cohorts.
     */
    protected int        total             = 0;

    /**
     * Compare variants by cohort.
     */
    protected Comparator variantComparator = new Comparator() {
                                               public int compare(Object o1, Object o2) {
                                                   return ((CohortAssignment) o1).getCohort().compareTo(((CohortAssignment) o2).getCohort());
                                               }
                                           };

    /**
     * Compare variants by mass. Used explicitly to (binary) search the cohort
     * the user belongs to.
     */
    protected Comparator massComparator    = new Comparator() {
                                               // get mass as integer
                                               private int getMass(Object o) {
                                                   if (o instanceof Integer) {
                                                       return ((Integer) o).intValue();
                                                   } else {
                                                       return ((CohortAssignment) o).getMass();
                                                   }
                                               }

                                               public int compare(Object o1, Object o2) {
                                                   return getMass(o1) - getMass(o2);
                                               }
                                           };

    /**
     * Add a cohort group definition.
     * 
     * @param cohort
     *            id
     * @param frequency
     *            relative frequency of cohort
     */
    protected void addCohort(String cohort, int frequency) {
        CohortAssignment vf = new CohortAssignment(cohort, frequency);
        int p = Collections.binarySearch(cdf, vf, variantComparator);
        if (p < 0) { // new
            p = -p - 1;
            // shift
            cdf.add(null);
            for (int i = cdf.size() - 1; i > p; --i) {
                cdf.set(i, cdf.get(i - 1));
            }
            // fill
            cdf.set(p, vf);
            if (p > 0)
                vf.mass = ((CohortAssignment) cdf.get(p - 1)).getMass();
        } else { // exists
            // adjust
            ((CohortAssignment) cdf.get(p)).frequency += frequency;
        }

        for (int i = p; i < cdf.size(); ++i) {
            ((CohortAssignment) cdf.get(i)).mass += frequency;
        }
        total += frequency;
    }

    /**
     * Select a cohort based on user id.
     * 
     * @param erpUserId
     *            user id
     * @return variant
     */
    public CohortAssignment getCohortAssignment(String erpUserId) {
        Integer v = new Integer(getCohortIndex(erpUserId));
        int p = Collections.binarySearch(cdf, v, massComparator);
        if (p < 0) {
            p = -p - 1;
        } else {
            ++p;
        }
        return ((CohortAssignment) cdf.get(p));
    }

    public String getCohortName(String erpUserId) {
        CohortAssignment cohortAssignment = getCohortAssignment(erpUserId);
        if (cohortAssignment != null) {
            return cohortAssignment.getCohort();
       
        }
        return null;
    }
    
    protected static byte[][] RANDOM_DIGIT_MAP = new byte[10][10];
    
    static {
    	Object generator = new Object() {
    		private long state = 439094301L;
    		private int bits = 8;
    		
    		public int hashCode() {
    			state = (state * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
        		return (int)(state >>> (48-bits));	
    		}
    	};
    	
    	for(int i = 0; i< RANDOM_DIGIT_MAP.length; ++i) {
    		for(int j=0; j<RANDOM_DIGIT_MAP[i].length; ++j) {
    			RANDOM_DIGIT_MAP[i][j] = (byte)j;
    		}
    		for(int j=2; j<RANDOM_DIGIT_MAP[i].length; ++j) {
    			int o = Math.abs(generator.hashCode()) % j;
    			
    			byte tmp = RANDOM_DIGIT_MAP[i][o];
    			RANDOM_DIGIT_MAP[i][o] = RANDOM_DIGIT_MAP[i][j];
    			RANDOM_DIGIT_MAP[i][j] = tmp;
    		}	
    	}
    	
    	for(int i=0; i< RANDOM_DIGIT_MAP.length; ++i) {
    		for(int j=0; j< RANDOM_DIGIT_MAP[i].length; ++j) {
    			System.out.print(' ');
    			System.out.print((int)RANDOM_DIGIT_MAP[i][j]);
    		}
    		System.out.println();
    	}
    	
    }

    /**
     * Randomize user id.
     * 
     * The algorithm requires randomly distributed keys. Since raw user ids
     * aren't sufficiently random, this function maps them to a more random set.
     * The requirements are the following.
     * <ul>
     * <li>if u1 == u2, then randomize(u1) == randomize(u2)</li>
     * <li>the distribution of (randomize(u) % m) is approximately U(0, 1, ...,
     * m-1) for small m</li>
     * </ul>
     * 
     * @param erpUserId
     *            user id
     * @return randomized user id
     */
    protected int randomize(String erpUserId) {
    	int z = 0;
    	int r = 0;
    	if (erpUserId.length() > 0) {
    		r = Math.abs(erpUserId.charAt(erpUserId.length()-1)) % RANDOM_DIGIT_MAP.length; 
    	}
    	
    	for(int i=0; i < erpUserId.length(); ++i) {
    		int c = erpUserId.charAt(i);
    		int x = (byte)c;
    		if (c >= '0' && c <= '9') {
    			x = RANDOM_DIGIT_MAP[r][c - '0'] + '0';
    		} 
    		z = 31*z + x;
    	}
    	return Math.abs(z);
    }
	
    /**
     * Important, this method is overridden in test cases, do not remove!
     * 
     * @param erpUserId
     * @return
     */
    protected int getCohortIndex(String erpUserId) {
        return erpUserId != null ? randomize(erpUserId) % getCohortWeightSum() : 0;
    }

    /**
     * Returns the cohort->weight map
     * 
     * @return Map<Cohort,weight>
     */
    public static synchronized Map getCohorts() {
        if (cohorts == null) {
            // cache cohort map
            cohorts = VariantSelection.getInstance().getCohorts();
            cohortSum = calculateSum();
        }
        return cohorts;
    }

    static int calculateSum() {
        int sum = 0;
        for (Iterator iter = cohorts.values().iterator(); iter.hasNext();) {
            Integer n = (Integer) iter.next();
            sum += n.intValue();
        }
        return sum;
    }

    synchronized int getCohortWeightSum() {
        if (cohorts == null) {
            getCohorts();
        }
        return cohortSum;

    }

    public static void setCohorts(Map cohorts) {
        CohortSelector.cohorts = cohorts;
        cohortSum = calculateSum();
    }

    public synchronized static CohortSelector getInstance() {
        if (instance == null) {
            Map ch = getCohorts();
            List cohortNames = CohortSelector.getCohortNames();

            CohortSelector cs = new CohortSelector();
            
            for (Iterator iter = cohortNames.iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                int freq = ((Number) ch.get(name)).intValue();
                cs.addCohort(name, freq);
            }
            instance = cs;
        }
        return instance;
    }

    

    /**
     * Return an <b>ordered</b> list of cohort names.
     * 
     * @return
     */
    public synchronized static List getCohortNames() {
        if (cohortNames == null) {
                // cache cohort map
                cohortNames = VariantSelection.getInstance().getCohortNames();
        }
        return cohortNames;
    }
    
    public synchronized static void setCohortNames(List cohortNames) {
        CohortSelector.cohortNames = cohortNames;
    }
    

}
