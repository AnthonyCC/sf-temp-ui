FreshDirect.standingorder = FreshDirect.standingorder || {};
FreshDirect.standingorder.USQPopupOpen = false;
FreshDirect.standingorder.isItSOAlcoholPopup = false;
FreshDirect.standingorder.alcoholVerified = "N";
FreshDirect.standingorder.healthWarningCookie = null;
/* refactor these addToSo calls to make them all one */
function addToSoEvenBetter($clickedButton) {
	var that = $clickedButton;
	var ids = $jq(that).parent().find('.so-select').val().split(':');
	var element = $jq('#evenBetterPopup .pdp-evenbetter-atc');
	AddProductToSO(element, ids, that);
}

function addToSoCustomize($clickedButton) {	
	var that = $clickedButton;	
	var ids = $jq(that).parent().find('.so-select').val().split(':');
	var element = $jq(that).closest('form[fdform="customize"]');
	AddProductToSO(element, ids, that);
}

$jq('button[data-component="addToSOButton"]').on('click', function() {	
	var that = this;
	var ids = $jq('.so-select').val().split(':');
	var element = $jq('.pdp-productconfig');
	AddProductToSO(element, ids, that);
});

function AddProductToSO(element, ids, that){
	if(!FreshDirect.user.recognized && !FreshDirect.user.guest){
		if(FreshDirect.components.AddToCart.requiredValidator(FreshDirect.modules.common.productSerialize(element, true, true))){
			$jq.post('/api/standingOrderCartServlet',
				{
					data: JSON.stringify({
						actiontype: 'AddProductToSO',
						standingOrderId: ids[0],
						alcoholVerified: FreshDirect.standingorder.alcoholVerified,
						listId: ids[1],
						items: FreshDirect.components.AddToCart.atcFilter(
							FreshDirect.modules.common.productSerialize(element)
						)
					})
				},
				function(data) {
					addToSoSuccessHandler(that, data);
				}
			).fail(function(data) {
				addToSoErrorHandler(data);
			});
		}
	}
}

function healthWarningOnClick(accept){	
	if(accept){
		$jq('button[data-component="addToSOButton"]').trigger("alcohol-accepted");
		FreshDirect.modules.common.utils.createCookie('freshdirect.healthwarning','1@'+FreshDirect.USQLegalWarning.getJSessionId());
	}
	if($jq("#USQPopup").hasClass("soShow")){
		FreshDirect.standingorder.isItSOAlcoholPopup = true;
		if(FreshDirect.USQWarning.Popup.isOpen()){
			FreshDirect.USQWarning.Popup.close();
		}
	}
}

$jq(document).on('click','[data-component="ATCButton"]', function() {
	if(FreshDirect.hasOwnProperty("standingorder")){
		FreshDirect.standingorder.isItSOAlcoholPopup = false;
	}
});

function addToSoSuccessHandler($contextElem, data) {
	FreshDirect.standingorder.alcoholVerified = "N";
	if (!data.success) {
		return addToSoErrorHandler(data);
	}
	if(data.alcohol){
		FreshDirect.standingorder.healthWarningCookie = FreshDirect.modules.common.utils.readCookie("freshdirect.healthwarning");
		FreshDirect.standingorder.USQPopupOpen = true;
		FreshDirect.USQWarning.Popup.open();
		$jq('button[data-component="addToSOButton"]').on("alcohol-accepted", function(){
			if($jq("#USQPopup").hasClass("soShow") && FreshDirect.USQWarning.Popup.isOpen()){				
				FreshDirect.standingorder.alcoholVerified = "Y";
				$jq($contextElem).click();
				FreshDirect.USQWarning.Popup.close();
			}
		});
		return false;
	}

	if(FreshDirect.USQWarning.Popup.isOpen()){
		FreshDirect.USQWarning.Popup.close();
	}
	if(!FreshDirect.standingorder.healthWarningCookie){		
		FreshDirect.modules.common.utils.eraseCookie("freshdirect.healthwarning");
	}
	
	var $soResultsCont = $jq($jq($contextElem).closest('.so-container')).find('.so-results-content');

	$soResultsCont.find('.so-results-addedTo').html('Added to: <a href="/quickshop/standing_orders.jsp?soid='+data.id+'#soid_'+data.id+'">'+data.name+'</a>');
	$soResultsCont.find('.so-results-items-total').html(data.productCount+', '+'<span class="total">$'+data.amount+'</span>');
	$soResultsCont.find('.so-results-changes-required').html(data.message);
	
	$jq('#customizePopup').addClass('so-review-success'); 
	$jq('#customizePopup .so-review-header').text("Added to");
	$jq('#customizePopup .so-review-date').text(data.name);
	$jq('#customizePopup .so-review-link').html('<a href="/quickshop/standing_orders.jsp?soid='+data.id+'#soid_'+data.id+'">See Order Details</a>');
	$jq('#customizePopup .so-listadd-content .cssbutton[data-component="addToSOButton"]').remove();
	if(data.reminderOverlayForNewSo){
		$jq('#customizePopup .so-review-selected').before('<button type="button" onclick="reviewSOOkHandler()" class="okReviewSOButton cssbutton cssbutton-flat green nontransparent">Ok</button>');
		$jq('#customizePopup a.so-review-min-not-show-text-link').attr('href','/quickshop/standing_orders.jsp?soid='+data.id+'#soid_'+data.id);
		$jq('#customizePopup a.so-review-min-not-show-go-so').attr('href','/quickshop/standing_orders.jsp?soid='+data.id+'#soid_'+data.id);
	} else {
		$jq('#customizePopup .so-review-selected').before('<button type="button" data-popup-control="close" class="okReviewSOButton cssbutton cssbutton-flat green nontransparent">Ok</button>');
	}
	
	$soResultsCont.toggleClass('so-close');
	
	function soResultsClose() {
		$soResultsCont.addClass('so-close');
	}
	
	window.setTimeout(soResultsClose, 3000);
}

function addToSoErrorHandler(data) {
	switch (data.error) {
		case 'Session Expired':
			//call session expired: refresh overlay functionality
			FreshDirect.common.dispatcher.signal('errorDialog', {message: '<div class="unauthorized">Session expired, please refresh!</div>'});
			break;
		default:
			if (data.hasOwnProperty('error') && data.hasOwnProperty('message')) {
				console.log('AddToSo Error', data.error, data.message);
			} else {
				console.log('AddToSo Error', data);
			}
	}
	
}

$jq('.so-toggler').on('click', function(e) {
	e.stopPropagation();
	$jq(this).parent('.so').toggleClass('so-close'); //toggle before setting
	var isOpen = !$jq(this).parent('.so').hasClass('so-close'); //has = closed, so send opposite bool

	$jq.ajax({
		url: '/api/manageStandingOrder',
		type: 'POST',
		data: "action=setOpenStatus&setOpenStatusTo="+isOpen,
		success: function(data) {
		},
		fail: function(data) {
			console.log('error: so-toggler', data);
		}
	});

	return false;
});

$jq('.so-test-added-toggler').on('click', function(e) {
	e.stopPropagation();
	$jq(this).closest('.so-container').find('.so-results-content').toggleClass('so-close');
	
	function soResultsClose() {
		$jq('.so-results-content').addClass('so-close');
	}
	window.setTimeout(soResultsClose, 3000);
	return false;
});

$jq('.pdp-evenbetter-soPreShow .cssbutton[data-component="showSOButton"]').on('click', function(e) {
	e.stopPropagation();
	$jq(this).closest('.pdp-evenbetter-soPreShow').toggleClass('pdp-evenbetter-soPreShow pdp-evenbetter-soShow');
	return false;
});


$jq('.cssbutton[data-component="createSOButton"]').on('click', function(e) {
	e.stopPropagation();
	window.location = "/quickshop/standing_orders.jsp";
	return false;
});

function disableAlertMinMetSO(){
	$jq('#customizePopup.so-review-min-met-alert #so-min-do-not-show-checkbox:checked').prop( "disabled", true );
	postStandingOrderData($jq('#customizePopup .so-select').val().split(':')[0],'turnOffReminderOverlay');
}

function disableAccidentalAtcSO(){
	$jq('.so-accidental-atc #so-accidental-atc-checkbox:checked').prop( "disabled", true );
	postStandingOrderData(0,'turnOffCartOverlayFirsttime');
}

function addToSONextHandler() {
	if(FreshDirect.components.AddToCart.requiredValidator(FreshDirect.modules.common.productSerialize($jq('#customizePopup form[fdform="customize"]'), true, true))){
		$jq('#customizePopup').addClass('so-review');
		getStandingOrderData($jq('#customizePopup .so-select').val().split(':'),'deliveryBegins');
		$jq('#customizePopup .so-review-selected').text($jq('#customizePopup .so-select option:selected').text());
		if($jq('#customizePopup .skucontrol-quantity input.qty').val() == 1){
			itemQuantity = $jq('#customizePopup .skucontrol-quantity input.qty').val() + ' item';
		} else {
			itemQuantity = $jq('#customizePopup .skucontrol-quantity input.qty').val() + ' items';
		}
		$jq('#customizePopup .rightColumn').append('<div class="so-review-item-total">' + itemQuantity + ' (' + $jq('#customizePopup .skucontrol-quantity .subtotal .subtotal-inner').text() + ')</div>');
	}
};

function reviewSOOkHandler(){
	$jq('#customizePopup').addClass('so-review-min-met-alert');
}

function getStandingOrderData(ids, action){
	var dataString = "soId=" + ids[0];
	$jq.ajax({
        url: '/api/manageStandingOrder',
        type: 'GET',
        data: dataString,
        success: function(data){
        	if('deliveryBegins'==action){
        		if(data.lastError=="MINORDER"){
        			var totalAfterAdd = $jq("#customizePopup .subtotal-inner").text().replace(/\$/g, '');
        			totalAfterAdd = Number(totalAfterAdd) + data.amount;
        			if(totalAfterAdd > data.soSoftLimit){
        				$jq('#customizePopup .so-review-date').html('<b>Congratulations!</b><br/>Add this item, and the $' + data.soSoftLimit + ' order minimum will be met.').addClass('so-review-date-add');
        			} else{
        				$jq('#customizePopup .so-review-date').text('Order Minimum Not Met').addClass('so-review-date-red');
            			$jq('#customizePopup .so-review-min-details').addClass('show');
        			}
        		} else{
        			$jq('#customizePopup .so-review-date').text(ids[2] + ', ' + ids[3]);
        			$jq('#customizePopup .so-review-selected').addClass('show');
        		}
        		$jq('#customizePopup .so-listadd-content .cssbutton[data-component="addToSOButton"]').addClass('show');
        	}
        }
 	});
}

function postStandingOrderData(id, action){
	$jq.post('/api/standingOrderCartServlet',
			{		data: JSON.stringify({
					actiontype: action,
					standingOrderId: id,
					})
			},function(data){
				if(action == "turnOffCartOverlayFirsttime"){
					FreshDirect.standingorder.isSoCartOverlayFirstTime = false;
				}
	})	
}