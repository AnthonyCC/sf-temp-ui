function updateTotal() {
    var total=0;
    var p;
    for(i=0; i<numberOfOrderLines; i++ ){
        p = eval("pricing"+i+".getPrice()");
        if (p!="") {
            total+=new Number(p.substring(1));
        }
    }
    document.forms['qs_cart']["total"].value="$"+currencyFormat(total);
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