<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.deliverypass.DlvPassConstants"%>
<%@ page import="com.freshdirect.deliverypass.DeliveryPassInfo"%>
<%@ page import="com.freshdirect.deliverypass.DlvPassUsageLine"%>
<%@ page import='com.freshdirect.webapp.util.CCFormatter' %>
<%@ page import='com.freshdirect.fdstore.deliverypass.DeliveryPassUtil' %>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassExtendReason"%>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassCancelReason"%>
<%@ page import="com.freshdirect.deliverypass.DeliveryPassModel"%>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassStatus"%>
<%@ page import="com.freshdirect.webapp.crm.security.*" %>


<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>

<script type="text/javascript">
	function openURL(inLocationURL) {

	    self.parent.location.href = inLocationURL;

	}

	function hideshow(obj, index,linkName){

		link = document.getElementById(linkName+index); 
		trowId = document.getElementById('trow'+index); 
		browId = document.getElementById('brow'+index); 
		
		if(navigator.appName.indexOf('Microsoft') != -1){

			if (obj.style.display=="block"){
				obj.style.display="none";
				trowId.style.backgroundColor="#FFFFFF";
				trowId.style.fontWeight="normal";
				browId.style.backgroundColor="#FFFFFF";
				if(linkName=="showLink") {
					link.innerText = "Expand to view details";
				}
				else {
					link.innerText = "Expand to cancel DeliveryPass";
				}
			}
			else {
				obj.style.display="block";
				trowId.style.backgroundColor="#FFFFCE";
				trowId.style.fontWeight="bold";
				browId.style.backgroundColor="#FFFFCE";
				link.innerText = "Hide details";
			}
		} else {
			if (obj.style.display=="block"){
				obj.style.display="none";
				trowId.style.backgroundColor="#FFFFFF";
				trowId.style.fontWeight="normal";
				browId.style.backgroundColor="#FFFFFF";
				if(linkName=="showLink") {
					link.innerText = "Expand to view details";
				}
				else {
					link.innerText = "Expand to cancel DeliveryPass";
				}
			}
			else {
				obj.style.display="block";
				trowId.style.backgroundColor="#FFFFCE";
				trowId.style.fontWeight="bold"
				browId.style.backgroundColor="#FFFFCE";
				link.firstChild.nodeValue = "Hide details";
			}
		}
	}
	function submitAction(counter,actionName) {
	    var form = document.forms['deliverypass_'+counter];
            form.elements['action_name'].value=actionName;
            form.elements['passNum'].value=counter;

            if(actionName == 'cancel_RTU_pass'){
            	if(form.orderAssigned.value == ''){
            		alert('Please select an order number involved.');
            		form.orderAssigned.focus();
            		return;
           		}
           		if(form.cancelReason.value == ''){
            		alert('Please select a reason code to cancel a DeliveryPass.');
            		form.cancelReason.focus();
            		return;
           		}
           		if(form.notes.value == ''){
				alert('Notes required');
				form.notes.focus();
				return;
           		}
            	var doCancel = confirm ("Are you sure you want to cancel this DeliveryPass?");
            	if(doCancel == false){
            		return;
			}            		
           	
            }
            form.notes.value=form.notes.value+". The refund amount is "+form.elements['refundAmount'].value;
            form.method='POST';
            form.submit();
            return false;
	}
	
</script>
<%
	FDUserI	user = (FDSessionUser) session.getAttribute(SessionName.USER);
	String agentRole1 =null;
%>
<crm:GetCurrentAgent id="currentAgent">
<% agentRole1 = currentAgent.getRole().getLdapRoleName(); %>
</crm:GetCurrentAgent>
<% boolean editable = false; %>
<crm:GetLockedCase id="cm">
    <% if (cm!=null && cm.getCustomerPK()!=null) { 
        String erpCustId = cm.getCustomerPK().getId();
            if (user.getIdentity().getErpCustomerPK().equals(erpCustId)) {
                editable = true;
            } 
        } %>
</crm:GetLockedCase>
<% 
String case_required = "<span class=\"cust_module_content_edit\">-Case required to add/extend or cancel an Active DeliveryPass-</span>"; 
%>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr><td><br><b>History</b></td></tr>
<%
	//Retreive the usage info from delivery pass info.
	String identity = user.getIdentity().getErpCustomerPK();
	Map dlvPassInfoMap = (Map) session.getAttribute("DLVP" + identity);

	List history = (List)dlvPassInfoMap.get(DlvPassConstants.PASS_HISTORY);
	if(history == null){
	//No History info available
%>
	<tr><td colspan="7" align="center"><b><u>There were no previous history of DeliveryPasses for this customer.</u></b></td></tr>
<%
	} else {
%>	

<%
	}
%>	
</table>
	
<%
	String successMsg = request.getParameter("successMsg");
	if(request.getMethod().equals("GET")&&successMsg != null){
	String orderId= request.getParameter("orderId");
	if(null !=agentRole1 && CrmSecurityManager.hasAccessToPage(agentRole1,"issue_credit.jsp")){
%>
	<script language="JavaScript">		
		window.top.location.href = "/returns/issue_credit.jsp?orderId=<%=orderId%>&successMsg=<%=successMsg%>"; 
	</script>
<% } else{%>
<script language="JavaScript">		
		alert('<%= successMsg %>'); 
	</script>
<% } } %>	
