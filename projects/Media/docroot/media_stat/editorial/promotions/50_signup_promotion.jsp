<%@ page import="com.freshdirect.fdstore.*"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
boolean isDepotUser = user==null?false:user.isDepotUser();
%>
<html>
<head>
<title>Fresh Direct</title>

<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>

</head>
<body bgcolor="White" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.focus()" leftmargin=0 topmargin=0>

<a name="top"></a>

		<table border=0 cellpadding="0" cellspacing="0">
		<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
		<tr>
			<td valign="top"><img src="/media_stat/images/template/promotions/50_offer_left.gif" width="180" height="300" alt="" border="0"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>			
			<td valign=top>

				<table border=0 cellpadding="0" cellspacing="0">
				<tr>
					<td><a href="javascript:self.close()"><img src="/media_stat/images/layout/pop_up_header_sm.gif"  alt="" border="0"></a></td>
				</tr>
				<tr>
					<td><img src="/media_stat/images/template/promotions/50_fresh_free_food.gif" width="160" height="16" alt="" border="0"></td>
				</tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" border="0"></td></tr>
				<tr>
					<td>We want you to try us. That's why we're treating you to $50 worth of free fresh food on your first order. We're also waiving the $40 minimum and the delivery charge for this order. Here's all there is to it:<br><br></td>
				</tr>
				<tr>
					<td>
					The first time you shop, load up your cart with all your favorite Fruit, Vegetables, Meat, Seafood, Deli goods, Cheeses, Coffee, and Tea. Almost all of our fresh foods are included in this offer, with only a few exceptions &#151; just look for this icon on the top of the product page. 
					
					</td>
				</tr>				
				
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>				
				<tr>
					<td>
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
						<td valign="top" class="text11orbold" align=right>Qualifies for<br> $50 offer! </td>
						<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
						<td><img src="/media_stat/images/template/promotions/offer_icon.gif" width="26" height="26" alt="" border="0"></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>		

				
				<tr>
					<td>
						While you shop, keep track of your savings with our Trial Offer Counter. During checkout, we'll subtract the total amount of your free food &#151; up to $50 &#151; from your order total. Go ahead and start shopping.
					</td>
				</tr>	
							
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>				
				<tr>
					<td>
					<img src="/media_stat/images/template/promotions/50_offer_stars.gif" width="140" height="11" alt="" border="0"><br>
					<img src="/media_stat/images/template/promotions/50_offer_free_food.gif" width="140" height="19" alt="" border="0"><br>
					You have <b>$50.00</b> left.<br>
					<img src="/media_stat/images/template/promotions/50_offer_stars.gif" width="140" height="11" alt="" border="0"><br>					
					</td>
				</tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>		
				
				<tr>
					<td>
						From all of us at FreshDirect &#151; Happy Eating!
						<br><br>
						<b>Other details</b><br>
						We're giving you up to $15 off any single item. We have not included branded and packaged products in this offer because we want you to try our fresh goods. This offer is good for a limited time only. <b><%=(isDepotUser?"":" To qualify for this offer, your credit card billing and delivery addresses must match.")%> One offer per household, good on the first order only.</b> Available in selected zones. Orders must be placed through our Web site &#151; unfortunately, phone orders are not eligible. Any unused balance cannot be carried forward, so make sure you use it all now.
					</td>
				</tr>
										
				</table>
			
			
			
			</td>
			<td><img src="/media_stat/images/layout/clear.gif" width="10" height="1" alt="" border="0"></td>
		</tr>
		</table>



<table border="0" cellpadding="0" cellspacing="0" width="530">
	<tr valign="TOP">
		<td width="380">
			&nbsp;<p>
		</td>
		<td width="150" align="RIGHT">			
			&nbsp;
			<p><a href="javascript:window.close();">close window</a>
		</td>
	</tr>
</table>
<br><br>
</body>
</html>
