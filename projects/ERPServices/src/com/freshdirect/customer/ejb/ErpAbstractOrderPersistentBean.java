
package com.freshdirect.customer.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import javax.ejb.EJBException;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;
import com.freshdirect.referral.extole.model.FDRafTransModel;

/**
 * ErpAbstractOrder persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
abstract class ErpAbstractOrderPersistentBean extends ErpTransactionPersistentBean {
	
	private ErpAbstractOrderModel model;
	
	/** Default constructor. */
	public ErpAbstractOrderPersistentBean() {
		super();
		this.model = this.createModel();
	}
	
	public ErpAbstractOrderPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/** Load constructor. */
	public ErpAbstractOrderPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/** Load constructor. */
	public ErpAbstractOrderPersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn, rs);
	}


	/**
	 * Copy constructor, from model.
	 * @param bean ErpAbstractOrderModel to copy from
	 */
	public ErpAbstractOrderPersistentBean(ErpAbstractOrderModel model) {
		this();
		this.setFromModel(model);
	}

	/**
	 * Template method to create a concrete instance of ErpAbstractOrderModel.
	 */
	protected abstract ErpAbstractOrderModel createModel();

	/**
	 * Template method to get the tx type.
	 */
	protected abstract EnumTransactionType getTransactionType();

	/**
	 * Copy into model.
	 * @return ErpAbstractOrderModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpAbstractOrderModel)model;
		this.setModified();
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, PRICING_DATE, REQUESTED_DATE, SUB_TOTAL, TAX, CUST_SRVC_MESSAGE, MARKETING_MESSAGE,INITIATOR, GL_CODE, CUSTOMER_ID, BUFFER_AMT, TAXATION_TYPE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp(this.model.getTransactionDate().getTime()));
		ps.setString(4, this.model.getTransactionType().getCode());
		ps.setBigDecimal(5, new BigDecimal(String.valueOf(this.model.getAmount())));		
		ps.setString(6, this.model.getTransactionSource().getCode());
		ps.setDate(7, new java.sql.Date(this.model.getPricingDate().getTime()));
		ps.setDate(8, new java.sql.Date(this.model.getRequestedDate().getTime()));
		ps.setBigDecimal(9, new BigDecimal(String.valueOf(this.model.getSubTotal())));
		ps.setBigDecimal(10, new BigDecimal(String.valueOf(this.model.getTax())));
		ps.setString(11, this.model.getCustomerServiceMessage());
		ps.setString(12, this.model.getMarketingMessage());
		ps.setString(13, this.model.getTransactionInitiator());
		ps.setString(14, this.model.getGlCode());
		ps.setString(15, this.model.getCustomerId());
		ps.setBigDecimal(16, new BigDecimal(String.valueOf(this.model.getBufferAmt())));
		ps.setString(17, model.getTaxationType()!=null?model.getTaxationType().getCode():"");
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
			ps = null;
		}

		// create children
		OrderLineList oList = this.getOrderLinePBList();
		oList.create(conn);

		// create delivery info
		ErpDeliveryInfoPersistentBean diPB = new ErpDeliveryInfoPersistentBean(model.getDeliveryInfo());
		diPB.setParentPK(this.getPK());
		diPB.create( conn );

		// create payment info. In case of gift card only order there will be a dummy payment info generated. 
		ErpPaymentInfoPersistentBean piPB = new ErpPaymentInfoPersistentBean((ErpPaymentMethodModel)model.getPaymentMethod());
		piPB.setParentPK(this.getPK());
		piPB.create( conn );

		// create applied credits
		AppliedCreditList aList = this.getAppliedCredtPBList();
		aList.create(conn);

		// create charges
		ChargeList cList = this.getChargePBList();
		cList.create(conn);

		// create discounts
		DiscountList pList = this.getDiscountPBList();
		pList.create(conn);
		
		// create receipents
		RecepientsList rList = this.getRecepientsPBList();
		rList.setParentPK(this.getPK());
		rList.create(conn);

		/*
		// create gift card payment methods.
		GiftCardPMList gcList = this.getGiftCardPMList();
		gcList.create(conn);
		*/
		// create gift card payment methods.
		AppliedGiftCardList agcList = this.getAppliedGiftCardList();
		agcList.create(conn);
		
		//Coupon related.
		if(null !=this.model.getCouponTransModel()){
			ErpCouponTransactionPersistentBean ctPB = new ErpCouponTransactionPersistentBean(this.model.getCouponTransModel());
			ctPB.setParentPK(this.getPK());
			ctPB.create( conn );
		}
		
		//RAF related
		if(null !=this.model.getRafTransModel()){
			ErpRafTransactionPersistentBean rtPB = new ErpRafTransactionPersistentBean(this.model.getRafTransModel());
			rtPB.setParentPK(this.getPK());
			rtPB.create( conn );
		}
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, PRICING_DATE, REQUESTED_DATE, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, SUB_TOTAL, TAX, CUST_SRVC_MESSAGE, MARKETING_MESSAGE,INITIATOR, GL_CODE, BUFFER_AMT, TAXATION_TYPE FROM CUST.SALESACTION WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(conn, rs);
		} else {
			throw new SQLException("No such ErpAbstractOrder PK: " + this.getPK());
		}
		rs.close();
		ps.close();
	}

	private void loadFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
		this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
		this.model.setPricingDate(rs.getDate("PRICING_DATE"));
		this.model.setRequestedDate(rs.getDate("REQUESTED_DATE"));
		this.model.setSubTotal(rs.getDouble("SUB_TOTAL"));
		this.model.setTax(rs.getDouble("TAX"));
		this.model.setCustomerServiceMessage(rs.getString("CUST_SRVC_MESSAGE"));
		this.model.setMarketingMessage(rs.getString("MARKETING_MESSAGE"));
		this.model.setTransactionInitiator(rs.getString("INITIATOR"));
		this.model.setGlCode(rs.getString("GL_CODE"));
		this.model.setBufferAmt(rs.getDouble("BUFFER_AMT"));
		String taxationType = rs.getString("TAXATION_TYPE");
		this.model.setTaxationType((null!=taxationType && !"".equals(taxationType))?EnumNotificationType.getNotificationType(taxationType):null);
		
		// load children
		OrderLineList oList = new OrderLineList();
		oList.setParentPK(this.getPK());
		oList.load(conn);
		this.model.setOrderLines(oList.getModelList());

		// load customer info
		ErpDeliveryInfoPersistentBean diPB = new ErpDeliveryInfoPersistentBean();
		diPB.setParentPK(this.getPK());
		diPB.load(conn);
		this.model.setDeliveryInfo((ErpDeliveryInfoModel)diPB.getModel());

		// load payment info
		ErpPaymentInfoPersistentBean piPB = new ErpPaymentInfoPersistentBean();
		piPB.setParentPK(this.getPK());
		piPB.load(conn);
		this.model.setPaymentMethod((ErpPaymentMethodModel)piPB.getModel());

		// load applied credits
		AppliedCreditList aList = new AppliedCreditList();
		aList.setParentPK(this.getPK());
		aList.load(conn);
		this.model.setAppliedCredits(aList.getModelList());

		// load charges
		ChargeList cList = new ChargeList();
		cList.setParentPK(this.getPK());
		cList.load(conn);
		this.model.setCharges(cList.getModelList());

		// load discounts
		DiscountList pList = new DiscountList();
		pList.setParentPK(this.getPK());
		pList.load(conn);
		this.model.setDiscounts(pList.getModelList());
		/*
		// load Gift card Payment Methods.
		GiftCardPMList gcList = new GiftCardPMList();
		gcList.setParentPK(this.getPK());
		gcList.load(conn);
		this.model.setGiftcardPaymentMethods(gcList.getModelList());
		*/

		// load Receipents
		RecepientsList rList = new RecepientsList();
		rList.setParentPK(this.getPK());
		rList.load(conn);
		this.model.setRecepientsList(rList.getModelList());

		// load Gift card Payment Methods.
		AppliedGiftCardList agcList = new AppliedGiftCardList();
		agcList.setParentPK(this.getPK());
		agcList.load(conn);
		this.model.setAppliedGiftcards(agcList.getModelList());
		
		// FIXME compatibility code for old-style discounts
		// if there's a header level discount not represented as discount, add as discountline 
	
		int type = rs.getInt("PROMOTION_TYPE");
		if (!rs.wasNull() && this.model.getDiscounts().isEmpty()) {
			EnumDiscountType promotionType = EnumDiscountType.getPromotionType(type);
			if (promotionType == null) {
				throw new EJBException("Unknown promotion type " + type);
			}
			Discount d = new Discount(rs.getString("PROMOTION_CAMPAIGN"), promotionType, rs.getDouble("PROMOTION_AMT"));
			this.model.addDiscount(new ErpDiscountLineModel(d));
		}
		
		RafTransList rafTransList = new RafTransList();
		rafTransList.setParentPK(this.getPK());
		rafTransList.load(conn);
		this.model.setRafTransModel(null !=rafTransList.getModelList() && rafTransList.getModelList().size() > 0 ? ((FDRafTransModel)rafTransList.getModelList().get(0)):null);

		this.unsetModified();
	}
	
	protected OrderLineList getOrderLinePBList() {
		OrderLineList lst = new OrderLineList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getOrderLines().iterator(); i.hasNext(); ){
			lst.add(new ErpOrderLinePersistentBean((ErpOrderLineModel)i.next()));
		}
		return lst;
	}
	
	protected AppliedCreditList getAppliedCredtPBList() {
		AppliedCreditList lst = new AppliedCreditList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getAppliedCredits().iterator(); i.hasNext(); ){
			lst.add(new ErpAppliedCreditPersistentBean((ErpAppliedCreditModel)i.next()));
		}
		return lst;
	}
	
	protected ChargeList getChargePBList() {
		ChargeList lst = new ChargeList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getCharges().iterator(); i.hasNext(); ){
			lst.add(new ErpChargeLinePersistentBean((ErpChargeLineModel)i.next()));
		}
		return lst;
	}

	protected DiscountList getDiscountPBList() {
		DiscountList lst = new DiscountList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getDiscounts().iterator(); i.hasNext(); ){
			lst.add(new ErpDiscountLinePersistentBean((ErpDiscountLineModel)i.next()));
		}
		return lst;
	}
	
	protected RecepientsList getRecepientsPBList() {
		RecepientsList lst = new RecepientsList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getRecipientsList().iterator(); i.hasNext(); ){
			lst.add(new ErpGCRecipientPersitanceBean((ErpRecipentModel)i.next()));
		}
		return lst;
	}
	/*
	protected GiftCardPMList getGiftCardPMList() {
		GiftCardPMList lst = new GiftCardPMList();
		System.out.println("Payment gift cards "+model.getGiftcardPaymentMethods().size());
	//	System.out.println("Payment gift cards %%%%%%%%%% "+this.model.getGiftcardPaymentMethods().size());
		lst.setParentPK(this.getPK());
		if(null != this.model.getGiftcardPaymentMethods()) {
			for(Iterator i = this.model.getGiftcardPaymentMethods().iterator(); i.hasNext(); ){
				ErpPaymentMethodModel gcModel = (ErpPaymentMethodModel)i.next();
				//clear PK before creation.
				gcModel.setPK(null);
				lst.add(new ErpGiftCardPaymentInfoPersistentBean(gcModel));
			}
		}
		return lst;
	}
	*/
	protected AppliedGiftCardList getAppliedGiftCardList() {
		AppliedGiftCardList lst = new AppliedGiftCardList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getAppliedGiftcards().iterator(); i.hasNext(); ){
			lst.add(new ErpAppliedGiftCardPersistentBean((ErpAppliedGiftCardModel)i.next()));
		}
		return lst;
	}
	
	
	private static class OrderLineList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpOrderLinePersistentBean.findByParent(conn, (PrimaryKey) OrderLineList.this.getParentPK()));
	    }
	}

	private static class AppliedCreditList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpAppliedCreditPersistentBean.findByParent(conn, (PrimaryKey) AppliedCreditList.this.getParentPK()));
	    }
	}

	private static class ChargeList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpChargeLinePersistentBean.findByParent(conn, (PrimaryKey) ChargeList.this.getParentPK()));
		}
	}
	private static class DiscountList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpDiscountLinePersistentBean.findByParent(conn, (PrimaryKey) DiscountList.this.getParentPK()));
		}
	}
	
	private static class RecepientsList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpGCRecipientPersitanceBean.findByParent(conn, (PrimaryKey) RecepientsList.this.getParentPK()));
		}
	}
	/*
	private static class GiftCardPMList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpGiftCardPaymentInfoPersistentBean.findByParent(conn, (PrimaryKey) GiftCardPMList.this.getParentPK()));
	    }
	}
	*/
	private static class AppliedGiftCardList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpAppliedGiftCardPersistentBean.findByParent(conn, (PrimaryKey) AppliedGiftCardList.this.getParentPK()));
	    }
	}
	
	private static class RafTransList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpRafTransactionPersistentBean.findByParent(conn, (PrimaryKey) RafTransList.this.getParentPK()));
	    }
	}
}
