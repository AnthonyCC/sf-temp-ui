<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<%    
  pageContext.setAttribute("HAS_ADDBUTTON", "false");
  pageContext.setAttribute("HAS_DELETEBUTTON", "false");
%>
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Zones</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form id="zoneListForm" action="" method="post">  
        <ec:table items="zones"   action="${pageContext.request.contextPath}/zone.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Zones"
            width="98%"  view="fd" form="zoneListForm" autoIncludeParameters="false" rowsDisplayed="25" >           
            <ec:exportPdf fileName="transportationzones.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Zones" />
              <ec:exportXls fileName="transportationzones.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationzones.csv" tooltip="Export CSV" delimiter="|"/>               
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="zoneCode" />              
              <ec:column property="name" title="Zone Name"/>
              <ec:column alias="trnZoneType" property="trnZoneType.name" title="Zone Type"/>
              <ec:column alias="area" property="area.name" title="Area"/>
              <ec:column alias="region" property="region.name" title="Region"/>
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editzone.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
