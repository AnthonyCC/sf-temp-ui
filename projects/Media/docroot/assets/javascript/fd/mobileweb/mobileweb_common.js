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
		});
		
		var API = $("#nav-menu").data( "mmenu" );
		
		$jq('#navbarShow').on('click', function() {
			API.open();
		});
		$jq('.signin>a').on('click', function(e) {
			e.stopPropagation();
			$jq('#signin_iframe').show();
			$jq('#createacc_iframe').hide();
			$jq('.loginDialog').addClass('open');
			$jq('body').addClass('no-scroll');

			API.close();
		});
		$jq('.createacc>a').on('click', function(e) {
			e.stopPropagation();
			$jq('#signin_iframe').hide();
			$jq('#createacc_iframe').show();
			$jq('.loginDialog').addClass('open');
			$jq('body').addClass('no-scroll');

			API.close();
		});
		
		$jq('.loginDialog .close-x').on('click touch', function(e) {
			console.log('close click?');
			$jq('.loginDialog').removeClass('open');
			$jq('body').removeClass('no-scroll');
		});
	});
}(jQuery));