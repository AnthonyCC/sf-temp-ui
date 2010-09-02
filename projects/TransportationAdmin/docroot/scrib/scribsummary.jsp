

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="/tld/extremecomponents" prefix="ec" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>

<%  
	pageContext.setAttribute("HAS_COPYBUTTON", "false");
%>
<% 
	String pageTitle = "Scrib Summary View";
%>

<tmpl:insert template='/common/sitelayout.jsp'>

  <tmpl:put name='title' direct='true'> Operations : <%=pageTitle%></tmpl:put>

  <tmpl:put name='content' direct='true'> 

	<c:if test="${not empty messages}">
		<div class="err_messages">
			<jsp:include page='/common/messages.jsp'/>
		</div>
	</c:if> 

  <div class="contentroot">

		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_<%=pageTitle%>">						
						<span class="scrTitle"><%=pageTitle%></span>
						<span style="font-weight:bold">
							Selected Date:<input maxlength="40" name="selectedDate" id="selectedDate" value='<c:out value="${selectedDate}"/>' style="width:90px"/>
						 	<a href="#" id="trigger_selectedDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</span>
						<span style="font-weight:bold">
							&nbsp;Base Date:<input maxlength="40" name="baseDate" id="basedate" value='<c:out value="${baseDate}"/>' style="width:90px"/>
						 	<a href="#" id="trigger_baseDate" style="font-size: 9px;">
                        			<img src="./images/icons/calendar.gif" width="16" height="16" border="0" alt="Select Date" title="Select Date"></a>
						</span>						
						<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:doCompositeLink('selectedDate','basedate','scribsummaryview.do');" onmousedown="this.src='./images/icons/view_ON.gif'" /><br>
						</span>  
				</div>
			</div>
		</div>
 </div>
 
 		
     <script>

      
      function doCompositeLink(compId1,compId2,url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
             
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2;
      } 

      addRowHandlersFilter('ec_table', 'rowMouseOver', 'editplan.do','id',0, 0);
      
      function doDelete(tableId, url) 
      {    
		    var paramValues = getParamList(tableId, url);
		    if (paramValues != null) {
		    	var hasConfirmed = confirm ("Do you want to delete the selected records?")
				if (hasConfirmed) 
				{
					var filter="&daterange="+document.getElementById("daterange").value+"&"+getFilterValue(document.getElementById("planListForm"),false)
				  	location.href = url+"?id="+ paramValues+filter;
				} 
		    } else {
		    	alert('Please Select a Row!');
		    }
		}
		
	  Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "selectedDate",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_selectedDate" 
                }
               );
	   Calendar.setup(
               {
                 showsTime : false,
                 electric : false,
                 inputField : "baseDate",
                 ifFormat : "%m/%d/%Y",
                 singleClick: true,
                 button : "trigger_baseDate" 
                }
               );

    </script>   
  </tmpl:put>
</tmpl:insert>
