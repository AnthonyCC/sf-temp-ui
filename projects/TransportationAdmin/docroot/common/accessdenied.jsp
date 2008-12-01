<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='java.io.*'%>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Access Denied</tmpl:put>

  <tmpl:put name='content' direct='true'>
  <div class="accessdenied" onclick="location.href='.'"></div>
  </tmpl:put>
</tmpl:insert>