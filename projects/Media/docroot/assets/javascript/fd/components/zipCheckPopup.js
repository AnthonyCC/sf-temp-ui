/*global jQuery, common*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $=fd.libs.$,
      DISPATCHER = fd.common.dispatcher,
      zipCheckPopup = Object.create(fd.modules.common.overlayWidget, {
    bodyTemplate: {
      value: function(data) {
        return common.zipcheck({data: data.data, isCorporateUser: fd.user.isCorporateUser});
      }
    },
    headerTemplate: {
      value: function (data) {
        var header = data.header ? data.header : '';
        return header;
      }
    },
    trigger: {
      value: '[data-zipcheck-popup]'
    },
    closeTrigger:{
      value: '[data-close-zipcheck]'
    },
    overlayId: {
      value: 'zipCheckPopup'
    },
    ariaDescribedby:{
      value:'zipcheck'
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
      value: 'overlay-medium'
    },
    openPopup:{
      value: function (e) {
        var $t = e && $(e.currentTarget) || $(document.body);

        this.refresh(FreshDirect.user);

        // set close callback
        $('#'+this.overlayId).attr('data-close-cb', 'FreshDirect.components.zipCheckPopup.closeCB');
      }
    },
    zipchekRetry:{
      value: function () {
        this.refresh(null, common.zipcheck);
      }
    },
    closeCB: {
      value: function () {
        DISPATCHER.signal('zipCheckClosed', {});
      }
    }
  });

  $(document).on('click', '[data-zipcheck-again]', zipCheckPopup.zipchekRetry.bind(zipCheckPopup));

  fd.modules.common.utils.register("components", "zipCheckPopup", zipCheckPopup, fd);
}(FreshDirect));

if (FreshDirect.user && !FreshDirect.user.isZipPopupUsed) {
  FreshDirect.libs.$( document ).ready( function () {
    setTimeout( function () {
      FreshDirect.components.zipCheckPopup.openPopup();
    }, 10);
  });
}
