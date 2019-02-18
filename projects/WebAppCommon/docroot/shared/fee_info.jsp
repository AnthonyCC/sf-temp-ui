<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
		request.setAttribute("sitePage", "www.freshdirect.com/shared/fee_info.jsp");
		
		String type = request.getParameter("type");
		boolean bottleDeposit = "bottle".equalsIgnoreCase(type);
		boolean fuelSurcharge = "fuel".equalsIgnoreCase(type);
		String template = "/shared/template/large_pop.jsp";
		String filePath = "/media/editorial/fee/fuel_surcharge_oas.html";
		String title = "Fuel Surcharge";
		
		if (bottleDeposit) {
			template = "/shared/template/small_pop.jsp";
			filePath = "/media/editorial/fee/bottle_deposit.html";
			title = "State Bottle Deposit"; 
		} 
%>

<tmpl:insert template='<%=template%>'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%=title%></tmpl:put>
    <tmpl:put name='content' direct='true'>
			<fd:IncludeMedia name="<%=filePath%>" />
    </tmpl:put>
</tmpl:insert>
