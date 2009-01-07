<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='hasSubs' direct='true'>subs</tmpl:put>

    <tmpl:put name='title' direct='true'> Operations : Route : Ad Hoc Routes : Add/Edit Route</tmpl:put>

  <tmpl:put name='content' direct='true'>
  <div class="MNM001 subsub or_999">

		<div class="subs_left">	
		
			<div class="sub_tableft sub_tabL_MNM001 <% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="route.do?routetype=R" class="<% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>MNM001<% } %>">Active Routes</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if("R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeR<% } %>">&nbsp;</div>
            
            <div class="sub_tableft sub_tabL_MNM001 <% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="route.do" class="<% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>MNM001<% } %>">Ad Hoc Routes</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM001 <% if(!"R".equalsIgnoreCase(request.getParameter("routetype"))) { %>activeR<% } %>">&nbsp;</div>		

		</div>
	</div>
				

	<div class="contentroot">

			<div class="cont_row">
					<br />
					  <form:form commandName = "routeForm" method="post">
					  <form:hidden path="routeId"/>
					  
					  <table width="100%" cellpadding="0" cellspacing="0" border="0">
						  <tr>
							<td class="screentitle">Add/Edit Route</td>
						  </tr>
						  <tr>
							<td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
						  </tr>
						  
						  <tr>
							<td class="screencontent">
							  <table class="forms1">          
								<tr>
								  <td>Route Number</td>
								  <td>                  
									<form:input maxlength="50" size="30" path="routeNumber" />
								</td>
								<td>
								  &nbsp;<form:errors path="routeNumber" />
								</td>
							   </tr>
											
								<tr>
								  <td>Description</td>
								  <td>                  
									<form:input maxlength="50" size="30" path="description" />
								</td>
								<td>
								  &nbsp;<form:errors path="description" />
								</td>
							   </tr>


							   <tr>
								  <td>Route Timing</td>
								  <td> 
								  <form:select path="routeAmPm">
										<form:option value="null" label="--Please Select Zone"/>
									<form:options items="${timings}"  />
								   </form:select>
								</td>
								<td>
								  &nbsp;<form:errors path="routeAmPm" />
								</td>
							   </tr>
														   
							  <tr><td colspan="3">&nbsp;</td></tr>
							  <tr>
								  <td colspan="3" align="center">
								   <input type = "submit" value="&nbsp;Save&nbsp;"  />
								</td>     
							  </tr>
							  </table>        
							  
							</td>
						  </tr>               
						</table>
					  
					  </form:form>

		 </div>
     </div>
     
  </tmpl:put>
</tmpl:insert>
