<%@ page import="java.util.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria" %>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrder.ErrorCode" %>
<%@ page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency" %>
<%@ page import="com.freshdirect.fdstore.standingorders.FDStandingOrderInfo" %>
<%@ page import="com.freshdirect.enums.WeekDay" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import="com.freshdirect.fdstore.FDTimeslot" %>
<%@ page import='com.freshdirect.framework.util.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<% boolean isGuest = false; %>
<script type="text/javascript"	>
function checkSelection(){
	var total=0;
	if(document.frmStndOrders.clearErrorSOId.checked){
		total =1;
	}else{
		for(var i=0; i < document.frmStndOrders.clearErrorSOId.length; i++){
			if(document.frmStndOrders.clearErrorSOId[i].checked){
				total =total +1;
			}
		}
	}
	if(total==0){
		alert("Please select atlease one standing order.");
		return false;
	}
	if(confirm(total+" standing orders selected. Do you want to continue?")){				
		return true;
	}
	return false;
}
</script>
    
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Standing Orders</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id="currentAgent">
	<%	FDStandingOrderFilterCriteria  soFilter =  (FDStandingOrderFilterCriteria)request.getSession().getAttribute("sofilter"); 
		Integer frequency = null;
		Integer dayOfWeek =null;
		String errorType = "";
		if(null == soFilter || soFilter.isEmpty()){
			soFilter = new FDStandingOrderFilterCriteria();
			soFilter.setActiveOnly(true);
			/*soFilter.setFrequency(frequency);
			soFilter.setDayOfWeek(dayOfWeek);
			soFilter.setErrorType(errorType);*/
		}
		if(null !=request.getParameter("so_filter_submit")){
			String strFrequency =request.getParameter("frequency");
			if("ALL".equals(strFrequency)){
				frequency = null;
			}else{
				frequency = Integer.parseInt(strFrequency);
			}
			String strDayOfWeek =request.getParameter("dayOfWeek");
			if("ALL".equals(strDayOfWeek)){
				dayOfWeek = null;
			}else{
				dayOfWeek = Integer.parseInt(strDayOfWeek);
			}			
			
			errorType =request.getParameter("errorType");
			soFilter = new FDStandingOrderFilterCriteria();
			String strActiveOnly=request.getParameter("activeOnly");
			if("activeOnly".equalsIgnoreCase(strActiveOnly)){
				soFilter.setActiveOnly(true);
			}else{
				soFilter.setActiveOnly(false);
			}
			soFilter.setFrequency(frequency);
			soFilter.setDayOfWeek(dayOfWeek);
			soFilter.setErrorType(errorType);
		}else if(null != soFilter && !soFilter.isEmpty()){
			frequency = soFilter.getFrequency();
			dayOfWeek = soFilter.getDayOfWeek();
			errorType = soFilter.getErrorType();
		}	
	%>
		<form method='POST' name="frmStndOrders" id="frmStndOrders" action="/main/crm_standing_orders.jsp">
		<div class="BG_live">
		
			<table class="BG_live" width="100%" style="border-bottom:2px solid #000000;border-top:1px solid #000000;">
				
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
				
				<tr>
					<td class="promo_page_header_text" width="20%">View Standing Orders</td>
					<td class="promo_page_header_text">&nbsp; &nbsp;</td>					
					<td>Frequency
						<select id="frequency" name="frequency" class="promo_filter">
						<option value="ALL" selected="selected">ALL</option>
						<% for (EnumStandingOrderFrequency frqItem : EnumStandingOrderFrequency.values()) { %>
									<option value="<%= frqItem.getFrequency() %>" <%= (null!=soFilter.getFrequency() && frqItem.getFrequency()== soFilter.getFrequency())?"selected":""%>><%= frqItem.getTitle() %></option>
								<% } %>							
						</select>
					</td>					
					<td>&nbsp;</td>
					<td>Error Type
						<select id="errorType" name="errorType" class="promo_filter">
							<option value="">ALL</option>
							<% 
						
						for (ErrorCode name:ErrorCode.values()) {
							if(null != name){
							%>
							<option value="<%=name %>" <%= (null!=soFilter.getErrorType() && name.toString().equals(soFilter.getErrorType()))?"selected":""%>><%= name %></option>
						<% } } %>
						</select>
					</td>
					
					<td>&nbsp;</td>
					<td>Day of Week
						<select id="dayOfWeek" name="dayOfWeek" class="promo_filter">
						<option value="ALL" selected="selected">ALL</option>	
						<% int j=1;
							for (WeekDay name:WeekDay.values()) {
							if(null != name){
							%>
							<option value="<%=j %>" <%= (null!=soFilter.getDayOfWeek() && j== soFilter.getDayOfWeek())?"selected":"" %>><%= name %></option>
						<% } j++; } %>					
						</select>
					</td>
					<td>&nbsp;</td>
					<td><input type="checkbox" id="activeOnly" name="activeOnly" value="activeOnly" <%= soFilter.isActiveOnly()?"checked":"" %>/>Active Only</td>
					<td>&nbsp;</td>
					<td><input type="submit" value="FILTER" onclick="" id="so_filter_submit" name="so_filter_submit" class="promo_btn_grn" /></td>
					<td>&nbsp;</td>
					<td width='10%'>&nbsp;</td>
					<td>&nbsp;</td>						
				</tr>				
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
			</table>
			
			</div>
			<crm:CrmStandingOrders id="soList" filter="<%= soFilter %>" result="result">
			<div class="errContainer">
					<fd:ErrorHandler result="<%=result%>" name="so_error" id="errorMsg">
						<%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>					
			</div>
			<table width="100%" cellspacing="0" border="0" style="empty-cells: show">
			<% if(null !=pageContext.getAttribute("successMsg")) {%>
				<tr >
				
				<td colspan='10' class="case_content_field"><%= pageContext.getAttribute("successMsg") %></td>
				
				</tr>
				<%} else { %>
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
			<% } %>
			<% if(null!=soList.getStandingOrdersInfo() && !soList.getStandingOrdersInfo().isEmpty()){ %>
				<tr> <td colspan="9"><b><%= soList.getStandingOrdersInfo().size()%> record(s) found.</b>&nbsp;&nbsp;<input type="submit" name="clear_errors" class="submit" id="clear_errors" value="Clear SO Errors" onclick="javascript:return checkSelection();"/></td>				
									
				</tr>
			<% } else { %>
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
			<% } %>
			<%!
			private final static Map<String,Comparator<FDStandingOrderInfo>> SO_COMPARATORS = new HashMap<String,Comparator<FDStandingOrderInfo>>();
			static {
				SO_COMPARATORS.put("soId", FDStandingOrderInfo.COMP_ID);
				SO_COMPARATORS.put("userId", FDStandingOrderInfo.COMP_USER_ID);
				SO_COMPARATORS.put("company", FDStandingOrderInfo.COMP_COMPANY);
				SO_COMPARATORS.put("freq", FDStandingOrderInfo.COMP_FREQUENCY);
				SO_COMPARATORS.put("timeslot", FDStandingOrderInfo.COMP_TIMESLOT);
				SO_COMPARATORS.put("nextDel", FDStandingOrderInfo.COMP_NEXT_DATE);
				SO_COMPARATORS.put("lastErr", FDStandingOrderInfo.COMP_LAST_ERROR);
			}
%>
			<%    
				JspTableSorter sort = new JspTableSorter(request);
			Comparator<FDStandingOrderInfo> comp = SO_COMPARATORS.get(sort.getSortBy());
			List<FDStandingOrderInfo> soInfoList = soList.getStandingOrdersInfo();
			if(null != soInfoList){
				if (comp == null) {
					Collections.sort(soInfoList, new ReverseComparator<FDStandingOrderInfo>(FDStandingOrderInfo.COMP_ID));
				} else {
					Collections.sort(soInfoList, sort.isAscending() ? comp : new ReverseComparator<FDStandingOrderInfo>(comp));					
				}
			}
			%>
				</table><div class="list_header">
				<table>
				<tr bgcolor="#333366" class="list_header_text">
					<td align="left" width="1%">&nbsp;</td>
					<td align="center" width="8%"><a href="?<%= sort.getFieldParams("soId") %>" class="list_header_text">SO ID</a></td>					
					<td align="left" width="12%"><a href="?<%= sort.getFieldParams("userId") %>" class="list_header_text">User ID</a></td>						
					<td align="left" width="10%"><a href="?<%= sort.getFieldParams("company") %>" class="list_header_text">Company</a></td>
					<td align="left" width="11%">Address</td>					
					<td align="left" width="10%">Business/Cell Phone</td>						
					<td align="left" width="7%"><a href="?<%= sort.getFieldParams("freq") %>" class="list_header_text">Frequency</a></td>
					<td align="left" width="10%"><a href="?<%= sort.getFieldParams("timeslot") %>" class="list_header_text">Time Slot</a></td>
					<td align="left" width="10%"><a href="?<%= sort.getFieldParams("nextDel") %>" class="list_header_text">Next Delivery</a></td>				
					<td align="left" width="20%"><a href="?<%= sort.getFieldParams("lastErr") %>" class="list_header_text">Last Error</a></td>
				</tr>				
			</table>
			</div>
			<div id="result" class="list_content" style="height:76%;">
				<table width="100%" cellspacing="0" border="0" style="empty-cells: show" id="resubmit_orders">
				<% if(soList.getStandingOrdersInfo().isEmpty()){ %>
					<tr valign="top" style="color:#CC0000; font-weight: bold;"><td colspan="11" align="center">No matching standing orders found.</td></tr>
				<% } %>
				<logic:iterate id="soOrderInfo" collection="<%= soList.getStandingOrdersInfo() %>" type="com.freshdirect.fdstore.standingorders.FDStandingOrderInfo" indexId="idx">
					<tr <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
						<td class="border_bottom" width="1%">&nbsp;</td>
						<td class="border_bottom" width="8%">
						<input name="clearErrorSOId" type="checkbox" id="" value="<%=soOrderInfo.getSoID()+","+soOrderInfo.getCustomerId()%>" <%= (null==soOrderInfo.getErrorHeader())?"disabled":"" %>>
						<%=soOrderInfo.getSoID()%></td>						
						<td class="border_bottom" width="12%"><a href="/main/summary.jsp?erpCustId=<%=soOrderInfo.getCustomerId()%>"><%=soOrderInfo.getUserId()%></a>&nbsp;&nbsp;</td>						
						<td class="border_bottom" width="10%"><%=soOrderInfo.getCompanyName()%></td>
						<td class="border_bottom" width="11%"><%=soOrderInfo.getAddress()%></td>					
						<td class="border_bottom" align="center" width="10%"><%=soOrderInfo.getBusinessPhone()%><% if(null!=soOrderInfo.getCellPhone()){ %>/<%=soOrderInfo.getCellPhone()%><% } %>&nbsp;</td>
						
						<td class="border_bottom" width="7%">
						<% for (EnumStandingOrderFrequency frqItem : EnumStandingOrderFrequency.values()) {
							if(frqItem.getFrequency()==soOrderInfo.getFrequency()){						
						%>	<%= frqItem.getTitle()%></td>
						<% break; }  } %>						
						<td class="border_bottom" width="10%"><%= FDTimeslot.getDisplayString(true,soOrderInfo.getStartTime(),soOrderInfo.getEndTime()) %>&nbsp;</td>
						<td class="border_bottom" width="10%"><%=DateUtil.formatDate(soOrderInfo.getNextDate())%></td>	
						<td class="border_bottom" width="20%"><%=soOrderInfo.getErrorHeader()%></td>
					</tr>
				</logic:iterate>
				</table>
			</div>
	</crm:CrmStandingOrders>
	</form>		
	</crm:GetCurrentAgent>	
	</tmpl:put>
</tmpl:insert>