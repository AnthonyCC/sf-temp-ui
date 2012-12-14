<%@ page import='com.freshdirect.content.nutrition.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.erp.model.*' %>
<%@ page import='com.freshdirect.erp.security.SecurityManager' %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.DecimalFormat' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
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
		<link rel="shortcut icon" href="/blackbirdjs/favicon.ico" type="image/x-icon" />		
		<script type="text/javascript" src="/assets/javascript/jquery/1.7.2/jquery.js"></script>
		<link rel="stylesheet" href="/ERPSAdmin/common/css/erpsadmin.css" type="text/css">	
		<script type="text/javascript" src="/ERPSAdmin/batch/blackbirdjs/blackbird.js"></script>
		<link type="text/css" rel="Stylesheet" href="/ERPSAdmin/batch/blackbirdjs/blackbird.css" />
		<script type="text/javascript" src="/ERPSAdmin/product/json2.js"></script>
		<script type="text/javascript" src="/ERPSAdmin/product/erpsydaisysku2urladdon.js"></script>
		<script type="text/javascript" src="http://www.freshdirect.com/assets/javascript/prototype.js"></script>
		<script>
		function copyConfirm(value){
			if(value == ""){
				alert("Please enter a valid WebId");
				return false;
			} else {
				return confirm('Copy attributes from ' + value + '?');
			}
		}
		</script>
		<style>
			table#_skuContTable, table#_skuContTable td, table#_keyTable, table#_keyTable td {
				padding: 0;
				border-collapse: collapse;
			}
			table#_skuContTable td#_skuCont_tdRelatedPrimary {
				width: 50%;
			}
			table#_skuContTable div, td#_skuCont_tdRelatedSecondary div, table#_skuContTable td#_skuCont_tdRelatedSecondary  {
				border: 1px solid #eee;
				padding: 0;
				margin: 0;
				float: left;
			}
			table#_skuContTable td#_skuCont_tdRelatedSecondary div {
				border: 0 none;
			}
			table#_skuContTable div {
				width: 170px;
			}
			td#_skuCont_tdRelatedPrimary a {
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
			#_keyTableTD0 div div {
				margin-right: 3px;
				width: 16px;
			}
			#_keyTableTD0 div {
				margin-right: 16px;
			}
			td#_skuCont_tdRelatedSecondary a, td#_skuCont_tdComponentsOpt a {
				color: #279F54;
			}
			td#_skuCont_tdVirtual a, td#_skuCont_tdRelatedPrimary span.siblingSKU a, span.siblingSKU a {
				color: #7F7F7F;
			}
			table td#_skuCont_tdRelatedPrimary, table td#_skuCont_tdRelatedSecondary, table td#_skuCont_tdComponentsOpt {
				vertical-align: top;
			}
			table td#_skuCont_tdRelatedPrimary div, table td#_skuCont_tdRelatedSecondary div, #_skuCont_tdVirtual div, table td#_skuCont_tdComponentsOpt div, table td#_skuCont_tdComponents div {
				width: 100%;
			}
			table td#_skuCont_tdComponents div a.skuToggle {
				float: right;
				margin-right: 5px;
				font-size: 9px;
				background-color: #666;
				color: #fff;
				padding: 0 2px 1px 2px;
			}
			#_skuContTable, table#_keyTable {
				width: 100%;
				margin-bottom: 5px;
			}
			#_keyTableTHKey {
				font-weight: bold;
			}
			table#_keyTable div {
				float: left;
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
            <fd:ErpProduct id="product" skuCode='<%= skuCode %>'>
            <%
                if ((skuCode == null) && (product != null)) skuCode = product.getSkuCode();
                if (product != null) {
            %>

                <fd:AttributeController erpObject="<%= product %>" userMessage="feedback" />
				<div id="feeback" style="color:red; font-size:12px;font-weight:bold;"><%=feedback%></div>
				
                <table width="600" cellspacing=2 cellpadding=0>
                    <tr><th align="left" class="section_title">PRODUCT:</th></tr>
                    <tr><td><%= (product.getSkuCode() != null) ? product.getSkuCode() : "" %></td></tr>					
					<tr><td id="add_on"><div id="addon">
					</div></td></tr>
                    <tr><td><%= product.getProxiedMaterial().getDescription() %></td></tr>
                    <% if (product.getSkuCode() == null) { %>
						<tr><td><b>There is no such product in ERPServices with skucode : <%= skuCode %></b></td></tr>
					<% } else {
                            if ((product.getUnavailabilityStatus() != null) && (product.getUnavailabilityStatus().equals("DISC"))) { %>
                        <tr><td><b>This product is discontinued</b></td></tr>
                    <%      } else if ((product.getUnavailabilityStatus() != null) && (product.getUnavailabilityStatus().equals("UNAV"))) { %>
                        <tr><td><b>This product is temporarily unavailable</b></td></tr>
                     <%     } else if ((product.getUnavailabilityStatus() != null) && (product.getUnavailabilityStatus().equals("SEAS"))) { %>
                        <tr><td><b>This product is out of season</b></td></tr>
                     <%     } else { %>
                        <tr><td><b>This product is currently available for sale</b></td></tr>
                     <%     }
                        } %>
                </table>
				
				<form method="post" name="copyForm">
				<table width="600" cellspacing=2 cellpadding=0>
					<tr>
						<td>Web ID:&nbsp;<input type="text" name="sourceId"/>&nbsp;<input type="submit" value="copy" onclick="return copyConfirm(document.forms['copyForm'].sourceId.value)"/></td>
						<input type=hidden name=action value=copy>
						<input type=hidden name="sku_code" value="<%= skuCode %>">
						<input type=hidden name="skuCode" value="<%= skuCode %>">						
					</tr>
				</table>
				</form>
				
                <%  if (product.getSkuCode() != null) { %>
                <form action="product_view.jsp" method="post">
                	<input type=hidden name=action value=save>
					<input type=hidden name="sku_code" value="<%= skuCode %>">
					<input type=hidden name="skuCode" value="<%= skuCode %>">
                <table width="600" cellspacing="2" cellpadding="0">
                    <tr><th align="left">Default Pricing Unit Description</th></tr>
                    <tr>                    
                    <td align="left"><input type=text size=20 name='<%= FormElementNameHelper.getFormElementName(product, EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName()) %>' value='<%= product.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION) %>'></td></tr>
                    <tr><td colspan="2" align="center"><input type="submit" value="save changes"></td></tr>
                </table>
                </form>
                <table width="600" cellspacing="2" cellpadding="0">
                <tr><th align="left">Zone Id</th><th align="left">Price</th></tr>
                <% 
                
                      java.util.List prices=product.getProxiedMaterial().getPrices();
                      for(int i=0;i<prices.size();i++)
                      {
                    	  ErpMaterialPriceModel model=(ErpMaterialPriceModel)prices.get(i);   
                      
                %>
                
                
                    
                    <tr>                    
                    <td align="left"><%=model.getSapId() %></td>
                    <td align="left"><%=model.getPrice() %></td></tr>                                
                <%
                      }
                 %></table>     
                                                                                     
                  <%  }   %>
                <table width="600" cellspacing=2 cellpadding=0>
                    <tr><th align="left" class="section_title" colspan=3>SAP Material</td></tr>
					<% if (product.getSkuCode() == null) { %>
						<tr><td align="center" colspan="3"><b>There is no such product in ERPServices with skucode : <%= skuCode %></b></td></tr>
					<%
						} else {
							com.freshdirect.erp.model.ErpMaterialModel material = product.getProxiedMaterial();
					%> 
						<tr><td><%= material.getDescription() %></td><td><%= material.getSapId() %></td><td><%= material.getUPC() %></td></tr>
					<% } %>
                </table>

                <%  if (product.getSkuCode() != null) { %>
                <form action="product_view.jsp" method="post">
                	<input type=hidden name=action value=save>
                	<input type=hidden name="sku_code" value="<%= skuCode %>">
                	<input type=hidden name="skuCode" value="<%= skuCode %>">
                <table width="600" cellspacing="2" cellpadding="0">
                    <tr><th align="left" class="section_title">New / Back-in-Stock Manual Override</th></tr>
                    <tr><td align="left">Date of becoming new <input type=text size=12 name='<%= FormElementNameHelper.getFormElementName(product, EnumAttributeName.NEW_PRODUCT_DATE.getName()) %>' value='<%= product.getAttribute(EnumAttributeName.NEW_PRODUCT_DATE) %>'>
                    (use one of the following formats: MM/dd/yy, MM/dd/yyyy, MM/dd/yyyy HH:mm)</td></tr>
                    <tr><td align="left">Back-in-stock date <input type=text size=12 name='<%= FormElementNameHelper.getFormElementName(product, EnumAttributeName.BACK_IN_STOCK_DATE.getName()) %>' value='<%= product.getAttribute(EnumAttributeName.BACK_IN_STOCK_DATE) %>'>
                    (use one of the following formats: MM/dd/yy, MM/dd/yyyy, MM/dd/yyyy HH:mm)</td></tr>
                    <tr><td colspan="2" align="center"><input type="submit" value="save changes"></td></tr>
                </table>
                </form>
                <% } %>
                <br>
                
            <%  } %>
            </fd:ErpProduct>

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
<script language="javascript">sku_urls();</script>
