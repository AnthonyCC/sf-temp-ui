<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='bean' prefix='bean' %>
<tmpl:insert template='/template/large_pop.jsp'>
<tmpl:put name='title' direct='true'>Edit Promo Customer</tmpl:put>
<tmpl:put name='content' direct='true'>
<crm:GetCurrentAgent id='currentAgent'>
<% 
	String customerId = request.getParameter("customer_id");
	String promotionId = request.getParameter("promotion_id");
	boolean isFromCustomer = (customerId != null && !"".equals(customerId));	
	boolean isDeleteable = isFromCustomer;    	
	boolean isAddable = isFromCustomer;    	
	SimpleDateFormat SDF = new SimpleDateFormat("MMM-dd-yyyy");
	String actionName = request.getParameter("action_name");
	if (actionName == null) {
		actionName = "edit_promo_custs";
	} 
%>
<fd:GetPromoCustomerInfoList id="promoCustomerInfoList" promotionId="<%=promotionId%>" customerId="<%=customerId%>">
<fd:PromoCustomerController promoCustomerInfoList="<%=promoCustomerInfoList%>" actionName="<%=actionName%>" result="result" successPage="/main/close_window.jsp">
<script language="javascript">
	function confirmDelete(formName, deleteFlagName) {
		var doDelete = confirm ("Are you sure you want to delete that?");
		if (doDelete == true) {
			var form = document.forms[formName];				
            form.elements[deleteFlagName].value="Y";
            form.elements['action_name'].value="delete_promo_custs";
			form.submit();
			return false;
		}
	}
    
</script>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
    <tr>
		<td width="1%"></td>
        <td width="45%"><%= (isFromCustomer) ? "Promotion" : "User Id"%></td>
        <td width="10%">Usage Count</td>
        <td width="28%">Exp. Date</td>
        <td width="5%">Used</td>
        <td width="10%"></td>
		<td width="1%"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	</tr>
</table>
</div>
<div class="list_content">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<form  method="POST" name="edit_promo">
<%
	int i_edit = 0;
	int i_delete = 0;
    int counter = 0;
	for(Iterator n = promoCustomerInfoList.iterator(); n.hasNext();) {
    	FDPromoCustomerInfo pci = (FDPromoCustomerInfo)n.next();
    	String expirationDayName = "expiration_day_" + i_edit;
    	String expirationMonthName = "expiration_month_" + i_edit;
    	String expirationYearName = "expiration_year_" + i_edit;
    	String usageCountName = "usage_cnt_" + i_edit;
    	String customerIdName = "customer_id_" + i_edit;
    	String promotionIdName = "promotion_id_" + i_edit;
    	String delCustomerIdName = "delete_customer_id_" + i_delete;
    	String delPromotionIdName = "delete_promotion_id_" + i_delete;
    	String deleteFlagName = "delete_flg_" + i_delete;
    	String expirationYearErrorName = expirationYearName + "_edit";
    	String usageCountErrorName = usageCountName + "_edit";    	
    	boolean isEditable = (pci.getIsMaxUsagePerCust() || pci.getRollingExpirationDays() > 0);    	
    	String promoExpirationDate = (pci.getExpirationDate() != null) ? SDF.format(pci.getExpirationDate()) : "";
        counter++;
		i_delete++;
%>
        <tr valign="top" <%= counter % 2 == 0 ? "class='list_odd_row'" : "" %> style="padding-top: 3px; padding-bottom: 3px;">
            <td width="1%" class="list_content_text">&nbsp;</td>
			<td width="45%" class="list_content_text"><%=(isFromCustomer) ? pci.getPromotionDesc() : pci.getUserId()%>&nbsp;</td>
<%		if (pci.getIsMaxUsagePerCust()) { %>
			<td width="10%" class="list_content_text"><input type="text" name="<%=usageCountName%>" value="<%=pci.getUsageCountStr()%>" maxlength = "3" class="text11" size="3">&nbsp;<fd:ErrorHandler result="<%= result %>" name="<%=usageCountErrorName%>" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
<%		} else { %>								
			<td width="10%" class="list_content_text"><%=pci.getUsageCount()%>&nbsp;</td>
<%		} %>				
<%		if (pci.getRollingExpirationDays() > 0) { %>
			<td  width="28%" class="list_content_text"><select name="<%=expirationMonthName%>" class="pulldown">
				<option></option>
				<%for(Iterator im = EnumMonth.getEnumList().iterator(); im.hasNext(); ){
					EnumMonth month = (EnumMonth)im.next(); %>
					<option <%=month.getDescription().equals(pci.getExpirationMonth()) ? "selected" : "" %> value="<%=month.getDescription()%>"><%=month.getDescription()%></option>
				<%}%>
			</select>&nbsp;<select name="<%=expirationDayName%>" class="pulldown">
				<option></option>
				<%for(int day = 1; day <=31; day++){
					String d = String.valueOf(day); %>
					<option <%=d.equals(pci.getExpirationDay()) ? "selected" : "" %> value="<%=d%>"><%=d%></option>
				<%}%>
			</select>&nbsp;<select name="<%=expirationYearName%>" class="pulldown">
				<option></option>
				<%for(int year = 2002; year <=2015; year++){
					String y = String.valueOf(year); %>
					<option <%=y.equals(pci.getExpirationYear()) ? "selected" : "" %> value="<%=y%>"><%=y%></option>
				<%}%>
			</select>&nbsp;<fd:ErrorHandler result="<%= result %>" name="<%=expirationYearErrorName%>" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
<%		} else { %>								
			<td width="28%" class="list_content_text"><%=promoExpirationDate%>&nbsp;</td>
<%		} %>				
			<td width="5%" class="list_content_text"><%=pci.getNumUsed()%>&nbsp;</td>
		<% if (isDeleteable) { %>
			<td width="10%" class="list_content_text"><a href="javascript:confirmDelete('edit_promo','<%= deleteFlagName %>');" class="delete">DELETE</a></td>
			<input type="hidden" name="<%=delPromotionIdName%>" value="<%=pci.getPromotionId()%>">
			<input type="hidden" name="<%=delCustomerIdName%>" value="<%=pci.getCustomerId()%>">
			<input type="hidden" name="<%=deleteFlagName%>" value="">
		<% } else { %>
			<td width="10%" class="list_content_text">&nbsp;</td>
		<% }%>
        	<td width="1%" class="list_content_text">&nbsp;</td>
<% 		if (isEditable) { %>            
			<input type="hidden" name="<%=promotionIdName%>" value="<%=pci.getPromotionId()%>">
			<input type="hidden" name="<%=customerIdName%>" value="<%=pci.getCustomerId()%>">
<%			i_edit++;  			
		} %>
		</tr>		
        <tr class="list_separator" style="padding: 0px;">
            <td colspan="7"></td>
        </tr>
<%
	} 
%>
	<input type="hidden" name="action_name" value="edit_promo_custs">
	<tr>
	<td colspan="7"><input type="reset" value="CLEAR" class="clear"><input type="submit" value="EDIT PROMOTION" class="submit" ></td>
	</tr>
</form>	
</table>
<% 
if (isAddable) {
// add promotion list box
	List availPromoList = FDPromotionManager.getAvailablePromosForCustomer(customerId);
	if (availPromoList != null && availPromoList.size() > 0) { 
		String expirationDayName = "expiration_day_0";
		String expirationMonthName = "expiration_month_0";
		String expirationYearName = "expiration_year_0";
		String usageCountName = "usage_cnt_0";
	    String customerIdName = "customer_id_0";
	    String promotionIdName = "promotion_id_0";	
	    String promoExpDateName = "promo_expiration_date_0";	
	    String promoUsageCountName = "promo_usage_cnt_0";	
		String expirationYearErrorName = expirationMonthName + "_add";
		String usageCountErrorName = usageCountName + "_add";
%>
<br></br>
<br></br>
<script language="JavaScript">
<!--
	function PromotionInfo(id, promoUsageCount, promoExpirationDate, isMaxUsagePerCustomer, rollingExpirationDays) {
		this.id = id;
		this.promoUsageCount = promoUsageCount;
		this.promoExpirationDate = promoExpirationDate;
		this.isMaxUsagePerCustomer = isMaxUsagePerCustomer;
		this.rollingExpirationDays = rollingExpirationDays;
	}

	function changePromotionInfo(formName, promoIdName, promoUsageCountName, promoExpirationDateName, usageCountName, expirationDayName, expirationMonthName, expirationYearName) {
		var form = document.forms[formName];				
		var promoId = form.elements[promoIdName][form.elements[promoIdName].selectedIndex].value;
		form.elements[promoUsageCountName].value = promotionInfos[promoId].promoUsageCount;
		form.elements[promoExpirationDateName].value = promotionInfos[promoId].promoExpirationDate;
		if (!promotionInfos[promoId].isMaxUsagePerCustomer) {
			form.elements[usageCountName].value = "";
			form.elements[usageCountName].disabled = true;
		} else {
			form.elements[usageCountName].disabled = false;
		}
		if (!promotionInfos[promoId].rollingExpirationDays > 0) {
			form.elements[expirationDayName].value = "";
			form.elements[expirationMonthName].value = "";
			form.elements[expirationYearName].value = "";
			form.elements[expirationDayName].disabled = true;
			form.elements[expirationMonthName].disabled = true;
			form.elements[expirationYearName].disabled = true;
		} else {
			form.elements[expirationDayName].disabled = false;
			form.elements[expirationMonthName].disabled = false;
			form.elements[expirationYearName].disabled = false;
		}
	}

	var promotionInfos = new Array();
	promotionInfos[""] = new PromotionInfo("", 0, "", false, 0);
	<%
	for(Iterator api = availPromoList.iterator(); api.hasNext();) {
		FDPromotionModel promo = (FDPromotionModel)api.next();
		String expirationDate = promo.getExpirationDate() != null ? SDF.format(promo.getExpirationDate()) : "";
		int rollingExpirationDays = (promo.getRollingExpirationDays() != null) ? promo.getRollingExpirationDays().intValue() : 0;
	%>
		promotionInfos['<%= promo.getId() %>'] = new PromotionInfo('<%= promo.getId() %>', <%= promo.getMaxUsage() %>, '<%= expirationDate %>', <%= promo.isMaxUsagePerCustomer()%>, <%= rollingExpirationDays %>);
	<%
	}
	%>		
//-->
</script>
<table width="100%" cellpadding="0" cellspacing="0" border="0">
<form  method="POST" name="add_promo">
    <tr class="list_separator" style="padding: 0px;">
        <td colspan="7"></td>
    </tr>
    <tr valign="top" style="padding-top: 3px; padding-bottom: 3px;">
    <td width="1%" class="list_content_text">&nbsp;</td>
	<td width="45%" class="list_content_text"><select name="<%=promotionIdName%>" class="pulldown" onchange="javascript:changePromotionInfo('add_promo', '<%=promotionIdName%>', '<%=promoUsageCountName%>', '<%=promoExpDateName%>', '<%=usageCountName%>', '<%=expirationDayName%>', '<%=expirationMonthName%>', '<%=expirationYearName%>')">
			<option></option>
<%
		for(Iterator api = availPromoList.iterator(); api.hasNext();) {
			FDPromotionModel promo = (FDPromotionModel)api.next();
%>
			<option value="<%=promo.getId()%>"><%=promo.getDescription()%></option>
<%		} %>
		</select>&nbsp;</td>    	
		<td width="10%" class="list_content_text"><input type="text" name="<%=usageCountName%>" value="" maxlength = "3" class="text11" size="3" disabled>&nbsp;<fd:ErrorHandler result="<%= result %>" name="<%=usageCountErrorName%>" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
		<td  width="28%" class="list_content_text"><select name="<%=expirationMonthName%>" class="pulldown" disabled>
			<option></option>
			<%for(Iterator im = EnumMonth.getEnumList().iterator(); im.hasNext(); ){
				EnumMonth month = (EnumMonth)im.next(); %>
				<option value="<%=month.getDescription()%>"><%=month.getDescription()%></option>
			<%}%>
		</select>&nbsp;<select name="<%=expirationDayName%>" class="pulldown" disabled>
			<option></option>
			<%for(int day = 1; day <=31; day++){
				String d = String.valueOf(day); %>
				<option value="<%=d%>"><%=d%></option>
			<%}%>
		</select>&nbsp;<select name="<%=expirationYearName%>" class="pulldown" disabled>
			<option></option>
			<%for(int year = 2002; year <=2015; year++){
				String y = String.valueOf(year); %>
				<option value="<%=y%>"><%=y%></option>
			<%}%>
		</select>&nbsp;<fd:ErrorHandler result="<%= result %>" name="<%=expirationYearErrorName%>" id='errorMsg'><span class="error"><%=errorMsg%></span></fd:ErrorHandler></td>
		<td width="5%" class="list_content_text">&nbsp;</td>
		<td width="10%" class="list_content_text">&nbsp;</td>
        <td width="1%" class="list_content_text">&nbsp;</td>
		<input type="hidden" name="action_name" value="add_promo_custs">
		<input type="hidden" name="<%=promoUsageCountName%>" value="">
		<input type="hidden" name="<%=promoExpDateName%>" value="">
		<input type="hidden" name="<%=customerIdName%>" value=<%=customerId%>>
	</tr>
	<tr>
	<td colspan="7"><input type="reset" value="CLEAR" class="clear"><input type="submit" value="ADD PROMOTION" class="submit" ></td>
	</tr>
</form>	
</table>
<% 
	}
}
%>
</div>
</fd:PromoCustomerController>
</fd:GetPromoCustomerInfoList>
</crm:GetCurrentAgent>
</tmpl:put>
</tmpl:insert>