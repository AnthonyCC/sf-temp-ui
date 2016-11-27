<!DOCTYPE html>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%  request.setAttribute("sitePage", "www.freshdirect.com/");
	request.setAttribute("listPos", "SystemMessage,LittleRandy");
%>
<fd:CheckLoginStatus id="user"/>
<html>
	<head>
        <%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<jwr:style src="/oldglobal.css"/>
		<jwr:style src="/global.css"/>
	  	<jwr:style src="/pdp.css"/>
        <jwr:style src="/quickshop.css"/>
		<jwr:script src="/fdlibs.js" useRandomParam="false" />	  	
	</head>
	<body>
		<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
		<jsp:include page="/includes/product/cartConfirm.jsp">
			<jsp:param name="catId" value="cut_veg_pkgsld_org"/>
			<jsp:param name="productId" value="veg_earthbd_sldclm_4"/>
		</jsp:include>
		<soy:import packageName="pdp"/>
		<jwr:script src="/fdmodules.js"  useRandomParam="false" />
		<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
		<jwr:script src="/pdp.js"  useRandomParam="false" />
	</body>
</html>
