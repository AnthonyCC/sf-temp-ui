/*global jQuery*/
var FreshDirect = FreshDirect || {};

(function (fd) {
  "use strict";

  var $ = fd.libs.$,
      latestElem = null;

  if (fd && fd.homepage && fd.homepage.data && fd.homepage.data.isHomepage) {
    setTimeout(function() {
      var modules = $(document).find('.content-module'),
          triggerPositioning = [],
          triggerIndex = [];

      function cmTrigger() {
        if (triggerPositioning[0] < window.scrollY){
          FreshDirect.components.coremetrics.playOneItem(['cmCreateElementTag',triggerIndex[0] + '. elements', 'Home Page Scrolling','-_--_--_--_--_-' + FreshDirect.user.cohortName]);
          triggerPositioning.shift();
          triggerIndex.shift();
        }
        if (triggerPositioning.length < 1) {
          $(window).off('scroll', cmTrigger)
        }
      }

      for(var i =1; i < modules.length; i += 2) {
        var elem = $(modules[i])[0].getBoundingClientRect(),
            position = elem.bottom - $(window).height();
        if (position > 0) {
          triggerPositioning.push(position);
          triggerIndex.push(i + 1);
        }
      }

      $(window).scroll(cmTrigger);
    },1000);
  }
}(FreshDirect));
