<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>/ View Labels For Dates  /</title>
	<link rel="stylesheet" href="css/transportation.css" type="text/css" />		
	<link rel="stylesheet" href="css/extremecomponents.css" type="text/css" />
	<link rel="stylesheet" href="css/jscalendar-1.0/calendar-system.css" type="text/css" />
	<script src="js/jsonrpc.js" language="javascript" type="text/javascript"></script>
    <script src="js/json2.js" language="javascript" type="text/javascript"></script>   
</head>

 <body marginwidth="0" marginheight="0" border="0" style="background-color:#D7C8FF">
		
		<div align="center">      
      		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="forms1">
		     	  <tr>
			          <td colspan="2" class="screentitle"><br/>View Scrib Dates For Labels<br/><br/></td>			          
			      </tr><tr></tr>
		      	 <tr>		      	 		  
		                  <td align="right">Scrib Label:</td>
		                  <td>
		                  	<select id="slabel" name="slabel" onchange="javascript:handleLabelChangeEvt();">
		                  		<option value="<c:out value=""/>">--Please Select</option>       
							 	<c:forEach var="slabel" items="${sLabels}">
							 		<option value="<c:out value="${slabel}"/>"><c:out value="${slabel}"/></option>
			        			</c:forEach>		        						
							</select>
							<script>
								 var resultDatesScriblabel;
								 function handleLabelChangeEvt() {
			      	  					var jsonrpcScribLabelClient = new JSONRpcClient("dispatchprovider.ax");
			          					resultDatesScriblabel = jsonrpcScribLabelClient.AsyncDispatchProvider.getDatesByScribLabel
			         												(document.getElementById("slabel").value);		         					
			         					var x = document.getElementById('datestable');
			         					strX = '';
			         					strX += '<table>';
			         					strX += '<hr size="1" width="350">';
			         					strX += '<tr><td></td></tr><tr><td>Scrib Dates For Label ## '+ document.getElementById("slabel").value +'</td></tr><tr><td></td></tr>';
										
										var list = resultDatesScriblabel.list;
										for (var i = 0; i< list.length; i++) {
			         						strX += '<tr><td><a href="#" style="font-weight:bold;" onclick="javascript:showSummary(\''+list[i]+'\'); return false;">'+list[i]+'</a></td></tr>';				         				
			         					}
			         					strX += '</table>';
			         					x.innerHTML = strX;
			         					document.getElementById('summarytable').innerHTML = '';
				  				  }
			        		</script>  			                  	
                    	 </td>
		         </tr>		          
		    </table>   
		</div>
		<div id="summarytable" class="view"> 

		</div>
		
		<style>
			#datestable.dates{
				height:250px;
				margin-left:15px;
				overflow-y:auto;				
				align:left;
				padding-top:15px;
			}
			#summarytable.view{
				margin-left:15px;
				background-color:#F7F7F7;
				width:150px;
				align:left;
				margin-top:15px;				
			}
		</style>	
		<div id="datestable" class="dates"> 
      		
		</div>

		<script>
			var resultTotalTrucks;
			function showSummary(date){

				var jsonrpcScribLabelClient = new JSONRpcClient("dispatchprovider.ax");
				resultTotalTrucks = jsonrpcScribLabelClient.AsyncDispatchProvider.getTotalNoTrucksByDate(date);		         					
				var x = document.getElementById('summarytable');
				strX = '';
				strX += '<table>';
				strX += '<tr><td colspan="2" style="font-weight:bold;">Scrib Summary</td></tr>';
				strX += '<tr></tr><tr><td>Scrib Date: </td><td>'+date+'</td></tr><tr><td>No. of Trucks # </td><td>'+ resultTotalTrucks  +'</td></tr><tr><td>Scrib Label:</td><td>'+ document.getElementById("slabel").value +'</td></tr>';
				strX += '</table>';
				x.innerHTML = strX;
		
			}
		</script>

		
</body>
</html>

