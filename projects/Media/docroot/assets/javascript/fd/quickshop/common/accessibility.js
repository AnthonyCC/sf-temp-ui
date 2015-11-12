/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$;
  var DISPATCHER = fd.common.dispatcher;

  $(document).ready(function(e){
    var currentTab = $("[role='tab'].selected")[0];
    fd.components.accessibleTabs.updateSelectedTab(currentTab);
    fd.components.accessibleTabs.updateTabContentLabelledBy(currentTab);

    console.log('quickshop tab accessibility set up');
  });

  // fd.modules.common.utils.register("quickshop.common", "accessibility", {}, fd);
}(FreshDirect));
