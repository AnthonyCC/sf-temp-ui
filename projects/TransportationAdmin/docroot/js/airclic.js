
var escapeOverlay = {
	fx: function(e) 
	{
		// To make script compatable with both MSIE and Firefox
		var kC  = (window.event) ? event.keyCode : e.keyCode;
		var Esc = (window.event) ? 27 : e.DOM_VK_ESCAPE;
		
		// If keypressed is escape and the new entry field is empty
		if(kC==Esc && ($F('newvalue') == '' || $F('newvalue') == null) )
			closeDialogue();
		else if(kC==Esc && window.confirm('Are you sure you wish to close the dialogue box?') )
			closeDialogue();
	}
}

// Save in cache (to be able to stopObserving() it), see Prototype API docs for more info:
// http://www.prototypejs.org/api/event
escapeOverlay.bfx = escapeOverlay.fx.bindAsEventListener(escapeOverlay);

// loadPopup shows the overlay and dialogue box
function loadPopup()
{
	var table = document.getElementById('ec_table');
    var checkboxList = table.getElementsByTagName("input");
    var paramValues = 0, dispatched = false;
     var dateField = document.getElementById("dispDate").value;    
    
     for (var i=0; i < checkboxList.length; i++) {
       if (checkboxList[i].type=="checkbox" && checkboxList[i].checked&& !checkboxList[i].disabled&&checkboxList[i].name.indexOf("_")==-1) {
    	  var parent =  checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[3];
    	  var childelements = parent.getElementsByTagName("input")
    	  for (var j=0; j < childelements.length; j++) {
    		  if(childelements[j].type=="checkbox" && childelements[j].checked)
    			  dispatched = true;
    	  }
    	  var routeId = checkboxList[i].parentNode.parentNode.getElementsByTagName("td")[11].innerHTML;
         paramValues++;
       }
     }
    if(paramValues==0)
    {
 		alert('Please select a route!');
 		return false;
    }
 	else if(paramValues>1)
 	{
 		alert('Please select only one route');
 		return false;
 	}
 	else if(paramValues ==1 && !dispatched)
 	{
 		alert('You cannot send the message before route is dispatched.');
 		return false;
 	}	
     
    	// Show the overlay (disables rest of page)
	showOverlay();
	
	
	document.getElementById('ddate').value = dateField;
	
     document.getElementById('route').value = routeId;  
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



// Adds an entry
function addEntry(date, route, stop, message, msgSrc, userId, orderId)
{
	try
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
	 
	 result = jsonrpcClient.manager.sendMessage(_data);
	 $('error').update(result);
	}
	catch(e)
	{
		 $('error').update(e);
	}
	 

}
function populateText(message)
{
	$('messageDesc').update(message);
}
