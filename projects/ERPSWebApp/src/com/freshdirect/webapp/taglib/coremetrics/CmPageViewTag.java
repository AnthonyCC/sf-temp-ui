package com.freshdirect.webapp.taglib.coremetrics;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.WineFilterValue;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmPageViewTag extends AbstractCmTag {
	private static final Logger LOGGER = LoggerFactory.getInstance(CmPageViewTag.class);
	private static final String PAGE_VIEW_TAG_FS = "cmCreatePageviewTag(%s,%s,%s,%s,%s);";
	
	private boolean forceTagEffect = false;

	private PageViewTagModelBuilder tagModelBuilder = new PageViewTagModelBuilder();

	@Override
	protected String getTagJs() throws SkipTagException {

		tagModelBuilder.setRequest((HttpServletRequest) ((PageContext) getJspContext()).getRequest());
		PageViewTagModel tagModel = tagModelBuilder.buildTagModel();

		String tagJs = String.format(PAGE_VIEW_TAG_FS, 
				toJsVar(tagModel.getPageId()), 
				toJsVar(tagModel.getCategoryId()), 
				toJsVar(tagModel.getSearchTerm()), 
				toJsVar(tagModel.getSearchResults()), 
				toJsVar(mapToAttrString(tagModel.getAttributesMaps())));
		
		LOGGER.debug(tagJs);
		return tagJs;
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
