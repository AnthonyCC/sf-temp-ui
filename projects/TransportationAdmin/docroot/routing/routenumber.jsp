<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<%  pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
    pageContext.setAttribute("HAS_DELETEBUTTON", "false"); %>
    
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Route Numbers</tmpl:put>

  <tmpl:put name='content' direct='true'>
    <br/> 
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <tr>
            <td class="screentitle">Route Numbers</td>
          </tr>
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          </c:if>         
          <tr>
            <td class="screencontent">
              <table class="forms1">          
                <tr>
                  <td>Select Date</td>
                  <td> 
                                
                    <input maxlength="10" size="10" name="routeDate"
                      id="routeDate" value="<c:out value="${routeDate}"/>" />
                    
                    &nbsp;<a href="#" id="trigger_routeDate" style="font-size: 9px;">
                        <img src="images/calendar.gif"  style="border:0"  alt=">>" />
                        </a>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "routeDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_routeDate" 
                       }
                      );
                  </script>
                </td>
                          
                   <td colspan="3" align="center">
                     <input type = "button" value="&nbsp;Go&nbsp;" onclick="javascript:doLink('routeDate','routenumber.do')" />
                </td>     
                      
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
      
     </div>
    <div align="center">
      <ec:table items="routenumberlist"   action="${pageContext.request.contextPath}/routenumber.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="routenumber.pdf" tooltip="Export PDF" 
                       headerTitle="Route Numbers" />
              <ec:exportXls fileName="routenumber.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="routenumber.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row>                                   
              <ec:column property="routeNumberId.routeDate" title="Route Date"/>
          <ec:column property="routeNumberId.cutOffId" title="Cut Off"/>
              <ec:column property="routeNumberId.areaCode" title="Area"/>
              <ec:column property="currentVal" title="No of Routes"/>
            </ec:row>
          </ec:table>
    </div>  
  </tmpl:put>
</tmpl:insert>