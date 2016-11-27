		
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

		function rejectIssue(userId){
			var id = document.getElementById('issueId').value;
			if(id !== ''){
				var result = jsonrpcClient.AsyncDispatchProvider.doRejectMaintenanceIssue(sendRejectFormCallback, id, userId);
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
			 getElectricFleetMetrics();
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
		
		

		// Restrict user input in a text field
		var digitsOnly = /[1234567890]/g;
		var integerOnly = /[0-9\.]/g;
		var alphaOnly = /[A-Za-z]/g;
		var usernameOnly = /[0-9A-Za-z\._-]/g;

		var oneToTen = /((^[1-9]{1}$)|(^10$))/g;

		function restrictInput(myfield, e, restrictionType, matchThisValue, checkdot) {
			if (!e)
				var e = window.event
			if (e.keyCode)
				code = e.keyCode;
			else if (e.which)
				code = e.which;
			var character = String.fromCharCode(code);

			// if user pressed esc... remove focus from field...
			if (code == 27) {
				this.blur();
				return false;
			}

			// ignore if the user presses other keys
			// strange because code: 39 is the down key AND ' key...
			// and DEL also equals .
			if (!e.ctrlKey && code != 9 && code != 8 && code != 36 && code != 37
					&& code != 38 && (code != 39 || (code == 39 && character == "'"))
					&& code != 40) {
		        
		        if (matchThisValue) {
		              if ((myfield.value.toString() + character).match(restrictionType)) {
		                   return true;
		              } else {
		                   return false;
		              }
		        } else {
		        	if (character.match(restrictionType)) {
						if (checkdot == "checkdot") {
							return !isNaN(myfield.value.toString() + character);
						} else {
							return true;
						}
		        	}
				} 
			}
		}
		

function getElectricFleetMetrics() {
	addSysMessage("", false);
	$("#socStart").val('');
	$("#socEnd").val('');
	$("#socReeferStart").val('');
	$("#socReeferEnd").val('');
	var truckNumber = document.getElementById("truckNumber");
	var truckNumber = truckNumber.options[truckNumber.selectedIndex].value;
	if (truckNumber.length != 0) {
		jsonrpcClient.AsyncDispatchProvider.getElectricFleetMetrics(
				sendElectricFleetCallback, truckNumber);
	}
}
		

function sendElectricFleetCallback(result, exception) {
	if (exception) {
		alert('Unable to connect to host system. Please contact system administrator!');
		return;
	}
	var socDriveFlag;
	var socReeferFlag;
	$("#electricDrive").hide();
	$("#electricReeferTextDiv").hide();
	$("#electricReeferDropdownDiv").hide();
	if (result !== null) {
		socDriveFlag = result.map.EDF;
		socReeferFlag = result.map.ERF;
		if (socDriveFlag === 'Electric') {
			$("#electricDrive").show();
		}
		if (socDriveFlag === 'Electric' && socReeferFlag === 'Electric') {
			$("#electricReeferTextDiv").show();
		} else if (socDriveFlag === 'Diesel' && socReeferFlag === 'Generator') {
			$("#electricReeferDropdownDiv").show();
		}

	} else {
		addSysMessage("Populating vendor info failed for TRUCK selected", true);
	}
}