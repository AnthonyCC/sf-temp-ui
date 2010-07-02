package com.freshdirect.fdstore.content;

import java.util.Locale;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceModel;

public class PriceCalculator {

	private final static java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

    PricingContext ctx;
    SkuModel skuModel;
    ProductModel product;

    transient FDProductInfo productInfo;
    transient FDProduct fdProduct;
    transient ZonePriceInfoModel zonePriceInfoModel;
    
    public PriceCalculator(PricingContext ctx, ProductModel product, SkuModel sku) {
        this.ctx = ctx;
        this.product = product;
        this.skuModel = sku;
    }

    public PriceCalculator(PricingContext ctx, ProductModel product) {
        this.ctx = ctx;
        this.product = product;
        this.skuModel = product.getDefaultSku(ctx);
    }

    
    /**
     * @return
     * @throws FDResourceException
     * @throws FDSkuNotFoundException
     */
    public FDProductInfo getProductInfo() throws FDResourceException, FDSkuNotFoundException {
        if (skuModel != null) { 
            if (productInfo == null) {
                productInfo = FDCachedFactory.getProductInfo(skuModel.getSkuCode());
            }
        }
        return productInfo;
    }

    
    public FDProduct getProduct() throws FDResourceException, FDSkuNotFoundException {
        if (fdProduct == null) {
            fdProduct = FDCachedFactory.getProduct(getProductInfo());
        }
        return fdProduct;
    }
    
    
    
    public String getDefaultPrice() {
        try {
            if (skuModel == null)
                return "";
            FDProductInfo productInfo = getProductInfo();
            if (productInfo == null)
                return "";
            return currencyFormatter.format(getZonePriceInfoModel().getDefaultPrice()) + "/" + productInfo.getDefaultPriceUnit();
        } catch (FDResourceException e) {
            throw new RuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDefaultPriceOnly() {
        try {
            if (skuModel == null)
                return "";
            FDProductInfo productInfo = getProductInfo();
            if (productInfo == null)
                return "";
            return currencyFormatter.format(getZonePriceInfoModel().getDefaultPrice());
        } catch (FDResourceException e) {
            throw new RuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    

    public String getSellingPriceOnly() {
        try {
            if (skuModel == null)
                return "";
            FDProductInfo productInfo = getProductInfo();
            if (productInfo == null)
                return "";
            return currencyFormatter.format(getZonePriceInfoModel().getSellingPrice());
        } catch (FDResourceException e) {
            throw new RuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public String getDefaultUnitOnly() {
        try {
            if (skuModel == null)
                return "";
            FDProductInfo productInfo = getProductInfo();
            if (productInfo == null)
                return "";
            return productInfo.getDefaultPriceUnit();
        } catch (FDResourceException e) {
            throw new RuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getDealPercentage() {
        if (skuModel != null) {
            try {
                ZonePriceInfoModel zonePriceInfo = getZonePriceInfoModel();
                if (zonePriceInfo.isItemOnSale()) {
                    return zonePriceInfo.getDealPercentage();
                }
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public int getTieredDealPercentage() {
        if (skuModel != null) {
            try {
                return getZonePriceInfoModel().getTieredDealPercentage();
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public int getHighestDealPercentage() {
        if (skuModel != null) {
            try {
                return getZonePriceInfoModel().getHighestDealPercentage();
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public String getTieredPrice(double savingsPercentage) {

        if (skuModel != null) {
            try {
                FDProduct product = getProduct();
                if (product != null) {
                    String[] tieredPricing = null;

                    if (savingsPercentage > 0) {
                        tieredPricing = product.getPricing().getZonePrice(ctx.getZoneId()).getScaleDisplay(savingsPercentage);
                    } else {
                        tieredPricing = product.getPricing().getZonePrice(ctx.getZoneId()).getScaleDisplay();
                    }

                    if (tieredPricing.length > 0) {
                        return tieredPricing[tieredPricing.length - 1];
                    }
                }
                return null;
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return null;
    }

    public double getPrice(double savingsPercentage) {
        if (skuModel != null) {
            try {
                FDProductInfo productInfo = getProductInfo();
                if (productInfo != null) {
                    double price;
                    ZonePriceInfoModel zonePriceInfo = getZonePriceInfoModel();
                    if (savingsPercentage > 0) {
                        price = zonePriceInfo.getDefaultPrice() * (1 - savingsPercentage);
                    } else {
                        price = zonePriceInfo.getDefaultPrice();
                    }

                    return price;
                }
                return 0;
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public String getPriceFormatted(double savingsPercentage) {
        if (skuModel != null) {
            try {
                FDProductInfo productInfo = getProductInfo();
                if (productInfo != null) {
                    double price;
                    ZonePriceInfoModel zonePriceInfo = getZonePriceInfoModel();
                    if (savingsPercentage > 0) {
                        price = zonePriceInfo.getDefaultPrice() * (1 - savingsPercentage);
                        // } else if (zonePriceInfo.isItemOnSale()) {
                        // price = zonePriceInfo.getDefaultPrice();
                    } else {
                        price = zonePriceInfo.getDefaultPrice();
                    }

                    return ProductModel.CURRENCY_FORMAT.format(price) + "/" + productInfo.getDisplayableDefaultPriceUnit().toLowerCase();
                }
                return null;
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return null;
    }

    public String getWasPriceFormatted(double savingsPercentage) {
        if (skuModel != null) {
            try {
                FDProductInfo productInfo = getProductInfo();
                if (productInfo != null) {
                    Double wasPrice = null;

                    if (savingsPercentage > 0.) {
                        wasPrice = getZonePriceInfoModel().getDefaultPrice();
                    } else if (getZonePriceInfoModel().isItemOnSale()) {
                        wasPrice = getZonePriceInfoModel().getSellingPrice();
                    }

                    if (wasPrice != null) {
						//Fix for Defect QC 407.	
                        return ProductModel.CURRENCY_FORMAT.format(wasPrice);
                    }
                }
                return null;
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return null;
    }

    public String getAboutPriceFormatted(double savingsPercentage) {

        if (skuModel != null) {
            try {
                FDProductInfo productInfo = getProductInfo();
                if (productInfo != null) {
                    String displayPriceString = null;
                    FDProduct fdProduct = getProduct();
                    if (fdProduct.getDisplaySalesUnits() != null && fdProduct.getDisplaySalesUnits().length > 0) {
                        FDSalesUnit fdSalesUnit = fdProduct.getDisplaySalesUnits()[0];
                        double salesUnitRatio = (double) fdSalesUnit.getDenominator() / (double) fdSalesUnit.getNumerator();
                        String alternateUnit = fdSalesUnit.getName();

                        if (savingsPercentage < 0.) {
                            savingsPercentage = 0;
                        }
                        ZonePriceModel zonePrice = fdProduct.getPricing().getZonePrice(ctx.getZoneId());
                        String[] scales = savingsPercentage > 0 ? zonePrice.getScaleDisplay(savingsPercentage) : zonePrice.getScaleDisplay();
                        double displayPrice = 0.;
                        if (scales != null && scales.length > 0) {
                            displayPrice = zonePrice.getMinPrice() * (1. - savingsPercentage) / salesUnitRatio;
                        } else {
                            displayPrice = getZonePriceInfoModel().getDefaultPrice() * (1. - savingsPercentage) / salesUnitRatio;
                        }
                        if (displayPrice > 0.) {
                            displayPriceString = "about " + ProductModel.CURRENCY_FORMAT.format(displayPrice) + "/" + alternateUnit.toLowerCase();
                        }
                    }
                    return displayPriceString;
                }
                return null;
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return null;
    }

    /**
     * @param productInfo
     * @return
     * @throws FDSkuNotFoundException 
     * @throws FDResourceException 
     * @throws FDSkuNotFoundException 
     * @throws FDResourceException 
     */
    public ZonePriceInfoModel getZonePriceInfoModel() throws FDResourceException, FDSkuNotFoundException {
        if (zonePriceInfoModel == null) {
            FDProductInfo info = getProductInfo();
            zonePriceInfoModel = info != null ? info.getZonePriceInfo(ctx.getZoneId()) : null;
        }
        return zonePriceInfoModel;
    }
    
    
    public ZonePriceModel getZonePriceModel() throws FDResourceException, FDSkuNotFoundException {
        return getProduct().getPricing().getZonePrice(ctx.getZoneId());
    }
    


    public String getSizeDescription() throws FDResourceException {
        try {
            if (skuModel == null)
                return null;

            FDProduct pr = getProduct();
            FDSalesUnit[] sus = pr.getSalesUnits();
            if (sus.length == 1) {
                String salesUnitDescr = sus[0].getDescription();

                // clean sales unit description
                if (salesUnitDescr != null) {
                    if (salesUnitDescr.indexOf("(") > -1) {
                        salesUnitDescr = salesUnitDescr.substring(0, salesUnitDescr.indexOf("("));
                    }
                    salesUnitDescr = salesUnitDescr.trim();
                    // empty descriptions, "nm" and "ea" should be ignored
                    if ((!"".equals(salesUnitDescr)) && (!"nm".equalsIgnoreCase(salesUnitDescr)) && (!"ea".equalsIgnoreCase(salesUnitDescr))) {
                        if (!product.getSellBySalesunit().equals("SALES_UNIT")) {
                            return salesUnitDescr;
                        }
                    }
                }

            }
        } catch (FDSkuNotFoundException ex) {
        }
        return null;
    }

    public String getKosherSymbol() throws FDResourceException {
        try {
            if (skuModel == null)
                return "";

            FDProduct pr = getProduct();
            FDKosherInfo ki = pr.getKosherInfo();
            if (ki.hasKosherSymbol() && ki.getKosherSymbol().display()) {
                return ki.getKosherSymbol().getCode();
            } else {
                return "";
            }
        } catch (FDSkuNotFoundException fdsnfe) {
        }
        return "";
    }

    public String getKosherType() throws FDResourceException {
        try {
            if (skuModel == null)
                return "";

            FDProduct pr = getProduct();
            FDKosherInfo ki = pr.getKosherInfo();
            if (ki.hasKosherType() && ki.getKosherType().display()) {
                return ki.getKosherType().getName();
            } else {
                return "";
            }
        } catch (FDSkuNotFoundException fdsnfe) {
        }
        return "";
    }

    public boolean isKosherProductionItem() throws FDResourceException {
        try {
            if (skuModel == null)
                return false;

            FDProduct pr = getProduct();
            FDKosherInfo ki = pr.getKosherInfo();
            return ki.isKosherProduction();
        } catch (FDSkuNotFoundException fdsnfe) {
        }
        return false;
    }

    public int getKosherPriority() throws FDResourceException {
        try {
            if (skuModel == null)
                return 999;

            FDProduct pr = getProduct();
            FDKosherInfo ki = pr.getKosherInfo();
            return ki.getPriority();
        } catch (FDSkuNotFoundException fdsnfe) {
        }
        return 999;
    }

    public SkuModel getSkuModel() {
        return skuModel;
    }
    
}
