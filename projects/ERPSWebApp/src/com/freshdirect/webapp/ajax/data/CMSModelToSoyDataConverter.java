package com.freshdirect.webapp.ajax.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategorySectionModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.GlobalNavigationModel;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.SuperDepartmentModel;
import com.freshdirect.webapp.ajax.browse.data.CategoryData;
import com.freshdirect.webapp.ajax.filtering.NavigationUtil;
import com.freshdirect.webapp.globalnav.data.DepartmentData;
import com.freshdirect.webapp.globalnav.data.GlobalNavData;
import com.freshdirect.webapp.globalnav.data.SuperDepartmentData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;

public class CMSModelToSoyDataConverter {

	public static CategoryData createCategoryData(CategoryModel cat, FDSessionUser user, boolean extractPopularCategories){
		
		if (cat == null) return null;
		
		Image catImage = cat.getCategoryPhoto();
		Image globalNavPostNameImage = cat.getGlobalNavPostNameImage();
		
		List<CategoryData> popularCategories = null;
		if(extractPopularCategories){
			popularCategories = new ArrayList<CategoryData>();
			for (CategoryModel categoryModel : cat.getPopularCategories()) {
				if (NavigationUtil.isCategoryHiddenInContext(user, categoryModel)) {
					continue;
				}
				popularCategories.add(createCategoryData(categoryModel, user, false));
			}
		}
		String domains = FDStoreProperties.getSubdomains();
		return new CategoryData(catImage == null ? null : domains+catImage.getPath(), cat.getContentKey().getId(), cat.getFullName(), globalNavPostNameImage == null ? null : globalNavPostNameImage.getPath(), popularCategories);
	}
	
	private static void extractCategorySections(DepartmentData departmentData, List<CategorySectionModel> categorySectionList, FDSessionUser user) {
		List<Map<String, Object>> columnSection = new ArrayList<Map<String, Object>>();

		for (CategorySectionModel globalNavCategorySectionModel : categorySectionList) {
		
			Map<String, Object> categorySection = new HashMap<String, Object>();
			addHeadLineToCategorySection(globalNavCategorySectionModel.getHeadline(), categorySection, departmentData);
			
			List<CategoryData> sectionCategories = new ArrayList<CategoryData>();
			for (CategoryModel categoryModel : globalNavCategorySectionModel.getSelectedCategories()) {
				if (NavigationUtil.isCategoryHiddenInContext(user, categoryModel)) {
					continue;
				}
				sectionCategories.add(createCategoryData(categoryModel, user, true));
			}
			categorySection.put("categories", sectionCategories);
			
			columnSection.add(categorySection);
		}
		
		if (columnSection.size() > 0) {
			departmentData.addDropDownCategory(columnSection);
		}
		
	}
	
	public static DepartmentData createDepartmentData(DepartmentModel departmentModel, FDSessionUser user) {
		
		DepartmentData departmentData = new DepartmentData();
		
		departmentData.setId(departmentModel.getContentKey().getId());
		departmentData.setAltText(departmentModel.getAltText());
		departmentData.setHideDropDown(departmentModel.isHideGlobalNavDropDown());
		
		for (CategoryModel categoryModel : departmentModel.getCategories()) {
			if (NavigationUtil.isCategoryHiddenInContext(user, categoryModel)) {
				continue;
			}
			if (categoryModel.isPreferenceCategory()) {
				departmentData.addPreferenceCategoryData(createCategoryData(categoryModel, user, false));
			} else {
				departmentData.addCategoryData(createCategoryData(categoryModel, user, false));
			}
		}
		
		Collections.sort(departmentData.getCategories(), new Comparator<CategoryData>() {
			@Override
			public int compare(CategoryData o1, CategoryData o2) {
				if (o1.getName() == null) return -1;
				if (o2.getName() == null) return  1;
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		if (departmentModel.getCategorySections().size() > 0) {
			List<CategorySectionModel> categorySections = new ArrayList<CategorySectionModel>(); 
			for (CategorySectionModel categorySection : departmentModel.getCategorySections()) {
				categorySections.add(categorySection);
				if (categorySection.isLastSectionPerColumn()) {
					extractCategorySections(departmentData, categorySections, user);
					categorySections.clear();
				}
			}
			extractCategorySections(departmentData, categorySections, user);
		} else {
			Integer[][] ranges = generateRanges(departmentData.getCategories().size());
			
			for (int columnCount = 0; columnCount < ranges.length; columnCount++) {
				
				List<CategoryData> categoryColumn = new ArrayList<CategoryData>();
				for(int categoryCount = ranges[columnCount][0]; categoryCount < ranges[columnCount][1]; categoryCount++) {
					if (departmentData.getCategories().size() > categoryCount) {
						categoryColumn.add(departmentData.getCategories().get(categoryCount));
					}
				}
				List<Map<String, Object>> columnSection = new ArrayList<Map<String, Object>>();
				
				Map<String, Object> categorySection = new HashMap<String, Object>();
				if (departmentData.getDropDownCategories().size() == 0) {
					addHeadLineToCategorySection(departmentModel.getRegularCategoriesNavHeader(), categorySection, departmentData);
				} else {
					addHeadLineToCategorySection("", categorySection, departmentData);
				}
				categorySection.put("categories", categoryColumn);
				
				columnSection.add(categorySection);
				
				departmentData.addDropDownCategory(columnSection);
			}
		}
		
		final int max = FDStoreProperties.getPopularCategoriesMax();
		int counter=0;
		for (CategoryModel categoryModel : departmentModel.getPopularCategories()) {
			if (NavigationUtil.isCategoryHiddenInContext(user, categoryModel)) {
				continue;
			}
			counter++;
			if(counter<=max){
				departmentData.addPopularCategoryData(createCategoryData(categoryModel, user, false));				
			}else{
				break;
			}
		}

		if(departmentData.getPopularCategories().size() > 0){
			int baseHeight = departmentData.getPopularCategories().size() / 3;
			int residuum = departmentData.getPopularCategories().size() % 3;
			int pointer = 0;
			for (int i = 0; i < 3; i++) {
				int columnHeight = baseHeight + (residuum > 0 ? 1 : 0);
				residuum --;
				List<CategoryData> popularCategoryColumn = new ArrayList<CategoryData>();
				for (int j = pointer; j < pointer + columnHeight; j++) {
					popularCategoryColumn.add(departmentData.getPopularCategories().get(j));
				}
				departmentData.addPopularCategoryColumn(popularCategoryColumn);
				pointer += columnHeight;
			}
		}
		// APPDEV - 4207 Optimize the webpage changes to append the domain
		String domains = FDStoreProperties.getSubdomains();		
		Image heroImage = departmentModel.getHeroImage();
		Image deptPhoto = departmentModel.getPhoto();
		Html seasonalMedia = departmentModel.getSeasonalMedia();
		departmentData.setName(departmentModel.getFullName());
		departmentData.setHeroImage(heroImage == null ? "" : domains + heroImage.getPath());
		departmentData.setDeptPhoto(deptPhoto == null ? "" : domains + deptPhoto.getPath());
//		departmentData.setSeasonalMedia(seasonalMedia == null ? "" : MediaUtils.renderHtmlToString(seasonalMedia, user));
		departmentData.setSeasonalMedia((seasonalMedia== null || seasonalMedia.getPath() == null || "".equals(seasonalMedia.getPath())) ? "" : MediaUtils.renderHtmlToString(seasonalMedia, user));
		departmentData.setPreferenceCategoriesNavHeader(departmentModel.getPreferenceCategoriesNavHeader());
		
		return departmentData;
		
	}
	
	private static void addHeadLineToCategorySection(String headLine, Map<String, Object> categorySection, DepartmentData departmentData){
		if (headLine !=null && !"".equals(headLine)){
			departmentData.setHeaderTextUsed(true);
		}
		categorySection.put("headline", headLine);
	}
	
	private static Integer[][] generateRanges(int numberOfCategories) {
		
		Integer[][] result = null;
		
		if (numberOfCategories <= 8){
			result = new Integer[][]{{0, 8}};
		}
		
		// equally divide ranges, if it's odd then
		// add to the first range
		if (numberOfCategories > 8 && numberOfCategories <= 16) {
			double segment = Math.ceil((double)numberOfCategories / 2);
			result = new Integer[][]{{0, (int)segment}, {(int)segment, numberOfCategories}};
		}
		
		// fill up ranges continually
		if (numberOfCategories > 16 && numberOfCategories <= 24) {
			int segment = numberOfCategories % 8 > 0 ? numberOfCategories % 8 : 8;
			result = new Integer[][]{{0, 8}, {8, 16}, {16, 16 + segment}};
		}

		if (numberOfCategories > 24) {
		
			int div = numberOfCategories / 3;
		    int mod = numberOfCategories % 3;
		
		    if (mod == 0 || mod == 1){
		    	result = new Integer[][]{{0, 1 * div + mod}, {1 * div + mod, 2 * div + mod}, {2 * div + mod, 3 * div + mod}};
		    }
		
		    if (mod == 2){
		    	result = new Integer[][]{{0, 1 * div + 1}, {1 * div + 1, 2 * div + 2}, {2 * div + 2, 3 * div + mod}};
		    }
		}

		return result;
	}
	
	public static SuperDepartmentData createSuperDepartmentData(SuperDepartmentModel superDepartmentModel, FDSessionUser user) {
		
		SuperDepartmentData superDepartmentData = new SuperDepartmentData();

		superDepartmentData.setId(superDepartmentModel.getContentKey().getId());

		superDepartmentData.setName(superDepartmentModel.getName());
		
		for (DepartmentModel departmentModel : superDepartmentModel.getDepartments()) {
			superDepartmentData.addDepartment(createDepartmentData(departmentModel, user));
		}
		superDepartmentData.setName(superDepartmentModel.getName());
		superDepartmentData.setBrowseName(superDepartmentModel.getBrowseName());
		superDepartmentData.setHideDropDown(superDepartmentModel.isHideGlobalNavDropDown());
		
		return superDepartmentData;
		
	}

	public static GlobalNavData createGlobalNavData(GlobalNavigationModel globalNavigationModel, FDSessionUser user) throws FDResourceException {
		
		GlobalNavData globalNavData = new GlobalNavData();
		
		globalNavData.setId(globalNavigationModel.getContentKey().getId());
		
		Html media = globalNavigationModel.getMedia();
		globalNavData.setMedia(media == null ? "" : media.getPath());
		
		for (ContentNodeModel abstractDepartment : globalNavigationModel.getItems()) {
			
			if (abstractDepartment instanceof DepartmentModel) {
				globalNavData.addAbstractDepartment(createDepartmentData((DepartmentModel)abstractDepartment, user));
			} else if (abstractDepartment instanceof SuperDepartmentModel) {
				globalNavData.addAbstractDepartment(createSuperDepartmentData((SuperDepartmentModel)abstractDepartment, user));
			}
		}
		
		return globalNavData;
		
	}
}
