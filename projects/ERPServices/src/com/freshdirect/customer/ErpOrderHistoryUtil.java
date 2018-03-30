package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.ewallet.EnumEwalletType;
import com.freshdirect.payment.EnumPaymentMethodType;


public class ErpOrderHistoryUtil {

    public static int getDeliveredOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (saleInfo.isDelivered()) {
				ret++;
			}
		}
		return ret;
	}

    public static int getSettledOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (saleInfo.isSettled()) {
				ret++;
			}
		}
		return ret;
	}
    
    public static int getSettledOrderCountByStore(Collection<ErpSaleInfo> erpSaleInfos, EnumEStoreId estoreId) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (saleInfo.isSettled() && saleInfo.geteStore().equals(estoreId)) {
				ret++;
			}
		}
		return ret;
	}
	
    public static int getPhoneOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (EnumTransactionSource.CUSTOMER_REP.equals(saleInfo.getSource())) {
				ret++;
			}
		}
		return ret;
	}

    public static int getReturnOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
            if (saleInfo.getStatus().isReturned())
				ret++;
		}
		return ret;
	}

    public static int getTotalOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		return erpSaleInfos.size();
	}

    public static int getValidECheckOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (!saleInfo.getStatus().isCanceled() && EnumPaymentMethodType.ECHECK.equals(saleInfo.getPaymentMethodType())){
				ret++;
			}
		}
		return ret;
	}
	
    public static int getValidMasterPassOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (null !=saleInfo.getEwalletType() && !saleInfo.getStatus().isCanceled() && EnumEwalletType.MP.equals(saleInfo.getEwalletType())){
				ret++;
			}
		}
		return ret;
	}

    public static int getValidOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
        int count = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
            if (!saleInfo.getStatus().isCanceled()) {
                count++;
            }
        }
        return count;
	}
	
    public static int getValidOrderCount(Collection<ErpSaleInfo> erpSaleInfos, EnumDeliveryType deliveryType) {
        int count = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
            if ((null == deliveryType || deliveryType.equals(saleInfo.getDeliveryType())) && !saleInfo.getStatus().isCanceled()) {
                count++;
            }
        }
        return count;
	}

    public static int getValidOrderCount(Collection<ErpSaleInfo> erpSaleInfos, EnumEStoreId storeId) {
        int count = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
            if ((null == storeId || storeId.equals(saleInfo.geteStore())) && !saleInfo.getStatus().isCanceled()) {
                count++;
            }
        }
        return count;
    }
    
    public static int getValidOrderCount(Collection<ErpSaleInfo> erpSaleInfos, String salesOrg) {
        int count = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
            if ((null != salesOrg && salesOrg.equals(saleInfo.getSalesOrg())) && !saleInfo.getStatus().isCanceled()) {
                count++;
            }
        }
        return count;
    }

    public static int getValidPhoneOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (!saleInfo.getStatus().isCanceled() && EnumTransactionSource.CUSTOMER_REP.equals(saleInfo.getSource())) {
				ret++;
			}
		}
		return ret;
	}

    public static Date getFirstOrderDate(Collection<ErpSaleInfo> erpSaleInfos) {
		Date date = null;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			Date createDate = saleInfo.getCreateDate();
			if (date==null || createDate.before(date)) {
				date = createDate;
			}
		}
		return date;
	}

    public static Date getFirstOrderDateByStore(Collection<ErpSaleInfo> erpSaleInfos, EnumEStoreId estoreId) {
		Date date = null;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			Date createDate = saleInfo.getCreateDate();
			if ((null == estoreId || estoreId.equals(saleInfo.geteStore())) && (date==null || createDate.before(date))) {
				date = createDate;
			}
		}
		return date;
	}
	
    public static Date getFirstNonPickupOrderDate(Collection<ErpSaleInfo> erpSaleInfos) {
		Date date = null;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (!saleInfo.getStatus().isCanceled() && !EnumDeliveryType.PICKUP.equals(saleInfo.getDeliveryType()) ) {
				Date createDate = saleInfo.getCreateDate();
				if (date==null || createDate.before(date)) {
					date = createDate;
				}
			}
		}
		return date;
	}

    public static ErpSaleInfo getLastSale(Collection<ErpSaleInfo> erpSaleInfos) {
		return getLastSaleBefore(erpSaleInfos, null);
	}

    public static String getLastOrderId(Collection<ErpSaleInfo> erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getSaleId();
	}

    public static Date getLastOrderCreateDate(Collection<ErpSaleInfo> erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getCreateDate();
	}

    public static Date getLastOrderDlvDate(Collection<ErpSaleInfo> erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getRequestedDate();
	}
	
    public static EnumDeliveryType getLastOrderType(Collection<ErpSaleInfo> erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getDeliveryType();
	}
	
    public static String getLastOrderZone(Collection<ErpSaleInfo> erpSaleInfos) {
		ErpSaleInfo lastOrder = getLastSale(erpSaleInfos);
		return lastOrder==null ? null : lastOrder.getZone();
	}
	
    private static ErpSaleInfo getLastSaleBefore(Collection<ErpSaleInfo> erpSaleInfos, Date maxCreateDate) {
		ErpSaleInfo lastOrder = null;
		Date date = null;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			Date createDate = saleInfo.getCreateDate();
			if ((date == null || createDate.after(date)) && (maxCreateDate==null || maxCreateDate.after(createDate))) {
				date = createDate;
				lastOrder = saleInfo;
			}
		}
		return lastOrder;
	}

    public static ErpSaleInfo getSecondToLastSale(Collection<ErpSaleInfo> erpSaleInfos) {
		ErpSaleInfo lastSale = getLastSale(erpSaleInfos);
		return lastSale == null ? null : getLastSaleBefore(erpSaleInfos, lastSale.getCreateDate());
	}
	
    public static String getSecondToLastSaleId(Collection<ErpSaleInfo> erpSaleInfos) {
		ErpSaleInfo secondToLastOrder = getSecondToLastSale(erpSaleInfos);
		return secondToLastOrder==null ? null : secondToLastOrder.getSaleId();
	}
	
    public static int getOrderCountForChefsTableEligibility(Collection<ErpSaleInfo> erpSaleInfos) {
		Calendar beginCal = Calendar.getInstance();
		beginCal.set(Calendar.DAY_OF_MONTH, 1);
		Calendar endCal = Calendar.getInstance();
		beginCal.add(Calendar.MONTH, -2);
		int orderCount = 0;
		Date beginDate = beginCal.getTime();
		Date endDate = endCal.getTime();
	
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			Date deliveryDate = saleInfo.getRequestedDate();

			if (!saleInfo.isMakeGood()&&deliveryDate.after(beginDate) && deliveryDate.before(endDate) && 
					saleInfo.getDeliveryType()!=null && !saleInfo.getDeliveryType().equals(EnumDeliveryType.CORPORATE) &&
					!saleInfo.getStatus().equals(EnumSaleStatus.CANCELED) &&
					!saleInfo.getSaleType().equals(EnumSaleType.SUBSCRIPTION)) {
				orderCount++;
			}
		}
		return orderCount;
	}
	
	
    public static double getOrderSubTotalForChefsTableEligibility(Collection<ErpSaleInfo> erpSaleInfos) {
		double amount=0.0;
		Calendar beginCal = Calendar.getInstance();
		beginCal.set(Calendar.DAY_OF_MONTH, 1);
		Calendar endCal = Calendar.getInstance();
		beginCal.add(Calendar.MONTH, -2);
		Date beginDate = beginCal.getTime();
		Date endDate = endCal.getTime();
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			Date deliveryDate = saleInfo.getRequestedDate();
			if (!saleInfo.isMakeGood()&& deliveryDate.after(beginDate) && deliveryDate.before(endDate) && 
					saleInfo.getDeliveryType()!=null && !saleInfo.getDeliveryType().equals(EnumDeliveryType.CORPORATE) &&
					!saleInfo.getStatus().equals(EnumSaleStatus.CANCELED) &&
					!saleInfo.getSaleType().equals(EnumSaleType.SUBSCRIPTION)) {
				amount=amount+saleInfo.getSubTotal();
			}
		}
		return amount;
	}

	public static Collection<ErpSaleInfo> filterOrders(Collection<ErpSaleInfo> erpSaleInfos, EnumSaleType saleType) {
		if(saleType==null) {
			return erpSaleInfos;
		}
        List<ErpSaleInfo> filteredOrders = new ArrayList<ErpSaleInfo>();
		if(erpSaleInfos!=null) {
            for (ErpSaleInfo saleInfo : erpSaleInfos) {
				if(saleType.equals(saleInfo.getSaleType())) {
					filteredOrders.add(saleInfo);
				}
			}
		}
		
		return filteredOrders;
	}

    public static int getTotalRegularOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int intCount = 0;
		if(erpSaleInfos != null) {
            for (ErpSaleInfo saleInfo : erpSaleInfos) {
				if (saleInfo.getSaleType().equals(EnumSaleType.REGULAR)) {
					intCount++;
				}
			}
		}
		return intCount;
	}
	
    public static int getSettledECheckOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (saleInfo.isSettled() && EnumPaymentMethodType.ECHECK.equals(saleInfo.getPaymentMethodType())){
				ret++;
			}
		}
		return ret;
	}
	
    public static int getUnSettledEBTOrderCount(Collection<ErpSaleInfo> erpSaleInfos) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (!saleInfo.isSettled()&& !saleInfo.getStatus().isCanceled() && EnumPaymentMethodType.EBT.equals(saleInfo.getPaymentMethodType())){
				ret++;
			}
		}
		return ret;
	}

    public static int getUnSettledEBTOrderCount(Collection<ErpSaleInfo> erpSaleInfos, String currSaleId) {
		int ret = 0;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (!saleInfo.getSaleId().equalsIgnoreCase(currSaleId)&& !saleInfo.isSettled()&& !saleInfo.getStatus().isCanceled() && EnumPaymentMethodType.EBT.equals(saleInfo.getPaymentMethodType())){
				ret++;
			}
		}
		return ret;
	}
    
    public static boolean hasSettledOrders(Collection<ErpSaleInfo> erpSaleInfos) {
		boolean hasSettledOrder = false;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (saleInfo.isSettled()) {
				hasSettledOrder =true;
				break;
			}
		}
		return hasSettledOrder;
	}
    
    public static boolean hasSettledOrders(Collection<ErpSaleInfo> erpSaleInfos, EnumEStoreId estoreId) {
		boolean hasSettledOrder = false;
        for (ErpSaleInfo saleInfo : erpSaleInfos) {
			if (saleInfo.isSettled() && estoreId.equals(saleInfo.geteStore())) {
				hasSettledOrder =true;
				break;
			}
		}
		return hasSettledOrder;
	}
}
