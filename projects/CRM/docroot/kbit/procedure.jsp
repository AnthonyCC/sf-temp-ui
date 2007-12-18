<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<tmpl:insert template='/template/kbit.jsp'>

    <tmpl:put name='title' direct='true'>Help - Standard Procedures</tmpl:put>
	
    <tmpl:put name='content' direct='true'>

<%--
	Require:
	isSummary boolean
	show string parameter
	section string parameter
--%>

<% String show = request.getParameter("show"); %>
 
<% if ("CreditCard".equalsIgnoreCase(show)) { %>
<span class="title18">Adding Credit Cards</span><br>
<b>There is a problem with the account information you entered. Please double check and make any changes or enter a different account.</b>
<br><br>
When you receive the following message while trying to add a credit card to a customers account, it means that the credit card is being used on another account.  
<ol><li>Copy the address, note the apartment number then click home. </li>
<li>While on the home page select advance under the customer search heading. </li>
<li>In the address field paste the address and put in the apartment number. </li>
<li>Click search customer</li>
<li>If more than one customer appears then check each account for the credit card in question. You will more than likely find the card on one of these accounts. If the card is not on any of the accounts, ask the customer if they have another account or if there are any other people in the house who might have created an account. If the card is not on any other accounts, please escalate the issue to account services.
</li></ol>
<% } %> 

<% if ("Credit".equalsIgnoreCase(show)) { %>
<span class="title18">Issuing a Credit</span><br>
Customers are often issued partial credit for their order when items are missing, damaged, spoiled, or otherwise not in satisfactory condition, or if a customer is overcharged for an item. 
The following section shows you how to issue a credit for an item in the CRM.
<ol>
<li>If you are not there already, go to the CRM Home Page by clicking on Home in the Tool Bar.
From the Home page, follow steps 1-5 from the section Create a New Case on pages 9-12. This case should be focused on the issue the customer has with the item that the credit is being issued for, so be sure to explain the situation in the Notes section.
When you are finished, go back to the Account Details page, and you should have Edit functionality in all of the available fields.<hr class="grey1px"></li>
<li>From the Account Details page, click [Order History]. Select the order from which the item being issued credit for is from, and click on it.<br>
<b>Result:</b> You will be directed to the Order Details page.<br>
Next to the order number in the top half of the screen are three new links: View Transaction, Reverse Credit, and Issue Credit. Click Issue Credit to continue.<hr class="grey1px"></li>
<li>The Issue Credit screen displays every item sent in that delivery. Select the items to be credited, and enter the reasons for the issuing of credit in the Notes field.
When you are finished issuing credits, click [Process Credit].
<b>Result:</b> A credit is issued to the customer's account, applicable to a future order, in the amount specified. Also, the "Enter credit notes here" field changes color when a credit has been successfully issued.
</li>
</ol>
<br><br>
<span class="title18">Reversing Credit</span>
<% } %> 

<% if ("MakeGoodOrder".equalsIgnoreCase(show)) { %>
<span class="title18"><b>Step by Step of A Make Good Order</b></span>
<ol>
<li>Create a case with specifics as to what is missing or damaged and what is going to be included in the make good order.<hr class="grey1px"></li>

<li>Click "Order History".<hr class="grey1px"></li>

<li>Click on the order that has the damaged or missing items.<hr class="grey1px"></li>

<li>Click on "Issue Credit".<hr class="grey1px"></li>

<li>Select the appropriate credit reason for each item that will be redelivered in the make good order.<hr class="grey1px"></li>

<li>DO NOT CHANGE THE 0 QUANTITY.<hr class="grey1px"></li>

<li>Page down to the "Credit Notes field" and enter your notes.<hr class="grey1px"></li>

<li>Under the "email options" select "Don't Send".<hr class="grey1px"></li>

<li>Page down and click "Process Credit".<hr class="grey1px"></li>

<li>At the top of the page click "Quickshop".<hr class="grey1px"></li>

<li>Zero out all quantities of items not being redelivered.<hr class="grey1px"></li>

<li>Click "Add Selected To Cart".<hr class="grey1px"></li>

<li>Click "Checkout".<hr class="grey1px"></li>

<li>Step 1 of checkout-Review order and click "Continue Checkout."<hr class="grey1px"></li>

<li>Step 2 of checkout-Make sure that the correct delivery address is selected if there is more than one option.<br> 
Make sure the correct billing address is selected if there is more than one option.<br>
Check the "This is a MAKE GOOD ORDER" box.<br>
Select the reference order number.<hr class="grey1px"></li>

<li>Step 3 of checkout-Select the delivery time and date for the make good order.<hr class="grey1px"></li>

<li>Step 4 of checkout-Review the order and make sure that under Payment Information, the payment type says Make Good and there is a Referenced Order Number.<br>
Page down and waive any delivery or phone order fees. Select "Place Order Now".<hr class="grey1px"></li>
</ol>

<% } %>

<% if ("LateDelivery".equalsIgnoreCase(show)) { %>
<span class="title18">Late delivery</span>
<% } %>
</tmpl:put>

</tmpl:insert>
