<%@page import="com.freshdirect.fdstore.util.TimeslotLogic"%>
<%@page import="com.freshdirect.fdstore.customer.FDIdentity"%>
<%@ page import='org.json.JSONObject' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.fdlogistics.model.FDTimeslot' %>
<%@ page import='com.freshdirect.fdstore.FDDeliveryManager' %>
<% 
boolean result = true;
FDUserI user = (FDUserI)session.getAttribute( SessionName.USER );
JSONObject json = new JSONObject();
json.put("status", "ok");
String deliveryTimeSlotId = (String)request.getParameter("deliveryTimeSlotId");

FDTimeslot timeslot = FDDeliveryManager.getInstance().getTimeslotsById(deliveryTimeSlotId, user.getShoppingCart().getDeliveryAddress().getBuildingId(), true);
TimeslotLogic.applyOrderMinimum(user, timeslot);
json.put("minordermet", timeslot.isMinOrderMet());
json.put("minorderamt", timeslot.getMinOrderAmt());

if(!result){
		json = new JSONObject();
		json.put("status", "error");
		json.put("minordermet", true);
 } 
%>
 <%=json.toString()%>