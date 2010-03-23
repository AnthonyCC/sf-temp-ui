package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public abstract class AbstractProductModelImpl extends ContentNodeModelImpl implements ProductModel {

	protected final static Image IMAGE_BLANK = new Image("/media_stat/images/layout/clear.gif", 1, 1);

	private List alsoSoldAs = new ArrayList();
	private List alsoSoldAsList = new ArrayList();
	private List<ProductModel> alsoSoldAsRefs = new ArrayList();

	public AbstractProductModelImpl(ContentKey key) {
		super(key);
	}

	//
	// common implementation between Product and ConfiguredProduct
	// 

	/*

	 FULL_NAME
	 GLANCE_NAME
	 NAV_NAME
	 BLURB
	 PROD_DESCR
	 PROD_IMAGE
	 PROD_IMAGE_CONFIRM
	 PROD_IMAGE_DETAIL
	 PROD_IMAGE_FEATURE
	 PROD_IMAGE_ZOOM
	 PROD_DESCRIPTION_NOTE
	 ALTERNATE_IMAGE
	 PRODUCT_QUALITY_NOTE
	 RELATED_PRODUCTS
	 ALSO_SOLD_AS

	 */
	

	private Image getImage(String key) {
            return FDAttributeFactory.constructImage(this, key, IMAGE_BLANK);
	}

	public Image getCategoryImage() {
		return getImage("PROD_IMAGE");
	}

	public Image getConfirmImage() {
		return getImage("PROD_IMAGE_CONFIRM");
	}

	public Image getDetailImage() {
		return getImage("PROD_IMAGE_DETAIL");
	}

	public Image getThumbnailImage() {
		return getImage("PROD_IMAGE_FEATURE");
	}

	public Image getZoomImage() {
		return getImage("PROD_IMAGE_ZOOM");
	}

	public int getTemplateType(int defaultValue) {
	    Integer i = (Integer) getCmsAttributeValue("TEMPLATE_TYPE");
	    return i != null ? i.intValue() : defaultValue;
	}

	public ProductModel getAlsoSoldAs(int idx) {
		return (ProductModel) getAlsoSoldAs().get(idx);
	}
	
	/**
	 * Can the product be recommended.
	 * @return if the EXCLUDED_RECOMMENDATION flag is set to true (default false)
	 */
	public boolean isExcludedRecommendation() {
		return getAttribute("EXCLUDED_RECOMMENDATION", false);
	}

	/** Getter for property skus.
	 * @return Value of property skus.
	 */
	public List<ProductModel> getAlsoSoldAs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAs, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAs);
		}

		ArrayList<ProductModel> refs = new ArrayList<ProductModel>();
		for (Iterator iter = alsoSoldAs.iterator(); iter.hasNext();) {
			ProductModel pm = (ProductModel) iter.next();
			refs.add(pm);
		}
		return refs;
	}

	public List<ProductModel> getAlsoSoldAsRefs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAsList, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAsList);
			alsoSoldAsRefs.clear();
			for (Iterator tmp = alsoSoldAsList.iterator(); tmp.hasNext();) {
			    ProductModel m = (ProductModel) tmp.next();
			    alsoSoldAsRefs.add(m);
			}
		}
		return alsoSoldAsRefs;
	}

	
	public Html getProductDescription() {
	    return FDAttributeFactory.constructHtml(this, "PROD_DESCR");
	}

	public boolean hasTerms() {
	    Object a = getCmsAttributeValue("PRODUCT_TERMS_MEDIA");
	    return a == null ? false : true;
	}
	
	public Html getProductTerms() {
            return FDAttributeFactory.constructHtml(this, "PRODUCT_TERMS_MEDIA");
	}
	
	public boolean isShowTopTenImage() {
		return getAttribute("SHOW_TOP_TEN_IMAGE", false);
	}

	public PriceCalculator getPriceCalculator() {
	    return new PriceCalculator(getPricingContext(), this, this.getDefaultSku(getPricingContext()));
	}

        public PriceCalculator getPriceCalculator(String skuCode) {
            return new PriceCalculator(getPricingContext(), this, this.getValidSkuCode(getPricingContext(), skuCode));
        }
	
        /**
         * @param skuCode
         * @return the default sku code, if sku code is not specified or the sku is
         *         not in the products sku.
         */
        public SkuModel getValidSkuCode(PricingContext ctx, String skuCode) {
            if (skuCode == null) {
                return getDefaultSku(ctx);
            } else {
                for (SkuModel s : getSkus()) {
                    if (skuCode.equals(s.getSkuCode())) {
                        return s;
                    }
                }
                return getDefaultSku(ctx);
            }
        }	
	
	
	public boolean isDisplayable() {
		return !(isHidden() || isDiscontinued() || isUnavailable() || isOrphan() || isInvisible());
	}
	
    public boolean isDisplayableBasedOnCms() {        
        return !(isHidden() ||  isOrphan() || isInvisible());
    }
	
	public boolean isNew() {
		return ContentFactory.getInstance().getNewProducts().containsKey(this);
	}
	
	@Override
	public double getNewAge() {
		return ContentFactory.getInstance().getNewProductAge(this);
	}
	
	@Override
	public Date getNewDate() {
		return ContentFactory.getInstance().getNewProducts().get(this);
	}
	
	@Override
	public boolean isBackInStock() {
		return ContentFactory.getInstance().getBackInStockProducts().containsKey(this);
	}
	
	@Override
	public double getBackInStockAge() {
		return ContentFactory.getInstance().getBackInStockProductAge(this);
	}
	
	@Override
	public Date getBackInStockDate() {
		return ContentFactory.getInstance().getBackInStockProducts().get(this);
	}

        public int getDealPercentage() {
            return getDealPercentage(null);
        }

        public int getTieredDealPercentage() {
            return getTieredDealPercentage(null);
        }
        
        public int getHighestDealPercentage() {
            return getHighestDealPercentage(null);
        }
        
        /* price calculator calls */

        public String getDefaultPrice() {
            return getPriceCalculator().getDefaultPrice();
        }
        
        public String getDefaultPriceOnly() {
            return getPriceCalculator().getDefaultPriceOnly();
        }

        public String getDefaultUnitOnly() {
            return getPriceCalculator().getDefaultUnitOnly();
        }

	public int getDealPercentage(String skuCode) {
	    return getPriceCalculator(skuCode).getDealPercentage();
	}


	public int getTieredDealPercentage(String skuCode) {
	    return getPriceCalculator(skuCode).getTieredDealPercentage();
	}
	
	public int getHighestDealPercentage(String skuCode) {
	    return getPriceCalculator(skuCode).getHighestDealPercentage();
	}

        public String getTieredPrice(double savingsPercentage) {
            return getPriceCalculator().getTieredPrice(savingsPercentage);
        }

	public double getPrice(double savingsPercentage) {
	    return getPriceCalculator().getPrice(savingsPercentage);
	}

        public String getPriceFormatted(double savingsPercentage) {
            return getPriceCalculator().getPriceFormatted(savingsPercentage);
        }
	
        public String getPriceFormatted(double savingsPercentage, String skuCode) {
            return getPriceCalculator(skuCode).getPriceFormatted(savingsPercentage);
        }
        

        public String getWasPriceFormatted(double savingsPercentage) {
            return getPriceCalculator().getWasPriceFormatted(savingsPercentage);
        }
        
        public String getAboutPriceFormatted(double savingsPercentage) {
            return getPriceCalculator().getAboutPriceFormatted(savingsPercentage);
        }
        
        /* end of the price calculator calls */
        
	
	public String getYmalHeader() {
		final YmalSet activeYmalSet = getActiveYmalSet();
		if (activeYmalSet != null && activeYmalSet.getProductsHeader() != null)
			return activeYmalSet.getProductsHeader();
		
		return getAttribute("RELATED_PRODUCTS_HEADER", null);
	}
	
	public List<String> getCountryOfOrigin() throws FDResourceException{
		List<String> coolInfo=new ArrayList();
		
		List skus = getPrimarySkus(); 
	       SkuModel sku = null;
	       //remove the unavailable sku's
	       for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
	           sku = (SkuModel)li.next();
	           if ( sku.isUnavailable() ) {
	              li.remove();
	           }
	       }
	       if (skus.size()==0) return coolInfo;  // skip this item..it has no skus.  Hmmm?
	       if (skus.size()==1) {
	           sku = (SkuModel)skus.get(0);  // we only need one sku
	           FDProductInfo productInfo;
				try {
					productInfo = sku.getProductInfo();
					List countries=productInfo.getCountryOfOrigin();
					String text=getCOOLText(countries);
					if(!"".equals(text))
						coolInfo.add(text);
				} catch (FDSkuNotFoundException ignore) {
				}
	       } else {
	    	  /* int MAX_COOL_COUNT=5;
	    	   List countries=new ArrayList(MAX_COOL_COUNT);
	    	   FDProductInfo productInfo;
	    	   int index=0;
	    	   while(index<MAX_COOL_COUNT && countries.size()<MAX_COOL_COUNT ) {
	    		   for(Iterator it=skus.iterator();it.hasNext();) {
	    			   sku = (SkuModel)it.next();
		    		   try {
		    			   if(countries.size()<MAX_COOL_COUNT) {
		    				   productInfo = FDCachedFactory.getProductInfo( sku.getSkuCode());
		    				   List _countries=productInfo.getCOOLInfo();
		    				   if(_countries.size()>index)
		    					   countries.add(_countries.get(index));
		    			   } else {
		    				   break;
		    			   }
							
						} catch (FDSkuNotFoundException ignore) {
						} 
		    	   } 
	    		   index++;
	    	   }
	    	   text=getCOOLText(countries);*/
	    	   Map coolInfoMap=new HashMap();
	    	   FDProductInfo productInfo=null;
	    	   List countries=null;
	    	   String domainValue="";
	    	   String text="";
	    	   for(Iterator it=skus.iterator();it.hasNext();) {
    			   sku = (SkuModel)it.next();
    			   if(sku.getVariationMatrix()!=null && sku.getVariationMatrix().size()>0) {
    				   domainValue= ((DomainValue)sku.getVariationMatrix().get(0)).getValue();
    				   try {
    					   productInfo = sku.getProductInfo();
    					   countries=productInfo.getCountryOfOrigin();
    					   text=getCOOLText(countries);
    				   } catch (FDSkuNotFoundException ignore) {
    					   text="";
    				   }
    				   
    				   if(!coolInfoMap.containsKey(domainValue) && !"".equals(text))
    					   coolInfoMap.put(domainValue, text);
    			   }
	    	   } 
	    	   
	    	   StringBuffer temp=new StringBuffer(100);
	    	   for(Iterator it=coolInfoMap.keySet().iterator();it.hasNext();) {
	    		   domainValue=it.next().toString();
	    		   coolInfo.add(temp.append(domainValue).append(": ").append(coolInfoMap.get(domainValue).toString()).toString());
	    		   temp=new StringBuffer(100);
	    	   }
	    	  Collections.sort(coolInfo);
	    	   
	       }
	       
	       return coolInfo;
	}

	private String getCOOLText(List countries) {
		if(countries==null)
			return "";
		
		StringBuffer temp=new StringBuffer(50);
		for(int i=0;i<countries.size();i++) {
			if(i<(countries.size()-2)){
				temp.append(countries.get(i).toString()).append(", ");
			}else if(countries.size()>1 && i==(countries.size()-1)){
				temp.append(" and/or ").append(countries.get(i).toString());
			} else {
				temp.append(countries.get(i));
			}	
		}
		return temp.toString();
	}
	
	@Override
	public String getDefaultSkuCode() {
	    SkuModel sku = getDefaultSku();
	    return sku != null ? sku.getSkuCode() : null;
	}

	/*
	 * iPhone related
	 */
	@Override
	public Image getAlternateProductImage() {
	    return FDAttributeFactory.constructImage(this, "ALTERNATE_PROD_IMAGE");
	}

	@Override
	public boolean hideIphone() {
	    return getAttribute("HIDE_IPHONE", false);
	}
	
}
