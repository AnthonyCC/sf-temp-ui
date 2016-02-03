/*global expressco, Bacon*/
var FreshDirect = FreshDirect || {};

/*kills unwanted dupes, typically resulting from bad logic in a loop within a soy file*/
function dupe_buster(css_classname, id_prefix){
	if( $jq(css_classname).length > 1 ){
		$jq(css_classname).each(function(index){
			$jq(this).attr("id", id_prefix+"_"+index);

			if( index > 0 ){
				$jq("#"+id_prefix+"_"+index).remove();
			}
		})
	}
}

/*batch remove various known classes of unwanted html element duplicates*/
function template_dupe_cleaner(){
	/*an array of css classes to hunt for and kill duplicates of*/
	var common_dupe_classnames = new Array("deliveryFeeToolTips", "st_label_deliveryfee", "st_val_deliveryfee", "st_label_subtotal", "st_val_subtotal", "st_label_totaltax", "st_val_totaltax", "st_label_statebottledeposit", "st_val_statebottledeposit", "st_label_ssOrderTotal", "st_val_ssOrderTotal");
	
	/*kill certain accidental unwanted repetitive elements*/
	for(var i=0; i<common_dupe_classnames.length; i++){
		dupe_buster("."+common_dupe_classnames[i], common_dupe_classnames[i]);
	}
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

/*the function fired by hitting that green link within the excessive tip tooltip box */
function populateCustomTipField(maxPossibleTip){
	/* place the maximum allowable tip value into the field, based upon subtotal */
	$jq(etids.inp_tipTextBox).val( maxPossibleTip );

	tip_entered();
}

/* this forces an entry to be a realistic money entry,
* with all characters except numbers and characters being filtered out and
* only one possible period and also only 2 possible trailing digits after said period
*/
function money_format( input ){
	
	console.log("money input with : " + input);
	
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
function tip_entered(){
	/*clean the initial tip amount of unwanted characters or needless extra periods or trailing numbers after the 1st period */
	var tip = money_format( $jq(etids.inp_tipTextBox).val().trim() );
	
	var tipFloat = parseFloat(tip);

	/*re-populate the optional custom tip textfield*/
	$jq(etids.inp_tipTextBox).val( tip );

	var subTotalStr = money_format( $jq('#hiddenSubTotal').val().trim() );

	var subTotal = (Math.round(subTotalStr*100)/100).toFixed(2);

	var maximumTipAllowed = subTotal * 0.32;

	var roundedMaxTip = (Math.round(maximumTipAllowed*100)/100).toFixed(2);
	
	/*if(tip > maximumTipAllowed){*/
	if(tipFloat > roundedMaxTip){ /*APPBUG-4270*/
		$jq(etids.btn_tipApply).prop('disabled', true);

		/*this goes in the hover box for the excessive amount tip*/
		var innerHtml = "<div class='tooltip-inner'><b>That's quite a tip, thank you!</b><br/><p>As of now, we cap all electronic tips at 32% of the subtotal, making the highest allowed tip to be <a href='javascript:populateCustomTipField(" + roundedMaxTip + ")'>$" + roundedMaxTip + " for this order.</a></p></div>";

		$jq(etids.div_toolTipTextBox).html('').append(innerHtml);
		
		/*hover over the optional tooltip icon and also delivery fee tooltip icon */
		$jq(etids.inp_tipTextBox).on('mouseover mouseenter', function(e){
			if( $jq(etids.div_toolTipTextBox).html().length > 2 ){
				$jq(etids.div_tooltipPopup).addClass("toomuch-etip");
				
				$jq(etids.div_toolTipTextBox).show();
			}
		});
		
		$jq(etids.inp_tipTextBox).mouseout(function(){
			$jq(etids.div_tooltipPopup).removeClass("toomuch-etip");
		})
		
		/*if that green tick is seen, then make it not seen */
		$jq( etids.ck_tipAppliedTick ).hide();
		
		console.log("LINE 103");
		
		/*forcibly show the excessive amount tooltip box */
		$jq(etids.div_toolTipTextBox).show();
	}else{ /*if the tip is a proper number, including zero */
		$jq(etids.div_toolTipTextBox).html('');
		$jq(etids.btn_tipApply).prop('disabled', false);
		$jq(etids.sel_tipDropdown).val('Other Amount');
		
		console.log("line 114");
		
		/*forcibly hide the excessive amount tooltip box */
		$jq(etids.div_toolTipTextBox).hide();
	}
}


/*this object contains the names of elements relevant to etipping */
var etids = new Object();

/*buttons, first for applying the tip, the next just to tell you that you did, doesn't actually do anything when clicked*/
etids.btn_tipApply = "#tipApply";
etids.btn_tipApplied = "#tipApplied";

/*checkmark which shows up after applying tip*/
etids.ck_tipAppliedTick = "#tipAppliedTick";

/*select box to choose what tip you want*/
etids.sel_tipDropdown = "#tipDropdown";

/*hidden by default, until shown by means of choosing from above select box 'other amount'*/
etids.inp_tipTextBox = "#tipTextBox";

/*also hidden by default, a popup word balloon that contains information for the user under certain scenarios, typically when user hovers over a tooltip/information icon*/
etids.div_toolTipTextBox = "#toolTipTextBox";

/*the tooltip popup for too great of a tip*/
etids.div_tooltipPopup = "#tooltipPopup";


(function (fd) {
	'use strict';

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
	var requestCounter = 0;
	var focusedElementId;

	var cartcontent = Object.create(WIDGET,{
		signal:{
			value:'cartData'
		},
		template:{
			value: function(data){
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
					
					/*find out if the subtotal member of the data object will have an order total array member*/
					if( typeof(data.subTotalBox.estimatedTotalBox) == "object" ){
						data.subTotalBox.subTotalBox.push( data.subTotalBox.estimatedTotalBox[0] );
					}
				}
				
				data.tipAmountsStr = data.tipAmounts.join(",");
				
				/* need to change between templates based on data param */
				var lineTemplate = $(this.placeholder).data('ec-linetemplate');

				var processFn = fd.modules.common.utils.discover(lineTemplate) || expressco.viewcartlines;

				this.updateTopCheckoutButton(data);
				
				/*process the soy template, using the data to populate it, then kill certain accidental unwanted repetive elements*/
				return processFn(data) + '<SCR'+'IPT>template_cleanup();<\/SCR'+'IPT>';
			}
		},
		placeholder:{
			value:'#cartcontent'
		},
		updateTopCheckoutButton: {
			value: function (data) {
				var $placeholder = $jq('#checkoutbutton_top');

				if($placeholder.size() && expressco.checkoutButton){
					$placeholder.html(expressco.checkoutButton(data));
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
							change: (userData || {})
						})
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
					fd.common.dispatcher.signal('productSampleCarousel', ajaxData);
					if(focusedElementId) {
						try {
							document.getElementById(focusedElementId).focus();
						} catch(e) {}
					}
					fd.expressco.checkout.coFlowChecker.checkFlow();
					try {
						fd.common.transactionalPopup.close();
					} catch(e) {}
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
				if($jq(etids.sel_tipDropdown).val() === "Other Amount"){
					$jq(etids.sel_tipDropdown).hide();
					$jq(etids.inp_tipTextBox).show();
					$jq(etids.inp_tipTextBox).focus();
					/*APPBUG-4219, disable the button if one switches to 'other amount' */
					/*$jq( etids.btn_tipApply ).prop("disabled", "disabled");*/
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
				tip_entered();
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

	$(document).on('change', cartcontent.placeholder + ' [data-component="changeETip"]',
		cartcontent.onChangeETip.bind(cartcontent));

	$(document).on('input', cartcontent.placeholder + ' [data-component="tooltip"]',
		cartcontent.onTipEntered.bind(cartcontent));

	fd.modules.common.utils.register('expressco', 'cartcontent', cartcontent, fd);
}(FreshDirect));