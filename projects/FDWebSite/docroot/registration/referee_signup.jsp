<%@ page import='java.util.*'  %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='com.freshdirect.fdstore.sempixel.FDSemPixelCache' %>
<%@ page import='com.freshdirect.fdstore.sempixel.SemPixelModel' %>
<%@ page import="com.freshdirect.fdstore.referral.FDReferralManager"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%
	System.out.println("*********************"+request.getRequestURI());
	String referralurl = request.getRequestURI();
	String referral = null;
	if(referralurl.indexOf("invite") != -1) {
		referral = referralurl.substring(8);
		pageContext.getSession().setAttribute("REFERRALNAME",referral);
	}
	if(pageContext.getSession().getAttribute("REFERRALNAME") != null) {
		if(referral == null)
			referral = (String) pageContext.getSession().getAttribute("REFERRALNAME");
	}
	String refname = FDReferralManager.getReferralName(referral);	
	response.addHeader("Pragma", "no-cache"); 
	String successPage = NVL.apply(request.getParameter("successPage"), "");
	String zipcode = NVL.apply(request.getParameter("zipcode"), "");
    String serviceType=NVL.apply(request.getParameter("serviceType"), "");
    String corpZipcode = NVL.apply(request.getParameter("corpZipcode"), "");
    String corpServiceType=NVL.apply(request.getParameter("corpServiceType"), "");
	boolean isCorporate = "corporate".equalsIgnoreCase(serviceType);
	/* get overlay type passed */
	String overlayType=NVL.apply(request.getParameter("ol"), "");
	String siteAccessPage = NVL.apply(request.getParameter("siteAccessPage"), "");
	String email = NVL.apply(request.getParameter("email"), "");
	String email_error = NVL.apply(request.getParameter("email_error"), "");

    
    if (successPage == null || successPage == "") {
  		// null, default to index.jsp
  		successPage = "/index.jsp";
 	}
    
    
    //EnumServiceType.CORPORATE.getName().equalsIgnoreCase(corpServiceType)
    if (successPage.startsWith("/index.jsp") && corpZipcode!=null && corpZipcode.length()==5)  {
		successPage = "/department.jsp?deptId=COS";
	}

	/* moreInfo, redirect back to the same page, and pass in the overlayType */
	 String moreInfoPage = "/registration/referee_signup.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=moreInfo";
        String failurePage =  "/registration/referee_signup.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;
    
    //--------OAS Page Variables-----------------------
	request.setAttribute("sitePage", "www.freshdirect.com/site_access/site_access.jsp");
	request.setAttribute("listPos", "CategoryNote,SiteAccess");
    
	String actionURI = request.getRequestURI()+"?siteAccessPage="+siteAccessPage+"&successPage="+successPage;
	String url = request.getRequestURI();
	System.out.println("[******moreInfoPage*****]" + moreInfoPage);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
		<title>FreshDirect</title>

		<%@ include file="/common/template/includes/metatags.jspf" %>
		<meta name="msvalidate.01" content="2E163086C8383686A98EE1B694357FE7" />

		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<fd:javascript src="/assets/javascript/swfobject.js" />
		
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	</head>
	<body bgcolor="#ffffff" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">		
		
		<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
		<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
		
		<% if(refname != null) { %>
		<fd:SiteAccessController action='checkByZipCode' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' result='result'>
			<%--
				Put any java-related variables needed by the page into the _page_options object. 
			--%>
			<script type="text/javascript">
			<!--
				var _page_options = {					
					enums: {
						WEB: '<%=EnumServiceType.WEB.getName()%>',
						HOME: '<%=EnumServiceType.HOME.getName()%>',
						CORPORATE: '<%=EnumServiceType.CORPORATE.getName()%>',
						DLV_ZIPCODE: '<%=EnumUserInfoName.DLV_ZIPCODE.getCode()%>',
						DLV_CORP_ZIPCODE: '<%=EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()%>'
					},
					overlayURL: '',
					successPage: '<%=successPage%>',
					siteAccessPage: '<%=URLEncoder.encode(siteAccessPage)%>',
					errMsg: '',
					errMsg_web: '',
					errMsg_email: '',
					errMsg_email_web: '',
					zip: {
						home: '<%= zipcode %>',
						cos: '<%= corpZipcode %>',
						web: '<%= zipcode %>'
					},
					email: '<%= email %>',
					actionURI: '<%= actionURI %>',
					loginPage: '<%= "/login/login_main.jsp?successPage=" + URLEncoder.encode(successPage) %>',
					invitename: '<%= url %>',
					refName: '<%=refname%>',
					serviceType: {
						serviceType: '',
						corpServiceType: ''
					}
				};
				<% if ( !"WEB".equals(serviceType) ) { %>
					<% if ( result.hasError("technicalDifficulty") ) { %>
						_page_options.errMsg = "<%=result.getError("technicalDifficulty").getDescription() %>";
					<% } else {
						if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
							_page_options.errMsg = "<%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %>";
						<% }
						if(result.hasError(EnumUserInfoName.EMAIL.getCode()) ) { %>
							_page_options.errMsg_email = "<%=result.getError(EnumUserInfoName.EMAIL.getCode()).getDescription() %>";
						<%}%>
					<%}%>
				<% } %>
				<% if ( "WEB".equals(serviceType) ) { %>
					<% if ( result.hasError("technicalDifficulty") ) { %>
						_page_options.errMsg_web = "<%=result.getError("technicalDifficulty").getDescription() %>";
					<% } else {
						if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
							_page_options.errMsg_web = "<%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %>";
						<% }
						if(result.hasError(EnumUserInfoName.EMAIL.getCode()) ) { %>
							_page_options.errMsg_email_web = "<%=result.getError(EnumUserInfoName.EMAIL.getCode()).getDescription() %>";
						<%}%>
					<%}%>
				<% } %>
				/*	check if an overlayType was pased in, and put it into the js object */
				<% if ( !"".equals(overlayType) && !"true".equals(email_error)) { %>
					<% if ( "moreInfo".equalsIgnoreCase(overlayType) ) { System.out.println("overlay is more infoo......................");%>						
						<% if ( isCorporate ) { %>
							_page_options.overlayURL = '<%=  "referee_signup2.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=CORPORATE" %>';
						<% }else{ %>
							_page_options.overlayURL = '<%=  "referee_signup2.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=HOME" %>';
						<% } %>
					<% } %>
					<% if ( "partialmoreInfo".equalsIgnoreCase(overlayType) ) { %>
						<% if ( isCorporate ) { %>
							_page_options.overlayURL = '<%= "referee_address.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=CORPORATE" %>';
						<% }else{ %>
							_page_options.overlayURL = '<%= "referee_address.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=HOME" %>';
						<% } %>
					<% } %>
					<% if ( "altCorp".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "/site_access/alt_dlv_corporate.jsp" %>';
					<% } %>
					<% if ( "altHome".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "/site_access/alt_dlv_home.jsp" %>';
					<% } %>
					<% if ( "na".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "/site_access/delivery.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=" + serviceType %>';
					<% } %>
					<% if ( "corpSurvey".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "/survey/cos_site_access_survey.jsp?successPage=" + URLEncoder.encode(successPage) %>';
					<% } %>
				<% } %>
				
			//-->
			</script>
			
			<fd:IncludeMedia name="/media/editorial/site_access/referral_site_access.html" />

		
			<%@ include file="/includes/net_insight/i_tag_footer.jspf" %>			

		</fd:SiteAccessController>
		<% }  else { 
			response.sendRedirect("/index.jsp");
			}
		%>
		
	</body>
</html>