
/*-----------------------------------------------------------------------------
*/ var version='1.20.01' /*					Last Edit: 2011.01.01_12.51.57.PM
-----------------------------------------------------------------------------*/

var changeLog = ""+
"/*\n"+
	" *\n"+
	" **	1.30.01 : 2011.03.22_01.50.06.PM\n"+
	" *			Fixed display issue in KOS when an item was already checked\n"+
	" **	1.30.00 : 2011.03.22_01.15.17.AM\n"+
	" *			Added KOSHER functionality\n"+
	" **	1.20.01 : 2011.01.01_12.51.57.PM\n"+
	" *			Added Trim to SAP IDs when adding leading zeroes\n"+
	" **	1.20.00 : 2010.12.01_12.37.28.AM\n"+
	" *			Added Default Pricing Unit Description functionality to NBIS\n"+
	" *			Added Change Log viewer\n"+
	" **	1.20.00 PRE : 2010.08.17_10.16.21.AM\n"+
	" *			Added Options (non-functional)\n"+
	" **	1.12.05 : \n"+
	" *			fixing verify error(s)\n"+
	" *			Fixed time error where time calc always returned 1:01 if took over a min\n"+
	" **	1.12.04 : 2010.08.05_01.45.48.PM\n"+
	" *			Fixed auto-format for SAP ids when not using web descriptions\n"+
	" *			Fixed a few calls to post data that if missing, would cause a hang\n"+
	" **	1.12.03 : 2010.08.05_01.04.29.PM\n"+
	" *			Added output for what the dupe was when dupe checking (to console)\n"+
	" **	1.12.02 : 2010.04.18_11:46:30.PM\n"+
	" *			Added New/Back In Stock functionality\n"+
	" *			Added regexp date verify\n"+
	" *			Added tab-insert functionality\n"+
	" **	1.12.01 : 2010.04.16_04.52.29.PM\n"+
	" *			fixed non-existant button calls\n"+
	" *			wrote regexp for date/time confirmation\n"+
	" **	1.12.00 : 2010.04.16_12:32:56.AM\n"+
	" *			Starting NBIS functionality\n"+
	" *			Changed button generation to be expandable\n"+
	" **	1.11.00 : 2010.03.21_07:54:05.PM\n"+
	" *			Added Auto Update functionality\n"+
	" *			Added Auto Update (force)\n"+
	" **	1.10.01 : 2010.03.20_10:23:33.PM\n"+
	" *			Fixed bug in CAO that made error SKUs cause an infinite loop\n"+
	" *			Fixed bug that caused status to display wrong (always showed last sku)\n"+
	" **	1.10.00 : 2010.03.08_01.25.05.PM\n"+
	" *			Fixed some status storage bugs (and color bugs)\n"+
	" *			Added after-POST status\n"+
	" **	1.09.00 : 2010.03.08_01:44:25.AM\n"+
	" *			Re-wrote structure to allow easier expansion of code\n"+
	" *			Fixed a few minor bugs and display issues\n"+
	" *			Added ability to edit CAO\n"+
	" **	1.08.00 : 2010.02.09_02.57.09.PM\n"+
	" *			Added deposit amount\n"+
	" *			Fixed curData define (to be easier)\n"+
	" *			Fixed a few minor mis-names in label\n"+
	" **	1.07.01 : 2010.02.03_06.06.16.PM\n"+
	" *			Fixed SAP->SKU output to allow copy/paste into excel correctly\n"+
	" **	1.07.00 : 2010.02.01_04.25.58.PM\n"+
	" *			Added SAP2SKU Lookup\n"+
	" *			Fixed verify bug that would cause single item to get stuck\n"+
	" **	1.06.00 : 2010.01.26_02.35.26.PM\n"+
	" *			Added Label Name\n"+
	" *			Fixed verfiy bug that saw {SKIP} as a mismatch\n"+
	" **	1.05.02 : 2010.01.25_05.55.24.PM\n"+
	" *			Turned on GET and POST by default\n"+
	" **	1.05.01 : 2010.01.22_12.24.16.PM\n"+
	" *			Fixed minor bug caused by newest firebug\n"+
	" **	1.05.00 : 2010.01.22_12.24.16.PM\n"+
	" *			Added verification scheme (for rest/web descrip)\n"+
	" *			Added display to show mismatches on verify (and which items mismatch)\n"+
	" *			Changed status updates to always show SOME status (and errors)\n"+
	" *			Cleaned up debug messages\n"+
	" *			Fixed curData structure to make it expandable\n"+
	" *			Fixed bugs in count/dupe check\n"+
	" *			Fixed bug when no ID was passed, all zeroes were used (now skips)\n"+
	" **	1.04.00 : 2010.01.20_10:56:11.PM\n"+
	" *			Added POST delay option to fix a (ERPSy-side) bug\n"+
	" *			Fixed web description to take data from lines/empty/skip\n"+
	" **	1.03.00 : 2010.01.20_02.39.00.PM\n"+
	" *			Changed the way Web Description works. Each line can now set it's own\n"+
	" *			value.\n"+
	" **	1.02.00 : 2010.01.20_12.19.20.PM\n"+
	" *			Web Copy done\n"+
	" **	1.01.06 : 2010.01.19_11:56:36.PM\n"+
	" *			Added Web Copy HTML / toggler\n"+
	" *			Added prototype.js script to page\n"+
	" *			Fixed console grab, if console wasn't available, addon didn't work\n"+
	" **	1.01.05 : 2010.01.18_06.09.17.PM\n"+
	" *			Fixed bug that caused checkLocation to fail\n"+
	" *			Added GUI for advance order flag\n"+
	" *			Added code for advance order flag\n"+
	" **	1.01.04 : 2008.11.18_07:38:44.PM\n"+
	" *			Tweaked urls to include (should now work on all erps urls)\n"+
	" *			Added code to get correct baseURLs with new unclude urls\n"+
	" *			Moved versioning to var\n"+
	" *			Added version to Batch Update title bar\n"+
	" *			Tweaked style (to var, easier to edit)\n"+
	" *			Fixed DupeCheck returning '1' on an empty check\n"+
	" **	1.01.03 : 2008.10.27_01:00:53.AM\n"+
	" *			Fixed timing\n"+
	" *			Fixed menu bugs\n"+
	" *			Fixed/Added unknown form data logging (DEBUG)\n"+
	" *			!BUG items left is incorrect (display)\n"+
	" **	1.01.02 : 2008.10.26_09:15:50.PM\n"+
	" *			Fixed dupe counter (again)\n"+
	" **	1.01.01 : 2008.10.26_06:16:38.PM\n"+
	" *			Added killing of calendar script/div\n"+
	" *			These aren't implemented, and tend to cause errors randomly.\n"+
	" *			It was easier to remove them than worry about them.\n"+
	" **	1.01.00 : 2008.10.26_04:06:42.AM\n"+
	" *			Added Dupe Check standalone function\n"+
	" *			Added shouldPost/ShouldGet options to menu for debugging\n"+
	" *			Fixed Dupe checking (wasn't finding dupes correctly)\n"+
	" *			Tweaked heights\n"+
	" *			Added Logging to firebug console if firebug is installed\n"+
	" **	1.00.01 : 2008.07.20_12.28.38.PM\n"+
	" *			Added timing\n"+
	" **	1.00.00 : 2008.07.20_03.21.41.AM\n"+
	" *			Working version\n"+
	" *\n"+
	" */\n"
"";

/*
	TODO:

		combine tempArray -> curData into single function call
		simplify applyChange if/elses and combine them into a function
		remember which view you were on
		more kill switches, so the code can't get stuck in loops on GET/POSTS
		new/bis check messages from posted data (like "attributes saved")
*/


/* -- GLOBAL variables -- */
	
	/* Data structures */
		/* -- CAO_content --
			CAO_content [0]  Current Status WORD as string
						[1]  Current Status TEXT as string
		-- CAO_content -- */
			var CAO_content = new Array("", "");
		/* -- CAOtemplate --
			CAOtemplate ['COLUMN|NAME'] [0] Displayed Text
										[1] Current Val
										[2] Value to Use
										[3] After POST status
		-- CAOtemplate -- */
			var CAOtemplate = new Array();
			
		/* -- KOS_content --
			KOS_content [0]  Current Status WORD as string
						[1]  Current Status TEXT as string
		-- KOS_content -- */
			var KOS_content = new Array("", "");

		/* -- KOStemplate --
			KOStemplate ['COLUMN|NAME'] [0] Displayed Text/Image
										[1] Current Val
										[2] Value to Use
										[3] After POST status
		-- KOStemplate -- */
			var KOStemplate = new Array();
			
		/* -- curData --
			CurData	[0]	[0]  id as string (base id for Web Copy)
			int id		[1]  verb as string
			in sequence	[2]  fetch respStaus as string
						[3]  fetch respText as string
						[4]  postData as string
						[5]  post respStatus as string
						[6]  post respText as string
						[7] [0]  web description text
						    [1]  restriction text
						    [2]  restriction verb
						    [3]  advance order true/false
							[4]  label name text
							[5]  deposit amount
							[6]  newness date
							[7]  back in stock date
							[8]  default pricing unit description
						[8] [0]  VERIFY web description text
						    [1]  VERIFY restriction text
						    [2]  VERIFY restriction verb
						    [3]  VERIFY advance order true/false
							[4]  VERIFY label name text
							[5]  VERIFY deposit amount
							[6]  VERIFY newness date
							[7]  VERIFY back in stock date
							[8]  VERIFY default pricing unit description
						[9]  verify done (removed from queue) true/false
						[10] target id as string (Web Copy)
						[11] CAO results (in the form of CAOtemplate)
						[12] KOS results (in the form of KOStemplate)


		-- curData -- */
		//for define: find "curData[curLine] =  new Array(curDataLength);"
			var curData = new Array();
			var curDataLength = 12;
			var curDataLengthSeven = 9;
			var curDataLengthEight = 9;
		/* -- postData --
			these are the values we can post (from MaterialTag.java),
			additionally see EnumAttributeName for all the values not just that one action on that page

			which gives us:
				DESCRIPTION               = "description", STRING
				OPTIONAL                  = "optional", BOOLEAN, default false
				PRIORITY                  = "priority", INTEGER, default 0
				DISPLAY_FORMAT            = "display_format", STRING, "dropdown"
				LABEL_VALUE               = "label_value", BOOLEAN, default false
				DEFAULT                   = "default", BOOLEAN, default false
				UNDER_LABEL               = "under_label", STRING
				SELECTED                  = "selected", BOOLEAN, default false
				PRODUCT_CODE              = "product_code", STRING
				DISPLAY_GROUP             = "display_group", STRING
				TAXABLE                   = "taxable", BOOLEAN, default false
				CUST_PROMO                = "cust_promo", BOOLEAN, default false
				SKUCODE                   = "skucode", STRING
				LABEL_NAME                = "label_name", STRING
				PRICING_UNIT_DESCRIPTION  = "pricing_unit_description", STRING
				DEPOSIT_AMOUNT            = "deposit_amount", INTEGER, default 0
				KOSHER_PRODUCTION         = "kosher_production", BOOLEAN, default false
				RESTRICTIONS              = "restrictions", STRING
				SPECIALPRODUCT            = "specialproduct", STRING
				ADVANCE_ORDER_FLAG        = "advance_order_flag",BOOLEAN, default false
				NEW_PRODUCT_DATE          = "new_prod_date", STRING
				BACK_IN_STOCK_DATE        = "back_in_stock", STRING

			POST /ERPSAdmin/product/product_view.jsp action=save&%3A%3Apricing_unit_description=Default+Pricing+Unit+Description

			postData [0]  'action=save'
			         [1]  LABEL_NAME
			         [2]  CUST_PROMO
			         [3]  DEPOSIT_AMOUNT
			         [4]  RESTRICTIONS
			         [5]  DESCRIPTION
			         [6]  SELECTED
			         [7]  TAXABLE
			         [8]  ADVANCE_ORDER_FLAG
			         [9]  KOSHER_PRODUCTION
					 [10] new_prod_date
					 [11] back_in_stock
					 [12] pricing_unit_description
		-- postData -- */
			var postData = new Array(13);
			
			var browser = "IE";
			
		/* -- buttonArr --
			buttonArr [i] [0] prefix
			              [1] parent container id
			              [2] text value of button
			              [3] currently active
		-- buttonArr -- */
	buttonArr = new Array();
		//buttonArr[buttonArr.length] = new Array('debug', 'debug', 'DEBUG', false);
		buttonArr[buttonArr.length] = new Array('wc', 'webcopy', 'WC', false);
		buttonArr[buttonArr.length] = new Array('rest', 'rest', 'REST', true);
		buttonArr[buttonArr.length] = new Array('CAO', 'CAO', 'CAO', false);
		buttonArr[buttonArr.length] = new Array('NBIS', 'NBIS', 'NBIS', false);
		buttonArr[buttonArr.length] = new Array('KOS', 'KOS', 'KOS', false);

	/* Timing */
		var timeStart;
		var timeEnd;

	/* Interval checks */
		//used to watch page script: calendar, and kill it
			var checkKillCal;
		//status update check [BOOL]
			var checkStat;
		//verify update check [BOOL]
			var checkVerify;
		//CAO fetch check [BOOL]
			var checkCAO;
		//KOS fetch check [BOOL]
			var checkKOS;

	/* Menu variables */
		//holds DEBUG value from menu for logging [BOOL]
			var DEBUG = true; 
		//holds shouldPost value from menu for posting debug [BOOL]
			var shouldPost = true; 
		//holds shouldGet value from menu for fetching debug [BOOL]
			var shouldGet = true; 

	/* URL variables */
		//url operating on for dev/stage compatibility
			var urlMain = checkLocation();
		//url to get id's info from
			var urlSapId = urlMain+'attribute/material/material_view.jsp?sapId=';
		//url to post to
			var urlPost = urlMain+'attribute/material/material_view.jsp';
		//url to view SKU
			var urlWebCopy = urlMain+'product/product_view.jsp?skuCode=';
		//url to view CAO
			var urlCAOView = urlMain+'product/product_view.jsp?skuCode=';
		//url to get CAO
			var urlCAO = urlMain+'product/claims_edit.jsp';
		//url to view KOS
			var urlKOSView = urlMain+'product/product_view.jsp?skuCode=';
		//url to get KOS
			var urlKOS = urlMain+'product/kosher_edit.jsp';
		//post url for sapid -> skus
			var urlSAP2SKU = urlMain+'product/product_search.jsp';
		//view (GET) url for NBIS
			var urlGetNBIS = urlMain+'product/product_view.jsp?skuCode=';
		//post url for NBIS
			var urlPostNBIS = urlMain+'product/product_view.jsp';
			var urlRestPost = urlMain+'attribute/material/material_view.jsp';

	/* Misc variables */
		//bool to control sku lookup
			var SAP2SKU = false;
		//post check [BOOL]
			var doPost = shouldPost;
		//get check [BOOL]
			var doGet = shouldGet;
		//chosen option
			var restOpt = '';
		//ids left in queue
			var queue = 0;
		//CAO fetch killswitch [INT]
			var checkCAOKS = 60; //(interval = 500ms, so 60/2 = 30 secs)
		//KOS fetch killswitch [INT]
			var checkKOSKS = 60; //(interval = 500ms, so 60/2 = 30 secs)
		
		//boolean to allow cao to finish before kicking off kos
			var caoDone = false;
			
		//main and backup skus for viewing
			var skuCAOmain = 'CAN0070215';
			var skuCAObackup = 'SPE0068675';
			
		//main and backup skus for viewing (kosher)
			var skuKOSmain = 'CAN0070215';
			var skuKOSbackup = 'SPE0068675';
			
		//date regExp pattern
			dateRegExp = /^(0[1-9]|1[012])\/([123]0|[012][1-9]|31)\/(2[0-9]{3}|[0-9]{2})([\s]?(0[1-9]|1[012])[:]([0-5][0-9]))?$/;

/* -- GLOBAL variables -- */

/* -- GENERIC functions -- */

	/* magic protoype $ function */
		function $() {
			var elements = new Array();
			for (var i = 0; i < arguments.length; i++) {
				var element = arguments[i];
				if (typeof element == 'string')
					element = document.getElementById(element);
				if (arguments.length == 1)
					return element;
				elements.push(element);
			}
			return elements;
		}

	/*	get elements by name
	 *
	 *	gets element by name, extension of the $ function
	 *	returns only the one val if only one element is found
	 *	otherwise returns an array
	 */
		function $n() {
			var elements = new Array();
			for (var i = 0; i < arguments.length; i++) {
				var element = arguments[i];
				if (typeof element == 'string') {
					elements = document.getElementsByName(element);
				}
				if (elements.length == 1) {
					return elements[0];
				}
			}
			return elements;
		}

	/*	extend array object to include the functions:
	 *
	 *		inArray
	 *			usage: ARRAY.inArray(VALUE)
	 *			returns true/false
	 *
	 *		clone
	 *			usage: var arrClone = ARRAY.clone()
	 *			returns a clone of the Array
	 */
		Array.prototype.inArray = function (value) {
			var i;
			for (i=0; i < this.length; i++) {
				if (this[i] === value) {
					return true;
				}
			}
			return false;
		};
		Array.prototype.clone = function () {
			var a = new Array();
			for (var property in this) {
				a[property] = typeof (this[property]) == 'object'
					? this[property].clone() 
					: this[property]
			}
			return a;
		}

	/* check object to see if it's an array */
		function isArray(obj) {
		   if (obj.constructor.toString().indexOf("Array") == -1)
			  return false;
		   else
			  return true;
		}

	/*	function to handle inserting elements
	 *
	 *	target [type STRING] : the id of the target element to insert
	 *		new element before or after
	 *
	 *	insert [type STRING] : element type to insert
	 *		i.e. 'div', 'hr', 'p', etc
	 *	BIA [type STRING] : insert element before, inside, or after target
	 *		defaults to AFTER
	 *	id [type STRING] : id to give element
	 */
		function insEle(target, insert, BIA, insId) {
			var targetElement, newElement, id;
			(target!='' && $(target))
				?targetElement = $(target)
				:targetElement = document.getElementsByTagName("body")[0];
			
			if (targetElement) {
				newElement = document.createElement(insert);
				newElement.id = insId;

				switch(BIA) {
					case 'B':
					  targetElement.parentNode.insertBefore(newElement, targetElement);
					  break;    
					case 'I':
					  targetElement.appendChild(newElement, targetElement);
					  break;
					case 'A':
					default:
					  targetElement.parentNode.insertBefore(newElement, targetElement.nextSibling);
				}
			}
			return true;
		}
		
		function insEle_f1(target, insert, BIA, insId, iType) {
			var targetElement, newElement, id;
			(target!='' && $(target))
				?targetElement = $(target)
				:targetElement = document.getElementsByTagName("body")[0];
			
			if (targetElement) {
				if(browser == "FF") {
					newElement = document.createElement(insert);
					newElement.id = insId;
				}
				switch(iType) {
					case 'C':
						if(browser == "IE")
							newElement = document.createElement("<input type=checkbox id=" + insId + " name=" + target + ">");
						else
							newElement.setAttribute('type', 'checkbox');
						break;
					case 'R':
						if(browser == "IE")
							newElement = document.createElement("<input type=radio id=" + insId + " name=" + target + ">");
						else
							newElement.setAttribute('type', 'radio');
						break;
					case 'B':
						if(browser == "IE")
							newElement = document.createElement("<input type=button id=" + insId + " name=" + target + ">");
						else
							newElement.setAttribute('type', 'button');
						break;
					default:
						if(browser == "IE")
							newElement = document.createElement("<input type=text id=" + insId + " name=" + target + ">");
						else
							newElement.setAttribute('type', 'text');
						break;
				}

				switch(BIA) {
					case 'B':
					  targetElement.parentNode.insertBefore(newElement, targetElement);
					  break;    
					case 'I':
					  targetElement.appendChild(newElement, targetElement);
					  break;
					case 'A':
					default:
					  targetElement.parentNode.insertBefore(newElement, targetElement.nextSibling);
				}
			}
			return true;
		}

	/*
	 *	strip off page from location
	 *	pass a url to the function (OPTIONAL)
	 *	default is to use current location
	 */
		function checkLocation(loca) {
			var locaV = loca || window.location.href;
			fdLog.debug("locaV:" + locaV);
			var page = locaV.substring(locaV.lastIndexOf('/batch/') + 1);
			fdLog.debug("page:" + page);
			locaV = locaV.replace(page, '');			
			fdLog.debug("returning locaV:" + locaV);
			return locaV;
		}
	
	/* choice animations (user-side) */
		function ani(key, id) {
			switch (key) {
				case '.': return '..';
				case '..': return '...';
				case '...': return '.';

				case '+': $(id).className = 'm'; return '-';
				case '-': $(id).className = 'o'; return 'o';
				case 'o': $(id).className = 'p'; return '+';

				default: return '';
			}
		}
		
		function aniKOS(key, id) {
			switch (key) {
				case '+': $(id).className = 'm'; return '-';
				case '-': $(id).className = 'p'; return '+';

				default: return '';
			}
		}
		
	/* choice animations (code-side) */
		function ani(key) {
			switch (key) {
				case '.': return '..';
				case '..': return '...';
				case '...': return '.';

				case '+': return '-';
				case '-': return 'o';
				case 'o': return '+';

				default: return '';
			}
		}
		
		function aniKOS(key) {
			switch (key) {
				case '+': return '-';
				case '-': return '+';

				default: return '';
			}
		}

	/* function to allow a pause */
		function pausecomp(millis) {
			var date = new Date();
			var curDate = null;

			do { curDate = new Date(); }
			while((curDate-date) < millis);
		}

	/* kills the calendar */
		function init() {
			//wait for init function to finish
			/*
			if(unsafeWindow.bPageLoaded) {
				
				//wipe calendar script
				var calScript = document.getElementsByTagName("script")[0];
				calScript.innerHTML = '';

				//eliminate calendar divs from page
				var bodytag = document.getElementsByTagName("body")[0];

				//do this in a better way, firebug messes with these numbers
				for (var n=10; n> 0; n--){
					if (
						document.getElementsByTagName("div")[n].id == 'calendar' ||
						document.getElementsByTagName("div")[n].id == 'selectMonth' ||
						document.getElementsByTagName("div")[n].id == 'selectYear'
						) {
						var remTarget = document.getElementsByTagName("div")[n];
						bodytag.removeChild(remTarget);
					}
				}

				//clear check interval
				clearInterval(checkKillCal);
				fdLog.debug('Calendar killed.'); 
			}
			*/
		}

	/* add tab functionality */
		function insertTab2(o, e) {	
			o = $(o);
			var kC = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which;
			if (kC == 9 && !e.shiftKey && !e.ctrlKey && !e.altKey) {
				var oS = o.scrollTop;
				if (o.setSelectionRange) {
					var sS = o.selectionStart;
					var sE = o.selectionEnd;
					o.value = o.value.substring(0, sS) + "\t" + o.value.substr(sE);
					o.setSelectionRange(sS + 1, sS + 1);
					o.focus();
				}
				else if (o.createTextRange) {
					document.selection.createRange().text = "\t";
					e.returnValue = false;
				}
				o.scrollTop = oS;
				if (e.preventDefault) {
					e.preventDefault();
				}
				return false;
			}
			return true;
		}
		function insertTab(o, e) {
			o = $(o);
			var kC = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which;
			if (kC == 9 && !e.shiftKey && !e.ctrlKey && !e.altKey) {
				var oS = o.scrollTop;
				if (o.setSelectionRange) {
					var sS = o.selectionStart;
					var sE = o.selectionEnd;
					o.value = o.value.substring(0, sS) + "\t" + o.value.substr(sE);
					o.setSelectionRange(sS + 1, sS + 1);
					o.focus();
				}
				else if (o.createTextRange) {
					document.selection.createRange().text = "\t";
					e.returnValue = false;
				}
				o.scrollTop = oS;
				if (e.preventDefault) {
					e.preventDefault();
				}
				return false;
			}
			return true;
		}

/* -- GENERIC functions -- */

	function getTime() { var d = new Date(); return Math.floor(d.getTime()/1000/60/60/24); }

/* -- MENU -- */

	/* add menu items */
		function addGMMenu() {
			//if not defined, set it to false
			DEBUG = true;
			//if not defined, set it to false
			shouldPost = true;
			//if not defined, set it to false
			shouldGet = true;

			//write to log
			fdLog.debug('  Debug mode: '+DEBUG);
			fdLog.debug('  Posting: '+shouldPost);
			fdLog.debug('  Get: '+shouldGet);
			
			return true;
		}

/* -- MENU -- */

/* -- REST functions -- */

	/* Run Restriction-Web Description */
		function actionRest() {
			//set timer
			timeStart = makeTime('start');

			//check to see if any action should take place
				fdLog.debug('SAP2SKU: '+SAP2SKU); 

			if ( 
				(
					!SAP2SKU &&
					(
						(!$('rests_use').checked && !$('advOrder_use').checked && !$('WD_use').checked && !$('LN_use').checked && !$('DEP_use').checked) || 
						($('advOrder_use').checked && (!$('advOrder_val_on').checked && !$('advOrder_val_off').checked))
					)
				) || 
				$('rests_ids').value == ''
			   ) {
					while(!updStatus('No action selected/No IDs to act on', 'error', '', ''));
					fdLog.debug('REST: No action selected/No IDs to act on'); 
				return true;
			}
			//clear old data
			curData = [];

			//update status
			while(!updStatus('checking IDs...', 'checking', '0', '0'));

			//clear output status
			$('status_of').innerHTML = 'of';
			$('status_cont').innerHTML = '<hr />';
			
			//temp arrays for dupecheck
			var tempIdsArr = new Array();
			var dupeArr = new Array();
			//temp var for dupe count
			var dupes = 0;
			
			//start processing id
			while(!updStatus('checking for dupe IDs', 'checking', '', ''));

			//get id list and split into an array for use
			//remove extra newline
			var cont = $('rests_ids').value.replace(/\n*$/, '');
			var tempIdsArr = cont.split('\n');

			fdLog.debug('tempIdsArr '+tempIdsArr); 

			//loop through temp array, and add to curData
			if ( isArray(tempIdsArr) ) {
				var curLine = 0;
				for (var n=0; n < tempIdsArr.length; n++ ) {

					//if item is not already defined
					if (!dupeArr.inArray(tempIdsArr[n])) {
						//add it
						
						dupeArr.push(tempIdsArr[n]);
						curData.push(curLine);
						curData[curLine] =  new Array(curDataLength);
							curData[curLine][7] = new Array(curDataLengthSeven);
							curData[curLine][8] = new Array(curDataLengthEight);
						curData[curLine][0] = tempIdsArr[n];
						curData[curLine][1] = 'queued';
						curData[curLine][7][0] = '{SKIP}'; //safety
						curData[curLine][8][0] = '{SKIP}'; //safety
						
						//pull data from line: SAPId [TAB] WEBDESCRIP
						var extraData = tempIdsArr[n].split('\t');
						if (extraData.length >= 2) { //we can safely ignore any extra data
							//0 == SAPId
							//1 == WEBDESCRIP

							//check here for correctly formatted SAPId
							extraData[0] = (extraData[0]).replace(/^\s+|\s+$/g,"");
							while (extraData[0].length < 18) {
								extraData[0] = '0'+extraData[0];
							}

							curData[curLine][0] = extraData[0];
							
							if (extraData[1] != '') {
								curData[curLine][7][0] = extraData[1];
								//if we want it empty, pass '{EMPTY}'
								if (extraData[1] == '{EMPTY}') {
									curData[curLine][7][0] = '';
								}
							}
						}else{
							//we're not using web descrip, check here for correctly formatted SAPId
							curData[curLine][0] = (curData[curLine][0]).replace(/^\s+|\s+$/g,"");
							while (curData[curLine][0].length < 18) {
								curData[curLine][0] = '0'+curData[curLine][0];
								fdLog.debug('Reformatting SAP Id (no webdescrip) '+curData[curLine][0]); 
							}

						}

						//check here for web description
						if ( $('WD_use').checked ) {
							if ( $('WD_descrip').value == '{DATA}' ) {
								if (extraData[1] != '') {
									curData[curLine][7][0] = extraData[1];
									//if we want it empty, pass '{EMPTY}'
									if (extraData[1] == '{EMPTY}') {
										curData[curLine][7][0] = '';
									}
								}
							}else{
								curData[curLine][7][0] = $('WD_descrip').value;
							}
						}

						//check here for label name
						if ( $('LN_use').checked ) {
							if ( $('LN_descrip').value == '{DATA}' ) {
								if (extraData[1] != '') {
									curData[curLine][7][4] = extraData[1];
									//if we want it empty, pass '{EMPTY}'
									if (extraData[1] == '{EMPTY}') {
										curData[curLine][7][4] = '';
									}
								}
							}else{
								curData[curLine][7][4] = $('LN_descrip').value;
							}
						}

						//check here for deposit amnount
						if ( $('DEP_use').checked ) {
							if ( $('DEP_descrip').value == '{DATA}' ) {
								if (extraData[1] != '') {
									curData[curLine][7][5] = extraData[1];
									//if we want it empty, pass '{EMPTY}'
									if (extraData[1] == '{EMPTY}') {
										curData[curLine][7][5] = '';
									}
								}
							}else{
								curData[curLine][7][5] = $('DEP_descrip').value;
							}
						}

							fdLog.debug('Added item '+curLine+' to CurData'); 

						//add item line div
						while(!insEle('status_cont', 'div', 'I', 'fetchitem'+curLine)) {}
						while(!updStatus('ID ok '+curData[curLine], 'checking', '', ''));
						

						//check here for missing ID
						if (extraData[0] == '000000000000000000') {
							curData.pop(curLine);
						}else{
							curLine++;
						}
					}else{
						//skip
							fdLog.debug('Skipped item '+curLine+' : '+tempIdsArr[n]+' (dupe)'); 
						while(!updStatus('skipping dupe '+curData[curLine], 'checking', '', ''));
						dupes++;
					}
				}
			}
			while(!updStatus('dupe check done ('+dupes+' skipped)', 'checking', '', curData.length, ''));
			
				fdLog.debug('Dupe Count: '+dupes); 

			//start processing id
			//update status (calle dat start of run)
			while(!updStatus('fetching', 'processing', '1', curData.length));
			
			//set queue total
			queue = curData.length;

			//start fetch status updater
			checkStat = setInterval(fetchProgress, 500);

			return true;
		}

	/* count ids */
		function actionCountIds() {
			//if no ids are set, break
			if ( $('rests_ids').value == '' ) {
					while(!updStatus('No action selected/No IDs to act on', 'error', '', ''));
				fdLog.debug('WEBCOPY: No action selected/No IDs to act on'); 

				return true;
			}else{
				while(!updStatus('couting IDs...', 'counting', '', ''));
				var count=0;
				var cont;
				var contArr = new Array;
				cont = $('rests_ids').value.replace(/\n*$/, '');
				contArr = cont.split('\n');
				count = contArr.length;
				while(!updStatus('Count: '+count, 'done.', '', ''));
			}

			return true;
		}

	/* set restOpt and help text on radio button clicks */
		function rest_type() {			
			switch (this.id) {
				case 'rest_type_add':
					$('opt_descrip_rest').innerHTML = 'Change restrictions, old restrictions removed first<br /><br />CHANGE: EXA<br />BEFORE: EX1,EX2<br />AFTER: EXA';
					restOpt = 'add';
					break;
				case 'rest_type_rem':
					$('opt_descrip_rest').innerHTML = 'Remove restrictions, leaves exsiting (if any)<br /><br />REMOVE: EX2<br />BEFORE: EX1,EX2,EX3<br />AFTER: EX1,EX3';
					restOpt = 'rem';
					break;
				case 'rest_type_app':
					$('opt_descrip_rest').innerHTML = 'Appends restrictions to existing restrictions<br /><br />APPEND: EXA<br />BEFORE: EX1,EX2<br />AFTER: EX1,EX2,EXA';
					restOpt = 'app';
					break;
				case 'rest_type_clr':
					$('opt_descrip_rest').innerHTML = 'Clears any existing restrictions<br /><br />CLEAR: (ignored)<br />BEFORE: EX1,EX2<br />AFTER: (none)';
					restOpt = 'clr';
					break;
			}
		}

		function rest_type_f1(e) {	
			fdLog.debug("1111111111###########################" + e.srcElement.id);
			switch (e.srcElement.id) {
				case 'rest_type_add':
					$('opt_descrip_rest').innerHTML = 'Change restrictions, old restrictions removed first<br /><br />CHANGE: EXA<br />BEFORE: EX1,EX2<br />AFTER: EXA';
					restOpt = 'add';
					break;
				case 'rest_type_rem':
					$('opt_descrip_rest').innerHTML = 'Remove restrictions, leaves exsiting (if any)<br /><br />REMOVE: EX2<br />BEFORE: EX1,EX2,EX3<br />AFTER: EX1,EX3';
					restOpt = 'rem';
					break;
				case 'rest_type_app':
					$('opt_descrip_rest').innerHTML = 'Appends restrictions to existing restrictions<br /><br />APPEND: EXA<br />BEFORE: EX1,EX2<br />AFTER: EX1,EX2,EXA';
					restOpt = 'app';
					break;
				case 'rest_type_clr':
					$('opt_descrip_rest').innerHTML = 'Clears any existing restrictions<br /><br />CLEAR: (ignored)<br />BEFORE: EX1,EX2<br />AFTER: (none)';
					restOpt = 'clr';
					break;
			}
		}

		
	/*	fetch page (non-webcopy, uses urlSapId)
	 *
	 *	item [INT] : item in curData to apply to
	 *	sapId [STR] : sapId to complete urlSapId
	 *	optStatus [STR] : optional status to set (defaults to 'fetched')
	 */
		function fetchData(item, sapId, optStatus) {
			var fetchedStatus = optStatus || 'fetched';
			if (doGet) {
				fdLog.debug('Fetching '+sapId); 
				$('fetchitem'+item).innerHTML = curData[item][0]+' : '+curData[item][1];
				doGet = false;
				fdLog.debug("Ajax call for1:"+ urlSapId+sapId);
				new Ajax.Request(urlSapId+sapId, {
						method: 'GET',
						onComplete: function(responseDetails) {
									curData[item][1] = fetchedStatus;
									curData[item][2] = responseDetails.status;
									curData[item][3] = responseDetails.responseText;
									$('fetchitem'+item).innerHTML = curData[item][0]+' : '+curData[item][1];
									doGet = true;
									fdLog.debug('Fetched '+sapId); 
								},
						onError: function(responseDetails) {
									curData[item][1] = 'error';
									curData[item][2] = responseDetails.status;
									curData[item][3] = responseDetails.responseText;
									$('fetchitem'+item).innerHTML = curData[item][0]+' : '+curData[item][1];
									fdLog.debug('Error... '+responseDetails.status+' '+curData[item][3]); 
								}
					});	
					return true;
			}
		}

/* -- REST functions -- */

/* -- WEBCOPY functions -- */

	/* fetch progress (webcopy) */
		function fetchWcProgress() {
			//only run if checkStat is running
			if (checkStat) {
				
				//update status (this probably won't show if quick)
				while(!updStatus('fetching', 'processing', n, ''));
				for (var n = 0; n < curData.length; n++ ) {
					if (curData[n][1] == 'queued') {
						$('fetchitem'+n).innerHTML = curData[n][0]+'=>'+curData[n][10]+' : '+curData[n][1];
						if (!shouldGet) { 
							fdLog.debug('Skipping GET of item: '+n); 
							//set doPost to false
							doGet = false;
							//set fake 'get' data
							curData[n][1] = 'changed';
						}
						if (doGet) {
							//fetch
							while(!fetchDataWebCopy(n, curData[n][0], curData[n][10]));
							
							$('fetchitem'+n).innerHTML = curData[n][0]+'=>'+curData[n][10]+' : '+curData[n][1];
						}
					}
					if (curData[n][1] == 'fetched' && curData[n][2] == '200') {
						//once it's fetched and successful, webcopy is done, move to changed status
						curData[n][1] = 'changed';
					}else{
						//not often enough to be seen
						while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
					}
					if (curData[n][1] == 'changed') {
						//changed == copied, update and remove from queue
						curData[n][1] = 'copied';
						$('fetchitem'+n).innerHTML = '<A href="'+urlWebCopy+curData[n][10]+'" target="_new">'+curData[n][10]+'</a> : '+curData[n][1];
						queue--;
					}
					
				}
				if (queue <= 0) {

					//set timer
					timeEnd = makeTime('end');

					//clear status updater
					checkStat = clearInterval(checkStat);
						var time_run = makeTime('formatted', timeStart, timeEnd);
						fdLog.debug('all changes posted ('+time_run+')'); 
						while(!updStatus('processing done in '+time_run, 'done.', curData.length, ''));
				}
			}			
						
		}

	/* fetch page (webcopy) */
		function fetchDataWebCopy(item, baseSKU, targetSKU) {
			if (doGet) {
				fdLog.debug('Fetching '+baseSKU+'=>'+targetSKU); 
				$('fetchitem'+item).innerHTML = curData[item][0]+'=>'+curData[item][10]+' : '+curData[item][1];
				doGet = false;
					fdLog.debug("Ajax call for2:"+ urlWebCopy+targetSKU+'&action=copy&sourceId='+baseSKU);
					new Ajax.Request(urlWebCopy+targetSKU+'&action=copy&sourceId='+baseSKU, {
						method: 'GET',
						onComplete: function(responseDetails) {
									curData[item][1] = 'fetched';
									curData[item][2] = responseDetails.status;
									curData[item][3] = responseDetails.responseText;
									$('fetchitem'+item).innerHTML = curData[item][0]+'=>'+curData[item][10]+' : '+curData[item][1];
									doGet = true;
									fdLog.debug('Fetched '+baseSKU+'=>'+targetSKU); 
								},
							onError: function(responseDetails) {
									curData[item][1] = 'error';
									curData[item][2] = responseDetails.status;
									curData[item][3] = responseDetails.responseText;
									$('fetchitem'+item).innerHTML = curData[item][0]+'=>'+curData[item][10]+' : '+curData[item][1];
									doGet = true;
									fdLog.debug('Error '+baseSKU+'=>'+targetSKU); 
								},
							on500: function(responseDetails) {
									curData[item][1] = 'error (500)';
									curData[item][2] = responseDetails.status;
									curData[item][3] = responseDetails.responseText;
									$('fetchitem'+item).innerHTML = curData[item][0]+'=>'+curData[item][10]+' : '+curData[item][1];
									doGet = true;
									fdLog.debug('Error '+baseSKU+'=>'+targetSKU); 
								}
					});	
					return true;
			}
		}

	/* Run Web Copy */
		function actionWC() {
			//set timer
			timeStart = makeTime('start');

			//check to see if any action should take place
			
			//if no ids are set, break
			if ( $('wc_ids').value == '' ) {
					while(!updStatus('No action selected/No IDs to act on', 'error', '', ''));
				fdLog.debug('WEBCOPY: No action selected/No IDs to act on'); 
				return true;
			}
			//clear old data
			curData = [];

			//update status
			while(!updStatus('checking SKUs...', 'checking', '0', '0'));

			//clear output status
			$('status_of').innerHTML = 'of';
			$('status_cont').innerHTML = '<hr />';
			
			//temp arrays for dupecheck
			var tempIdsArr = new Array();
			var dupeArr = new Array();
			//temp var for error count
			var errors = 0;
			//temp var for dupe count
			var dupes = 0;

			//start processing id
			while(!updStatus('checking for dupe IDs', 'checking', '', ''));

			//get id list and split into an array for use
			//remove extra newline
			var cont = $('wc_ids').value.replace(/\n*$/, '');
			//trim out spaces NEEDS FIX
			//cont = $('wc_ids').value.replace(/[ ]*/g, '');
			//change tabs to commas
			cont = $('wc_ids').value.replace(/\t/g, ',');
				//$('wc_ids').value = cont;//$('wc_ids').value.replace(/\t/g, ',');
			var tempIdsArr = cont.split('\n');

			//pre-clean

			//loop through temp array, and add to curData
			if ( isArray(tempIdsArr) ) {
				var curLine = 0;
				for (var n=0; n < tempIdsArr.length; n++ ) {
					//if item is not already defined
					//at this point, we are still using "BASE,TARGET"
					if (!dupeArr.inArray(tempIdsArr[n])) {

						//split at comma now
						var tempWcData = tempIdsArr[n].split(',');

						//check here for an error
						if (tempWcData.length != 2 || tempWcData[0] == '' || tempWcData[1] == '') {
							fdLog.debug('curData error : '+n+' : '+tempIdsArr[n]); 
							errors++;
						}else{
							//add it
							dupeArr.push(tempIdsArr[n]);
							curData.push(curLine);
							curData[curLine] =  new Array(curDataLength);
							curData[curLine][7] = new Array(curDataLengthSeven);
							curData[curLine][8] = new Array(curDataLengthEight);

							curData[curLine][0] = tempWcData[0];
							curData[curLine][10] = tempWcData[1];
							curData[curLine][1] = 'queued';
								fdLog.debug('Added item '+curLine+' to curData'); 
							
							//add item line div
							while(!insEle('status_cont', 'div', 'I', 'fetchitem'+curLine)) {}
							while(!updStatus('ID ok '+curData[curLine], 'checking', '', ''));
							curLine++;
						}
						
					}else{
						//skip
							fdLog.debug('Skipped item '+curLine+' : '+tempIdsArr[n]+' (dupe)'); 
						while(!updStatus('skipping dupe '+curData[curLine], 'checking', '', ''));
						dupes++;
					}
				}
			}
			while(!updStatus('dupe check done ('+dupes+' skipped)', 'checking', '', curData.length, ''));
			
				fdLog.debug('Error Count: '+errors); 
				fdLog.debug('Dupe Count: '+dupes); 

			//start processing id
			//update status
			while(!updStatus('fetching', 'processing', '1', curData.length));

			//set queue total
			queue = curData.length;

			//start fetch status updater
			checkStat = setInterval(fetchWcProgress, 500);

			return true;
		}

/* -- WEBCOPY functions -- */

/* -- CAO functions -- */
	
	/* view a product, applies status to CAO_content[0] */
		function viewProductTemplate(skuCode) {
			if (doGet) {
				fdLog.debug('Viewing '+skuCode); 
				doGet = false;
					fdLog.debug("Ajax call for3:"+ urlCAOView+skuCode);
					new Ajax.Request(urlCAOView+skuCode, {
						method: 'GET',
						onComplete: function(responseDetails) {
									CAO_content[0] = "VIEWED";
									doGet = true;
									fdLog.debug('viewed '+skuCode); 
								},
							onError: function(responseDetails) {
									fdLog.debug('Error viewing '+skuCode); 
									if (skuCode == skuCAOmain) {
										//try back up
										while(!viewProductTemplate(skuCAObackup));
									}else{
										//epic fail
										CAO_content[0] = "ERROR";
										CAO_content[1] = "Error fetching CAO data!";
									}
								}
					});	
					return true;
			}
		}
	
	/* run CAO */
		function actionCAO() {
			fdLog.debug('running CAO'); 

			//set timer
			timeStart = makeTime('start');


			if ( $('CAO_ids').value == '' ) {
					while(!updStatus('No action selected/No IDs to act on', 'error', '', ''));
					fdLog.debug('REST: No action selected/No IDs to act on'); 
				return true;
			}

			//clear old data
			curData = [];

			//update status
			while(!updStatus('checking IDs...', 'checking', '0', '0'));

			//clear output status
			$('status_of').innerHTML = 'of';
			$('status_cont').innerHTML = '<hr />';
			
			//temp arrays for dupecheck
			var tempIdsArr = new Array();
			var dupeArr = new Array();
			//temp var for dupe count
			var dupes = 0;
			
			//start processing id
			while(!updStatus('checking for dupe IDs', 'checking', '', ''));

			//get id list and split into an array for use
			//remove extra newline
			var cont = $('CAO_ids').value.replace(/\n*$/, '');
			var tempIdsArr = cont.split('\n');

			fdLog.debug('tempIdsArr '+tempIdsArr); 

			//loop through temp array, and add to curData
			if ( isArray(tempIdsArr) ) {
				var curLine = 0;
				for (var n=0; n < tempIdsArr.length; n++ ) {

					//if item is not already defined
					if (!dupeArr.inArray(tempIdsArr[n])) {
						//add it
						
						dupeArr.push(tempIdsArr[n]);
						curData.push(curLine);
						curData[curLine] =  new Array(curDataLength);
						curData[curLine][7] = new Array(curDataLengthSeven);
						curData[curLine][8] = new Array(curDataLengthEight);
						//add copy of CAOtemplate
						curData[curLine][11] = CAOtemplate.clone();
						curData[curLine][0] = tempIdsArr[n];
						curData[curLine][1] = 'queued';
						curData[curLine][7][0] = '{SKIP}'; //safety
						curData[curLine][8][0] = '{SKIP}'; //safety

						fdLog.debug('Added item '+curLine+' to CurData'); 
						
						//add item line div
						while(!insEle('status_cont', 'div', 'I', 'fetchitem'+curLine)) {}
						while(!updStatus('ID ok '+curData[curLine], 'checking', '', ''));
						
						fdLog.debug('before curData[curLine][11]: ' + curData[curLine][11]); 

						//loop through CAOtemplate and set user-vals
						for (var index in CAOtemplate) {		
							fdLog.debug('index '+ document.getElementById(index).value); 
							if ($(index)) {								
								curData[curLine][11][index] = $(index).value;
								fdLog.debug("Printing" + curData[curLine][11][index] + " | curLine:" + curLine + " | index:" + index );
							}							
						}
						
						fdLog.debug('after curData[curLine][11]: ' + curData[curLine][11]); 

						curLine++;
						fdLog.debug('came here'); 
					}else{
						//skip
							fdLog.debug('Skipped item '+curLine+' : '+tempIdsArr[n]+' (dupe)'); 
						while(!updStatus('skipping dupe '+curData[curLine], 'checking', '', ''));
						dupes++;
					}
				}
			}
			while(!updStatus('dupe check done ('+dupes+' skipped)', 'checking', '', curData.length, ''));
			
				fdLog.debug('Dupe Count: '+dupes); 

			//start processing id
			//update status (called at start of run)
			while(!updStatus('fetching', 'processing', '1', curData.length));
			
			//set queue total
			queue = curData.length;

			//start fetch status updater
			checkCAO = setInterval(caoFetchProgress, 500);

			return true;
		}

	/* CAO setup progress */
		function caoSetupProgress() {
			fdLog.debug('in here:' + checkCAO); 
			if (checkCAO) {
				checkCAOKS--;
				fdLog.debug('checkCAOKS '+checkCAOKS); 

				$('CAO_fetching').innerHTML = ani($('CAO_fetching').innerHTML);
				//you need to view a product first, any product (existing)
				if (CAO_content[0] == "") {
					viewProductTemplate(skuCAOmain);
				}
				if (CAO_content[0] == "VIEWED") {
					fetchCAOTemplate();
				}
				if (CAO_content[0] == "PARSE") {
					while(!parseCAOTemplate(CAO_content[1]));
				}
				if ( (CAO_content[0] == "HTML" && CAO_content[1] != "") || (CAO_content[0] == "ERROR") || checkCAOKS<0 ) {
					if (checkCAOKS<0) {
						//fail
						CAO_content[0] = "ERROR";
						CAO_content[1] = "Error fetching CAO data! (Timed Out)";	
					}
					//set visible html
					$('CAO_data').innerHTML = CAO_content[1];

					//reset checkCAOKS jik
					checkCAOKS = 60;
					
					fdLog.debug('before Updated timer in here:' + checkCAO); 
					
					//clear updater
					checkCAO = window.clearInterval(checkCAO);
					
					//set as done so KOS can proceed
					caoDone = true;
					
					fdLog.debug('Updated timer in here:' + checkCAO); 
				}
			}
			
		}
	
	/* fetch CAO page for template */
		function fetchCAOTemplate() {
			if (doGet) {
				fdLog.debug('fetching CAO '); 
				doGet = false;
					fdLog.debug("Ajax call for4:"+ urlCAO);
					new Ajax.Request(urlCAO, {
						method: 'GET',
						onComplete: function(responseDetails) {
								CAO_content[0] = "PARSE";
								CAO_content[1] = responseDetails.responseText;
								doGet = true;
								fdLog.debug('fetched CAO '); 
							},
						onError: function(responseDetails) {
								fdLog.debug('Error fetching CAO'); 
								//fail
								CAO_content[0] = "ERROR";
								CAO_content[1] = "Error fetching CAO data!";
							}
					});	
					return true;
			}
		}

	/* fetch CAO page */
		function fetchCAO(item, afterPost) {
			fdLog.debug('fetching CAO '); 
			doGet = false;
				fdLog.debug("Ajax call for5:"+ urlCAO);
				new Ajax.Request(urlCAO, {
					method: 'GET',
					onComplete: function(responseDetails) {
						//call parser to parse and store info
						fdLog.debug("Parsing response");
						if (afterPost) {
							fdLog.debug("Parsing response: afterPost");
							curData[item][1] = "fetched2";
							//fetching after changes posted
							while(!parseCAO(item, responseDetails.responseText, 3));
						}else{
							fdLog.debug("Parsing response: not afterPost");
							curData[item][1] = "fetched";
							//fetching original
							while(!parseCAO(item, responseDetails.responseText));
						}
						
						doGet = true;
						fdLog.debug('fetched CAO '); 
					},
					onError: function(responseDetails) {
						fdLog.debug('Error fetching CAO '+item); 
						//fail
						curData[item][1] = "error";
						curData[item][2] = responseDetails.responseText;
					}
				});	
			return true;
		}
	
	/*
	 *	parses CAO page
	 *		pass in CAO page source as string
	 *			item as INT for curData[item][11]
	 *	puts each value in curData[item][11][key][optStoreInVal]
	 *		where optStoreInVal defaults to 1
	 */
		function parseCAO(item, CAOData, optStoreIn) {
			fdLog.debug('Parsing CAO...'); 

			var optStoreInVal = optStoreIn || 1;

			//using a regexp
			var rePattern = /name=\"([^\"]*)\".*value=\"([^\"]*)\"[\s]?([^\s\>]*)[^>]*\>\<\/td\>\<td\>([^\<]*)/gi;

			var CAODataTemp = CAOData.match(rePattern);

			if (CAODataTemp != null && CAODataTemp.length > 0) {
				//parse and add to template
				for (var index = 0; index < CAODataTemp.length; index++) {
					var t = CAODataTemp[index];

					t = t.replace('name="', '');
					t = t.replace('" type="checkbox" value="', '|');
					t = t.replace('" CHECKED ></td><td>', '|CHECKED|');
					t = t.replace('"  ></td><td>', '|');
					t = t.replace('</td><td>', '|');
					t = t.replace('" CHECKED', '|CHECKED|');
					t = t.replace(',\'claim\'', '');
					t = t.replace(',\'allergen\'', '');
					t = t.replace(',\'organic\'', '');
					t = t.replace('"  onClick', '" onClick');
					t = t.replace('" onClick', '|');

					
					t = t.split('|');
					/*
						82 allergen|MC_PEA|CHECKED|Produced in our bakery, where tree nuts and peanuts are used.
						83 organic|NONE|CHECKED| onClick="clearAll(13)">|None
						84 organic|ORGN|Organic
					*/
					
					var key = t[0]+'|'+t[1];
					//see if t[2] == 'CHECKED', if so match in stored values
					if (t[2] == 'CHECKED') {
						curData[item][11][key][optStoreInVal] = true;
						fdLog.debug('parseCAO '+item, optStoreInVal, curData[item][11]); 
					}else{
						curData[item][11][key][optStoreInVal] = false;
					}

				}
			
			}else{
				CAO_content[0] = "ERROR";
				CAO_content[1] = "Error parsing CAO data!";
			}

			return true;
		}


	/*
	 *	parses CAO page for template
	 *		pass in CAO page source as string
	 *		parsed data as array in the form [COLUMN][NAME][TEXT]
	 */
		function parseCAOTemplate(CAOData) {
			fdLog.debug('Parsing CAO (template)...'); 

			//using a regexp
			var rePattern = /name=\"([^\"]*)\".*value=\"([^\"]*)\"[\s]?([^\s\>]*)[^>]*\>\<\/td\>\<td\>([^\<]*)/gi;

			var CAODataTemp = CAOData.match(rePattern);

			if (CAODataTemp != null && CAODataTemp.length > 0) {
				//parse and add to template
				for (var index = 0; index < CAODataTemp.length; index++) {
					var t = CAODataTemp[index];

					t = t.replace('name="', '');
					t = t.replace('" type="checkbox" value="', '|');
					t = t.replace('" CHECKED ></td><td>', '|CHECKED|');
					t = t.replace('"  ></td><td>', '|');
					t = t.replace('</td><td>', '|');
					t = t.replace('" CHECKED', '|CHECKED|');
					t = t.replace(',\'claim\'', '');
					t = t.replace(',\'allergen\'', '');
					t = t.replace(',\'organic\'', '');
					t = t.replace('"  onClick', '" onClick');
					t = t.replace('" onClick', '|');

					
					t = t.split('|');
					/*
						82 allergen|MC_PEA|CHECKED|Produced in our bakery, where tree nuts and peanuts are used.
						83 organic|NONE|CHECKED| onClick="clearAll(13)">|None
						84 organic|ORGN|Organic
					*/

					var disp_text = '';
					var cur_val = false;
					var use_val = 'o';
					var stat_val = '';
					
					//attempt to get disp_text, it should always be last
					disp_text = t[t.length-1];

					CAOtemplate[t[0]+'|'+t[1]] = new Array(disp_text, cur_val, use_val, stat_val);

				}
				
					fdLog.debug('CAOtemplate ', CAOtemplate); 
			
			}else{
				CAO_content[0] = "ERROR";
				CAO_content[1] = "Error parsing CAO data!";
			}

			//send data over to generate HTML
			genCAOHTML();


			return true;
		}
	
	/*
	 *	generates html using CAOtemplate
	 *	puts html in CAO_content[1]
	 */
		function genCAOHTML() {
			fdLog.debug('Generating CAO HTML...'); 

			CAO_content[0] = "HTML";
			CAO_content[1] = "<table>";
			CAOsection = '';
			for (var index in CAOtemplate) {
				
				//skip over any functions we've added to array objectsc
				if (typeof CAOtemplate[index] == 'function') { continue; }

				var t = CAOtemplate[index];

				key = index.split('|');

				if (key[0] != CAOsection) {
					CAOsection = key[0];
					CAO_content[1] += "<tr><th colspan=\"2\">"+CAOsection+"</th></tr>";
				}

				CAO_content[1] += "<tr>";
				
				CAO_content[1] += "<td class=\"CAO_opt\"><input type=\"button\" onclick=\"this.value=ani(this.value, this.id); return false;\" id=\""+CAOsection+"|"+key[1]+"\" class=\"o\" value=\"o\" /></td>";

				CAO_content[1] += "<td>"+t[0]+"</td>";

				CAO_content[1] += "</tr>";

			}
			CAO_content[1] += "</table>";
			
			return true;
		}
	
	/*
	 *	generates html using CAOdata in the form of CAOtemplate
	 */
		function genResultsCAOHTML(CAOdata, debug) {
			var debugV = debug || false;
			var h = "<table><tr><td width=\"33%\">";
			CAOsection = '';
			var openTable = false;
			var odd = true;
			for (var index in CAOdata) {
				
				//skip over any functions we've added to array objects
				if (typeof CAOdata[index] == 'function') { continue; }

				var t = CAOdata[index];

				key = index.split('|');

				if (key[0] != CAOsection) {
					CAOsection = key[0];
					if (openTable) { h += '</table></td><td width=\"33%\">'; }
					h += "<table><tr><th colspan=\"2\">"+CAOsection+"</th></tr>";
					openTable = true;
					odd = true;
				}

				var oVal = CAOdata[index][1];
				var uVal = CAOdata[index][2];
				var pVal = CAOdata[index][3];

				var valBGColor = '#333';
				var valFGColor = '#fff';

				if (uVal == '+' || uVal == '-') {
					if (oVal == true && uVal == '-') {
						if (pVal == true) {
							// err
							valBGColor = '#f00';
							alert('genResultsCAOHTML err '+oVal+uVal+pVal);
						}else{
							//matches
							valBGColor = '#fcc';
							valFGColor = '#333';
						}
					}
					if (oVal == false && uVal == '+') {
						if (pVal == false) {
							// err
							valBGColor = '#f00';
							alert('genResultsCAOHTML err '+oVal+uVal+pVal);
						}else{
							//matches
							valBGColor = '#cfc';
							valFGColor = '#333';
						}
					}
				}

				h += "<tr>";
				
				//test randomize
				(debugV)
					? data = Math.round(Math.random())
					: data = t[3];

				if (data) { 
					h += '<th style="background-color: '+valBGColor+'; color: '+valFGColor+';" >x</th>';
				}else{
					h += "<td style=\"background-color: ";
					if ( valBGColor == '#fcc' ) {
						 h += "#fcc;"
					}else{
						(odd)
							? h += "#ddd;"
							: h += "#fff;";
					}
					h += "\">&nbsp;</td>";
				}

				h += "<td style=\"background-color: ";

				(odd)
					? h += "#ddd;"
					: h += "#fff;";

				h += "\">"+t[0]+"</td>";

				h += "</tr>";
				odd = !odd;
			}
			h += "</table>";
			h += "</td></tr></table>";

			$('overlay').innerHTML = h;
		}

	/*
	 *	caoFetchProgress
	 *
	 *	this should go:
	 *		queued	(lookup sku) => viewed
	 *		viewed	(fetch CAO page) => fetched
	 *		fetched	(parse out dif) => changed
	 *		changed	(post back changes) => posted
	 *		posted	(lookup sku again) => view2
	 *		fetched	(fetch CAO page again) => (results stored), remove from queue
	 *
	 *	we can't move on until item is posted
	 */
		function caoFetchProgress() {
			//only run if checkCAO is running
			if (checkCAO) {

				var workingItem = 0;

				//update status (this probably won't show if quick)
				while(!updStatus('fetching', 'processing', n, ''));

				for (var n = 0; n < curData.length; n++ ) {
					//workingItem keeps us on one item at a time
					if (workingItem == n) {

						// queued => viewed
						if (curData[n][1] == 'queued') {
							fdLog.debug('inside queued => viewed'); 
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							if (!shouldGet) { 
								fdLog.debug('Skipping GET of item: '+n); 
								//set doPost to false
								doGet = false;
								//set fake 'get' data
								curData[n][1] = 'changed';
							}
							if (doGet) {
								//view
								//is this call necessary?
								//while(!viewProduct(n, curData[n][0]));
								while(!viewProduct(n, urlCAOView));
								
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}
						}
						// viewed => fetched
						if (curData[n][1] == 'viewed' && (curData[n][2] == '200' || curData[n][2] == '500')) {
							fdLog.debug('inside viewed => fetched'); 
								
							if (curData[n][2] == '500') {
								//error
								curData[n][2] = '200'; //reset status code
								curData[n][5] = 'error'; //use POST status code to store the error
								curData[n][1] = 'done'; //set done status
								queue--;
								doGet = true;
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}else{
								//fetch CAO page
								while(!fetchCAO(n));
								
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}
						}
						//fetched => changed
						if (curData[n][1] == 'fetched' && curData[n][2] == '200') {
							fdLog.debug('inside fetched => changed'); 

							//here we need to compare data and construct a post string
							//if it has no changes we'll need to skip posting it
							
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							while(!updStatus('parsing', 'processing', n, ''));
								fdLog.debug('Parsing '+curData[n][0]); 
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							
							curData[n][1] = 'comparing';
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];

							//do compare
							var tempPostData = new Array();
								tempPostData[0] = 'action=claims';

							for (var index in curData[n][11]) {	
								fdLog.debug("__________Printing" + curData[n][11][index] + " | n:" + n + " | index:" + index );
								var oVal = curData[n][11][index];
								fdLog.debug("**oVal: " + oVal);
								//see if we need to post the value back (if any of these are true)								
								if (oVal == '+') {
									//we don't care what the oVal was
									tempPostData[tempPostData.length] = index.replace('|','=');
								}
							}
							
							fdLog.debug("Did I get any thing in tempost?" + tempPostData);
							
							if (tempPostData.length > 1) {
								//we have changes, construct string
								curData[n][4] = tempPostData.join('&');
								//remove extra &s
								while ( (curData[n][4]).indexOf('&&') >= 0 ) {
									curData[n][4] = curData[n][4].replace('&&', '&');
								}
								
								curData[n][1] = 'changed';
							}else{
								//skipper
								
								//set fake 'posted' data, and decrease status queue
								curData[n][1] = '(SKIP) posted';
								curData[n][5] = '200';
								queue--;
							}

							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						}else{
							//doesn't call here often
							while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
						}
						//changed => posted
						if (curData[n][1] == 'changed') {
							fdLog.debug('inside changed => posted'); 

							if (!shouldPost) { 
								fdLog.debug('Skipping POST of item: '+n); 
								//set doPost to false
								doPost = false;
								//set fake 'posted' data, and decrease status queue
								curData[n][1] = '(SKIP) posted';
								curData[n][5] = '200';
								queue--;
							}
							if (doPost) {
								urlPost = urlCAO;
								//now post
								while(!postBack(n));
							}
						}
						//
						if ((curData[n][1] == 'posted' || curData[n][1] == '(SKIP) posted') && curData[n][5] == '200') {
							while(!updStatus('posted '+curData[n][0], 'processing', '', ''));
							//this doesn't fire often
							//set status for no posting
							if (!shouldPost) { while(!updStatus('(NOT) posted '+curData[n][0], 'processing', '', '')); }

							fdLog.debug('fetchCAO again: '+n); 
							//fetch CAO page again
							curData[n][1] = 'fetch2';
							while(!fetchCAO(n, true));
							curData[n][1] = 'done';
						}

						if ((curData[n][1] == 'fetched2' || curData[n][1] == 'done') && curData[n][2] == '200') {
							curData[n][1] = 'done';

							if (curData[n][5] == 'error') {
								$('fetchitem'+n).innerHTML = curData[n][0]+' : error';
							}else{
								$('fetchitem'+n).innerHTML = '<a href="'+urlCAOView+curData[n][0]+'" target="_new">'+curData[n][0]+'</a> : '+curData[n][1]+' (<a href="#" onclick="genResultsCAOHTML(window[\'curData\']['+n+'][11], false); $(\'overlay\').toggle(); $(\'overlay\').focus(); return false;">status</a>)';
							}
							//move to next workingItem
							workingItem++;
						}else{
							while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						}
					}
								
				}
				if (queue <= 0) {
					//do status here

					//set timer
					timeEnd = makeTime('end');

					//clear status updater
					checkStat = clearInterval(checkCAO);
						var time_run = makeTime('formatted', timeStart, timeEnd);
						fdLog.debug('all changes posted ('+time_run+')'); 
						while(!updStatus('processing done in '+time_run, 'done.', curData.length, ''));

					//add copy of curData to page
					//unsafeWindow.curData = curData;
				}
			}
		}

/* -- CAO functions -- */

/* -- KOS functions -- */

	/* clear KOS choices
	 *	so if user chooses a symbol, the other items become false
	 */
		function clearOtherKOSChoices(refId) {
			//refId is needed so we can skip it
			if ($(refId).value == '-') {
				return;
			}
			//an id is section|item, so we'll use that to make sure we're in the right section to clear
			var section = refId.split('|')[0];
			//get items
			var items = $('KOS_data').getElementsByTagName('input');
			for (var i = 0; i<items.length; i++) {
				if ( (items[i].id).indexOf(section) == 0 ) {
					//right section
					if (items[i].id == refId) {
						//skip
					}else{
						//set off
						items[i].value = aniKOS('+', items[i].id);
					}
				}else{
					continue;
				}
			}

		}

	/* run KOS */
		function actionKOS() {
			fdLog.debug('running KOS');

			//set timer
			timeStart = makeTime('start');


			if ( $('KOS_ids').value == '' ) {
					while(!updStatus('No action selected/No IDs to act on', 'error', '', ''));
					fdLog.debug('REST: No action selected/No IDs to act on'); 
				return true;
			}

			//clear old data
			curData = [];

			//update status
			while(!updStatus('checking IDs...', 'checking', '0', '0'));

			//clear output status
			$('status_of').innerHTML = 'of';
			$('status_cont').innerHTML = '<hr />';
			
			//temp arrays for dupecheck
			var tempIdsArr = new Array();
			var dupeArr = new Array();
			//temp var for dupe count
			var dupes = 0;
			
			//start processing id
			while(!updStatus('checking for dupe IDs', 'checking', '', ''));

			//get id list and split into an array for use
			//remove extra newline
			var cont = $('KOS_ids').value.replace(/\n*$/, '');
			var tempIdsArr = cont.split('\n');

			fdLog.debug('tempIdsArr '+tempIdsArr); 

			//loop through temp array, and add to curData
			if ( isArray(tempIdsArr) ) {
				var curLine = 0;
				for (var n=0; n < tempIdsArr.length; n++ ) {

					//if item is not already defined
					if (!dupeArr.inArray(tempIdsArr[n])) {
						//add it
						
						dupeArr.push(tempIdsArr[n]);
						curData.push(curLine);
						curData[curLine] =  new Array(curDataLength);
						curData[curLine][7] = new Array(curDataLengthSeven);
						curData[curLine][8] = new Array(curDataLengthEight);
						//add copy of KOStemplate
						curData[curLine][12] = KOStemplate.clone();
						curData[curLine][0] = tempIdsArr[n];
						curData[curLine][1] = 'queued';
						curData[curLine][7][0] = '{SKIP}'; //safety
						curData[curLine][8][0] = '{SKIP}'; //safety

						fdLog.debug('Added item '+curLine+' to CurData'); 
						fdLog.debug('item curLine '+curData[curLine]); 
						
						//add item line div
						while(!insEle('status_cont', 'div', 'I', 'fetchitem'+curLine)) {}
						while(!updStatus('ID ok '+curData[curLine], 'checking', '', ''));
						
						fdLog.debug('curData[curLine][12] '+curData[curLine][12]); 

						//loop through KOStemplate and set user-vals
						for (var index in KOStemplate) {
							if ($(index)) {
								fdLog.debug('curLine:' + curLine + '|index '+ $(index).value); 
								curData[curLine][12][index] = $(index).value;
								fdLog.debug("Printing" + curData[curLine][12][index][2] + " | curLine:" + curLine + " | index:" + index );
							}
						}

						fdLog.debug('curData[curLine][12] '+curData[curLine][12]); 

						fdLog.debug('curData['+curLine+'] '+curData[curLine]); 
							
						curLine++;
					}else{
						//skip
							fdLog.debug('Skipped item '+curLine+' : '+tempIdsArr[n]+' (dupe)'); 
						while(!updStatus('skipping dupe '+curData[curLine], 'checking', '', ''));
						dupes++;
					}
				}
			}
			while(!updStatus('dupe check done ('+dupes+' skipped)', 'checking', '', curData.length, ''));
			
				fdLog.debug('Dupe Count: '+dupes); 

			//start processing id
			//update status (called at start of run)
			while(!updStatus('fetching', 'processing', '1', curData.length));
			
			//set queue total
			queue = curData.length;

			//start fetch status updater
			checkKOS = setInterval(kosFetchProgress, 500);

			return true;
		}

	/*	parses KOS page
	 *		pass in KOS page source as string
	 *			item as INT for curData[item][12]
	 *	puts each value in curData[item][12][key][optStoreInVal]
	 *		where optStoreInVal defaults to 1
	 */
		function parseKOS(item, KOSData, optStoreIn) {
			fdLog.debug('Parsing KOS...'); 

			var optStoreInVal = optStoreIn || 1;

			//because these are radio buttons, we need to parse a checked elem differently

			//using a regexp
			var rePattern = /name=\"([^\"]*)\".*value=\"([^\"]*)\"[\sA-Z]?([^\s\>]*)>([\s]*.*)/gi;

			var KOSDataTemp = KOSData.match(rePattern);

			if (KOSDataTemp != null && KOSDataTemp.length > 0) {
				//parse and add to template
				for (var index = 0; index < KOSDataTemp.length; index++) {
					var t = KOSDataTemp[index];

					if (t.indexOf('action" value="kosher">') > -1) { continue; }
					
					var isChecked = false;
					if (t.indexOf('CHECKED>') > -1) { isChecked = true; }

					t = t.replace('name="', '');
					t = t.replace('" type="radio" value="', '|');
					t = t.replace('CHECKED', '');
					t = t.replace('" onclick="selectNoImage(31)">', '|');
					t = t.replace('" >', '');
					t = t.replace(/[\r\n\t]*[\s]*</g, '|<');
					t = t.replace('|<br>', '');
					t = t.replace('img src="../', 'img src="./'); //fix image path

					//remove this, way too verbose
					//fdLog.debug(index, t); 

					t = t.split('|');
					
					var key = t[0]+'|'+t[1];
					//see if t[2] == 'CHECKED', if so match in stored values
					curData[item][12][key][optStoreInVal] = isChecked;

				}
			
			}else{
				KOS_content[0] = "ERROR";
				KOS_content[1] = "Error parsing KOS data!";
			}

			return true;
		}

	/* view a product, applies status to KOS_content[0] */
		function viewProductTemplateKOS(skuCode) {
			if (doGet) {
				fdLog.debug('Viewing '+skuCode); 
				doGet = false;
					fdLog.debug("Ajax call for8:"+ urlKOSView+skuCode);
					new Ajax.Request(urlKOSView+skuCode, {
							method: 'GET',
							onComplete: function(responseDetails) {
									KOS_content[0] = "VIEWED";
									doGet = true;
									fdLog.debug('viewed '+skuCode); 
								},
							onError: function(responseDetails) {
									fdLog.debug('Error viewing '+skuCode); 
									if (skuCode == skuKOSmain) {
										//try back up
										while(!viewProductTemplateKOS(skuKOSbackup));
									}else{
										//epic fail
										KOS_content[0] = "ERROR";
										KOS_content[1] = "Error fetching KOS data!";
									}
								}
							});
					return true;
			}
		}

	/* KOS setup progress */
		function KOSSetupProgress() { //we HAVE to wait for cao to finish
			if (checkKOS && caoDone) {

				checkKOSKS--;
				fdLog.debug('checkKOSKS '+checkKOSKS); 

				$('KOS_fetching').innerHTML = ani($('KOS_fetching').innerHTML);

				//you need to view a product first, any product (existing), cao takes care of this.
				if (KOS_content[0] == "" && CAO_content[0] == 'ERROR') {
					viewProductTemplate(skuKOSmain);
				}
				if (KOS_content[0] == "" && CAO_content[0] != 'ERROR') {
					KOS_content[0] = "VIEWED";
				}
				if (KOS_content[0] == "VIEWED") {
					fetchKOSTemplate();
				}
				if (KOS_content[0] == "PARSE") {
					KOS_content[0] = "PARSING";
					while(!parseKOSTemplate(KOS_content[1]));
					KOS_content[0] = "ERROR";
				}
				if ( (KOS_content[0] == "HTML" && KOS_content[1] != "") || (KOS_content[0] == "ERROR") || checkKOSKS<0 ) {
					if (checkKOSKS<0) {
						//fail
						KOS_content[0] = "ERROR";
						KOS_content[1] = "Error fetching KOS data! (Timed Out)";	
					}
					//set visible html
					$('KOS_data').innerHTML = KOS_content[1];

					//reset checkCAOKS jik
					checkKOSKS = 60;
					
					//clear updater
					checkKOS = clearInterval(checkKOS);
				}
			}
		}

	/* fetch KOS page for template */
		function fetchKOSTemplate() {
			if (doGet) {
				fdLog.debug('fetching KOS '); 
				doGet = false;
					fdLog.debug("Ajax call for9:"+ urlKOS);
					new Ajax.Request(urlKOS, {
							method: 'GET',
							onComplete: function(responseDetails) {
									KOS_content[0] = "PARSE";
									KOS_content[1] = responseDetails.responseText;
									doGet = true;
									fdLog.debug('fetched KOS '); 
								},
							onError: function(responseDetails) {
									fdLog.debug('Error fetching KOS'); 
									//fail
									KOS_content[0] = "ERROR";
									KOS_content[1] = "Error fetching KOS data!";
								}
							});
					return true;
			}
		}

	/* fetch KOS page */
		function fetchKOS(item, afterPost) {
			fdLog.debug('fetching KOS '); 
			doGet = false;
				fdLog.debug("Ajax call for10:"+ urlKOS);
					new Ajax.Request(urlKOS, {
						method: 'GET',
						onComplete: function(responseDetails) {
								//call parser to parse and store info
								if (afterPost) {
									curData[item][1] = "fetched2";
									//fetching after changes posted
									while(!parseKOS(item, responseDetails.responseText, 3));
								}else{
									curData[item][1] = "fetched";
									//fetching original
									while(!parseKOS(item, responseDetails.responseText));
								}
								
								doGet = true;
								fdLog.debug('fetched KOS '); 
							},
						oneError: function(responseDetails) {
								fdLog.debug('Error fetching KOS '+item); 
								//fail
								curData[item][1] = "error";
								curData[item][2] = responseDetails.responseText;
							}
						});
			return true;
		}

	/*	parses KOS page for template
	 *		pass in KOS page source as string
	 *		parsed data as array in the form [COLUMN][NAME][TEXT]
	 */
		function parseKOSTemplate(KOSData) {
			fdLog.debug('Parsing KOS (template)...'); 

			//using a regexp
			var rePattern = /name=\"([^\"]*)\".*value=\"([^\"]*)\"[\s]?([^\s\>]*)>([\s]*.*)/gi;

			var KOSDataTemp = KOSData.match(rePattern);
			if (KOSDataTemp != null && KOSDataTemp.length > 0) {
				//parse and add to template
				for (var index = 0; index < KOSDataTemp.length; index++) {
					var t = KOSDataTemp[index];

					if (t.indexOf('action" value="kosher">') > -1) { continue; }

					t = t.replace('name="', '');
					t = t.replace('" type="radio" value="', '|');
					t = t.replace('CHECKED', '');
					t = t.replace('" onclick="selectNoImage(31)">', '|');
					t = t.replace('" >', '');
					t = t.replace(/[\r\n\t]*[\s]*</g, '|<');
					t = t.replace('|<br>', '');
					//t = t.replace('img src="../', 'img src="./'); //fix image path

					t = t.split('|');

					var disp_text = '';
					var cur_val = false;
					var use_val = '-';
					var stat_val = '';
					
					//attempt to get disp_text, it should always be last
					disp_text = t[t.length-1];
					
					fdLog.debug("&&&&KOSTemplate, key:"+ t[0]+'|'+t[1]);

					KOStemplate[t[0]+'|'+t[1]] = new Array(disp_text, cur_val, use_val, stat_val);
					
				}
				
					fdLog.debug('KOStemplate '+ KOStemplate); 
			
			}else{
				KOS_content[0] = "ERROR";
				KOS_content[1] = "Error parsing KOS data!";
			}

			//send data over to generate HTML
			genKOSHTML();


			return true;
		}

	/*	generates html using KOStemplate
	 *	puts html in KOS_content[1]
	 */
		function genKOSHTML() {
			fdLog.debug('Generating KOS HTML...'); 

			KOS_content[0] = "HTML";
			KOS_content[1] = "<table>";
			KOSsection = '';
			for (var index in KOStemplate) {
				
				//skip over any functions we've added to array objectsc
				if (typeof KOStemplate[index] == 'function') { continue; }
				fdLog.debug('index '+index); 

				var t = KOStemplate[index];

				key = index.split('|');

				if (key[0] != KOSsection) {
					KOSsection = key[0];
					KOS_content[1] += "<tr><th colspan=\"2\">"+KOSsection.replace('_', ' ')+"</th></tr>";
				}

				KOS_content[1] += "<tr>";

				//set defaults
				var buttonVal = '-';
				var classNameVal = 'm';
				if (KOSsection+"|"+key[1] == 'kosher_type|NONE' || KOSsection+"|"+key[1] == 'kosher_symbol|NONE') {
					buttonVal = '+';
					classNameVal = 'p';
				}
				
				KOS_content[1] += "<td class=\"KOS_opt\"><input type=\"button\" onclick=\"this.value=aniKOS(this.value, this.id); clearOtherKOSChoices(this.id); return false;\" id=\""+KOSsection+"|"+key[1]+"\" class=\""+classNameVal+"\" value=\""+buttonVal+"\" /></td>";

				KOS_content[1] += "<td>"+t[0]+"</td>";

				KOS_content[1] += "</tr>";

			}
			KOS_content[1] += "</table>";

			fdLog.debug('...done.'); 

			return true;
		}
	
	/* generates html using KOSdata in the form of KOStemplate */
		function genResultsKOSHTML(KOSdata, debug) {
			var debugV = debug || false;
			var h = "<table><tr><td width=\"33%\">";
			KOSsection = '';
			var openTable = false;
			var odd = true;
			for (var index in KOSdata) {
				
				//skip over any functions we've added to array objects
				if (typeof KOSdata[index] == 'function') { continue; }

				var t = KOSdata[index];

				key = index.split('|');

				if (key[0] != KOSsection) {
					KOSsection = key[0];
					if (openTable) { h += '</table></td><td width=\"33%\">'; }
					h += "<table><tr><th colspan=\"2\">"+KOSsection.replace('_', ' ')+"</th></tr>";
					openTable = true;
					odd = true;
				}

				var oVal = KOSdata[index][1];
				var uVal = KOSdata[index][2];
				var pVal = KOSdata[index][3];

				var valBGColor = '#333';
				var valFGColor = '#fff';

				if (uVal == '+' || uVal == '-') {
					if (oVal == true && uVal == '-') {
						if (pVal == true) {
							// err
							valBGColor = '#f00';
							alert('genResultsKOSHTML err '+oVal+uVal+pVal);
						}else{
							//matches
							valBGColor = '#fcc';
							valFGColor = '#333';
						}
					}
					if (oVal == false && uVal == '+') {
						if (pVal == false) {
							// err
							valBGColor = '#f00';
							alert('genResultsKOSHTML err '+oVal+uVal+pVal);
						}else{
							//matches
							valBGColor = '#cfc';
							valFGColor = '#333';
						}
					}
				}

				h += "<tr>";
				
				//test randomize
				(debugV)
					? data = Math.round(Math.random())
					: data = t[3];

				if (data) { 
					h += '<th style="background-color: '+valBGColor+'; color: '+valFGColor+';" >x</th>';
				}else{
					h += "<td style=\"background-color: ";
					if ( valBGColor == '#fcc' ) {
						 h += "#fcc;"
					}else{
						(odd)
							? h += "#ddd;"
							: h += "#fff;";
					}
					h += "\">&nbsp;</td>";
				}

				h += "<td style=\"background-color: ";

				(odd)
					? h += "#ddd;"
					: h += "#fff;";

				h += "\">"+t[0]+"</td>";

				h += "</tr>";
				odd = !odd;
			}
			h += "</table>";
			h += "</td></tr></table>";

			$('overlay').innerHTML = h;
		}

	/*	kosFetchProgress
	 *
	 *	this should go:
	 *		queued	(lookup sku) => viewed
	 *		viewed	(fetch KOS page) => fetched
	 *		fetched	(parse out dif) => changed
	 *		changed	(post back changes) => posted
	 *		posted	(lookup sku again) => view2
	 *		fetched	(fetch KOS page again) => (results stored), remove from queue
	 *
	 *	we can't move on until item is posted
	 */
		function kosFetchProgress() {
			//only run if checkKOS is running
			if (checkKOS) {

				var workingItem = 0;

				//update status (this probably won't show if quick)
				while(!updStatus('fetching', 'processing', n, ''));

				for (var n = 0; n < curData.length; n++ ) {
					//workingItem keeps us on one item at a time
					if (workingItem == n) {

						// queued => viewed
						if (curData[n][1] == 'queued') {
							fdLog.debug('inside queued => viewed'); 
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							if (!shouldGet) { 
								fdLog.debug('Skipping GET of item: '+n); 
								//set doPost to false
								doGet = false;
								//set fake 'get' data
								curData[n][1] = 'changed';
							}
							if (doGet) {
								//view
								//is this call necessary?
								//while(!viewProduct(n, curData[n][0]));
								while(!viewProduct(n, urlKOSView));
								
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}
						}
						// viewed => fetched
						if (curData[n][1] == 'viewed' && (curData[n][2] == '200' || curData[n][2] == '500')) {
							fdLog.debug('inside viewed => fetched'); 
								
							if (curData[n][2] == '500') {
								//error
								curData[n][2] = '200'; //reset status code
								curData[n][5] = 'error'; //use POST status code to store the error
								curData[n][1] = 'done'; //set done status
								queue--;
								doGet = true;
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}else{
								//fetch KOS page
								while(!fetchKOS(n));
								
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}
						}
						//fetched => changed
						if (curData[n][1] == 'fetched' && curData[n][2] == '200') {
							fdLog.debug('inside fetched => changed'); 

							//here we need to compare data and construct a post string
							//if it has no changes we'll need to skip posting it
							
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							while(!updStatus('parsing', 'processing', n, ''));
								fdLog.debug('Parsing '+curData[n][0]); 
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							
							curData[n][1] = 'comparing';
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];

							//do compare
							var tempPostData = new Array();
								tempPostData[0] = 'action=kosher';
							fdLog.debug('curLine:' + n + 'curData[n][12]: '+curData[n][12]);   
							for (var index in curData[n][12]) {				
								fdLog.debug("______________________________:" + index);
								var oVal = curData[n][12][index];
								var uVal = curData[n][12][index];
								
								fdLog.debug("reading data to post, oVal:" + oVal);
								//see if we need to post the value back (if any of these are true)
								if (uVal == 'o') {
									//only look at oVal
									if (oVal == true) {
										tempPostData[tempPostData.length] = index.replace('|','=');
									}
								}
								if (uVal == '+') {
									//we don't care what the oVal was
									tempPostData[tempPostData.length] = index.replace('|','=');
								}
								if (uVal == '-' && oVal == true) {
									//do nothing, we want it off 
								}
							}
							
							if (tempPostData.length > 1) {
								//we have changes, construct string
								curData[n][4] = tempPostData.join('&');
								//remove extra &s
								while ( (curData[n][4]).indexOf('&&') >= 0 ) {
									curData[n][4] = curData[n][4].replace('&&', '&');
								}
								
								curData[n][1] = 'changed';
							}else{
								//skipper
								
								//set fake 'posted' data, and decrease status queue
								curData[n][1] = '(SKIP) posted';
								curData[n][5] = '200';
								queue--;
							}

							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							//fdLog.debug('curData'+n+':'+curData[n]); 
						}else{
							//doesn't call here often
							while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
						}
						//changed => posted
						if (curData[n][1] == 'changed') {
							fdLog.debug('inside changed => posted'); 

							if (!shouldPost) { 
								fdLog.debug('Skipping POST of item: '+n); 
								//set doPost to false
								doPost = false;
								//set fake 'posted' data, and decrease status queue
								curData[n][1] = '(SKIP) posted';
								curData[n][5] = '200';
								queue--;
							}
							if (doPost) {
								urlPost = urlKOS;
								//now post
								while(!postBack(n));
							}
						}
						//
						if ((curData[n][1] == 'posted' || curData[n][1] == '(SKIP) posted') && curData[n][5] == '200') {
							while(!updStatus('posted '+curData[n][0], 'processing', '', ''));
							//this doesn't fire often
							//set status for no posting
							if (!shouldPost) { while(!updStatus('(NOT) posted '+curData[n][0], 'processing', '', '')); }

							fdLog.debug('fetchKOS again: '+n); 
							//fetch KOS page again
							curData[n][1] = 'fetch2';
							while(!fetchKOS(n, true));
							curData[n][1] = 'done';
						}

						if ((curData[n][1] == 'fetched2' || curData[n][1] == 'done') && curData[n][2] == '200') {
							curData[n][1] = 'done';

							if (curData[n][5] == 'error') {
								$('fetchitem'+n).innerHTML = curData[n][0]+' : error';
							}else{
								$('fetchitem'+n).innerHTML = '<a href="'+urlKOSView+curData[n][0]+'" target="_new">'+curData[n][0]+'</a> : '+curData[n][1]+' (<a href="#" onclick="genResultsKOSHTML(window[\'curData\']['+n+'][12], false); $(\'overlay\').toggle(); $(\'overlay\').focus(); return false;">status</a>)';
							}
							//move to next workingItem
							workingItem++;
						}else{
							while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						}
					}
								
				}
				if (queue <= 0) {
					//do status here

					//set timer
					timeEnd = makeTime('end');

					//clear status updater
					checkStat = clearInterval(checkKOS);
						var time_run = makeTime('formatted', timeStart, timeEnd);
						fdLog.debug('all changes posted ('+time_run+')'); 
						while(!updStatus('processing done in '+time_run, 'done.', curData.length, ''));

					//add copy of curData to page
					curData = curData;
				}
			}
		}

/* -- KOS functions -- */

/* -- NBIS functions -- */
	
	//this regexp matches: "MM/DD/YY", "MM/DD/YYYY", "MM/DD/YY HH:MM" or "MM/DD/YYYY HH:MM"
		function date_verify(id) {
			fdLog.debug("got the Id:" + id);
			var v = $(id).value;
			if ( v == '' || v == '{DATA}' || v== '{SKIP}' || v.match(/^(0[1-9]|1[012])\/([123]0|[012][1-9]|31)\/(2[0-9]{3}|[0-9]{2})([\s]?(0[1-9]|1[012])[:]([0-5][0-9]))?$/gi) ) {
				$(id).style.color='';
				return true;
			}
			$(id).style.color='#f00';
			return false;
		}
		
		function date_verify_f1(e) {
			fdLog.debug("got the date value:" + e.srcElement.value);
			var v = e.srcElement.value;
			if ( v == '' || v == '{DATA}' || v== '{SKIP}' || v.match(/^(0[1-9]|1[012])\/([123]0|[012][1-9]|31)\/(2[0-9]{3}|[0-9]{2})([\s]?(0[1-9]|1[012])[:]([0-5][0-9]))?$/gi) ) {
				e.srcElement.style.color='';
				return true;
			}
			e.srcElement.style.color='#f00';
			return false;
		}

	/* run NBIS */
		function actionNBIS() {

			fdLog.debug('running NBIS'); 

			//set timer
			timeStart = makeTime('start');
			
			//check for ids
			if ( $('NBIS_ids').value == '' || (!$('NBIS_new_use').checked && !$('NBIS_bis_use').checked && !$('NBIS_dpud_use').checked) ) {
				while(!updStatus('No action selected/No IDs to act on', 'error', '', ''));
				fdLog.debug('NBIS: No action selected/No IDs to act on'); 
				return true;
			}
			
			//check that both aren't checked, only one at a time
			if ($('NBIS_new_use').checked && $('NBIS_bis_use').checked) {
				while(!updStatus('Cannot set New and BIS at the same time.', 'error', '', ''));
				fdLog.debug('NBIS: Cannot set New and Back In stock at the same time.'); 
				return true;
			}

			//clear old data
			curData = [];

			//update status
			while(!updStatus('checking IDs...', 'checking', '0', '0'));

			//clear output status
			$('status_of').innerHTML = 'of';
			$('status_cont').innerHTML = '<hr />';

			//temp arrays for dupecheck
			var tempIdsArr = new Array();
			var dupeArr = new Array();
			//temp var for dupe count
			var dupes = 0;

			//start processing id
			while(!updStatus('checking for dupe IDs', 'checking', '', ''));

			//get id list and split into an array for use
			//remove extra newline
			var cont = $('NBIS_ids').value.replace(/\n*$/, '');
			var tempIdsArr = cont.split('\n');

				fdLog.debug('tempIdsArr '+tempIdsArr); 

			//loop through temp array, and add to curData
			if ( isArray(tempIdsArr) ) {
				var curLine = 0;
				for (var n=0; n < tempIdsArr.length; n++ ) {

					//if item is not already defined
					if (!dupeArr.inArray(tempIdsArr[n])) {
						//add it
						
						dupeArr.push(tempIdsArr[n]);
						curData.push(curLine);
						curData[curLine] =  new Array(curDataLength);
							curData[curLine][7] = new Array(curDataLengthSeven);
							curData[curLine][8] = new Array(curDataLengthEight);
						curData[curLine][0] = tempIdsArr[n];
						curData[curLine][1] = 'queued';
						curData[curLine][7][6] = '{SKIP}'; //safety
						curData[curLine][7][7] = '{SKIP}'; //safety
						curData[curLine][7][8] = '{SKIP}'; //safety
						curData[curLine][8][6] = '{SKIP}'; //safety
						curData[curLine][8][7] = '{SKIP}'; //safety
						curData[curLine][8][8] = '{SKIP}'; //safety
						
						//pull data from line: WEBId [TAB] DATE
						var extraData = tempIdsArr[n].split('\t');
						if (extraData.length >= 2) { //we can safely ignore any extra data
							//0 == WEBId
							//1 == DATE

							curData[curLine][0] = extraData[0];
							
						}

						//check here for new date						
						if ( $('NBIS_new_use').checked ) {
							if ( $('NBIS_new_date').value == '{DATA}' ) {
								if (extraData[1] != '') {
									curData[curLine][7][6] = extraData[1];
									//if we want it empty, pass '{EMPTY}'
									if (extraData[1] == '{EMPTY}') {
										curData[curLine][7][6] = '';
									}
								}
							}else{
								fdLog.debug("NBIS_new_use is changed" + $('NBIS_new_date').value);
								curData[curLine][7][6] = $('NBIS_new_date').value;
							}
						}
						//check here for bis date
						if ( $('NBIS_bis_use').checked ) {
							if ( $('NBIS_bis_date').value == '{DATA}' ) {
								if (extraData[1] != '') {
									curData[curLine][7][7] = extraData[1];
									//if we want it empty, pass '{EMPTY}'
									if (extraData[1] == '{EMPTY}') {
										curData[curLine][7][7] = '';
									}
								}
							}else{
								curData[curLine][7][7] = $('NBIS_bis_date').value;
							}
						}
						//check here default pricing unit description
						if ( $('NBIS_dpud_use').checked ) {
							if ( $('NBIS_dpud_text').value == '{DATA}' ) {
								if (extraData[1] != '') {
									curData[curLine][7][8] = extraData[1];
									//if we want it empty, pass '{EMPTY}'
									if (extraData[1] == '{EMPTY}') {
										curData[curLine][7][8] = '';
									}
								}
							}else{
								curData[curLine][7][8] = $('NBIS_dpud_text').value;
							}
						}

						fdLog.debug('Added item '+curLine+' to CurData'); 

						fdLog.debug('item curLine '+curData[curLine]); 
						
						//add item line div
						while(!insEle('status_cont', 'div', 'I', 'fetchitem'+curLine)) {}
						while(!updStatus('ID ok '+curData[curLine], 'checking', '', ''));
						

						//check here for missing ID
						if (extraData[0] == '') {
							curData.pop(curLine);
						}else{
							curLine++;
						}
					}else{
						//skip
							fdLog.debug('Skipped item '+curLine+' : '+tempIdsArr[n]+' (dupe)'); 
						while(!updStatus('skipping dupe '+curData[curLine], 'checking', '', ''));
						dupes++;
					}
				}
			}

			while(!updStatus('dupe check done ('+dupes+' skipped)', 'checking', '', curData.length, ''));
			
			fdLog.debug('Dupe Count: '+dupes); 


			//start processing id
			//update status (called at start of run)
			while(!updStatus('fetching', 'processing', '1', curData.length));


			//set queue total
			queue = curData.length;

			//start fetch status updater
			checkNBIS = setInterval(nbisFetchProgress, 500);
			workingItem = 0; //reset workingItem

			return true;
		}

	
	/*
	 *	nbisFetchProgress
	 *
	 *	this should go:
	 *		queued	(lookup sku) => viewed
	 *		viewed	(post back changes) => changed
	 *		changed	(post back changes) => posted
	 *		posted	(lookup sku again) => view2
	 *		view2	=> (results stored), remove from queue
	 *
	 *	we can't move on until item is posted
	 */
		var workingItem = 0;
		function nbisFetchProgress() {
			//only run if checkNBIS is running
			if (checkNBIS) {

				if (workingItem > curData.length) { workingItem = 0; }
				//update status (this probably won't show if quick)
				while(!updStatus('fetching', 'processing', n, ''));
				
				for (var n = 0; n < curData.length; n++ ) {
				
					fdLog.debug("curData[n][1]=" + curData[n][1]);
					//workingItem keeps us on one item at a time
					if (workingItem == n) {

						// queued => viewed
						if (curData[n][1] == 'queued') {
							fdLog.debug('inside queued => viewed'); 
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							if (!shouldGet) { 
								fdLog.debug('Skipping GET of item: '+n); 
								//set doPost to false
								doGet = false;
								//set fake 'get' data
								curData[n][1] = 'changed';
							}
							if (doGet) {
								//view
								while(!viewProduct(n, urlGetNBIS, 'viewed'));
								
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}
						}
						//viewed => changed
						if (curData[n][1] == 'viewed' && curData[n][2] == '200') {
							fdLog.debug('inside viewed => changed'); 

							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							while(!updStatus('parsing', 'processing', n, ''));
								fdLog.debug('Parsing '+curData[n][0]); 
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							
							curData[n][1] = 'parsing';
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							while(!itemParse(n));

							curData[n][1] = 'changing';
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
								fdLog.debug('Applying changes to '+curData[n][0]); 
							while(!applyChange(n));
								fdLog.debug('Changes done for '+curData[n][0]); 
							fdLog.debug("Lets see what the postData has: " + postData);

							curData[n][4] = postData.join('&');
							
							fdLog.debug("11***Lets see what the postData has: " + curData[n][4]);

							//check for empty post vars
							/*
							for (var i=1; i < postData.length; i++) {
								fdLog.debug("TT***postdata: " + postData[i]);
								curData[n][4] = curData[n][4].replace(postData[i]+'&', '&');
							}*/
							
							fdLog.debug("22***Lets see what the postData has: " + curData[n][4]);

							//remove extra &s
							while ( (curData[n][4]).indexOf('&&') >= 0 ) {
								curData[n][4] = curData[n][4].replace('&&', '&');
							}
							
							curData[n][1] = 'changed';
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							
							fdLog.debug("33***Lets see what the postData has: " + curData[n][4]);
						}else{
							//doesn't call here often
							while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
						}
						//changed => posted
						if (curData[n][1] == 'changed') {
							fdLog.debug('inside changed => posted'); 

							if (!shouldPost) { 
								fdLog.debug('Skipping POST of item: '+n); 
								//set doPost to false
								doPost = false;
								//set fake 'posted' data, and decrease status queue
								curData[n][1] = '(SKIP) posted';
								curData[n][5] = '200';
								queue--;
							}
							if (doPost) {
								urlPost = urlPostNBIS;
								//now post
								while(!postBack(n));
							}
						}
						//
						if ((curData[n][1] == 'posted' || curData[n][1] == '(SKIP) posted') && curData[n][5] == '200') {
							while(!updStatus('posted '+curData[n][0], 'processing', '', ''));
							//this doesn't fire often
							//set status for no posting
							if (!shouldPost) { while(!updStatus('(NOT) posted '+curData[n][0], 'processing', '', '')); }

							fdLog.debug('fetchNBIS again: '+n); 
							doGet=true;
							//fetch CAO page again
							curData[n][1] = 'fetch2';
							while(!viewProduct(n, urlGetNBIS, 'fetched2'));
							curData[n][1] = 'done';
							while(!applyChange(n, true)); //apply change as verify
						}

						if ((curData[n][1] == 'fetched2' || curData[n][1] == 'done') && curData[n][2] == '200') {
							curData[n][1] = 'done';

							if (curData[n][5] == 'error') {
								$('fetchitem'+n).innerHTML = curData[n][0]+' : error';
							}else{
								$('fetchitem'+n).innerHTML = '<a href="'+urlGetNBIS+curData[n][0]+'" target="_new">'+curData[n][0]+'</a> : '+curData[n][1];
							}
							//move to next workingItem
							doGet=true;
							workingItem++;
						}else{
							while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						}
						
					}
								
				}
				if (queue <= 0) {
					//console.log(curData);
					//do status here

					//set timer
					timeEnd = makeTime('end');

					//clear status updater
					checkStat = clearInterval(checkNBIS);
						var time_run = makeTime('formatted', timeStart, timeEnd);
						fdLog.debug('all changes posted ('+time_run+')'); 
						while(!updStatus('processing done in '+time_run, 'done.', curData.length, ''));

					//make sure we left the lights on when we leave
					doGet = true;
				}
			}
		}

/* -- NBIS functions -- */

/* -- SHARED functions -- */

	/* view a product, applies status to curData[item] [1] */
		function viewProduct(item, url, status) {
			var url = url || urlCAOView;
			var status = status || 'viewed';
			if (doGet) {
				fdLog.debug('Viewing '+curData[item][0]); 
				doGet = false;
					fdLog.debug("Ajax call for6:"+ url+curData[item][0]);
					new Ajax.Request(url+curData[item][0], {
						method: 'GET',
						onComplete: function(responseDetails) {
									curData[item][1] = status;
									curData[item][2] = responseDetails.status;
									if (url == urlGetNBIS) {
										curData[item][3] = responseDetails.responseText;
									}
									//don't reset doGet here (do it in fetch)
									//doGet = true;
									fdLog.debug('viewed '+curData[item][0]); 
								},
						onError: function(responseDetails) {
									curData[item][1] = 'error';
									curData[item][2] = responseDetails.status;
									curData[item][3] = responseDetails.responseText;
									$('fetchitem'+item).innerHTML = curData[item][0]+' : '+curData[item][1];
									doGet = true;
									fdLog.debug('Error viewing'+curData[item][0]); 
								}
					});	
					return true;
			}
		}

	/* apply changes */
		function applyChange(item, doVerify) {
			
			//set into curData (defaults)
			if (doVerify) {
				curData[item][8][1] = '{SKIP}';
				curData[item][8][2] = '{SKIP}';
			}else{
				curData[item][7][1] = '{SKIP}';
				curData[item][7][2] = '{SKIP}';
			}

			//apply restriction changes if any
			if ( $('rests_use').checked ) {
				//cutoff at options missing required data
				if (!$('rests_names').value && restOpt != 'clr') {

					
					if (doVerify) {
						curData[item][8][1] = '{SKIP}';
						curData[item][8][2] = restOpt;
					}else{
						//set into curData
						curData[item][7][1] = '{SKIP}';
						curData[item][7][2] = restOpt;
					}

					return true;
				}

				
				//only if restriction is set to change
				var rest = postData[4].split('=');

				//combine code for rem and app
				if (restOpt == 'rem' || restOpt == 'app') {
					
						//new restriction holder
						var postArr = new Array();

						//check if restriction is already multiple
						if (rest[1].indexOf(',') == -1) {
							//no multi
							var oldVal = rest[1];
						}else{
							//multi
							var oldVal = rest[1].split(',');
						}
						//check if change is already multiple
						if ($('rests_names').value.indexOf(',') == -1) {
							//no multi
							var newVal = $('rests_names').value;
						}else{
							//multi
							var newVal = $('rests_names').value.split(',');
						}
				}

				switch (restOpt) {
				case 'add':
					//change old value to new value
					postData[4] = rest[0]+'='+$('rests_names').value;

					if (doVerify) {
						curData[item][8][1] = $('rests_names').value;
						curData[item][8][2] = restOpt;
					}else{
						//set into curData
						curData[item][7][1] = $('rests_names').value;
						curData[item][7][2] = restOpt;
					}

					break;
				case 'rem':
					//remove only specified value

					//status holder var
					var set = false;

					//check if we're dealing with an old array and new array
					if ( isArray(oldVal) && isArray(newVal) && !set ) {
						//alert(1);
						//array
						for (var n = 0; n < newVal.length; n++ ) {
							//if new val is not in old val, add it
							if (!oldVal.inArray(newVal[n])) {
								postArr.push(newVal[n]);
							}
						}
						//array
						for (var n = 0; n < oldVal.length; n++ ) {
							//if new val is not in old val, add it
							if (!newVal.inArray(oldVal[n])) {
								postArr.push(oldVal[n]);
							}
						}
						set = true;
					}
					//check if only old is an array
					if ( isArray(oldVal) && !isArray(newVal) && !set ) {
						//alert(2);
						for (var n = 0; n < oldVal.length; n++ ) {
							//if new val is not in old val, add it
							if (oldVal[n] != newVal) {
								postArr.push(oldVal[n]);
							}
						}
						set = true;
					}
					//check if only new is an array
					if ( !isArray(oldVal) && isArray(newVal) && !set ) {
						//alert(3);
						//array
						for (var n = 0; n < newVal.length; n++ ) {
							//if old val is not in new val, add it
							if (newVal[n] != oldVal) {
								postArr.push(newVal[n]);
							}
						}
						set = true;
					}
					//now just check var=var
					if (!set) {
						//alert(4);
						if (newVal != oldVal) {
							if (newVal)	{ postArr.push(newVal); }
							if (oldVal)	{ postArr.push(oldVal); }
						}
					}
					postData[4] = rest[0]+'='+postArr.join(',');

					if (doVerify) {
						curData[item][8][1] = postArr.join(',');
						curData[item][8][2] = restOpt;
					}else{
						//set into curData
						curData[item][7][1] = postArr.join(',');
						curData[item][7][2] = restOpt;
					}

					break;
				case 'app':
					//append specified value

					if (isArray(oldVal)) {
						//array
						for (var n = 0; n < oldVal.length; n++ ) {
							if (!postArr.inArray(oldVal[n])) {
								postArr.push(oldVal[n]);
							}
						}
					}else{
						if (oldVal && !postArr.inArray(oldVal)) { postArr.push(oldVal); }
					}
					if (isArray(newVal)) {
						//array
						for (var n = 0; n < newVal.length; n++ ) {
							if (!postArr.inArray(newVal[n])) {
								postArr.push(newVal[n]);
							}
						}
					}else{
						if (newVal && !postArr.inArray(newVal)) { postArr.push(newVal); }
					}
					postData[4] = rest[0]+'='+postArr.join(',');

					if (doVerify) {
						curData[item][8][1] = postArr.join(',');
						curData[item][8][2] = restOpt;
					}else{
						//set into curData
						curData[item][7][1] = postArr.join(',');
						curData[item][7][2] = restOpt;
					}

					break;
				case 'clr':
					//clear old value
					postData[4] = rest[0]+'=';

					if (doVerify) {
						curData[item][8][1] = '';
						curData[item][8][2] = restOpt;
					}else{
						//set into curData
						curData[item][7][1] = '';
						curData[item][7][2] = restOpt;
					}

					break;
				}
			}
			//apply advance_order_flag changes if any
			if ( $('advOrder_use').checked ) {
				//only if description is set to change
				if (postData[8]) {
					var advOrd = postData[8].split('=');
					($('advOrder_val_on').checked)
						?postData[8] = advOrd[0]+'=on'
						:postData[8] = '';
				}

			}
			//set into curData
			if ($('advOrder_use').checked) {
					if (doVerify) {
						($('advOrder_val_on').checked)
							?curData[item][8][3] = true
							:curData[item][8][3] = false;
					}else{
						($('advOrder_val_on').checked)
							?curData[item][7][3] = true
							:curData[item][7][3] = false;
					}
			}else{
				if (doVerify) {
					curData[item][8][3] = '{SKIP}';
				}else{
					curData[item][7][3] = '{SKIP}';
				}
			}

			//apply description changes if any
			if ( $('WD_use').checked ) {
				//only if description is set to change
				var desc = '';
					if (postData[5]) { desc = postData[5].split('='); }
				//safety
				if (curData[item][7][0] != '{SKIP}') {
					if (doVerify) {
						curData[item][8][0] = curData[item][7][0];
					}else{
						desc[1] = curData[item][7][0];
					}
					postData[5] = desc[0]+'='+desc[1];
				}
				fdLog.debug(' -      POST', item, postData[5]); 
			}

			
			//apply label name changes if any
			if ( $('LN_use').checked ) {
				//only if label name is set to change
				var desc = '';
					if (postData[1]) { desc = postData[1].split('='); }
				//safety
				if (curData[item][7][4] != '{SKIP}') {
					if (doVerify) {
						curData[item][8][4] = curData[item][7][4];
					}else{
						desc[1] = curData[item][7][4];
					}
					postData[1] = desc[0]+'='+desc[1];
				}
				fdLog.debug(' -      POST', item, postData[1]); 
			}

			
			//apply deposit changes if any
			if ( $('DEP_use').checked ) {
				//only if description is set to change
				var desc = '';
					if (postData[3]) { desc = postData[3].split('='); }
				//safety
				if (curData[item][7][5] != '{SKIP}') {
					if (doVerify) {
						curData[item][8][5] = curData[item][7][5];
					}else{
						desc[1] = curData[item][7][5];
					}
					postData[3] = desc[0]+'='+desc[1];
				}
				fdLog.debug(' -      POST', item, postData[3]); 
			}

			//items in this view (product_view) need to NOT pass if they're empty
			//apply new date changes if any
			if( $('NBIS_new_use').checked ) {
				//only if new date is set to change
				var newDate = postData[10].split('=');
				fdLog.debug("**********newDate:"+newDate);
				//safety
				if (curData[item][7][6] != '{SKIP}') {
					if (doVerify) {
						curData[item][8][6] = curData[item][7][6];
					}else{
						newDate[1] = curData[item][7][6];
					}
					postData[10] = newDate[0]+'='+newDate[1];
				}
				fdLog.debug(' -      POST:' + item + "|" + postData[10]); 
			}else{
				if (postData[10]) { //this may not have been set
					var newDate = postData[10].split('=');
					postData[10] = newDate[0]+'=';
				}
				curData[item][7][6] = '{SKIP}';
			}


			//apply bis date changes if any
			if( $('NBIS_bis_use').checked ) {
				//only if new date is set to change
				var bisDate = ''
					if (postData[11]) { bisDate = postData[11].split('='); }
				//safety
				if (curData[item][7][7] != '{SKIP}') {
					if (doVerify) {
						curData[item][8][7] = curData[item][7][7];
					}else{
						bisDate[1] = curData[item][7][7];
					}
					postData[11] = bisDate[0]+'='+bisDate[1];
				}
				fdLog.debug(' -      POST', item, postData[11]); 
			}else{
				if (postData[11]) { //this may not have been set
					var bisDate = postData[11].split('=');
					postData[11] = bisDate[0]+'=';
				}
				curData[item][7][7] = '{SKIP}';
			}
			//apply default pricing unit description changes if any
			if( $('NBIS_dpud_use').checked ) {
				//only if new text is set to change
				var dpudText = ''
					if (postData[12]) { dpudText = postData[12].split('='); }
				//safety
				if (curData[item][7][8] != '{SKIP}') {
					if (doVerify) {
						curData[item][8][8] = curData[item][7][8];
					}else{
						dpudText[1] = curData[item][7][8];
					}
					postData[12] = dpudText[0]+'='+(dpudText[1]).replace(/\s/gi, "+"); //make text safe for posting
				}
				fdLog.debug(' -      POST', item, postData[12]); 
			}else{
				if (postData[12]) { //this may not have been set
					var dpudText = postData[12].split('=');
					postData[12] = dpudText[0]+'=';
				}
				curData[item][7][8] = '{SKIP}';
			}

			return true;
		}

	/* item parse for material_view.jsp and product_view.jsp */
		function itemParse(item) {

			var z = '';
			z = curData[item][3];

			if (z=='') {
				fdLog.debug('itemParse : Error, nothing to parse...'); 
				return true;
			}

			if (z.indexOf('form action="material_view.jsp"') != -1) {
				//we're parsing material_view.jsp

				//split off useless page data
				z = z.split('<form action="material_view.jsp" method="post">');
				z = z[1].split('</form>');
				z = '<form action="material_view.jsp" method="post">'+z[0]+'</form>';
			}else if(z.indexOf('form action="product_view.jsp"') != -1) {
				//we're parsing product_view.jsp
				/*
					this page has several forms on it, so we'll use them all and
					just keep what we want out of it
				 */
				var temp = '';
				z = z.split('<form action="product_search.jsp" method="post">');
				z = z[1].split('<form action="product_view.jsp" method="post">');
				for (var n=0; n<z.length; n++) {
					if (z[n].indexOf('</form>') != -1) {
						zs = z[n].split('</form>');
						temp += zs[0];
					}
				}
				z = '<form action="product_view.jsp" method="post">'+temp+'</form>';

			}else{
				fdLog.debug('itemParse : Error, unknown text to be parsed...'); 
			}
			
			//set form in page
			$('parse').innerHTML = z;

			//loop through form and get values for item
			var elem = document.forms[0].elements;
			
			//this one is always the same
			postData[0] = 'action=save';

			//rest of the items
			var unknownFound = false;
			
			var postDataTemp = -1;

			for(var i = 0; i < elem.length; i++) {
				postDataTemp = -1;
				if (elem[i].name.indexOf('label_name') != -1) { postDataTemp = 1; }
				if (elem[i].name.indexOf('cust_promo') != -1) { postDataTemp = 2; }
				if (elem[i].name.indexOf('deposit_amount') != -1) { postDataTemp = 3; }
				if (elem[i].name.indexOf('restrictions') != -1) { postDataTemp = 4; }
				if (elem[i].name.indexOf('description') != -1) { postDataTemp = 5; }
				if (elem[i].name.indexOf(':selected') != -1) { postDataTemp = 6; }
				if (elem[i].name.indexOf('taxable') != -1) { postDataTemp = 7; }
				if (elem[i].name.indexOf('advance_order_flag') != -1) { postDataTemp = 8; }
				if (elem[i].name.indexOf('kosher_production') != -1) { postDataTemp = 9; }
				//great naming scheme going on here
				if (elem[i].name.indexOf('::new_prod_date') != -1) { postDataTemp = 10; }
				if (elem[i].name.indexOf('::back_in_stock') != -1) { postDataTemp = 11; }
				if (elem[i].name.indexOf('::pricing_unit_description') != -1) { postDataTemp = 12; }

				if (postDataTemp != -1) { 
					postData[postDataTemp] = elem[i].name + '=' + elem[i].value;	
					fdLog.debug("name=" + elem[i].name + "|value=" + elem[i].value);
				}
				
				if (postDataTemp == -1) {
					//mark as found
					unknownFound = true;
					//log unknown name=value
				}else{
					//log known name=value
				}
				


			}
			
			fdLog.debug('postData '+postData); 
			//postData now contains all previous info from item

			return true;
		}

	/* fetch progress */
		function fetchProgress() {
			//only run if checkStat is running
			if (checkStat) {
				//this spits out a LOT of data

				//update status (this probably won't show if quick)
				while(!updStatus('fetching', 'processing', n, ''));

				for (var n = 0; n < curData.length; n++ ) {
					fdLog.debug("Current status:" +  curData[n][1]);
					if (curData[n][1] == 'queued') {
						fdLog.debug("Processing queued");
						if (SAP2SKU) {
							//skip get status and go right to post status
							curData[n][1] = 'changed';
							//change post data
							curData[n][4] = 'searchterm='+curData[n][0]+'&searchtype=SAPID';
							fdLog.debug('Doing SAP2SKU: '+n  + " | " +  curData[n][0] + " | " + curData[n][4]); 
						}else{
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							if (!shouldGet) { 
								fdLog.debug('Skipping GET of item: '+n); 
								//set doPost to false
								doGet = false;
								//set fake 'get' data
								curData[n][1] = 'changed';
							}
							if (doGet) {
								//fetch
								while(!fetchData(n, curData[n][0]));
								
								$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							}
						}
					}
					if (curData[n][1] == 'fetched' && curData[n][2] == '200') {
					
						fdLog.debug("Processing fetched");
						
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						while(!updStatus('parsing', 'processing', n, ''));
							fdLog.debug('Parsing '+curData[n][0]); 
							$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						
						curData[n][1] = 'parsing';
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						while(!itemParse(n));

						curData[n][1] = 'changing';
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							fdLog.debug('Applying changes to '+curData[n][0]); 
						while(!applyChange(n));
							fdLog.debug('Changes done for '+curData[n][0]); 

						curData[n][4] = postData.join('&');

						//remove extra &s
						while ( (curData[n][4]).indexOf('&&') >= 0 ) {
							curData[n][4] = curData[n][4].replace('&&', '&');
						}

						curData[n][1] = 'changed';
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
					}else{
						//doesn't call here often
						while(!updStatus('fetching ('+queue+' left)', 'processing', '', ''));
					}
					
					if (curData[n][1] == 'changed') {			
						fdLog.debug("Processing changed");
						if (!shouldPost) { 
							fdLog.debug('Skipping POST of item: '+n); 
							//set doPost to false
							doPost = false;
							//set fake 'posted' data, and decrease status queue
							curData[n][1] = '(SKIP) posted';
							curData[n][5] = '200';
							queue--;
						}
						if (doPost) {
							//now post
							urlPost = urlRestPost;
							if ( $('postVerify').checked && !SAP2SKU ) { //SAP2SKU doesn't need verify
								while(!postBack(n, 'verify queued'));
							}else{
								while(!postBack(n));
							}
						}
					}
					//
					if ((curData[n][1] == 'posted' || curData[n][1] == '(SKIP) posted') && curData[n][5] == '200') {
						fdLog.debug("Processing posted");
						$('fetchitem'+n).innerHTML = '<a href="'+urlSapId+curData[n][0]+'" target="_new">'+curData[n][0]+'</a> : '+curData[n][1];
						//add in SAP2SKU text
						if (SAP2SKU) {
							fdLog.debug('SAP2SKU '+SAP2SKU); 
							var SAP2SKUtemp = curData[n][6].split('<!-- search results -->');
							if (SAP2SKUtemp.length == 2) {
									fdLog.debug('split 1 succeed '+SAP2SKUtemp); 
								SAP2SKUtemp = SAP2SKUtemp[1].split('</div>');
								if (SAP2SKUtemp.length >= 2) {
									fdLog.debug('split 2 succeed '+SAP2SKUtemp); 
									//full results message
									//SAP2SKUtemp = SAP2SKUtemp[0];

									//pull out links via a regexp
										SAP2SKUtemp = SAP2SKUtemp[0].match(/\<a href=\"product_view\.jsp\?skuCode=[^\>]*\>([^\<]*)<\/a>/ig);
									//we should now have array of skus
									if (SAP2SKUtemp != null) {
										$('fetchitem'+n).innerHTML += '<br /><table><tr><td>'+curData[n][0]+"</td><td>"+SAP2SKUtemp.join('</td><td>').replace(/\<a href=\"product_view\.jsp\?skuCode=[^\>]*\>/ig, '')+'</tr></table>';
									}
								}else{
									fdLog.debug('split 2 failed '+SAP2SKUtemp.length); 
								}
							}else{
								fdLog.debug('split 1 failed '+SAP2SKUtemp.length); 
							}
						}
							//if (SAP2SKU) { $('fetchitem'+n).innerHTML += '<br />SAP2SKU'; }

						while(!updStatus('posted '+curData[n][0], 'processing', '', ''));
						//this doesn't fire often
						//set status for no posting
						if (!shouldPost) { while(!updStatus('(NOT) posted '+curData[n][0], 'processing', '', '')); }

					}else{
						while(!updStatus('posting ('+queue+' left)', 'processing', '', ''));
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
					}
					
								
				}
				
				if (queue <= 0) {					
					if ( $('postVerify').checked && !SAP2SKU ) {
						//clear status updater
						checkStat = clearInterval(checkStat);

						//reset queue total
						queue = curData.length;

						//verify, setup and startup
						checkVerify = setInterval(verifyProgress, 500);
					}else{
						//no verify, we're done.

						//set timer
						timeEnd = makeTime('end');

						//clear status updater
						checkStat = clearInterval(checkStat);
							var time_run = makeTime('formatted', timeStart, timeEnd);
							fdLog.debug('all changes posted ('+time_run+')'); 
							while(!updStatus('processing done in '+time_run, 'done.', curData.length, ''));
					}
				}
			}
		}

	/* verify progress */
		function verifyProgress() {

			//only run if checkVerify is running
			if (checkVerify) {
				//update status (this probably won't show if quick)
				while(!updStatus('verifying', 'processing', n, ''));

				
				for (var n = 0; n < curData.length; n++ ) {
					fdLog.debug("curData[n][1]:" + curData[n][1]);
					//here we should be able to re-check the post
					if ( curData[n][1] == 'verify queued' ) {
						//reset statusCode
						curData[n][2] = '';
						//set as not verified
						curData[n][9] = false;

						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
						if (!shouldGet) { 
							fdLog.debug('Skipping GET of item: '+n); 
							//set doPost to false
							doGet = false;
							//set fake 'get' data
							curData[n][1] = 'verify fetched';
							curData[n][2] = '200';
						}
						if (doGet) {
							//fetch
							while(!fetchData(n, curData[n][0], 'verify fetched'));
							
							$('fetchitem'+n).innerHTML = curData[n][0]+' 2: '+curData[n][1];
						}
					}
					if (curData[n][1] == 'verify fetched' && curData[n][2] == '200') {
						
						curData[n][1] = 'verify parsing';
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							fdLog.debug('Verify parsing '+curData[n][0]); 
						while(!itemParse(n));

						curData[n][1] = 'verifying changes';
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							fdLog.debug('Verifying changes to '+curData[n][0]); 
						while(!applyChange(n, true)); //apply change as verify
						
						curData[n][1] = 'verification done';
						$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];
							fdLog.debug('Verification done for '+curData[n][0]); 
						
						if (curData[n][7][0] == '{SKIP}' || curData[n][8][0] == '{SKIP}') { curData[n][7][0] = '{SKIP}'; curData[n][8][0] = '{SKIP}'; }
						if (curData[n][7][1] == '{SKIP}' || curData[n][8][1] == '{SKIP}') { curData[n][7][1] = '{SKIP}'; curData[n][8][1] = '{SKIP}'; }
						if (curData[n][7][2] == '{SKIP}' || curData[n][8][2] == '{SKIP}') { curData[n][7][2] = '{SKIP}'; curData[n][8][2] = '{SKIP}'; }
						if (curData[n][7][3] == '{SKIP}' || curData[n][8][3] == '{SKIP}') { curData[n][7][3] = '{SKIP}'; curData[n][8][3] = '{SKIP}'; }
						if (curData[n][7][4] == '{SKIP}' || curData[n][8][4] == '{SKIP}') { curData[n][7][4] = '{SKIP}'; curData[n][8][4] = '{SKIP}'; }
						if (curData[n][7][5] == '{SKIP}' || curData[n][8][5] == '{SKIP}') { curData[n][7][5] = '{SKIP}'; curData[n][8][5] = '{SKIP}'; }
						if (curData[n][7][6] == '{SKIP}' || curData[n][8][6] == '{SKIP}') { curData[n][7][6] = '{SKIP}'; curData[n][8][6] = '{SKIP}'; }
						if (curData[n][7][7] == '{SKIP}' || curData[n][8][7] == '{SKIP}') { curData[n][7][7] = '{SKIP}'; curData[n][8][7] = '{SKIP}'; }
						if (curData[n][7][8] == '{SKIP}' || curData[n][8][8] == '{SKIP}') { curData[n][7][8] = '{SKIP}'; curData[n][8][8] = '{SKIP}'; }

						if (
								(curData[n][8][0] != curData[n][7][0]) || 
								(curData[n][8][1] != curData[n][7][1]) || 
								(curData[n][8][2] != curData[n][7][2]) || 
								(curData[n][8][3] != curData[n][7][3]) || 
								(curData[n][8][4] != curData[n][7][4]) || 
								(curData[n][8][5] != curData[n][7][5]) ||
								(curData[n][8][6] != curData[n][7][6]) ||
								(curData[n][8][7] != curData[n][7][7]) ||
								(curData[n][8][8] != curData[n][7][8])
							) {
							
								fdLog.debug('Mismatch '+
									curData[n][8][0]+'!='+curData[n][7][0], 
									curData[n][8][1]+'!='+curData[n][7][1], 
									curData[n][8][2]+'!='+curData[n][7][2], 
									curData[n][8][3]+'!='+curData[n][7][3], 
									curData[n][8][4]+'!='+curData[n][7][4], 
									curData[n][8][5]+'!='+curData[n][7][5], 
									curData[n][8][6]+'!='+curData[n][7][6], 
									curData[n][8][7]+'!='+curData[n][7][7], 
									curData[n][8][8]+'!='+curData[n][7][8]
								); 

							curData[n][1] = 'mismatch!';
							
							//create mismatch message here
								var mismatch_message = '';
								if ((curData[n][8][0] != curData[n][7][0]) && curData[n][8][0] != '{SKIP}' ) { mismatch_message += '<br /><span class="err">web description</span>'; }
								if (curData[n][8][1] != curData[n][7][1]) { mismatch_message += '<br /><span class="err">restriction</span>'; }
								if (curData[n][8][2] != curData[n][7][2]) { mismatch_message += '<br /><span class="err">restriction verb</span>'; }
								if (curData[n][8][3] != curData[n][7][3]) { mismatch_message += '<br /><span class="err">advance order</span>'; }
								if (curData[n][8][4] != curData[n][7][4]) { mismatch_message += '<br /><span class="err">label</span>'; }
								if (curData[n][8][5] != curData[n][7][5]) { mismatch_message += '<br /><span class="err">deposit</span>'; }
								if (curData[n][8][6] != curData[n][7][6]) { mismatch_message += '<br /><span class="err">newness date</span>'; }
								if (curData[n][8][7] != curData[n][7][7]) { mismatch_message += '<br /><span class="err">back in stock date</span>'; }
								if (curData[n][8][8] != curData[n][7][8]) { mismatch_message += '<br /><span class="err">default pricing unit description</span>'; }

							$('fetchitem'+n).innerHTML = '<A href="'+urlSapId+curData[n][0]+'" target="_new">'+curData[n][0]+'</a> : '+curData[n][1]+mismatch_message;
						}else{
							curData[n][1] = 'verified';
							
								fdLog.debug('Verified ');
								fdLog.debug('   '+curData[n][7]);
								fdLog.debug('   '+curData[n][8]);
							$('fetchitem'+n).innerHTML = '<A href="'+urlSapId+curData[n][0]+'" target="_new">'+curData[n][0]+'</a> : '+curData[n][1];
						}
						//$('fetchitem'+n).innerHTML = curData[n][0]+' : '+curData[n][1];

					}else{
						while(!updStatus('verifying ('+queue+' left)', 'verifying', '', ''));
					}
					//catch leftovers
					if ( (curData[n][1] == 'verified' || curData[n][1] == 'mismatch!') && !curData[n][9]) {
						curData[n][9] = true;
						//$('fetchitem'+n).innerHTML = '<a href="'+urlSapId+curData[n][0]+'" target="_new">'+curData[n][0]+'</a> : '+curData[n][1];
						queue--;
					}
					
				}
				if (queue <= 0) {
					//set timer
					timeEnd = makeTime('end');

					//clear status updater
					checkVerify = clearInterval(checkVerify);

					var time_run = makeTime('formatted', timeStart, timeEnd);
					fdLog.debug('all changes verified ('+time_run+')'); 
					while(!updStatus('proc & verify done in '+time_run, 'done.', curData.length, ''));
				}
			}
		}

	/* Perform an action based on the callee button id */
		function perform_action() {
			switch (this.id) {
				case 'rest_act_dupecheck':
					while(!actionDupeCheck()) {}
					break;
				case 'rest_act_count':
					while(!actionCountIds()) {}
					break;
				case 'rest_act_runSAP2SKU':
					//setup for sku lookup
						//turn on sku lookup
							SAP2SKU = true;
						//flip to other post URL
							urlPost = urlSAP2SKU;
					//re-call action now that it's setup
						$('rest_act_run').click();
					break;
				case 'rest_act_run':
					while(!actionRest()) {}
					break;
				case 'wc_act_run':
					while(!actionWC()) {}
					break;
				case 'CAO_act_run':
					while(!actionCAO()) {}
					break;
				case 'NBIS_act_run':
					while(!actionNBIS()) {}
					break;
				case 'KOS_act_run':
					while(!actionKOS()) {}
					break;
			}
		}
		
		function perform_action_f1(e) {
			fdLog.debug("1111111111###########################" + e.srcElement.id);
			switch (e.srcElement.id) {
				case 'rest_act_dupecheck':
					while(!actionDupeCheck()) {}
					break;
				case 'rest_act_count':
					while(!actionCountIds()) {}
					break;
				case 'rest_act_runSAP2SKU':
					//setup for sku lookup
						//turn on sku lookup
							SAP2SKU = true;
						//flip to other post URL
							urlPost = urlSAP2SKU;
					//re-call action now that it's setup
						$('rest_act_run').click();
					break;
				case 'rest_act_run':
					while(!actionRest()) {}
					break;
				case 'wc_act_run':
					while(!actionWC()) {}
					break;
				case 'CAO_act_run':
					while(!actionCAO()) {}
					break;
				case 'NBIS_act_run':
					while(!actionNBIS()) {}
					break;
				case 'KOS_act_run':
					while(!actionKOS()) {}
					break;
			}
		}

	/*	post page (uses generic urlPost variable)
	 *
	 *	item [INT] : item in curData to apply to
	 *	optStatus [STR] : optional status to set (defaults to 'posted')
	 *
	 *	!set urlPost before calling function
	 */
		function postBack(item, optStatus) {
			var postedStatus = optStatus || 'posted';
			if (doPost) {
				doPost = false;
				fdLog.debug('posting '+curData[item][0]); 
				fdLog.debug('curData[item][0] '+curData[item][0]+' : '+curData[item][1]); 
				$('fetchitem'+item).innerHTML = curData[item][0]+' : '+curData[item][1];
				
				curData[item][1] = 'posting';
					fdLog.debug("Ajax call for7:"+ urlPost+"?" + curData[item][4]);
					
					new Ajax.Request(urlPost, {
						method: 'POST',			
						parameters: encodeURI(curData[item][4]),
						onComplete: function(retdata) {
								curData[item][1] = postedStatus;
								curData[item][5] = retdata.status;
								curData[item][6] = retdata.responseText;
								fdLog.debug('posted '+curData[item][0]); 
								$('fetchitem'+item).innerHTML = curData[item][0]+' : '+curData[item][1];
								doPost = true;

								queue--;
							},
						onFailure: function(){ fdLog.debug('Something went wrong...') }
					});
					
				//wait after to avoid a delay before first post
				//also, check if we have more in the queue to delay FOR
				var postDelay = $('postDelay').value;
				if (postDelay > 0 && queue >= (item+1)) {
					fdLog.debug('posting... waiting ('+postDelay+' MS)'); 
					fdLog.debug('item '+item); 
					fdLog.debug('queue '+queue); 

					//pausecomp(postDelay);
				}else{
					fdLog.debug('posting... skipping wait'); 
					//pausecomp(postDelay);
				}

				return true;
			}
		}

	/*	update status line
	 *
	 *	set each field in status line. passes strings along, so if a var is
	 *	not passed, it won't be updated.
	 */
		function updStatus(note, verb, current, count) {
			//setup defaults so all arguments are optional
			if (!note) { }else{ $('status_note').innerHTML = note; }
			if (!verb) { }else{ $('status_verb').innerHTML = verb; }
			if (!current) { }else{ $('status_current').innerHTML = current; }
			if (!count) { }else{ $('status_count').innerHTML = count+''; }

			return true;
		}

	/* timing generator */
		function makeTime(type, timeStart, timeEnd) {
			var time = new Date();
			var timeS = timeStart || time.getTime();
			var timeE = timeEnd || time.getTime();
			var timeTemp;

			switch (type) {
				case 'start':
					return time.getTime();
					break;
				case 'end':
					return time.getTime();
					break;
				case 'formatted':
					timeTemp = ( (timeEnd-timeStart) / 1000 );
						//return mins instead
						if (timeTemp > 60) {
							var mins = 0;
							while (timeTemp.toFixed(2) > 60) {
								mins++;
								timeTemp = timeTemp - 60;
								//timeTemp = mins;
							}
							timeTemp = Math.ceil(timeTemp);
							if (timeTemp<10) {
								timeTemp = '0'+timeTemp;
							}
							timeTemp = mins+':'+timeTemp + ' mins';
						}else{
							timeTemp = timeTemp.toPrecision(2) + ' secs';
						}
					return timeTemp;
					break;
				default :
					return time;
			}
		}

	/* perform a dupe check */
		function actionDupeCheck() {
			//if no ids are set, break
			if ( $('rests_ids').value == '' ) {
					while(!updStatus('No action selected/No IDs to act on', 'error', '', ''));
				fdLog.debug('DUPE CHECK: No action selected/No IDs to act on'); 
				return true;
			}
			//just dupe check//temp array for dupecheck
			var tempIdsArr = new Array();
			var dupeArr = new Array();
			//temp var for dupe count
			var dupes = 0;
			
			//start processing id
			while(!updStatus('checking for dupe IDs', 'checking', '', ''));

			//get id list and split into an array for use
			//remove extra newline
			var cont = $('rests_ids').value.replace(/\n*$/, '');
			var tempIdsArr = cont.split('\n');

			//loop through temp array, and add to curData
			if ( isArray(tempIdsArr) ) {
				var curLine = 0;
				for (var n=0; n < tempIdsArr.length; n++ ) {
					//if item is not already defined
					if (!dupeArr.inArray(tempIdsArr[n])) {
						//add it
						
						dupeArr.push(tempIdsArr[n]);
						curData.push(curLine);
						curLine++; 
					}else{
						//skip
						fdLog.debug('Skipped item '+curLine+' : '+tempIdsArr[n]+' (dupe)'); 
						while(!updStatus('skipping dupe '+curData[curLine], 'checking', '', ''));
						dupes++;
					}
				}
			}

			fdLog.debug('Dupe Count: '+dupes+', '+curData.length); 
			
			/* if checking an empty set, don't count it */
			//if (curData[0][0].length == 0) { curData = 0; }
			
			while(!updStatus('dupe check done ('+dupes+' skipped)', 'done.', '', curData.length, ''));

			return true;				
		}

	/* generate textarea source */
		function genTASource(id) {
			return '<textarea id="'+id+'" class="dataentry idlist" onkeydown="insertTab(this.id, event);"></textarea>';
		}

/* -- SHARED functions -- */

	function batch_update() {
		/* -- SETUP -- */
		fdLog.debug('Running Erpsy Batch Update v '+version); 
		fdLog.debug('----------------------------------------'); 
		fdLog.debug('Starting setup...'); 
	
		//call GM menu
		while(!addGMMenu());

		fdLog.debug('  Using location: '+urlMain); 
	
		//add script to page load
		if (window.addEventListener)
			window.addEventListener("load", function() { erpsy_addon(); }, false);
		else
			window.onload=erpsy_addon; 
	}

	//setup all necessary container elements for user interface
	function erpsy_addon() {
	
		var nAgent = navigator.userAgent;
		fdLog.debug("Browser Agent: " + nAgent);
		
		if(nAgent.indexOf("MSIE")!=-1)
			browser = "IE";
		else
			browser = "FF";

		fdLog.debug('  Killing calendar...');
		//wipes unused calendar script/div from page. 
		checkKillCal = setInterval(init, 500);
		//add prototype.js to page
		fdLog.debug("  Adding protoype to page...");
		var headID = document.getElementsByTagName("head")[0];         
		var newScript = document.createElement('script');
		newScript.type = 'text/javascript';
		newScript.src = 'http://www.freshdirect.com/assets/javascript/prototype.js';
		headID.appendChild(newScript);
		
		fdLog.debug('  Adding elements to page...'); 

		//spacer
		$('content').innerHTML += '<br /><br />';

		/* - parse div holder - */

			//add a parse div to hold form later
				while(!insEle('main', 'div', 'I', 'parse')) {}

		/* - parse div holder - */

		/* overlay div holder */

			while(!insEle('main', 'div', 'I', 'overlay')) {}
				$('overlay').className = 'overlay';
					$('overlay').style.display = 'none';
							
		/* overlay div holder */

		//add container for new items
		while(!insEle('content', 'div', 'I', 'container')) {}

		//call individual functions to add content (cleaner)
		/* - title row - */
		while(!setupTitleRow()) {}
		/* - title row - */

		/* - user container - */
		while(!insEle('container', 'div', 'I', 'user')) {}
		/* - restrictions container - */
		while(!setupRest()) {}
		/* - webcopy container - */
		while(!setupWebcopy()) {}
		/* - kosher container - */
		while(!setupKOS()) {}
		/* - CAO container - */
		while(!setupCAO()) {}
		/* - NBIS container - */
		while(!setupNBIS()) {}
		/* - add buttons - */
		while(!setupButtons()) {}
		
		/* - user container - */

		/* - activity container - */

		while(!setupActivity()) {}

		/* - activity container - */
		
		fdLog.debug('  Adding event listeners...'); 

		/* -- events -- */
		
		while(!setupEvents()) {}

		/* -- events -- */

		
		fdLog.debug('...setup done'); 		

	}

	function setupTitleRow() {
		/* - title row - */
			while(!insEle('container', 'div', 'I', 'title')) {}
			/* title row text and class */
				$('title').innerHTML += 'Batch Update v'+version;
				$('title').className = 'sec_title';
			while(!insEle('title', 'div', 'I', 'changeLogContainer')) {}
			/* title row text and class */
				$('changeLogContainer').innerHTML += '<a href="#" id="changeLogLink" onclick="return false;">Change Log</a>';
				$('changeLogContainer').className = 'sec_title_options';
				$('changeLogContainer').style.width = '90px';
			while(!insEle('title', 'div', 'I', 'optionsContainer')) {}
			/* title row text and class */
				$('optionsContainer').innerHTML += 'Options';
				$('optionsContainer').className = 'sec_title_options';
		/* - title row - */

		return true;
	}

	function setupButtons() {
		fdLog.debug("Setting up buttons");
		for (var n=0; n<buttonArr.length; n++) {
			//loop through list of buttons
			for (var o=0; o<buttonArr.length; o++) {
				//make sure we're not on the same button
				if (buttonArr[o][0] != buttonArr[n][0]) {
					//add button to current button's bar
					if(!$(buttonArr[n][0]+'_title')) {
						fdLog.debug("Err: button parent doesn't exist, adding...", buttonArr[n][0]+'_title'); 
							while(!insEle('user', 'div', 'I',  buttonArr[n][0])) {}
								$( buttonArr[n][0]).style.display = 'none';
							while(!insEle(buttonArr[n][0], 'div', 'I', buttonArr[n][0]+'_title')) {}
								$(buttonArr[n][0]+'_title').innerHTML = '(Auto-generated)';
								$(buttonArr[n][0]+'_title').className = 'sec_title';
						fdLog.debug("... button parent added."); 
					}
					
					$(buttonArr[n][0]+'_title').innerHTML += '<input type="button" onclick="toggleView(this.id);return false;" class="toggler_choice toggler" value="'+buttonArr[o][2]+'" id="'+buttonArr[n][0]+'|'+buttonArr[o][0]+'" />';
				}
			}
		}
		//add copy of buttonArr to window
		//unsafeWindow.buttonArr = buttonArr;

		return true;
	}
	//add toggler funcitonality to buttons
	function toggleView(buttonInfo) {
		fdLog.debug("Executing toggle window");
		//receive buttonInfo in the form of 'FROM_PREFIX|TO_PREFIX'
		var buttonInfo = buttonInfo.split('|');
		//loop through button array, using it to know which view is active
		for (var n=0; n<buttonArr.length; n++) {
			if (buttonArr[n][0] == buttonInfo[0] && buttonArr[n][3]) {
				//currently active, make inactive
				if ($(buttonArr[n][1])) {
					$(buttonArr[n][1]).style.display = 'none';
				}
				buttonArr[n][3] = false;
			}
			if (buttonArr[n][0] == buttonInfo[1] && !buttonArr[n][3]) {
				//currently inactive, make active
				if ($(buttonArr[n][1])) {
					$(buttonArr[n][1]).style.display = '';
				}
				buttonArr[n][3] = true;
			}
		}
	}

	function setupRest() {
		/* - restrictions container - */
			while(!insEle('user', 'div', 'I', 'rest')) {}
			
			/* title row */
				while(!insEle('rest', 'div', 'I', 'rest_title')) {}
					$('rest_title').innerHTML = 'REST';
					$('rest_title').className = 'sec_title';
			/* title row */

			/* - left data container - */
				while(!insEle('rest', 'div', 'I', 'rest_data')) {}
					$('rest_data').className = 'data';
					

				/* top restrictions */
					while(!insEle('rest_data', 'div', 'I', 'rest_data_rests')) {}
							$('rest_data_rests').className = 'field';
						while(!insEle('rest_data_rests', 'div', 'I', 'rest_data_title')) {}
							$('rest_data_title').innerHTML = 'Restrictions';
					//check box
						while(!insEle_f1('rest_data_rests', 'input', 'I', 'rests_use', 'C')) {}
								//$('rests_use').type = 'checkbox';
					//input box
						while(!insEle('rest_data_rests', 'input', 'I', 'rests_names')) {}
							$('rests_names').className = 'dataentry';
					/* left options */
						while(!insEle('rest_data_rests', 'div', 'I', 'rest_choices_opts')) {}
							$('rest_choices_opts').className = 'options';

						/* radio buttons */
							while(!insEle_f1('rest_choices_opts', 'input', 'I', 'rest_type_add', 'C')) {}
								//$('rest_type_add').type = 'radio';
								$('rest_type_add').name = 'rest_type';
								$('rest_choices_opts').innerHTML += 'change';
							while(!insEle_f1('rest_choices_opts', 'input', 'I', 'rest_type_rem', 'R')) {}
								//$('rest_type_rem').type = 'radio';
								$('rest_type_rem').name = 'rest_type';
								$('rest_choices_opts').innerHTML += 'remove';
							while(!insEle_f1('rest_choices_opts', 'input', 'I', 'rest_type_app', 'R')) {}
								//$('rest_type_app').type = 'radio';
								$('rest_type_app').name = 'rest_type';
								$('rest_choices_opts').innerHTML += 'append';
							while(!insEle_f1('rest_choices_opts', 'input', 'I', 'rest_type_clr', 'R')) {}
								//$('rest_type_clr').type = 'radio';
								$('rest_type_clr').name = 'rest_type';
								$('rest_choices_opts').innerHTML += 'clear';
						/* radio buttons */
					/* left options */
					
					/* advance order flag */
						while(!insEle('rest_data', 'div', 'I', 'advOrder_data_descrip')) {}
								$('advOrder_data_descrip').className = 'field';
							while(!insEle('advOrder_data_descrip', 'div', 'I', 'advOrder_title')) {}
								$('advOrder_title').innerHTML = 'Advance Order';
						//check box
							while(!insEle_f1('advOrder_data_descrip', 'input', 'I', 'advOrder_use', 'C')) {}
									//$('advOrder_use').type = 'checkbox';
							
							$('advOrder_data_descrip').innerHTML += '<br style="clear:both;" />';
							
							while(!insEle('advOrder_data_descrip', 'div', 'I', 'advOrder_choices_opts')) {}
								$('advOrder_choices_opts').className = 'options';
							/* radio buttons */
								while(!insEle_f1('advOrder_choices_opts', 'input', 'I', 'advOrder_val_on', 'R')) {}
									//$('advOrder_val_on').type = 'radio';
									$('advOrder_val_on').name = 'advOrder_val';
									$('advOrder_choices_opts').innerHTML += 'true';
								while(!insEle_f1('advOrder_choices_opts', 'input', 'I', 'advOrder_val_off', 'R')) {}
									//$('advOrder_val_off').type = 'radio';
									$('advOrder_val_off').name = 'advOrder_val';
									$('advOrder_choices_opts').innerHTML += 'false';
							/* radio buttons */
					/* advance order flag */
				/* top restrictions */

				
				/* bottom web description */
					while(!insEle('rest_data', 'div', 'I', 'WD_data_descrip')) {}
							$('WD_data_descrip').className = 'field';
						while(!insEle('WD_data_descrip', 'div', 'I', 'WD_data_title')) {}
							$('WD_data_title').innerHTML = 'Web Description';
					//check box
						while(!insEle_f1('WD_data_descrip', 'input', 'I', 'WD_use', 'C')) {}
								//$('WD_use').type = 'checkbox';
					//input box
						while(!insEle('WD_data_descrip', 'input', 'I', 'WD_descrip')) {}
							$('WD_descrip').value = '{DATA}';
							$('WD_descrip').className = 'dataentry';
				/* bottom web description */

				
				/* bottom label name */
					while(!insEle('rest_data', 'div', 'I', 'LN_data_descrip')) {}
							$('LN_data_descrip').className = 'field';
						while(!insEle('LN_data_descrip', 'div', 'I', 'LN_data_title')) {}
							$('LN_data_title').innerHTML = 'Label Name';
					//check box
						while(!insEle_f1('LN_data_descrip', 'input', 'I', 'LN_use', 'C')) {}
								//$('LN_use').type = 'checkbox';
					//input box
						while(!insEle('LN_data_descrip', 'input', 'I', 'LN_descrip')) {}
							$('LN_descrip').value = '{DATA}';
							$('LN_descrip').className = 'dataentry';
				/* bottom label name */
				
				/* bottom deposit amount */
					while(!insEle('rest_data', 'div', 'I', 'DEP_data_descrip')) {}
							$('DEP_data_descrip').className = 'field';
						while(!insEle('DEP_data_descrip', 'div', 'I', 'DEP_data_title')) {}
							$('DEP_data_title').innerHTML = 'Deposit Amount';
					//check box
						while(!insEle_f1('DEP_data_descrip', 'input', 'I', 'DEP_use', 'C')) {}
								//$('DEP_use').type = 'checkbox';
					//input box
						while(!insEle('DEP_data_descrip', 'input', 'I', 'DEP_descrip')) {}
							$('DEP_descrip').value = '{DATA}';
							$('DEP_descrip').className = 'dataentry';
				/* bottom deposit amount  */
				
					/* left bottom descrip */
						while(!insEle('rest_data', 'div', 'I', 'opt_descrip_rest')) {}
							$('opt_descrip_rest').className = 'opt_descrip';
					/* left bottom descrip */

					/* left bottom post delay */
						while(!insEle('rest_data', 'div', 'I', 'opt_additional_options')) {}
							$('opt_additional_options').innerHTML = '<br />Wait <input id="postDelay" value="2000" /> MS between POSTs';
					/* left bottom post delay */

					/* left bottom post verify */
						while(!insEle('rest_data', 'div', 'I', 'opt_additional_options_verifyPosts')) {}
							$('opt_additional_options_verifyPosts').innerHTML = '<input type="checkbox" id="postVerify" checked="true" /> Verify posted data after each POST';
					/* left bottom post verify */
					

					/* button actions */
						while(!insEle('rest_data', 'div', 'I', 'rest_choices_acts')) {}
							$('rest_choices_acts').className = 'actions';
							$('rest_choices_acts').innerHTML = '<hr />';

						/* buttons */
							while(!insEle_f1('rest_choices_acts', 'input', 'I', 'rest_act_dupecheck', 'B')) {}
								//$('rest_act_dupecheck').type = 'button';
								$('rest_act_dupecheck').value = 'Dupe Check';
								$('rest_act_dupecheck').className = 'toggler_choice toggler';
							while(!insEle_f1('rest_choices_acts', 'input', 'I', 'rest_act_count', 'B')) {}
								//$('rest_act_count').type = 'button';
								$('rest_act_count').value = 'count IDs';
								$('rest_act_count').className = 'toggler_choice toggler';
								//$('rest_choices_acts').innerHTML += '<br />';
							while(!insEle_f1('rest_choices_acts', 'input', 'I', 'rest_act_run', 'B')) {}
								//$('rest_act_run').type = 'button';
								$('rest_act_run').value = 'RUN';
								$('rest_act_run').className = 'toggler_choice toggler';
							while(!insEle_f1('rest_choices_acts', 'input', 'I', 'rest_act_runSAP2SKU', 'B')) {}
								//$('rest_act_runSAP2SKU').type = 'button';
								$('rest_act_runSAP2SKU').value = 'SAP -> SKU';
								$('rest_act_runSAP2SKU').className = 'toggler_choice toggler';
								//$('rest_choices_acts').innerHTML += '<br /><br />';
						/* buttons */
					/* button actions */


			/* - left data container - */

			/* - right choices container - */
				while(!insEle('rest', 'div', 'I', 'rest_choices')) {}
					$('rest_choices').className = 'choices';


				/* bottom ids */
					while(!insEle('rest_choices', 'div', 'I', 'rests_data_ids')) {}
						$('rests_data_ids').innerHTML = '<div class="idTitle">SAP IDs:</div>';
						$('rests_data_ids').className = 'field';
					
					//textarea
						$('rests_data_ids').innerHTML += genTASource('rests_ids');

				/* bottom ids */

			/* - right choices container - */
		/* - restrictions container - */

		return true;
	}

	function setupWebcopy() {
		/* - webcopy container - */
			while(!insEle('user', 'div', 'I', 'webcopy')) {}
				$('webcopy').style.display = 'none';

			/* title row */
				while(!insEle('webcopy', 'div', 'I', 'wc_title')) {}
					$('wc_title').innerHTML = 'Web Copy';
					$('wc_title').className = 'sec_title';
			/* title row */

			/* - right choices container - */

				/* bottom ids */
					while(!insEle('webcopy', 'div', 'I', 'wc_data_ids')) {}
						$('wc_data_ids').innerHTML = '<div class="idTitle">Base SKU, Target SKU:</div>';
						$('wc_data_ids').className = 'field';
					
					//textarea
						$('wc_data_ids').innerHTML += genTASource('wc_ids');
				/* bottom ids */
					

					/* right actions */
						while(!insEle('webcopy', 'div', 'I', 'wc_choices_acts')) {}
							$('wc_choices_acts').className = 'actions';

						/* buttons */
							while(!insEle_f1('wc_choices_acts', 'input', 'I', 'wc_act_run', 'B')) {}
								//$('wc_act_run').type = 'button';
								$('wc_act_run').value = 'RUN';
								$('wc_act_run').className = 'toggler_choice toggler';
						/* buttons */
					/* right actions */

			/* - right choices container - */

		/* - webcopy container - */

		return true;
	}

	function setupCAO() {
		/* - CAO container - */
			while(!insEle('user', 'div', 'I', 'CAO')) {}
				$('CAO').style.display = 'none';

			/* title row */
				while(!insEle('CAO', 'div', 'I', 'CAO_title')) {}
					$('CAO_title').innerHTML = 'CAO';
					$('CAO_title').className = 'sec_title';
			/* title row */

			/* - left data container - */
				while(!insEle('CAO', 'div', 'I', 'CAO_data_cont')) {}
					$('CAO_data_cont').className = 'data';

					while(!insEle('CAO_data_cont', 'div', 'I', 'CAO_data')) {}
						$('CAO_data').innerHTML = '';
						if (CAO_content[1] == "") {
							$('CAO_data').innerHTML += 'fetching current CAO<span id="CAO_fetching">...</span>';
							checkCAO = window.setInterval(caoSetupProgress, 500);
						}else{
							$('CAO_data').innerHTML = CAO_content[1];
						}
						
					/* button actions */
						while(!insEle('CAO_data_cont', 'div', 'I', 'CAO_choices_acts')) {}
							$('CAO_choices_acts').className = 'actions w100per';
							$('CAO_choices_acts').innerHTML = '<hr />';
							while(!insEle_f1('CAO_choices_acts', 'input', 'I', 'CAO_act_run', 'B')) {}
								//$('CAO_act_run').type = 'button';
								$('CAO_act_run').value = 'RUN';
								$('CAO_act_run').className = 'toggler_choice toggler';
					/* button actions */

			/* - right choices container - */
				while(!insEle('CAO', 'div', 'I', 'CAO_choices')) {}
					$('CAO_choices').className = 'choices';

				/* bottom ids */
					while(!insEle('CAO_choices', 'div', 'I', 'CAO_data_ids')) {}
						$('CAO_data_ids').innerHTML = '<div class="idTitle">WEB IDs:</div>';
						$('CAO_data_ids').className = 'field';
					
					//textarea
						$('CAO_choices').innerHTML += genTASource('CAO_ids');
				/* bottom ids */

			/* - right choices container - */

		/* - CAO container - */

		return true;
	}
	
	function setupKOS() {
		/* - KOS container - */
			while(!insEle('user', 'div', 'I', 'KOS')) {}
				$('KOS').style.display = 'none';

			/* title row */
				while(!insEle('KOS', 'div', 'I', 'KOS_title')) {}
					$('KOS_title').innerHTML = 'KOS';
					$('KOS_title').className = 'sec_title';
			/* title row */

			/* - left data container - */
				while(!insEle('KOS', 'div', 'I', 'KOS_data_cont')) {}
					$('KOS_data_cont').className = 'data';

					while(!insEle('KOS_data_cont', 'div', 'I', 'KOS_data')) {}
						$('KOS_data').innerHTML = '';
						if (KOS_content[1] == "") {
							$('KOS_data').innerHTML += 'fetching current KOS<span id="KOS_fetching">...</span>';
							checkKOS = setInterval(KOSSetupProgress, 500);
						}else{
							$('KOS_data').innerHTML = KOS_content[1];
						}
					/* button actions */
						while(!insEle('KOS_data_cont', 'div', 'I', 'KOS_choices_acts')) {}
							$('KOS_choices_acts').className = 'actions w100per';
							$('KOS_choices_acts').innerHTML = '<hr />';
							while(!insEle_f1('KOS_choices_acts', 'input', 'I', 'KOS_act_run', 'B')) {}
								//$('KOS_act_run').type = 'button';
								$('KOS_act_run').value = 'RUN';
								$('KOS_act_run').className = 'toggler_choice toggler';
					/* button actions */

			/* - right choices container - */
				while(!insEle('KOS', 'div', 'I', 'KOS_choices')) {}
					$('KOS_choices').className = 'choices';

				/* bottom ids */
					while(!insEle('KOS_choices', 'div', 'I', 'KOS_data_ids')) {}
						$('KOS_data_ids').innerHTML = '<div class="idTitle">WEB IDs:</div>';
						$('KOS_data_ids').className = 'field';
					
					//textarea
						$('KOS_choices').innerHTML += genTASource('KOS_ids');
				/* bottom ids */

			/* - right choices container - */

		/* - KOS container - */

		return true;
	}

	function setupNBIS() {
	fdLog.debug("Setting NBIS");
		/* - NBIS container - */
			while(!insEle('user', 'div', 'I', 'NBIS')) {}
				$('NBIS').style.display = 'none';

			/* title row */
				while(!insEle('NBIS', 'div', 'I', 'NBIS_title')) {}
					$('NBIS_title').innerHTML = 'New & Back In Stock';
					$('NBIS_title').className = 'sec_title';
			/* title row */

			/* - left data container - */
				while(!insEle('NBIS', 'div', 'I', 'NBIS_data')) {}
					$('NBIS_data').className = 'data';
				
				/* bottom Newness Date Override */
					while(!insEle('NBIS_data', 'div', 'I', 'NBIS_data_new')) {}
							$('NBIS_data_new').className = 'field';
						while(!insEle('NBIS_data_new', 'div', 'I', 'NBIS_data_new_title')) {}
							$('NBIS_data_new_title').className = 'fleft marTop4';
							$('NBIS_data_new_title').innerHTML = 'Newness Date Override';
					//check box
						while(!insEle_f1('NBIS_data_new', 'input', 'I', 'NBIS_new_use', 'C')) {}
							$('NBIS_new_use').className = 'fright';
							//$('NBIS_new_use').type = 'checkbox';
					//input box
						while(!insEle('NBIS_data_new', 'input', 'I', 'NBIS_new_date')) {}
							$('NBIS_new_date').value = '{DATA}';
							$('NBIS_new_date').className = 'dataentry';
				/* bottom Newness Date Override */
				
				/* bottom Back In Stock Date Override */
					while(!insEle('NBIS_data', 'div', 'I', 'NBIS_data_bis')) {}
							$('NBIS_data_bis').className = 'field';
						while(!insEle('NBIS_data_bis', 'div', 'I', 'NBIS_data_bis_title')) {}
							$('NBIS_data_bis_title').className = 'fleft marTop4';
							$('NBIS_data_bis_title').innerHTML = 'Back In Stock Date Override';
					//check box
						while(!insEle_f1('NBIS_data_bis', 'input', 'I', 'NBIS_bis_use', 'C')) {}
							$('NBIS_bis_use').className = 'fright';
							//$('NBIS_bis_use').type = 'checkbox';
					//input box
						while(!insEle('NBIS_data_bis', 'input', 'I', 'NBIS_bis_date')) {}
							$('NBIS_bis_date').value = '{DATA}';
							$('NBIS_bis_date').className = 'dataentry';
				/* bottom Back In Stock Date Override */
				
				/* bottom Default Pricing Unit Description Override */
					while(!insEle('NBIS_data', 'div', 'I', 'NBIS_data_dpud')) {}
							$('NBIS_data_dpud').className = 'field';
						while(!insEle('NBIS_data_dpud', 'div', 'I', 'NBIS_data_dpud_title')) {}
							$('NBIS_data_dpud_title').className = 'fleft marTop4';
							$('NBIS_data_dpud_title').innerHTML = 'Default Pricing Unit Description Override';
					//check box
						while(!insEle_f1('NBIS_data_dpud', 'input', 'I', 'NBIS_dpud_use', 'C')) {}
							$('NBIS_dpud_use').className = 'fright';
							//$('NBIS_dpud_use').type = 'checkbox';
					//input box
						while(!insEle('NBIS_data_dpud', 'input', 'I', 'NBIS_dpud_text')) {}
							$('NBIS_dpud_text').value = '{DATA}';
							$('NBIS_dpud_text').className = 'dataentry';
				/* bottom Back In Stock Date Override */

				/* button actions */
					while(!insEle('NBIS_data', 'div', 'I', 'NBIS_choices_acts')) {}
						$('NBIS_choices_acts').className = 'actions w100per';
						$('NBIS_choices_acts').innerHTML = '<hr />';

					/* buttons */
						while(!insEle_f1('NBIS_choices_acts', 'input', 'I', 'NBIS_act_run', 'B')) {}
							//$('NBIS_act_run').type = 'button';
							$('NBIS_act_run').value = 'RUN';
							$('NBIS_act_run').className = 'toggler_choice toggler';
					/* buttons */
				/* button actions */

			/* - left data container - */

			/* - right choices container - */
				while(!insEle('NBIS', 'div', 'I', 'NBIS_choices')) {}
					$('NBIS_choices').className = 'choices';

				/* bottom ids */
					while(!insEle('NBIS_choices', 'div', 'I', 'NBIS_data_ids')) {}
						$('NBIS_data_ids').innerHTML = '<div class="idTitle">WEB IDs:</div>';
						$('NBIS_data_ids').className = 'field';
					
					//textarea
						$('NBIS_data_ids').innerHTML += genTASource('NBIS_ids');
				/* bottom ids */

			/* - right choices container - */

		/* - NBIS container - */

		return true;
	}

	function setupActivity() {
		/* - activity container - */
		fdLog.debug("Setting Activity");
			while(!insEle('container', 'div', 'I', 'activity')) {}

			/* title row */
				while(!insEle('activity', 'div', 'I', 'activ_title')) {}
					$('activ_title').innerHTML = 'Activity';
					$('activ_title').className = 'sec_title';
			/* title row */

			/* status row */
				while(!insEle('activity', 'div', 'I', 'status')) {}
					while(!insEle('status', 'div', 'I', 'status_note')) {}
					//reverse order since they're float: right
					while(!insEle('status', 'div', 'I', 'status_count')) {}
					while(!insEle('status', 'div', 'I', 'status_of')) {}
					while(!insEle('status', 'div', 'I', 'status_current')) {}
					while(!insEle('status', 'div', 'I', 'status_verb')) {}
					while(!insEle('status', 'div', 'I', 'status_cont')) {}
			/* status row */

		/* - activity container - */

		return true;
	}

	function setupEvents() {
		
		//add radio button descrip text
		if (window.addEventListener) {
			$('rest_type_add').addEventListener('click', rest_type, true);
			$('rest_type_rem').addEventListener('click', rest_type, true);
			$('rest_type_app').addEventListener('click', rest_type, true);
			$('rest_type_clr').addEventListener('click', rest_type, true);
			
			//add button actions
			$('rest_act_dupecheck').addEventListener('click', perform_action, true);
			$('rest_act_count').addEventListener('click', perform_action, true);
			$('rest_act_run').addEventListener('click', perform_action, true);
			$('rest_act_runSAP2SKU').addEventListener('click', perform_action, true);
			$('wc_act_run').addEventListener('click', perform_action, true);
			$('CAO_act_run').addEventListener('click', perform_action, true);
			$('NBIS_act_run').addEventListener('click', perform_action, true);
			$('KOS_act_run').addEventListener('click', perform_action, true);
			
			//date verify
			$('NBIS_new_date').addEventListener('blur', function() { date_verify(this.id); }, true);
			$('NBIS_bis_date').addEventListener('blur', function() { date_verify(this.id); }, true);

			//overlay
			$('overlay').addEventListener('blur', overlayHide, true);
			
			//add link events
			$('changeLogLink').addEventListener('click', showChangeLog, true);
		}
		else {
			$('rest_type_add').attachEvent('onclick', rest_type_f1);
			$('rest_type_rem').attachEvent('onclick', rest_type_f1);
			$('rest_type_app').attachEvent('onclick', rest_type_f1);
			$('rest_type_clr').attachEvent('onclick', rest_type_f1);
			
			//add button actions
			$('rest_act_dupecheck').attachEvent('onclick', perform_action_f1);
			$('rest_act_count').attachEvent('onclick', perform_action_f1);
			$('rest_act_run').attachEvent('onclick', perform_action_f1);
			$('rest_act_runSAP2SKU').attachEvent('onclick', perform_action_f1);
			$('wc_act_run').attachEvent('onclick', perform_action_f1);
			$('CAO_act_run').attachEvent('onclick', perform_action_f1);
			$('NBIS_act_run').attachEvent('onclick', perform_action_f1);
			$('KOS_act_run').attachEvent('onclick', perform_action_f1);
			
			//date verify
			$('NBIS_new_date').attachEvent('onblur', date_verify_f1);
			$('NBIS_bis_date').attachEvent('onblur', date_verify_f1);

			//overlay
			$('overlay').attachEvent('onblur', overlayHide);
			
			//add link events
			$('changeLogLink').attachEvent('onclick', showChangeLog);
		}
		/* -- defaults -- */
			//set add radio by default
			$('rest_type_add').click();
		/* -- defaults -- */

		return true;
	}
	
	function overlayHide() { $('overlay').style.display = 'none'; }
	
/* -- SETUP -- */

function showChangeLog() {
	$('overlay').innerHTML='<pre>'+changeLog+'</pre>';
	$('overlay').toggle();
	$('overlay').focus();
}


/* objects */