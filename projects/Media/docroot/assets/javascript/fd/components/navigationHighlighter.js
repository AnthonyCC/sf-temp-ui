/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$,
      utils = FreshDirect.modules.common.utils;
  
  var navigationHighlighter = {
    topNavCurrentLocation: FreshDirect.globalnav && FreshDirect.globalnav.curNav ? FreshDirect.globalnav.curNav : "",
    bottomNavCurrentLocation: FreshDirect.globalnav && FreshDirect.globalnav.botNav ? FreshDirect.globalnav.botNav : "",
    globalComponent: "[data-component='globalnav']",
    footerComponent: "[data-component='footer']",
    highlightNavItem : function(){
    /*
      console.log("top loc: " + this.topNavCurrentLocation);
      console.log("bottom loc: " + this.bottomNavCurrentLocation);
    */

      this.setHighlightById(this.onGlobalNavItem, this.topNavCurrentLocation);

      if(!this.isHighlighted()){
        this.setHighlightById(this.onFooterNavItem, this.bottomNavCurrentLocation);
      }
    },
    setHighlightById : function(componentFn, location){
        return componentFn.call(navigationHighlighter, "[data-id='" + location + "']");
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
