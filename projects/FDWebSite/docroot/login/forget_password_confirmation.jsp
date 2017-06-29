<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_FORGET_PASSWORD_CONFIRMATION_TOTAL = 700;
%>

<%String previousPage;%>
<fd:CheckLoginStatus id="user" /> 
<tmpl:insert template='/common/template/no_space_border.jsp'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - ID Confirmed - Security Word Confirmed"/>
    </tmpl:put>
    <tmpl:put name='title' direct='true'>FreshDirect - ID Confirmed - Security Word Confirmed</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<%@ include file="/login/includes/forget_password_confirmation.jspf" %>
	</tmpl:put>
</tmpl:insert>