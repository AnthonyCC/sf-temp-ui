<%@ page import='javax.servlet.http.HttpSession'%>

<%@ page import='com.freshdirect.customer.ErpCustomerInfoModel'%>
<%@ page import='com.freshdirect.fdstore.customer.FDUserI'%>
<%@ page import='com.freshdirect.fdstore.customer.FDIdentity'%>
<%@ page import='com.freshdirect.fdstore.customer.FDCustomerFactory'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed="true" />
<%
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
FDIdentity identity = user.getIdentity();
boolean loggedIn = user.getLevel() > 1;
boolean hasIdentity = false;
ErpCustomerInfoModel erpCust = null;
if (identity != null) {
	erpCust = FDCustomerFactory.getErpCustomerInfo(identity);
	hasIdentity = true;
}
boolean isPost = request.getMethod().equalsIgnoreCase("POST");

String catId = request.getParameter("catId"); 
String successPage = request.getRequestURI();
boolean submitted = "thankYou".equalsIgnoreCase(request.getParameter("info"));
%>
<tmpl:insert template='/common/template/right_nav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Catering</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<fd:CateringOrderTag result="result" actionName="sendCateringOrder" successPage="/departments/hmr/catering.jsp?deptId=hmr">
			<table width="565" cellpadding="0" cellspacing="0" border="0">
				<form name="catering" method="post">
				<tr>
					<td colspan="7" align="center" class="text12"><img src="/media_stat/images/layout/clear.gif" width="1" height="8"><br>
					<% if (submitted) { %>
						<span class="title18"><b>Thank you!</b></span><br>
						We will contact you within 24 hours.
					<% } else { %>
						<span class="title18"><b>Catering from the FreshDirect Kitchen</b></span>
					<% } %><br>
					<img src="/media_stat/images/template/kitchen/catering/catering.jpg" width="375" height="71" vspace="8">
					<br>
					<b>From sweets to sandwiches to ready-to-eat entr&eacute;es, we've got your event covered.<br>
					Our extensive catering menu includes:</b><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=breakfast','large_long')">Breakfast Platters</a><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=party','large_long')">Party Platters</a><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=entree','large_long')">Entr&eacute;es</a><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=salad','large_long')">Salads</a><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=sandwich','large_long')">Sandwich Platters & Heros</a><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=kids','large_long')">Kids' Menu</a><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=dessert','large_long')">Dessert Platters</a><br>
					<span class="space4pix"><br></span>
					<a href="javascript:popup('/departments/hmr/catering_menu.jsp?show=hors_doeuvre','large_long')">Hors d'Oeuvres</a><br>
					<span class="space8pix"><br></span>
					<a href="/media_stat/pdf/catering_menu.pdf" target="fd_pdf"><b>Download a printable PDF file of our menu</b></a><br><span class="space4pix"><br></span>
					</td>
				</tr>
			<%-- FORM --%>
			<% if (!submitted) { %>
				<tr>
					<td colspan="7" align="center" class="text12">
					<img src="/media_stat/images/layout/cccccc.gif" width="565" height="1" vspace="8"><br>
					<span class="text11"><b>Please contact us for details by submitting the form below at least three days in advance of your event. We will contact you within 24 hours. Fields marked with <font color="#CC0000">*</font> are required.</b></span>
					<img src="/media_stat/images/layout/cccccc.gif" width="565" height="1" vspace="8"><br>
					</td>
				</tr>
				<tr>
					<td><img src="/media_stat/images/layout/clear.gif" width="150" height="8"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="5" height="8"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="165" height="8"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="25" height="8"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="50" height="8"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="5" height="8"></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="150" height="8"></td>
				</tr>
				<tr valign="top">
					<td align="right" class="text12"><span class="space4pix"><br></span><font color="#CC0000">*</font>First Name&nbsp;</td>
					<td></td>
					<td><input type="text" name="firstName" class="text13" size="24" value="<%= (loggedIn && hasIdentity && !isPost) ? erpCust.getFirstName() : request.getParameter("firstName") %>"><fd:ErrorHandler result='<%=result%>' name="firstName" id="errorMsg"><br><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
					<td colspan="2" align="right" class="text12"><span class="space4pix"><br></span><font color="#CC0000">*</font>Last Name&nbsp;</td>
					<td></td>
					<td><input type="text" name="lastName" class="text13" size="20" value="<%= (loggedIn && hasIdentity && !isPost) ? erpCust.getLastName() : request.getParameter("lastName") %>"><fd:ErrorHandler result='<%=result%>' name="lastName" id="errorMsg"><br><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
				</tr>
				<tr><td colspan="7"><span class="space2pix"><br></span></td></tr>
				<tr valign="top">
					<td align="right" class="text12"><span class="space4pix"><br></span><font color="#CC0000">*</font>Contact #</td>
					<td></td>
					<td colspan="2"><input type="text" class="text13" name="phone" size="30" value="<%= (loggedIn && hasIdentity && !isPost) ? erpCust.getHomePhone().getPhone() : request.getParameter("phone") %>"><fd:ErrorHandler result='<%=result%>' name="phone" id="errorMsg"><br><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler></td>
					<td align="right" class="text12"><span class="space4pix"><br></span>Ext.</td>
					<td></td>
					<td><input type="text" name="phoneExt" class="text13" size="6" value="<%= request.getParameter("phoneExt") %>"></td>
				</tr>
				<tr><td colspan="7"><span class="space2pix"><br></span></td></tr>
				<tr valign="top">
					<td align="right" class="text12"><span class="space4pix"><br></span>Best time to call</td>
					<td></td>
					<td colspan="5"><input type="text" class="text13" name="bestTime" size="30" value="<%= request.getParameter("bestTime") %>"></td>
				</tr>
				<tr><td colspan="7"><span class="space2pix"><br></span></td></tr>
				<tr valign="top">
					<td align="right" class="text12"><span class="space4pix"><br></span><font color="#CC0000">*</font>Email Address</td>
					<td></td>
					<td colspan="5"><input type="text" class="text13" name="email" size="30" value="<%= (loggedIn && hasIdentity && !isPost) ? erpCust.getEmail() : request.getParameter("email") %>"><fd:ErrorHandler result='<%=result%>' name="email" id="errorMsg"><span class="text11rbold"> <%=errorMsg%></span></fd:ErrorHandler></td>
				</tr>
				<tr><td colspan="7"><span class="space2pix"><br></span></td></tr>
				<tr valign="top">
					<td align="right" class="text12"><span class="space4pix"><br></span>Date of event</td>
					<td></td>
					<td colspan="5"><input type="text" class="text13" name="eventDate" size="30" value="<%= request.getParameter("eventDate") %>"></td>
				</tr>
				<tr><td colspan="7"><span class="space2pix"><br></span></td></tr>
				<tr valign="top">
					<td align="right" class="text12"><span class="space4pix"><br></span>Number of guests</td>
					<td></td>
					<td colspan="5"><input type="text" class="text13" name="partySize" size="8" value="<%= request.getParameter("partySize") %>"></td>
				</tr>
				<tr><td colspan="7"><span class="space2pix"><br></span></td></tr>
				<tr valign="top">
					<td align="right" class="text12"><span class="space4pix"><br></span>Brief summary of event and type of food required</td>
					<td></td>
					<td colspan="5"><textarea name="summaryRequest" class="text13" rows="6" wrap="VIRTUAL" style="width: 390px;"><%= request.getParameter("summaryRequest") %></textarea></td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td colspan="5"><span class="space4pix"><br></span><input type="image" value="SUBMIT" src="/media_stat/images/buttons/submit.gif" width="48" height="16" onClick="catering.submit()"><br><br></td>
				</tr>
			<% } %>
			<%-- FORM --%>
				<% if (submitted) { %>
					<tr><td colspan="7" align="center" class="text12"><br><a href="/departments/hmr/catering.jsp?deptId=hmr"><i>Submit another catering request.</i></a><br><br></td></tr>
				<% } %>
				</form>
			</table>
		</fd:CateringOrderTag>
	</tmpl:put>	
</tmpl:insert>