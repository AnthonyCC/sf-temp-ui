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
	String moreInfoPage = "site_access.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=moreInfo";
	//String failurePage = "delivery.jsp?successPage="+ URLEncoder.encode(successPage)+"&serviceType="+serviceType;	
	String failurePage = "site_access.jsp?successPage="+ URLEncoder.encode(successPage)+"&ol=na&serviceType="+serviceType;

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
request.setAttribute("sitePage", "site_access");
request.setAttribute("listPos", "CategoryNote");
    
	//String actionURI = "/site_access/site_access.jsp";
	String actionURI = request.getRequestURI()+"?siteAccessPage="+siteAccessPage+"&successPage="+successPage;

	//if (request.getParameter("newRequest") == null) {
	//	response.sendRedirect(response.encodeRedirectURL("/site_access/site_access_lite.jsp?successPage="+successPage));
	//}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>FreshDirect</title>

		<%@ include file="/common/template/includes/metatags.jspf" %>
		<meta http-equiv="X-UA-Compatible" content="IE=8">

		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<script src="/assets/javascript/swfobject.js" type="text/javascript"></script>
		
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	</head>
	<body bgcolor="#ffffff" text="#333333" class="text11" marginwidth="0" marginheight="20" leftmargin="0" topmargin="20">
	
		
		<jsp:include page="/shared/template/includes/server_info.jsp" flush="false"/>
		<jsp:include page="/common/template/includes/ad_server.jsp" flush="false"/>
		
		<fd:SiteAccessController action='checkByZipCode' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' result='result'>
			<fd:IncludeMedia name="/media/editorial/site_access/site_access.html" />

			<%--
				Put any java-related variables needed by the page into the _page_options object. 
			--%>
			<script type="text/javascript">
			<!--
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
							_page_options.overlayURL = '<%= "site_access_address.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=CORPORATE" %>';
						<% }else{ %>
							_page_options.overlayURL = '<%= "site_access_address.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=HOME" %>';
						<% } %>
					<% } %>
					<% if ( "altCorp".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "/site_access/alt_dlv_corporate.jsp" %>';
					<% } %>
					<% if ( "altHome".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "/site_access/alt_dlv_home.jsp" %>';
					<% } %>
					<% if ( "na".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "delivery.jsp?successPage=" + URLEncoder.encode(successPage) + "&serviceType=" + serviceType %>';
					<% } %>
					<% if ( "corpSurvey".equalsIgnoreCase(overlayType) ) { %>
						_page_options.overlayURL = '<%= "/survey/cos_site_access_survey.jsp?successPage=" + URLEncoder.encode(successPage) %>';
					<% } %>
				<% } %>
			//-->
			</script>

		
			<%@ include file="/includes/net_insight/i_tag_footer.jspf" %>
		</fd:SiteAccessController>
	</body>
</html>