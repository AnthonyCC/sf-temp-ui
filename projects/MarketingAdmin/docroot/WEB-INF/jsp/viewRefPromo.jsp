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
	
	<% String success = (String) session.getAttribute("success");
	   if("true".equals(success)) {			
	%>
		<div style="float:left;padding:15px;color:red;font-size:12px;font-weight:bold;">Successfully applied the changes.</div><br/>
	<% } %>
	
	<form method="post" onSubmit="return confirm('Are you sure you want to delete this item?');">
	<spring1:bind path="command.action">
		<input type="hidden" name="action" value="deleteRefs" />
	</spring1:bind>
	
	<div align="center">
		<div class="eXtremeTable" >
			<br/>
			<span class="title" style="float:left;padding:15px;">Refer-A-Friend Promotions</span>
			<div style="float:right;padding:15px;">
				<input type="button" name="Create Promotion" value="Create Promotion" onclick="location.href='createRefPromo.do'"/> &nbsp;&nbsp;&nbsp;
				<input type="submit" name="Save Changes" value="Save Changes" />
			</div>
			<br/><br/>
			<table id="ec_table"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="98%" >
				<thead>
				<tr>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick="">Campaign Name</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick="">Referral Promotion</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By GetText" >Share Header</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By GetText" >Share Description</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By GetText" >Get Header</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By GetText" >Get Description</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By GetText" >Give Header</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By GiveText" >Give Description</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By ReferralFee" >Referral Credit</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By ExpirationDate" >Campaign Expiration Date</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By ExpirationDate" >Default Promo</td>					
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By ExpirationDate" >Delete</td>
					<td class="tableHeader"  onmouseover="this.className='tableHeaderSort';this.style.cursor='pointer'"  onmouseout="this.className='tableHeader';this.style.cursor='default'"  onclick=""  title="Sort By ExpirationDate" >Edit Details</td>
				</tr>
				</thead>
				<% int cnt = 2; String style ="odd"; %>
				<c1:forEach var="promo" items="${promotions}">       
					<tr class="<%= style %>" >
						<td><c1:out value="${promo.description}" /></td>
						<td><c1:out value="${promo.promoDescription}" /></td>
						<td><c1:out value="${promo.shareHeader}" /></td>
						<td><c1:out value="${promo.shareText}" /></td>
						<td><c1:out value="${promo.getHeader}" /></td>
						<td><c1:out value="${promo.getText}" /></td>
						<td><c1:out value="${promo.giveHeader}" /></td>
						<td><c1:out value="${promo.giveText}" /></td>
						<td><c1:out value="${promo.referralFee}" /></td>
						<td><c1:out value="${promo.expirationDate}" /></td>						
						<td><c1:if test="${!promo.defaultPromo}" >N</c1:if><c1:if test="${promo.defaultPromo}" >Y</c1:if></td>
						
						<td><c1:if test="${!promo.defaultPromo}" ><input type="checkbox" name='delete_<c1:out value="${promo.referralId}" />' value='<c1:out value="${promo.referralId}" />' /></c1:if>
						<c1:if test="${promo.defaultPromo}" >&nbsp;&nbsp;-</c1:if>
						</td>						
						<td><a href="editRefPromo.do?ref_id=<c1:out value="${promo.referralId}" />">View/Modify</a></td>
					</tr>
					<% if (cnt % 2 == 0) {
						style="even";
					} else {
						style = "odd";
					}
					cnt++;
					%>
				</c1:forEach>
			</table>
			<br/>
			<div style="float:right;padding:15px;">
			<input type="button" name="Create Promotion" value="Create Promotion" onclick="location.href='createRefPromo.do'"/>&nbsp;&nbsp;&nbsp;
			<input type="submit" name="Save Changes" value="Save Changes" />
			</div>
		</div>
	</div>
	</form>
		 
</tmpl:put>
</tmpl:insert>

