<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>

<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Transportation Service Time Scenario's</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div align="center">
      <form id="dlvServiceTimeScenarioForm" action="" method="post">  
        <ec:table items="dlvservicetimescenariolist"   action="${pageContext.request.contextPath}/dlvservicetimescenario.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="Transportation Service Time Scenarios"
            width="98%"  view="fd" form="dlvServiceTimeScenarioForm" autoIncludeParameters="false" rowsDisplayed="25"  >
            
            <ec:exportPdf fileName="transportationservicetimescenarios.pdf" tooltip="Export PDF" 
                      headerTitle="Transportation Service Time Scenarios" />
              <ec:exportXls fileName="transportationservicetimescenarios.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="transportationservicetimescenarios.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row interceptor="obsoletemarker">
              <ec:column title=" " width="5px" 
                    filterable="false" sortable="false" cell="selectcol"
                    property="code" />  
          <ec:column width="5px" alias="scenariocode" property="code" title="Code"/>    
          <ec:column property="description" title="Description"/>
          <ec:column width="5px" property="isDefault" title="Is Default"/>
          <ec:column property="serviceTimeFactorFormula" title="Service Time Factor Formula"/>
          <ec:column property="serviceTimeFormula" title="Service Time Formula"/>
          <ec:column property="defaultCartonCount" width="5px" title="Carton Count"/>
          <ec:column property="defaultCaseCount" width="5px" title="Case Count"/>
          <ec:column property="defaultFreezerCount" width="5px" title="Freezer Count"/>
          <ec:column property="orderSizeFormula" title="Order Size Formula"/>
          
          <ec:column alias="defaultServiceTimeType" width="5px" property="defaultServiceTimeType.code" title="Service Time Type"/>
          <ec:column alias="defaultZoneType" property="defaultZoneType.name" title="Zone Type"/>  
          
          <ec:column property="needsLoadBalance" width="5px" title="Load Balance"/> 
          <ec:column property="loadBalanceFactor" width="5px" title="Balance Factor"/>
          <ec:column format="com.freshdirect.routing.constants.EnumBalanceBy" cell="enumcol" property="balanceBy" title="Balance By"/>                         
          <ec:column property="lateDeliveryFactor" width="10px" title="Late Delivery Factor"/> 
            </ec:row>
          </ec:table>
       </form>  
     </div>
     <script>
      addRowHandlers('ec_table', 'rowMouseOver', 'editdlvservicetimescenario.do','id',0, 0);
    </script>   
  </tmpl:put>
</tmpl:insert>
