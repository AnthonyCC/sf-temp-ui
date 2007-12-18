<%@ page import='com.freshdirect.content.nutrition.ErpNutritionType' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='product view page' direct='true'/>

    <tmpl:put name='content' direct='true'>

            <fd:ErpProduct id="product">
                 <fd:Nutrition id="nutrition" skuCode="<%= product.getSkuCode() %>">
                     <fd:NutritionController nutrition="<%= nutrition %>" userMessage="feedback" successPage="nutrition_edit.jsp"/>
                     <br>
                    <table width="600" cellspacing=2 cellpadding=0>
                        <tr><td align="left" class="section_title">Nutrition Component</td></tr>
			<tr><td align="right"><a href="nutrition_edit.jsp">Back to Nutrition Edit</a></td></tr>
                    </table>
                    <form name="edit_nutrition_component" method="post" action="nutrition_component_edit.jsp">
                    <input type="hidden" name="action" value="update_components">
                    <table>
                    <%int i = 1;%>
                        <tr>
                        <logic:iterate id="value" collection="<%= ErpNutritionType.getTypesIterator() %>" type="java.lang.String">
                            <% ErpNutritionType.Type ntype = (ErpNutritionType.Type) ErpNutritionType.getType(value); %>
                            <td><input <% if (ntype.getStartingSet()) { %>tabindex="-1" onFocus="this.blur()" onClick="this.checked=true"<% } %> name="<%= value %>" class="checkbox" type="checkbox" <%= (ntype.getStartingSet() || (nutrition.getValueFor(value) != -999))?"checked":"" %>></td>
                            <td><%= (ntype.getStartingSet()?"<b>":"") + ntype.getDisplayName() + (ntype.getStartingSet()?"</b>":"") %>
                            </td>
                        <%if(i++%2 == 0){%>
                        </tr><tr>
                        <%}%>
                        </logic:iterate>
                        </tr>
                        <tr>
                            <td colspan=2><table><tr>
                            <td><input type="submit" value="Save Change"></td>
                            <td><input type="button" value="cancel" onClick="window.location.href='nutrition_edit.jsp'"></td>
                            </tr></table></td>
                        </tr>
                    </table>
                    </form>
                </fd:Nutrition>
            </fd:ErpProduct>

    </tmpl:put>

</tmpl:insert>


