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

    <tmpl:put name='title' direct='true'>TimeSlot Log</tmpl:put>
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
                    <span style="font-size: 18px;font-weight: bold;">TimeSlot Log View</span>
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
                      
                      function doTimeSlotLog(compId1,compId2, compId3) {
			        	 var param1 = document.getElementById(compId1).value;
			        	 var param2 = document.getElementById(compId2).value;
			        	 var param3 = document.getElementById(compId3).value;
			        	 if(param1.trim().length ==0 || 
			        	 	param2.trim().length ==0 || 
			        	 		param3.trim().length ==0 ) {
			        	 		alert("Please select required values");
			        	 } else {
			        	 	showForm(param1, param2, param3);
			        	 }
        			  }           
                  </script>
                </td>
                <td>
                  &nbsp;<form:errors path="rDate" />
                </td>                  
                 
                  <td><span style="font-size: 12px;font-weight: plain;">Start Time:</span><input type="text" id="sTime" name="sTime" value="<c:out value="${sTime}"/>" style="width: 95px;" onblur="this.value=time(this.value);"></td> 
                   <td><span style="font-size: 12px;font-weight: plain;">End Time:</span><input type="text" id="eTime" name="eTime" value="<c:out value="${eTime}"/>" style="width: 88px;" onblur="this.value=time(this.value);"></td>
                   
                   <td>
                     <input type = "button" value="&nbsp;View Log&nbsp;" onclick="javascript:doCompositeLink('timeslotlog.do')" />
                  </td>
              </tr>
              </table>        
              
            </td>
          </tr>               
        </table>    
       <script>
         function doCompositeLink(url) {
          var param1 = document.getElementById('rDate').value;
          var param2 = document.getElementById('sTime').value;
          var param3 = document.getElementById('eTime').value;
          
          if(param1.trim().length ==0 || 
	        	 	param2.trim().length ==0 || 
	        	 		param3.trim().length ==0 ) {
	        	 		alert("Please select required values");
	       } else {
          		location.href = url+"?"+"rDate="+ param1+"&sTime="+ param2+"&eTime="+ param3;
	       }
        } 
       
      
      </script>      
      </div>
    
    <div align="center">
      <ec:table items="timeslotlogs"   action="${pageContext.request.contextPath}/timeslotlog.do"
            imagePath="${pageContext.request.contextPath}/images/table/*.gif"   title="&nbsp;"
            width="98%"  rowsDisplayed="25" view="fd" >
            
            <ec:exportPdf fileName="timeslotlog.pdf" tooltip="Export PDF" 
                      headerTitle="Timeslot Log" />
              <ec:exportXls fileName="timeslotlog.xls" tooltip="Export PDF" />
              <ec:exportCsv fileName="timeslotlog.csv" tooltip="Export CSV" delimiter="|"/>
                
            <ec:row>               
              <ec:column width="5px" property="reservationId" title="RSV ID"/>
			  <ec:column property="orderId" title="Order ID"/>
			  <ec:column property="customerId" title="Customer ID"/>
			  <ec:column width="15px" property="eventtype" title="Event Type"/>
			  <ec:column width="5px"  property="eventTime" title="Event Time" />
			  <ec:column width="5px"  property="responseTime" title="Response Time"/>
			  <ec:column width="25px" property="comments" title="Comments"/>					  
			  <ec:column cell="timeslotDtlCell" property="timeslotLogDtls" title="Event Details"/>			 		              
            </ec:row>
          </ec:table>
    </div>
  </tmpl:put>
  
  
</tmpl:insert>
