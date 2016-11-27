/**
 * 
 */
package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.model.ErpPlantMaterialModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.VersionedPrimaryKey;
import com.freshdirect.framework.util.DayOfWeekSet;

/**
 * @author ksriram
 *
 */
public class ErpPlantMaterialPersistentBean extends ErpPersistentBeanSupport
{
	private static final long serialVersionUID = -906626747734317784L;

	private String plantId;
	
	private boolean kosherProduction;

	private boolean platter;

	private DayOfWeekSet blockedDays;	
	
	/** the ATPRule to use when checking for availablility of this material */
	private EnumATPRule atpRule;
	
	/** product rating */
	private String rating;
	
	/** product days in house */
	private String days_in_house;
	
	/** sustainability rating */
	private String sustainabilityRating;

	/** the lead time in days to stock this product **/
	private int leadTime;
	
	/** hide if its out of stock **/
	private boolean hideOutOfStock;
	
	/**
	 * @param model
	 */
	public ErpPlantMaterialPersistentBean(ErpPlantMaterialModel model) {
		super();
		this.setFromModel(model);
	}
	
	
	/**
	 * @param pk
	 * @param plantId
	 * @param kosherProduction
	 * @param platter
	 * @param blockedDays
	 * @param atpRule
	 * @param rating
	 * @param days_in_house
	 * @param sustainabilityRating
	 * @param leadTime
	 */
	public ErpPlantMaterialPersistentBean(VersionedPrimaryKey pk,String plantId,
			boolean kosherProduction, boolean platter,
			DayOfWeekSet blockedDays, EnumATPRule atpRule, String rating, String days_in_house,
			String sustainabilityRating, int leadTime, boolean hideOutStock) {
		super(pk);
		this.plantId = plantId;
		this.kosherProduction = kosherProduction;
		this.platter = platter;
		this.blockedDays = blockedDays;
		this.atpRule = atpRule;
		this.rating = rating;
		this.days_in_house = days_in_house;
		this.sustainabilityRating = sustainabilityRating;
		this.leadTime = leadTime;
		this.hideOutOfStock = hideOutStock;
	}


	@Override
	public ModelI getModel() {
		ErpPlantMaterialModel model =
				new  ErpPlantMaterialModel(this.plantId, this.kosherProduction,
						this.platter, this.blockedDays, this.atpRule,
						this.rating, this.days_in_house,
						this.sustainabilityRating, this.leadTime, this.hideOutOfStock);
			super.decorateModel(model);
			return model;
	}

	
	@Override
	public PrimaryKey create(Connection conn) throws SQLException {
		if(null !=this.plantId){
			String id = this.getNextId(conn, "ERPS");
			int version = ((VersionedPrimaryKey) this.getParentPK()).getVersion();
			PreparedStatement ps = conn
					.prepareStatement("insert into erps.PLANT_MATERIAL (ID,VERSION,MAT_ID,RATING,DAYS_IN_HOUSE,SUSTAINABILITY_RATING,PLANT_ID,KOSHER_PRODUCTION, BLOCKED_DAYS, ATP_RULE, LEAD_TIME,PLATTER,HIDE_OOS) values (?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)");
			ps.setString(1, id);
			ps.setInt(2, version);
			ps.setString(3, this.getParentPK().getId());/*		
			ps.setString(4, this.unavailabilityStatus);
	      	ps.setTimestamp(5, new Timestamp(this.unavailabilityDate.getTime()));
	      	ps.setString(6, this.unavailabilityReason);*/
			ps.setString(4, this.rating);
			ps.setString(5, this.days_in_house);
			ps.setString(6,this.sustainabilityRating);
			ps.setString(7, this.plantId);
			ps.setString(8, this.kosherProduction ? "X" : "");
			ps.setString(9, this.blockedDays == null ? "" : this.blockedDays.encode());		
			ps.setInt(10, null !=this.atpRule?this.atpRule.getValue():0);
			ps.setInt(11, this.leadTime);
			ps.setString(12, this.platter ? "X" : "");
			ps.setString(13, this.hideOutOfStock ? "X" : "");
			
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
		ErpPlantMaterialModel plantMaterialModel = (ErpPlantMaterialModel)model;
		if(null != plantMaterialModel){
			this.plantId = plantMaterialModel.getPlantId();		
			this.kosherProduction = plantMaterialModel.isKosherProduction();
			this.platter = plantMaterialModel.isPlatter();
			this.blockedDays = plantMaterialModel.getBlockedDays();
			this.atpRule = plantMaterialModel.getAtpRule();		
			this.rating = plantMaterialModel.getRating();
			this.days_in_house = plantMaterialModel.getDays_in_house();
			this.sustainabilityRating = plantMaterialModel.getSustainabilityRating();
			this.leadTime = plantMaterialModel.getLeadTime();
			this.hideOutOfStock = plantMaterialModel.isHideOutOfStock();
		}
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
				.prepareStatement("SELECT ID, PLANT_ID, ATP_RULE, RATING, DAYS_IN_HOUSE, SUSTAINABILITY_RATING, KOSHER_PRODUCTION, BLOCKED_DAYS, LEAD_TIME, PLATTER, HIDE_OOS from ERPS.PLANT_MATERIAL where mat_id = ?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()){
			ErpPlantMaterialPersistentBean bean = new ErpPlantMaterialPersistentBean(new VersionedPrimaryKey(rs.getString("ID"),	parentPK.getVersion())
					, rs.getString("PLANT_ID"), "X".equalsIgnoreCase(rs.getString("KOSHER_PRODUCTION"))
					, "X".equalsIgnoreCase(rs.getString("PLATTER")), DayOfWeekSet.decode(rs.getString("BLOCKED_DAYS"))
					, EnumATPRule.getEnum(rs.getInt("ATP_RULE")), rs.getString("RATING"), rs.getString("DAYS_IN_HOUSE")
					, rs.getString("SUSTAINABILITY_RATING"),  rs.getInt("LEAD_TIME"),"X".equalsIgnoreCase(rs.getString("HIDE_OOS")));
			
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}
		
}
