/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
	'use strict';

	var $ = fd.libs.$;
	var DRAWER_WIDGET = fd.modules.common.drawerWidget;
	var POPUPWIDGET = fd.modules.common.popupWidget;
	var DISPATCHER = fd.common.dispatcher;
	var paymentMethod = Object.create(DRAWER_WIDGET,{
		signal: {
			value:'payment',
			writable: true
		},
		callback: {
			value: function (data, signal) {
				if (this.isOpenSignal(signal)) {
					DRAWER_WIDGET.callback.call(this, data, signal);
					return;
				}
				if (fd.utils.isDeveloper()) {
					console.log('paymentMethod: callback', data);
				}
				// remove the xxxx in payment account number
				if (data && data.payments) {
					data.payments.forEach(function (p) {
						p.accountNumber = p.accountNumber.replace('XXXX','');
					});
				}
				DRAWER_WIDGET.callback.call(this, data, signal);
				this.check();
				
				/* APPDEV-4904, update the global freshdirect object */
				set_current_payment_choice_JSonly( data.payments );

				/* get what the cart contents should be, assuming that the global window.Freshdirect object has it yet */
				var templateRendered = cart_content_template_htmlstr();
				
				/* if the cart element exists AND anything worthwhile is returned from above, then repopulate it */
				if( ($("#cartcontent").length > 0) && (templateRendered.length > 1) ) {
					$("#cartcontent").html( templateRendered );
				}
			}
		},
		contentTemplate: {
			value: expressco.paymentmethodcontent
		},
		previewTemplate: {
			value: expressco.paymentmethodpreview
		},
		check: {
			value: function () {
				var data = this.serialize(),
					id = data.id;

				if (id === 'fake') {
					fd.expressco.drawer.lock('payment');
				} else {
					fd.expressco.drawer.unlock('payment');
				}
			}
		},
		serialize:{
			value: function() {
				return fd.modules.common.forms.serialize('payment');
			}
		}
	});

	paymentMethod.listen();
	var externalPayment = Object.create(fd.modules.common.widget,{
		signal:{
			value: ['payment']
		},
		template: {
			value: expressco.externalpayments
		},
		placeholder:{
			value:'#external-payments-container'
		}
	});
	externalPayment.listen();
	
	var defaultPaymentPrompt = Object.create(POPUPWIDGET,{
		headerContent: {
			value: ''
		},
		customClass: {
			value: 'defaultpaymentprompt'
		},
		hideHelp: {
			value: true
		},
		hasClose: {
			value: false
		},
		$trigger: {
			value: null // TODO
		},
		trigger: {
			value: '' // TODO
		},
		bodySelector:{
			value: '.ec-popup-content'
		},
		signal: {
			value: ''
		},
		scrollCheck: {
			value: '.ec-popup'
		},
		template: {
			value: expressco.eccenterpopup
		},
		bodyTemplate: {
			value: expressco.defaultpaymentprompt
		},
		popupId: {
			value: 'defaultPaymentPrompt'
		},
		decorate: {
			value: function () {
				$(this.trigger).attr('role', 'alertdialog');
			}
		},
		popupConfig: {
			value: {
				zIndex: 2000,
				openonclick: true,
				overlayExtraClass: 'centerpopupoverlay',
				align: false,
				hidecallback: function (e) {
					if (fd.utils.isDeveloper()) {
						console.log('defaultPaymentPrompt: hidecallback', 'forcing setAsDefault: false');
					}

					//submit payment form
					DISPATCHER.signal('server', {
						url: '/api/expresscheckout/payment',
						method: 'POST',
						data: {
							data: JSON.stringify({
							fdform: 'payment',
							formdata: paymentMethod.serialize()
							})
						}
					});
					
					//reset as false for next pass (safety)
					$('#paymentSetAsDefault').val(false);
				}
			}
		},
		open: {
			value: function (e) {
				if (fd.utils.isDeveloper()) {
					console.log('defaultPaymentPrompt: open');
				}
				var $t = e && $(e.currentTarget) || $(document.body);

				this.popup.show($t);
				this.popup.clicked = true;

				this.noscroll(true);
			}
		},
		close: {
			value: function (e) {
				if (fd.utils.isDeveloper()) {
					console.log('defaultPaymentPrompt: close');
				}
				
				var $t = e && $(e.currentTarget) || $(document.body);

				this.popup.hide($t);
				this.popup.clicked = false;
			}
		},
		toggleSetAsDefault: {
			value: function(e) {
				if (fd.utils.isDeveloper()) {
					console.log('defaultPaymentPrompt: toggleSetAsDefault', $(e.target).data('makepaymentdefault'));
				}

				$('#paymentSetAsDefault').val(!!$(e.target).data('makepaymentdefault'));
				this.close();
			}
		}
	});

	defaultPaymentPrompt.listen();
	defaultPaymentPrompt.render();
	/* bind to buttons */
	$(document).on('click', '#' + defaultPaymentPrompt.popupId +' button[data-makepaymentdefault]', defaultPaymentPrompt.toggleSetAsDefault.bind(defaultPaymentPrompt));

	$(document).ready(function () {
		paymentMethod.check();
	});

	// $(document).on('click', paymentMethod.previewHolder(),	paymentMethod.previewClick.bind(paymentMethod));

	// payment related forms
	fd.modules.common.forms.register({
	id: "CC",
	success: function () {
		if (fd.expressco.addpaymentmethodpopup) {
			fd.expressco.addpaymentmethodpopup.close();
		}
	}
	});
	fd.modules.common.forms.register({
		id: "MP",
		success: function (id, result) {
			MasterPass.client.checkout({
				"requestToken":result.eWalletResponseData.token,
				"callbackUrl":result.eWalletResponseData.callbackUrl,
				"requestedDataTypes":[result.eWalletResponseData.reqDatatype],
				"merchantCheckoutId":result.eWalletResponseData.eWalletIdentifier,
				"allowedCardTypes":[result.eWalletResponseData.allowedPaymentMethodTypes],
				"suppressShippingAddressEnable": result.eWalletResponseData.suppressShippingEnable,
				"requestBasicCheckout" : result.eWalletResponseData.requestBasicCkt,
				"version":result.eWalletResponseData.version,
				/*
				"cancelCallback": function onCancelledCheckout(data) {
					// no op
				}
				*/
			});
		},
		failure: function (id, result) {
			$('#MP_ERROR').css("display","inline-block");
		},
		error:function (id, result) {
			$('#MP_ERROR').css("display","inline-block");
		},
		fail:function (id, result) {
			$('#MP_ERROR').css("display","inline-block");
		}
	});

	fd.modules.common.forms.register({
		id: "EC",
		success: function () {
			if (fd.expressco.addpaymentmethodpopup) {
				fd.expressco.addpaymentmethodpopup.close();
			}
		}
	});

	fd.modules.common.forms.register({
		id: "ET",
		success: function () {
			if (fd.expressco.addpaymentmethodpopup) {
				fd.expressco.addpaymentmethodpopup.close();
			}
		}
	});

	fd.modules.common.forms.register({
		id: "payment",
		success: function (id) {
			if (id && fd.expressco.drawer) {
				fd.expressco.drawer.reset();
			}
			$("#ec-drawer").trigger("paymentmethod-update");
		},
		submit: function() {
			/* check if there's a debit card or echeck selected that isn't already default, so we'll need the prompt
			 * this would do all types:
			 * $jq('.paymentmethod[data-isdefault="false"]:checked').length
			 */
			if (
				fd.utils.isActive('debitCardSwitch', '2017') && /* prop is on */
				(
					$jq('.paymentmethod[data-isdebit="true"][data-isdefault="false"]:checked').length ||
					$jq('.paymentmethod[data-type="ECheck"][data-isdefault="false"]:checked').length
				)
			) {
				if (fd.utils.isDeveloper()) {
					console.log('payment form: submit', 'opening defaultPaymentPrompt');
				}

				FreshDirect.expressco.defaultPaymentPrompt.open();
			} else {
				if (fd.utils.isDeveloper()) {
					console.log('payment form: submit', 'payment does not qualify to save as default', $('.paymentmethod[checked]'));
				}

				//submit payment form
				DISPATCHER.signal('server', {
					url: '/api/expresscheckout/payment',
					method: 'POST',
					data: {
						data: JSON.stringify({
						fdform: 'payment',
						formdata: paymentMethod.serialize()
						})
					}
				});
			}
			return true;
		}
	});

	fd.modules.common.forms.register({
		id: "payment_loadPaymentMethod",
		success: function (id, result) {
			if (id &&	result && result.paymentEditValue) {
				fd.expressco.addpaymentmethodpopup.open(null, result.paymentEditValue);
			}
		}
	});

	fd.modules.common.utils.register('expressco',	'paymentmethod', paymentMethod, fd);
	fd.modules.common.utils.register('expressco',	'defaultPaymentPrompt', defaultPaymentPrompt, fd);
}(FreshDirect));
