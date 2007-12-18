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

<tmpl:put name='title' direct='true'>Supervisor Resources > Re-Submit Customers</tmpl:put>

<tmpl:put name='content' direct='true'>
<% 
  boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
  int customersToDisplay = 0;
%>
<jsp:include page="/includes/admintools_nav.jsp" />
<div class="sub_nav">
<span class="sub_nav_title">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<crm:ResubmitCustomerController id="nsmCustomers" actionName='<%="resubmitCustomer"%>' result="resubmitResult" successPage="<%= request.getRequestURI() %>">
   <form method='POST' name="frmResubmitCustomers">
  		<table width="100%" cellpadding="0" cellspacing="0" class="sub_nav">
			<tr><td>Resubmit Customers that are in Non-Submitted Mode</span></td></tr>
		</table>
	<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
    
 <%  System.out.println("resubmitResult ::"+resubmitResult); %>
  
<%	if (!resubmitResult.isSuccess() ) {	%>
		<logic:iterate id="errs" collection="<%= resubmitResult.getErrors() %>" type="com.freshdirect.framework.webapp.ActionError" indexId="idx">
		<tr>
			<td class="border_bottom">&nbsp;</td>
			<td class="text11rbold"><%=errs.getDescription()%></td>
		</tr>         
		</logic:iterate>
        
<%   }   %>    
</table>

<% //if (!isPost) { %>
		
<%		if (nsmCustomers.size() > 0) {  %>		
			<table width="100%" cellpadding="0" cellspacing="0" class="content_fixed">
				<tr><td align="center">
				<input type="submit" value="resubmit checked items">	</td></tr>
				<tr><td align="left"><%=nsmCustomers.size()%>&nbsp;customers found&nbsp;
				  &nbsp;&nbsp;Amount marked for resubmittal: <input onFocus="blur();" type=text size="6" name="customersSelected" readonly value="0">
				  <br> <a href="javascript:checkAll(true);">Select first 100 customers</a>&nbsp;&nbsp;&nbsp;
				  <a href="javascript:checkAll(false);">Clear all</a>
				</td></tr>
			</table>
			<div class="list_header">
			 <table class="list_header_text" width="100%" cellspacing="0" border="1" border="0" style="empty-cells: show">
				<tr>
					<td align="left"></td>
					<td align="left"><img src="/media_stat/images/layout/clear.gif" width="150" height="1"></td>
					<td align="left"><img src="/media_stat/images/layout/clear.gif" width="180" height="1"></td>
					<td align="left"><img src="/media_stat/images/layout/clear.gif" width="450" height="1"></td>
				</tr>
				<tr>
					<td align="left"></td>
					<td align="left">User ID</td>
					<td align="left">First Name</td>
					<td align="left">Last Name</td>
				</tr>
			</table> 
			</div>
<%		}  
    //} %>
<div id="result" class="list_content" style="height:70%;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">     
       
   
<%   if (!isPost && nsmCustomers!=null && nsmCustomers.size() == 0) { %>
		<tr><td colspan="4" align="center"><br><b>There were no NSM Customers found</b></td></tr>
<%   } else if (nsmCustomers!=null && nsmCustomers.size() > 0) { %>
		<tr>
			<td align="left"></td>
			<td align="left"><img src="/media_stat/images/layout/clear.gif" width="150" height="1"></td>
			<td align="left"><img src="/media_stat/images/layout/clear.gif" width="180" height="1"></td>
			<td align="left"><img src="/media_stat/images/layout/clear.gif" width="450" height="1"></td>
		</tr>
		<logic:iterate id="customerInfo" collection="<%= nsmCustomers %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="idx">
			<tr <%= idx.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
				<td class="border_bottom">&nbsp;</td>
				<td class="border_bottom"><input name="customerId" type="checkbox" onClick="countChecked(this);" value="<%=customerInfo.getIdentity().getErpCustomerPK()%>"><a href="/main/account_details.jsp?erpCustId=<%=customerInfo.getIdentity().getErpCustomerPK()%>"><%=customerInfo.getEmail()%></a>&nbsp;</td>
				<td class="border_bottom"><%=customerInfo.getFirstName()%>&nbsp;</td>
				<td class="border_bottom"><%=customerInfo.getLastName()%>&nbsp;</td>
			</tr>
		</logic:iterate>
<%	 	
	 } %>
	 
	</table>
</div>
</form>
<script language"javascript">
	<!--
	var customersSelected =0;
	function countChecked(cbObj) {
		
		//alert(cbObj.checked)
		if (cbObj.checked) {
			customersSelected++;
		} else {
			customersSelected--;
		}
		showSelected();
	}
	
	function showSelected() {
		var outputObj = document.forms["frmResubmitCustomers"].customersSelected;
		if (customersSelected<=0) customersSelected=0;
		outputObj.value=customersSelected;
	}
	
	function checkAll(flag) {
		var elements = document.forms["frmResubmitCustomers"].elements;
		for (var i=0;i<elements.length;i++) {
			//alert(elements[i].name+" / "+ elements[i].type);
            //100 + 3 pre-existing elements in this form
           if(i < 103) {
			if (elements[i].name=="customerId" && elements[i].type=="checkbox") {
				if (flag && !elements[i].checked) {
					elements[i].checked=true;
					customersSelected++;
				}
				if (!flag && elements[i].checked) {
				   elements[i].checked=false;
				   customersSelected--;
				}
			}
           }
           showSelected();
		}
	}
	
	
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.customer_status.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>

</crm:ResubmitCustomerController>
</tmpl:put>
</tmpl:insert>
