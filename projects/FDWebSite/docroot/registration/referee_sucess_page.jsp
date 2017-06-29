<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.freshdirect.fdstore.referral.ManageInvitesModel" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%
String successPage = "/index.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/index.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>

<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<% 
    DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy EEEE");
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    ErpCustomerInfoModel customerInfo = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
        customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);	
    }
	
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");
%>

<style>

	/* default styles from website css */
	body { font-family: Verdana, Arial, sans-serif; font-size: 10px; height: 100%; }	
	.thankyoutext {
		align:center;
		width:100%;
		font-size:30px;
		font-weight:bold;
		color: #F87217;
	}
	.cbutton {
	}


</style>

<tmpl:insert template='/common/template/no_shell.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="Welcome to FreshDirect"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>Welcome to FreshDirect</tmpl:put>
<tmpl:put name='content' direct='true'>
	<div id="thankyoutext">THANK YOU FOR SIGNING UP!</div>
	<div id="cbutton"><a href="/index.jsp">CONTINUE SHOPPING</a>

</tmpl:put>
</tmpl:insert>
