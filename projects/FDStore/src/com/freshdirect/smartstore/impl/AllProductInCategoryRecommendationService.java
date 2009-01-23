/**
 * 
 */
package com.freshdirect.smartstore.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.ProductStatisticsProvider;

/**
 * @author zsombor
 *
 */
public class AllProductInCategoryRecommendationService extends AbstractRecommendationService {

    
    
    public AllProductInCategoryRecommendationService(Variant variant) {
        super(variant);
    }

    /* (non-Javadoc)
     * @see com.freshdirect.smartstore.impl.AbstractRecommendationService#recommendNodes(int, com.freshdirect.smartstore.SessionInput)
     */
    public List recommendNodes(SessionInput input) {
        List result = Collections.EMPTY_LIST;
        if (input.getCurrentNode() != null) {
            ContentNodeModel model = input.getCurrentNode();
            if (model instanceof CategoryModel) {
                ProductStatisticsProvider statisticsProvider = ProductStatisticsProvider.getInstance();
                TreeSet ordered = new TreeSet();

                CategoryModel category = (CategoryModel) model;
                collectCategories(statisticsProvider, ordered, category);
                    
                result = new ArrayList(ordered.size());
                for (Iterator iter = ordered.iterator(); iter.hasNext();) {
                    result.add(((ProductScore) iter.next()).product); 
                }
            }
        }
        return result;
    }

    protected void collectCategories(ProductStatisticsProvider statisticsProvider, TreeSet ordered, CategoryModel category) {
        List products = category.getProducts();

        collectProducts(statisticsProvider,ordered, products  );
            
        for (Iterator iter=category.getSubcategories().iterator(); iter.hasNext();) {
            CategoryModel c = (CategoryModel) iter.next();
            collectCategories(statisticsProvider, ordered, c);
        }
    }

    protected void collectProducts(ProductStatisticsProvider statisticsProvider, TreeSet ordered,List products) {
        if ((products==null) || (products.size()==0)) {
            return;
        }
        for (Iterator iter = products.iterator();iter.hasNext();) {
            ProductModel product = (ProductModel) iter.next();
            collectProduct(statisticsProvider, ordered, product);
        }
    }

    protected void collectProduct(ProductStatisticsProvider statisticsProvider, TreeSet ordered, ProductModel product) {
        ordered.add(new ProductScore(statisticsProvider.getGlobalProductScore(product.getContentKey()),product));
    }

    static class ProductScore implements Comparable {
        float score;
        ProductModel product;
        
        public ProductScore(float score, ProductModel product) {
            this.score = score;
            this.product = product;
        }

        public int compareTo(Object o) {
            int result = -Float.compare(score, ((ProductScore)o).score);
            if (result==0) {
                return product.getFullName().compareTo(((ProductScore)o).product.getFullName());
            }
            return result;
        }
        
        public String toString() {
            return "productscore["+product.getContentKey().getId()+'/'+product.getFullName()+':'+score+']';
        }
    }
    
}
