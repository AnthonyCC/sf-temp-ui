<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Zone Types</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form id="zoneTypeListForm" action="" method="post">  
        <ec:table items="zonetypes"   action="${pageContext.request.contextPath}/zonetype.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Zone Types"
            width="98%"  view="fd" form="zoneTypeListForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationzonetypes.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Zone Type" />
              <ec:exportXls fileName="transportationzonetypes.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationzonetypes.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="zoneTypeId" />              
              <ec:column property="name" title="Zone Type Name"/>
              <ec:column property="description" title="Description"/>             
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editzonetype.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
