<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Service Time Type</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form id="dlvServiceTimeTypeForm" action="" method="post">  
        <ec:table items="dlvservicetimetypelist"   action="${pageContext.request.contextPath}/dlvservicetimetype.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Service Time Type"
            width="98%"  view="fd" form="dlvServiceTimeTypeForm" autoIncludeParameters="false" rowsDisplayed="25">
            
            <ec:exportPdf fileName="transportationdlvservicetimetypes.pdf" tooltip="Export PDF" 
                      headerTitle="Service Time Type" />
              <ec:exportXls fileName="transportationdlvservicetimetypes.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationdlvservicetimetypes.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="code" alias="keycode" />              
              <ec:column property="code" title="Code"/>
              <ec:column property="name" title="Name"/>
              <ec:column property="description" title="Description"/>             
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editdlvservicetimetype.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
