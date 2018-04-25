/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function(fd) {
	"use strict";
	var $ = fd.libs.$;
	var POPUPWIDGET = fd.modules.common.popupWidget;

	function pushToDataLayer(dataObj) {
		if (fd.utils.hasOwnNestedProperty('dataLayer')) {
			dataLayer.push(dataObj);
		}
	}

	var ordermodify_ACTIONS = {
		DEFAULT: 'ordermodify',
		MIN: 'ordermodify_minimize',
		MAX: 'ordermodify_maximize',
		SEEN: 'ordermodify_seen'
	};
	var ordermodify_PAYLOADS = {
		DEFAULT: { "action": ordermodify_ACTIONS.DEFAULT },
		MIN: { "action": ordermodify_ACTIONS.MIN },
		MAX: { "action": ordermodify_ACTIONS.MAX },
		SEEN: { "action": ordermodify_ACTIONS.SEEN } /* optional "value": {boolean=true} */
	};
	var orderModify_GTMDATAS = {
		OPEN: {
			event: 'info-ordermodify-open',
			eventCategory: 'informational',
			eventAction: 'seen ordermodify informational',
			eventLabel: 'info ordermodify load',
		},
		CTA_CLICK: {
			event: 'info-ordermodify-cta-click',
			eventCategory: 'informational',
			eventAction: 'clicked cta button',
			eventLabel: 'info ordermodify cta click',
		},
		X_CLICK: {
			event: 'info-ordermodify-x-click',
			eventCategory: 'informational',
			eventAction: 'clicked x button',
			eventLabel: 'info ordermodify x click',
		},
		DONT_SHOW_CLICK: {
			event: 'info-ordermodify-dont-show-click',
			eventCategory: 'informational',
			eventAction: 'clicked dont show',
			eventLabel: 'info ordermodify dont show click',
		}
	};

	var ordermodify = Object.create(POPUPWIDGET, {
		KEY: { value: 'inform.ordermodify' },
		APIPATH: { value: '/api/informational' },
		ACTIONS: { value: ordermodify_ACTIONS },
		PAYLOADS: { value: ordermodify_PAYLOADS },
		GTMDATAS: { value: orderModify_GTMDATAS },
		template: {
			value: common.inform.informPopup /* generic shell, includes "don't show again" */
		},
		headerContent: {
			value: 'How to modify your order:'
		},
		bodyTemplate: {
			value: common.inform.ordermodify
		},
		popupId: {
			value: 'informOrderModify'
		},
		hasClose: {
			value: true
		},
		popupConfig: { /* required */
			value: {
				zIndex: 2000,
				overlayExtraClass: 'inform-ghost centerpopupoverlay', /* use to fix ghost css */
				align: false,
				hidecallback: function (e) {
					/* do nothing extra on overlay click */
				}
			}
		},
		isEnabled: {
			value: function() {
				return (
					fd.utils.hasOwnNestedProperty('FreshDirect.properties.'+this.KEY+'.enabled')
					&& fd.properties.inform.ordermodify.enabled
				);
			}
		},
		shouldFire: {
			value: function() {
				return (
					fd.utils.hasOwnNestedProperty('FreshDirect.'+this.KEY+'.fire')
					&& FreshDirect.inform.ordermodify.fire
				);
			}
		},
		isTesting: {
			value: function() {
				return ( fd.utils.hasOwnNestedProperty('FreshDirect.'+this.KEY+'.test') );
			}
		},
		init: {
			value: function(force, forceListeners) {
				if ( (this.isEnabled() && this.shouldFire()) || this.isTesting() || forceListeners) {
					this.addEventListeners();
				}
				if ((this.isEnabled() && this.shouldFire()) || force)  {
					if (this.isTesting()) { /* don't fire on test page */
						if (fd.utils.isDeveloper()) { console.log( this.KEY, 'Test Page, ignoring fire call' ); }
					} else {
						this.fire();
					}
				}
			}
		},
		gtmAddData: { /* add gtm data for events */
			value: function(e) { /* NO TRACKING */
				//pushToDataLayer(this.GTMDATAS.OPEN);
				//click tracking
				//$('#'+this.popupId+' .continueBtn').attr('data-gtm-click', JSON.stringify(this.GTMDATAS.CTA_CLICK));
				//$('#'+this.popupId+' .close-icon').attr('data-gtm-click', JSON.stringify(this.GTMDATAS.X_CLICK));
				//$('#'+this.popupId+' [data-inform-fn-maximize]').attr('data-gtm-click', JSON.stringify(this.GTMDATAS.DONT_SHOW_CLICK));
			}
		},
		gtmClick: {
			value: function(e) {
				try {
					var dataObj = JSON.parse($(e.target).attr('data-gtm-click'));
					pushToDataLayer(dataObj);
				} catch (ex) {
					if (fd.utils.isDeveloper()) { console.log(ex); }
				}
			}
		},
		addEventListeners: {
			value: function() {
				if (fd.utils.isDeveloper()) { console.log( this.KEY, 'adding event listeners' ); }
				/* bind "don't show again" checkbox to maximize */
				$(document).on('change', '#'+this.popupId+' [data-inform-fn-maximize]', this.maximize.bind(this));
				$(document).on('click', '#'+this.popupId+' .close', this.close.bind(this));
				$(document).on('click', '#'+this.popupId+' [data-conditional-navigate-uri]', this.conditionalNavigate.bind(this));

				/* bind generic gtm event */
				$(document).on('click', '#'+this.popupId+' [data-gtm-click]', this.gtmClick.bind(this));
			}
		},
		conditionalNavigate: { /* navigate unless already at URI */
			value: function(e) {
				var checkUriData = $(e.currentTarget).attr('data-conditional-navigate-uri'),
				useCheckUriData = (checkUriData !== '' && checkUriData != null),
				checkUri = (useCheckUriData) ? checkUriData : $(e.currentTarget).attr('href') || '';

				if (window.top.location.toString().indexOf(checkUri) === -1) {
					window.top.location = checkUri;
				} else {
					e.preventDefault();
					return;
				}
			}
		},
		fire: {
			value: function() {
				if (fd.utils.isDeveloper()) { console.log( this.KEY, 'fire call' ); }

				$.get(this.APIPATH, this.PAYLOADS.DEFAULT, this.handlerGetSuccess.bind(this)).fail(this.handlerGetError.bind(this));
			}
		},
		maximize: {
			value: function() {
				$.post(this.APIPATH, this.PAYLOADS.MAX, this.handlerPostSuccess.bind(this)).fail(this.handlerPostError.bind(this));
			}
		},
		minimize: {
			value: function() {
				$.post(this.APIPATH, this.PAYLOADS.MIN, this.handlerPostSuccess.bind(this)).fail(this.handlerPostError.bind(this));
			}
		},
		seen: {
			value: function() {
				$.post(this.APIPATH, this.PAYLOADS.SEEN, this.handlerPostSuccess.bind(this)).fail(this.handlerPostError.bind(this));
			}
		},
		open: {
			value: function(data) {
				if (fd.utils.isDeveloper()) { console.log( this.KEY, 'fire call open', data ); }

				if (!data || !data.media) { return; }

				this.refreshBody({ body: data.media });
				
				this.gtmAddData(); /* after refresh */

				//force checkbox clear
				$('[data-inform-fn-maximize]').prop('checked', false);
				this.popup.show($(document.body));
				this.popup.clicked = true;

				this.seen();
			}
		},
		handlerGetSuccess: {
			value: function(resp) {
				if (resp.success && resp.media !== '' && resp.viewCount <= resp.viewCountLimit) {
					if (fd.utils.isDeveloper()) { console.log( this.KEY, 'GET call success', resp ); }

					this.open(resp);
				} else {
					if (fd.utils.isDeveloper()) { console.log( this.KEY, 'GET call fail', resp ); }
				}
			},
			writable: true /* true so we can proxy in test page */
		},
		handlerGetError: {
			value: function(resp) {
				if (fd.utils.isDeveloper()) { console.log( this.KEY, 'GET call error', err ); }
			},
			writable: true /* true so we can proxy in test page */
		},
		handlerPostSuccess: {
			value: function(resp) {
				if (resp.success) {
					if (fd.utils.isDeveloper()) { console.log( this.KEY, 'POST call success', resp ); }

					/* post-POST */
					if (resp.action === this.ACTIONS.MAX) {
						/* use delay so it doesn't close instantly, which "feels" better */
						setTimeout((function() { this.close()}).bind(this), 500);
					}
				} else {
					if (fd.utils.isDeveloper()) { console.log( this.KEY, 'POST call fail', resp ); }
				}
			},
			writable: true /* true so we can proxy in test page */
		},
		handlerPostError: {
			value: function(resp) {
				if (fd.utils.isDeveloper()) { console.log( this.KEY, 'POST call error', err ); }
			},
			writable: true /* true so we can proxy in test page */
		}
	});
	//always init
	ordermodify.init();

	//register
	fd.modules.common.utils.register("components.inform", "ordermodify", ordermodify, fd);
}(FreshDirect));