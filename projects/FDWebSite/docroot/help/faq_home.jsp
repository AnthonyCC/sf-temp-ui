<%@ page import="com.freshdirect.webapp.util.MediaHelper" %><%
String faqPage = "faqHome";
Map params = new HashMap();
params.put("baseUrl", "");
params.put("helper", new MediaHelper()); // include helper object. It allows to include media templates into template

if (request.getParameter("page")!= null){
    faqPage = request.getParameter("page");
}

//set up template parameters
params.put("isPage", new Boolean(request.getParameter("page") != null) );
params.put("isPageNull", new Boolean(request.getParameter("page") == null) );
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
%><tmpl:insert template='/common/template/faq_help.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Help - FAQs</tmpl:put>
	<tmpl:put name='leftnav' direct='true'>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<table border="0" cellpadding="0" cellspacing="0" width=570>
		<tr>
			<td valign="top">
				<img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0">
			</td>
			<td><%
			if(faqPage.equals("about")){%>
				<fd:IncludeMedia name="/media/editorial/faq/about.ftl" parameters="<%=params%>" withErrorReport="true"/>	
			<%}
			
			else if(faqPage.equals("signup")){
				if (user2 != null && user2.isEligibleForSignupPromotion()) {
                    final java.text.DecimalFormat promoFormatter = new java.text.DecimalFormat("$#,##0");
					params.put("eligibleForSignupPromotion", Boolean.TRUE);
					params.put("maxSignupPromotion", promoFormatter.format(user2.getMaxSignupPromotion()) );
				} else {
                    params.put("eligibleForSignupPromotion", Boolean.FALSE);
                    params.put("maxSignupPromotion", "" );
				}
			%>
				<fd:IncludeMedia name="/media/editorial/faq/signup.ftl" parameters="<%=params%>" withErrorReport="true"/>
			<%}else if(faqPage.equals("security")){%>
				<fd:IncludeMedia name="/media/editorial/faq/security.ftl" parameters="<%=params%>" withErrorReport="true"/>
			<%}else if(faqPage.equals("shopping")){
                params.put("customerServiceContact", user2.getCustomerServiceContact() );
			%>
				<!-- leave as-is --><%@ include file="/help/shopping.jsp"%>
			<%}else if(faqPage.equals("payment")){%>
				<fd:IncludeMedia name="/media/editorial/faq/payment.ftl" parameters="<%=params%>" withErrorReport="true"/>
			<%}else if(faqPage.equals("deliveryHome")){
                boolean flag = request.getRequestURI().toLowerCase().endsWith("delivery_info_faq.jsp");
                params.put("deliveryInfoFaq", new Boolean(flag) );
			%>
				<fd:IncludeMedia name="/media/editorial/faq/delivery_home.ftl" parameters="<%=params%>" withErrorReport="true"/>
			<%}else if(faqPage.equals("deliveryDepot")){%>
				<!-- leave as-is --><%@ include file="/help/delivery_depot.jsp"%>				
			<%}else if(faqPage.equals("inside")){
				params.put("careerLink", FDStoreProperties.getCareerLink());
			%><fd:IncludeMedia name="/media/editorial/faq/inside.ftl" parameters="<%=params%>" withErrorReport="true"/>
            <%}else if(faqPage.equals("cos")){
            	boolean flag = request.getRequestURI().toLowerCase().endsWith("delivery_info_faq.jsp");
            	params.put("deliveryInfoFaq", new Boolean(flag) );
            	if (user2 != null) {
	            	params.put("corpDeliveryFee", new Double(user2.getCorpDeliveryFee()) );
	            	params.put("corpDeliveryFeeMonday", new Double(user2.getCorpDeliveryFeeMonday()) );
	                params.put("minCorpOrderAmount", new Integer((int)user2.getMinCorpOrderAmount()) );
            	} else {
                    params.put("corpDeliveryFee", new Double(0) );
                    params.put("corpDeliveryFeeMonday", new Double(0) );
                    params.put("minCorpOrderAmount", new Integer(0) );
            	}
            %><fd:IncludeMedia name="/media/editorial/faq/cos.ftl" parameters="<%=params%>" withErrorReport="true"/>
			<%}else{ // page = faqHome or other not handled in any conditions above
				if (user2 != null) {
				    params.put("isCorp", new Boolean(user2.isCorporateUser()) );
				    params.put("isDepotUser", new Boolean(user2.isDepotUser()) );
				} else {
                    params.put("isCorp", Boolean.FALSE );
                    params.put("isDepotUser", Boolean.FALSE );
				}
			    %><fd:IncludeMedia name="/media/editorial/faq/intro.ftl" parameters="<%=params%>" withErrorReport="true"/>
			<%}%>
			</td>
		</tr>
		</table>
	</tmpl:put>
</tmpl:insert>
