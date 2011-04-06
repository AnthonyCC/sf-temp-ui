var styleStrExp = '';
var styleStrCon = '';
var globalTS = -1;
var expandDuration = 0; //seconds
var appearDuration = 0.8; //seconds
var contractDuration = 1; //seconds
var timedExpandWait = 500; //ms
var global_tsDebug = false;
var tsdebugTimeDif = 0;

//morph effect will match each of these styles from ref elem.
var styleArr = [];
styleArr[styleArr.length] = 'width';
styleArr[styleArr.length] = 'background-color';
styleArr[styleArr.length] = 'border-right';

function buildStyleStr(ref) {
	if (!$(ref)) { return ''; }

	var styleString = '';
	for (var i=0; i < styleArr.length; i++)	{
		if ($(ref).getStyle) {
			if ($(ref).getStyle(styleArr[i]) != '') {
				styleString += styleArr[i];
				styleString += ':';
				styleString += $(ref).getStyle(styleArr[i]);
				styleString += ';';
			}
		}
	}

	return styleString;
}

function parseDay(elemId) {
	if (elemId === undefined || elemId === '' || elemId === null) {
		return -1;
	}else{
		elemId = elemId.split('_');
		if (elemId.length >= 2) {
			return elemId[1].replace('d', '');
		}else{
			return -1;
		}
	}
}

/* check object to see if it's an array */
function isArray(obj) {
	if (obj.constructor.toString().indexOf("Array") == -1) {
		return false;
	} else {
		return true;
	}
}

/* get lower bound of an array, ignoring undefined. return -1 on fail */
function getLBound(array) {
	if (isArray(array)) {
		for (var i = 0; i < array.length; i++) {
			if (array[i] !== undefined) {
				return i;
			}
		}
	}
	return -1;
}

//fill ref data
function fillRef(refDataCur, startIndex) {
	var day = startIndex;
	var dayPartIndexCur = daypartIndex;
	var reorganizer = []; //hold elems to be reorganized
	if (day >= 10 && window.refAdvData !== undefined) {
		dayPartIndexCur = daypartAdvIndex;
	}
	if (day >= 20 && window.refAdvData !== undefined) {
		dayPartIndexCur = daypartAdvNewIndex;
	}

	//check if refData is empty
	if (refDataCur.join("").replace(/,/g,"") === "") {
		//we have no slots. check daypartIndex
		if (dayPartIndexCur == 1 && refDataCur[day].length == 2) {
			//no slots, dayPartIndex is 1, redData has 2 slots, assume an empty day
			//put in empty strings
			for (var d = 0; d < refDataCur.length; d++) {
				if (refDataCur[d] === undefined) { continue; }
				for (var k=0; k < refDataCur[d].length; k++) {
					var t = 0;
					while (refDataCur[d][k].length < 8) {
						refDataCur[d][k][t] = "";
						t++;
					}
				}
			}
			//reset daypartIndex to 4
			if (day >= 10) {
				daypartAdvIndex = 4;
				dayPartIndexCur = 4;
			}else if (day >= 20) {
				daypartAdvNewIndex = 4;
				dayPartIndexCur = 4;
			}else{
				daypartIndex = 4;
				dayPartIndexCur = 4;
			}
			if (day >= 10) { day = 9999; }

		}

		while (!fillRefAddRows(refDataCur, startIndex)) {}
		reorganizer = getReorganizerData(refDataCur, dayPartIndexCur, day);
	}

	//call reorg
	while (!tsReorganizer(reorganizer)) {}

	return true;
}

/* this function filles in a table according to refData
 *	it should add missing rows/cells withe matching ids
 *	it will also move the am/pm split to the appropriate index
 */
function fillRefAddRows(refDataCur, startIndexArg) {
	
	var rowIndex = 0;
	var cellIndex = 0;
	var newRow;
	var newCell;
	var daypartRow = -1;
	var dayPartInfo = []; //use to store info for daypart row
	var startIndex = startIndexArg || 0;
	if (refDataCur === undefined) { return true; }

	//check header ref so we know we have a table to act on

	//loop through table and add rows where they're needed to match refData
	for (var d = 0; d < refDataCur.length; d++) {
		
		if (refDataCur[d] === undefined) { continue; }
		
		var advId = '';
		var dayPartIndexCur = daypartIndex;
		/* only recalc dayPartIndexCur in here if it's missing */
		if (startIndex >= 10) {
			advId = 'Adv';
			dayPartIndexCur = daypartAdvIndex;
			if (dayPartIndexCur == -1) { dayPartIndexCur = 4; }
		}
		if (startIndex >= 20) {
			advId = 'Adv';
			dayPartIndexCur = daypartAdvNewIndex;
			if (dayPartIndexCur == -1) { dayPartIndexCur = 4; }
		}
		//get table ref
		var node = $('ts_d'+d+'_tsTable');
		if (!node) { continue; }

		//find daypart td in table
		var tRows = node.getElementsByTagName('TR');
		for (var tr = 0; tr < tRows.length; tr++) {
			if (tRows[tr].id != null && tRows[tr].id == 'day'+advId+'_part'+d+'_row') {
				daypartRow = tr;

				//store it's info so we can add it back to the correct spot
				dayPartInfo['tr_id'] = tRows[tr].id;
				dayPartInfo['tr_className'] = tRows[tr].className;
				dayPartInfo['tr_name'] = tRows[tr].name;
				dayPartInfo['td_id'] = tRows[tr].lastChild.id;
				dayPartInfo['td_className'] = tRows[tr].lastChild.className;
				dayPartInfo['td_name'] = tRows[tr].lastChild.name;
				dayPartInfo['td_innerHTML'] = tRows[tr].lastChild.innerHTML;

				//and now, delete it
				node.deleteRow(tr);

				break;
			}
		}
		for (var t = 0; t < refDataCur[d][0].length; t++) {
			tRows = node.getElementsByTagName('TR');
			//check if we need a cutoff row
			if (t == 0 ) { //dayPart is not zero based
				if (!$('co'+advId+'_d'+d+'_ts'+t+'_row')) {
					//no cutoff, add it
					newRow = node.insertRow(t);
					newRow.id = 'co'+advId+'_ts'+t+'_row';
					newCell = newRow.insertCell(cellIndex);
					newCell.innerHTML = '<div id="co_d'+d+'_ts'+t+'">&nbsp;</div>';
					newCell.className = 'cutoff';
				}
			}
			
			//skip if row already exists
			if ($('ts_d'+d+'_ts'+t)) {
				continue;
			}
			newRow = node.insertRow(tRows.length);
			newRow.id = 'ts_d'+d+'_ts'+t+'_tr';
			newCell = newRow.insertCell(cellIndex);
			newCell.innerHTML = '<div class="tsContent "><div class="fleft ts_rb" id="ts_d'+d+'_ts'+t+'_rbCont"></div><div class="fleft tsCont " id="ts_d'+d+'_ts'+t+'_time" style="width: 80px;">&nbsp;</div></div>';
			newCell.className = 'tsCol tsContainerC tsContainerBGC';
			newCell.id = 'ts_d'+d+'_ts'+t;
		}

		//now reinsert daypart row
		newRow = node.insertRow(dayPartIndexCur+2); //+1 for cutoff, +1 for zero based
		newRow.id = dayPartInfo['tr_id'];
		newRow.className = dayPartInfo['tr_className'];
		newRow.name = dayPartInfo['tr_name'];

		newCell = newRow.insertCell(cellIndex);
		newCell.id = dayPartInfo['td_id'];
		newCell.className = dayPartInfo['td_className'];
		newCell.name = dayPartInfo['td_name'];
		newCell.innerHTML = dayPartInfo['td_innerHTML'];
	}

	return true;
}

/* pass in an array, index to start at, and index to stop at
 *	returns when an undefined it hit
 *	(counts items between start and undefined)
 */
function seekUntilUndef(arrayArg, startIndexArg, indexLimitArg) {
	var array = arrayArg || [];
	var startIndex = startIndexArg || 0;
	var indexLimit = indexLimitArg || array.length-1;
	var count = 0;
	if (array[startIndex] !== undefined) {
		for (var i = startIndex; startIndex < indexLimitArg; i++) {
			if (array[i] === undefined) {
				return count;
			}
			count++;
		}
	}
	return count;
}

var addRows = true;
var addRowsAdv = true;

//one function to rule them all
function solveDisplay(elemId, autoCheckRadioArg) {
	
	var autoCheckRadio = true;
	if (autoCheckRadioArg !== undefined) { autoCheckRadio = autoCheckRadioArg; }

	var day = parseDay(elemId); //should now be the day index

	var d = 0, t = 0, i = 0;

	var sequenceKey = -1;
	var hasSpace = false;
	
	var refDataCur = refData;
	var dayPartIndexCur = daypartIndex;
	var advId = '';
	//we'll need these two further down...
	var refDataCurTemp1 = [];
	var refDataCurTemp2 = [];
	if (day >= 10 && window.refAdvData !== undefined) {
		refDataCur = refAdvData;
		advId = 'Adv';
		dayPartIndexCur = daypartAdvIndex;

		if (refDataCur[10] !== undefined && refDataCur[20] !== undefined) {
			//split off multiple ao sets
			var endIndex = 10+seekUntilUndef(refAdvData, 10, 19);
			refDataCurTemp1 = refAdvData.slice(10, endIndex);
			refDataCur = [];
			var indexCount = 10;
			for (var i = 0; i < refDataCurTemp1.length; i++) {
				refDataCur[indexCount] = refDataCurTemp1[i];
				indexCount++;
			}
			//reset to corrected format for tsReorganizer
			refDataCurTemp1 = refDataCur;
			tsLog('part 1 '+refDataCur.length);
			
			//if rows haven't been added already, do so now
			if (addRowsAdv && refDataCur.join("").replace(/,/g,"") === "") {
				var startIndex = 1;
				if (advId !== '') { startIndex = 10; }
				if (addRowsAdv) {
					while (!fillRef(refDataCur, startIndex)) {}
					//make sure we have this
					dayPartIndexCur = daypartAdvIndex;
				}
			}

			//split off multiple ao sets
			refDataCurTemp2 = refAdvData.slice(20);
			refDataCur = [];
			var indexCount = 20;
			for (var i = 0; i < refDataCurTemp2.length; i++) {
				refDataCur[indexCount] = refDataCurTemp2[i];
				indexCount++;
			}
			//reset to corrected format for tsReorganizer
			refDataCurTemp2 = refDataCur;
			tsLog('part 2 '+refDataCur.length);

			
			//if rows haven't been added already, do so now
			if (addRowsAdv && refDataCur.join("").replace(/,/g,"") === "") {
				var startIndex = 1;
				if (advId !== '') { startIndex = 20; }
				if (addRowsAdv) {
					while (!fillRef(refDataCur, startIndex)) {}
					//make sure we have this
					dayPartIndexCur = daypartAdvNewIndex;
				}
			}
			
			refDataCur = refAdvData;
			addRowsAdv = false;
		}

	}

	//if rows haven't been added already, do so now
	if ((addRows || addRowsAdv) && refDataCur.join("").replace(/,/g,"") === "") {
		var startIndex = 0;
		if (advId !== '') { startIndex = 10; }
		if (addRows && startIndex < 10) {
			while (!fillRef(refDataCur, startIndex)) {}
			addRows = false;
		}
		if (addRowsAdv && startIndex >= 10) {
			while (!fillRef(refDataCur, startIndex)) {}
			addRowsAdv = false;
			//make sure we have this
			dayPartIndexCur = daypartAdvIndex;
		}
	}

	tsLog('starting refData navigate');
	//setup vars
	var tsHc = null;
	var tsHe = null;
	var tsHeader = null;
	var daypart = null;
	var daypartParentId = null;
	var tsFc = null;
	var tsFE = null;
	var tsId = null;
	var coId = null;
	var coRowId = null;
	var tsRbCont = null;
	var tsRb = null;
	var tsForceX = null;
	var tsMsgC = null;
	var tsMsgE = null;
	var tsMsgAre = null;
	var tsTime = null;

	//navigate refDataCur matrix
	tsLog('Starting refData navigate');
	for (d = 0; d < refDataCur.length; d++) {
		if (refDataCur[d] === undefined) { continue; }
		
		//extend once per day
		tsHc = $('ts_d'+d+'_ts_hC');
		tsHe = $('ts_d'+d+'_ts_hE');
		tsHeader = $('ts_d'+d+'_ts_header');
		daypart = $('day_part'+d);
		if (daypart) { daypartParentId = $(daypart.parentNode.id); }
		tsFc = $('ts_d'+d+'_ts_fC');
		tsFE = $('ts_d'+d+'_ts_fE');

		//show for current day
		if (d == day) {
			if (lastTS_expanded == -1 && (day == -1 || day == 9999)) { continue; }

			//this is our sequence ref key
			sequenceKey = d;
			//header
			//collapsed
			if (tsHc) { tsHc.hide(); }
			//expanded
			if (tsHe) { /* $('ts_d'+d+'_ts_hE').show(); */ }
			//line fix
			if (tsHeader) { tsHeader.className = 'noTopBorder'; }
			/* AM/PM split */
			if (daypart) {
				var eId = daypart.parentNode.id;
				if ($(eId)) { $(eId).hide(); }
			}

			//footer
			//collapsed
			if (tsFc) { $('ts_d'+d+'_ts_fC').hide(); }
			//expanded
			if (tsFE) { tsFE.show(); }

			for (t = 0; t < refDataCur[d][0].length; t++) {
				//extend once per timeslot
				tsId = $('ts_d'+d+'_ts'+t);
				coId = $('co_d'+d+'_ts'+t);
				coRowId = $('co'+advId+'_d'+d+'_ts'+t+'_row');
				tsRbCont = $('ts_d'+d+'_ts'+t+'_rbCont');
				tsRb = $('ts_d'+d+'_ts'+t+'_rb');
				tsForceX = $('ts_d'+d+'_ts'+t+'_forceX');
				tsMsgC = $('ts_d'+d+'_ts'+t+'_msgC');
				tsMsgE = $('ts_d'+d+'_ts'+t+'_msgE');
				tsMsgAre = $('ts_d'+d+'_ts'+t+'_msgARE');
				tsTime = $('ts_d'+d+'_ts'+t+'_time');
				//cutoffs
				if (coId) {
					coId.parentNode.className = 'cutoffDisp';
					if (coId.innerHTML === '&nbsp;'){
						coId.parentNode.className = coId.parentNode.className.replace("cutoffDisp","cutoffDispBGC");
					}
					

					if (refDataCur[d][1][t] !== undefined && refDataCur[d][1][t] != '') {
						coId.show();
						//rows
						if (coRowId) { coRowId.show(); }
					}else{
						//rows
						if (coRowId) { coRowId.hide(); }
					}
				}
				//radios
				if (tsRbCont) { tsRbCont.show(); }
				if (timeslot_info) { //timeslot_info page
					if (tsRb) { tsRb.hide(); }
				}else{ //checkout
					if (tsRb) { tsRb.show(); }
					//check the radio button on the clicked slot
					if ((elemId == 'ts_d'+d+'_ts'+t) && $(elemId+'_rb')) {
						if (autoCheckRadio) {
							$(elemId+'_rb').checked = true;
						}
					}
					if (tsId && tsRb && tsRb.checked && tsId.className.indexOf('tcSelectionBGC') == -1) {
						//and if it's not already selected
						tsId.className += ' tcSelectionBGC';
					}
				}
				
				if (tsId) {
					//background color
					tsId.className = tsId.className.replace("tsContainerC", "tsContainerE");
					tsId.className = tsId.className.replace("tsContainerBGC", "tsContainerBGE");
					tsId.className = tsId.className.replace("tsContainerNoDelBGC", "tsContainerBGE");
					//Force order fix
					if (tsForceX) { tsForceX.show(); }
					//msging
					if (tsMsgC) { tsMsgC.hide(); }
					if (tsMsgE) { tsMsgE.show(); }
					//borders
					if (t == refDataCur[d][0].length) {
						tsId.style.borderBottom = '1px solid #ccc';
					}
					//fix margins for wrapped times
					if (tsTime) {
						tsTime.style.width = '60px';
						//if (tsTime.innerHTML.length > 9) {
							//tsTime.innerHTML = tsTime.innerHTML.split(' ').join('&nbsp;');
						//}
					}
					
					/* Alcohol Restriction */
					if (tsMsgAre) {
						tsMsgAre.show();
						var children = tsMsgAre.childElements();
						if (children.length >= 1 && children[0].tagName == 'IMG') {
							children[0].show();
						}
					}
				}
			}
		}else{
			//hide others
			//header
			//collapsed
			if (tsHc) { tsHc.show(); }
			//expanded
			if (tsHe) { tsHe.hide(); }
			//line fix
			if (tsHeader) {
				tsHeader.className = '';
			}
			/* AM/PM split */
			if (daypartParentId) { daypartParentId.show(); }

			//footer
			//collapsed
			if (tsFc) { tsFc.show(); }
			//expanded
			if (tsFE) { tsFE.hide(); }

			for (t = 0; t < refDataCur[d][0].length; t++) {
				//extend once per timeslot
				tsId = $('ts_d'+d+'_ts'+t);
				coId = $('co_d'+d+'_ts'+t);
				coRowId = $('co'+advId+'_d'+d+'_ts'+t+'_row');
				tsRbCont = $('ts_d'+d+'_ts'+t+'_rbCont');
				tsRb = $('ts_d'+d+'_ts'+t+'_rb');
				tsForceX = $('ts_d'+d+'_ts'+t+'_forceX');
				tsMsgC = $('ts_d'+d+'_ts'+t+'_msgC');
				tsMsgE = $('ts_d'+d+'_ts'+t+'_msgE');
				tsMsgAre = $('ts_d'+d+'_ts'+t+'_msgARE');
				tsTime = $('ts_d'+d+'_ts'+t+'_time');

				//cutoffs
				if (refDataCur[d][1][t] !== '') {
					if (coId) { coId.hide(); }
				}
				if (coRowId) { coRowId.hide(); }
				if (coId) { coId.parentNode.className = 'cutoff'; }
				
				//background color
				if (tsId) {
					tsId.className = tsId.className.replace("tsContainerE", "tsContainerC");
					tsId.className = tsId.className.replace("tsContainerBGE", "tsContainerBGC");
				}

				//we can skip out here if ts is hidden already
				if (tsId && tsId.style.display == 'none') { continue; }

				//radios
				if (tsRbCont) { tsRbCont.hide(); }
				if (timeslot_info) { //timeslot_info page
					if (tsRbCont) { tsRbCont.hide(); }
				}else{ //checkout
					if (tsRb) { tsRb.hide(); }
					if (tsId && tsRb && tsRb.checked && tsId.className.indexOf('tcSelectionBGC') == -1) {
						//and if it's not already selected
						tsId.className += ' tcSelectionBGC';
					}
				}
				//Force order fix
				if (tsForceX) { tsForceX.hide(); }

				hasSpace = false;
				//msging
				if (tsMsgC) {
					tsMsgC.show();
					//check if ts has more space to use to display
					if ( !tsMsgC.getElementsByTagName('IMG') ) {
						hasSpace = true;
					}
				}else{
					hasSpace = true;
				}
				if (tsMsgE) { tsMsgE.hide(); }
				//borders
				if (tsId) {
					tsId.style.borderBottom = '1px solid #ccc';
					tsId.style.borderTop = '1px solid #ccc';
					//fix row heights, unless it's spanned, then assume the height is ok
					if (tsId.rowSpan === undefined || tsId.rowSpan <= 1) {
						tsId.style.height = '31px';
					}
				}

				//fix margins for wrapped times
				if (tsTime) {
					if (hasSpace) {
						if (tsMsgC) { tsMsgC.hide(); }
						tsTime.style.width = '80px';
					}else{
						tsTime.style.width = '60px';
					}
					if (tsTime.style.width == '60px' && tsTime.innerHTML.length > 9) {
						tsTime.innerHTML = tsTime.innerHTML.split(' ').join('&nbsp;');
					}
				}
				/* Alcohol Restriction */
				if (tsMsgAre) {
					tsMsgAre.hide();
					var children = tsMsgAre.childElements();
					if (children.length >= 1 && children[0].tagName == 'IMG') {
						children[0].hide();
					}
				}
				
			}
		}
	}
	tsLog('Ending refData navigate');

	var lbnd = getLBound(refDataCur);
	var sequenceKeyTemp = sequenceKey;
	if (sequenceKeyTemp == -1) {
		sequenceKeyTemp = lbnd;
	}

	for (i = 0; i < 2; i++) {
		advId = (i) ? 'Adv' : '';
		
		
		for (d = 0; d < refDataCur.length; d++) {
			//always add borders for outer cutoffs, and show row 0
			if ($('co'+advId+'_d'+d+'_ts0_row')) {
				if (d != day) {
					$('co'+advId+'_d'+d+'_ts0_row').show();
					//reset table height to collapsed height
					if (refDataCur[d] !== undefined) {
						$('co'+advId+'_d'+d+'_ts0_row').getOffsetParent().style.height = getCalcdRowHeight(refDataCur[d][0].length, null, true, 1);
					}
				}
				if (
					$('co'+advId+'_d'+d+'_ts0_row').childElements().length >= 1 && 
					$('co'+advId+'_d'+d+'_ts0_row').childElements()[0].className == 'cutoff' && 
					$('co'+advId+'_d'+d+'_ts0_row').childElements()[0].style.display == 'none'
				) {
					$('co'+advId+'_d'+d+'_ts0_row').childElements()[0].show();
					$('co'+advId+'_d'+d+'_ts0_row').childElements()[0].style.height = '27px';
				}
			}
		}

		//do the same for the opposite
		for (t = 0; t < refDataCur[sequenceKeyTemp][1].length; t++) {
			if ($('co_d'+sequenceKeyTemp+'_ts'+t)) {
				if ($('co_d'+sequenceKeyTemp+'_ts'+t)) {
					$('co_d'+sequenceKeyTemp+'_ts'+t).parentNode.style.borderLeft = '1px solid #ccc';
				}
				if ($('co_d'+(refDataCur.length-1)+'_ts'+t)) {
					$('co_d'+(refDataCur.length-1)+'_ts'+t).parentNode.style.borderRight = '1px solid #ccc';
				}
			}
		}

		if (sequenceKeyTemp == 10 && refDataCur[20] !== undefined) {
			//do the second ao as well, so we're doing set01's end and set02's start
			sequenceKeyTemp = 20;
			var endIndex = 10+seekUntilUndef(refAdvData, 10, 19);

			for (t = 0; t < refDataCur[sequenceKeyTemp][1].length; t++) {
				if ($('co_d'+sequenceKeyTemp+'_ts'+t)) {
					if ($('co_d'+sequenceKeyTemp+'_ts'+t)) {
						$('co_d'+sequenceKeyTemp+'_ts'+t).parentNode.style.borderLeft = '1px solid #ccc';
					}
					if ($('co_d'+(endIndex-1)+'_ts'+t)) {
						$('co_d'+(endIndex-1)+'_ts'+t).parentNode.style.borderRight = '1px solid #ccc';
					}
				}
			}

		}
		
	}

	var reorganizer = []; //hold elems to be reorganized
	var reorganizer1 = []; //hold elems to be reorganized
	var reorganizer2 = []; //hold elems to be reorganized
	var emptyDay = false;

	tsLog('starting getReorganizerData');
	
	if (day == '9999' && refDataCur[10] !== undefined && refDataCur[20] !== undefined) {
		reorganizer1 = getReorganizerData(refDataCurTemp1, -1, day);
		reorganizer2 = getReorganizerData(refDataCurTemp2, -1, day);
	}else{
		//check if whole day is empty
		if (refDataCur[day] !== undefined && (refDataCur[day][0]).join('') == '') {
			emptyDay = true;
			dayPartIndexCur = -1;
		}
		reorganizer = getReorganizerData(refDataCur, dayPartIndexCur, day);
	}
	tsLog('ending getReorganizerData');

	if (day != '9999' && day != '-1') {
		//deal with expanded view

		if (!emptyDay) {
			//find cutoffs and display it/them
			for (t = 0; t < refDataCur[day][1].length; t++) {
				if (refDataCur[day][1][t] != '') {
					if ($('co_d'+day+'_ts'+t+'_row')) {
						if (refDataCur[day][1][t] != '') {
							$('co_d'+day+'_ts'+t+'_row').show();
						}else{
							$('co_d'+day+'_ts'+t+'_row').hide();
						}
					}
				}
			}
			var lastShownTS = '';
			var rowsHidden = 0;
			//hide rows in expanded view
			for (t = 0; t < refDataCur[day][0].length; t++) {
				if (refDataCur[day][0][t] == '') {
					if ($('ts_d'+day+'_ts'+t)) {
						$('ts_d'+day+'_ts'+t).hide();
						var eId = $('ts_d'+day+'_ts'+t).parentNode.id;
						if ($(eId)) { $(eId).hide(); }
						rowsHidden++;
					}
				}else{
					lastShownTS = $('ts_d'+day+'_ts'+t);
				}
			}
			rowsHidden++; //always one more than esitmated
			//find missing height and make last row match
			if (lastShownTS !== '') {
				lastShownTS.style.height = getCalcdRowHeight(rowsHidden, null, true, 1); //add a row here
			}
		}else{
			//hide rows in expanded view
			for (t = 1; t < refDataCur[day][0].length; t++) {
				if (refDataCur[day][0][t] == '') {
					if ($('ts_d'+day+'_ts'+t)) {
						var eId = $('ts_d'+day+'_ts'+t).parentNode.id;
						if ($(eId)) { $(eId).hide(); }
					}
				}
			}
			//now make table fill space
			if ($('ts_d'+day+'_ts0')) {
				//fill in space
				if ($('ts_d'+day+'_ts0').getAttribute('name') != null && 
					(
						($('ts_d'+day+'_ts0').getAttribute('name')).indexOf('nodelivery_') == 0 ||
						($('ts_d'+day+'_ts0').getAttribute('name')).indexOf('cutoffpassed_') == 0 ||
						($('ts_d'+day+'_ts0').getAttribute('name')).indexOf('holiday_') == 0
					)
				) {
					var contId = ($('ts_d'+day+'_ts0').getAttribute('name')).split('_');
					if (isArray(contId) && $(contId[1])) {
						//found content to use
						$('ts_d'+day+'_ts0').innerHTML = $(contId[1]).innerHTML;
						$('ts_d'+day+'_ts0').style.height = '100%';
					}else{
						//no content, fallback to no delivery
						$('ts_d'+day+'_ts0').innerHTML = $('NDdayE').innerHTML;
						$('ts_d'+day+'_ts0').style.height = '100%';
					}
				}else{
					$('ts_d'+day+'_ts0').innerHTML = $('NDdayE').innerHTML;
					$('ts_d'+day+'_ts0').style.height = '100%';
				}
				//remove border
				$('ts_d'+day+'_ts0').style.borderBottom = '0 none';
				$('ts_d'+day+'_ts0').style.borderTop = '0 none';

				if ($('ts_d'+day+'_ts0').style.height != '100%') {
					//if not one of the specials
					$('ts_d'+day+'_ts0').style.height = getCalcdRowHeight(refDataCur[day][0].length, null, true, 1);
				}
				//fill in height
				$('ts_d'+day+'_ts0').getOffsetParent().style.height = getCalcdRowHeight(refDataCur[day][0].length+1, null, true, 1);
			}

		}
	}

	//call reorg
	if (day == '9999' && refDataCur[10] !== undefined && refDataCur[20] !== undefined) {
		while (!tsReorganizer(reorganizer1)) {}
		while (!tsReorganizer(reorganizer2)) {}
	}else{
		while (!tsReorganizer(reorganizer)) {}
	}

	return true;
}

/* return a css style height size based on arguments
 * both args optional
 */
function getCalcdRowHeight(numRowsArg, pxEachArg, addOneArg, pxSubtractArg) {
	var numRows = numRowsArg || 1;
	var pxEach = pxEachArg || 32;
	var addOne = addOneArg || false;
	var pxSubtract = pxSubtractArg || 0;
	var pxTotal;

	pxTotal = pxEach * numRows;
	if (addOne) { pxTotal = pxTotal + pxEach; }
	if (pxSubtract) { pxTotal = pxTotal - pxSubtract; }
	return pxTotal+'px';
}

/* gets reorganizer data based on refData and a daypartIndex
 *	if daypartIndex is not passed (or set == -1), treats it as one set
 *	if it is passed, splits at that index and treats as two separate sets
  * dayArg is the selected day 
 */
function getReorganizerData(refDataCurArg, daypartIndexCurArg, dayArg) {
	
	var reorganizer = []; //hold elems to be reorganized
	var refDataCur = refDataCurArg || reorganizer;
	var day = dayArg || -1;
	var daypartIndexCur = daypartIndexCurArg || -1;
	if (refDataCur.length == 0) { return reorganizer; } //no refData, no reorganize

	var refDataCurArr = []; //setup
	if (daypartIndexCur != -1) {
		//split into refDataCurArr
		var tempArr = []; //hold data while splitting
		for (var d = 0; d < refDataCur.length; d++) {
			if (refDataCur[d] == undefined) { continue; }
			
			var name = "";
			($('ts_d'+d+'_ts0'))
				? name = $('ts_d'+d+'_ts0').getAttribute('name')
				: name = "";
			if ( (name != "" && name != null) && 
				(
					name.indexOf('nodelivery_') == 0 ||
					name.indexOf('cutoffpassed_') == 0 ||
					name.indexOf('holiday_') == 0
				) &&
				(d == day) //only if day is the day to show expanded
			) {
				//no split, just put it in the way it is
				tempArr[d] = refDataCur[d];
				refDataCurArr[refDataCurArr.length] = tempArr;
				tempArr = []; //clear
				
			}else{
				tempArr[d] = [[], []];
				for (var t = 0; t < refDataCur[d][0].length; t++) {
					if (t == daypartIndexCur) {
						//hit split, add what we have to the refDataCurArr and start fresh
						refDataCurArr[refDataCurArr.length] = tempArr;
						tempArr = []; //clear
						//and setup next section, but only if we're not done
						if (t != refDataCur[d][0].length-1) {
							tempArr[d] = [[], []];
						}
					}
					if(!tempArr[d]){
						tempArr[d] = [[], []];
					}
					tempArr[d][0][t] = refDataCur[d][0][t];
					tempArr[d][1][t] = refDataCur[d][1][t];
				}
				
				//if we have anything in tempArr now, put it in to be used
				if (tempArr.length != 0) {
					refDataCurArr[refDataCurArr.length] = tempArr;
						tempArr = []; //clear
				}
			}
		}
		//if we have anything in tempArr now, put it in to be used
		if (tempArr.length != 0) {
			refDataCurArr[refDataCurArr.length] = tempArr;
		}
	}else{
		//no split, just put it in the way it is
		refDataCurArr[refDataCurArr.length] = refDataCur;
	}

	//loop through the refDataCurArr and parse each subset

	for (var i = 0; i < refDataCurArr.length; i++) {
		refDataCur = refDataCurArr[i];

		var sequenceCount = 0; //reset count
		var startSequence = false; //reset sequencer
		var reorgStart = -1;
		var reorgEnd = -1;
		var lastWasTS = false;

		//navigate again, using sequence key ref
		for (var d = 0; d < refDataCur.length; d++) {
			if (refDataCur[d] === undefined) { continue; }
			if (d == day) { continue; } //skip expanded day
			sequenceCount = 0; //reset count
			startSequence = false; //reset sequencer
			reorgStart = -1; //reset start marker
			reorgEnd = -1; //reset end marker
			lastWasTS = false; //reset last TS marker
			
			var sequenceKey = -1;
			var lbnd = getLBound(refDataCur);
			var sequenceKeyTemp = sequenceKey;
			if (sequenceKeyTemp == -1) {
				sequenceKey = lbnd;
			}
			
			for (var t = 0; t < refDataCur[d][0].length; t++) {
				if (refDataCur[d][0][t] === undefined) { continue; }
				
				//reset reorgs
				if ($('ts_d'+d+'_ts'+t)) {
					if ($('ts_d'+d+'_ts'+t).rowSpan > 1 || refDataCur[d][0][t] == '') { //blanks only
						$('ts_d'+d+'_ts'+t).innerHTML = '<div class="tsContent "><div class="fleft ts_rb" id="ts_d'+d+'_ts'+t+'_rbCont"></div><div class="fleft tsCont " id="ts_d'+d+'_ts'+t+'_time" style="width: 60px;">&nbsp;</div></div>';
					}
					$('ts_d'+d+'_ts'+t).rowSpan = 1;
					$('ts_d'+d+'_ts'+t).show();

					var eId = $('ts_d'+d+'_ts'+t).parentNode.id;
					$(eId).show();
				}
				if (refDataCur[d][1][t] !== '' || (refDataCur[d][1][t] === '' && t === 0) || (refDataCur[d][1][t] === '' && t === daypartIndexCurArg)) {
					//reorg now
					if (reorgStart != -1 && reorgEnd != -1) {
						reorganizer[reorganizer.length] = Array(d, reorgStart, reorgEnd);
					}
					startSequence = true;
					reorgStart = t;
					reorgEnd = -1;
					sequenceCount = 0; //reset count
				}
				//sequence
				if (startSequence && refDataCur[d][0][t] === '') {
					sequenceCount++;
					if (lastWasTS) {
						reorgStart = t; //reset to current ts
						lastWasTS = false;
					}
				}else{
					//reorg now
					if (reorgStart != -1 && reorgEnd != -1) {
						reorganizer[reorganizer.length] = Array(d, reorgStart, reorgEnd);
					}
					reorgStart = -1;
					reorgEnd = -1;
					sequenceCount = 0; //reset count
					lastWasTS = true; //mark for next loop to reset
				}
				if (sequenceCount > 3) {
					reorgEnd = t;
				}
			}
			//check reorg again one final time
			if (reorgStart != -1 && reorgEnd != -1) {
				reorganizer[reorganizer.length] = Array(d, reorgStart, reorgEnd);
			}
		}

	}

	return reorganizer;
}

function tsReorganizer(reorganizer) {
	var i, j, d, reorgStart, reorgEnd, sequenceCount, contId, padHeight;

	//go through reorganizer and do the reorganization
	for (i = 0; i < reorganizer.length; i++) {
		d = reorganizer[i][0];
		reorgStart = reorganizer[i][1];
		reorgEnd = reorganizer[i][2];

		sequenceCount = (reorgEnd-reorgStart)+1;

		//first, change rowspan
		if ($('ts_d'+d+'_ts'+reorgStart)) {
			padHeight = Math.floor((sequenceCount-1)*31/2+(sequenceCount/2));
			$('ts_d'+d+'_ts'+reorgStart).rowSpan = sequenceCount;
			
			//change the contents (holiday? no delivery?)
			if ($('ts_d'+d+'_ts'+reorgStart).getAttribute('name') != null && ($('ts_d'+d+'_ts'+reorgStart).getAttribute('name')).indexOf('holiday_') == 0) {
				//holiday
				contId = ($('ts_d'+d+'_ts'+reorgStart).getAttribute('name')).split('_');
				if (isArray(contId) && $(contId[1])) {
					//found content to use
					$('ts_d'+d+'_ts'+reorgStart).innerHTML = $(contId[1]).innerHTML;
				}
			}else{
				//no delivery
				$('ts_d'+d+'_ts'+reorgStart).innerHTML = $('NDdayC').innerHTML;
				$('ts_d'+d+'_ts'+reorgStart).className = $('ts_d'+d+'_ts'+reorgStart).className.replace("tsContainerBGC","tsContainerNoDelBGC");
			}
			//readjust for height
			//this is the default collapsed no delivery, don't add a row and use negative subtraction
			$('ts_d'+d+'_ts'+reorgStart).style.height = getCalcdRowHeight(sequenceCount, null, false, 2);
			
		}
		//now hide extra tds
		for (j = reorgStart+1; j < reorgEnd+1; j++) {
			if ($('ts_d'+d+'_ts'+j)) {
				$('ts_d'+d+'_ts'+j).hide();
			}
		}


		
	}

	return true;
}

var lastTS_expanded = -1;
function tsExpand(elemIdArg, autoCheckRadioArg, retryArg) {
	if (lastTS_expanded != -1 && parseDay(elemIdArg) == parseDay(lastTS_expanded)) { return true; }

	var retry = true;
	if (retryArg !== undefined) { retry = retryArg; }
	var elemId = elemIdArg || '';
	var autoCheckRadio = true;
	if (autoCheckRadioArg !== undefined) { autoCheckRadio = autoCheckRadioArg; }

	//get the values from ref elements, if we haven't already
	if (styleStrExp === '') {
		styleStrExp = buildStyleStr('tsRefcol_expanded');
		styleStrCon = buildStyleStr('tsRefcol_contracted');
	}

	if (elemId !== '' && $(elemId)) {

		var day = parseDay(elemId); //should now be the day index

		//solve display issues selectively
		if (lastTS_expanded == -1 || parseDay(lastTS_expanded) < 10) {
			tsLog('starting solveDisplay -1');
			while (!solveDisplay('ts_d-1_ts0', autoCheckRadio)) { };
			tsLog('ending solveDisplay');
		}
		if (lastTS_expanded == -1 || parseDay(lastTS_expanded) >= 10) {
			tsLog('starting solveDisplay 9999');
			while (!solveDisplay('ts_d9999_ts0', autoCheckRadio)) { };
			tsLog('ending solveDisplay');
		}

		tsLog('starting solveDisplay DAY');
		while (!solveDisplay(elemId, autoCheckRadio)) { };
		tsLog('ending solveDisplay DAY');

		//set as last expanded
		lastTS_expanded = elemId;
		set_02_last = elemId;

		var day = parseDay(elemId); //should now be the day index
		
		var refDataCur = refData;
		if (day >= 10) {
			refDataCur = refAdvData;
		}

		for (var t = 0; t < refDataCur[day][0].length; t++) {
			if ($('ts_d'+day+'_ts'+t) && $('ts_d'+day+'_ts'+t).style.display != 'none' && $('ts_d'+day+'_ts'+t).parentNode.style.display != 'none') {
				elemId = 'ts_d'+day+'_ts'+t;
			}
		}

		if ( parseDay(elemId) < 10) { globalTS  = elemId; }

		$('ts_d'+day+'_ts_hE').show();
		$(elemId).morph(styleStrExp, {
			duration: expandDuration,
			beforeStart: function() {
				$('ts_d'+day+'_ts_hE').morph(styleStrExp, { duration: expandDuration });
				$('ts_d'+day+'_ts_hE_content').hide();
				$('ts_d'+day+'_tsTable').hide();
			},
			afterFinish: function() {
				$('ts_d'+day+'_ts_hE_content').appear({duration: appearDuration});
				$('ts_d'+day+'_tsTable').appear({duration: appearDuration});
			}
		});

		//hide bottom border on final ts of displayed ts
		$(elemId).style.borderBottomWidth = '0';

	}else{
		if (retry) {
			//run through solve display and try again
			while (!solveDisplay(elemId)) {}; //solve display issues
			tsExpand(elemId, autoCheckRadio, false);
		}else{
			while (!solveDisplay(elemId)) {}; //solve display issues
		}
	}

	return true;
}

/*
 *	parentId = the id of the top-level container (can be itself)
 *		check via descendantOf, so does not need to be direct child
 *	exceptId = an id of a child div under the parentId to NOT contract
 */
var contract;
function tsContractAll(parentIdArg, exceptIdArg) {
	if (parseDay(set_02) == -1 || parseDay(set_02) == parseDay(set_02_last)) { return true; }
	var parentId = parentIdArg || '';
	var exceptId = exceptIdArg || '';
	var refs = $$('td.tsCol'); //style ref

	if (!$(parentId) || parentId === '') { //no parent, use body tag
		if (document.body.id === "") {
			//no id, add one
			document.body.id = 'bodytag';
			parentId = 'bodytag';
		}
		parentId = document.body.id;
	}

	//get the values from ref elements, if we haven't already
	if (styleStrExp === '') {
		styleStrExp = buildStyleStr('tsRefcol_expanded');
		styleStrCon = buildStyleStr('tsRefcol_contracted');
	}

	//assuming an id like 'ts_d#_ts#', split out the day so we can skip contracts
	var conExcepDay = (exceptId).split('_')[1]+'_'; //should now be 'd#_'

	for (var i=0; i< refs.length; i++) {
		if ($(refs[i]).id != exceptId && ($(refs[i]).descendantOf(parentId) || $(refs[i]).id == parentId) ) {
			//skip exceptions
			if (($(refs[i]).id).indexOf(conExcepDay) == -1) {
				contract = new Effect.Morph(refs[i], {
					style: styleStrCon,
					duration: contractDuration
				});
			}
			//children
			var refsChildren = $(refs[i]).getElementsByTagName('table');
			for (var j=0; j< refsChildren.length; j++) {
				//skip exceptions
				if (($(refsChildren[j]).id).indexOf(conExcepDay) == -1) {
					//required?
					contract = new Effect.Morph(refsChildren[j], {
						style: styleStrCon,
						duration: contractDuration
					});
				}
			}
		}
	}

	return true;
}

function defaultColumnExpand(dayIndex, slotIndex) {
	if(dayIndex == 0 && slotIndex == 0){
		tsExpand('ts_d'+dayIndex+'_ts'+slotIndex, false);
	}else{
		tsExpand('ts_d'+dayIndex+'_ts'+slotIndex);
	}
}

function hideAdvanceOrder() { 
	$("timeslots_grid0").toggle();

	if($("timeslots_grid2")){
		$("timeslots_grid1").toggle();
	}

	if($("timeslots_grid0").style.display != "none") {
		$('displayAdvanceOrderGrid').innerHTML = "Hide Details";
	}else{
		$('displayAdvanceOrderGrid').innerHTML = "Show Details";
		tsContractAll('tsContainer');
		if (globalTS != -1) {
			tsExpand(globalTS);
		}else{
			defaultColumnExpand(0,0);
		}
	}
}

function checkDeliveryShow(elemIdarg) {
		var elemId = elemIdarg ||'learnMoreContent';
		if($(elemId)){
			var contentWidth = '400';
			if ($(elemId)) {
				if(($(elemId).getStyle('width'))!=null)
					contentWidth = ($(elemId).getStyle('width')).replace("px","");
			}
			Modalbox.show($(elemId), {
					loadingString: 'Loading Window...',
					closeValue: '<img src="/media_stat/images/giftcards/your_account/close.gif" border="0" alt="" />',
					closeString: 'Close Window',
					title: 'Delivery Information',
					overlayOpacity: .30,
					overlayClose: false,
					width: contentWidth,
					transitions: false,
					autoFocusing: false,
					centered: true,
					afterLoad: function() { $('MB_content').style.overflow='hidden';window.scrollTo(0,0); },
					afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
				})
		}
}

function showLegendOverlay(mediaPathArg, titleArg) {
			var mediaPath = mediaPathArg || '';
			var title = titleArg || '';
			var contentWidth = '450';
			
			Modalbox.show(mediaPath, {
					loadingString: 'Loading Window...',
					closeValue: '<img src="/media_stat/images/giftcards/your_account/close.gif" border="0" alt="" />',
					closeString: 'Close Window',
					title: title,
					overlayOpacity: .40,
					overlayClose: true,
					width: contentWidth,
					transitions: false,
					autoFocusing: false,
					centered: true,
					afterLoad: function() { $('MB_content').style.overflow='hidden'; },
					afterHide: function() { window.scrollTo(Modalbox.initScrollX,Modalbox.initScrollY); }
				});

	return true;
}

var timer;
var clickOnly = false;

/* add even listeners for mouse over expands, needs to be on window load for refData */
document.observe('dom:loaded', function() {
	
	var refDataCur = null;

	//add adv ts events (if they exist)
	if (window.refAdvData !== undefined) {
		refDataCur = refAdvData;
		for (d = 0; d < refDataCur.length; d++) {
			if (refDataCur[d] === undefined) { continue; }
			
			//headers
			createMouseOverOutEvents('ts_d'+d+'_ts_hC', 'tsContainer');
			createMouseOverOutEvents('ts_d'+d+'_ts_hE', 'tsContainer');
			//footer
			createMouseOverOutEvents('ts_d'+d+'_ts_fC', 'tsContainer');
			createMouseOverOutEvents('ts_d'+d+'_ts_fE', 'tsContainer');
			//ts's and co's
			for (t = 0; t < refDataCur[d][0].length; t++) {
				createMouseOverOutEvents('ts_d'+d+'_ts'+t, 'tsContainer');
				createMouseOverOutEvents('co_d'+d+'_ts'+t, 'tsContainer');
			}
			//radio buttons
			createMouseOverOutEvents('ts_d'+d+'_ts'+t+'_rb', 'tsContainer');
		}
	}

	//add ts events
	if (window.refData !== undefined) {
		refDataCur = refData;
		for (d = 0; d < refDataCur.length; d++) {
			if (refDataCur[d] === undefined) { continue; }
			
			//headers
			createMouseOverOutEvents('ts_d'+d+'_ts_hC', 'tsContainer');
			createMouseOverOutEvents('ts_d'+d+'_ts_hE', 'tsContainer');
			//footer
			createMouseOverOutEvents('ts_d'+d+'_ts_fC', 'tsContainer');
			createMouseOverOutEvents('ts_d'+d+'_ts_fE', 'tsContainer');
			//ts's and co's
			for (t = 0; t < refDataCur[d][0].length; t++) {
				createMouseOverOutEvents('ts_d'+d+'_ts'+t, 'tsContainer');
				createMouseOverOutEvents('co_d'+d+'_ts'+t, 'tsContainer');
			}
		}
	}

});

function createChildrenMouseClickEvent(elemArg, parentIdArg) {
	var elem = elemArg || '';
	var parentId = parentIdArg || '';

	if ($(parentId)) {
		elem.observe('click', checkRadio.bindAsEventListener(elem, elem, parentId));
	}
}

function createMouseOverOutEvents(elemIdArg, parentIdArg) {
	var elemId = elemIdArg || '';
	var parentId = parentIdArg || '';

	if ($(elemId)) {
		//try { 
			$(elemId).observe('mouseover', setter.bindAsEventListener(elemId, parentId, elemId));
			$(elemId).observe('mouseout', forgetter);
			$(elemId).observe('click', forgetter);
			$(elemId).observe('click', checkRadio.bindAsEventListener(elemId, elemId));
		//} catch (e) { /* do nothing, IE7 doesn't support these */ }
	}
}

function checkRadio(event, elemIdArg) {
	if ($(elemIdArg+'_rb')) {
		$(elemIdArg+'_rb').checked = true;
		
		if(window.refAdvData !== undefined){
			checkTsBgColor(refData, refAdvData);
		}else{
			checkTsBgColor(refData);
		}
	}
}

function checkTsBgColor() {
	for (var i = 0; i < arguments.length; i++) {
		var refDataCur = arguments[i];
		for (d = 0; d < refDataCur.length; d++) {
			if (refDataCur[d] === undefined) { continue; }
			for (t = 0; t < refDataCur[d][0].length; t++) {
				//check if color should be selected
				if ($('ts_d'+d+'_ts'+t)) {
					if ($('ts_d'+d+'_ts'+t).className.indexOf('tsContentResE') == -1) { //not reserved
						if ($('ts_d'+d+'_ts'+t+'_rb')){
							if (!$('ts_d'+d+'_ts'+t+'_rb').checked) {
								//only if rb present and false
								$('ts_d'+d+'_ts'+t).className = $('ts_d'+d+'_ts'+t).className.replace(' tcSelectionBGC', '');
								if ($('ts_d'+d+'_ts'+t+'_msgE')) {
									if ($('ts_d'+d+'_ts'+t+'_msgE').parentNode.className.indexOf('tsContentSelE') != -1) {
										$('ts_d'+d+'_ts'+t+'_msgE').parentNode.className = $('ts_d'+d+'_ts'+t+'_msgE').parentNode.className.replace('tsContentSelE', '');
										$('ts_d'+d+'_ts'+t+'_msgE').innerHTML = '&nbsp;';
										if ($('ts_d'+d+'_ts'+t+'_msgC')) {
											$('ts_d'+d+'_ts'+t+'_msgC').innerHTML = '&nbsp;';
										}
										if ($('ts_d'+d+'_ts'+t+'_time')) {
											$('ts_d'+d+'_ts'+t+'_time').className = $('ts_d'+d+'_ts'+t+'_time').className.replace('tsContentSelC', '');
										}
									}
								}
							}else{
								//only if rb present and true
								if ($('ts_d'+d+'_ts'+t).className.indexOf('tcSelectionBGC') == -1) {
									//and if it's not already selected
									$('ts_d'+d+'_ts'+t).className += ' tcSelectionBGC';
								}
							}
						}
					}
				}
			}
		}
	}
}

var set_01 = '';
var set_02 = '';
var set_02_last = '';
function setter(e, set01, set02) {
	if (!clickOnly) {
		set_01 = set01;
		set_02 = set02;
		timer = setTimeout('timedExpand()', timedExpandWait);
	}
}

function forgetter() {
	set_01 = '-2';
	set_02 = '-2';
	clearTimeout(timer);
}

function timedExpand() {
	//check that it's not the same element and we're not in the same column
	if (set_02_last != set_02 && parseDay(set_02) != parseDay(set_02_last)) {
		tsContractAll(set_01, set_02);
		tsExpand(set_02, false);
		set_02_last = set_02;
	}
	forgetter();
}


function tsLog(logMsg) {
	var d = new Date();
	var now = d.getTime();
	var took = ' (Time to here: ' +(now-tsdebugTimeDif)+'ms) ';
	tsdebugTimeDif = now;
	if (global_tsDebug && window.console) { console.log(d.toUTCString()+took+'Log: '+logMsg); }
}