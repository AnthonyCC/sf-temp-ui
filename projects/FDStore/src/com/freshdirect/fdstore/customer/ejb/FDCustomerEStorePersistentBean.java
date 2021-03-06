package com.freshdirect.fdstore.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.storeapi.content.ContentFactory;

/**
 *
 * @author ksriram
 *
 */
public class FDCustomerEStorePersistentBean extends DependentPersistentBeanSupport{

	private static final long serialVersionUID = 8004436858355527189L;

	private FDCustomerEStoreModel model;

	public FDCustomerEStorePersistentBean() {
		super();
		model = new FDCustomerEStoreModel();
	}

	public FDCustomerEStorePersistentBean(PrimaryKey pk) {
		super(pk);
		model = new FDCustomerEStoreModel();
	}

	/*public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
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
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.FDCUSTOMER_ESTORE (FDCUSTOMER_ID, E_STORE, DEFAULT_SHIPTO, DEFAULT_PAYMENT, DEFAULT_DEPOT_LOC,EMAIL_OPTIN,TC_AGREE_DATE,TC_AGREE,RAF_CLICK_ID,RAF_PROMO_CODE,DP_FREE_TRIAL_OPTIN,INFORM_ORDERMODIFY) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, this.getParentPK().getId());
		ps.setString(2, model.geteStoreId().getContentId());
		ps.setString(3, model.getDefaultShipToAddressPK());
		ps.setString(4, model.getDefaultPaymentMethodPK());
		ps.setString(5, model.getDefaultDepotLocationPK());
		ps.setString(6, model.getEmailOptIn()?"X":"");
		ps.setTimestamp(7, new Timestamp(new Date().getTime()));
		ps.setString(8, "X");
		ps.setString(9, model.getRafClickId());
		ps.setString(10, model.getRafPromoCode());
		ps.setString(11, "N");
		ps.setInt(12, model.getInformOrderModifyViewCount(model.geteStoreId(), false));

		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
		} catch (SQLException sqle) {
			this.setPK(null);
			throw sqle;
		} finally {
			ps.close();
		}
		return this.getParentPK();
	}

	@Override
	public void load(Connection conn) throws SQLException {
		EnumEStoreId eStoreId =getCustomerEStoreId();

		PreparedStatement ps = null;

		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT DEFAULT_SHIPTO, DEFAULT_PAYMENT, DEFAULT_DEPOT_LOC,TC_AGREE,EMAIL_OPTIN,RAF_CLICK_ID,RAF_PROMO_CODE,DP_FREE_TRIAL_OPTIN,INFORM_ORDERMODIFY, AUTORENEW_DP_TYPE , HAS_AUTORENEW_DP  FROM CUST.FDCUSTOMER_ESTORE WHERE FDCUSTOMER_ID=? AND E_STORE=?");
			ps.setString(1, this.getParentPK().getId());
			ps.setString(2, eStoreId.getContentId());
			rs = ps.executeQuery();
			if (rs.next()) {
				model.setPK(getPK());
				model.seteStoreId(eStoreId);
				model.setDefaultShipToAddressPK(rs.getString("DEFAULT_SHIPTO"));
				model.setDefaultPaymentMethodPK(rs.getString("DEFAULT_PAYMENT"));
				model.setDefaultDepotLocationPK(rs.getString("DEFAULT_DEPOT_LOC"));
				model.setTcAcknowledge("X".equalsIgnoreCase(rs.getString("TC_AGREE")) ? true : false);
				model.setEmailOptIn("X".equalsIgnoreCase(rs.getString("EMAIL_OPTIN")) ? true : false);
				model.setRafClickId(rs.getString("RAF_CLICK_ID"));
				model.setRafPromoCode(rs.getString("RAF_PROMO_CODE"));
				model.setDpFreeTrialOptin("Y".equalsIgnoreCase(rs.getString("DP_FREE_TRIAL_OPTIN")) ? true : false);
				model.setInformOrderModifyViewCount(eStoreId, rs.getInt("INFORM_ORDERMODIFY"));
				model.setAutoRenewDpType(rs.getString("AUTORENEW_DP_TYPE"));
				model.setHasAutoRenewDP(rs.getString("HAS_AUTORENEW_DP"));
				
				if (EnumEStoreId.FDX.equals(eStoreId)) {
					model.setFdxEmailOptIn("X".equalsIgnoreCase(rs.getString("EMAIL_OPTIN")) ? true : false);
				} else {
					/* re-use result set */
					ps = conn.prepareStatement(
							"SELECT EMAIL_OPTIN FROM CUST.FDCUSTOMER_ESTORE WHERE FDCUSTOMER_ID=? AND E_STORE=?");
					ps.setString(1, this.getParentPK().getId());
					ps.setString(2, EnumEStoreId.FDX.getContentId());
					rs = ps.executeQuery();
					if (rs.next()) {
						model.setFdxEmailOptIn("X".equalsIgnoreCase(rs.getString("EMAIL_OPTIN")) ? true : false);
					}
				}
			}
		} finally {
			DaoUtil.close(rs, ps);
		}

	}

	@Override
	public void store(Connection conn) throws SQLException {
		EnumEStoreId eStoreId = getCustomerEStoreId();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"UPDATE CUST.FDCUSTOMER_ESTORE SET DEFAULT_SHIPTO=?, DEFAULT_PAYMENT=?, DEFAULT_DEPOT_LOC=?, INFORM_ORDERMODIFY=? WHERE FDCUSTOMER_ID=? AND E_STORE=?");
			ps.setString(1, model.getDefaultShipToAddressPK());
			ps.setString(2, model.getDefaultPaymentMethodPK());
			ps.setString(3, model.getDefaultDepotLocationPK());
			ps.setInt(4, model.getInformOrderModifyViewCount(eStoreId, false));
			ps.setString(5, this.getParentPK().getId());
			ps.setString(6, eStoreId.getContentId());
			
			if (ps.executeUpdate() < 1) {
				create(conn);
			}
		} finally {
			DaoUtil.close(ps);
		}
		this.unsetModified();

	}


	@Override
	public void remove(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("DELETE FROM CUST.FDCUSTOMER_ESTORE WHERE FDCUSTOMER_ID= ?");
			ps.setString(1, this.getPK().getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not deleted");
			}
		} finally {
			DaoUtil.close(ps);
		}
	}

	@Override
	public void setFromModel(ModelI model) {
		this.model =(FDCustomerEStoreModel)model;

	}

	private static EnumEStoreId getCustomerEStoreId(){
		return EnumEStoreId.valueOfContentId((ContentFactory.getInstance().getStoreKey().getId()));
	}

}
