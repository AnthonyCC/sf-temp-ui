<%@ page import='java.util.*'  %>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%@ page buffer="16kb" %>
<%
	response.addHeader("Pragma", "no-cache");
	boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	String successPage = NVL.apply(request.getParameter("successPage"), "");
	String zipcode = NVL.apply(request.getParameter("zipcode"), "");
    String serviceType=NVL.apply(request.getParameter("serviceType"), "");
    String corpZipcode = NVL.apply(request.getParameter("corpZipcode"), "");
    String corpServiceType=NVL.apply(request.getParameter("corpServiceType"), "");
	boolean isCorporate = "corporate".equalsIgnoreCase(serviceType);
	/* get overlay type passed */
	String overlayType=NVL.apply(request.getParameter("ol"), "");
	String siteAccessPage = NVL.apply(request.getParameter("siteAccessPage"), "");

    
    if (successPage == null || successPage == "") {
  		// null, default to index.jsp
  		successPage = "/index.jsp";
 	}
    
    
    //EnumServiceType.CORPORATE.getName().equalsIgnoreCase(corpServiceType)
    if (successPage.startsWith("/index.jsp") && corpZipcode!=null && corpZipcode.length()==5)  {
		successPage = "/department.jsp?deptId=COS";
	}
 

	String refProStr="ref_prog_id=";
	if(successPage.indexOf(refProStr)!=-1)
	{
		String refProgId=successPage.substring(successPage.indexOf(refProStr)+refProStr.length(),successPage.indexOf(refProStr)+refProStr.length()+successPage.substring(successPage.indexOf(refProStr)+refProStr.length(),successPage.length()).indexOf("&"));        
		request.setAttribute("RefProgId",refProgId);
	}

	//String moreInfoPage = "site_access_address.jsp?successPage="+ URLEncoder.encode(successPage);
	/* moreInfo, redirect back to the same page, and pass in the overlayType */
	String moreInfoPage = "site_access.jsp?ol=moreInfo&successPage="+ URLEncoder.encode(successPage);
	//String failurePage = "delivery.jsp?successPage="+ URLEncoder.encode(successPage)+"&serviceType="+serviceType;	
	String failurePage = "site_access.jsp?ol=na&serviceType="+serviceType+"&successPage="+ URLEncoder.encode(successPage);

	//check for new serviceType, and if either GC or RH is enabled
	String gcLanding = FDStoreProperties.getGiftCardLandingUrl();
	String rhLanding = FDStoreProperties.getRobinHoodLandingUrl();
	boolean isGiftCardEnabled = FDStoreProperties.isGiftCardEnabled();
	boolean isRobinHoodEnabled = FDStoreProperties.isRobinHoodEnabled();

	/* service type is "WEB" AND either of GC/RH is enabled check successPage */
    if ( EnumServiceType.WEB.getName().equalsIgnoreCase(serviceType)) {
        String successPageFinal="";
		if (successPage.indexOf(rhLanding)>-1){
            //Successpage is robinhood
            if(isRobinHoodEnabled)
				successPageFinal = rhLanding;
            else
                successPageFinal = "/index.jsp";
		} else	if (successPage.indexOf(gcLanding)>-1){
            //Successpage is giftcard  
            if(isGiftCardEnabled)
				successPageFinal = gcLanding;
            else
                successPageFinal = "/index.jsp";
		} 
		if ("".equalsIgnoreCase(successPageFinal)) {
			//success page has not been set, default to giftcard
            if(isGiftCardEnabled)
				successPageFinal = gcLanding;
            else
                successPageFinal = "/index.jsp";
		}
        //Set the success page
        successPage = successPageFinal;
	
		//moreInfoPage = successPageFinal;
		//failurePage = successPageFinal;
	}
	
    
    //--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/site_access/site_access.jsp");
request.setAttribute("listPos", "CategoryNote,SiteAccess,DeliveryFees");
    
	//String actionURI = "/site_access/site_access.jsp";
	String actionURI = request.getRequestURI()+"?siteAccessPage="+siteAccessPage+"&successPage="+successPage;

	//if (request.getParameter("newRequest") == null) {
	//	response.sendRedirect(response.encodeRedirectURL("/site_access/site_access_lite.jsp?successPage="+successPage));
	//}
%>
<fd:SiteAccessController action='checkByZipCode' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' result='result'>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
		"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html>
		<head>
			<title>FreshDirect</title>
	
			<%-- <%@ include file="/common/template/includes/metatags.jspf" %> --%>
			<tmpl:get name="seoMetaTag"/>
			<meta name="msvalidate.01" content="2E163086C8383686A98EE1B694357FE7" />
	
			<%@ include file="/common/template/includes/i_javascripts.jspf" %>
			<fd:javascript src="/assets/javascript/swfobject.js" />
			
			<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
		<%@ include file="/shared/template/includes/i_head_end.jspf" %>
	</head>
		<body bgcolor="#ffffff" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
		<%@ include file="/shared/template/includes/i_body_start.jspf" %>
			
			
			<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
			<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
			
			
				<% if ( request.getParameter("lang") != null) { %>
					<% if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) { %> 
						<fd:IncludeMedia name="/media/editorial/site_access/site_access_espanol.html" />
					<% } else { %>
						<fd:IncludeMedia name="/media/editorial/site_access/site_access.html" />
					<% } %>
				<% } else { %>
					<fd:IncludeMedia name="/media/editorial/site_access/site_access.html" />
				<% } %>
	
				<%--
					Put any java-related variables needed by the page into the _page_options object. 
				--%>
				<script type="text/javascript">
					var _page_options = {
						gc: {
							isEnabled: <%=FDStoreProperties.isGiftCardEnabled()%>,
							landingPage: '<%=FDStoreProperties.getGiftCardLandingUrl()%>'
						},
						rh: {
							isEnabled: <%=FDStoreProperties.isRobinHoodEnabled()%>,
							landingPage: '<%=FDStoreProperties.getRobinHoodLandingUrl()%>'
						},
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
						zip: {
							home: '<%= zipcode %>',
							cos: '<%= corpZipcode %>',
							web: '<%= zipcode %>'
						},
						actionURI: '<%= request.getRequestURI()+"?siteAccessPage="+siteAccessPage+"&successPage="+successPage %>',
						loginPage: '<%= "/login/login_main.jsp?successPage=" + URLEncoder.encode(successPage) %>',
						serviceType: {
							serviceType: '',
							corpServiceType: ''
						}
					};
					<% if ( !"WEB".equals(serviceType) ) { %>
						<% if ( result.hasError("technicalDifficulty") ) { %>
							_page_options.errMsg = "<%=result.getError("technicalDifficulty").getDescription() %>";
						<% } else if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
							_page_options.errMsg = "<%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %>";
						<%}%>
					<% } %>
					<% if ( "WEB".equals(serviceType) ) { %>
						<% if ( result.hasError("technicalDifficulty") ) { %>
							_page_options.errMsg_web = "<%=result.getError("technicalDifficulty").getDescription() %>";
						<% } else if ( result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode()) ) { %>
							_page_options.errMsg_web = "<%=result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() %>";
						<%}%>
					<% } %>
					/*	check if an overlayType was pased in, and put it into the js object */
					<% if ( !"".equals(overlayType) ) { %>
						<% if ( "moreInfo".equalsIgnoreCase(overlayType) ) { %>
							<% if ( isCorporate ) { %>
								_page_options.overlayURL = '<%= "site_access_address.jsp?serviceType=CORPORATE&successPage=" + URLEncoder.encode(successPage) %>';
							<% }else{ %>
								_page_options.overlayURL = '<%= "site_access_address.jsp?serviceType=HOME&successPage=" + URLEncoder.encode(successPage) %>';
							<% } %>
						<% } %>
						<% if ( "altCorp".equalsIgnoreCase(overlayType) ) { %>
							_page_options.overlayURL = '<%= "/site_access/alt_dlv_corporate.jsp" %>';
						<% } %>
						<% if ( "altHome".equalsIgnoreCase(overlayType) ) { %>
							_page_options.overlayURL = '<%= "/site_access/alt_dlv_home.jsp" %>';
						<% } %>
						<% if ( "na".equalsIgnoreCase(overlayType) ) { %>
							_page_options.overlayURL = '<%= "delivery.jsp?serviceType=" + serviceType + "&successPage=" + URLEncoder.encode(successPage) %>';
						<% } %>
						<% if ( "corpSurvey".equalsIgnoreCase(overlayType) ) { %>
							_page_options.overlayURL = '<%= "/survey/cos_site_access_survey.jsp?successPage=" + URLEncoder.encode(successPage) %>';
						<% } %>
					<% } %>
				</script>
		</body>
	</html>
</fd:SiteAccessController>
