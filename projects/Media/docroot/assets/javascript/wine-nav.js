if (!FreshDirect || !FreshDirect) {
	var FreshDirect = {};
}

(function() {
	var WineNav = {};
	
	FreshDirect.WineNav = WineNav;

	var menus = {};

	var get = function(id) {
		return document.getElementById(id);
	};

	function contains(anyarray, element) {
		for (var i = 0; i < anyarray.length; ++i) {
			if (anyarray[i] === element) {
				return true;
			}
		}
		return false;
	}
	
	var baseURL = location.protocol + "//" + location.host;

	var getByClass = function(className) {
		return YAHOO.util.Dom.getElementsByClassName(className);
	};
	
	WineNav.addSideNavMenu = function(menuId, domainId) {
		if (document.getElementById(menuId)) {
			menus[domainId] = {
				node: document.getElementById(menuId),
				popup: document.getElementById(domainId)
			};
		}
	};
	
	WineNav.initSideNavMenus = function() {
		WineNav.addSideNavMenu('wine-left-side-nav-closed', 'sidenav-domain-menu');
		WineNav.addSideNavMenu('wine-left-side-nav-closed-TYPE', 'sidenav-domain-submenu-TYPE');
		WineNav.addSideNavMenu('wine-left-side-nav-closed-COUNTRY', 'sidenav-domain-submenu-COUNTRY');
		WineNav.addSideNavMenu('wine-left-side-nav-closed-RATING', 'sidenav-domain-submenu-RATING');
		WineNav.addSideNavMenu('wine-left-side-nav-closed-PRICE', 'sidenav-domain-submenu-PRICE');
		WineNav.addSideNavMenu('wine-left-side-nav-closed-MORE', 'sidenav-domain-submenu-MORE');		
	};
	
	WineNav.selectedMoreOptions = [];
	
	WineNav.initMoreOptions = function(domainArray) {
		WineNav.selectedMoreOptions = domainArray;
	};

	WineNav.showMenu = function(menu_id) {
		var menu = menus[menu_id];
		menu.node.style.zIndex = 501;
		if (menu.popup)
			menu.popup.style.display = 'block';
	};
	
	WineNav.hideMenu = function(menu_id) {
		var menu = menus[menu_id];
		menu.node.style.zIndex = 0;
		if (menu.popup)
			menu.popup.style.display = 'none';
	};

	var setMyTimeout = function(menu_id) {
		var menu = menus[menu_id];
		if (!menu.timeout)
			menu.timeout = setTimeout("FreshDirect.WineNav.hideMenu('" + menu_id + "');", 50);
	};

	var resetMyTimeout = function(menu_id) {
		var menu = menus[menu_id];
		if (menu.timeout) {
			clearTimeout(menu.timeout);
			menu.timeout = null;
		}
	};
	
	WineNav.popupIn = function(menu_id) {
		resetMyTimeout(menu_id);
	};
	
	WineNav.popupOut = function(menu_id) {
		setMyTimeout(menu_id);
	};
	
	WineNav.catMenuIn = function(menu_id) {
		resetMyTimeout(menu_id);
		WineNav.showMenu(menu_id);
	};
	
	WineNav.catMenuOut = function(menu_id) {
		setMyTimeout(menu_id);
	};

	WineNav.filterMenuIn = function(e, menu_id) {
		resetMyTimeout(menu_id);
		var menu = menus[menu_id];
/*		if (menu.popup && menu.popup.style.display != 'block') {
			var x = YAHOO.util.Event.getPageX(e);
			var y = YAHOO.util.Event.getPageY(e);
			var r = YAHOO.util.Region.getRegion(menu.node);
			x = x - r.left;
			y = menu.node.clientHeight - y + r.top;
			menu.popup.style.bottom = (y - 10) + "px";
			menu.popup.style.left = (x - 10) + "px";
		}
*/		WineNav.showMenu(menu_id);
	};
	
	WineNav.filterMenuOut = function(e, menu_id) {
		setMyTimeout(menu_id);
	};
	
	var renderCatMenuPopup = function(menu) {
		var s =	'<div class="wine-homenav-cat-popup" ' +
				'onmouseover="FreshDirect.WineNav.popupIn(\'' + menu.id + '\');" ' +
				'onmouseout="FreshDirect.WineNav.popupOut(\'' + menu.id + '\');">';
		var item;
		for (var i = 0; i < menu.items.length; i++) {
			item = menu.items[i];
			s +=	'<a href="/category.jsp?catId=' + item.id + '&trk=dpage" class="title12 wine-narrow">' +
					item.label + "</a>";
		}
		s +=	"</div>";
		return s;
	};

	var renderFilterMenuPopup = function(menu) {
		var s = "";
		var item;
		for (var i = 0; i < menu.items.length; i++) {
			item = menu.items[i];
			s +=	'<a href="/wine/filter.jsp?wineFilterClicked=' + item.id + '&wineFilter=' + item.id + '&trk=dpage" class="title12 wine-narrow">' +
					item.label + "</a>";
		}
		return s;
	};

	var renderFilterMenuItem = function(menu) {
		menu.node.style.cursor = "pointer";
		menu.popup = document.createElement("div");
		if (menu.popup) {
			menu.popup.className = "wine-homenav-filter-popup";
			menu.popup.style.display = "none";
			menu.node.style.position = "relative";
			menu.popup.innerHTML = renderFilterMenuPopup(menu);
			menu.node.appendChild(menu.popup);
			YAHOO.util.Event.addListener(menu.node, "mouseover", WineNav.filterMenuIn, menu.id);
			YAHOO.util.Event.addListener(menu.node, "mouseout", WineNav.filterMenuOut, menu.id);
		}
	};

	var renderCatMenuItem = function(menu) {
		var s;
		if (menu.items.length) {
			s = '<div class="wine-main-item" ' +
					'onmouseover="FreshDirect.WineNav.catMenuIn(\'' + menu.id + '\');" ' +
					'onmouseout="FreshDirect.WineNav.catMenuOut(\'' + menu.id + '\');">' +
					'<a href="JavaScript:return false;" class="wine-main-item">' +
					menu.node.innerHTML + "</a>" +
					renderCatMenuPopup(menu) +
					"</div>";
		} else {
			s = '<a href="/category.jsp?catId=' + menu.id + '&trk=dpage" class="wine-main-item">' +
					menu.node.innerHTML + "</a>";
		}
		menu.node.innerHTML = s;
		var nodes = YAHOO.util.Dom.getElementsByClassName("wine-homenav-cat-popup", "div", menu.node);
		if (nodes.length)
			menu.popup = nodes[0];
	};

	var ajaxSuccess = function(o) {
		var lines = o.responseText.split("\n");
		o.argument.items = [];
		for (var i = 0; i < lines.length; i++) {
			var data = lines[i].split("|");
			if (data.length >= 2)
				o.argument.items.push({
					id: data[0],
					label: data[1]
				});
		}
		o.argument.render(o.argument);
	};

	var ajaxFailure = function(o) {
	};

	var createCatMenu = function(catId, node) {
		var menuItem = {
			id: catId,
			node: node,
			timeout: null,
			render: renderCatMenuItem
		};
		menus[catId] = menuItem;
		var transaction = YAHOO.util.Connect.asyncRequest("GET",
				baseURL + "/ajax/wine_nav.jsp?catId=" + catId, {
					success: ajaxSuccess,
					failure: ajaxFailure,
					argument: menuItem
				});
	};
	
	
	var createFilterMenu = function(domId, node) {
		var menuItem = {
			id: domId,
			node: node,
			timeout: null,
			render: renderFilterMenuItem
		};
		menus[domId] = menuItem;
		var transaction = YAHOO.util.Connect.asyncRequest("GET",
				baseURL + "/ajax/wine_nav.jsp?domId=" + domId, {
					success: ajaxSuccess,
					failure: ajaxFailure,
					argument: menuItem
				});
	};	
	
	var lastDomain;
			
	var setClearAllLink = function(domainId) {
		var clearLinks = YAHOO.util.Dom.getElementsByClassName('clear-all-link-value', 'a'), i;		
		for (i = 0; i < clearLinks.length; ++i) {		
			clearLinks[i].href = clearLinks[i].href.replace(/wineFilterClicked=.*&/, 'wineFilterClicked=' + (domainId || '') + '&');		
		}
	};
	
	WineNav.toggleDomain = function(domainId, clickLink) {	
		var cName;
		var Dom = YAHOO.util.Dom;

		if (lastDomain) {
			Dom.addClass(lastDomain + '-carrot', "nav-filter-carrot-" + lastDomain);
			Dom.removeClass(lastDomain + '-carrot', "nav-filter-carrot-" + lastDomain +"-open");			
			document.getElementById(lastDomain + '-all').style.display = 'none';
			document.getElementById(lastDomain + '-selected').style.display = 'block';
			document.getElementById(lastDomain + '-selected').style.display = 'block';			
			
			if (lastDomain == domainId) {
				lastDomain = null;		
				setClearAllLink('');
				return false;
			}
		}
		
		Dom.removeClass(domainId + '-carrot', "nav-filter-carrot-" + domainId);
		Dom.addClass(domainId + '-carrot', "nav-filter-carrot-" + domainId +"-open");
		document.getElementById(domainId + '-all').style.display = 'block';
		document.getElementById(domainId + '-selected').style.display = 'none';	
		setClearAllLink(clickLink);
		lastDomain = domainId;
		return false;
	};
	
	var lastGroup;	
	
	WineNav.initGroup = function(groupId) {
		lastGroup = groupId;
	};
	
	WineNav.toggleGroup = function(groupId) {		
		var Dom = YAHOO.util.Dom;
		
		if (lastGroup) {			
			Dom.removeClass('nav-group-' + lastGroup, 'nav-group-selected');
			
			Dom.removeClass('nav-group-' + lastGroup + '-all', 'nav-group-selected');			
			Dom.addClass('nav-group-' + lastGroup + '-all', 'nav-group-hidden');
			
			Dom.removeClass('nav-group-' + lastGroup + '-selected', 'nav-group-hidden');
			Dom.addClass('nav-group-' + lastGroup + '-selected', 'nav-group-selected');
			
			if (lastGroup == groupId) {
				lastGroup = null;		
				return false;
			}
		}
				
		Dom.addClass('nav-group-' + groupId, 'nav-group-selected');
		
		Dom.removeClass('nav-group-' + groupId + '-all', 'nav-group-hidden');
		Dom.addClass('nav-group-' + groupId + '-all', 'nav-group-selected');
		
		Dom.removeClass('nav-group-' + groupId + '-selected', 'nav-group-selected');
		Dom.addClass('nav-group-' + groupId + '-selected', 'nav-group-hidden');		
		
		lastGroup = groupId;
		return false;
	};
	
	WineNav.showMoreOptions = function() {
		var i;
		var Dom = YAHOO.util.Dom;
		var linkContainer = new YAHOO.util.Element('wineNavMoreOptionsTable');
		
		var links = linkContainer.getElementsByTagName('a');
		var selecteds = WineNav.selectedMoreOptions;
		
		Dom.get('wineNavMoreOptions').style.display = 'block';
		Dom.addClass('wineNavMoreOptions', 'wineNavMoreOptionsOpen');
		Dom.get('Varietal-all').style.display = 'none';
		Dom.get('Varietal-selected').style.display = 'none';		
						
		for (i = 0; i < links.length; ++i) {				
			if (contains(selecteds, links[i].id)) {
				Dom.removeClass(links[i], 'nav-filter-checkbox-Varietal');
				Dom.addClass(links[i], 'nav-filter-checkbox-Varietal-select');
				continue;
			}
			Dom.removeClass(links[i], 'nav-filter-checkbox-Varietal-select');
			Dom.addClass(links[i], 'nav-filter-checkbox-Varietal');			
		}
	};
	
	WineNav.hideMoreOptions = function() {
		document.getElementById('wineNavMoreOptions').style.display = 'none';
		YAHOO.util.Dom.removeClass('wineNavMoreOptions', 'wineNavMoreOptionsOpen');
		document.getElementById('Varietal-all').style.display = 'block';
		document.getElementById('Varietal-selected').style.display = 'none';
	};
	
	WineNav.toggleMoreOption = function(domain) {		
		var Dom = YAHOO.util.Dom;
		var link = Dom.get(domain);
		if (Dom.hasClass(link, 'nav-filter-checkbox-Varietal-select')) {
			Dom.removeClass(link, 'nav-filter-checkbox-Varietal-select');
			Dom.addClass(link, 'nav-filter-checkbox-Varietal');
			return false;
		}		
		Dom.removeClass(link, 'nav-filter-checkbox-Varietal');
		Dom.addClass(link, 'nav-filter-checkbox-Varietal-select');
		return false;
	};
	
	WineNav.clearAllMoreOption = function() {
		var Dom = YAHOO.util.Dom;
		var linkContainer = new YAHOO.util.Element('wineNavMoreOptionsTable');		
		var links = linkContainer.getElementsByTagName('a');
		for (i = 0; i < links.length; ++i) {		
			Dom.removeClass(links[i], 'nav-filter-checkbox-Varietal-select');
			Dom.addClass(links[i], 'nav-filter-checkbox-Varietal');			
		}
	};
	
	WineNav.submitMoreOptions = function(url) {
		var Dom = YAHOO.util.Dom;		
		var linkContainer = new YAHOO.util.Element('wineNavMoreOptionsTable');		
		var links = linkContainer.getElementsByTagName('a');
		var selectedLinks = [];
		
		for (i = 0; i < links.length; ++i) {
			for (i = 0; i < links.length; ++i) {
				if (Dom.hasClass(links[i], 'nav-filter-checkbox-Varietal-select')) {
					selectedLinks.push(links[i].id);
				}
			}
		}
		
		window.location.href = url + ',' + selectedLinks.join(',');		
	};

	WineNav.onLoad = function() {
		var countries = getByClass("wine_filt_menu_country");
		if (countries != null && countries.length > 0) {
			var country = countries[0];
			createFilterMenu("COUNTRY", country);
		}
		var prices = getByClass("wine_filt_menu_price");
		if (prices != null && prices.length > 0) {
			var price = prices[0];
			createFilterMenu("PRICE", price);
		}
		var menus = getByClass("wine-homenav-cat-menu");	
		
		if (menus != null && menus.length > 0) {
			for (var i = 0; i < menus.length; i++) {
				var splt = menus[i].className.split(" ");
				var menu = null;
				var category = null;
				for (var j = 0; j < splt.length; j++) {
					if (splt[j].indexOf("wine_cat_") == 0) {
						menu = menus[i];
						category = splt[j].substr(9);
					}
				}
				if (menu != null && category != null) {
						createCatMenu(category, menu);
				}
			}
		}			
	};
})();
