<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionType" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.delivery.restriction.OneTimeRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.OneTimeReverseRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.RecurringRestriction"%>
<%@ page import="com.freshdirect.delivery.restriction.RestrictionI"%>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.framework.webapp.ActionError' %>
<%@ page import='java.util.List' %>
<%@ page import="com.freshdirect.framework.webapp.*" %>

	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<link rel="stylesheet" href="/ccassets/css/case.css" type="text/css">	
	<script language="JavaScript" src="/ccassets/javascript/callcenter_javascript.js"></script>
    
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Add Platter Restrictions</tmpl:put>
<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/admintools_nav.jsp" />
<%! DateFormat DLV_TIME_FORMATTER = new SimpleDateFormat("hh:mm a");
DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a"); %>
    <% 
      String restrictionId=request.getParameter("restrictionId");
      Date startDt=null;
      if(null !=request.getParameter("startDate")){
    	  startDt =dateFormat.parse(request.getParameter("startDate")+" "+request.getParameter("platterStartTime"));
      }
      String startDate = 
			NVL.apply(request.getParameter("startDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
      String endDate = 
			NVL.apply(request.getParameter("endDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
			String reason = NVL.apply(request.getParameter("reason"), "all");
			String message = NVL.apply(request.getParameter("message"), "");
			String restrictedType = NVL.apply(request.getParameter("restrictedType"), "");
            String name=NVL.apply(request.getParameter("name"), "");
            String criterion=NVL.apply(request.getParameter("criterion"), "");
            String dayOfWeek=NVL.apply(request.getParameter("dayOfWeek"), "");
            String path=NVL.apply(request.getParameter("path"), "");
            
            
            final TreeMap timeMap=new TreeMap();
            timeMap.put(new Integer(0),"00:00 AM");    
            timeMap.put(new Integer(1),"01:00 AM");    
            timeMap.put(new Integer(2),"02:00 AM");    
            timeMap.put(new Integer(3),"03:00 AM");    
            timeMap.put(new Integer(4),"04:00 AM");    
            timeMap.put(new Integer(5),"05:00 AM");    
            timeMap.put(new Integer(6),"06:00 AM");    
            timeMap.put(new Integer(7),"07:00 AM");    
            timeMap.put(new Integer(8),"08:00 AM"); 
            timeMap.put(new Integer(9),"09:00 AM");
            timeMap.put(new Integer(10),"10:00 AM");    
            timeMap.put(new Integer(11),"11:00 AM");    
            timeMap.put(new Integer(12),"12:00 PM");    
            timeMap.put(new Integer(13),"01:00 PM");    
            timeMap.put(new Integer(14),"02:00 PM");    
            timeMap.put(new Integer(15),"03:00 PM");    
            timeMap.put(new Integer(16),"04:00 PM");    
            timeMap.put(new Integer(17),"05:00 PM");    
            timeMap.put(new Integer(18),"06:00 PM");    
            timeMap.put(new Integer(19),"07:00 PM");    
            timeMap.put(new Integer(20),"08:00 PM");    
            timeMap.put(new Integer(21),"09:00 PM");    
            timeMap.put(new Integer(22),"10:00 PM");    
            timeMap.put(new Integer(23),"11:00 PM");   
      
    %>
    <div class="home_search_module_content" style="height:91%;">
  
</div>
</tmpl:put>
</tmpl:insert>