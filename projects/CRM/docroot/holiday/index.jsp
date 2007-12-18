<%@ page import="java.text.*, java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.content.meal.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>
	
	<tmpl:put name='title' direct='true'>/ FD CRM : Holiday Order /</tmpl:put>

<%!	
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat longDateFormatter = new SimpleDateFormat("EEEEEEEE, MMMMMMMM d, yyyy");
    NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
    Comparator deliveryComparator = new Comparator() {
        public int compare(Object o1, Object o2) {
            MealModel m1 = (MealModel) o1;
            MealModel m2 = (MealModel) o2;
            if (m1.getDelivery().before(m2.getDelivery())) {
                return -1;
            } else if (m1.getDelivery().after(m2.getDelivery())) {
                return 1;
            } else {
                return 0;
            }
        }
    };
%>

<tmpl:put name='content' direct='true'>
<br><br><div align="center">...Holiday Orders Coming Soon...</div>
<%--
<fd:GetCustomerObjs fdCustomerId="fdCustomer" erpCustomerId="erpCustomer" erpCustomerInfoId="erpCustomerInfo">
<fd:GetHolidayMeals id='meals'>
<%	
	Collections.sort(meals, deliveryComparator);
%>
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="2" BORDER="0" ALIGN="CENTER">
	<TR VALIGN="MIDDLE">
		<TD WIDTH="33%" CLASS="text13blackbold"><FONT CLASS="space4pix"><BR></FONT><FONT CLASS="space2pix"><BR></FONT>&nbsp;Holiday Meal Orders</TD>
		<TD WIDTH="33%" ALIGN="RIGHT">&nbsp;&nbsp;</TD>
		<TD WIDTH="33%" CLASS="text8redbold" BGCOLOR="#FFFFFF" ALIGN="RIGHT"> <FONT CLASS="space4pix"><BR></FONT>&nbsp;<BR></TD>
	</TR>
</TABLE>
<FONT CLASS="space4pix"><BR></FONT>
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR VALIGN="BOTTOM" BGCOLOR="#9999CC" >
        <TD CLASS="text8whitebold" width="20%">&nbsp;&nbsp;Order #</TD>
		<TD CLASS="text8whitebold" width="20%">Delivery</TD>
        <TD CLASS="text8whitebold" width="20%">Status</TD>
        <TD CLASS="text8whitebold" width="20%">Name</TD>
        <TD CLASS="text8whitebold" width="20%">Price</TD>
	</TR>
<%	if ( meals.size() == 0 ) { %>
	<TR VALIGN="BOTTOM">
		<td colspan="5" align="center"><br>No holiday meal orders found for <b><%= erpCustomerInfo.getFirstName() %> <%= erpCustomerInfo.getLastName() %></b>.</td>
	</TR>
<% 	} %>
<logic:iterate id="meal" collection="<%= meals %>" type="com.freshdirect.fdstore.content.meal.MealModel" indexId="counter">
	<TR VALIGN="BOTTOM" BGCOLOR="<%= counter.intValue() % 2 == 0 ? "#EEEEEE" : "#FFFFFF" %>">
        <TD width="20%">&nbsp;&nbsp;
        <%  if (meal.getStatus().equals(EnumMealStatus.CAN) || meal.getStatus().equals(EnumMealStatus.DEL)) { %>
            <%= meal.getPK().getId() %>
        <%  } else { %>
            <a href="/holiday/meal.jsp?mealId=<%= meal.getPK().getId() %>"><%= meal.getPK().getId() %></a>
        <%  } %>
        </TD>
		<TD width="20%"><%= longDateFormatter.format(meal.getDelivery()) %></TD>
        <TD width="20%"><%= meal.getStatus().getDisplayName() %></TD>
        <TD width="20%"><%= meal.getName() %></TD>
        <TD width="20%"><%= currencyFormatter.format(meal.getPrice()) %></TD> 
	</TR>
</logic:iterate>
</TABLE>
<br><br><br><br>
<table width="100%" cellpadding="0" cellspacing="2" border="0">
    <tr>
        <td align="center"><a href="meal.jsp">Order a meal for <%= erpCustomerInfo.getFirstName() %> <%= erpCustomerInfo.getLastName() %></a></td>
    </tr>
</table>
</fd:GetHolidayMeals>
</fd:GetCustomerObjs>
--%>
</tmpl:put>

</tmpl:insert>


