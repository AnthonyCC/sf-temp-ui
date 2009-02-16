package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
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

	public static SortedMap getVariantsSortedInWeight(EnumSiteFeature feature) {
		return getVariantsSortedInWeight(feature, null);
	}

	public static SortedMap getVariantsSortedInWeight(EnumSiteFeature feature,
			Date date) {

		final VariantSelection vs = VariantSelection.getInstance();
		Map cohorts = vs.getCohorts();
		Map assignment = vs.getVariantMap(feature,
				date);
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
		return getVariantNamesSortedInUse(feature, null);
	}

	public static List getVariantNamesSortedInUse(EnumSiteFeature feature, Date date) {
		final VariantSelection vs = VariantSelection.getInstance();
		List variants = vs.getVariants(feature);
		final Map assignment = vs.getVariantMap(feature, date);

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
		return variants;
	}
	
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
}
