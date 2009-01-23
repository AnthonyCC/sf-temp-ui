package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeModelImpl;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductModelImpl;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;

public class CandidateProductRecommendationService extends AllProductInCategoryRecommendationService {

    public CandidateProductRecommendationService(Variant variant) {
        super(variant);
    }

    public List recommendNodes(SessionInput input) {
        List result = Collections.EMPTY_LIST;
        if (input.getCurrentNode() != null) {
            ContentNodeModel model = input.getCurrentNode();
            if (model instanceof CategoryModel) {
                CategoryModel category = (CategoryModel) model;

                result = new ArrayList(100);
                collectNodes(category, result);
            }
        }
        return result;
    }

    protected List collectNodes(CategoryModel category, List result) {
        ProductStatisticsProvider statisticsProvider = ProductStatisticsProvider.getInstance();
        TreeSet resultSet = new TreeSet();
        List candidateList = category.getCandidateList();
        if (candidateList.size() == 0) {
            // fallback, default behaviour
            collectCategories(statisticsProvider, resultSet, category);
        } else {
            for (int i = 0; i < candidateList.size(); i++) {
                ContentNodeModelImpl candidate = (ContentNodeModelImpl) candidateList.get(i);
                if (candidate instanceof ProductModelImpl) {
                    collectProduct(statisticsProvider, resultSet, (ProductModel) candidate);
                } else if (candidate instanceof CategoryModel) {
                    collectCategories(statisticsProvider, resultSet, (CategoryModel) candidate);
                }
            }
        }
        Set manualSlots = result.isEmpty() ? Collections.EMPTY_SET : new HashSet(result);
        for (Iterator iter = resultSet.iterator(); iter.hasNext();) {
            ProductModel product = ((ProductScore) iter.next()).product;
            if (!manualSlots.contains(product)) {
                result.add(product);
            }
        }
        return result;
    }
}
