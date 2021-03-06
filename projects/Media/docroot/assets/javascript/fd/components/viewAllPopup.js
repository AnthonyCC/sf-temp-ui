/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$;
  var viewallPopup = Object.create(fd.modules.common.popupWidget, {
    template: {
      value: common.viewAllPopup
    },
    bodyTemplate: {
      value: function (data) {
        var spinner = '<div class="spinner"></div>';
        var nestedData = $.extend({}, data.data, {config: data.data.config, data: data.data.data});
        return data.data.config ? common.contentModules(nestedData) : spinner;
      }
    },
    trigger: {
      value: '[data-view-all-popup]'
    },
    closeTrigger: {
      value: '[data-close-viewall-popup]'
    },
    popupId: {
      value: 'viewallPopup'
    },
    popupConfig: {
      value: {
        align:false,
        overlay:true,
        overlayExtraClass:'white-popup-overlay',
        hideOnOverlayClick: true,
        zIndex:460
      }
    },
    hasClose: {
      value: true
    },
    doCriteoImpressionBeacons: {
    	value: function(e) {
    		$('#'+this.popupId).find('[data-hooklogic-beacon-impress]').each(function(i,e) {
    			$(e).append('<img src="'+$(e).attr('data-hooklogic-beacon-impress')+'" alt="" style="display: none;" />');
    			$(e).attr('data-hooklogic-beacon-impress', null); //remove
    		});
    	}
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

        this.refreshBody();
        this.popup.show($t);
        this.popup.clicked = true;

        $(window).scroll(closeTransactionalPopup);
      }
    },
    signal:{
      value: 'moduleContent'
    },
    close: {
        value: function (e) {
          if (this.popup) {
            this.popup.hide(e);
            $(window).off('scroll', closeTransactionalPopup);
          }
        }
    },
    callback:{
      value:function( data ) {
        this.refreshBody(data);

        fd.common.dispatcher.signal('productImpressions', { el: $('#'+this.popupId), type: 'impressionsViewAll'});
        
        this.doCriteoImpressionBeacons();
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
    viewallPopup.openPopup(e);
    e.preventDefault();
    e.stopPropagation();
  });
  $(document).on('click', viewallPopup.closeTrigger, viewallPopup.close.bind(viewallPopup));

  fd.modules.common.utils.register("components", "viewallPopup", viewallPopup, fd);
}(FreshDirect));
