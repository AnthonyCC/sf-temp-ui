function fdTSDisplay(refIdArg) {
	//check for required data
	if (!window.refData && !window.refAdvData) {
		return {}; //no data, no display
	}


	// refId
	this.refId = refIdArg;	//STRING

	this.opts = {
		clickOnly: true, //sets click only functionality
		dayVar: '%%D%%', //idTemplates var
		timeVar: '%%T%%', //idTemplates var
		indexVar: '%%I%%', //idTemplates var
		timeSlotInfo: false, //global variable setting if on timeslot info page
		topLevelElemId: 'tsContainer', //top-level HTML elem. effect children under this
		debug: false, //global debug controller
		cleaner: null, //cleanup function
		cleanerWaitTime: 2000, //ms to wait after expand before attempting clean
		timer_StartTime: -1, //timer function holder
		timer_LastTime: -1, //timer function holder
		timer_EndTime: -1, //timer function holder
		reorgRowLimit: 3, //sequential days required for a reorg
		emptyDayAMRowTotal: 4, //make empty day have this many rows in AM section
		emptyDayPMRowTotal: 4, //make empty day have this many rows in PM section
		emptyDayNewDayPart: 4, //day part for an empty day defaults to this
		IEver: -1, //IE version for calc (-1 = not IE)
		preExtend: true, //use pre-extend (IE<=7 is autoset to false)
		noDeliveryCId: 'NDdayC', //container ID for no delivery (contracted view)
		noDeliveryEId: 'NDdayE', //container ID for no delivery (expanded view)
		rowHeight: 32, //height of a ts row, the TD (for calcdHeight)
		cutoffHeight: 28, //height of cutoff (for calcdHeight)
		emptyCellHtml: '<div class="tsContent"><div id="ts_d%%D%%_ts%%T%%_rbCont" class="fleft ts_rb">&nbsp;</div><div id="ts_d%%D%%_ts%%T%%_time" class="fleft tsCont ">&nbsp;</div></div>', //HTML for an empty cell
		mouseOverSetTimeout: null, //timeout function holder
		mouseOverTimeLimit: 800, //ms for mmouse over before expand animation starts
		expandDuration: 200, //ms for expand animation
		appearDuration: 200, //ms for appear animation
		contractDuration: 200, //ms for contract animation
		expandedDayId: null, //holds the currently expanded day's id
		hC_beforeContractWidth: '221px', //an expanded ts is set tp this width before contract animation
		beforeExpandWidth: '107px', //hidden content is set to this width before expand animation
		cssRefStyleAttributes: ['width', 'border-right'], //array holding attributes to use in cssRefString
		cssRefString: {}, //object that holds cssString refs (refId.cssString)
		radioCheckedCur: null, //currently checked radio  (not checked -> cur ?(OnlyRow) -> last)
		radioCheckedLast: null, //last checked radio (for re check on ao hide)
		radioCheckedLastUndo: null, //last checked radio to undo color for
		negSubt: (0-76-14-4-60-15), //assume a negative subtraction of hExt-fExt-daypart-two cutoffs-margin for row height
		showPremiumSlots: false, //reorg based on prem.slots
		premSlotsDayId: 'ts_d0_tsTable', //dayId where prem.slots is shown
		premSlotsCO: null, //date object initialized with timestamp ref for cutoff timing (UTC ms)
		premSlotsTimerElem: null, //html elem where timer is displayed as child (pre extended)
		premSlotsTimerElem_timer: 'premSlotsTimer', //html elem id for timer
		premSlotsTimerElem_msg: 'premSlotsTimerMsg', //html elem id for timer msg
		premSlotsTimerElemClass: 'premSlotTimer', //timer elems have this className
		premSlotsClock: null, //timer interval storage
		showDpTc: false, //show DP T & C
		premSlotsDpTcElem: 'premDpTc', //the container for the dp t&c
		premDcTpAgreed: false, //has user agreed to dctp
		intializeEventFuncs: this.refId+'InitializeFuncs', //check for, and run if found, function on an initialize (passes fdTSDisplay obj in as argsObj.thisObj)
		tsSpecialMsgs: {} //special messaging object (taken from window in construct, set in addEvents)
	};

	this.extendQueue = {};

	//id templates, these are always the html element refs as well
		this.idTemplates = {
			//row (uses index, must be passed explicitly)
			'rowId': 'timeslots_grid'+this.opts.indexVar,
			//day table
			'dayId': 'ts_d'+this.opts.dayVar+'_tsTable',
			//timeslot items
			'tsId': 'ts_d'+this.opts.dayVar+'_ts'+this.opts.timeVar,
			'tsPId': 'ts_d'+this.opts.dayVar+'_ts'+this.opts.timeVar+'_tr',
			'tsRadio': 'ts_d'+this.opts.dayVar+'_ts'+this.opts.timeVar+'_rb',
			'tsRadioCont': 'ts_d'+this.opts.dayVar+'_ts'+this.opts.timeVar+'_rbCont',
			'tsTime': 'ts_d'+this.opts.dayVar+'_ts'+this.opts.timeVar+'_time',
			//cutoff items
			'coId': 'co_d'+this.opts.dayVar+'_ts'+this.opts.timeVar,
			'coPId': 'co_d'+this.opts.dayVar+'_ts'+this.opts.timeVar+'_tr',
			//dayPart items
			'dayPartId': 'daypart_d'+this.opts.dayVar,
			'dayPartPId': 'daypart_d'+this.opts.dayVar+'_tr',
			//headers/footer
			'dayHeaderE': 'ts_d'+this.opts.dayVar+'_hE',
			'dayHeaderC': 'ts_d'+this.opts.dayVar+'_hC',
			'dayFooterE': 'ts_d'+this.opts.dayVar+'_fE'
		};

	//associative array of dayID = dayObj
		this.dayObjs = {};
	//associative array of slotId = slotObj
		this.slotObjs = {};
	//associative array of cutoffId = cutoffObj
		this.cutoffObjs = {};
	//object containing all rows
		this.rowObjs = {};

	/* construct display */
		this.construct = function() {
			//get timeslot_info var
			this.setTimeSlotInfo();
			//set prem.slots global
			this.setShowPremiumSlots();
			//set DP T & C global
			this.setShowDpTc();

			if (this.opts.IEver !== -1 && this.opts.IEver <= 7) {
				/* turn off extending objects as we hit them
				 *	this is an unfortunate fix to make up for IE's poor DOM speed
				 */
				this.opts.preExtend = false;
				this.log('Pre-extending elements is OFF');
			}
			
			var rowId = '';

			if (window.refData) {
				rowId = this.getID('rowId');

				//uses DOM extends
				this.getReferenceData(refData, null, null, rowId, daypartIndex);
				
				if (this.rowObjs[rowId]) {
					this.getReorgData(rowId);

					//set keyIds and then set cssRefString
					this.rowObjs[rowId].eRefId = rowId+'_ERef';
					this.rowObjs[rowId].cRefId = rowId+'_CRef';
					this.setCssRefString(this.rowObjs[rowId].eRefId);
					this.setCssRefString(this.rowObjs[rowId].cRefId);
					
					//check extend queue
					this.checkQueue(rowId);

					//set row calcd height
					if (this.rowObjs[rowId].dayIds[0] && this.dayObjs[this.rowObjs[rowId].dayIds[0]]) {
						//if (this.opts.showPremiumSlots) { //add one row height to compensate for prem.slots
						//	this.rowObjs[rowId].ext.style.height = this.getCalcdRowHeight(this.dayObjs[this.rowObjs[rowId].dayIds[0]].TSs.length+1, null, false, this.opts.negSubt);
						//} else {
							this.rowObjs[rowId].ext.style.height = this.getCalcdRowHeight(this.dayObjs[this.rowObjs[rowId].dayIds[0]].TSs.length, null, false, this.opts.negSubt);
						//}
					}
				}
			}
			if (window.refAdvData) {
				rowId = this.getID('rowId', null, null, 'Adv');

				//uses DOM extends
				this.getReferenceData(refAdvData, 10, 19, rowId, daypartAdvIndex);

				if (this.rowObjs[rowId]) {
					this.getReorgData(rowId);

					//set keyIds and then set cssRefString
					this.rowObjs[rowId].eRefId = rowId+'_ERef';
					this.rowObjs[rowId].cRefId = rowId+'_CRef';
					this.setCssRefString(this.rowObjs[rowId].eRefId);
					this.setCssRefString(this.rowObjs[rowId].cRefId);

					//check extend queue
					this.checkQueue(rowId);

					//set row calcd height
					if (this.rowObjs[rowId].dayIds[0] && this.dayObjs[this.rowObjs[rowId].dayIds[0]]) {
						this.rowObjs[rowId].ext.style.height = this.getCalcdRowHeight(this.dayObjs[this.rowObjs[rowId].dayIds[0]].TSs.length, null, false, this.opts.negSubt);
					}
				}
				
				rowId = this.getID('rowId', null, null, 'AdvNew');
				
				//uses DOM extends
				this.getReferenceData(refAdvData, 20, null, rowId, daypartAdvNewIndex);

				if (this.rowObjs[rowId]) {
					this.getReorgData(rowId);

					//set keyIds and then set cssRefString
					this.rowObjs[rowId].eRefId = rowId+'_ERef';
					this.rowObjs[rowId].cRefId = rowId+'_CRef';
					this.setCssRefString(this.rowObjs[rowId].eRefId);
					this.setCssRefString(this.rowObjs[rowId].cRefId);

					//check extend queue
					this.checkQueue(rowId);
					
					//set row calcd height
					if (this.rowObjs[rowId].dayIds[0] && this.dayObjs[this.rowObjs[rowId].dayIds[0]]) {
						this.rowObjs[rowId].ext.style.height = this.getCalcdRowHeight(this.dayObjs[this.rowObjs[rowId].dayIds[0]].TSs.length, null, false, this.opts.negSubt);
					}
				}
			}

			if (this.opts.showPremiumSlots && this.dayObjs[this.opts.premSlotsDayId] && this.dayObjs[this.opts.premSlotsDayId].showPremium) {
				var lastCO = this.dayObjs[this.opts.premSlotsDayId].lastCO;
				this.checkQueue(lastCO);
				lastCO = $(lastCO);
				
				//check for prem.slots info
				if (lastCO) {
					this.log('Timer CO:'+lastCO.innerHTML);
					//hide existing CO display info
					/* lastCO.childElements().each(function(e) { e.hide(); }); */
					
					//add t&c div for hover display (put this first, so it covers the timer)
					if (this.opts.showDpTc && $('PREDPTCdayE')) { //PREDPTCdayE div is in the page
						var firstPmTs = $(this.dayObjs[this.rowObjs[rowId].dayIds[0]].TSIds[this.dayObjs[this.rowObjs[rowId].dayIds[0]].dayPart]);
						var realTs = 0;
						for (var i=this.dayObjs[this.rowObjs[rowId].dayIds[0]].dayPart; i < this.dayObjs[this.rowObjs[rowId].dayIds[0]].TSs.length; i++) {
							if (this.dayObjs[this.rowObjs[rowId].dayIds[0]].TSs[i] !== '') { realTs++; }
						}

						var negSub = parseInt(this.getCalcdRowHeight(1, null, false, 0), 10);

						var styleString = 'display: none;';
							//styleString += 'top: '+this.getCalcdRowHeight(1, null, false, negSub)+';';
							styleString += 'height: '+this.getCalcdRowHeight(realTs, null, false, 0)+';';

						firstPmTs.insertBefore( new Element('div', {'id': this.opts.premSlotsDpTcElem, 'class': 'premSlotDpTc', 'style': styleString }), firstPmTs.firstChild );
						
						var dpTcElem = $(this.opts.premSlotsDpTcElem);
						if (dpTcElem) {
							
							//add the content
							dpTcElem.innerHTML = $('PREDPTCdayE').innerHTML;
							//add mouse out to terms block
							this.addEvent('mouseOvers', dpTcElem);

							//add mouseover to CO
							//this.addEvent('mouseOversClickOnly', lastCO.id, 'coId');
						}
					}

					//add timer elem
					this.opts.premSlotsTimerElem = lastCO.appendChild( new Element('div', {'id': this.opts.premSlotsTimerElem_timer, 'class': 'premSlotTimer'}) );
					//add msg elem
					lastCO.appendChild( new Element('div', {'id':  this.opts.premSlotsTimerElem_msg, 'class': 'premSlotMsg'}) );
					//kick off interval function to show timer
					this.opts.premSlotsClock = setInterval(this.premSlotsTimer, 750);
					//modify height
					//lastCO.style.height = this.getCalcdRowHeight(2, null, false, 1);
				}
			}
;
			if (window['tsSpecialMsgs']) {
				this.opts.tsSpecialMsgs = window.tsSpecialMsgs;
			}
		}

	/* add events to constructed elements */
		this.addEvents = function() {
			for (var slot in this.slotObjs) {
				this.addEvent('mouseClicks', slot, 'tsId');
				if (this.opts.clickOnly) {
					this.addEvent('mouseOversClickOnly', slot, 'tsId');
				}
				
				/* log change click event */
				var timeslotId;
				if (this.slotObjs[slot].radioExt != null) {
					timeslotId = this.slotObjs[slot].radioExt.value;

					this.slotObjs[slot].addCustomEvent({
						event: 'click', 
						func: function(argsObj) { logChange(argsObj.timeslotId); },	
						params: {'timeslotId': timeslotId}
					});
				}
			}
			for (var day in this.dayObjs) {
				//check extend queue
				this.checkQueue(day);
				this.checkQueue(this.dayObjs[day].hC);
				if (!this.opts.clickOnly) { //allow mouseover events
					this.addEvent('mouseOvers', day, 'dayId');
				}
				this.addEvent('mouseOvers', day, 'dayHeaderC');
				this.addEvent('mouseClicks', day, 'dayHeaderC');
			}

			/* add specialMsgs */
			try {
				this.addTsSpecialMsgsEvents(this.opts.tsSpecialMsgs);
			} catch (e) {
				this.log("\tError in addTsSpecialMsgsEvents!");
				this.log("---\n"+e.name + ": " + e.message+"\n---");
			}

			/* check for additional initialized events */
			if (window[this.opts.intializeEventFuncs]) {
				var intializeEventFuncs = window[this.opts.intializeEventFuncs];
				this.log('Found intializeEventFuncs Array...')
					for (var i = 0; i < intializeEventFuncs.length; i++) {
						this.log("\tRunning intializeEventFuncs["+i+']...');
						try {
							intializeEventFuncs[i]({'thisObj': this});
						} catch (e) {
							this.log("\tError in intializeEventFuncs["+i+']!');
							this.log("---\n"+e.name + ": " + e.message+"\n---");
						}
					}
				this.log('...Finished intializeEventFuncs');
			}

		}
	/* add special ts event msgs */
		this.addTsSpecialMsgsEvents = function(tsSpecialMsgsObj) {
			if (Object.size(tsSpecialMsgsObj) > 0) {
				//create a mapping for the dates to dayObjs, this is the shortest safe check we can do to start
				var dateToDayMap = {};
				for (var day in this.dayObjs) {
					var curDayObj = this.dayObjs[day];
					dateToDayMap[curDayObj.timeMonth+'/'+curDayObj.timeDay+'/'+curDayObj.timeYear] = curDayObj.id;
				}

				for (var date in tsSpecialMsgsObj.msgs) {
					if (dateToDayMap.hasOwnProperty(date)) {
						for (var s = 0; s < this.dayObjs[dateToDayMap[date]].TSIds.length; s++) {
							var curSlotObj = this.slotObjs[this.dayObjs[dateToDayMap[date]].TSIds[s]];


							if (!curSlotObj.hasOwnProperty('specialMsgMediaPath') || curSlotObj.specialMsgMediaPath != tsSpecialMsgsObj.msgs[date]) {
								if (curSlotObj.hasOwnProperty('specialMsgMediaPath') && curSlotObj.specialMsgMediaPath != tsSpecialMsgsObj.msgs[date]) {
									this.log('addTsSpecialMsgsEvents: Updated special msg on Slot ('+curSlotObj.id+') from: '+curSlotObj.specialMsgMediaPath+' to:'+tsSpecialMsgsObj.msgs[date]);
								}
								if (!curSlotObj.hasOwnProperty('specialMsgMediaPath')) {
									curSlotObj.addCustomEvent({
										event: 'click', 
										func: function(argsObj) { 
											doOverlayDialog(argsObj.slot.specialMsgMediaPath); 
											if (argsObj.thisObj.opts.debug) {
												$jq('#uimodal-output').append(argsObj.slot.specialMsgMediaPath);
											}
										},
										params: { 'thisObj': this, 'slot': curSlotObj }
									});
									this.log('addTsSpecialMsgsEvents: Added special msg click event to Slot ('+curSlotObj.id+').');
								}
								curSlotObj.specialMsgMediaPath = tsSpecialMsgsObj.msgs[date];
							} else {
								this.log('addTsSpecialMsgsEvents: Slot ('+curSlotObj.id+') already has special msg event set, skipping.');
							}

						}
					}
				}
			}
		}
	/* remove mouse over expands */
		this.removeMouseOverExpands = function() {
			for (var day in this.dayObjs) {
				Event.stopObserving(day, 'mouseover');
				Event.stopObserving(day, 'mouseout');
				//Event.stopObserving(this.dayObjs[day].hC, 'mouseover');
				//Event.stopObserving(this.dayObjs[day].hC, 'mouseout');
			}
		}
	/* remove mouse over colors */
		this.removeMouseOverColors = function() {
			for (var slot in this.slotObjs) {
				Event.stopObserving(slot, 'mouseover');
			}
		}
	/* add specific events
	 *	optionally, if type of elemId is know, pass it in as optKnownTypeArg
	 *	otherwise, an attempt to auto determine type is made (slower)
	 *	either way, elemId is extended if needed
	 */
		this.addEvent = function(eventTypeArg, elemIdArg, optKnownTypeArg) {
			var elemId = elemIdArg || null;
			var eventType = eventTypeArg || null;
			var optKnownType = optKnownTypeArg || null;
			
			if (!elemId || !eventType) { return; }

			var eventObject = null;
			//determine obj holding id
			var objTypeAndRef = this.getTypeAndRefById(elemId, optKnownType);
			if (objTypeAndRef && objTypeAndRef.typeStr !== null && objTypeAndRef.parentRef !== null && objTypeAndRef.childStr !== null) {
				eventObject = objTypeAndRef.parentRef[elemId][objTypeAndRef.childStr];
			}
			
			//couldn't find an obj, better to return than set an incorrect event handler
			if (!eventObject) { return; }

			//seperate out events, extra calls, but more flexibility
			switch (eventType) {
				case 'mouseOvers':
					switch (objTypeAndRef.typeStr) {
						case 'dayId':
						case 'dayHeaderC':
							//day needs expand
							eventObject.observe('mouseover', this.dayMouseOver.bindAsEventListener(this, eventObject.id));
							eventObject.observe('mouseout', this.dayMouseOut.bindAsEventListener(this, eventObject.id));
							break;
						default:
					}
					break;
				case 'mouseOversClickOnly':
					switch (objTypeAndRef.typeStr) {
						case 'tsId':
						case 'coId': //needed for DpTc
							//day needs expand
							eventObject.observe('mouseover', this.tsMouseOverClickOnly.bindAsEventListener(this, eventObject.id));
							eventObject.observe('mouseout', this.tsMouseOutClickOnly.bindAsEventListener(this, eventObject.id));
							break;
						default:
					}
					break;
				case 'mouseClicks':
					switch (objTypeAndRef.typeStr) {
						case 'dayHeaderC':
							eventObject.observe('click', this.dayMouseClick.bindAsEventListener(this, eventObject.id));
							break;
						case 'tsId':
							eventObject.observe('click', this.tsMouseClick.bindAsEventListener(this, eventObject.id));
							break;
					}
					break;
				default:
			}
		}
	/* event handlers*/
		/* day-level mouse over */
			this.dayMouseOver = function() {
				var elemId = arguments[1];
				var dayId = this.convertId(elemId, 'dayId');
				var fdTSDisplay = this; //ref to fdTSDisplay
				//fdTSDisplay.log('dayMouseOver ', elemId, dayId, this.opts.expandedDayId);

				//once per day
				if (dayId !== this.opts.expandedDayId) {
					if (fdTSDisplay.opts.clickOnly) {
						//add class for contracted gradient
						if (fdTSDisplay.dayObjs[dayId].hCext.className.indexOf(' tsHeaderCMouseOver') === -1) {
							fdTSDisplay.dayObjs[dayId].hCext.className += ' tsHeaderCMouseOver';
						}
					}

					var waitTime = this.opts.mouseOverTimeLimit;
					var expandedDay = this.opts.expandedDayId;
					//var isExpanded = this.dayObjs[dayId].isExpanded;
					//var lastMouseOverElemId = this.opts.lastMouseOverElemId;

					//we set it, setup and call setTimeout
					var tsMouseOverFunc = function() {
						if (dayId === expandedDay) { return; }
						while(!fdTSDisplay.setDayAsExpanded(dayId)){};
					}
					if (this.opts.mouseOverSetTimeout === null) {
						this.opts.mouseOverSetTimeout = setTimeout(tsMouseOverFunc, waitTime);
					}else{ //fix stuck mouseovers
						clearTimeout(this.opts.mouseOverSetTimeout);
						this.opts.mouseOverSetTimeout = null;
						this.opts.mouseOverSetTimeout = setTimeout(tsMouseOverFunc, waitTime);
					}
				}

				
				/* custom event call
				 *	this gets called EVERY day HEADER mousover (collapsed days only)
				 *	careful, this can be called a bunch of times before a mouseout event
				 */
				var dayObj = fdTSDisplay.dayObjs[dayId];
				if (dayObj.hasOwnProperty('customEvents') && 'mouseover' in dayObj.customEvents) {
					dayObj.handleCustomEvents({event: 'mouseover'});
				}
			}
		/* day-level mouse out */
			this.dayMouseOut = function() {
				var elemId = arguments[1];
				var fdTSDisplay = this; //ref to fdTSDisplay

				var srcElem = arguments[0].target || arguments[0].srcElement;
				var dayId = this.convertId(elemId, 'dayId');
				var srcDayId = this.convertId(srcElem.id, 'dayId');

				//fdTSDisplay.log('dayMouseOut ', elemId, srcDayId);

				if (fdTSDisplay.opts.clickOnly) {
					//remove class for contracted gradient
					fdTSDisplay.dayObjs[dayId].hCext.className = fdTSDisplay.dayObjs[dayId].hCext .className.replace(' tsHeaderCMouseOver', '');
				}

				//don't clear on child elems
				if (dayId === srcDayId && srcDayId !== -1) { return }
				
				//clear waiting event
				clearTimeout(fdTSDisplay.opts.mouseOverSetTimeout);
				//reset on mouse out
				this.opts.mouseOverSetTimeout = null;
			}
		/* day-level mouse click (left) */
			this.dayMouseClick = function() {
				var elemId = arguments[1];
				var fdTSDisplay = this; //ref to fdTSDisplay
				var srcElem = arguments[0].target || arguments[0].srcElement;
				var dayId = this.convertId(elemId, 'dayId');

				while(!fdTSDisplay.setDayAsExpanded(dayId)){};

			}
		/* slot-level click */
			this.tsMouseClick = function() {
				var elemId = arguments[1];
				var fdTSDisplay = this; //ref to fdTSDisplay
				var srcElem = arguments[0].target || arguments[0].srcElement;
				var dayId = fdTSDisplay.convertId(elemId, 'dayId');

				var slotObj = fdTSDisplay.slotObjs[elemId];

				fdTSDisplay.log('You clicked:', slotObj.id);
				fdTSDisplay.log('Click Info Before:', fdTSDisplay.opts.radioCheckedCur, fdTSDisplay.opts.radioCheckedLast, fdTSDisplay.opts.radioCheckedLastUndo);

				if (srcElem && srcElem.id === slotObj.radio) {
					//we already have the radio
					if (!fdTSDisplay.opts.radioCheckedCur) {
						//first time here?

						//turn srcElem.id radio into a day id
						var tempDayIdFirst = fdTSDisplay.convertId(srcElem.id, 'dayId');
						var rowObjFirst = fdTSDisplay.getRowObjByDayId(tempDayIdFirst);

						//set as clicked
						fdTSDisplay.opts.radioCheckedCur = srcElem.id;

						//set as currently checked
						slotObj.isChecked = true;

						//first slot chosen on load, add it
						//fdTSDisplay.opts.radioCheckedLast = srcElem.id;

						//set as lastUndo
						fdTSDisplay.opts.radioCheckedLastUndo = srcElem.id;

						//and add classname (if it doesn't already have it)
						if (slotObj.ext.className.indexOf(' tsContentResE') === -1) {
							if (slotObj.ext.className.indexOf(' tcSelectionBGC') === -1) {
								slotObj.ext.className +=' tcSelectionBGC';
							}
						}
					}else{
						//good to see you again

						//make sure this isn't the same as what was last clicked
						if (srcElem.id != fdTSDisplay.opts.radioCheckedLastUndo) {

							//clear pre-loaded selections
							var preDef = $$('.tsSelectedSlotPropE');
							for (var p = 0; p < preDef.length; p++) {
								preDef[p].innerHTML = '&nbsp;';
							}
							var preDef = $$('.tsSelectedSlotPropC');
							for (var p = 0; p < preDef.length; p++) {
								preDef[p].innerHTML = '&nbsp;';
							}
							preDef = $$('.tsContentSelE');
							for (var p = 0; p < preDef.length; p++) {
								preDef[p].className = preDef[p].className.replace('tsContentSelE', '');
							}
							preDef = $$('.tsContentSelC');
							for (var p = 0; p < preDef.length; p++) {
								preDef[p].className = preDef[p].className.replace('tsContentSelC', '');
							}

							//set as clicked
							slotObj.isChecked = true;
							//set as currently checked
							fdTSDisplay.opts.radioCheckedCur = srcElem.id;
							//and add classname (if it doesn't already have it)
							if (slotObj.ext.className.indexOf(' tcSelectionBGC') === -1) {
								slotObj.ext.className +=' tcSelectionBGC';
							}
							//is selected not overriding resv? if so, comment this next line
							//slotObj.ext.className = slotObj.ext.className.replace(' tsContentResE', '');

							//turn cur radio into a day id
							var tempDayId = fdTSDisplay.convertId(srcElem.id, 'dayId');
							var rowObj = fdTSDisplay.getRowObjByDayId(tempDayId);

							//check that clicked one isn't the same as lastUndo
							if (srcElem.id !== fdTSDisplay.opts.radioCheckedLastUndo) {
								//it's not, clear it now
								//undo last one
								var undoSlotId = fdTSDisplay.convertId(fdTSDisplay.opts.radioCheckedLastUndo, 'tsId');
								var undoSlotObj = fdTSDisplay.slotObjs[undoSlotId];
								//make sure it's no longer marked as checked
								undoSlotObj.isChecked = false;
								//undo bgcolor class
								undoSlotObj.ext.className = undoSlotObj.ext.className.replace(' tcSelectionBGC', '');
								
								//move to lastUndo
								fdTSDisplay.opts.radioCheckedLastUndo = fdTSDisplay.opts.radioCheckedCur;
							}

							//mark as the last one clicked
							fdTSDisplay.opts.radioCheckedLastUndo = srcElem.id;

						}

					}

					/* custom event call
					 *	this gets called EVERY click/TS change (not just when the slot is first selected)
					 */
					if (slotObj.hasOwnProperty('customEvents') && 'click' in slotObj.customEvents) {
						slotObj.handleCustomEvents({event: 'click'});
					}
					
					fdTSDisplay.log('Click Info After:', fdTSDisplay.opts.radioCheckedCur, fdTSDisplay.opts.radioCheckedLast, fdTSDisplay.opts.radioCheckedLastUndo);
				}else{
					//call click so the logic follows
					if (fdTSDisplay.slotObjs[elemId].radioExt) {
						fdTSDisplay.slotObjs[elemId].radioExt.click();
						return;
					}
				}

				if (!fdTSDisplay.dayObjs[dayId].isExpanded) {
					while(!fdTSDisplay.setDayAsExpanded(dayId)){};
				}

			}
		/* slot-level mouse over, click only */
			this.tsMouseOverClickOnly = function() {
				var elemId = arguments[1];
				var fdTSDisplay = this; //ref to fdTSDisplay
				var slotObj = fdTSDisplay.slotObjs[elemId];
				var dayId = fdTSDisplay.convertId(elemId, 'dayId');
				
				//check extend queue
				this.checkQueue(elemId);
				if (slotObj) { this.checkQueue(slotObj.radio); }

				//only show colors on non-expanded days, on slots that have radios
				if (slotObj && slotObj.radioExt) {
					slotObj.ext.className += ' clickOnlyMouseOver';
				}

				//hover on for dp T&C
				if (this.opts.showDpTc && !this.opts.premDcTpAgreed) {
					if (this.dayObjs[dayId].showDpTc && this.dayObjs[dayId].isExpanded) {
						if ($(this.opts.premSlotsDpTcElem)) { $(this.opts.premSlotsDpTcElem).show(); }
					}
				}

			}
		/* slot-level mouse out, click only */
			this.tsMouseOutClickOnly = function() {
				var elemId = arguments[1];
				var fdTSDisplay = this; //ref to fdTSDisplay
				var slotObj = fdTSDisplay.slotObjs[elemId];
				var dayId = fdTSDisplay.convertId(elemId, 'dayId');

				if (slotObj) {
					slotObj.ext.className = slotObj.ext.className.replace(' clickOnlyMouseOver', '');
				}
				//hover on for dp T&C
				if (this.opts.showDpTc) {
					if (!this.dayObjs[dayId].showDpTc || elemId !== this.opts.premSlotsDpTcElem) {
						if ($(this.opts.premSlotsDpTcElem)) { $(this.opts.premSlotsDpTcElem).hide(); }
					}
				}

			}

	/* --- UTIL FUNCTIONS --- */

	/* toggle debug */
		this.toggleDebug = function() {
			this.opts.debug = !this.opts.debug;
			this.log('debug now set to: '+this.opts.debug);
		}

	/* toggle clickOnly */
		this.toggleClickOnly = function() {
			this.startTimer();
				this.log('Toggling clickOnly...');
					if (this.opts.clickOnly) {
						//clickOnly going from true to false, remove mouse over colors
						this.removeMouseOverColors();
					}else{
						//clickOnly going from false to true, remove mouse over expands
						this.removeMouseOverExpands();
					}
					this.opts.clickOnly = !this.opts.clickOnly;
					
					//now put back events
					this.addEvents();
				this.log('...clickOnly is now '+this.opts.clickOnly);
			this.endTimer();
		}

	/* set if in timeslot info page */
		this.setTimeSlotInfo = function() {
			if (window.timeslot_info) {
				this.opts.timeSlotInfo = timeslot_info;
			}
		}
	
	/* set prem.slots bool, and timestamp fetch */
		this.setShowPremiumSlots = function() {
			var match = null;
			var showPremSlots = window.showPremiumSlots || false;

			this.log('PremSlots: '+showPremSlots);

			if (showPremSlots) {
				this.opts.showPremiumSlots = showPremiumSlots;

				if (window.premSlotsCO) {
					//check for timestamp format
					if (premSlotsCO.indexOf(' ') != -1) {
						//timestamp
						match = premSlotsCO.match("^(\\d{2})-(\\d{2})-(\\d{4})\\s(\\d{1,2}):(\\d{1,2}):(\\d{1,2})$");
						this.opts.premSlotsCO = new Date(Date.UTC(
							parseInt(match[3], 10), // Y
							parseInt(match[2], 10) - 1, // M
							parseInt(match[1], 10), // D
							parseInt(match[4], 10), // h
							parseInt(match[5], 10), // m
							parseInt(match[6], 10), // s
							0 //ms
						));
						
						this.log('PremSlots Timestamp:'+window.premSlotsCO+' Parsed after:'+this.opts.premSlotsCO);
					} else {
						this.opts.premSlotsCO = new Date(premSlotsCO);
						this.log('PremSlots Timestamp not set, using curDate:'+this.opts.premSlotsCO);
					}
				} else {
					//no timstamp, set to now so it's expired.
					this.opts.premSlotsCO = new Date().getTime();
					this.log('PremSlots Timestamp not set, using curDate:'+this.opts.premSlotsCO);
				}
			
				if (showPremiumSlots && match != null && match[1] != new Date().getDay()) {
					this.log('same day mismatch, check data.');
				}
			}else{
				//clear premSlotsDayId so it can't be referenced
				this.opts.premSlotsDayId = null;
			}
			this.log('PremSlots premSlotsDayId: '+this.opts.premSlotsDayId);

		}
	
	/* prem slots interval function for timer display */
		this.premSlotsTimer = function() {
			 //kill timer, no display elem or no cutoff time
			if (window.fdTSDisplay.opts.premSlotsTimerElem === null || window.fdTSDisplay.opts.premSlotsCO === null) {
				window.clearInterval(window.fdTSDisplay.opts.premSlotsClock);
			}
			var timeDisp = '00:00:00';
			//create a new date object and compare to premCO obj, kill if past
			var curTime = new Date();
			if (curTime.getTime() > window.fdTSDisplay.opts.premSlotsCO.getTime()) {
				window.fdTSDisplay.premGoUnavail();
			} else {
				var timeCheck = window.fdTSDisplay.opts.premSlotsCO.getTime() - curTime.getTime();
				if (timeCheck > 0) {
					timeDisp = window.fdTSDisplay.formatTimer(timeCheck);
					$(window.fdTSDisplay.opts.premSlotsTimerElem_msg).innerHTML = 'to place your order';
				}
			}

			//update all timer elems
			$$('.'+window.fdTSDisplay.opts.premSlotsTimerElemClass).each(function(e, i) {
				var iHtml = timeDisp + ' LEFT';
				if ($(e).up('div').className === 'premSlotTimerExpanded') {
					iHtml += '<div>to place your order</div>';
				}
				
				$(e).innerHTML = iHtml;
			});


			//killtimer safety
			if (timeDisp.indexOf('00:00:00') != -1) {
				window.fdTSDisplay.premGoUnavail();
			}
		}
	
	/* set DPTC bool */
		this.setShowDpTc = function() {
			if (window.showDpTc) {
				this.opts.showDpTc = showDpTc;
			}
		}

	/* correct Date obj for DLST, do NOT apply this multiple times */
		this.getCorrectedDateObj = function(timeVal) {
			
			var timeInt = parseInt(timeVal) || 0;

			if (timeInt > 0) {
				//offset already applied to timeVal, put it back
				//be sure to init the date object here, DLST will affect the calc.
				timeInt = timeInt + (new Date(timeInt).getTimezoneOffset() * 60 * 1000);
			}

			var correctedDateObj = new Date(timeInt);

			return correctedDateObj;
		}

	/* format timer for display */
		this.formatTimer = function(timeVal) {
			var time = '';
			var timeObj = this.getCorrectedDateObj(timeVal);

			var hours = timeObj.getHours();
				time += (hours < 10) ? '0'+hours : hours;

			time += ':';

			var mins = timeObj.getMinutes();
				time += (mins < 10) ? '0'+mins : mins;

			time += ':';

			var secs = timeObj.getSeconds();
				time += (secs < 10) ? '0'+secs : secs;

			return time;
		}

	/* get reorg data
	 *	pass in rowId, must exist in rowObjs
	 *	adds reOrg data to dayObj
	 */
		this.getReorgData = function (rowIdArg) {
			var rowId = rowIdArg || '';
			if (!this.rowObjs[rowId]) { return; }
			
			for (var d = 0; d < this.rowObjs[rowId].dayIds.length; d++) {
				var dayId = this.rowObjs[rowId].dayIds[d];
				if (!this.dayObjs[dayId]) { continue; }
				this.setReorgDataForDay(dayId, this.dayObjs[dayId].dayPart);
				this.parseDayTimeInfo(this.dayObjs[dayId]);
			}

		}
		this.setReorgDataForDay = function(dayIdArg, dayPartArg) {
			var day = this.dayObjs[dayIdArg];
			var dayPart = dayPartArg || -1;
			var reorgData = [];
			var sequenceCount = 0; //reset count
			var startSequence = false; //reset sequencer
			var reorgStart = -1; //reset start marker
			var reorgEnd = -1; //reset end marker
			var lastWasTS = false; //reset last TS marker

			//skip if we don't have enough slots to span anyway
			if (day.TSs.length < this.opts.reorgRowLimit) { return; }

			//check that refData and ID lengths match (otherwise we can't get reorg data)
			if (day.TSs.length != day.COs.length) {
				this.log('ERR: TS/CO refData mismatch, cannot get reOrg data for '+dayIdArg);
				return;
			}
			if (day.TSs.length != day.TSIds.length) {
				this.log('ERR: TS refData mismatch, cannot get reOrg data for '+dayIdArg);
				return;
			}
			if (day.COs.length != day.COIds.length) {
				this.log('ERR: CO refData mismatch, cannot get reOrg data for '+dayIdArg);
				return;
			}

			for (var t = 0; t < day.TSs.length; t++) {

				if (t === 0) {
					/* check slot 0 for contentId */
					var curSlotId = this.dayObjs[dayIdArg].TSIds[t];

					//check extend queue
					this.checkQueue(curSlotId);

					if (this.slotObjs[curSlotId]) {
						var curSlotObj = this.slotObjs[curSlotId];
						var curContentId = '';
						var curContentExt = null;
						if (curSlotObj.ext.getAttribute('name') != null && curSlotObj.ext.getAttribute('name').indexOf('_') != -1) {
							curContentId = curSlotObj.ext.getAttribute('name').split('_');
							var tempId = curContentId[1];
							if (tempId !== '') {
								if ($(tempId+'C')) { tempId = tempId+'C'; } //if C/E versions exist, use them

								curContentExt = $(tempId);
							}
							if (curContentExt && !day.showPremium) { //for slots.prem, it's NOT the whole day
								//we have a holiday or cutoffpassed
								curSlotObj.ext.innerHTML = curContentExt.innerHTML;
								//set in slot so we don't have to seek again
								curSlotObj.contentId = tempId;
								/* if we have a special contentId, it's a full day, so we can fill in reorgData and return */
								day.reorgData = [[0, day.TSs.length-1]];
								return;
							}
						}
					}
				}

				//try parsing content for times
				this.parseSlotTimeInfo(this.slotObjs[ this.dayObjs[dayIdArg].TSIds[t] ]);

				/* break out if we haven't started a sequence yet
				 * and don't have enough rows left to be able to span
				 */
				if (!startSequence && day.TSs.length-t < this.opts.reorgRowLimit) { break; }

				if (
					day.COs[t] !== '' //hit a cutoff
					|| (day.COs[t] === '' && t === 0) //first cutoff
					|| (day.COs[t] === '' && t === dayPart) //hit dayPart
				) {
					//reorg now
					if (reorgStart != -1 && reorgEnd != -1) {
						reorgData[reorgData.length] = [reorgStart, reorgEnd];
					}
					startSequence = true;
					reorgStart = t;
					reorgEnd = -1;
					sequenceCount = 0; //reset count
				}
				if (startSequence && day.TSs[t] === '') {
					sequenceCount++;
					if (lastWasTS) {
						reorgStart = t; //reset to current ts
						lastWasTS = false;
					}
				}else{
					if (reorgStart !== -1 && reorgEnd !== -1) {
						reorgData[reorgData.length] = [reorgStart, reorgEnd];
					}
					reorgStart = -1;
					reorgEnd = -1;
					sequenceCount = 0; //reset count
					lastWasTS = true; //mark for next loop to reset
				}
				if (sequenceCount > this.opts.reorgRowLimit-1) {
					reorgEnd = t;
				}
			}
			//check if data is leftover from loop
			if (reorgStart !== -1 && reorgEnd !== -1) {
				reorgData[reorgData.length] = [reorgStart, reorgEnd];
			}

			//set to day
			day.reorgData = reorgData;
		}

	/* parse displayed content to get start and end hours for a slot */
		this.parseSlotTimeInfo = function(slotObj) {
			if (slotObj == null || !slotObj.hasOwnProperty('id') || !this.slotObjs[slotObj.id] || !$(slotObj.id+'_timeInfo')) { return; }

			var timeString = $(slotObj.id+'_timeInfo').getText();
			var timeStringParse = timeString.split('-');
			slotObj.timeStart = null;
			slotObj.timeEnd = null;


			if (timeStringParse.length == 2) {
				slotObj.timeStart = new Date(parseInt(timeStringParse[0]));
				slotObj.timeEnd = new Date(parseInt(timeStringParse[1]));
			}

		}
	/* parse displayed content to get start and end hours for a slot */
		this.parseDayTimeInfo = function(dayObj) {
			if (dayObj == null || !dayObj.hasOwnProperty('id') || !this.dayObjs[dayObj.id] || !$(dayObj.id+'_timeInfo')) { return; }

			var timeString = $(dayObj.id+'_timeInfo').getText();
			dayObj.timeDay = null;
			dayObj.timeMonth = null;
			dayObj.timeYear = null;

			var dayDateObj = this.getCorrectedDateObj(timeString);

			if (dayDateObj.getDate() < 10) { dayObj.timeDay = '0'; }
			if (dayDateObj.getMonth()+1 < 10) { dayObj.timeMonth = '0'; }

			dayObj.timeDay += dayDateObj.getDate();
			dayObj.timeMonth += dayDateObj.getMonth()+1;
			dayObj.timeYear = dayDateObj.getFullYear();
		}

	/* read refData and put values into the correct vars
	 *	optionally, use optStartIndexArg and optEndIndexArg to specify
	 *	the index range to use. defaults to 0, refData.length
	 *	Additionally pass in the rowId the refData will be added to,
	 *	this is the elementId for the row data.
	 *	dayPart is shared across the row, but set per day
	 *
	 *	Uses extend as it processes, any invalid data will be skipped
	 */
		this.getReferenceData = function(refDataArg, optStartIndexArg, optEndIndexArg, rowIdArg, dayPartArg) {
			var refDataCur = refDataArg || [];
			var optStartIndex = optStartIndexArg || 0;
			var optEndIndex = optEndIndexArg || refDataCur.length;
			var rowId = rowIdArg || '';
			var dayPart = dayPartArg || -1;

			var tempRow = new Row(rowId); //temp Arr rowObjs
			if (this.opts.preExtend) {
				//make sure row exists via extend, or return
				tempRow.ext = $(tempRow.id);
				if (!tempRow.ext) { return; }
			}else{
				//put in extendQueue
				this.extendQueue[tempRow.id] = 'row';
			}

			for (var d = optStartIndex; d < optEndIndex; d++) {
				if (refDataCur[d] === undefined) { continue; }
				var tempDay = new Day(this.getID('dayId', d));
				
				if (this.opts.preExtend) {
					//make sure day exists with extend, or continue
					tempDay.ext = $(tempDay.id);
					if (!tempDay.ext) { continue; }
					
					//make sure dayPart exists with extend, or continue
					tempDay.dayPartExt = $(this.getID('dayPartId', d));
					if (!tempDay.dayPartExt) { continue; }
				}else{
					//put in extendQueue
					this.extendQueue[tempDay.id] = 'day';
					this.extendQueue[this.getID('dayPartId', d)] = 'dayPart';
				}

				tempDay.dayPart = dayPart;

				//check for prem.slots display
				if (this.opts.showPremiumSlots && (this.getID('dayId', d) == this.opts.premSlotsDayId)) {
					tempDay.showPremium = true; //false by default
				}
				
				//check for DP T& C display (must be in prem slots day)
				if (this.opts.showDpTc && (this.getID('dayId', d) == this.opts.premSlotsDayId)) {
					tempDay.showDpTc = true; //false by default
				}
				
				if (this.opts.preExtend) {
					//make sure header expanded exists with extend, or continue
					tempDay.hEext = $(this.getID('dayHeaderE', d));
					if (!tempDay.hEext) { continue; }
				}else{
					//put in extendQueue
					this.extendQueue[this.getID('dayHeaderE', d)] = 'dayHeaderE';
				}
				tempDay.hE = this.getID('dayHeaderE', d);
				
				if (this.opts.preExtend) {
					//make sure footer expanded exists with extend, or continue
					tempDay.fEext = $(this.getID('dayFooterE', d));
					if (!tempDay.fEext) { continue; }
				}else{
					//put in extendQueue
					this.extendQueue[this.getID('dayFooterE', d)] = 'dayFooterE';
				}
				tempDay.fE = this.getID('dayFooterE', d);
				
				if (this.opts.preExtend) {
					//make sure header contracted exists with extend, or continue
					tempDay.hCext = $(this.getID('dayHeaderC', d));
					if (!tempDay.hCext) { continue; }
				}else{
					//put in extendQueue
					this.extendQueue[this.getID('dayHeaderC', d)] = 'dayHeaderC';
				}
				tempDay.hC = this.getID('dayHeaderC', d);

				//parent key the  row to the day
				tempDay.parentId = tempRow.id;

				//set refs, we'll use these in reorganizer
				tempDay.TSs = refDataCur[d][0];
				tempDay.COs = refDataCur[d][1];

				//loop through day and set timeslots and cutoffs
				for (var t = 0; t < tempDay.TSs.length; t++) {
					if (tempDay.TSs[t] === undefined) { continue; }
					if (tempDay.COs[t] === undefined) { continue; }

					var tempCutoff = new Cutoff(this.getID('coId', d, t));
					if (tempDay.COs[t] !== '') {
						//set each CO we find, so we end up with the last one
						tempDay.lastCO = tempCutoff.id;
					}

					if (this.opts.preExtend) {
						//extend cutoff
						tempCutoff.ext = $(tempCutoff.id);
					}else{
						//put in extendQueue
						this.extendQueue[tempCutoff.id] = 'cutoff';
					}
					//make sure cutoff exists with extend, or skip addition
					if (tempCutoff.ext || !this.opts.preExtend) {
						//add to cutoff array
						this.cutoffObjs[tempCutoff.id] = tempCutoff;

						//add cutoffId to day
						tempDay.COIds[tempDay.COIds.length] = tempCutoff.id;
					}

					var tempSlot = new Slot(this.getID('tsId', d, t));

					if (this.opts.preExtend) {
						//extend slot
						tempSlot.ext = $(tempSlot.id);
					}else{
						//put in extendQueue
						this.extendQueue[tempSlot.id] = 'slot';
					}
					//make sure slot exists with extend, or skip addition
					if (tempSlot.ext || !this.opts.preExtend) {
						
						if (this.opts.preExtend) {
							//see if radio exists with extend (not required)
							tempSlot.radioExt = $(this.getID('tsRadio', d, t));
						}else{
							//put in extendQueue
							this.extendQueue[this.getID('tsRadio', d, t)] = 'radio';
						}
						if (tempSlot.radioExt || !this.opts.preExtend) {
							tempSlot.radio = this.getID('tsRadio', d, t);
						}

						//add to slot array
						this.slotObjs[tempSlot.id] = tempSlot;

						//add slotId to day
						tempDay.TSIds[tempDay.TSIds.length] = tempSlot.id;
					}
					

				}

				//add day to array
				this.dayObjs[tempDay.id] = tempDay;
				
				//add day to row
				tempRow.dayIds[tempRow.dayIds.length] = tempDay.id;
			}
			
			//add row to array
			this.rowObjs[rowId] = tempRow;
		}

	/* logging functionality. non-console browser safe. */
		this.log = function() {
			var logMsg = '';
			for (var a = 0; a < arguments.length; a++) {
				logMsg += arguments[a];
				//add space between args
				logMsg += ' ';
			}
			//add space between timestamp and msg
			logMsg = ' '+logMsg;

			if (this.opts.debug && window.console) {
				console.log(new Date().toLocaleTimeString(), logMsg);
			}
		}

	/* timer functionality
	 *	Timer uses log for printing times, stored in opts for usage when debug = false
	 *	
	 *	use startTimer to start timing
	 *	use checkTimer to get a current time of a timed run
	 *	use endTimer to get a final time
	 *
	 *	calling out of order will log a warning msg
	 *	NOTE: timing and logging adds slightly to timing total.
	 */
		//timer shortcuts
		this.startTimer = function() { this.timer('start'); }
		this.checkTimer = function() { this.timer(); }
		this.endTimer = function() { this.timer('end'); }

		this.timer = function(action) {
			switch (action) {
				case 'start':
					this.opts.timer_StartTime = new Date().getTime();
					this.log('Timer: Starting Timer.');
					break;
				case 'end':
					if (this.opts.timer_StartTime === -1) {
						this.checkTimer();
					}else{
						var endTime = new Date().getTime();
						this.opts.timer_EndTime = endTime;
						this.log('Timer: '+(endTime-this.opts.timer_StartTime)+'ms elapsed.');
					}
					break;
				default:
					if (this.opts.timer_StartTime === -1) {
						this.log('Timer: Timer Not Started!');
					}else{
						var lastTime = new Date().getTime();
						this.opts.timer_LastTime = lastTime;
						this.log('Timer:'+(lastTime-this.opts.timer_StartTime)+'ms have passed since Timer started.');
					}
			}
		}

	/* get formatted id by type
	 *	formats come from idTemplates array
	 *	if typeArg is null and overload is a string, applies replace to it
	 *	otherwise, returns an empty string
	 */
		this.getID = function(typeArg, dayArg, timeArg, indexArg, overloadArg) {
			var type = (typeArg === undefined) ? '' : typeArg+'' || '';
			var day = (dayArg === undefined) ? '' : dayArg+'' || '';
			var time = (timeArg === undefined) ? '' : timeArg+'' || '';
			var index = (indexArg === undefined) ? '' : indexArg+'' || '';
			var overload = (overloadArg === undefined) ? null : overloadArg+'' || '';
			if (this.idTemplates[typeArg]) {
				return this.idTemplates[typeArg].replace(this.opts.dayVar, day).replace(this.opts.timeVar, time).replace(this.opts.indexVar, index);
			}else{
				if (overload === null) { return overload; }
				while (
					overload.indexOf(this.opts.dayVar) !== -1 || 
					overload.indexOf(this.opts.timeVar) !== -1 || 
					overload.indexOf(this.opts.indexVar) !== -1
				) {
					overload = overload.replace(this.opts.dayVar, day).replace(this.opts.timeVar, time).replace(this.opts.indexVar, index);
				}
				return overload;
			}

			return '';
		}

	/* detect IE version 
	 *	Returns the version of Internet Explorer or 
	 *	-1 (indicating the use of another browser).
	 */
		this.detectIEVersion = function() {
			var rv = -1; // Return value assumes failure.
			if (navigator.appName == 'Microsoft Internet Explorer') {
				var ua = navigator.userAgent;
				var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
				if (re.exec(ua) != null)
					rv = parseFloat( RegExp.$1 );
			}
			this.opts.IEver = rv;
		}
	
	/* ID parsers */
		/* parse day number from an id */
			this.parseDay = function (elemIdArg) {
				var elemId = elemIdArg || null;
				if (!elemId) { return -1; }
				
				elemId = (elemId+'').split('_');
				if (elemId.length >= 2) {
					return elemId[1].replace('d', '');
				}else{
					return -1;
				}
			}
		/* parse timeslot number from an id */
			this.parseTime = function (elemIdArg) {
				var elemId = elemIdArg || null;

				elemId = (elemId+'').split('_');
				if (elemId.length >= 3) {
					return elemId[2].replace('ts', '');
				}else{
					return -1;
				}
			}
		/* convert one id into another */
			this.convertId = function(elemIdArg, toIdTypeArg, indexArg) {
				var elemId = elemIdArg || null;
				var toIdType = toIdTypeArg || null;
				var index = indexArg || '';
				if (!elemId || !toIdType) { return -1; }

				var d = this.parseDay(elemId);
				var t = this.parseTime(elemId);
				elemId = this.getID(toIdType, d, t, index);

				if (!elemId || elemId.indexOf('-1') === -1) {
					return elemId;
				}else{
					return -1;
				}
			}
				
		/* find info for an id
		 *	returns an object with three properties
		 *		typeStr = string of 'type' (like 'slot')
		 *		parentRef = obj reference to obj containing the id (like slotObjs)
		 *		childStr = string of the property from parent (like 'hCext')
		 *
		 *	Extends elemId before checking (assuming it's in queue)
		 *	returns null if not found at all, or an object with null in each
		 *	property that was not found/used if id itself was found
		 *	Always returns the ext childStr (so it will then be extended)
		 *
		 *	check can then be done with returned typeStr vs the original elemId
		 *	by doing:
		 *		elemId == this.convertId(elemId, objTypeAndRef.typeStr)
		 *	this should be true if the id is of that type.
		 */
			this.getTypeAndRefById = function (elemIdArg, optKnownTypeArg) {
				var elemId = elemIdArg || null;
				if (!elemId) { return null; }
				var optKnownType = optKnownTypeArg || null;

				//check extend queue
				this.checkQueue(elemId);
				
				var testId =null;
				//return known types (assume optKnownType is actually this type)
				if (optKnownType) {
					switch (optKnownType) {
						case 'row':
							return { typeStr: optKnownType, parentRef: this.rowObjs, childStr: 'ext' };
						case 'day':
							return { typeStr: optKnownType, parentRef: this.dayObjs, childStr: 'ext' };
						case 'slot':
							return { typeStr: optKnownType, parentRef: this.slotObjs, childStr: 'ext' };
						case 'cutoff':
							return { typeStr: optKnownType, parentRef: this.cutoffObjs, childStr: 'ext' };
						case 'dayPartExt':
							return { typeStr: optKnownType, parentRef: this.dayObjs, childStr: 'dayPartExt' };
						case 'dayHeaderE':
							return { typeStr: optKnownType, parentRef: this.dayObjs, childStr: 'hEext' };
						case 'dayHeaderC':
							return { typeStr: optKnownType, parentRef: this.dayObjs, childStr: 'hCext' };
						case 'dayFooterE':
							return { typeStr: optKnownType, parentRef: this.dayObjs, childStr: 'fEext' };
						case 'tsRadio':
							return { typeStr: optKnownType, parentRef: this.dayObjs, childStr: 'radioExt' };
						default:
					}
				}

				//check if one of the main types
				if (this.rowObjs[elemId]) {
					return { typeStr: 'rowId', parentRef: this.rowObjs, childStr: 'ext' };
				}
				if (this.dayObjs[elemId]) {
					return { typeStr: 'dayId', parentRef: this.dayObjs, childStr: 'ext' };
				}
				if (this.slotObjs[elemId]) {
					return { typeStr: 'tsId', parentRef: this.slotObjs, childStr: 'ext' };
				}
				if (this.cutoffObjs[elemId]) {
					return { typeStr: 'coId', parentRef: this.cutoffObjs, childStr: 'ext' };
				}

				//not one of the main types, try subtypes
				testId = this.convertId(elemId, 'day');
				if (testId !== -1 && this.dayObjs[testId]) {
					if (this.dayObjs[testId].dayPartExt && this.dayObjs[testId].dayPartExt.id === elemId) {
						//this is a dayPart id
						return { typeStr: 'dayPartExt', parentRef: this.dayObjs, childStr: 'dayPartExt' };
					}
					if (this.dayObjs[testId].hEext && this.dayObjs[testId].hEext.id === elemId) {
						//this is a headerE
						return { typeStr: 'dayHeaderE', parentRef: this.dayObjs, childStr: 'hEext' };
					}
					if (this.dayObjs[testId].fEext && this.dayObjs[testId].fEext.id === elemId) {
						//this is a footerE
						return { typeStr: 'dayFooterE', parentRef: this.dayObjs, childStr: 'fEext' };
					}
					if (this.dayObjs[testId].fEext && this.dayObjs[testId].hCext.id === elemId) {
						//this is a headerC
						return { typeStr: 'dayHeaderC', parentRef: this.dayObjs, childStr: 'hCext' };
					}
				}
				testId = this.convertId(elemId, 'slot');
				if (testId !== -1 && this.slotObjs[testId]) {
					if (this.slotObjs[testId].radioExt && this.slotObjs[testId].radioExt.id === elemId) {
						//this is a headerC
						return { typeStr: 'tsRadio', parentRef: this.slotObjs, childStr: 'radioExt' };
					}
				}

				//no matches
				return null;
			}
		/* get rowObj for a dayId
		 *	this is only necessary since convertId(dayId, 'rowId')
		 *	can't get a rowId (they're in a unique format)
		 */
			this.getRowObjByDayId = function(dayIdArg) {
				var dayId = dayIdArg || null;
				if (!dayId) { return -1; }
				for (var row in this.rowObjs) {
					var curRowObj = this.rowObjs[row];
					for (var d = 0; d < curRowObj.dayIds.length; d++) {
						if (dayId === curRowObj.dayIds[d]) { return curRowObj; }
					}
				}
				return -1;
			}

	/* CSS ref functions */
		/* set CSS string by refElemId */
			this.setCssRefString = function (refElemIdArg) {
				var refElemId = refElemIdArg || '';

				//see if it's already set
				if (this.opts.cssRefString[refElemId]) { return; }

				var cssString = '';
				//it's not, set already, do so now
				var refElemExt = $(refElemId);
				if (refElemExt) {
					for (var i=0; i < this.opts.cssRefStyleAttributes.length; i++)	{
						if (refElemExt.getStyle) {
							if (
								refElemExt.getStyle(this.opts.cssRefStyleAttributes[i]) !== null && refElemExt.getStyle(this.opts.cssRefStyleAttributes[i]) !== ''
							) {
								cssString += this.opts.cssRefStyleAttributes[i];
								cssString += ':';
								var tempCSS = refElemExt.getStyle(this.opts.cssRefStyleAttributes[i]);

								/* Opera 10.x returns 0px width for display: none elements, fallback to getWidth */
								if (this.opts.cssRefStyleAttributes[i] === 'width' && tempCSS === '0px') {
									cssString += Element.getWidth(refElemId)+'px';
								}else{
									cssString += refElemExt.getStyle(this.opts.cssRefStyleAttributes[i]);
								}
								cssString += ';';
							}
						}
					}
				}

				//set it, even if it's an empty string
				this.opts.cssRefString[refElemId] = cssString;
			}
		/* get CSS string by refElemId
		 *	if it doesn't already exist, set it and then return it
		 *	a bad refElemId will always return an empty string
		 */
			this.getCssRefString = function (refElemIdArg) {
				var refElemId = refElemIdArg || '';
				if (refElemId === '') { return ''; }

				//see if it's already set, return it if so
				if (this.opts.cssRefString[refElemId]) { return this.opts.cssRefString[refElemId]; }

				//it's not, set it
				this.setCssRefString(refElemId);

				//and now return it
				return this.opts.cssRefString[refElemId];
			}

	/* return a css style height size based on arguments
	 *	numRowsArg = number of rows to calc for
	 *	pxEachArg = px height for each row
	 *	addOneArg = add one row's height to total
	 *	pxSubtractArg = px to be subtracted from final total (not per row)
	 */
		this.getCalcdRowHeight = function (numRowsArg, pxEachArg, addOneArg, pxSubtractArg) {
			var numRows = parseInt(numRowsArg, 10) || 1;
			var pxEach = pxEachArg || this.opts.rowHeight;
			var addOne = addOneArg || false;
			var pxSubtract = pxSubtractArg || 0;
			var pxTotal;

			pxTotal = pxEach * numRows;
			if (addOne) { pxTotal = pxTotal + pxEach; }
			if (pxSubtract) { pxTotal = pxTotal - pxSubtract; }
			
			return pxTotal+'px';
		}

	/* extend object in queue (if found) */
		this.checkQueue = function(elemId) {
			if (this.extendQueue[elemId]) {
				//items that need day id
				var extendType = this.extendQueue[elemId];
				var dayNumb = -1;
				var dayId = '';

				if (
					extendType === 'dayPart' ||
					extendType === 'dayHeaderE' ||
					extendType === 'dayFooterE' ||
					extendType === 'dayHeaderC'
				) {
					dayNumb = this.parseDay(elemId);
					dayId = this.getID('dayId', dayNumb);
				}

				switch (extendType) {
					case 'row':
						if (this.rowObjs[elemId]) {
							this.rowObjs[elemId].ext = $(elemId);
						}
						break;
					case 'day':
						if (this.dayObjs[elemId]) {
							this.dayObjs[elemId].ext = $(elemId);
						}
						break;
					case 'dayPart':
						if (dayNumb === -1) { break; }
						if (this.dayObjs[dayId]) {
							this.dayObjs[dayId].dayPartExt = $(elemId);
						}
						break;
					case 'dayHeaderE':
						if (dayNumb === -1) { break; }
						if (this.dayObjs[dayId]) {
							this.dayObjs[dayId].hEext = $(elemId);
						}
						break;
					case 'dayFooterE':
						if (dayNumb === -1) { break; }
						if (this.dayObjs[dayId]) {
							this.dayObjs[dayId].fEext = $(elemId);
						}
						break;
					case 'dayHeaderC':
						if (dayNumb === -1) { break; }
						if (this.dayObjs[dayId]) {
							this.dayObjs[dayId].hCext = $(elemId);
						}
						break;
					case 'slot':
						if (this.slotObjs[elemId]) {
							this.slotObjs[elemId].ext = $(elemId);
						}
						break;
					case 'radio':
						if (this.slotObjs[elemId.replace('_rb', '')]) {
							this.slotObjs[elemId.replace('_rb', '')].radioExt = $(elemId);
						}
						break;
					case 'cutoff':
						if (this.cutoffObjs[elemId]) {
							this.cutoffObjs[elemId].ext = $(elemId);
						}
						break;
					default:
				}
				//remove from queue
				delete this.extendQueue[elemId];
			}
		}
	/* extend all objects in queue
	 *	if onlyType is passed, only extended queued items of that type
	 */
		this.extendAllQueued = function(onlyTypeArg) {
			var onlyType = onlyTypeArg || null;
			for (var qItem in this.extendQueue) {
				if (onlyType !== null && onlyType !== this.extendQueue[qItem]) {
					continue;
				}
				this.checkQueue(qItem);
			}
		}

	/* reorganize based on reorgData
	 *	this will automatically reorganize based on 
	 *	if the day is expanded or not
	 */
		this.reorganize = function () {

			for (var dayId in this.dayObjs) {
				//check extend queue
				this.checkQueue(dayId);

				if (this.dayObjs[dayId].reorgData.length > 0) {
					this.reorganizeDay(dayId);
				}
				
				var dayObj = this.dayObjs[dayId];

				//check extend queue
				this.checkQueue(dayObj.ext.id);

				//ref other days in row
				var otherDayIds = this.rowObjs[dayObj.parentId].dayIds;

				if (dayObj.ext && dayObj.ext.down('.cutoff') && dayId === otherDayIds[0]) {
					//and add left line since it's the first one
					dayObj.ext.down('.cutoff').style.borderLeft = '1px solid #ccc';
				}
				if (dayObj.ext.down('.cutoff') && dayId === otherDayIds[otherDayIds.length-1]) {
					//and add right line since it's the last one
					this.dayObjs[otherDayIds[otherDayIds.length-1]].ext.down('.cutoff').style.borderRight = '1px solid #ccc';
				}
			}
		}
	/* regorganize a specific day by dayId */
		this.reorganizeDay = function (dayId) {
			if (!this.dayObjs[dayId]) { return; }
			var dayObj = this.dayObjs[dayId];
			var sequenceCount;
			
			//check queue
			this.checkQueue(dayId);

			if (dayObj.isExpanded) {
				//expanded day
				dayObj.ext.className = dayObj.ext.className.replace('tsTableC', 'tsTableE');

				//check extend queue
				this.checkQueue(this.convertId(dayId, 'dayPartId'));

				//hide dayPart
				//check for prem.slots
				if (!dayObj.showPremium) {
					dayObj.dayPartExt.up('tr').hide();
				}

				//see if day is totally empty
				if (dayObj.TSs.join('') === '') {
					//day has no timeslots, loop through all stored slots
					for (var t = 0; t < this.dayObjs[dayId].TSIds.length; t++) {
						var curSlotId = this.dayObjs[dayId].TSIds[t];
						var curCutoffId = this.dayObjs[dayId].COIds[t];

						//check extend queue
						this.checkQueue(curSlotId);
						this.checkQueue(curCutoffId);

						if (!this.slotObjs[curSlotId]) { continue; }
						if (!this.cutoffObjs[curCutoffId]) { continue; }

						var curSlotObj = this.slotObjs[curSlotId];
						var curCutoffObj = this.cutoffObjs[curCutoffId];

						var curCORow = curCutoffObj.ext.up('tr');
						var curTSRow = curSlotObj.ext.up('tr');

						//hide all empty cutoffs
						if (!curCORow.down('.cutoffDispChild')) {
							curCORow.hide();
						}
						if (dayObj.COs[t] === '') {
							curCORow.hide();
						}

						if (t === 0) {
							//take the current content id, change it to the expanded id
							//and make sure it exists
							var curContentId = ''
							if (curSlotObj.contentId === null) {
								curContentId = this.opts.noDeliveryEId;
							}else{
								curContentId = curSlotObj.contentId;
								if (curSlotObj.contentId.slice(curSlotObj.contentId.length-1) === 'C') {
									curContentId = curSlotObj.contentId.slice(0, -1)+'E';
								}
							}
							var curContentExt = $(curContentId);
							if (curContentExt) {
								//it does, set it and use it
								curSlotObj.contentId = curContentId;
								curSlotObj.ext.innerHTML = curContentExt.innerHTML;
							}
							//replace out className
							if (curSlotObj.contentId === this.opts.noDeliveryEId) {
								curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGC','tsContainerNoDelBGE');
								curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGE','tsContainerNoDelBGE');
								curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerNoDelBGC','tsContainerNoDelBGE');
							}
							curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGC','tsContainerBGE');

							if (curSlotObj.contentId && curSlotObj.contentId !== this.opts.noDeliveryEId) {
								//non-noDelivery prob don't wan't vertical-align top
								curSlotObj.ext.style.verticalAlign = 'middle';
							}

							//reset height, positive subtraction of cutoff height since there's no cutoff being used
							curSlotObj.ext.style.height = this.getCalcdRowHeight(dayObj.TSIds.length, null, false, (-2-this.opts.cutoffHeight));
							//and remove it's bottom line
							curSlotObj.ext.style.borderBottom = '0 solid #ccc';
						}else{
							//no time, hide
							curTSRow.hide();
						}
					}
				}else{
					var hiddenRows = 0;
					var lastVisRow = 0;
					//day has some timeslots, handle CO and TS at the same time
					for (var t = 0; t < dayObj.TSIds.length; t++) {
						var curSlotId = dayObj.TSIds[t];
						var curCutoffId = dayObj.COIds[t];
						if (dayObj.TSs[t] !== '') { lastVisRow = t; }

						//check extend queue
						this.checkQueue(curSlotId);
						this.checkQueue(curCutoffId);

						if (!this.slotObjs[curSlotId]) { continue; }
						if (!this.cutoffObjs[curCutoffId]) { continue; }

						var curSlotObj = this.slotObjs[curSlotId];
						var curCutoffObj = this.cutoffObjs[curCutoffId];

						var curCORow = curCutoffObj.ext.up('tr');
						var curTSRow = curSlotObj.ext.up('tr');

						//hide all empty cutoffs
						if (dayObj.COs[t] === '' || dayObj.showPremium) {
							curCORow.hide();
						}else{
							curCORow.show(); //tr
							curCORow.down('.cutoffDispChild').show(); //div
							//and add l/r lines
							curCORow.down('td').className = 'cutoffLR';
						}
						

						//hide if needed for prem.slots
						if (dayObj.TSs[t] === '' && !dayObj.showPremium) {
							//no time, hide
							curTSRow.hide();
							hiddenRows++;
						}else{
							if (!dayObj.showPremium) {
								curTSRow.show();
							} else if (dayObj.showPremium && dayObj.TSs[t] === '' && t >= dayObj.dayPart) {
								curTSRow.hide();
								hiddenRows++;
							}
							//time, toggle className
							this.toggleClassName(this.slotObjs[dayObj.TSIds[t]].ext, 'tsContainerBGC');
							this.toggleClassName(this.slotObjs[dayObj.TSIds[t]].ext, 'tsContainerC');
						}
						
						//check prem.slots
						//contents can be overriden here (check t vs dayPart for AM vs PM)
						if (dayObj.showPremium) {
							if (t === 0) {
								//adjust height, expanded
								curSlotObj.ext.style.height = (parseInt(curSlotObj.ext.style.height) + this.opts.cutoffHeight)+'px';
							}

							//change content (assuming we have it) change to expanded and proceed like normal
							var curContentId = curSlotObj.contentId;
							if (curContentId && curSlotObj.contentId.slice(curSlotObj.contentId.length-1) === 'C') {
								curContentId = curSlotObj.contentId.slice(0, -1)+'E';
							}
							curContentExt = $(curContentId);
							if (curContentExt) {
								curSlotObj.ext.innerHTML = curContentExt.innerHTML;
								//set in slot
								curSlotObj.contentId = curContentId;
							}
						}
						

					}
					//adjust height of final VISIBLE row
					if (hiddenRows > 0) { hiddenRows++; } //if we hid rows, include last ts in count
					curSlotObj = this.slotObjs[dayObj.TSIds[lastVisRow]];
					curSlotObj.ext.style.height = this.getCalcdRowHeight(hiddenRows, null, false, 0);
					//and remove it's bottom line
					curSlotObj.ext.style.borderBottom = '0px solid #fff';
				}
			}else{
				//day is contracted
				var reorgData = dayObj.reorgData;
				dayObj.ext.className = dayObj.ext.className.replace('tsTableE', 'tsTableC');

				for (var r = 0; r < reorgData.length; r++) {
					var reorgStart = reorgData[r][0];
					var reorgEnd = reorgData[r][1];
					sequenceCount = (reorgEnd-reorgStart)+1;
					
					//loop through slots being used in reorg set
					for (var t = reorgStart; t <= reorgEnd; t++) { //<= so we hide all
						var curSlotId = dayObj.TSIds[t];
						var curCutoffId = dayObj.COIds[t];

						//check extend queue (shouldn't be in queue now, since it's unorg)
						this.checkQueue(curSlotId);
						this.checkQueue(curCutoffId);

						if (!this.slotObjs[curSlotId]) { continue; }
						if (!this.cutoffObjs[curCutoffId]) { continue; }

						var curSlotObj = this.slotObjs[curSlotId];
						
						var curCORow = this.cutoffObjs[curCutoffId].ext.up('tr');

						if (t === reorgStart) {
							//based on name, get contentId if needed
							//check if we already seeked it
							var curContentExt = null;
							if (curSlotObj.contentId === null) {
								//we haven't, do so now
								var curContentId;
								if (curSlotObj.ext.getAttribute('name') != null && curSlotObj.ext.getAttribute('name').indexOf('_') != -1) {
									curContentId = curSlotObj.ext.getAttribute('name').split('_');
									var tempId = curContentId[1];
									if (tempId !== '') {
										if ($(tempId+'C')) { tempId = tempId+'C'; } //if C/E versions exist, use them

										curContentExt = $(tempId);

										if (curContentExt) {
											//we have a holiday or cutoffpassed
											curSlotObj.ext.innerHTML = curContentExt.innerHTML;
											//set in slot so we don't have to seek again
											curSlotObj.contentId = tempId;
										}
									}
								}else{
									curContentId = this.opts.noDeliveryCId;
									//no holiday, try no delivery (this is the default)
									curContentExt = $(curContentId);
									if (curContentExt) {
										//we have no delivery
										curSlotObj.ext.innerHTML = curContentExt.innerHTML;
										//set in slot so we don't have to seek again
										curSlotObj.contentId = curContentId;
									}
								}
							}else{
								//we have it, change to contracted and proceed like normal
								var curContentId = curSlotObj.contentId;
								if (curSlotObj.contentId.slice(curSlotObj.contentId.length-1) === 'E') {
									curContentId = curSlotObj.contentId.slice(0, -1)+'C';
								}
								curContentExt = $(curContentId);
								if (curContentExt) {
									curSlotObj.ext.innerHTML = curContentExt.innerHTML;
									//set in slot so we don't have to seek again
									curSlotObj.contentId = curContentId;
								}
							}
							//replace out className
							if (curSlotObj.contentId === this.opts.noDeliveryCId) {
								curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGC','tsContainerNoDelBGC');
								curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGE','tsContainerNoDelBGC');
								curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerNoDelBGE','tsContainerNoDelBGC');
							}
							curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGE','tsContainerBGC');
							
							//check extend queue
							this.checkQueue(this.convertId(curSlotId, 'dayPartId'));

							var tempNegSubt = 0;
							//check if we're hiding the whole day, if so, hide day part, and mod negative subtraction to accommodate
							if (reorgStart === 0 && reorgEnd === dayObj.TSs.length-1) {
								dayObj.dayPartExt.up('tr').hide();
								tempNegSubt = -2;
							}
							if (this.opts.IEver !== -1) { tempNegSubt++; }

							//reset height
							curSlotObj.ext.style.height = this.getCalcdRowHeight(sequenceCount, null, false, tempNegSubt);
						}else{
							//hide the rest of the trs (not the tds)
							if (!curSlotObj.parentExt) {
								var curId = this.convertId(curSlotObj.ext.id, 'tsPId');
								if (curId !== -1) {
									var curSlotObjParentExt = $(curId);
									if (curSlotObjParentExt) {
										curSlotObjParentExt.hide();
										//add to slot obj for later use
										curSlotObj.parentId = curSlotObjParentExt.id;
										curSlotObj.parentExt = curSlotObjParentExt;
									}
								}
							}else{
								curSlotObj.parentExt.hide();
							}
						}
					}
				}
			}

			return true;
		}
	/* unorganize a specific day by dayId (undoes reorganizeDay) */
		this.unorganizeDay = function (dayId) {
			var sequenceCount;
			if (!this.dayObjs[dayId]) { return; }
			
			//check queue
			this.checkQueue(dayId);
			this.checkQueue(this.convertId(dayId, 'dayPartId'));
			
			//show dayPart
			this.dayObjs[dayId].dayPartExt.up('tr').show();

			var reorgData = this.dayObjs[dayId].reorgData;
			if (this.dayObjs[dayId].isExpanded) {
				reorgData = [[0, this.dayObjs[dayId].TSIds.length-1]];
			}
			for (var r = 0; r < reorgData.length; r++) {
				var reorgStart = reorgData[r][0];
				var reorgEnd = reorgData[r][1];
				sequenceCount = (reorgEnd-reorgStart)+1;

				//loop through slots being used in reorg set
				for (var t = reorgStart; t <= reorgEnd; t++) { //<= so we unhide all
					var curSlotId = this.dayObjs[dayId].TSIds[t];
					var curCutoffId = this.dayObjs[dayId].COIds[t];

					//check extend queue (shouldn't be in queue now, since it's unorg)
					this.checkQueue(curSlotId);
					this.checkQueue(curCutoffId);

					if (!this.slotObjs[curSlotId]) { continue; }
					if (!this.cutoffObjs[curCutoffId]) { continue; }

					var curSlotObj = this.slotObjs[curSlotId];
					var curCutoffObj = this.cutoffObjs[curCutoffId];
					
					var curTSRow = curSlotObj.ext.up('tr');
					var curCORow = curCutoffObj.ext.up('tr');

					//reset cutoff lines
					if (curCORow.down('.cutoffLR')) {
						curCORow.down('.cutoffLR').className = curCORow.down('.cutoffLR').className.replace('cutoffLR', 'cutoff');
					}
					
					if (this.dayObjs[dayId].isExpanded) {
						curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerNoDelBGE','tsContainerBGC');
						curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGE','tsContainerBGC');
						curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerE','tsContainerC');

						//make sure it's vis
						curTSRow.show();

						//reset it's bottom line
						curSlotObj.ext.style.borderBottom = '1px solid #ccc';

						//hide co's
						//hide all non empty cutoffs
						if (curCORow.down('.cutoffDispChild')) {
							curCORow.hide();
						}
						//show 0 cutoff if we're coming from an expanded day
						if (t === reorgStart) {
							if (curCORow.down('.cutoffDispChild')) {
								curCORow.down('.cutoffDispChild').hide();
							}
							curCORow.show();

						}
					}

					if (t === reorgStart) {
						//put back innerHTML, but only if not a normal slot
						if (curSlotObj.contentId !== null) {
							curSlotObj.ext.innerHTML = this.getID(null, this.parseDay(curSlotObj.ext.id), this.parseTime(curSlotObj.ext.id), null, this.opts.emptyCellHtml);
						}
						//and reset height
						var sub;
						//subtract 1 here, so first element is right height
						curSlotObj.ext.style.height = this.getCalcdRowHeight(1, null, false, 1);
						//reset classname
						if (curSlotObj.contentId === this.opts.noDeliveryCId) {
							curSlotObj.ext.className = curSlotObj.ext.className.replace('tsContainerBGE','tsContainerBGC');
						}
					}else{
						//unhide the rest of the trs
						if (!curSlotObj.parentExt) {
							var curId = this.convertId(curSlotObj.ext.id, 'tsPId');
							if (curId !== -1) {
								var curSlotObjParentExt = $(curId);
								if (curSlotObjParentExt) {
									//and reset height
									var sub;
									//subtract 1 here, so first element is right height
									curSlotObj.ext.style.height = this.getCalcdRowHeight(1, null, false, 1);
									curSlotObjParentExt.show();
									//add to slot obj for later use
									if (!curSlotObj.parentId) { curSlotObj.parentId = curSlotObjParentExt.id; }
									if (!curSlotObj.parentId) { curSlotObj.parentExt = curSlotObjParentExt; }
								}
							}
						}else{
							//and reset height
							var sub;
							//subtract 1 here, so first element is right height
							curSlotObj.ext.style.height = this.getCalcdRowHeight(1, null, false, 1);
							curSlotObj.parentExt.show();
						}
					}
				}
			}
			return true;
		}

	/* expand/contract setters */
		/* set day as expanded */
			this.setDayAsExpanded = function(dayIdArg) {
				var prevExpandedDayId = this.opts.expandedDayId;
				var dayId = dayIdArg || '';

				if (this.dayObjs[dayId] && !this.dayObjs[dayId].isExpanded && prevExpandedDayId !== dayId) {
					//check extend queue
					this.checkQueue(dayId);
					this.checkQueue(this.dayObjs[dayId].hC);
					this.checkQueue(this.dayObjs[dayId].hE);
					this.checkQueue(this.dayObjs[dayId].fE);

					//set day as expanded
					this.dayObjs[dayId].isExpanded = true;
					this.opts.expandedDayId = dayId;

					var fdTSDisplay = this; //ref to fdTSDisplay
					var rowRefId = this.rowObjs[this.convertId(dayId, 'rowId')];
					var cssString = this.getCssRefString(rowRefId.eRefId);
					var expandDuration = this.opts.expandDuration / 1000;
					var appearDuration = this.opts.appearDuration / 1000;

					var beforeStartFunc = function () {

						fdTSDisplay.dayObjs[dayId].ext.morph(cssString, { duration: expandDuration });
						fdTSDisplay.dayObjs[dayId].ext.hide();
						fdTSDisplay.reorganizeDay(dayId);
						fdTSDisplay.dayObjs[dayId].ext.style.width = fdTSDisplay.opts.beforeExpandWidth;
						
						fdTSDisplay.dayObjs[dayId].hCext.fade({duration: appearDuration/10});
						fdTSDisplay.dayObjs[dayId].hCext.hide();

						fdTSDisplay.dayObjs[dayId].hEext.style.width = fdTSDisplay.opts.beforeExpandWidth;
						fdTSDisplay.dayObjs[dayId].hEext.appear({duration: appearDuration});
						fdTSDisplay.dayObjs[dayId].hEext.morph(cssString, { duration: (expandDuration) });
						
						fdTSDisplay.dayObjs[dayId].fEext.style.width = fdTSDisplay.opts.beforeExpandWidth;
						fdTSDisplay.dayObjs[dayId].fEext.morph(cssString, { duration: (expandDuration) });
						fdTSDisplay.dayObjs[dayId].fEext.appear({duration: appearDuration});
					}
					var afterFinishFunc = function () {
						fdTSDisplay.dayObjs[dayId].hEext.down('.tsHeadE').appear({duration: (appearDuration)});
						fdTSDisplay.dayObjs[dayId].ext.appear({duration: appearDuration});
					}

					this.dayObjs[dayId].ext.up(0).morph(cssString, {
						duration: (expandDuration),
						beforeStart: beforeStartFunc,
						afterFinish: afterFinishFunc
					});
				}

				//unset previous as expanded
				if (prevExpandedDayId !== null && prevExpandedDayId !== dayId) {
					this.setDayAsContracted(prevExpandedDayId);
				}

				//call cleanup jik
				if (this.opts.cleaner === null) {
					this.cleanup();
				}else{
					clearInterval(this.opts.cleaner);
					this.opts.cleaner = null;
					this.cleanup();
				}

				return true;
			}
		/* set day as contracted */
			this.setDayAsContracted = function(dayIdArg) {
				var dayId = dayIdArg || '';
				if (this.dayObjs[dayId]) {
					
					this.dayObjs[dayId].isExpanded = false;

					var fdTSDisplay = this; //ref to fdTSDisplay
					var rowRefId = this.rowObjs[this.convertId(dayId, 'rowId')];
					var cssString = this.getCssRefString(rowRefId.cRefId);
					var contractDuration = this.opts.contractDuration / 1000;
					var appearDuration = this.opts.appearDuration / 1000;


					var beforeStartFunc = function () {
						//set this so we can unorg
						fdTSDisplay.dayObjs[dayId].isExpanded = true;
						fdTSDisplay.unorganizeDay(dayId);
						fdTSDisplay.dayObjs[dayId].isExpanded = false;
						fdTSDisplay.reorganizeDay(dayId);

						fdTSDisplay.dayObjs[dayId].hEext.hide();
						fdTSDisplay.dayObjs[dayId].hCext.style.width = fdTSDisplay.opts.hC_beforeContractWidth;
						fdTSDisplay.dayObjs[dayId].hCext.show();
						//fdTSDisplay.hEext.morph(cssString, { duration: (appearDuration) });
						fdTSDisplay.dayObjs[dayId].hEext.down('.tsHeadE').hide();
						fdTSDisplay.dayObjs[dayId].fEext.hide();
						fdTSDisplay.dayObjs[dayId].hCext.morph(cssString, {
							duration: (contractDuration)
						});
						fdTSDisplay.dayObjs[dayId].ext.morph(cssString, {
							duration: (contractDuration)
						});
					}

					var afterFinishFunc = function () {
						fdTSDisplay.dayObjs[dayId].hEext.hide();
						fdTSDisplay.dayObjs[dayId].hCext.show();
					}


					//this is the main contract visual
					this.dayObjs[dayId].ext.up().morph(cssString, {
						duration: (contractDuration),
						beforeStart: beforeStartFunc,
						afterFinish: afterFinishFunc
					});
					
				}

				return true;
			}

	/* toggle className by changing final 'C' to 'E' or vice-versa */
		this.toggleClassName = function (elemWithClassNameArg, origClassNameArg) {
			var origClassName = origClassNameArg || '';
			var elemWithClassName = elemWithClassNameArg || null;

			if (
				origClassName === '' || !elemWithClassName || 
				!elemWithClassName.className || elemWithClassName.className.indexOf(origClassName) === -1
			) { return; }

			var tempStr = origClassName.slice(0, -1); 
			if (origClassName.endsWith('C')) {
				elemWithClassName.className = elemWithClassName.className.replace(origClassName, tempStr+'E');
			}else if (origClassName.endsWith('E')) {
				elemWithClassName.className = elemWithClassName.className.replace(origClassName, tempStr+'C');
			}
		}

	/* cleanup function, in case any of the transitions get 'stuck' */
		this.cleanup = function() {
			var fdTSDisplay = this; //ref to fdTSDisplay

			var cleaner = function() {
				fdTSDisplay.log('Running cleaner...');
				for (var dayId in fdTSDisplay.dayObjs) {
					
					//check extend queue
					fdTSDisplay.checkQueue(dayId);
					fdTSDisplay.checkQueue(fdTSDisplay.dayObjs[dayId].hC);
					fdTSDisplay.checkQueue(fdTSDisplay.dayObjs[dayId].hE);
					fdTSDisplay.checkQueue(fdTSDisplay.dayObjs[dayId].fE);

					var dayObj = fdTSDisplay.dayObjs[dayId];

					if (dayObj.ext) {
						if (!dayObj.isExpanded && (dayObj.ext.up('td').getWidth() !== parseInt(fdTSDisplay.opts.beforeExpandWidth.replace('px', '')) || dayObj.hEext.style.display != 'none' || dayObj.fEext.style.display != 'none')) {
							fdTSDisplay.log(dayId, 'con cleaning...');
							while (!fdTSDisplay.setDayAsContracted(dayId)){}
						}
						if (dayObj.isExpanded && (dayObj.ext.up('td').getWidth() !== parseInt(fdTSDisplay.opts.hC_beforeContractWidth.replace('px', '')) || dayObj.hCext.style.display != 'none' || dayObj.fEext.style.display === 'none')) {
							fdTSDisplay.log(dayId, 'exp cleaning...');
							while (!fdTSDisplay.setDayAsExpanded(dayId)){}
						}
					}
				}
				fdTSDisplay.log('...cleaner finished.');
				clearInterval(fdTSDisplay.opts.cleaner);
				fdTSDisplay.opts.cleaner = null;
				
			}
			fdTSDisplay.log('Cleaner will run in '+(fdTSDisplay.opts.cleanerWaitTime/1000)+' secs..');
			fdTSDisplay.opts.cleaner = setInterval(cleaner, fdTSDisplay.opts.cleanerWaitTime);
		}
	
	/* prem.slots: called when they go unavailable */
		this.premGoUnavail = function() {
			window.clearInterval(this.opts.premSlotsClock);
			var curSlotId = this.dayObjs[this.opts.premSlotsDayId].TSIds[0];
			var curSlotObj =  this.slotObjs[curSlotId];
			curSlotObj.contentId = 'CPday'+curSlotObj.contentId.slice(curSlotObj.contentId.length-1);
			curSlotObj.ext.innerHTML = $(curSlotObj.contentId).innerHTML;
			
			$$('.'+window.fdTSDisplay.opts.premSlotsTimerElemClass).each(function(e, i) {
				$(e).innerHTML = 'UNAVAILABLE';
			});

			$(this.opts.premSlotsTimerElem_msg).innerHTML = 'Choose Another Day'; //expanded
			//loop through ts's and disable radio button (if they exist)
			for (var slotIdIndex in this.dayObjs[this.opts.premSlotsDayId].TSIds) {
				var slotId = this.dayObjs[this.opts.premSlotsDayId].TSIds[slotIdIndex];
				if (slotId && this.slotObjs[slotId] && this.slotObjs[slotId].hasOwnProperty('radioExt') && this.slotObjs[slotId].radioExt !== null) {
					//remove selected color
					this.slotObjs[slotId].ext.className = curSlotObj.ext.className.replace('tcSelectionBGC','');
					this.slotObjs[slotId].ext.className = curSlotObj.ext.className.replace('tcSelectionBGE','');
					//remove selection
					this.slotObjs[slotId].radioExt.checked = false;
					//disable it
					this.slotObjs[slotId].radioExt.disabled = true;
				}
			}
		}

	/* --- SETUP --- */
	
	this.log('Starting '+this.refId+'...');

	/* add size() to Objects to make life easier */
	if (!Object.hasOwnProperty('size')) {
		Object.size = function(obj) {
			var size = 0, key;
			for (key in obj) {
				if (obj.hasOwnProperty(key)) size++;
			}
			return size;
		};
	}

	/* add plain text method */
	if (!Element.hasOwnProperty('getText')) {
		Element.addMethods({
			getText: function(element) {
				element = $(element);
				return element.innerHTML.strip().stripTags().replace(/\n/g,' ').replace(/\s+/g,' ');
			}
		});
	}

	/* detect IE version */
		this.detectIEVersion();

	/* setup TSDisplay */
		this.startTimer();
			this.log('Starting constructor...');
				this.construct();
			this.log('...constructor completed.');
		this.endTimer();

	/* reorganize */
		this.startTimer();
			this.log('Reorganizing...');
				//any non-extended slots will be extended in here
				this.reorganize();
			this.log('...reorganizer done.');
		this.endTimer();

	/* add events */
		this.startTimer();
			this.log('Adding events...');
				this.addEvents();
			this.log('...events added.');
		this.endTimer();

	this.log('...'+this.refId+' ready.');
	
	return this;
}

//row object
	function Row(idArg) {
		this.id = idArg || '';
		this.ext = null;
		this.dayIds = [];
		this.eRefId = '';
		this.cRefId = '';
	}

//day object
	function Day(idArg) {
		this.id = idArg || ''; //day id as html elemId
		this.ext = null; //extend check
		this.TSs = []; //timeslots as refData
		this.TSIds = []; //timeslots as html elemId
		this.COs = []; //cutoffs as refData
		this.COIds = []; //cutoffs as html elemId
		this.reorgData = []; //indexes of TSIds
		this.dayPart = -1; //dayPart index
		this.dayPartExt = null; //dayPart extended
		this.hC = null; //header contracted as html elemId
		this.hCext = null; //header contracted extended
		this.hE = null; //header expanded as html elemId
		this.hEext = null; //header expanded extended
		this.fE = null; //footer expanded as html elemId
		this.fEext = null; //footer expanded extended
		this.isExpanded = false; //is currently expanded
		this.parentId = null; //parent (row) ease key
		this.showPremium = false; //show prem.slots
		this.showDpTc = false; //show DP T & C
		this.lastCO = null; //last cutoff as html elemId
		this.customEvents = {}; //additional events to be set from outside

		$jq.extend(this, new customTSEvents());
	}

//timeslot object
	function Slot(idArg) {
		this.id = idArg || ''; //slot id as html elemId
		this.ext = null; //extend check
		this.isChecked = false; //is currently checked
		this.radio = null; //radio
		this.radioExt = null; //radio extended
		this.contentId = null; //elemId for reorg content
		this.customEvents = {}; //additional events to be set from outside

		$jq.extend(this, new customTSEvents());
	}

//events obj
/* add this to an object to add event handles:
 *		$jq.extend(this, new customTSEvents());
 */
	function customTSEvents() {

		this.addCustomEvents = function(argsObj) {
			if ('events' in argsObj) {
				for (var i=0; i < argsObj.events.length; i++) {
					this.addCustomEvent(argsObj.events[i]);
				}
			}
		}
		
		this.addCustomEvent = function (argsObj) {
			if ('event' in argsObj && 'func' in argsObj) {
				var eName = argsObj.event;
				if (!'customEvents' in this) {
					this.customEvents = {};
				}
				if (!this.customEvents.hasOwnProperty(eName)) {
					this.customEvents[eName] = {funcs: []};
				}
				
				if (eName in this.customEvents) {
					var funcCall = argsObj.func;
					var funcParams =  {'thisObj': this};
					if ('params' in argsObj) {
						$jq.extend(funcParams, argsObj.params);
					}
					this.customEvents[eName].funcs.push({func: funcCall, params: funcParams});
				}
			}
		}

		this.handleCustomEvents = function (argsObj) {
			if ('event' in argsObj) {
				var eName = argsObj.event;
				if (eName in this.customEvents) {
					for (var i=0; i < this.customEvents[eName].funcs.length; i++) {
						var funcCall = this.customEvents[eName].funcs[i].func;
						var funcParams =  {};
						if ('params' in $jq.extend(funcParams, this.customEvents[eName].funcs[i].params)) {
							$jq.extend(funcParams, this.customEvents[eName].funcs[i].params);
						}
						funcCall(funcParams);
					}
				}
			}
		}
	}

//cutoff object
	function Cutoff(idArg) {
		this.id = idArg || '';
		this.ext = null;
	}


/* initializer array */
var fdTSDisplayInitializeFuncs = window['fdTSDisplayInitializeFuncs'] || [];


/* initialize the TS display */
	document.observe('dom:loaded', function() {
		if (initializeTS()) {

			if (!window.dayIndex) {
				dayIndex = 0;
			}
			if (!window.slotIndex) {
				slotIndex = 0;
			}
			defaultColumnExpandNew(dayIndex, slotIndex);
		}
	});

	function initializeTS(refIdArg) {
		var refId = refIdArg || 'fdTSDisplay';
			
		window[refId] = new fdTSDisplay(refId);
		
		if (window[refId].hasOwnProperty('refId')) {
			return true;
		}

		window[refId] = null;
		return false;
	}

/* expand a day and check the radio */
	function defaultColumnExpandNew(dayIndex, slotIndex) {
		if (window.fdTSDisplay) {
			var fdTSDisplay = window.fdTSDisplay;
			
			if (fdTSDisplay.opts && !fdTSDisplay.opts.timeSlotInfo) {
				/* click radio */
				if (fdTSDisplay.slotObjs[fdTSDisplay.getID('tsId', dayIndex, slotIndex)] && fdTSDisplay.slotObjs[fdTSDisplay.getID('tsId', dayIndex, slotIndex)].radioExt) {
					fdTSDisplay.slotObjs[fdTSDisplay.getID('tsId', dayIndex, slotIndex)].radioExt.click();
				}else{
					//passed indexes have no radio, just expand
					fdTSDisplay.setDayAsExpanded(fdTSDisplay.getID('dayId', dayIndex));
				}
			}else{
				/* just expand day */
				fdTSDisplay.setDayAsExpanded(fdTSDisplay.getID('dayId', dayIndex));
			}
		}
		return;
	}
	

	//ignore old function
	function defaultColumnExpand() { return }

/* hide ao */
	function hideAdvanceOrder() { 
		$('timeslots_gridAdv').toggle();

		if($('timeslots_gridAdvNew')){
			$('timeslots_gridAdvNew').toggle();
		}

		if($('timeslots_gridAdv').style.display !== 'none') {
			$('displayAdvanceOrderGrid').innerHTML = "Hide Delivery Timeslots";
		}else{
			$('displayAdvanceOrderGrid').innerHTML = "Show Delivery Timeslots";
		}
	}

/* cm logger*/
	function logChange(timeSlotId){	
		if (timeslotChooserFunctions.hasOwnProperty(timeSlotId)) {
			timeslotChooserFunctions[timeSlotId]();
		}
	}
	
	function checkPremiumSlot(deliveryTimeSlotId, formId){
		
		new Ajax.Request('/checkout/check_premium_slot.jsp', {
			parameters: {
				deliveryTimeSlotId: deliveryTimeSlotId						
			},
			onComplete: function(transport) {
				checkPremiumSlotPopup(transport.responseText, formId);
			}
		});	
		return false;
	}

	function checkPremiumSlotPopup(JSONstring, formId) {
		var params = JSONstring.evalJSON(true);	
		var url = '/overlays/variable_minnotmet_popup.jsp?amt='+params.minorderamt;
		if (!(params.minordermet)) {
			$jq(document).ready(function() {
				vmo_overlayDialog = doOverlayDialogWithSpinner(url);
				$jq('#choosenewtimeslot').live('click', function(e) { e.preventDefault(); vmo_overlayDialog.dialog('close'); });

			});
		} else{
			document.forms[formId].submit();
		}
	}
