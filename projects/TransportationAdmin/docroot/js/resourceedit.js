var jsonrpcRptClient = new JSONRpcClient("dispatchprovider.ax");

function resoureChangeEvent(src, view, actionDate, srcId) {
	if(src != null && src.id != null && (src.id.indexOf("drivers") != -1 || src.id.indexOf("helpers") != -1)) {	
			
		var result;
		var resultList;
		
		if("P" == view) {
			resultList = jsonrpcRptClient.AsyncDispatchProvider.getPlanForResource(actionDate.value , src.value, srcId.value);
		} else {
			resultList = jsonrpcRptClient.AsyncDispatchProvider.getDispatchForResource(actionDate.value , src.value, srcId.value);
		}
		if(resultList != null) {
			result = resultList.list[0];			
			var hasConfirmed = confirm ("This employee is part of a team.  Would you like to assign their teammate(s) to the route?  Please Confirm")
			if (hasConfirmed) {
				for (var _driver in result.drivers.list) {
					var _field = document.getElementById("drivers["+_driver+"].employeeId");
					if(_field != null) {
						_field.value = result.drivers.list[_driver].employeeId;
					}	
				}
				for (var _helper in result.helpers.list) {
					var _field = document.getElementById("helpers["+_helper+"].employeeId");
					if(_field != null) {
						_field.value = result.helpers.list[_helper].employeeId;
					}	
				}
				/*for (var _runner in result.runners.list) {
					var _field = document.getElementById("runners["+_runner+"].employeeId");
					if(_field != null) {
						_field.value = result.runners.list[_runner].employeeId;
					}	
				}*/
				var refContext = document.getElementById("referenceContextId");				
				if(refContext != null) {	
					if("P" == view) {
						var _planId = "";
						for (var _plan in resultList.list) {
							if(_planId != "") {
								_planId = _planId + ",";
							}
							_planId = _planId + resultList.list[_plan].planId;							
						}														
						refContext.value = _planId;
					} else {
						var _dispatchId = "";
						for (var _dispatch in resultList.list) {
							if(_dispatchId != "") {
								_dispatchId = _dispatchId + ",";
							}
							_dispatchId = _dispatchId + resultList.list[_dispatch].dispatchId;							
						}						
						refContext.value = _dispatchId;
					}					
				}
			} else {
				var refTeamOverride = document.getElementById("isTeamOverride");				
				if(refTeamOverride != null) {
					refTeamOverride.value = 'true';
				}				
			}
		}
	}
}
	
	function assetChangeEvent(src) {
		
		var result = false;
		var destinationId;
		if("gpsNumber" == src.id) {
			result = jsonrpcRptClient.AsyncDispatchProvider.hasDispatchForGPS(document.getElementById('dispatchDate').value
																				, document.getElementById('dispatchId').value
																				, document.getElementById('firstDeliveryTime').value
																				, src.value);
			destinationId = "curr_gpsNumber";																	
		} else if("ezpassNumber" == src.id) {
			result = jsonrpcRptClient.AsyncDispatchProvider.hasDispatchForEZPass(document.getElementById('dispatchDate').value
																				, document.getElementById('dispatchId').value
																				, document.getElementById('firstDeliveryTime').value
																				, src.value);
			destinationId = "curr_ezpassNumber";																				
		} else if("motKitNumber" == src.id) {
			result = jsonrpcRptClient.AsyncDispatchProvider.hasDispatchForMotKit(document.getElementById('dispatchDate').value
																				, document.getElementById('dispatchId').value
																				, document.getElementById('firstDeliveryTime').value
																				, src.value);
			destinationId = "curr_motKitNumber";																	
		}
		if(result) {
			var hasConfirmed = confirm ("This Asset is already used in a active dispatch.  Would you like to still assign to the dispatch?  Please Confirm")
			if (hasConfirmed) {
				document.getElementById(destinationId).value = src.value;
				return true;
			} else {
				src.value = document.getElementById(destinationId).value;
				return false;
			}
		}
		document.getElementById(destinationId).value = src.value;
		return true;
	}