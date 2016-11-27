<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib prefix="spring1" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c1" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt1" uri="http://java.sun.com/jstl/fmt" %>
<%@ page import='com.freshdirect.mktAdmin.model.ReferralAdminModel' %>


<tmpl:insert template='site.jsp'>

    <tmpl:put name='title' direct='true'>Home Page</tmpl:put>

	<tmpl:put name='content' direct='true'>
	
	<form method="post" enctype="multipart/form-data">	
	
	<% String success = (String) session.getAttribute("success");
	   String showApprove = (String) session.getAttribute("showApprove");
	   ReferralAdminModel model = (ReferralAdminModel) session.getAttribute("model");		
	   if("true".equals(success)) {
			//success come here
	%>
		<br/>	
		<div align="center">
			<table border="0" align="center"> <tbody>
				<tr><td colspan="2"><h2>Successfully Created Refer A Friend Promotion</h2></td></tr>
				<% if(!model.getDefaultPromo()) { 
					List<String> invalidUsers = (List) session.getAttribute("invalidusers");
					if(invalidUsers != null && invalidUsers.size() > 0) {					
				%>
				<tr><td colspan="2">Below users are not added to this campaign as they are invalid customer ids or they have been already added into another active campaign</td></tr>
				<tr><td colspan="2">
				<% java.util.Iterator iter =  invalidUsers.iterator(); 
					while(iter.hasNext()) {
				%>	
					<%= (String) iter.next() %> <br/>
				<% } %>
				</td></tr>
				<% } } %>
			</tbody></table>
			<a href="welcome.do">Go Home</a>	
		</div>
	<%
	   }
	   else if("true".equals(showApprove)) {		
	%>
		<input type="hidden" name="action" value="addRef" />
		<br/>	
		<div align="center">
			<table border="0" align="center"> <tbody>
				<tr><td colspan="2"><h2>Review Refer A Friend Promotion</h2></td></tr>
				<tr><td>Campaign Name:</td><td><%=model.getDescription()%></td></tr>
				<tr><td>Referral System Code:</td><td><%=model.getPromotionId()%></td></tr>
				<tr><td>Share Headerline:</td><td><%=model.getShareHeader()%></td></tr>
				<tr><td>Share Description:</td><td><%=model.getShareText()%></td></tr>				
				<tr><td>Give Header:</td><td><%=model.getGiveHeader()%></td></tr>
				<tr><td>Give Description:</td><td><%=model.getGiveText()%></td></tr>
				<tr><td>Get Header:</td><td><%=model.getGetHeader()%></td></tr>
				<tr><td>Get Description:</td><td><%=model.getGetText()%></td></tr>
				<tr><td>Referral Credit:</td><td>$<%=model.getReferralFee()%></td></tr>
				<tr><td>Campaign Expiration Date:</td><td><%=model.getExpirationDate()%></td></tr>
				<tr><td>Default Campaign:</td><td><%=model.getDefaultPromo()?"Yes":"No"%></td></tr>				
				<% if(!model.getDefaultPromo()) { %>
					<tr><td>Customer Email List:</td><td><%= model.getName() %></td></tr>
				<% } %>
				<tr><td>Notes:</td><td><%=model.getNotes()%></td></tr>
				<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
				</td></tr>
					
				<!----------------facebook---------------------------------------->
				<tr><td colspan="2"><b>Facebook</b></td></tr>
				<tr><td>Image Upload:</td><td><%=model.getFbFile()%></td></tr>
				<tr><td>Headline:</td><td><%=model.getFbHeadline()%></td></tr>
				<tr><td>Text:</td><td><%=model.getFbText()%></td></tr>
				
				<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
				</td></tr>
					
				<!----------------Twitter---------------------------------------->
				<tr><td colspan="2"><b>Twitter</b></td></tr>
				<tr><td>Text:</td><td><%=model.getTwitterText()%></td></tr>
				
				<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
					</td></tr>
					
				<!----------------Referral landing page---------------------------------------->
				<tr><td colspan="2"><b>Referral landing page</b></td></tr>					
				<tr><td valign="top">Default text for website: <br/>Note this text may be deleted by the customer</td>
				<td><%=model.getReferralPageText()%></td></tr>
				<tr><td>Legal:</td><td><%=model.getReferralPageLegal()%></td></tr>
					
				<tr><td colspan="2" align="center">
				<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
				</td></tr>
					
				<!----------------Invitation Email---------------------------------------->
				<tr><td colspan="2"><b>Invitation Email</b></td></tr>
				<tr><td>Email Subject Line:</td><td><%=model.getInviteEmailSubject()%></td></tr>
				<tr><td>Offer Text:</td><td><%=model.getInviteEmailOfferText() %></td></tr>
				<tr><td>Default text for website: <br/>Note this text may be deleted by the customer:</td>
				<td><%=model.getInviteEmailText()%></td></tr>
				<tr><td>Legal:</td><td><%=model.getInviteEmailLegal()%></td></tr>
				
				<tr><td colspan="2" align="center">
				<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
				</td></tr>
					
				<!----------------Referee Credit Earned Email---------------------------------------->
				<tr><td colspan="2"><b>Referee Credit Earned Email</b></td></tr>
				<tr><td>Subject Line:</td><td><%=model.getReferralCreditEmailSubject()%></td></tr>
				<tr><td>Default Text for Email:</td><td><%=model.getReferralCreditEmailText()%></td></tr>
				
				<tr>
				<td colspan="2" align="center"><INPUT type = "submit" value="Modify"  name="modifybutton"/>&nbsp;&nbsp;&nbsp;
				<INPUT type = "submit" value="Approve"  /></td>					
				</tr>
					
			</tbody></table>	
		</div>
		
	<% } if(!"true".equals(success)) { %>
	 
		<br/>	
		<div align="center" id="mainform" style="display:<%=("true".equals(success) || "true".equals(showApprove))?"none":"block"%>;">			
				<input type="hidden" name="submission" value="true" />
				<center><h3>Create Refer a Friend Promotion</h3></center>
				<table border="0" align="center"> <tbody>					
					<tr><td>Campaign Name:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td><spring1:bind path="command.description">						
						<input type="text" name="description" value="<c1:out value='${status.value}'/>" size="50"/>
						<c1:if test="${not empty status.errorMessage}" >
							<br/>
							<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
						</c1:if>
						</spring1:bind>
					</td>
					</tr>
					<tr><td>Referral Promotion:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>					
					<td><spring1:bind path="command.promotionId"><select name="promotionId">					
					<c1:if test="${empty status.value}" >
					<option value="-1">-Select Promotion-</option>
					</c1:if>
					<c1:forEach var="promo" items="${promotions}">
						<c1:choose>
							<c1:when test="${status.value eq promo.promotionId}">
								<OPTION value="<c1:out value="${promo.promotionId}" />" selected="true"><c1:out value="${promo.description}" /></OPTION>
							</c1:when>
							<c1:otherwise>
								<OPTION value="<c1:out value="${promo.promotionId}" />"><c1:out value="${promo.description}" /></OPTION>
							</c1:otherwise>
						</c1:choose>
						
					</c1:forEach>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>					
					</spring1:bind>
					</td>
					</tr>
					<tr><td>Share Headline:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td>
					<spring1:bind path="command.shareHeader"><input type="text" name="shareHeader" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					<tr><td>Share Description:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td>
					<spring1:bind path="command.shareText"><input type="text" name="shareText" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					<tr><td>Give Header:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td>
					<spring1:bind path="command.giveHeader"><input type="text" name="giveHeader" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					<tr><td>Give Description:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td>
					<spring1:bind path="command.giveText"><input type="text" name="giveText" value="<c1:out value='${status.value}'/>" size="50"/>
						<c1:if test="${not empty status.errorMessage}" >
							<br/>
							<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
						</c1:if>
					</spring1:bind></td>
					</tr>
					<tr><td>Get Header:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td>
					<spring1:bind path="command.getHeader"><input type="text" name="getHeader" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					<tr><td>Get Description:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td>
					<spring1:bind path="command.getText"><input type="text" name="getText" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					<tr><td>Referral Credit:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td>
					<spring1:bind path="command.referralFee"><input type="text" name="referralFee" value="<c1:out value='${status.value}'/>" size="25"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					<tr><td>Campaign Expiration Date:<span style="color:red;font-weight:bold;font-size:12px;">*</span></td>
					<td><spring1:bind path="command.expirationDate">
							<input id="calendar-inputField1" type="text" name="expirationDate" value="<c1:out value='${status.value}'/>"/><button id="calendar-trigger1">...</button>
							<script language="Javascript">
								Calendar.setup({
									inputField : "calendar-inputField1",
									trigger    : "calendar-trigger1",
									showTime   : "12",
									fixed      : true,
									dateFormat : "%m/%d/%Y",
									onSelect   : function() { this.hide() },
									align      : "Bc/Bc/Bc/Bc/Bc"
								});
							</script>
							<c1:if test="${not empty status.errorMessage}" >
								<br/>
								<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
							</c1:if>
					</spring1:bind>
					</td>
					</tr>					
					<tr><td>Customer Email List</td>					
					<td><spring1:bind path="command.userListFile">					
					<input type="file" name="userListFile"/>
					<br/> <span style="color:blue;font-size:11px;font-style:italic;">Add CSV file with just user email addresses. **Cusomter List is not required if Default promo is selected.**</span>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind>
					<%if("true".equals(showApprove)) { 
						ReferralAdminModel model1 = (ReferralAdminModel) session.getAttribute("model");
					%>
						<spring1:bind path="command.userListFileHolder">	
							<input type="hidden" name="userListFileHolder" value="<%= model1.getName() %>"/>						
						</spring1:bind>
					<% } %>	
					</td>
					</tr>
					</tr>
					<tr><td>Default Campaign:</td>
					<td>
					<spring1:bind path="command.defaultPromo">
					<input type="hidden" name="_defaultPromo" value="visible" />
					<input type="checkbox" name="defaultPromo" value="true" <c1:if test="${status.value}">checked </c1:if> />
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td valign="top">Notes:</td>
					<td>
					<spring1:bind path="command.notes"><textarea name="notes" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
					</td></tr>
					
					<!----------------facebook---------------------------------------->
					<tr><td colspan="2"><b>Facebook</b></td></tr>
					<tr><td valign="top">Image Upload:</td>
					<td>
					<spring1:bind path="command.fbFile"><input type="text" name="fbFile" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr>
					<td valign="top">Head Line:</td>
					<td>
					<spring1:bind path="command.fbHeadline"><input type="text" name="fbHeadline" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td valign="top">Text:</td>
					<td>
					<spring1:bind path="command.fbText"><textarea name="fbText" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
					</td></tr>
					
					<!----------------Twitter---------------------------------------->
					<tr><td colspan="2"><b>Twitter</b></td></tr>
					<tr><td valign="top">Text:</td>
					<td>
					<spring1:bind path="command.twitterText"><textarea name="twitterText" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
					</td></tr>
					
					<!----------------Referral landing page---------------------------------------->
					<tr><td colspan="2"><b>Referral landing page</b></td></tr>
					
					<tr><td valign="top">Default text for website: <br/>Note this text may be deleted by the customer</td>
					<td>
					<spring1:bind path="command.referralPageText"><textarea name="referralPageText" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td valign="top">Legal</td>
					<td>
					<spring1:bind path="command.referralPageLegal"><textarea name="referralPageLegal" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
					</td></tr>
					
					<!----------------Invitation Email---------------------------------------->
					<tr><td colspan="2"><b>Invitation Email</b></td></tr>
					
					<tr>
					<td valign="top">Email Subject Line:</td>
					<td>
					<spring1:bind path="command.inviteEmailSubject"><input type="text" name="inviteEmailSubject" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr>
					<td valign="top">Offer Text:</td>
					<td>
					<spring1:bind path="command.inviteEmailOfferText"><textarea name="inviteEmailOfferText" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td valign="top">Default text for website: <br/>Note this text may be deleted by the customer</td>
					<td>
					<spring1:bind path="command.inviteEmailText"><textarea name="inviteEmailText" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td valign="top">Legal</td>
					<td>
					<spring1:bind path="command.inviteEmailLegal"><textarea name="inviteEmailLegal" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
					</td></tr>
					
					<!----------------Referee Credit Earned Email---------------------------------------->
					<tr><td colspan="2"><b>Referee Credit Earned Email</b></td></tr>
					
					<tr>
					<td valign="top">Subject Line:</td>
					<td>
					<spring1:bind path="command.referralCreditEmailSubject"><input type="text" name="referralCreditEmailSubject" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td valign="top">Default text for website:</td>
					<td>
					<spring1:bind path="command.referralCreditEmailText"><textarea name="referralCreditEmailText" cols="50" rows="3"><c1:out value='${status.value}'/></textarea>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr><td colspan="2" align="center">
					<hr style="color: #FFF; background-color: #FFF; border: 1px dotted #000000; border-style: none none dotted;" />
					</td></tr>
					
					<!----------------Media for the RAF siteaccess page---------------------------------------->
					<tr><td colspan="2"><b>RAF SiteAccess Page</b></td></tr>
					
					<tr>
					<td valign="top">Image Upload:</td>
					<td>
					<spring1:bind path="command.siteAccessImageFile"><input type="text" name="siteAccessImageFile" value="<c1:out value='${status.value}'/>" size="50"/>
					<c1:if test="${not empty status.errorMessage}" >
						<br/>
						<span style="color:red;font-weight:bold;font-size:12px;"><c1:out value="${status.errorMessage}" /></span>
					</c1:if>
					</spring1:bind></td>
					</tr>
					
					<tr>
					<td colspan="2" align="center">&nbsp;</td>					
					</tr>
					
					<tr>
					<td colspan="2" align="center"><INPUT type = "button" value="Cancel"  onclick="window.location.href='viewRefPromo.do'"/>&nbsp;&nbsp;&nbsp;
					<INPUT type = "submit" value="Add Promotion"  /></td>					
					</tr>
				</tbody>
			</table>
		 </div>
		 
		 <% } %>
		</form>
</tmpl:put>
</tmpl:insert>

