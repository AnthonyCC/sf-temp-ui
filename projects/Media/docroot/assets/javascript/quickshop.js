function updateTotal() {
	var i, p, total=0, totalSpan = document.getElementById('notYetAvailableOrderTotalPrice');
	for(i=0; i<numberOfOrderLines; i++ ){
		try{
			p = eval("pricing"+i).getPrice();
			if (p!="") {
				total+=new Number(p.substring(1));
			}
		} catch (e){
			//"pricing"+i does not exist due to unavailability
		}
	}
	total = "$"+currencyFormat(total);
	document.forms['qs_cart']["total"].value=total;
	if(totalSpan) {
		totalSpan.innerHTML = total;
	}
}

function chgQuickShopQty(idx, delta, min, max, increment) {
	var val = eval('document.qs_cart.quantity_'+idx+'.value');
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
	eval("document.qs_cart.quantity_"+idx+".value = qty");
	eval("pricing"+idx+".setQuantity(qty)");
}

function viewModify(page, orderId,successInfo) {
    document.forms['qs_cart'].action=page+'?action=modify&orderId='+orderId+'&successPage='+escape(successInfo);
    document.forms['qs_cart'].submit();
}