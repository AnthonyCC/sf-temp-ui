package com.freshdirect.webapp.ajax.data;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.webapp.ajax.browse.data.CategoryData;

public class CMSModelToSoyDataConverter {

	public static CategoryData createCategoryData(CategoryModel cat){
		Image catImage = cat.getCategoryPhoto();
		// TODO alias check
		return new CategoryData(catImage == null ? null : catImage.getPath(), cat.getContentKey().getId(), cat.getFullName());
	}
	

}
