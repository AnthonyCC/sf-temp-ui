package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.Variant;

/**
 * General utilities for SmartStore.
 * 
 * @author istvan
 * 
 */
public class SmartStoreUtil {

	/**
	 * Deduce the product content key.
	 * 
	 * If the argument is a product, it will return the argument, if it is a
	 * sku, it returns the corresponding product. Otherwise, and in case of any
	 * problems, it returns null.
	 * 
	 * @param key
	 *            query content key
	 * @return corresponding product
	 */
	public static ContentKey getProductContentKey(ContentKey key) {
		try {
			if (key.getType().equals(FDContentTypes.PRODUCT))
				return key;
			else if (key.getType().equals(FDContentTypes.SKU)) {
				SkuModel skuModel = (SkuModel) ContentFactory.getInstance()
						.getContentNodeByKey(key);
				return skuModel.getParentNode().getContentKey();
			} else
				return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the product corresponding to a SKU code.
	 * 
	 * @param skuCode
	 * @return product
	 */
	public static ContentKey getProductContentKey(String skuCode) {
		return getProductContentKey(new ContentKey(FDContentTypes.SKU, skuCode));
	}

	public static final String SKIP_OVERRIDDEN_VARIANT = "__skip_overridden_variant__";

	/**
	 * Get the recommendation service for the user.
	 * 
	 * This method checks if the variant was overridden for the user in his
	 * profile (otherwise it returns the service).
	 * 
	 * @param user
	 * @param feature
	 * @param overridde
	 *            variant id to use (forced)
	 * @return selected recommendation service
	 * @throws FDResourceException
	 */
	public static RecommendationService getRecommendationService(FDUserI user,
			EnumSiteFeature feature, String override)
			throws FDResourceException {
		RecommendationService svc = null;

		if (!SKIP_OVERRIDDEN_VARIANT.equalsIgnoreCase(override)) {
			String value = null;

			// a., manual override
			if (override != null) {
				value = override;
			}
			// b., get overridden variant from customer's profile
			if (value == null) {
				// lookup overridden variant
				OverriddenVariantsHelper helper = new OverriddenVariantsHelper(
						user);
				value = helper.getOverriddenVariant(feature);
			}

			if (value != null) {
				svc = (RecommendationService) SmartStoreServiceConfiguration
						.getInstance().getServices(feature).get(value);
			}
		}

		// default case - use the basic facility
		if (svc == null) {
			String customerPK = user != null ? user.getPrimaryKey() : null;
			svc = VariantSelectorFactory.getInstance(feature)
					.select(customerPK);
			System.out.println("Recommended service "+svc);
		}

		return svc;
	}

	/**
	 * Returns label-description couple for variant. This function is used by
	 * PIP
	 * 
	 * @param v
	 *            {@link Variant variant}
	 * 
	 * @return String[label, inner text] couple
	 * 
	 *         Tags: SmartStore, PIP
	 */
	public static synchronized String[] getVariantPresentation(Variant v) {
		return new String[] { v.getServiceConfig().getPresentationTitle(),
				v.getServiceConfig().getPresentationDescription() };
	}

	/**
	 * Checks if 'anId' is a valid variant ID
	 * 
	 * @param anId
	 *            variant ID
	 * @param feat
	 *            {@link EnumSiteFeature} site feature
	 * 
	 * @return result of check
	 */
	public static boolean checkVariantId(String anId, EnumSiteFeature feat) {
		if (anId == null)
			return false;

		if (feat == null)
			feat = EnumSiteFeature.DYF;

		Map services = SmartStoreServiceConfiguration.getInstance()
				.getServices(feat);
		for (Iterator it = services.keySet().iterator(); it.hasNext();) {
			if (anId.equals((String) it.next())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Sort cohort names numerically
	 * 
	 * @param names
	 */
	public static void sortCohortNames(List names) {
		Collections.sort(names, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					else
						return -1;
				} else {
					if (o2 == null)
						return -1;
					else {
						String s1 = o1.toString();
						String s2 = o2.toString();
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
		});
	}

	/**
	 * get the currently active variants sorted in weight
	 * 
	 * @param feature
	 * @return
	 */
	public static SortedMap getVariantsSortedInWeight(EnumSiteFeature feature) {
		return getVariantsSortedInWeight(feature, null, true);
	}

	/**
	 * get the variant mappings in the DB for a given date (null means latest)
	 * @param feature
	 * @param date
	 * @return
	 */
	public static SortedMap getVariantsSortedInWeight(EnumSiteFeature feature, Date date) {
		return getVariantsSortedInWeight(feature, date, false);
	}
	
	/**
	 * get the currently active variant map or the latest or the one for a
	 * specific date in DB, depending on the specified parameters
	 * 
	 * @param feature
	 * @param date
	 *            if current is false, specifies the date for the configuration
	 *            is searched (if null and current is false then get the latest)
	 * @param current
	 *            it true, get the current
	 * @return
	 */
	public static SortedMap getVariantsSortedInWeight(EnumSiteFeature feature,
			Date date, boolean current) {

		final VariantSelection vs = VariantSelection.getInstance();
		Map cohorts = vs.getCohorts();
		Map assignment = getAssignment(feature, date, current);
		final Map clone = new HashMap();
		List variants = vs.getVariants(feature);
		Iterator it = variants.iterator();
		while (it.hasNext()) {
			clone.put(it.next(), new Integer(0));
		}

		it = cohorts.keySet().iterator();
		while (it.hasNext()) {
			String cohort = (String) it.next();
			String variant = (String) assignment.get(cohort);
			if (variant != null) {
				int weight = ((Integer) clone.get(variant)).intValue();
				weight += ((Integer) cohorts.get(cohort)).intValue();
				clone.put(variant, new Integer(weight));
			}
		}

		SortedMap weights = new TreeMap(new Comparator() {
			public int compare(Object o1, Object o2) {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					else
						return -1;
				} else {
					if (o2 == null)
						return -1;
					else {
						String s1 = o1.toString();
						String s2 = o2.toString();
						int i1 = ((Integer) clone.get(s1)).intValue();
						int i2 = ((Integer) clone.get(s2)).intValue();
						if (i1 == i2)
							return s1.compareToIgnoreCase(s2);
						else {
							return i2 - i1;
						}
					}
				}
			}
		});
		weights.putAll(clone);

		return weights;
	}

	public static List getVariantNamesSortedInUse(EnumSiteFeature feature) {
		return getVariantNamesSortedInUse(feature, null, true);
	}

	public static List getVariantNamesSortedInUse(EnumSiteFeature feature, Date date) {
		return getVariantNamesSortedInUse(feature, date, false);
	}

	public static List getVariantNamesSortedInUse(EnumSiteFeature feature, Date date, boolean current) {
		final VariantSelection vs = VariantSelection.getInstance();
		List variants = vs.getVariants(feature);
		Map assignment;
		assignment = getAssignment(feature, date, current);

		getVariantNamesSortedInUse(variants, assignment);
		return variants;
	}

	/**
	 * get the currently active cohort-variant assignment for a given feature
	 * 
	 * @param feature
	 * @return
	 */
	public static Map getAssignment(EnumSiteFeature feature) {
		return getAssignment(feature, null, true);
	}

	/**
	 * get the cohort-variant assignment for a given feature and for a given date
	 * 
	 * Null date means get the latest in the DB (does not necessary equal to the
	 * currently active 
	 * 
	 * @param feature
	 * @param date
	 * @return
	 */
	public static Map getAssignment(EnumSiteFeature feature, Date date) {
		return getAssignment(feature, date, false);
	}

	/**
	 * get the currently active cohort-variant assignment or the one specific to
	 * a given date depending on the specified parameters
	 * 
	 * @param feature
	 * @param date
	 * @param current
	 * @return
	 */
	public static Map getAssignment(EnumSiteFeature feature, Date date,
			boolean current) {
		Map assignment;
		final VariantSelection vs = VariantSelection.getInstance();
		if (current) {
			List cohorts = vs.getCohortNames();
			VariantSelector vsr = VariantSelectorFactory.getInstance(feature);
			assignment = new HashMap(cohorts.size());
			for (int i = 0; i < cohorts.size(); i++)
				assignment.put(cohorts.get(i), vsr.getService((String) cohorts.get(i)) == null ? null :
						vsr.getService((String) cohorts.get(i)).getVariant().getId());
		} else
			assignment = vs.getVariantMap(feature, date);
		return assignment;
	}

	private static void getVariantNamesSortedInUse(List variants, final Map assignment) {
		Collections.sort(variants, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (o1 == null) {
					if (o2 == null) {
						return 0;
					} else {
						return -1;
					}
				} else {
					if (o2 == null) {
						return -1;
					} else {
						String s1 = o1.toString();
						String s2 = o2.toString();
						if (assignment.containsValue(s1)) {
							if (assignment.containsValue(s2)) {
								return s1.compareTo(s2);
							} else {
								return -1;
							}
						} else {
							if (assignment.containsValue(s2)) {
								return 1;
							} else {
								return s1.compareTo(s2);
							}
						}
					}
				}
			}
		});
	}
	
	/**
	 * sorts the names with ignored case
	 * 
	 * @param names
	 * @return
	 */
	public static List sortVariantNames(List names) {
		Collections.sort(names, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (o1 == null) {
					if (o2 == null)
						return 0;
					else
						return -1;
				} else {
					if (o2 == null)
						return -1;
					else {
						String s1 = o1.toString();
						String s2 = o2.toString();
						return s1.compareToIgnoreCase(s2);
					}
				}
			}
		});
		
		return names;
	}
	
	public static boolean isCohortAssigmentUptodate() {
		List siteFeatures = EnumSiteFeature.getSmartStoreEnumList();
		Iterator it = siteFeatures.iterator();
		while (it.hasNext()) {
			EnumSiteFeature sf = (EnumSiteFeature) it.next();
			Map curVars = getAssignment(sf, null, true);
			Map asgmnt = (Map) getAssignment(sf, null, false);
			if (!curVars.equals(asgmnt)) {
				return false;
			}
		}

		return true;
	}


	/**
	 * Convenience method to get Variant by its ID
	 * 
	 * @param featureId Site Feature
	 * @param variantId Variant ID
	 * 
	 * @return variant
	 */
	public static Variant getVariant(String featureId, String variantId) {
		if (featureId == null || variantId == null)
			return null;

		EnumSiteFeature feature = EnumSiteFeature.getEnum(featureId);
		if (feature == null)
			return null;

		RecommendationService svc = (RecommendationService) SmartStoreServiceConfiguration
		.getInstance().getServices(feature).get(variantId);

		return svc.getVariant();
	}
	
	
	/**
	 * 
	 * @param models List<ContentNodeModel>
	 * @return List<ContentKey>
	 */
        public static List toContentKeysFromModels(List models) {
            if (models==null) {
                return null;
            }
            List result = new ArrayList(models.size());
            for (Iterator iter = models.iterator(); iter.hasNext();) {
                ContentNodeModel model = (ContentNodeModel) iter.next();
                result.add(model.getContentKey());
            }
            return result;
        }

        public static List toContentNodesFromKeys(List keys) {
            if (keys==null) {
                return null;
            }
            List result = new ArrayList(keys.size());
            ContentFactory factory = ContentFactory.getInstance();
            for (Iterator iter = keys.iterator(); iter.hasNext();) {
                ContentKey contentKey = (ContentKey) iter.next();
                ContentNodeModel model = factory.getContentNodeByKey(contentKey);
                result.add(model);
            }
            return result;
        }

	public static ThreadLocal CFG_PRODS = new ThreadLocal() {
	    protected Object initialValue() {
	        return new HashMap();
	    }
	};

	public static void clearConfiguredProductCache() {
		((Map) CFG_PRODS.get()).clear();
	}

	/**
	 * This method returns a ProductModel from a ConfiguredProduct or ConfiguredProductGroup and store the original CP or CPG in a thread local cache, this is used later by the configurator.
	 * @param pm
	 * @return
	 */
	public static ProductModel addConfiguredProductToCache(ProductModel pm) {
		ProductModel orig = pm;
		while (pm instanceof ConfiguredProduct)
			pm = ((ConfiguredProduct) pm).getProduct();
		if (pm != orig && pm != null)
			((Map) CFG_PRODS.get()).put(pm.getContentKey().getId(), orig);
		return pm;
	}

	public static Map getConfiguredProductCache() {
		return (Map) CFG_PRODS.get();
	}

	public static List addConfiguredProductToCache(List list) {
		List ret = new ArrayList(list.size());
		for (ListIterator it = list.listIterator(); it.hasNext(); ) {
			ProductModel current = (ProductModel) it.next();
			ProductModel replace = addConfiguredProductToCache((ProductModel) current);
			if (replace != null)
				ret.add(replace);
		}
		return ret;
	}
}
