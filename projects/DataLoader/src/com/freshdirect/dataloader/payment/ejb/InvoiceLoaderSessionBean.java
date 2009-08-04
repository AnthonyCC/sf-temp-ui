package com.freshdirect.dataloader.payment.ejb;

import java.util.ArrayList;
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
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.mail.FDEmailFactory;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ejb.MailerGatewayHome;
import com.freshdirect.mail.ejb.MailerGatewaySB;

public class InvoiceLoaderSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(InvoiceLoaderSessionBean.class);
	private final static ServiceLocator LOCATOR = new ServiceLocator();

	public void addAndReconcileInvoice(String saleId, ErpInvoiceModel invoice, ErpShippingInfo shippingInfo)
		throws ErpTransactionException {
		
		try {
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
				throw new EJBException("Sale#: " + saleId + " is not in correct status to add invoice");
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

				List pList = new ArrayList();
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
				for(Iterator i = invoice.getCharges().iterator(); i.hasNext(); ) {
					ErpChargeLineModel invCharge = (ErpChargeLineModel) i.next();
					ErpChargeLineModel orderCharge = order.getCharge(invCharge.getType());
					
					if(orderCharge != null) {
						invCharge.setTaxRate(orderCharge.getTaxRate());
					}
				}
			}
			
			
			ErpCustomerManagerSB managerSB = this.getErpCustomerManagerHome().create();
			managerSB.addInvoice(invoice, saleId, shippingInfo);
			managerSB.reconcileSale(saleId);
			
			// get salemodel w/ invoice	
			saleModel = (ErpSaleModel)eb.getModel();
			FDOrderAdapter fdOrder = new FDOrderAdapter(saleModel);
			
			PrimaryKey customerPK = eb.getCustomerPk();
			
			ErpCustomerEB customerEB = this.getErpCustomerHome().findByPrimaryKey(customerPK);
			ErpCustomerInfoModel erpInfo = ((ErpCustomerModel)customerEB.getModel()).getCustomerInfo();
			FDCustomerInfo fdInfo = new FDCustomerInfo(erpInfo.getFirstName(), erpInfo.getLastName());
			fdInfo.setHtmlEmail(!erpInfo.isEmailPlaintext());
			fdInfo.setEmailAddress(erpInfo.getEmail());
			
			MailerGatewaySB mailBean = this.getMailerGatewayHome().create();
			mailBean.enqueueEmail(FDEmailFactory.getInstance().createFinalAmountEmail(fdInfo, fdOrder));
			
			// collect recipes that will be sent to the users
			List orderLines = fdOrder.getOrderLines();
			Set  recipes    = new HashSet();
			for (Iterator it = orderLines.iterator(); it.hasNext();) {
				FDCartLineI cartLine = (FDCartLineI) it.next();
				String		recipeId = cartLine.getRecipeSourceId();
				
				if (recipeId != null && cartLine.isRequestNotification()) {
					Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNode(recipeId);
					recipes.add(recipe);
				}
			}
			
			// send recipe e-mails on delivery for those which were requested
			for (Iterator it = recipes.iterator(); it.hasNext();) {
				Recipe recipe = (Recipe) it.next();
				
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
			return (ErpSaleHome) LOCATOR.getRemoteHome("freshdirect.erp.Sale", ErpSaleHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.CustomerManager", ErpCustomerManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
	
	private ErpCustomerHome getErpCustomerHome() {
		try {
			return (ErpCustomerHome) LOCATOR.getRemoteHome("freshdirect.erp.Customer", ErpCustomerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private MailerGatewayHome getMailerGatewayHome() {
		try {
			return (MailerGatewayHome) LOCATOR.getRemoteHome("freshdirect.mail.MailerGateway", MailerGatewayHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
}
