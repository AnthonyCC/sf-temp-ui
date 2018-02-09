if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function () {
	Coremetrics = {
		pageId : "",
		pageContentHierarchy : "",
		virtualCategory : ""
	};
	
	FreshDirect.Coremetrics = Coremetrics;
	window.coremetricsQueue = window.coremetricsQueue || [];
	Coremetrics.populateTrackingObject = function(pageId, pageContentHierarchy, virtualCategory) {
		
		Coremetrics.pageId = pageId;
		Coremetrics.pageContentHierarchy = pageContentHierarchy;
		Coremetrics.virtualCategory = virtualCategory;
		
	};
	
	Coremetrics.setDecorationFieldValue = function(fieldId, trackingObjectField) {
		var elements = document.getElementsByName(fieldId);
		for (i = 0; i < elements.length; i++) {
			elements[i].value = trackingObjectField;
		}
	}
	// if there is element in the array, run them
	window.coremetricsQueue.forEach( function(func) {
		if (typeof func === 'function') {
			func();
		}
	});
	// listen to the push() function, when an element is added to the coremetricsQueue, run it.
	Object.defineProperty(window.coremetricsQueue, "push", {
		  configurable: false,
		  enumerable: false, // hide from for...in
		  writable: false,
		  value: function () {
			if (typeof arguments[0] === 'function') {
				arguments[0]();
			}
		  }
		});
})();