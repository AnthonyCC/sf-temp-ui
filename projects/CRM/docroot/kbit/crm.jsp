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
String section = request.getParameter("section");
 %>
 
<% if ("checkout".equalsIgnoreCase(show)) { %>
	<% if (section != null && section.indexOf("review") > -1) { %>
		Review items
	<% } else if (section != null && section.indexOf("select_address") > -1) { %>
		At this stage, you should ask the customer to verify his/her home delivery address or pickup location. All address fields are editable in this step, you can also add new delivery addresses. When you are finished, click [CONTINUE CHECKOUT].
	<% } %>
<% } %>

<% if ("overview".equalsIgnoreCase(show)) { %>

<b>Usage Tips:</b>
<ul>
	<li>IE 5.5 and up</li>
	<li>Maximize browser to full screen for best viewing (minimum resolution: 1280 x 960)</li>
	<li>Avoid using browser's 'Back' button at all costs!</li>
	<li>User account changes take effect after logging out and logging in again</li>
	<li>Cases can be created from 3 locations (Order Details, Case History, Home).
			<ul>
			<li>To attach order number and customer, case must be created from 'Order Details' page.</li>
			<li>To attach a customer, create new case from 'Case History'.</li>
			<li>For a fully unattached case (no customer or order), create case from 'Home'.</li>
			</ul>
	</li>
	<li>Look for 'Print Version' option before printing</li>
	<li>Report bugs or request features using the 'CRM Issue' form in Outlook (File > New > Choose Form) available in the 'CRM Issue Tracking' folder under Public Folders</li>
</ul>

<b>Navigation Directory:</b> (top > bottom)
<ul><li>
	Main nav - access main CRM sections (top most)
					<ul>L
					<li>'Home' - back to search/index page & clear customer</li>
					<li>'Worklist' - list of current open & assigned cases</li>
					<li>'Promotions' - all available promotions</li>
					<li>'Maps' - rollout maps sent</li>
					<li>'Help' - usage tips & navigation directory</li>
						<li>'Last Search: Customer' - back to last customer search performed (if has search results)</li>
					</ul>
					<br>
					<ul>R
					<li>'Logout' - sign out from CRM application</li>
					<li>'<img src="/media_stat/crm/images/fd_icon.gif" width="13" height="14">' icon - open www.freshdirect.com in new window</li>
					</ul> 
	</li>
	<br>					
	<li>				
	Customer nav - access customer account details (under customer header if has customer in session)
					<ul>
					<li>'Account Details' - add/edit contact info, username password, status eligibility, address & credit cards</li>
					<li>'Case History' - all created cases for this customer</li>
					<li>'Order History' - all placed orders for this customer</li>
					<li>'Credit History' - all issued credits, pending, approved & totals</li>
					<li>'Promotion History' - all used promotions</li>
					<li>'New Order' - place new order for customer</li>
					<li>'Activity log' - customer account activity</li>
					</ul>
	</li>
	<br>
	<li>Case header - quicklink back to case/ customer, displays case summary.
				<ul>
					<li>'Case Number' - view case details if not on details page</li>
					<li>'Customer Name' - retrieve customer if not yet in session</li>
					<li>'Order Number' - view order detail if available</li>
					<li>'Unlock' - unlock case, return home or worklist if unlocked from worklist page</li>
				</ul>
	</li>
	<br>
	<li>	
	Section nav - subsections
	<br>New order/Checkout nav - access build order options, checkout options (when placing new order)
			<ul><li>Build Order</li>
				<ul>
				<li>'Search' - main search products page</li>
				<li>'Browse' - main departments page</li>
				<li>'QuickShop' - previous orders page</li>
				<li>'Checkout' - go to checkout, default to 'review items'</li>
				</ul>
			</ul>
			<br>
			<ul><li>Checkout</li>
				<ul>
				<li>'Build Order' - back to build order, access above links</li>
				<li>'1: Review Items' - back to step 1</li>
				<li>'2: Select address & payment' - back to step 2</li>
				<li>'3: Select delivery time' - back to step 3</li>
				</ul>
			</ul>
		<br>
		<ul><li>Order nav - access placed order options (when viewing order details, only actionable buttons are shown)</li>
				<ul>
					<li>'View Transactions' - all actions on this order</li>
					<li>'Reverse Credits' - reverse an approved and unused credit</li>
					<li>'Process Return' - return order, subject to approval</li>
					<li>'Resubmit Order' - resubmit</li>
					<li>'Modify Order' - modify</li>
					<li>'Cancel Order' - cancel</li>
					<li>'Issue Credit' - issue credits on Settled orders, subject to approval above certain value</li>
					<li>'Payment Exception' - view order exception</li>
					<li>'New Case' - create a new case for this order</li>
				</ul>
		</ul>
		</ul>
	</li>
</ul>
<% } %>
					
					
<% if ("address".equalsIgnoreCase(show)) { %>
<span class="title18">Address Validation</span>
<br><br>
All fields marked with an asterisk * are required.<br>
If a failure message is encountered, verify each required field again with customer (<a href="/kbit/phone.jsp?show=Etiquette#Keyspell">keyspelling</a>).<br><br> 
Possible failure message combinations and actions to take after re-verification:
<br><br>
<b>ADDRESS BAD, GEOCODE FAIL</b><br>
Location needs to be added to our database. Alert supervisor, provide full address given. Supervisor, contact App Support for resolution.
<br><br>
<b>ADDRESS BAD, GEOCODE OK</b><br>
Location needs to be added to the list of Exceptions. Alert supervisor, provide full address given. Supervisor, enter problem address into Exception table.
<br><br>
<b>APT WRONG, GEOCODE OK</b><br>
Verify apartment number/name and click 'Add Apartment' to add to our database; or enter one from list shown and re-Check address
<% } %>

<% if ("Shopping".equalsIgnoreCase(show)) { %>
shopping
<% } %>

<% if ("CreateCase".equalsIgnoreCase(show)) { %>
create case
<% } %>

<% if ("OrderTransaction".equalsIgnoreCase(show)) { %>
<span class="title18">Reading back Order Transactions</span>
<br><br>
Precede each statement with date and time action was executed.
<br><br>
<b>Create Order</b><br>
Order #xxx created with an <a href="/kbit/policy.jsp?show=Shopping#EstPrice">estimated</a> total of $_AMOUNT.<br>
Email sent confirming items ordered & <a href="/kbit/policy.jsp?show=Shopping#EstPrice">estimated price</a>.<br>
Authorization received from Credit Card company in the amount of $_AMOUNT.<br>
FreshDirect authorizes up to 125% for perishables and 100% non-perishables.
<br><br>
<b>Modify Order</b><br>
Order #xxx modified with a new <a href="/kbit/policy.jsp?show=Shopping#EstPrice">estimated</a> total of $_AMOUNT.<br>
Email sent confirming items ordered & <a href="/kbit/policy.jsp?show=Shopping#EstPrice">estimated price</a>.<br>
Second authorization received from Credit Card company if necessary to secure funds in the amount of $_AMOUNT. Authorization(s) is/are not dropped until 24-72 hrs after an order is settled.<br>
FreshDirect authorizes up to 125% for perishables and 100% non-perishables.
<br><br>
<b>Invoice</b><br>
Order #xxx confirmed produced and packaged.<br>
Email invoice sent confirming items delivered and final price.
<br><br>
<b>Capture</b><br>
Order #xxx confirmed delivered.<br>
Note is sent to Bank or Credit Card company requesting to release funds.
<br><br>
<b>Settled</b><br>
Order #xxx is finalized.<br>
After receipt of requested funds from Bank or Credit Card Company, Authorization Hold release is initiated.<br>
It's typically done within 24-72 hrs, is dependent on the Bank or Credit Card Company and is out of FreshDirect's control.
<br><br>
<b>Store Credit</b><br>
Credit issued on customer's account to be used for subsequent purchases.
<br><br>
<b>Cash Back</b><br>
Refund issued on customer's Credit Card.<br>
<% } %>
</tmpl:put>

</tmpl:insert>
						

						
	
	
