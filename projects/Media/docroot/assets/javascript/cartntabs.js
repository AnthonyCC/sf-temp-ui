/*
 * @require YAHOO.util.Connect
 * @require YAHOO.util.Dom
 * @require YAHOO.util.Event
 * @require YAHOO.plugin.Dispatcher
 * 
 * @param (String) facility
 * @param (String) formURI
 * @param (Array of String) tabs
 * @param (Object) features Variant to Site Feature map
 * @param (Array of String) impressionIds
 * @param (String) parentImpressionId Parent impression ID
 * @param (String) selected ID of selected tab
 */
var PipClass = function(facility, formURI, tabs, features, impressions, parentImpressionId, selected) {
	/* var my = {
		tabs: tabs,
		features: features,
		impressionIds: impressions
	}; */

	var my = this;
	
	this.tabs = tabs;
	this.features = features;
	this.impressionIds = impressions;
	this.parentImpressionId = parentImpressionId;
	this.selected = selected;
	
	// constants
	//
	my.MSG_LOADING = "<br/><br/><br/><br/><p class=\"tab_text_simple\">Refreshing, please wait...</p><br/><br/><br/><br/>";
	my.MSG_ERROR = "<br/><br/><br/><br/><p class=\"tab_text_simple\">Sorry, we weren't able to get recommendations.<br/><a href=\"/unsupported.jsp\" onClick=\"window.location.reload(false); return false;\">Click here</a> to refresh with new products. </p><br/><br/><br/><br/>";
	my.MSG_REFRESH = "<span class=\"text13bold\" style=\"color: #875087; \">Refreshing...</span>";

	var REFRESH_BASE = '/ajax/refresh.jsp';
	var NOREFRESH_BASE = '/ajax/norefresh.jsp';
	
	
	// private vars
	//
	var _facility		= facility; /* SS facility */
	var _prezFormURI	= formURI;


	// private members
	//
	var _call = function(tabId, uri) {
		if ( YAHOO.util.Dom.get('ra_'+tabId) != null ) {
			YAHOO.util.Dom.get('ra_'+tabId).innerHTML = my.MSG_REFRESH;
		}
		YAHOO.util.Connect.asyncRequest('GET', uri, {
			success: function(resp) {
				if ( resp.status === 205 ) {
					my.onTimeout();
				} else {
					my.onSuccess(resp, tabId);
				}
			},
			failure: function(resp) {
				my.onFailure(tabId);
			}
		});
	};


	var _baseURI = function(pageURI, tabId, tabPos, isRefresh) {
		var uri = (pageURI || REFRESH_BASE)+
			'?tab='+tabPos+
			'&variant='+tabId+
			'&siteFeature='+my.features[tabId]+
			'&formUri='+_prezFormURI+'&impId='+my.impressionIds[tabPos] +
			'&pImpId='+my.parentImpressionId+
			'&facility='+_facility+
			(isRefresh?'&refresh=true':'');
		return uri;
	};

	
	var _refresh = function(tabId, tabPos, isRefresh) {
		var uri = _baseURI(null, tabId, tabPos, isRefresh);
		_call(tabId, uri);
	};

	var _norefresh = function(tabId, tabPos) {
		var uri = NOREFRESH_BASE + '?tab='+tabPos+'&variant='+tabId+'&siteFeature='+my.features[tabId];
		YAHOO.util.Connect.asyncRequest('HEAD', uri, {});
	};

	
	
	// public vars

	// public members
	
	my.onFailure = function(tabId) {
		YAHOO.util.Dom.get('content_'+tabId).innerHTML = my.MSG_ERROR;
	};

	my.onSuccess = function(resp, tabId) {
		YAHOO.plugin.Dispatcher.process('content_'+tabId, resp.responseText);
	};

	my.onTimeout = function() {
		window.location.reload(false);
	};

	my.load = function(tab, tabPos) {
		var ctnt = YAHOO.util.Dom.get('content_'+tab);
		if (ctnt.childNodes.length == 0 || (ctnt.childNodes.length == 1 && ctnt.firstChild.nodeType != 1)) {
			YAHOO.util.Dom.get('content_'+tab).innerHTML = my.MSG_LOADING;
			_refresh(tab, tabPos, false);
		} else {
			_norefresh(tab, tabPos);
		}
	};


	// --- pagers -- //
	
	my.backward = function(tabId, tabPos, isRefresh) {
		var uri = _baseURI(null, tabId, tabPos, isRefresh)+'&page=prev';
		_call(tabId, uri);
	};

	my.forward = function(tabId, tabPos, isRefresh) {
		var uri = _baseURI(null, tabId, tabPos, isRefresh)+'&page=next';
		_call(tabId, uri);
	};

	my.goToPage = function(tabId, tabPos, isRefresh, pnumber) {
		var uri = _baseURI(null, tabId, tabPos, isRefresh)+'&page='+pnumber;
		_call(tabId, uri);
	};



	my.attachHandlers = function(clickHandlers) {
		var k;
		for (k=0; k<my.tabs.length; k++) {
			YAHOO.util.Event.on('td_'+my.tabs[k], 'click', function(e) {
				YAHOO.util.Event.preventDefault(e);

				var sel = this.getAttribute('vid');
				var ii;

				for ( ii=0; ii<my.tabs.length; ii++ ) {

					if ( ii === 0 ) { // first tab
						YAHOO.util.Dom.get('img_'+my.tabs[ii]).src = '/media_stat/images/carttab_corners/carttab_first_left_'+(my.tabs[ii]===sel ? 'on' : 'off')+'.gif';
					} else {
						YAHOO.util.Dom.get('img_'+my.tabs[ii]).src = '/media_stat/images/carttab_corners/carttab_btwn_'+(my.tabs[ii-1]===sel ? 'on' : 'off')+'_'+(my.tabs[ii]===sel ? 'on' : 'off')+'.gif';
					}

					if ( ii === my.tabs.length-1 ) { // last tab
						YAHOO.util.Dom.get('img_last').src = '/media_stat/images/carttab_corners/carttab_btwn_'+(my.tabs[ii]===sel ? 'on' : 'off')+'_nothing.gif';
					}					
				}
				
				if (my.selected != this.getAttribute('vid')) {

					// previous selected tab					
					YAHOO.util.Dom.get('content_'+my.selected).style.display = 'none';
					YAHOO.util.Dom.get('header_'+my.selected).style.display = 'none';
					YAHOO.util.Dom.get('footer_'+my.selected).style.display = 'none';
					
					YAHOO.util.Dom.get('a_'+my.selected).className = 'tab_inactive';				
					YAHOO.util.Dom.get('td_'+my.selected).className = 'tab_title_inactive';					
					
					// newly selected tab 
					my.selected = this.getAttribute('vid');
					YAHOO.util.Dom.get('content_'+my.selected).style.display = '';
					YAHOO.util.Dom.get('header_'+my.selected).style.display = '';
					YAHOO.util.Dom.get('footer_'+my.selected).style.display = '';
					
					YAHOO.util.Dom.get('a_'+my.selected).className = 'tab_active';				
					YAHOO.util.Dom.get('td_'+my.selected).className = 'tab_title_active';
		
					// load content (if necessary)
					my.load(my.selected, this.getAttribute("tpos"));
				}

				if(clickHandlers!=null){
					clickHandlers[this.getAttribute("tpos")]();
				}
				
			});
		}
	};

	/* return my; */
};
