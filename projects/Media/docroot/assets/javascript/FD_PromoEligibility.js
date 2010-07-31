

function checkPromoEligibilityByAddress(promotion){
	if("not null"==promotion){	
		var selectAddressList1 = document.getElementsByName('selectAddressList');
		var selectAddressList ="";
		for(var i =0;i<=selectAddressList1.length;i++){
	
				
				if(selectAddressList1[i].checked==true){
					selectAddressList = selectAddressList1[i];
					break;
				}
		}
		new Ajax.Request('/checkout/promo_eligibility.jsp', {
			parameters: {
				isByAddress: true,
				
				selectAddressList: selectAddressList.value						
			},
			onComplete: function(transport) {
				checkPromoPopup(transport.responseText);
			}
		});	
			
	    return false;
	}else{
		return true;
	}
}

function checkPromoPopup(JSONstring) {
	
	var params = JSONstring.evalJSON(true);
	
	if (params.status != "ok") {
		//stick values into overlay html		
		$('promoCode').innerHTML = params.promoCode;
		$('more_info').innerHTML = params.promoTerms;
		
		//$('MB_frame').style.border = 'none';
		Modalbox.show($('gcResendBox'), {
			loadingString: 'Loading Preview...',
			title: '',
			overlayOpacity: .85,
			overlayClose: false,
			width: 225,
			transitions: false,
			autoFocusing: false,
			centered: true,
			afterLoad: function() { $('MB_frame').style.border = 'none'; window.scrollTo(0,0); },
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
		})

	}
	else{
		document.forms['step1Form'].submit();
	}

}


function checkPromoEligibilityByTimeSlot(promotion){	
	if("not null"==promotion){	
		var deliveryTimeslotId1 = document.getElementsByName('deliveryTimeslotId');
		var deliveryTimeslotId ="";
		for(var i =0;i<=deliveryTimeslotId1.length;i++){
	
				
				if(deliveryTimeslotId1[i].checked==true){
					deliveryTimeslotId = deliveryTimeslotId1[i];
					break;
				}
		}
		new Ajax.Request('/checkout/promo_eligibility.jsp', {
			parameters: {
				isByTimeSlot: true,
				hasDiscountedTimeslots: hasDiscountedTimeslots.value,
				deliveryTimeslotId: deliveryTimeslotId.value						
			},
			onComplete: function(transport) {
				checkPromoTimeSlotPopup(transport.responseText);
			}
		});	
			
	    return false;
	}else{
		return true;
	}
}

function checkPromoTimeSlotPopup(JSONstring) {
	
	var params = JSONstring.evalJSON(true);		
	
	if (params.status != "ok") {
		//stick values into overlay html		
		$('promoCode').innerHTML = params.promoCode;
		$('more_info').innerHTML = params.promoTerms;

		Modalbox.show($('gcResendBox'), {
			loadingString: 'Loading Preview...',
			title: '',
			overlayOpacity: .85,
			overlayClose: false,
			width: 225,
			transitions: false,
			autoFocusing: false,
			centered: true,
			afterLoad: function() { $('MB_frame').style.border = 'none'; window.scrollTo(0,0); },
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
		})

	}
	else{
		document.forms['step2Form'].submit();
	}

}
function checkPromoEligibilityByPayment(promotion){
	if("not null"==promotion){	
		var paymentMethodList1 = document.getElementsByName('paymentMethodList');
		var paymentMethodList ="";
		for(var i =0;i<=paymentMethodList1.length;i++){
	
				
				if(paymentMethodList1[i].checked==true){
					paymentMethodList = paymentMethodList1[i];
					break;
				}
		}
		new Ajax.Request('/checkout/promo_eligibility.jsp', {
			parameters: {
				isByPayment: true,
				paymentMethodList: paymentMethodList.value						
			},
			onComplete: function(transport) {
				checkPromoPaymentPopup(transport.responseText);
			}
		});	
			
	    return false;
	}else{
		return true;
	}
}

function checkPromoPaymentPopup(JSONstring) {
	
	var params = JSONstring.evalJSON(true);		
	
	if (params.status != "ok") {
		//stick values into overlay html		
		$('promoCode').innerHTML = params.promoCode;
		$('more_info').innerHTML = params.promoTerms;

		Modalbox.show($('gcResendBox'), {
			loadingString: 'Loading Preview...',
			title: '',
			overlayOpacity: .85,
			overlayClose: false,
			width: 225,
			transitions: false,
			autoFocusing: false,
			centered: true,
			afterLoad: function() { $('MB_frame').style.border = 'none'; window.scrollTo(0,0); },
			afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
		})

	}
	else{
		document.forms['step_3_choose'].submit();
	}

}
			