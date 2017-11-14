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

<form name = "scrubbingInProgress" id="scrubbingInProgress" action="/addressscrubbing.jsp" method="post">
<div style="background: #fff; overflow: auto; width: 100%; height: 420px;">
		<div class="scrubbingText">Address Scrubbing Tool:</div>
		<div class="scrubbingText">Address Scrubbing is in progress...</div>
<!-- 	<a href="/scrubbingtool/address_scrubbing_tool.jsp" ><input type="submit" id = "submit" value="Cancel Scrubbing" class="cancelSrubbing_btn"/>  -->
<!-- 	<input type="button" value="Cancel Scrubbing" name="cancel" class="cancelSrubbing_btn"></a> -->
	<br>
	<%
		if(request.getAttribute("future") != null){
			request.setAttribute("future", (Future)request.getAttribute("future"));
		}
	%>
	<input type="hidden" name="addressScrubbingAction" id="addressScrubbingAction" value="cancelscrubbing" />
	<input type="submit" id = "submit" value="Cancel Scrubbing" class="cancelSrubbing_btn"/>
	</div>
</form>


</tmpl:put>

</tmpl:insert>



<script type="text/javascript">
	var id = setInterval(function(){ myFunction(); }, 3000);
	function myFunction () {
		var oReq = new XMLHttpRequest();
		oReq.addEventListener("load", reqListener);
		oReq.open("GET", "/addressscrubbing.jsp");
		oReq.send();
		}
	function reqListener () {
		 if(this.responseText === 'Completed'){
			 document.getElementById('addressScrubbingAction').value='scrubbingCompleted' ;
			 clearInterval(id);
			 
			 var submitBtn = document.getElementById("submit");
			 submitBtn.click();
		 }
		
	}
		
</script>