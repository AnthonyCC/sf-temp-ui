<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:FDShoppingCart id='cart' action='addMultipleToCart' result='result' successPage='<%= "/cart_confirm_pdp.jsp?catId="+request.getParameter("catId") %>'>
</fd:FDShoppingCart>