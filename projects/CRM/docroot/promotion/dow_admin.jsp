<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.callcenter.GenericLocatorTag"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionNewManager"%>
<%@ page import="com.freshdirect.webapp.crm.security.CrmSecurityManager" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.crm.CrmAgentModel" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>

<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Manage Day of Week Limits</tmpl:put>
	
<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
	<crm:WSPromoController result="result">		
	<%@ include file="/includes/promotions/i_promo_trn_nav.jspf" %>

	<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
	<script language="JavaScript" type="text/javascript">
	function numbersonly(e, decimal) {
		var key;
		var keychar;

		if (window.event) {
   			key = window.event.keyCode;
		}
		else if (e) {
   			key = e.which;
		}
		else {
   			return true;
		}
		keychar = String.fromCharCode(key);

		if ((key==null) || (key==0) || (key==8) ||  (key==9) || (key==13) || (key==27) ) {
   			return true;
		}
		else if ((("0123456789").indexOf(keychar) > -1)) {
   			return true;
		}
		else if (decimal && (keychar == ".")) { 
  			return true;
		}
		else
   			return false;
	}
	
	function doEdit(dow) {
		var tf = eval('document.frmDowAdmin.maxlimit'+dow);
		tf.disabled = false;
		tf.value = eval('document.frmDowAdmin.limit'+dow+'.value');
		tf.focus();
		var button = eval('document.frmDowAdmin.save'+dow);
		button.disabled = false;
	}
	function doCancel(dow) {
		var tf = eval('document.frmDowAdmin.maxlimit'+dow);
		tf.value = eval('document.frmDowAdmin.orglimit'+dow+'.value');
		tf.disabled = true;
		var button = eval('document.frmDowAdmin.save'+dow);
		button.disabled = true;
	}

	function doSave(dow) {
		document.frmDowAdmin.dayofweek.value = dow;
		var limit = eval('document.frmDowAdmin.maxlimit'+dow+'.value');
		var dowtext = eval('document.frmDowAdmin.dayofweektxt'+dow+'.value');
		document.frmDowAdmin.limit.value = limit;
		var doConfirm = confirm ("Are you sure you want to change the Max Spending Limit for "+dowtext+"?");
		if(doConfirm == false){
			return;
		}            
		document.frmDowAdmin.actionName.value = "setDOWLimit";
		document.frmDowAdmin.submit();
	}	
</script>
<%! 
	final java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	final java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("0.00"); 
%>
<%
	Map<Integer,Double> dowLimits = FDPromotionNewManager.getDOWLimits();
	Map<Integer,Double> dowSpent = FDPromotionNewManager.getActualAmountSpentByDays();
%>
<form method='POST' name="frmDowAdmin" id="frmDowAdmin">
<input type="hidden" name="dayofweek" value=""/>
<input type="hidden" name="limit" value=""/>
<input type="hidden" name="actionName" id="actionName" value="">
<table width="60%" border="0" cellspacing="5" cellpadding="5">
	<tr>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Day of Week</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Max Spending Amount</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold"><span class="detail_text"><b>Actual Amount Spent</b></span></td>	
	<td class="border_bold">&nbsp;</td>
	<% if(FDStoreProperties.isPromoPublishNodeMaster() && CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"dow_admin.jsp")) { %>
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold">&nbsp;</td>
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold">&nbsp;</td>
	<td class="border_bold">&nbsp;</td>
	<td valign="bottom" class="border_bold">&nbsp;</td>
	<% } %>
	</tr>
	
	<%
		String[] weekdays = new DateFormatSymbols().getWeekdays();
		for(int dow = 1; dow <= 7 ; dow++) {
			String maxLimit = "";
			String limitstr = "";
			String spentstr = "";
			Double limit = dowLimits.get(new Integer(dow));
			Double spent = dowSpent.get(new Integer(dow));
			if(limit != null){
				maxLimit = currencyFormatter.format(limit.doubleValue());
				limitstr = decimalFormat.format(limit.doubleValue());
				spentstr = decimalFormat.format(spent.doubleValue());
			}
	%>
	<tr>
	<td class="border_bottom"><span class="detail_text">
		<input type="hidden" name="dayofweektxt<%= dow %>" value="<%= weekdays[dow] %>"/>
		<%= weekdays[dow] %>
	</span></td>
	<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text">
		<input type="hidden" name="limit<%= dow %>" value="<%= limitstr %>"/>
		<input type="hidden" name="orglimit<%= dow %>" value="<%= maxLimit %>"/>
		<input type="text" size="10" id="maxlimit<%= dow %>" name="maxlimit<%= dow %>" value="<%= maxLimit %>" disabled="true" onKeyPress="return numbersonly(this, event)"/>
	</span></td>
		<td class="border_bottom">&nbsp;</td>
	<td class="border_bottom"><span class="detail_text">$<%= spentstr %></span></td>
	<td class="border_bottom">&nbsp;</td>
	<% if(FDStoreProperties.isPromoPublishNodeMaster() && CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"dow_admin.jsp")) { %>
		<td class="border_bottom">&nbsp;</td>
		<td class="border_bottom"><span class="detail_text"><b><a onClick="javascript:doEdit('<%= dow %>');" href="#" style="color: #008800;font-size: 8pt;">EDIT</a></b></span></td>
		<td class="border_bottom">&nbsp;</td>
		<td class="border_bottom">
			<input name="save<%= dow %>" type="button" value="   SAVE   " class="submit" onClick="javascript:doSave('<%= dow %>');" disabled />
		</td>
		<td class="border_bottom">&nbsp;</td>		
		<td class="border_bottom">
			<input name="cancel" type="button" value=" CANCEL " class="submit" onClick="javascript:doCancel('<%= dow %>');" />
		</td>
	<% } %>
	</tr>
	<%
		}
	%>		
	
</table>
</form>
</crm:WSPromoController>
</crm:GetCurrentAgent>
</tmpl:put>
</tmpl:insert>