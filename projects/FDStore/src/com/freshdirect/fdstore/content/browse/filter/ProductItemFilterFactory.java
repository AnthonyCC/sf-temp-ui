package com.freshdirect.fdstore.content.browse.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.AndFilter;
import com.freshdirect.storeapi.content.BrandModel;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.OrFilter;
import com.freshdirect.storeapi.content.ProductFilterGroup;
import com.freshdirect.storeapi.content.ProductFilterGroupModel;
import com.freshdirect.storeapi.content.ProductFilterModel;
import com.freshdirect.storeapi.content.ProductFilterMultiGroupModel;
import com.freshdirect.storeapi.content.ProductFilterType;
import com.freshdirect.storeapi.content.ProductItemFilterI;
import com.freshdirect.storeapi.content.TagModel;

public class ProductItemFilterFactory {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductItemFilterFactory.class);
	
	private static final ProductItemFilterFactory INSTANCE = new ProductItemFilterFactory();
	
	public static ProductItemFilterFactory getInstance(){
		return INSTANCE;
	}

	private ProductItemFilterFactory() {

	}

	private static final ProductItemFilterI NULL_FILTER = new AbstractProductItemFilter(){
		@Override
		public boolean apply(FilteringProductItem prod) {
			return true;
		}
		
		@Override
		public FilterCacheStrategy getCacheStrategy() {
			return FilterCacheStrategy.CMS_ONLY;
		}
	};
	
	/**
	 * Transforms a model to a concrete filter implementation
	 * 
	 * @param filterModel A model object containing necessary filter configuration
	 * @param parentId
	 * @return Filter instance
	 */
	public ProductItemFilterI getProductFilter(ProductFilterModel filterModel, String parentId, FDUserI user){
		
		switch (ProductFilterType.toEnum(filterModel.getType())){
		
		case AND:
			return new AndFilter(filterModel.getContentName(), parentId, filterModel.getName(), filterModel.isInvert(), createInnerFilters(filterModel, user));
		
		case OR:
			return new OrFilter(filterModel.getContentName(), parentId, filterModel.getName(), filterModel.isInvert(), createInnerFilters(filterModel, user));
		
		case ALLERGEN:
			return new AllergenFilter(filterModel, parentId);
			
		case BACK_IN_STOCK:
			return new BackInStockFilter(filterModel, parentId);
			
            case GOING_OUT_OF_STOCK:
                return new GoingOutOfStockFilter(filterModel, parentId);

		case BRAND:
			return new BrandFilter(filterModel, parentId);
			
		case CLAIM:
			return new ErpsyClaimFilter(filterModel, parentId);
		
		case CUSTOMER_RATING:
			return new CustomerRatingFilter(filterModel, parentId);

		case DOMAIN_VALUE:
			return new DomainValueFilter(filterModel, parentId);

		case EXPERT_RATING:
			return new ExpertRatingFilter(filterModel, parentId);
			
		case FRESHNESS:
			if (FDStoreProperties.IsFreshnessGuaranteedEnabled()) {
				return new FreshnessFilter(filterModel, parentId);
			} else {
				return NULL_FILTER;
			}

		case KOSHER:
			return new KosherFilter(filterModel, parentId);
					
		case NEW:
			return new NewProductFilter(filterModel, parentId);

		case NUTRITION:
			return new NutritionFilter(filterModel, parentId);

		case ON_SALE:
			return new OnSaleFilter(filterModel, parentId);

		case ORGANIC:
			return new OrganicFilter(filterModel, parentId);

		case PRICE:
			return new PriceFilter(filterModel, parentId, user);
		
		case SUSTAINABILITY_RATING:
			return new SustainabilityRatingFilter(filterModel, parentId,user);
			
		case TAG:
			return new TagFilter(filterModel, parentId);


		default:
			LOGGER.error("type not found, returning always true filter");
			return NULL_FILTER;
		}		
	}

    public ProductItemFilterI getBrandFilter(BrandModel brandModel, String parentId) {
        return new BrandFilter(brandModel, parentId);
    }

    public List<ProductItemFilterI> getBrandFilters(Collection<BrandModel> brandModels, String parentId) {
        List<ProductItemFilterI> productFilters = new ArrayList<ProductItemFilterI>();
        for (BrandModel brandModel : brandModels) {
            productFilters.add(getBrandFilter(brandModel, parentId));
        }
        return productFilters;
    }
	
	private List<ProductItemFilterI> createInnerFilters(ProductFilterModel filterModel, FDUserI user){
		
		List<ProductItemFilterI> innerFilters = new ArrayList<ProductItemFilterI>();
		
		for(ProductFilterModel innerFilter : filterModel.getFilters()){
			innerFilters.add(getProductFilter(innerFilter, filterModel.getContentName(), user));
		}
		
		return innerFilters;
	}

    public ProductFilterGroup createProductFilterGroup(String id, String name, String type, String allSelectedLabel, List<ProductItemFilterI> productFilters,
            boolean displayOnCategoryListingPage, boolean multiGroupModel) {
        ProductFilterGroup group = new ProductFilterGroup();
        group.setProductFilters(productFilters);
        group.setId(id);
        group.setName(name);
        group.setType(type);
        group.setAllSelectedLabel(allSelectedLabel);
        group.setDisplayOnCategoryListingPage(displayOnCategoryListingPage);
        return group;
    }

	public ProductFilterGroup getProductFilterGroup(ProductFilterGroupModel groupModel, FDUserI user){
		List<ProductItemFilterI> productFilters = new ArrayList<ProductItemFilterI>();

		String contentName = groupModel.getContentName();
		for (ProductFilterModel filter : groupModel.getProductFilterModels()) {
			productFilters.add(getProductFilter(filter, contentName, user));
		}

		return createProductFilterGroup(groupModel.getContentName(), groupModel.getName(), groupModel.getType(), groupModel.getAllSelectedLabel(), productFilters, groupModel.isDisplayOnCategoryListingPage(), false);
	}
	
    public List<ProductFilterGroup> getProductFilterGroups(ProductFilterMultiGroupModel multiGroupModel, List<TagModel> selection) {
        List<ProductFilterGroup> list = new ArrayList<ProductFilterGroup>();

        // level 1
        String level1Id = multiGroupModel.getContentName() + "_l1";
        list.add(createProductFilterGroup(level1Id, multiGroupModel.getLevel1Name(), multiGroupModel.getLevel1Type(), multiGroupModel.getLevel1AllSelectedLabel(),
                getProductFilters(multiGroupModel.getRootTag(), level1Id), false, true));

        String l2Name = multiGroupModel.getLevel2Name();
        if (l2Name != null && l2Name.length() > 0) {
            // level 2
            String level2Id = multiGroupModel.getContentName() + "_l2";
            list.add(createProductFilterGroup(level2Id, l2Name, multiGroupModel.getLevel2Type(), multiGroupModel.getLevel2AllSelectedLabel(),
                    (selection == null || selection.size() < 1) ? Collections.<ProductItemFilterI> emptyList() : getProductFilters(selection.get(0), level2Id), false, true));
        }
        return list;
    }
	
	/**
	 * @param tag
	 * @param parentId
	 * @return
	 * 
	 * create tag filters from the given tagmodels
	 */
	public List<ProductItemFilterI> getProductFilters(TagModel tag, String parentId){
		List<ProductItemFilterI> list = new ArrayList<ProductItemFilterI>();
		
		if (tag!=null){
			List<TagModel> children = tag.getChildren();
			if (children != null){
				for (TagModel child : children){
					list.add(new TagFilter(child, parentId));
				}
			}
		}
		return list;
	}

}
