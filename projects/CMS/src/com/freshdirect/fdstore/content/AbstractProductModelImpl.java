package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.attributes.Attribute;

public abstract class AbstractProductModelImpl extends ContentNodeModelImpl implements ProductModel {

	protected final static Image IMAGE_BLANK = new Image("/media_stat/images/layout/clear.gif", 1, 1);

	private List alsoSoldAs = new ArrayList();
	private List alsoSoldAsList = new ArrayList();
	private List alsoSoldAsRefs = new ArrayList();

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
		return (Image) getAttribute(key, IMAGE_BLANK);
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
	public List getAlsoSoldAs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAs, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAs);
		}

		ArrayList refs = new ArrayList();
		for (Iterator iter = alsoSoldAs.iterator(); iter.hasNext();) {
			ProductModel pm = (ProductModel) iter.next();
			ProductRef ref = new ProductRef(pm.getParentNode().getContentName(), pm.getContentName());
			refs.add(ref);
		}
		return refs;
	}

	public List getAlsoSoldAsRefs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAsList, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAsList);
			alsoSoldAsRefs.clear();
			for (Iterator tmp = alsoSoldAsList.iterator(); tmp.hasNext();) {
				ContentNodeModel m = (ContentNodeModel) tmp.next();
				alsoSoldAsRefs.add(new ProductRef(m.getParentNode().getContentName(), m.getContentName()));
			}
		}
		return alsoSoldAsRefs;
	}

	
	public Html getProductDescription() {
		Attribute a = getAttribute("PROD_DESCR");
		return a == null ? null : (Html) a.getValue();
	}

	public boolean hasTerms() {
		Attribute a = getAttribute("PRODUCT_TERMS_MEDIA");
		return a == null ? false : true;
	}
	
	public Html getProductTerms() {
		Attribute a = getAttribute("PRODUCT_TERMS_MEDIA");
		return a == null ? null : (Html) a.getValue();
	}
	
	public boolean isShowTopTenImage() {
		return getAttribute("SHOW_TOP_TEN_IMAGE", false);
	}
	
	public String getDefaultPrice() {
			try {
				SkuModel skuModel = getDefaultSku();
				if(skuModel == null)
					return "";
				FDProductInfo productInfo = skuModel.getProductInfo();
				if(productInfo == null)
					return "";
				return productInfo.getDefaultPrice() + "/" +productInfo.getDefaultPriceUnit();
			} catch (FDResourceException e) {
				throw new RuntimeException(e);
			} catch (FDSkuNotFoundException e) {
				throw new RuntimeException(e);
			}
	}
	
	public boolean isDisplayable() {
		return !(isHidden() || isDiscontinued() || isUnavailable() || isOrphan() || isInvisible());
	}
	
    public boolean isDisplayableBasedOnCms() {        
        return !(isHidden() ||  isOrphan() || isInvisible());
    }
	
	public String getDefaultPriceOnly() {
		try {
			SkuModel skuModel = getDefaultSku();
			if(skuModel == null)
				return "";
			FDProductInfo productInfo = skuModel.getProductInfo();
			if(productInfo == null)
				return "";
			return productInfo.getDefaultPrice() + "";
		} catch (FDResourceException e) {
			throw new RuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getDefaultUnitOnly() {
		try {
			SkuModel skuModel = getDefaultSku();
			if(skuModel == null)
				return "";
			FDProductInfo productInfo = skuModel.getProductInfo();
			if(productInfo == null)
				return "";
			return productInfo.getDefaultPriceUnit();
		} catch (FDResourceException e) {
			throw new RuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean isNew() {
		try {
			Collection newProds = ContentFactory.getInstance().getNewProducts(
					FDStoreProperties.getSmartstoreNewproductsDays(), null);
			return newProds != null && newProds.contains(this);
		} catch (FDResourceException e) {
			return false;
		}
	}

	public int getDealPercentage() {
		return getDealPercentage(null);
	}
	
	public int getDealPercentage(String skuCode) {
		SkuModel defaultSku = getDefaultSku();
                if (skuCode == null) {
			skuCode = defaultSku != null ? defaultSku.getSkuCode() : null;
		} else {
			if (getSkuCodes().indexOf(skuCode) < 0) {
				// invalid sku code using default
				skuCode = defaultSku.getSkuCode();
			}
		}
		FDProductInfo productInfo = null;
		int deal = 0;
		if (skuCode != null) {
			try {
				productInfo = FDCachedFactory.getProductInfo(skuCode);
				if (productInfo.hasWasPrice()) {
					deal = productInfo.getDealPercentage();
				}
			} catch (FDSkuNotFoundException ex) {
			} catch (FDResourceException e) {
			}
		}
		return deal;
	}

	public int getTieredDealPercentage() {
		return getTieredDealPercentage(null);
	}
	
	public int getTieredDealPercentage(String skuCode) {
		SkuModel defaultSku = getDefaultSku();
                if (skuCode == null) {
			skuCode = defaultSku != null ? defaultSku.getSkuCode() : null;
		} else {
			if (getSkuCodes().indexOf(skuCode) < 0) {
				// invalid sku code using default
				skuCode = defaultSku.getSkuCode();
			}
		}
		FDProductInfo productInfo = null;
		int deal = 0;
		if (skuCode != null) {
			try {
				productInfo = FDCachedFactory.getProductInfo(skuCode);
				if (productInfo.hasWasPrice()) {
					deal = productInfo.getTieredDealPercentage();
				}
			} catch (FDSkuNotFoundException ex) {
			} catch (FDResourceException e) {
			}
		}
		return deal;
	}
	
	public int getHighestDealPercentage() {
		return getHighestDealPercentage(null);
	}
	
	public int getHighestDealPercentage(String skuCode) {
		SkuModel defaultSku = getDefaultSku();
		if (skuCode == null) {
			skuCode = defaultSku != null ? defaultSku.getSkuCode() : null;
		} else {
			if (getSkuCodes().indexOf(skuCode) < 0) {
				// invalid sku code using default
				skuCode = defaultSku.getSkuCode();
			}
		}
		FDProductInfo productInfo = null;
		int deal = 0;
		if (skuCode != null) {
			try {
				productInfo = FDCachedFactory.getProductInfo(skuCode);
				deal = productInfo.getHighestDealPercentage();
			} catch (FDSkuNotFoundException ex) {
			} catch (FDResourceException e) {
			}
		}
		return deal;
	}

	public String getTieredPrice(double savingsPercentage) {
		SkuModel skuCode = getDefaultSku();

		if (skuCode != null) {
			try {
				FDProductInfo productInfo = FDCachedFactory
						.getProductInfo(skuCode.getSkuCode());
				FDProduct product = FDCachedFactory.getProduct(productInfo);
				if (product != null) {
					String[] tieredPricing = null;

					if (savingsPercentage > 0)
						tieredPricing = product.getPricing().getScaleDisplay(
								savingsPercentage);
					else
						tieredPricing = product.getPricing().getScaleDisplay();

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
	
	public String getPriceFormatted(double savingsPercentage) {
		SkuModel skuCode = getDefaultSku();

		if (skuCode != null) {
			try {
				FDProductInfo productInfo = FDCachedFactory
						.getProductInfo(skuCode.getSkuCode());
				if (productInfo != null) {
					double price;
					if (savingsPercentage > 0) {
						price = productInfo.getDefaultPrice()
								* (1 - savingsPercentage);
					} else if (productInfo.hasWasPrice()) {
						price = productInfo.getDefaultPrice();
					} else {
						price = productInfo.getDefaultPrice();
					}

					return CURRENCY_FORMAT.format(price)
							+ "/"
							+ productInfo.getDisplayableDefaultPriceUnit()
									.toLowerCase();
				}
				return null;
			} catch (FDSkuNotFoundException ex) {
			} catch (FDResourceException e) {
			}
		}
		return null;
	}

	public String getWasPriceFormatted(double savingsPercentage) {
		SkuModel skuCode = getDefaultSku();

		if (skuCode != null) {
			try {
				FDProductInfo productInfo = FDCachedFactory
						.getProductInfo(skuCode.getSkuCode());
				if (productInfo != null) {
					Double wasPrice = null;
					
					if ( savingsPercentage > 0. ) {
						wasPrice = productInfo.getDefaultPrice();
					} else if ( productInfo.hasWasPrice() ) {
						wasPrice = productInfo.getBasePrice();
					}

					if (wasPrice != null)
						return CURRENCY_FORMAT.format(wasPrice)
								+ "/"
								+ productInfo.getDisplayableDefaultPriceUnit()
										.toLowerCase();
				}
				return null;
			} catch (FDSkuNotFoundException ex) {
			} catch (FDResourceException e) {
			}
		}
		return null;
	}

	public String getAboutPriceFormatted(double savingsPercentage) {
		SkuModel skuCode = getDefaultSku();

		if (skuCode != null) {
			try {
				FDProductInfo productInfo = FDCachedFactory
						.getProductInfo(skuCode.getSkuCode());
				if (productInfo != null) {
					String displayPriceString = null;
					FDProduct fdProduct = FDCachedFactory
							.getProduct(productInfo);
					if (fdProduct.getDisplaySalesUnits() != null
							&& fdProduct.getDisplaySalesUnits().length > 0) {
						FDSalesUnit fdSalesUnit = fdProduct
								.getDisplaySalesUnits()[0];
						double salesUnitRatio = (double) fdSalesUnit.getDenominator()
								/ (double) fdSalesUnit.getNumerator();
						String alternateUnit = fdSalesUnit.getName();

						if (savingsPercentage < 0.)
							savingsPercentage = 0.;
						String[] scales = savingsPercentage > 0. ?
								fdProduct.getPricing().getScaleDisplay() : 
								fdProduct.getPricing().getScaleDisplay(savingsPercentage);
						double displayPrice = 0.;
						if (scales != null && scales.length > 0) {
							displayPrice = fdProduct.getPricing().getMinPrice()
									* (1. - savingsPercentage) / salesUnitRatio;
						} else {
							displayPrice = productInfo.getDefaultPrice()
									* (1. - savingsPercentage) / salesUnitRatio;
						}
						if (displayPrice > 0.) {
							displayPriceString = "about "
									+ CURRENCY_FORMAT.format(displayPrice)
									+ "/" + alternateUnit.toLowerCase();
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
	
	public String getYmalHeader() {
		final YmalSet activeYmalSet = getActiveYmalSet();
		if (activeYmalSet != null && activeYmalSet.getProductsHeader() != null)
			return activeYmalSet.getProductsHeader();
		
		return getAttribute("RELATED_PRODUCTS_HEADER", null);
	}
	
	public List getCountryOfOrigin() throws FDResourceException{
		List coolInfo=new ArrayList();
		
		List skus = getSkus(); 
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
					productInfo = FDCachedFactory.getProductInfo( sku.getSkuCode());
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
    					   productInfo = FDCachedFactory.getProductInfo( sku.getSkuCode());
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
}
