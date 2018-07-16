/*global expressco*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
  'use strict';

  var $=fd.libs.$;
  var POPUPWIDGET = fd.modules.common.popupWidget;
  var DISPATCHER = fd.common.dispatcher;
  var deliverypasspopup = Object.create(POPUPWIDGET,{
    headerContent: {
      value: ''
    },
    customClass: {
      value: 'deliverypasspopup'
    },
    hideHelp: {
      value: true
    },
    hasClose: {
      value: true
    },
    $trigger: {
      value: null
    },
    trigger: {
      value: '[data-component="deliverypasspopup"]'
    },
    toggleTrigger: {
      value: '[data-component="deliverypasspopup-toggler"]'
    },
    bodySelector:{
      value: '.ec-popup-content'
    },
    signal: {
      value: 'deliveryPass'
    },
    scrollCheck: {
      value: '.ec-popup'
    },
    template: {
      value: expressco.eccenterpopup
    },
    bodyTemplate: {
      value: expressco.deliverypasspopup
    },
    popupId: {
      value: 'deliverypasspopup'
    },
    popupConfig: {
      value: {
        zIndex: 2000,
        openonclick: true,
        overlayExtraClass: 'centerpopupoverlay',
        align: false
      }
    },
    close: {
      value: function () {
        if (this.popup) { this.popup.hide(); }

        return false;
      }
    },
    open: {
      value: function (e, data) {
        e.preventDefault();
        e.stopPropagation();

        var $t = e && $(e.currentTarget) || $(document.body);

        if (data) {
          this.refreshBody(data);
        }
        this.popup.show($t);
        this.popup.clicked = true;

        this.noscroll(true);
      }
    },
    render: {
      value: function (data) {
        POPUPWIDGET.render.call(this, data);
        this.adjustBoxes();
      }
    },
    adjustBoxes: {
      value: function () {
        var $boxes = $('#'+this.popupId+' .deliverypasspopup__option'),
            hMax = 0;

        $boxes.each(function (i, el) {
          hMax = Math.max($(el).outerHeight(), hMax);
        });

        if (hMax > 0) {
          $boxes.css('height', hMax);
        }
      }
    },
    loadData: {
      value: function (e) {
        this.open(e);
        e.preventDefault();

        DISPATCHER.signal('server', {
          url: '/api/expresscheckout/deliverypass',
          method: 'GET'
        });
      }
    },
    toggle: {
      value: function (e) {
        e.preventDefault();
        e.stopPropagation();

        $('#' + deliverypasspopup.popupId + ' [data-component="deliverypasspopup-page"]').each(function(){
          $(this).toggle();
        });
      }
    }
  });

  deliverypasspopup.listen();
  deliverypasspopup.render();

  fd.modules.common.forms.register({
    id: "deliverypass",
    submit: function (e) {
      var product = $('[data-product-id="' + $('[data-selected-product-id]').attr('data-selected-product-id') + '"]');
      if (product.size()) {
    	if(!!$('form.deliverypass_form').attr('data-dlvpasscart') == true){
    		fd.deliveryPassSelectedTitle = $('form.deliverypass_form').attr('data-selected-dp-title');
    		fd.components.AddToCart.addToCart(product, {"dlvPassCart":"true"});
    	} else {
    		fd.components.AddToCart.addToCart(product);
    	}
      } else {
        deliverypasspopup.close();
      }
      e.preventDefault();
    }
  });

  $(document).on('click', deliverypasspopup.trigger, deliverypasspopup.loadData.bind(deliverypasspopup));
  $(document).on('click', '#' + deliverypasspopup.popupId + ' .close', deliverypasspopup.close.bind(deliverypasspopup));
  $(document).on('click', '#' + deliverypasspopup.popupId + '.centerpopup-helper', function (e) {
    if ($(e.target).hasClass('centerpopup-helper')) {
      deliverypasspopup.close();
    }
  });
  $(document).on("click", "[data-deliverypass-terms]", function(){
	  pop("/shared/template/generic_popup.jsp?contentPath=/media/editorial/picks/deliverypass/dp_tc.html&windowSize=large&name=Delivery Pass Information',400,560,alt='Delivery Pass Information");
  });
  $(document).on('click', deliverypasspopup.toggleTrigger, deliverypasspopup.toggle.bind(deliverypasspopup));
  $(document).on('click', "[data-component='deliverypassfreetrialpopup']", function (e) {
	dataLayer.push({
      'event': 'deliverypass-click',
      'eventCategory': 'deliverypass',
      'eventAction': 'free delivery',
      'eventLabel': 'free'
    });
    doOverlayDialogNew('/includes/freetrial_popup.jsp');
  });
  
  fd.modules.common.utils.register('expressco', 'deliverypasspopup', deliverypasspopup, fd);
}(FreshDirect));
