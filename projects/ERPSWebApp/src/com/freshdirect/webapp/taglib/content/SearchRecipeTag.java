package com.freshdirect.webapp.taglib.content;

import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.FilteringFlow;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringMenuItem;
import com.freshdirect.fdstore.content.FilteringValue;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class SearchRecipeTag extends BodyTagSupportEx {

	private static final long serialVersionUID = 5809709785695385027L;
	
	private String domainsId;
	private String itemsId;
	private String filteredItemCountId;
	protected FilteringNavigator nav;
	
	private SearchResults results;

	private final static Logger LOG = LoggerFactory.getInstance(FilteringFlow.class);
	
	private RecipeFilterImpl recipeFilter;

	@Override
	public int doStartTag() throws JspException {

		// Check required attributes
		if (nav == null || domainsId == null || itemsId == null || filteredItemCountId == null) {
			LOG.warn("FilteringFlow received null attributes, skipping...");
			return SKIP_BODY;
		}
		
		FilteringFlowResult result = getProductsFilter().doFlow(nav, null);

		// Put results into jsp page context
		pageContext.setAttribute(itemsId, result.getItems());
		pageContext.setAttribute(domainsId, result.getMenu());
		pageContext.setAttribute(filteredItemCountId, result.getItems().size());
		return EVAL_BODY_INCLUDE;
	}
	
	private RecipeFilterImpl getProductsFilter(){
		if(recipeFilter==null){
			recipeFilter = new RecipeFilterImpl(results, nav);
		}
		return recipeFilter;
		
	}

	public void setDomainsId(String domainsId) {
		this.domainsId = domainsId;
	}

	public String getDomainsId() {
		return domainsId;
	}

	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}

	public String getItemsId() {
		return itemsId;
	}

	public String getFilteredItemCountId() {
		return filteredItemCountId;
	}

	public void setFilteredItemCountId(String filteredItemCountId) {
		this.filteredItemCountId = filteredItemCountId;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}
	
	public void setResults(SearchResults results) {
		this.results = results;
	}

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { new VariableInfo(
					data.getAttributeString("domainsId"),
					Map.class.getName() + "<" + FilteringValue.class.getName() + "," + Map.class.getName() + "<" + String.class.getName() + "," + FilteringMenuItem.class.getName() + ">>",
					true,
					VariableInfo.NESTED), new VariableInfo(
					data.getAttributeString("itemsId"),
					List.class.getName(),
					true,
					VariableInfo.NESTED), new VariableInfo(
					data.getAttributeString("filteredItemCountId"),
					Integer.class.getName(),
					true,
					VariableInfo.NESTED) };
		}
	}

}
