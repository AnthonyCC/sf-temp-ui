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

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Settlement Batch</tmpl:put>

<tmpl:put name='content' direct='true'>
<style type="text/css">
body {
	background-color: #FFFFFF;
}
</style>
<script>
    function doAction(actionName,index) {
	if(actionName == 'fixSettlemnentBatch'){
		var doFix = confirm ("Do you want to fix this settlement batch?");
		if(doFix == false){
			return;
		}            		
		document.forms[index].elements[0].value = actionName;
		document.forms[index].submit();	
	}
    }

</script>
<jsp:include page="/includes/admintools_nav.jsp" />

</tmpl:put>
</tmpl:insert>