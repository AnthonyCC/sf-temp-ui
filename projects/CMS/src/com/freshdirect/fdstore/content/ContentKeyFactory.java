package com.freshdirect.fdstore.content;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;


public class ContentKeyFactory {
	private static final String PRES_PICKS_CAT_ID = "picks_love";
	private static final String PRES_PICKS_FDX_CAT_ID = "picks_love_fdx";


	private static ContentKeyFactory instance = null;
	
	private final EnumEStoreId eStoreId;
	
	public static ContentKeyFactory getIntance() {
		if (instance == null) {
			synchronized( ContentKeyFactory.class ) {
				if (instance == null) {
					instance = new ContentKeyFactory();
				}
				
			}
		}
		return instance;
	}

	
	private ContentKeyFactory() {
		this.eStoreId = CmsManager.getInstance().getEStoreEnum();
	}
	

	/**
	 * @param estoreId
	 * @return
	 * @throws InvalidContentKeyException
	 */
	public ContentKey getECouponsCategoryKey(EnumEStoreId estoreId) throws InvalidContentKeyException {
		ContentKey key = null;
		
		switch(estoreId) {
		case FD:
			try {
				key = ContentKey.create(FDContentTypes.CATEGORY, FDCouponProperties.getCouponCMSCategory());
			} catch (InvalidContentKeyException e) {}
			break;
		case FDX:
			try {
				key = ContentKey.create(FDContentTypes.CATEGORY, FDCouponProperties.getCouponCMSCategoryFDX());
			} catch (InvalidContentKeyException e) {}
			break;
		}
		
		if (key == null) {
			throw new InvalidContentKeyException();
		}
		
		return key;
	}

	public ContentKey getECouponsCategoryKey() throws InvalidContentKeyException {
		return getECouponsCategoryKey(eStoreId);
	}

	

	public ContentKey getNewProductsCategoryKey(EnumEStoreId estoreId) throws InvalidContentKeyException {
		ContentKey key = null;
		
		switch(estoreId) {
		case FD:
			try {
				key = ContentKey.create(FDContentTypes.CATEGORY, FDStoreProperties.getNewProductsCatId());
			} catch (InvalidContentKeyException e) {}
			break;
		case FDX:
			try {
				key = ContentKey.create(FDContentTypes.CATEGORY, FDStoreProperties.getNewProductsCatFDX());
			} catch (InvalidContentKeyException e) {}
			break;
		}
		
		if (key == null) {
			throw new InvalidContentKeyException();
		}
		
		return key;
		
	}

	public ContentKey getNewProductsCategoryKey() throws InvalidContentKeyException {
		return getNewProductsCategoryKey(eStoreId);
	}




	public ContentKey getPresidentsPicksCategoryKey(EnumEStoreId estoreId) throws InvalidContentKeyException {
		ContentKey key = null;
		
		switch(estoreId) {
		case FD:
			try {
				key = ContentKey.create(FDContentTypes.CATEGORY, PRES_PICKS_CAT_ID);
			} catch (InvalidContentKeyException e) {}
			break;
		case FDX:
			try {
				key = ContentKey.create(FDContentTypes.CATEGORY, PRES_PICKS_FDX_CAT_ID);
			} catch (InvalidContentKeyException e) {}
			break;
		}
		
		if (key == null) {
			throw new InvalidContentKeyException();
		}
		
		return key;
		
	}

	public ContentKey getPresidentsPicksCategoryKey() throws InvalidContentKeyException {
		return getPresidentsPicksCategoryKey(eStoreId);
	}
}
