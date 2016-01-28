/*global expressco, Bacon*/
var FreshDirect = FreshDirect || {};

//kills unwanted dupes, typically resulting from bad logic in a loop
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

function template_dupe_cleaner(){
	//kill certain accidental unwanted repetive elements
	dupe_buster(".deliveryFeeToolTips", "deliveryFeeToolTips");
	dupe_buster(".st_label_deliveryfee", "st_label_deliveryfee");
	dupe_buster(".st_val_deliveryfee", "st_val_deliveryfee");
	dupe_buster(".st_label_subtotal", "st_label_subtotal");
	dupe_buster(".st_val_subtotal", "st_val_subtotal");
	dupe_buster(".st_label_ssOrderTotal", "st_label_ssOrderTotal");
	dupe_buster(".st_val_ssOrderTotal", "st_val_ssOrderTotal");
}

(function (fd) {
	'use strict';

	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
	var requestCounter = 0;
	var focusedElementId;
	
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

	var cartcontent = Object.create(WIDGET,{
		signal:{
			value:'cartData'
		},
		template:{
			value: function(data){
				//if there is a tip amount, just forcibly make sure that etipping is enabled on the javascript side
				if( data.etipTotal && data.etipTotal.length > 0 ){
					var floatDoubleTip = Number(data.etipTotal.replace(/[^0-9\.]+/g,""));
					if( floatDoubleTip > 0 ){
						data.eTippingEnabled = true;
					}
				}
				
				//override, turn etipping off for the view_cart page
				var path = window.location.pathname;
				var page = path.split("/").pop();

				if( page == "view_cart.jsp" ){
					data.eTippingEnabled = false;
					
					//find out if the subtotal member of the data object will have an order total array member
					if( typeof(data.subTotalBox.estimatedTotalBox) == "object" ){
						data.subTotalBox.subTotalBox.push( data.subTotalBox.estimatedTotalBox[0] );
					}
				}
				
				/* need to change between templates based on data param */
				var lineTemplate = $(this.placeholder).data('ec-linetemplate');

				var processFn = fd.modules.common.utils.discover(lineTemplate) || expressco.viewcartlines;

				this.updateTopCheckoutButton(data);
				
				//kill certain accidental unwanted repetive elements
				return processFn(data) + '<SCR'+'IPT>template_dupe_cleaner();<\/SCR'+'IPT>';
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

				//console.log( '$(".cartsection__totalwrapper").length = ' + $(".cartsection__totalwrapper").length );

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

				template_dupe_cleaner();
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
				console.log("onTipSelectionChange >>>>>");
				$jq(etids.btn_tipApplied).hide();
				$jq(etids.btn_tipApply).show();
				$jq(etids.ck_tipAppliedTick).hide();
				if($jq(etids.sel_tipDropdown).val() == "Other Amount"){
					console.log("Selected Other Amount");
					$jq(etids.sel_tipDropdown).hide();
					$jq(etids.inp_tipTextBox).show();
					
					/*APPBUG-4219, disable the button if one switches to 'other amount' */
					//$jq( etids.btn_tipApply ).prop("disabled", "disabled");
					//console.log( '$(".cartsection__totalwrapper").length = ' + $(".cartsection__totalwrapper").length );
				}
			}
		},
		onTipEntered: {
			value: function(e){
				e.preventDefault();
				e.stopPropagation();
				console.log("onTipEntered");
				$jq(etids.btn_tipApply).show();
				$jq(etids.btn_tipApplied).hide();
				var tip = $(etids.inp_tipTextBox).val().replace(/[A-Za-z\-\_\!\#\%\^\&\*\(\)\[\]\{\}\<\>\,\?]/g, '').trim();

				$(etids.inp_tipTextBox).val( tip );
				
				var subTotalStr = $('#hiddenSubTotal').val();
				var subTotal = subTotalStr.substring(1);
				console.log("Sub Total : " + subTotal + " Tip : " + tip);
				var maximumTipAllowed = subTotal * 32 / 100;
				var roundedMaxTip = Math.round(maximumTipAllowed * 100) / 100;
				
				//console.log("maximumTipAllowed = " + maximumTipAllowed + " , roundedMaxTip = " + roundedMaxTip);
				
				//if(tip > maximumTipAllowed){
				if(tip > roundedMaxTip){ //APPBUG-4270
					console.log("Tip greater than maximum tip");
					$jq(etids.btn_tipApply).prop('disabled', true);

					//this goes in the hover box
					var innerHtml = "<div class='tooltip-inner'><b>That's quite a tip, thank you!</b><br/><p>As of now, we cap all electronic tips at 32% of the subtotal, making the highest allowed tip to be $" + roundedMaxTip + " for this order.</p></div>";

					$jq(etids.div_toolTipTextBox).html('').append(innerHtml);
					
					
					$jq("#tipTextBox").mouseover(function(){
						$jq("#tooltipPopup").addClass("toomuch-etip");
					})
					$jq("#tipTextBox").mouseout(function(){
						$jq("#tooltipPopup").removeClass("toomuch-etip");
					})
					
					//console.log( "etids.div_toolTipTextBox = " + etids.div_toolTipTextBox );
				//}else if( parseFloat(tip) > 0 ){ /*if the tip is a proper number and is greater than zero */
				}else{
					$jq(etids.div_toolTipTextBox).html('');
					$jq(etids.btn_tipApply).prop('disabled', false);
				}

				//console.log( '$(".cartsection__totalwrapper").length = ' + $(".cartsection__totalwrapper").length );
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
				this.render(value); //everything after this within this function assumes that the soy template has been rendered on the page
				fd.components.carousel && fd.components.carousel.initialize();

				//remove extra e-tip element crap (sorry for this hack solution)
				if( $(".cartsection__totalwrapper").length > 1 ){
					$(".cartsection__totalwrapper:first div.subtotalboxes").remove();

					$(".cartsection__totalwrapper:first div.cartsection__tax").remove();
				}
				
				//tooltip hover fix hack
				$jq("#deliveryFeeToolTip, #optionalToolTip").mouseover(function(){
					$jq("#tooltipPopup").addClass("msg-etip");
				})
				$jq("#deliveryFeeToolTip, #optionalToolTip").mouseout(function(){
					$jq("#tooltipPopup").removeClass("msg-etip");
				})

				//kill certain accidental unwanted repetive elements
				template_dupe_cleaner();
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
				
				console.log( '$(".cartsection__totalwrapper").length = ' + $(".cartsection__totalwrapper").length );
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

		//console.log( '$(".cartsection__totalwrapper").length = ' + $(".cartsection__totalwrapper").length );
	});
  
	$(document).on('change', cartcontent.placeholder + ' [data-component="changeETip"]', 
		cartcontent.onChangeETip.bind(cartcontent));
  
	$(document).on('input', cartcontent.placeholder + ' [data-component="tooltip"]', 
		cartcontent.onTipEntered.bind(cartcontent));

	fd.modules.common.utils.register('expressco', 'cartcontent', cartcontent, fd);
}(FreshDirect));
