<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.cms.ContentNodeI" %>
<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/faq_help.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - Help - FAQs</tmpl:put>
	<tmpl:put name='leftnav' direct='true'>	</tmpl:put>	
	<tmpl:put name='content' direct='true'>
		
	<form method="post" name="contact_fd">
	<table border="0" cellpadding="0" cellspacing="0" width="570">
		<tr>
		<td valign="top">
		<img src="/media_stat/images/template/help/faq_hdr.gif"  height="21" alt="" border="0"><br>
		</td>
		<td>
		<input type="text" class="search" value="" maxlength="100" style="width: 100px;" name="searchFAQ2" />
		<input type="image" name="searchFAQButton" style="border: 0pt none ; padding: 3px; width: 35px; height: 14px;" src="/media_stat/images/template/search/search_find_button.gif"/>
		</td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2"><img src="/media_stat/images/layout/999966.gif" height="1" border="0" width="100%" VSPACE="3"><br></td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		</table>
		<table border="0" cellpadding="0" cellspacing="0" width="570">
		<fd:SearchFaq id="result">
		<tr><td colspan="2"><b>Search Results for '<%= pageContext.getAttribute("keywords") %>'</b></td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<logic:iterate id="faq" collection="<%= result%>" type="com.freshdirect.fdstore.content.Faq">
		<tr><td colspan="2"><a href="/help/faq_home.jsp?page=<%= (String)faq.getParentNode().getContentKey().getId()%>"><%= ((String)faq.getParentNode().getCmsAttributeValue("name")).toUpperCase() %></a> > <b> <%=faq.getQuestion() %></b></td></tr>
		<tr><td colspan="2"><br/><%=faq.getAnswer() %></td></tr><br/>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		</logic:iterate>
		
		</fd:SearchFaq>
		</table>
		</form>
		
	</tmpl:put>
	
	</tmpl:insert>