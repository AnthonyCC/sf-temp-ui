<%@page import="com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag"%>
<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>

<%@ taglib uri="freshdirect" prefix="fd" %>


<!DOCTYPE html>
<html>
<head>
    <title>FreshDirect</title>
    <%@ include file="/common/template/includes/metatags.jspf" %>
    <%@ include file="/common/template/includes/i_javascripts.jspf" %>  
    <%@ include file="/shared/template/includes/style_sheet_grid_compat.jspf" %>
    <%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
</head>
<body>
	
	<div id="sulCont" class="signup-style-social signin-social-forgot-pass-confirm">
		<div class="form-side-social-header">Check your email.</div>
		<div class="signin-social-forgot-pass-header-message">A link to reset your password is on its way.</div>
	</div>

<%@ include file="/common/template/includes/i_jsmodules.jspf" %>	
</body>
</html>
