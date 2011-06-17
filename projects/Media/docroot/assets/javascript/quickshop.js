function updateTotal() {
	var total = 0;
	var p;

	/* convert single refs into an array if needed */
	if (!window.pricings && (window['pricing0'] && window['pricing0'] !== null)) {
		window['pricings'] = [];
		for (var i = 0; i < numberOfOrderLines; i++ ) {
			if (window['pricing'+i]) {
				window.pricings[i] = window['pricing'+i];

				//define new callback, mind the closures
				window.pricings[i].updatePriceField = (
					function(i) {
						var idx = i;
						return function() {
							if (document.forms['qs_cart'] && document.forms['qs_cart']['estPrice_'+idx]) {
								document.forms['qs_cart']['estPrice_'+idx].value = window.pricings[idx].getPrice();
								updateTotal();
							}
						}
					}
				)(i);

				//and reset callback
				window.pricings[i].setCallbackFunction( window.pricings[i].updatePriceField );

				//and relink setQuanitity (used by onBlur)
				window['pricing'+i].setQuantity = window.pricings[i].setQuantity;
			}
		}
	}

	if (window.pricings) {
		for (var i = 0; i<numberOfOrderLines; i++ ) {
			if (window.pricings[i]) {
				p = window.pricings[i].getPrice();
				if (p !== '') {
					total+=new Number(p.substring(1));
				}
			}else{
				//not loaded all the way, set to call again on window load instead
				Event.observe(window, 'load', function() {
					updateTotal();
				});
				return;
			}
		}
		//make sure helpers load in
		addAlcoholHelpers();
	}
	if (document.forms['qs_cart'] && document.forms['qs_cart']['total']) {
		document.forms['qs_cart']['total'].value='$'+currencyFormat(total);
	}
}

function chgQuickShopQty(idx, delta, min, max, increment) {
	var val = document.forms['qs_cart']['quantity_'+idx].value;
	var qty = parseFloat(val) + delta;
	var round = true;
	if ((delta > 0) && (val == "")) {
		if (delta < min) {
			delta = min;
		}
		qty = delta;
	} else if (isNaN(qty) || (qty < 0)) {
		qty = 0;
		round = false;
	} else if ((qty > 0) && (qty < min) && (delta < 0)) {
		qty = 0;
		round = false;
	} else if ((qty > 0) && (qty < min) && (delta >= 0)) {
		qty = min;
	} else if (qty >= max) {
		qty = max;
	}
	if (round) {
		qty = Math.floor( (qty-min)/increment )*increment + min;
	}
	document.forms['qs_cart']['quantity_'+idx].value = qty;
	if (window.pricings) {
		//if we have the new array
		window.pricings[idx].setQuantity(qty);
	}else if (window['pricing'+idx]) {
		//fallback
		window['pricing'+idx].setQuantity(qty);
	}
}

function viewModify(page, orderId,successInfo) {
	document.forms['qs_cart'].action=page+'?action=modify&orderId='+orderId+'&successPage='+escape(successInfo);
	document.forms['qs_cart'].submit();
}