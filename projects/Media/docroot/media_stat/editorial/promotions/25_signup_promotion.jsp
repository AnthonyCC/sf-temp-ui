<%@ page import="com.freshdirect.fdstore.*"%>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%
String fruitDeptLink = "/department.jsp?deptId=fru&trk=promo";
String vegetableDeptLink = "/department.jsp?deptId=veg&trk=promo";
String meatDeptLink = "/department.jsp?deptId=mea&trk=promo";
String seafoodDeptLink = "/department.jsp?deptId=sea&trk=promo";
String deliDeptLink = "/department.jsp?deptId=del&trk=promo";
String cheeseDeptLink = "/department.jsp?deptId=che&trk=promo";
String dairyDeptLink = "/department.jsp?deptId=dai&trk=promo";
String coffeeDeptLink = "/department.jsp?deptId=cof&trk=promo";
String teaDeptLink = "/department.jsp?deptId=tea&trk=promo";
String pastaDeptLink = "/department.jsp?deptId=pas&trk=promo";
String bakeryDeptLink = "/department.jsp?deptId=bak&trk=promo";
String kitchenDeptLink = "/department.jsp?deptId=hmr&trk=promo";
String groceryDeptLink = "/department.jsp?deptId=gro&trk=promo";
String frozenDeptLink = "/department.jsp?deptId=fro&trk=promo";
String specialtyDeptLink = "/department.jsp?deptId=spe&trk=promo";
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
boolean isDepotUser = user==null?false:user.isDepotUser();
%>
<html>
<head>

<SCRIPT LANGUAGE="Javascript1.1">

function goToPage(pagePath) {
	redirectUrl = "http://" + location.host + pagePath;
	self.opener.location = redirectUrl;
	self.opener.focus();
}
</SCRIPT>

<title>Fresh Direct</title>

<%@ include file="/shared/template/includes/style_sheet_detect.jspf" %>

</head>
<body bgcolor="White" link="#336600" vlink="#336600" alink="#FF9900" text="#333333" onLoad="window.focus()" leftmargin=0 topmargin=0>

<a name="top"></a>

		<table border=0 cellpadding="0" cellspacing="0">
		<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>
		<tr>
			<td valign="top"><img src="/media_stat/images/template/promotions/25_offer_left.gif" width="180" height="300" alt="" border="0"></td>
			<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" alt="" border="0"></td>			
			<td valign=top>

				<table border=0 cellpadding="0" cellspacing="0">
				<tr>
					<td><a href="javascript:self.close()"><img src="/media_stat/images/layout/pop_up_header_sm.gif"  alt="" border="0"></a></td>
				</tr>
				<tr>
					<td><img src="/media_stat/images/template/promotions/25_fresh_free_food.gif" width="160" height="16" alt="" border="0"></td>
				</tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="20" alt="" border="0"></td></tr>
				<tr>
					<td>
                                        <b>Please note that our $25.00 free fresh food offer on the second order will become $10.00 effective on Wednesday, March 19, 2003.</b> If you would like to take advantage of our $25.00 promotional offer, you must place your second order online before Wednesday, March 19. If you place your order on or after March 19 you will receive the $10.00 free fresh food promotion.
                                        <br><br>Your feedback helps us keep improving all the time. That's why we'll give you $25 worth of free fresh food when you answer some questions about your FreshDirect experience, food, and you. Here's all there is to it:</td>
				</tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>				

				<tr>
					<td>
					<img src="/media_stat/images/template/promotions/50_offer_stars.gif" width="140" height="11" alt="" border="0"><br>
					<img src="/media_stat/images/template/promotions/25_offer_free_food.gif" width="140" height="19" alt="" border="0"><br>
					You have <b>$25.00</b> left.<br>
					<img src="/media_stat/images/template/promotions/50_offer_stars.gif" width="140" height="11" alt="" border="0"><br>					
					</td>
				</tr>

				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>		

				<tr>
					<td>
						Shop for all of your favorite <a href="javascript:goToPage('<%=fruitDeptLink%>')">Fruit</a>, <a href="javascript:goToPage('<%=vegetableDeptLink%>')">Vegetables</a>, <a href="javascript:goToPage('<%=meatDeptLink%>')">Meat</a>, <a href="javascript:goToPage('<%=seafoodDeptLink%>')">Seafood</a>, <a href="javascript:goToPage('<%=deliDeptLink%>')">Deli</a> goods, <a href="javascript:goToPage('<%=cheeseDeptLink%>')">Cheeses</a>, <a href="javascript:goToPage('<%=coffeeDeptLink%>')">Coffee</a>, and <a href="javascript:goToPage('<%=teaDeptLink%>')">Tea</a>. Almost all of our fresh foods are included in this offer &#151; just look for this icon on the top of the product page. 
					</td>
				</tr>	
							
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>				

				<tr>
					<td>

					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
						<td valign="top" class="text11orbold" align=right>Qualifies for<br> $25 offer! </td>
						<td><img src="/media_stat/images/layout/clear.gif" width="5" height="1" alt="" border="0"></td>
						<td><img src="/media_stat/images/template/promotions/offer_icon.gif" width="26" height="26" alt="" border="0"></td>
						</tr>
					</table>

					</td>
				</tr>
				<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"></td></tr>		
				
				<tr>
					<td>
						While you shop, keep track of your savings with our Trial Offer Counter. When you check out, answer all nine questions, and click continue to check out. We'll subtract the total amount of your free food (up to $25) from the cost of your order. If you wish, you can skip the survey and waive the offer. Delivery is still free on this and your next order. Go ahead and <a href="javascript:goToPage('/index.jsp')">start shopping</a>.
						<br><br>
						From all of us at FreshDirect &#151; Happy Eating!
						<br><br>
						<b>Other details</b><br>
						We're giving you up to $15 off any single item. Certain perishables may be excluded. We have not included branded and packaged products in this offer because we want you to try our fresh goods. This offer is good for a limited time only. <b><%=(isDepotUser?"":"To qualify for this offer, your credit card billing and delivery addresses must match.")%> One offer per household, good only when you complete the survey on your second order.</b> There's a $40 order minimum, from which the cost of your eligible goods will be deducted. Orders must be placed through our Web site &#151; unfortunately, phone orders are not eligible. Any unused balance cannot be carried forward, so make sure you use it all now. 
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
