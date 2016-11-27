package com.freshdirect.webapp.ajax.reorder.service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;

public class QuickShopSortingService {

	private static final QuickShopSortingService INSTANCE = new QuickShopSortingService();

	/** Sorts items by delivery date */
	private final static Comparator<FilteringSortingItem<QuickShopLineItemWrapper>> WRAPPED_DELIVERY_DATE_COMPARATOR = new Comparator<FilteringSortingItem<QuickShopLineItemWrapper>>() {
		@Override
		public int compare(FilteringSortingItem<QuickShopLineItemWrapper> o0, FilteringSortingItem<QuickShopLineItemWrapper> o1) {
			return DELIVERY_DATE_COMPARATOR.compare(o0.getNode(), o1.getNode());
		}
	};

	private static final Comparator<FilteringSortingItem<QuickShopLineItemWrapper>> WRAPPED_ORDER_ID_COMPARATOR = new Comparator<FilteringSortingItem<QuickShopLineItemWrapper>>() {

		@Override
		public int compare(FilteringSortingItem<QuickShopLineItemWrapper> o1, FilteringSortingItem<QuickShopLineItemWrapper> o2) {
			return ORDER_ID_COMPARATOR.compare(o1.getNode(), o2.getNode());
		}
	};

	@SuppressWarnings("unchecked")
	private static final ComparatorChain<FilteringSortingItem<QuickShopLineItemWrapper>> WRAPPED_DELIVERY_DATE_AND_ORDER_ID_COMPARATOR = ComparatorChain.create(WRAPPED_DELIVERY_DATE_COMPARATOR,
			WRAPPED_ORDER_ID_COMPARATOR);

	private final static Comparator<QuickShopLineItemWrapper> DELIVERY_DATE_COMPARATOR = new Comparator<QuickShopLineItemWrapper>() {
		@Override
		public int compare(QuickShopLineItemWrapper o0, QuickShopLineItemWrapper o1) {
			return o1.getDeliveryDate().compareTo(o0.getDeliveryDate());
		}
	};

	private static final Comparator<QuickShopLineItemWrapper> ORDER_ID_COMPARATOR = new Comparator<QuickShopLineItemWrapper>() {

		@Override
		public int compare(QuickShopLineItemWrapper o1, QuickShopLineItemWrapper o2) {
			BigInteger orderId1 = new BigInteger(o1.getOrderId());
			BigInteger orderId2 = new BigInteger(o2.getOrderId());
			return orderId2.compareTo(orderId1);
		}
	};

	@SuppressWarnings("unchecked")
	private static final ComparatorChain<QuickShopLineItemWrapper> DELIVERY_DATE_AND_ORDER_ID_COMPARATOR = ComparatorChain.create(DELIVERY_DATE_COMPARATOR, ORDER_ID_COMPARATOR);

	private QuickShopSortingService() {
	}

	public static QuickShopSortingService defaultService() {
		return INSTANCE;
	}

	public void sortByDeliveryDateAndOrderId(List<QuickShopLineItemWrapper> items) {
		if (items != null) {
			Collections.sort(items, DELIVERY_DATE_AND_ORDER_ID_COMPARATOR);
		}
	}

	public void sortByWrappedDeliveryDateAndOrderId(List<FilteringSortingItem<QuickShopLineItemWrapper>> items) {
		Collections.sort(items, WRAPPED_DELIVERY_DATE_AND_ORDER_ID_COMPARATOR);
	}
}
