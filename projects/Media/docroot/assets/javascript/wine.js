if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function() {
	var Wine = {};
	
	FreshDirect.Wine = Wine;

	var baseURL = location.protocol + "//" + location.host;
	
	var tabGroups = {};

    var get = function(id) {
        return document.getElementById(id);
    };

    Wine.showTab = function(tabGroupId, tabId) {
        var tabGroup = tabGroups[tabGroupId];
        if (tabGroup != undefined) {
        	for (var i = 0; i < tabGroup.length; i++) {
        		var selected = tabGroup[i] == tabId;
        		var left = i > 0 && tabGroup[i - 1] != tabId && !selected;
        		var right = i < tabGroup.length - 1 && tabGroup[i + 1] != tabId && !selected;
            	get(tabGroup[i]).style.display = selected ? "block" : "none";
            	var label = get(tabGroup[i] + "_label");
            	if (label.className.indexOf("carousel-tab-label-selected") >= 0) {
            		if (!selected)
                    	label.className = label.className.replace("carousel-tab-label-selected", "carousel-tab-label");
            	} else {
            		if (selected)
                    	label.className = label.className.replace("carousel-tab-label", "carousel-tab-label-selected");
            	}
           	
            	if (label.className.indexOf(" carousel-tab-left-label") >= 0) {
            		if (!left)
                    	label.className = label.className.replace(" carousel-tab-left-label", "");
            	} else {
            		if (left)
            			label.className += " carousel-tab-left-label";
            	}
            	if (label.className.indexOf(" carousel-tab-right-label") >= 0) {
            		if (!right)
                    	label.className = label.className.replace(" carousel-tab-right-label", "");
            	} else {
            		if (right)
            			label.className += " carousel-tab-right-label";
            	}
        	}
        }
        return false;
    };
	
	Wine.hideTabOnLoad = function(tabGroupId, tabId) {
		var node = get(tabId);
		if (node) {
			node.style.display = "none";
		}
	};

    Wine.addTabItem = function(tabGroupId, tabId) {
        if (tabGroups[tabGroupId] == undefined) {
        	tabGroups[tabGroupId] = [];
        }
        tabGroups[tabGroupId].push(tabId);
        return false;
    };
    
    var erpLoading = false;
    var erPanel = null;
    
    var ajaxSuccess = function(o) {
		erPanel = new YAHOO.widget.Panel("usq-er-panel", {
			fixedcenter: true,
			underlay: "none",
			close: false,
			visible: false,
			modal: true,
			draggable: false,
			width: "500px"
		});
		erPanel.setBody(o.responseText);
		
		erPanel.render(document.body);
		YAHOO.util.Dom.addClass("usq-er-panel_c", "usq-brown-border");
		YAHOO.util.Dom.addClass("usq-er-panel_c", "usq-lightbrown-bg");
		
		// override .yui-panel hidden setting
		YAHOO.util.Dom.get("usq-er-panel").style.overflow = "visible";
		
		// show panel
		erPanel.show();
		
		erpLoading = false;
    };

	var ajaxFailure = function(o) {
		erpLoading = false;
	};
    
    Wine.showExpertRatingPopup = function() {
    	if (erpLoading)
    		return false;
    	else if (erPanel)
    		erPanel.show();
    	else
    		YAHOO.util.Connect.asyncRequest("GET",
				baseURL + "/wine/expert_rating_popup.jsp", {
					success: ajaxSuccess,
					failure: ajaxFailure
				});
    	return false;
    };
    
    Wine.hideExpertRatingPopup = function() {
    	if (erPanel)
    		erPanel.hide();
    	return false;
    };
})();
