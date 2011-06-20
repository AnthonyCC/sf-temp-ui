package com.freshdirect.dataloader.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.promotion.EnumPromoChangeType;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewManager;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.promotion.management.WSPromotionInfo;
import com.freshdirect.fdstore.promotion.pxp.PromoPublisher;
import com.freshdirect.framework.util.log.LoggerFactory;


public class WSPromoCancellationCron {
	private static Category	LOGGER	= LoggerFactory.getInstance( WSPromoCancellationCron.class );
	
	private Set<String> getWSPromotionstoCancel() throws FDResourceException {
		Set<String> cancelPromoCodes = new HashSet<String>();
		Date today = new Date();
		PromoPublisher publisherAgent = new PromoPublisher();
		List<WSPromotionInfo> wspromotions = (List<WSPromotionInfo>)publisherAgent.getAllActiveWSPromotions(); // calling ?action=getWSPromosForAutoCancel
		if(wspromotions == null || wspromotions.size() == 0){
			return Collections.emptySet();
		}
		
		//Load dayofweek limits from Database.
		Map<Integer,Double> dowLimits = FDPromotionNewManager.getDOWLimits();
		LOGGER.debug("getWSPromotionstoCancel dowLimits.>"+dowLimits);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		for(int i = 1 ; i <= 7; i++) {
			//check for next 7 days delivery horizon
			cal.add(Calendar.DATE, 1);
			double totalDiscount = 0;
			Set<String> temp = new HashSet<String>();
			int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
			Iterator<WSPromotionInfo> it = wspromotions.iterator();
			while(it.hasNext()){
				WSPromotionInfo promo = it.next();
				if(promo.getDayofweek() == dayofweek) {
					if(promo.getStatus().equals(EnumPromotionStatus.LIVE))
						//add to the list only Promotion is still LIVE.
						temp.add(promo.getPromotionCode());
					totalDiscount += (promo.getDiscount() * promo.getRedemptions());
				}
			}
			Double dowLimit = dowLimits.get(new Integer(dayofweek));
			if(dowLimit == null) continue;
			BigDecimal limit = new BigDecimal(dowLimit.doubleValue()); 
			BigDecimal total = new BigDecimal(totalDiscount);
			if(total.compareTo(limit) >= 0){
				//limit reached. add all temp codes to main set.
				cancelPromoCodes.addAll(temp);
			}
		}
		//Now check for individual promotion redemption limit.
		Iterator<WSPromotionInfo> it = wspromotions.iterator();
		while(it.hasNext()){
			WSPromotionInfo promo = it.next();
			if(!cancelPromoCodes.contains(promo.getPromotionCode())){
				//Check for redemption limit.
				if(promo.getRedemptions() >= promo.getRedeemCount()) {
					//Limit reached.
					cancelPromoCodes.add(promo.getPromotionCode());
				}
			}
		}
		return cancelPromoCodes;
	}
	private List<FDPromotionNewModel> cancelPromotions(Set<String> cancelPromoCodes) throws FDResourceException, FinderException{
		List<FDPromotionNewModel> cancelledPromotions = new ArrayList<FDPromotionNewModel>();
		
		if(cancelPromoCodes.size() > 0 ){
			Iterator<String> it = cancelPromoCodes.iterator();
			while(it.hasNext()) {
				String promoCode = it.next();
				FDPromotionNewModel promotion = FDPromotionNewManager.loadPromotion(promoCode);
				if(promotion == null){
					throw new FDResourceException("Unable to locate Windows Steering Promotion. Please contact AppSupport.");
				}
				promotion.setAuditChanges(FDPromotionNewManager.loadPromoAuditChanges(promotion.getId()));
				promotion.setStatus(EnumPromotionStatus.CANCELLING);
				promotion.setModifiedBy("system");
				promotion.setModifiedDate(new Date());						
				FDPromoChangeModel changeModel = new FDPromoChangeModel();
				List<FDPromoChangeModel> promoChanges = new ArrayList<FDPromoChangeModel>();
				changeModel.setPromotionId(promotion.getId());
				changeModel.setActionDate(new Date());
				changeModel.setActionType(EnumPromoChangeType.CANCEL);
				changeModel.setUserId("system");
				promoChanges.add(changeModel);
				promotion.setAuditChanges(promoChanges);
				
				FDPromotionNewManager.storePromotionStatus(promotion,EnumPromotionStatus.CANCELLING);
				FDPromotionNewModel promo = FDPromotionNewManager.loadPromotion(promoCode);
				cancelledPromotions.add(promo);
			}
		}
		return cancelledPromotions;
	}
	
	public void publishPromotions(List<FDPromotionNewModel> ppList) {
		CrmAgentModel agent = new CrmAgentModel();
		agent.setLdapId("system");
		agent.setUserId("system");
		
		PromoPublisher publisher = new PromoPublisher();
		publisher.setPromoList(ppList);
		publisher.setAgent(agent);
		final boolean result = publisher.doPublish();
		if (!result) {
			//Log Error
			LOGGER.error("Published failed. Check Server Log.");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		WSPromoCancellationCron cron = new WSPromoCancellationCron();
		Set<String> cancelPromoCodes = cron.getWSPromotionstoCancel();
		List<FDPromotionNewModel> cancelledPromotions = cron.cancelPromotions(cancelPromoCodes);
		cron.publishPromotions(cancelledPromotions);
	}

}
