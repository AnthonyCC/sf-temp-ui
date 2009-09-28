package com.freshdirect.dataloader.autoorder.create.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDuplicatePaymentMethodException;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.dataloader.autoorder.create.util.Card;
import com.freshdirect.dataloader.autoorder.create.util.CardUtil;
import com.freshdirect.dataloader.autoorder.create.util.IConstants;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ejb.GeographyDAO;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUser;

public class CustomerConsumer implements IConsumer  {
	
	private static final String PAYMENT_CHECK = "select * from cust.paymentmethod where account_number = ?";
	
	
	public void start(String filePath, Date baseDate)  {		
	}
	
	public synchronized void consume(Object object, Connection conn) {
		try {
			OrderBean model = (OrderBean)object;
			createCustomers(conn, model);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void createCustomers(Connection conn, OrderBean orderBean) throws SQLException, InvalidAddressException {
        
		GeographyDAO dao = new GeographyDAO();
		
		AddressModel address = new AddressModel();
		address.setAddress1(orderBean.getAddress1());
       	address.setAddress2(orderBean.getAddress2());
       	address.setApartment(orderBean.getApt());
       	address.setCity(orderBean.getCity());
       	address.setState(orderBean.getState());
       	address.setZipCode(orderBean.getZip());
       	
		dao.geocode(address,true, conn);
		
		ErpCustomerInfoModel erpCustInfo = new ErpCustomerInfoModel();
        erpCustInfo.setTitle("Mr.");
        erpCustInfo.setFirstName("F"+orderBean.getCustomerId());
        erpCustInfo.setMiddleName("M"+orderBean.getCustomerId());
        erpCustInfo.setLastName("L"+orderBean.getCustomerId());
        erpCustInfo.setHomePhone(new PhoneNumber("212 212 2121"));
        erpCustInfo.setCellPhone(new PhoneNumber("917 917 9171"));
        erpCustInfo.setBusinessPhone(new PhoneNumber("666 666 6666"));
        erpCustInfo.setOtherPhone(new PhoneNumber("777 777 7777"));
        erpCustInfo.setFax(new PhoneNumber("999 999 9999"));
        erpCustInfo.setEmail(orderBean.getCustomerId() + "@freshdirect.com");
        erpCustInfo.setAlternateEmail(orderBean.getCustomerId() + "@freshdirect.com");
        erpCustInfo.setEmailPlaintext(false);
        erpCustInfo.setReceiveNewsletter(true);
        erpCustInfo.setWorkDepartment("test planning");
        
        ErpCustomerModel erpCust = new ErpCustomerModel();
        erpCust.setCustomerInfo(erpCustInfo);
        //System.out.println(erpCustInfo.getEmail());
        erpCust.setUserId(erpCustInfo.getEmail());
        erpCust.setPasswordHash("test");
        erpCust.setActive(true);
        ErpAddressModel shipTo = new ErpAddressModel();
        shipTo.setFirstName(erpCustInfo.getFirstName());
        shipTo.setLastName(erpCustInfo.getLastName());
        shipTo.setAddress1(orderBean.getAddress1());
        shipTo.setAddress2(orderBean.getAddress2());
        shipTo.setApartment(orderBean.getApt());
        shipTo.setCity(orderBean.getCity());
        shipTo.setState(orderBean.getState());
        shipTo.setZipCode(orderBean.getZip());
        shipTo.setCountry(orderBean.getCountry());
        shipTo.setPhone(erpCustInfo.getHomePhone());    
        shipTo.setAddressInfo(address.getAddressInfo());
        erpCust.addShipToAddress(shipTo);
        
        Card c = CardUtil.generateCards ();
        while(hasPayment(conn, c.getNumber())) {
        	c = CardUtil.generateCards ();
        }
        ErpCreditCardModel erpCC = new ErpCreditCardModel();
        erpCC.setName(erpCustInfo.getFirstName() + " " + erpCustInfo.getLastName());
        erpCC.setAccountNumber(c.getNumber());
        erpCC.setCardType(EnumCardType.getCardType(c.getBrand()));
        erpCC.setExpirationDate(new java.util.Date(System.currentTimeMillis() + 365*24*60*60*1000));
        erpCC.setAddress1(shipTo.getAddress1());
        erpCC.setAddress2(shipTo.getAddress2());
        erpCC.setApartment(shipTo.getApartment());
        erpCC.setCity(shipTo.getCity());
        erpCC.setState(shipTo.getState());
        erpCC.setZipCode(shipTo.getZipCode());
        erpCC.setCountry(shipTo.getCountry());
        erpCust.addPaymentMethod(erpCC);
        
        FDCustomerModel fdCust = new FDCustomerModel();
        fdCust.setPasswordHint("Confidential");
        
        try {
            
            FDUser user = FDCustomerManager.createNewUser(shipTo, EnumServiceType.getEnum(orderBean.getServiceType()));
			FDActionInfo actionInfo = new FDActionInfo(EnumTransactionSource.SYSTEM, user.getIdentity(), "AutoOrder", "",IConstants.AGENT);
            FDCustomerManager.register(actionInfo, erpCust, fdCust, user.getCookie(), false, true, null,EnumServiceType.getEnum(orderBean.getServiceType()));
            
        } catch (FDResourceException fdre) {
            fdre.printStackTrace();	            
        } catch (ErpDuplicateUserIdException eduie) {
        	eduie.printStackTrace();	
        } 
        
    }
		
	
	private static boolean hasPayment(Connection conn, String number) throws SQLException {
		boolean tmpBoolean = false;
				
		PreparedStatement ps = conn.prepareStatement(PAYMENT_CHECK);
		ps.setString(1, number);
		
		ResultSet rs = ps.executeQuery();
				
		while (rs.next()) {				
			rs.close();
			ps.close();			
			return true;		
		}
						
		return false;
	}
	
	public void end() {
		
	}
}
