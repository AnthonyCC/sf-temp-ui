<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/print.jsp'>

	<tmpl:put name='title' direct='true'>Help</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	
	<div class="content" style="padding-left: 8px;">

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
	</div>

	</tmpl:put>

</tmpl:insert>
	
						
						

						
	
	
