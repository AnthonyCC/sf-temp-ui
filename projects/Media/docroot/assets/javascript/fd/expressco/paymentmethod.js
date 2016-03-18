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
	    }
	  });
  
  fd.modules.common.forms.register({
	    id: "PP",
	    success: function (id, result) {
	    	//alert("PP Success !!!- token"+result.eWalletResponseData.token);
	    	var x = document.getElementById("PP_button");
	    	
	    	var deviceObj = "";
	    	braintree.setup(result.eWalletResponseData.token, "custom", {
	    		  dataCollector: {
	    			    paypal: true
	    			  },
	    		  onReady: function (integration) {
	    			 // alert("integration.deviceData :"+integration.deviceData);
	    		    checkout = integration;
	    		    checkout.paypal.initAuthFlow();
	    		    deviceObj = JSON.parse(integration.deviceData);
	    		   
	    		   // x.addEventListener("click", mySecondFunction(checkout));
	    		  },
	    		  onPaymentMethodReceived: function (payload) {
	    		    // retrieve nonce from payload.nonce
	    		    //alert("I'm in onPaymentMethodReceived!!! "+ payload.nonce );
	    		   // alert("nonce:"+payload.nonce);
	    		/*    document.getElementById('payemntMethodNonce').value = payload.nonce ;
                     var submitBtn = document.getElementById("addpaymentmethod_paypal_vaultToken");
                     submitBtn.submit();
                     
                      deviceId = integration.deviceData;
                     ,\"deviceId\":\""+deviceId+"\"  
                     */
	    	        $.ajax({
	                      url:"/api/expresscheckout/addpayment/ewalletPayment?data={\"fdform\":\"EPP\",\"formdata\":{\"action\":\"PP_Pairing_End\",\"ewalletType\":" +
	                      		"\"PP\",\"paymentMethodNonce\":\""+payload.nonce+"\",\"email\":\""+payload.details.email+"\",\"firstName\":\""+payload.details.firstName+"\"," +
	                      				"\"lastName\":\""+payload.details.lastName+"\" ,\"deviceId\":\""+deviceObj.correlation_id+"\"}}",
	                      type: 'post',
	                      success: function(id, result){
	                    	 // alert("AJAX Call Success !!!!");
	                        // alert("AJAX Call Success !!!!"+id.submitForm.result.eWalletResponseData.paymentMethod.pk.id ); 
	                        /* $.ajax({
	   	                      url:"/api/expresscheckout/payment?data={\"fdform\":\"payment\",\"formdata\":{\"action\":\"selectPaymentMethod\",\"id\":\""+id.submitForm.result.eWalletResponseData.paymentMethod.pk.id+"\"}}",
	   	                      type: 'post',
	   	                      success: function(id, result){ 
	   	                         alert("Success !!!!");
	   	                      //if (id && fd.expressco.drawer) {
	   	                       //fd.expressco.drawer.reset();
	   	                         paymentMethod.listen();
	   	                    // }
	   	                   alert("Success End !!!!");
	   	                      }
	   	    	        });*/
	                    	 //location.reload(true);
	                    	 window.location.assign("/expressco/checkout.jsp");
	                      }
	    	        });
	    		    

	    		  },
	    		  paypal: {
	    		    singleUse: false,
	    		    /* amount: 10.00,
	    		    currency: 'USD',
	    		    locale: 'en_us',
	    		    enableShippingAddress: true, */
	    		    headless: true
	    		  }
	    		  
	    		});
	    			/*alert("I'm in End!!");
	    	
	    			function mySecondFunction(checkout){
	    				alert("mySecondFunction!!!");
	    					  checkout.paypal.initAuthFlow();
	    					  alert("I'm in End -mySecondFunction!!");
	    			}

	    			function myFunction(nonce){
	    				alert("myFunction!!!");
	    					  //checkout.paypal.initAuthFlow();
	    					  alert("nonce:"+nonce);
	    			}*/

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
