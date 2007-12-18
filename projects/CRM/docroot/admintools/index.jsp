<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Re-Submit Orders</tmpl:put>

<tmpl:put name='content' direct='true'>
<%
    boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
%>
<jsp:include page="/includes/admintools_nav.jsp" />
<div class="sub_nav">
<span class="sub_nav_title">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<crm:CrmResubmitOrders id="nsmOrders" result="result">


<% if (!result.isSuccess() ) { %>
        <div id="result" class="list_content" style="height:70%;">
        <table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
		<logic:iterate id="errs" collection="<%= result.getErrors() %>" type="com.freshdirect.framework.webapp.ActionError" indexId="idx">
        <tr>
			<td class="border_bottom">&nbsp;</td>
			<td class="border_bottom"><%=errs.getDescription()%></td>
		</tr>
		</logic:iterate>
        </table>
        </div>
<% } %>  
<% if (!isPost && nsmOrders.size() == 0) { %>
        <tr><td colspan="4" align="center"><br><b>There were no Non Submitted Orders found</b></td></tr>
<% } %>
   <form method='POST' name="frmResubmitOrders">
  		<table width="100%" cellpadding="0" cellspacing="0" class="sub_nav">
			<tr><td>Resubmit Orders that are in Non-Submitted Mode</span></td></tr>
		</table>
<% if (!isPost && !nsmOrders.isEmpty()) { %>
    <table width="100%" cellpadding="0" cellspacing="0" class="content_fixed">
        <tr>
          <td align="center">
          </td>
        </tr>
        <tr>
          <td align="left">
            <%=nsmOrders.size()%>&nbsp;orders found &nbsp;&nbsp;&nbsp;
            Amount marked for resubmittal: <input onFocus="blur();" type=text size="6" name="ordersSelected" readonly value="0"><br> 
            <a href="javascript:checkAll(true);">Select first 100 orders</a>&nbsp;&nbsp;&nbsp;
            <a href="javascript:checkAll(false);">Click here to Clear all</a><br><br>
            <input type="hidden" name="action" value="resubmitOrders">
            <input type="submit" value="resubmit checked items">
          </td>
        </tr>
    </table>
    <table width="100%" cellspacing="0" border="0" style="empty-cells: show">
        <tr bgcolor="#333366" class="list_header_text">
            <td align="left">&nbsp;</td>
            <td align="left">Order ID</td>
            <td align="left">Last Action Date</td>
            <td align="left">Status</td>
            <td align="left">Amount</td>
            <td align="left">Delivery Date</td>
            <td align="left">Customer Name</td>
        </tr>
    <div id="result" class="list_content" style="height:70%;">
        <logic:iterate id="orderInfo" collection="<%= nsmOrders %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="idx">
            <tr <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
                <td class="border_bottom">&nbsp;</td>
                <td class="border_bottom">
                    <input name="resubmitSaleId" type="checkbox" onClick="countChecked(this);" value="<%=orderInfo.getSaleId()%>">
                    <a href="/main/order_details.jsp?orderId=<%=orderInfo.getSaleId()%>"><%=orderInfo.getSaleId()%></a>&nbsp;
                </td>
                <td class="border_bottom"><%=orderInfo.getLastCroModDate()%></td>
                <td class="border_bottom"><%=orderInfo.getOrderStatus()%></td>
                <td class="border_bottom"><%=orderInfo.getAmount()%></td>
                <td class="border_bottom"><%=orderInfo.getDeliveryDate()%>&nbsp;</td>
                <td class="border_bottom"><%=orderInfo.getFirstName()+" "+orderInfo.getLastName()%>&nbsp;</td>
            </tr>
        </logic:iterate>
<% } %>
	 
	</table>
</div>
</form>
<script language"javascript">
	<!--
	var ordersSelected =0;
	function countChecked(cbObj) {
		
		//alert(cbObj.checked)
		if (cbObj.checked) {
			ordersSelected++;
		} else {
			ordersSelected--;
		}
		showSelected();
	}
	
	function showSelected() {
		var outputObj = document.forms["frmResubmitOrders"].ordersSelected;
		if (ordersSelected<=0) ordersSelected=0;
		outputObj.value=ordersSelected;
	}
	
	function checkAll(flag) {
		var elements = document.forms["frmResubmitOrders"].elements;
		for (var i=0;i<elements.length;i++) {
			//alert(elements[i].name+" / "+ elements[i].type);
            //100 + 3 pre-existing elements in this form
           if(i < 103) {
			if (elements[i].name=="resubmitSaleId" && elements[i].type=="checkbox") {
				if (flag && !elements[i].checked) {
					elements[i].checked=true;
					ordersSelected++;
				}
				if (!flag && elements[i].checked) {
				   elements[i].checked=false;
				   ordersSelected--;
				}
			}
           }
           showSelected();
		}
	}
	
	
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.order_status.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>

</crm:CrmResubmitOrders>
</tmpl:put>

</tmpl:insert>

