package com.freshdirect.webapp.taglib.coremetrics;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.WineFilterValue;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagInput;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.AbstractTagModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmPageViewTag extends AbstractCmTag {
	@SuppressWarnings("unused")
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
		tagModelBuilder.setInput(PageViewTagInput.populateFromRequest(getRequest()));
		PageViewTagModel tagModel = tagModelBuilder.buildTagModel();

		Object CM_VC = this.getRequest().getParameter("cm_vc");

		StringBuilder sb = new StringBuilder();
		sb.append(getFormattedTag( 
				toJsVar(tagModel.getPageId()), 
				toJsVar(tagModel.getCategoryId()), 
				toJsVar(tagModel.getSearchTerm()), 
				toJsVar(tagModel.getSearchResults()), 
				toJsVar(mapToAttrString(tagModel.getAttributesMaps()))));
		
		sb.append(getTagDelimiter());				
		sb.append(getFormattedTag(INIT_TRACKING_JS_PBJECT, new String[]{toJsVar(tagModel.getPageId()), toJsVar(getPackedPageLocationSubset(tagModel)), toJsVar(CM_VC == null ? "" : CM_VC)}));
		
		//LOGGER.debug(sb.toString());
		return sb.toString();
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
	
	protected boolean insertTagInCaseOfCrmContext(){
		return forceTagEffect;
	}
	
	public void setRecipeSource(String recipeSource) {
		tagModelBuilder.setRecipeSource(recipeSource);
	}
	public void setWineFilterValue(WineFilterValue wineFilterValue) {
		tagModelBuilder.setWineFilterValue(wineFilterValue);
	}

}
