<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ taglib uri='template' prefix='tmpl' %>

<%	String custId = null;
	FDIdentity identity = null;
	try {
		FDSessionUser _user = (FDSessionUser) session.getAttribute(SessionName.USER);
		identity = _user.getIdentity();
		custId = identity.getFDCustomerPK();
	} catch (NullPointerException ex) {
		System.out.println("\n caught exception trying to get USER from session...\n");
	}
	FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(custId);
%>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>New Customer > Registration Warning</tmpl:put>

<tmpl:put name='content' direct='true'>
<br><br>
<table width="70%" cellpadding="0" cellspacing="0" align="center" class="register">
	<tr>
		<td>
<%	if ( !fdCustomer.isEligibleForSignupPromo() ) { %>
			Registration successful, but this customer will not be eligible for the signup promotion.<br>
			<br>
			Certain information entered at registration matches current or historical information in our database.<br>
			Please contact a supervisor if you would like to make this customer eligible for the promotion.<br>
<%	} %>
			<br>
			<a href="/main/account_details.jsp">Click here</a> to proceed to the <b>Customer Details page</b>.
		</td>
	</tr>
</table>
<br><br><br>
</tmpl:put>

</tmpl:insert>
