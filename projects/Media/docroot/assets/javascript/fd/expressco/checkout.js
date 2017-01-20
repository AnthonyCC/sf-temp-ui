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
        var address = FORMS.serialize('address').id,
            timeslot = $('[timeslot-id]').attr('timeslot-id'),
            payment = FORMS.serialize('payment').id;

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
        $('#cartcontent.checkout, .checkout-contentheader, [fdform="checkout"] button').attr('checkout-disabled', null);
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
        $('#cartcontent.checkout, .checkout-contentheader, [fdform="checkout"] button').attr('checkout-disabled', 'true');
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

  fd.utils.registerModule('expressco', 'checkout', {
    coFlowChecker: coFlowChecker
  }, fd, 'checkout', '2_0');

}(FreshDirect));
