var $jq;
var jqInit = false;
function initJQuery() {
	if (typeof(jQuery) == 'undefined') {
		if (!jqInit) {
			jqInit = true;
		}
		setTimeout("initJQuery()", 100);
	} else {
		$jq = jQuery.noConflict();
		jqInit = void 0;
	}
}
initJQuery();