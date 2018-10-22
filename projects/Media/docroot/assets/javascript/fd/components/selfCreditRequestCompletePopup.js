/*global jQuery, common*/

var FreshDirect = window.FreshDirect || {};

(function (fd) {
  "use strict";
  var $ = fd.libs.$;
  var DISPATCHER = fd.common.dispatcher;

  var selfCreditRequestCompletePopup = Object.create(fd.modules.common.overlayWidget, {
    bodyTemplate: {
      value: function () {
        return common.selfCreditRequestComplete();
      }
    },
    headerTemplate: {
      value: function () {
        return '';
      }
    },
    trigger: {
      value: '[data-selfcreditrequestcomplete-popup]'
    },
    closeTrigger: {
      value: '[data-close-selfcreditrequestcomplete]'
    },
    overlayId: {
      value: 'selfCreditRequestComplete'
    },
    ariaDescribedby: {
      value: 'creditRequestCompleteHeader'
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
        this.overlayEl.attr('aria-describedby', 'creditRequestCompleteHeader creditRequestCompleteDetails');
        this.OKButton = $('.cssbutton.selfcredit-button.ok-button');
        setTimeout(function() {
          this.OKButton.focus();
        }.bind(this), 200);
        // set close callback
        this.overlayEl.attr('data-close-cb', 'FreshDirect.components.selfCreditRequestCompletePopup.closeCB');
      }
    },
    closeCB: {
      value: function () {
        DISPATCHER.signal('selfCreditRequestCompletePopupClosed', {});
      }
    },
    closePopup: {
      value: function (e) {
        this.close(e);
      }
    }
  });

  fd.modules.common.utils.register("components", "selfCreditRequestCompletePopup", selfCreditRequestCompletePopup, fd);
}(FreshDirect));
