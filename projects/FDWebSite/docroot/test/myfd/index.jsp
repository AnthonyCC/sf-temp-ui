<%@page import="com.freshdirect.fdstore.myfd.blog.MyFdFeed"%>
<%@page import="com.freshdirect.fdstore.myfd.blog.MyFdPost"%>


<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<fd:CheckLoginStatus/>
<tmpl:insert template='/common/template/no_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - MyFD</tmpl:put>
<tmpl:put name='content' direct='true'>

<%@ include file="/includes/myfd/i_myfd_blogs.jspf" %>		

</tmpl:put>
</tmpl:insert>