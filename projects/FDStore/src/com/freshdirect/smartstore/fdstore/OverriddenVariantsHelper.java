package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.service.VariantRegistry;

public class OverriddenVariantsHelper {

	public static final String PROFILE_KEY = "OverrideVariants";
	public static List SS_FEATURES;

	FDUserI	user;
	
	public OverriddenVariantsHelper(FDUserI user) {
		this.user = user;
	}


	/**
	 * Returns the 'raw' (unverified) list of overridden variants
	 *
	 * @return
	 * @throws FDResourceException
	 */
	public List getOverriddenVariants() {
		ArrayList list = new ArrayList();
		
		// prevent 'no identity' crash
		if (user == null || user.getIdentity() == null)
			return Collections.EMPTY_LIST;

		try {
			String value = user.getFDCustomer().getProfile().getAttribute(PROFILE_KEY);
			if (value != null) {
				StringTokenizer st = new StringTokenizer(value, ",");
				while (st.hasMoreTokens()) {
					String ov = st.nextToken().trim();
					if (!"".equalsIgnoreCase(ov))
						list.add(ov.trim());
				}
			}
		} catch (Exception exc) {}

		return list;
	}


	/**
	 * Stores the list of overridden variants in the form of "var1,var2,..."
	 * Be careful as it neither verify if variants are valid
	 * nor the length of resulted profile value which can be at most 255 characters.
	 * 
	 * @param variantsList
	 * @throws FDResourceException
	 */
	public void storeOverriddenVariants(List variantsList) throws FDResourceException {
		// prevent 'no identity' crash
		if (user == null || user.getIdentity() == null)
			return;

		ProfileModel profile = user.getFDCustomer().getProfile();
		
		if (variantsList == null || variantsList.size() == 0) {
			profile.removeAttribute(PROFILE_KEY);
		} else {
			StringBuffer buf = new StringBuffer();
			final int s = variantsList.size();
			for (int i=0; i<s; ++i) {
				buf.append( ((String) variantsList.get(i)).trim());
				if (i<s-1)
					buf.append(",");
			}
			profile.setAttribute(PROFILE_KEY, buf.toString());
		}
	}


	/***
	 * Returns the first valid variantID for the given site feature or null if not found any
	 * 
	 * @param feature
	 * @return variant ID
	 * 
	 * @throws FDResourceException
	 */
	public String getOverriddenVariant(EnumSiteFeature feature) throws FDResourceException {
		Collection<String> variants = VariantRegistry.getInstance().getServices(feature).keySet();
		
		for (Iterator it=getOverriddenVariants().iterator(); it.hasNext();) {
			String vID = (String) it.next();
			if (variants.contains(vID))
				return vID;
		}
		
		return null;
	}



	public static class VariantInfo {
		public String	variant = null;
		public boolean	exists = false; // variant exists
		public boolean	duplicate = false;
		public EnumSiteFeature 	feature = null;
		
		public VariantInfo(String variant) {
			this.variant = variant;
		}
		
		public boolean isValid() {
			return exists && !duplicate && feature != null;
		}
	}


	public static class VariantInfoList {
		List info;
		
		public VariantInfoList(List infoList) {
			this.info = infoList;
		}

		public VariantInfo get(EnumSiteFeature feature) {
			if (feature != null) {
				for (Iterator it=info.iterator(); it.hasNext();) {
					VariantInfo vi = (VariantInfo) it.next();
					if (feature.equals(vi.feature))
						return vi;
				}
			}
			return null;
		}
		
		public boolean hasEntries() {
			return info.size() > 0;
		}
		
		public Iterator iterator() {
			return info.iterator();
		}
	}



	public VariantInfoList consolidateVariantsList(List variantList) {
		ArrayList features = new ArrayList();
		HashMap featVarsMap = new HashMap();
		ArrayList variantInfoList = new ArrayList();
		
		// fill feature->variants map
		for (Iterator it=SS_FEATURES.iterator(); it.hasNext(); ) {
			EnumSiteFeature feature = (EnumSiteFeature) it.next();
			featVarsMap.put(feature, VariantSelection.getInstance().getVariants(feature));
		}

		for (Iterator it=variantList.iterator(); it.hasNext();) {
			String v = (String) it.next();
			
			VariantInfo vi = new VariantInfo(v);

			
			for (Iterator fit=SS_FEATURES.iterator(); fit.hasNext();) {
				EnumSiteFeature feature = (EnumSiteFeature) fit.next();
				
				if ( ((List)featVarsMap.get(feature)).contains(v)) {
					vi.feature = feature;
					vi.exists = true;
					vi.duplicate = features.contains(feature);
					
					if (!features.contains(feature))
						features.add(feature);
				}
			}
			
			variantInfoList.add(vi);
		}
		
		return new VariantInfoList(variantInfoList);
	}

	
	static {
		ArrayList ssList = new ArrayList();
		for (Iterator it = EnumSiteFeature.getEnumList().iterator(); it.hasNext();) {
			EnumSiteFeature feature = (EnumSiteFeature) it.next();
			if (feature.isSmartStore()) {
				ssList.add(feature);
			}
		}
		SS_FEATURES = ssList;
	}
}
