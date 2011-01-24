<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.framework.util.*'%>

<%@ taglib uri="http://jakarta.apache.org/struts/tags-template-1.0" prefix='tmpl' %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic-1.0" prefix='logic'%>
<%@ taglib uri="/WEB-INF/shared/tld/freshdirect.tld" prefix='fd' %>
<%@ taglib uri="/WEB-INF/tld/crm.tld" prefix="crm" %>
<style type="text/css">
.case_content_red_field {
color: #CC0000;
font-weight: bold;
font-size: 10pt;
}
</style>
<crm:GetFDUser id="user">
<%
ErpActivityRecord template = (ErpActivityRecord)request.getSession().getAttribute("template");

Date fromDate = null;
String fromDateStr = null;
Date toDate =null;
String toDateStr = null;
String actType = "";
String initiateBy = null;
String transactionSource = "";

if(null == template || template.isEmptyForFilter()){
	template = new ErpActivityRecord();
}
if(null !=request.getParameter("activity_filter_submit")){
	fromDateStr =request.getParameter("activity_cal_start");
	toDateStr =request.getParameter("activity_cal_end");
	actType =request.getParameter("actType");
	initiateBy =request.getParameter("initiatedBy");
	transactionSource =request.getParameter("trnSource");
	
	template = new ErpActivityRecord();
	EnumAccountActivityType enumActType = EnumAccountActivityType.getActivityType(actType);
	template.setActivityType( !enumActType.equals(EnumAccountActivityType.getActivityType(null))?enumActType:null);
	template.setInitiator((null ==initiateBy ||"".equals(initiateBy)?null:initiateBy));
	template.setSource(EnumTransactionSource.getTransactionSource(transactionSource));
	template.setFromDateStr(fromDateStr);
	template.setToDateStr(toDateStr);
}else if(null != template && !template.isEmptyForFilter()){
	fromDate =template.getFromDate();
	toDate =template.getToDate();
	fromDateStr =template.getFromDateStr();
	toDateStr =template.getToDateStr();
	actType =template.getActivityType().getCode();
	initiateBy =template.getInitiator();
	transactionSource =template.getSource().getCode();		
}
%>
<fd:AccountActivity activities='activities' template="<%= template%>">
<%!
private final static Map<String,Comparator<ErpActivityRecord>> ACTIVITY_COMPARATORS = new HashMap<String,Comparator<ErpActivityRecord>>();
static {
	ACTIVITY_COMPARATORS.put("date", ErpActivityRecord.COMP_DATE);
	ACTIVITY_COMPARATORS.put("activity", ErpActivityRecord.COMP_ACTIVITY);
	ACTIVITY_COMPARATORS.put("initiator", ErpActivityRecord.COMP_INITIATOR);
	ACTIVITY_COMPARATORS.put("source", ErpActivityRecord.COMP_SOURCE);
}
%>
<%
JspTableSorter sort = new JspTableSorter(request);

Comparator<ErpActivityRecord> comp = ACTIVITY_COMPARATORS.get(sort.getSortBy());
if(null != activities){
	if (comp == null) {
		Collections.sort(activities, new ReverseComparator<ErpActivityRecord>(ErpActivityRecord.COMP_DATE));
	} else {
		if (comp.equals(ErpActivityRecord.COMP_DATE)) {
			Collections.sort(activities, sort.isAscending() ? new ReverseComparator<ErpActivityRecord>(comp) : comp);
		} else {
			Collections.sort(activities, sort.isAscending() ? comp : new ReverseComparator<ErpActivityRecord>(comp));
		}
	}
}

Map map =(HashMap)pageContext.getAttribute("filterList");
%>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Activity Log</tmpl:put>
	
		<tmpl:put name='content' direct='true'>
		<div class="BG_live">
		<form method='POST' name="frmActivityLog" id="frmActivityLog" action="/main/activity_log.jsp">
			<table class="BG_live" width="100%">
				
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
				
				<tr>
					<td class="promo_page_header_text">View &nbsp;</td>
					<td class="promo_page_header_text">&nbsp; &nbsp;</td>
					<td class="promo_page_header_text">&nbsp; &nbsp;</td>
					<td>Date From
						<input type="text" id="activity_cal_start" value="<%= fromDateStr %>" name="activity_cal_start" class="w100px" value=""/> <a href="" class="promo_ico_cont" id="activity_cal_start_trigger" name="activity_cal_start_trigger"><img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
					</td>
					<td>&nbsp;</td>
					<td>To
						<input type="text" id="activity_cal_end" value="<%= toDateStr %>" name="activity_cal_end" class="w100px" value=""/> <a href="" class="promo_ico_cont" id="activity_cal_end_trigger" name="activity_cal_end_trigger"><img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a>
					</td>
					<td>&nbsp;</td>
					<td>Action
						<select id="actType" name="actType" class="promo_filter">
							<option value="">ALL</option>
							<% List<EnumAccountActivityType> listActType =  (List<EnumAccountActivityType>)map.get("activity_id");
						if(null != listActType){
						for (EnumAccountActivityType name:listActType) { %>
							<option value="<%=name.getCode() %>" <%=name.getCode().equalsIgnoreCase(actType)?"selected":"" %>><%= name.getName() %></option>
						<% } }%>
						</select>
					</td>
					
					<td>&nbsp;</td>
					<td>By
						<select id="initiatedBy" name="initiatedBy" class="promo_filter">
						<option value="" selected="selected">ALL</option>
						<% List<String> listInit =  (List<String>)map.get("initiator");
						if(null != listInit){
						for (String name:listInit) { %>
							<option value="<%=name %>" <%= name.equalsIgnoreCase(initiateBy)?"selected":"" %>><%= name %></option>
						<% } }%>
						</select>
					</td>
					<td>&nbsp;</td>
					<td>Source
						<select id="trnSource" name="trnSource" class="promo_filter">
						<option value="" selected="selected">ALL</option>
						
						<% List<EnumTransactionSource> list =  (List<EnumTransactionSource>)map.get("source");
						if(null != list){
						for (EnumTransactionSource name:list) { %>
							<option value="<%=name.getCode() %>" <%=name.getCode().equalsIgnoreCase(transactionSource)?"selected":"" %>><%= name.getName() %></option>
						<% } }%>
						</select>
						
					</td>
					<td>&nbsp;</td>
					<td><input type="submit" value="FILTER" onclick="" id="activity_filter_submit" name="activity_filter_submit" class="promo_btn_grn" /></td>					
				</tr>
				<% if(null !=pageContext.getAttribute("endDateBeforeErr")) {%>
				<tr >
				<td colspan='2'>&nbsp;</td>
				<td colspan='7' class="case_content_red_field"><%= pageContext.getAttribute("endDateBeforeErr") %></td>
				</tr>
				<%} else { %>
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
				<% } %>
			</table>
			</form>
			</div>
		<div class="list_header">
		<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
			<tr>
				<td width="1%"></td>
				<td width="18%"><a href="?<%= sort.getFieldParams("date") %>" class="list_header_text">Date | Time</a></td>
				<td width="52%"><a href="?<%= sort.getFieldParams("activity") %>" class="list_header_text">Action</a></td>
				<td width="14%"><a href="?<%= sort.getFieldParams("initiator") %>" class="list_header_text">By</a></td>
				<td width="14%"><a href="?<%= sort.getFieldParams("source") %>" class="list_header_text">Source</a></td>
				<td><img src="/media_stat/crm/images/clear.gif" width="8" height="1"></td>
			</tr>
		</table>
		</div>

		<div class="list_content">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
		<% if (null != activities && activities.size() > 0) {%>
			<logic:iterate id="activity" collection="<%= activities %>" type="com.freshdirect.customer.ErpActivityRecord"> 
			<tr valign="top"">
				<td width="1%"></td>
				<td width="18%"><%= CCFormatter.formatDateTime(activity.getDate()) %></td>
				<td width="52%"><b><%= activity.getActivityType().getName() %></b><%= (activity.getNote() != null && !"".equals(activity.getNote())) ? " - " : "" %><%= activity.getNote() %>
					<% if (activity.getChangeOrderId() != null) { %><i>(Order #: <%= activity.getChangeOrderId() %>)</i><% } %>
					<% if (activity.getStandingOrderId() != null) { %><i>(Standing Order #: <%= activity.getStandingOrderId() %>)</i><% } %>
				</td>
				<td width="14%"><%= activity.getMasqueradeAgent() == null ? activity.getInitiator() : activity.getMasqueradeAgent() + " as " + activity.getInitiator() %></td>
				<td width="14%"><%= activity.getSource().getName() %></td>
			</tr>
			<tr class="list_separator" style="padding: 0px;"><td colspan="5"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
			</logic:iterate>
		<% } else { %>
			<tr>
				<td></td>
				<td colspan="5"><br><i>No activity logged.</i></td>
				<td></td>
			</tr>
		<% } %>
		</table>
		</div>

	</tmpl:put>

</tmpl:insert>
</fd:AccountActivity>
</crm:GetFDUser>
<script language="javascript">
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "activity_cal_start",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "activity_cal_start_trigger"
		}
	);
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "activity_cal_end",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "activity_cal_end_trigger"
		}
	);
</script>