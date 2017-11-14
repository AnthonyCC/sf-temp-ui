package com.freshdirect.webapp.taglib.coremetrics;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.PageViewTagInput;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.extradata.CoremetricsExtraData;
import com.freshdirect.fdstore.coremetrics.tagmodel.AbstractTagModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.fdstore.coremetrics.util.CoremetricsUtil;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.WineFilterValue;
import com.freshdirect.webapp.ajax.expresscheckout.coremetrics.service.CoremetricsService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CmPageViewTag extends AbstractCmTag {

    private static final Logger LOGGER = LoggerFactory.getInstance(CmPageViewTag.class);
    private static final String INIT_TRACKING_JS_PBJECT = "FreshDirect.Coremetrics.populateTrackingObject";

    private boolean forceTagEffect = false;

    private PageViewTagModelBuilder tagModelBuilder = new PageViewTagModelBuilder();

    @Override
    protected String getFunctionName() {
        return "cmCreatePageviewTag";
    }

    @Override
    public String getTagJs() throws SkipTagException {
        FDUserI user = (FDUserI) getSession().getAttribute(SessionName.USER);
        tagModelBuilder.setUserCohort(user.getCohortName());

        CoremetricsExtraData cmExtraData = new CoremetricsExtraData();
        cmExtraData.setCustomerType(CoremetricsUtil.defaultService().getCustomerTypeByOrderCount(user));
        cmExtraData.setCorporateUser(user.isCorporateUser());
        tagModelBuilder.setCoremetricsExtraData(cmExtraData);

        tagModelBuilder.setInput(PageViewTagInput.populateFromRequest(getRequest()));
        PageViewTagModel tagModel = tagModelBuilder.buildTagModel();

        String CM_VC = this.getRequest().getParameter("cm_vc");
        if (CM_VC == null || "".equals(CM_VC)) {
            // pick value of cm_vc param from referer URL
            CM_VC = extractParameterFromHeader(getRequest());
        }

        StringBuilder sb = new StringBuilder();
        sb.append(getFormattedTag(toJsVar(tagModel.getPageId()), toJsVar(tagModel.getCategoryId()), toJsVar(tagModel.getSearchTerm()), toJsVar(tagModel.getSearchResults()),
                toJsVar(mapToAttrString(tagModel.getAttributesMaps()))));

        sb.append(getTagDelimiter());
        sb.append(getFormattedTag(INIT_TRACKING_JS_PBJECT, new String[] { toJsVar(tagModel.getPageId()), toJsVar(getPackedPageLocationSubset(tagModel)),
                toJsVar(CM_VC == null ? "" : CM_VC) }));

        // LOGGER.debug(sb.toString());
        return sb.toString();
    }

    /**
     * Basic utility method that extracts 'cm_vc' parameter from the query part of referer URL
     * 
     * @param request
     * @return
     */
    private static String extractParameterFromHeader(final HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        try {
            URL refUrl = new URL(request.getHeader("referer"));

            final String query = refUrl.getQuery();
            if (query != null && query.contains("cm_vc=")) {
                String[] params = query.split("&");
                for (String p : params) {
                    if (p.startsWith("cm_vc=")) {
                        return p.split("=")[1];
                    }
                }
            }
        } catch (MalformedURLException exc) {
            LOGGER.debug("Failed to process referer URL " + request.getHeader("referer"));
        }
        return null;
    }

    private String getPackedPageLocationSubset(PageViewTagModel tagModel) {

        StringBuilder sb = new StringBuilder();
        sb.append(tagModel.getAttributesMaps().get(3));
        for (int i = 4; i < 7; i++) {
            sb.append(AbstractTagModel.ATTR_DELIMITER);
            sb.append(tagModel.getAttributesMaps().get(i) == null ? "" : tagModel.getAttributesMaps().get(i));
        }
        return sb.toString();

    }

    public void setSearchResultsSize(Integer searchResultsSize) {
        tagModelBuilder.setSearchResultsSize(searchResultsSize);
    }

    public void setSearchTerm(String searchTerm) {
        tagModelBuilder.setSearchTerm(searchTerm);
    }

    public void setSuggestedTerm(String suggestedTerm) {
        tagModelBuilder.setSuggestedTerm(suggestedTerm);
    }

    public void setRecipeSearchResultsSize(Integer recipeSearchResultsSize) {
        tagModelBuilder.setRecipeSearchResultsSize(recipeSearchResultsSize);
    }

    public void setProductModel(ProductModel productModel) {
        tagModelBuilder.setProductModel(productModel);
    }

    public void setCurrentFolder(ContentNodeModel currentFolder) {
        tagModelBuilder.setCurrentFolder(currentFolder);
    }

    public void setForceTagEffect(boolean forceTagEffect) {
        this.forceTagEffect = forceTagEffect;
    }

    @Override
    protected boolean insertTagInCaseOfCrmContext() {
        return forceTagEffect;
    }

    public void setRecipeSource(String recipeSource) {
        tagModelBuilder.setRecipeSource(recipeSource);
    }

    public void setWineFilterValue(WineFilterValue wineFilterValue) {
        tagModelBuilder.setWineFilterValue(wineFilterValue);
    }

}
