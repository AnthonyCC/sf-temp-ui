<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.mktAdmin.util.CustomerModel' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

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
	
	<% if("mails".equals(request.getParameter("action")) || "sendmails".equals(request.getParameter("action"))) {
	
		if("sendmails".equals(request.getParameter("action")) && session.getAttribute("error") == null) {
			//request complete
		%>
		<table>
		<tr><td colspan="2"><font color="red"><b>Emails sent successfully to all below recipients:</b></font>
			</td>
		</tr>
		<tr><td valign="top"><b>Recipients:</b></td>
			<td><%= request.getParameter("toemails") %></td>
		</tr>
		<tr><td valign="top"><b>Subject:</b></td>
			<td><%= request.getParameter("subject") %></td>
		</tr>
		<tr><td valign="top"><b>Message:</b></td>
			<td><%= request.getParameter("message") %></td>
		</tr>
		</table>
		<%
		} else {	
			StringBuffer sb = new StringBuffer();
			if(session.getAttribute("customers") != null) {
				List cList = (List) session.getAttribute("customers");
				java.util.Iterator iter = cList.iterator();							
				while(iter.hasNext()) {
					CustomerModel cm = (CustomerModel) iter.next();
					sb.append(cm.getEmail());
					if(iter.hasNext())
						sb.append(";");				
				}
			}
	%>

	
	<form action="upsOutage.do" method="POST">
	 <input type="hidden" name="action" value="sendmails" />
	 <table>
		<tr><td colspan="2"><font color="red"> <%=session.getAttribute("error")%> </font>
			</td>
		</tr>
		<tr><td valign="top"><b>From:</b></td>
			<td><input name="femail" value="<%=FDStoreProperties.getCustomerServiceEmail() %>" /></td>
		</tr>
		<tr><td valign="top"><b>To:</b></td>
			<td><input name="toemails" value="<%=sb.toString() %>" /><br/>
			Emails shoudl be separated with semi-colon(;)</td>
		</tr>
		<tr><td valign="top"><b>Subject:</b></td>
			<td><input name="subject" value="<%= request.getParameter("subject") %>" /></td>
		</tr>
		<tr><td valign="top"><b>Message:</b></td>
			<td><textarea name="message" rows="10" cols="50" ><%= request.getParameter("message") %></textarea></td>
		</tr>
		<tr><td valign="top">&nbsp;</td>
		    <td align="center"><input type="submit" value="Send this email to all users" />
			</td>
		</tr>
	 </table>
	</form>
	<% } %>
    <% } else { %>
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
			
			<font color="red"> <%=session.getAttribute("error")%> </font>
			<p />
			
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
			<script language="Javascript">
				function poponload()
				{
					testwindow = window.open("upsOutage.do?action=mails&FromDate=<%=request.getParameter("FromDate")%>&ToDate=<%=request.getParameter("ToDate")%>","new","toolbar=no,directories=no,status=no,titlebar=no,menubar=no,scrollbars=no,resizable=no,width=800,height=600,top=0,left=0");
					testwindow.moveTo(0, 0);
				}				
			</script>
			
			<table>
				<tbody><tr>
					<td><a href="upsOutage.do?action=xls&FromDate=<%=request.getParameter("FromDate")%>&ToDate=<%=request.getParameter("ToDate")%>" style="background:#FFE900;color:#000;padding:3px 5px 3px 5px;border:1px solid #000;">Download Customer List To Excel</a></td>					
					<td><a href="#" onclick="window.open('upsOutage.do?action=mails&FromDate=<%=request.getParameter("FromDate")%>&ToDate=<%=request.getParameter("ToDate")%>','new','toolbar=no,directories=no,status=no,titlebar=no,menubar=no,scrollbars=yes,resizable=no,width=800,height=400,top=100,left=100')" style="background:#FFE900;color:#000;padding:3px 5px 3px 5px;border:1px solid #000;">Send Outage Email to the Customer List</a></td>
					
				</tr>
				</tbody>
			</table>
			<% } %>
		 </div>
		<% } %>
</tmpl:put>
</tmpl:insert>
<% } %>
