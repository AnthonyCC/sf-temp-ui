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

})();