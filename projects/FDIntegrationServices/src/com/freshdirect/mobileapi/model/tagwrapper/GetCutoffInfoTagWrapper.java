package com.freshdirect.mobileapi.model.tagwrapper;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.model.ResultBundle;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.WebEvent;
import com.freshdirect.mobileapi.model.tagwrapper.UtilsWrapper.UtilsExecutionWrapper;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.taglib.fdstore.CutoffInfo;
import com.freshdirect.webapp.taglib.fdstore.GetCutoffInfoTag;
import com.freshdirect.webapp.taglib.smartstore.GenericRecommendationsTag;
import com.freshdirect.webapp.taglib.smartstore.Impression;
import com.freshdirect.webapp.util.RecommendationsCache;

/**
 * @author Rob
 *
 */
public class GetCutoffInfoTagWrapper extends GetterTagWrapper implements RequestParamName, SessionParamName {

    public GetCutoffInfoTagWrapper(SessionUser user) {
        super(new GetCutoffInfoTag(), user);
    }

    /**
     * @param ymalSource
     * @param previousRecommendations
     * @param overriddenVariantID
     * @param webEvent
     * @return
     * @throws FDException
     */
    public CutoffInfo getCutoff() throws FDException {
        return (CutoffInfo) getResult();
    }

}
