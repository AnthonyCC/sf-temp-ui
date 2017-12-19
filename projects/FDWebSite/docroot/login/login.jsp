<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ taglib uri="template" prefix="tmpl" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<% //expanded page dimensions
int W_LOGIN_TOTAL = 970;
%>
<fd:CheckLoginStatus/>
<% 
FDUserI login_user = (FDUserI)session.getAttribute(SessionName.USER);
boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, login_user) && JspMethods.isMobile(request.getHeader("User-Agent"));
boolean isOAuthPage = false;
String template = "/common/template/no_nav.jsp"; //default
if (mobWeb) {
	W_LOGIN_TOTAL = 320;
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
	request.setAttribute("sitePage", "www.freshdirect.com/mobileweb/login/login.jsp"); //change for OAS
}

	//diff nav for popup login
	//if ("popup".equals( request.getParameter("type") ))
boolean isPopup = false;
Boolean fdTcAgree = (Boolean)session.getAttribute("fdTcAgree");
String nextSuccesspage = ((String)session.getAttribute("nextSuccesspage")!=null)?(String)session.getAttribute("nextSuccesspage"):"/login/index.jsp";

String sPage = (request.getParameter("successPage")!=null)?request.getParameter("successPage").toLowerCase():null;
String templateId = request.getParameter("template");
	if (sPage != null) {
		
	    // determine the preSuccessPage from previous workflow
	    session.setAttribute(SessionName.PREV_SUCCESS_PAGE, sPage); 		
		
		if (sPage.indexOf("type=popup") != -1){
			template = "/common/template/large_pop.jsp";
			isPopup = true;
		}else if ( sPage.indexOf("gift_card") > 0 && FDStoreProperties.isGiftCardEnabled() ) {
			template = "/common/template/giftcard.jsp";
		}else if ( sPage.indexOf("robin_hood") > 0 && FDStoreProperties.isRobinHoodEnabled() ) {
			template = "/common/template/robinhood.jsp";
		}else if (sPage.startsWith("/oauth/") || (templateId != null && templateId.equals("oauth"))) {
			template = "/common/template/oAuth.jsp";
			isOAuthPage = true;
		}
	}
%>
<tmpl:insert template='<%=template%>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Log In"/>
    </tmpl:put>
<%-- <tmpl:put name='title' direct='true'>FreshDirect - Log In</tmpl:put> --%>
<tmpl:put name='content' direct='true'>
	
	<%if(fdTcAgree!=null&&!fdTcAgree.booleanValue()){%>
		<script type="text/javascript">
		var nextpage = '<%=nextSuccesspage %>';
		doOverlayWindow("<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage="+nextpage+"\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>");
		</script>
	<%}%>
	
	<table border="0" cellspacing="0" cellpadding="0" width="<%=(mobWeb || isOAuthPage)?"100%":W_LOGIN_TOTAL%>" align="center">
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt=""></td></tr>
		
		<tr>
			<td colspan="2" width="<%=W_LOGIN_TOTAL%>">
				<!-- <img src="/media_stat/images/navigation/current_cust_log_in_now.gif" width="222" height="13" border="0" alt="CURRENT CUSTOMERS LOG IN NOW"> -->
				<span class="Container_Top_CurrentCustLogin">CURRENT CUSTOMERS LOG IN NOW</span>
			</td>
		</tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt=""></td></tr>
		<tr><td colspan="2" bgcolor="#999966" class="onePxTall"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt=""></td></tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="14" alt=""></td></tr>
		<tr>
			<td colspan="2">
				<%@ include file="/includes/i_login_field.jspf" %>
			</td>
		</tr>
	</table>
</tmpl:put>
</tmpl:insert>
