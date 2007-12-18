/*
 * ProductRef.java
 *
 * Created on March 19, 2002, 10:42 AM
 */

package com.freshdirect.fdstore.content;

/**
 *
 * @author  rgayle
 * @version 
 */
public class SkuRef extends ContentRef {

    /** Creates new SkuRef */
    public SkuRef(String categoryId, String productId, String skuCode)  throws NullPointerException {
        super(ContentNodeI.TYPE_SKU);
        if (categoryId==null) throw new NullPointerException("Category Id cannot be null");
        if (productId==null) throw new NullPointerException("Product Id cannot be null");
        if (skuCode==null) throw new NullPointerException("Sku Code cannot be null");
        
        this.refName = categoryId;
        this.refName2 = productId;
        this.refName3 = skuCode;
    }
    
    public String getCategoryName() {
            return this.refName;
    }
    public String getProductName() {
          return this.refName2;
    }

    public String getSkuCode() {
          return this.refName3;
    }
    
    public CategoryModel getCategory() {
        return (CategoryModel)ContentFactory.getInstance().getContentNodeByName(this.refName);
    }

    public ProductModel getProduct() {
        return ContentFactory.getInstance().getProductByName(this.refName, this.refName2);
    }
    
    public SkuModel getSku() {
           ProductModel pm = this.getProduct();
           if (pm!=null) {
                return pm.getSku(this.refName3);
           }
           return null;
    }
    
}
