<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCartonInfo"%>
<%@ page import="com.freshdirect.framework.webapp.*"%>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>

<%	String orderId = request.getParameter("orderId"); %>
<%	List cartonInfo = null; %>
<%  List cartonDetails = null; %>
<%	String orderLineNumber = request.getParameter("orderLineNumber"); %>
<%	String showForm = request.getParameter("showForm"); %>
<%  boolean bShowForm = false; %>
<%  if("true".equals(showForm))
		bShowForm=true;
%>

<tmpl:insert template='/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>Carton Contents View</tmpl:put>
		<tmpl:put name='content' direct='true'>

<fd:GetOrder id='order' saleId='<%= orderId %>'>

<%
    if(orderLineNumber != null)
        cartonInfo = ((FDOrderAdapter)order).getCartonContents(orderLineNumber);
    else
    	cartonInfo = ((FDOrderAdapter)order).getCartonContents();
%>
<% String successPage = request.getRequestURI() + "?" + request.getQueryString(); %>
<% String updated = request.getParameter("cartUpdated"); %>
<% int idx = 0; %>
<% if(updated != null)
	   successPage += "&cartUpdated=" + updated; 
   %>

<fd:FDShoppingCart id='cart'	action='addMultipleToCart' result='result' multiSuccessPage='<%= successPage %>' successPage='<%= successPage %>'>
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
	  <th align="left">Quantity</th>
	  <th align="left">Product</th>
	  <th align="left">Final Weight</th>
	  <th align="left">Unit Price</th>
		<logic:iterate id="carton" collection="<%= cartonInfo %>" type="com.freshdirect.fdstore.customer.FDCartonInfo" indexId="counter"> 
<% if(bShowForm) { %>
 <FORM ACTION="<%= successPage %>&cartonNumber=<%=carton.getCartonInfo().getCartonNumber()%>" METHOD="POST" NAME="carton_<%= counter %>">
<% } //bShowForm%>
    <input type="hidden" name="itemCount" value="<%= carton.getCartonDetails().size() %>">
    <input type="hidden" name="cartUpdated" value="true">
	<tr valign="top" class="list_odd_row">
		<td>Carton: <%= carton.getCartonInfo().getCartonNumber() %></td>
		<td>Type: <%= carton.getCartonInfo().getCartonType() %></td>
		<td colspan="2">
<% if(bShowForm) { %>
	   		<input type="image" 
   				name="addMultipleToCart" 
   				src="/media_stat/images/template/quickshop/qs_add_selected_to_cart.gif" 
   				width="145" height="17" border="0" vspace="8" alt="ADD CARTON TO CART">
<% } //bShowForm%>
   		</td>
	</tr>
	<tr>
		<td colspan="4" class="list_separator" style="padding: 0px;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	</tr>
	
	<% idx = 0; %>
			<logic:iterate id="cartonDetail" collection="<%= carton.getCartonDetails() %>" type="com.freshdirect.fdstore.customer.FDCartonDetail" indexId="det"> 
    <% if(cartonDetail.getCartLine() != null) { %>
		    <input type="hidden" name="skuCode_<%=idx%>" value="<%= cartonDetail.getCartLine().getSkuCode() %>">
		    <input type="hidden" name="quantity_<%=idx%>" value="<%= cartonDetail.getCartonDetail().getPackedQuantity() %>">
		    <input type="hidden" name="salesUnit_<%=idx%>" value="<%= cartonDetail.getCartLine().getSalesUnit() %>">
		    <input type="hidden" name="estPrice_<%=idx%>" value="">
        <logic:iterate id="entry" collection="<%= cartonDetail.getCartLine().getConfiguration().getOptions().entrySet() %>" type="java.util.Map.Entry">
            <input type="hidden" name='<%= entry.getKey() + "_" + idx %>' value="<%= entry.getValue() %>">
        </logic:iterate>

			<tr valign="top">
				<td> 
					<%= cartonDetail.getCartonDetail().getPackedQuantity() %>&nbsp;
					<% if(cartonDetail.getCartonDetail().getWeightUnit() != null) { %>
 						<%= cartonDetail.getCartonDetail().getWeightUnit().toLowerCase() %>
 					<% } %>
				</td>
				<td>
					&nbsp;&nbsp;&nbsp;&nbsp;<%= cartonDetail.getCartLine().getDescription() %>
					(<%= cartonDetail.getCartLine().getSkuCode() %>)
				</td>
				<td> 
					<%= cartonDetail.getCartonDetail().getNetWeight() %>
				</td>
				<td>
					<%= cartonDetail.getCartLine().getUnitPrice() %>
				</td>
			</tr>
    <%    idx++;
    	} // cartonDetail.getCartLine() != null %>

			</logic:iterate>
<% if(bShowForm) { %>
 </FORM>
<% } //bShowForm%>
	</logic:iterate>
  <% } else { %>
	<tr valign="top">
	  No cartons found!
	</tr>
  <% } %>
</table>
</fd:GetOrder>
		</tmpl:put>
</tmpl:insert>


