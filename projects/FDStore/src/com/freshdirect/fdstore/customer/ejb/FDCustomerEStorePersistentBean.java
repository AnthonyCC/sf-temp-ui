package com.freshdirect.fdstore.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * 
 * @author ksriram
 *
 */
public class FDCustomerEStorePersistentBean extends DependentPersistentBeanSupport{

	private static final long serialVersionUID = 8004436858355527189L;
	
	private FDCustomerEStoreModel model;
	private String orderNotices=null;
	private String orderExceptions=null;
	private String offers=null;
	private String partnerMessages=null;
	private String smsPreferenceflag;
	private String fdxSmsPreferenceflag;
	private String fdxOrderNotices=null;
	private String fdxOrderExceptions=null;
	private String fdxOffers=null;
	private String fdxPartnerMessages;
	
	boolean OfferNotification=false;
	boolean DeliveryNotification=false;
	boolean FdxOfferNotification=false;
	boolean FdxDeliveryNotification=false;
	
	
	public FDCustomerEStorePersistentBean() {
		super();
		model = new FDCustomerEStoreModel();
	}

	public FDCustomerEStorePersistentBean(PrimaryKey pk) {
		super(pk);
		model = new FDCustomerEStoreModel();
	}

	/*	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		EnumEStoreId eStoreId =getCustomerEStoreId();
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT DEFAULT_SHIPTO, DEFAULT_PAYMENT, DEFAULT_DEPOT_LOCATION FROM CUST.FDCUSTOMER_ESTORE WHERE FDCUSTOMER_ID=? AND E_STORE=?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, eStoreId.getContentId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			FDCustomerEStorePersistentBean bean = new FDCustomerEStorePersistentBean( parentPK);
			FDCustomerEStoreModel model = new FDCustomerEStoreModel();
			model.setDefaultShipToAddressPK(rs.getString("DEFAULT_SHIPTO"));
			model.setDefaultPaymentMethodPK(rs.getString("DEFAULT_PAYMENT"));
			model.setDefaultDepotLocationPK(rs.getString("DEFAULT_DEPOT_LOCATION"));
			bean.setParentPK(parentPK);			
			bean.setFromModel(model);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}*/

	@Override
	public ModelI getModel() {
		if(this.model!=null)
			return this.model.deepCopy();
		return new FDCustomerEStoreModel().deepCopy();
	}

	@Override
	public PrimaryKey create(Connection conn) throws SQLException {
		this.setPK(this.getParentPK());
		Date optinDate=new Date();
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.FDCUSTOMER_ESTORE (FDCUSTOMER_ID, E_STORE, DEFAULT_SHIPTO, DEFAULT_PAYMENT, DEFAULT_DEPOT_LOC, " +
				" MOBILE_NUMBER, ORDER_NOTIFICATION, ORDEREXCEPTION_NOTIFICATION, SMS_OFFERS_ALERT, PARTNERMESSAGE_NOTIFICATION, SMS_OPTIN_DATE, DELIVERY_NOTIFICATION, OFFERS_NOTIFICATION, SMS_PREFERENCE_FLAG ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
		ps.setString(1, this.getParentPK().getId());
		ps.setString(2, EnumEStoreId.FD.getContentId());
		ps.setString(3, model.getDefaultShipToAddressPK());
		ps.setString(4, model.getDefaultPaymentMethodPK());
		ps.setString(5, model.getDefaultDepotLocationPK());
		ps.setString(6, (model.getMobileNumber() !=null ? model.getMobileNumber().getPhone() :"")); 
		ps.setString(7, model.getOrderNotices());
		ps.setString(8, model.getOrderExceptions());
		ps.setString(9, model.getOffers());
		ps.setString(10, model.getPartnerMessages());
		ps.setTimestamp(11, new java.sql.Timestamp(optinDate.getTime()));
		if(model.getDeliveryNotification()!=null)
			ps.setString(12,  model.getDeliveryNotification()?"Y":"N");
		else
			ps.setString(12,  "N");
		
		if(model.getOffersNotification()!=null)
			ps.setString(13,  model.getOffersNotification()?"Y":"N");
		else
			ps.setString(13,  "N");
		ps.setString(14, model.getSmsPreferenceflag());
		
		
		PreparedStatement ps1 = conn.prepareStatement("INSERT INTO CUST.FDCUSTOMER_ESTORE (FDCUSTOMER_ID, E_STORE, DEFAULT_SHIPTO, DEFAULT_PAYMENT, DEFAULT_DEPOT_LOC, " +
				" MOBILE_NUMBER, ORDER_NOTIFICATION, ORDEREXCEPTION_NOTIFICATION, SMS_OFFERS_ALERT, PARTNERMESSAGE_NOTIFICATION, SMS_OPTIN_DATE,DELIVERY_NOTIFICATION, OFFERS_NOTIFICATION, SMS_PREFERENCE_FLAG ) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); 
		ps1.setString(1, this.getParentPK().getId());
		ps1.setString(2, EnumEStoreId.FDX.getContentId());
		ps1.setString(3, model.getDefaultShipToAddressPK());
		ps1.setString(4, model.getDefaultPaymentMethodPK());
		ps1.setString(5, model.getDefaultDepotLocationPK());
		ps1.setString(6, (model.getMobileNumber() !=null ? model.getMobileNumber().getPhone() :"")); 
		ps1.setString(7, model.getFdxOrderNotices());
		ps1.setString(8, model.getFdxOrderExceptions());
		ps1.setString(9, model.getFdxOffers());
		ps1.setString(10, model.getFdxPartnerMessages());
		ps1.setTimestamp(11, new java.sql.Timestamp(optinDate.getTime()));
		if(model.getDeliveryNotification()!=null)
			ps1.setString(12,  model.getDeliveryNotification()?"Y":"N");
		else
			ps1.setString(12,  "N");
		if(model.getOffersNotification()!=null)
			ps1.setString(13,  model.getOffersNotification()?"Y":"N");
		else
			ps1.setString(13,  "N");
		ps1.setString(14, model.getSmsPreferenceflag());
		
		
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created FD");
			}
			if (ps1.executeUpdate() != 1) {
				throw new SQLException("Row not created FDX");
			}
		} catch (SQLException sqle) {
			this.setPK(null);
			throw sqle;
		} finally {
			ps.close();
			ps1.close();
		}
		return this.getParentPK();
	}
	
	@Override
	public void load(Connection conn) throws SQLException {
		PreparedStatement ps, ps1;
		ResultSet rs, rs1;
		 ps = conn.prepareStatement("SELECT DEFAULT_SHIPTO, DEFAULT_PAYMENT, DEFAULT_DEPOT_LOC, " +
										"MOBILE_NUMBER, ORDER_NOTIFICATION, ORDEREXCEPTION_NOTIFICATION, SMS_OFFERS_ALERT, PARTNERMESSAGE_NOTIFICATION, SMS_OPTIN_DATE, DELIVERY_NOTIFICATION, OFFERS_NOTIFICATION, SMS_PREFERENCE_FLAG FROM CUST.FDCUSTOMER_ESTORE" +
										" WHERE FDCUSTOMER_ID=? AND E_STORE='FreshDirect'");
		ps.setString(1, this.getParentPK().getId());
		 rs = ps.executeQuery();
		if (rs.next()) {
				model.setPK(getPK());
				model.setDefaultShipToAddressPK(rs.getString("DEFAULT_SHIPTO"));
				model.setDefaultPaymentMethodPK(rs.getString("DEFAULT_PAYMENT"));
				model.setDefaultDepotLocationPK(rs.getString("DEFAULT_DEPOT_LOC"));
				if(null!=rs.getString("MOBILE_NUMBER"))
				model.setMobileNumber(new PhoneNumber(rs.getString("MOBILE_NUMBER")));
				this.orderNotices=rs.getString("ORDER_NOTIFICATION");	
				this.orderExceptions=rs.getString("ORDEREXCEPTION_NOTIFICATION");	
				this.offers=rs.getString("SMS_OFFERS_ALERT");		
				this.partnerMessages=rs.getString("PARTNERMESSAGE_NOTIFICATION");
				model.setSmsOptinDate(rs.getTimestamp("SMS_OPTIN_DATE"));
				this.DeliveryNotification="Y".equalsIgnoreCase(rs.getString("DELIVERY_NOTIFICATION"))?true:false;
				this.OfferNotification="Y".equalsIgnoreCase(rs.getString("OFFERS_NOTIFICATION"))?true:false;
				this.smsPreferenceflag= rs.getString("SMS_PREFERENCE_FLAG");
				
		}
		 ps1 = conn.prepareStatement("SELECT MOBILE_NUMBER, ORDER_NOTIFICATION, ORDEREXCEPTION_NOTIFICATION, SMS_OFFERS_ALERT, PARTNERMESSAGE_NOTIFICATION, SMS_OPTIN_DATE, DELIVERY_NOTIFICATION, OFFERS_NOTIFICATION, SMS_PREFERENCE_FLAG  FROM CUST.FDCUSTOMER_ESTORE" +
				" WHERE FDCUSTOMER_ID=? AND E_STORE='FDX'");
				ps1.setString(1, this.getParentPK().getId());
				 rs1 = ps1.executeQuery();
				if (rs1.next()) {
				if(null!=rs1.getString("MOBILE_NUMBER"))
				model.setFdxMobileNumber(new PhoneNumber(rs1.getString("MOBILE_NUMBER")));
				this.fdxOrderNotices=rs1.getString("ORDER_NOTIFICATION");	
				this.fdxOrderExceptions=rs1.getString("ORDEREXCEPTION_NOTIFICATION");	
				this.fdxOffers=rs1.getString("SMS_OFFERS_ALERT");		
				this.fdxPartnerMessages=rs1.getString("PARTNERMESSAGE_NOTIFICATION");
				model.setFdxSmsOptinDate(rs1.getTimestamp("SMS_OPTIN_DATE"));
				this.FdxDeliveryNotification="Y".equalsIgnoreCase(rs1.getString("DELIVERY_NOTIFICATION"))?true:false;
				this.FdxOfferNotification="Y".equalsIgnoreCase(rs1.getString("OFFERS_NOTIFICATION"))?true:false;
				this.fdxSmsPreferenceflag=rs1.getString("SMS_PREFERENCE_FLAG");
				}
				
				model.setOrderNotices(this.orderNotices !=null ? this.orderNotices :"N");	
				model.setOrderExceptions(this.orderExceptions !=null ? this.orderExceptions :"N");	
				model.setOffers(this.offers!=null ? this.offers:"N");		
				model.setPartnerMessages(this.partnerMessages !=null ? this.partnerMessages :"N");
				model.setFdxOrderNotices(this.fdxOrderNotices!=null?this.fdxOrderNotices:"N");
				model.setFdxOrderExceptions(this.fdxOrderExceptions!=null ?this.fdxOrderExceptions:"N");	
				model.setFdxOffers(this.fdxOffers!=null?this.fdxOffers:"N");		
				model.setFdxPartnerMessages(this.fdxPartnerMessages!=null?this.fdxPartnerMessages:"N");
				model.setOffersNotification(this.OfferNotification);
				model.setDeliveryNotification(this.DeliveryNotification);
				model.setFdxOffersNotification(this.FdxOfferNotification);
				model.setFdxdeliveryNotification(this.FdxDeliveryNotification);
				model.setSmsPreferenceflag(this.smsPreferenceflag !=null? this.smsPreferenceflag:"N");
				model.setFdxSmsPreferenceflag(this.fdxSmsPreferenceflag!=null? this.fdxSmsPreferenceflag:"N");
				
				ps.close();
				ps1.close();
				rs.close();
				rs1.close();
		}
	

	@Override
	public void store(Connection conn) throws SQLException {
		
		PreparedStatement ps,ps1 = null;
		EnumEStoreId eStoreId = getCustomerEStoreId();
			
		Date optinDate = new Date();
			ps = conn.prepareStatement("UPDATE CUST.FDCUSTOMER_ESTORE SET DEFAULT_SHIPTO=?, DEFAULT_PAYMENT=?, DEFAULT_DEPOT_LOC=?," +
										" MOBILE_NUMBER=?, ORDER_NOTIFICATION=?, ORDEREXCEPTION_NOTIFICATION=?, SMS_OFFERS_ALERT=?, PARTNERMESSAGE_NOTIFICATION=?," +
										" SMS_OPTIN_DATE=?, DELIVERY_NOTIFICATION=?, OFFERS_NOTIFICATION=? WHERE FDCUSTOMER_ID=? AND E_STORE=?");
				ps.setString(1, model.getDefaultShipToAddressPK());
				ps.setString(2, model.getDefaultPaymentMethodPK());
				ps.setString(3, model.getDefaultDepotLocationPK());
				ps.setString(4, (model.getMobileNumber() !=null ? model.getMobileNumber().getPhone() :"")); 
				ps.setString(5, model.getOrderNotices());
				ps.setString(6, model.getOrderExceptions());
				ps.setString(7, model.getOffers());
				ps.setString(8, model.getPartnerMessages());
				ps.setTimestamp(9, new java.sql.Timestamp(optinDate.getTime()));
				if(model.getDeliveryNotification()!=null)
					ps.setString(10,  model.getDeliveryNotification()?"Y":"N");
				else
					ps.setString(10,  "N");
				
				if(model.getOffersNotification()!=null)
					ps.setString(11,  model.getOffersNotification()?"Y":"N");
				else
					ps.setString(11,  "N");
				
				ps.setString(12, this.getParentPK().getId());
				ps.setString(13, EnumEStoreId.FD.getContentId());
				if(ps.executeUpdate() < 1){
					create(conn);
				}
				
				ps1 = conn.prepareStatement("UPDATE CUST.FDCUSTOMER_ESTORE SET DEFAULT_SHIPTO=?, DEFAULT_PAYMENT=?, DEFAULT_DEPOT_LOC=?," +
						" MOBILE_NUMBER=?, ORDER_NOTIFICATION=?, ORDEREXCEPTION_NOTIFICATION=?, SMS_OFFERS_ALERT=?, PARTNERMESSAGE_NOTIFICATION=?," +
						" SMS_OPTIN_DATE=?, DELIVERY_NOTIFICATION=?, OFFERS_NOTIFICATION=? WHERE FDCUSTOMER_ID=? AND E_STORE=?");
				  	ps1.setString(1, model.getDefaultShipToAddressPK());
					ps1.setString(2, model.getDefaultPaymentMethodPK());
					ps1.setString(3, model.getDefaultDepotLocationPK());
					ps1.setString(4, (model.getFdxMobileNumber() !=null ? model.getFdxMobileNumber().getPhone() :"")); 
					ps1.setString(5, model.getFdxOrderNotices());
					ps1.setString(6, model.getFdxOrderExceptions());
					ps1.setString(7, model.getFdxOffers());
					ps1.setString(8, model.getFdxPartnerMessages());
					ps1.setTimestamp(9, new java.sql.Timestamp(optinDate.getTime()));
					if(model.getDeliveryNotification()!=null)
						ps1.setString(10,  model.getDeliveryNotification()?"Y":"N");
					else
						ps1.setString(10,  "N");
					if(model.getOffersNotification()!=null)
						ps1.setString(11,  model.getOffersNotification()?"Y":"N");
					else
						ps1.setString(11,  "N");
					ps1.setString(12, this.getParentPK().getId());
					ps1.setString(13, EnumEStoreId.FDX.name());
				
					if(ps1.executeUpdate() < 1){
						create(conn);
					}
				if(ps1.executeUpdate() < 1){
					create(conn);
				}
				ps.close();
				ps1.close();
		this.setModified();
	}

	@Override
	public void remove(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.FDCUSTOMER_ESTORE WHERE FDCUSTOMER_ID= ?");
		ps.setString(1, this.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();		
	}

	@Override
	public void setFromModel(ModelI model) {
		this.model =(FDCustomerEStoreModel)model;
		
	}

	private static EnumEStoreId getCustomerEStoreId(){
		return ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId();
	}

}
