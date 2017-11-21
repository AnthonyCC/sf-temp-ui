package com.freshdirect.storeapi.content;

import java.text.DecimalFormat;
import java.util.Locale;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.ZonePriceModel;
import com.freshdirect.storeapi.util.ProductInfoUtil;

/**
 * This class is encapsulates a product with a sku and a pricing context, which is essential most of the price calculations happening on the site.
 * This object is intended to shared for a request, as it cache the FDProduct and FDProductInfo objects, so passing this around ensures that
 * unneeded cache lookup doesn't happens.
 *
 * @author zsombor
 *
 */
public class PriceCalculator {
	private final static java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

	private final static DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");

    final PricingContext ctx;
    final SkuModel skuModel;
    final ProductModel product;

    transient FDProductInfo productInfo;
    transient FDProduct fdProduct;
    transient ZonePriceInfoModel zonePriceInfoModel;
    final GroupPricingInfo groupPricingInfo;

    public PriceCalculator(PricingContext ctx, ProductModel product, SkuModel sku) {
        this.ctx = ctx;
        this.product = product;
        this.skuModel = sku;
        this.groupPricingInfo = this.initGroupPricingInfo();

    }

    public PriceCalculator(PricingContext ctx, ProductModel product) {
        this.ctx = ctx;
        this.product = product;
        this.skuModel = product.getDefaultSku(ctx);
        this.groupPricingInfo = this.initGroupPricingInfo();
    }

    public ProductModel getProductModel() {
        return product;
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
        if (fdProduct == null && skuModel != null) {
            fdProduct = FDCachedFactory.getProduct(getProductInfo());
        }
        return fdProduct;
    }

    public FDGroup getFDGroup() {
        try {
//            return skuModel != null ? getProductInfo().getGroup(this.getPricingContext().getZoneInfo().getSalesOrg(),this.getPricingContext().getZoneInfo().getDistributionChanel()) : null;
        	return skuModel != null ? getProductInfo().getGroup(this.getPricingContext().getZoneInfo()) : null;
        } catch (FDResourceException e) {
            return null;
        } catch (FDSkuNotFoundException e) {
            return null;
        }
    }

    public FDGroup getProductGroup() throws FDResourceException, FDSkuNotFoundException{
         return skuModel != null && getProductInfo()!=null ? getProductInfo().getGroup(this.getPricingContext().getZoneInfo()) : null;
    }

    public double getDefaultPriceValue() {
        try {
            if (skuModel == null)
                return 0.0;
            FDProductInfo productInfo = getProductInfo();
            if (productInfo == null)
                return 0.0;
            return getZonePriceInfoModel().getDefaultPrice();
        } catch (FDResourceException e) {
            throw new RuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new RuntimeException(e);
        }
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

	/**
	 * This method has been modified to return only regular deals percentage if this sku
	 * is part of a group scale else returns sku's highest deals percentage.
	 * @param productInfo
	 * @param user
	 * @return
	 */

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
                if (zonePriceInfo!=null && zonePriceInfo.isItemOnSale()) {
                    return zonePriceInfo.getDealPercentage();
                }
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }




    /**
     * This method returns only regular deals percentage if product's default
     * sku is part of a group scale else returns product's highest deals
     * percentage.
     *
     * @param calculator
     * @return
     */
    public int getBurstDealsPercentage(double[] exception) {
        int tieredPercentage = getTieredDealPercentage(exception);
        FDGroup group = getFDGroup();
        if (tieredPercentage > 0 && group != null) {
            // Check to see current pricing zone has group price defined.
            if (group != null) {
                try {
                    MaterialPrice gsPrice = GroupScaleUtil.getGroupScalePrice(group, ctx.getZoneInfo());
                    if (gsPrice != null) {
                        // return regular deal percentage
                        return getDealPercentage();
                    }
                } catch (FDResourceException e) {
                    // ignore ...
                }
            }
        }
        // At this point there is no gs price defined for default sku.
        // ... so we can get the 'filtered tiered deal percentage' or the maximum of the deal percentage,
        // we don't need call 'getHighestDealPercentage()' because we already know, that we don't have group scale price,
        return Math.max(tieredPercentage, getDealPercentage());
    }


    public int getTieredDealPercentage() {
        if (skuModel != null) {
            try {
            	ZonePriceInfoModel priceModel = getZonePriceInfoModel();
            	if(priceModel != null) {
            		return priceModel.getTieredDealPercentage();
            	}
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public int getTieredDealPercentage(double[] exclusion) {
        if (skuModel != null) {
            try {
                if (exclusion == null || exclusion.length == 0) {
                    return getZonePriceInfoModel().getTieredDealPercentage();
                } else {
                    return (int) getZonePriceModel().getTieredDealPercentage(exclusion);
                }
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public int getHighestDealPercentage() {
        if (skuModel != null) {
            try {
                //return getZonePriceInfoModel().getHighestDealPercentage();
    			ZonePriceInfoModel priceModel = getZonePriceInfoModel();
    			if(priceModel != null) {
    				int tieredPercentage = priceModel.getTieredDealPercentage();
    				FDGroup group = productInfo.getGroup(this.getPricingContext().getZoneInfo());
    				//if(tieredPercentage > 0 && group != null){
    				//APPDEV-2414 - Take group price into account if exists when calculating highest deal percentage
    				if( (priceModel.isItemOnSale() || tieredPercentage > 0) && group != null){
    					//Check to see current pricing zone has group price defined.
    					if(group != null) {
    						MaterialPrice gsPrice = GroupScaleUtil.getGroupScalePrice(group, ctx.getZoneInfo());
    						if(gsPrice != null) {
    							//return regular deal percentage
    							//return priceModel.getDealPercentage();
    							double sellingPrice = priceModel.getSellingPrice();
    							int val = (int) ((sellingPrice - gsPrice.getPrice()) * 100.0 / sellingPrice + 0.2);
    							if( ((val%5)==0)||((val%2)==0)) {
    								return val;
    							}
    							return val-1;
    						}
    					}
    				}
    			}
    			//At this point there is no gs price defined for default sku.
    			return priceModel.getHighestDealPercentage();
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public int getGroupDealPercentage() {
        if (skuModel != null) {
            try {
                //return getZonePriceInfoModel().getHighestDealPercentage();
    			ZonePriceInfoModel priceModel = getZonePriceInfoModel();
    			if(priceModel != null) {
    				FDGroup group = productInfo.getGroup(this.getPricingContext().getZoneInfo());
   					if(group != null) {
   						MaterialPrice gsPrice = GroupScaleUtil.getGroupScalePrice(group, ctx.getZoneInfo());
   						if(gsPrice != null) {
   							double sellingPrice = priceModel.getSellingPrice();
   							int val = (int) ((sellingPrice - gsPrice.getPrice()) * 100.0 / sellingPrice + 0.2);
   							if( ((val%5)==0)||((val%2)==0)) {
   								return val;
   							}
   							return val-1;
   						}
   					}
   				}
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return 0;
    }

    public String getTieredPrice(double savingsPercentage) {
    	return getTieredPrice(savingsPercentage, null);
    }


    public String getTieredPrice(double savingsPercentage, double exclusion[]) {

        if (skuModel != null) {
            try {
                FDProduct product = getProduct();
                if (product != null) {
                    String[] tieredPricing = null;

                    if (savingsPercentage > 0) {
                        tieredPricing = product.getPricing().getZonePrice(ctx.getZoneInfo()).getScaleDisplay(savingsPercentage, exclusion);
                    } else {
                        tieredPricing = product.getPricing().getZonePrice(ctx.getZoneInfo()).getScaleDisplay(exclusion);
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
                return "";
            } catch (FDSkuNotFoundException ex) {
            } catch (FDResourceException e) {
            }
        }
        return "";
    }

    public PricingContext getPricingContext() {
        return ctx;
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
                        ZonePriceModel zonePrice = fdProduct.getPricing().getZonePrice(ctx.getZoneInfo());
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
       /* if (zonePriceInfoModel == null) {
            FDProductInfo info = getProductInfo();
            zonePriceInfoModel = info != null ? info.getZonePriceInfo(ctx.getZoneInfo()) : null;
        }
        */

        ZoneInfo zone=ctx.getZoneInfo();
        if (zonePriceInfoModel == null) {
        	FDProductInfo info = getProductInfo();
        	if(info!=null) {
        		zonePriceInfoModel=info.getZonePriceInfo(zone);
        		/*while(zonePriceInfoModel==null && zone!=null) {
        			zone=zone.getParentZone();
        			zonePriceInfoModel=info.getZonePriceInfo(zone);
        		}*/
        	}
        }
        return zonePriceInfoModel;

    }


    public ZonePriceModel getZonePriceModel() throws FDResourceException, FDSkuNotFoundException {
    	ZoneInfo zone=ctx.getZoneInfo();
    	//System.out.println("PriceCalculator.getZonePriceModel() =>"+getProduct());
    	ZonePriceModel zpm=getProduct().getPricing().getZonePrice(zone);
    	return zpm;
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
        	//String plantID=getPlantID();
            String plantID=ProductInfoUtil.getPickingPlantId(getProductInfo());
            FDKosherInfo ki = pr.getKosherInfo(plantID);
            if (ki.hasKosherSymbol() && ki.getKosherSymbol().display()) {
                return ki.getKosherSymbol().getCode();
            } else {
                return "";
            }
        } catch (FDSkuNotFoundException fdsnfe) {
        }
        return "";
    }

	private String getPlantID() {
		return ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
	}

    public String getKosherType() throws FDResourceException {
        try {
            if (skuModel == null)
                return "";

            FDProduct pr = getProduct();
        	//String plantID=getPlantID();
            FDKosherInfo ki = pr.getKosherInfo(ProductInfoUtil.getPickingPlantId(getProductInfo()));
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
            String plantID=ProductInfoUtil.getPickingPlantId(getProductInfo());
            return pr.isKosherProduction(plantID);
        } catch (FDSkuNotFoundException fdsnfe) {
        }
        return false;
    }

    public int getKosherPriority() throws FDResourceException {
        try {
            if (skuModel == null)
                return 999;
            FDProduct pr = getProduct();
        	String plantID=ProductInfoUtil.getPickingPlantId(getProductInfo());
            FDKosherInfo ki = pr.getKosherInfo(plantID);
            return ki.getPriority();
        } catch (FDSkuNotFoundException fdsnfe) {
        }
        return 999;
    }

    public SkuModel getSkuModel() {
        return skuModel;
    }



	public boolean isOnSale() {
		try {
			FDProductInfo pInfo = getProductInfo();
			if ( pInfo == null ) {
				return false;
			}
			return pInfo.getZonePriceInfo(ctx.getZoneInfo()).isItemOnSale();
			/*ZonePriceInfoModel zpInfo=pInfo.getZonePriceInfo(ctx.getZoneInfo());
			return (zpInfo!=null) ?zpInfo.isItemOnSale():false;*/


		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}
	}

	public double getWasPrice() {
		try {
			ZonePriceInfoModel zpm = getProductInfo().getZonePriceInfo(ctx.getZoneInfo());
			if(zpm == null){
				throw new FDRuntimeException("ZonePriceModel is null");
			} else {
				return zpm.getSellingPrice();
			}
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}
	}

	public double getGroupPrice() {
		try{
			if(getProductGroup() == null) return 0.0;
			MaterialPrice grpMatPrice = GroupScaleUtil.getGroupScalePrice(getProductGroup(), ctx.getZoneInfo());
			if(grpMatPrice != null)
				return grpMatPrice.getPrice();
			else
				return 0.0;
		} catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}  catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}
	public double getGroupQuantity() {
		try{
			if(getProductGroup() == null) return 0.0;
			MaterialPrice grpMatPrice = GroupScaleUtil.getGroupScalePrice(getProductGroup(), ctx.getZoneInfo());
			if(grpMatPrice != null)
				return grpMatPrice.getScaleLowerBound();
			else
				return 0.0;
		}  catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}  catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public String getGroupScaleUnit() {
		try{
			if(getProductGroup() == null) return null;
			MaterialPrice grpMatPrice = GroupScaleUtil.getGroupScalePrice(getProductGroup(), ctx.getZoneInfo());
			if(grpMatPrice != null)
				return grpMatPrice.getScaleUnit();
			else
				return null;
		}catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}  catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public String getGroupPricingUnit() {
		try{
			if(getProductGroup() == null) return null;
			MaterialPrice grpMatPrice = GroupScaleUtil.getGroupScalePrice(getProductGroup(), ctx.getZoneInfo());
			if(grpMatPrice != null)
				return grpMatPrice.getPricingUnit();
			else
				return null;

		} catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}  catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public MaterialPrice[] getGroupScalePrices() {
		try {
			if (getProductGroup() == null)
				return null;
			MaterialPrice[] matPrices = GroupScaleUtil.getGroupScalePrices(getProductGroup(), ctx.getZoneInfo());
			return matPrices;
		}  catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}  catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
	}

	public GroupPricingInfo getGroupPricingInfo() {
		return groupPricingInfo;
	}

	public String getGroupLongOfferDescription() {

		if(getGroupPricingInfo() != null) {
			StringBuffer buf1 = new StringBuffer();
			buf1.append( "Any "+getGroupPricingInfo().getLongDescription() +" ");
			buf1.append( getGroupPricingInfo().getLowerBound() );

			if(getGroupPricingInfo().isLowerBoundUnitPlural()) {//Other than eaches append the /pricing unit for clarity.
				buf1.append(getGroupPricingInfo().getLowerBoundUnit()).append("s");
			}
			buf1.append( " for " );
			buf1.append( getGroupPricingInfo().getDisplayPrice());
			if(getGroupPricingInfo().isSaleUnitDiff()) {
				buf1.append("/").append(getGroupPricingInfo().getDisplayPriceUnit());
			}
			return buf1.toString();
		}
		return null;

	}

	public String getGroupShortOfferDescription() {

		if(getGroupPricingInfo() != null) {
			StringBuffer buf1 = new StringBuffer();
			buf1.append( "Any " );
			buf1.append( getGroupPricingInfo().getLowerBound() );
			if(getGroupPricingInfo().isLowerBoundUnitPlural()) {//Other than eaches append the /pricing unit for clarity.
				buf1.append(getGroupPricingInfo().getLowerBoundUnit()).append("s");
			}
			buf1.append( " " );
			buf1.append( getGroupPricingInfo().getShortDescription() );
			buf1.append( " for " );
			buf1.append( getGroupPricingInfo().getDisplayPrice() );
			if(getGroupPricingInfo().isSaleUnitDiff()) {
				buf1.append("/").append(getGroupPricingInfo().getDisplayPriceUnit());
			}
			return buf1.toString();

		}
		return null;
	}

	public String getGroupOfferDescriptionText() {
		if(getGroupPricingInfo() != null) {
			return ( "Any "+getGroupPricingInfo().getLongDescription() +" ");
		}
		return null;
	}

	public String getGroupOfferPriceText() {
		if(getGroupPricingInfo() != null) {
			StringBuffer buf1 = new StringBuffer();
			buf1.append( getGroupPricingInfo().getLowerBound() );

			if(getGroupPricingInfo().isLowerBoundUnitPlural()) {//Other than eaches append the /pricing unit for clarity.
				buf1.append(getGroupPricingInfo().getLowerBoundUnit()).append("s");
			}
			buf1.append( " for " );
			buf1.append( getGroupPricingInfo().getDisplayPrice());
			if(getGroupPricingInfo().isSaleUnitDiff()) {
				buf1.append("/").append(getGroupPricingInfo().getDisplayPriceUnit());
			}
			return buf1.toString();
		}
		return null;
	}

	public GroupPricingInfo initGroupPricingInfo() {

		try{
			if(getProductInfo() != null && getProductGroup() != null) {//Check if Group Discount is there
				GroupScalePricing grpPricing = GroupScaleUtil.lookupGroupPricing(getProductGroup());
				MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(getProductGroup(), ctx.getZoneInfo());
				if(grpPricing != null && matPrice != null) {
					double displayPrice = 0.0;
					boolean isSaleUnitDiff = false;
					if(matPrice.getPricingUnit().equals(matPrice.getScaleUnit())){
						if(matPrice.getPricingUnit().equals("EA"))
							displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
						else {
							//other than eaches.
							displayPrice = matPrice.getPrice();
							isSaleUnitDiff = true;
						}
					} else {
						displayPrice = matPrice.getPrice();
						isSaleUnitDiff = true;
					}
					return new GroupPricingInfo(grpPricing.getLongDesc()
																	, grpPricing.getShortDesc()
																	, FORMAT_QUANTITY.format( matPrice.getScaleLowerBound() )
																	, matPrice.getScaleUnit().toLowerCase()
																	, matPrice.getScaleUnit().equals("LB")
																	, currencyFormatter.format(displayPrice )
																	, matPrice.getPricingUnit().toLowerCase()
																	, isSaleUnitDiff);
				}
			}
		} catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}  catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}
		return null;
	}

	class GroupPricingInfo {
		String longDescription;
		String shortDescription;
		String lowerBound;
		String lowerBoundUnit;
		boolean lowerBoundUnitPlural;
		String displayPrice;
		String displayPriceUnit;
		boolean isSaleUnitDiff;

		public GroupPricingInfo(String longDescription,
				String shortDescription, String lowerBound,
				String lowerBoundUnit, boolean lowerBoundUnitPlural,
				String displayPrice, String displayPriceUnit,
				boolean isSaleUnitDiff) {
			super();
			this.longDescription = longDescription;
			this.shortDescription = shortDescription;
			this.lowerBound = lowerBound;
			this.lowerBoundUnit = lowerBoundUnit;
			this.lowerBoundUnitPlural = lowerBoundUnitPlural;
			this.displayPrice = displayPrice;
			this.displayPriceUnit = displayPriceUnit;
			this.isSaleUnitDiff = isSaleUnitDiff;
		}

		public String getLongDescription() {
			return longDescription;
		}
		public void setLongDescription(String longDescription) {
			this.longDescription = longDescription;
		}
		public String getShortDescription() {
			return shortDescription;
		}
		public void setShortDescription(String shortDescription) {
			this.shortDescription = shortDescription;
		}
		public String getLowerBound() {
			return lowerBound;
		}
		public void setLowerBound(String lowerBound) {
			this.lowerBound = lowerBound;
		}
		public String getLowerBoundUnit() {
			return lowerBoundUnit;
		}
		public void setLowerBoundUnit(String lowerBoundUnit) {
			this.lowerBoundUnit = lowerBoundUnit;
		}

		public boolean isLowerBoundUnitPlural() {
			return lowerBoundUnitPlural;
		}

		public void setLowerBoundUnitPlural(boolean lowerBoundUnitPlural) {
			this.lowerBoundUnitPlural = lowerBoundUnitPlural;
		}

		public String getDisplayPrice() {
			return displayPrice;
		}
		public void setDisplayPrice(String displayPrice) {
			this.displayPrice = displayPrice;
		}
		public String getDisplayPriceUnit() {
			return displayPriceUnit;
		}
		public void setDisplayPriceUnit(String displayPriceUnit) {
			this.displayPriceUnit = displayPriceUnit;
		}
		public boolean isSaleUnitDiff() {
			return isSaleUnitDiff;
		}
		public void setSaleUnitDiff(boolean isSaleUnitDiff) {
			this.isSaleUnitDiff = isSaleUnitDiff;
		}
	}

}
