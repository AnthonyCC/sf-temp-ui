<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>

<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Admin Tools > Remove Skus from Order</tmpl:put>

<tmpl:put name='content' direct='true'>

<script language="javascript">
	function doAction(actionName) {
		if(actionName == 'modifyOrders'){
			var doModify = confirm ("Click OK to Continue Or Cancel to Abort");
			if(doModify == false){
				return;
			}            
			if(document.getElementById("sendEmail").checked == true){
				document.getElementById("sendEmail").value = "true";
			}
			document.getElementById("actionName").value = actionName;
			document.getElementById("modifyorders").submit();	
		
		}
	}

</script>

<jsp:include page="/includes/admintools_nav.jsp" />
<div class="home_search_module_content" style="height:100%;">

</div>
</tmpl:put>
</tmpl:insert>

