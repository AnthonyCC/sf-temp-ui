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

<% boolean isGuest = false;
//com.freshdirect.fdstore.standingorders.service.StandingOrdersServiceCmd.main(new String[]{"orders=2201247313","sendEmail=false"});
%>
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
			
			<crm:CrmStandingOrders id="soList" filter="<%= soFilter %>" result="result">
			
		
			
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
		<div >
				<table border="1">
				<tr >
					<td align="center" width="8%"><b>SO ID</b></td>					
					<td align="left" width="12%"><b>User ID</b></td>						
					<td align="left" width="10%"><b>Company</b></td>
					<td align="left" width="11%"><b>Address</b></td>					
					<td align="left" width="10%"><b>Business/Cell Phone</b></td>						
					<td align="left" width="7%"><b>Frequency</b></td>
					<td align="left" width="10%"><b>Time Slot</b></td>
					<td align="left" width="10%"><b>Next Delivery</b></td>				
					<td align="left" width="20%"><b>Last Error</b></td>
				</tr>				
			</table>
			</div>
				<table width="100%" cellspacing="0" border="1" style="empty-cells: show" id="resubmit_orders">				
				<logic:iterate id="soOrderInfo" collection="<%= soList.getStandingOrdersInfo() %>" type="com.freshdirect.fdstore.standingorders.FDStandingOrderInfo" indexId="idx">
					<tr >
					
						<td  width="8%">
						<%=soOrderInfo.getSoID()%></td>						
						<td  width="12%"><%=soOrderInfo.getUserId()%>&nbsp;&nbsp;</td>						
						<td  width="10%"><%=soOrderInfo.getCompanyName()%></td>
						<td  width="11%"><%=soOrderInfo.getAddress()%></td>					
						<td  align="center" width="10%"><%=soOrderInfo.getBusinessPhone()%><% if(null!=soOrderInfo.getCellPhone()){ %>/<%=soOrderInfo.getCellPhone()%><% } %>&nbsp;</td>
						
						<td  width="7%">
						<% for (EnumStandingOrderFrequency frqItem : EnumStandingOrderFrequency.values()) {
							if(frqItem.getFrequency()==soOrderInfo.getFrequency()){						
						%>	<%= frqItem.getTitle()%></td>
						<% break; }  } %>						
						<td  width="10%"><%= FDTimeslot.getDisplayString(true,soOrderInfo.getStartTime(),soOrderInfo.getEndTime()) %>&nbsp;</td>
						<td  width="10%"><%=DateUtil.formatDate(soOrderInfo.getNextDate())%></td>	
						<td  width="20%"><%=soOrderInfo.getErrorHeader()%></td>
					</tr>
				</logic:iterate>
				</table>
			
	</crm:CrmStandingOrders>
	</form>		
	</crm:GetCurrentAgent>	
	