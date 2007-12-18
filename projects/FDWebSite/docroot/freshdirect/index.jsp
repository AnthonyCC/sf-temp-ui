<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<fd:DepotLoginController actionName='checkByDepotCode' depotCode='freshdirect' successPage='/index.jsp' result='result'>

  <tmpl:insert template='/common/template/depot_site_access.jsp'>

    <tmpl:put name='splash' direct='true'>
		<img src="/media_stat/images/template/depot/splash_logo_fd.gif" width="332" height="108" alt="FreshDirect Employee Depot" border="0">
    </tmpl:put>

    <tmpl:put name='blurb' direct='true'>
		<img src="/media_stat/images/template/depot/noboy_delivers_fd.gif" width="400" height="25" border="0">
    </tmpl:put>

	<tmpl:put name='error' direct='true'>
		<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
		<fd:ErrorHandler result='<%=result%>' name='depotAccessCode' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
	</tmpl:put>

  </tmpl:insert>

</fd:DepotLoginController>
