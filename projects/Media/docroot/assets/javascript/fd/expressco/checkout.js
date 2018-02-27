var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  'use strict';

  var DISPATCHER = fd.common.dispatcher,
      FORMS = fd.modules.common.forms,
      $ = fd.libs.$;
  var FORMS = fd.modules.common.forms;

  // checkout flow drawer enabler/disabler
  var coFlowChecker = Object.create(fd.common.signalTarget, {
    signal: {
      value: ['address','timeslot','payment']
    },
    callback: {
      value: function (data, sig) {
        $(document).trigger(sig+'-change');
      }
    },
    init: {
      value: function () {
        var eStream = $(document).asEventStream('checkoutFlow-change');

        this.signal.forEach(function (sig) {
          eStream = eStream.merge($(document).asEventStream(sig+'-change'));
        });
        eStream = eStream.debounce(100);
        eStream = eStream.merge($(document).asEventStream('checkoutFlowImmediate-change'));

        eStream.onValue(this.checkFlow.bind(this));

        this.listen();
      }
    },
    checkFlow: {
      value: function () {
        var address = (fd.expressco && fd.expressco.address && fd.expressco.address.data && fd.expressco.address.data.selected) || FORMS.serialize('address').id,
            timeslot = $('[timeslot-id]').attr('timeslot-id'),
            payment = (fd.expressco && fd.expressco.paymentmethod && fd.expressco.paymentmethod.data && fd.expressco.paymentmethod.data.selected) || (FORMS.serialize('payment').id)

        if (address) {
          this.enableTimeslot();
          if (timeslot && payment) {
            this.enableCheckout();
          } else {
            this.disableCheckout();
          }
        } else {
          this.disableTimeslot();
          this.disableCheckout();
        }

      }
    },
    enableCheckout: {
      value: function () {
        FORMS.releaseLockFormResubmit($('[fdform="checkout"]'));
        $('#cartcontent.checkout, .checkout-contentheader, [fdform="checkout"] button').attr('checkout-disabled', null)
        	.find('.tabbable').removeAttr('nofocus').each(function (i, e) {
        		var originalTabIndex = $(e).data('orig-tab-index');
        		if (originalTabIndex != null){
        			$(e).attr('tabindex', originalTabIndex);
        		} else {
        			$(e).removeAttr('tabindex');
        		}
        			
        	});
      }
    },
    enableTimeslot: {
      value: function () {
        fd.expressco.drawer.unlock('timeslot');
      }
    },
    disableCheckout: {
      value: function () {
        FORMS.lockFormResubmit($('[fdform="checkout"]'));
        $('#cartcontent.checkout, .checkout-contentheader, [fdform="checkout"] button').attr('checkout-disabled', 'true')
        	.find(':tabbable, .tabbable').addClass('tabbable').attr('nofocus', true).each(function(i, e) {
        		// store the original tab index to the element
        		if ($(e).data('orig-tab-index') == null) {
        		var originalTabIndex = $(e).attr('tabindex');
        		if (originalTabIndex != null)
        			$(e).data('orig-tab-index', originalTabIndex);
        		}
        		$(e).attr('tabindex', -1);
        	})
      }
    },
    disableTimeslot: {
      value: function () {
        fd.expressco.drawer.lock('timeslot');
      }
    }
  });
  coFlowChecker.init();

  // main checkout form
  fd.modules.common.forms.register({
    id: "checkout",
    submit: function (e) {
    	

      var formData = fd.modules.common.forms.serialize('checkout');


      e.preventDefault();
      e.stopPropagation();
     
      if(!fd.terms){
      	  doOverlayWindow('<iframe id=\'signupframe\' src=\'/registration/tcaccept_lite.jsp?successPage=nonIndex\' width=\'320px\' height=\'400px\' frameborder=\'0\' ></iframe>');
      	  $('#cartcontent.checkout, .checkout-contentheader, [fdform="checkout"] button').attr('disabled', null);
      	  return false;
        }
     
      formData.action = 'placeOrder';
      DISPATCHER.signal("server", {
        url: "/api/expresscheckout",
        method: "POST",
        data: {
          data: JSON.stringify({
            fdform: "checkout",
            formdata: formData
          })
        }
      });
    },
    success:function(id, data){
      id && FORMS.get(id).releaseLockWhenNotRedirecting(id, data);
    },
    failure:function(id, data){
      id && FORMS.get(id).releaseLockWhenNotRedirecting(id, data);
    },
    releaseLockWhenNotRedirecting:function(id, data){
      var hasRedirectUrl = data && data.redirectUrl && data.redirectUrl.length;
      if(!hasRedirectUrl){
        FORMS.releaseLockFormResubmit(FORMS.getEl(id));
      }
    }
    
    
  });
  var timeslotDrawerDeferred = jQuery.Deferred();
  var timeslotDrawerDependencyLoaded = function () {
	  timeslotDrawerDeferred.resolve();
  }
  var initSoyComponents = function () {
	  // Check if the page should redirect to another page
	  $.get('/api/expresscheckout?action=getRedirectUrl')
	  	.done( function (d) {
	  		if (d) {
				FreshDirect.common.dispatcher.signal('redirectUrl', d);
			}
	  	});
	  // Check restriction
	  $.get('/api/expresscheckout/restriction')
	  	.done( function (d) {
			FreshDirect.common.dispatcher.signal('restriction', d);
			
	  	});
	  
	  // Check ATP
	  $.get('/api/expresscheckout/atpfailure')
	  	.done( function (d) {
	  		if (d)
	  			FreshDirect.common.dispatcher.signal('atpFailure', d);
	  	});
	  var drawerDeferred = jQuery.Deferred();
	  var contextDeferred = jQuery.Deferred();
	  // Load Drawer, Form metadata, and session context info for checkout
	  $.when($.get('/api/expresscheckout?action=getDrawer'), $.get('/api/expresscheckout?action=getFormMetaData'))
	  	.done( function (v1, v2) {
	  		window.FreshDirect = window.FreshDirect || {};
	  		window.FreshDirect.expressco.data = window.FreshDirect.expressco.data || {};
	  		window.FreshDirect.expressco.data.formMetaData = window.FreshDirect.metaData = v2 && v2[0];
	  		FreshDirect.common.dispatcher.signal('drawer', v1 && v1[0]);
	  		drawerDeferred.resolve();
	  	});
	  $.get('/api/expresscheckout?action=resetContext').always( function() {
		  contextDeferred.resolve();
	  });

	  $.when(drawerDeferred, contextDeferred).then(function () {
		  // Load payment
		  $.get('/api/expresscheckout/payment')
			.done( function (d) {
				FreshDirect.common.dispatcher.signal('payment', d);
			});
		  // Load address
		  $.get('/api/expresscheckout/deliveryaddress')
		  	.done( function (d) {
		  		FreshDirect.common.dispatcher.signal('address', d);
			});
		  	
		// Load timeslot;
		  $.get('/api/expresscheckout/timeslot?action=getCurrentSelected')
		  	.done( function (d) {
		  		timeslotDrawerDeferred.then(function () {
		  			FreshDirect.common.dispatcher.signal('timeslot', d);
		  		});
		  	});
	  });
	  
  }
  fd.utils.registerModule('expressco', 'checkout', {
    coFlowChecker: coFlowChecker,
    initSoyComponents: initSoyComponents,
    timeslotDrawerDependencyLoaded: timeslotDrawerDependencyLoaded
  }, fd, 'checkout', '2_0');

}(FreshDirect));
