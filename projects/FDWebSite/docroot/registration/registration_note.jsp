<%@ page import='com.freshdirect.fdstore.promotion.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id='user' />
<%
	FDUserI userx = (FDUserI)session.getAttribute(SessionName.USER);
	int regType = 0;
	String successPage = "/index.jsp";

	if(userx.isCorporateUser()){
		successPage = "/department.jsp?deptId=COS";
	}else if (userx.isDepotUser()){
		successPage = "/index.jsp";
	}else{
		successPage = "/index.jsp";
	}
%>
<tmpl:insert template='/common/template/no_nav.jsp'>
	<tmpl:put name='title' direct='true'>Important Note</tmpl:put>
		<tmpl:put name='content' direct='true'>
<%
String promoChange = "false";
String dlvChange = "false";

Promotion promotion = (Promotion) user.getEligibleSignupPromotion();
int promotionValue = promotion!=null ? (int)promotion.getHeaderDiscountTotal(): 0;
%>
<div align="center">
<img src="/media_stat/images/template/registration/thank_you_signup.gif" width="544" height="34" alt="Thank you for signing up!" border="0"><br><br>
<br>
<%@ include file="/includes/registration/i_promoEligibility.jspf" %>
<a href="<%= response.encodeURL(successPage) %>"><img src="/media_stat/images/template/registration/continue_shopping.gif" width="211" height="33" alt="Continue Shopping" border="0"></a>
</div>
</tmpl:put>
</tmpl:insert>