<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<jwr:style src="/your_account.css" media="all"/>

<%
String successPage = "/your_account/customer_profile_summary.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
%>
<fd:CheckLoginStatus guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>
<%
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
    FDIdentity customerIdentity = null;
    ErpCustomerInfoModel customerInfo = null;
    if (user!=null && user.getLevel() == 2){
        customerIdentity = user.getIdentity();
        customerInfo = FDCustomerFactory.getErpCustomerInfo(customerIdentity);
    }
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    String department = request.getParameter("department");

%>
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Your Profile</tmpl:put>
<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="customer_profile"></fd:SEOMetaTag>
	</tmpl:put>
<tmpl:put name='content' direct='true'>
  <script type="text/javascript">
    function clearCustomerProfileForm(p) {
        var x = p;
        for(i=0; i<x.length; i++) {
            x[i].checked= false;
            x[i].disabled= false;
            x[i].selectedIndex=0;
        }
        return false;
    }
  </script>
  <fd:CustomerProfileSurveyTag actionName="submitSurvey" result="result" successPage="<%=successPage%>" survey="Customer Profile Survey">
    <div class="customer-profile-media">
      <fd:IncludeMedia name="/media/editorial/site_pages/survey/cps_intro.html" />
    </div>
    <form id="junk" name="request_product" method="POST">
      <div class="customer-profile-content text-left">
        <% request.setAttribute(FDSurveyConstants.SURVEY,EnumSurveyType.CUSTOMER_PROFILE_SURVEY.getLabel());%>
        <%@ include file="/includes/your_account/i_customer_profile.jspf" %>
      </div>
      <div class="customer-profile-actions text-left">
        <button type="submit" class="cssbutton orange">SUBMIT MY PROFILE</button>
        <button type="reset" onclick="clearCustomerProfileForm(document.request_product)" class="cssbutton green transparent">CLEAR FORM</button>
      </div>
    </form>
  </fd:CustomerProfileSurveyTag>
</tmpl:put>
</tmpl:insert>
