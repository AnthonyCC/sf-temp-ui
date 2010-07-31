package com.freshdirect.fdstore.promotion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.DCPDPromoProductCache;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.adapter.OrderPromotionHelper;

public class CartStrategy extends DCPDLineItemStrategy implements PromotionStrategyI {
	private static final long serialVersionUID = -3120395698927259060L;

	private final static String[] DRY_GOODS = { "gro", "spe" };
	private boolean needDryGoods;	
	private int minSkuQuantity;
	private Map<EnumDCPDContentType, Set<String>> dcpdData = new HashMap<EnumDCPDContentType, Set<String>>();//Will contain only SKU and BRAND types.
	
	
	@Override
	public int evaluate(String promotionCode, PromotionContextI context) {
		int qualifiedSku=0;
		int allowORdeny = PromotionStrategyI.RESET;
		FDCartModel cart = context.getShoppingCart();
		if(null != cart){
			try {
				if (needDryGoods && !cart.hasItemsFrom(DRY_GOODS)) {
					return PromotionStrategyI.DENY;
				}
				List<FDCartLineI> orderLines = cart.getOrderLines();
				if(null != orderLines && !orderLines.isEmpty()){
					
					for (Iterator<FDCartLineI> iterator = orderLines.iterator(); iterator.hasNext();) {
						FDCartLineI cartLine = iterator.next();
						
						if(!contentKeys.isEmpty() || !dcpdData.isEmpty()){
							allowORdeny = evaluate(cartLine, promotionCode, context);							
							if(PromotionStrategyI.RESET==allowORdeny || PromotionStrategyI.DENY==allowORdeny){
								Set<String> skuSet =dcpdData.get(EnumDCPDContentType.SKU);
								if(null != skuSet && skuSet.contains(cartLine.getSkuCode())){
									qualifiedSku += cartLine.getQuantity();
									allowORdeny = PromotionStrategyI.ALLOW;
								}							
							}
						}
						if(PromotionStrategyI.RESET==allowORdeny || (PromotionStrategyI.ALLOW ==allowORdeny && qualifiedSku >= minSkuQuantity))
							break;
								
					}
				}
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}
		}
		if(PromotionStrategyI.RESET==allowORdeny)
			//At this point there are no cart eligiblity set.
			return PromotionStrategyI.ALLOW;
		
		return allowORdeny;
	}

	@Override
	public int evaluate(FDCartLineI lineItem, String promotionCode,
			PromotionContextI context) {
		boolean eligible = false;
		ProductModel model = lineItem.getProductRef().lookupProductModel();
		String productId = model.getContentKey().getId();
		DCPDPromoProductCache dcpdCache = context.getUser().getDCPDPromoProductCache();
		//Check if the line item product is already evaluated.
		if(dcpdCache.isEvaluated(productId, promotionCode)){
			eligible = dcpdCache.isEligible(productId, promotionCode);
		}else{
			if(contentKeys.size() > 0){
				//Check if the line item is eligible for a category or department discount.
				eligible = OrderPromotionHelper.evaluateProductForDCPDPromo(model, contentKeys);
				//Set Eligiblity info to user session.
				dcpdCache.setPromoProductInfo(productId, promotionCode, eligible);
			}
		}
		if(!eligible){
//			Set<String> skuSet =dcpdData.get(EnumDCPDContentType.SKU);
			Set<String> brandSet =dcpdData.get(EnumDCPDContentType.BRAND);
			/*if(null != skuSet && skuSet.contains(lineItem.getSkuCode())){
				qualifiedSku = qualifiedSku++;
				eligible = true;
			}*/
			if(!eligible && brandSet != null && (lineItem.hasBrandName(brandSet))){
				eligible = true;
			}
			
		}
		if(eligible){
			return PromotionStrategyI.ALLOW;
		}		
		return PromotionStrategyI.DENY;
		
	}

	@Override
	public int getPrecedence() {
		// TODO Auto-generated method stub
		return 0;
	}	

	public boolean isNeedDryGoods() {
		return needDryGoods;
	}

	public void setNeedDryGoods(boolean needDryGoods) {
		this.needDryGoods = needDryGoods;
	}	

	public Map<EnumDCPDContentType, Set<String>> getDcpdData() {
		return dcpdData;
	}

	public void setDcpdData(Map<EnumDCPDContentType, Set<String>> dcpdData) {
		this.dcpdData = dcpdData;
	}

	public int getMinSkuQuantity() {
		return minSkuQuantity;
	}

	public void setMinSkuQuantity(int minSkuQuantity) {
		this.minSkuQuantity = minSkuQuantity;
	}

}
