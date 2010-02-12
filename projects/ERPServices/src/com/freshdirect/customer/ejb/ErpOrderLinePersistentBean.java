package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.ejb.EJBException;

import org.hibernate.SQLQuery;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.NVL;


/**
 * ErpOrderLine persistent bean.
 * @version	$Revision:10$
 * @author	 $Author:Viktor Szathmary$
 * @stereotype fd-persistent
 */
public class ErpOrderLinePersistentBean extends ErpReadOnlyPersistentBean {
	
	private ErpOrderLineModel model;
	
	public ErpOrderLinePersistentBean() {
		super();
		this.model = new ErpOrderLineModel();
	}

	/** Load constructor. */
	public ErpOrderLinePersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.load(conn);
	}

	private ErpOrderLinePersistentBean(PrimaryKey pk, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(rs);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpOrderLineModel to copy from
	 */
	public ErpOrderLinePersistentBean(ErpOrderLineModel model) {
		this();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 * @return ErpOrderLineModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpOrderLineModel)model;
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}

	/**
	 * Find ErpOrderLinePersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpOrderLinePersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID, ORDERLINE_NUMBER, SKU_CODE, VERSION, QUANTITY, SALES_UNIT, CONFIGURATION, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, DESCRIPTION, CONFIGURATION_DESC, DEPARTMENT_DESC, MATERIAL_NUMBER, PRICE, PERISHABLE, TAX_RATE, DEPOSIT_VALUE, ALCOHOL, AFFILIATE, CARTLINE_ID, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION, RATING,BASE_PRICE,BASE_PRICE_UNIT,DISCOUNT_AMT, VARIANT_ID, SAVINGS_ID,PRICING_ZONE_ID FROM CUST.ORDERLINE WHERE SALESACTION_ID=? ORDER BY ORDERLINE_NUMBER");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpOrderLinePersistentBean bean = new ErpOrderLinePersistentBean( new PrimaryKey(rs.getString("ID")), rs );
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}
	
	private final static String INSERT_QUERY =
		"INSERT INTO CUST.ORDERLINE (ID, SALESACTION_ID, ORDERLINE_NUMBER, SKU_CODE, VERSION,"
		+ " QUANTITY, SALES_UNIT, CONFIGURATION, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN,"
		+ " DESCRIPTION, CONFIGURATION_DESC, DEPARTMENT_DESC, MATERIAL_NUMBER, PRICE, PERISHABLE,"
		+ " TAX_RATE, DEPOSIT_VALUE, ALCOHOL, AFFILIATE, CARTLINE_ID, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION,RATING,BASE_PRICE,BASE_PRICE_UNIT,DISCOUNT_AMT,VARIANT_ID,SAVINGS_ID,PRICING_ZONE_ID)"
		+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, this.model.getOrderLineNumber());
		ps.setString(4, this.model.getSku().getSkuCode());
		ps.setInt(5, this.model.getSku().getVersion());
		ps.setDouble(6, this.model.getConfiguration().getQuantity());
		ps.setString(7, this.model.getConfiguration().getSalesUnit());
		ps.setString(8, ErpOrderLineUtil.convertHashMapToString(this.model.getConfiguration().getOptions()));

		Discount d = this.model.getDiscount();
		if (d!=null) {		
			ps.setInt(9, d.getDiscountType().getId());
			ps.setDouble(10, d.getAmount());
			ps.setString(11, d.getPromotionCode());
		} else {
			ps.setNull(9, Types.INTEGER);
			ps.setNull(10, Types.DOUBLE);
			ps.setNull(11, Types.VARCHAR);
		}

		ps.setString(12, this.model.getDescription());
		ps.setString(13, this.model.getConfigurationDesc());
		ps.setString(14, this.model.getDepartmentDesc());
		ps.setString(15, this.model.getMaterialNumber());
		ps.setDouble(16, this.model.getActualPrice());
		ps.setString(17, (this.model.isPerishable() ? "X" : ""));
		ps.setDouble(18, this.model.getTaxRate());
		ps.setDouble(19, this.model.getDepositValue());
		ps.setString(20, (this.model.isAlcohol() ? "X" : ""));
		ps.setString(21, this.model.getAffiliate().getCode());
		ps.setString(22, this.model.getCartlineId());
		ps.setString(23, this.model.getRecipeSourceId());
		ps.setString(24, this.model.isRequestNotification() ? "X" : "");
		if(this.model.getProduceRating()!=null)
		   ps.setString(25, this.model.getProduceRating().getStatusCode());	
		else
			ps.setNull(25, Types.NULL);
		if(this.model.getBasePrice()!=0) {
			ps.setDouble(26, this.model.getBasePrice());
		} else {
			ps.setNull(26, Types.NULL);
		}
		if(this.model.getBasePriceUnit()!=null) {
			ps.setString(27,this.model.getBasePriceUnit());
		} else {
			ps.setNull(27, Types.VARCHAR);
		}
		
		ps.setDouble(28,model.getDiscountAmount());
		
		if(this.model.getVariantId()!=null) {
			ps.setString(29,this.model.getVariantId());
		} else {
			ps.setNull(29, Types.VARCHAR);
		}				
		if(this.model.getSavingsId()!=null) {
			ps.setString(30,this.model.getSavingsId());
		} else {
			ps.setNull(30, Types.VARCHAR);
		}
		/*
		if(this.model.getPricingContext().getZoneId()!=null) {
			ps.setString(31,this.model.getPricingContext().getZoneId());
		} else {
			ps.setNull(31, Types.VARCHAR);
		}
		*/
		//zone where the order line was originally priced from
		if(this.model.getPricingZoneId()!=null) {
			ps.setString(31,this.model.getPricingZoneId());
		} else {
			ps.setNull(31, Types.VARCHAR);
		}
		
		
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
		}
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
				
		PreparedStatement ps = conn.prepareStatement("SELECT ORDERLINE_NUMBER, SKU_CODE, VERSION, QUANTITY, SALES_UNIT, CONFIGURATION, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, DESCRIPTION, CONFIGURATION_DESC, DEPARTMENT_DESC, MATERIAL_NUMBER, PRICE, PERISHABLE, TAX_RATE, DEPOSIT_VALUE, ALCOHOL, AFFILIATE, CARTLINE_ID, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION, RATING,BASE_PRICE,BASE_PRICE_UNIT,DISCOUNT_AMT,VARIANT_ID,SAVINGS_ID,PRICING_ZONE_ID FROM CUST.ORDERLINE WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(rs);
		} else {
			throw new SQLException("No such ErpOrderLine PK: " + this.getPK()); 
		}
		rs.close();
		ps.close();
	}

	private void loadFromResultSet(ResultSet rs) throws SQLException {
		this.model.setOrderLineNumber(rs.getString("ORDERLINE_NUMBER"));
		this.model.setSku(new FDSku(rs.getString("SKU_CODE"), rs.getInt("VERSION")));
		this.model.setConfiguration(new FDConfiguration(
			rs.getDouble("QUANTITY"),
			rs.getString("SALES_UNIT"),
			ErpOrderLineUtil.convertStringToHashMap(rs.getString("CONFIGURATION"))));
		this.model.setDescription(rs.getString("DESCRIPTION"));
		this.model.setConfigurationDesc(NVL.apply(rs.getString("CONFIGURATION_DESC"), ""));
		this.model.setDepartmentDesc(rs.getString("DEPARTMENT_DESC"));
		this.model.setMaterialNumber(rs.getString("MATERIAL_NUMBER"));
		this.model.setPrice(rs.getDouble("PRICE") - rs.getDouble("DISCOUNT_AMT"));
		this.model.setPerishable(NVL.apply(rs.getString("PERISHABLE"), "").equals("X"));
		this.model.setTaxRate(rs.getDouble("TAX_RATE"));
		this.model.setDepositValue(rs.getDouble("DEPOSIT_VALUE"));
		this.model.setAlcohol(NVL.apply(rs.getString("ALCOHOL"), "").equals("X"));
        this.model.setAffiliate(ErpAffiliate.getEnum(rs.getString("AFFILIATE")));
        this.model.setCartlineId(rs.getString("CARTLINE_ID"));
        this.model.setRecipeSourceId(rs.getString("RECIPE_SOURCE_ID"));
        this.model.setRequestNotification(NVL.apply(rs.getString("REQUEST_NOTIFICATION"), "").equals("X"));
        this.model.setProduceRating(EnumOrderLineRating.getEnumByStatusCode(NVL.apply(rs.getString("RATING"), "X")));  
        this.model.setBasePrice(rs.getDouble("BASE_PRICE"));
        this.model.setBasePriceUnit(rs.getString("BASE_PRICE_UNIT"));
        this.model.setDiscountAmount(rs.getDouble("DISCOUNT_AMT"));
        this.model.setVariantId(rs.getString("VARIANT_ID"));
        this.model.setSavingsId(rs.getString("SAVINGS_ID"));
        String pricingZoneId=rs.getString("PRICING_ZONE_ID");
        this.model.setPricingContext(new PricingContext(pricingZoneId==null?ZonePriceListing.MASTER_DEFAULT_ZONE:pricingZoneId)); 

        
		// deal with no promotion (PROMOTION_TYPE is NULL)
		int type = rs.getInt("PROMOTION_TYPE");
		if(!rs.wasNull()){
			EnumDiscountType promotionType = EnumDiscountType.getPromotionType(type);
			if(promotionType == null){
				throw new EJBException("Promotion code is wrong or does not exist");
			}
			this.model.setDiscount(new Discount(rs.getString("PROMOTION_CAMPAIGN"), promotionType, rs.getDouble("PROMOTION_AMT")));
		}
		this.unsetModified();
	}
}
