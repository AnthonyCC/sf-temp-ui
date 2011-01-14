var styleStrExp = '';
var styleStrCon = '';
var globalTS = -1;

//morph effect will match each of these styles from ref elem.
var styleArr = [];
styleArr[styleArr.length] = 'width';
styleArr[styleArr.length] = 'background-color';

function buildStyleStr(ref) {
	if (!$(ref)) { return ''; }

	var styleString = '';
	for (var i=0; i < styleArr.length; i++)	{
		styleString += styleArr[i];
		styleString += ':';
		styleString += $(ref).getStyle(styleArr[i]);
		styleString += ';';
	}

	return styleString;
}

function parseDay(elemId) {
	if (elemId === undefined || elemId === '') {
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
	//check if refData is empty
	if (refDataCur.join("").replace(/,/g,"") === "") {
		var reorganizer = []; //hold elems to be reorganized
		//it is, put in empty strings
		for (var r=0; r < refDataCur.length; r++) {
			if (refDataCur[r] === undefined) { continue; }

			for (var s=0; s < refDataCur[r].length; s++) {
				refDataCur[r][s][0] = "";
				refDataCur[r][s][1] = "";
				refDataCur[r][s][2] = "";
				refDataCur[r][s][3] = "";
			}
			reorganizer[reorganizer.length] = new Array(r, 0, 3);
		}
		while (!fillRefAddRows(refDataCur, startIndex)) {}
	}

	//call reorg
	while (!tsReorganizer(reorganizer)) {}

	return true;
}

//add in rows
function fillRefAddRows(refDataCur, startIndex) {
	//check header ref so we know we have a table to act on
	if ($('ts_d'+startIndex+'_ts_header') && $('ts_d'+startIndex+'_ts_header').tagName === 'TD') {
		var advId = '';
		if (startIndex >= 10) { advId = 'Adv'; }
		//get table ref
		var node = $('ts_d'+startIndex+'_ts_header');
		while (node.tagName !== 'TABLE') {
			node = node.parentNode;
		}
		
		var rowIndex = 0;
		var cellIndex;
		var newRow;
		var newCell;
		
		for (var r = 0; r < 4; r++) {
			rowIndex++;
			newRow = node.insertRow(rowIndex);
			cellIndex = 0;
			
			//insert a cutoff
			if (r === 0) {
				
				for (var d = 0; d < refDataCur.length; d++) {
					if (refDataCur[d] === undefined) { continue; }

					newRow.id = 'co'+advId+'_ts'+r+'_row';
				
					newCell = newRow.insertCell(cellIndex);
					//newCell.id = 'co_d'+d+'_ts'+r;
					newCell.innerHTML = '<div id="co_d'+d+'_ts'+r+'">&nbsp;</div>';
					newCell.colSpan = 3;
					newCell.className = 'cutoff';
					cellIndex++;
				}
				
				rowIndex++;
				newRow = node.insertRow(rowIndex);
				cellIndex = 0;
			}

			for (var d = 0; d < refDataCur.length; d++) {
				if (refDataCur[d] === undefined) { continue; }
				
				newCell = newRow.insertCell(cellIndex);
				newCell.id = 'ts_d'+d+'_ts'+r;
				newCell.innerHTML = '<div class="tsContent"><div class="fleft ts_rb" id="ts_d'+d+'_ts'+r+'_rbCont" style="display: none;">&nbsp;</div><div>&nbsp;</div></div>';
				newCell.colSpan = 3;
				newCell.className = 'tsCol tsContainerC tsContainerBGC';
				cellIndex++;
			}
		}
	}
	return true;
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
	
	var refDataCur = refData;
	var advId = '';
	if (day >= 10 && window.refAdvData !== undefined) {
		refDataCur = refAdvData;
		advId = 'Adv';
	}

	
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
		}
	}

	//navigate refDataCur matrix
	for (d = 0; d < refDataCur.length; d++) {
		if (refDataCur[d] === undefined) { continue; }
		//show for current day
		if (d == day) {
			//this is our sequence ref key
			sequenceKey = d;
			//header
			//collapsed
			if ($('ts_d'+d+'_ts_hC')) {
				$('ts_d'+d+'_ts_hC').hide();
			}
			//expanded
			if ($('ts_d'+d+'_ts_hE')) {
				$('ts_d'+d+'_ts_hE').show();
			}
			//line fix
			if ($('ts_d'+d+'_ts_header')) {
				$('ts_d'+d+'_ts_header').className = 'noTopBorder';
			}

			//footer
			//collapsed
			if ($('ts_d'+d+'_ts_fC')) {
				$('ts_d'+d+'_ts_fC').hide();
			}
			//expanded
			if ($('ts_d'+d+'_ts_fE')) {
				$('ts_d'+d+'_ts_fE').show();
			}
			for (t = 0; t < refDataCur[d][0].length; t++) {
				//cutoffs
				if ($('co_d'+d+'_ts'+t)) {
					$('co_d'+d+'_ts'+t).parentNode.className = 'cutoffDisp';
				}

				if (refDataCur[d][1][t]) {
					if ($('co_d'+d+'_ts'+t)) {
						$('co_d'+d+'_ts'+t).show();
						//rows
						if ($('co'+advId+'_ts'+t+'_row')) {
							$('co'+advId+'_ts'+t+'_row').show();
						}
					}
				}else{
					//rows
					if ($('co'+advId+'_ts'+t+'_row')) {
						$('co'+advId+'_ts'+t+'_row').hide();
					}
				}
				//radios
				if ($('ts_d'+d+'_ts'+t+'_rbCont')) {
					$('ts_d'+d+'_ts'+t+'_rbCont').show();
				}
				if (timeslot_info) { //timeslot_info page
					if ($('ts_d'+d+'_ts'+t+'_rb')) {
						$('ts_d'+d+'_ts'+t+'_rb').hide();
					}
				}else{ //checkout
					if ($('ts_d'+d+'_ts'+t+'_rb')) {
						$('ts_d'+d+'_ts'+t+'_rb').show();
					}
					//check the radio button on the clicked slot
					if ($(elemId+'_rb')) {
						if (autoCheckRadio) {
							$(elemId+'_rb').checked = true;
						}
					}
				}
				//Force order fix
				if ($('ts_d'+d+'_ts'+t+'_forceX')) {
						$('ts_d'+d+'_ts'+t+'_forceX').show();
				}
				//msging
				if ($('ts_d'+d+'_ts'+t+'_msgC')) {
					$('ts_d'+d+'_ts'+t+'_msgC').hide();
				}
				if ($('ts_d'+d+'_ts'+t+'_msgE')) {
					$('ts_d'+d+'_ts'+t+'_msgE').show();
				}
				//borders
				if (t == refDataCur[d][0].length) {
					$('ts_d'+d+'_ts'+t).style.borderBottomWidth = '1px';
				}
				//background color
				if ($('ts_d'+d+'_ts'+t)) {
					$('ts_d'+d+'_ts'+t).className = $('ts_d'+d+'_ts'+t).className.replace("tsContainerBGC", "tsContainerBGE");
				}
			}
		}else{
			//hide others
			//header
			//collapsed
			if ($('ts_d'+d+'_ts_hC')) {
				$('ts_d'+d+'_ts_hC').show();
			}
			//expanded
			if ($('ts_d'+d+'_ts_hE')) {
				$('ts_d'+d+'_ts_hE').hide();
			}
			//line fix
	        if ($('ts_d'+d+'_ts_header')) {
              $('ts_d'+d+'_ts_header').className = '';
			}
			//footer
			//collapsed
			if ($('ts_d'+d+'_ts_fC')) {
				$('ts_d'+d+'_ts_fC').show();
			}
			//expanded
			if ($('ts_d'+d+'_ts_fE')) {
				$('ts_d'+d+'_ts_fE').hide();
			}
			for (t = 0; t < refDataCur[d][0].length; t++) {
				if ($('co_d'+d+'_ts'+t)) {
					$('co_d'+d+'_ts'+t).parentNode.className = 'cutoff';
				}

				//cutoffs
				if (refDataCur[d][1][t] !== '') {
					if ($('co_d'+d+'_ts'+t)) {
						$('co_d'+d+'_ts'+t).hide();
					}
				}
				//radios
				if ($('ts_d'+d+'_ts'+t+'_rbCont')) {
					$('ts_d'+d+'_ts'+t+'_rbCont').hide();
				}
				if (timeslot_info) { //timeslot_info page
					if ($('ts_d'+d+'_ts'+t+'_rbCont')) {
						$('ts_d'+d+'_ts'+t+'_rbCont').hide();
					}
				}else{ //checkout
					if ($('ts_d'+d+'_ts'+t+'_rb')) {
						$('ts_d'+d+'_ts'+t+'_rb').hide();
					}
				}
				//Force order fix
				if ($('ts_d'+d+'_ts'+t+'_forceX')) {
						$('ts_d'+d+'_ts'+t+'_forceX').hide();
				}
				//msging
				if ($('ts_d'+d+'_ts'+t+'_msgC')) {
					$('ts_d'+d+'_ts'+t+'_msgC').show();
				}
				if ($('ts_d'+d+'_ts'+t+'_msgE')) {
					$('ts_d'+d+'_ts'+t+'_msgE').hide();
				}
				//borders
				if ($('ts_d'+d+'_ts'+t)) {
					$('ts_d'+d+'_ts'+t).style.borderBottomWidth = '1px';
				}
				//background color
				if ($('ts_d'+d+'_ts'+t)) {
					$('ts_d'+d+'_ts'+t).className = $('ts_d'+d+'_ts'+t).className.replace("tsContainerBGE", "tsContainerBGC");
				}
			}
		}
	}

	var lbnd = getLBound(refDataCur);
	var sequenceKeyTemp = sequenceKey;
	if (sequenceKeyTemp == -1) {
		sequenceKeyTemp = lbnd;
	}

	for (i = 0; i < 2; i++) {
		advId = (i) ? 'Adv' : '';

		//always add borders for outer cutoffs, and show row 0
		if ($('co'+advId+'_ts0_row')) {
			$('co'+advId+'_ts0_row').show();
		}
		//do the same for the opposite
		for (t = 0; t < refDataCur[sequenceKeyTemp][1].length; t++) {
			if ($('co_d'+sequenceKeyTemp+'_ts'+t)) {
				if ($('co_d'+lbnd+'_ts'+t)) {
					$('co_d'+lbnd+'_ts'+t).parentNode.style.borderLeft = '1px solid #ccc';
				}
				if ($('co_d'+(refDataCur.length-1)+'_ts'+t)) {
					$('co_d'+(refDataCur.length-1)+'_ts'+t).parentNode.style.borderRight = '1px solid #ccc';
				}
			}
		}
		
	}

	//skip out if day is invalid, this shouldn't normally happen
	if (day == -1 || sequenceKey == -1) { return true; }

	var sequenceCount = 0; //reset count
	var startSequence = false; //reset sequencer
	var reorgStart = -1;
	var reorgEnd = -1;
	var lastWasTS = false;
	var reorganizer = []; //hold elems to be reorganized

	//navigate again, using sequence key ref
	for (d = 0; d < refDataCur.length; d++) {
		if (refDataCur[d] === undefined) { continue; }
		sequenceCount = 0; //reset count
		startSequence = false; //reset sequencer
		reorgStart = -1; //reset start marker
		reorgEnd = -1; //reset end marker
		lastWasTS = false; //reset last TS marker
		
		for (t = 0; t < refDataCur[d][0].length; t++) {
			//reset reorgs
			if ($('ts_d'+d+'_ts'+t)) {
				if ($('ts_d'+d+'_ts'+t).rowSpan > 1) { //blanks only
					$('ts_d'+d+'_ts'+t).innerHTML = '<div class="tsContent"><div class="fleft ts_rb" id="ts_d'+d+'_ts'+t+'_rbCont"></div><div class="">&nbsp;</div></div>';
				}
				$('ts_d'+d+'_ts'+t).rowSpan = 1;
				$('ts_d'+d+'_ts'+t).show();
			}
			if (refDataCur[sequenceKey][1][t] !== '' || (refDataCur[sequenceKey][1][t] === '' && t === 0)) {
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

	//call reorg
	while (!tsReorganizer(reorganizer)) {}

	return true;
}

function tsReorganizer(reorganizer) {
	//go through reorganizer and do the reorganization
	for (i = 0; i < reorganizer.length; i++) {
		d = reorganizer[i][0];
		reorgStart = reorganizer[i][1];
		reorgEnd = reorganizer[i][2];
		sequenceCount = (reorgEnd-reorgStart)+1;

		//first, change rowspan
		if ($('ts_d'+d+'_ts'+reorgStart)) {
			var padHeight = (sequenceCount-1)*31/2;
			$('ts_d'+d+'_ts'+reorgStart).rowSpan = sequenceCount;
			
			//change the contents (holiday? no delivery?)
			if ($('ts_d'+d+'_ts'+reorgStart).getAttribute('name') != null && ($('ts_d'+d+'_ts'+reorgStart).getAttribute('name')).indexOf('holiday_') == 0) {
				//holiday
				var contId = ($('ts_d'+d+'_ts'+reorgStart).getAttribute('name')).split('_');
				if (isArray(contId) && $(contId[1])) {
					//found content to use
					$('ts_d'+d+'_ts'+reorgStart).innerHTML = $(contId[1]).innerHTML;
				}
			}else{
				//no delivery
				$('ts_d'+d+'_ts'+reorgStart).innerHTML = '<div class="tsContent" style="padding-top: '+padHeight+'px; padding-bottom: '+padHeight+'px;" onclick="tsContractAll(\'tsContainer\', \'ts_d'+d+'_ts'+reorgStart+'\'); tsExpand(\'ts_d'+d+'_ts'+reorgStart+'\')"><div class="fleft ts_rb" id="ts_d'+d+'_ts'+reorgStart+'_rbCont"></div><div class="fleft tsNoDeliv"><b>No Deliveries</b></div></div>';
			}
		}
		//now hide extra tds
		for (var j = reorgStart+1; j < reorgEnd+1; j++) {
			if ($('ts_d'+d+'_ts'+j)) {
				$('ts_d'+d+'_ts'+j).hide();
			}
		}
	}

	return true;
}

function tsExpand(elemIdArg, autoCheckRadioArg, retryArg) {
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
		//solve display issues
		while (!solveDisplay('ts_d-1_ts0', autoCheckRadio)) { };
		while (!solveDisplay('ts_d9999_ts0', autoCheckRadio)) { };
		while (!solveDisplay(elemId, autoCheckRadio)) { };

		var day = parseDay(elemId); //should now be the day index
		
		var refDataCur = refData;
		if (day >= 10) {
			refDataCur = refAdvData;
		}

		for (var t = 0; t < refDataCur[day][0].length; t++) {
			if ($('ts_d'+day+'_ts'+t) && $('ts_d'+day+'_ts'+t).style.display != 'none') {
				elemId = 'ts_d'+day+'_ts'+t;
			}
		}

		if ($(elemId)) {
			if ( parseDay(elemId) < 10) { globalTS  = elemId; }
			$(elemId).morph(styleStrExp);
			//hide bottom border on final ts of displayed ts
			$(elemId).style.borderBottomWidth = '0';
		}
	}else{
		if (retry) {
			//run through solve display and try again
			while (!solveDisplay(elemId)) {}; //solve display issues
			tsExpand(elemId, autoCheckRadio, false);
		}else{
			while (!solveDisplay(elemId)) {}; //solve display issues
		}
	}
}

/*
 *	parentId = the id of the top-level container (can be itself)
 *		check via descendantOf, so does not need to be direct child
 *	exceptId = an id of a child div under the parentId to NOT contract
 */
function tsContractAll(parentIdArg, exceptIdArg) {
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
	var conExcepDay = (exceptId).split('_')[1]; //should now be 'd#'

	for (var i=0; i< refs.length; i++) {
		if ($(refs[i]).id != exceptId && ($(refs[i]).descendantOf(parentId) || $(refs[i]).id == parentId) ) {
			//skip exceptions
			if (($(refs[i]).id).indexOf(conExcepDay) == -1) {
				$(refs[i]).morph(styleStrCon);
			}
			//children
			var refsChildren = $(refs[i]).getElementsByTagName('td');
			for (var j=0; j< refsChildren.length; j++) {
				//skip exceptions
				if (($(refsChildren[j]).id).indexOf(conExcepDay) == -1) {
					$(refsChildren[j]).morph(styleStrCon);
				}
			}
		}
	}

}

function defaultColumnExpand(dayIndex, slotIndex) {
	tsExpand('ts_d'+dayIndex+'_ts'+slotIndex);
}


function hideAdvanceOrder() { 
	$("timeslots_grid0").toggle();

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

var timer;
var timedExpandWait = 1000; //ms
clickOnly = false;

/* add even listeners for mouse over expands, needs to be on window load for refData */
Event.observe(window, 'load', function() {
	var refDataCur = null;

	//add adv ts events (if they exist)
	if (window.refAdvData !== undefined) {
		refDataCur = refAdvData;
		for (d = 0; d < refDataCur.length; d++) {
			if (refDataCur[d] === undefined) { continue; }
			
			//headers
			createMouseOverOutEvents('ts_d'+d+'_ts_header', 'tsContainer');
			//footer
			createMouseOverOutEvents('ts_d'+d+'_ts_footer', 'tsContainer');
			//ts's and co's
			for (t = 0; t < refDataCur[d][0].length; t++) {
				createMouseOverOutEvents('ts_d'+d+'_ts'+t, 'tsContainer');
				createMouseOverOutEvents('co_d'+d+'_ts'+t, 'tsContainer');
			}
		}
	}

	//add ts events
	if (window.refData !== undefined) {
		refDataCur = refData;
		for (d = 0; d < refDataCur.length; d++) {
			if (refDataCur[d] === undefined) { continue; }
			
			//headers
			createMouseOverOutEvents('ts_d'+d+'_ts_header', 'tsContainer');
			//footer
			createMouseOverOutEvents('ts_d'+d+'_ts_footer', 'tsContainer');
			//ts's and co's
			for (t = 0; t < refDataCur[d][0].length; t++) {
				createMouseOverOutEvents('ts_d'+d+'_ts'+t, 'tsContainer');
				createMouseOverOutEvents('co_d'+d+'_ts'+t, 'tsContainer');
			}
		}
	}

});

function createMouseOverOutEvents(elemIdArg, parentIdArg) {
	var elemId = elemIdArg || '';
	var parentIdArg = parentIdArg || '';

	if ($(elemId)) {
		$(elemId).observe('mouseover', setter.bindAsEventListener(elemId, parentIdArg, elemId));
		$(elemId).observe('mouseout', forgetter);
		$(elemId).observe('click', forgetter);
	}
}

function setter(e, set01, set02) {
	if (!clickOnly) {
		set_01 = set01;
		set_02 = set02;
		timer=setTimeout('timedExpand()', timedExpandWait);
	}
}

function forgetter() {
	set_01 = '';
	set_02 = '';
	clearTimeout(timer);
}

function timedExpand() {
	tsContractAll(set_01, set_02);
	tsExpand(set_02, false);
	forgetter();
}