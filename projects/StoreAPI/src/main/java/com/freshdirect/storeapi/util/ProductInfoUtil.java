package com.freshdirect.storeapi.util;

import org.apache.log4j.Logger;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;

public class ProductInfoUtil {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(ProductInfoUtil.class);
	
	public static String getPickingPlantId(FDProductInfo fdpi){
    	String pickingPlantId = null;
        UserContext userContext = ContentFactory.getInstance().getCurrentUserContext();
        if(null != userContext && null !=userContext.getPricingContext() && null !=userContext.getPricingContext().getZoneInfo()){
             if(fdpi!=null){        
                     String salesOrg=userContext.getPricingContext().getZoneInfo().getSalesOrg();
                     String distChannel=userContext.getPricingContext().getZoneInfo().getDistributionChanel();
                     pickingPlantId = fdpi.getPickingPlantId(salesOrg, distChannel);    
                     if(null == pickingPlantId){
                    	 LOGGER.info("PickingPlantId is not found for this product: "+fdpi.getSkuCode()+", for customer:"+userContext.getFdIdentity());
                     }
             }
             if(null == pickingPlantId && null !=userContext.getFulfillmentContext()){
//            	 LOGGER.info("PickingPlantId is not found for this product: "+fdpi.getSkuCode());
                 pickingPlantId = userContext.getFulfillmentContext().getPlantId();
             }
        }
        return pickingPlantId;
	}

}
