<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.erp.security.SecurityManager' %>
<fd:DrugController redirectUrl="/ERPSAdmin/product/product_view.jsp"/>
<tmpl:insert template='/common/templates/main_with_doctype.jsp'>
	<tmpl:put name="content" direct="true">
		<h1 style="width:600px;background:black;color:white;font-size: 11px;line-height: 18px;font-weight: bold;text-transform: uppercase;letter-spacing: 2px;margin:0;">DRUG NUTRITIONAL INFORMATION</h1>
		<div style="width:600px;text-align:right"><a href="/ERPSAdmin/product/product_view.jsp?skuCode=<%= pageContext.getAttribute("skuCode") %>">Back to Product</a></div>
		<script>var drugPanelConfig={  };</script>
		<% if(SecurityManager.isUserAdmin(request)) {%>	
			<script>drugPanelConfig.events = true;</script>
			<script>drugPanelConfig.display = 'editor';</script>
			<button id="savebutton">Save panel</button><button id="switchbutton">Switch view</button>
		<% } %>
		<div id="drugpanel"></div>
		<script>drugPanelConfig.container=$('#drugpanel');</script>
		<script src="/assets/javascript/drug_nutrition_editor.js"></script>
		<script>drugPanel(jQuery,<%= pageContext.getAttribute("panel") %>, drugPanelConfig)</script>
		<% if(SecurityManager.isUserAdmin(request)) {%>	
		<button id="deletebutton">Delete panel</button>
		<form method="post" url="#" id="saveform"><input type="hidden" name="panel"><input type="hidden" name="skuCode"></form>
		<form method="post" url="#" id="deleteform"><input type="hidden" name="delete"><input type="hidden" name="skuCode"></form>
		<% } %>
	</tmpl:put>
</tmpl:insert>