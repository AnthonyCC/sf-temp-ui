jQuery(function() {
		
		var dialog1, dialog2, 
		dispcardvalue = jQuery("#dispcardvalue"),
		cardnotassigned = jQuery("#cardnotassigned"),
		chkincardvalue= jQuery("#chkincardvalue"),
		cardnotreturned = jQuery("#cardnotreturned");
		var tips = jQuery( ".validateTips" );
		 function updateTips( t ) {
		      tips
		        .text( t )
		        .addClass( "ui-state-highlight" );
		      setTimeout(function() {
		        tips.removeClass( "ui-state-highlight", 1500 );
		      }, 500 );
		    }
		dialog1 = jQuery( "#dialog-dispatch" ).dialog({
		      autoOpen: false,
		      height: 210,
		      width: 300,
		      modal: true,
			  closeOnEscape: false,
		      dialogClass: "noclose",
			  //position: { my: "left+10 bottom", at: "right top", of: jQuery(".dispoverlay") },
		      buttons: {
		        "save": function() {
		        	updateMuniMeterCardDetails(true);
		         
		        }
		      }
		      
		    });
		dialog2 = jQuery( "#dialog-checkin" ).dialog({
		      autoOpen: false,
		      height: 210,
		      width: 300,
		      modal: true,
			  closeOnEscape: false,
		      dialogClass: "noclose",
			  //position: { my: "left+10 bottom", at: "right top", of: jQuery(".chkinoverlay") },
		      buttons: {
		        "save": function() {
		        	updateMuniMeterCardDetails(false);
		         
		        }
		      }
		      
		    });
		function updateMuniMeterCardDetails(isDispatch, value, status){
			//Validate data and close the overlay
			var valid=true;
			var status="N";
			if(isDispatch){
				valid=validateDialog(dispcardvalue, cardnotassigned);
				
				if(valid){
					if(cardnotassigned.is(':checked')){
						status="X";
						
					}
					var result = jsonrpcClient.AsyncDispatchProvider.updateMuniMeterCardDetails(updateCardCallBack, "dispatch", jQuery.trim(dispcardvalue.val()), status, dispId,'<%= com.freshdirect.transadmin.security.SecurityManager.getUserName(request)%>');
					
				}
			} else {
				valid=validateDialog(chkincardvalue, cardnotreturned);
				
				if( assignedValue>0 && jQuery.trim(chkincardvalue.val()).length>0 && parseFloat(jQuery.trim(chkincardvalue.val())) > assignedValue){
					
					valid=false;
					chkincardvalue.addClass( "ui-state-error" );
					updateTips("Value exceded assigned value : jQuery"+assignedValue);
				}
				if(valid){
					if(cardnotreturned.is(':checked')){
						status="X";
					}
					var result = jsonrpcClient.AsyncDispatchProvider.updateMuniMeterCardDetails(updateCardCallBack, "checkin", jQuery.trim(chkincardvalue.val()), status, dispId, '<%= com.freshdirect.transadmin.security.SecurityManager.getUserName(request)%>');
					
				}
			}
			
			
		}
		
		function validateDialog(valueField, checkboxField){
			valid=false;
			if(jQuery.trim(valueField.val()).length==0 && !checkboxField.is(':checked') ){
				valid=false;
				valueField.addClass( "ui-state-error" );
				updateTips("Please fill one of the values");
			} else if(jQuery.trim(valueField.val()).length>0 && checkboxField.is(':checked')){
				valid=false;
				valueField.addClass( "ui-state-error" );
				updateTips("Please fill only one field");
		
			} else if(jQuery.trim(valueField.val()).length>0 && !checkboxField.is(':checked')){
				if( !/^\s*jQuery/.test(jQuery.trim(valueField.val())) && !isNaN(parseFloat(jQuery.trim(valueField.val())))){
					
					if(parseFloat(jQuery.trim(valueField.val())) > 0){
						valid=true;
					} else {
						valid=false;
						valueField.addClass( "ui-state-error" );
						updateTips("Negative dollar amount!");
					}
					
				} else{
					valid=false;
					valueField.addClass( "ui-state-error" );
					updateTips("Please enter a valid dollar amount!");
				}
				
			} else if(jQuery.trim(valueField.val()).length==0 && checkboxField.is(':checked')){
				valid=true;
				
			} else{
				
				valid=false;
				valueField.addClass( "ui-state-error" );
				updateTips("Please Enter valid values");
			}
			return valid;
		}
		
		function updateCardCallBack(result, exception) {
			
     	   if(result != null && result) {
     		  dispcardvalue.removeClass( "ui-state-error");
     		 chkincardvalue.removeClass( "ui-state-error");
     		jQuery( ".validateTips" ).empty();
     		  dialog2.dialog( "close" );
    		   dialog1.dialog("close");
     		   alert('Card data updated successfully');
     		  
     	   } else {
     		   alert('Error updating card data');
     		  dispcardvalue.addClass( "ui-state-error" );
     		 chkincardvalue.addClass( "ui-state-error" );
     		  updateTips("Please give a valid value");
     	   }
     	   
        }
		
		function displayDialog(dialogFlag){
			
			//run a jsonrpcclient call 
			var result = jsonrpcClient.AsyncDispatchProvider.getDialogDisplayFlag(displayDialogCallBack, dialogFlag, dispId);
			
		}
		
		function displayDialogCallBack(result, exception){
			
			if(result!=null && result=="dispatch"){
				
				 dialog1.dialog( "open" );
			} else if(result!=null && result.indexOf("checkin")>-1){
				var resultSplit=result.split("_");
				assignedValue=parseFloat(resultSplit[1].trim());
				 dialog2.dialog( "open" );
			} else {
				//Do nothing result=="checkin"
			}
		}
		jQuery(".dispoverlay").change(function() {
			
		       if (jQuery(this).is(':checked')) {
		    	   
		    	   var classes = jQuery(this).attr("class").split(" ");
		    	  dispId=classes[2].trim();
		    	   displayDialog("dispatch");
			       
		        }
		    });
		jQuery(".chkinoverlay").click(function() {
			
		       if (jQuery(this).is(':checked')) {
		    	   var classes = jQuery(this).attr("class").split(" ");
		    	   dispId=classes[2].trim();
		    	   displayDialog("checkin");
			        
		        }
		    });
	 });