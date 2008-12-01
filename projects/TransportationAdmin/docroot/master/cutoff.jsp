<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation CutOffs</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form id="cutOffForm" action="" method="post">  
        <ec:table items="cutoffs"   action="${pageContext.request.contextPath}/cutoff.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation CutOffs"
            width="98%"  view="fd" form="cutOffForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationcutoffs.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation CutOff" />
              <ec:exportXls fileName="transportationcutoffs.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationcutoffs.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="cutOffId" />             
              <ec:column property="name" title="Name"/>
              <ec:column property="description" title="Description"/>
              <ec:column property="sequenceNo" title="Sequence No"/>              
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editcutoff.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
