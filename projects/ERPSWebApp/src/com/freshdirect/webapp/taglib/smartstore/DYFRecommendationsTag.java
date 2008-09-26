package com.freshdirect.webapp.taglib.smartstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.DYFUtil;
import com.freshdirect.webapp.util.FDEventUtil;

/**
 * SmartStore
 * DYF Recommendations Tag
 * 
 * @author segabor
 *
 */
public class DYFRecommendationsTag extends AbstractGetterTag implements SessionName {

	private static final long serialVersionUID = -3790027913916829707L;
	
	private static Category LOGGER = LoggerFactory.getInstance(DYFRecommendationsTag.class);
	
	// maximum number of recommended items
	private int itemCount = 5;

	// skip checking user eligibility
	private boolean skipCheck = false;

	// if this set true tag should not recommend new. Instead, return the previous if any
	private boolean errorOccurred = false;
	
	public void setItemCount(int cnt) {
		this.itemCount = cnt;
	}

	public void setSkipCheck(boolean flag) {
		this.skipCheck = flag;
	}

	public void setErrorOccurred(boolean flag) {
		this.errorOccurred = flag;
	}

	/**
	 * Heart of tag can be found here.
	 * 
	 * @return List of <{@link Recommendation}>
	 */
	protected Object getResult() throws Exception {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute("fd.user");

		if (!skipCheck && !DYFUtil.isCustomerEligible(user)) {
			return null;
		}

		
		Recommendations results = null;
		boolean restored = false;
		
		if (errorOccurred && session.getAttribute("dyf.recommendation") != null) {
			// restore recommendation from session
			// Variant ID=>[content_key1, ...]
			String rstr = (String)session.getAttribute("dyf.recommendation");
			LOGGER.debug("Restore recommendation from string " + rstr);

			Pattern p = Pattern.compile("\"([^\"]+)\"\\s*=\\>\\[\\s*(\"\\w+\"(\\s*,\\s*\"\\w+\")*)\\s*\\]");
			Matcher m = p.matcher(rstr);
			
			if (m.matches()) {
				String variantId = m.group(1); // eg. Random DYF
				
				String cIds[] = Pattern.compile("\\s*,\\s*").split(m.group(2)); // "c1","c2", ...
				List products = new ArrayList();
				for (int k=0; k<cIds.length; k++) {
					ContentKey key = ContentKey.create(FDContentTypes.PRODUCT, cIds[k].substring(1, cIds[k].length()-1));
					products.add((ProductModel) ContentFactory.getInstance().getContentNodeByKey(key));
				}
				
				results = new Recommendations(new Variant(variantId, EnumSiteFeature.DYF,null), products);
				restored = true;
			}
		}

		

		// get recommendations by recommender
		if (results == null) {
			Trigger trigger = new Trigger(EnumSiteFeature.DYF, itemCount);
			FDStoreRecommender recommender = FDStoreRecommender.getInstance();
			
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			
			String overriddenVariantID = request.getParameter("DYF.VariantID"); 
			if (overriddenVariantID != null) session.setAttribute("DYF.VariantID", overriddenVariantID);
			
			results = recommender.getRecommendations(trigger, session);
			
			if (!restored) {
				// freeze recommendation
				StringBuffer frozen = new StringBuffer();
				frozen.append("\"" + results.getVariant().getId()+"\"");
				frozen.append("=>[");
				List products = results.getContentNodes();
				int psize = products.size();
				if (psize > 0) {
					for (int k=0; k<psize-1; k++) {
						frozen.append("\""+ ((ProductModel) products.get(k)).getContentKey().getId() + "\",");
					}
					frozen.append("\""+ ((ProductModel) products.get(psize-1)).getContentKey().getId() + "\"]");
				} else {
					frozen.append("]");
				}
				
				LOGGER.debug("Freezing " + frozen);
				
				session.setAttribute("dyf.recommendation", frozen.toString());
			}
		}

		if (results != null && results.getContentNodes().size() == 0) {
			results = null;
			
		} else {
			// do impression logging
			logImpressions(results);
		}

		return results;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.smartstore.fdstore.Recommendations";
		}
	}



	/**
	 * Generate event for impression logging
	 * 
	 * @param r Recommendation to log
	 * @author segabor
	 */
	private void logImpressions(Recommendations r) {
		Iterator it = r.getContentNodes().iterator();
		while (it.hasNext()) {
			ProductModel p = (ProductModel) it.next();
			FDEventUtil.logRecommendationImpression(r.getVariant().getId(), p.getContentKey());
		}
	}
}
