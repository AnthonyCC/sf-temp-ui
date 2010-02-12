<%@ page import='com.freshdirect.content.nutrition.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='com.freshdirect.erp.model.*' %>
<%@ page import='java.text.DecimalFormat' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! 
	DecimalFormat formatter = new DecimalFormat();
%>

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
<%	String skuCode = request.getParameter("skuCode");%>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='product view page' direct='true'/>

    <tmpl:put name='content' direct='true'>

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
					</tr>
				</table>
				</form>
				
                <%  if (product.getSkuCode() != null) { %>
                <form action="product_view.jsp" method="post">
                <input type=hidden name=action value=save>
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
                <br>
                
            <%  } %>
            </fd:ErpProduct>

            <%  if (skuCode != null) { %>
                <fd:Nutrition id="nutrition" skuCode='<%= skuCode %>'>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Ingredients</td></tr>
                    </table>
                    <table width="600">
                        <tr><td><%= nutrition.getIngredients() %></td></tr>
                        <tr><td align="left"><a href="ingredients_edit.jsp">Edit Ingredients</td></tr>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Hidden Ingredients</td></tr>
                    </table>
                    <table width="600">
                        <tr><td><%= nutrition.getHiddenIngredients() %></td></tr>
                        <tr><td align="left"><a href="hidden_ingredients_edit.jsp">Edit Hidden Ingredients</td></tr>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Heating Instructions</td></tr>
                    </table>
                    <table width="600">
                        <tr><td><%= nutrition.getHeatingInstructions() %></td></tr>
                        <tr><td align="left"><a href="heating_edit.jsp">Edit Heating Instructions</td></tr>
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
                        <tr><td align="left"><a href="kosher_edit.jsp">Edit Kosher Information</td></tr>
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
                        <tr><td align="left"><a href="claims_edit.jsp">Edit Claims, Allergens and Organic Statements</td></tr>
                    </table>
                    <br>

                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Nutritional Information</td></tr>
                    </table>
                    <table width="600">
                        <tr><td align="left"><a href="nutrition_edit.jsp">Edit Nutritional Information</td></tr>
                        <tr><td align="center">
                            Information source: <%= nutrition.getUomFor(ErpNutritionType.SOURCE) %><br>
                            <br>
                            <%  double netCarbs = nutrition.getNetCarbs();
                                double wwPoints = nutrition.getValueFor(ErpNutritionType.WEIGHT_WATCHERS_POINTS);
                                if(netCarbs > 0){%>
                                    Net Carbs: <%=netCarbs%><br>
                                <%}
                                if(wwPoints > 0){%>
                                    WeightWatchers Points: <%=wwPoints%>
                                <%}%>
                            <br><br>
<%                  ArrayList nutritionList = new ArrayList();
                    for (Iterator nIter = nutrition.getKeyIterator(); nIter.hasNext(); ) {
                        String key = (String) nIter.next();
                        if ("IGNORE".equalsIgnoreCase(key)) { %>
                            <b>This information is currently hidden from the website</b>
                            <br><br>
<%                      }
                        FDNutrition fdn = new FDNutrition(ErpNutritionType.getType(key).getDisplayName(), nutrition.getValueFor(key), nutrition.getUomFor(key));
                        nutritionList.add(fdn);
                    }
%>
<%@ include file="i_nutrition_sheet.jspf" %>

                    </td></tr>
                    </table>
                </fd:Nutrition>
            <%  } %>
                                
     </tmpl:put>
</tmpl:insert>
