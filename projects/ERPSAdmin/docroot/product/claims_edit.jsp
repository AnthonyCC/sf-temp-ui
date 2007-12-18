<%@ page import='com.freshdirect.content.nutrition.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<script language="JavaScript" type="text/javascript">
<!-- Begin
function clearAll(numOfCheckboxes, cType) {
    var checkBoxes = new Array();
    var i = 0;
    if(cType == "claim"){
        for(i; i<numOfCheckboxes; i++){
            checkBoxes[i] = document.claimsInfo.claim[i];
        }

    }else if(cType == "allergen"){
        for(i; i<numOfCheckboxes; i++){
            checkBoxes[i] = document.claimsInfo.allergen[i];
        }
    }else if(cType == "organic"){
        for(i; i<numOfCheckboxes; i++){
            checkBoxes[i] = document.claimsInfo.organic[i];
        }

    }
    if(checkBoxes[0].checked){
        for(i = 1; i<numOfCheckboxes; i++){
            checkBoxes[i].checked = false;
            checkBoxes[i].disabled = true;
        }
    }else{
        for(i = 1; i<numOfCheckboxes; i++){
            checkBoxes[i].checked = false;
            checkBoxes[i].disabled = false;
        }
    }
}

//End -->
</script>

<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='claims edit page' direct='true'/>

     <tmpl:put name='content' direct='true'>

	     <fd:Nutrition id="nutrition">

            <fd:NutritionController nutrition='<%= nutrition %>' userMessage='feedback' successPage='<%= "product_view.jsp?skuCode=" + nutrition.getSkuCode() %>'/>

            <table width="600" cellspacing=2 cellpadding=0>
                <tr><td align="left" class="section_title">Claims, Allergens, Organic Statements</td></tr>
                <tr><td align="right"><a href="product_view.jsp?skuCode=<%= nutrition.getSkuCode() %>">Back to Product</a></td></tr>
                <tr><td align="right"><%= feedback %></td></tr>
            </table>
            <form name="claimsInfo" action="claims_edit.jsp" method="post">
            <input type="hidden" name="action" value="claims">
            <table width="600">
                <tr align=center>
                    <td width="200" valign="top" align="left">
                        <b>Claims:</b><br>
                        <table>
                        <logic:iterate id="infoType" collection="<%= EnumClaimValue.getValues() %>" type="com.freshdirect.content.nutrition.NutritionValueEnum">
                        <tr><td><input name="claim" type="checkbox" value="<%= infoType.getCode() %>" <%= nutrition.makesClaim((EnumClaimValue)infoType)?"CHECKED":"" %> <%=("NONE".equalsIgnoreCase(infoType.getCode())?"onClick=\"clearAll(" + EnumClaimValue.getValues().size() + ",'claim')\"":"")%>></td><td><%= infoType.getName() %></td></tr>
                        </logic:iterate>
                        <%=(nutrition.getClaims().contains(EnumClaimValue.getValueForCode("NONE")))?"<script>clearAll(" + EnumClaimValue.getValues().size() + ",'claim')</script>":""%>
                        </table>
                    </td><td width="200" valign="top" align="left">
                        <b>Allergens:</b><br>
                        <table>
                        <logic:iterate id="infoType" collection="<%= EnumAllergenValue.getValues() %>" type="com.freshdirect.content.nutrition.NutritionValueEnum">
                        <tr><td><input name="allergen" type="checkbox" value="<%= infoType.getCode() %>" <%= nutrition.hasAllergen((EnumAllergenValue)infoType)?"CHECKED":"" %> <%=("NONE".equalsIgnoreCase(infoType.getCode())?"onClick=\"clearAll(" + EnumAllergenValue.getValues().size() + ",'allergen')\"":"")%>></td><td><%= infoType.getName() %></td></tr>
                        </logic:iterate>
                        <%=(nutrition.getAllergens().contains(EnumAllergenValue.getValueForCode("NONE")))?"<script>clearAll(" + EnumAllergenValue.getValues().size() + ",'allergen')</script>":""%>
                        </table>
                    </td><td width="200" valign="top" align="left">
                        <b>Organic Statements:</b><br>
                        <table>
                        <logic:iterate id="infoType" collection="<%= EnumOrganicValue.getValues() %>" type="com.freshdirect.content.nutrition.NutritionValueEnum">
                        <tr><td><input name="organic" type="checkbox" value="<%= infoType.getCode() %>" <%= nutrition.hasOrganicStatement((EnumOrganicValue)infoType)?"CHECKED":"" %> <%=("NONE".equalsIgnoreCase(infoType.getCode())?"onClick=\"clearAll(" + EnumOrganicValue.getValues().size() + ",'organic')\"":"")%>></td><td><%= infoType.getName() %></td></tr>
                        </logic:iterate>
                        <%=(nutrition.getOrganicStatements().contains(EnumOrganicValue.getValueForCode("NONE")))?"<script>clearAll(" + EnumOrganicValue.getValues().size() + ",'organic')</script>":""%>
                        </table>
                    </td>
                <tr align=center><td colspan="3"><table><tr>
                    <td><input type="submit" value="save info"></td>
                    <td><input type="button" value="cancel" onClick="window.location.href='product_view.jsp'"></td>
                </tr></table></td></tr>
             </table>
             </form>
	    </fd:Nutrition>
    
     </tmpl:put>
</tmpl:insert>

