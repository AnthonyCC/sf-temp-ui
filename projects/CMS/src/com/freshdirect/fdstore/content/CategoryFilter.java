package com.freshdirect.fdstore.content;

import com.freshdirect.fdstore.FDResourceException;

/**
 * Category Filter
 * 
 * @author zsombor, segabor
 * 
 */
public class CategoryFilter extends AbstractProductFilter {
    String  category;
    boolean recursive = true;

    public CategoryFilter(String category) {
        this.category = category;
    }

    private boolean checkCategory(CategoryModel model) {
        if (model.getContentKey().getId().equals(category)) {
            return true;
        }
        ContentNodeModel parentNode = model.getParentNode();
        if (parentNode instanceof CategoryModel) {
            return checkCategory( (CategoryModel) parentNode );
        }
        return false;
    }

    public boolean applyTest(ProductModel prod) throws FDResourceException {
        if (category != null) {
            if (recursive) {
                if (!checkCategory(prod.getPrimaryHome())) {
                    return false;
                }
            } else {
                if (!prod.getPrimaryHome().getContentKey().getId().equals(category)) {
                    return false;
                }
            }
        }
        return true;
    }
}
