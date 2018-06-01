<%@ page import="java.util.*"%>
<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.storeapi.content.*'  %>
<%@ page import='com.freshdirect.fdstore.referral.FDReferralManager'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<% //expanded page dimensions
final int W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL = 970;
%>
<%
String successPage = "/your_account/credits.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
    }	
    

	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");	
	
%>




<tmpl:insert template='/common/template/dnav.jsp'>
<%-- <tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put> --%>
<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Profile" pageId="credit"></fd:SEOMetaTag>
	</tmpl:put>
<tmpl:put name='content' direct='true'>

<style>

	body { font-family: Verdana, Arial, sans-serif; font-size: 10px; height: 100%; }
	a:link, a:visited { color:#360}
	a:active { color:#f90; }
	
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
	.tcredits {background-color:#D0E1F1;font-size:11px;font-weight:normal;padding:5px;border-radius: 3px;-moz-border-radius: 3px;-webkit-border-radius: 3px;}	

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


<!-- * start the actual summary info * -->

<%
	List<ErpCustomerCreditModel> mimList = FDReferralManager.getUserCredits(customerIdentity.getErpCustomerPK());
%>
<table role="presentation" cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td class="t20px bolded">
			FreshDirect Account Credits <br/> <span class="t11px" style="font-weight:normal;">Please review your orders. To check the status of an order, click on the order number.</span>
		</td>
		<td align="right">
			<span class="tcredits">Total remaining credits: <span id="totalAmount" class="t11px bolded"><% if (mimList.size() == 0) { %>$0.00<% } %></span></span>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			<img width="970" height="1" border="0" alt="" src="/media_stat/images/layout/ff9933.gif"><br/>
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
			<div id="dynamicdata"><% if (mimList.size() == 0) { %><center><b>You do not have credits at this time</b></center><%}%></div> 			
		</td>
	</tr>	
</table>
	


<!-- * end the actual summary info * -->
<br><br>
<IMG src="/media_stat/images/layout/ff9933.gif" ALT="" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>" HEIGHT="1" BORDER="0"><BR>
<FONT CLASS="space4pix"><BR><BR></FONT>
<TABLE role="presentation" BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="<%= W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL %>">
<tr VALIGN="TOP">
<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="" ALIGN="LEFT">
CONTINUE SHOPPING
<BR>from <FONT CLASS="text11bold">Home Page</A></FONT><BR><IMG src="/media_stat/images/layout/clear.gif" alt="" WIDTH="340" HEIGHT="1" BORDER="0"></TD>
</tr>

</TABLE>

<% if (mimList.size() > 0) { %>
<script type="text/javascript">
YAHOO.example.DynamicData = function() {

	// Override the built-in formatter
    YAHOO.widget.DataTable.formatLink = function(elCell, oRecord, oColumn, oData) {
       if(oData != null)
		elCell.innerHTML = "<a href=\"/your_account/order_details.jsp?orderId=" + oData + "\">" +"<span class=\"offscreen\">details of order number</span>"+ oData + "</a>";
    };

    // Column definitions
    var myColumnDefs = [ // sortable:true enables sorting
		{key:"date", label:"Date", sortable:false},
        {key:"type", label:"Type", sortable:false},
        {key:"order", label:"Against Order", sortable:false, formatter:YAHOO.widget.DataTable.formatLink},
        {key:"amount", label:"Credit Amount", sortable:false}
    ];
    
    // DataSource instance
    var myDataSource = new YAHOO.util.DataSource("/your_account/creditsjson.jsp?");
    myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    myDataSource.responseSchema = {
        resultsList: "records",
        fields: [
			{key:"date"},
            {key:"type"},
            {key:"order"},
            {key:"amount"}
        ],
        metaFields: {
            totalRecords: "totalRecords", // Access to value in the server response
			totalAmount: "totalAmount"
        }
    };
    
    // DataTable configuration
    var myConfigs = {
        initialRequest: "sort=type&dir=asc&startIndex=0&results=25", // Initial request for first page of data
        dynamicData: true, // Enables dynamic server-driven data
        sortedBy : {key:"type", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
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
		$('totalAmount').innerHTML = oResponse.meta.totalAmount;
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
