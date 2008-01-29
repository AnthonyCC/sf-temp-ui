<#include "fd-macros.ftl" />
<#setting locale="en_US"/>
<#assign isPageNull=parameters["isPageNull"]/>
<#assign isCorpUser=parameters["isCorpUser"]/>
<#assign isDepotUser=parameters["isDepotUser"]/>
<#if isPageNull>
<#assign indexLink="/help/faq_index.jsp?show=intro"/>
<#assign aboutLink="/help/faq_index.jsp?show=about"/>
<#assign signupLink="/help/faq_index.jsp?show=signup"/>
<#assign securityLink="//help/faq_index.jsp?show=security"/>
<#assign shoppingLink="/help/faq_index.jsp?show=shopping"/>
<#assign paymentLink="/help/faq_index.jsp?show=payment"/>
<#assign deliveryHomeLink="/help/faq_index.jsp?show=delivery"/>
<#assign deliveryDepotLink="/help/faq_index.jsp?show=delivery_depot"/>
<#assign infoLink="/help/faq_index.jsp?show=inside_fd"/>	
<#else>
<#assign indexLink="/help/faq_home.jsp?page=faqHome"/>
<#assign aboutLink="/help/faq_home.jsp?page=about"/>
<#assign signupLink="/help/faq_home.jsp?page=signup"/>
<#assign securityLink="/help/faq_home.jsp?page=security"/>
<#assign shoppingLink="/help/faq_home.jsp?page=shopping"/>
<#assign paymentLink="/help/faq_home.jsp?page=payment"/>
<#assign deliveryHomeLink="/help/faq_home.jsp?page=deliveryHome"/>
<#assign deliveryDepotLink="/help/faq_home.jsp?page=deliveryDepot"/>
<#assign infoLink="/help/faq_home.jsp?page=inside"/>
</#if>
<#assign cosLink="/help/faq_home.jsp?page=cos"/>
<A NAME="top"></A>
<table cellpadding="0" cellspacing="0" border="0">
<td><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
<td>
<img src="/media_stat/images/template/help/faq_hdr.gif" width="358" height="21" alt="" border="0"><br>
<FONT CLASS="space2pix"><BR><br></FONT><#if isPageNull><IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="375" HEIGHT="1"><#else><IMG src="/media_stat/images/layout/cccccc.gif" WIDTH="500" HEIGHT="1"></#if><BR><FONT CLASS="space2pix"><BR><br><br></FONT>
<FONT CLASS="title12"><a href="${aboutLink}" target="_parent">What We Do</a></FONT><BR>
Find out who we are and what we sell.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>What is FreshDirect?</li>
			<li>Why is FreshDirect fresher AND less expensive than a supermarket?</li>
			<li>What kind of food does FreshDirect sell?</li>
			<li>Where is my order prepared?</li>
			<li>Does FreshDirect offer organic products?</li>
			<li>How can I find out more about FreshDirect?></li>
		</ul>
	</td>
</tr>
</table>

<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="${signupLink}" target="_parent">Signing Up</a>
</FONT><BR>
Find out how simple it is to sign up with us.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>How do I sign up?</li>
			<li>What if I forget my password?</li>
		</ul>
	</td>
</tr>
</table>


<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="${securityLink}" target="_parent">Security &amp; Privacy</a>
</FONT><BR>
Find out how we keep your information private and safe.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>Is FreshDirect secure?</li>
			<li>Will my information be kept private?</li>
			<li>Does FreshDirect use cookies?</li>
		</ul>
	</td>
</tr>
</table>

<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="${shoppingLink}" target="_parent">Shopping</a>
</FONT><BR>
Find out how to order and reorder.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>How do I order?</li>
			<li>Can I order by phone?</li>
			<li>What is Quickshop?</li>
			<li>Why are some prices estimated?</li>
			<li>How can I check on my order?</li>
			<li>What happens to my cart if I leave before checking out?</li>
			<li>Can I change or cancel an order?</li>
			<li>Does FreshDirect make substitutions?</li>
			<li>Can I return something if I'm not satisfied?</li>
			<li>When are credits applied?</li>
		</ul>
	</td>
</tr>
</table>



<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="${paymentLink}" target="_parent">Payment</a>
</FONT><BR>
Find out about your different payment options.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>How do I pay?</li>
			<li>What is the minimum order?</li>
			<li>Does FreshDirect accept coupons?</li>
			<li>Does FreshDirect accept personal checks or cash?</li>
			<li>Does FreshDirect accept food stamps?</li>
		</ul>
	</td>
</tr>
</table>

<!-- %if(!user.isCorporateUser()){% -->
<#if !isCorpUser>
<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="${deliveryHomeLink}" target="_parent">Home Delivery</a>
</FONT><BR>
Find out where, when, and how we deliver.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>Where does FreshDirect deliver?</li>
			<li>When does FreshDirect deliver?</li>
			<li>Who is responsible for assembling my order?</li>
			<li>How does delivery work?</li>
			<li>Is there a charge for delivery?</li>
			<li>Should I tip the driver?</li>
			<li>What if I'm not home to receive my order? </li>
			<li>How long will my order stay fresh in the delivery boxes?</li>
			<li>Can I pick up my order from your facility?</li>
			<li>What if my order contains beer or wine?</li>
		</ul>
	</td>
</tr>
</table>

<#else>
<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="${cosLink}" target="_parent">Corporate Services</a>
</FONT><BR>
Find out where, when, and how we deliver to businesses.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>What is FreshDirect Corporate Services?</li>
			<li>Where does FreshDirect deliver?</li>
			<li>When does FreshDirect deliver?</li>
			<li>Who is responsible for assembling my order?</li>
			<li>How does delivery work?</li>
			<li>Is there a charge for delivery?</li>
			<li>Should I tip the driver?</li>
			<li>What if no one is available to receive the order?</li>
			<li>How long will my order stay fresh in the delivery boxes?</li>
			<li>Can I pick up my order from your facility?</li>
			<li>What is the "Bottle Deposit" line on my invoice?</li>
			<li>What if my order contains beer or wine?</li>
		</ul>
	</td>
</tr>
</table>
</#if>

<!-- %	if(user.isDepotUser()){% -->
<#if isDepotUser>
	<BR><FONT CLASS="space2pix"><BR></FONT>
	<FONT CLASS="title12">
		<a href="${deliveryDepotLink}" target="_parent">Depot Delivery</a>
	</FONT><BR>
	Find out about delivery to your company.<br>
	<font class="space4pix"><br></font>
	<table>
	<tr>
		<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
		<td>
			<ul>
				<li>What is depot delivery?</li>
				<li>How far in advance should I place my depot order?</li>
				<li>When does FreshDirect deliver to my company?</li>
				<li>How does delivery work?</li>
				<li>Is there a charge for depot delivery?</li>
				<li>Should I tip the driver?</li>
				<li>What if I'm not at work to receive my order?</li>
				<li>What if my company is closed on the day of delivery?</li>
				<li>What happens if I no longer work for the company?</li>
			</ul>
		</td>
	</tr>
	</table>
</#if>
	
<BR><FONT CLASS="space2pix"><BR></FONT>
<FONT CLASS="title12">
	<a href="${infoLink}" target="_parent">Jobs &amp; Corporate Info</a>
</FONT><BR>
Find out more about what's happening inside FreshDirect.<br>
<font class="space4pix"><br></font>
<table>
<tr>
	<td><img src="/media/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
	<td>
		<ul>
			<li>How can I find out about jobs at FreshDirect?</li>
			<li>Where can I find corporate information about FreshDirect?</li>
		</ul>
	</td>
</tr>
</table>
</td>
</table>
