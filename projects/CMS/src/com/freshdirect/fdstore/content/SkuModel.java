/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.fdstore.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.OncePerRequestDateCache;
import com.freshdirect.fdstore.atp.FDAvailabilityHelper;
import com.freshdirect.fdstore.atp.FDAvailabilityI;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DateUtil;

/**
 *
 * @version	 $Revision$
 * @author	  $Author$
 */
public class SkuModel extends ContentNodeModelImpl implements AvailabilityI {
	
	private List variationMatrix = new ArrayList();
	
	private List variationOptions = new ArrayList();
	
	private boolean unavailable;
	private long lastRefresh = 0;
	private static final long AVAILABILITY_REFRESH = 1000 * FDStoreProperties.getSkuAvailabilityRefreshPeriod();

	public SkuModel(ContentKey cKey) {
		super(cKey);
	}
	
	public Object clone() {
		return super.clone();
	}

	public String getSkuCode(){
	    return this.getContentKey().getId();
	}
	
	public List getVariationMatrix() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_MATRIX", variationMatrix, false);
		return Collections.unmodifiableList(variationMatrix);
	}
	
	public List getVariationOptions() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_OPTIONS", variationOptions, false);
		return Collections.unmodifiableList(variationOptions);
	}
	
	public boolean isDiscontinued() {
		return this.getAvailability().isDiscontinued();
	}
	
	public boolean isTempUnavailable() {
		return this.getAvailability().isTempUnavailable();
	}

	public boolean isOutOfSeason() {
		return this.getAvailability().isOutOfSeason();
	}
	
	public boolean isUnavailable() {
		long now = System.currentTimeMillis();
		if(now - lastRefresh > AVAILABILITY_REFRESH) {
			this.unavailable = this.getAvailability().isUnavailable();
			lastRefresh = now;
		}
		return this.unavailable;
	}
		
	
	public boolean isAvailableWithin(int days) {
		return this.getAvailability().isAvailableWithin(days);
	}
	
	public Date getEarliestAvailability() {
		return this.getAvailability().getEarliestAvailability();
	}
	
	/** @return null if sku is available */
	public String getEarliestAvailabilityMessage() {
		DateRange dr = OncePerRequestDateCache.getAvailabilityHorizon();
		Date tomorrow = null;
		if(dr != null) {
			tomorrow = dr.getStartDate();
		}else{
			tomorrow = DateUtil.truncate(DateUtil.addDays(new Date(), 1));
		}
		
		Date earliestAvailability = getEarliestAvailability();
		
	    // cheat: if no availability indication, show the horizon as the
	    //        earliest availability
		if (earliestAvailability == null) {
			if(dr != null){
				earliestAvailability = dr.getEndDate();
			}else{
				earliestAvailability = DateUtil.addDays(DateUtil.truncate(new Date()), ErpServicesProperties.getHorizonDays());
			}
		}		
		
		if (earliestAvailability.after(tomorrow)) {
			SimpleDateFormat sf = new SimpleDateFormat("EEE M/dd");
			return sf.format(earliestAvailability);
		}

		return null;
	}

	
	public FDProductInfo getProductInfo() throws FDResourceException, FDSkuNotFoundException {
		return FDCachedFactory.getProductInfo(this.getSkuCode());
	}
    
    public FDProduct getProduct() throws FDResourceException, FDSkuNotFoundException {
		return FDCachedFactory.getProduct(this.getProductInfo());
	}

    public ProductModel getProductModel() {
    	return (ProductModel)getParentNode();
    }

    private AvailabilityI getAvailability() {
		try {

			FDProductInfo fdpi = this.getProductInfo();
			
			FDAvailabilityI av = AvailabilityFactory.createAvailability(this);
			
			return new AvailabilityAdapter(fdpi, av);

		} catch (FDSkuNotFoundException fdsnfe) {
			return UNAVAILABLE;
		} catch (FDResourceException fdre) {
			throw new FDRuntimeException(fdre);
		}
	}

	public List getBrands() {
		List brandModels = new ArrayList();
		ContentNodeModelUtil.refreshModels(this, "brands", brandModels, true);

		List newList = new ArrayList();
		for (int i = 0; i < brandModels.size(); i++) {
			BrandModel b = (BrandModel) brandModels.get(i);
			String str = b.getFullName();
			if ((str != null) && !str.equals("")) {
				newList.add(b);
			}
		}
		return newList;
	}

    
	private final static AvailabilityI UNAVAILABLE = new AvailabilityI() {
	    public boolean isDiscontinued() {
	    	return true;
		}
		public boolean isTempUnavailable() {
			return false;
		}
	    public boolean isOutOfSeason() {
	    	return false;
	    }
	    public boolean isUnavailable() {
	    	return true;
	    }
	    public boolean isAvailableWithin(int days) {
	    	return false;
	    }
	    public Date getEarliestAvailability() {
	    	return null;
	    }		
	};
	
	/**
	 *  An adapter class, to provide availability information to a
	 *  SkuModel
	 *  
	 *  @see SkuModel
	 */
	static class AvailabilityAdapter implements AvailabilityI {
		
		private final FDProductInfo productInfo;
		private final FDAvailabilityI availability;
		
		public AvailabilityAdapter(FDProductInfo fdpi, FDAvailabilityI availability) {
			this.productInfo = fdpi;
			this.availability = availability;
		}
		
		public boolean isDiscontinued() {
			return this.productInfo.isDiscontinued();
		}

		public boolean isTempUnavailable() {
			return this.productInfo.isTempUnavailable();
		}

		public boolean isOutOfSeason() {
			return this.productInfo.isOutOfSeason();
		}

		public boolean isUnavailable() {
			if (this.isDiscontinued() || this.isTempUnavailable() || this.isOutOfSeason()) {
				return true;
			}
			
			Date startDate = OncePerRequestDateCache.getToday();
			if(startDate == null){
				startDate = DateUtil.truncate(new Date());
			}
			Date endDate = null;
			DateRange dr = OncePerRequestDateCache.getAvailabilityHorizon();
			if(dr != null){
				endDate = dr.getEndDate(); 
			} else {
				endDate = DateUtil.addDays(startDate, ErpServicesProperties.getHorizonDays());
			}
			
			Date firstAvDate = FDAvailabilityHelper.getFirstAvailableDate(this.availability);
			return firstAvDate == null || !firstAvDate.before(endDate);
			
		}
    
		public Date getEarliestAvailability() {
			if (this.isDiscontinued() || this.isTempUnavailable() || this.isOutOfSeason()) {
				return null;
			}
			
			return FDAvailabilityHelper.getFirstAvailableDate(this.availability);
		}
		
		public boolean isAvailableWithin(int days) {
			if (this.isDiscontinued() || this.isTempUnavailable() || this.isOutOfSeason()) {
				return false;
			}
			
			return FDAvailabilityHelper.getFirstAvailableDate(this.availability, days) != null;
		}
		
	}

}