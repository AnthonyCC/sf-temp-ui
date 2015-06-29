/*global expressco*/
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  'use strict';

  var DISPATCHER = fd.common.dispatcher;
  var WIDGET = fd.modules.common.widget;
  var FORMS = fd.modules.common.forms;

  // main viewcart form
  FORMS.register({
    id: 'viewcart_startcheckout',
    submit: function (e) {
      e.preventDefault();
      e.stopPropagation();

      DISPATCHER.signal('server', {
        url: '/api/expresscheckout/cartdata?action=startCheckout',
        method: 'GET',
        data: {
          data: JSON.stringify({
            fdform: 'viewcart_startcheckout',
            formdata: {
            }
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

  var warningMessage = Object.create(WIDGET, {
      signal: {
        value: 'viewCartHeaderMessage'
      },
      placeholder:{
        value:'#warningmessage'
      },
      template:{
        value:expressco.warningmessage
      }
  });
  warningMessage.listen();

  fd.modules.common.utils.register('expressco', 'viewCartWarningMessage', warningMessage, fd);
}(FreshDirect));
