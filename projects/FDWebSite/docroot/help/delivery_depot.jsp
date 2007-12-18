<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>

<%
String tableWidth = "500";
String scheduleWidth = "130";
String serviceAreaLink = "/index.jsp";
String depotName = "";

if(request.getParameter("page")== null){
tableWidth = "375";

serviceAreaLink = "javascript:linkTo('/index.jsp')";
scheduleWidth = "110";

	if(user.isDepotUser()){
		depotName = com.freshdirect.fdstore.FDDepotManager.getInstance().getInstance().getDepot(user.getDepotCode()).getName();
	}

}%>

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
					<img src="/media_stat/images/template/help/faq_hdr_pop_depot.gif" width="380" height="30" alt="" border="0"><br><img src="/media_stat/images/layout/cccccc.gif" width="<%=tableWidth%>" height="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
				   </TD>
				</TR>
			</TABLE>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<table cellpadding="0" cellspacing="0">
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question1">What is depot delivery?</A></td></tr>
							<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question3">How far in advance should I place my depot order?</A></td></tr>
							<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question4">When does FreshDirect deliver to my company?</A></td></tr>
							<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question5">How does delivery work?</A></td></tr>
							<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question6">Is there a charge for depot delivery?</A></td></tr>
							<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question7">Should I tip the driver?</A></td></tr>
							<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question8">What if I'm not at work to receive my order?</A></td></tr>
							<tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question9">What if my company is closed on the day of delivery?</A></td></tr>
							<tr><td ><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>		
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question10">What happens if I no longer work for the company?</A></td></tr>
                            <tr><td ><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
                            <tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question11">Who is responsible for assembling my order?</A></td></tr>
                            <tr><td ><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
                            <tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question12">How long will my order stay fresh in the delivery boxes?</A></td></tr>
                            <tr><td ><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
                            <tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question13">Why are there so many boxes in my order?</A></td></tr>
                            <tr><td ><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
                            <tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question14">What is the "Bottle Deposit" line on my invoice?</A></td></tr>
                            <tr><td><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>			
							<tr><td valign=top><li type="dot"></li></td><td class="bodyCopy"><A HREF="#question15">What if my order contains beer or wine?</A></td></tr>							
						</table>
				   </TD>
				</TR>
				

			</TABLE>

			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question1"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What is FreshDirect Depot?</b><BR>
						FreshDirect's Corporate Depot Program is offered by companies, hospitals, and office parks in conjunction with these entities' "Work-Life Benefit Programs." Participating employees and tenants will have access to FreshDirect's irresistibly good food, great prices, and delivery at the end of the business day. As a depot customer, you'll shop exactly the same way as a home delivery customer. Instead of delivering to your home, we'll bring orders to an area of your company parking lot on designated days. After work, just swing by our refrigerator/freezer FreshDirect truck, pick up your order, and head home.<br>
				   </TD>
				</TR>
			</TABLE>
			<BR>

			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question3"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>How far in advance should I place my depot order?</b><BR>
						Orders can be placed as early as seven days in advance of delivery, and as late as midnight the night before delivery. For example, if your company receives delivery on Thursdays, place your order anytime from Friday morning to midnight on Wednesday.<br>
				   </TD>
				</TR>
			</TABLE>
			<BR>
			
			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question4"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>When does FreshDirect deliver?</b><BR>
						The delivery day and time for FreshDirect <%=depotName%> depot locations is listed below. <BR>
						<br>
						<%boolean allowSelection =  false;%>
						<%@ include file="/shared/includes/i_depot_locations.jspf"%>
				   </TD>
				</TR>
			</TABLE>
			<BR>
			
			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question5"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>How does depot delivery work?</b><BR>
						On designated delivery days and hours, we'll dispense orders from a FreshDirect refrigerator/freezer truck in an area in your company parking lot. After work, simply pull your car into the area, and show your company identification to the driver. The driver will load the order into your trunk.
						<br><br>
						Please note that drivers cannot wait for an inspection of the items delivered. If anything is wrong with your order, please contact FreshDirect Customer Service right away.
						<br><br>
						Bad weather or other unforeseeable emergencies may force us to postpone or suspend chosen delivery dates and times. If there is a significant delay, a customer service representative will attempt to contact you to update you on delivery status.
						<BR>
				   </TD>
				</TR>
			</TABLE>
			<BR>
			
			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question6"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Is there a charge for depot delivery?</b><BR>
						There is a nominal delivery fee for depot delivery - just $3.95. The minimum order is $40.<BR>
				   </TD>
				</TR>
			</TABLE>
			<BR>
			
			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question7"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Should I tip the driver?</b><BR>
						You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service. FreshDirect delivery personnel are not permitted to solicit tips under any circumstances. If you have a comment or compliment please e-mail us. We'd love to hear from you!<br>
				   </TD>
				</TR>
			</TABLE>
			<BR>
			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question8"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What if I'm not at work to receive my order?</b><BR>
						On the day of delivery, if you're unable to pick up your order, please contact FreshDirect customer service ahead of time at <%=user.getCustomerServiceContact()%> to arrange for a colleague to pick it up on your behalf. Please note that your colleague will be responsible for delivering your order to you, so choose someone you know and like &#151; and be sure to ask their permission first.<br>
				   </TD>
				</TR>
			</TABLE>
			<BR>
			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question9"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What if my company is closed on the day of delivery?</b><BR>
						In the case of inclement weather or any other emergency that forces your company to close on a delivery day, we will work with you and your company's depot representative to make alternate delivery plans.<br>
				   </TD>
				</TR>
			</TABLE>
			<BR>
			<FONT CLASS="space4pix"><BR><br></FONT>
			<A NAME="question10"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What happens if I no longer work for the company?</b><BR>
						Our depot customers are only eligible for our service when employed by an affiliate company. When you no longer work for your company, your account will be deactivated and we will send you an e-mail listing your options. If home delivery is available in your area, you can register as an individual customer and continue using FreshDirect just as you had before. If not, we will send you an e-mail letting you know as soon as we do come to your area.<br>
				   </TD>
				</TR>
			</TABLE>
            <BR>
			<font class="space4pix"><br><br></font>
			<A NAME="question11"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Who is responsible for assembling my order?</b><BR>
						At FreshDirect, the only people who handle your food have special expertise in their area. So, whether we are selecting nice plump fruits and vegetables, cutting steaks, or packing your food into boxes, each member of our team takes pride in the quality of his or her products and handles every item with care.<br>
				   </TD>
				</TR>
			</TABLE>
			<br>
            <font class="space4pix"><br><br></font>
            <A NAME="question12"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>How long will my order stay fresh in the delivery boxes?</b><BR>
						Refrigerated perishables will stay fresh in the delivery boxes up to two hours after delivery. Frozen items should be placed in the freezer immediately.<br>

				   </TD>
				</TR>
			</TABLE>
            <br>
            <font class="space4pix"><br><br></font>
            <A NAME="question13"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>Why are there so many boxes in my order?</b><br>
FreshDirect was designed to be the perfect environment for food. We separate different types of food into 12 different climate zones to ensure the quality of our products. Our design for handling food has resulted in three separate assembly processes which create separate boxes for the following:
<ul>
<li>Meat, Seafood, and Produce items packed together 
<li>Dairy, Deli, Cheese, Coffee, and Tea items packed together
<li>Grocery, Specialty, and select non-refrigerated Produce (like bananas and tomatoes) packed together
</ul>
The number of items in a box has a direct relationship to the number of items ordered from a given department. Sometimes, boxes may be packed with only a few items depending on what you have ordered. We apologize for any inconvenience this may cause and encourage you to recycle your boxes. Unfortunately, FreshDirect cannot currently pick up or re-use boxes.
				   </TD>
				</TR>
			</TABLE>
            <br><font class="space4pix"><br><br></font>
            <A NAME="question14"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What is the "Bottle Deposit" line on my invoice?</b><BR>
						In accordance with New York State law, FreshDirect charges a bottle deposit on certain items. Your total deposit amount is displayed as a separate line item in your shopping cart and on your invoice. 
						<br><br>
						If you wish to recover this deposit, empty bottles and cans may be redeemed at any redemption center or you may bring them to our facility at 23-30 Borden Avenue, Long Island City, Queens. FreshDirect can only redeem bottles and cans of products that we sell. Our drivers cannot accept bottles and cans for redemption. Bottle deposit will not be charged for deliveries to New Jersey.
					</TD>
				</TR>
			</TABLE>
			<br><font class="space4pix"><br><br></font>
			<A NAME="question15"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy">
						<b>What if my order contains beer or wine?</b><BR>
						If your order contains alcoholic beverages, the person receiving your delivery must have identification proving they are over the age of 21 and will be asked for their signature. If no one over the age of 21 can sign for delivery, the driver will remove alcoholic beverages from the order and you will be charged a 50% restocking fee.<br>
				   </TD>
				</TR>
			</TABLE>
<BR>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="<%=tableWidth%>" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="<%=tableWidth%>" class="bodyCopy"><A HREF="#top"><img src="/media_stat/images/template/help/up_arrow.gif" width="17" height="9" hspace="0" vspace="4" border="0" align="left"><img src="/media/images/layout/clear.gif" width="6" height="1" border="0">top of page</A>
						<br><br><br>			
				   </TD>
				</TR>
			</TABLE>
