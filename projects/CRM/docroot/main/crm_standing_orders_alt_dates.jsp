<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import='com.freshdirect.fdstore.standingorders.FDStandingOrdersManager'%>
<%@page import='com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<style type="text/css">
.crm_standing_orders_alt_dates_col {
float:left;
width:150px;
text-align: center;
}

.crm_standing_orders_alt_dates_col_wide {
float:left;
width:300px;
text-align: center;
}

.crm_standing_orders_alt_dates_row {
float:left;
padding: 4px 0px;
}

.crm_standing_orders_alt_dates_table {
width:900px;
margin-left:auto;
margin-right:auto;
margin-top: 20px;
}
</style>

<script type="text/javascript"	>
function submitAltDeliveryDate(id, operation){
	var origDate = document.getElementById("origDate-"+id).value; 
	var altDate = document.getElementById("altDate-"+id).value;
	var description = document.getElementById("description-"+id).value;
	
	var confirmText = operation.charAt(0).toUpperCase() + operation.slice(1) + " '" + description + "' (original: " + origDate +", alternative: " + altDate + ")?"; 
	
	if (confirm(confirmText)){
		document.getElementById("operation-"+id).value = "alt_delivery_date_" + operation;
		document.getElementById("form-"+id).submit();
	}
}
</script>    
    
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Standing Order Alternative Delivery Dates</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<crm:GetCurrentAgent id="currentAgent">
			<jsp:include page="/includes/crm_standing_orders_nav.jsp" />
			<crm:CrmStandingOrderAltDates id="altDeliveryDates" filter="filter" intervals="intervals" result="result">
				<div>
					<form method='POST' action="/main/crm_standing_orders_alt_dates.jsp">
						<div class="BG_live" width="100%" style="border-bottom:2px solid #000000;border-top:1px solid #000000; padding:3px">
							<div class="promo_page_header_text">Manage Alternative Delivery Dates</div>
							<div style="padding-left:20px;padding-top:5px">
								Year:
								<select name="filter" class="promo_filter">
								<logic:iterate id="interval" collection="<%=intervals%>" type="java.lang.String">
									<option value="<%=interval%>" 
										<%if(filter.equals(interval)) {%> 
											selected="selected"
										<%}%>
										><%=interval%></option>
								</logic:iterate>
								</select>
								<input type="hidden" name="operation" value="filter_change" />
								<input type="submit" value="FILTER" class="promo_btn_grn" />
							</div>
						</div>
					</form>
				</div>
				<div class="errContainer">
					<fd:ErrorHandler result="<%=result%>" name="so_error" id="errorMsg">
						<%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>					
				</div>
				<% if(null !=pageContext.getAttribute("successMsg")) {%>
				<div class="case_content_field"><%= pageContext.getAttribute("successMsg") %></div>
				<% } %>
				
				<div style="text-align: center">
					<div class="crm_standing_orders_alt_dates_table">
						<div class="list_header_text crm_standing_orders_alt_dates_col" style="background-color:#333366;">Original Date</div>				
						<div class="list_header_text crm_standing_orders_alt_dates_col" style="background-color:#333366;">Alternative Date</div>
						<div class="list_header_text crm_standing_orders_alt_dates_col_wide" style="background-color:#333366;">Description</div>
						<div class="list_header_text crm_standing_orders_alt_dates_col" style="background-color:#333366;">Actions</div>
		
						<div class="crm_standing_orders_alt_dates_row list_odd_row">
							<form id="form-create" method="POST" action="/main/crm_standing_orders_alt_dates.jsp">
								<input type="hidden" name="operation" id="operation-create"/>
								<div class="crm_standing_orders_alt_dates_col">
									<input name="origDate" id="origDate-create" value="" size="10" maxlength="10">
									<a href="#" onclick="return false;" class="promo_ico_cont" id="origDate-create_trigger">
									<img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
								</div>
								<script language="javascript">
									Calendar.setup(
											{
												showsTime : false,
												electric : false,
												inputField : "origDate-create",
												ifFormat : "%m/%d/%Y",
												singleClick: true,
												button : "origDate-create_trigger"
											}
										);
								</script>
								
								<div class="crm_standing_orders_alt_dates_col">
									<input name="altDate" id="altDate-create" value="" size="10" maxlength="10">
									<a href="#" onclick="return false;" class="promo_ico_cont" id="altDate-create_trigger">
									<img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
								</div>
								<script language="javascript">
									Calendar.setup(
											{
												showsTime : false,
												electric : false,
												inputField : "altDate-create",
												ifFormat : "%m/%d/%Y",
												singleClick: true,
												button : "altDate-create_trigger"
											}
										);
								</script>
	
								<div class="crm_standing_orders_alt_dates_col_wide">
									<input name="description" id="description-create" value="" size="35" maxlength="35">
								</div>
	
								<div class="crm_standing_orders_alt_dates_col">
									<a href="#" onclick='submitAltDeliveryDate("create", "create")'>Create</a>
								</div>
							</form>
						</div>
		
						<logic:iterate id="altDeliveryDate" collection="<%= altDeliveryDates %>" type="FDStandingOrderAltDeliveryDate" indexId="idx">
							<%String rowClass = idx.intValue() % 2 == 1 ? "list_odd_row" : "";%>
							<div class="crm_standing_orders_alt_dates_row <%=rowClass%>">
								<form id="form-<%=idx%>" method="POST" action="/main/crm_standing_orders_alt_dates.jsp">
									<input type="hidden" id="operation-<%=idx%>" name="operation" value="" />
									<div class="crm_standing_orders_alt_dates_col">
										<%=altDeliveryDate.getOrigDateFormatted()%>
										<input name="origDate" id="origDate-<%=idx%>" value="<%=altDeliveryDate.getOrigDateFormatted()%>" type="hidden">
									</div>
	
									<div class="crm_standing_orders_alt_dates_col">
										<input name="altDate" id="altDate-<%=idx%>" value="<%=altDeliveryDate.getAltDateFormatted()%>" size="10" maxlength="10">
										<a href="#" onclick="return false;" class="promo_ico_cont" id="altDate-<%=idx%>_trigger">
											<img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" />
										</a>
									</div>
									<script language="javascript">
									Calendar.setup(
											{
												showsTime : false,
												electric : false,
												inputField : "altDate-<%=idx%>",
												ifFormat : "%m/%d/%Y",
												singleClick: true,
												button : "altDate-<%=idx%>_trigger"
											}
										);
									</script>
	
									<div class="crm_standing_orders_alt_dates_col_wide">
										<input name="description" id="description-<%=idx%>" value="<%=altDeliveryDate.getDescription()%>" size="35" maxlength="35">
									</div>
									
									<div class="crm_standing_orders_alt_dates_col">
										<a href='#' onclick='submitAltDeliveryDate("<%=idx%>", "save")'>Save</a>
										<a href='#' onclick='submitAltDeliveryDate("<%=idx%>", "delete")'>Delete</a>
									</div>
								</form>
							</div>
						</logic:iterate>
					</div>
				</div>
			</crm:CrmStandingOrderAltDates>
		</crm:GetCurrentAgent>	
	</tmpl:put>
</tmpl:insert>