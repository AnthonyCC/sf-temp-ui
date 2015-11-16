var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var aria = Object.create(fd.common.signalTarget, {
    modules: {
      value: [
          "modules.common.popupWidget",
          "components.confirmpopup",
          "components.customizePopup",
          "modules.common.addtolistpopup",
          "modules.common.createlistpopup"
        ]
    },
    signal: {
      value: 'aria'
    },
    callback: {
      value: function () {
        this.decorate();
      }
    },
    decorate: {
      value: function () {
        this.modules.forEach(function (moduleName) {
          var module = fd.utils.discover(moduleName, fd);

          if (module && module.decorate) {
            module.decorate();
          }
        });
      }
    },
    initModule: {
      value: function () {
        this.decorate();
        this.listen();
      }
    }
  });

  fd.modules.common.utils.registerModule("modules.common", "aria", aria, fd);
  $(document).ready(function () {
    fd.utils.initModule("modules.common.aria", fd);
  });

}(FreshDirect));
