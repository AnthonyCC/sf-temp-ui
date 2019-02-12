<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<link rel="stylesheet" href="/ccassets/css/crm.css" type="text/css">
<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>
<script>
    function doAction(actionName) {
	if(actionName == 'fixTEmailBatch'){
		var doFix = confirm ("Do you want to fix this failed email transactions?");
		if(doFix == false){
			return;
		}            		
		
		
		document.forms[0].elements[0].value = actionName;
		document.forms[0].submit();	
	}
	if(actionName == 'genarateTEmailFile'){
		var doFix = confirm ("Do you really want to generate file for failed email transactions?");
		if(doFix == false){
			return;
		}            		
		
		alert("forwarding to new URL");
		document.forms[0].elements[0].value = actionName;
		document.forms[0].action="trans_email_error.jsp";
		//window.location = "http://www.google.com/"
		document.forms[0].submit();	
	}
	
    }

</script>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Settlement Batch</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/admintools_nav.jsp" />

</tmpl:put>
</tmpl:insert>