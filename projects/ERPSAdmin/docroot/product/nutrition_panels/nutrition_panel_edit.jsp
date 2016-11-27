<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.erp.security.SecurityManager' %>
<%
	boolean hasSample = false; 
	try { hasSample = Boolean.parseBoolean( request.getParameter("sample") ); } catch( Exception e) {};
%>
<fd:NutritionPanelController skuCode='<%=request.getParameter("skuCode")%>' type='<%=request.getParameter("type")%>' redirectUrl='/ERPSAdmin/product/product_view.jsp' sample="<%=hasSample %>"/>

<tmpl:insert template='/common/templates/main_with_doctype.jsp'>

	<tmpl:put name="extracss" direct="true">
    <link rel="stylesheet" href="/assets/css/common/autocomplete.css" type="text/css" />
  </tmpl:put>

	<tmpl:put name="content" direct="true">
		<h1 style="width:600px;background:black;color:white;font-size: 11px;line-height: 18px;font-weight: bold;text-transform: uppercase;letter-spacing: 2px;margin:0;">DRUG NUTRITIONAL INFORMATION</h1>
		<div style="width:600px;text-align:right"><a href="/ERPSAdmin/product/product_view.jsp?skuCode=<%= pageContext.getAttribute("skuCode") %>">Back to Product</a></div>
		<script src="/assets/javascript/fd/modules/common/utils.js"></script>
		<script src="/assets/javascript/fd/modules/common/autocomplete.js"></script>
		<script src="/assets/javascript/nutrition_panel.js"></script>
    <div class="editorcontainer">
		<% if(SecurityManager.isUserAdmin(request)) {%>	
      <%-- editor --%>
			<button id="savebutton">Save panel</button><button id="refreshbutton">Force preview refresh</button><button id="deletebutton">Delete panel</button>
      <div id="editorpanel"></div>
		<% } %>

    <%-- preview --%>
    <div id="previewpanel"></div>
    <script>
      var previewPanelConfig = {
          events: false,
          view: 'display',
          container: $('#previewpanel')
      };
      var previewPanel = drugPanel(jQuery,<%= pageContext.getAttribute("panel") %>, previewPanelConfig);
    </script>


		<% if(SecurityManager.isUserAdmin(request)) {%>	
      <%-- editor --%>
      <script>
        var editorPanelConfig = {
            events: true,
            view: 'editor',
            container: $('#editorpanel'),
            refreshCallback: previewPanel.refresh
        };
        drugPanel(jQuery,<%= pageContext.getAttribute("panel") %>, editorPanelConfig);
      </script>
      <form method="post" url="#" id="saveform"><input type="hidden" name="panel"><input type="hidden" name="skuCode"></form>
      <form method="post" url="#" id="deleteform"><input type="hidden" name="delete"><input type="hidden" name="skuCode"></form>
		<% } %>
    </div>
	</tmpl:put>
</tmpl:insert>
