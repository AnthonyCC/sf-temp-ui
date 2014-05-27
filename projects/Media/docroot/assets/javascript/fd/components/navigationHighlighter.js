/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$,
      utils = FreshDirect.modules.common.utils;
  
  var navigationHighlighter = {
    globalComponent: "[data-component='globalnav']",
    footerComponent: "[data-component='footer']",
    highlightNavItem : function(){
      this.setHighlightById(this.onGlobalNavItem).length || this.setHighlightByFullUrl(this.onGlobalNavItem).length;

      if(!this.isHighlighted()){
        this.setHighlightOnFooter();
      }
    },
    setHighlightById : function(componentFn){
        var locId = this.substractIdFromLocationString();
        
        return componentFn.call(navigationHighlighter, "[data-id=" + locId + "]");
    },
    setHighlightByFullUrl : function(componentFn){
        var locId = this.getFullUrl();

        return componentFn.call(navigationHighlighter, "a[href='" + locId + "']");
    },
    setHighlightOnFooter : function(){
      var locId = this.getFullUrl();

      if(!locId || locId === "/"){
        return;
      }

      $(this.footerComponent).find("a").each(function(e){
        var exp = $(this).data('hlregex'); 

        if(exp && locId.match(exp)){
         $(this).attr('data-highlight', 'on'); 
        }
      });
    },
    onGlobalNavItem : function(location){
      return $(this.globalComponent).find(location).find("a").attr("data-highlight", "on");
    },
    substractIdFromLocationString : function(){
      return utils.getParameterByName("id") || utils.getParameterByName("deptId") || utils.getParameterByName("catId");
    },
    getFullUrl : function(){
      return window.location.pathname + window.location.search;
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
