var selectBox;
function getActiveZoneInfo(date,s)
{
     if(date!='')
     {
    	selectBox=s;
    	var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");    	        	
    	jsonrpcClient.AsyncDispatchProvider.getActiveZones(getActiveZoneInfoCallback,date);
     }
}
   
   
function getActiveZoneInfoCallback(result, exception) 
{
  	  
      if(exception) 
      {               
        //  alert('Unable to connect to host system. Please contact system administrator!');               
          return;
      }
 
  var selectedZone=selectBox.value;	  
  for(var i=selectBox.options.length-1;i>=1;i--)
  {
	  selectBox.remove(i);
  }		 
  var selected=false;
  var results=result.list;
  for(var i=0;i<results.length;i++)
  {			
	  	var optn = document.createElement("OPTION");
	  	optn.text = results[i].name;
      	optn.value = results[i].zoneCode;
      	if(optn.value==selectedZone)
      	{
      		optn.selected=true;	          		
      	}	          	
      	selectBox.options.add(optn);	        
  }                               
}   