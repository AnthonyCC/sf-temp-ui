package com.freshdirect.webapp.ajax.browse.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.RecipeData;

public class SectionContext extends SectionData {

	private static final long serialVersionUID = 2762428822897828535L;
	private static final Logger LOGGER = LoggerFactory.getInstance( SectionContext.class );

	@JsonIgnore
	private List<FilteringProductItem> productItems;

	@JsonIgnore
	private List<FilteringProductItem> recipeItems;

	@JsonIgnore
	private List<SectionContext> sectionContexts;

	@JsonIgnore
	private boolean special;

    public SectionData extractDataFromContext(CmsFilteringNavigator nav) {
        if (sectionContexts != null) {
            List<SectionData> sections = new ArrayList<SectionData>();
            for (SectionContext context : sectionContexts) {
                sections.add(context.extractDataFromContext(nav));
            }
            this.setSections(sections);
        }

        if (productItems != null) {
            final boolean productPreviewMode = FDStoreProperties.getPreviewMode();

            List<ProductData> productDatas = new ArrayList<ProductData>();
            for (FilteringProductItem productItem : productItems) {
                ProductModel product = productItem.getProductModel();

                if (!productPreviewMode && (product.isDiscontinued() || product.isOutOfSeason())) {
                    continue;
                }

                productDatas.add(new ProductData(product));
            }
            this.setProducts(productDatas);
        }

        if (recipeItems != null && !nav.populateSectionsOnly()) {
            List<RecipeData> recipesData = new ArrayList<RecipeData>();
            for (FilteringProductItem recipeItem : recipeItems) {
                Recipe recipe = recipeItem.getRecipe();
                RecipeData recipeData = createRecipeData(recipe);
                recipesData.add(recipeData);
            }
            this.setRecipes(recipesData);
        }

        return this;

    }

	private RecipeData createRecipeData(Recipe recipe) {
		RecipeData result = new RecipeData();
		result.setContentName(recipe.getContentName());
		result.setImagePath(recipe.getPhoto());
		result.setFullName(recipe.getFullName());
		result.setRecipeSourceName(recipe.getSource());
		result.setRecipeAuthors(recipe.getSource());
		return result;
	}

	public List<FilteringProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(List<FilteringProductItem> productItems) {
		this.productItems = productItems;
	}

	public List<SectionContext> getSectionContexts() {
		return sectionContexts;
	}

	public void setSectionContexts(List<SectionContext> sectionContexts) {
		this.sectionContexts = sectionContexts;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public List<FilteringProductItem> getRecipeItems() {
		return recipeItems;
	}

	public void setRecipeItems(List<FilteringProductItem> recipeItems) {
		this.recipeItems = recipeItems;
	}

}
