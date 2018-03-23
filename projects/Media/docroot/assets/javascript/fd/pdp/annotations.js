/*global jQuery,pdp*/
var FreshDirect = FreshDirect || {};


// module initialization
(function (fd) {
	
  if (!fd || !fd.pdp || !fd.pdp.annotations) {
    return;
  }

	var $=fd.libs.$;
  var $popup = $('<div id="annotationPopup"></div>');
  var $trigger = $('.pdp .annotation');

  $trigger.css({
    display: 'block'
  });

  $popup.html(pdp.annotationPopup(fd.pdp.annotations));
  var popupConfig = {
    halign: 'left',
    overlay: true,
    placeholder: true,
    stayOnClick: true
  };

  var popup = new fd.modules.common.PopupContent(
    $popup,
    $trigger,
    popupConfig
  );

}(FreshDirect));

