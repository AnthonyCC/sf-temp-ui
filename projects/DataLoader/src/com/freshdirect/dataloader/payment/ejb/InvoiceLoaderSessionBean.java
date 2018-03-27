package com.freshdirect.dataloader.payment.ejb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpCustomerInfoHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.dataloader.analytics.GoogleAnalyticsReportingService;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.Recipe;

public class InvoiceLoaderSessionBean extends SessionBeanSupport {

	/**
     *
     */
    private static final long serialVersionUID = 1L;
    private static Category LOGGER = LoggerFactory.getInstance(InvoiceLoaderSessionBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();

	public void addAndReconcileInvoice(String saleId, ErpInvoiceModel invoice, ErpShippingInfo shippingInfo)
		throws ErpTransactionException {

		try {
			Boolean isShorted = false;
			ErpSaleEB eb = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));

			ErpSaleModel saleModel = (ErpSaleModel)eb.getModel();

			EnumSaleStatus status = saleModel.getStatus();

            if (status.equals(EnumSaleStatus.ENROUTE) || status.equals(EnumSaleStatus.PAYMENT_PENDING) || status.equals(EnumSaleStatus.REFUSED_ORDER)
                || status.equals(EnumSaleStatus.PENDING) || status.equals(EnumSaleStatus.RETURNED) || status.equals(EnumSaleStatus.REDELIVERY)
                || status.equals(EnumSaleStatus.CAPTURE_PENDING))
            {
                ErpInvoiceModel invoiceOnSale = saleModel.getFirstInvoice();
				if (invoiceOnSale.equals(invoice)) {
					LOGGER.info("Got duplicate invoice for sale#: " + saleId);
					return;
				} else {
					throw new EJBException("Got another different invoice for sale#: " + saleId);
				}

			}

			if (!status.equals(EnumSaleStatus.INPROCESS)) {
				throw new EJBException("Sale#: " + saleId + "["+status.getStatusCode()+"] is not in correct status[PRC] to add invoice");
			}

			// FIXME fix Discount promotionCode, since parser cannot provide it

			List invDiscountLines = invoice.getDiscounts();
			List oldDiscountLines = saleModel.getRecentOrderTransaction().getDiscounts();
			if (invDiscountLines != null && !invDiscountLines.isEmpty() && oldDiscountLines != null && !oldDiscountLines.isEmpty()) {

				if (oldDiscountLines.size() != invDiscountLines.size()) {
					throw new EJBException("Discount line count mismatch, expected "
						+ oldDiscountLines.size()
						+ ", invoice had "
						+ invDiscountLines.size());
				}

				// FIXME furhter validation should be performed to ensure discount lines match up (no can do, w/o promo codes)

				List<ErpDiscountLineModel> pList = new ArrayList<ErpDiscountLineModel>();
				for (Iterator invPromosIter = invDiscountLines.iterator(), oldPromosIter = oldDiscountLines.iterator();
					invPromosIter.hasNext() && oldPromosIter.hasNext(); ) {
					ErpDiscountLineModel  invDiscountLine = (ErpDiscountLineModel) invPromosIter.next();
					ErpDiscountLineModel  oldDiscountLine = (ErpDiscountLineModel) oldPromosIter.next();
					Discount invDisc = invDiscountLine.getDiscount();
					Discount oldDisc = oldDiscountLine.getDiscount();
					pList.add(new ErpDiscountLineModel(new Discount(oldDisc.getPromotionCode(), oldDisc.getDiscountType(), invDisc.getAmount())));
				}
				invoice.setDiscounts(pList);
			}

			// !!! fix Delivery charge tax
			double tax = invoice.getTax();
			if(tax > 0){
				ErpAbstractOrderModel order = saleModel.getRecentOrderTransaction();
				for (Object element : invoice.getCharges()) {
					ErpChargeLineModel invCharge = (ErpChargeLineModel) element;
					ErpChargeLineModel orderCharge = order.getCharge(invCharge.getType());

					if(orderCharge != null) {
						invCharge.setTaxRate(orderCharge.getTaxRate());
					}
				}
			}


			ErpCustomerManagerSB managerSB = this.getErpCustomerManagerHome().create();
			managerSB.addInvoice(invoice, saleId, shippingInfo);
			managerSB.reconcileSale(saleId, isShorted);
			LOGGER.debug("Completed reconcile Sale method for sale id " + saleId);

			// get salemodel w/ invoice
			saleModel = (ErpSaleModel)eb.getModel();
			FDOrderAdapter fdOrder = new FDOrderAdapter(saleModel);
	/*		if(isShorted && FDStoreProperties.getAvalaraTaxEnabled() && saleModel.getFirstOrderTransaction().getTaxationType().equals(EnumNotificationType.AVALARA)){
				AvalaraContext avalaraContext = new AvalaraContext(fdOrder);
				fdOrder.getAvalaraTaxValue(avalaraContext);
			}*/
			Collections.sort(fdOrder.getOrderLines(), FDCartModel.PRODUCT_SAMPLE_COMPARATOR);

			PrimaryKey customerPK = eb.getCustomerPk();

			/*ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(customerPK);
			ErpCustomerInfoModel erpInfo = ((ErpCustomerModel)customerEB.getModel()).getCustomerInfo();*/
			ErpCustomerInfoModel erpInfo = (ErpCustomerInfoModel) this.getErpCustomerInfoHome().findByErpCustomerId(customerPK.getId()).getModel();
			FDCustomerInfo fdInfo = new FDCustomerInfo(erpInfo.getFirstName(), erpInfo.getLastName());
			fdInfo.setHtmlEmail(!erpInfo.isEmailPlaintext());
			fdInfo.setEmailAddress(erpInfo.getEmail());
			fdInfo.setGoGreen(erpInfo.isGoGreen());
			LOGGER.debug("Start - Sending Invoice email:"+saleId);
			MailerGatewaySB mailBean = this.getMailerGatewayHome().create();
			try {
				
				mailBean.enqueueEmail(FDEmailFactory.getInstance().createFinalAmountEmail(fdInfo, fdOrder));
			} catch (Exception e1) {
				LOGGER.warn("Unexpected Exception while sending invoice email, for order#: "+saleId, e1);
				e1.printStackTrace();
			}
			LOGGER.debug("End - Sending Invoice email:"+saleId);
            try {
                if (saleModel.geteStoreId() == EnumEStoreId.FD) {
                	LOGGER.info("Start - Sending Invoice GAEvent:"+saleId);
                    GoogleAnalyticsReportingService.defaultService().postGAReporting(fdOrder);
                    LOGGER.info("End - Sending Invoice GAEvent:"+saleId);
                } else {
                	LOGGER.info("EStore Filtered - Not Sending Invoice GAEvent:"+saleId);
                }
			} catch (Exception e) {
				LOGGER.warn("Unexpected Exception in GoogleAnalyticsReportingService while reported to GA, for order#: "+saleId, e);
			}

			// collect recipes that will be sent to the users
			List orderLines = fdOrder.getOrderLines();
			Set<Recipe>  recipes    = new HashSet<Recipe>();
			for (Iterator it = orderLines.iterator(); it.hasNext();) {
				FDCartLineI cartLine = (FDCartLineI) it.next();
				String		recipeId = cartLine.getRecipeSourceId();

				if (recipeId != null && cartLine.isRequestNotification()) {
					Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
					recipes.add(recipe);
				}
			}

			// send recipe e-mails on delivery for those which were requested
			for (Recipe recipe : recipes) {
				mailBean.enqueueEmail(FDEmailFactory.getInstance().createRecipeEmail(fdInfo, recipe));
			}

		} catch (ErpTransactionException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.warn("Unexpected Exception while trying to process invoice for order#: "+saleId, e);
			throw new EJBException("Unexpected Exception while trying to process invoice for order#: "+saleId, e);
		}
	}

	private ErpSaleHome getErpSaleHome() {
		try {
			return (ErpSaleHome) LOCATOR.getRemoteHome("freshdirect.erp.Sale");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.CustomerManager");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private MailerGatewayHome getMailerGatewayHome() {
		try {
			return (MailerGatewayHome) LOCATOR.getRemoteHome("freshdirect.mail.MailerGateway");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpCustomerInfoHome getErpCustomerInfoHome() {
		try {
			return (ErpCustomerInfoHome) LOCATOR.getRemoteHome(FDStoreProperties.getErpCustomerInfoHome());
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

}
