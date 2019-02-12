<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.delivery.model.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>

<%@page import="com.freshdirect.fdstore.FDDeliveryManager"%>
<%@page import="com.freshdirect.framework.util.GenericSearchCriteria"%>
<%@page import="com.freshdirect.common.pricing.MunicipalityInfoWrapper"%>
<%@page import="com.freshdirect.common.pricing.MunicipalityInfo"%>
<%@page import="com.freshdirect.webapp.util.CCFormatter"%>


<%@page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionType"%>
<%@page import="com.freshdirect.framework.util.NVL"%>
<%@page import="com.freshdirect.delivery.restriction.EnumDlvRestrictionReason"%>
	<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Add Alcohol Restriction</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
	<jsp:include page="/includes/admintools_nav.jsp" />
<% 
	Calendar cal1 = Calendar.getInstance();
	cal1.set(Calendar.YEAR, 1970);
	cal1.set(Calendar.MONTH, 01);
	cal1.set(Calendar.DATE, 01);
	Calendar cal2 = Calendar.getInstance();
	cal2.set(Calendar.YEAR, 2099);
	cal2.set(Calendar.MONTH, 01);
	cal2.set(Calendar.DATE, 01);

%>
<script type="text/javascript">
		var dowShort = new Array();
		dowShort[0] = 'SUN';
		dowShort[1] = 'MON';
		dowShort[2] = 'TUE';
		dowShort[3] = 'WED';
		dowShort[4] = 'THU';
		dowShort[5] = 'FRI';
		dowShort[6] = 'SAT';
		function toggleDate(resType){
			if(document.getElementById('newStartDate') == null) return;
			if(resType == 'OTR') {
				document.getElementById('newStartDate').value = '';
				document.getElementById('newEndDate').value = '';
				document.getElementById('startDate').value = '';
				document.getElementById('endDate').value = '';
				document.getElementById('imgStartDate').style.display = "";
				document.getElementById('imgEndDate').style.display = "";
			} else {
				document.getElementById('startDate').value = '<%= CCFormatter.formatDateYear(cal1.getTime())%>';
				document.getElementById('endDate').value = '<%= CCFormatter.formatDateYear(cal2.getTime())%>';
				document.getElementById('newStartDate').value = '<%= CCFormatter.formatDateYear(cal1.getTime())%>';
				document.getElementById('newEndDate').value = '<%= CCFormatter.formatDateYear(cal2.getTime())%>';
				document.getElementById('imgStartDate').style.display = "none";
				document.getElementById('imgEndDate').style.display = "none";
			 }	
		 }	
		 function disableCheckBoxes(id) {
			 cbToggle(id);
			 if(id != 'edit_dlvreq_chkANY') return;
			 var cbAny = document.getElementById(id);
			 if(cbAny.checked) {
				 for(var i=0;i<dowShort.length;i++) {
					 var cbName = 'edit_dlvreq_chk'+dowShort[i];
					 var cb = document.getElementById(cbName);
					 cb.disabled = true;
				 }
			 } else {
				 for(var i=0;i<dowShort.length;i++) {
					 var cbName = 'edit_dlvreq_chk'+dowShort[i];
					 var cb = document.getElementById(cbName);
					 cb.disabled = false;
				 }			 
		 	}
		 }
</script>
	
	<div class="promo_page_header" style="height:91%;">
	
	</div>
	</tmpl:put>
</tmpl:insert>