/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$,
      utils = FreshDirect.modules.common.utils;
  
  var navigationHighlighter = {
    currentLocation: FreshDirect.globalnav && FreshDirect.globalnav.botNav ? FreshDirect.globalnav.botNav : "",
    globalComponent: "[data-component='globalnav']",
    footerComponent: "[data-component='footer']",
    highlightNavItem : function(){
      // console.log("cur loc: " + this.currentLocation);

      this.setHighlightById(this.onGlobalNavItem);

      if(!this.isHighlighted()){
        this.setHighlightById(this.onFooterNavItem);
      }
    },
    setHighlightById : function(componentFn){
        return componentFn.call(navigationHighlighter, "[data-id='" + this.currentLocation + "']");
    },
    onGlobalNavItem : function(location){
      return $(this.globalComponent).find(location).find("a").attr("data-highlight", "on");
    },
    onFooterNavItem : function(location){
      return $(this.footerComponent).find(location).attr("data-highlight", "on");
    },
    isHighlighted : function(){
      return $(document.body).find("[data-highlight='on']").length > 0;
    }
  };

  $(function(){
    navigationHighlighter.highlightNavItem.bind(navigationHighlighter)();
  
    fd.modules.common.utils.register("components", "navigationHighlighter", navigationHighlighter, fd);
  });

}(FreshDirect));
