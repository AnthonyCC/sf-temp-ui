package com.freshdirect.fdstore.coremetrics.builder;

import javax.servlet.http.HttpServletRequest;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.coremetrics.tagmodel.PageViewTagModel;

public class PageViewTagModelBuilder  {
	
	/**
	 * enum for all category ids not listed in FDStoreProperties - used in CDF generation too
	 */
	public enum CustomCategory {
		SEARCH, SO_TEMPLATE
	}
	
	private static final String PAGE_ID_DELIMITER = ": ";

	private HttpServletRequest request;
	private Integer searchResultsSize;
	private String searchTerm;
	private String suggestedTerm;
	private Integer recipeSearchResultsSize;
	private ProductModel productModel;
	private ContentNodeModel currentFolder;
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
			
			} else if ("department".equals(fileName) || "category".equals(fileName) || "newsletter".equals(fileName) || "whatsgood".equals(fileName)){
				processDeptOrCat();
			} else if ("product".equals(fileName)){
				processProduct();
			}
		} 
		
		//could not identify category from uri
		if (tagModel.getCategoryId()==null) {
			tagModel.setCategoryId(FDStoreProperties.getCoremetricsCatIdOtherPage());
			tagModel.setPageId(uriAfterSlash);
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
}