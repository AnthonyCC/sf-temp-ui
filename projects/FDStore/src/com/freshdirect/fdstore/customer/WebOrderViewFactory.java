package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpInvoiceLineI;

/**
 * @author vszathmary
 */
public class WebOrderViewFactory {

	private WebOrderViewFactory() {
	}

	public static WebOrderViewI getOrderView(List cartLines, ErpAffiliate affiliate) {
		if (!affiliate.isPrimary() && !hasItemsForAffiliate(cartLines, affiliate)) {
			return null;
		}

		return new OrderView(affiliate, getOrderLinesForAffiliate(cartLines, affiliate), affiliate.isPrimary()
			&& !hasItemsForSecondaryAffiliates(cartLines));
	}

	public static WebOrderViewI getInvoicedOrderView(List cartLines, List sampleLines, ErpAffiliate affiliate) {
		if (!affiliate.isPrimary() && !hasItemsForAffiliate(cartLines, affiliate)) {
			return null;
		}

		InvoicedOrderView ov = new InvoicedOrderView(affiliate, getOrderLinesForAffiliate(cartLines, affiliate), affiliate
			.isPrimary()
			&& !hasItemsForSecondaryAffiliates(cartLines));

		if (affiliate.isPrimary()) {
			List deliveredSamples = new ArrayList();
			for (Iterator i = sampleLines.iterator(); i.hasNext();) {
				FDCartLineI line = (FDCartLineI) i.next();
				if (line.getInvoiceLine().getQuantity() > 0) {
					deliveredSamples.add(line);
				}
			}

			ov.setSampleLines(deliveredSamples);
		}

		return ov;
	}

	public static List getOrderViews(List cartLines) {
		ArrayList views = new ArrayList();
		for (Iterator i = getShownAffiliates(cartLines).iterator(); i.hasNext();) {
			WebOrderViewI view = getOrderView(cartLines, (ErpAffiliate) i.next());
			if (view != null) {
				views.add(view);
			}
		}
		return views;
	}

	public static List getInvoicedOrderViews(List cartLines, List sampleLines) {
		ArrayList views = new ArrayList();
		for (Iterator i = getShownAffiliates(cartLines).iterator(); i.hasNext();) {
			WebOrderViewI view = getInvoicedOrderView(cartLines, sampleLines, (ErpAffiliate) i.next());
			if (view != null) {
				views.add(view);
			}
		}
		return views;
	}

	private static List getShownAffiliates(List cartLines) {
		ErpAffiliate[] affils = new ErpAffiliate[] {
			ErpAffiliate.getEnum(ErpAffiliate.CODE_FD),
			ErpAffiliate.getEnum(ErpAffiliate.CODE_WBL),
			ErpAffiliate.getEnum(ErpAffiliate.CODE_BC)};
		List l = new ArrayList();
		for (int i = 0; i < affils.length; i++) {
			//if (affils[i].isPrimary() || hasItemsForAffiliate(cartLines, affils[i])) {
			if (hasItemsForAffiliate(cartLines, affils[i])) {
				l.add(affils[i]);
			}
		}
		return l;
	}

	private static List getOrderLinesForAffiliate(List cartLines, ErpAffiliate affiliate) {
		List lines = new ArrayList();
		for (Iterator i = cartLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (affiliate.equals(line.getAffiliate())) {
				lines.add(line);
			}
		}
		return lines;
	}

	private static boolean hasItemsForAffiliate(List cartLines, ErpAffiliate affiliate) {
		return getOrderLinesForAffiliate(cartLines, affiliate).size() > 0;
	}

	private static boolean hasItemsForSecondaryAffiliates(List cartLines) {
		for (Iterator i = cartLines.iterator(); i.hasNext();) {
			FDCartLineI line = (FDCartLineI) i.next();
			if (!line.getAffiliate().isPrimary()) {
				return true;
			}
		}
		return false;
	}

	public static abstract class AbstractOrderView implements WebOrderViewI {

		private final ErpAffiliate affiliate;
		private final List lines;
		private final boolean hideDescription;
		private List sampleLines = Collections.EMPTY_LIST;

		protected double tax;
		protected double subtotal;
		protected double depositValue;

		public AbstractOrderView(ErpAffiliate affiliate, List lines, boolean hideDescription) {
			this.affiliate = affiliate;
			this.lines = lines;
			this.hideDescription = hideDescription;
		}

		public ErpAffiliate getAffiliate() {
			return this.affiliate;
		}

		public List getOrderLines() {
			return this.lines;
		}

		public List getSampleLines() {
			return this.sampleLines;
		}

		public void setSampleLines(List sampleLines) {
			this.sampleLines = sampleLines;
		}

		public boolean isDisplayDepartment() {
			return affiliate.isPrimary();
		}

		public String getDescription() {
			return this.hideDescription ? "" : affiliate.getName();
		}

		public double getTax() {
			return tax;
		}

		public double getDepositValue() {
			return depositValue;
		}

		public double getSubtotal() {
			return subtotal;
		}

		public boolean isEstimatedPrice() {
			for (Iterator i = getOrderLines().iterator(); i.hasNext();) {
				FDCartLineI line = (FDCartLineI) i.next();
				if (line.isEstimatedPrice()) {
					return true;
				}
			}
			return false;
		}

	}

	public static class OrderView extends AbstractOrderView {

		public OrderView(ErpAffiliate affiliate, List lines, boolean hideDescription) {
			super(affiliate, lines, hideDescription);

			for (Iterator i = lines.iterator(); i.hasNext();) {
				FDCartLineI line = (FDCartLineI) i.next();
				tax += line.getTaxValue();
				depositValue += line.getDepositValue();
				subtotal += line.getPrice();
			}
		}

	}

	public static class InvoicedOrderView extends AbstractOrderView {

		public InvoicedOrderView(ErpAffiliate affiliate, List lines, boolean hideDescription) {
			super(affiliate, lines, hideDescription);

			for (Iterator i = lines.iterator(); i.hasNext();) {
				FDCartLineI line = (FDCartLineI) i.next();

				ErpInvoiceLineI invoiceLine = line.getInvoiceLine();
				tax += invoiceLine.getTaxValue();
				depositValue += invoiceLine.getDepositValue();
				subtotal += invoiceLine.getPrice();
			}
		}

	}

}

