package com.freshdirect.webapp.taglib.smartstore;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * SmartStore YMAL Recommendations Tag
 * 
 * @author csongor
 * 
 */
public class YMALRecommendationsTag extends RecommendationsTag implements SessionName {
	private static final long serialVersionUID = 5976696010559642821L;
	
	private YmalSource source = null;
	
	public YMALRecommendationsTag() {
		super();
		itemCount = 6;
	}

    public void setSource(YmalSource source) {
		this.source = source;
	}

	protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
        Recommendations results = null;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        if (errorOccurred) {
        	String recsCachedId = request.getParameter("recs_cached_id");
			if (recsCachedId != null
					&& recsCachedId.equals(request.getSession().getAttribute("recs_cached_id"))) {
				results = (Recommendations) request.getSession().getAttribute("recs_cached");
			}
        }

        // get recommendations by recommender
        if (results == null) {
			results = extractRecommendations();
            request.getSession().setAttribute("recs_cached_id", results.getRequestId());
			request.getSession().setAttribute("recs_cached", results);
        }

        return results;
    }

    private Recommendations extractRecommendations() throws FDResourceException {
        Recommendations results;
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        // setup an input
        FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
        SessionInput inp = new SessionInput(user);
        initFromSession(inp);
        if (source != null) {
            inp.setYmalSource(source);
            if (source instanceof ProductModel) {
                inp.setCurrentNode(source);
            }
        } else {
            inp.setYmalSource(YmalUtil.resolveYmalSource(user, null, request));
        }

        if (inp.getCurrentNode() == null) {
            inp.setCurrentNode(YmalUtil.getSelectedCartLine(user).lookupProduct());
        }

        inp.setMaxRecommendations(itemCount);

        results = FDStoreRecommender.getInstance().getRecommendations(EnumSiteFeature.YMAL, user, inp);
        persistToSession(results);
        collectRequestId(request, results, user);
        return results;
    }

	public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.smartstore.fdstore.Recommendations";
        }
    }

}
