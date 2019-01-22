<%@ page import='com.freshdirect.content.nutrition.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.erp.model.*' %>
<%@ page import='com.freshdirect.erp.security.SecurityManager' %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DecimalFormat' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! 
	DecimalFormat formatter = new DecimalFormat();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>Erpsy - ProductView</title>
		<link rel="stylesheet" href="/ERPSAdmin/common/css/erpsadmin.css" type="text/css">
		<link type="text/css" rel="Stylesheet" href="/ERPSAdmin/batch/blackbirdjs/blackbird.css" />
		
		<script type="text/javascript" src="/assets/javascript/jquery/1.7.2/jquery.js"></script>
		<script type="text/javascript" src="/assets/javascript/jquery/ui/1.9.2/jquery-ui.min.js"></script>
		<script type="text/javascript" src="/ERPSAdmin/batch/blackbirdjs/blackbird.js"></script>
		<script type="text/javascript" src="/ERPSAdmin/product/json2.js"></script>
		<script type="text/javascript" src="http://www.freshdirect.com/assets/javascript/prototype.js"></script>
		<script>
			var FreshDirect = FreshDirect || {};
			FreshDirect.sku2url = {
					'base': {
						'FD': '<%= FDStoreProperties.getErpsyLinkStorefrontFD() %>',
						'FDX': '<%= FDStoreProperties.getErpsyLinkStorefrontFDX() %>'
					}
			};
		</script>
		<script type="text/javascript" src="/ERPSAdmin/product/erpsydaisysku2urladdon.js"></script>
		<script>
		function copyConfirm(value){
			if(value == ""){
				alert("Please enter a valid WebId");
				return false;
			} else {
				return confirm('Copy nutrition attributes from ' + value + '?');
			}
		}
		</script>
		<style>
			table.skuContTable, table.skuContTable td, table.keyTable, table.keyTable td {
				padding: 0;
				border-collapse: collapse;
			}
			table.skuContTable td.skuCont_tdRelatedPrimary {
				width: 50%;
			}
			table.skuContTable td.skuCont_tdRelatedSecondary div {
				border: 0 none;
			}
			td.skuCont_tdRelatedPrimary a {
				color: #BF0B34;
			}
			span.defSKU {
				color: #00f;
				font-weight: bold;
			}
			.orphan {
				text-decoration: line-through;
			}
			span.prefSKU {
				color: #CF8C19;
				font-weight: bold;
			}
			.defSKUBG {
				background-color: #00f;
			}
			.prefSKUBG {
				background-color: #CF8C19;
			}
			.localLinkBG {
				background-color: #7F7F7F;
			}
			.www1LinkBG {
				background-color: #BF0B34;
			}
			.www2LinkBG {
				background-color: #279F54;
			}
			.keyTableTD0 div div {
				margin-right: 3px;
				width: 16px;
			}
			.keyTableTD0 div {
				margin-right: 16px;
			}
			td.skuCont_tdRelatedSecondary a, td.skuCont_tdComponentsOpt a {
				color: #279F54;
			}
			td.skuCont_tdVirtual a, td.skuCont_tdRelatedPrimary span.siblingSKU a, span.siblingSKU a {
				color: #7F7F7F;
			}
			table td.skuCont_tdRelatedPrimary, table td.skuCont_tdRelatedSecondary, table td.skuCont_tdComponentsOpt, table td.skuCont_tdComponents {
				vertical-align: top;
				padding: 5px;
			}
			table td.skuCont_tdRelatedPrimary div, table td.skuCont_tdRelatedSecondary div, .skuCont_tdVirtual div, table td.skuCont_tdComponents div {
				width: 100%;
			}
			table td.skuCont_tdComponents div a.skuToggle {
				font-size: 9px;
				background-color: #666;
				color: #fff;
				padding: 0 2px 1px 2px;
				position: absolute;
				right: 5px;
				width: 70px;
				text-align: center;
			}
			/*.skuContTable, */
			table.keyTable {
				width: 100%;
				margin-bottom: 5px;
			}
			.keyTableTHKey {
				font-weight: bold;
			}
			table.keyTable div {
				float: left;
			}
			.skuContTableCont {
				display: inline-block;
			}
			.skuContTableBase {
				font-size: 11px;
				color: #fff;
				background-color: #000;
				font-weight: bold;
				padding: 3px;
			}
			.siblingSkus>div, .optProds>div, .component-label, .component-skus {
				padding-left: 15px;
			}
			.baseCont {
				border: 1px solid #333;
			}
			.baseCont.FD .skuContTableBase {
				background-color: #f90;
			}
			.baseCont.FDX .skuContTableBase {
				background-color: #808;
			}
			.skuContTable td {
				vertical-align: top;
			}
			.component {
				position: relative;
			}
		</style>
	</head>
	<body>
		<%
		if(!request.isUserInRole("ErpsyAdminGrp")) {
		%>
			<%@ include file='/common/templates/main1_readonly.jsp' %>
		<%	
		} else {
		%>
			<%@ include file='/common/templates/main1.jsp' %>
		<%}
		%>
		<div id="main">		
			<div id="content">			
<%	String skuCode = request.getParameter("skuCode");%>


        <fd:ProductSearch results='searchResults' searchtype='<%= request.getParameter("searchtype") %>' searchterm='<%= request.getParameter("searchterm") %>'>
            <form action="product_search.jsp" method="post">
                <table width="600" cellspacing=2 cellpadding=0>
                	<tr><td align="left" class="section_title">Search Products</td></tr>
                	<tr><td>
                		<input name=searchterm type=text size=30 value="">
                		<input type=submit value="FIND">
                        <a href="product_search.jsp">Return to Search Results</a><br>
                	</td></tr>
                    <tr><td>
                            <input type=radio name=searchtype value="SAPID" <%= ("SAPID".equals(searchtype)||"".equals(searchtype))?"CHECKED":"" %>> SAP ID
                            <input type=radio name=searchtype value="WEBID" <%= "WEBID".equals(searchtype)?"CHECKED":"" %>> Web ID
                            <input type=radio name=searchtype value="UPC"   <%= "UPC".equals(searchtype)?"CHECKED":"" %>> UPC
                            <input type=radio name=searchtype value="DESCR" <%= "DESCR".equals(searchtype)?"CHECKED":"" %>> SAP Description
                    </td></tr>
                </table>
            </form>
        </fd:ProductSearch>

		<% if(!SecurityManager.isUserAdmin(request)) {%>
			<%@ include file='/product/product_view_reader.jspf' %>
		<%} else { %>
            

			<%}%>

            <%  if (skuCode != null) { %>
                <fd:Nutrition id="nutrition" skuCode='<%= skuCode %>'>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Ingredients</td></tr>
                    </table>
                    <table width="600">
                        <tr><td><%= nutrition.getIngredients() %></td></tr>
						<% if(SecurityManager.isUserAdmin(request)) {%>
                        <tr><td align="left"><a href="ingredients_edit.jsp">Edit Ingredients</td></tr>
						<% } %>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Hidden Ingredients</td></tr>
                    </table>
                    <table width="600">
                        <tr><td><%= nutrition.getHiddenIngredients() %></td></tr>
						<% if(SecurityManager.isUserAdmin(request)) {%>
                        <tr><td align="left"><a href="hidden_ingredients_edit.jsp">Edit Hidden Ingredients</td></tr>
						<% } %>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Heating Instructions</td></tr>
                    </table>
                    <table width="600">
                        <tr><td><%= nutrition.getHeatingInstructions() %></td></tr>
						<% if(SecurityManager.isUserAdmin(request)) {%>
                        <tr><td align="left"><a href="heating_edit.jsp">Edit Heating Instructions</td></tr>
						<% } %>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Kosher</td></tr>
                    </table>
                    <table width="600">
                        <tr valign="middle"><td>
                            Kosher symbol: <% if (nutrition.getKosherSymbol() != null && nutrition.getKosherSymbol().display()){ %><img src="../images/kosher/<%= nutrition.getKosherSymbol().getName() %>.gif" width="30" height="30">
                                           <%}else{%> No Image<%}%>
                        </td></tr>
                        <tr><td><% if (nutrition.getKosherType() != null) { %>
                            Kosher type: <%= nutrition.getKosherType().getName() %>
                        <% } %></td></tr>
						<% if(SecurityManager.isUserAdmin(request)) {%>
                        <tr><td align="left"><a href="kosher_edit.jsp">Edit Kosher Information</td></tr>
						<% } %>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Claims, Allergens, Organic Statements</td></tr>
                    </table>
                    <table width="600">
                        <tr><td>
                            <table>
                                <tr><td width="200" valign="top">
                                    <b>Claims:</b><br>
                                    <logic:iterate id="claim" collection="<%= nutrition.getClaims() %>" type="com.freshdirect.content.nutrition.NutritionValueEnum">
                                    <%= claim.getName() %><br>
                                    </logic:iterate>
                                </td><td width="200" valign="top">
                                    <b>Allergens:</b><br>
                                    <logic:iterate id="allergen" collection="<%= nutrition.getAllergens() %>" type="com.freshdirect.content.nutrition.NutritionValueEnum">
                                    <%= allergen.getName() %><br>
                                    </logic:iterate>
                                </td><td width="200" valign="top">
                                    <b>Organics:</b><br>
                                    <logic:iterate id="organic" collection="<%= nutrition.getOrganicStatements() %>" type="com.freshdirect.content.nutrition.NutritionValueEnum">
                                    <%= organic.getName() %><br>
                                    </logic:iterate>
                                </td></tr>
                            </table>
                        </td></tr>
						<% if(SecurityManager.isUserAdmin(request)) {%>
                        <tr><td align="left"><a href="claims_edit.jsp">Edit Claims, Allergens and Organic Statements</td></tr>
						<% } %>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Nutritional Information</td></tr>
                    </table>
                    <table width="600">
                    
						<fd:NutritionPanelController skuCode="<%=skuCode%>" redirectUrl="/ERPSAdmin/product/product_view.jsp"/>
						
						<% if(SecurityManager.isUserAdmin(request)) {%>
							<% if( pageContext.getAttribute("panel") == null ) { %>
                <tr><td align="left">
                  Nutritional Information:
                  <select id="editnutritionselect">
                    <option value="nutrition_edit.jsp">Edit Classic Nutritional Information</option>
                    <option value="nutrition_panels/nutrition_panel_edit.jsp?skuCode=<%= skuCode %>&type=DRUG">Create Drug Nutrition Panel</option>
                    <option value="nutrition_panels/nutrition_panel_edit.jsp?skuCode=<%= skuCode %>&type=PET">Create Pet Nutrition Panel</option>
                    <option value="nutrition_panels/nutrition_panel_edit.jsp?skuCode=<%= skuCode %>&type=BABY">Create Baby Nutrition Panel</option>
                    <option value="nutrition_panels/nutrition_panel_edit.jsp?skuCode=<%= skuCode %>&type=SUPPL">Create Supplement Nutrition Panel</option>
                  </select>
                  <label><input id="samplecheckbox" type="checkbox" name="sample" value="true">Include sample data</label> 
                  <button id="editnutritionbutton">Ok</button>
                  <script>
                    (function ($) {
                      $("#editnutritionbutton").click(function (e) {
                    	var checked = $("#samplecheckbox").attr('checked'),
                            url = $("#editnutritionselect").val();
                    	
                    	if (url.indexOf("?") > 0) {
                    	  window.location.href = url + '&sample=' + !!checked;
                    	} else {
                    	  window.location.href = url;
                    	}
                      });
                    }(jQuery));
                  </script>
                </td></tr>
							<% } else { %>
              <tr><td align="left">
                <form action="nutrition_panels/nutrition_panel_edit.jsp" method="GET">
                  <input type="hidden" name="skuCode" value="<%= skuCode %>"/>
                  <input type="submit" value="Edit Nutrition Panel"/>
                </form>
                <form id="deletenutritionpanelform" action="nutrition_panels/nutrition_panel_edit.jsp" method="POST">
                  <input type="hidden" name="skuCode" value="<%= skuCode %>"/>
                  <input type="hidden" name="delete" value=""/>
                  <input type="button" name="deletebutton" value="Delete this panel and go back to classic nutrition panel"/>
                </form>
                <script>
                  (function ($) {
                    $("#deletenutritionpanelform input[name=deletebutton]").click(function () {
                      var data = $("#deletenutritionpanelform input[name=skuCode]").val();
                      if(data && window.confirm("Deleting panel. Are you sure?")) {
                        
                        $('#deletenutritionpanelform input[name=delete]').val(data);
                        $('#deletenutritionpanelform').submit();
                      }
                    });
                  }(jQuery));
                </script>
              </td></tr>
						    <% } %>							
						<% } %>
							
				        <tr><td align="center"><fd:NutritionPanel skuCode="<%=skuCode%>" nutritionModel="<%=nutrition%>" showErpsExtra="true" useCache="false"/></td></tr>
				        
                    </table>
                </fd:Nutrition>
            <%  } %>
        
                                
</body>
</html>