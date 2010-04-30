<%@ page import="com.freshdirect.webapp.util.MediaHelper" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import="com.freshdirect.webapp.util.FDFaqUtil" %>
<%
String faqPage = "faqHome";
Map params = new HashMap();
params.put("baseUrl", "");
params.put("helper", new MediaHelper()); // include helper object. It allows to include media templates into template

if (request.getParameter("page")!= null){
    faqPage = request.getParameter("page");
}
params.put("isPopup", true);

//set up template parameters
params.put("isPage", new Boolean(request.getParameter("page") != null) );

%><%@ taglib uri='template' prefix='tmpl' 
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='bean' prefix='bean'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><fd:CheckLoginStatus /><%
FDUserI user2 = (FDUserI)session.getAttribute(SessionName.USER);
if (user2 != null) {
    params.put("fd_user", user2);
	params.put("minimumOrderAmount", new Integer((int)user2.getMinimumOrderAmount()) );
	params.put("isUserCheckEligible", new Boolean((user2 != null && user2.isCheckEligible())) );
	params.put("customerServiceContact", user2.getCustomerServiceContact() );
	params.put("baseDeliveryFee", new Double(user2.getBaseDeliveryFee()) );
} else {
    params.put("minimumOrderAmount", new Integer(0) );
    params.put("isUserCheckEligible", Boolean.FALSE );
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
params.put("deliveryInfoFaq", new Boolean(flag));
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
	params.put("isCorp", new Boolean(user2.isCorporateUser()));
	params.put("isDepotUser", new Boolean(user2.isDepotUser()));
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

%><tmpl:insert template='/shared/template/large_pop.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Help - FAQs</tmpl:put>
	<tmpl:put name='leftnav' direct='true'>
	</tmpl:put>
    
    <tmpl:put name='content' direct='true'>
		<TABLE width="520">
		    <TR VALIGN="TOP">
			   
			    <TD align="right" class="text12"> 
					<A HREF="faq_home_pop.jsp?page=faqHome" TARGET="_top"><img src="/media_stat/images/template/help/fdqa_catnav.gif" width="118" height="56" alt="FreshDirect Q &amp; A" border="0"></A><BR>
				<font class="space2pix"></font><br/>
			
			<%  if(null != faqSections){
				  StringTokenizer st = new StringTokenizer(faqSections,",");
				  while (st.hasMoreTokens()) {
					String nextToken=st.nextToken().trim();
					ContentNodeModel contentNode = ContentFactory.getInstance().getContentNode(nextToken);
					if(null !=contentNode){	
				  	if(nextToken.equals(faqPage)){
								
				%><b><%= contentNode.getCmsAttributeValue("name") %></b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>
				<%}else{%><A HREF="faq_home_pop.jsp?page=<%= contentNode.getContentKey().getId() %>" TARGET="_top"><%= contentNode.getCmsAttributeValue("name") %></A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>
				<%}}%>	
				<% }} %>
<!--				<%if(faqPage.equals("about")){%><b>What We Do</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=about" TARGET="_top">What We Do</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>	-->
<!--			-->
<!--				<%if(faqPage.equals("signup")){%><b>Signing Up</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=signup" TARGET="_top">Signing Up</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>	-->
<!--			-->
<!--				<% if(faqPage.equals("security")){%><b>Security &amp; Privacy</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=security" TARGET="_top">Security &amp; Privacy</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>	-->
<!--				-->
<!--				<%if(faqPage.equals("shopping")){%><b>Shopping</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=shopping" TARGET="_top">Shopping</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>-->
<!--				-->
<!--				<%if(faqPage.equals("payment")){%><b>Payment</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=payment" TARGET="_top">Payment</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>	-->
<!--                <%if(faqPage.equals("deliveryHome")){%><b>Home Delivery</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=deliveryHome" TARGET="_top">Home Delivery</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>	-->
<!--				<%if(faqPage.equals("cos")){%><b>Corporate Delivery</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=cos" TARGET="_top">Corporate Delivery</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>-->
<!---->
<!--				<%if(faqPage.equals("chefstable")){%><b>Chef's Table</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=chefstable" TARGET="_top">Chef's Table</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>	-->
<!--				-->
<!--				<%if(faqPage.equals("inside")){%><b>Jobs &amp; Corporate Info</b><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}else{%><A HREF="faq_home_pop.jsp?page=inside" TARGET="_top">Jobs &amp; Corporate Info</A><br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0"><br>-->
<!--				<%}%>	-->
			</TD>
			
			<TD>
						
				<table>
				<tr>
					<TD bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" height="0"></TD>
					<td>
					<% if(!isDefaultFtl){ %>
			<fd:IncludeMedia name="<%= ftl%>" parameters="<%=params%>" withErrorReport="true"/>
			<% } else { %>
			<fd:IncludeMedia name="<%= defaultFtl%>" parameters="<%=params%>" withErrorReport="true"/>
			<% } %>
					</td>
				</TR>
				</table>
			</TD>
		</TR>
		
		</TABLE>
	
	</tmpl:put>
</tmpl:insert>
