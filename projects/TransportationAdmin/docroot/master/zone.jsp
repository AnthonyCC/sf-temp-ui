<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Zones</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form id="zoneListForm" action="" method="post">  
        <ec:table items="zones"   action="${pageContext.request.contextPath}/zone.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Zones"
            width="98%"  view="fd" form="zoneListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationzones.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Zones" />
              <ec:exportXls fileName="transportationzones.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationzones.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="zoneId" />              
              <ec:column property="zoneNumber" title="Zone Number"/>
              <ec:column property="neighborhood" title="Neighborhood"/>
              <ec:column alias="trnSupervisorname" property="trnSupervisor.name" title="Supervisor"/>
              <ec:column alias="trnZoneType" property="trnZoneType.name" title="Zone Type"/>
              <ec:column alias="trnArea" property="trnArea.name" title="Area"/>
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editzone.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
