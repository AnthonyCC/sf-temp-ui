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
        return data.data.config ? common.contentModules({config: data.data.config, data: data.data.data}) : spinner;
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
        zIndex:450
      }
    },
    hasClose: {
      value: true
    },
    openPopup:{
      value: function (e) {
        var moduleId = e.currentTarget.getAttribute('data-moduleId');
        fd.common.dispatcher.signal('server',{
    			url:'/api/modulehandling/load?moduleId=' + moduleId + '&viewAll=true'
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
        return this.refreshBody(data);
      }
    }
  });

  function closeTransactionalPopup () {
    var transactionalPopup = $(document).find('.transactional-popup.shown')[0];
    $(transactionalPopup).removeClass('shown');
  }

  viewallPopup.listen();

  $(document).on('click', viewallPopup.trigger, viewallPopup.openPopup.bind(viewallPopup));
  $(document).on('click', viewallPopup.closeTrigger, viewallPopup.close.bind(viewallPopup));

  fd.modules.common.utils.register("components", "viewallPopup", viewallPopup, fd);
}(FreshDirect));
