/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * 
 * @version $Revision$
 * @author $Author$
 */
public class GetNewProductsTagEI extends TagExtraInfo {

    /**
     * Return information about the scripting variables to be created.
     */
    public VariableInfo[] getVariableInfo(TagData data) {
        List variables = new ArrayList();
        addToList(variables, new VariableInfo(data.getAttributeString("searchResults"), "com.freshdirect.fdstore.content.FilteredSearchResults", true,
                VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("productList"), "java.util.List", true, VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("categorySet"), "java.util.Set", true, VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("categoryTree"), "com.freshdirect.fdstore.content.CategoryNodeTree", true,
                VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("filteredCategoryTreeName"), "com.freshdirect.fdstore.content.CategoryNodeTree", true,
                VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("brandSet"), "java.util.Set", true, VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("selectedCategories") != null ? data.getAttributeString("selectedCategories")
                : "__selectedCategories", "java.util.Set", true, VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("navigator"), "com.freshdirect.fdstore.util.NewProductsNavigator", true,
                VariableInfo.NESTED));
        addToList(variables, new VariableInfo(data.getAttributeString("showGroup"), "java.lang.Boolean", true,
                VariableInfo.NESTED));

        
        
        return (VariableInfo[]) variables.toArray(new VariableInfo[variables.size()]);

    }

    void addToList(List variables, VariableInfo i) {
        if (i.getVarName() != null) {
            variables.add(i);
        }
    }

}
