<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.freshdirect.fdstore.referral.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import="com.freshdirect.framework.util.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>


<%
SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy");
FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
FDIdentity identity = user.getIdentity();
List referralRptList = new ArrayList();

referralRptList = FDReferralManager.getManageInvitesForCRM(identity.getErpCustomerPK());
org.json.JSONObject jobj = new org.json.JSONObject();
jobj.put("totalRecords", referralRptList.size());
org.json.JSONArray jsonItems = new org.json.JSONArray();
for(int i=0; i< referralRptList.size(); i++) {
	ManageInvitesModel mim = (ManageInvitesModel) referralRptList.get(i);
	String scredit = "";
	org.json.JSONObject obj = new org.json.JSONObject();
	obj.put("referee", mim.getRecipientEmail());
	obj.put("date", mim.getSentDate());
	obj.put("status", mim.getStatus());
	obj.put("credit", mim.getCredit()!=null?JspMethods.formatPrice(Double.parseDouble(mim.getCredit())):"");
	obj.put("custid", mim.getRecipientCustId()!=null?"<a href='/main/summary.jsp?erpCustId="+ mim.getRecipientCustId() + "'>" + mim.getRecipientCustId() + "</a>":"");
	obj.put("order", mim.getSaleId()!=null?"<a href='/main/order_details.jsp?orderId=" + mim.getSaleId() + "'>" + mim.getSaleId() + "</a>":"");
	obj.put("rcredit", mim.getCreditIssuedDate() != null?DATE_FORMATTER.format(mim.getCreditIssuedDate()):"");
	jsonItems.put(obj);
}

jobj.put("records", jsonItems);
					
String jsonString = jobj.toString();
%>
<style>
	.yui-skin-sam .yui-pg-container {
		text-align: right;
		padding-right: 30px;
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
	
	.t11px { font-size: 11px; }
	.t12px { font-size: 12px; }
	.t20px { font-size: 20px; }
	.tOrange { color: orange; }
	.bolded { font-weight: bold; }
	.tcredits {background-color:#C9EFFF;font-size:11px;font-weight:normal;padding:5px;border-radius: 10px;-moz-border-radius: 10px;-webkit-border-radius: 10px;}	

}
</style>
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

<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Referral History</tmpl:put>
<tmpl:put name='content' direct='true'>

<div id="pagenums"></div>
<div id="dynamicdata"></div> 
<table>
<tr>
		<td colspan="2">
			<!-- Legend-->
			<br/><br/>
			<div id="legend" style="float:left;">
			<span class="t11px bolded">Legend</span>
			<ul>
				<li>No response – no reply to the email you sent</li>
				<li>Signed up – responded and eligibility was pre-approved</li>
				<li>Offer redeemed --approved as as first time residential customer and received their first order.</li>
				<li>Offer not redeemed – entered another discount code </li>
				<li>Inelgible – recipient was already a customer or was previously referred by another customer</li>
			</ul>
			</div>
		</td>
	</tr>
</table>

			<script type="text/javascript">
				YAHOO.util.Event.addListener(window, "load", function() {
					YAHOO.example.ClientPagination = function() {
						var myColumnDefs = [
							{key:"referee", label:"Referee", sortable:true},
							{key:"date", label:"Date Sent", sortable:true},
							{key:"status", label:"Status", sortable:true},
							{key:"credit", label:"Credit Amount To Referral", sortable:true},
							{key:"custid", label:"New Customer ID", sortable:true},
							{key:"order", label:"New Customer Order#", sortable:true},
							{key:"rcredit", label:"Referral Credit Received", sortable:true}
						];

						var myDataSource = new YAHOO.util.DataSource(<%= jsonString %>);
						myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
						myDataSource.responseSchema = {
							resultsList: "records",
							fields: ["referee","date","status","credit","custid","order","rcredit"]
						};

						var oConfigs = {								
								initialRequest: "results=10504",
								paginator: new YAHOO.widget.Paginator({ rowsPerPage:15,
									template : "<style='padding:10px;'>Page: {PageLinks}",
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
