<%@ page import='com.freshdirect.content.nutrition.ErpNutritionType' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='java.text.DecimalFormat' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! DecimalFormat formatter = new DecimalFormat(); %>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='nutrition edit page' direct='true'/>

     <tmpl:put name='content' direct='true'>

	     <fd:Nutrition id="nutrition">

            <fd:NutritionController nutrition='<%= nutrition %>' userMessage='feedback' successPage='<%= "product_view.jsp?skuCode=" + nutrition.getSkuCode() %>'/>

            <table width="600" cellspacing=2 cellpadding=0>
                <tr><td align="left" class="section_title">Nutritional Information</td></tr>
                <tr><td align="right"><a href="product_view.jsp?skuCode=<%= nutrition.getSkuCode() %>">Back to Product</a></td></tr>
                <tr><td align="right"><%= feedback %></td></tr>
            </table>
            <form name="nutritonalInfo" action="nutrition_edit.jsp" method="post">
            <input type="hidden" name="action" value="update">
            <a href="nutrition_component_edit.jsp">ADD/DELETE COMPONENT</a>
            <br>
            <table cellspacing="0">
                <tr>
                    <td class="title12">Nutritional Component</td>
                    <td class="title12">Less than</td>
                    <td class="title12">Value</td>
                    <td class="title12">Unit</td>
                </tr>
                <%
                    String lastValueName = "";
                    String white         = "#FFFFFF";
                    String grey          = "#DDDDDD";
                    int count            = 0;
                %>
                <logic:iterate id="value" collection="<%= (nutrition.hasInfo() ? nutrition.getKeyIterator() : ErpNutritionType.getStarterSet()) %>" type="java.lang.String">
                <%
                    String valueName = ErpNutritionType.getType(value).getDisplayName();
                    int valNameLength = (valueName.length()<7)?valueName.length():7;
                    int lastValNameLength = (lastValueName.length()<7)?lastValueName.length():7;
                    if (!valueName.substring(0,valNameLength).equalsIgnoreCase(lastValueName.substring(0,lastValNameLength)) || valueName.startsWith("Vitamin")) count++;
                    lastValueName = valueName;
                %>
                <tr bgcolor="<%= ((count%2)==0)?white:grey %>">
                    <td><%= ((ErpNutritionType.Type)ErpNutritionType.getType(value)).getDisplayName()%></td>
                    <td align="center">
                        <% if (!ErpNutritionType.SERVING_SIZE.equals(value) && !ErpNutritionType.NUMBER_OF_SERVINGS.equals(value) &&
                                    !ErpNutritionType.TOTAL_CALORIES.equals(value) && !ErpNutritionType.TOTAL_CALORIES_FROM_FAT.equals(value) &&
                                    !ErpNutritionType.SOURCE.equals(value) && !ErpNutritionType.SERVING_WEIGHT.equals(value)) { %>
                        <input name="<%=value+"_LT"%>" type="checkbox" <%= ((nutrition.getValueFor(value) < 0) && (nutrition.getValueFor(value) > -999)) ? "checked" : "" %>></td>
                        <% } %>
                    <td>
                        <% if (!ErpNutritionType.SOURCE.equals(value)) { %>
                        <input name="<%=value%>" type="text" size="4" value="<%=Math.abs(nutrition.getValueFor(value)) %>">
                        <% } %>
                    </td>
                    <td>
                        <% if (!ErpNutritionType.TOTAL_CALORIES.equals(value) && !ErpNutritionType.TOTAL_CALORIES_FROM_FAT.equals(value)) { %>
                            <input name="<%=value+"_UOM"%>" type="text" size="20" value="<%= nutrition.getUomFor(value) %>">
                        <% }
                           if (ErpNutritionType.SERVING_SIZE.equals(value)) { %>
                            description
                        <% } else if (ErpNutritionType.NUMBER_OF_SERVINGS.equals(value)) { %>
                            ie - about
                        <% } %>
                    </td>
                </tr>
                </logic:iterate>
                <tr><td colspan="4"><table><tr>
                    <td><input type="submit" value="save nutrition"></td>
                    <td><input type="button" value="cancel" onClick="window.location.href='product_view.jsp'"></td>
                </tr></table></td></tr>
             </table>
             </form>
		 
	    </fd:Nutrition>
    
     </tmpl:put>
</tmpl:insert>

