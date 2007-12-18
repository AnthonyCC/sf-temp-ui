<%@ page import='com.freshdirect.fdstore.customer.FDUserI' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="true" />

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
	} else if ("delivery".equalsIgnoreCase(show)) {
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
				<%= about ? "<b>" : "<a href='/help/faq_index.jsp?show=about'>"%>What We Do<%= about ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= signup ? "<b>" : "<a href='/help/faq_index.jsp?show=signup'>"%>Signing Up<%= signup ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= security ? "<b>" : "<a href='/help/faq_index.jsp?show=security'>"%>Security &amp; Privacy<%= security ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= shopping ? "<b>" : "<a href='/help/faq_index.jsp?show=shopping'>"%>Shopping<%= shopping ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= payment ? "<b>" : "<a href='/help/faq_index.jsp?show=payment'>"%>Payment<%= payment ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
                <%= delivery ? "<b>" : "<a href='/help/faq_index.jsp?show=delivery'>"%>Home Delivery<%= delivery ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<%= cos ? "<b>" : "<a href='/help/faq_index.jsp?show=cos'>"%>Corporate Delivery<%= cos ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>

				<% if(_user.isDepotUser()){%>	
					<%= delivery_depot ? "<b>" : "<a href='/help/faq_index.jsp?show=delivery_depot'>"%>Depot Delivery<%= delivery_depot ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<% } %>
				<%= inside_fd ? "<b>" : "<a href='/help/faq_index.jsp?show=inside_fd'>"%>Jobs & Corporate Info<%= inside_fd ? "</b>" : "</a>"%><br><span class="space4pix"><br></span>
				<br><br>
				</td>
				<td></td>
				<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
				<td>
				<%if(intro){%>
					<jsp:include page="/help/intro.jsp"/>
				<%}else if(about){%>
					<jsp:include page="/help/about_freshdirect.jsp"/>	
				<%}else if(signup){%>
					<jsp:include page="/help/signing_up.jsp"/>
				<%}else if(security){%>
					<jsp:include page="/help/security.jsp"/>
				<%}else if(shopping){%>
					<jsp:include page="/help/shopping.jsp"/>
				<%}else if(payment){%>
					<jsp:include page="/help/payment.jsp"/>
				<%}else if(delivery){%>
					<jsp:include page="/help/delivery_home.jsp"/>
				<%}else if(delivery_depot){%>
					<jsp:include page="/help/delivery_depot.jsp"/>				
				<%}else if(inside_fd){%>
					<jsp:include page="/help/inside_fd.jsp"/>		
				<%}else if(cos){%>
                    <jsp:include page="/help/cos.jsp"/>		
                <%}%>
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
