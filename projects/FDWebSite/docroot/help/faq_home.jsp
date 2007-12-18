<%
String faqPage = "faqHome";

if (request.getParameter("page")!= null){
faqPage = request.getParameter("page");
}

%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<fd:CheckLoginStatus />
<tmpl:insert template='/common/template/faq_help.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Help - FAQs</tmpl:put>
	<tmpl:put name='leftnav' direct='true'>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<table border="0" cellpadding="0" cellspacing="0" width=570>
		<tr>
			<td valign="top">
				<img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0">
			</td>
			<td>
			<%if(faqPage.equals("faqHome")){%>
				<%@ include file="/help/intro.jsp"%>
			<%}else if(faqPage.equals("about")){%>
				<%@ include file="/help/about_freshdirect.jsp"%>	
			<%}else if(faqPage.equals("signup")){%>
				<%@ include file="/help/signing_up.jsp"%>
			<%}else if(faqPage.equals("security")){%>
				<%@ include file="/help/security.jsp"%>
			<%}else if(faqPage.equals("shopping")){%>
				<%@ include file="/help/shopping.jsp"%>
			<%}else if(faqPage.equals("payment")){%>
				<%@ include file="/help/payment.jsp"%>
			<%}else if(faqPage.equals("deliveryHome")){%>
				<%@ include file="/help/delivery_home.jsp"%>
			<%}else if(faqPage.equals("deliveryDepot")){%>
				<%@ include file="/help/delivery_depot.jsp"%>				
			<%}else if(faqPage.equals("inside")){%>
				<%@ include file="/help/inside_fd.jsp"%>
            <%}else if(faqPage.equals("cos")){%>
                <%@ include file="/help/cos.jsp"%>
			<%}else{%>
				<%@ include file="/help/index.jsp"%>
			<%}%>
			</td>
		</tr>
		</table>
	</tmpl:put>
</tmpl:insert>
