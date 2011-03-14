/////////////////////////////////////
/// SoftCore(TM) Pricing Engine :-)
/////////////////////////////////////
function Pricing_setCallbackFunction(callbackFunction) {
	this.callback=callbackFunction;
}
function Pricing_setSKU(sku) {
	this.selectedSku = sku;
	this.updatePrice();
}
function Pricing_setQuantity(qty) {
	this.quantity = qty;
	this.updatePrice();
}
function Pricing_setSalesUnit(su) {
	this.salesUnit = su;
	this.updatePrice();
}
function Pricing_setOption(ch, cv) {
	var max = this.options.length;
	for (i=0; i<max; i++) {
		if (this.options[i].charName==ch) {
			this.options[i].charValue=cv;
			this.updatePrice();
			return;
		}
	}
	var o = new Object();
	o.charName = ch;
	o.charValue = cv;
	this.options[max] = o;
	this.updatePrice();
}
function Pricing_updatePrice() {
	if (this.selectedSku!="" && this.salesUnit!="" && this.quantity>0) {
		var p = getConfiguredPrice(this.selectedSku, this.quantity, this.salesUnit, this.options);
		this.price = p.price;
		this.estimatedQuantity = p.salesUnitRatio==null ? null : p.salesUnitRatio.ratio * this.quantity;
	} else {
		this.price = 0.0;
		this.estimatedQuantity = null;
	}
	if (this.callback) this.callback();
}
function Pricing_getPrice() {
	return this.price==0 ? "" : "$" + currencyFormat(this.price);
}
function Pricing_getQuantity() {
	return this.quantity;
}
function Pricing_getEstimatedQuantity() {
	return this.estimatedQuantity==null ? "" : (currencyFormat(this.estimatedQuantity) + " lb");
}
function Pricing() {
	this.useGroupScalePricing = false;
	this.callback = null;
	this.price = 0.0;
	this.estimatedQuantity = null;
	this.selectedSku = "";
	this.salesUnit = "";
	this.quantity = 0;
	this.options = new Array();
	this.setCallbackFunction = Pricing_setCallbackFunction;
	this.setSKU = Pricing_setSKU;
	this.setQuantity = Pricing_setQuantity;
	this.getQuantity = Pricing_getQuantity;
	this.setSalesUnit = Pricing_setSalesUnit;
	this.setOption = Pricing_setOption;
	this.updatePrice = Pricing_updatePrice;
	this.getPrice = Pricing_getPrice;
	this.getEstimatedQuantity = Pricing_getEstimatedQuantity;
}

/////////////////////////////////////
/// Data carriers
/////////////////////////////////////

function MaterialPrice(price, pricingUnit, scaleLowerBound, scaleUpperBound, scaleUnit) {
	this.price = price;
	this.pricingUnit = pricingUnit;
	this.scaleLowerBound = scaleLowerBound;
	this.scaleUpperBound = scaleUpperBound;
	this.scaleUnit = scaleUnit;
}

function CharValuePrice(charName, charValue, price, pricingUnit, applyHow) {
	this.charName = charName;
	this.charValue = charValue;
	this.price = price;
	this.pricingUnit = pricingUnit;
	this.applyHow = applyHow;
}

function SalesUnitRatio(alternateUnit, salesUnit, ratio) {
	this.alternateUnit = alternateUnit;
	this.salesUnit = salesUnit;
	this.ratio = ratio;
}

/////////////////////////////////////
/// Helper methods
/////////////////////////////////////
function currencyFormat(price) {
	if (price.price) { price = price.price; }
	//if the price has a decimal point then make sure two digits appear after it..
	var sPrice = "" + (Math.round(price*100)/100);
	if (isNaN(sPrice)) { sPrice = "0"; }
	var dotIndex = sPrice.indexOf(".");
	if (dotIndex!=-1) {
		if (sPrice.substring(dotIndex+1).length == 1) {
			sPrice = sPrice+"0";
		}
	} else {
		sPrice = sPrice+".00";
	}
	return sPrice;
}

/////////////////////////////////////
/// HardCore(TM) Pricing Engine :-)
/////////////////////////////////////
var _perPricingUnit = 0;
var _perSalesUnit = 1;

function getConfiguredPrice(selectedSkuCode, qty, salesUnit, configuration) {
	var matPrice = calculateMaterialPrice(selectedSkuCode, qty, salesUnit)
	var p = new Object();
	p.price = matPrice.price + calculateCVPrices(selectedSkuCode, qty, salesUnit, configuration);
	p.salesUnitRatio = matPrice.salesUnitRatio;
	return p;
}


function calculateMaterialPrice(selectedSkuCode, qty, salesUnit, selectedIndex) {
	var selectedIndex = selectedIndex || 0;
		if (selectedIndex <= 0) {
			for (var i=0; i<document.materialPricesArray[selectedSkuCode].length; i++) {
				if ( isWithinBounds(qty, document.materialPricesArray[selectedSkuCode][i]) == true ){
					selectedIndex = i;
					break;
				}
			}
		}
	if (hasScales(selectedSkuCode, selectedIndex) == true) {
		return calculateScalePrice(selectedSkuCode, qty, salesUnit, selectedIndex);
	} else {
		return calculateSimplePrice(selectedSkuCode, qty, salesUnit);
	}
}

function calculateScalePrice(selectedSkuCode, qty, salesUnit, selectedIndex) {
	var selectedIndex = selectedIndex || 0;
	var p = new Object();
	
	var scaleUnit = getScaleUnit(selectedSkuCode, selectedIndex);
	var scaledQuantity = qty;
	var obj_salesUnitRatio = null;
	if ( salesUnit != scaleUnit ) {
		// different UOMs, perform conversion
		obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, salesUnit);
		// multiply by ratio
		scaledQuantity = parseFloat(qty) * parseFloat(obj_salesUnitRatio.ratio);
		if ( scaleUnit != obj_salesUnitRatio.salesUnit ) {
			// this is not the scale unit yet, we need another conversion (division)
			obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, scaleUnit);
			scaledQuantity = parseFloat(scaledQuantity) / parseFloat(obj_salesUnitRatio.ratio);
		}
	}
	
	// find pricing condition for quantity (in scaleUnit)
	var obj_materialPrice = findMaterialPriceByQuantity(selectedSkuCode, scaledQuantity);
	
	var pricingQuantity = qty;
	if ( salesUnit != obj_materialPrice.pricingUnit ) {
		// we need a ratio
		obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, salesUnit);
		pricingQuantity = parseFloat(qty) * parseFloat(obj_salesUnitRatio.ratio);
	}

	p.price = parseFloat(pricingQuantity) * parseFloat(obj_materialPrice.price);
	p.salesUnitRatio = obj_salesUnitRatio;
	return p;
}

function calculateGroupScalePrice(selectedSkuCode, grpQty, qty, salesUnit, selectedIndex) {
	var selectedIndex = selectedIndex || 0;
	var p = new Object();
	
	var scaleUnit = getScaleUnit(selectedSkuCode, selectedIndex);
	var scaledQuantity = grpQty;
	var obj_salesUnitRatio = null;
	if ( salesUnit != scaleUnit ) {
		// different UOMs, perform conversion
		obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, salesUnit);
		// multiply by ratio
		scaledQuantity = parseFloat(qty) * parseFloat(obj_salesUnitRatio.ratio);
		if ( scaleUnit != obj_salesUnitRatio.salesUnit ) {
			// this is not the scale unit yet, we need another conversion (division)
			obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, scaleUnit);
			scaledQuantity = parseFloat(scaledQuantity) / parseFloat(obj_salesUnitRatio.ratio);
		}
	}
	
	// find pricing condition for quantity (in scaleUnit)
	var obj_materialPrice = findMaterialPriceByQuantity(selectedSkuCode, scaledQuantity);
	
	var pricingQuantity = qty;
	if ( salesUnit != obj_materialPrice.pricingUnit ) {
		// we need a ratio
		obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, salesUnit);
		pricingQuantity = parseFloat(qty) * parseFloat(obj_salesUnitRatio.ratio);
	}

	p.price = parseFloat(pricingQuantity) * parseFloat(obj_materialPrice.price);
	p.salesUnitRatio = obj_salesUnitRatio;
	return p;
}

function calculateSimplePrice(selectedSkuCode, qty, salesUnit) {
	var p = new Object();
	var obj_materialPrice = findMaterialPrice(selectedSkuCode, salesUnit);
	var obj_salesUnitRatio = null;
	if (obj_materialPrice == null) {
		// not found, we need a ratio
		obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, salesUnit);

		// find pricing condition by pricing unit
		obj_materialPrice = findMaterialPrice(selectedSkuCode, obj_salesUnitRatio.salesUnit);
		p.price = parseFloat(qty) * parseFloat(obj_salesUnitRatio.ratio) * parseFloat(obj_materialPrice.price);
	} else {
		// found, just apply price
		p.price = parseFloat(qty) * parseFloat(obj_materialPrice.price);
	}
	p.salesUnitRatio = obj_salesUnitRatio;
	return p;
}

function calculateCVPrices(selectedSkuCode, qty, salesUnit, configuration) {
	var price = 0.0;
	for (var i=0; i<configuration.length; i++) {
		var chName = "";
		var chValue = "";
		if (configuration[i].charName!=null) {
			chValue = configuration[i].charValue;
			chName = configuration[i].charName;	
		}
		var obj_cvPrice = findCharacteristicValuePrice(selectedSkuCode, chName, chValue);
		if(obj_cvPrice == null) {
			continue;
		}
		if(obj_cvPrice.applyHow == _perSalesUnit) {
			price += parseFloat(qty) * parseFloat(obj_cvPrice.price);

		} else if(obj_cvPrice.applyHow == _perPricingUnit) {
			var obj_salesUnitRatio = findSalesUnitRatio(selectedSkuCode, salesUnit);
			price += parseFloat(qty) * parseFloat(obj_salesUnitRatio.ratio) * parseFloat(obj_cvPrice.price);
		}
	}
	return price;
}


///// various helper methods...

function hasScales(selectedSkuCode, selectedIndex) {
	var selectedIndex = selectedIndex || 0;
	var scaleUnit = document.materialPricesArray[selectedSkuCode][selectedIndex].scaleUnit;
	if (scaleUnit == "") {
		return false;
	} else {
		return true;
	}
}

function getScaleUnit(selectedSkuCode, selectedIndex) {
	var selectedIndex = selectedIndex || 0;
	return document.materialPricesArray[selectedSkuCode][selectedIndex].scaleUnit;

}

function isWithinBounds(scaledQuantity, obj_material_price) {
	scaledQuantity = parseFloat(scaledQuantity);
	if( scaledQuantity >= obj_material_price.scaleLowerBound && scaledQuantity < obj_material_price.scaleUpperBound ) {
		return true;
	}
	return false;
}

function findSalesUnitRatio(selectedSkuCode, salesUnit) {
	for (var i=0; i<document.salesUnitRatiosArray[selectedSkuCode].length; i++) {
		if (salesUnit == document.salesUnitRatiosArray[selectedSkuCode][i].alternateUnit) {
			return document.salesUnitRatiosArray[selectedSkuCode][i];
		}
	}

	dummy = new Object();
	dummy.charName = '';
	dummy.charValue = '';
	dummy.price = 0.0; 
	dummy.pricingUnit = '';
	dummy.applyHow = 0;
	return dummy;
}

function findMaterialPriceByQuantity(selectedSkuCode, scaledQuantity) {
	for (var i=0; i<document.materialPricesArray[selectedSkuCode].length; i++) {
		if ( isWithinBounds(scaledQuantity, document.materialPricesArray[selectedSkuCode][i]) == true ) {
			return document.materialPricesArray[selectedSkuCode][i];
		}
	}
	return null;
}

function findMaterialPrice(selectedSkuCode, pricingUnit) {
	for (var i=0; i<document.materialPricesArray[selectedSkuCode].length; i++) {
		if (pricingUnit == document.materialPricesArray[selectedSkuCode][i].pricingUnit) {
			return document.materialPricesArray[selectedSkuCode][i];
		}
	}
	return null;
}

function findCharacteristicValuePrice(selectedSkuCode, chName, chValue) {
	if (document.cvPricesArray[selectedSkuCode]==null) {
		return null;
	}
	for (var i=0; i<document.cvPricesArray[selectedSkuCode].length; i++) {
		if ( chName==document.cvPricesArray[selectedSkuCode][i].charName && chValue==document.cvPricesArray[selectedSkuCode][i].charValue ) {
			return document.cvPricesArray[selectedSkuCode][i];
		}
	}

	return null;
}

/* load pricing by qty on page load */
document.observe("dom:loaded", function() {
	var priceReset = document.getElementsByName('quantity');
	for (var i = 0; i < priceReset.length; i++) {
		pricing.setQuantity(priceReset[i].value);
	}
});
