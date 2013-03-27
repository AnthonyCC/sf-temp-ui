package com.freshdirect.fdstore.coremetrics.builder;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;

public class PageViewTagModelBuilder  {
	
	/** enum for all category ids not listed in FDStoreProperties - used in CDF generation too */
	public enum CustomCategory {
		SEARCH, SO_TEMPLATE, ACCOUNT, BUYING_GUIDES, CART, ERROR, HOMEPAGE, INVITE, POPUPS, RECIPE, NEW_PRODUCTS_DEPARTMENT
	}
	
	private static final String PAGE_ID_DELIMITER = ": ";
	private static final String INDEX_FILE = "index.jsp";
	private static final int INDEX_FILE_SUFFIX_LENGTH = INDEX_FILE.length();
	

	private HttpServletRequest request;
	private Integer searchResultsSize;
	private String searchTerm;
	private String suggestedTerm;
	private Integer recipeSearchResultsSize;
	private ProductModel productModel;
	private ContentNodeModel currentFolder;
	private String recipeSource;
	private PageViewTagModel tagModel = new PageViewTagModel();
	
	public PageViewTagModel buildTagModel() throws SkipTagException{
		
		identifyPageAndCategoryId();
		identifyAttributes();
		return tagModel;
	}
	
	private void identifyPageAndCategoryId() throws SkipTagException{
		//uri always starts with a slash	
		String uriAfterSlash = request.getRequestURI().substring(1);
		int slashAfterDirNamePos = uriAfterSlash.indexOf("/");

		//uri has a directory name
		if (slashAfterDirNamePos>-1){ 
			String dirName = uriAfterSlash.substring(0, slashAfterDirNamePos);
			if (FDStoreProperties.getCoremetricsCatIdDirs().contains(dirName)){
				tagModel.setCategoryId(dirName);
				tagModel.setPageId(uriAfterSlash.substring(slashAfterDirNamePos+1));
				processHelpDir();
				decoratePageIdWithCatId();
			}

		//uri has only a file name	
		} else {
			String fileName = TagModelUtil.dropExtension(uriAfterSlash);
			
			if ("search".equalsIgnoreCase(fileName)){
				processSearchAttributes();
				tagModel.setPageId("search");
				tagModel.setCategoryId(CustomCategory.SEARCH.toString());
				decoratePageIdWithCatId();
			
			} else if ("department".equals(fileName) || "department_cohort_match".equals(fileName) || "category".equals(fileName) || "newsletter".equals(fileName) || "whatsgood".equals(fileName)){
				processDeptOrCat();
			} else if ("product".equals(fileName)){
				processProduct();
			}
		} 
		
		//could not identify category from uri yet, try custom categorization rules 
		if (tagModel.getCategoryId()==null) {
			if (uriAfterSlash.contains("popup.jsp") || uriAfterSlash.contains("pop.jsp")){
				tagModel.setCategoryId(CustomCategory.POPUPS.toString());

			//recipe begin
			} else if (uriAfterSlash.contains("recipe.jsp")){
				tagModel.setCategoryId(CustomCategory.RECIPE.toString());

			} else if (uriAfterSlash.contains("recipe_print.jsp")){
				tagModel.setCategoryId(CustomCategory.RECIPE.toString());
				tagModel.setPageId("PRINT");

			} else if (uriAfterSlash.contains("recipe_search.jsp")){
				processSearchAttributes();
				tagModel.setCategoryId(CustomCategory.RECIPE.toString());
				tagModel.setPageId("SEARCH");
			
			} else if (uriAfterSlash.contains("recipe_dept.jsp") || uriAfterSlash.contains("recipe_cat.jsp") || uriAfterSlash.contains("recipe_subcat.jsp")){
				if (currentFolder==null) {
					throw new SkipTagException("currentFolder is null");
				} else {				
					tagModel.setCategoryId(CustomCategory.RECIPE.toString());
					tagModel.setPageId(currentFolder.getFullName());
				}

			} else if (uriAfterSlash.contains("recipe_source.jsp")){
				if (recipeSource==null) {
					throw new SkipTagException("recipeSource is null");
				} else {
					tagModel.setCategoryId(CustomCategory.RECIPE.toString());
					tagModel.setPageId(recipeSource);
				}
			//recipe end	
				
			} else if (uriAfterSlash.contains("invite")){
				tagModel.setCategoryId(CustomCategory.INVITE.toString());
				tagModel.setPageId("PERSONAL");

			} else if (uriAfterSlash.contains(INDEX_FILE)){
				tagModel.setCategoryId(CustomCategory.HOMEPAGE.toString());

				int uriPathLen = uriAfterSlash.length() - INDEX_FILE_SUFFIX_LENGTH - 1; //remove slash as well
				if (uriPathLen > 0){
					tagModel.setPageId(uriAfterSlash.substring(0, uriPathLen)); //use path without file name as page name
				}

			} else if (uriAfterSlash.contains("error.jsp") || uriAfterSlash.contains("unsupported.jsp")){
				tagModel.setCategoryId(CustomCategory.ERROR.toString());
			
			} else if (uriAfterSlash.contains("cart.jsp") || uriAfterSlash.contains("confirm.jsp") || uriAfterSlash.contains("quickbuy")
					|| uriAfterSlash.contains("shop5.jsp") || uriAfterSlash.contains("product_modify.jsp")){
				tagModel.setCategoryId(CustomCategory.CART.toString());
				
			} else if (uriAfterSlash.contains("cheese/101_") || uriAfterSlash.contains("coffee/coffee_")
					|| uriAfterSlash.contains("producers_map.jsp") || uriAfterSlash.contains("peakproduce.jsp")
					|| uriAfterSlash.contains("rating_ranking.jsp") || uriAfterSlash.contains("seasonal_guide.jsp")
					|| uriAfterSlash.contains("nutrition_info.jsp")){
				tagModel.setCategoryId(CustomCategory.BUYING_GUIDES.toString());
				
			//account begin
			} else if (uriAfterSlash.contains("main/account_details.jsp")){
				tagModel.setCategoryId(CustomCategory.ACCOUNT.toString());
				tagModel.setPageId("MAIN");

			} else if (uriAfterSlash.contains("logout.jsp")){
				tagModel.setCategoryId(CustomCategory.ACCOUNT.toString());
				tagModel.setPageId("LOG OUT");
			//account end
				
			} else if (uriAfterSlash.contains("newproducts.jsp")){
				tagModel.setCategoryId(CustomCategory.NEW_PRODUCTS_DEPARTMENT.toString());
				tagModel.setPageId("");
			}
			
			//fill page id if still empty
			if (tagModel.getPageId()==null){
				tagModel.setPageId(uriAfterSlash);
			}
			
			decoratePageIdWithCatId();
		}		
		
		//could not identify category from uri, fallback to other category
		if (tagModel.getCategoryId()==null) {
			tagModel.setCategoryId(FDStoreProperties.getCoremetricsCatIdOtherPage());
			tagModel.setPageId(uriAfterSlash);
			decoratePageIdWithCatId();
		} 
	}
	
	private void decoratePageIdWithCatId(){
		tagModel.setPageId(tagModel.getCategoryId().replace("_", " ").toUpperCase() + PAGE_ID_DELIMITER + tagModel.getPageId());
	}
	
	private void processHelpDir() throws SkipTagException{
		if ("help".equals(tagModel.getCategoryId())){
			String pageParam = request.getParameter("page");
			if (pageParam != null){
				tagModel.setPageId(pageParam);

			} else if(tagModel.getPageId().contains("faq_search")){
				processSearchAttributes();
				tagModel.setPageId("faq_search");
			}
		}
	}

	private void processDeptOrCat() throws SkipTagException{
		if (currentFolder==null){
			throw new SkipTagException("currentFolder is null");
		} else {
			StringBuilder sb = new StringBuilder();
			getDeptOrCatPageId(currentFolder, sb);
			tagModel.setPageId(sb.toString());
			tagModel.setCategoryId(currentFolder.getContentKey().getId());
		}
	}
	
	private void getDeptOrCatPageId(ContentNodeModel curFolder, StringBuilder sb){
		if (curFolder != null){
			if (curFolder instanceof DepartmentModel){
				sb.append(curFolder.getFullName());
	
			} else {
				getDeptOrCatPageId(curFolder.getParentNode(), sb);
				sb.append(PAGE_ID_DELIMITER).append(curFolder.getFullName());
			}
		}
	}

	private void processProduct() throws SkipTagException{
		if (productModel==null){
			throw new SkipTagException("productModel is null");
		} else {
			tagModel.setPageId("PRODUCT" + PAGE_ID_DELIMITER + productModel.getFullName() + " ("+ productModel.getContentKey().getId() +")");
			tagModel.setCategoryId(productModel.getParentId());
		}
	}

	
	/**
	 * Page view tags only fire for search pages if the searchResultsSize and searchTerm is set. 
	 * Therefore the page view tag placed at the top of all pages (which doesn't have these pieces of information yet) won't fire. 
	 * But the additional page view tags placed in the search and faq_search pages will.
	 * @throws CmTagMissingAttributesException 
	 */
	private void processSearchAttributes() throws SkipTagException{
		if (searchResultsSize == null || searchTerm == null){
			throw new SkipTagException("searchResultsSize or searchTerm is null");
		
		} else {
			tagModel.setSearchTerm(searchTerm);
			tagModel.setSearchResults(searchResultsSize.toString());
		}
	}
	
	private void identifyAttributes(){
		if (suggestedTerm != null) {
			tagModel.getAttributesMaps().put(1, suggestedTerm);
		}
		if (recipeSearchResultsSize != null) {
			tagModel.getAttributesMaps().put(2, recipeSearchResultsSize.toString());
		}
	}

	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSearchResultsSize(Integer searchResultsSize) {
		this.searchResultsSize = searchResultsSize;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public void setSuggestedTerm(String suggestedTerm) {
		this.suggestedTerm = suggestedTerm;
	}

	public void setRecipeSearchResultsSize(Integer recipeSearchResultsSize) {
		this.recipeSearchResultsSize = recipeSearchResultsSize;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}

	public void setCurrentFolder(ContentNodeModel currentFolder) {
		this.currentFolder = currentFolder;
	}
	
	public void setRecipeSource(String recipeSource) {
		this.recipeSource = recipeSource;
	}

}