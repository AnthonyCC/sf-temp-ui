package com.freshdirect.customer.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpAbstractInvoiceModel;
import com.freshdirect.customer.ErpAppliedCreditModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpDiscountLineModel;
import com.freshdirect.customer.ErpInvoiceLineModel;
import com.freshdirect.customer.ErpInvoiceModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MathUtil;
import com.freshdirect.giftcard.ErpAppliedGiftCardModel;


/**
 * ErpInvoice persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpInvoicePersistentBean extends ErpTransactionPersistentBean {
	
	protected ErpInvoiceModel model;
	
	/**
	 * Constructor for ErpInvoicePersistentBean.
	 */
	public ErpInvoicePersistentBean() {
		super();
		this.model = new ErpInvoiceModel();
	}
	
	public ErpInvoicePersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/**
	 * Constructor for ErpInvoicePersistentBean.
	 * @param pk
	 * @param conn
	 * @throws SQLException
	 */
	public ErpInvoicePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.load(conn);
	}

	/**
	 * Constructor for ErpInvoicePersistentBean.
	 * @param pk
	 * @param conn
	 * @param rs
	 * @throws SQLException
	 */
	public ErpInvoicePersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn, rs);
	}

	/**
	 * Constructor for ErpInvoicePersistentBean.
	 * @param model
	 */
	public ErpInvoicePersistentBean(ErpInvoiceModel model) {
		this();
		this.setFromModel(model);
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}
	
	public ModelI getModel() {
		return this.model.deepCopy();
	}
	
	public void setFromModel(ModelI m) {
		this.model = (ErpInvoiceModel)m;
	}
	
	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, INVOICE_NUMBER, TAX, SUB_TOTAL, CUSTOMER_ID) values (?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp(this.model.getTransactionDate().getTime()));
		ps.setString(4, this.getTransactionType().getCode());
		ps.setBigDecimal(5, new BigDecimal(String.valueOf(this.model.getAmount())));
		ps.setString(6, this.model.getTransactionSource().getCode());
		ps.setString(7, this.model.getInvoiceNumber());
		ps.setBigDecimal(8, new BigDecimal(String.valueOf(this.model.getTax())));
		ps.setBigDecimal(9, new BigDecimal(String.valueOf(this.model.getActualSubTotal()))); 
		ps.setString(10, this.model.getCustomerId());	
		
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
		InvoiceLineList ilList = this.getInvoiceLinePBList();
		ilList.create(conn);
		
		AppliedCreditList acList = this.getAppliedCreditPBList();
		acList.create(conn);
		
		ChargeList cList = this.getChargeLinePBList();
		cList.create(conn);
		
		DiscountList pList = this.getDiscountLinePBList();
		pList.create(conn);

		AppliedGiftCardList agcList = this.getAppliedGiftCardList();
		agcList.create(conn);
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, INVOICE_NUMBER, TAX, SUB_TOTAL FROM CUST.SALESACTION WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(conn, rs);
		} else {
			throw new SQLException("No such ErpInvoice PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		
	}
	
	private void loadFromResultSet(Connection conn, ResultSet rs) throws SQLException {
		
		this.model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
		this.model.setAmount(rs.getDouble("AMOUNT"));
		this.model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
		this.model.setInvoiceNumber(rs.getString("INVOICE_NUMBER"));
		this.model.setTax(rs.getDouble("TAX"));
		//this.model.setSubTotal(rs.getDouble("SUB_TOTAL"));
		
		
		// load children
		InvoiceLineList ilList = new InvoiceLineList();
		ilList.setParentPK(this.getPK());
		ilList.load(conn);
		this.model.setInvoiceLines(ilList.getModelList());
		/*
		 * This section sums up all line item discounts if any and subtract the sum from actual sub total.
		 * If no line item discounts then sub total remains the same.
		 */
		double discountAmt = 0.0;
		if(this.model.getInvoiceLines() != null){
			for (Iterator i = model.getInvoiceLines().iterator(); i.hasNext();) {
				ErpInvoiceLineModel invoiceLine = (ErpInvoiceLineModel)i.next();
				discountAmt += MathUtil.roundDecimal(invoiceLine.getActualDiscountAmount());
			}
		}
		this.model.setSubTotal(rs.getDouble("SUB_TOTAL") - discountAmt);
		
		AppliedCreditList acList = new AppliedCreditList();
		acList.setParentPK(this.getPK());
		acList.load(conn);
		this.model.setAppliedCredits(acList.getModelList());
		
		ChargeList cList = new ChargeList();
		cList.setParentPK(this.getPK());
		cList.load(conn);
		this.model.setCharges(cList.getModelList());
		
		DiscountList dList = new DiscountList();
		dList.setParentPK(this.getPK());
		dList.load(conn);
		this.model.setDiscounts(dList.getModelList());
		
		AppliedGiftCardList agcList = new AppliedGiftCardList();
		agcList.setParentPK(this.getPK());
		agcList.load(conn);
		this.model.setAppliedGiftCards(agcList.getModelList());
		
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
		this.unsetModified();
		
	}
	

	/**
	 * Find ErpInvoicePersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpInvoicePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.INVOICE.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpInvoicePersistentBean bean = new ErpInvoicePersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
	
	protected InvoiceLineList getInvoiceLinePBList(){
		InvoiceLineList lst = new InvoiceLineList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getInvoiceLines().iterator(); i.hasNext(); ){
			lst.add(new ErpInvoiceLinePersistentBean((ErpInvoiceLineModel)i.next()));
		}
		return lst;
	}
	
	protected ChargeList getChargeLinePBList(){
		ChargeList lst = new ChargeList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getCharges().iterator(); i.hasNext(); ){
			lst.add(new ErpChargeLinePersistentBean((ErpChargeLineModel)i.next()));
		}
		return lst;
	}
	
	protected AppliedCreditList getAppliedCreditPBList() {
		AppliedCreditList lst = new AppliedCreditList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getAppliedCredits().iterator(); i.hasNext(); ){
			lst.add(new ErpAppliedCreditPersistentBean((ErpAppliedCreditModel)i.next()));
		}
		return lst;
	}
	
	protected DiscountList getDiscountLinePBList() {
		DiscountList lst = new DiscountList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getDiscounts().iterator(); i.hasNext(); ){
			lst.add(new ErpDiscountLinePersistentBean((ErpDiscountLineModel)i.next()));
		}
		return lst;
	}

	protected AppliedGiftCardList getAppliedGiftCardList() {
		AppliedGiftCardList lst = new AppliedGiftCardList();
		lst.setParentPK(this.getPK());
		for(Iterator i = this.model.getAppliedGiftCards().iterator(); i.hasNext(); ){
			lst.add(new ErpAppliedGiftCardPersistentBean((ErpAppliedGiftCardModel)i.next()));
		}
		return lst;
	}
	/**
	 * Template method to create a concrete instance of ErpAbstractOrderModel.
	 */
	protected ErpAbstractInvoiceModel createModel(){
		return new ErpInvoiceModel();
	}

	/**
	 * Template method to get the tx type.
	 */
	protected EnumTransactionType getTransactionType(){
		return EnumTransactionType.INVOICE;
	}
	
	private static class AppliedCreditList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpAppliedCreditPersistentBean.findByParent(conn, (PrimaryKey) AppliedCreditList.this.getParentPK()));
	    }
	}
	
	private static class InvoiceLineList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpInvoiceLinePersistentBean.findByParent(conn, (PrimaryKey)InvoiceLineList.this.getParentPK()));
	    }
	}

	private static class ChargeList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpChargeLinePersistentBean.findByParent(conn, (PrimaryKey) ChargeList.this.getParentPK()));
		}
	}

	private static class DiscountList extends DependentPersistentBeanList {
		public void load(Connection conn) throws SQLException {
			this.set(ErpDiscountLinePersistentBean.findByParent(conn, DiscountList.this.getParentPK()));
		}
	}
	
	private static class AppliedGiftCardList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpAppliedGiftCardPersistentBean.findByParent(conn, (PrimaryKey) AppliedGiftCardList.this.getParentPK()));
	    }
	}
}
