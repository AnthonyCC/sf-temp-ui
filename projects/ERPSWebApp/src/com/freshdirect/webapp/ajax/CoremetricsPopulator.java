package com.freshdirect.webapp.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.ElementTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagInput;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.expresscheckout.coremetrics.service.CoremetricsService;

/**
 * 
 * @author segabor
 *
 */
public class CoremetricsPopulator {

    private static final Logger LOGGER = LoggerFactory.getInstance(CoremetricsPopulator.class);

    private ElementTagModelBuilder elementBuilder = new ElementTagModelBuilder();

    private PageViewTagModelBuilder tagModelBuilder = new PageViewTagModelBuilder();

    public static final String CM_KEY = "coremetrics";

    /**
     * This method is deprecated.
     * 
     * Use {@link CoremetricsPopulator#appendPageViewTag(Map, PageViewTagInput)} instead.
     * 
     * @param flatData
     * @param request
     * @throws SkipTagException
     */
    @Deprecated
    public void appendPageViewTag(Map<String, Object> flatData, HttpServletRequest request) throws SkipTagException {
        tagModelBuilder.setInput(PageViewTagInput.populateFromRequest(request));

        final List<String> cmResult = tagModelBuilder.buildTagModel().toStringList();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Appending page view tag to payload:  " + cmResult);
        }

        appendCMData(flatData, cmResult);
    }

    public void appendPageViewTag(Map<String, Object> flatData, final PageViewTagInput input, String searchTerm, String suggestedTerm, Integer searchResultsSize,
            Integer recipeSearchResultsSize, FDUserI user) throws SkipTagException {
        tagModelBuilder.setSearchTerm(searchTerm);
        tagModelBuilder.setSuggestedTerm(suggestedTerm);
        tagModelBuilder.setSearchResultsSize(searchResultsSize);
        tagModelBuilder.setRecipeSearchResultsSize(recipeSearchResultsSize);

        appendPageViewTag(flatData, input, user);
    }

    public void appendPageViewTag(Map<String, Object> flatData, final PageViewTagInput input, FDUserI user) throws SkipTagException {

        tagModelBuilder.setInput(input);
        tagModelBuilder.setUserCohort(user.getCohortName());
        tagModelBuilder.setCustomerType(CoremetricsService.defaultService().getCustomerTypeByOrderCount(user));

        final List<String> cmResult = tagModelBuilder.buildTagModel().toStringList();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Appending page view tag to payload:  " + cmResult);
        }

        appendCMData(flatData, cmResult);
    }

    public void appendFilterElementTag(Map<String, Object> flatData, Map<String, Object> filters, FDUserI user) throws SkipTagException {
        if (flatData == null || filters == null)
            return;

        elementBuilder.setUser(user);
        elementBuilder.setElementCategory(ElementTagModelBuilder.CAT_BROWSE_FILTER);
        elementBuilder.setLeftNavFilters(filters);

        final List<String> cmResult = elementBuilder.buildTagModel().toStringList();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Appending element tag to payload:  " + cmResult);
        }

        appendCMData(flatData, cmResult);
    }

    public void appendSortElementTag(Map<String, Object> flatData, String sortId, FDUserI user) throws SkipTagException {
        if (flatData == null || sortId == null)
            return;

        elementBuilder.setUser(user);
        elementBuilder.setElementCategory(ElementTagModelBuilder.CAT_BROWSE_SORT);
        elementBuilder.setBrowseSortId(sortId);

        final List<String> cmResult = elementBuilder.buildTagModel().toStringList();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Appending element tag to payload:  " + cmResult);
        }

        appendCMData(flatData, cmResult);
    }

    private void appendCMData(Map<String, Object> flatData, final List<String> cmData) {
        if (flatData == null || cmData == null)
            return;

        List<List<String>> cmValueList = (List<List<String>>) flatData.get(CM_KEY);

        if (!flatData.keySet().contains(CM_KEY)) {
            cmValueList = new ArrayList<List<String>>();
            flatData.put(CM_KEY, cmValueList);
        }

        cmValueList.add(cmData);
    }
}
