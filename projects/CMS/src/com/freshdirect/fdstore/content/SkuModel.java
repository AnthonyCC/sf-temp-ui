package com.freshdirect.fdstore.content;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
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

public class SkuModel extends ContentNodeModelImpl implements AvailabilityI {
	
	private static final long	serialVersionUID	= 4368164084658898445L;

	private List<DomainValue> variationMatrix = new ArrayList<DomainValue>();
	
	private List<DomainValue> variationOptions = new ArrayList<DomainValue>();
	
	//private boolean unavailable;
	//private long lastRefresh = 0;
	//private static final long AVAILABILITY_REFRESH = 1000 * FDStoreProperties.getSkuAvailabilityRefreshPeriod();

	public SkuModel(ContentKey cKey) {
		super(cKey);
	}
	
	public Object clone() {
		return super.clone();
	}

	public String getSkuCode(){
	    return this.getContentKey().getId();
	}
	
	public List<DomainValue> getVariationMatrix() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_MATRIX", variationMatrix, false);
		return Collections.unmodifiableList(variationMatrix);
	}
	
	public List<DomainValue> getVariationOptions() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_OPTIONS", variationOptions, false);
		return Collections.unmodifiableList(variationOptions);
	}
	
	@Override
	public boolean isDiscontinued() {
		return this.getAvailability().isDiscontinued();
	}
	
	@Override
	public boolean isTempUnavailable() {
		return this.getAvailability().isTempUnavailable();
	}

	@Override
	public boolean isOutOfSeason() {
		return this.getAvailability().isOutOfSeason();
	}
	
	@Override
	public boolean isUnavailable() {
		/**
		 * Availability cannot be cached, as plant might have switched.
		 * long now = System.currentTimeMillis();
		if(now - lastRefresh > AVAILABILITY_REFRESH) {
			this.unavailable = this.getAvailability().isUnavailable();
			lastRefresh = now;
		}
		return this.unavailable;*/
		return this.getAvailability().isUnavailable();
	}
		
	@Override
	public boolean isAvailableWithin(int days) {
		return this.getAvailability().isAvailableWithin(days);
	}
	
	@Override
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
    	if(FDStoreProperties.isDeveloperDisableAvailabilityLookup()) {
    		return DUMMY_AVAILABLE;
    	}
		try {

			FDProductInfo fdpi = this.getProductInfo();
//			String plantID=ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
//			FDAvailabilityI av = AvailabilityFactory.createAvailability(this, fdpi,plantID);
			String salesOrg=ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().getSalesOrg();
			String distChannel=ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo().getDistributionChanel();			
			String pickingPlantId = fdpi.getPickingPlantId(salesOrg, distChannel);			
			if(null == pickingPlantId ||"".equals(pickingPlantId.trim())){
				pickingPlantId = ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
			}
			FDAvailabilityI av = AvailabilityFactory.createAvailability(this, fdpi,pickingPlantId);
			AvailabilityAdapter answer = new AvailabilityAdapter(fdpi, av,salesOrg,distChannel,pickingPlantId); 
			return answer;

		} catch (FDSkuNotFoundException fdsnfe) {
			return UNAVAILABLE;
		} catch (FDResourceException fdre) {
			throw new FDRuntimeException(fdre);
		}
	}

	public List<BrandModel> getBrands() {
		List<BrandModel> brandModels = new ArrayList<BrandModel>();
		ContentNodeModelUtil.refreshModels(this, "brands", brandModels, false);

		List<BrandModel> newList = new ArrayList<BrandModel>();
		for (int i = 0; i < brandModels.size(); i++) {
			BrandModel b = brandModels.get(i);
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
		public double getAvailabileQtyForDate(java.util.Date targetDate) {
			return 0;
		}
	};

	private final static AvailabilityI DUMMY_AVAILABLE = new AvailabilityI() {
		
	    public boolean isDiscontinued() {
	    	return false;
		}
		public boolean isTempUnavailable() {
			return false;
		}
	    public boolean isOutOfSeason() {
	    	return false;
	    }
	    public boolean isUnavailable() {
	    	return false;
	    }
	    public boolean isAvailableWithin(int days) {
	    	return true;
	    }
	    public Date getEarliestAvailability() {
	    	return new Date();
	    }	
		public double getAvailabileQtyForDate(java.util.Date targetDate) {
			return 1;
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
		private final String salesArea;
		private final String distrChannel;
		private final String plantId;
		
		public AvailabilityAdapter(FDProductInfo fdpi, FDAvailabilityI availability,String salesArea, String distrChannel, String plantId ) {
			this.productInfo = fdpi;
			this.availability = availability;
			this.salesArea=salesArea;
			this.distrChannel=distrChannel;
			this.plantId = plantId;
		}
		
		@Override
		public boolean isDiscontinued() {
			return this.productInfo.isDiscontinued(salesArea,distrChannel) ||(this.productInfo.isLimitedQuantity(plantId) && isNotAvailableWithInHorizon()) ;//To treat limit quantity products as discontinued ones.
		}

		@Override
		public boolean isTempUnavailable() {
			return this.productInfo.isTempUnavailable(salesArea,distrChannel);
		}

		@Override
		public boolean isOutOfSeason() {
			return this.productInfo.isOutOfSeason(salesArea,distrChannel);
		}

		@Override
		public boolean isUnavailable() {
			if (this.isDiscontinued() || this.isTempUnavailable() || this.isOutOfSeason()) {
				return true;
			}
			
			return isNotAvailableWithInHorizon();
			
		}

		/**
		 * @return
		 */
		private boolean isNotAvailableWithInHorizon() {
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
    
		@Override
		public Date getEarliestAvailability() {
			if (this.isDiscontinued() || this.isTempUnavailable() || this.isOutOfSeason()) {
				return null;
			}
			
			return FDAvailabilityHelper.getFirstAvailableDate(this.availability);
		}
		
		@Override
		public boolean isAvailableWithin(int days) {
			if (this.isDiscontinued() || this.isTempUnavailable() || this.isOutOfSeason()) {
				return false;
			}
			
			return FDAvailabilityHelper.getFirstAvailableDate(this.availability, days) != null;
		}
		
		@Override
		public double getAvailabileQtyForDate(java.util.Date targetDate) {
			return this.availability.getAvailabileQtyForDate(targetDate);
		}	
		
		
	}

	public PricingContext getPricingContext() {
		return PricingContext.DEFAULT;
	}
	
	public PriceCalculator getSkuPriceCalculator() {
	    return new PriceCalculator(getPricingContext(), getProductModel(), this);
	}

	/**
	 * 
	 * @param targetDate
	 *            date to check for inventory qty, if NULL will default to
	 *            Today.
	 * @return qty avail for targetDate, if FDX and qty 0 for Target date,
	 *         includes next date.
	 */
	public double getAvailabileQtyForDate(java.util.Date targetDate) {
		return this.getAvailability().getAvailabileQtyForDate(targetDate);
	}
}
