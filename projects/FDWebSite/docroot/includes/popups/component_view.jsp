<%@ page import='com.freshdirect.fdstore.content.*'  %>
<%@ page import='com.freshdirect.fdstore.attributes.*'  %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
    ComponentPopupView cpv = (ComponentPopupView) request.getAttribute("componentView");
    if (cpv == null) {
        throw new JspException("Hey!  You need to pass me a ComponentPopupView object!");
    }
    List products = cpv.getProducts();
    String level  = cpv.getLevel();
    String title  = cpv.getTitle();
    String catId  = cpv.getCategory().getContentName();
    String parent = cpv.getParentProductSku();
    String prodId = request.getParameter("prodId");
    
    
    if(!"detail".equalsIgnoreCase(level)){
        level = "home";
    }
    
    if(products.size() == 1){
        level = "detail";
        prodId = ((ProductModel)products.get(0)).getContentName();
    }
    
    String catImg = "/media_stat/images/layout/clear.gif";
    if(cpv.getCategory().getAttribute("CAT_TITLE")!=null){
        catImg = ((Image)cpv.getCategory().getAttribute("CAT_TITLE").getValue()).getPath();
    }
%>

<tmpl:insert template='/common/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - <%=title%> Options</tmpl:put>
        
        <tmpl:put name='content' direct='true'>

        <table border="0" cellpadding="0" cellspacing="0">
            <tr valign="top"><td colspan="5" width="520"></td></tr>
            <tr valign="top">
                <td width="130" align="right" rowspan="2"><a href="<%=request.getRequestURI()%>?catId=<%=catId%><%=parent != null?"&parent="+parent:""%>&title=<%=title%>"><img src="<%=catImg%>" border="0"></a><br>
                    <br>
                    <%if(products.size() > 1){%>
                    <logic:iterate id='item' collection="<%=products%>" type='com.freshdirect.fdstore.content.ProductModel' indexId="idx">
                        <%if(parent != null){
                            catId = item.getParentNode().getContentName(); 
                        }%>
                        <a href="<%=request.getRequestURI()%>?catId=<%=catId%>&prodId=<%=item.getContentName()%><%=parent != null?"&parent="+parent:""%>&title=<%=title%>&level=detail"><%=item.getNavName() != null?item.getNavName():item.getFullName()%></a><br>
                        <img src="/media_stat/images/layout/clear.gif" height="15" width="1">
                    </logic:iterate>
                    <%}%>
                    <%catId = cpv.getCategory().getContentName();%>
                    <br><br><br>             
                </td>
                <td rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="10" height="1"></td>
                <td width="1" bgcolor="#999966" rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
                <td width="15" rowspan="2"><img src="/media_stat/images/layout/clear.gif" width="15" height="1"></td>                
                <td valign="top" width='349'>
<%
if (level.equalsIgnoreCase("home")){
    Attribute attr = cpv.getCategory().getAttribute("EDITORIAL");
    MediaI media = null;
    if(attr != null){
        media = (MediaI)attr.getValue();
    }
%>
                    <table border="0" cellpadding="0" cellspacing="0" width="349">
                        <tr>
                            <td colspan='3'>
                            <%if(media!=null){%>
                                <fd:IncludeMedia name='<%=media.getPath()%>'/>	
                            <%}%>
                            <br><br>
                            </td>
                        </tr>
                        <logic:iterate id='item' collection="<%=products%>" type='com.freshdirect.fdstore.content.ProductModel' indexId="idx">
                                    <tr>
					<td>
						<% if(item.getAttribute("PROD_IMAGE")!=null){
							Image prodImg = (Image)item.getAttribute("PROD_IMAGE").getValue(); %>
						<img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" border="0">
						<% } %>
                                                <font class="space4pix"><br><br></font>
					</td>
					<td><img src="/media_stat/images/layout/clear.gif" width="10" height="0"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="249" height="0">
                                            <font class="text11bold">
                                            <%if(parent != null){
                                                catId = item.getParentNode().getContentName(); 
                                            }%>
                                            <a href="<%=request.getRequestURI()%>?catId=<%=catId%>&prodId=<%=item.getContentName()%><%=parent != null?"&parent="+parent:""%>&title=<%=title%>&level=detail"><%=item.getNavName() != null?item.getNavName():item.getFullName()%></a></font><br>
						
                                            <%if(item.getBlurb()!=null){%>
                                                <%=item.getBlurb()%><br>
                                            <%}%><font class="space4pix"><br><br></font>
						<br>		
                                        </td>
                                    </tr>		
                    </logic:iterate>
                    <%catId = cpv.getCategory().getContentName();%>
                    </table>
<%
}else{
    ProductModel product = ContentFactory.getInstance().getProductByName(catId, prodId);
	//accomodate claims include
	ProductModel productNode = product;
    Image prodImg = product.getDetailImage();
    MediaI prodDescription = (Html)product.getProductDescription();
    FDProduct fdprd = null;
    if (!productNode.isUnavailable()) {
      fdprd = ((SkuModel)product.getSku(0)).getProduct();
    }
    
%>
                <img src="/media_stat/images/layout/clear.gif" width="349" height="0"><br>
		<img src="<%=prodImg.getPath()%>" width="<%=prodImg.getWidth()%>" height="<%=prodImg.getHeight()%>" alt="" border="0">
		
		<br>
		<br>
		<font class="title16"><%=product.getFullName()%></font><br>
		<font class="text11">
		<%if(prodDescription!=null){%>
			<fd:IncludeMedia name='<%=prodDescription.getPath()%>'/>	
		<%}%>
		</font><br><br>

	<%  if (fdprd!=null && fdprd.hasIngredients()) {    %>
	        <%@ include file="/includes/product/i_product_ingredients.jspf"%>
	        <br><br>
	<%  }      
	    if (fdprd != null && fdprd.hasNutritionFacts()) { %>
	        <%@ include file="/includes/product/i_product_nutrition.jspf"%>
	        <br><br><br>
            <%}%>
                
<%}%>
                </td>
            </tr>
        </table>

        </tmpl:put>
</tmpl:insert>
