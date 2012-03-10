<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.framework.core.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
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
	
	.yui-skin-sam th.yui-dt-asc, .yui-skin-sam th.yui-dt-desc {
		background: url("/assets/yui-2.9.0/assets/skins/sam/sprite.png") repeat-x scroll 0 0 #D8D8DA !important;
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

    <tmpl:put name='title' direct='true'>Credit History</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
		
        <fd:CustomerCreditHistoryGetterTag id="customerCreditHistory">
		<% List creditHistory = customerCreditHistory.getCreditHistory();%>
		
		<div class="sub_nav">
            <table width="100%">
                <tr>
					<td width="25%" align="center">TOTAL: <b><%=customerCreditHistory.getSumCredit()  + customerCreditHistory.getSumRefund()%></b> <span class="note">(Store Credit<%=customerCreditHistory.getSumCredit()  > 1 ? "s" : ""%>: <b><%=customerCreditHistory.getSumCredit() %></b>, Refund<%=customerCreditHistory.getSumRefund() > 1 ? "s" : ""%>: <b><%=customerCreditHistory.getSumRefund()%></b>)</span--%></td>
                    <td width="25%" align="center">Available store credit: <b><%=JspMethods.formatPrice(customerCreditHistory.getRemainingAmount())%></b></td>
                    <td width="25%" align="center">Total store credit issued: <b><%=JspMethods.formatPrice(customerCreditHistory.getTotalCreditsIssued())%></b></td>
                    <td width="25%" align="center">Total cash back issued: <b><%=JspMethods.formatPrice(customerCreditHistory.getTotalCashBack())%></b></td>
                </tr>
            </table>
		</div>		
        <div class="list_content">
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <%	if ( creditHistory.size() == 0 ) { %>
                    <tr>
                            <td></td>
                            <td colspan="8" align="center"><br><b>No credits found.</b></td>
                            <td></td>
                    </tr>
            <% } else { 
				org.json.JSONObject jobj = new org.json.JSONObject();
				jobj.put("totalRecords", creditHistory.size());
				org.json.JSONArray jsonItems = new org.json.JSONArray();
				for(int i=0; i< creditHistory.size(); i++) {
					FDCustomerCreditModel credit = (FDCustomerCreditModel) creditHistory.get(i);
					String scredit = "";
					if(EnumComplaintLineMethod.STORE_CREDIT.equals(credit.getMethod())) {
						scredit = JspMethods.formatPrice(credit.getOriginalAmount());
					} 
					
					if (credit.getRefSaleId()!=null && credit.getRefSaleId().length() != 0) {
						scredit = scredit + " <a href='/main/order_details.jsp?orderId=" +credit.getRefSaleId() + "'> #" +credit.getRefSaleId()+"</a>";
					} 
					
					String type_code = "";
					if(EnumSaleType.REGULAR.equals(credit.getOrderType())){ 
						if("Referral".equals(credit.getDepartment())) { 
							type_code ="R";
						} else { 
							type_code = "M";
						} 
					}else if(EnumSaleType.SUBSCRIPTION.equals(credit.getOrderType())){
						type_code = "<Font color=\"red\">A</Font>";
					}
					
					org.json.JSONObject obj = new org.json.JSONObject();
					obj.put("date", CCFormatter.formatDateTime(credit.getCreateDate()));
					obj.put("order", credit.getSaleId());
					obj.put("type", type_code);
					obj.put("department", credit.getDepartment());
					obj.put("status", credit.getStatus().getName());
					obj.put("scredit", scredit);
					obj.put("cashback", EnumComplaintLineMethod.CASH_BACK.equals(credit.getMethod())? JspMethods.formatPrice(credit.getOriginalAmount()):" " );
					obj.put("issuedby", credit.getIssuedBy());
					obj.put("approvedby", credit.getApprovedBy());
					jsonItems.put(obj);
				}

				jobj.put("records", jsonItems);
					
				String jsonString = jobj.toString();
			%>
                    <div id="pagenums"></div>
					<div id="dynamicdata"></div> 
					
					<script type="text/javascript">
				YAHOO.util.Event.addListener(window, "load", function() {
					YAHOO.example.ClientPagination = function() {
						var myColumnDefs = [
							{key:"date", label:"Date/Time", sortable:true},
							{key:"order", label:"Order#", sortable:true},
							{key:"type", label:"Type", sortable:true},
							{key:"department", label:"Department", sortable:true},
							{key:"status", label:"Status", sortable:true},
							{key:"scredit", label:"Store Credit", sortable:true},
							{key:"cashback", label:"Cach Back", sortable:true},
							{key:"issuedby", label:"Issued By", sortable:true},
							{key:"approvedby", label:"Approved By", sortable:true}
						];

						var myDataSource = new YAHOO.util.DataSource(<%= jsonString %>);
						myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
						myDataSource.responseSchema = {
							resultsList: "records",
							fields: ["date","order","type","department","status","scredit","cashback","issuedby","approvedby"]
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
            <% } %>       
            </table>
        </div>

			
                    
    </fd:CustomerCreditHistoryGetterTag>

	</tmpl:put>
		
</tmpl:insert>
