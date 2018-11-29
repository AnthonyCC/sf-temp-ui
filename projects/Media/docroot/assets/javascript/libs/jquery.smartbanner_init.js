(function ($) {
	$(function () {
		$.smartbanner({daysHidden: 0, daysReminder: 0,author:'FreshDirect',button: 'VIEW'});
		if(!$jq('#smartbanner.shown').is(':visible')) { $jq('#smartbanner').show(); }
	});
})(jQuery);