FreshDirect.standingorder = FreshDirect.standingorder || {};
FreshDirect.standingorder.isSelectFrequencyChanged = false;
var soName = "";
var isNewSOCreated = false;
var isActiveDrawerOpen = false;

$jq( document ).ready(function() {
	if($jq(".standing-orders-3-newso-drawer-container").length){
		//new_standing_order
		soName = $jq(".standing-orders-3-name-input[name='soName']").val();
		FreshDirect.standingorder.currentPage = "new";
		/*
		 soName = $jq(".standing-orders-3-name-input[name='soName']").val();
		$jq("input.standing-orders-3-name-input").focus(function() {
			$jq(".standing-orders-3-name-input-change").addClass("show");
		});
		$jq("input.standing-orders-3-name-input").blur(function() {
			$jq(".standing-orders-3-name-input-change.show").removeClass("show");
			$jq(".standing-orders-3-name-input[name='soName']").val(soName);
		});
		*/
		$jq(".standing-orders-3-char-count").text($jq("input.standing-orders-3-name-input").val().length + "/25");
		$jq('input.standing-orders-3-name-input').on('keyup',function(){
	   		$jq(".standing-orders-3-char-count").text($jq(this).val().length + "/25");
	   		newStandingOrderInfoCheck();
		});
		/*
		if($jq(".standing-orders-3-name-input[name='soName']").val() != ""){
			isNewSOCreated = true;
    		$jq("#soFreq").select2({
				minimumResultsForSearch: Infinity
			});
			$jq(".standing-orders-3 .standing-orders-3-newso-drawer-container").addClass("show");
		}
		*/
		submitFormNewSO("onloadNewStandingOrder");
	    FreshDirect.common.dispatcher.signal('drawer', FreshDirect.expressco.data.drawer);
	  	FreshDirect.common.dispatcher.signal('payment', FreshDirect.expressco.data.payment);
	  	FreshDirect.common.dispatcher.signal('address', FreshDirect.expressco.data.address);
	  	FreshDirect.common.dispatcher.signal('timeslot', FreshDirect.expressco.data.timeslot);
	  	$jq("#ec-drawer").on( "address-update", function(){ newStandingOrderInfoCheck(); });
		$jq("#ec-drawer").on( "timeselector-update", function(){ newStandingOrderInfoCheck(); });
		$jq("#ec-drawer").on( "paymentmethod-update", function(){ newStandingOrderInfoCheck(); });
	}

	// manage_standing_orders
	if(typeof $jq.QueryString["soid"] !== "undefined"){
		openSOSettings($jq.QueryString["soid"]);
		$jq("#soid_" + $jq.QueryString['soid'] + " .standing-orders-3-so-settings-item").focus();
	}
	if($jq(".standing-orders-3-so-settings-container").length == 1){
		$jq(".standing-orders-3-so-settings-container button.standing-orders-3-so-button-settings").click();
	}
	FreshDirect.standingorder.isStandingOrderContext = true;
});

// new_standing_order
function standingOrderNewCancel(){
	window.location.href="/quickshop/standing_orders.jsp";
}

function newStandingOrderInfoCheck(){
	if($jq("[fdform='address'] input:checked").length && $jq("[fdform='payment'] input:checked").length && $jq("[fdform='timeslot'] input:checked").length && $jq(".standing-orders-3-name-input[name='soName']").val() != ""){
		$jq(".standing-orders-3 .standing-orders-3-create-header .standing-orders-3-new-start-shop").removeAttr('disabled');
	}
	else {
		$jq(".standing-orders-3 .standing-orders-3-create-header .standing-orders-3-new-start-shop").attr('disabled','disabled');
	}
}

function submitFormNewSO(action){
	var dataString = "";
	if(action=="create"){
		$jq("body").append("<div id='create-so-page-loading'>Creating Standing Order</div>");
	}
	dataString = "action="+ action +"&soName=" + $jq(".standing-orders-3-name-input[name='soName']").val() + "&isSO=true" + "&address=" + $jq("[fdform='address'] input:checked").val() + "&payment=" + $jq("[fdform='payment'] input:checked").val() + "&timeslot=" + $jq("[fdform='timeslot'] input:checked").val() + "&freq=" + $jq("#soFreq2 option:selected").val();
	$jq.ajax({
	    url: '/api/manageStandingOrder',
	    type: 'POST',
	    data: dataString,
	    success: function(data) {
	    	if(action=="create"){
		    	if(!isNaN(data)){
		    		window.location.href="/";
		    	} else {
		    		$jq("body #create-so-page-loading").remove()
		    		$jq('#newsoErroMessage').html(data);
		    	}
	    	}
	    }
	});
}

//manage_standing_orders
function openUpcomingOrderCancel(id){
	var usoID = "#usoid_" + id;
	closeUpcomingOrderCancel();
	$jq(usoID + " .td-so-cancel .td-so-cancel-popup").addClass("open");
	$jq(usoID + " td").addClass("cancel");
}

function closeUpcomingOrderCancel(){
	$jq(".td-so-cancel .td-so-cancel-popup.open").removeClass("open");
	$jq(".table-so-upcoming-deliveries td.cancel").removeClass("cancel");
}

function openSettingsDelete(id){
	var soID = "#soid_" + id;
	$jq(".standing-orders-3-so-settings-buttons .so-delete-popup.open").removeClass("open");
	$jq(soID + " .standing-orders-3-so-settings-buttons .so-delete-popup").addClass("open");
}

function closeSettingsDelete(id){
	var soID = "#soid_" + id;
	$jq(soID + " .standing-orders-3-so-settings-buttons .so-delete-popup.open").removeClass("open");
}

function deleteSO(id){
	var soID = "#soid_" + id;
	var usoID = "#usoid_" + id;
	submitFormManageSO(id,"delete",null,null);
	if($jq(usoID).length > 0){
		openUpcomingOrderCancel(id);
	}	
	closeSettingsDelete(id);
	$jq(soID).remove();
}

function openSOSettings(id) {			
	closeSOSettings();
	submitFormManageSO(id,'settings',null,null);
}

function closeSOSettings(){
	$jq(".standing-orders-3-so-settings-buttons .so-delete-popup.open").removeClass("open");
	if($jq("#ec-drawer")){
		$jq(".standing-orders-3-so-settings-container.open").removeClass("open");
		$jq(".standing-orders-3-so-settings-item .standing-orders-3-so-settings-error .standing-orders-3-so-settings-error-steps-link:disabled").prop( "disabled", false );
		$jq('.standing-orders-3-so-settings .standing-orders-3-so-settings-activate.open').removeClass('open');
		$jq(".standing-orders-3-so-settings-drawer-cart-container-wrap").empty();
	}
}
function opencloseSOSettings(id){
	if($jq(".standing-orders-3-so-settings-container.open").length){
		closeSOSettings();
	} else {
		openSOSettings(id);
	}
}
function changeSOName(id){
	var soID = "#soid_" + id;
	$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input-container").addClass("open");
	$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").focus();
	soName = $jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").val();
	$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input[name = 'soName']").blur(function(){
		$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-name-change-input-container").removeClass("open");
	});
}

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

function soPlaceOrderDisplay(amount){
	if ($jq(".standing-orders-3-so-settings-activate-button").length) {
		if(amount < FreshDirect.standingorder.softLimitDisplay){
			$jq(".standing-orders-3-so-settings-activate-button").prop("disabled", true);
		} else {
			$jq(".standing-orders-3-so-settings-activate-button").prop("disabled", false);
		}
	}
}

function soSaved(id, activatedAndHasAddress, isNewSO){
	var soID = "#soid_" + id;
	if(isNewSO){
		$jq(".standing-orders-3-so-new-saved").addClass("show");
		setTimeout(function(){
			$jq(".standing-orders-3-so-new-saved.show").removeClass("show");
		}, 5000);
	} else {
		if(activatedAndHasAddress){
			$jq(soID).addClass("show-saved");
			$jq(soID).addClass("show-saved-activated");
			setTimeout(function(){
				$jq(soID).removeClass("show-saved").removeClass("show-saved-activated").removeClass("drawer-saved").removeClass("cart-saved");
			}, 5000);
		} else {
			$jq(soID).addClass("show-saved");
			setTimeout(function(){
				$jq(soID).removeClass("show-saved").removeClass("drawer-saved").removeClass("cart-saved");
			}, 5000);
		}
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
        		$jq(".standing-orders-3-so-settings-drawer-cart-container-wrap").empty();
         	    window.FreshDirect.expressco.data = data;
        	    window.FreshDirect.metaData = window.FreshDirect.expressco.data.formMetaData;
      			$jq(soID).addClass("open");
      			$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-error .standing-orders-3-so-settings-error-steps-link").prop( "disabled", true );
      			if(data.timeslot.soDeliveryDate !== null && $jq(soID + " .standing-orders-3-so-settings-activate").length){
      				$jq(soID + " .standing-orders-3-so-settings-activate").addClass("open");
	      		}
      			$jq(soID + ' .standing-orders-3-so-settings-drawer-cart .standing-orders-3-so-settings-drawer-cart-container .standing-orders-3-so-settings-drawer-cart-container-wrap').prepend('<div id="cartcontent" class="view_cart" data-ec-linetemplate="expressco.viewcartlines" gogreen-status="false"></div>');
      			populateCartContent();
      			$jq(soID + ' .standing-orders-3-so-settings-drawer-cart .standing-orders-3-so-settings-drawer-cart-container .standing-orders-3-so-settings-drawer-cart-container-wrap').prepend("<div class='standing-orders-3-saved-container-cart'>" + $jq(soID + " .standing-orders-3-saved-container").html() + "</div>");
      			$jq(soID + ' .standing-orders-3-so-settings-drawer-cart .standing-orders-3-so-settings-drawer-cart-container .standing-orders-3-so-settings-drawer-cart-container-wrap').prepend('<div id="ec-drawer"></div>');      			
      			populateDrawer(id);
      			$jq("#soFreq").select2({
      				minimumResultsForSearch: Infinity
        		});
      			// Catches all updates
      			$jq("#cartcontent").on( "cartcontent-update", function(){ soItemTriggerUpdate(id, data, false); });
      			$jq("#cartcontent").on( "quantity-change", function(){ soItemTriggerUpdate(id, data, true); });
      			$jq("#cartcontent").on( "cartline-delete", function(){ soItemTriggerUpdate(id, data, true); });
      			$jq("#ec-drawer").on( "address-update", function(){ soItemTriggerUpdate(id, data, false); });
      			$jq("#ec-drawer").on( "timeselector-update", function(){ soItemTriggerUpdate(id, data, false); });
      			$jq("#ec-drawer").on( "paymentmethod-update", function(){ soItemTriggerUpdate(id, data, false); });
            }
            if('selectFreq'==action || 'selectFreq2'==action){
            	if('selectFreq'==action){
            		if(typeof data.timeslot !== "undefined" && typeof data.timeslot.soFreq !== "undefined" && data.timeslot.soFreq !== null){
      		      		//document.getElementById("soFreq").selectedIndex = data.timeslot.soFreq;
      		      		document.getElementById("soFreq2").selectedIndex = data.timeslot.soFreq;
            		}
            		if(typeof data.soId !== "undefined" && data.soId !== null){
                		getSOData(data.soId, action);
            		}
            	} else if('selectFreq2'==action){
             	    window.FreshDirect.expressco.data = data;
             		FreshDirect.common.dispatcher.signal('timeslot', FreshDirect.expressco.data.timeslot);
             		if(typeof data.standingOrderResponseData !== "undefined" && typeof data.standingOrderResponseData.id !== "undefined" && data.standingOrderResponseData.id !== null){
            		 getSOData(data.standingOrderResponseData.id, action);
             		}
            	}
            }
            if('changename'==action){
            	soSaved(id, false, false);
            }
            if('activate'==action){
            	getSOData(id, action);
            }
            if('delete'==action){
            	$jq(soID).remove();
            }
        }
 	});
}

function populateCartContent(){
	if(FreshDirect.expressco.cartcontent !== null){
  		FreshDirect.expressco.cartcontent.listen();
  		FreshDirect.expressco.cartcontent.watchChanges();
  		FreshDirect.expressco.cartcontent.update();
	}
}

function populateDrawer(id){
	FreshDirect.common.dispatcher.signal('drawer', FreshDirect.expressco.data.drawer);
	FreshDirect.common.dispatcher.signal('payment', FreshDirect.expressco.data.payment);
	FreshDirect.common.dispatcher.signal('address', FreshDirect.expressco.data.address);
	FreshDirect.common.dispatcher.signal('timeslot', FreshDirect.expressco.data.timeslot);
	$jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='address'] button.change.cssbutton").on( "click", function(){ hideShopNowButtuns(); isActiveDrawerOpen = true; });
	$jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='timeslot'] button.change.cssbutton").on( "click", function(){ hideShopNowButtuns(); isActiveDrawerOpen = true; });
	$jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='payment'] button.change.cssbutton").on( "click", function(){ hideShopNowButtuns(); isActiveDrawerOpen = true; });
	if(id!=0){
		getSOData( id, "displayShopNow");
		$jq("#ec-drawer").on( "drawer-reset", function(){
			if(isActiveDrawerOpen){
				isActiveDrawerOpen = false;
				getSOData( id, "displayShopNow");
			}
		});
	}
}

function soItemTriggerUpdate(id, data, isCartUpdate){
	var soID = "#soid_" + id;
	if(isCartUpdate){
		$jq(soID).addClass("cart-saved");
		$jq(soID + ' .standing-orders-3-so-settings-drawer-cart .standing-orders-3-so-settings-drawer-cart-container .standing-orders-3-so-settings-drawer-cart-container-wrap .standing-orders-3-saved-container-cart').html($jq(soID + " .standing-orders-3-saved-container").html());
		setTimeout(function(){
			getSOData(id, "soItemUpdate");
			if(data.standingOrderResponseData.activate){
    			$jq(soID + " .standing-orders-3-so-settings-activate").addClass("open");
	      	}
		}, 7000);
	} else {
		$jq(soID).addClass("drawer-saved");
		getSOData(id, "soItemUpdate");
		if(data.standingOrderResponseData.activate){
			$jq(soID + " .standing-orders-3-so-settings-activate").addClass("open");
      	}
	}
}

function getSOData(id, action){
	var soID = "#soid_" + id;
	var dataString = "soId=" + id;
	var activatedAndHasAddress;
	$jq.ajax({
        url: '/api/manageStandingOrder',
        type: 'GET',
        data: dataString,
        success: function(data){
        	if('selectFreq'==action || 'selectFreq2'==action){
        		soSaved(id, data.activated, false);

        		if(data.frequencyDesc !== null && data.dayOfWeek !== null){
        			$jq(soID + " .standing-orders-3-so-settings-item .standing-orders-3-so-settings-item-details .standing-orders-3-so-settings-item-details-frequency").html(data.frequencyDesc + " " + data.dayOfWeek + " /");
        			if(FreshDirect.standingorder.currentPage != "new"){
        				updateSOItem(id, data);
        			}
        		}
        	}
        	if('activate'==action){
            	$jq(soID + " .standing-orders-3-so-settings-activate .standing-orders-3-so-settings-activate-button").remove();
            	$jq(soID + " .standing-orders-3-so-settings-activate .standing-orders-3-so-settings-activate-message .standing-orders-3-so-settings-activate-message-date").html("Your Standing Order has been placed.");
            	$jq(soID + " .standing-orders-3-so-settings-activate .standing-orders-3-so-settings-activate-message .standing-orders-3-so-settings-activate-message-text").html("First Delivery: " + data.dayOfWeek + ", " + data.shortDeliveryDate);
            	setTimeout(function(){
            		$jq(soID + " .standing-orders-3-so-settings-activate").remove();
					closeSOSettings();
				}, 3000);
            	window.location.href = "/expressco/success.jsp?soId=" + id;
            	updateSOItem(id, data);
            }
        	if('soItemUpdate'==action){
        		if(data.activated && data.deliveryDate !=  null){
        			activatedAndHasAddress = true;
        		} else {
        			activatedAndHasAddress = false;
        		}
        		soPlaceOrderDisplay(data.amount);
        		soSaved(id, activatedAndHasAddress, false);
        		updateSOItem(id, data);
        	}
        	if('displayShopNow'==action){
        		if(data.displayCart){
        			showShopNowButtuns();
        		} else {
        			hideShopNowButtuns();
        		}
        	}
        }
 	});
}

function selectFrequency(item){
	if($jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='timeslot'] button.cancel.cssbutton").css("display") == "none"){
		$jq(".standing-orders-3 #ec-drawer .drawer-header li[data-drawer-id='timeslot'] button.change.cssbutton").click();
	}
	freq = item.value;
	
	var soId = '', soName = '';
	soId = $jq(item).closest('.standing-orders-3-so-settings-container.open').find('.standing-orders-3-so-settings-id-value').val();
	soName = $jq(item).closest('.standing-orders-3-so-settings-container.open').find('.standing-orders-3-so-settings-name-value').val();
	
	if(FreshDirect.standingorder.currentPage == "new"){
		submitFormManageSO("","selectFreq2",null,freq);
	}
	if(FreshDirect.standingorder.currentPage == "manage"){
		submitFormManageSO(soId,"selectFreq2",soName,freq);
	}
}

function activateSo(id){
	$jq(".standing-orders-3-so-settings-activate-button").prop("disabled", true);
	submitFormManageSO(id,"activate",null,null);
}

function updateSOItem(id, data){
	var soID = "#soid_" + id;
	var softLimit = FreshDirect.standingorder.softLimitDisplay || "50";
	$jq(soID + " .standing-orders-3-so-settings-item").html(standingorder.standingOrderSettingsItem({item:data, softLimit:softLimit}));

}

function hideShopNowButtuns(){
	$jq(".standing-orders-3 #cartcontent.show").removeClass("show");
	$jq(".standing-orders-3 .standing-orders-3-create-header .standing-orders-3-new-start-shop").attr('disabled','disabled');
	//$jq(".standing-orders-3 .standing-orders-3-newso-drawer-container .standing-orders-3-newso-shop-buttons-container.show").removeClass("show");
}

function showShopNowButtuns(){
	hideShopNowButtuns();
	if(FreshDirect.standingorder.currentPage === "manage"){
		$jq(".standing-orders-3 #cartcontent").addClass("show");
	} else {
		$jq(".standing-orders-3 .standing-orders-3-create-header .standing-orders-3-new-start-shop").removeAttr('disabled');
		//$jq(".standing-orders-3 .standing-orders-3-newso-drawer-container .standing-orders-3-newso-shop-buttons-container").addClass("show");
	}
}