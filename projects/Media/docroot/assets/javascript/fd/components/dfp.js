var FreshDirect = FreshDirect || {};
var googletag = window.googletag || {};

// load DFP
(function(fd) {
  "use strict";
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

  /* using strict means this function declaration needs to not be nested */
  function getDfpSlotNameFromOasName(slot) {
    return slot.id.substring(4,slot.id.length);
  }

  if (fd.properties.isDFPEnabled) {
    var $=fd.libs.$,
        slots=document.querySelectorAll("[id^=oas_]"),
        attributes=fd.utils.getParameters(DFP_query),
        dfpId= fd.properties.dfpId || '1072054678';

    googletag.cmd = googletag.cmd || [];
    googletag.cmd.push(function() {
      googletag.pubads().enableSingleRequest();
      googletag.enableServices();

      Object.keys(attributes).forEach(function (attribute) {
        googletag.pubads().setTargeting(attribute, attributes[attribute]);
      });

      slots.forEach(function (slot,index) {
        var size = [[186, 216], [228, 275], [240, 154], [273, 60], [310, 200], [349, 200], [480, 279], [480, 480], [578, 216], [683, 250], [774, 95], [970, 100], [970, 329], [970, 80]];
        if (slot.getAttribute('ad-size-width') && slot.getAttribute('ad-size-height')) {
          size = [+slot.getAttribute('ad-size-width'), +slot.getAttribute('ad-size-height')];
        }
        googletag.defineSlot('/'+ dfpId +'/'+ getDfpSlotNameFromOasName(slot), size, slot.id).addService(googletag.pubads())
        .setCollapseEmptyDiv(true,true);
        googletag.display(slot.id);
      });
    });
  }
}(FreshDirect));
