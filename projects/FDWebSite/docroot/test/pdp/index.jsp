<!DOCTYPE html>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<html>
	<head>
		<jwr:style src="/oldglobal.css"/>
		<jwr:style src="/global.css"/>
	  	<jwr:style src="/pdp.css"/>
        <jwr:style src="/quickshop.css"/>
		<jwr:script src="/fdlibs.js" useRandomParam="false" />	  	
	</head>
	<body>
		<jsp:include page="/includes/product/productDetail.jsp" >
			<jsp:param name="catId" value="${(empty param.catId) ? 'dai_butte_unsa' : param.catId }"/>
			<jsp:param name="productId" value="${(empty param.productId) ? 'dai_orgvlly_cltrdswtbtr2' : param.productId }"/>
		</jsp:include>
		<soy:import packageName="pdp"/>
		<jwr:script src="/fdmodules.js"  useRandomParam="false" />
		<jwr:script src="/fdcomponents.js"  useRandomParam="false" />
		<jwr:script src="/pdp.js"  useRandomParam="false" />
	</body>
</html>
