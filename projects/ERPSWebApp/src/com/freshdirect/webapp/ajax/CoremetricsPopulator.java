package com.freshdirect.webapp.ajax;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.coremetrics.builder.ElementTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 * @author segabor
 *
 */
public class CoremetricsPopulator {
	private static final Logger LOGGER = LoggerFactory.getInstance( CoremetricsPopulator.class );
	
	
	private static ElementTagModelBuilder elementBuilder = new ElementTagModelBuilder();

	private static PageViewTagModelBuilder tagModelBuilder = new PageViewTagModelBuilder();


	public static final String CM_KEY = "coremetrics";

	public static void appendPageViewTag(Map<String,Object> flatData, HttpServletRequest request) throws SkipTagException {
		tagModelBuilder.setRequest(request);

		final List<String> cmResult = tagModelBuilder.buildTagModel().toStringList();
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Appending page view tag to payload:  " + cmResult);
		}
		
		appendCMData(flatData, cmResult );
	}
	

	public static void appendFilterElementTag(Map<String, Object> flatData,
			Map<String, Object> filters) throws SkipTagException {
		if (flatData == null || filters == null)
			return;

		elementBuilder.setElementCategory(ElementTagModelBuilder.CAT_BROWSE_FILTER);
		elementBuilder.setLeftNavFilters(filters);

		final List<String> cmResult = elementBuilder.buildTagModel().toStringList();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Appending element tag to payload:  " + cmResult);
		}

		appendCMData(flatData, cmResult );
	}
	
	public static void appendSortElementTag(Map<String, Object> flatData, String sortId) throws SkipTagException {
		if (flatData == null || sortId == null)
			return;

		elementBuilder.setElementCategory(ElementTagModelBuilder.CAT_BROWSE_SORT);
		elementBuilder.setBrowseSortId(sortId);

		final List<String> cmResult = elementBuilder.buildTagModel().toStringList();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Appending element tag to payload:  " + cmResult);
		}

		appendCMData(flatData, cmResult );
	}



	private static void appendCMData(Map<String,Object> flatData, final List<String> cmData) {
		if (flatData == null || cmData == null)
			return;
		
		List<List<String>> cmValueList = (List<List<String>>) flatData.get(CM_KEY);
		
		if (!flatData.keySet().contains(CM_KEY)) {
			cmValueList = new ArrayList<List<String>>();
			flatData.put(CM_KEY, cmValueList);
		}

		cmValueList.add( cmData );
	}
}
