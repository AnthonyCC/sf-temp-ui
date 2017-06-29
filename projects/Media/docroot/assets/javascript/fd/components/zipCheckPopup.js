/*global jQuery, common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$,
      DISPATCHER = fd.common.dispatcher,
      zipCheckPopup = Object.create(fd.modules.common.popupWidget, {
    template: {
      value: common.fixedPopup
    },
    bodyTemplate: {
      value: function(data) {
        return common.zipcheck({data: data.data});
      },
      writable: true
    },
    trigger: {
      value: '[data-zipcheck-popup]'
    },
    closeTrigger:{
      value: '[data-close-zipcheck]'
    },
    popupId: {
      value: 'zipCheckPopup'
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
    openPopup:{
      value: function (e) {
        var $t = e && $(e.currentTarget) || $(document.body);

        this.refreshBody(FreshDirect.user);
        this.popup.show($t);
        this.popup.clicked = true;
      }
    },
    zipchekRetry:{
      value: function () {
        this.refreshBody(null,common.zipcheck);
      }
    },
    closeCB: {
      value: function () {
        DISPATCHER.signal('zipCheckClosed', {});
      }
    }
  });

  $(document).on('click', '[data-zipcheck-again]', zipCheckPopup.zipchekRetry.bind(zipCheckPopup));
  $(document).on('click', zipCheckPopup.closeTrigger, zipCheckPopup.close.bind(zipCheckPopup));

  fd.modules.common.utils.register("components", "zipCheckPopup", zipCheckPopup, fd);
}(FreshDirect));

if (FreshDirect.user && !FreshDirect.user.isZipPopupUsed) {
  FreshDirect.libs.$( document ).ready( function () {
    setTimeout( function () {
      FreshDirect.components.zipCheckPopup.openPopup();
    }, 10);
  });
}
