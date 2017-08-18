/* global common jquery */

var FreshDirect = FreshDirect || {};
var checkout;

(function (fd) {
	'use strict';
	var $ = fd.libs.$;
	var WIDGET = fd.modules.common.widget;
	var POPUPWIDGET = fd.modules.common.popupWidget;
	var DISPATCHER = fd.common.dispatcher;
	var SAVE_DEFAULT_API = '/api/expresscheckout/payment', /* API url to call */
		SAVE_DEFAULT_FDFORM = 'payment', /* form we're faking */
		DEFAULT_CHECKBOX_SELECTOR = '.paymentDef', /* find all checkmark elems */
		NEEDS_SAVE_SELECTOR = '.paymentDef[data-isdefault="false"]:checked', /* if has length, needs saving */
		SET_AS_DEFAULT_SELECTOR = '.paymentDef[data-isdefault="true"]'; /* currently selected default */
	var _lastPostedPaymentId = '';

	var paymentinfo = Object.create(WIDGET, {
		signal: {
			value:'payment'
		},
		callback: {
			value: function (data) {
				var $fallback = $(SET_AS_DEFAULT_SELECTOR);
				var $setAsDefault;

				//clear all checked
				$(DEFAULT_CHECKBOX_SELECTOR).prop('checked', '');
				//clear all previous default (since PP is not returned in api call)
				$(DEFAULT_CHECKBOX_SELECTOR).attr('data-isdefault', 'false');
				//set the labels as default
				$jq(DEFAULT_CHECKBOX_SELECTOR+' + label>.offscreen').html(paymentinfo.getNonDefaultLabel($(DEFAULT_CHECKBOX_SELECTOR)));

				if (data && data.payments) {
					//match frontend to backend
					for (var payment in data.payments) {
						if (!data.payments[payment].id) { continue; }
						$('[data-paymentid="'+data.payments[payment].id+'"]').attr('data-isdefault', data.payments[payment].default);
					}
				}

				$setAsDefault = $(SET_AS_DEFAULT_SELECTOR);
				//if we don't have a default selected now, then it's prob PP. try via id
				if (!$setAsDefault.length) {
					$setAsDefault = $(DEFAULT_CHECKBOX_SELECTOR+'[data-paymentid="'+paymentinfo.lastPostedPaymentId()+'"]');
				}
				//reset checked based on data attr
				$setAsDefault.prop('checked', 'checked');
				//check which label to put in
				if ($fallback != $setAsDefault) {
					//saved, use saved label
					$setAsDefault.find('+ label>.offscreen').html(paymentinfo.getJustSavedLabel($(SET_AS_DEFAULT_SELECTOR)));
				} else {
					//failed to save, revert back to default
					$setAsDefault.find('+ label>.offscreen').html(paymentinfo.getDefaultLabel($(SET_AS_DEFAULT_SELECTOR)));
				}
			}
		},
		getNonDefaultLabel: {
			value: function($elem) {
				var str = '';
				if ($elem && $elem.attr('data-type') && $elem.attr('data-lastfour')) {
					var last4 = ($elem.attr('data-lastfour')).replace(/X/g, '');
					str += '' +$elem.attr('data-type') + ((last4 !== '') ? ', ending in '+last4 : '');
				}
				return str;
			}
		},
		getDefaultLabel: {
			value: function($elem) {
				var str = '';
				if ($elem && $elem.attr('data-type') && $elem.attr('data-lastfour')) {
					var last4 = ($elem.attr('data-lastfour')).replace(/X/g, '');
					str += 'Default payment option. ' +$elem.attr('data-type') + ((last4 !== '') ? ', ending in '+last4 : '');
				}
				return str;
			}
		},
		getJustSavedLabel: {
			value: function($elem) {
				var str = '';
				if ($elem && $elem.attr('data-type') && $elem.attr('data-lastfour')) {
					var last4 = ($elem.attr('data-lastfour')).replace(/X/g, '');
					str += $elem.attr('data-type') + ((last4 !== '') ? ' + '+last4 : '') + ' is now your default payment option.';
				}
				return str;
			}
		},
		selectAsDefault: {
			value: function() {
				/* don't use serialize, the options are across multiple forms */
				var data = {
					action: 'selectPaymentMethod',
					/* paymentSetAsDefault must be a string */
					paymentSetAsDefault: ''+!!$(NEEDS_SAVE_SELECTOR).length,
					id: $(NEEDS_SAVE_SELECTOR).attr('data-paymentid')
				};
				if (data.paymentSetAsDefault && data.id) {
					/* submit */
					if (fd.utils.isDeveloper()) {
						console.log('paymentinfo', 'setting as default', data);
					}
					paymentinfo.postSelectAsDefault(data);
				} else {
					if (fd.utils.isDeveloper()) {
						console.log('paymentinfo', 'not setting as default', data);
					}
					//clear all checked
					$(DEFAULT_CHECKBOX_SELECTOR).prop('checked', '');
					//reset checked based on data attr
					$(SET_AS_DEFAULT_SELECTOR).prop('checked', 'checked');
				}
			}
		},
		postSelectAsDefault: {
			value: function (data) {
				//submit payment form
				DISPATCHER.signal('server', {
					url: SAVE_DEFAULT_API,
					method: 'POST',
					data: {
						data: JSON.stringify({
							fdform: SAVE_DEFAULT_FDFORM,
							formdata: data
						})
					}
				});
			}
		},
		setAsDefaultEvent: {
			value: function (event) {
				//clear all checked
				$(DEFAULT_CHECKBOX_SELECTOR).prop('checked', '');
				//FORCE as checked (instead of a toggle, since it's a checkbox
				$(event.target).prop('checked', 'checked');
				paymentinfo.lastPostedPaymentId($(event.target).attr('data-paymentid'));
				paymentinfo.selectAsDefault();
			}
		},
		lastPostedPaymentId: {
			value: function(id) { /* pass id to set */
				if (id) {
					_lastPostedPaymentId = id;
				}
				return _lastPostedPaymentId;
			}
		}
	});
	
	var paymentinfoerror = Object.create(POPUPWIDGET,{
		headerContent: {
			value: ''
		},
		customClass: {
			value: 'paymentinfoerror'
		},
		hideHelp: {
			value: true
		},
		hasClose: {
			value: true
		},
		$trigger: {
			value: null // TODO
		},
		trigger: {
			value: '' // TODO
		},
		bodySelector:{
			value: '.c-popup-content'
		},
		signal: {
			value: ''
		},
		scrollCheck: {
			value: '.c-popup'
		},
		template: {
			value: common.centerpopup
		},
		bodyTemplate: {
			value: youraccount.paymentinfoerror
		},
		popupId: {
			value: 'paymentInfoError'
		},
		popupConfig: {
			value: {
				zIndex: 2000,
				openonclick: true,
				overlayExtraClass: 'centerpopupoverlay',
				align: false,
				hidecallback: function (e) {
					/* do nothing extra on overlay click */
				}
			}
		},
		open: {
			value: function (e, data) {
				var $t = e && $(e.currentTarget) || $(document.body);
				if (data) {
					//if there's no edit button (like PP), remove the id before sending to the soy template
					if (!$('[data-paymentidedit="'+data.id+'"]').length) {
						delete data.paymentId;
					}
					console.log('open', data);
					this.refreshBody(data, this.bodyTemplate);
				}
				this.popup.show($t);
				this.popup.clicked = true;

				this.noscroll(true);
			}
		},
		close: {
			value: function (e) {
				var $t = e && $(e.currentTarget) || $(document.body);

				this.popup.hide($t);
				this.popup.clicked = false;
			}
		},
		editErrorPayment: {
			value: function(e) {
				//proxy click the existing edit button
				window.top.location = $('[data-paymentidedit="'+$(e.target).attr('data-paymentid')+'"]').attr('href');
			}
		}
	});

	paymentinfoerror.listen();
	$(document).on('click', '#'+paymentinfoerror.popupId+' .close', paymentinfoerror.close.bind(paymentinfoerror));
	$(document).on('click', '#'+paymentinfoerror.popupId+' .cssbutton[data-paymentid]', paymentinfoerror.editErrorPayment.bind(paymentinfoerror));


	//catch save errors that don't throw an error
	$( document ).ajaxSuccess(function(event, xhr, settings) {
		if (xhr && xhr.hasOwnProperty('responseJSON') && $.isPlainObject(xhr.responseJSON)) {
			var data = $.extend({}, xhr.responseJSON);
			if (data.submitForm && data.submitForm.fdform === 'payment' && data.submitForm.success === false && data.validationResult && data.validationResult.errors && data.validationResult.errors.length) {
				paymentinfoerror.open(event, { error: data.validationResult.errors[0].error, 'paymentId': paymentinfo.lastPostedPaymentId()});
				paymentinfo.callback({}); //pass in an empty object and let existing data sort itself out
			}
		}
	})

	//set the initial label(s)
	$(DEFAULT_CHECKBOX_SELECTOR+' + label>.offscreen').html(paymentinfo.getNonDefaultLabel($(DEFAULT_CHECKBOX_SELECTOR)));
	$(SET_AS_DEFAULT_SELECTOR+' + label>.offscreen').html(paymentinfo.getDefaultLabel($(SET_AS_DEFAULT_SELECTOR)));

	paymentinfo.listen();
	$(document).on('click', DEFAULT_CHECKBOX_SELECTOR, paymentinfo.setAsDefaultEvent);
	
	fd.modules.common.utils.register('youraccount', 'paymentinfo', paymentinfo, fd);
	fd.modules.common.utils.register('youraccount', 'paymentinfoerror', paymentinfoerror, fd);

	/* code migrated from jsp */
	$(document).ready(function($) {
		var isPayPalPaired = $('#isPayPalWalletConnected').val();
		if (isPayPalPaired != 'true') {
			// Brain Tree setup-- Start
			brainTreeSetup($);
			// Brain Trees setup -- END
		}

		if (document.querySelector('#PP-connect') != null) {
			document.querySelector('#PP-connect').addEventListener('click', function(event) {
				if (event.preventDefault) {
					event.preventDefault();
				} else {
					event.returnValue = false;
				}
				if (checkout != null) {
					checkout.paypal.initAuthFlow();
				}
				var isPayPalDown = $('#isPayPalDown').val();
				if (isPayPalDown == 'true') {
					$('#PP_ERROR').css("display", "inline-block");
				}else{
					$('#PP_ERROR').css("display", "none");
				}
			});
		}

		$('#deletePPWallet').click(function() {
			$.ajax({
				url: "/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EPPDISC\",\"formdata\":{\"action\":\"PP_Disconnect_Wallet\",\"ewalletType\":\"PP\"}}",
				type: 'post',
				contentType: "application/json; charset=utf-8",
				dataType: "json",
				success: function(result1) {
					/* $('#pp_wallet_cards').css("display","none"); */
					$('#pp_wallet_cards').empty();
					$('#PP_logo').css("display", "none");
					$('#PP-connect').css("display", "inline-block");
					$("#isPayPalWalletConnected").val("false");
					$('#PP_ERROR').css("display", "none");
					brainTreeSetup($);
				}
			});
		});
	});

	function brainTreeSetup($){
		$.ajax({
			url: "/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"PPSTART\",\"formdata\":{\"action\":\"PP_Connecting_Start\",\"ewalletType\":\"PP\"}}",
			type: 'post',
			contentType: "application/json; charset=utf-8",
			dataType: "json",
			success: function(result) {
				if (result.submitForm.success) {
					$('#PP_ERROR').css("display", "none");
					$("#isPayPalDown").val("false");
					//var x = document.getElementById("PP_button");
					var deviceObj = "";
					if (window.hasOwnProperty('braintree')) {
						braintree.setup(result.submitForm.result.eWalletResponseData.token, "custom", {
							dataCollector: {
								paypal: true
							},
							onReady: function(integration) {
								// alert("integration.deviceData :"+integration.deviceData);
								checkout = integration;
								// checkout.paypal.initAuthFlow();
								deviceObj = JSON.parse(integration.deviceData);

							},
							// For saving the PayPal payment Method to FD
							onPaymentMethodReceived: function(payload) {

								$.ajax({
									url: "/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"PPEND\",\"formdata\":{\"action\":\"PP_Connecting_End\",\"ewalletType\":" +
										"\"PP\",\"origin\":\"your_account\",\"paymentMethodNonce\":\"" + payload.nonce + "\",\"email\":\"" + payload.details.email + "\",\"firstName\":\"" + payload.details.firstName + "\"," +
										"\"lastName\":\"" + payload.details.lastName + "\" ,\"deviceId\":\"" + deviceObj.correlation_id + "\"}}",
									type: 'post',
									success: function(id, result) {
										location.reload(true);
									}
								});
							},
							paypal: {
								singleUse: false,
								headless: true
							}
						});
					}
				} else {
					$("#isPayPalDown").val("true");
				}
			}
		});
	}
}(FreshDirect));