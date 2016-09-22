package com.freshdirect.fdstore;

import java.util.List;
import java.util.Map;

import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.framework.util.DayOfWeekSet;

/**
 * FD class representing an ERP material.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDMaterial extends FDAttributeProxy {

	private static final long	serialVersionUID	= -3900022254504839075L;
	
	private final String materialNumber;
	private final String salesUnitCharacteristic;
	private final String quantityCharacteristic;
	private final EnumAlcoholicContent alcoholicContent;
	private final boolean taxable;
	private String taxCode;
	private final String skuCode;
	private Map<String, FDPlantMaterial> materialPlants;
	private Map<SalesAreaInfo, FDMaterialSalesArea> materialSalesAreas;
	private final String materialGroup;
	

	/*public FDMaterial(
		AttributesI attributes,
		String materialNumber,
		EnumATPRule atpRule,
		String salesUnitCharacteristic,
		String quantityCharacteristic,
		EnumAlcoholicContent alcoholicContent,
		boolean taxable,
		boolean kosherProduction,
		boolean platter,
		DayOfWeekSet blockedDays,
		int leadTime) {
		super(attributes);
		this.materialNumber = materialNumber;
		this.atpRule = atpRule;
		this.salesUnitCharacteristic = salesUnitCharacteristic;
		this.quantityCharacteristic = quantityCharacteristic;
		this.alcoholicContent = alcoholicContent;
		this.taxable = taxable;
		this.kosherProduction = kosherProduction;
		this.platter = platter;
		this.blockedDays = blockedDays;
		this.leadTime = leadTime;
	}*/
	
	public FDMaterial(
			AttributesI attributes,
			String materialNumber,
			String salesUnitCharacteristic,
			String quantityCharacteristic,
			EnumAlcoholicContent alcoholicContent,
			boolean taxable,String skuCode, String taxCode, String materialGroup) {
			super(attributes);
			this.materialNumber = materialNumber;
			this.salesUnitCharacteristic = salesUnitCharacteristic;
			this.quantityCharacteristic = quantityCharacteristic;
			this.alcoholicContent = alcoholicContent;
			this.taxable = taxable;
			this.skuCode = skuCode;
			this.taxCode = taxCode;
			this.materialGroup = materialGroup;
		}

	public String getMaterialNumber() {
		return this.materialNumber;
	}

	/**
	 * Get the name of the characteristic to put the selected sales unit in.
	 *
	 * @return null or empty String if none
	 */
	public String getSalesUnitCharacteristic() {
		return this.salesUnitCharacteristic;
	}

	/**
	 * Get the name of the characteristic to put the ordered quantity in.
	 *
	 * @return null or empty String if none
	 */
	public String getQuantityCharacteristic() {
		return this.quantityCharacteristic;
	}

	public EnumAlcoholicContent getAlcoholicContent() {
		return this.alcoholicContent;
	}

	public boolean isTaxable() {
		return this.taxable;
	}

	
	
	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	
	

	public String getMaterialGroup() {
		return materialGroup;
	}

	/**
	 * @return the skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	@Override
	public String toString() {
	    return "FDMaterial[materialNumber:'" + materialNumber + "' salesUnitCharacteristic:'" + salesUnitCharacteristic
                + "' quantityCharacteristic:'" + quantityCharacteristic 
                + "' alcoholicContent:'" + alcoholicContent 
                + "' taxable:'" + taxable ;
	}

	
	
	/**
	 * @return the materialPlants
	 */
	public Map<String, FDPlantMaterial> getMaterialPlants() {
		return materialPlants;
	}

	/**
	 * @param materialPlants the materialPlants to set
	 */
	public void setMaterialPlants(Map<String, FDPlantMaterial> materialPlants) {
		this.materialPlants = materialPlants;
	}

	/**
	 * @return the materialSalesAreas
	 */
	public Map<SalesAreaInfo, FDMaterialSalesArea> getMaterialSalesAreas() {
		return materialSalesAreas;
	}

	/**
	 * @param materialSalesAreas the materialSalesAreas to set
	 */
	public void setMaterialSalesAreas(
			Map<SalesAreaInfo, FDMaterialSalesArea> materialSalesAreas) {
		this.materialSalesAreas = materialSalesAreas;
	}

	//TODO: Refactor these methods later, after doing all the required changes wrt plantId in the context.
	public int getLeadTime(){
		return getLeadTime(null);
	}
	
	public int getLeadTime(String plantId){
		return null !=materialPlants.get(plantId)?materialPlants.get(plantId).getLeadTime():0;
	}
	
	/*public DayOfWeekSet getBlockedDays(){
		return getBlockedDays(null);//::FDX::
	}*/
	
	public DayOfWeekSet getBlockedDays(String plantId){
		return null!=materialPlants.get(plantId)?materialPlants.get(plantId).getBlockedDays():DayOfWeekSet.EMPTY;
	}
	
	/*public boolean isKosherProduction(){
		return isKosherProduction(null);
	}*/
	
	public boolean isKosherProduction(String plantId){
		return null!=materialPlants.get(plantId)?materialPlants.get(plantId).isKosherProduction():false;
	}
	
	/*public boolean isPlatter(){
		return isPlatter(null);
	}*/
	
	public boolean isPlatter(String plantId){
		return null!=materialPlants.get(plantId)?materialPlants.get(plantId).isPlatter():false;
	}
	
	public EnumATPRule getAtpRule(){
		return getAtpRule(null);
	}
	
	public EnumATPRule getAtpRule(String plantId){
		return null!=materialPlants.get(plantId)?materialPlants.get(plantId).getAtpRule():null;
	}
}
