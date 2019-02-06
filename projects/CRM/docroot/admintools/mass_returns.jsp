<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Admin Tools > Mass Returns</tmpl:put>

<tmpl:put name='content' direct='true'>

<script>
	function openURL(inLocationURL) {

	popResize(inLocationURL, 400,400,'');

	}
	
	function doSearch() {
	   	document.getElementById("searchFlag").value = "true";
	}
</script>
<%@ include file="/includes/i_globalcontext.jspf" %>
<jsp:include page="/includes/admintools_nav.jsp" />
<% 
	String dlvDate = 
	NVL.apply(request.getParameter("deliveryDate"), CCFormatter.formatDateYear(Calendar.getInstance().getTime()));
	String zone = NVL.apply(request.getParameter("zone"), "");
	String fromTimeSlot = NVL.apply(request.getParameter("fromTimeSlot"), "");
	String fromTimePeriod = NVL.apply(request.getParameter("fromTimePeriod"), "AM");
	String toTimeSlot = NVL.apply(request.getParameter("toTimeSlot"), "");
	String toTimePeriod = NVL.apply(request.getParameter("toTimePeriod"), "AM");
	String filterType = NVL.apply(request.getParameter("filterType"), "");
/* 	String fromWaveNumber = NVL.apply(request.getParameter("fromWaveNumber"), ""); 
	String toWaveNumber = NVL.apply(request.getParameter("toWaveNumber"), "");*/
	String fromTruckNumber = NVL.apply(request.getParameter("fromTruckNumber"), "");
	String toTruckNumber = NVL.apply(request.getParameter("toTruckNumber"), "");
	String notes = NVL.apply(request.getParameter("notes"), "");
	int prcLimit = FDStoreProperties.getOrderProcessingLimit();		
%>
<div class="home_search_module_content" style="height:100%;">

</div>
</tmpl:put>
</tmpl:insert>
