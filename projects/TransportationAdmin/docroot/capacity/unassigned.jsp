<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<% 
	pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
	pageContext.setAttribute("HAS_DELETEBUTTON", "false"); 
   
   String dateRangeVal = request.getParameter("rDate") != null ? request.getParameter("rDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getCurrentDate();
  %>
  
  <link rel="stylesheet" href="css/transportation.css" type="text/css" />		
<tmpl:insert template='/common/sitelayout.jsp'>

    <tmpl:put name='title' direct='true'>Unassigned Management</tmpl:put>
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>	
	<tmpl:put name='yui-skin'>yui-skin-sam</tmpl:put>
	
  <tmpl:put name='content' direct='true'>
    <br/> 
    <div class="contentroot">               
      <table width="100%" cellpadding="0" cellspacing="0" border="0">
          <c:if test="${not empty messages}">
          <tr>
            <td class="screenmessages"><jsp:include page='/common/messages.jsp'/></td>
          </tr>
          </c:if>         
          <tr>
            <td>

              <table border = "0">
                <tr>
                <td> 
                    <span style="font-size: 18px;font-weight: bold;">Unassigned View</span>
                </td>                
                  <td> 
                    <span><input maxlength="10" size="10" name="rDate" id="rDate" value='<c:out value="${rDate}"/>' /></span>
                     <span><a href="#" id="trigger_dispatchDate" style="font-size: 9px;">
                        <img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date">
                    </a></span>
                     <script language="javascript">                 
                      Calendar.setup(
                      {
                        showsTime : false,
                        electric : false,
                        inputField : "rDate",
                        ifFormat : "%m/%d/%Y",
                        singleClick: true,
                        button : "trigger_dispatchDate" 
                       }
                      );
                      
                               
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="rDate" />
                </td>
                 <td>
                     <input type = "button" value="&nbsp;View UnAssigned&nbsp;" onclick="javascript:doCompositeLink('rDate','unassigned.do')" />
                  </td> 
                  
                  
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
       <script>
         function doCompositeLink(compId1, url) {
          var param1 = document.getElementById(compId1).value;
          
          location.href = url+"?"+compId1+"="+ param1;
        } 
       
      
      </script>      
      </div>
    
    <div align="center">
      <ec:table items="unassigneds"   action="${pageContext.request.contextPath}/unassigned.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="unassigned.pdf" tooltip="Export PDF" 
                      headerTitle="Unassigned" />
              <ec:exportXls fileName="unassigned.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="unassigned.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row>                            	
			  <ec:column property="reservationId" title="ID"/>
			  <ec:column property="orderId" title="Order ID"/>
			  <ec:column property="customerId" title="Customer ID"/>
			  <ec:column property="zone" title="Zone"/>
			  <ec:column property="unassignedAction" title="Event Type"/>
			 			  
			  <ec:column property="timeWindow" title="Time Window"/>			                                   
			  <ec:column  property="unassignedTime" title="Unassigned Time"  cell="date" format="MM/dd/yyyy HH:MM"/>
			  <ec:column  property="createModTime" title="Order Create/Mod Time"  cell="date" format="MM/dd/yyyy HH:MM"/>				              
            </ec:row>
          </ec:table>
    </div>
  </tmpl:put>
  
  
</tmpl:insert>
