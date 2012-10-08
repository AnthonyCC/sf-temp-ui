<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.erp.security.SecurityManager' %>
<fd:DrugController/>
<tmpl:insert template='/common/templates/main_with_doctype.jsp'>
	<tmpl:put name="content" direct="true">
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
		<form method="post" url="#" id="saveform"><input type="hidden" name="panel"></form>
		<form method="post" url="#" id="deleteform"><input type="hidden" name="delete"></form>
		<% } %>
	</tmpl:put>
</tmpl:insert>