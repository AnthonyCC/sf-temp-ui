/*-----------------------------------------------------------------------------
*/ var version='1.01.04' /*					Last Edit: 2010.10.06_06.03.44.PM
-----------------------------------------------------------------------------*/

/*
 *
 **	1.01.04 : 2010.10.06_06.03.44.PM
 *		Changed new tab id to use a random number instead of always using "_new"
 **	1.01.02/1.01.03 : 2010.04.06_03.16.17.PM
 *		Fixed for stage/stagepreview
 **	1.01.01 : 2010.04.06_10.30.21.AM
 *		Updated URLs to preview pages
 **	1.01.00 : 2010.03.21_08:00:41.PM
 *		Added Auto Update functionality
 *		Added Auto Update (force)
 **	1.00.00 : 2010.03.20_01:39:17.AM
 *		Initial version
 *
 */

/*-----------------------------------------------------------------------------


-----------------------------------------------------------------------------*/


/* -- GLOBAL variables -- */

	/* Menu variables */
		//holds DEBUG value from menu for logging [BOOL]
			var DEBUG = true;

	/* URL variables */
		//url operating on for dev/stage compatibility
			var urlMain = '';

/* -- GLOBAL variables -- */

/* -- GENERIC functions -- */

	/*
	 *	decide on web hostname based on erpsy hostname
	 *	pass a url to the function (OPTIONAL)
	 *	default is to use current location
	 */
		function getCluster(loca) {
			var locaV = loca || window.location.hostname;
			if (locaV.indexOf('dev') > -1) {
				urllookup = 'http://dev.freshdirect.com';
				urlMain = 'http://devpreview.freshdirect.com';
			}else if (locaV.indexOf('stg') > -1 || locaV.indexOf('stage') > -1) {
				urllookup = 'http://stage.freshdirect.com';
				urlMain = 'http://stagepreview.freshdirect.com';
			}

			return true;
		}
	/*
	 *	get URL param
	 */
		function gup(key) {
			key = key.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
			var regexS = "[\\?&]"+key+"=([^&#]*)";
			var regex = new RegExp( regexS );
			var results = regex.exec( window.location.href );
			if (results == null) {
				return "";
			} else {
				return results[1];
			}
		}

/* -- GENERIC functions -- */

/* -- SETUP -- */

	//if (DEBUG){ GM_log('Running ERPSy-Daisy SKU2URL Addon v '+version); }
	//if (DEBUG){ GM_log('----------------------------------------'); }
	//if (DEBUG){ GM_log('Starting setup...'); }
	
		//call GM menu
			while(!addGMMenu());
	
		//add script to page load
			window.addEventListener("load", function() { erpsy_addon(); }, false);


	function erpsy_addon() {

		while(!getCluster());

		var sku = '';
		
		sku = gup('skuCode');

		if (sku != '') {
			//if (DEBUG){ GM_log('Fetching product URL...'); }

			new Ajax.Request(urllookup+'/test/freemarker_testing/all_info.jsp?sku2url=true&sku='+sku, {
				method: 'GET',
					onComplete: function(responseDetails) {
						var url = responseDetails.responseText.replace(/[\s\n]*/gi, '');

						//this is where we avoid an error if the ftl isn't correct...
						if (url.substring(0,12) == '/product.jsp') {
							url = urlMain+url;
							if (DEBUG){ GM_log('Success! ('+url+')'); }

							var tds = document.body.getElementsByTagName("td");
							var d = new Date();
							for (var i=0; i<tds.length; i++) {
								if (tds[i].innerHTML == sku) {
									tds[i].innerHTML = '<a href="'+url+'" target="_new'+(Math.floor(d.getTime()*Math.random()))+'">'+sku+'</a>';
									continue;
								}
							}
						}
					},
					onerror: function() {
						//if (DEBUG){ GM_log('Error... '+responseDetails.status+' '+responseDetails.responseText); }
					}
			});
		}
		

		//if (DEBUG){ GM_log('Checking for an update (background)...'); }
			runAutoUpdate()
	}

/* -- SETUP -- */