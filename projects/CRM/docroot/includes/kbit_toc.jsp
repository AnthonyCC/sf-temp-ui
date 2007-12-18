<%@ taglib uri='logic' prefix='logic' %>

<% 
//csg.jsp
String[] customerServiceBasics = {
"csg.jsp","Customer Service Basics"//,
//"?param=supcontact", "Supervisor contact", 
//"?param=attendance", "Attendance guidelines", 
//"?param=daily", "Daily procedures", 
//"?param=training", "Training checklist"
};

//phone.jsp
String[] phoneSystemEtiquette = {
"phone.jsp","Phone System & Etiquette",
//"?show=scripts", "Call Scripts", 
"?show=technique", "Call Techniques", 
"?show=etiquette", "Etiquette",
"?show=system", "ACD Phone System" 
};

//procedure.jsp
String[] standardProcedures = {
"procedure.jsp","Standard Procedures",
"?show=CreditCard", "Adding Credit Card",
"?show=Credit", "Credits",
//"?show=CreditChef", "Credits, Chef\'s Table",
"?show=MakeGoodOrder", "Make good order"//, 
//"?show=LateDelivery", "Late delivery", 
//"?show=Scripts", "Scripts"
};

//policy.jsp
String[] fdPoliciesFaq = {
"policy.jsp","FreshDirect Policies & FAQ",
"?show=About", "Service and Business model", 
"?show=Signup", "Signup & Service Availability", 
"?show=Shopping", "Shopping", 
"?show=Delivery", "Delivery", 
"?show=Payment", "Payment", 
"?show=ChargeAuth", "Charges & Authorizations"//, 
//"?show=Promotion", "Promotion rules"
};

//crm.jsp
String[] crmSystem = {
"crm.jsp","CRM System",
"?show=Address", "Address Validation",
"?show=OrderTransaction", "Reading OrderTransaction"//, 
//"?show=Registration", "Registration", 
//"?show=CustomerAccount", "Customer Account", 
//"?show=Shopping", "Shopping", 
//"?show=Checkout", "Checkout",
//"?show=CreateCase", "Case creation guidelines"
};

//store.jsp
String[] storeProductFacts = {
"store.jsp","Store Product Facts",
//"?show=fru", "Fruit", 
"?show=veg", "Vegetables",
"?show=mea", "Meat", 
//"?show=sea", "Seafood", 
//"?show=del", "Deli",
//"?show=che", "Cheese",
//"?show=pas", "Pasta", 
"?show=cof", "Coffee", 
//"?show=tea", "Tea", 
"?show=bak", "Bakery"//,
//"?show=cat", "Catering",
//"?show=hmr", "Meals", 
//"?show=dai", "Dairy", 
//"?show=gro", "Grocery", 
//"?show=fro", "Frozen",
//"?show=hba", "Health & Beauty",
//"?show=win", "Wine"
};

List kbtoc = new ArrayList();
kbtoc.add(standardProcedures );
kbtoc.add(fdPoliciesFaq);
kbtoc.add(crmSystem);
kbtoc.add(storeProductFacts);
kbtoc.add(customerServiceBasics);
kbtoc.add(phoneSystemEtiquette);

String currentPage = request.getRequestURI();
String currentContent = request.getParameter("show");

String display = "none";

String basePage = "";
%>

<logic:iterate id='category' collection="<%= kbtoc %>" type="String[]" indexId="i">
	<% for (int n = 0; n < category.length; n++) { %>
		<% if (n == 0) { 
			basePage = category[n];
		} else if (n == 1) {%>
			<div style="border-bottom: solid 1px #999999; "><a href="#" onclick="toggleDisplay('<%="category"+i.intValue()%>'); return false" class="icon">&plusmn;</a> <b><%=category[n]%></b></div>
			<div id="<%="category"+i.intValue()%>" style="padding: 3px; padding-left: 16px; display:<%= currentPage.indexOf(basePage) > -1?"block": "none"%>; height:auto;">
		<% } else if (n%2 == 0) { %>
			<div <%= currentContent!=null && !"".equals(currentContent) && category[n].endsWith(currentContent) ? "style=\"background: #99CCFF; color: #FFFFFF; padding-left: 4px; padding-right: 4px; text-decoration: none; font-weight: bold;\">&raquo;" :">&middot;"%>  <a href="<%=basePage%><%=category[n]%>">
		<% } else { %>
			<%=category[n]%></a></div>
		<% } %>
	<% } %>
	</div><br>
</logic:iterate>
