<%@ page contentType='text/plain' import='com.freshdirect.erp.model.*,com.freshdirect.fdstore.*,com.freshdirect.fdstore.content.*' %><%@ taglib uri='template' prefix='tmpl' %><%@ taglib uri='logic' prefix='logic' %><%@ taglib uri='freshdirect' prefix='fd' %>
<fd:ProductSearch results='searchResults' searchtype='<%= request.getParameter("searchtype") %>' searchterm='<%= request.getParameter("searchterm") %>'>
<logic:iterate id="productInfo" collection="<%= searchResults %>" type="com.freshdirect.erp.model.ErpProductInfoModel"><fd:Nutrition id="nutrition" skuCode='<%= productInfo.getSkuCode() %>'><%

    out.print(productInfo.getSkuCode());
    out.print("\t");
    try {
        ProductModel pm = ContentFactory.getInstance().getProduct(productInfo.getSkuCode());
        out.print(pm.getFullName());
    } catch (FDSkuNotFoundException fdsnfe) {
        out.print(productInfo.getDescription());
    }
    out.print("\t");
    out.print(nutrition.getIngredients());
    out.print("\t");
    out.print(nutrition.getHeatingInstructions());
    out.println();

%></fd:Nutrition></logic:iterate>
</fd:ProductSearch>
        