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

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Broken Accounts</tmpl:put>

<tmpl:put name='content' direct='true'>

<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>
<script>
    function doAction(actionName) {
	if(actionName == 'fixBrokenAccounts'){
		var doFix = confirm ("Do you want to fix the Broken Accounts?");
		if(doFix == false){
			return;
		}            		
		document.getElementById("actionName").value = actionName;
		document.forms[0].submit();	
	}
    }

</script>

</tmpl:put>
</tmpl:insert>