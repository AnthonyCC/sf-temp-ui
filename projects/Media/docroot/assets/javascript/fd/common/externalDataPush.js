/* data to be pushed to external services */
var FreshDirect = FreshDirect || {};
(function(fd) {
	function addVars() {/* APPDEV-7239 */
		BOOMR.addVar({
			"FDUserID": (fd.user && fd.user.fdId) ? fd.user.fdId : '',
			"ERPCustomerID": (fd.user && fd.user.erpId) ? fd.user.erpId : ''
		});
	}
	
	if (document.addEventListener) {
		document.addEventListener("onBoomerangLoaded", addVars);
	} else if (document.attachEvent) {
		document.attachEvent("onpropertychange", function(e) {
			if (!e) e = window.event;
			if (e && e.propertyName === "onBoomerangLoaded") {
				addVars();
			}
		});
	}
}(FreshDirect));