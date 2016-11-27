var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;

  var FDFeatures = function (fd) {
    return {
      initFeatures: function () {
        var features = fd.utils.getActiveFeatures(),
            developer = fd.utils.readCookie('developer');

        if (features) {
          Object.keys(features).forEach(function (feature) {
            document.body.setAttribute('data-feature-'+feature, features[feature]);
          });
        }

        if (developer) {
          document.body.setAttribute('developer', developer);
        }
      },
      initModule: function () {
        // initialize A/B testing feature framework
        $(document).ready(function () {
          this.initFeatures();
        }.bind(this));
      }
    };
  };

  fd.utils.registerModule("modules.common", "features", FDFeatures, fd, "Features", "1.0");
  fd.utils.initModule("modules.common.features", fd);

}(FreshDirect));
