/*global expressco, Bacon*/
var FreshDirect = FreshDirect || {};

/*batch remove various known classes of unwanted html element duplicates*/
function template_dupe_cleaner(){
	//an array to be populated by the targeted classes that are desired to have only one member be seen on the page
	var there_can_only_be_one = new Array();

	//loop through elements that are known to be unwanted dupes, identified by particular classnames
	$jq("*[class*='st_label_'], *[class*='st_val_'], .deliveryFeeToolTips").each(function(i) {
		var str = $jq(this).attr("class");
		var re = /(st\_label\_([\w])+|st\_val\_([\w])+|deliveryFeeToolTips)/i;
		var found = str.match(re);

		if(found != null && found[0] != null && there_can_only_be_one.indexOf(found[0]) == -1){
			there_can_only_be_one.push(found[0]);
		} else {
			$jq(this).remove();
		}
	})
}

/* should be called everytime the cartlines soy template is rendered or re-rendered. */
function template_cleanup(){
	var thisOldHTML = "";
	var thisNewHTML = "";
	
	/*unescape soy template escaped html content (like from within a template call parameter) (turn it back into usable html)*/
	if( $jq(".js_unescape").length > 0 ){
		$jq(".js_unescape").each(function(){
			thisOldHTML = $jq(this).html();
			thisNewHTML = thisOldHTML.replace(/\&lt\;/g, "<").replace(/\&gt\;/g, ">");
			
			$jq(this).html( thisNewHTML );
		})
	}
	
	template_dupe_cleaner();
}


function button_enableDisable(buttonId, eORd){ /*String, Boolean*/
	if( $jq(buttonId).length > 0 ){
		if( eORd == true ){ // if this button SHOULD be enabled...
			$jq(buttonId).prop('disabled', false).removeClass("grey").addClass("green"); // enables button
		}else{ // or if not
			$jq(buttonId).prop('disabled', 'disabled').removeClass("green").addClass("grey");  // disables button
		}
	}
}

/*the function fired by hitting that green link within the excessive tip tooltip box */
function populateCustomTipField(maxPossibleTip, e){
	/* place the maximum allowable tip value into the field, based upon subtotal */
	var tipTextBox = e? $jq(e).parents('.cartsection__tax').find(etids.inp_tipTextBox) : $jq(etids.inp_tipTextBox);
	tipTextBox.val( maxPossibleTip ).focus().select();
	
	tip_entered(tipTextBox);
}

/* this forces an entry to be a realistic money entry,
* with all characters except numbers and characters being filtered out and
* only one possible period and also only 2 possible trailing digits after said period
*/
function money_format( input ){
	
	/*lets get rid of multiple dot characters, so we don't have something like '10.2121.121.56.1'*/
	var index = input.indexOf( '.' );
	
	if ( index > -1 ) {
	    input = input.substr( 0, index + 1 ) + 
	            input.slice( index ).replace( /\./g, '' );
	}
	
	/*second pass, replace all characters which are not numbers or periods / dots*/
	input = input.trim().replace(/[^0-9\.]/g,'');
	
	/*third pass*/
	input = input.replace(/(\d)(?=(\d{3})+\.)/g, "$1,");
	
	/*
	 * does this input have a dot / period with MORE than 2 digits afterward?
	 * if so, apply the parseFloat and toFixed functions to it. Otherwise, do not.
	*/
	if( /\.([0-9]{3,})$/.test(input) ){
		var inputFloat = parseFloat(input).toFixed(2);
		
		input = inputFloat.toString();
	}

	return input;
}

/*this code inside needs to potentially be called from standard js functions as well as the soy template js code*/
function tip_entered(tipTextBox){
	/*clean the initial tip amount of unwanted characters or needless extra periods or trailing numbers after the 1st period */
	tipTextBox = tipTextBox? tipTextBox : $jq(etids.inp_tipTextBox);
	var tip = money_format( tipTextBox.val().trim() );
	
	var tipFloat = parseFloat(tip);

	/*re-populate the optional custom tip textfield*/
	$jq(etids.inp_tipTextBox).val( tip );

	var subTotalStr = money_format( $jq('.st_val_subtotal').html() );

	var subTotal = (Math.round(subTotalStr*100)/100).toFixed(2);

	var maximumTipAllowed = subTotal * 0.32;

	var roundedMaxTip = (Math.round(maximumTipAllowed*100)/100).toFixed(2);
	
	//return;
	
	/*if(tip > maximumTipAllowed){*/
	if(tipFloat > roundedMaxTip){ /*APPBUG-4270*/
		$jq(etids.btn_tipApply).prop('disabled', true);

		/*this goes in the hover box for the excessive amount tip*/
		var innerHtml = "<div class='tooltip-inner'><b>That's quite a tip, thank you!</b><br/><p>As of now, we cap all electronic tips at 32% of the subtotal, making the highest allowed tip to be <a href='' onclick='event.preventDefault();populateCustomTipField(" + roundedMaxTip + ", this)'>$" + roundedMaxTip + " for this order.</a></p></div>";

		$jq(etids.div_toolTipTextBox).html('').append(innerHtml);
	    $jq(etids.div_toolTipTextBox).attr('tabindex', '0');
			
		/*hover over the optional tooltip icon and also delivery fee tooltip icon */
		$jq(etids.inp_tipTextBox).on('mouseover mouseenter', function(e){
			if( $jq(etids.div_toolTipTextBox).html().length > 2 ){
				$jq(etids.div_tooltipPopup).addClass("toomuch-etip");
				
				$jq(etids.div_toolTipTextBox).addClass('shown');
			}
		});
		
		$jq(etids.inp_tipTextBox).mouseout(function(){
			$jq(etids.div_tooltipPopup).removeClass("toomuch-etip");
		});
		
		/*if that green tick is seen, then make it not seen */
		$jq( etids.ck_tipAppliedTick ).hide();
			
		/*forcibly show the excessive amount tooltip box */
	    $jq(etids.div_toolTipTextBox).addClass('shown');
	
	    /* set the input field invalid */
	    $jq(etids.inp_tipTextBox).attr('invalid','');
	}else{ /*if the tip is a proper number, including zero */
		$jq(etids.div_toolTipTextBox).html('');
		
		if(tipFloat >= 0 && !isNaN(tip) && (tip.length > 0) ){
			//$jq(etids.btn_tipApply).prop('disabled', false);
			button_enableDisable(etids.btn_tipApply, true);
		}else{
			button_enableDisable(etids.btn_tipApply, false);
		}
			
		$jq(etids.sel_tipDropdown).val('Other Amount');
		
		/*forcibly hide the excessive amount tooltip box */
		$jq(etids.div_toolTipTextBox).removeClass('shown');
		$jq(etids.div_toolTipTextBox).attr('tabindex', '-1');

	    /* remove invalid property from input field */
	    $jq(etids.inp_tipTextBox).attr('invalid',null);
	}
	
	//cartcontent.update();
}

/* APPDEV-4904, get the chosen payment type for the customer */
function get_current_paymenttype_choice(){
	var fd = window.FreshDirect || {};
	
	if( fd.expressco && fd.expressco.data &&
		fd.expressco.data.payment &&
		fd.expressco.data.payment.payments && 
		fd.expressco.data.payment.payments.length ){
		
		for(var i=0; i<fd.expressco.data.payment.payments.length; i++){
			if( fd.expressco.data.payment.payments[i].selected == true ){
				return fd.expressco.data.payment.payments[i].type;
			}
		}
	}
	
	return "not set yet";
}

/* APPDEV-4904, Set the chosen payment type for the customer within the global js object.
NOTE: only affects javascript objects/variables on the current page until the next tab/page refresh or close */
function set_current_payment_choice_JSonly(arr){
	var fd = window.FreshDirect,
		arr = arr || [];
	
	/*first correct the window.FreshDirect version of what payment the customer currently has selected */
	for(var i=0; i<arr.length; i++){
		if(fd.expressco && fd.expressco.data && fd.expressco.data.payment && fd.expressco.data.payment.payments[i] ) {
			if( arr[i].selected == true ){
				FreshDirect.expressco.data.payment.payments[i].selected = true;
			}else{
				FreshDirect.expressco.data.payment.payments[i].selected = false;
			}
		}
	}
	
	/* APPDEV-4904, update the cart data about it's current EBT status */
	if( (fd.cartTemplateObj) && (fd.cartTemplateObj.data) ){
		window.FreshDirect.cartTemplateObj.data.isEBTused = ( get_current_paymenttype_choice() == "EBT")? true : false;
	}
}

/* APPDEV-4904, this standalone function creates the potential to re-render the cart content and associated elements from anywhere else in javascript */
function cart_content_template_htmlstr(){
	if( (window.FreshDirect.cartTemplateObj) && (window.FreshDirect.cartTemplateObj.data) && (window.FreshDirect.cartTemplateObj.processFn) ){
		var data = window.FreshDirect.cartTemplateObj.data;
		
		//window.cartdata = data;
		
		//console.log("data = ", data);
		
		var processFn = window.FreshDirect.cartTemplateObj.processFn;
						
		/*process the soy template, using the data to populate it, then kill certain accidental unwanted repetive elements*/
		return processFn(data) + '<SCR'+'IPT>template_cleanup();<\/SCR'+'IPT>';
	}else{
		return "";
	}
}


/*this object contains the names of elements relevant to etipping */
var etids = new Object();

/*buttons, first for applying the tip, the next just to tell you that you did, doesn't actually do anything when clicked*/
etids.btn_tipApply = ".tipApply";
etids.btn_tipApplied = ".tipApplied";

/*checkmark which shows up after applying tip*/
etids.ck_tipAppliedTick = ".tipAppliedTick";

/*select box to choose what tip you want*/
etids.sel_tipDropdown = ".tipDropdown";

/*hidden by default, until shown by means of choosing from above select box 'other amount'*/
etids.inp_tipTextBox = ".tipTextBox";

/*also hidden by default, a popup word balloon that contains information for the user under certain scenarios, typically when user hovers over a tooltip/information icon*/
etids.div_toolTipTextBox = ".toolTipTextBox";

/*the tooltip popup for too great of a tip*/
etids.div_tooltipPopup = "#tooltipPopup";

function parseTipTotal(data) {
	/*only if etipping is turned on in the properties*/
	if( !data || data.etipTotal == null || typeof data.etipTotal !== 'string'){
		return;
	}
	
	/*APPBUG-4312, make a new var from the etip amount into a float value to compare against zero*/
	var parsedEtipTotal = parseFloat(data.etipTotal.replace(/\$/g, ""));
	
	/*APPBUG-4312, detects custom tip by the actual earlier granted amount, based on whether or not it is part of the list which populates the dropdown */

	/*split the tip list that populates the dropdown into a string array*/
	var tipAmountsArr = data.tipAmounts &&  data.tipAmounts.length? data.tipAmounts : data.tipAmountsStr.split(",");
	
	/*this will hold the value of the current member of the just made array above*/
	var currentArrMem = "";
	
	/*to find out whether or not the current tip amount is actually within the tip dropdown list array*/
	var boolInTipList = false;
	for(var i=0; i<tipAmountsArr.length; i++){
		/*remove the dollar sign from the current member of the array*/
		currentArrMem = tipAmountsArr[i].replace(/\$/g, "");
	    
		/*if the current member IS a number AND the parsedFloated current tip amount equals the parsedFloated current member*/
	    if ( !isNaN(currentArrMem) && (parseFloat(currentArrMem) == parsedEtipTotal) ){
	    	/*so the current tip amount previously entered DOES exist within the dropdown array*/
	    	boolInTipList = true;
	    }
	}
	
	/*if the currently placed tip amount is NOT within the tip dropdown array AND it equals more than 0,
	then declare the customTip property of the data to be true.
	NOTICE: this property could have been true before for other reasons*/
	if( (boolInTipList == false) && (parsedEtipTotal > 0) ){
		data.customTip = true;
	}
	
	/*APPDEV-4887, if there is an etip total, then logically, it should be thought of as having a tip applied */
	if( data.etipTotal && (parsedEtipTotal > 0) ){
		data.tipApplied = true;
	}
	
	/*
	To address bug in which the soy template select box does not correctly recognize the etip amount as being the same as one of its members.
	This makes it so that the amount will only have a decimal place when the digits to the RIGHT of the decimal place are both greater than zero.
	e.g., '$5.02' is still '$5.02', but '$5.00' becomes '$5'

	#APPBUG-4391, We don't need $ sign for manual tip changes
	*/
	return parsedEtipTotal;
}
(function (fd) {
	'use strict';

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
	var DISPATCHER = fd.common.dispatcher;
	var requestCounter = 0;
	var focusedElementId;
	
	//APPBUG-4365
	var currentpagefile = location.pathname.substring(location.pathname.lastIndexOf("/") + 1);

	var cartcontent = Object.create(WIDGET,{
		signal:{
			value:'cartData'
		},
		template:{
			value: function(data){
				window.FreshDirect.cartTemplateObj = window.FreshDirect.cartTemplateObj || new Object();
				
				/*if there is a tip amount, just forcibly make sure that etipping is enabled on the javascript side*/
				if( data.etipTotal && data.etipTotal.length > 0 ){
					var floatDoubleTip = money_format( data.etipTotal.trim() );
					
					if( floatDoubleTip > 0 ){
						data.eTippingEnabled = true;
					}
				}
				
				/*override, turn etipping off for the view_cart page*/
				var path = window.location.pathname;
				var page = path.split("/").pop();

				if( page == "view_cart.jsp" ){
					data.eTippingEnabled = false;
					
					/* add sub total and save amount */
					if( typeof(data.subTotalBox.estimatedTotalBox) == "object" ){
						data.subTotalBox.estimatedTotalBox.forEach(function (obj){
							data.subTotalBox.subTotalBox.push(obj);
						});
					}
					
					/*APPBUG-4365*/
					if( typeof(data.subTotalBox.subTotalBox) == "object"  ){
						for(var j=0; j<data.subTotalBox.subTotalBox.length; j++){
							if( data.subTotalBox.subTotalBox[j]["id"] == "totaltax" ){
								data.subTotalBox.subTotalBox[j]["text"] = "Sales Tax";
							}
						}
					}
					
					/* correct copy for when avalara is enabled in subtotal areas */
					if( typeof(data.subTotalBox.estimatedTotalBox) == "object"  ){
						if(data.avalaraEnabled == true){
							data.subTotalBox.estimatedTotalBox[0]["text"] = "Order Subtotal with rodents";
						}
					}
				}
				
				/*APPBUG-4365*/
				if( page == "checkout.jsp" ){
					
					/*APPBUG-4407*/
					var salesOrTotal = "Sales Tax";
					var tempJ = 0;
					var tempJ2 = 0;
					
					if(data.cartSections && data.cartSections.length > 1){
						for(var i=0; i<data.cartSections.length; i++){
							if( data.cartSections[i].title == "wineSectionKey" ){
								salesOrTotal = "Total Tax";
							}
						}
					}
					
					if( typeof(data.subTotalBox.subTotalBox) == "object"  ){
						for(var j=0; j<data.subTotalBox.subTotalBox.length; j++){
							if( (data.subTotalBox.subTotalBox[j]["id"] == "totaltax") || (data.subTotalBox.subTotalBox[j]["id"] == "totalAvalaratax") ){
								//data.subTotalBox.subTotalBox[j]["text"] = "Total Tax";
								data.subTotalBox.subTotalBox[j]["text"] = salesOrTotal;
							}
							
							if(data.subTotalBox.subTotalBox[j]["id"] == "totaltax"){
								tempJ = j;
							}
							
							if(data.subTotalBox.subTotalBox[j]["id"] == "totalAvalaratax"){
								tempJ2 = j;
							}
						}
						
						//if avalara is present, get rid of the 'totaltax' field
						if(tempJ2 > 0){
							//data.subTotalBox.subTotalBox.splice(tempJ, 1);
						}
					}
				}
				
				$(this.placeholder).trigger('cartData', data);
				
				data.tipAmountsStr = data.tipAmounts.join(",");
				data.mobWeb = fd.mobWeb;
				/* need to change between templates based on data param */
				var lineTemplate = $(this.placeholder).data('ec-linetemplate');
				//var lineTemplate = $( the_placeholder ).data('ec-linetemplate');

				var processFn = fd.modules.common.utils.discover(lineTemplate) || expressco.viewcartlines;
				
				window.FreshDirect.cartTemplateObj.processFn = processFn;

				this.updateTopCheckoutButton(data);

        // GTM related data processing
        if (data.googleAnalyticsData) {
          DISPATCHER.signal('googleAnalyticsData', data.googleAnalyticsData);
        }
				var parsedTipTotal = parseTipTotal(data);
				data.etipTotal = parsedTipTotal == null? data.etipTotal : parsedTipTotal;

				/* APPDEV-4904 */
				data.isEBTused = ( get_current_paymenttype_choice() == "EBT")? true : false;
				
				//make the object for this global
				window.FreshDirect.cartTemplateObj.data = data;
								
				/*process the soy template, using the data to populate it, then kill certain accidental unwanted repetive elements
				 * no arguments sent, as the data it works with is global by now */
				return cart_content_template_htmlstr();
			}
		},
		placeholder:{
			value:'#cartcontent'
		},
		callback: {
			value: function(value){
				this.render(value);
				fd.expressco.checkout.coFlowChecker.checkFlow();
			}
		},
		updateTopCheckoutButton: {
			value: function (data) {
				var $placeholder = $jq('#checkoutbutton_bottom');

				if ($placeholder.size() && expressco.checkoutBanner) {
					$placeholder.html(expressco.checkoutBanner(data));
					$(document).trigger('checkoutFlowImmediate-change');
				}
			}
		},
		serialize:{
			value:function(){
				var data = {},
				changeCounter = 0;

				$(this.placeholder + ' [data-component="cartline"].modified').each(function(){
					var cartlineId = $(this).data('cartlineid'),
					changedComponent = $(this).data('changed'),
					componentType = {
						salesunit: 'csu',
						complaintreason: 'ccr',
						quantitybox: 'cqu'
					},
					changeType, changeValue, changedEl;

					if(changedComponent){
						changedEl = $('[data-component="' + changedComponent + '"]', this);
						changeType = componentType[changedComponent];

						if(changedComponent === 'quantitybox'){
							changeValue = changedEl.quantityBox('value');
						}else{
							changeValue = changedEl.val();
						}

						if(cartlineId && changeType && changeValue){
							data[cartlineId] = { type: changeType, data: changeValue };
							changeCounter++;
						}
					}
				});

				$(this.placeholder + ' [data-component="cartline"].deleted').each(function(){
					var cartlineId = $(this).data('cartlineid');
					data[cartlineId]={type:'rmv',data:null};
					changeCounter++;
				});

				return changeCounter ? data : null;
			}
		},
		onDeleteRecipe: {
			value: function(e) {
				e.preventDefault();
				e.stopPropagation();
				$(e.target).closest('[data-recipe-section]').find('[data-component="cartline"]').addClass('deleted');
				$(cartcontent.placeholder).trigger('cartline-delete');
			}
		},
		onDeleteCartLine: {
			value: function(e) {
				e.preventDefault();
				e.stopPropagation();
				$(e.target).closest('[data-component="cartline"]').addClass('deleted');
				$(cartcontent.placeholder).trigger('cartline-delete');
			}
		},
		onQuantityChange: {
			value: function(e) {
				e.preventDefault();
				e.stopPropagation();
				$(e.target).closest('[data-component="cartline"]').addClass('modified').attr('data-changed', $(e.target).data('component'));
				$(cartcontent.placeholder).trigger('quantity-change');
			}
		},
		update: {
			value: function(e) {
				if(e) {
					e.preventDefault();
					e.stopPropagation();
				}
				$(cartcontent.placeholder).trigger('cartcontent-update');
			}
		},
		getRequestURI: {
			value: function() {
				return $(this.placeholder).data('ec-request-uri') || '/api/expresscheckout/cartdata';
			}
		},
		createRequestConfig: {
			value: function(userData) {
				var reqType = userData && 'POST' || 'GET';
				return {
					data: {
						data: JSON.stringify({
							header: {
								requestCounter: (++requestCounter)
							},
							change: (userData || {}),
							warningMessage: fd.utils.getParameterByName('warning_message'),
							page: currentpagefile
						}),
						fetch: fd.utils.getParameterByName('fetch')  ===  'true'
					},
					url: this.getRequestURI(),
					type: reqType,
					dataType: 'json'
				};
			}
		},
		watchChanges: {
			value: function() {
				var deleteStream = $(cartcontent.placeholder).asEventStream('cartline-delete');
				var updateStream = $(cartcontent.placeholder).asEventStream('cartcontent-update');
				var changeStream = $(cartcontent.placeholder).asEventStream('quantity-change').filter(function() {
					var $el = $('input[name="dontupdatecartlines"]:checked');
					return $el.size() === 0;
				});
				var dataStream = deleteStream.merge(updateStream).merge(changeStream);
				var bouncedDataStream = dataStream.debounce(500);
				var setDirty = dataStream.map(function() {
					return true;
				});
				var setClean = bouncedDataStream.map(function() {
					return false;
				});
				/* an immediate user action should mark the form 'dirty', after some timing it should mark 'clean' */
				var isDirty = setDirty.merge(setClean).toProperty(false);
				var ajaxStream = bouncedDataStream.flatMapLatest(function() {
					var serializedData = cartcontent.serialize();
					var ajax = Bacon.fromPromise($.ajax(cartcontent.createRequestConfig(serializedData)));
					focusedElementId = document.activeElement && document.activeElement.id;
					return ajax;
				});
				ajaxStream.onError(function() {
					$(cartcontent.placeholder).
					html('<p class="error">Something went wrong. Please refresh the page to continue.</p>');
					return false;
				});
				/* if user makes the form dirty before the ajax response comes back then we should not update UI
				   instead send the changed form again after some timing again */
				ajaxStream.filter(isDirty.not()).onValue(function(ajaxData) {
					var $ph = $(cartcontent.placeholder);
					/* check gogreen status */
					if(!$ph.attr('gogreen-status')) {
						$ph.attr('gogreen-status', !!ajaxData.goGreen);
					}
					cartcontent.render(ajaxData);
					if(ajaxData.coremetrics) {
						fd.common.dispatcher.signal('coremetrics', ajaxData.coremetrics);
					}

					fd.common.dispatcher.signal('cartHeader', ajaxData);
					fd.common.dispatcher.signal('checkoutCartHeader', ajaxData);
					fd.common.dispatcher.signal('productSampleCarousel', ajaxData.carouselData);
					fd.common.dispatcher.signal('donationProductSampleCarousel', ajaxData.carouselData);
					if (ajaxData.carouselData && ajaxData.carouselData.recommendationTabs) {
						var selected = ajaxData.carouselData.recommendationTabs.filter(function (tab) { return tab.selected; })[0];
						if (selected && selected.updateContent) {
							fd.common.dispatcher.signal('cartCarousels', ajaxData.carouselData);
						}
					}
					
					if(focusedElementId) {
						try {
							document.getElementById(focusedElementId).focus();
						} catch(e) {}
					}
					fd.expressco.checkout.coFlowChecker.checkFlow();
					try {
						fd.common.transactionalPopup.close();
					} catch(e) {}
					

					/* make apply buttons disabled when input is also empty */
					function toggleDisabled(selector, makeDisabled) {
						var $elems = $(selector);
						if (makeDisabled) {
							$elems.addClass('disabled').prop('disabled', true);
						} else {
							$elems.removeClass('disabled').prop('disabled', false);
						}
					}
					/* update load */
					toggleDisabled('#promotional-code-applybtn,#apply-gift-card-applybtn', true);
					
					$(document).on('keyup change input', '#promotional-code', function(event) {
						toggleDisabled('#promotional-code-applybtn', $(this).val() === '');
					});
					$(document).on('keyup change input', '#apply-gift-card', function(event) {
						toggleDisabled('#apply-gift-card-applybtn', $(this).val() === '');
					});
				});

				template_cleanup();
			}
		},
		onEmptyCart: {
			value: function(e) {
				e.preventDefault();
				e.stopPropagation();
				/* adding class to all cartlines */
				if(confirm('Are you sure that you want to delete all items from your cart?')) {
					$(cartcontent.placeholder + ' [data-component="cartline"]').addClass('deleted');
					$(cartcontent.placeholder).trigger('cartline-delete');
				}
			}
		},
		onChangeETip: {
			value: function(e) {
				e.preventDefault();
				e.stopPropagation();
				$jq(etids.btn_tipApplied).hide();
				$jq(etids.btn_tipApply).show();
				$jq(etids.ck_tipAppliedTick).hide();
				var target = $jq(e.target || etids.sel_tipDropdown);
				if(target.val() === "Other Amount"){
					$jq(etids.sel_tipDropdown).hide();
					$jq(etids.inp_tipTextBox).show();
					$jq(e.currentTarget || e.target).parents('.cartetip-container').find(etids.inp_tipTextBox).focus().select();
					
					/*APPBUG-4963, immediately disable the 'add tip' button, because there is nothing in the blank field */
					/*APPBUG-4219, disable the button if one switches to 'other amount' */
					$jq( etids.btn_tipApply ).prop("disabled", "disabled").removeClass("green").addClass("grey");
				}
			}
		},
		onTipEntered: {
			value: function(e){
				e.preventDefault();
				e.stopPropagation();

				$jq(etids.btn_tipApply).show();
				$jq(etids.btn_tipApplied).hide();

				/*what used to be around here moved to this function*/
				tip_entered($(e.target));
			}
		}
	});

	var cartHeader = Object.create(WIDGET,{
		signal:{
			value:'cartHeader'
		},
		template: {
			value:expressco.cartheader
		},
		placeholder:{
			value:'#cartheader'
	    }
	});
	cartHeader.listen();

	var billingReferenceInfo = Object.create(WIDGET,{
		signal:{
			value:'billingReferenceInfo'
		},
		template: {
			value:expressco.billingReferenceInfo
		},
		placeholder:{
			value:'#billingReferenceInfoContainer'
		}
	});
	billingReferenceInfo.listen();

	var productSampleCarousel = Object.create(WIDGET,{
		signal:{
			value:'productSampleCarousel'
		},
		template: {
			value:expressco.productSampleCarouselWrapper
		},
		placeholder:{
			value:'#productsamplecarousel'
		},
		callback: {
			value: function(value){
				this.render(value); /*everything after this within this function assumes that the soy template has been rendered on the page*/
				fd.components.carousel && fd.components.carousel.initialize();

				/*remove extra e-tip element crap (sorry for this hack solution)*/
				if( $(".cartsection__totalwrapper").length > 1 ){
					$(".cartsection__totalwrapper:first div.subtotalboxes").remove();

					$(".cartsection__totalwrapper:first div.cartsection__tax").remove();
				}

				/*kill certain accidental unwanted repetive elements*/
				template_cleanup();
			}
		}
	});
	productSampleCarousel.listen();

	
	//APPDEV-5516 : Cart Carousel - Grand Giving Donation Technology
	var donationProductSampleCarousel = Object.create(WIDGET,{
		signal:{
			value:'donationProductSampleCarousel'
		},
		template: {
			value:expressco.donationProductSampleCarouselWrapper
		},
		placeholder:{
			value:'#donationProductSampleCarousel'
		},
		callback: {
			value: function(value){
			//	this.render(value); /*everything after this within this function assumes that the soy template has been rendered on the page*/
			//	fd.components.carousel && fd.components.carousel.initialize();

				/*remove extra e-tip element crap (sorry for this hack solution)*/
				if( $(".cartsection__totalwrapper").length > 1 ){
					$(".cartsection__totalwrapper:first div.subtotalboxes").remove();

					$(".cartsection__totalwrapper:first div.cartsection__tax").remove();
				}

				/*kill certain accidental unwanted repetive elements*/
				template_cleanup();
			}
		}
	});
	donationProductSampleCarousel.listen();

	var cartCarousel = Object.create(WIDGET,{
		signal:{
			value:'cartCarousels'
		},
		template: {
			value:common.viewCartTabbedCarousel
		},
		placeholder:{
			value:'#cartCarousels'
		},
		callback: {
			value: function(value){
				this.render(value); /*everything after this within this function assumes that the soy template has been rendered on the page*/
				fd.components.carousel && fd.components.carousel.initialize();

				/*remove extra e-tip element crap (sorry for this hack solution)*/
				if( $(".cartsection__totalwrapper").length > 1 ){
					$(".cartsection__totalwrapper:first div.subtotalboxes").remove();

					$(".cartsection__totalwrapper:first div.cartsection__tax").remove();
				}

				/*kill certain accidental unwanted repetive elements*/
				template_cleanup();
			}
		}
	});
	var checkoutCartHeaderData = {
			deliveryFee: null,
			tip: null,
			estTotal: null,
			estTotalLegend: null,
			customTip: null,
			isEBTused: null,
			etipTotal: null,
			tipAmounts: null,
			tipApplied: null,
			tipAppliedTick: null,
			userRecognized: null,
			userCorporate: null,
			mobWeb: null,
			saveAmount: null,
			modifyOrder: null
		};
	var checkoutCartHeader = Object.create(WIDGET,{
		signal:{
			value: ['cartData','subTotalBox','checkoutCartHeader']
		},
		headerData: {
			value: checkoutCartHeaderData
		},
		template: {
			value: function(data) {
				var subTotalBox = data.subTotalBox;
				if (subTotalBox && subTotalBox.subTotalBox && subTotalBox.subTotalBox.length) {
					// APPDEV-7055, do not show delivery fee if displayDeliveryFeeForCosUserInHeader is false and user is COS
					var displayDeliveryFee = !subTotalBox.userCorporate || (fd && fd.properties && fd.properties.displayDeliveryFeeForCosUserInHeader);
					if (displayDeliveryFee) {
						subTotalBox.subTotalBox.forEach( function(box) {
							if (box.id === 'deliveryfee') {
								var showDeliveryPass = box.other && box.other.deliveryPassPopupNeeded;
								var freeWithDeliveryPass = false;
								var deliveryFeeValue = box.value;
								if (!showDeliveryPass && box.value && box.value == 'FREE with DeliveryPass') {
									deliveryFeeValue = '<div><span class="delivery-free-with-label">Free with</span><div class="delivery-pass-label">DeliveryPass</div></div>';
									freeWithDeliveryPass = true;
								}
								checkoutCartHeaderData.deliveryFee = [{id: 'checkout-cart-header-'+box.id+(showDeliveryPass? '' :freeWithDeliveryPass?'-with-delivery-pass':'-no-action'), text: box.text, value: deliveryFeeValue, other: (freeWithDeliveryPass? null : box.other)}];
							}
						});
					} else {
						checkoutCartHeaderData.deliveryFee = null;
					}
					checkoutCartHeaderData.userCorporate = subTotalBox.userCorporate;
					checkoutCartHeaderData.userRecognized = subTotalBox.userRecognized;
				}
				
				if (subTotalBox && subTotalBox.estimatedTotalBox && subTotalBox.estimatedTotalBox.length) {
					subTotalBox.estimatedTotalBox.forEach( function(box) {
						if (box.id === 'ssOrderTotal') {
							checkoutCartHeaderData.estTotal = box.value;
							checkoutCartHeaderData.estTotalLegend = (box.other && box.other.mark) || '';
						} else if (box.id === 'youSaved') {
							checkoutCartHeaderData.saveAmount = box.value;
						}
					});
				}
				
				if (data.eTippingEnabled && data.tipAmounts) {
					checkoutCartHeaderData.etipTotal = parseTipTotal(data) || data.etipTotal;
					checkoutCartHeaderData.tipAmounts = data.tipAmounts;
					checkoutCartHeaderData.tipApplied = data.tipApplied;
					checkoutCartHeaderData.isEBTused = data.isEBTused;
					checkoutCartHeaderData.customTip = data.customTip;
					checkoutCartHeaderData.tipAppliedTick = data.tipAppliedTick;
				}
				checkoutCartHeaderData.mobWeb = fd.mobWeb;
				checkoutCartHeaderData.modifyOrder = checkoutCartHeaderData.modifyOrder || data.modifyOrder;
				
				return expressco.checkoutCartHeader(checkoutCartHeaderData);
			}
		},
		placeholder:{
			value:'#checkout-cart-header'
		},
		callback: {
			value: function(value, signal){
				if (signal === 'subTotalBox')
					value = {subTotalBox:value};
				
				this.render(value);
				//fix duplicate ids
                var $elem = $('#tt_optionalToolTip:first');
                var newId = $elem.attr('id')+'_new';
                $elem.attr('id', newId);
                $elem.parent().attr('aria-describedby', newId);
				template_cleanup();
			}
		}
	});
	checkoutCartHeader.listen();
	
	var atcHandler = Object.create(fd.common.signalTarget, {
		signal: {
			value: 'atcResult'
		},
		callback: {
			value: function (data){
				cartcontent.update();
				/* close delivery pass popup */
				if(fd.expressco.deliverypasspopup){
					fd.expressco.deliverypasspopup.close();
				}
			}
		}
	});

	var subtotalbox = Object.create(WIDGET, {
		signal: {
			value: 'subTotalBox'
		},
		template: {
			value: expressco.subTotalBox
		},
		placeholder: {
			value: '#subtotalbox1'
			/*value: '.subtotalboxes'*/
		}
	});

	cartcontent.listen();
	cartcontent.watchChanges();
	cartcontent.update();

	atcHandler.listen();
	subtotalbox.listen();

	$(document).on('click', cartcontent.placeholder + ' .cartline-delete',
		cartcontent.onDeleteCartLine.bind(cartcontent));

	$(document).on('click', cartcontent.placeholder + ' [data-component="removeRecipeButton"]',
		cartcontent.onDeleteRecipe.bind(cartcontent));

	$(document).on('quantity-change', cartcontent.placeholder + ' [data-component="cartline"]',
		cartcontent.onQuantityChange.bind(cartcontent));

	$(document).on('change', cartcontent.placeholder + ' [data-component="cartline"] select',
		cartcontent.onQuantityChange.bind(cartcontent));

	$(document).on('click', '#questions [data-component="emptycart"]',
		cartcontent.onEmptyCart.bind(cartcontent));

	$(document).on('click', cartcontent.placeholder + ' [data-component="updatecart"]',
		cartcontent.update.bind(cartcontent));

	$(document).on('click', '[data-component="update-cart"]', function () {
		$(cartcontent.placeholder).trigger('cartcontent-update');
	});

	$(document).on('change','[data-component="changeETip"]',
		cartcontent.onChangeETip.bind(cartcontent));

	$(document).on('input','[data-component="tooltip"]',
		cartcontent.onTipEntered.bind(cartcontent));

	fd.modules.common.utils.register('expressco', 'cartcontent', cartcontent, fd);
	fd.modules.common.utils.register('expressco', 'checkoutCartHeader', checkoutCartHeader, fd);
}(FreshDirect));
