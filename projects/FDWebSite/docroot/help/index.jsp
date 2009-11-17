<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<fd:CheckLoginStatus />

<%  FDUserI user = (FDUserI)session.getAttribute(SessionName.USER); %>

<tmpl:insert template='/common/template/no_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put>
    <tmpl:put name='content' direct='true'>
<TABLE CELLSPACING="0" CELLPADDING="0" BORDER="0" width="670">
	<tr VALIGN="TOP">
	    <TD CLASS="text11" colspan="4">
		<span class="title16">Welcome to Help</span><br>
		Find answers to questions here or <a href="contact_fd.jsp">contact us</a>.
	    </td>
	    <TD>
	    	<img src="/media_stat/images/template/help/quick_links.gif" width="82" height="12"><br>
			<font class="space4pix"><br></font>
	    	<SELECT ONCHANGE="location = this.options[this.selectedIndex].value;">
	    	    <OPTION>We can help you...
	    	    <OPTION VALUE="/your_account/order_history.jsp">Check the status of your order
	    	    <OPTION VALUE="/your_account/order_history.jsp">Change or cancel your order
	    	    <OPTION VALUE="/search.jsp">Find a product
	    	    <OPTION VALUE="/help/contact_fd.jsp">Ask us a question
	    	    <OPTION VALUE="/your_account/signin_information.jsp">Change your password
	    	    <OPTION VALUE="/your_account/manage_account.jsp">Change delivery info
	    	    <OPTION VALUE="/your_account/manage_account.jsp">Change credit card info
	    	</SELECT>
	    </TD>	    
        </TR>
		<tr>
			<td colspan="5">
				<br/>
				<%
				boolean loyaltyHelpContact = false;
				%>
				<%@ include file="/shared/help/i_loyalty_banner.jspf"%>
			</td>
		</tr>
        <TR>
            <TD colspan="5">
	    	<br>
	    	    <img src="/media_stat/images/layout/cccccc.gif" width="670" height="1"><br>
	    	<br>
			<img src="/media_stat/images/layout/clear.gif" width="1" height="2">
	    </td>
	    
        </tr>
			     
	<tr>
	    <TD valign="top" width="190">
					<a href="/help/faq_home.jsp?page=faqHome"><img src="/media_stat/images/template/help/faq_home_hdr.gif" width="51" height="19" border="0" alt="FAQs"></a><br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
					Find answers to our most frequently asked questions.<br>
					<br>
					<font class="text11bold">
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=about">What We Do</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=signup">Signing Up</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=security">Security &amp; Privacy</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=shopping">Shopping</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=payment">Payment</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=deliveryHome">Home Delivery</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=cos">Corporate Delivery</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=chefstable">Chef's Table</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
		
		<!--<%	if(user.isDepotUser()){%>	
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=deliveryDepot">Depot Delivery</a><br>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="4" BORDER="0"><br>
		<%}%>-->
					
					<img src="/media_stat/images/layout/orangedot.gif" width="8" height="8" border="0" ALIGN="BOTTOM">&nbsp;&nbsp;<a href="/help/faq_home.jsp?page=inside">Jobs &amp; Corporate Info</a>
					</font>
	    </td>
	    
	    <TD valign="top" align="CENTER" width="40">
	 	<img src="/media_stat/images/layout/cccccc.gif" width="1" height="280"><br>
	    </td>
	    
	    <TD valign="top" width="190" class="bodyCopy">
					<img src="/media_stat/images/template/help/policies.gif" width="88" height="18" alt="POLICIES">
					<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
					<b><a href="terms_of_service.jsp">Customer Agreement</a></b>
					<br>
					All the details about what to expect when you shop with us.<br>
					<br>
					<b><a href="/help/privacy_policy.jsp">Privacy Policy</a></b>
					<br>
					We keep your account secure and your information private.<br>
					<br>
					<b><a href="javascript:pop('/help/freshness.jsp',335,375)">Our Freshness Guarantee</a></b>
					<br>
					100% satisfaction with every product, every time.
	    </td>
		
		<TD valign="top" align="CENTER" width="40">
	    	<img src="/media_stat/images/layout/cccccc.gif" width="1" height="280"><br>
	    </td>
		
		<TD valign="top" width="305">
		            <a href='contact_fd.jsp'><img src="/media_stat/images/template/help/contact_us.gif" border="0" width="137" height="19" alt="CONTACT US"></a>
					<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
	            	    FreshDirect Customer Service is standing by to answer your questions seven days a week.  The best way to resolve problems with an order you have received, or to get answers to general questions, is email.<br>
	            	    <br>
                        <div align="center">
	            	        <BIG><b><a href="contact_fd.jsp">CLICK HERE TO CONTACT US</a></b></BIG>
                        </div>
                        <br>
	            	    <br>
                        <%--MEDIA INCLUDE--%><fd:IncludeMedia name="/media/editorial/site_pages/help_home_hours.html" /><%--END MEDIA INCLUDE --%>
                        </br>You may also call <%=user.isChefsTable()?"toll-free":"us"%> at <%=user.getCustomerServiceContact()%>.<br>
	            	    <br>
	            	    <%--For more information on our <b>Corporate and Commercial Services</b>, <a href="mailto:service@freshdirect.com">click here</a>--%>
	    </td>
	</tr>
	<tr>
	    <TD colspan="5">
		<br><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
		    <img src="/media_stat/images/layout/cccccc.gif" width="670" height="1"><br>
		<br>	
	    </td>
	</tr>
	<tr><TD colspan="5">
		<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="675">
		<tr VALIGN="TOP">
		<TD WIDTH="35"><a href="/index.jsp"><img src="/media_stat/images/buttons/arrow_green_left.gif" border="0" alt="CONTINUE SHOPPING" ALIGN="LEFT"></a></td>
		<TD WIDTH="640"  class="text11" ><a href="/index.jsp"><img src="/media_stat/images/buttons/continue_shopping_text.gif"  border="0" alt="CONTINUE SHOPPING"></a>
		<BR>from <a href="/index.jsp"><b>Home Page</b></a><BR><img src="/media_stat/images/layout/clear.gif" WIDTH="340" HEIGHT="1" BORDER="0"></td>
		</tr>
		</TABLE>
	</td></tr>
				
</TABLE>
	<br>
	<br>
</tmpl:put>
</tmpl:insert>


					
