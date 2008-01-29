<#include "fd-macros.ftl" />
<#setting locale="en_US"/>
<#assign isPageNull=parameters["isPageNull"]/>
<#assign deliveryInfoFaq=parameters["deliveryInfoFaq"]/>
<#assign customerServiceContact=parameters["customerServiceContact"]/>
<#assign baseDeliveryFee=parameters["baseDeliveryFee"]/>
<#if isPageNull>
<#assign tableWidth="375"/>
<#assign isPopup=true/>
<#assign securityPolicyLink="javascript:linkTo('/help/privacy_policy.jsp')"/>
<#assign hdrImgName="faq_hdr_pop_delivery.gif"/> 
<#assign hdrImgWidth="380"/>
<#assign hdrImgHeight="30"/>
<#else>
<#assign tableWidth="500"/>
<#assign securityPolicyLink="/help/privacy_policy.jsp"/>
<#assign hdrImgName="faq_hdr_delivery.gif"/> 
<#assign hdrImgWidth="453"/>
<#assign hdrImgHeight="30"/>
</#if>
<#assign isPopup=false/>
<#assign isDlvInfo=false/>
<#if deliveryInfoFaq>
<#assign isDlvInfo=true/>
<#assign tableWidth="683"/>
</#if>
<#assign helper=parameters["helper"]/>
<script>
function linkTo(url){
	redirectUrl = "http://" + location.host + url;
	parent.opener.location = redirectUrl;
}
</script>
<A NAME="top"></A>
			<TABLE width="${tableWidth}" CELLPADDING="0" CELLSPACING="0" BORDER="0">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD>
					<img src="/media_stat/images/template/help/${hdrImgName}" width="${hdrImgWidth}" height="${hdrImgHeight}" alt="" border="0">
				   </TD>
				</TR>
			</TABLE>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<img src="/media_stat/images/layout/cccccc.gif" width="${tableWidth}" height="1"><br><img src="/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0"><br>
						<ul>
							<li><A HREF="#question1">Where does FreshDirect deliver?</A></li>
							<li><A HREF="#question2">When does FreshDirect deliver?</A></li>
							<li><A HREF="#question3">Who is responsible for assembling my order?</A></li>
							<li><A HREF="#question4">How does delivery work?</A></li>
							<li><A HREF="#question5">Is there a charge for delivery?</A></li>
							<li><A HREF="#question_fuel">What is the Fuel Surcharge?</A></li>
							<li><A HREF="#question6">Should I tip the driver?</A></li>
							<li><A HREF="#question7">What if I'm not home to receive my order?</A></li>
							<li><A HREF="#question8">How long will my order stay fresh in the delivery boxes?</A></li>
							<li><A HREF="#question9">Why are there so many boxes in my order?</A></li>
							<li><A HREF="#question10">Can I pick up my order from your facility?</A></li>
							<li><A HREF="#question11">What is the "Bottle Deposit" line on my invoice?</A></li>
							<li><A HREF="#question12">What if my order contains beer or wine?</A></li>
						</ul>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question1"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>Where does FreshDirect deliver?</b><br>
						We deliver to certain neighborhoods in New York. Very soon, we'll be delivering to every address in Manhattan as well as parts of Brooklyn and Queens. To find out if FreshDirect is in your area now, <a href="javascript:popup('/help/delivery_zones.jsp','large')">click here</a>.<br>

				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question2"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP" class="bodyCopy">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>When does FreshDirect deliver?</b><br>
						We deliver as early as the day after you place your order, seven days a week, in two-hour delivery slots, so you won't have to wait around. On weekdays, our trucks are rolling from 2:00 p.m. to 11:30 p.m. On weekends we deliver all day, starting at 7:30 a.m. You can schedule a delivery time up to a week in advance, or as late as the night before. Listed below is our standard schedule - the time slots and order deadlines for your neighborhood may vary slightly.
						<br><br>
${helper.includeMedia("/media/editorial/site_pages/delivery_plan_table.html", parameters)}
					    <br>
					We are closed on Thanksgiving Day and Christmas Day. Other changes to our delivery schedule and to order deadlines will be posted on the FreshDirect site.
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question3"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>Who is responsible for assembling my order?</b><br>
						At FreshDirect, the only people who handle your food have special expertise in their area. So, whether we are selecting nice plump fruits and vegetables, cutting steaks, or packing your food into boxes, each member of our team takes pride in the quality of his or her products and handles every item with care.<br>
				   </TD>
				</TR>
			</TABLE>
			<br>
			<A NAME="question4"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>How does delivery work?</b><br>
						When you check out, select a delivery time as soon as the next day. A FreshDirect driver will bring the order to your door (please make sure he or she can get into the building). You'll be able to recognize our drivers by their FreshDirect uniforms &#151; and by the boxes of carefully packed fresh food they'll have with them. They'll also hand you an itemized receipt.
						<br><br>
						After your second order, we recommend authorizing an alternate recipient in your building (read more below about alternate recipients).
						<br><br>
						Please note that in order to meet our delivery times for all of our customers, drivers can't wait for an inspection of the items delivered. If anything is wrong with your order, please contact FreshDirect Customer Service right away at ${customerServiceContact}.
						<br><br>
						Bad weather or other unforeseeable traffic emergencies may force us to postpone or suspend chosen delivery dates and times. If there is a significant delay, a customer service representative will call or e-mail to update you on your delivery status.
						<br>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question5"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">	
						<b>Is there a charge for delivery?</b><br>
						${helper.includeMedia("/media/editorial/site_pages/delivery_info.html", parameters)}
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question_fuel"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">	
						<b>What is the Fuel Surcharge?</b><br>
						In order to help offset the impact of dramatic rises in oil and fuel prices FreshDirect has added a fuel surcharge. The fuel surcharge helps cover not merely the increased cost of making deliveries to our customers, but also broad increases in commodity and utility prices that directly effect running &mdash; and refrigerating &mdash; our facility. <a href="javascript:popup('/shared/fee_info.jsp?type=fuel','large')">Click here for details</a>.<br>
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question6"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>Should I tip the driver?</b><br>
						You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service. FreshDirect delivery personnel are not permitted to solicit tips under any circumstances. If you have a comment or compliment please e-mail us. We'd love to hear from you!<br>

				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question7"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>What if I'm not home to receive my order?</b><br>
						If you think that you may not be home in time to receive your scheduled delivery, or if you miss your delivery, please call FreshDirect Customer Service at ${customerServiceContact}. When you're not home during your selected timeslot, we will do our best to redeliver your order later the same day (a &#36;${baseDeliveryFee} redelivery fee applies). If your order cannot be redelivered that day there is a restocking fee of 100% of the cost of perishable items in the order plus 25% of the cost of packaged goods plus 50% of the total amount of any beer items, excluding fees and tax.
						<br><br>
						We'll always try to deliver your order directly to you. If your building has a doorman, you may authorize him to receive orders on your behalf. After your second order, we recommend selecting a neighbor in your building to receive your order in case you're not home when we arrive. The only catch is that they are responsible for keeping your perishable items cold and your frozen items frozen until you pick them up, so choose a neighbor you know and like &#151; and be sure to ask their permission first. 

				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question8"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>How long will my order stay fresh in the delivery boxes?</b><br>
						Refrigerated perishables will stay fresh in the delivery boxes up to two hours after delivery. Frozen items should be placed in the freezer immediately.<br>

				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
                        <A NAME="question9"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>Why are there so many boxes in my order?</b><br>
FreshDirect was designed to be the perfect environment for food. We separate different types of food into 12 different climate zones to ensure the quality of our products. Our design for handling food has resulted in three separate assembly processes which create separate boxes for the following:
						<ul>
							<li>Meat, Seafood, and Produce items packed together</li>
							<li>Dairy, Deli, Cheese, Coffee, and Tea items packed together</li>
							<li>Grocery, Specialty, and select non-refrigerated Produce (like bananas and tomatoes) packed together</li>
						</ul>
The number of items in a box has a direct relationship to the number of items ordered from a given department. Sometimes, boxes may be packed with only a few items depending on what you have ordered. We apologize for any inconvenience this may cause and encourage you to recycle your boxes. Unfortunately, FreshDirect cannot currently pick up or re-use boxes.
				   </TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question10"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>Can I pick up my order from your facility?</b><br>
						Yes, anyone in the Tri-State area (including existing home delivery customers) can place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens. For details <#if isPopup><a href="javascript:linkTo('/help/delivery_lic_pickup.jsp')">click here</a><#else><a href="/help/delivery_lic_pickup.jsp">click here</a></#if>.<br>
				   </TD>
				</TR>
			</TABLE>
                        <br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question11"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>What is the "Bottle Deposit" line on my invoice?</b><br>
						In accordance with New York State law, FreshDirect charges a bottle deposit on certain items. Your total deposit amount is displayed as a separate line item in your shopping cart and on your invoice. 
						<br><br>
						If you wish to recover this deposit, empty bottles and cans may be redeemed at any redemption center or you may bring them to our facility at 23-30 Borden Avenue, Long Island City, Queens. FreshDirect can only redeem bottles and cans of products that we sell. Our drivers cannot accept bottles and cans for redemption. Bottle deposit will not be charged for deliveries to New Jersey.
					</TD>
				</TR>
			</TABLE>
			<br><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border=0><br>
			<A NAME="question12"></A>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy">
						<b>What if my order contains beer or wine?</b><br>
						If your order contains alcoholic beverages, the person receiving your delivery must have identification proving they are over the age of 21 and will be asked for their signature. If no one over the age of 21 can sign for delivery, the driver will remove alcoholic beverages from the order and you will be charged a 50% restocking fee. FreshDirect does not deliver alcohol outside NY state.<br>
				   </TD>
				</TR>
			</TABLE>
			<br>
			<TABLE CELLPADDING="0" CELLSPACING="0" BORDER="0" WIDTH="${tableWidth}" class="bodyCopy">
				<TR VALIGN="TOP">
					<TD WIDTH="15"><img src="/media/images/layout/clear.gif" width="15" height="1" alt="" border="0"></TD>
					<TD WIDTH="${tableWidth}" class="bodyCopy"><A HREF="#top"><img src="/media_stat/images/template/help/up_arrow.gif" width="17" height="9" hspace="0" vspace="4" border="0" align="left"><img src="/media/images/layout/clear.gif" width="6" height="1" border="0">top of page</A>
						<br><br><br>
				   </TD>
				</TR>
			</TABLE>
