<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<%--
	Require:
	isSummary boolean
	show string parameter
	section string parameter
--%>

<tmpl:insert template='/template/kbit.jsp'>

    <tmpl:put name='title' direct='true'>Help - CRM System</tmpl:put>
	
    <tmpl:put name='content' direct='true'>

<% 
String show = request.getParameter("show");
 %>
 
<a name="top"></a>

<% if ("About".equalsIgnoreCase(show)) { %>
	<b><span class="title18">Service & Business Model FAQs</span>
	<ol>
	<li><a href="#Who" class="topic">Who we are</a></li>
	<li><a href="#Service" class="topic">Why is FreshDirect not delivering to all of New York City?</a></li>
	</ol></b>
	<hr class="grey1px">
	<a name="Who"></a>
	<span class="topic_header"><b>How do I sign up?</b></span><br><br>
	To sign up on-line, visit us at <b>www.freshdirect.com</b>. You can then browse our website by department or search for any specific items that you want. 
	When placing an order, select an item, choose the quantity and any other options that are available for that item and then add it to your cart. 
	Once you have everything you need in your cart, proceed to checkout. You may then select a 2-hour delivery slot as early as the next day. 
	Pay by credit card (Visa, MasterCard, Amex, Discover - debit cards backed with a Visa or MasterCard logo are also accepted); 
	your card will not be charged until we have filled your order on the day of delivery and with us your credit card number will always be encrypted for your protection.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="Service"></a>
	<span class="topic_header"><b>Why is FreshDirect not delivering to all of New York City?</b></span><br><br>	
	Unfortunately, we cannot service all of New York City at this time. We are expanding to new areas as quickly as possible. By entering your email address in our zip code and address check, we will inform you as soon as we know when we will be delivering in your area. We can tell what areas have the greatest demand for our services and this information helps us decide which areas we will expand to next.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>

<% if ("Signup".equalsIgnoreCase(show)) { %>
	<b><span class="title18">Signup & Service Availability FAQs</span>
	<ol>
	<li><a href="#Signup">How do I sign up?</a></li>
	<li><a href="#Service">Why is FreshDirect not delivering to all of New York City?</a></li>
	</ol></b>
	<hr class="grey1px">
	<a name="Signup"></a>
	<b>How do I sign up?</b><br>
	To sign up on-line, visit us at <b>www.freshdirect.com</b>. You can then browse our website by department or search for any specific items that you want. 
	When placing an order, select an item, choose the quantity and any other options that are available for that item and then add it to your cart. 
	Once you have everything you need in your cart, proceed to checkout. You may then select a 2-hour delivery slot as early as the next day. 
	Pay by credit card (Visa, MasterCard, Amex, Discover - debit cards backed with a Visa or MasterCard logo are also accepted); 
	your card will not be charged until we have filled your order on the day of delivery and with us your credit card number will always be encrypted for your protection.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="Service"></a>
	<b>Why is FreshDirect not delivering to all of New York City?</b><br>	
	Unfortunately, we cannot service all of New York City at this time. We are expanding to new areas as quickly as possible. By entering your email address in our zip code and address check, we will inform you as soon as we know when we will be delivering in your area. We can tell what areas have the greatest demand for our services and this information helps us decide which areas we will expand to next.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>

<% if ("Shopping".equalsIgnoreCase(show)) { %>
	<b><span class="title18">Shopping FAQs</span>
	<ol>
	<li><a href="#Order">How do I place an order with FreshDirect?  </a></li>
	<li><a href="#SaveCart">Will my cart be saved?</a></li>
	<li><a href="#EstPrice">What do you mean by Estimated Pricing?</a></li>
	<li><a href="#ChangeOrderPostCutoff">Why can't I make changes to my order after the order deadline time?</a></li>
	<li><a href="#Quickshop">What is Quickshop?</a></li>
	<li><a href="#PhoneFee">Why is there a fee for telephone orders?</a></li>
	</ol></b>
	<hr class="grey1px">
	<a name="Order"></a>
	<b>How do I place an order with FreshDirect?  </b><br>
	To place an order with FreshDirect, go to our website <b>www.freshdirect.com</b>, sign-up for an account. 
	You can then browse our website by department or search for a specific item.  
	Once you put the items into your cart, begin the Checkout process. During Checkout, you can add your credit card information and choose your delivery date and timeslot. 
	Your order will go thru an inventory check to make sure all items are available for when you want them. The last thing you do is review your order before you submit. 
	Once you have submitted your order you will be given an order number, and an email will be sent to you confirming your order with your estimated pricing. 
	On the day of your delivery you will receive another email with your final pricing, and you will also receive an invoice when your order is delivered.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="PhoneFee"></a>
	<b>Will my cart be saved?</b><br>
	As long as you log into your account, all items in your cart will be saved. When placing an order over the phone we will also save your cart.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="EstPrice"></a>
	<b>What do you mean by Estimated Pricing?</b><br>
	Estimated pricing is the actual price per pound of an item times the estimated weight of that item. 
	Since every price of perishable food naturally varies in weight and size, 
	while you shop we give you an estimated weight and price for everything priced by the pound. 
	The exact weight of that item can not be know until we assemble your order. 
	Sometimes this may be a little more or less than you ordered, but you will always pay the true price per pound of an item.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="ChangeOrderPostCutoff"></a>
	<b>Why can't I make changes to my order after the order deadline time?</b><br>
	FreshDirect's production is done in the overnight hours, just after the order deadline time.
	By morning, your order is already produced and being packed on one of our refrigerated trucks.  
	It's all automated and unfortunately manual accommodations cannot be made.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="Quickshop"></a>
	<b>What is Quickshop?</b><br>
	Quickshop is a feature which allows you to shop from existing orders. To use our Quickshop feature, just click on the Quickshop icon, and choose the order you wish to shop from, 
	or all items ever ordered. Move selected items to your cart, and then to add more items just click on the Continue Shopping from Homepage link. 
	Add items to your cart then when you are done shopping simply Checkout.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="PhoneFee"></a>
	<b>Why is there a fee for telephone orders?</b><br>
	Since we are an Internet based company and we would like to maintain the lowest cost possible to you the consumer, our telephone order fee is implemented to try to maintain that balance. 
	This way, we may continue offering you the highest quality products at the lowest possible prices.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>

<% if ("Delivery".equalsIgnoreCase(show)) { %>
	<b><span class="title18">Delivery FAQs</span>
	<ol>
	<li><a href="#WhenDlv">When do you deliver?</a></li>
	<li><a href="#CheckTimeslot">How can I check delivery timeslots?</a></li>
	<li><a href="#ChangeDlvPostCutoff">Why can't I change the delivery time after cutoff time?</a></li>
	<li><a href="#ChangeDatePostCutoff">Why can't I change the delivery date after cutoff time?</a></li>
	<li><a href="#SameDayDlv">Why doesn't FreshDirect do same day deliveries?</a></li>
	<li><a href="#LeaveOrder">Why can't you just leave my order outside my door?</a></li>
	<li><a href="#WaiveDlvFee">Can you waive the delivery fee if I place a large order?</a></li>
	<li><a href="#RunningTrucks">Why does FreshDirect keep trucks running outside my building?</a></li>
	</ol></b>
	<hr class="grey1px">
	<a name="WhenDlv"></a>
	<b>When do you deliver?</b><br>
	Our delivery service is available in the evenings during the week and all day on the weekends within 2-hour intervals. You can order today for delivery tomorrow! Our uniformed personnel will deliver your order directly to your door, so you must be home to accept your order or choose the doorman option and we will leave it with your doorman. 
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="CheckTimeslot"></a>
	<b>How can I check delivery timeslots?</b><br>
	You can go to our Deliver Information page, click on Check Available Home Delivery Timeslots and then enter your address. 
	The website will then show you the available timeslots for the next 7 days. 
	We cannot guarantee that all timeslots will still be available when you have completed your checkout, 
	because other customers may checkout before you do and choose the last available slot in that timeframe.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="ChangeDlvPostCutoff"></a>
	<b>Why can't I change the delivery time after cutoff time?</b><br>
	Our system is completely automated, including the routing of our trucks.  It's all automated and unfortunately manual accommodations cannot be made.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="ChangeDatePostCutoff"></a>
	<b>Why can't I change the delivery date after cutoff time?</b><br>
	Since your order was produced in the overnight hours, your items have already been prepared for you, so if you do not accept delivery today, we would have to dispose of the perishable items, and restock grocery items. You may also be liable for restocking fees, if you order is not delivered to you today. Because we want to deliver the freshest items, we do not hold order to be delivered another day.  Is there, perhaps, a neighbor or doorman who will accept your order?
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="SameDayDlv"></a>
	<b>Why doesn't FreshDirect do same day deliveries?</b><br>
	FreshDirect's production is done in the overnight hours, just after the order deadline time. Once production is completed for that days orders our facility is sanitized for the next days production, so we are unable to process any orders the same day.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="LeaveOrder"></a>
	<b>Why can't you just leave my order outside my door?</b><br>
	FreshDirect requires a signature for each delivery, either someone in your household or an alternate delivery person such as a neighbor or a doorman, must accept delivery and sign for your order."
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="WaiveDlvFee"></a>
	<b>Can you waive the delivery fee if I place a large order?</b><br>
	Since FreshDirect wants to give our customers the best possible prices, we have a delivery fee for each order. This way we can offset the cost of delivery without raising prices.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="RunningTrucks"></a>
	<b>Why does FreshDirect keep trucks running outside my building?</b><br>
	When our trucks are parked, we do not keep the engines running, but we must keep the refrigeration units running to keep the food inside fresh. 
	The noise you hear is the refrigeration, not the engines of the trucks.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>

<% if ("Payment".equalsIgnoreCase(show)) { %>
	<b><span class="title18">Payment Method FAQs</span>
	<ol>
	<li><a href="#CCInfo">Is my credit card information safe?</a></li>
	<li><a href="#3StateCC">Why doesn't FreshDirect accept out-of-state credit cards?</a></li>
	<li><a href="#ChangeCCInfo">What if I don't want to change my billing information on my credit card?</a></li>
	<li><a href="#DuplicateCC">Why can't I use the same credit card on more than one account?</a></li>
	<li><a href="#ErrCCSelect">[Error Message] <i>There was a problem with the credit card you selected. Please choose or add another payment method.</i></a></li>
	<li><a href="#ErrCCApt">[Error Message] <i>Sorry, we're unable to recognize this apartment #. Please make sure it's entered correctly.</i></a></li>
	</ol></b>
	<hr class="grey1px">
	<a name="CCInfo"></a>
	<b>Is my credit card information safe?</b><br>
	All pages containing personal and financial information are highly secured and in fact both your credit card and password information are encrypted, 
	so that not even our Customer Service Agents can see them.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="3StateCC"></a>
	<b>Why doesn't FreshDirect accept out-of-state credit cards?</b><br>
	FreshDirect does accept out-of-state credit cards, but since FreshDirect only has access to the Post Office database for NY, NJ, and CT we can only accept billing addresses within those 3 states.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="ChangeCCInfo"></a>
	<b>What if I don't want to change my billing information on my credit card?</b><br>
	If you do not want to change your credit card billing address information to the delivery address, you can always contact your credit card company and 
	add the delivery address as an alternate address on your credit card. Then when filling out the credit card information on our website, 
	list the delivery address as the billing information. We can confirm billing and alternate addresses with your credit card company.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="DuplicateCC"></a>
	<b>Why can't I use the same credit card on more than one account?</b><br>
	At this time FreshDirect only allows a credit card to be used on one account, to prevent the promotion from being used in ways we did not intent it to be used.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="ErrCCSelect"></a>
	<b>[Error Message] <i>There was a problem with the credit card you selected. Please choose or add another payment method.</i></b><br>
	Unfortunately we were unable to get authorization on the credit card you submitted.  If you would like to add another credit card to your account, I would be happy to enter that into the system for you.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="ErrCCApt"></a>
	<b>[Error Message] <i>Sorry, we're unable to recognize this apartment #. Please make sure it's entered correctly.</i></b><br>
	May I have the address you were trying to create the account with?  
	What we can do is verify the billing address with the issuing bank, and once we have verified the address, we can add it the apt number to our database. 
	May I have the credit card number and address as it reads on your bank statement?
	Once the information is verified and updated by our technical deptartment, we will call you back.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>

<% if ("ChargeAuth".equalsIgnoreCase(show)) { %>
	<b><span class="title18">Charges & Authorizations FAQs</span>
	<ol>
	<li><a href="#CCLimit">I have plenty of money on the card I gave you.</a></li>
	<li><a href="#2Charges">I am on my bank website and I see 2 charges, why did you charge me twice for the same order?</a></li>
	<li><a href="#125Auth">Why do you authorize for 125%?</a></li>
	<li><a href="#Hold">My bank says that you can release the hold.</a></li>
	</ol></b>
	<hr class="grey1px">
	<a name="CCLimit"></a>
	<b>I have plenty of money on the card I gave you.</b><br>
	There are many reasons why the authorization might not have been successful. For security reasons we only get a code, which tells us, the authorization was not successful.  
	In the mean time I would be more than happy to enter another credit card into the system.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="2Charges"></a>
	<b>I am on my bank website and I see 2 charges, why did you charge me twice for the same order?</b><br>
	(After reviewing the "order transactions", you will see an authorization and a settled charge). 
	What you are seeing is an authorization hold and the final charge. 
	When you placed your order, we authorized your card for 125% of the perishable items and 100% of the dry goods. 
	2-3 days after the delivery of your order, we settle with your bank for the final charges and the bank begins the process of releasing the authorization hold.  
	It usually takes the bank a few days to release the hold.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="125Auth"></a>
	<b>Why do you authorize for 125%? </b><br>
	We authorize for 125% of the perishable items because at the time you place your order, we are not sure exactly how much each item will weigh. 
	Our perishables are weighed by the ounce and pound and sometimes the final weight may be a little more or a little less than the estimated price 
	but you will always be charged for the guaranteed price per pound.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
	<a name="Hold"></a>
	<b>My bank says that you can release the hold.</b><br>
	We are unable to release authorization holds.
	<%@ include file="/includes/back_to_top.jspf" %><br><br>
<% } %>
						
</tmpl:put>

</tmpl:insert>
						

						
	
	
