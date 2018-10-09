/*global jQuery, common*/

var FreshDirect = window.FreshDirect || {};

(function (fd) {
  "use strict";
  var $ = fd.libs.$;
  var DISPATCHER = fd.common.dispatcher;

  var selfCreditRequestErrorPopup = Object.create(fd.modules.common.overlayWidget, {
    bodyTemplate: {
      value: function () {
        return common.selfCreditRequestError();
      }
    },
    headerTemplate: {
      value: function () {
        return '';
      }
    },
    trigger: {
      value: '[data-selfcreditrequesterror-popup]'
    },
    closeTrigger: {
      value: '[data-close-selfcreditrequesterror]'
    },
    overlayId: {
      value: 'selfCreditRequestError'
    },
    ariaDescribedby: {
      value: 'creditRequestErrorHeader'
    },
    ariaLabelledby: {
      value: ''
    },
    overlayConfig: {
      value: {
        zIndex: 460
      }
    },
    customClass: {
      value: 'overlay-medium selfcredit-overlay'
    },
    openPopup: {
      value: function () {
        this.render(this.data);
        this.overlayEl = $('#'+this.overlayId);
        this.overlayEl.attr('aria-describedby', 'creditRequestErrorDetails');
        this.OKButton = $('.cssbutton.selfcredit-button.ok-button');
        setTimeout(function() {
          this.OKButton.focus();
        }.bind(this), 200);
        // set close callback
        this.overlayEl.attr('data-close-cb', 'FreshDirect.components.selfCreditRequestErrorPopup.closeCB');
      }
    },
    closeCB: {
      value: function () {
        DISPATCHER.signal('selfCreditRequestErrorPopupClosed', {});
      }
    },
    closePopup: {
      value: function (e) {
        this.close(e);
      }
    }
  });

  fd.modules.common.utils.register("components", "selfCreditRequestErrorPopup", selfCreditRequestErrorPopup, fd);
}(FreshDirect));
