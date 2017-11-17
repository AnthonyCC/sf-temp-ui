<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ page import='java.util.ArrayList'%>
<%@ page import='java.util.HashMap'%>
<%@ page import='java.util.Iterator'%>
<%@ page import='java.util.List'%>
<%@ page import='java.util.Map'%>
<%@ page import='com.freshdirect.logistics.delivery.dto.ScrubbedAddress' %>
<%@ page import='java.util.concurrent.Future' %>

<%@ page import="com.freshdirect.crm.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<% boolean isGuest = false; %>
	<crm:GetCurrentAgent id="currentAgent">
		<%-- isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); --%>
	</crm:GetCurrentAgent>
	
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Address Scrubbing Tool</tmpl:put>
	<tmpl:put name='content' direct='true'>
	
		<form name = "scrubbingCompleted" id="scrubbingCompleted" action="/scrubbedCSVReport.jsp" method="post">
			
			<div style="background: #fff; overflow: auto; width: 100%; height: 420px;">
			<br>	
				<div class="scrubbingText">Address Scrubbing Tool:</div>
			<br>	
				<div class="scrubbingText">Your download will begin in a few moments.</div><br>	
			<br>
			<%= request.getAttribute("report")%>
				<%
					Future future  = (Future)request.getAttribute("future");
					String value = "Null";
					if(future != null ){
						request.setAttribute("future", future);
						session.setAttribute("future", future);
						value = "Not Null";
					}else{
						value = "Null";
					}
			%>
			<a href="/scrubbingtool/address_scrubbing_tool.jsp" ><input type="button" value="Scrub New File" name="cancel" class="scrubb_btn_enable"></a>
			</div>
		</form>
	
</tmpl:put>

</tmpl:insert>

<script type="text/javascript">
	var id = setInterval(function(){ myFunction(); }, 2000);
	function myFunction () {
		clearInterval(id);
		document.getElementById("scrubbingCompleted").submit();
		}
		
</script>
