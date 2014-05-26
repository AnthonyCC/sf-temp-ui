/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$,
      utils = FreshDirect.modules.common.utils;
  
  var navigationHighlighter = {
    globalComponent: "[data-component='globalnav']",
    highlightNavItem : function(){
      var isTopNavLink = this.setHighlightById(this.onGlobalNavItem).length || this.setHighlightByFullUrl(this.onGlobalNavItem).length;

      if(!isTopNavLink){
        this.setHighlightById(this.onFooterItem).length || this.setHighlightByFullUrl(this.onFooterItem).length;
      }
    },
    setHighlightById : function(componentFn){
        var locId = this.substractIdFromLocationString();
        console.log("location id: " + locId);
        
        return componentFn.bind(navigationHighlighter)("[data-id=" + locId + "]");
    },
    setHighlightByFullUrl : function(componentFn){
        var locId = this.getFullUrl();
        console.log("location id: " + locId);

        return componentFn.bind(navigationHighlighter)("a[href='" + locId + "']");
    },
    onGlobalNavItem : function(location){
      return $(this.globalComponent).find(location).find("a").attr("data-highlight", "on");
    },
    onFooterItem : function(location){
      return $(".gnav-footer").find(location).attr("data-highlight", "on");
    },
    substractIdFromLocationString : function(){
      return utils.getParameterByName("id") || utils.getParameterByName("deptId") || utils.getParameterByName("catId");
    },
    getFullUrl : function(){
      return window.location.pathname + window.location.search;
    },
    getCurrentNavigationPoint : function(){
      return "veg";
    }
  };

  $(function(){
    navigationHighlighter.highlightNavItem.bind(navigationHighlighter)();
  
    fd.modules.common.utils.register("components", "navigationHighlighter", navigationHighlighter, fd);
  });

}(FreshDirect));
