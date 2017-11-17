package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;

public abstract class AbstractProductItemFilter implements ProductItemFilterI {

    protected String id;
    protected String parentId;
    protected String name;
    protected boolean invert;

    protected AbstractProductItemFilter() {

    }

    /**
     * Convenience constructor
     *
     * @param model
     */
    public AbstractProductItemFilter(ProductFilterModel model, String parentId) {
        this(model.getContentName(), parentId, model.getName(), model.isInvert());
    }

    public AbstractProductItemFilter(String id, String parentId, String name) {
        this(id, parentId, name, false);
    }

    public AbstractProductItemFilter(String id, String parentId, String name, boolean invert) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.invert = invert;
    }

    @Override
    public List<FilteringProductItem> apply(Collection<FilteringProductItem> nodes) throws FDResourceException {

        List<FilteringProductItem> result = new ArrayList<FilteringProductItem>();

        for (FilteringProductItem item : nodes) {
            if (apply(item)) {
                result.add(item);
            }
        }

        return result;

    }

    @Override
    public abstract boolean apply(FilteringProductItem prod) throws FDResourceException;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isInvert() {
        return invert;
    }

    protected boolean invertChecker(boolean value) {
        if (invert) {
            return !value;
        }
        return value;
    }

}
