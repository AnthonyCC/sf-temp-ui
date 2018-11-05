package com.freshdirect.sap.bapi;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.sap.ejb.SapException;
import com.freshdirect.sap.jco.JcoBapiFunctionFactory;

/**
 * Abstract factory for BAPIs.
 * 
 * @author kkanuganti
 */
public abstract class BapiFactory {

	private final static BapiFactory INSTANCE = new JcoBapiFunctionFactory();

	/**
	 * @return BapiFactory
	 */
	public static BapiFactory getInstance() {
		return INSTANCE;
	}

	/**
	 * @return BapiCreateCustomer
	 * @throws SapException
	 */
	public abstract BapiCreateCustomer getCreateCustomerBuilder()
			throws SapException;

	/**
	 * @param saleType
	 * @return BapiSalesOrderCreate
	 * @throws SapException
	 */
	public abstract BapiSalesOrderCreate getSalesOrderCreateBuilder(
			EnumSaleType saleType) throws SapException;
	
	
	/**
	 * @param saleType
	 * @return BapiSalesOrderCreate
	 * @throws SapException
	 */
	public abstract BapiSalesOrderCreate getSalesOrderPlantChangeBuilder() throws SapException;

	/**
	 * @return BapiMaterialAvailability
	 * @throws SapException
	 */
	public abstract BapiMaterialAvailability getMaterialAvailabilityBuilder(EnumATPRule atpRule)
			throws SapException;

	
	/**
	 * @return BapiSalesOrderSimulate
	 * @throws SapException
	 */
	public abstract BapiSalesOrderSimulate getSalesOrderSimulateBuilder()
			throws SapException;
	
	/**
	 * @return BapiSalesOrderSimulate
	 * @throws SapException
	 */
	public abstract BapiSalesOrderSimulate getCompositeSimulateBuilder()
			throws SapException;

	/**
	 * @return BapiSalesOrderChange
	 * @throws SapException
	 */
	public abstract BapiSalesOrderChange getSalesOrderChangeBuilder()
			throws SapException;
	
	/**
	 * @return BapiSalesOrderCancel
	 * @throws SapException
	 */
	public abstract BapiSalesOrderCancel getSalesOrderCancelBuilder()
			throws SapException;

	/**
	 * @return BapiSendSettlement
	 * @throws SapException
	 */
	public abstract BapiSendSettlement getSendSettlementBuilder()
			throws SapException;

	/**
	 * @return BapiPostReturnI
	 * @throws SapException
	 */
	public abstract BapiPostReturnI getPostReturnBuilder() throws SapException;

	/**
	 * @return BapiCartonInfo
	 * @throws SapException
	 */
	public abstract BapiCartonInfo getCartonInfoProvider() throws SapException;

	/**
	 * @return BapiSendEmployeeInfo
	 * @throws SapException
	 */
	public abstract BapiSendEmployeeInfo getSendEmployeeInfoSender()
			throws SapException;

	/**
	 * @return BapiRouteStatusInfo
	 * @throws SapException
	 */
	public abstract BapiRouteStatusInfo getBapiRouteStatusInfoBuilder()
			throws SapException;

	/**
	 * @param type 
	 * @return BapiProductPromotionPreviewI
	 * @throws SapException
	 */
	public abstract BapiProductPromotionPreviewI getBapiProductPromotionPreviewBuilder(
			EnumProductPromotionType type) throws SapException;

	/**
	 * @return BapiSendSettlementByCommand
	 * @throws SapException
	 */
	public abstract BapiSendSettlementByCommand getBapiSendEBTSettlementSender()
			throws SapException;
	
	/**
	 * @return BapiCartonDetailsForSale
	 * @throws SapException
	 */
	public abstract BapiCartonDetailsForSale getBapiCartonDetailsForSale()
			throws SapException;
	
	/**
	 * @return BapiSendSettlementByCommand
	 * @throws SapException
	 */
	public abstract BapiSendPickEligibleOrders getBapiSendPickEligibleOrders()
			throws SapException;
	
}


