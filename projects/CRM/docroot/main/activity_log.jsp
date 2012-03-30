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
<style>
.case_content_red_field {
color: #CC0000;
font-weight: bold;
font-size: 10pt;
}

	.yui-skin-sam .yui-pg-container {
		text-align: right;
	}

	.yui-skin-sam .yui-dt table {
		width: 100% !important;
		BORDER: 0px !important;
		font-family: Verdana,Arial,sans-serif !important;
	}
	
	.yui-skin-sam .yui-dt thead {		
		font-size: 11px;
		font-weight: bold !important;
		border: none;
	}
	
	.yui-skin-sam .yui-dt th, .yui-skin-sam .yui-dt th a {
		font-weight:bold !important;
		text-decoration:underline !important;
		font-size: 10pt;
	}
	
	.yui-skin-sam th.yui-dt-asc .yui-dt-liner	{
		background: none !important;
	}
	
	.yui-skin-sam th.yui-dt-asc, .yui-skin-sam th.yui-dt-desc, .yui-skin-sam th.yui-dt-asc, .yui-skin-sam th.yui-dt-desc {
		background: #DDDDDD !important;
	}
	
	.yui-skin-sam .yui-dt th {
		background: #DDDDDD !important;
	}
	
	.yui-skin-sam .yui-dt th, .yui-skin-sam .yui-dt td {
		border-width: 0 !important;
		text-align: left;		
	}
	
	.yui-dt-label {
		font-size: 11px;
		font-weight: bold !important;
	}
	
	.yui-skin-sam tr.yui-dt-even td.yui-dt-asc, .yui-skin-sam tr.yui-dt-even td.yui-dt-desc {
		background-color: #FFFFFF !important;
	}
	
	.yui-skin-sam tr.yui-dt-odd, .yui-skin-sam tr.yui-dt-odd td.yui-dt-asc, .yui-skin-sam tr.yui-dt-odd td.yui-dt-desc {
		background-color: #EEEEEE !important;		
	}
	
	
	.yui-skin-sam .yui-dt-liner {
		text-align: center;
	}
	
	.yui-skin-sam .yui-pg-page {
		border: 0px !important;
		padding: 2px !important;		
	}
	
	#yui-history-iframe {
	  position:absolute;
	  top:0; left:0;
	  width:1px; height:1px; /* avoid scrollbars */
	  visibility:hidden;
	}
	
	.yui-skin-sam .yui-dt-paginator {
		font-weight: bold;
	}
	
	.yui-skin-sam .yui-pg-first, .yui-skin-sam .yui-pg-previous, .yui-skin-sam .yui-pg-next, .yui-skin-sam .yui-pg-last, .yui-skin-sam .yui-pg-current, .yui-skin-sam .yui-pg-pages, .yui-skin-sam .yui-pg-page {
		font-family: Verdana,Arial,sans-serif !important;
		font-size: 9px;
		font-weight: bold;
	}
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


org.json.JSONObject jobj = new org.json.JSONObject();
jobj.put("totalRecords", activities.size());
org.json.JSONArray jsonItems = new org.json.JSONArray();
for(int i=0; i< activities.size(); i++) {
	com.freshdirect.customer.ErpActivityRecord activity = (com.freshdirect.customer.ErpActivityRecord) activities.get(i);
	String action = "<b>" + activity.getActivityType().getName() + "</b>" + ((activity.getNote() != null && !"".equals(activity.getNote())) ? " - " +activity.getNote() : "");
	if (activity.getChangeOrderId() != null) { 
		action = action + "<i>(Order #: " + activity.getChangeOrderId() + ")</i>";
	} 
	if (activity.getStandingOrderId() != null) { 
		action = action + "<i>(Standing Order #: " + activity.getStandingOrderId() + ")</i>";
	} 
	
	org.json.JSONObject obj = new org.json.JSONObject();
	obj.put("adate", CCFormatter.formatDateTime(activity.getDate()));
	obj.put("action", action);
	obj.put("actionby", activity.getMasqueradeAgent() == null ? activity.getInitiator() : (activity.getMasqueradeAgent() + " as " + activity.getInitiator()));
	obj.put("source", activity.getSource().getName());
	jsonItems.put(obj);
}

jobj.put("records", jsonItems);
	
String jsonString = jobj.toString();
%>
<!-- Combo-handled YUI CSS files: -->
<fd:css href="/assets/yui-2.9.0/paginator/assets/skins/sam/paginator.css" />
<fd:css href="/assets/yui-2.9.0/datatable/assets/skins/sam/datatable.css" />

<!-- Combo-handled YUI JS files: -->
<fd:javascript  src="/assets/yui-2.9.0/yahoo-dom-event/yahoo-dom-event.js" />
<fd:javascript  src="/assets/yui-2.9.0/connection/connection-min.js" />
<fd:javascript  src="/assets/yui-2.9.0/element/element-min.js" />
<fd:javascript  src="/assets/yui-2.9.0/paginator/paginator-min.js"/>
<fd:javascript  src="/assets/yui-2.9.0/datasource/datasource-min.js" />
<fd:javascript  src="/assets/yui-2.9.0/datatable/datatable-min.js" />
<fd:javascript  src="/assets/yui-2.9.0/json/json-min.js" />
<fd:javascript  src="/assets/yui-2.9.0/selector/selector-min.js" />

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
						for (EnumAccountActivityType name:listActType) {
							if(null != name){
							%>
							<option value="<%=name.getCode() %>" <%=name.getCode().equalsIgnoreCase(actType)?"selected":"" %>><%= name.getName() %></option>
						<% } } }%>
						</select>
					</td>
					
					<td>&nbsp;</td>
					<td>By
						<select id="initiatedBy" name="initiatedBy" class="promo_filter">
						<option value="" selected="selected">ALL</option>
						<% List<String> listInit =  (List<String>)map.get("initiator");
						if(null != listInit){
						for (String name:listInit) {
							if(null != name){
						%>
							<option value="<%=name %>" <%= name.equalsIgnoreCase(initiateBy)?"selected":"" %>><%= name %></option>
						<% } } }%>
						</select>
					</td>
					<td>&nbsp;</td>
					<td>Source
						<select id="trnSource" name="trnSource" class="promo_filter">
						<option value="" selected="selected">ALL</option>
						
						<% List<EnumTransactionSource> list =  (List<EnumTransactionSource>)map.get("source");
						if(null != list){
						for (EnumTransactionSource name:list) {
							if(null != name){
							%>
							<option value="<%=name.getCode() %>" <%=name.getCode().equalsIgnoreCase(transactionSource)?"selected":"" %>><%= name.getName() %></option>
						<% } } }%>
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
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
			<tr><td>
			<div id="pagenums"></div>
			<div id="dynamicdata"></div> 
			</td></tr>
			</table>
			
			<script type="text/javascript">
				YAHOO.util.Event.addListener(window, "load", function() {
					YAHOO.example.ClientPagination = function() {
						var myColumnDefs = [
							{key:"adate", label:"Date | Time", sortable:true},
							{key:"action", label:"Action", sortable:true},
							{key:"actionby", label:"By", sortable:true},
							{key:"source", label:"Source", sortable:true}
						];

						var myDataSource = new YAHOO.util.DataSource(<%= jsonString %>);
						myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
						myDataSource.responseSchema = {
							resultsList: "records",
							fields: ["adate","action","actionby","source"]
						};

						var oConfigs = {
								paginator: new YAHOO.widget.Paginator({
									rowsPerPage: 15
								}),
								initialRequest: "results=10504",
								paginator: new YAHOO.widget.Paginator({ rowsPerPage:15,
									template : "Page: {PageLinks}",
									containers  : 'pagenums'
								}) // Enables pagination 
						};
						var myDataTable = new YAHOO.widget.DataTable("dynamicdata", myColumnDefs,
								myDataSource, oConfigs);
								
						return {
							oDS: myDataSource,
							oDT: myDataTable
						};
					}();
				});
				</script>

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
