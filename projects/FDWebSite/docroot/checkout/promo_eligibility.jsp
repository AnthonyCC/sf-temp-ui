
<%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@page import="com.freshdirect.customer.ErpAddressModel"%>
<%@page import="com.freshdirect.customer.ErpDepotAddressModel"%>
<%@page import="com.freshdirect.common.address.AddressModel"%>
<%@page import="com.freshdirect.delivery.depot.DlvLocationModel"%>
<%@page import="com.freshdirect.fdstore.FDDepotManager"%>
<%@page import="com.freshdirect.delivery.depot.DlvDepotModel"%><%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='org.json.JSONObject' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.PromotionHelper' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.fdstore.promotion.Promotion' %>


<% 
boolean isByAddress = false;
boolean isByTimeSlot = false;
boolean isByPayment = false;
boolean result = true;
boolean hasDiscountedTimeslots = "true".equals((String)request.getParameter("hasDiscountedTimeslots"));
FDUserI user = (FDUserI)session.getAttribute( SessionName.USER );
JSONObject json = new JSONObject();
json.put("status", "ok");

if ( "true".equals((String)request.getParameter("isByAddress")) ) {
	isByAddress = true;
}
if ( "true".equals((String)request.getParameter("isByTimeSlot")) ) {
	isByTimeSlot = true;
}
if ( "true".equals((String)request.getParameter("isByPayment")) ) {
	isByPayment = true;
}
Promotion promotion = (Promotion)user.getRedeemedPromotion();
if(null != promotion && user.getPromotionEligibility().isEligible(promotion.getPromotionCode())){
	if(isByAddress){
		String addressOrLocation = request.getParameter( "selectAddressList" );
		if ( addressOrLocation != null && addressOrLocation.startsWith( "field_" ) ) {
			addressOrLocation = request.getParameter( addressOrLocation.substring( "field_".length() ) );
		}		
		
		if ( addressOrLocation.startsWith( "DEPOT_" ) ) {
			String locationId = addressOrLocation.substring( "DEPOT_".length() );
			DlvDepotModel depot = FDDepotManager.getInstance().getDepotByLocationId( locationId );
			DlvLocationModel location = depot.getLocation( locationId );
			if ( depot != null ) {
				AddressModel addrModel = location.getAddress();
				ErpDepotAddressModel depotAddress = new ErpDepotAddressModel(addrModel);
				depotAddress.setRegionId( depot.getRegionId());
				depotAddress.setZoneCode( location.getZoneCode() );
				depotAddress.setLocationId( location.getPK().getId() );
				depotAddress.setFacility( location.getFacility() );
				depotAddress.setPickup( depot.isPickup());
				result = PromotionHelper.checkPromoEligibilityForAddress(user,depotAddress);		
			} 
		} else {
			ErpAddressModel shippingAddress = FDCustomerManager.getAddress(user.getIdentity(), addressOrLocation);
			result = PromotionHelper.checkPromoEligibilityForAddress(user,shippingAddress);		
		}
	}
	
	else if(isByTimeSlot && hasDiscountedTimeslots){
		String deliveryTimeslotId = request.getParameter( "deliveryTimeslotId" );
		if(null != deliveryTimeslotId){
			result = PromotionHelper.checkPromoEligibilityForTimeslot(user,deliveryTimeslotId);			
		}
	}
	else if(isByPayment){
		String paymentMethodList = request.getParameter( "paymentMethodList" );
		if(null != paymentMethodList){
			result = PromotionHelper.checkPromoEligibilityForPayment(user,paymentMethodList);			
		}		
	}
}
if(!result){
	json = new JSONObject();
	json.put("status", "error");
	json.put("promoCode", user.getRedeemedPromotion().getRedemptionCode());
}
%>

 <%=json.toString()%>