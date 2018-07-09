/*global expressco*/
var FreshDirect = FreshDirect || {};

// module initialization
(function (fd) {
  'use strict';

  var DISPATCHER = fd.common.dispatcher;
  var WIDGET = fd.modules.common.widget;
  var FORMS = fd.modules.common.forms;
  var $ = fd.libs.$;

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
      if(data.reasonFailures){
        data.reasonFailures.forEach(function(cartline){
          $("[data-component='cartline'][data-cartlineid='" + cartline + "']").addClass('invalid').addClass('reason');
        });
      }
    },
    releaseLockWhenNotRedirecting:function(id, data){
      var hasRedirectUrl = data && data.redirectUrl && data.redirectUrl.length;
      if(!hasRedirectUrl){
        FORMS.releaseLockFormResubmit(FORMS.getEl(id));
      }
    }
  });

  $(document).on('click', '[data-apply-button]', function() {
    var $selects = $('select[data-component="complaintreason"]');
    var $currentSelect = $(this).parent().find('select[data-component="complaintreason"]');

    $selects.each(function() {
      $(this).val($currentSelect.val());
    });
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

  var cartCarousel = Object.create(WIDGET,{
    signal:{
      value:'carouselData'
    },
    template: {
      value:common.viewCartTabbedCarousel
    },
    placeholder:{
      value:'#cartCarousels'
    }
  });
  cartCarousel.listen();

  fd.modules.common.utils.register('expressco', 'viewCartWarningMessage', warningMessage, fd);
}(FreshDirect));
