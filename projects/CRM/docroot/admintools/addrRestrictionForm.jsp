<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.delivery.model.RestrictedAddressModel"%>
<%@ page import="com.freshdirect.fdlogistics.model.EnumRestrictedAddressReason"%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.webapp.ActionError' %>
<%@ page import='java.util.List' %>
<html>
<head></head>
	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/javascript/jscalendar-1.0/calendar-system.css" type="text/css">
	<script language="JavaScript" src="/assets/javascript/common_javascript.js"></script>
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/lang/calendar-en.js"></script>
    <script type="text/javascript" src="/ccassets/javascript/jscalendar-1.0/calendar-setup.js"></script>

    <script language="JavaScript">
    
    function doAction(actionName) {
             document.addr_restriction_detail.submit();                
	}
     </script> 

    <% 
      String address1=request.getParameter("address1");
      String reason = NVL.apply(request.getParameter("reason"), "all");
	  String apartment = NVL.apply(request.getParameter("apartment"), "");
      String zipCode = NVL.apply(request.getParameter("zipCode"), "");
                              
    %>
    

</html>