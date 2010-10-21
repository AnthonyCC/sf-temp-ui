var jsonrpcRptClient = new JSONRpcClient("dispatchprovider.ax");

function resoureChangeEvent(src, view, actionDate, srcId) {
	if(src != null && src.id != null && (src.id.indexOf("drivers") != -1 || src.id.indexOf("helpers") != -1)) {	
			
		var result;
		if("P" == view) {
			result = jsonrpcRptClient.AsyncDispatchProvider.getPlanForResource(actionDate.value , src.value, srcId.value);
		} else {
			result = jsonrpcRptClient.AsyncDispatchProvider.getDispatchForResource(actionDate.value , src.value, srcId.value);
		}
		if(result != null) {
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
						refContext.value = result.planId;
					} else {				
						refContext.value = result.dispatchId;
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