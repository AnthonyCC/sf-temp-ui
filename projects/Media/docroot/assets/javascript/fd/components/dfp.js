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

  if (fd.properties.isDFPEnabled) {

    var fixedSizeSlots = document.querySelectorAll('[ad-fixed-size]');
    for (var i = 0; i < fixedSizeSlots.length; ++i) {
      var height = fixedSizeSlots[i].getAttribute('ad-size-height');
      var width = fixedSizeSlots[i].getAttribute('ad-size-width');
      if (height && width) {
        fixedSizeSlots[i].style.height = height + 'px' ;
        fixedSizeSlots[i].style.width = width + 'px';
      }
    }

    function getDfpSlotNameFromOasName(slot) {
      return slot.id.substring(4,slot.id.length);
    }

    function getTimeslotsData() {
    		var week = document.querySelectorAll('.tsDayDateC');
    		var weekList = [];
    		for (var i = 0; i < 7; ++i) {
    			var day = 'day' + i;
    			var text = week[i].innerText.replace(/\s/g, '');
          var dayText = {};
          dayText[day] = text;
          weekList.push(dayText);
    		}
    		return weekList;
    };

    function getSpecialTargetingKey(slot) {
      return !!document.querySelector("[id^=oas_" + slot + "]");
    }

    var $=fd.libs.$,
        slots=document.querySelectorAll("[id^=oas_]"),
        query = DFP_query,
        dfpId= fd.properties.dfpId || '1072054678'

    if (getSpecialTargetingKey("DFPDeliveryTimeslotMessage")) {
      query = fd.utils.setParameters(DFP_query, getTimeslotsData());
    }

    var attributes = fd.utils.getParameters(query)
    googletag.cmd = googletag.cmd || [];
    googletag.cmd.push(function() {
      googletag.pubads().enableSingleRequest();
      googletag.enableServices();
      Object.keys(attributes).forEach(function (attribute) {
        googletag.pubads().setTargeting(attribute, attributes[attribute]);
      });

      slots.forEach(function (slot,index) {
        var size = ['fluid', [186, 216], [228, 275], [240, 154], [273, 60], [310, 200], [349, 200], [480, 279], [480, 480], [578, 216], [683, 250], [774, 95], [970, 100], [970, 329], [970, 80]];
        var slotHeight = slot.getAttribute('ad-size-height');
        var slotWidth = slot.getAttribute('ad-size-width');
        if (slotWidth && slotHeight) {
          size = [ +slotWidth, +slotHeight];
        }
        googletag.defineSlot('/'+ dfpId +'/'+ getDfpSlotNameFromOasName(slot), size, slot.id).addService(googletag.pubads())
        .setCollapseEmptyDiv(true,true);
        googletag.display(slot.id);
      });
    });
  }
}(FreshDirect));
