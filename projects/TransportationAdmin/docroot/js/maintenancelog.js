		
		var jsonrpcClient = new JSONRpcClient("dispatchprovider.ax");
		
		var resultList;
		var subTypeId;
		var vendorResult;
		
		function getIssueSubTypes() {
			addSysMessage("",false);
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
			 if(resultList.list.length === 0){
				  addSysMessage("No IssueSubTypes to IssueType selected",true);
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

		function doMaintenanceIssueLink(compId1,compId2, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;

          location.href = url+"?issueLog=M&"+compId1+"="+ param1+"&"+compId2+"="+param2;
        }

		function doVIRRecordLink(compId1,compId2, url) {
          var param1 = document.getElementById(compId1).value;
          var param2 = document.getElementById(compId2).value;
		 
          location.href = url+"?"+compId1+"="+ param1+"&"+compId2+"="+param2;
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
				var result = jsonrpcClient.AsyncDispatchProvider.doRejectMaintenanceIssue(sendRejectFormCallback, id);
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

		function getVendorInfo() {
			addSysMessage("", false);
			document.getElementById("vendor").value = ''; 
			var truckNumber = document.getElementById("truckNumber");
			// get selected truckNumber from dropdown list  
			var truckNumber = truckNumber.options[truckNumber.selectedIndex].value;
			if(truckNumber.length != 0){
				 vendorResult = jsonrpcClient.AsyncDispatchProvider.getTruckVendorInfo(sendVendorFormCallback, truckNumber);
			}
		}

		function sendVendorFormCallback(vendorResult, exception) {
      	  if(exception) {
              alert('Unable to connect to host system. Please contact system administrator!');
              return;
          }
          if(vendorResult === '') {
			  addSysMessage("Vendor Information not mapped to TRUCK selected",true);
		  }else if(vendorResult != null && vendorResult !== '') {
			 document.getElementById("vendor").value = vendorResult; 
		  }else {
          	  addSysMessage("Populating vendor info failed for TRUCK selected",true);
          }
		}
		
		function doBack() {
	      	var filters = unescape(getParameter("filter"));
	      	var params = filters.split("&");
	      	var maintenanceRecordForm = document.forms["maintenancelog"];
	      	for(var i=0;i < params.length;i++)
	      	{
	      		var param = params[i].split("=");
	      		add_input(maintenanceRecordForm,"hidden",param[0],param[1]);
	      	}
	      	maintenanceRecordForm.submit();
	    }