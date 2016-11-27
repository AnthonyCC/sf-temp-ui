package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ComponentGroupModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.mobileapi.exception.ModelException;

public class ComponentGroup {

    private static final Logger LOG = Logger.getLogger(ComponentGroup.class);

    protected List<Product> productList = new ArrayList<Product>();

    protected List<Variation> variations = new ArrayList<Variation>();

    protected String name;

    protected String description;

    protected boolean optionsDropDownVertical;

    protected Product parent;

    @SuppressWarnings("unchecked")
    public ComponentGroup(ComponentGroupModel cgm, Product productParent, FDUserI user, FDCartLineI cartLine, EnumCouponContext ctx) throws FDResourceException, FDSkuNotFoundException,
            ModelException {
        optionsDropDownVertical = cgm.isOptionsDropDownVertical();
        this.name = cgm.getContentKey().getId();
        this.description = cgm.getFullName();

        List<String> matCharNames = cgm.getCharacteristicNames();
        for (String matCharName : matCharNames) {
        	FDVariationOption[] varOpts = (FDVariationOption[]) cgm.getVariationOptions().get(matCharName);
            if (varOpts != null) {
                for (FDVariationOption varOpt : varOpts) {
                    String optSkuCode = varOpt.getSkuCode();
                    if (optSkuCode != null) {
                        try {
                            ProductModel pm = ContentFactory.getInstance().getProduct(optSkuCode);
                            if (!pm.isUnavailable()) {
                                productList.add(Product.wrap(pm, user, cartLine, ctx));
                            }
                        } catch (FDSkuNotFoundException e) {
                            LOG.warn("Could not get product with sku:" + optSkuCode + "::desc="
                                    + varOpt.getDescription(), e);
                        }
                    }
                }

                List<Variation> parentVariations = productParent.getVariations();
                for (Variation parentVariation : parentVariations) {
                    if (matCharName.equals(parentVariation.getName())) {
                        variations.add(parentVariation);
                    }
                }
            }
        }
        
        
        
        //This is specific for Great Additions
        if(this.getDescription() != null && this.getDescription().contains("Great Additions") && cgm.getOptionalProducts() != null){
        	for(Object p : cgm.getAvailableOptionalProducts()){
        		ProductModel pm = (ProductModel)p;
        		productList.add(Product.wrap(pm, user, cartLine, ctx));
        	}
        }
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOptionsDropDownVertical() {
        return optionsDropDownVertical;
    }

    public void setOptionsDropDownVertical(boolean optionsDropDownVertical) {
        this.optionsDropDownVertical = optionsDropDownVertical;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    public void setVariations(List<Variation> variations) {
        this.variations = variations;
    }

}
