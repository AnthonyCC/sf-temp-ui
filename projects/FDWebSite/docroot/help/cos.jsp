<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>

<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>	
<%
String tableWidth = "500";
String securityPolicyLink = "/help/privacy_policy.jsp";

String hdrImgName = "faq_hdr_cos.gif"; 
String hdrImgWidth = "453";
String hdrImgHeight = "30";

boolean isPopup = false;

if(request.getParameter("page")== null){
tableWidth = "375";
isPopup = true;
securityPolicyLink = "javascript:linkTo('/help/privacy_policy.jsp')";
hdrImgName = "faq_hdr_pop_cos.gif"; 
hdrImgWidth = "380";
hdrImgHeight = "30";
}

boolean isDlvInfo = false;

if (request.getRequestURI().toLowerCase().endsWith("delivery_info_faq.jsp")){
isDlvInfo = true;
tableWidth = "683";
} 
%>
<script>
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
</script>
<A NAME="top"></A>
			<TABLE width="<%=tableWidth%>" CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD>
					<img src="/media_stat/images/template/help/<%=hdrImgName%>" width="<%=hdrImgWidth%>" height="<%=hdrImgHeight%>" alt="" border="0">
				   </TD>
				</TR>
			</TABLE>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<img src="/media_stat/images/layout/cccccc.gif" width="<%=tableWidth%>" height="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
						<table cellpadding="0" cellspacing="0">
							<tr><td colspan="2" class="bodyCopy"><span class="text13"><b>What is FreshDirect At The Office?</b></span><br><br>
					FreshDirect At The Office brings area businesses the same great, fresh food that's made FreshDirect indispensable for tens of thousands of New York households, with expanded product and service offerings especially for corporate clients. These include:<br><br></td></tr>
							<tr><td valign="top"><li type="dot"></li></td><td class="bodyCopy">Chef-prepared breakfast and luncheon catering platters perfect for business meetings;</td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign="top"><li type="dot"></li></td><td class="bodyCopy">Popular brands of snacks, beverages, and pantry-stocking items;</td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign="top"><li type="dot"></li></td><td class="bodyCopy">Delicious restaurant-quality individual meals;</td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign="top"><li type="dot"></li></td><td class="bodyCopy">Catering services for upscale events;</td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign="top"><li type="dot"></li></td><td class="bodyCopy">Convenient morning and afternoon delivery windows;</td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy">Dedicated corporate account manager and customer service representatives.</td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/cccccc.gif" width="<%=tableWidth%>" height="1" alt="" border="0" vspace="14"></td></tr>	
							
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question1">Where does FreshDirect deliver?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question2">When does FreshDirect deliver?</A> </td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question3">Who is responsible for assembling my order?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question4">How does delivery work?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question5">Will FreshDirect set up for my meeting or event?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question6">Is there a charge for delivery?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>	
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question_fuel">What is the Fuel Surcharge?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question7">Should I tip the driver?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question8">What if no one is available to receive the order?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question9">How long will my order stay fresh in the delivery boxes?</A></td></tr>	                                   
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question10">Can I pick up my order from your facility?</A></td></tr>
							<tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
                            <tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question11">What is the "Bottle Deposit" line on my invoice?</A></td></tr>
                            <tr><td class="bodyCopy" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>			
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question12">What if my order contains beer or wine?</A></td></tr>						
						</table>	
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question1"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Where does FreshDirect deliver?</b><br>
						We currently deliver to downtown areas of Manhattan but will soon be expanding throughout Manhattan, as well as to parts of Brooklyn and Queens. Call us at <%=user.getCustomerServiceContact()%> to find out if we deliver to your office.<br>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question2"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP" class="bodyCopy">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>When does FreshDirect deliver?</b><br>
						We deliver from Monday-Friday, starting as early as 7:00 a.m. You can schedule a delivery time up to a week in advance, or as late as 7:00 p.m. on the day before delivery.<br>
						<br><%-- delivery times --%>
                        <fd:IncludeMedia name="/media/editorial/site_pages/cos_delivery_plan_table.html"/>    
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question3"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Who is responsible for assembling my order?</b><br>
						At FreshDirect, the only people who handle your food have special expertise in their area. So, whether we are selecting nice plump fruits and vegetables, cutting steaks, or packing your food into boxes, each member of our team takes pride in the quality of his or her products and handles every item with care. <br>
				   </TD>
				</TR>
			</TABLE>
			<br>
			<A NAME="question4"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>How does delivery work?</b><br>
						When you check out, select a delivery time as soon as the next day. A FreshDirect driver will bring the order to your building and deliver it to your designated floor or receiving area. Please make sure that the driver can get into the building during the designated delivery time slot.
						<br><br>
						You'll be able to recognize our drivers by their FreshDirect uniforms - and by the boxes of carefully packed fresh food they'll have with them. They'll also hand you an itemized receipt.
						<br><br>
						If anything is wrong with your order, please contact FreshDirect Customer Service right away at <%=user.getCustomerServiceContact()%>.
						<br>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question5"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">	
						<b>Will FreshDirect set up for my meeting or event?</b><br>
						FreshDirect delivery personnel will be happy to unpack your boxes and collect them after delivery. However, they do not stay to set up and arrange platters for events.<br>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question6"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">	
						<b>Is there a charge for delivery?</b><br>
						Delivery costs $<%=(double)user.getCorpDeliveryFee()%>. The minimum order is $<%=(int)user.getMinCorpOrderAmount()%>.<br>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question_fuel"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">	
						<b>What is the Fuel Surcharge?</b><br>
						In order to help offset the impact of dramatic rises in oil and fuel prices FreshDirect has added a fuel surcharge. The fuel surcharge helps cover not merely the increased cost of making deliveries to our customers, but also broad increases in commodity and utility prices that directly effect running &mdash; and refrigerating &mdash; our facility. <a href="javascript:popup('/shared/fee_info.jsp?type=fuel','large')">Click here for details</a>.<br>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question7"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Should I tip the driver?</b><br>
						You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service. FreshDirect delivery personnel are not permitted to solicit tips under any circumstances. If you have a comment or compliment please e-mail us. We'd love to hear from you!<br>

				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question8"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What if no one is available to receive the order?</b><br>
						If you think that no one will be available to receive your scheduled delivery, or if you miss your delivery, please call FreshDirect Customer Service at <%=user.getCustomerServiceContact()%>. If your order cannot be redelivered that day there is a restocking fee of 100% of the cost of perishable items in the order plus 25% of the cost of packaged goods plus 50% of the total amount of any beer items, excluding fees and tax. 
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question9"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b> How long will my order stay fresh in the delivery boxes?</b><br>
						Refrigerated perishables will stay fresh in the delivery boxes up to two hours after delivery. Frozen items should be placed in a freezer immediately.<br>

				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question10"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Can I pick up my order from your facility?</b><br>
						Yes, anyone in the Tri-State area can place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens. For details <a href="<%= isPopup ? "javascript:linkTo('" : ""%>/help/delivery_lic_pickup.jsp<%= isPopup ? "')" : ""%>">click here</a>.<br>
				   </TD>
				</TR>
			</TABLE>
                        <br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question11"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What is the "Bottle Deposit" line on my invoice?</b><br>
						In accordance with New York State law, FreshDirect charges a bottle deposit on certain items. Your total deposit amount is displayed as a separate line item in your shopping cart and on your invoice. 
						<br><br>
						If you wish to recover this deposit, empty bottles and cans may be redeemed at any redemption center or you may bring them to our facility at 23-30 Borden Avenue, Long Island City, Queens. FreshDirect can only redeem bottles and cans of products that we sell. Our drivers cannot accept bottles and cans for redemption. Bottle deposit will not be charged for deliveries to New Jersey.
					</TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question12"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What if my order contains beer or wine?</b><br>
						If your order contains alcoholic beverages, the person receiving your delivery must have identification proving they are over the age of 21 and will be asked for their signature. If no one over the age of 21 can sign for delivery, the driver will remove alcoholic beverages from the order and you will be charged a 50% restocking fee.<br>
				   </TD>
				</TR>
			</TABLE>
			<br>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy"><A HREF="#top"><img src="/media_stat/images/template/help/up_arrow.gif" width="17" height="9" hspace="0" vspace="4" border="0" align="left"><img src="/media/images/layout/clear.gif" width="6" height="1" border="0">top of page</A>
						<br><br><br>			
				   </TD>
				</TR>
			</TABLE>
