package com.freshdirect.mobileapi.util;

import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.FilteredSearchResults;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;

/**
 * Utility class for sorting list of products. SortType specifies the possible sorting types for a product list.
 * This utility wraps current implementation of sorting products from Fresh Direct API
 * @author fgarcia
 *
 */
public class ProductModelSortUtil extends SortUtil<ProductModel> {

    public enum SortType {
        RELEVANCY("relevancy"), NAME("name"), PRICE("prce"), POPULARITY("pplr"), SALE("sale");

        String fdSortValue;
        
        
        SortType(String sortValue){
            fdSortValue = sortValue;
        }
        public static SortType valueFromString(String str) {
            SortType result = RELEVANCY;
            if (RELEVANCY.toString().equalsIgnoreCase(str)) {
                result = RELEVANCY;
            }
            if (NAME.toString().equalsIgnoreCase(str)) {
                result = NAME;
            }
            if (PRICE.toString().equalsIgnoreCase(str)) {
                result = PRICE;
            }
            if (POPULARITY.toString().equalsIgnoreCase(str)) {
                result = POPULARITY;
            }
            if (SALE.toString().equalsIgnoreCase(str)) {
                result = SALE;
            }
            return result;
        }

        public String getSortValue(){
            return fdSortValue;
        }
    }

    public ProductModelSortUtil(List<ProductModel> targetList, SortType sortType, CategoryNodeTree nodeTree) {
        super(targetList);

        SearchResults originalSearchResults = new SearchResults(targetList, null, false, "");

        FilteredSearchResults filterResults = new FilteredSearchResults("", originalSearchResults, null);
        SearchSortType sortMode = null;
        switch (sortType) {
        case RELEVANCY:
            sortMode = null;
            break;
        case NAME:
            sortMode = SearchSortType.BY_NAME;
            break;
        case POPULARITY:
            sortMode = SearchSortType.BY_POPULARITY;
            break;
        case PRICE:
            sortMode = SearchSortType.BY_PRICE;
            break;
        case SALE:
            sortMode = SearchSortType.BY_SALE;
            break;
        default:
            sortMode = null;
        }
        filterResults.setNodeTree(nodeTree);
        filterResults.sortProductsBy(sortMode, false);
        Comparator comp = filterResults.getCurrentComparator();

        this.setComparator(comp);

    }

}
