<%@ page import='java.util.*' %>
<%@ page import="java.net.*"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%
	System.out.println("IN REFEREE_ADDRESS.jsp==========================================================================================");
    String successPage = NVL.apply(request.getParameter("successPage"), "").trim();
	String serviceType = NVL.apply(request.getParameter("serviceType"), "").trim();
	String corpServiceType = NVL.apply(request.getParameter("corpServiceType"), "").trim();
	/* obsolete, removed from media and page title
		boolean isBestCellars = request.getServerName().toLowerCase().indexOf("bestcellars") > -1;
	*/
	boolean isCorporate = "corporate".equalsIgnoreCase(serviceType);
	/* will we ever end up using the moreInfo from here? */
	String moreInfoPage = "/registration/referee_signup.jsp?ol=moreInfo&successPage="+ URLEncoder.encode(successPage);
    String failurePage = "/site_access/delivery.jsp?successPage="+ URLEncoder.encode(successPage)+"&serviceType="+serviceType;
	String failureCorporatePage	= "/survey/cos_site_access_survey.jsp?successPage="+ URLEncoder.encode(successPage);
	
	String action = "checkByAddress";
	if(session.getAttribute("DISPLAY") != null) {
		action = "doNothing";
	}
%>
<fd:SiteAccessController action='checkByAddress' successPage='<%= successPage %>' moreInfoPage='<%= moreInfoPage %>' failureHomePage='<%= failurePage %>' failureCorporatePage='<%= failureCorporatePage %>' result='result'>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%-- <title>FreshDirect Address Check</title> --%><% /* if this title changes, you need to change the media JS as well */ %>
	 <fd:SEOMetaTag title="FreshDirect Address Check"/>
	<% if("slite".equals(request.getParameter("referrer_page"))) { %>
		<%@ include file="/common/template/includes/metatags.jspf" %>
		<%@ include file="/common/template/includes/i_javascripts.jspf" %>
		<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>
	<% } %>
	<%@ include file="/shared/template/includes/i_head_end.jspf" %>
</head>
<body>
	<%@ include file="/shared/template/includes/i_body_start.jspf" %>	
		<% if(session.getAttribute("DISPLAY") != null || "doNothing".equals(action)) { %>
			<% if(!FDStoreProperties.isExtoleRafEnabled()) { %>
				<jsp:include page="/registration/referee_signup2.jsp" flush="false"/> 
			<% } else { %>
	<%-- 				<jsp:include page="/registration/referee_signup2.jsp" flush="false"/> --%>
						<jsp:include page="/registration/invite_signup2.jsp" flush="false"/>
			<% } %>
		<% } else {
			
			String fldAddress1 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_1.getCode()), "");
			String fldAddress2 = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ADDRESS_2.getCode()), "");
			String fldApartment = NVL.apply(request.getParameter(EnumUserInfoName.DLV_APARTMENT.getCode()), "");
			String fldCity = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CITY.getCode()), "");
			String fldState = NVL.apply(request.getParameter(EnumUserInfoName.DLV_STATE.getCode()), "");
			String fldZipCode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_ZIPCODE.getCode()), "");

			if ("".equals(fldZipCode) && isCorporate) {
				fldZipCode = NVL.apply(request.getParameter(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()), "");
			}
	
			if ("".equals(fldZipCode)) {
				FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
				if (user != null) {
					fldZipCode = NVL.apply(user.getZipCode(), "");
				}
			}
			String zipErrMsg = (isCorporate)
									? (result.hasError(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode())) 
										? result.getError(EnumUserInfoName.DLV_CORP_ZIPCODE.getCode()).getDescription() 
										: "" 
									: (result.hasError(EnumUserInfoName.DLV_ZIPCODE.getCode())) 
										? result.getError(EnumUserInfoName.DLV_ZIPCODE.getCode()).getDescription() 
										: "";
		%>
			<%--
				re-use the options object from site access (if it exists, otherwise just create it)
				Put any java-related variables needed by the page into the _page_options object. 
				all the new vars here go in to a sub-variable, so we don't override pre-existing vars
			--%>
			<script type="text/javascript">
	
				var _page_options = $jq.extend(true, _page_options||{}, {
					saAddress: {
						fldAddress: {
							fldAddress1: {
								name: '<%=EnumUserInfoName.DLV_ADDRESS_1.getCode()%>',
								value: '<%= fldAddress1 %>',
								errMsg: '<%= StringEscapeUtils.escapeJavaScript( (result.hasError(EnumUserInfoName.DLV_ADDRESS_1.getCode())) ? result.getError(EnumUserInfoName.DLV_ADDRESS_1.getCode()).getDescription() : "" ) %>'
							},
							fldAddress2: {
								name: '<%=EnumUserInfoName.DLV_ADDRESS_2.getCode()%>',
								value: '<%= fldAddress2 %>',
								errMsg: '<%= StringEscapeUtils.escapeJavaScript( (result.hasError(EnumUserInfoName.DLV_ADDRESS_2.getCode())) ? result.getError(EnumUserInfoName.DLV_ADDRESS_2.getCode()).getDescription() : "" ) %>'
							},
							fldApartment: {
								name: '<%=EnumUserInfoName.DLV_APARTMENT.getCode()%>',
								value: '<%= fldApartment %>',
								errMsg: '<%= StringEscapeUtils.escapeJavaScript( (result.hasError(EnumUserInfoName.DLV_APARTMENT.getCode())) ? result.getError(EnumUserInfoName.DLV_APARTMENT.getCode()).getDescription() : "" ) %>'
							},
							fldCity: {
								name: '<%=EnumUserInfoName.DLV_CITY.getCode()%>',
								value: '<%= fldCity %>',
								errMsg: '<%= StringEscapeUtils.escapeJavaScript( (result.hasError(EnumUserInfoName.DLV_CITY.getCode())) ? result.getError(EnumUserInfoName.DLV_CITY.getCode()).getDescription() : "" ) %>'
							},
							fldState: {
								name: '<%=EnumUserInfoName.DLV_STATE.getCode()%>',
								value: '<%= fldState %>',
								errMsg: '<%= StringEscapeUtils.escapeJavaScript( (result.hasError(EnumUserInfoName.DLV_STATE.getCode())) ? result.getError(EnumUserInfoName.DLV_STATE.getCode()).getDescription() : "" ) %>'
							},
							fldZipCode: {
								name: '<%= (isCorporate) ? EnumUserInfoName.DLV_CORP_ZIPCODE.getCode() : EnumUserInfoName.DLV_ZIPCODE.getCode() %>',
								value: '<%= fldZipCode %>',
								errMsg: '<%= StringEscapeUtils.escapeJavaScript(zipErrMsg) %>'
							}
						},
						actionURI: '<%= request.getRequestURI() %>',
						actionURIAdd: '<%= response.encodeURL("/site_access/delivery.jsp?successPage=") +URLEncoder.encode(successPage) %>',
						successPage: '<%= StringEscapeUtils.escapeJavaScript( successPage ) %>',
						serviceType: '<%= serviceType %>',
						corpServiceType: '<%= corpServiceType %>',
						isCorporate: <%= isCorporate %>,
						moreInfoPage: '<%= moreInfoPage %>',
						failurePage: '<%= failurePage %>',
						failureCorporatePage: '<%= failureCorporatePage %>',
						errMsgs: {
							cantGeocode: '<%= StringEscapeUtils.escapeJavaScript((result.hasError("cantGeocode")) ? result.getError("cantGeocode").getDescription() : "") %>',
							suggestedAddresses: '<%= (result.hasError(EnumUserInfoName.DLV_ADDRESS_SUGGEST.getCode())) ? EnumUserInfoName.DLV_ADDRESS_SUGGEST.getCode() : "" %>',
							suggestedAddressesURL: '/shared/includes/messages/i_error_suggested_address.jsp'
						},
						raf: true
					}
				});
			</script>
			<div id="saAddress_suggestedAddresses_hidden" style="display: none;">
				<% if (result.hasError(EnumUserInfoName.DLV_ADDRESS_SUGGEST.getCode())) { %><%@ include file="/shared/includes/messages/i_error_suggested_address.jspf" %><% } %></div>
			<fd:IncludeMedia name="/media/editorial/site_access/zipfail/site_access_address.html" />
		<% } %>
</body>
</html>
</fd:SiteAccessController>