<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.payment.BillingCountryInfo"%>
<%@ page import="com.freshdirect.payment.BillingRegionInfo"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.AddressName"
contentType="text/plain; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
String countryCode=request.getParameter("country");  
StringBuffer buffer=new StringBuffer(200);  
 try{
 BillingCountryInfo bc=BillingCountryInfo.getEnum(countryCode);
 List<BillingRegionInfo> _list=new ArrayList<BillingRegionInfo>();
 if(bc!=null)
	_list=bc.getRegions();
   for(BillingRegionInfo regionInfo : _list) {
	buffer.append(regionInfo.getCode()).append("|").append( regionInfo.getName()).append(",");
   }  
 response.getWriter().println(buffer.substring(0,buffer.length()-1)); 
 }
 catch(Exception e){
     System.out.println(e);
 }

 %>
 