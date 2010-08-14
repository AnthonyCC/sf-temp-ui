<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.*' %>
<%@ page import= 'com.freshdirect.transadmin.web.model.*' %>
<%@ page import= 'java.util.*' %>
<% 
	pageContext.setAttribute("HAS_ADDBUTTON", "false"); 
	pageContext.setAttribute("HAS_DELETEBUTTON", "false"); 
   
   String dateRangeVal = request.getParameter("rDate") != null ? request.getParameter("rDate") : "";
   if(dateRangeVal == null || dateRangeVal.length() == 0) dateRangeVal = TransStringUtil.getNextDate();
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
                <td><span style="font-size: 12px">Auto Refresh :</span><input type="checkbox" name="autorefresh" id="autorefresh" <%= ("true".equalsIgnoreCase(request.getParameter("autorefresh")) ? "checked=\"true\"" : "false") %>  /></td>
                 <td>
                     <input type = "button" value="&nbsp;View&nbsp;" onclick="javascript:doCompositeLink('rDate','unassigned.do', 'autorefresh')" />
                  </td> 
                  <td>
                     <input type = "button" value="&nbsp;Mass ReRoute&nbsp;" onclick="javascript:showReRouteForm()" />
                  </td>
              </tr>
              </table>        
              
            </td>
          </tr>
          <tr><td>&nbsp;</td></tr>
          
          <tr><td>
          	  <% 
          	  	int ucount = 0;
          	  	List summaryObj = (List)request.getAttribute("unassigneds");
          	  	Set zones = new HashSet();
          	  	Set zonesByWindow = new HashSet();
          	    UnassignedCommand _command = null; 
          	  	if(summaryObj != null) {
          	  		ucount = summaryObj.size(); 
          	  		Iterator _itr = summaryObj.iterator();
          	  		while(_itr.hasNext()) {
          	  			_command = (UnassignedCommand)_itr.next();
          	  			zones.add(_command.getZone());
          	  			zonesByWindow.add(_command.getZone()+"_"+_command.getTimeWindow());
          	  		}
          	  	}
          	  %>
	          <table style="font-size: 12pt;font-family: Verdana, Arial, Helvetica,sans-serif;background-color:#e7e7d6;">
	          
	          <tr><td>Unassigned Orders:</td><td><%= ucount %></td></tr>
	          <tr><td># of Zones Affected:</td><td><%= zones.size() %></td></tr>
	          <tr><td># of Zone and Time Windows Affected:</td><td><%= zonesByWindow.size() %></td></tr>      
	          </table> 
          </td></tr>               
        </table>    
       <script>
       	  
         function doCompositeLink(compId1, url, autorefresh) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(autorefresh).checked;
          
          location.href = url+"?"+compId1+"="+ param1+"&autorefresh="+param2;
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
                
            <ec:row interceptor="unassignedmarker">                            	
			  <ec:column property="reservationId" title="ID" width="5px"/>
			  <ec:column property="orderId" title="Order ID" width="5px"/>
			  <ec:column property="customerId" title="Customer ID" width="5px"/>
			  <ec:column property="zone" title="Zone" width="5px"/>
			  
			  <ec:column property="unassignedOrderSize" title="* SIZE" width="5px"/>
			  <ec:column property="unassignedServiceTime" title="* SERVICE TIME" width="5px"/>
			  <ec:column property="orderSize" title="SIZE" width="5px"/>
			  <ec:column property="serviceTime" title="SERVICE TIME" width="5px"/>
			  <ec:column property="updateStatus" title="STATUS" width="5px"/>
			  			  
			  <ec:column property="unassignedAction" title="Unassigned By"/>
			 			  
			  <ec:column property="timeWindow" title="Time Window"/>
			  <ec:column property="isForced" title="FORCED" width="5px"/>
			  <ec:column property="isChefsTable" title="CT" width="5px"/>			                                   
			  <ec:column  property="formattedUnassignedTime" title="Unassigned Time" />
			  <ec:column  property="formattedCreateModTime" title="Create/Mod Time" />				              
            </ec:row>
          </ec:table>
           <%@ include file='i_unassignededitor.jspf'%>
           <%@ include file='i_massrerouting.jspf'%>
    </div>
   
    <script>
    <% if("true".equalsIgnoreCase(request.getParameter("autorefresh"))) { %>
		doRefresh(<%= TransportationAdminProperties.getCapacityRefreshTime() %>);

			
	<% } else { %>
	function addAsyncHandler(tableId, rowClassName, columnIndex) {
		
		var previousClass = null;
	    var table = document.getElementById(tableId);
	    
	    if(table != null) {
		    var rows = table.tBodies[0].getElementsByTagName("tr");	 	       
		    for (i = 0; i < rows.length; i++) {	    	
		        var cells = rows[i].getElementsByTagName("td");
		        
		        for (j = 0; j < cells.length; j++) {
		        	
		            cells[j].onmouseover = function () {
		            	previousClass = this.parentNode.className;
		            	this.parentNode.className = this.parentNode.className + " " + rowClassName ;
		            };
		        
		            cells[j].onmouseout = function () {
		              	this.parentNode.className = previousClass;
		            };
		        
		           cells[j].onclick = function () {			    		
					      		var cell = this.parentNode.getElementsByTagName("td")[columnIndex];
					      		if("CANCEL_TIMESLOT" != this.parentNode.getElementsByTagName("td")[7].innerHTML) {							      		
					      			populateOrder(cell.innerHTML);
					      		}				      		
					      			      		
				   };
			    		    	
		        }
		    }
		}
	 }

	 addAsyncHandler('ec_table', 'rowMouseOver', 0);
	<% } %>	
    </script>
  </tmpl:put>
  
  
</tmpl:insert>
