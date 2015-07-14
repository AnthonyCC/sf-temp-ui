package com.freshdirect.sap.jco;

import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.erp.EnumProductPromotionType;
import com.freshdirect.sap.bapi.BapiCartonDetailsForSale;
import com.freshdirect.sap.bapi.BapiCartonInfo;
import com.freshdirect.sap.bapi.BapiCreateCustomer;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiMaterialAvailability;
import com.freshdirect.sap.bapi.BapiPostReturnI;
import com.freshdirect.sap.bapi.BapiProductPromotionPreviewI;
import com.freshdirect.sap.bapi.BapiRouteStatusInfo;
import com.freshdirect.sap.bapi.BapiSalesOrderCancel;
import com.freshdirect.sap.bapi.BapiSalesOrderChange;
import com.freshdirect.sap.bapi.BapiSalesOrderCreate;
import com.freshdirect.sap.bapi.BapiSalesOrderSimulate;
import com.freshdirect.sap.bapi.BapiSendEmployeeInfo;
import com.freshdirect.sap.bapi.BapiSendSettlement;
import com.freshdirect.sap.bapi.BapiSendSettlementByCommand;
import com.freshdirect.sap.ejb.SapException;
import com.sap.conn.jco.JCoException;


/**
 * Abstract factory for builders.
 *
 * @author kkanuganti
 */
public class JcoBapiFunctionFactory extends BapiFactory {
    
	public BapiCreateCustomer getCreateCustomerBuilder() throws SapException
	{
		try {
			return new JcoBapiCreateCustomer();
		} catch (JCoException e) {
			throw new SapException(e);
		}
	}

	public BapiSalesOrderCreate getSalesOrderCreateBuilder(EnumSaleType saleType) throws SapException
	{
		try {
			String functionName = "";
			if (EnumSaleType.REGULAR.equals(saleType)) {
				functionName = "ZBAPI_SALESORDER_CREATEFROMDAT";
			} else if (EnumSaleType.SUBSCRIPTION.equals(saleType)) {
				functionName = "ZBAPI_SALESORDER_XOR_CREATE";
			} else if (EnumSaleType.GIFTCARD.equals(saleType)) {
				functionName = "ZBAPI_SALESORDER_XOR_CREATE";
			} else if (EnumSaleType.DONATION.equals(saleType)) {
				functionName = "ZBAPI_SALESORDER_XOR_CREATE";
			}
			else {
				return new JcoBapiSalesOrderCreate();
			}
			return new JcoBapiSalesOrderCreate(functionName);
		} catch (JCoException e) {
			throw new SapException(e);
		}
	}

	public BapiMaterialAvailability getMaterialAvailabilityBuilder() throws SapException
	{
		try
		{
			return new JcoBapiMaterialAvailability();
		} 
		catch (JCoException e) {
			throw new SapException(e);
		}
	}
	
	public BapiSalesOrderSimulate getSalesOrderSimulateBuilder() throws SapException
	{
		try
		{
			return new JcoBapiSalesOrderSimulate(false);
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}
	
	public BapiSalesOrderSimulate getCompositeSimulateBuilder() throws SapException
	{
		try
		{
			return new JcoBapiSalesOrderSimulate(true);
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}

	public BapiSalesOrderChange getSalesOrderChangeBuilder()  throws SapException
	{
		try{
			return new JcoBapiSalesOrderChange();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}

	public BapiSalesOrderCancel getSalesOrderCancelBuilder() throws SapException
	{
		try 
		{
			return new JcoBapiSalesOrderCancel();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}

	public BapiSendSettlement getSendSettlementBuilder() throws SapException {
		try
		{
			return new JcoBapiSendSettlement();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}
	
	public BapiPostReturnI getPostReturnBuilder() throws SapException
	{
		try 
		{
			return new JcoBapiPostReturn();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}
	
	public BapiCartonInfo getCartonInfoProvider() throws SapException
	{
		try
		{
			return new JcoBapiCartonInfo();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}

	public BapiSendEmployeeInfo getSendEmployeeInfoSender() throws SapException
	{
		try
		{
			return new JcoBapiSendEmployeeInfo();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}

	@Override
	public BapiRouteStatusInfo getBapiRouteStatusInfoBuilder() throws SapException
	{
		try 
		{
			return new JcoBapiRouteStatusInfo();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}

	@Override
	public BapiProductPromotionPreviewI getBapiProductPromotionPreviewBuilder(EnumProductPromotionType type) throws SapException {
		try
		{
			if(EnumProductPromotionType.PRODUCTS_ASSORTMENTS.equals(type)){
				return new JcoBapiProductPromotionPreview("ZDDPA_PREVIEW");
			}
			return new JcoBapiProductPromotionPreview("ZDDPP_PREVIEW");
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}
	@Override
	public BapiSendSettlementByCommand getBapiSendEBTSettlementSender() throws SapException
	{
		try
		{
			return new JcoBapiSendSettlementByCommand();
		} catch (JCoException e)
		{
			throw new SapException(e);
		}
	}

	@Override
	public BapiCartonDetailsForSale getBapiCartonDetailsForSale() throws SapException
	{
		try
		{
			return new JcoBapiCartonDetailsForSale();
		} 
		catch (JCoException e)
		{
			throw new SapException(e);
		}
	}
}