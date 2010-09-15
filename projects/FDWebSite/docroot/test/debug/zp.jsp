<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.common.customer.*'%>
<%@ page import='com.freshdirect.fdstore.*'%>
<%@ page import='java.util.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.net.URLEncoder' %>
<%@ taglib uri='freshdirect' prefix='fd' %>




<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

		<title>Gift Card Examples</title>
		

	<script src="/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>
	<script src="/assets/javascript/scriptaculous.js?load=effects,builder" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/modalbox.js" type="text/javascript" language="javascript"></script>
	<script  src="/assets/javascript/FD_GiftCards.js" type="text/javascript" language="javascript"></script>

	<link href="/assets/css/pc_ie.css" rel="stylesheet" type="text/css" />
	<link href="/assets/css/giftcards.css" rel="stylesheet" type="text/css" />
	<link href="/assets/css/modalbox.css" rel="stylesheet" type="text/css" />


 </head>

<body>

<%
    FDSessionUser currentUser= (FDSessionUser)session.getAttribute( SessionName.USER );
    String zoneId=currentUser.getPricingZoneId();
    EnumServiceType serviceType=currentUser.getZPServiceType();
    String zipCode=currentUser.getZipCode(); 

%>
   <table border='1' length='50%'>
      <tr>
         <td width='100'>&nbsp;&nbsp;&nbsp;&nbsp;</td> 
         <td> SERVICE TYPE :</td>
         <td> <%=serviceType.getName()%></td>
      </tr>
      <tr>
         <td width='100'>&nbsp;&nbsp;&nbsp;&nbsp;</td> 
         <td> ZIP CODE :</td>
         <td> <%=zipCode%></td>
      </tr>
      <tr>
         <td width='100'>&nbsp;&nbsp;&nbsp;&nbsp;</td> 
         <td> ZONE ID :</td>
         <td> <%=zoneId%></td>
      </tr>
   </table>
   <table border='1' length='50%'>
      
<%
   


    
    String skuCode=request.getParameter("sku");
    if(skuCode!=null) {
      
          FDProductInfo prodInfo=FDCachedFactory.getProductInfo(skuCode);      
          //FDProduct product=FDCachedFactory.getProduct(skuCode);
          ZonePriceInfoListing zLis=prodInfo.getZonePriceInfoList();
          Collection zoneList=zLis.getZonePriceInfos();
          Iterator ite=zoneList.iterator();
          while(ite.hasNext()){
            ZonePriceInfoModel model=(ZonePriceInfoModel)ite.next();
            
 %>   
       <tr>
         <td width='100'>&nbsp;&nbsp;&nbsp;&nbsp;</td> 
         <td> ZONE ID :<%=model.getSapZoneId()%></td>
         <td> PRICE :<%=model.getDefaultPrice()%></td>
         <td> DAFAULT UNIT  :<%=prodInfo.getDefaultPriceUnit()%></td>         
         <td> DEAL PERCENTAGE  :<%=model.getDealPercentage()%></td>
         
         
      </tr>
   
<%             
          
          }
    }
   
%>
</table>           
<div id="error" style="clear: both;"></div>

</body>
</html>