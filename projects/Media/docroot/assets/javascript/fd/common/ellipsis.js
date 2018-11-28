/* requires jQuery, jQuery.ellipsis */
/* NOTE: jQuery.ellipsis has a bug where if the text to truncate is repetitive (i.e. 'test test test') it may not truncate properly. */
(function (fd) {
	"use strict";
  
	var $=fd.libs.$;

	/* add ellipsis if the browser doesn't support line-clamp */
	var tempDiv = document.createElement('div'),
		supportsLineClamp = tempDiv.style.WebkitLineClamp === '';
	tempDiv = void 0;

	if ( !supportsLineClamp ) {
		if ($.fn['ellipsis']) {
			// brand name
			$('.portrait-item-header-name .portrait-item-header-brandname').each(function(i, e) {
				window.requestAnimationFrame(function() {
					$(e).ellipsis({ lines: 1 });
				});
			});
			//brand name + product name
			$('.portrait-item-header-name .product-name-no-brand').each(function(i, e) {
				window.requestAnimationFrame(function() {
					$(e).ellipsis({ lines: 2 });
				});
			});
			//configuration description
			$('.portrait-item .configDescr').each(function(i, e) {
				window.requestAnimationFrame(function() {
					$(e).ellipsis({ lines: 1 });
				});
			});
		}
	}
}(FreshDirect));
