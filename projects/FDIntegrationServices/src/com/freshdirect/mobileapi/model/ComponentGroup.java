package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ComponentGroupModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
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
    public ComponentGroup(ComponentGroupModel cgm, Product productParent) throws FDResourceException, FDSkuNotFoundException,
            ModelException {
        optionsDropDownVertical = cgm.isOptionsDropDownVertical();
        this.name = cgm.getContentKey().getId();
        this.description = cgm.getFullName();

        List<String> matCharNames = cgm.getCharacteristicNames();

        for (String matCharName : matCharNames) {
            FDVariationOption[] varOpts = (FDVariationOption[]) cgm.getVariationOptions().get(matCharName);
            if (varOpts != null) {
                for (FDVariationOption varOpt : varOpts) {
                    String optSkuCode = varOpt.getAttribute(EnumAttributeName.SKUCODE);
                    if (optSkuCode != null) {
                        try {
                            ProductModel pm = ContentFactory.getInstance().getProduct(optSkuCode);
                            productList.add(Product.wrap(pm));
                        } catch (FDSkuNotFoundException e) {
                            LOG.warn("Could not get product with sku:" + optSkuCode + "::desc=" + varOpt.getAttribute(EnumAttributeName.DESCRIPTION), e);
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
