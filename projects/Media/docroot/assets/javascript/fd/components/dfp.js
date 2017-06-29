var FreshDirect = FreshDirect || {};
var googletag = window.googletag || {};

// load DFP
(function(fd) {
  "use strict"
  if (fd.properties.isDFPEnabled) {
    var gads = document.createElement('script'),
        useSSL = 'https:' == document.location.protocol,
        node = document.getElementsByTagName('script')[0];

    googletag.cmd = googletag.cmd || [];

    gads.async = true;
    gads.type = 'text/javascript';
    gads.src = (useSSL ? 'https:' : 'http:') + '//www.googletagservices.com/tag/js/gpt.js';
    node.parentNode.insertBefore(gads, node);
  }

}(FreshDirect));

//load ad slots
(function (fd) {
  "use strict";

  if (fd.properties.isDFPEnabled) {
    var $=fd.libs.$,
        slots=document.querySelectorAll("[id^=oas_]"),
        attributes=fd.utils.getParameters(DFP_query),
        dfpId= fd.properties.dfpId || '1072054678';

    function getDfpSlotNameFromOasName(slot) {
      return slot.id.substring(4,slot.id.length);
    }

    googletag.cmd = googletag.cmd || [];
    googletag.cmd.push(function() {
      googletag.pubads().enableSingleRequest();
      googletag.enableServices();

      Object.keys(attributes).forEach(function (attribute) {
        googletag.pubads().setTargeting(attribute, attributes[attribute]);
      });

      slots.forEach(function (slot,index) {
        var size = [[700, 250], [728, 90], [186, 216]];
        if (slot.getAttribute('ad-size-width') && slot.getAttribute('ad-size-height')) {
          size = [+slot.getAttribute('ad-size-width'), +slot.getAttribute('ad-size-height')];
        }
        googletag.defineSlot('/'+ dfpId +'/'+ getDfpSlotNameFromOasName(slot), size, slot.id).addService(googletag.pubads())
        .setCollapseEmptyDiv(true)
        googletag.display(slot.id);
      });
    });
  }
}(FreshDirect));
