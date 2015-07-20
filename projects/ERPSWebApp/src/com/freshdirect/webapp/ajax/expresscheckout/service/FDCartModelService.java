package com.freshdirect.webapp.ajax.expresscheckout.service;

import java.util.Iterator;

import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.fdstore.customer.FDCartI;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.payment.EnumPaymentMethodType;

public class FDCartModelService {

	private static final FDCartModelService INSTANCE = new FDCartModelService();

	private FDCartModelService() {
	}

	public static FDCartModelService defaultService() {
		return INSTANCE;
	}

	public boolean isEbtPaymentForCart(FDCartI cart) {
		return null != cart.getPaymentMethod() && EnumPaymentMethodType.EBT.equals(cart.getPaymentMethod().getPaymentMethodType());
	}

	public double getTaxValueWithoutWineAndSpirit(FDCartI cart) {
		double taxValue = 0.0;
		for (Iterator<FDCartLineI> i = cart.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI item = i.next();
			if (!item.isWine()) {
				taxValue += item.getTaxValue();
			}
		}
		return taxValue;
	}

	public double getTaxValueOnlyWineAndSpirit(FDCartI cart) {
		double taxValue = 0.0;
		for (Iterator<FDCartLineI> i = cart.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI item = i.next();
			if (item.isWine()) {
				taxValue += item.getTaxValue();
			}
		}
		return taxValue;
	}

	public double getSubTotalWithoutWineAndSpirit(FDCartI cart) {
		double subTotal = 0.0;
		for (Iterator<FDCartLineI> i = cart.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI line = i.next();
			if (!line.isWine()) {
				subTotal += line.getPrice();
			}
		}
		return subTotal;
	}

	public double getSubTotalOnlyWineAndSpirit(FDCartI cart) {
		double subTotal = 0.0;
		for (Iterator<FDCartLineI> i = cart.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI line = i.next();
			if (line.isWine()) {
				subTotal += line.getPrice();
			}
		}
		return subTotal;
	}
}
