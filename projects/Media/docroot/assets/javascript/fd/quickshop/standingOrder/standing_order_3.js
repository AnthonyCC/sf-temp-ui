var soName = "";
var isNewSOCreated = false;

$jq( document ).ready(function() {
	if($jq(".standing-orders-3-newso-drawer-container").length){
		//new_standing_order
		soName = $jq(".standing-orders-3-name-input[name='soName']").val();
		
		if (typeof newsoID === 'undefined') {
			newsoID = 0;
		}
		$jq("input.standing-orders-3-name-input").focus(function() {
			$jq(".standing-orders-3-name-input-change").addClass("show");
		});
		$jq("input.standing-orders-3-name-input").blur(function() {
			$jq(".standing-orders-3-name-input-change.show").removeClass("show");
			$jq(".standing-orders-3-name-input[name='soName']").val(soName);
		});
		$jq(".standing-orders-3-char-count").text($jq("input.standing-orders-3-name-input").val().length + "/25");
		$jq('input.standing-orders-3-name-input').on('keyup',function(){
	   		$jq(".standing-orders-3-char-count").text($jq(this).val().length + "/25");
		});
		if($jq(".standing-orders-3-name-input[name='soName']").val() != ""){
			isNewSOCreated = true
			populateDrawer();
    		$jq("#soFreq").select2({
				minimumResultsForSearch: Infinity
			});
			$jq("#soFreq").on("select2:select", function (e) {
				$jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='timeslot'] button.change.cssbutton").click();
			});
			$jq(".standing-orders-3 .standing-orders-3-newso-drawer-container").addClass("show");
		}
	}

	//first_standing_order
	$jq('input.standing-orders-3-first-name-input').on('keyup', function(){
	    if ($jq(this).val().length != '' && $jq(this).val().length > 0){
	    	$jq(".standing-orders-3-char-count").css("display", "block");
	        $jq("button.standing-orders-3-start-button").prop("disabled", false);
	        $jq("input.standing-orders-3-first-name-input").css("border", "1px solid #aaa");
	        $jq(".standing-orders-3-background-container").stop().fadeTo(500, 0.1);
	        $jq(".standing-orders-3-intro-h-bottom").stop().animate({
	            "color": "#464646"
	        }, 500);
	    } else {
	    	$jq(".standing-orders-3-char-count").css("display", "none")
	        $jq("button.standing-orders-3-start-button").prop("disabled", true);
	        $jq(".standing-orders-3-background-container").stop().fadeTo(500, 1);
	        $jq(".standing-orders-3-intro-h-bottom").stop().animate({
	            "color": "#ffffff"
	        }, 500);
	        if ($jq("#so-error-message").length) {
	            $jq("#so-error-message").css("display", "none")
	        }
	    }
	    $jq(".standing-orders-3-char-count").text($jq(this).val().length + "/25");
	});

	if ($jq("#so-error-message").length){
	    $jq("button.standing-orders-3-start-button").prop("disabled", false);
	    $jq(".standing-orders-3-background-container").fadeTo(500, 0.1);
	    $jq(".standing-orders-3-intro-h-bottom").css("color", "#464646");
	    $jq("input.standing-orders-3-first-name-input").css("border", "1px solid #c30");
	    $jq(".standing-orders-3-first-name-input").focus();
	}

	// manage_standing_orders
	if(typeof $jq.QueryString["soid"] !== "undefined"){
		openSOSettings($jq.QueryString["soid"]);
		$jq("#soid_" + $jq.QueryString['soid'] + " .standing-orders-3-so-settings-item").focus();
	}
});

// new_standing_order
function standingOrderNewCancel(){
	window.location.href="/quickshop/standing_orders.jsp";
};

function newStandingOrderNameInputChangeOK(){
	$jq('#newsoErroMessage').text("");
	if (!isNewSOCreated && $jq(".standing-orders-3-name-input[name='soName']").val() != ""){
		soName = $jq(".standing-orders-3-name-input[name='soName']").val();
		submitFormNewSO("create", null, soName);
	} else if(soName != $jq(".standing-orders-3-name-input[name='soName']").val() && $jq(".standing-orders-3-name-input[name='soName']").val() != ""){
		soName = $jq(".standing-orders-3-name-input[name='soName']").val();
		submitFormNewSO("changename", newsoID, soName);
	}
};

function submitFormNewSO(action, id, name){
	if(action == "create"){
		var dataString = "action=create&soName=" + name + "&isSO=true";
	} else if (action == "changename"){
		var dataString = "soId=" + id + "&action=changename&soName=" + name + "&isSO=true&frequency=" + null;
	}
	$jq.ajax({
	    url: '/api/manageStandingOrder',
	    type: 'POST',
	    data: dataString,
	    success: function(data) {
	    	if(!isNaN(data)){
	    		newsoID = data;
	    	} else {
	    		$jq('#newsoErroMessage').html(data);
	    	}
	    	if($jq('#newsoErroMessage').text() == "" && action=="create"){
	    		isNewSOCreated = true;
	    		populateDrawer();
	    		$jq("#soFreq").select2({
					minimumResultsForSearch: Infinity
				});
				$jq("#soFreq").on("select2:select", function (e) {
					$jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='timeslot'] button.change.cssbutton").click();
				});
	    		$jq(".standing-orders-3 .standing-orders-3-newso-drawer-container").addClass("show");
	    	}
	    }
	});
};

//manage_standing_orders
function openUpcomingOrderCancel(id){
	var usoID = "#usoid_" + id;
	closeUpcomingOrderCancel();
	$jq(usoID + " .td-so-cancel .td-so-cancel-popup").addClass("open");
	$jq(usoID + " td").addClass("cancel");
};

function closeUpcomingOrderCancel(){
	$jq(".td-so-cancel .td-so-cancel-popup.open").removeClass("open");
	$jq(".table-so-upcoming-deliveries td.cancel").removeClass("cancel");
};

function openSettingsDelete(id){
	var soID = "#soid_" + id;
	$jq(".standing-orders-3-so-settings-buttons .so-delete-popup.open").removeClass("open");
	$jq(soID + " .standing-orders-3-so-settings-buttons .so-delete-popup").addClass("open");
};

function closeSettingsDelete(id){
	var soID = "#soid_" + id;
	$jq(soID + " .standing-orders-3-so-settings-buttons .so-delete-popup.open").removeClass("open");
};

function deleteSO(name, id){
	var soID = "#soid_" + id;
	var usoID = "#usoid_" + id;
	submitFormManageSO(id,"delete",name,null);
	if($jq(usoID).length > 0){
		openUpcomingOrderCancel(id);
	}	
	closeSettingsDelete(id);
	$jq(soID).remove();
};

function openSOSettings(id) {			
	closeSOSettings();
	submitFormManageSO(id,'settings',null,null);
};

function closeSOSettings(){
	$jq(".standing-orders-3-so-settings-buttons .so-delete-popup.open").removeClass("open");
	if($jq("#ec-drawer")){
		$jq(".standing-orders-3-so-settings-container.open").removeClass("open");
		$jq(".standing-orders-3-so-settings-item .standing-orders-3-so-settings-error .standing-orders-3-so-settings-error-steps-link:disabled").prop( "disabled", false );
		$jq('.standing-orders-3-so-settings .standing-orders-3-so-settings-activate.open').removeClass('open');
		$jq("#ec-drawer").remove();
		$jq("#cartcontent").remove();
	}
};

function changeSOName(id){
	var soID = "#soid_" + id;
	$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input-container").addClass("open");
	$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").focus();
	soName = $jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").val();
	$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").blur(function(){
		$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input-container").removeClass("open");
	});
};

function standingOrdersNameInputChangeOK(id){
	var usoID = "#usoid_" + id;
	var soID = "#soid_" + id;
	if(soName != $jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").val() && 
	$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").val() != ""){
		soName = $jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").val();
		$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-header").text(soName);
		$jq(usoID + " td.td-so-name").text(soName);
		submitFormManageSO(id,"changename",soName,null);
	}			
}

function soSaved(id, activated){
	var soID = "#soid_" + id;
	if(activated){
		$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-buttons .standing-orders-3-so-settings-saved").addClass("show");
		$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-buttons .standing-orders-3-so-settings-saved-activated").addClass("show");
		setTimeout(function(){
			$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-buttons .standing-orders-3-so-settings-saved").removeClass("show");
			$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-buttons .standing-orders-3-so-settings-saved-activated").removeClass("show");
		}, 3000);
	} else {
		$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-buttons .standing-orders-3-so-settings-saved").addClass("show");
		setTimeout(function(){
			$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-buttons .standing-orders-3-so-settings-saved").removeClass("show");
		}, 3000);
	}
}

function submitFormManageSO(id,action,name,freq){
	var soID = "#soid_" + id;
	var dataString = "soId=" + id + "&action=" + action+ "&soName=" + name+ "&isSO=true&frequency="+freq;
	$jq.ajax({
        url: '/api/manageStandingOrder',
        type: 'POST',
        data: dataString,
        success: function(data) {
            if('settings'==action){
        		$jq("#ec-drawer").remove();
        		$jq("#cartcontent").remove();
         	    window.FreshDirect.expressco.data = data;
        	    window.FreshDirect.metaData = window.FreshDirect.expressco.data.formMetaData;
      			$jq(soID).addClass("open");
      			$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-error .standing-orders-3-so-settings-error-steps-link").prop( "disabled", true );
      			if(data.standingOrderResponseData.activate){
      				$jq(soID + " .standing-orders-3-so-settings-activate").addClass("open");
	      		}
      			$jq(soID + ' .standing-orders-3-so-settings-drawer-cart .standing-orders-3-so-settings-drawer-cart-container').prepend('<div id="cartcontent" class="view_cart" data-ec-linetemplate="expressco.viewcartlines" gogreen-status="false"></div>');
      			populateCartContent();
      			$jq(soID + ' .standing-orders-3-so-settings-drawer-cart .standing-orders-3-so-settings-drawer-cart-container').prepend('<div id="ec-drawer"></div>');
      			populateDrawer();
      			$jq("#soFreq").select2({
      				minimumResultsForSearch: Infinity
        		});
      			$jq("#soFreq").on("select2:select", function (e) {
      				$jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='timeslot'] button.change.cssbutton").click();
      			});
      			// Catches all updates
      			$jq("#cartcontent").on( "cartcontent-update", function(){ soItemTriggerUpdate(id, data, false) });
      			$jq("#cartcontent").on( "quantity-change", function(){ soItemTriggerUpdate(id, data, true) });
      			$jq("#cartcontent").on( "cartline-delete", function(){ soItemTriggerUpdate(id, data, true) });
      			$jq("#ec-drawer").on( "address-update", function(){ soItemTriggerUpdate(id, data, false) });
      			$jq("#ec-drawer").on( "timeselector-update", function(){ soItemTriggerUpdate(id, data, false) });
      			$jq("#ec-drawer").on( "paymentmethod-update", function(){ soItemTriggerUpdate(id, data, false) });
            }
            if('selectFreq'==action){
            	if(typeof data.timeslot !== "undefined" && typeof data.timeslot.soFreq !== "undefined" && data.timeslot.soFreq != null){
      		      	document.getElementById("soFreq").selectedIndex = data.timeslot.soFreq;
      		    	document.getElementById("soFreq2").selectedIndex = data.timeslot.soFreq;
            	}
            	getSOData(id, action);
            }
            if('changename'==action){
            	soSaved(id, false);
            }
            if('activate'==action){
            	getSOData(id, action);
            }
            if('delete'==action){
            	$jq(soID).remove();
            }
        }
 	});
};

function populateCartContent(){
	if(null!=FreshDirect.expressco.cartcontent){
  		FreshDirect.expressco.cartcontent.listen();
  		FreshDirect.expressco.cartcontent.watchChanges();
  		FreshDirect.expressco.cartcontent.update();
	}
}

function populateDrawer(){
	FreshDirect.common.dispatcher.signal('drawer', FreshDirect.expressco.data.drawer);
	FreshDirect.common.dispatcher.signal('payment', FreshDirect.expressco.data.payment);
	FreshDirect.common.dispatcher.signal('address', FreshDirect.expressco.data.address);
	FreshDirect.common.dispatcher.signal('timeslot', FreshDirect.expressco.data.timeslot);
}

function soItemTriggerUpdate(id, data, isCartUpdate){
	var soID = "#soid_" + id;
	if(isCartUpdate){
		setTimeout(function(){
			getSOData(id, "soItemUpdate");
			if(data.standingOrderResponseData.activate){
    			$jq(soID + " .standing-orders-3-so-settings-activate").addClass("open");
	      	}
		}, 3000);
	} else {
		getSOData(id, "soItemUpdate");
		if(data.standingOrderResponseData.activate){
			$jq(soID + " .standing-orders-3-so-settings-activate").addClass("open");
      	}
	}
}

function getSOData(id, action){
	var soID = "#soid_" + id;
	var dataString = "soId=" + id;
	$jq.ajax({
        url: '/api/manageStandingOrder',
        type: 'GET',
        data: dataString,
        success: function(data){
        	bugaga = data;
        	if('selectFreq'==action){
        		soSaved(id, data.activated);
        		if(data.frequencyDesc != null && data.dayOfWeek != null){
        			$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-details .standing-orders-3-so-settings-item-details-frequency").html(data.frequencyDesc + " " + data.dayOfWeek + " /");
        		}
        	}
        	if('activate'==action){
        		$jq(soID + " .standing-orders-3-so-settings-activate .standing-orders-3-so-settings-activate-icon").remove();
            	$jq(soID + " .standing-orders-3-so-settings-activate .standing-orders-3-so-settings-activate-button").remove();
            	$jq(soID + " .standing-orders-3-so-settings-activate .standing-orders-3-so-settings-activate-message .standing-orders-3-so-settings-activate-message-date").html("Your Standing Order has been activated.");
            	$jq(soID + " .standing-orders-3-so-settings-activate .standing-orders-3-so-settings-activate-message .standing-orders-3-so-settings-activate-message-text").html("First Delivery: " + data.dayOfWeek + ", " + data.shortDeliveryDate);
            	setTimeout(function(){
            		$jq(soID + " .standing-orders-3-so-settings-activate").remove();
					closeSOSettings();
				}, 3000);
            	updateSOItem(id, data);
            }
        	if('soItemUpdate'==action){
        		soSaved(id, data.activated);
        		updateSOItem(id, data);
        	}
        }
 	});
};

function selectFrequency(item){
	var freq;
	if(item.id == "soFreq" && item.value != $jq("#soFreq2").val()){
		freq = item.value;
		$jq("#soFreq2").val(freq);
		$jq("#soFreq2").select2("val", freq);
		submitFormManageSO(id,"selectFreq",null,freq);
	}
	if(item.id == "soFreq2"){
		freq = item.value;
		if($jq("#soFreq").length > 0 && item.value != $jq("#soFreq").val()){
			$jq("#soFreq").val(freq);
			$jq("#soFreq").select2("val", freq);
		}
		submitFormManageSO(id,"selectFreq",null,freq);
	}
};

function activateSo(id){
	submitFormManageSO(id,"activate",null,null);
};

function updateSOItem(id, data){
	var soID = "#soid_" + id;
	var softLimit = FreshDirect.standingorder.softLimitDisplay || "50";
	$jq(soID + " .standing-orders-3-so-settings-item").html(standingorder.standingOrderSettingsItem({item:data, softLimit}));
};