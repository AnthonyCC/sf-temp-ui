
// loadPopup shows the overlay and dialogue box
function loadPopup()
{
	var table = document.getElementById('ec_table');
    var checkboxList = table.getElementsByTagName("input");
    var paramValues = 0, dispatched = false;
     var dateField = document.getElementById("dispDate").value;    
    var routes = null;
     for (var i=0; i < checkboxList.length; i++) {
       if (checkboxList[i].type=="checkbox" && checkboxList[i].checked&& !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
    	  var parent =  checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[3];
    	  var childelements = parent.getElementsByTagName("input");
    	  for (var j=0; j < childelements.length; j++) {
    		  if(childelements[j].type=="checkbox" && childelements[j].checked)
    		  { 
    			  if(routes!=null) 
    				  routes=routes+","+checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[12].innerHTML;
    			  else
    				  routes = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[12].innerHTML;
    		  dispatched = true;
    		  }
    	  }
    	  paramValues++;
       }
     }
    if(paramValues==0)
    {
 		alert('Please select a route!');
 		return false;
    }
 	else if(paramValues>0 && !dispatched)
 	{
 		alert('You cannot send the message before route is dispatched.');
 		return false;	
 	}
 	else if(paramValues>1 && dispatched)
 	{
 		try
 		{
 		var accesscode = prompt("You are about to send mass message to all the selected routes. Do you want to continue?","<Please enter access code>");
 		 if (accesscode != null && accesscode != "")  {
 			 var dispatchClient = new JSONRpcClient("dispatchprovider.ax");
 			var result = dispatchClient.AsyncDispatchProvider.validateAccessCode(accesscode);
 			
 			if(!result)
 				return false;
 		 }
 		}
 		catch(rpcException) {
				alert("There was a problem in communication to the server. Please try to refresh the browser window!\n"+e);
			}
 	    
 	}
    	// Show the overlay (disables rest of page)
	showOverlay();
	$('messageDesc').value="";
	$('ac_info').update("");
	$('ac_error').update("");
	document.getElementById('ddate').value = dateField;
	
     document.getElementById('route').value = routes;  
	//$('route').update(route);
	// Show dialogue and focus on newvalue
	$('dialogue').show();
}
 
// Shows the overlay and starts the ESCAPE event listener
function showOverlay()
{
	$('overlay').show();
}

// Hides the overlay and stops the ESCAPE event listener
function hideOverlay()
{
	$('overlay').hide();
}

// Closes the dialogue box, resets it and hides the overlay
function closeDialogue()
{
	hideOverlay();
	
	// Hide dialogue
	$('dialogue').hide();
}

function addEntry(date, route, stop, message, msgSrc, userId, orderId,customerId)
{
	try
	{
		if(message=="")
		{
		 	$('ac_info').update("");
			$('ac_error').update("Message is empty. Please select a message");
		}
		else
		{
			var jsonrpcClient = new JSONRpcClient("api/message.jsp");

			 var _data = new Array();
			 _data[0] = date;
			 _data[1] = route;
			 _data[2] = stop;
			 _data[3] = message;
			 _data[4] = msgSrc;
			 _data[5] = userId;
			 _data[6] = orderId;
			 _data[7] = customerId;
			 var result=jsonrpcClient.manager.sendMessage(_data,null);
			 $('ac_error').update("");
			 $('ac_info').update(result);
			 closeDialogue();
		}
		
	}
	catch(e)
	{
		$('ac_info').update("");
		$('ac_error').update("There was an error. Try again later");
	}
	 

}


function populateText(message)
{
	$('messageDesc').update(message);
}
