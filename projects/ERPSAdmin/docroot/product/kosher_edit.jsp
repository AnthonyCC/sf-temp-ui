<%@ page import='com.freshdirect.content.nutrition.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<script language="JavaScript" type="text/javascript">
<!-- Begin
function selectNoImage(optionCount) {
    var i = 1;
    if (document.kosherInfo.kosher_type[1].checked){
        document.kosherInfo.kosher_symbol[0].checked = true;
        for(i;i<optionCount;i++){
            document.kosherInfo.kosher_symbol[i].disabled = true;
        }
    } else if(document.kosherInfo.kosher_symbol[1].disabled){
        for(i;i<optionCount;i++){
            document.kosherInfo.kosher_symbol[i].disabled = false;
        }
    }
}
//End -->
</script>


<tmpl:insert template='/common/templates/main.jsp'>
    <tmpl:put name='title' content='kosher information edit page' direct='true'/>

     <tmpl:put name='content' direct='true'>

	     <fd:Nutrition id="nutrition">

            <fd:NutritionController nutrition='<%= nutrition %>' userMessage='feedback' successPage='<%= "product_view.jsp?skuCode=" + nutrition.getSkuCode() %>'/>

            <table width="600" cellspacing=2 cellpadding=0>
                <tr><td align="left" class="section_title">Kosher Information</td></tr>
                <tr><td align="right"><a href="product_view.jsp?skuCode=<%= nutrition.getSkuCode() %>">Back to Product</a></td></tr>
                <tr><td align="right"><%= feedback %></td></tr>
            </table>
            <form name="kosherInfo" action="kosher_edit.jsp" method="post">
            <input type="hidden" name="action" value="kosher">
            <table width="600">
                <tr align=center><td width="100">
                </td><td align="left" valign="top">
                    <b>Kosher Type:</b><br>
                    <logic:iterate id="kosherType" collection="<%= EnumKosherTypeValue.getValues() %>" type="com.freshdirect.content.nutrition.EnumKosherTypeValue">
                    <input name="kosher_type" type="radio" value="<%= kosherType.getCode() %>" onclick="selectNoImage(<%=EnumKosherSymbolValue.getValues().size()%>)"<%= (kosherType.equals(nutrition.getKosherType()))?"CHECKED":"" %>><%= kosherType.getName() %><br>
                    </logic:iterate>
                </td><td align="left" valign="top">
                    <b>Kosher Symbol:</b><br>
                    <logic:iterate id="kosherSymbol" collection="<%= EnumKosherSymbolValue.getValues() %>" type="com.freshdirect.content.nutrition.EnumKosherSymbolValue">
                    <input name="kosher_symbol" type="radio" value="<%= kosherSymbol.getCode() %>" <%= (kosherSymbol.equals(nutrition.getKosherSymbol()))?"CHECKED":"" %>>
                    <img src="../images/kosher/<%= (!kosherSymbol.display())?"NOIMAGE":kosherSymbol.getName() %>.gif" width="30" height="30"><br>
                    </logic:iterate>
                    <%=(EnumKosherTypeValue.getValues().contains(EnumKosherTypeValue.getValueForCode("NONE")) || EnumKosherTypeValue.getValues().contains(EnumKosherTypeValue.getValueForCode("NOT_KOSHER")))?"<script>selectNoImage(" + EnumKosherSymbolValue.getValues().size() + ")</script>":""%>
                </td></tr>
                <tr align=center><td colspan="3"><table><tr>
                    <td><input type="submit" value="save kosher information"></td>
                    <td><input type="button" value="cancel" onClick="window.location.href='product_view.jsp'"></td>
                </tr></table></td></tr>
             </table>
             </form>
		 
	    </fd:Nutrition>
    
     </tmpl:put>
</tmpl:insert>

