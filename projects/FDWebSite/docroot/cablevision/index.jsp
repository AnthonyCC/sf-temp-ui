<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<fd:DepotLoginController actionName='checkByDepotCode' depotCode='cablevision' successPage='/index.jsp' result='result'>

  <tmpl:insert template='/common/template/depot_discontinued.jsp'>

    <tmpl:put name='splash' direct='true'>
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="10" border="0"><br>
		<img src="/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"><br>
		<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="20" border="0"><br>
		<img src="/media_stat/images/logos/cablevision/cvcs.gif" width="156" height="57" alt="CableVision" border="0">
    </tmpl:put>

    <tmpl:put name='depot_discontinued' direct='true'>
		<%@ include file="/common/template/includes/depot_discontinued.jspf" %>
	</tmpl:put>

  </tmpl:insert>

</fd:DepotLoginController>
