<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="fd" uri="freshdirect" %>
<%@ taglib prefix="logic" uri="logic" %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); 
	//expanded page dimensions
	final int W_GLOBAL_NAV_TOP_TOTAL = 970;
%>
<div id="messages" class="visHidden">
	<ul class="content"></ul>
	<hr class="shadow">
	<a href="#" class="handler close-handler" onclick="event.preventDefault();" id="locabar-messages-close"><span class="offscreen">close</span></a>
	<br class="NOMOBWEB" />
</div>
