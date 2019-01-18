/*global common*/

var FreshDirect = window.FreshDirect || {};

(function (fd) {
  "use strict";

  var warningOverlay = Object.create(fd.modules.common.overlayWidget, {
    signal: {
      value: ['warning', 'warnings']
    },
    bodyTemplate: {
      value: common.warning
    },
    headerTemplate: {
      value: function () {
        return '<div id="warningHeader">Warning!</div>';
      }
    },
    overlayId: {
      value: 'warningOverlay'
    },
    ariaLabelledby:{
      value: 'warningHeader'
    },
    ariaDescribedby: {
      value: 'warningMessage'
    },
    overlayConfig: {
      value: {
        zIndex: 1000
      }
    },
    customClass: {
      value: 'overlay-medium warning-overlay'
    }
  });
  warningOverlay.listen();

  fd.modules.common.utils.register("common", "warningOverlay", warningOverlay, fd);
}(FreshDirect));
