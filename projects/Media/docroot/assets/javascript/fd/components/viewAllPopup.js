/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$;
  var viewallPopup = Object.create(fd.modules.common.overlayWidget, {
    template: {
      value: common.viewAllPopup
    },
    bodyTemplate: {
      value: function (data) {
        var spinner = '<div class="spinner"></div>';

        if (data.data.config) {
          data.data.config[0].dontFocusForm = true;
        }

        return data.data.config ? common.contentModules({config: data.data.config, data: data.data.data}) : spinner;
      }
    },
    headerTemplate: {
      value: function (data) {
        var header = data.header ? data.header : '';
        return header;
      }
    },
    trigger: {
      value: '[data-view-all-popup]'
    },
    overlayId: {
      value: 'viewallPopup'
    },
    ariaDescribedby:{
      value:'vieaw-all'
    },
    ariaLabelledby:{
      value:''
    },
    overlayConfig: {
      value: {
        zIndex:460
      }
    },
    customClass: {
      value: 'fullscreen'
    },
    openPopup:{
      value: function (e) {
        var moduleId = e.currentTarget.getAttribute('data-module-id'),
            iconId = e.currentTarget.getAttribute('data-icon-id'),
            moduleVirtualCategory = e.currentTarget.getAttribute('data-module-virtual-category'),
            notProductList = e.currentTarget.hasAttribute('data-not-product-list'),
            url = '';

        if (moduleId) {
          url = '/api/modulehandling/load?moduleId=' + moduleId + '&viewAll=' + !notProductList +'&moduleVirtualCategory=' + moduleVirtualCategory;
        } else {
          url = '/api/modulehandling/imageproduct?iconId=' + iconId + '&moduleVirtualCategory=' + moduleVirtualCategory;
        }

        fd.common.dispatcher.signal('server',{
    			url: url
    		});
        var $t = e && $(e.currentTarget) || $(document.body);

        this.refresh();

        $(window).scroll(closeTransactionalPopup);
      }
    },
    signal:{
      value: 'moduleContent'
    },
    close: {
        value: function (e) {
          if (this.overlay) {
            this.overlay.close(e);
            $(window).off('scroll', closeTransactionalPopup);
          }
        }
    },
    callback:{
      value:function( data ) {
        return this.refresh(data);
      }
    }
  });

  function closeTransactionalPopup () {
    var transactionalPopup = fd.common.transactionalPopup;
    transactionalPopup.popup.lastFocused = null;
    transactionalPopup.close();
  }

  viewallPopup.listen();

  $(document).on('click', viewallPopup.trigger, function (e) {
    e.preventDefault();
    e.stopPropagation();
    viewallPopup.openPopup(e);
  });


  fd.modules.common.utils.register("components", "viewallPopup", viewallPopup, fd);
}(FreshDirect));
