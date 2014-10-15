package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class StoreModel extends ContentNodeModelImpl {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7256497583339960753L;

	private List<DepartmentModel> departments = new ArrayList<DepartmentModel>();
	private List<BrandModel> brands;
	private List<Domain> domains;
	private List<MyFD> myfds = new ArrayList<MyFD>();
	private List<PageModel> pages = new ArrayList<PageModel>();
	private List<SortOptionModel> searchPageSortOptions =  new ArrayList<SortOptionModel>();
	private List<SortOptionModel> newProductsPageSortOptions =  new ArrayList<SortOptionModel>();	
	private List<SortOptionModel> presidentsPicksPageSortOptions =  new ArrayList<SortOptionModel>();
	private List<SortOptionModel> eCouponsPageSortOptions =  new ArrayList<SortOptionModel>();
	private List<CategoryModel> tabletFeaturedCategories = new ArrayList<CategoryModel>();
	private List<SearchSuggestionGroupModel> tabletSearchSuggestionGroups = new ArrayList<SearchSuggestionGroupModel>(); 
	private List<CategoryModel> tabletIdeasFeaturedPicksLists = new ArrayList<CategoryModel>();
	private List<RecipeTagModel> tabletIdeasRecipeTags = new ArrayList<RecipeTagModel>();
	
	public StoreModel(com.freshdirect.cms.ContentKey cKey) {
		super(cKey);
	}

	public int getVersion() {
		return 100;
	}

	public String getName() {
		return this.getAttribute("FULL_NAME", "FreshDirect");
	}

	public List<DepartmentModel> getDepartments() {
		ContentNodeModelUtil.refreshModels(this, "departments", departments, true);
		return new ArrayList<DepartmentModel>(departments);
	}

	public Set<DepartmentModel> getSortedDepartments(Comparator<DepartmentModel> comp) {
		ContentNodeModelUtil.refreshModels(this, "departments", departments, true);
		Set<DepartmentModel> result = new TreeSet<DepartmentModel>(comp);
		result.addAll(departments);
		return result;
	}

	public List<BrandModel> getBrands() {
		return new ArrayList<BrandModel>(brands);
	}

	public void setBrands(List<BrandModel> brands) {
		this.brands = brands;
	}

	public List<Domain> getDomains() {
		return new ArrayList<Domain>(domains);
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	public MyFD getMyFD() {
		ContentNodeModelUtil.refreshModels(this, "myFD", myfds, true);
		if (myfds.size() > 0)
			return myfds.get(0);
		else
			return null;
	}
	
	public Html getEditorial(){
		return FDAttributeFactory.constructHtml(this, "EDITORIAL");
	}
	
	public List<PageModel> getPages() {
		ContentNodeModelUtil.refreshModels(this, "pages", pages, true);
		return new ArrayList<PageModel>( pages );
	}

	public List<CategoryModel> getTabletFeaturedCategories() {
		ContentNodeModelUtil.refreshModels(this, "tabletFeaturedCategories", tabletFeaturedCategories, false);
		return new ArrayList<CategoryModel>( tabletFeaturedCategories );
	}

	public List<SearchSuggestionGroupModel> getTabletSearchSuggestionGroups() {
		ContentNodeModelUtil.refreshModels(this, "tabletSearchSuggestionGroups", tabletSearchSuggestionGroups, false);
		return new ArrayList<SearchSuggestionGroupModel>( tabletSearchSuggestionGroups );
	}
    public List<SortOptionModel> getSearchPageSortOptions() {
        ContentNodeModelUtil.refreshModels(this, "searchPageSortOptions", searchPageSortOptions, false);
        return new ArrayList<SortOptionModel>(searchPageSortOptions);
    }

    public List<SortOptionModel> getNewProductsPageSortOptions() {
        ContentNodeModelUtil.refreshModels(this, "newProductsPageSortOptions", newProductsPageSortOptions, false);
        return new ArrayList<SortOptionModel>(newProductsPageSortOptions);
    }

    public List<SortOptionModel> getPresidentsPicksPageSortOptions() {
        ContentNodeModelUtil.refreshModels(this, "presidentsPicksPageSortOptions", presidentsPicksPageSortOptions, false);
        return new ArrayList<SortOptionModel>(presidentsPicksPageSortOptions);
    }

    public List<SortOptionModel> getECouponsPageSortOptions() {
        ContentNodeModelUtil.refreshModels(this, "eCouponsPageSortOptions", eCouponsPageSortOptions, false);
        return new ArrayList<SortOptionModel>(eCouponsPageSortOptions);
    }
    
	public Html getEcouponsPageTopMediaBanner() {
		return FDAttributeFactory.constructHtml(this, "eCouponsPageTopMediaBanner");
	}
	
	public Html getSearchPageTopMediaBanner() {
		return FDAttributeFactory.constructHtml(this, "searchPageTopMediaBanner");
	}
	
	public Html getNewProductsPageTopMediaBanner() {
		return FDAttributeFactory.constructHtml(this, "newProductsPageTopMediaBanner");
	}
	
	public Html getPresidentPicksPageTopMediaBanner() {
		return FDAttributeFactory.constructHtml(this, "presPicksPageTopMediaBanner");
	}
	
	public BannerModel getTabletIdeasBanner() {
		return FDAttributeFactory.lookup(this, "tabletIdeasBanner", null);
	}
	
	public List<CategoryModel> getTabletIdeasFeaturedPicksLists() {
		ContentNodeModelUtil.refreshModels(this, "tabletIdeasFeaturedPicksLists", tabletIdeasFeaturedPicksLists, false);
		return new ArrayList<CategoryModel>( tabletIdeasFeaturedPicksLists );
	}
	
	public List<RecipeTagModel> getTabletIdeasRecipeTags() {
		ContentNodeModelUtil.refreshModels(this, "tabletIdeasRecipeTags", tabletIdeasRecipeTags, false);
		return new ArrayList<RecipeTagModel>( tabletIdeasRecipeTags );
	}
}
