<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>

  <tmpl:insert template='/common/template/depot_discontinued.jsp'>

    <tmpl:put name='splash' direct='true'>
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10" border="0"><br>
		<img src="/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"><br>
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="20" border="0"><br>
		<img src="/media_stat/images/template/depot/splash_logo_cmp.gif" width="109" height="93" alt="CMP" border="0">
    </tmpl:put>

    <tmpl:put name='depot_discontinued' direct='true'>
		<%@ include file="/common/template/includes/depot_discontinued_recent.jspf" %>
	</tmpl:put>

  </tmpl:insert>

