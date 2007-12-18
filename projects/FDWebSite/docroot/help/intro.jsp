<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />

<%
String indexLink ="/help/faq_home.jsp?page=faqHome";
String aboutLink ="/help/faq_home.jsp?page=about";
String signupLink ="/help/faq_home.jsp?page=signup";
String securityLink ="/help/faq_home.jsp?page=security";
String shoppingLink ="/help/faq_home.jsp?page=shopping";
String paymentLink ="/help/faq_home.jsp?page=payment";
String deliveryHomeLink ="/help/faq_home.jsp?page=deliveryHome";
String deliveryDepotLink ="/help/faq_home.jsp?page=deliveryDepot";
String infoLink ="/help/faq_home.jsp?page=inside";
String cosLink  ="/help/faq_home.jsp?page=cos";

String tableWidth = "500";

if(request.getParameter("page")== null){
	tableWidth ="375";
	indexLink ="/help/faq_index.jsp?show=intro";
	aboutLink ="/help/faq_index.jsp?show=about";
	signupLink ="/help/faq_index.jsp?show=signup";
	securityLink ="//help/faq_index.jsp?show=security";
	shoppingLink ="/help/faq_index.jsp?show=shopping";
	paymentLink ="/help/faq_index.jsp?show=payment";
	deliveryHomeLink ="/help/faq_index.jsp?show=delivery";
	deliveryDepotLink ="/help/faq_index.jsp?show=delivery_depot";
	infoLink ="/help/faq_index.jsp?show=inside_fd";	
        
}

FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>

<A NAME="top"></A>
<table cellpadding="0" cellspacing="0" border="0">
<td><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
<td>
<img src="/media_stat/images/template/help/faq_hdr.gif" width="358" height="21" alt="" border="0"><br>
<FONT CLASS="space2pix"><BR><br></FONT><IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="<%=tableWidth%>" HEIGHT="1"><BR><FONT CLASS="space2pix"><BR><br><br></FONT>
<FONT CLASS="title12"><a href="<%=aboutLink%>" target="_parent">What We Do</a></FONT><BR>
Find out who we are and what we sell.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<table border="0" cellspacing="0" cellpadding="0">
		<tr><td><li type="dot"></li></td><td>What is FreshDirect?</td></tr>
		<tr><td><li type="dot"></li></td><td>Why is FreshDirect fresher AND less expensive than a supermarket?</td></tr>
		<tr><td><li type="dot"></li></td><td>What kind of food does FreshDirect sell?</td></tr>
		<tr><td><li type="dot"></li></td><td>Where is my order prepared?</td></tr>
		<tr><td><li type="dot"></li></td><td>Does FreshDirect offer organic products?</td></tr>
		<tr><td><li type="dot"></li></td><td>Does FreshDirect have sales?</td></tr>
		<tr><td><li type="dot"></li></td><td>How can I find out more about FreshDirect?</td></tr>
		</table>
	</td>
</tr>
</table>

<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="<%=signupLink%>" target="_parent">Signing Up</a>
</FONT><BR>
Find out how simple it is to sign up with us.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<li type="dot">How do I sign up?</li>
		<li type="dot">What if I forget my password?</li>
	</td>
</tr>
</table>


<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="<%=securityLink%>" target="_parent">Security &amp; Privacy</a>
</FONT><BR>
Find out how we keep your information private and safe.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<li type="dot">Is FreshDirect secure?</li>
		<li type="dot">Will my information be kept private?</li>
		<li type="dot">Does FreshDirect use cookies?</li>		
	</td>
</tr>
</table>

<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="<%=shoppingLink%>" target="_parent">Shopping</a>
</FONT><BR>
Find out how to order and reorder.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<li type="dot">How do I order?</li>
		<li type="dot">Can I order by phone?</li>
		<li type="dot">What is Quickshop?</li>		
		<li type="dot">Why are some prices estimated?</li>
		<li type="dot">How can I check on my order?</li>
		<li type="dot">What happens to my cart if I leave before checking out?</li>		
		<li type="dot">Can I change or cancel an order?</li>
		<li type="dot">Does FreshDirect make substitutions?</li>
		<li type="dot">Can I return something if I'm not satisfied?</li>		
		<li type="dot">When are credits applied?</li>
	</td>
</tr>
</table>



<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="<%=paymentLink%>" target="_parent">Payment</a>
</FONT><BR>
Find out about your different payment options.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<li type="dot">How do I pay?</li>
		<li type="dot">What is the minimum order?</li>
		<li type="dot">Does FreshDirect accept coupons?</li>		
		<li type="dot">Does FreshDirect accept personal checks or cash?</li>
		<li type="dot">Does FreshDirect accept food stamps?</li>
	</td>
</tr>
</table>

<%if(!user.isCorporateUser()){%>

<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="<%=deliveryHomeLink%>" target="_parent">Home Delivery</a>
</FONT><BR>
Find out where, when, and how we deliver.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<li type="dot">Where does FreshDirect deliver?</li>
		<li type="dot">When does FreshDirect deliver?</li>
		<li type="dot">Who is responsible for assembling my order?</li>		
		<li type="dot">How does delivery work?</li>		
		<li type="dot">Is there a charge for delivery?</li>
		<li type="dot">Should I tip the driver?</li>
		<li type="dot">What if I'm not home to receive my order? </li>		
		<li type="dot">How long will my order stay fresh in the delivery boxes?</li>
		<li type="dot">Can I pick up my order from your facility?</li>
		<li type="dot">What if my order contains beer or wine?</li>		
	</td>
</tr>
</table>

<%} else { %>
<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="<%=cosLink%>" target="_parent">Corporate Services</a>
</FONT><BR>
Find out where, when, and how we deliver to businesses.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<li type="dot">What is FreshDirect Corporate Services?</li>
		<li type="dot">Where does FreshDirect deliver?</li>
		<li type="dot">When does FreshDirect deliver?</li>		
		<li type="dot">Who is responsible for assembling my order?</li>		
		<li type="dot">How does delivery work?</li>
		<li type="dot">Is there a charge for delivery?</li>
		<li type="dot">Should I tip the driver?</li>		
		<li type="dot">What if no one is available to receive the order?</li>
		<li type="dot">How long will my order stay fresh in the delivery boxes?</li>
		<li type="dot">Can I pick up my order from your facility?</li>	
                <li type="dot">What is the "Bottle Deposit" line on my invoice?</li>	
                <li type="dot">What if my order contains beer or wine?</li>	
	</td>
</tr>
</table>
<%}%>

<%	if(user.isDepotUser()){%>	
	<BR><FONT CLASS="space2pix"><BR></FONT>
	<FONT CLASS="title12">
		<a href="<%=deliveryDepotLink%>" target="_parent">Depot Delivery</a>
	</FONT><BR>
	Find out about delivery to your company.<br>
	<font class="space4pix"><br></font>
	<table>
	<tr>
		<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
		<td>
			<li type="dot">What is depot delivery?</li>
			<li type="dot">How far in advance should I place my depot order?</li>
			<li type="dot">When does FreshDirect deliver to my company?</li>
			<li type="dot">How does delivery work?</li>
			<li type="dot">Is there a charge for depot delivery?</li>
			<li type="dot">Should I tip the driver?</li>
			<li type="dot">What if I'm not at work to receive my order?</li>
			<li type="dot">What if my company is closed on the day of delivery?</li>
			<li type="dot">What happens if I no longer work for the company?</li>
		</td>
	</tr>
	</table>
<%}%>
	
<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="<%=infoLink%>" target="_parent">Jobs &amp; Corporate Info</a>
</FONT><BR>
Find out more about what's happening inside FreshDirect.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<li type="dot">How can I find out about jobs at FreshDirect?</li>
		<li type="dot">Where can I find corporate information about FreshDirect?</li>		
	</td>
</tr>
</table>
</td>
</table>