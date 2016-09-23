(function ($) {
	$(document).ready(function() {
		$("#nav-menu").mmenu({
			"extensions": [
				"pagedim-black",
				"border-full"
			],
			"offCanvas": {
				"zposition": "front"
			},
			"navbar": {
				"title": "<img src=\"/media/mobileweb/images/topbar-fd-logo.png\" alt=\"FreshDirect\" class=\"img-responsive\" />"
			},
			"navbars": [
				{
					"position": "bottom",
					"content": (FreshDirect && FreshDirect.locabar && FreshDirect.locabar.hasFdxServices) ? "<div class='navbar-cont'><a href='https://foodkick.freshdirect.com' class='locabar-tab locabar-tab-fdx-cont'><div class='locabar-tab-fdx'></div></a></div>" : ""
				}
			],
			"iconPanels": true,
			"screenReader": true,
			"setSelected": {
				"hover": true,
				"parent": true
			 }
		}, {
			// configuration
			offCanvas: {
				pageSelector: "#page-content"
			}
		}).show();		
		
		var API = $("#nav-menu").data( "mmenu" );
		
		$jq('#navbarShow').on('click', function() {
			API.open();
		});
		
		$jq('.signin>a').on('click', function(e) {
			e.stopPropagation();
			window.top.location = '/login/login.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;

			API.close();
		});
		$jq('.createacc>a').on('click', function(e) {
			e.stopPropagation();
			window.top.location = '/social/signup_lite.jsp?successPage=' + window.location.pathname + window.location.search + window.location.hash;


			API.close();
		});
		$jq('.logout>a').on('click', function(e) {
			window.top.location = '/logout.jsp';
		});
	});
}(jQuery));