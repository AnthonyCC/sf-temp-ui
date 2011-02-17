<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.webapp.util.MediaHelper" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.webapp.util.FDFaqUtil" %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<fd:CheckLoginStatus guestAllowed="true" />
<%
	String faqPage = "faqHome";
	Map params = new HashMap();
	params.put("baseUrl", "");
	params.put("helper", new MediaHelper()); // include helper object. It allows to include media templates into template

	if (request.getParameter("show") != null) {
		faqPage = request.getParameter("show");
	}

	//set up template parameters
	params.put("isPage", Boolean.valueOf(
			request.getParameter("page") != null));
	params.put("isPopup", true);
%><%@ taglib uri='template' prefix='tmpl' 
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='bean' prefix='bean'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><fd:CheckLoginStatus /><%
	FDUserI user2 = (FDUserI) session.getAttribute(SessionName.USER);
	if (user2 != null) {
		params.put("fd_user", user2);
		params.put("minimumOrderAmount", new Integer((int) user2
				.getMinimumOrderAmount()));
		params.put("isUserCheckEligible", Boolean.valueOf(
				(user2 != null && user2.isCheckEligible())));
		params.put("customerServiceContact", user2
				.getCustomerServiceContact());
		params.put("baseDeliveryFee", new Double(user2
				.getBaseDeliveryFee()));
	} else {
		params.put("minimumOrderAmount", new Integer(0));
		params.put("isUserCheckEligible", Boolean.FALSE);
	}
	String faqSections = FDStoreProperties.getFaqSections();
	params.put("faqNodes", FDFaqUtil.getFaqsByCategory(faqPage));

	if (user2 != null ) {
		final java.text.DecimalFormat promoFormatter = new java.text.DecimalFormat(
				"$#,##0");
		params.put("eligibleForSignupPromotion", Boolean.TRUE);
		params.put("maxSignupPromotion", promoFormatter.format(user2
				.getMaxSignupPromotion()));
	} else {
		params.put("eligibleForSignupPromotion", Boolean.FALSE);
		params.put("maxSignupPromotion", "");
	}

	params.put("customerServiceContact", user2
			.getCustomerServiceContact());
	boolean flag = request.getRequestURI().toLowerCase().endsWith(
			"delivery_info_faq.jsp");
	params.put("deliveryInfoFaq", Boolean.valueOf(flag));
	params.put("careerLink", FDStoreProperties.getCareerLink());
	if (user2 != null) {
		params.put("corpDeliveryFee", new Double(user2
				.getCorpDeliveryFee()));
		params.put("corpDeliveryFeeMonday", new Double(user2
				.getCorpDeliveryFeeMonday()));
		params.put("minCorpOrderAmount", new Integer((int) user2
				.getMinCorpOrderAmount()));
	} else {
		params.put("corpDeliveryFee", new Double(0));
		params.put("corpDeliveryFeeMonday", new Double(0));
		params.put("minCorpOrderAmount", new Integer(0));
	}
	if (user2 != null) {
		params.put("isCorp", Boolean.valueOf(user2.isCorporateUser()));
		params.put("isDepotUser", Boolean.valueOf(user2.isDepotUser()));
	} else {
		params.put("isCorp", Boolean.FALSE);
		params.put("isDepotUser", Boolean.FALSE);
	}
	boolean isDefaultFtl=true;
	if (null != faqSections) {
		StringTokenizer st = new StringTokenizer(faqSections, ",");
		while (st.hasMoreTokens()) {
			String nextToken = st.nextToken().trim();
			params.put(nextToken, FDFaqUtil
					.getFaqsByCategory(nextToken));
			if(nextToken.equalsIgnoreCase(faqPage)&& isDefaultFtl){
				isDefaultFtl = false;
			}
		}
	}
	String ftl="/media/editorial/faq/"+faqPage+".ftl";
	String defaultFtl="/media/editorial/faq/intro.ftl";
%>

<%
FDUserI _user = (FDUserI)session.getAttribute(SessionName.USER);

String show = "intro";

boolean intro = false;
boolean about = false;
boolean signup = false;
boolean security = false;
boolean shopping = false;
boolean payment = false;
boolean delivery = false;
boolean delivery_depot = false;
boolean inside_fd = false;
boolean cos = false;

String title = "Introduction";

if (request.getParameter("show") != null && !"".equals(request.getParameter("show"))) {
	show = request.getParameter("show");

	if ("about".equalsIgnoreCase(show)) {
		about = true;
		title = "What We Do";
	} else if ("signup".equalsIgnoreCase(show)) {
		signup = true;
		title = "Signing Up";
	} else if ("security".equalsIgnoreCase(show)) {
		security = true;
		title = "Security &amp; Privacy";
	} else if ("shopping".equalsIgnoreCase(show)) {
		shopping = true;
		title = "Shopping";
	} else if ("payment".equalsIgnoreCase(show)) {
		payment = true;
		title = "Payment";
	} else if ("home_delivery".equalsIgnoreCase(show)) {
		delivery = true;
		title = "Home Delivery";
	} else if ("delivery_depot".equalsIgnoreCase(show)) {
		delivery_depot = true;
		title = "Depot Delivery";
	} else if ("inside_fd".equalsIgnoreCase(show)) {
		inside_fd = true;
		title = "Jobs & Corporate Info";
	} else if ("intro".equalsIgnoreCase(show)) {
		intro = true;
		title = "Introduction";
	} else if ("cos".equalsIgnoreCase(show)) {
		cos = true;
		title = "Corporate Services";
	} 
} else {
	intro = true;
	title = "Introduction";
}
%>

<tmpl:insert template='/shared/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - FAQ - <%=title%></tmpl:put>
		<tmpl:put name='content' direct='true'>
		<table width="520" cellpadding="0" cellspacing="0" border="0">
			<tr valign="top">
				<td align="right" class="text12">
				<a href="/help/faq_index.jsp?show=intro"><img src="/media_stat/images/template/help/fdqa_catnav.gif" width="118" height="56" border="0" alt="FreshDirect Q &amp; A"></a><br><br>
				<font class="space4pix"><br></font>
				
				<%  if(null != faqSections){
				  StringTokenizer st = new StringTokenizer(faqSections,",");
				  while (st.hasMoreTokens()) {
					String nextToken=st.nextToken().trim();
					ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(nextToken);
					if(null !=contentNode){	
				  	if(nextToken.equals(faqPage)){
								
				%><b><%= contentNode.getCmsAttributeValue("name") %></b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>
				<%}else{%><A HREF="faq_index.jsp?show=<%= contentNode.getContentKey().getId() %>" TARGET="_top"><%= contentNode.getCmsAttributeValue("name") %></A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>
				<%}}%>	
				<% }} %>				<br><br>
				</td>
				<td></td>
				<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td>
<!--				<%if(intro){%>-->
<!--					<jsp:include page="/help/intro.jsp"/>-->
<!--				<%}else if(about){%>-->
<!--					<jsp:include page="/help/about_freshdirect.jsp"/>	-->
<!--				<%}else if(signup){%>-->
<!--					<jsp:include page="/help/signing_up.jsp"/>-->
<!--				<%}else if(security){%>-->
<!--					<jsp:include page="/help/security.jsp"/>-->
<!--				<%}else if(shopping){%>-->
<!--					<jsp:include page="/help/shopping.jsp"/>-->
<!--				<%}else if(payment){%>-->
<!--					<jsp:include page="/help/payment.jsp"/>-->
<!--				<%}else if(delivery){%>-->
<!--					<fd:IncludeMedia name="/media/editorial/faq/home_delivery.ftl" parameters="<%=params%>" withErrorReport="true"/>-->
					<jsp:include page="/help/delivery_home.jsp"/>
<!--				<%}else if(delivery_depot){%>-->
<!--					<jsp:include page="/help/delivery_depot.jsp"/>				-->
<!--				<%}else if(inside_fd){%>-->
<!--					<jsp:include page="/help/inside_fd.jsp"/>		-->
<!--				<%}else if(cos){%>-->
<!--                    <jsp:include page="/help/cos.jsp"/>		-->
<!--                <%}%>-->
<% if(!isDefaultFtl){ %>
			<fd:IncludeMedia name="<%= ftl%>" parameters="<%=params%>" withErrorReport="true"/>
			<% } else { %>
			<fd:IncludeMedia name="<%= defaultFtl%>" parameters="<%=params%>" withErrorReport="true"/>
			<% } %>
				</td>
			</tr>
			<tr>
				<td><img src="/media_stat/images/layout/clear.gif" width="125" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="6" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td><img src="/media_stat/images/layout/clear.gif" width="388" height="1"></td>
			</tr>
		</table>

	</tmpl:put>
</tmpl:insert>
