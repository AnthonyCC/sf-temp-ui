<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.mktAdmin.util.CustomerModel' %>

<%@ taglib uri='template' prefix='tmpl' %>

<% if("xls".equals(request.getParameter("action"))) { %>
	<table border="1" >
		<tbody><tr>
			<td>FirstName</td>
			<td>LastName</td>
			<td>Email</td>
			<td>CustomerID</td>
		</tr>
			<% 	List cList = (List) session.getAttribute("customers");
				java.util.Iterator iter = cList.iterator();
				while(iter.hasNext()) {
					CustomerModel cm = (CustomerModel) iter.next();
				
				%>
					<tr><td><%= cm.getFirstName() %></td>
					    <td><%= cm.getLastName() %></td>
						<td><%= cm.getEmail() %></td>
						<td><%= cm.getCustomerId() %></td>
					</tr>
				<%
				}
			%>    

<% } else { %>
<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Home Page</tmpl:put>

	<tmpl:put name='content' direct='true'>
	
    
		<br/>	
		<div align="center">
			<form id="upsForm" action="upsOutage.do" method="post">	
				<input type="hidden" name="submission" value="true" />
				<table border="1"> <tbody>
					<tr>
						<td>
							Start Date: <input id="calendar-inputField1" name="FromDate" value='<%= request.getParameter("FromDate") %>'/> <button id="calendar-trigger1">...</button>
							<script language="Javascript">
								Calendar.setup({
									inputField : "calendar-inputField1",
									trigger    : "calendar-trigger1",
									showTime   : "12",
									fixed      : true,
									dateFormat : "%m/%d/%Y %I:%M %p",
									onSelect   : function() { this.hide() }
								});
							</script>
						</td>
						<td>
							End Date: <input id="calendar-inputField2" name="ToDate" value='<%= request.getParameter("ToDate") %>'/><button id="calendar-trigger2">...</button>
							<script language="Javascript">
								Calendar.setup({
									inputField : "calendar-inputField2",
									trigger    : "calendar-trigger2",
									showTime   : "12",
									fixed      : true,
									dateFormat : "%m/%d/%Y %I:%M %p",
									onSelect   : function() { this.hide() }
								});
							</script>
						</td>
						<td><input type="submit" name="Submit" value="Get Customer List"/></td>
					</tr> </tbody>
				</table>
			</form>
			
			<p />
			<% if(request.getParameter("ToDate") != null) { %>
			<ec:table id="upsListForm" items="customers" var="customers" action="upsOutage.do" filterable="false"
				    imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="UPS Outage Effected Customers List"
				    width="98%" rowsDisplayed="25">
				        
				    <ec:row>				    				    	                      
				      <ec:column property="firstName" title="FirstName"/>
                      <ec:column property="lastName" title="LastName"/>
				      <ec:column property="email" title="Email"/>
                      <ec:column property="customerId" title="CustomerID"/>
                                                                                    
				    </ec:row>

			</ec:table>
			
			<p />
			<table>
				<tbody><tr>
					<td><a href="upsOutage.do?action=xls&FromDate=<%=request.getParameter("FromDate")%>&ToDate=<%=request.getParameter("ToDate")%>" style="background:#FFE900;color:#000;padding:3px 5px 3px 5px;border:1px solid #000;">Download Customer List To Excel</a></td>
					<% 	List cList = (List) session.getAttribute("customers");
						java.util.Iterator iter = cList.iterator();
						StringBuffer sb = new StringBuffer();
						while(iter.hasNext()) {
							CustomerModel cm = (CustomerModel) iter.next();
							sb.append(cm.getEmail());
							if(iter.hasNext())
								sb.append(",");
						}
					%> 
					<td><a href="mailto:<%=sb.toString()%>" style="background:#FFE900;color:#000;padding:3px 5px 3px 5px;border:1px solid #000;">Send Outage Email to the Customer List</a></td>
				</tr>
				</tbody>
			</table>
			<% } %>
		 </div>
</tmpl:put>
</tmpl:insert>
<% } %>
