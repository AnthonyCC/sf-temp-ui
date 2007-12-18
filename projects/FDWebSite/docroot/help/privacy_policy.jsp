<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
String help = " Help -";
String template = "/common/template/dnav.jsp";
String type = request.getParameter("type");
if (type != null && !"".equals(type) && type.equalsIgnoreCase("popup")) {
	template = "/shared/template/large_pop.jsp";
	help = "";
}
%>

<tmpl:insert template='<%=template%>'>

    <tmpl:put name='title' direct='true'>FreshDirect -<%=help%> Privacy Policy</tmpl:put>

    <tmpl:put name='content' direct='true'>
<table width=500>
<tr>
	<td align=left>

<table width="500" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td align=left><img src="/media_stat/images/template/help/privacy_policy.gif" width="178" height="21" alt="" border="0"></td>
	<td align=right>
	<a href="javascript:window.print();"><img src="/media/images/navigation/global_nav/print_page_01.gif" alt="" border="0"></a>
	</td>
</tr>
<tr><td align=left><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
<tr><td bgcolor="#cccccc" colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" alt="" border="0"></td></tr>
<tr><td align=left><img src="/media_stat/images/layout/clear.gif" width="1" height="3" alt="" border="0"></td></tr>
<tr>
	<td align=left colspan=2>
	
At FreshDirect (the "Company," "us," "we," or "our"), we recognize and respect the importance of maintaining the privacy of our customers, and as a result, we have established this Privacy Policy. We have provided you with this policy statement to inform you of the kinds of information we may gather during your visit to the FreshDirect site (the "Site"), why we gather customer information, what we use the information for, under what circumstances we may disclose such information, and how you can instruct us to limit the use of that information. 
<br><br>
<i>Note: You must be 18 years old or older to register as a customer of FreshDirect.</i>
<br><br>
If you have any questions please e-mail our Customer Service Team at <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a> or call <%=user.isChefsTable()?"toll-free":"us"%> at <%=user.getCustomerServiceContact()%>. 
<br><br>
<b>1. What Information We Gather</b>
<br><br>
Traffic Data Collected
<br><br> 
We automatically track and collect the following information when you visit our Site: (i) your IP address; (ii) the type of computer you are using; and (iii) the type of Web browser you are using (collectively "Traffic Data"). Traffic Data is helpful for marketing purposes and for improving your experience on the Site.
<br><br>
Personal Information
<br><br>
In order for you to access this Site, you must enter your zip code. In order for you to purchase products that we offer via the Site, we require you to provide us with information that personally identifies you. We receive and store any information you enter anywhere on the Company's Site. For example, we may ask you to complete a registration form that asks for contact information (such as name, address, telephone number, and e-mail address) or an order form that asks for financial information (like credit card number, expiration date, and billing address). Additionally, when you order from the Site we may collect certain consumer information (such as products ordered and relevant promotion codes) and delivery information (such as an alternate contact, address, and telephone number in the event you are not available to accept delivery). This information is collectively referred to as "Personal Information." If you communicate with us by e-mail, or otherwise complete online forms, surveys, or contest entries, any information provided in such communication may be collected as Personal Information. You may choose not to provide us with certain Personal Information. In such an event, you can still access and view much of the Site; however, you may not be able to order any products. In addition, you can choose not to provide certain optional information, but then you might not be able to take full advantage of many of the features on the Company's Site. 
<br><br>
<b>2. How We Use the Information We Gather</b>
<br><br>
We use your Personal Information for such purposes as responding to your requests, processing and filling customer orders, verifying your qualifications for certain products and services, billing, improving our services, providing an enhanced and more personalized shopping experience, communicating with you, and informing you of special offers and discounts. From time to time, we may also use your Personal Information to send you free samples of new products or product brands different from those you usually order. We may provide your Personal Information to third parties to the extent necessary or desirable to fill your order or complete your transaction. For example, we may use a third party supplier for certain goods, an outside shipping company to ship orders, and we do as a matter of course use a credit card processing company to bill users for goods and services. We may also share your Personal Information as described in section 5 below. Except as provided above, and in section 5 below, we will not release your Personal Information to a third party unless you have granted us permission to do so.  
<br><br>
<b>3. How We Use "Cookies"</b>
<br><br>
A "cookie" is a piece of data stored on your hard drive containing information about you. FreshDirect uses cookies to identify you as a previous visitor and to allow our systems to remember your shopping cart, and other shopping information, as you browse our site. By allowing our systems to set a cookie on your computer, you will not have to log in a password more than once per visit, thereby saving you time while on our Site. Cookies can also enable us to learn your interests and enhance your experience on our Site. For example, we use cookies to let us know that you are a prior customer and to allow our systems to recall information on your product selections as you shop on our Site. We do not store any Personal Information (except your e-mail address) or financial information about you using cookies. Most Web browsers automatically accept cookies, but allow you to instruct your browser to prevent the use of cookies. If you disable this feature, you may still use our Site, but certain aspects of the shopping experience with FreshDirect may be impacted. 
<br><br>
<b>4. Log Files</b>
<br><br>
We use IP addresses to analyze trends, administer the Site, track users' movements, and gather broad demographic information for anonymous and aggregate use.  
<br><br>
<b>5. Sharing of Personal Information </b>
<br><br>
In addition to Section 2 above, we share demographic information with our partners and advertisers on an anonymous and aggregate basis. This type of data is not readily linked to any personally identifiable information.
<br><br>
We work closely with third parties to bring you our services. We share information when another company is involved in the transaction so that company can perform its functions.
<br><br>
<b>6. Requesting Information from Potential and Registered Customers </b>
<br><br>
Our Site provides (in areas accessible to nonregistered users) an order form for potential customers to request information about our products and services. We may direct additional communications, including e-mail, to registered customers and these potential customers that we deem may be of interest to them. Additionally, at times, we may request customer feedback about our products, our services, or your past experiences, which may be displayed on our Site and in other marketing materials. Participation in these programs is optional, and by participating you grant us a nonexclusive, royalty-free, perpetual, irrevocable, and fully sublicensable right to use, reproduce, adapt, publish, translate, distribute, and display all or parts of your feedback and you grant us permission to use your full name or your first name, last initial, and neighborhood, as determined by FreshDirect, in connection with your feedback. To be removed from future FreshDirect e-mail campaigns ("opt out" request) you can simply access and update your information in the <a href="/your_account/manage_account.jsp">Your Account</a> area on our Site, or notify us at <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a>.
<br><br>
<b>7. Market Research</b>
<br><br>
FreshDirect may secure the services of a market research firm to collect information from you to improve our service and product mix. Your feedback is optional and results will be used in aggregate reviews.   
<br><br>
<b>8. Limits to Your Privacy</b>
<br><br>
Our Site may contain links to other Web sites. FreshDirect wants you to be aware that when you click on links and/or ad banners that take you to third-party Web sites, you will be subject to the third parties' privacy policies, not ours. While we support the protection of privacy on the Internet, we are not responsible for the actions and privacy policies of third parties and other Web sites. We encourage you to read the posted privacy statement and user terms and conditions whenever interacting with, and prior to providing any personal information to, any other Web site. This Privacy Policy applies solely to information collected by the Company on this Site.
<br><br>
<b>9. Your Security</b>
<br><br>
FreshDirect takes precautions to protect your Personal Information. When our registration/order form asks users to enter sensitive information (such as credit card number), that information is encrypted and is protected with encryption software. Our Site encrypts your credit card number prior to transmission over the Internet using secure socket layer (SSL) encryption technology. This technology works best when the Site is viewed using Microsoft's Internet Explorer or Netscape Navigator. While on a secure page, such as our order form, the lock icon on the bottom of Web browsers such as Netscape Navigator and Microsoft Internet Explorer becomes locked, as opposed to unlocked, or open, when you are just "surfing."  
<br><br>
While we make reasonable efforts to safeguard your Personal Information once we receive it, no transmission of data over the Internet or any other public network can be guaranteed to be 100% secure. As a result, we cannot ensure or warrant the security of any information you transmit to us or information we transmit to you from our online products or services, and you do so at your own risk.
<br><br>
<b>10. Lost or Stolen Information</b>
<br><br>
You must promptly notify us if your credit card, user name, or password is lost, stolen, or used without permission. In such an event, we will note that you have canceled your credit card number, user name, or password in our system and will update our records accordingly.
<br><br>
<b>11. How to limit the use of your information</b>
<br><br>
Our users are given the opportunity to "opt out" of having their information used for purposes not directly related to placement, processing, fulfillment or delivery of a product order at the point where we ask for the information. If you prefer to "opt out" and not have us send you material we think may be of interest to you, such as product information, product samples, and promotional mailings/e-mails from us and sites and companies we own, then you can notify us in one of the following two ways: 
<br><br>
<table>
<tr>
	<td width=20></td>
	<td align=right valign=top>1.</td>
	<td width="20"></td>	
	<td valign="top">
		Writing us at:<br><br>
		Customer Advocacy Group<br>
		FreshDirect, LLC<br>
		23-30 Borden Avenue<br>
		Long Island City, New York 11101<br><br>
		If you have a catalog, brochure, or other mailing label from us,
		please include it with your request, otherwise just include your
		mailing address and your e-mail address.
		<br><br>
		or
		<br><br>
	</td>
</tr>
<tr>
	<td width=20></td>
	<td align=right valign=top>2.</td>
	<td width="20"></td>	
	<td valign="top">
		E-mailing us at <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a> (and include your mailing address).
	</td>
</tr>

</table>
<br><br>
Your instructions to limit the use of your information will be processed as soon as reasonably practicable. In addition, you may "opt out" by accessing and updating your information in the <a href="/your_account/manage_account.jsp">Your Account</a> area on our Site.
<br><br>
<b>12. Corrections</b>
<br><br>
If any of your Personal Information changes (such as your zip code), or if you no longer desire our service, we will correct, update, or remove that personal data provided to us. This can be done by sending an e-mail with your new information to <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a>. In addition, you may access and update your information in the <a href="/your_account/manage_account.jsp">Your Account</a> area on our Site.
<br><br>
<b>13. Your Consent and Changes to This Policy</b>
<br><br>
By using our Site, you consent to the collection and use of information by us in accordance with the terms of this Privacy Policy. If we decide to change our Privacy Policy, we will post those changes on our Site so you are always aware of what information we collect, how we use it, and under what circumstances, if any, we disclose it. By continuing to use this site after notice is given, you will have consented to our use of your information in this different manner. If at any time you would like notice of changes to the Privacy Policy or the Customer Agreement, e-mail us at <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a> from the e-mail address to which you would like the notice sent. 
<br><br>
<b>14. Disclosure Required by Law</b>
<br><br>
We may disclose Personal Information when required by law or when we believe in good faith that such action is necessary in order to conform to the edicts of the law or comply with a court order or legal process served upon FreshDirect or any of its officers or principals. We further reserve the right to disclose such information when FreshDirect reasonably believes that it is needed to identify, contact, or bring legal action against a party who may be violating FreshDirect's Terms of Use or may be causing injury to or interference with FreshDirect's rights or property, registered customers or potential customers of FreshDirect, or anyone else that could be harmed by such activities. 
<br><br>
<b>15. Your Comments</b>
<br><br>
FreshDirect welcomes feedback concerning its Privacy Policy. Please send your comments and questions to <a href="mailto:<fd:GetServiceEmail />"><fd:GetServiceEmail /></a>.
	</td>
</tr>
</table>
	</td>
</tr>
</table>	
	</tmpl:put>
</tmpl:insert>
