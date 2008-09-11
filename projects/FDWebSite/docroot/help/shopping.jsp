<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>	
<%
String tableWidth = "500";
String yourAccountLink = "/your_account/manage_account.jsp";
String signupLink = "/registration/signup.jsp";
String changeOrderLink = "/your_account/order_history.jsp";

String hdrImgName = "faq_hdr_shopping.gif"; 
String hdrImgWidth = "453";
String hdrImgHeight = "30";

if(request.getParameter("page")== null){
tableWidth = "375";
signupLink = "javascript:linkTo('/registration/signup.jsp')";
yourAccountLink = "javascript:linkTo('/your_account/manage_account.jsp')";
changeOrderLink = "javascript:linkTo('/your_account/order_history.jsp')";

hdrImgName = "faq_hdr_pop_shopping.gif"; 
hdrImgWidth = "380";
hdrImgHeight = "30";
}

%>

<script>
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
</script>


<A NAME="top"></A>
<table cellpadding="0" cellspacing="0" border="0">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td>
		<img src="/media_stat/images/template/help/<%=hdrImgName%>" width="<%=hdrImgWidth%>" height="<%=hdrImgHeight%>" alt="" border="0">
	   </td>
	</tr>
</table>

<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<IMG src="/media_stat/images/layout/cccccc.gif" width="<%=tableWidth%>" HEIGHT="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
		<table cellpadding="0" cellspacing="0">
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question1">How do I order?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question2">Can I order by phone?</A> </td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question3">What is Quickshop?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question4">Why are some prices estimated?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question5">How can I check on my order?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question6">What happens to my cart if I leave before checking out?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question7">Can I change or cancel an order?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question8">Does FreshDirect make substitutions?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question9">Can I return something if I'm not satisfied?</A></td></tr>
			<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
			<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question10">When are credits applied?</A></td></tr>						
		</table>	  
	  
	   </td>
	</tr>
</table>

<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question1"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>How do I order?</b><br>
		<table cellpadding="0" cellspacing="0">
		<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy">First, <a href="<%=signupLink%>">sign up</a>.</td></tr>
		<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
		<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy">Browse our departments or search for specific items. </td></tr>
		<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
		<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy">When you find what you want, choose a quantity and any other options that are available for that item, and add it to your cart.</td></tr>
		<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
		<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy">Once you have everything you need ($<%= (int) user.getMinimumOrderAmount() %> minimum), go to Checkout. Select your delivery address and a two-hour delivery slot as early as the next day.</td></tr>
		<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
		<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy">Pay by credit card or from your checking account &mdash; we won't charge you until the day of delivery when we've finished preparing your order. We'll send you an e-mail confirming your order details.</td></tr>
		<tr><td class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
		<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy">Use Quickshop to reorder quickly.</td></tr>
		</table>
	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question2"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>Can I order by phone?</b><br>
            Yes, we accept telephone orders during our normal customer service hours, listed on the <a href="/help/index.jsp">Get Help</a> page. Call <%= user.getCustomerServiceContact() %>. A trained FreshDirect customer service representative will take your order and answer any questions you may have. The fee for telephone orders is $9.99.
			</td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question3"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>What is Quickshop?</b><br>
			Quickshop is where you can reorder your favorite items &#151; fast. We remember everything you've ordered, exactly the way you like it, so you can shop from your past orders in minutes. And soon you'll also be able to use our special lists to go directly to the most popular items in every department or get great ideas from FreshDirect on a range of food topics, from everyday to entertaining. <br>
	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question4"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>Why are some prices estimated?</b><br>
				Every apple, every steak, every cheese varies a little in size and weight. While you shop we show an estimated weight and price for everything priced by the pound. On the day of delivery we assemble your order and weigh each item to determine its final price. 
				Please note that until you complete checkout, the unit price of any product on the site and in your cart may change to reflect current market conditions. When you complete checkout, your price per pound is locked in. 
				<br><br>
				We guarantee that you'll always pay the true price for the actual weight of your products.
				<br>

	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question5"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>How can I check on my order?</b><br>
			To find out the status of your order, to check your delivery time, or to remember which items you ordered, visit <a href="<%=yourAccountLink%>">Your Account</a>.<br>

	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question6"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>What happens to my cart if I leave before checking out?</b><br>
			If you have an account with us, we'll save everything in your cart. The next time you come back, you can pick up where you left off.<br>

	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question7"></A>
<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
		
<b>Can I change or cancel an order?</b><br>
You can change, reschedule, or cancel an order at any time before the cancellation deadline indicated on the email we send to confirm your order. Listed below is our standard schedule - the time slots and order deadlines for your neighborhood may vary slightly.
<br>
<br>
<fd:IncludeMedia name="/media/editorial/site_pages/delivery_plan_table.html"/>
<br>
		</td>
	</tr>
</table>

<br>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			To avoid being charged for an order you wish to cancel, we must receive your cancellation before the deadline. For orders cancelled after the deadline there is a restocking fee of 100% of the cost of perishable items in the order plus 25% of the cost of packaged goods plus 50% of the total amount of any beer items, excluding fees and tax. If you miss the deadline, but for some reason still cannot accept your order, please call FreshDirect Customer Service at <%=user.getCustomerServiceContact()%>.
			<br><br>
			Please note that if you make changes to an order, the price and availability of some items may change.
			<br><br>
			To change or cancel an order, <a href="<%=changeOrderLink%>">click here</a>.
			<br>
	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question8"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>Does FreshDirect make substitutions?</b><br />
			Our goal is to fulfill 100% of each customer's order, but occasionally market conditions beyond our control may cause an item to become unavailable. At times when we have an item that's very similar to the item you ordered, we may make a substitution. Any substitute item will be marked as such. If you're not happy with the replacement, we hope you'll email us so we can make it right: <a href="mailto:subs@freshdirect.com">subs@freshdirect.com</a>.<br />
	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question9"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>Can I return something if I'm not satisfied?</b><br>
We take pride in the high quality of our fresh food and packaged goods. That's why we guarantee your satisfaction with every product, every time. If you are dissatisfied with any item in your order, please contact us right away &#151; we'll either replace it with an acceptable item, or refund 100% of the cost for that item. E-mail us at  <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a> or call <%=user.getCustomerServiceContact()%>.<br>

	   </td>
	</tr>
</table>
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border=0><br>
<A NAME="question10"></A>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<b>When are credits applied?</b><br>
			We will automatically apply all of your outstanding credit to your next order at the time of checkout. <br>

	   </td>
	</tr>
</table>
<br>
<table cellpadding="0" cellspacing="0" border="0" width="<%=tableWidth%>" class="bodyCopy">
	<tr valign="top">
		<td width="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
		<td width="<%=tableWidth%>" class="bodyCopy">
			<A HREF="#top"><img src="/media_stat/images/template/help/up_arrow.gif" width="17" height="9" hspace="0" vspace="4" border="0" align="left"><img src="/media/images/layout/clear.gif" width="6" height="1" border="0">top of page</A>
			<br><br><br>			
	   </td>
	</tr>
</table>



