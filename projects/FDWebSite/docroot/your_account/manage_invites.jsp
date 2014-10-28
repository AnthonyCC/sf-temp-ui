<%@ page import="java.util.*"%>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import="com.freshdirect.fdstore.referral.ManageInvitesModel" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<% //expanded page dimensions
final int W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL = 970;
String successPage = "/your_account/manage_invites.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
    }
	
	
response.setHeader("Pragma", "no-cache");
response.setHeader("Cache-Control", "no-cache");
%>

<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='customhead' direct='true'>
<style>

	.yui-skin-sam .yui-pg-container {
		text-align: right;
	}

	.yui-skin-sam .yui-dt table {
		width: 100%;
		text-align: center;
		BORDER: 0px !important;
		font-family: Verdana,Arial,sans-serif !important;
	}
	
	.yui-skin-sam .yui-dt thead {
		background-color: #DDDDDD;
		font-size: 11px;
		font-weight: bold !important;
		border: none;
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
	
	
	.t11px { font-size: 11px; }
	.t12px { font-size: 12px; }
	.t20px { font-size: 20px; }
	.tOrange { color: orange; }
	.bolded { font-weight: bold; }
	.tcredits {background-color:#C9EFFF;font-size:11px;font-weight:normal;padding:5px;border-radius: 10px;-moz-border-radius: 10px;-webkit-border-radius: 10px;}	

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
</tmpl:put>

<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name='content' direct='true'>

<!-- * start the actual summary info * -->

<% List<ManageInvitesModel> mimList = FDReferralManager.getManageInvites(customerIdentity.getErpCustomerPK()); %>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td class="t20px bolded">
			View Referrals
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<img width="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>" height="1" border="0" src="/media_stat/images/layout/ff9933.gif"><br/>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="2" CLASS="yui-skin-sam">
			<div id="pagenums"></div>
			<div id="dynamicdata"><% if (mimList.size() == 0) { %><center><b>You do not have any referrals at this time</b></center><%}%></div>
		</td>			
	</tr>	
	<tr>
		<td colspan="2">
			<!-- Legend-->
			<br/><br/>
			<div id="legend" style="float:left;">
			<span class="t11px bolded">Legend</span>
			<ul>
				<li>No response &ndash; no reply to the email you sent</li>
				<li>Signed up &ndash; responded and eligibility was pre-approved</li>
				<li>Offer redeemed &ndash; approved as first time residential customer and received their first order</li>
				<li>Offer not redeemed &ndash; entered another discount code </li>
				<li>Ineligible &ndash; recipient was already a customer or was previously referred by another customer</li>
			</ul>
			</div>
		</td>
	</tr>
</table>



<!-- * end the actual summary info * -->
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>">
<tr VALIGN="TOP">
<td WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
<td WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL - 35 %>"><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
<BR>from <FONT CLASS="text11bold"><A HREF="/index.jsp">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></td>
</tr>

</TABLE>
<% if(mimList.size() > 0 ) { %>
<script type="text/javascript">
YAHOO.example.DynamicData = function() {
    // Column definitions
    var myColumnDefs = [ // sortable:true enables sorting
        {key:"email", label:"Friends", sortable:false},
        {key:"date", label:"Invitation Sent", sortable:false},
        {key:"status", label:"Status", sortable:false},
        {key:"credit", label:"Credit Received", sortable:false}
    ];
    
    // DataSource instance
    var myDataSource = new YAHOO.util.DataSource("/your_account/manage_invites_json.jsp?");
    myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    myDataSource.responseSchema = {
        resultsList: "records",
        fields: [
            {key:"email"},
            {key:"date"},
            {key:"status"},
            {key:"credit"}
        ],
        metaFields: {
            totalRecords: "totalRecords" // Access to value in the server response
        }
    };
    
    // DataTable configuration
    var myConfigs = {
        initialRequest: "sort=date&dir=asc&startIndex=0&results=15", // Initial request for first page of data
        dynamicData: true, // Enables dynamic server-driven data
        sortedBy : {key:"date", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
        paginator: new YAHOO.widget.Paginator({ rowsPerPage:15,
			template : "Page: {PageLinks}",
			containers  : 'pagenums'
			}) // Enables pagination 
    };
    
    // DataTable instance
    var myDataTable = new YAHOO.widget.DataTable("dynamicdata", myColumnDefs, myDataSource, myConfigs);
    // Update totalRecords on the fly with value from server
    myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
        oPayload.totalRecords = oResponse.meta.totalRecords;
        return oPayload;
    }
	
    
    return {
        ds: myDataSource,
        dt: myDataTable
    };
        
}();

</script>
<% } %>
	</tmpl:put>
</tmpl:insert>
