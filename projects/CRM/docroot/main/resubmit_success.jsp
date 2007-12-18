<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<%@ page import="com.freshdirect.fdstore.*" %>

<tmpl:insert template="/template/top_nav.jsp">
<tmpl:put name='title' direct='true'>Customer Resubmitted</tmpl:put>
<tmpl:put name='content' direct='true'>
<div class="content_fixed" align="center"><br>
<span class="sub_nav_title">Customer Resubmitted</span>
<br>
Please note: It may take up to one day for the change to take place.<br><a href="/main/account_details.jsp">Back to account details</a>
<br><br>
</div>
</tmpl:put>
</tmpl:insert>