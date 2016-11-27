/*global expressco*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  'use strict';

  var $ = fd.libs.$;
  var DRAWER_WIDGET = fd.modules.common.drawerWidget;
  var checkout;
  var paymentMethod = Object.create(DRAWER_WIDGET,{
    signal: {
      value:'payment'
    },
    callback: {
      value: function (data) {
        DRAWER_WIDGET.callback.call(this, data);
        this.check();
          
          /* APPDEV-4904, update the global freshdirect object */
        set_current_payment_choice_JSonly( data.payments );

        /* get what the cart contents should be, assuming that the global window.Freshdirect object has it yet */
        var templateRendered = cart_content_template_htmlstr();
        
        /* if the cart element exists AND anything worthwhile is returned from above, then repopulate it */
        if( ($("#cartcontent").length > 0) && (templateRendered.length > 1) ){
          $("#cartcontent").html( templateRendered );
        }

        // update oas
        try {
          fd.modules.common.updateOAS(OAS_url, 'www.freshdirect.com/XCpaymentpromo', OAS_rns, ['FirstECheckPromo'], OAS_query);
        } catch (e) {
          console.trace(e);
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
      value:function(){
        return fd.modules.common.forms.serialize('payment');
      }
    }
  });

  paymentMethod.listen();

  $(document).ready(function () {
    paymentMethod.check();
  });

  // $(document).on('click', paymentMethod.previewHolder(), paymentMethod.previewClick.bind(paymentMethod));

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
            /* "cancelCallback": function onCancelledCheckout(data)
             {
                 
                 // no op

             }*/
        });
	    },
	    failure:function (id, result) {
			$('#MP_ERROR').css("display","inline-block");
		},error:function (id, result) {
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
    }
  });
  fd.modules.common.forms.register({
    id: "payment_loadPaymentMethod",
    success: function (id, result) {
      if (id && result && result.paymentEditValue) {
        fd.expressco.addpaymentmethodpopup.open(null, result.paymentEditValue);
      }
    }
  });

  fd.modules.common.utils.register('expressco', 'paymentmethod', paymentMethod, fd);
}(FreshDirect));
