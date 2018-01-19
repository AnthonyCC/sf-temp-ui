<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<% //expanded page dimensions
final int W_CONTACT_FD_TOTAL = 970;
final int W_CONTACT_FD_LEFT_PAD = 300;
%>

<fd:CheckLoginStatus />
<script language='javascript'>
<!--
function limitText(textArea, length) {
    if (textArea.value.length > length) {
        textArea.value = textArea.value.substr(0,length);
    }
}
-->
</script>
<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
	
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
        ErpCustomerInfoModel cm = null;
        if(identity != null){
            cm = FDCustomerFactory.getErpCustomerInfo(identity);
	}

    
%>		

<%
int subjectIndex = -1;
String email = "";
String firstName = "";
String lastName = "";
String homePhone = "";
String homePhoneExt = "";
String workPhone = "";
String workPhoneExt = "";
String altPhone = "";
String altPhoneExt = "";
String body = "";

if (cm != null) {
	email = cm.getEmail();
	lastName = cm.getLastName();
	firstName = cm.getFirstName();
	homePhone = cm.getHomePhone()==null?"":cm.getHomePhone().getPhone();
	homePhoneExt = cm.getHomePhone()==null?"":cm.getHomePhone().getExtension();
	altPhone = cm.getOtherPhone()==null?"":cm.getOtherPhone().getPhone();
	altPhoneExt = cm.getOtherPhone()==null?"":cm.getOtherPhone().getExtension();
}

if(request.getParameter("subject")!=null){
	try {
		subjectIndex = Integer.parseInt(request.getParameter("subject"));
	} catch (NumberFormatException Ex){
		subjectIndex = 0;
	}
}
if(request.getParameter("email")!=null){
	email=request.getParameter("email");
}
if(request.getParameter("first_name")!=null){
	firstName=request.getParameter("first_name");
}
if(request.getParameter("last_name")!=null){
	lastName=request.getParameter("last_name");
}
if(request.getParameter("home_phone")!=null){
	homePhone=request.getParameter("home_phone");
}
if(request.getParameter("home_phone_ext")!=null){
	homePhoneExt=request.getParameter("home_phone_ext");
}
if(request.getParameter("work_phone")!=null){
	workPhone=request.getParameter("work_phone");
}
if(request.getParameter("work_phone_ext")!=null){
	workPhoneExt=request.getParameter("work_phone_ext");
}
if(request.getParameter("alt_phone")!=null){
	altPhone=request.getParameter("alt_phone");
}
if(request.getParameter("alt_phone_ext")!=null){
	altPhoneExt=request.getParameter("alt_phone_ext");
}
if(request.getParameter("message")!=null){
	body=request.getParameter("message");
}

String successPage = "/help/contact_fd_thank_you.jsp";
String overlay = "false";
if(request.getParameter("overlay")!=null){
	overlay=request.getParameter("overlay");
}

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/dnav.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com/help/contact_fd.jsp";
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}

if ("true".equalsIgnoreCase(overlay)) {
	pageTemplate = "/common/template/no_nav_html5.jsp"; //instead of mobWeb template
	successPage += "?overlay=true";
}


%>

<fd:ContactFdController result="result" successPage='<%= successPage %>'>

<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name='extraCss' direct='true'>
    	<% if (mobWeb && "true".equalsIgnoreCase(overlay)) { %>
    		<jwr:style src="/mobileweb.css" media="all" /><%-- mobileweb should be last, for overriding --%>
    	<% } %>
   	</tmpl:put>
    <tmpl:put name="seoMetaTag" direct='true'>
      <fd:SEOMetaTag title="FreshDirect - Help - Contact Us"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Help - Contact Us</tmpl:put> --%>
    <tmpl:put name='content' direct='true'>
	    	
			<div class="contact_fd-cont"><%@ include file="/help/i_contact_us.jspf" %></div>
</tmpl:put>
</tmpl:insert>

</fd:ContactFdController>