package com.freshdirect.customer.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.ejb.EJBException;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;


/**
 * ErpOrderLine persistent bean.
 * @version	$Revision:10$
 * @author	 $Author:Viktor Szathmary$
 * @stereotype fd-persistent
 */
public class ErpOrderLinePersistentBean extends ErpReadOnlyPersistentBean {

	private ErpOrderLineModel model;
	private ErpCouponDiscountLineModel couponDiscount;

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

	private ErpOrderLinePersistentBean(PrimaryKey pk, ResultSet rs,Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(conn,rs);
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
	@Override
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	@Override
	public void setFromModel(ModelI model) {
		this.model = (ErpOrderLineModel)model;
	}

	@Override
	public PrimaryKey getPK() {
		return this.model.getPK();
	}

	@Override
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
		PreparedStatement ps = conn.prepareStatement("SELECT ID, ORDERLINE_NUMBER, SKU_CODE, VERSION, QUANTITY, SALES_UNIT, CONFIGURATION, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, DESCRIPTION, CONFIGURATION_DESC, DEPARTMENT_DESC, MATERIAL_NUMBER, PRICE, PERISHABLE, TAX_RATE, DEPOSIT_VALUE, ALCOHOL, AFFILIATE, CARTLINE_ID, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION, RATING,BASE_PRICE,BASE_PRICE_UNIT,DISCOUNT_AMT, VARIANT_ID, SAVINGS_ID,PRICING_ZONE_ID,GROUP_ID,GROUP_VERSION,GROUP_QTY,SUSTAINABILITY_RATING,SKU_LIMIT,UPC,TAXATION_TYPE, EXTERNAL_AGENCY, EXTERNAL_SOURCE, EXTERNAL_GROUP,NVL(SALES_ORG,'1400') SALES_ORG ,NVL(DISTRIBUTION_CHANNEL,'01') DISTRIBUTION_CHANNEL,SCALE_QTY, SOURCE, TAX_CODE, MATERIAL_GROUP, PICKING_PLANT_ID FROM CUST.ORDERLINE WHERE SALESACTION_ID=? ORDER BY ORDERLINE_NUMBER");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpOrderLinePersistentBean bean = new ErpOrderLinePersistentBean( new PrimaryKey(rs.getString("ID")), rs,conn );
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
		+ " TAX_RATE, DEPOSIT_VALUE, ALCOHOL, AFFILIATE, CARTLINE_ID, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION,RATING,BASE_PRICE,BASE_PRICE_UNIT,DISCOUNT_AMT,VARIANT_ID,SAVINGS_ID,PRICING_ZONE_ID,GROUP_ID,GROUP_VERSION,GROUP_QTY,SUSTAINABILITY_RATING, SKU_LIMIT,UPC,TAXATION_TYPE, EXTERNAL_AGENCY, EXTERNAL_SOURCE, EXTERNAL_GROUP,SALES_ORG,DISTRIBUTION_CHANNEL,SCALE_QTY,SOURCE,TAX_CODE, MATERIAL_GROUP,PICKING_PLANT_ID)"
		+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	@Override
	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, this.model.getOrderLineNumber());
		ps.setString(4, this.model.getSku().getSkuCode());
		ps.setInt(5, this.model.getSku().getVersion());
		ps.setBigDecimal(6, new BigDecimal(String.valueOf(this.model.getConfiguration().getQuantity())));
		ps.setString(7, this.model.getConfiguration().getSalesUnit());
		ps.setString(8, ErpOrderLineUtil.convertHashMapToString(this.model.getConfiguration().getOptions()));

		Discount d = this.model.getDiscount();
		if (d!=null) {
			ps.setInt(9, d.getDiscountType().getId());
			ps.setBigDecimal(10, new BigDecimal(String.valueOf(d.getAmount())));
			ps.setString(11, d.getPromotionCode());
			ps.setInt(36, d.getSkuLimit());
		} else {
			ps.setNull(9, Types.INTEGER);
			ps.setNull(10, Types.DOUBLE);
			ps.setNull(11, Types.VARCHAR);
			ps.setNull(36, Types.INTEGER);
		}

		ps.setString(12, this.model.getDescription());
		ps.setString(13, this.model.getConfigurationDesc());
		ps.setString(14, this.model.getDepartmentDesc());
		ps.setString(15, this.model.getMaterialNumber());
		ps.setBigDecimal(16, new BigDecimal(String.valueOf(this.model.getActualPrice())));
		ps.setString(17, (this.model.isPerishable() ? "X" : ""));
		ps.setBigDecimal(18, new BigDecimal(String.valueOf(this.model.getTaxRate())));
		ps.setBigDecimal(19, new BigDecimal(String.valueOf(this.model.getDepositValue())));
		ps.setString(20, (this.model.isAlcohol() ? "X" : ""));
		ps.setString(21, this.model.getAffiliate().getCode());
		ps.setString(22, this.model.getCartlineId());
		ps.setString(23, this.model.getRecipeSourceId());
		ps.setString(24, this.model.isRequestNotification() ? "X" : "");
		if(this.model.getProduceRating()!=null)
		   ps.setString(25, this.model.getProduceRating().getStatusCode());
		else
			ps.setNull(25, Types.NULL);
		ps.setBigDecimal(26, new BigDecimal(String.valueOf(this.model.getBasePrice())));
		/*if(this.model.getBasePrice()!=0) {
			ps.setBigDecimal(26, new BigDecimal(String.valueOf(this.model.getBasePrice())));
		} else {
			ps.setNull(26, Types.NULL);
		}*/
		if(this.model.getBasePriceUnit()!=null) {
			ps.setString(27,this.model.getBasePriceUnit());
		} else {
			ps.setNull(27, Types.VARCHAR);
		}

		ps.setBigDecimal(28, new BigDecimal(String.valueOf(this.model.getDiscountAmount())));

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

		FDGroup group = this.model.getFDGroup();
		if(group != null){
			ps.setString(32,group.getGroupId());
			ps.setInt(33,group.getVersion());
			ps.setBigDecimal(34, new BigDecimal(String.valueOf(this.model.getGroupQuantity())));
		}else{
			ps.setNull(32, Types.VARCHAR);
			ps.setNull(33, Types.INTEGER);
			ps.setNull(34, Types.DOUBLE);
		}
		if(this.model.getSustainabilityRating()!=null)
			   ps.setString(35, this.model.getSustainabilityRating().getStatusCode());
			else
				ps.setNull(35, Types.NULL);
		ps.setString(37, this.model.getUpc());
		ps.setString(38, null !=this.model.getTaxationType()?this.model.getTaxationType().getName():null);

		ps.setString(39, StringUtil.crop(model.getExternalAgency(), 30));
		ps.setString(40, StringUtil.crop(model.getExternalSource(), 30));
		ps.setString(41, StringUtil.crop(model.getExternalGroup(), 256));

		ps.setString(42, model.getSalesOrg());
		ps.setString(43, model.getDistChannel());

		if(null!=this.model.getScaleQuantity()){
			ps.setBigDecimal(44, new BigDecimal(String.valueOf(this.model.getScaleQuantity())));
		}else{
			ps.setNull(44, Types.DOUBLE);
		}

		if(null!=this.model.getSource()){
			ps.setString(45, model.getSource().toString());
		}else{
			ps.setNull(45, Types.VARCHAR);
		}
		ps.setString(46, model.getTaxCode());
		ps.setString(47, model.getMaterialGroup());

		if(null!=this.model.getPlantID()){
			ps.setString(48, model.getPlantID());
		}else{
			ps.setNull(48, Types.VARCHAR);
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

		// create children
		CouponLineList cList = this.getCouponLinePBList();
		cList.create(conn);

		this.unsetModified();
		return this.getPK();
	}

	@Override
	public void load(Connection conn) throws SQLException {

		PreparedStatement ps = conn.prepareStatement("SELECT ORDERLINE_NUMBER, SKU_CODE, VERSION, QUANTITY, SALES_UNIT, CONFIGURATION, PROMOTION_TYPE, PROMOTION_AMT, PROMOTION_CAMPAIGN, DESCRIPTION, CONFIGURATION_DESC, DEPARTMENT_DESC, MATERIAL_NUMBER, PRICE, PERISHABLE, TAX_RATE, DEPOSIT_VALUE, ALCOHOL, AFFILIATE, CARTLINE_ID, RECIPE_SOURCE_ID, REQUEST_NOTIFICATION, RATING,BASE_PRICE,BASE_PRICE_UNIT,DISCOUNT_AMT,VARIANT_ID,SAVINGS_ID,PRICING_ZONE_ID,GROUP_ID,GROUP_VERSION,GROUP_QTY,SUSTAINABILITY_RATING,SKU_LIMIT,UPC,TAXATION_TYPE, EXTERNAL_AGENCY, EXTERNAL_SOURCE, EXTERNAL_GROUP,SALES_ORG,DISTRIBUTION_CHANNEL,SCALE_QTY,SOURCE,TAX_CODE, MATERIAL_GROUP,PICKING_PLANT_ID FROM CUST.ORDERLINE WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(conn,rs);
			/*// load children
			loadChildren(conn);
			//Setting the final price for the line item after coupon discounts.
			if(null !=this.model.getCouponDiscount()){
				this.model.setPrice(this.model.getPrice()-this.model.getCouponDiscount().getDiscountAmt());
			}*/
		} else {
			throw new SQLException("No such ErpOrderLine PK: " + this.getPK());
		}
		rs.close();
		ps.close();
	}

	private void loadChildren(Connection conn) throws SQLException {
		CouponLineList cList = new CouponLineList();
		cList.setParentPK(this.getPK());
		cList.load(conn);
		if(!cList.isEmpty())
			this.model.setCouponDiscount((ErpCouponDiscountLineModel)cList.getModelList().get(0));
	}

	private void loadFromResultSet(Connection conn,ResultSet rs) throws SQLException {
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
        this.model.setPricingZoneId(pricingZoneId);
        this.model.setTaxCode(rs.getString("TAX_CODE"));
        this.model.setMaterialGroup(rs.getString("MATERIAL_GROUP"));

        String grpId = rs.getString("GROUP_ID");
        int version = rs.getInt("GROUP_VERSION");
        if(grpId!=null) {
			FDGroup group = new FDGroup(grpId, version);
			//set skip validate to true; Once they are reloaded from an order they don;t undergo any product price validation
			//for the group.
			group.setSkipProductPriceValidation(true);
        	this.model.setFDGroup(group);
        }
        this.model.setGroupQuantity(rs.getDouble("GROUP_QTY"));

        this.model.setSustainabilityRating(EnumSustainabilityRating.getEnumByStatusCode(NVL.apply(rs.getString("SUSTAINABILITY_RATING"), "X")));

		// deal with no promotion (PROMOTION_TYPE is NULL)
		int type = rs.getInt("PROMOTION_TYPE");
		if(!rs.wasNull()){
			EnumDiscountType promotionType = EnumDiscountType.getPromotionType(type);
			if(promotionType == null){
				throw new EJBException("Promotion code is wrong or does not exist");
			}

			Discount discount = new Discount(rs.getString("PROMOTION_CAMPAIGN"), promotionType, rs.getDouble("PROMOTION_AMT"));
			discount.setSkuLimit(rs.getInt("SKU_LIMIT"));
			this.model.setDiscount(discount);
		}
		this.model.setUpc(rs.getString("UPC"));
		this.model.setTaxationType(EnumTaxationType.getEnum(rs.getString("TAXATION_TYPE")));
		loadChildren(conn);
		//Setting the final price for the line item after coupon discounts.
		if(null !=this.model.getCouponDiscount()){
			this.model.setPrice(this.model.getPrice()-this.model.getCouponDiscount().getDiscountAmt());
		}

		this.model.setExternalAgency(ExternalAgency.safeValueOf(rs.getString("EXTERNAL_AGENCY")));
        this.model.setExternalSource(rs.getString("EXTERNAL_SOURCE"));
        this.model.setExternalGroup(rs.getString("EXTERNAL_GROUP"));

        String salesOrg=rs.getString("SALES_ORG");
        salesOrg = "1000".equals(salesOrg)?FDStoreProperties.getDefaultFdSalesOrg():salesOrg;
        this.model.setSalesOrg(salesOrg);
        String distrChannel=rs.getString("DISTRIBUTION_CHANNEL");
        distrChannel = "1000".equals(salesOrg)?FDStoreProperties.getDefaultFdDistributionChannel():distrChannel;
        this.model.setDistChannel(distrChannel);
        this.model.setScaleQuantity(rs.getDouble("SCALE_QTY"));
        String source = rs.getString("SOURCE");
        this.model.setSource((null!=source && !"".equals(source))?EnumEventSource.valueOf(source):null);
        this.model.setTaxCode(rs.getString("TAX_CODE"));
        this.model.setMaterialGroup(rs.getString("MATERIAL_GROUP"));
        ZoneInfo pricingZone=null;
        if(pricingZoneId==null) {
        	pricingZone=ZonePriceListing.DEFAULT_ZONE_INFO;
        } else {
        	pricingZone=new ZoneInfo(pricingZoneId,salesOrg,distrChannel);
        }
       UserContext userCtx=new UserContext();
        userCtx.setPricingContext(new PricingContext( pricingZone));//::FDX:: UserContext is not setup fully here.

        this.model.setUserContext(userCtx);
        this.model.setPlantID(rs.getString("PICKING_PLANT_ID"));


		this.unsetModified();
	}
	protected CouponLineList getCouponLinePBList() {
		CouponLineList lst = new CouponLineList();
		lst.setParentPK(this.getPK());
		if(null !=this.model.getCouponDiscount()){

				lst.add(new ErpCouponDiscountLinePersistentBean(this.model.getCouponDiscount()));

		}
		return lst;
	}


	private static class CouponLineList extends DependentPersistentBeanList {
	    @Override
		public void load(Connection conn) throws SQLException {
			this.set(ErpCouponDiscountLinePersistentBean.findByParent(conn, CouponLineList.this.getParentPK()));
	    }
	}
}
