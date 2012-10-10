<%@ page import='com.freshdirect.fdstore.content.*, com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.content.nutrition.*'%>
<%@ page import='java.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<fd:ProductGroup id='productNode' categoryId='<%= request.getParameter("catId") %>' productId='<%= request.getParameter("productId") %>'>
	<tmpl:insert template='/common/template/small_pop.jsp'>
		<tmpl:put name='title' direct='true'>FreshDirect - <%= productNode.getFullName() %></tmpl:put>
		<tmpl:put name='content' direct='true'>
		<font class="title13"><%=productNode.getFullName()%></font>
		<table width="220" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1" alt="" border="0"></td>
			<td align="left">
			<%
				//try default sku
				SkuModel aSku = productNode.getDefaultSku();

				//check to see if a sku was passed to the popup ...
				String tempSkuCode = (request.getParameter("skuCode") == null) ? "" : request.getParameter("skuCode");
				//... and try to get that sku
				if (!"".equals(tempSkuCode)) {
					aSku = (SkuModel)productNode.getSku(tempSkuCode);
				}

				//failing that and no default sku, try the 0 sku
				if (aSku==null ) {
					aSku = (SkuModel)productNode.getSkus().get(0);
				}

				FDProduct fdprd = null;

				//null check
				if (aSku!=null ) { fdprd = aSku.getProduct(); }
				if (fdprd==null) {
					//we should never reach here with a null fdprd. just in case, output a nice message instead of an error
					%>
						<br /><br />Please check product label for nutrition, ingredients, and allergens.<br /><br />
					<%
				}else{
					if(fdprd.hasDrugNutritionFacts()){%><%@ 
						include file="/shared/includes/i_drug_nutrition_sheet.jspf" 
					%><%
					} else if (fdprd.hasNutritionFacts()) {
			%>
						<font class="space4pix"><br></font>
							<%@ include file="/shared/includes/i_nutrition_sheet.jspf" %><br />
				<%
					}
					if (fdprd.hasIngredients()) { %>
						<table BORDER="0" CELLSPACING="0" CELLPADDING="2">
							<tr VALIGN="top">
								<td class="text9" align="center">
									<font class="title18">Ingredients:</font><br />
									<img src="media_stat/images/layout/330000.gif" height="4" width="220" vspace="4">
								</td>
							</tr>
							<tr><td><%= fdprd.getIngredients() %></td></tr>
						</table>
					<% }
				}
				%><%@ include file="/shared/includes/product/allergens.jspf" %><br />
				<a href="/shared/product_nutrition_note.jsp">An important note about our nutrition and ingredients information.</a><br />
			</td>
		</tr>
		</table>	

		</tmpl:put>
	</tmpl:insert>
</fd:ProductGroup>
