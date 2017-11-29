package com.freshdirect.fdstore.customer.util;

import java.util.List;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.framework.util.MathUtil;

public class FDCartUtil {

	public static double getSaveAmount(List<FDCartLineI> orderLines) {
		if (orderLines == null) {
			return 0;
		}
		double saveAmount = 0.0;
		for (FDCartLineI cartLineModel : orderLines) {
			saveAmount += cartLineModel.getSaveAmount();
			if (hasCartLineGroupDiscount(cartLineModel)) {
				saveAmount += cartLineModel.getGroupScaleSavings();
            }

		}
		return MathUtil.roundDecimal(saveAmount);
	}

	public static double getSubTotal(List<FDCartLineI> orderLines) {
		if (orderLines == null) {
			return 0;
		}
		double subTotal = 0.0;
		for (FDCartLineI cartline : orderLines) {
			subTotal += MathUtil.roundDecimal(cartline.getPrice());
		}
		return MathUtil.roundDecimal(subTotal);
	}
	
    private static boolean hasCartLineGroupDiscount(FDCartLineI cartLine) {
        return cartLine.getDiscount() == null && cartLine.getGroupQuantity() > 0 && cartLine.getGroupScaleSavings() > 0;
    }

}
