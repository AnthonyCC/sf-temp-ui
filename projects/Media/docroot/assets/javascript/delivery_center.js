

	var jsonrpcClient = new JSONRpcClient("/api/message.jsp");

	var loadingStr = '<div style="position:relative;"><img style="position:absolute;top:50%;left:50%;margin-top:-8px;margin-left:-8px" src="../media_stat/crm/images/loading.gif" /></div>';

	$jq(document).ready(function(){

			lookupNextels();
			lookupCartonScanHistory();
			lookupAirclicMessages();
			lookupDeliveryManifest();
			lookupOrderCallLog();
			lookupSmsMessages();

    });	

			function lookupNextels(){
				$jq("#nextelInfo").html('');
				$jq("#nextelInfo").append(loadingStr).find('div:first').css({height:"120px"});
				jsonrpcClient.manager.lookupNextels(nextelCallBack, orderNo, routeNo, date );
			}
			function lookupCartonScanHistory(){
				$jq("#cartonScanInfo").html('');
				$jq("#cartonScanInfo").append(loadingStr).find('div:first').css({height:"350px"});
				jsonrpcClient.manager.lookupCartonScanHistory(cartonScanHistoryCallBack, orderNo);
			}
			function lookupAirclicMessages(){
				$jq("#airclicMessage").html('');
				$jq("#airclicMessage").append(loadingStr).find('div:first').css({height:"150px"});
				jsonrpcClient.manager.lookupAirclicMessages(airClicMsgCallBack, orderNo);
			}
			function lookupDeliveryManifest(){
				$jq("#manifestDetail").html('');
				$jq("#manifestDetail").append(loadingStr).find('div:first').css({height:"550px"});
				jsonrpcClient.manager.getDeliveryManifest(manifestCallBack, orderNo, date,estoreId);
			}
			function lookupOrderCallLog(){
				$jq("#callLogInfo").html('');
				$jq("#callLogInfo").append(loadingStr).find('div:first').css({height:"200px"});
				jsonrpcClient.manager.getOrderCallLog(callLogCallBack, orderNo);
			}
			function lookupSmsMessages(){
				$jq("#smsInfo").html('');
				$jq("#smsInfo").append(loadingStr).find('div:first').css({height:"450px"});
				jsonrpcClient.manager.getSmsMessages(smsCallBack, orderNo);
			}
			
			function smsCallBack(smsResult, error){
				console.log(error);
				if(error!=null) return;
				
				var result='';
				if  (smsResult != null) { 
					var smsrows=smsResult.list;
					for(var i=0;i < smsrows.length;i++){
						if( i % 2 != 0){
							result += "<tr class=\"list_odd_row\">"; 
						} else {
							result += "<tr>";
						}
						result += "<td width=\"115\" style=\"text-align:center \">"+ smsrows[i].timeSent + "</td>";
						result += "<td width=\"111\" style=\"text-align:center \">"+ smsrows[i].alertType + "</td>";
						result += "<td width=\"115\" >"+ smsrows[i].message + "</td>";
						result += "<td width=\"115\" style=\"text-align:center \">"+ smsrows[i].status + "</td>";
						result += "<td width=\"115\">"+ smsrows[i].mobileNumber + "</td>";
						result += "</tr>";
					}
				} else {
					result += '<tr><td colspan=\"4\" class=\"gc_sms_not_present\">Sorry, There are no SMS Messages</td></tr>';
				}
				$jq('#smsInfo').html("<table>"  + result + "</table>");
			}

			function callLogCallBack(callLogResult, error){
				console.log(error);
				if(error!=null) return;
				
				var result = '';
				var calllog = callLogResult.list;
				for(var i=0;i < calllog.length;i++){
						if( i % 2 != 0){
							result += "<tr class=\"list_odd_row\">"; 
						} else {
							result += "<tr>";
						}
						result += "<td width=\"100\">"+ calllog[i].callerId + "</td>";
						result += "<td width=\"115\">"+ calllog[i].scanTime + "</td>";
						result += "<td width=\"115\">"+ calllog[i].duration + "</td>";
						result += "<td width=\"115\">"+ calllog[i].talkTime + "</td>";
						result += "<td width=\"115\">"+ calllog[i].phoneNumber + "</td>";
						result += "<td width=\"115\">"+ calllog[i].menuOption + "</td>";
						result += "<td width=\"115\">"+ calllog[i].callOutcome + "</td>";
						result += "</tr>";
				}
				$jq('#callLogInfo').html("<table>"  + result + "</table>");
			}
			
			
			function nextelCallBack(nextelResult, error){
				console.log(error);
				if(error!=null) return;
					var result = '';
					var nextels = nextelResult.list;
					for(var i=0;i < nextels.length;i++){
							if( i % 2 != 0){
								result += "<tr class=\"list_odd_row\">"; 
							} else {
								result += "<tr>";
							}
							result += "<td><input id=\""+ nextels[i].nextel+"\" type=\"checkbox\" name=\""+ nextels[i].nextel+"\" /></td>";
							result += "<td width=\"115\">" + nextels[i].nextel + "</td>";
							result += "<td width=\"205\">"+ nextels[i].employee + "</td>";
							result += "<td width=\"117\">"+ nextels[i].empId + "</td>";
							result += "</tr>";
					}
					$jq('#nextelInfo').html("<table id=\"nextel_table\">" + result + "</table>");
			}

			function cartonScanHistoryCallBack(cartonScanResult, error){
				console.log(error);
				if(error!=null) return;
				
				var result = '';
				var cartons = cartonScanResult.list;
				var temp = false;
				for(var i=0;i < cartons.length;i++){					
					var cartonDetails = cartons[i].details.list;				
					for(var j=0;j < cartonDetails.length;j++){
						if( temp ){
							result += "<tr class=\"list_odd_row\">"; 
						} else {
							result += "<tr>";
						}
						temp = !temp;
						result += "<td width=\"45\">" + cartonDetails[j].nextel + "</td>";
						result += "<td width=\"100\">"+ cartonDetails[j].employee + "</td>";
						result += "<td width=\"100\">"+ cartons[i].cartonNumber + "</td>";
						result += "<td width=\"30\">" + cartons[i].cartonType + "</td>";
						result += "<td width=\"150\">"+ cartonDetails[j].scanTime+ "</td>";
						result += "<td width=\"80\">" + cartonDetails[j].cartonStatus + "</td>";
						result += "<td width=\"80\">"
						if(cartonDetails[j].deliveredTo != null) {
							result += cartonDetails[j].deliveredTo;
						}
						result += "</td>"
						result += "<td width=\"130\">"
						if(cartonDetails[j].returnReason != null) {
							result += cartonDetails[j].returnReason;
						}
						result += "</td>"
						result += "<td width=\"130\">"
						if(cartonDetails[j].motDriverName != null) {
							result += cartonDetails[j].motDriverName;
						}
						result += "</td>"
						result += "</tr>";
					}
				}	
				$jq('#cartonScanInfo').html("<table>" + result + "</table>");
			}

			function airClicMsgCallBack(msgResult, error) {
				console.log(error);
				if(error!=null) return;
				
				var result = '';
				var messages = msgResult.list;
				for(var j=0;j < messages.length;j++){
						if( j % 2 != 0){
							result += "<tr class=\"list_odd_row\">"; 
						} else {
							result += "<tr>";
						}
						result += "<td width=\"130\">" + messages[j].createDateStr + "</td>";
						result += "<td width=\"80\">" + messages[j].sender + "</td>";
						result += "<td width=\"250\">"+ messages[j].message + "</td>";
						result += "<td width=\"200\">"+ messages[j].route+ "</td>";
						result += "<td width=\"30\">" + messages[j].stop + "</td>";
						result += "<td width=\"30\">" + messages[j].source + "</td>";
						result += "<td width=\"80\">" + messages[j].sentToAirclic + "</td>";
						result += "</tr>";
				}
				$jq('#airclicMessage').html("<table>" + result + "</table>");
			}

			function manifestCallBack(manifestResult){

				var result = '';
				result += "<tr><td colspan=\"2\"><b>Intraday Note</b><br/></td>";
				if(manifestResult.lastAirclicMsg != null){
					result += "<tr><td colspan=\"2\">"+manifestResult.lastAirclicMsg+"</td></tr>";
				}
				result += "<tr><td colspan=\"2\"><img height=\"15\" border=\"0\" src=\"/media_stat/images/layout/clear.gif\"></td></tr>";
				
				result += "<tr><td width=\"210\">Stop</td><td>"+ manifestResult.stopNo +"</td></tr>";
				if(manifestResult.deliveryETAWindowTime != null) {
					result += "<tr><td>ETA Window</td><td>"+ manifestResult.deliveryETAWindowTime +"</td></tr>";
				}
				result += "<tr><td>Delivery Window</td><td>"+ manifestResult.windowTime +"</td></tr>";
				result += "<tr><td>Boxes</td><td>"+ manifestResult.cartonCnt +"</td></tr>";
				result += "<tr><td>Order No</td><td>"+ orderNo +"</td></tr>";
				if( manifestResult.lastName !=null && manifestResult.firstName != null ) {
					result += "<tr><td>Name</td><td>"+ manifestResult.lastName + " " + manifestResult.firstName +"</td></tr>";
				}
				result += "<tr><td>Address</td><td>"+ manifestResult.address +"</td></tr>";
				result += "<tr><td>Cross Street</td><td>";
				if(manifestResult.crossStreet != null){
					result += manifestResult.crossStreet;
				}
				result += "</td></tr>";

				result += "<tr><td colspan=\"2\"><img height=\"15\" border=\"0\" src=\"/media_stat/images/layout/clear.gif\"></td></tr>";
									
				result += "<tr><td>Building Type</td><td>";
				if(manifestResult.crossStreet != null){
					result += manifestResult.crossStreet;
				}
				result += "</td></tr>";
				result += "<tr><td>Service Address</td><td>";
				if(manifestResult.buildingType != null){
					result += manifestResult.buildingType;
				}
				result += "</td></tr>";
				result += "<tr><td>Service Hours</td><td>";
				if(manifestResult.serviceHours != null){
					result += manifestResult.serviceHours;
				}
				result += "</td></tr>";
				
				result += "<tr><td colspan=\"2\"><img height=\"15\" border=\"0\" src=\"/media_stat/images/layout/clear.gif\"></td></tr>";

				result += "<tr><td colspan=\"2\"><b>Delivery Instructions:</b><br/>";
				if(manifestResult.deliveryInstructions != null){
					result += manifestResult.deliveryInstructions;
				}
				result += "</td></tr>";
				result += "<tr><td colspan=\"2\"><b>Uattended DeliveryInstructions:</b><br/>";
				if(manifestResult.uattendedDeliveryInstructions != null){
					result += manifestResult.uattendedDeliveryInstructions;
				}
				result += "</td></tr>";
				result += "<tr><td colspan=\"2\"><img height=\"15\" border=\"0\" src=\"/media_stat/images/layout/clear.gif\"></td></tr>";
									
				result += "<tr><td colspan=\"2\"><b>SIGNATURE</b><br/>By signing I am acknowledging<br/>only that I have received <br/>as listed below.<br/><br/></td></tr>";
				result += "<tr><td colspan=\"2\"><b>Customer Signature</b><br/>&nbsp;</td></tr>";
				if(hasSignature){
					result += "<tr><td colspan=\"2\"><iframe src=\"/api/viewsignature.jsp?orderId="+orderNo+"&estoreId="+estoreId+"\" /></td></tr>"
				}
				
				$jq('#manifestDetail').html("<table width=\"99%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" class=\"gc_table2footer\">" + result + "</table>");
			}

			// Adds an entry
			function addEntry(date, route, stop, message, msgSrc, userId, orderId, customerId)
			{
				try
				{
					$('ac_info').update("");
					if(message == "") {
						$('ac_info').update("Message is empty. Please enter message");
					} else {
						 var _data = new Array();
						 _data[0] = date;
						 _data[1] = route;
						 _data[2] = stop;
						 _data[3] = message;
						 _data[4] = msgSrc;
						 _data[5] = userId;
						 _data[6] = orderId;
						 _data[7] = customerId;
						 
						var _nextelData = new Array();
						var table_nextel = document.getElementById("nextel_table");
						var checkboxList_Nextels = table_nextel.getElementsByTagName("input");
						var j = 0;

						for (i = 0; i < checkboxList_Nextels.length; i++) {            
						  if (checkboxList_Nextels[i].type=="checkbox" && !checkboxList_Nextels[i].disabled)  {
							if(checkboxList_Nextels[i].checked) {
								_nextelData[j] = checkboxList_Nextels[i].name;
								j++;
							}
						  }
						}

						if(_nextelData != null && _nextelData.length == 0){
							$('ac_info').update("Please select CN(s) to send message.");
						} else {
							 var result = jsonrpcClient.manager.sendMessage(_data, _nextelData);
							 $('ac_info').update(result);
						}
					}
				} catch(e) {
						$('ac_info').update("");
						$('ac_info').update("There was an error. Try again later.");
				}
			}

			function getNextels(){
					var _nextelData = new Array();
					var table_neighbourhood = document.getElementById("nextel_table");
					var checkboxList_Nextels = table_neighbourhood.getElementsByTagName("input");
					var j = 0;
					
					for (i = 0; i < checkboxList_Nextels.length; i++) {            
					  if (checkboxList_Nextels[i].type=="checkbox" && !checkboxList_Nextels[i].disabled)  {
						if(checkboxList_Nextels[i].checked) {
							_nextelData[j] = checkboxList_Nextels[i].name;
							j++;
						}
					  }
					}
			}

