<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@page import="com.freshdirect.customer.ErpPaymentMethodI"%>
<%@page import="java.util.Collection"%>
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
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<% 
boolean isByAddress = false;
boolean isByTimeSlot = false;
boolean isByPayment = false;
boolean isByMaxRedemptions = false;
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
if ( "true".equals((String)request.getParameter("isByMaxRedemptions")) ) {
	isByMaxRedemptions = true;
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
			FDIdentity identity = user.getIdentity();
			Collection<ErpPaymentMethodI> paymentMethods = FDCustomerManager.getPaymentMethods( identity );
			ErpPaymentMethodI paymentMethod = null;

			for ( ErpPaymentMethodI item : paymentMethods ) {
				if ( item.getPK().getId().equals( paymentMethodList ) ) {
					paymentMethod = item;
					break;
				}
			}
			result = PromotionHelper.checkPromoEligibilityForPayment(user,paymentMethod);			
		}		
	}
	else if(isByMaxRedemptions){
		result = PromotionHelper.checkPromoEligibilityForMaxRedemptions(user);
	}
}
if(!result){
		json = new JSONObject();
		json.put("status", "error");
		json.put("promoCode", user.getRedeemedPromotion().getRedemptionCode());
	
	if(null != promotion && !"".equals(promotion.getPromotionCode())){ %>
		<fd:GetPromotionNew id="promotionNew" promotionId="<%= promotion.getPromotionCode() %>">
		<%
			String promoTerms = (promotionNew.getTerms() != null && !"".equals(promotionNew.getTerms())) ? promotionNew.getTerms() : "No additional information is available.";
			json.put("promoTerms", promoTerms);
		%>
		</fd:GetPromotionNew>
	<% } %>
<% } %>

 <%=json.toString()%>