<%@ page import='com.freshdirect.content.nutrition.ErpNutritionType' %>
<%@ page import='com.freshdirect.webapp.util.FormElementNameHelper' %>
<%@ page import='java.text.DecimalFormat' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%! DecimalFormat formatter = new DecimalFormat(); %>
<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='ingredients edit page' direct='true'/>

     <tmpl:put name='content' direct='true'>

         <fd:Nutrition id="nutrition">

            <fd:NutritionController nutrition='<%= nutrition %>' userMessage='feedback' successPage='<%= "product_view.jsp?skuCode=" + nutrition.getSkuCode() %>'/>

            <table width="600" cellspacing=2 cellpadding=0>
                <tr><td align="left" class="section_title">Hidden Ingredients</td></tr>
                <tr><td align="right"><a href="product_view.jsp?skuCode=<%= nutrition.getSkuCode() %>">Back to Product</a></td></tr>
                <tr><td align="right"><%= feedback %></td></tr>
            </table>
            <form name="ingredientsInfo" action="hidden_ingredients_edit.jsp" method="post">
            <input type="hidden" name="action" value="hidden_ingredients">
            <br>
            <table>
                <tr align=center>
                    <td><textarea cols='60' rows='5' name='hidden_ingredients'><%= nutrition.getHiddenIngredients() %></textarea></td>
                </tr>
                <tr align=center><td><table><tr>
                    <td><input type="submit" value="save ingredients"></td>
                    <td><input type="button" value="cancel" onClick="window.location.href='<%= "product_view.jsp?skuCode=" + nutrition.getSkuCode() %>'"></td>
                </tr></table></td></tr>
             </table>
             </form>
		 
	    </fd:Nutrition>
    
     </tmpl:put>
</tmpl:insert>

