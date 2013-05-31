<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="fd" uri="freshdirect" %>
<%@ taglib prefix="logic" uri="logic" %>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<% FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); 
	//expanded page dimensions
	final int W_GLOBAL_NAV_TOP_TOTAL = 970;
%>
<div id="messages">
<ul class="content"></ul>
<div class="handler"></div>
</div>
<script src="/assets/javascript/messages.js"></script>