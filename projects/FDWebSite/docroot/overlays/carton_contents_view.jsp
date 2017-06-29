<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartonInfo"%>
<%@ page import="com.freshdirect.customer.ErpCartonDetails"%>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<style type="text/css">
.list_component_row {
    background-color: lightblue;
    text-align: center;
}
</style>

<%	String orderId = request.getParameter("orderId"); %>
<%	List cartonInfo = null; %>
<%  List cartonDetails = null; %>
<%	String orderLineNumber = request.getParameter("orderLineNumber"); %>
<%	String showForm = request.getParameter("showForm"); %>
<%  boolean bShowForm = false; %>
<%  if("true".equals(showForm))
		bShowForm=true;
%>

<tmpl:insert template='/shared/template/large_pop.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Carton Contents View"/>
  </tmpl:put>
  <tmpl:put name='title' direct='true'>FreshDirect - Carton Contents View</tmpl:put>
		<tmpl:put name='content' direct='true'>

<fd:GetOrder id='order' saleId='<%= orderId %>'>

<%
    if(orderLineNumber != null)
        cartonInfo = ((FDOrderAdapter)order).getCartonContents(orderLineNumber);
    else
    	cartonInfo = ((FDOrderAdapter)order).getCartonContents();
	String successPageParams = request.getQueryString();
	if (successPageParams.indexOf("scroll=yes") != -1 && successPageParams.indexOf("cartonNumber=") != -1) {
		//if we've already submitted a carton, don't use it in the params again, otherwise we'll always use the first cartonNumber in complaintline
		successPageParams = successPageParams.substring(0, successPageParams.indexOf("scroll=yes")+"scroll=yes".length());
	}
%>
<% String successPage = request.getRequestURI() + "?" + successPageParams; %>
<% String updated = request.getParameter("cartUpdated"); %>
<% int idx = 0; %>
<% if(updated != null)
	   successPage += "&cartUpdated=" + updated; 
   %>

<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' multiSuccessPage='<%= successPage %>' successPage='<%= successPage %>'>
  <% if (result.getErrors().size() > 0) { %>
      <FONT CLASS="text11rbold">Errors in adding to cart</FONT><br>
  <%    for(Iterator errIter = result.getErrors().iterator(); errIter.hasNext(); ) {
      	ActionError ae = (ActionError) errIter.next(); %>
     <FONT CLASS="text11rbold"><%= ae.getDescription() %></FONT><br>
   <% } %>
  <% } else {
       if("true".equals(updated)) { %>
         <FONT CLASS="text11rbold">Cart updated</FONT><br>
        <script language="javascript">
         window.opener.location = window.opener.location;
        </script>
  <%   } else { %>
  <%   }
     } %>
     
</fd:FDShoppingCart>

<table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
	<tr valign="top">
  <% if( (cartonInfo != null) && (cartonInfo.size() > 0) ) { %>
	  <th align="left" width="150px">Quantity</th>
	  <th align="left">Product</th>
	  <th align="left" width="90px">Final Weight</th>
	  <th align="left" width="75px">Unit Price</th>
	<tr>
		<td colspan="4">
			<logic:iterate id="carton" collection="<%= cartonInfo %>" type="com.freshdirect.fdstore.customer.FDCartonInfo" indexId="counter"> 
			<% if(!"0000000000".equalsIgnoreCase(carton.getCartonInfo().getCartonNumber())){
			 if(bShowForm) { %>
				<FORM ACTION="<%= successPage %>&cartonNumber=<%=carton.getCartonInfo().getCartonNumber()%>" METHOD="POST" NAME="carton_<%= counter %>">
			<% } //bShowForm%>
				<table width="100%" class="order" cellspacing="0" cellpadding="0">
					<tr valign="top" class="list_odd_row">
						<td>
							Carton: <%= carton.getCartonInfo().getCartonNumber() %>
							<input type="hidden" name="itemCount" value="<%= carton.getCartonDetails().size() %>">
							<input type="hidden" name="cartUpdated" value="true">
						</td>
						<td>Type: <%= carton.getCartonInfo().getCartonType() %></td>
						<td colspan="2">
							<% if(bShowForm) { %>
								<input type="image" name="addMultipleToCart" src="/media_stat/images/template/quickshop/qs_add_selected_to_cart.gif" width="145" height="17" border="0" vspace="8" alt="ADD CARTON TO CART">
							<% } //bShowForm%>
						</td>
					</tr>
					<tr>
						<td colspan="4" class="list_separator" style="padding: 0px;"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="1"></td>
					</tr>
					
					<% idx = 0; %>
					<logic:iterate id="cartonDetail" collection="<%= carton.getCartonDetails() %>" type="com.freshdirect.fdstore.customer.FDCartonDetail" indexId="det"> 
						<% if(cartonDetail.getCartLine() != null) { %>
							<tr valign="top">
								<td width="150px"> 
									<input type="hidden" name="skuCode_<%=idx%>" value="<%= cartonDetail.getCartLine().getSkuCode() %>">
									<input type="hidden" name="quantity_<%=idx%>" value="<%= cartonDetail.getCartonDetail().getActualQuantity() %>">
									<input type="hidden" name="salesUnit_<%=idx%>" value="<%= cartonDetail.getCartLine().getSalesUnit() %>">
									<input type="hidden" name="estPrice_<%=idx%>" value="">
									<input type="hidden" name="originalOrderLineId_<%=idx%>" value="<%= cartonDetail.getCartLine().getOrderLineId() %>">

									<logic:iterate id="entry" collection="<%= cartonDetail.getCartLine().getConfiguration().getOptions().entrySet() %>" type="java.util.Map.Entry">
										<input type="hidden" name='<%= entry.getKey() + "_" + idx %>' value="<%= entry.getValue() %>">
									</logic:iterate>

									<%= cartonDetail.getCartonDetail().getActualQuantity() %>&nbsp;
									<% if(cartonDetail.getCartonDetail().getWeightUnit() != null) { %>
										<%= cartonDetail.getCartonDetail().getWeightUnit().toLowerCase() %>
									<% } %>
								</td>
								<td>
									<span style="margin-left: 20px;"><%= cartonDetail.getCartLine().getDescription() %>
									(<%= cartonDetail.getCartLine().getSkuCode() %>)</span>
								</td>
								<td width="90px"> 
									<%= cartonDetail.getCartonDetail().getNetWeight() %>
								</td>
								<td width="75px">
									<%= cartonDetail.getCartLine().getUnitPrice() %>
								</td>
							</tr>
							
							<% 
								if(cartonDetail.getCartonDetail().getComponents().size() > 0 ) { 
									for (int j = 0; j < cartonDetail.getCartonDetail().getComponents().size(); j++) {
										ErpCartonDetails component = cartonDetail.getCartonDetail().getComponents().get(j);
							
							%>
								
								<tr valign="top" class="list_component_row">
									<td width="150px">										
										<%= component.getActualQuantity() %>&nbsp;
									</td>
									<td>
										<span style="margin-left: 20px;"><%= component.getMaterialDesc() %>
										(<%= component.getSkuCode() %>)</span>
									</td>
									<td width="90px"> 
										<%= component.getNetWeight() %>
										<% if(component.getWeightUnit() != null) { %>
											<%= component.getWeightUnit().toLowerCase() %>
										<% } %>
									</td>
									<td width="75px">
										
									</td>
								</tr>							
							
							<% 		}
								} 
							%>
							
							<%

							idx++;
						} // cartonDetail.getCartLine() != null %>
						
					</logic:iterate>
				</table>

				<% if(bShowForm) { %>
					</FORM>
				<% } //bShowForm%>
				<% } %>
			</logic:iterate>
		</td>
	</tr>
  <% } else { %>
	<tr valign="top">
	  No cartons found!
	</tr>
  <% } %>
</table>
</fd:GetOrder>
		</tmpl:put>
</tmpl:insert>


