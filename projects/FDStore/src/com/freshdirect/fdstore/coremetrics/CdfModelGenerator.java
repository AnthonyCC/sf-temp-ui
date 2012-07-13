package com.freshdirect.fdstore.coremetrics;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.PageViewTagModelBuilder.CustomCategory;

public class CdfModelGenerator {

	private List<CdfRowModel> cdfRowModels = new ArrayList<CdfRowModel>();
	
	public List<CdfRowModel> generateCdfModel(){

		for (String catIdDir : FDStoreProperties.getCoremetricsCatIdDirs().split(",")) {
			addCmPageViewTagCategory(catIdDir);
		}

		addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdBlog());
		addCmPageViewTagCategory(FDStoreProperties.getCoremetricsCatIdOtherPage());

		for (CustomCategory category : PageViewTagModelBuilder.CustomCategory.values()){
			addCmPageViewTagCategory(category.toString());
		}
		
		for (DepartmentModel dept : ContentFactory.getInstance().getStore().getDepartments()) {
			processCmsCategory(dept, null);
		}
		
		return cdfRowModels;
	}
	
	private void addCmPageViewTagCategory(String catId){
		cdfRowModels.add(new CdfRowModel(catId, "Category: " + catId, null));
	}
	
	private void processCmsCategory(ProductContainer cat, String parentCatId) {
		
		String catId = cat.getContentKey().getId();
		cdfRowModels.add(new CdfRowModel(catId, cat.getFullName(), parentCatId));
		
		List<CategoryModel> subCats = cat.getSubcategories();
		if ( subCats != null) {
			for (ProductContainer subCat : subCats) {
				processCmsCategory(subCat, catId);
			}
		}
	}
}
