		
		var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");
		
		var resultList;
		var subTypeId;
		function getIssueSubTypes() {  
			var issueTypes = document.getElementById("issueType");
   
			// get selected issueType from dropdown list  
			var issueTypeId = issueTypes.options[issueTypes.selectedIndex].value;
			if(issueTypeId.length != 0){
				 resultList = jsonrpcClient.AsyncDispatchProvider.getIssueSubTypes(sendFormCallback, issueTypeId);
			}
		}

		function sendFormCallback(resultList, exception) {
      	  if(exception) {
              alert('Unable to connect to host system. Please contact system administrator!');
              return;
          }
          if(resultList != null) {
			
			 var issueSubTypes = document.getElementById("issueSubType");
			 issueSubTypes.length = 0;
			 issueSubTypes.options.add(new Option('--Please select SubType',''));
			 for(var i=0;i < resultList.list.length;i++){
				issueSubTypes.options.add(new Option(resultList.list[i].name, resultList.list[i].name));
			 }
			 document.getElementById("issueSubType").value = subTypeId; 
		  }else {
          	 alert("Populating Issue SubTypes failed");
          }
		}
		
		function addNewVIRRecord(){
			var confirmed = confirm ("You are about add new VIR Record");
			if(confirmed){
				location.href = "editvirrecordlog.do";
			}
		}

		window.onload = function(){
			var issueTypes = document.getElementById("issueType");
			if(document.getElementById("subTypeId")){
				subTypeId = document.getElementById("subTypeId").value;
			}
			
			if(issueTypes){
				getIssueSubTypes();
			}
		}

		function doMaintenanceIssueLink(compId1,compId2, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;

          location.href = url+"?recordType=M&"+compId1+"="+ param1+"&"+compId2+"="+param2;
        }

		function doVIRRecordLink(compId1,compId2,compId3, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
		  var param3 = document.getElementById(compId3).value;

          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2+"&"+compId3+"="+param3;
        }

		function addNewVIRRecord(){
			var confirmed = confirm ("You are about add new VIR Record");
			if(confirmed){
				location.href = "editvirrecordlog.do";
			}
		}

		function rejectIssue(){
			var id = document.getElementById('issueId').value;
			if(id !== ''){
				var result = jsonrpcClient.AsyncDispatchProvider.getRejectMaintenanceIssue(sendRejectFormCallback, id);
			}
		}

		function sendRejectFormCallback(result, exception) {
      	  if(exception) {
              addSysMessage("Unable to connect to host system. Please contact system administrator!",true);
              return;
          }
          if(result != null) {
			 addSysMessage("Maintenance Issue rejected successfully",false);
			 window.location.href = window.location.href;
		  }else {
          	 addSysMessage("Maintenance Issue rejection failed",true);
          }
		}
		
		var errTxtColor = "#FF0000";
	    var msgTxtColor = "#0066CC";

		function addSysMessage(msg, isError) {
      		var errContObj = YAHOO.util.Dom.get("errContainer");
		    if(isError) {
		    	errContObj.style.color = errTxtColor;
	      	} else {
	      		errContObj.style.color = msgTxtColor;
	      	}
	      	errContObj.style.fontWeight="bold";
      		YAHOO.util.Dom.get("errContainer").innerHTML = msg;
      }
		
		