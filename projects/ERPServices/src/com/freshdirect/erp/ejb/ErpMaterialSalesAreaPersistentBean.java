/**
 * 
 */
package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.DayOfWeekSet;

/**
 * @author ksriram
 *
 */
public class ErpMaterialSalesAreaPersistentBean extends
		ErpPersistentBeanSupport {
	
	/** sales organization */
	private String salesOrg;
	
	/** distribution channel */
	private String distChannel;
	
	/** unavailability status code */
    private String unavailabilityStatus;
    
    /** unavailability date */
    private Date unavailabilityDate;
    
    /** unavailability reason */
    private String unavailabilityReason;
    
    /** sku code */
    private String skuCode;
    
    /** Day Part Selling */
    private String dayPartSelling;

	public ErpMaterialSalesAreaPersistentBean(VersionedPrimaryKey pk,String salesOrg,
			String distChannel, String unavailabilityStatus,
			Date unavailabilityDate, String unavailabilityReason, String skuCode, String dayPartSelling) {
		super(pk);
		this.salesOrg = salesOrg;
		this.distChannel = distChannel;
		if(unavailabilityStatus==null)
			this.unavailabilityStatus = EnumAvailabilityStatus.AVAILABLE.getStatusCode();
		else 
			this.unavailabilityStatus=unavailabilityStatus;
		this.unavailabilityDate = unavailabilityDate;
		this.unavailabilityReason = unavailabilityReason;
		this.skuCode = skuCode;
		this.dayPartSelling = dayPartSelling;
	}

	/**
	 * @param model
	 */
	public ErpMaterialSalesAreaPersistentBean(ErpMaterialSalesAreaModel model) {
		super();
		this.setFromModel(model);
	}
	/* (non-Javadoc)
	 * @see com.freshdirect.framework.core.PersistentBeanI#getModel()
	 */
	@Override
	public ModelI getModel() {
		ErpMaterialSalesAreaModel salesAreaModel = new ErpMaterialSalesAreaModel(this.salesOrg,
				this.distChannel, this.unavailabilityStatus,
				this.unavailabilityDate, this.unavailabilityReason, this.skuCode, this.dayPartSelling);
		super.decorateModel(salesAreaModel);
		return salesAreaModel;
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.framework.core.PersistentI#create(java.sql.Connection)
	 */
	@Override
	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "ERPS");
		int version = ((VersionedPrimaryKey) this.getParentPK()).getVersion();
		if(null !=salesOrg && null != distChannel){
			PreparedStatement ps = conn.prepareStatement("INSERT INTO ERPS.MATERIAL_SALES_AREA (ID, MAT_ID, VERSION, SALES_ORG, DISTRIBUTION_CHANNEL, UNAVAILABILITY_STATUS, UNAVAILABILITY_REASON, UNAVAILABILITY_DATE, SKU_CODE, DATE_CREATED, DAYPART_VALUE) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, id);
			ps.setString(2, this.getParentPK().getId());
			ps.setInt(3, version);
			ps.setString(4, this.salesOrg);
			ps.setString(5, this.distChannel);
			ps.setString(6, this.unavailabilityStatus);
			ps.setString(7, this.unavailabilityReason);
			if(null !=this.unavailabilityDate){
				ps.setTimestamp(8, new java.sql.Timestamp(this.unavailabilityDate.getTime()));
			}else{
				ps.setNull(8, java.sql.Types.TIMESTAMP);
			}
			
			ps.setString(9, this.skuCode);
			ps.setTimestamp(10, new java.sql.Timestamp(new Date().getTime()));
			ps.setString(11, this.dayPartSelling);
			if (ps.executeUpdate() != 1) {
				throw new SQLException("No database rows created!");
			}
			ps.close();
	
			this.setPK(new VersionedPrimaryKey(id, version));
		}
		return this.getPK();
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.framework.core.PersistentBeanSupport#setFromModel(com.freshdirect.framework.core.ModelI)
	 */
	@Override
	public void setFromModel(ModelI model) {
		ErpMaterialSalesAreaModel materialSalesAreaModel =(ErpMaterialSalesAreaModel)model;
		this.salesOrg = materialSalesAreaModel.getSalesOrg();
		this.distChannel = materialSalesAreaModel.getDistChannel();
		this.unavailabilityStatus = materialSalesAreaModel.getUnavailabilityStatus();
		this.unavailabilityDate = materialSalesAreaModel.getUnavailabilityDate();
		this.unavailabilityReason = materialSalesAreaModel.getUnavailabilityReason();
		this.skuCode = materialSalesAreaModel.getSkuCode();
		this.dayPartSelling = materialSalesAreaModel.getDayPartSelling();
	}
	
	/**
	 * @param conn
	 * @param parentPK
	 * @return List
	 * @throws SQLException
	 */
	public static List findByParent(Connection conn, VersionedPrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn
				.prepareStatement("SELECT SKU_CODE, ID, UNAVAILABILITY_STATUS, UNAVAILABILITY_DATE, UNAVAILABILITY_REASON, SALES_ORG, DISTRIBUTION_CHANNEL, DAYPART_VALUE from ERPS.MATERIAL_SALES_AREA where mat_id = ?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		String unavailStatus="";
		while (rs.next()){
			unavailStatus=rs.getString("UNAVAILABILITY_STATUS");
			ErpMaterialSalesAreaPersistentBean bean = new ErpMaterialSalesAreaPersistentBean(new VersionedPrimaryKey(rs.getString("ID"),	parentPK.getVersion())
					, rs.getString("SALES_ORG"), rs.getString("DISTRIBUTION_CHANNEL")
					, "AVAL".equals(unavailStatus)?"":unavailStatus, rs.getDate("UNAVAILABILITY_DATE")
					, rs.getString("UNAVAILABILITY_REASON"), rs.getString("SKU_CODE"),rs.getString("DAYPART_VALUE"));
			
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}

	
}
