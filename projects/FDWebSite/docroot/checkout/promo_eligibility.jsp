<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='org.json.JSONObject' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.PromotionHelper' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>


<% 
boolean isByAddress = false;
boolean isByTimeSlot = false;
boolean isByPayment = false;
boolean result = true;

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
if(null != user.getRedeemedPromotion()){
	if(isByAddress){
		String addressOrLocation = request.getParameter( "selectAddressList" );
		
		if ( addressOrLocation != null && addressOrLocation.startsWith( "field_" ) ) {
			addressOrLocation = request.getParameter( addressOrLocation.substring( "field_".length() ) );
		}				
		result = PromotionHelper.checkPromoEligibilityForAddress(user,addressOrLocation);		
	}
	
	else if(isByTimeSlot){
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