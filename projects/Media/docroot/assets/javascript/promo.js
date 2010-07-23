/* requires protoype.js */

/* page helper methods */
	/* delete all for profile values */
	function deleteAllProfiles(parentId) {
		var parent = $(parentId);
		if (parent == null) { return; }
		var children = parent.getElementsByTagName('a');

		//remove all links except "Delete All"
		while(children.length > 1) {
			clickAllHREF(parentId);
		}
	}
	
	/*
	 *	execute onClick for all HREFs under a parent elem.
	 *
	 *	if includeClassName is passed, elems must have that className
	 *	to be clicked
	 *
 	 *	if excludeClassNames is passed, elems with that className
	 *	will be skipped. (Defaults to "clickAllExclude" if not passed.)
	 *
	 *	multiple include/exclude classnames can be passsed either as an
	 *	Array of strings, or as a single, comma-seperated string.
	 *
	 *	clickInvisElems is a boolean that determines if invisible elems
	 *	qualify for clicks (regardless of inclusion).
	 */
	function clickAllHREF(parentId, includeClassNames, excludeClassNames, clickInvisElems) { 
		var parent = $(parentId);
		if (parent == null) { return; }
		var excludeClassNames = excludeClassNames || 'clickAllExclude';
		var includeClassNames = includeClassNames || '';
		var clickInvisElems = clickInvisElems || false;

		var children = parent.getElementsByTagName('a');

		for (var i=children.length-1; i>0; i--) {

			if (parent.getElementsByTagName('a').length-1<i) { continue; }

			var foundExclude = false;
			var foundInclude = true;

			if (includeClassNames != '') {
				foundInclude = false;
				
				if (Object.isString(includeClassNames)) {
					includeClassNames = includeClassNames.split(",");
				}

				for (var j=0; j<includeClassNames.length; j++) {
					if ((children[i].className).indexOf(includeClassNames[j]) >= 0) {
						foundInclude = true;
					}
				}
			}

			if (excludeClassNames != '') {
				if (Object.isString(excludeClassNames)) {
					excludeClassNames = excludeClassNames.split(",");
				}

				for (var k=0; k<excludeClassNames.length; k++) {
					if ((children[i].className).indexOf(excludeClassNames[k]) >= 0) {
						foundExclude = true;
					}
				}
			}

			if (foundInclude && !foundExclude) {
				if (children[i].scrollWidth == 0) {
					if (clickInvisElems) {
						clickLink(children[i]);
					}
				}else{
					clickLink(children[i]);
				}
			}
		}

		return true;
	}

	function clickLink(linkobj) {
		if (linkobj.getAttribute('onclick') == null) {
			if (linkobj.getAttribute('href')) document.location = linkobj.getAttribute('href');
		}else{
			linkobj.onclick();
		}

		return true;
	}

/* profile add/updates */
	function ShortCut(name, attribute, value) {
		this.name = name;
		this.attribute = attribute;
		this.value = value;
	}

	var shortcuts = new Array();
	shortcuts['sc_chefstable'] = new ShortCut('Chefs Table', 'ChefsTable','1');
	shortcuts['sc_vip'] =        new ShortCut('VIP/Reserved Dlv', 'VIPCustomer','true');
	shortcuts['sc_inactive'] =   new ShortCut('Inactive', 'MetalCategory','0');
	shortcuts['sc_gold'] =       new ShortCut('Gold', 'MetalCategory','1');
	shortcuts['sc_silver'] =     new ShortCut('Silver', 'MetalCategory','2');
	shortcuts['sc_bronze'] =     new ShortCut('Bronze', 'MetalCategory','3');
	shortcuts['sc_copper'] =     new ShortCut('Copper', 'MetalCategory','4');
	shortcuts['sc_tin'] =        new ShortCut('Tin', 'MetalCategory','5');
	shortcuts['sc_new'] =        new ShortCut('New', 'MetalCategory','6');
	

	var rollingIndex = 0;

	function addProfile(toFormId) {
		var profileValueFld = $('profile_value');
		var profileNameFld = $('profile_name');
		var tmpANDCondition = $('profile_condition0');
		var tmpConditionValue = 'OR';
		if(tmpANDCondition.checked) {
			tmpConditionValue = 'AND';
		}

		$('profileOperator').value = tmpConditionValue;

		if(profileNameFld != null && profileNameFld.value.length > 0 
			&& profileValueFld != null && profileValueFld.value.length > 0) {
			addProfileRow(tmpConditionValue, profileNameFld.value, profileValueFld.value, toFormId);
			profileNameFld.selectedIndex=0;
			profileValueFld.value='';
		}
		for( var i in shortcuts ) {
			//if ($(i)) {
				if(!$(i)) { continue; }
				if($(i).checked) {
					addProfileRow(tmpConditionValue, shortcuts[i].name, shortcuts[i].attribute, shortcuts[i].value, toFormId);
					$(i).checked = false;
				}
			//}
		}

		reArrangeCondition();
	}

	function addProfileRow(cellCondition, cellName, cellAttribute, cellValue, toFormId) {
		if( document.createElement && document.childNodes ) {
			var profileTableFld = $('profileListTB').tBodies[0];
			if(profileTableFld != null) {
					var row = document.createElement('tr');
					rollingIndex++;
					var tmpID = 'attributeList['+rollingIndex+']';
					row.id = tmpID;
					var td1 = document.createElement('td');
						td1.appendChild(document.createTextNode(cellCondition));
						td1.className = "bordLgrayDash padL8R16";
					var td2 = document.createElement('td');
						td2.appendChild (document.createTextNode(cellName));
						td2.className = "bordLgrayDash padL8R16";
					var td3 = document.createElement('td');
						td3.appendChild (document.createTextNode(cellAttribute));
						td3.className = "bordLgrayDash padL8R16";
					var td4 = document.createElement('td');
						td4.appendChild (document.createTextNode(cellValue));
						td4.className = "bordLgrayDash padL8R16 alignC";
					var tdDelete = document.createElement('a');
						tdDelete.innerHTML = 'Delete';
						tdDelete.setAttribute("onclick", "javascript:deleteProfile('"+tmpID+"');");
						tdDelete.href = "javascript:deleteProfile('"+tmpID+"');";
						tdDelete.className = "greenLink padL8R16 alignC";
					var td5 = document.createElement('td');
						td5.appendChild (tdDelete);
					row.appendChild(td1);
					row.appendChild(td2);
					row.appendChild(td3);
					row.appendChild(td4);
					row.appendChild(td5);
					profileTableFld.appendChild(row);
					createHiddenInput(rollingIndex, cellAttribute, cellValue, cellCondition, toFormId);
			}
		}
	}

	function createHiddenInput(indexVal, cellName, cellValue, cellCondition, toFormId) {
		
		var tmpId = 'attributeList['+indexVal+']'+'.';
		var newElementName = document.createElement("input");
		newElementName.setAttribute("type", "hidden");
		newElementName.setAttribute("name", tmpId+'attributeName');
		newElementName.setAttribute("id", tmpId+'attributeName');
		newElementName.setAttribute("value", cellName);
		
		var newElementValue = document.createElement("input");
		newElementValue.setAttribute("type", "hidden");
		newElementValue.setAttribute("name", tmpId+'desiredValue');
		newElementValue.setAttribute("id", tmpId+'desiredValue');
		newElementValue.setAttribute("value", cellValue);

		//$('profileContainer').appendChild(newElementName);
		//$('profileContainer').appendChild(newElementValue);
		
		var toFormId = toFormId || '';
		var toForm = null;
		if (toFormId != '') { toForm = $(toFormId) }else{ toForm = document.forms[0]; }
		if (toForm == null) {
			alert('Error: Cannot add profile values to non-existent form with ID: "'+toFormId+'".');
		}else{
			toForm.appendChild(newElementName);
			toForm.appendChild(newElementValue);
		}
	}

	function deleteProfile(theCell) {
		var profileTableFld = $('profileListTB');
		var profileTableRow = $(theCell);
		
		if( document.createElement && document.childNodes 
				&& profileTableFld != null 
				&& profileTableRow != null) {

			var rowIndex = profileTableRow.rowIndex;
			var hiddenNameElement = $(profileTableRow.id+'.attributeName');
			var hiddenValueElement = $(profileTableRow.id+'.desiredValue');
			var parentElementNode = hiddenNameElement.parentNode;

			parentElementNode.removeChild(hiddenNameElement);
			parentElementNode.removeChild(hiddenValueElement);
			profileTableFld.deleteRow(rowIndex);
			reArrangeCondition();
		}
	}
	
	function reArrangeCondition() {
		var profileTableFld = $('profileListTB').tBodies[0];
		var profileTableCondition = $('profileOperator').value;
		var allTrElements = profileTableFld.getElementsByTagName("tr");
		for ( var i=0; i < allTrElements.length; i++ ) {
			var firstCell = allTrElements[i].getElementsByTagName("td")[0];
			if(i == 0) {
				firstCell.innerHTML = '&nbsp;';
			} else {
				firstCell.innerHTML = profileTableCondition;
			}
		}
	}


/* Calendar(s) setup */
	function setupCals(whichCals) {
		switch (whichCals) {
			case "edit_basic":
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_start_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_start_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_start_callback
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_start_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_startTime_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_start_callback
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_endSingle_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_endSingle_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_endSingle_callback
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_endSingle_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_endSingleTime_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_endSingle_callback
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_endRolling_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_endRolling_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_endRolling_callback
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_endRolling_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_endRollingTime_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_endRolling_callback
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_endRedemption_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_endRedemption_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_endRedemption_callback
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_basic_cal_endRedemption_hidden",
						ifFormat : "%m/%d/%Y %I:%M %p",
						singleClick: true,
						button : "edit_basic_cal_endRedemptionTime_trigger",
						showsTime: true,
						timeFormat: "12",
						onUpdate: edit_basic_cal_endRedemption_callback
					}
				);
				break;
			case "edit_custreq":
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_custreq_cal_dlvPassStart_in",
						ifFormat : "%m/%d/%Y",
						singleClick: true,
						button : "edit_custreq_cal_dlvPassStart_trigger"
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "edit_custreq_cal_dlvPassEnd_in",
						ifFormat : "%m/%d/%Y",
						singleClick: true,
						button : "edit_custreq_cal_dlvPassEnd_trigger"
					}
				);
				break;
			case "edit_dlvreq":
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "dlvDateInStartDate",
						ifFormat : "%m/%d/%Y",
						singleClick: true,
						button : "dlvDateInStartDate_trigger"
					}
				);
				Calendar.setup(
					{
						showsTime : false,
						electric : false,
						inputField : "dlvDateInEndDate",
						ifFormat : "%m/%d/%Y",
						singleClick: true,
						button : "dlvDateInEndDate_trigger"
					}
				);
				//init week (even though it starts hidden)
				var dow = new Array("MON","TUE","WED","THU","FRI","SAT","SUN");
				for (var i = 0; i < dow.length; i++) {
					Calendar.setup(
						{
							showsTime : false,
							electric : false,
							inputField : 'dlvTimeIn'+dow[i]+'StartTime',
							ifFormat : "%I:%M %p",
							singleClick: true,
							button : 'dlvTimeIn'+dow[i]+'StartTime_trigger',
							showsTime: true,
							timeFormat: "12"
						}
					);
					Calendar.setup(
						{
							showsTime : false,
							electric : false,
							inputField : 'dlvTimeIn'+dow[i]+'EndTime',
							ifFormat : "%I:%M %p",
							singleClick: true,
							button : 'dlvTimeIn'+dow[i]+'EndTime_trigger',
							showsTime: true,
							timeFormat: "12"
						}
					);
				}
				break;
		}
	}

/* callback functions */
	function edit_basic_cal_start_callback(cal) {
		dateTimeSplitter(cal, 'edit_basic_cal_start_in', 'edit_basic_cal_startTime_in');
	}
	function edit_basic_cal_endSingle_callback(cal) {
		dateTimeSplitter(cal, 'edit_basic_cal_endSingle_in', 'edit_basic_cal_endSingleTime_in');
	}
	function edit_basic_cal_endRolling_callback(cal) {
		dateTimeSplitter(cal, 'edit_basic_cal_endRolling_in', 'edit_basic_cal_endRollingTime_in');
	}
	function edit_basic_cal_endRedemption_callback(cal) {
		dateTimeSplitter(cal, 'edit_basic_cal_endRedemption_in', 'edit_basic_cal_endRedemptionTime_in');
	}

/* put values from cal OBJ into correct elements (used by callbacks) */
	function dateTimeSplitter(cal, dateElemId, timeElemId) {
		var date = cal.date;
		
		field = $(dateElemId);
		field.value = date.print("%m/%d/%Y");
		field = $(timeElemId);
		field.value = date.print("%I:%M %p");
	}

/* add/edit */
	var index = function (initQty, initElem) {
		this.qty;
		this.initd = false;
		this.updElemId;

		this.init = function () {
			if (!this.initd) {
				this.initd = true;

				(isNaN(initQty))
					?this.qty = 0
					:this.qty = initQty;

				if ($(initElem)) { this.updElemId = initElem; }
			}
		}
		this.upd = function () {
			if ($(this.updElemId)) { $(this.updElemId).value = this.qty; } 
		}
		this.inc = function () { this.qty++; this.upd(); }
		this.dec = function () { this.qty--; this.upd(); }
		this.emp = function () { this.qty = 0; this.upd(); }

		this.init();
	}
	/*
	 *	Edit/Cancel toggle
	 *
	 *	takes a baseId, and uses it to construct other ids.
	 *	assume it's baseLink[Edit|Cancel]
	 *	and the containers are baseEditCont and baseCancelCont
	 *
	 *	where base = any uinque identifier like 'id'+(new Date().getTime());
	 */
	function editCancelToggle(baseId, index) {
		var baseId = baseId || '';

		if (baseId == '' || !$(baseId)) { return false; }
		
		//seperate out the base
		baseId = baseId.replace("LinkEdit", "");
		baseId = baseId.replace("LinkCancel", "");
		
		if (index>=0 && baseId.indexOf('[') >0) { baseId = baseId.substring(0, baseId.indexOf('[')); }

		//toggle the base
		var editElem = baseId+'EditCont';
			if (index>=0) { editElem += '['+index+']'; }
		var canElem = baseId+'CancelCont';
			if (index>=0) { canElem += '['+index+']'; }

		$(editElem).toggle();
		$(canElem).toggle();

		return true;
	}
	/* delete row */
	function delRow(fromTableId, baseId, index) {
		var fromTableId = fromTableId || '';
		var baseId = baseId || '';

		if (fromTableId == '' || !$(fromTableId) || baseId == '') { return false; }

		var profileTableFld = $(fromTableId).tBodies[0];

		
		var editElem = baseId+'EditCont';
			if (index>=0) { editElem += '['+index+']'; }
		var canElem = baseId+'CancelCont';
			if (index>=0) { canElem += '['+index+']'; }

		profileTableFld.removeChild($(editElem));
		profileTableFld.removeChild($(canElem));

		//hide table if all data rows are deleted
		if (!profileTableFld.hasChildNodes()) {
			$(fromTableId).toggle();
		}else if (profileTableFld.childNodes.length == 1 && profileTableFld.firstChild.nodeType == 3 ) { 
			$(fromTableId).toggle();
		}

		return true;
	}
	/* add rows helper */
	//clearAddBaseId should always be true
	function addRows(toTableId, addBaseId, baseId, clearAddBaseId, indexObj, valuesArray) {
		
		var startValue = '';
		var endValue = '';
		for (var i = 0; i<valuesArray.length; i++) {
			if (startValue=='') {
				//set start
				startValue = valuesArray[i];
			}else if (endValue=='') {
				//set end
				endValue = valuesArray[i];
			}
			if (startValue!='' && endValue != '') {
				if ($(addBaseId+'StartDate') || $(addBaseId+'EndDate')) {
					//place vals
					$(addBaseId+'StartDate').value = startValue;
					$(addBaseId+'EndDate').value = endValue;
					//clear vals
					startValue = '';
					endValue = '';
					//call add row
					addRow(toTableId, addBaseId, baseId, clearAddBaseId, indexObj);
				}
				if ($(addBaseId+'StartTime') && $(addBaseId+'EndTime')) {
					//place vals
					$(addBaseId+'StartTime').value = startValue.split("_")[1];
					$(addBaseId+'EndTime').value = endValue.split("_")[1];
					//clear vals
					startValue = '';
					endValue = '';
					//call add row
					addRow(toTableId, addBaseId, baseId, clearAddBaseId, indexObj);
				}
			}
		}
	}
	/*
	 *	Add row
	 *
	 */
	function addRow(toTableId, addBaseId, baseId, clearAddBaseId, indexObj) {

		var toTableId = toTableId || '';
			if (toTableId == '' || !$(toTableId)) { return false; }
		
		//show table if hidden
			if ($(toTableId).style.display == 'none') { $(toTableId).style.display = 'block'; }

		var baseId = baseId;
		var index = -1;
		if (indexObj) { index = indexObj.qty; }
		
		if (baseId == '' && (index == null || index < 0)) { baseId = 'ran'+(new Date().getTime()); }

		var addBaseId = addBaseId || '';

		var clearAddBaseId = clearAddBaseId || false;

		if ( document.createElement && document.childNodes) {
			var profileTableFld = $(toTableId).tBodies[0];
			if(profileTableFld != null) {
				var tempTd;
				var tempSpan;
				var tempA;

				var elemRefCheck = baseId+'EditCont';
					if (index>=0) { elemRefCheck += '['+index+']'; }

				if (!$(elemRefCheck)) {
					//build out edit row (no modify)
					var rowEditCont = document.createElement('tr');
						rowEditCont.id = baseId+'EditCont';
						if (index>=0) { rowEditCont.id = rowEditCont.id+'['+index+']'; }
						
							tempTd = document.createElement('td');
							var EditContTd1Id = baseId+'EditContTd1';
								if (index>=0) { EditContTd1Id += '['+index+']'; }
							tempTd.id = EditContTd1Id;
							tempTd.className = 'bordLgrayDash padL8R16';
						
						rowEditCont.appendChild(tempTd);
						
							tempTd = document.createElement('td');
							var EditContTd2Id = baseId+'EditContTd2';
								if (index>=0) { EditContTd2Id += '['+index+']'; }
							tempTd.id = EditContTd2Id;
							tempTd.className = 'bordLgrayDash padL8R16';
						
						rowEditCont.appendChild(tempTd);
					
					profileTableFld.appendChild(rowEditCont);

					var initStartVal = '';
					var initEndVal = '';
					var triggerIcon = '';
					var dateOrTime = '';

					//dates
						if ($(addBaseId+'StartDate')) {
							dateOrTime = 'Date';
							initStartVal = $(addBaseId+'Start'+dateOrTime).value;
							if (clearAddBaseId) { $(addBaseId+'Start'+dateOrTime).value = '' }
							triggerIcon = '/media_stat/crm/images/calendar.gif';
						}
						if ($(addBaseId+'EndDate')) {
							dateOrTime = 'Date';
							initEndVal = $(addBaseId+'End'+dateOrTime).value;
							if (clearAddBaseId) { $(addBaseId+'End'+dateOrTime).value = '' }
							triggerIcon = '/media_stat/crm/images/calendar.gif';
						}
					//times
						if ($(addBaseId+'StartTime')) {
							dateOrTime = 'Time';
							initStartVal = $(addBaseId+'Start'+dateOrTime).value;
							if (clearAddBaseId) { $(addBaseId+'Start'+dateOrTime).value = '' }
							triggerIcon = '/media_stat/crm/images/blue_clock.gif';
						}
						
						if ($(addBaseId+'EndTime')) {
							dateOrTime = 'Time';
							initEndVal = $(addBaseId+'End'+dateOrTime).value;
							if (clearAddBaseId) { $(addBaseId+'End'+dateOrTime).value = '' }
							triggerIcon = '/media_stat/crm/images/blue_clock.gif';
						}

					//Edit built. set values
						//set initCal
						var initCalSrcStart = 'initCal(\''+baseId+'Start'+dateOrTime+'_in\', \'\', \'\', '+index+');';
						var initCalSrcEnd = 'initCal(\''+baseId+'End'+dateOrTime+'_in\', \'\', \'\', '+index+');';
						var widthClass = "w100px";
						var widthClassEditDel = "w75px";

						if (dateOrTime == 'Time') { 
							initCalSrcStart = 'initCal(\''+baseId+'Start'+dateOrTime+'_in\', \'%I:%M %p\', true, '+index+');';
							initCalSrcEnd = 'initCal(\''+baseId+'End'+dateOrTime+'_in\', \'%I:%M %p\', true, '+index+');';
							widthClass = "w100px";
							widthClassEditDel = "w50px";
						}

						var EditContTd1InnerHTML = '<div class="'+widthClass+' fleft" style="height: 22px;" id="'+baseId+'Start'+dateOrTime+'_disp';
							if (index>=0) { EditContTd1InnerHTML += '['+index+']'; }
							EditContTd1InnerHTML += '">'+initStartVal+'</div><div class="fleft gray"><img src="/media_stat/crm/images/clear.gif" width="1" height="15" alt="" />&nbsp;&nbsp;to&nbsp;&nbsp;</div><div class="'+widthClass+' fleft" id="'+baseId+'End'+dateOrTime+'_disp';
							if (index>=0) { EditContTd1InnerHTML += '['+index+']'; }
							EditContTd1InnerHTML += '">'+initEndVal+'</div>';

						var EditContTd2InnerHTML = '<div class="'+widthClassEditDel+' fleft"><img src="/media_stat/crm/images/clear.gif" width="0" height="16" alt="" /><a href="#" onclick="'+initCalSrcStart+initCalSrcEnd+' editCancelToggle(this.id, '+index+'); return false;" id="'+baseId+'LinkEdit';
							if (index>=0) { EditContTd2InnerHTML += '['+index+']'; }
							EditContTd2InnerHTML += '" class="greenLink">Edit</a></div><div class="'+widthClassEditDel+' fleft"><img src="/media_stat/crm/images/clear.gif" width="1" height="15" alt="" /><a href="#" onclick="delRow(\''+toTableId+'\', \''+baseId+'\', '+index+');" class="greenLink DelAllInclude">Delete</a></div>';

						$(EditContTd1Id).innerHTML = EditContTd1InnerHTML;
						$(EditContTd2Id).innerHTML = EditContTd2InnerHTML;
				}

				elemRefCheck = baseId+'CancelCont';
					if (index>=0) { elemRefCheck += '['+index+']'; }

				if (!$(baseId+'CancelCont')) {
					//build out cancel row (modify)
					var rowCancelCont = document.createElement('tr');
						rowCancelCont.id = baseId+'CancelCont';
							if (index>=0) { rowCancelCont.id = rowCancelCont.id+'['+index+']'; }
						rowCancelCont.setAttribute('style', 'display: none;');
						
							tempTd = document.createElement('td');
							var CancelContTd1Id = baseId+'CancelContTd1';
								if (index>=0) { CancelContTd1Id += '['+index+']'; }
							tempTd.id = CancelContTd1Id;
							tempTd.className = 'bordLgrayDash padL8R16';
						
						rowCancelCont.appendChild(tempTd);
						
							tempTd = document.createElement('td');
							var CancelContTd2Id = baseId+'CancelContTd2';
								if (index>=0) { CancelContTd2Id += '['+index+']'; }
							tempTd.id = CancelContTd2Id;
							tempTd.className = 'bordLgrayDash padL8R16';
						
						rowCancelCont.appendChild(tempTd);
					
					profileTableFld.appendChild(rowCancelCont);

					//Cancel built. set values
						widthClass = "w75px";
					
						//cals are not being init'd here, only on an 'edit' link click...

						var CancelContTd1InnerHTML = '';
						var CancelContTd2InnerHTML = '';

						CancelContTd1InnerHTML = '<input type="text" id="'+baseId+'Start'+dateOrTime+'_in';
							if (index>=0) { CancelContTd1InnerHTML += '['+index+']'; }
						CancelContTd1InnerHTML += '" name="'+baseId+'Start'+dateOrTime+'_in';
							if (index>=0) { CancelContTd1InnerHTML += '['+index+']'; }
						CancelContTd1InnerHTML += '" value="'+initStartVal+'" class="'+widthClass+'" /> <a href="#" onclick="return false;" class="promo_ico_cont clickAllExclude" id="'+baseId+'Start'+dateOrTime+'_in_trigger';
							if (index>=0) { CancelContTd1InnerHTML += '['+index+']'; }
						CancelContTd1InnerHTML += '"><img src="'+triggerIcon+'" width="16" height="16" alt="" /></a><span class="gray">&nbsp;&nbsp;to&nbsp;&nbsp;</span><input type="text" id="'+baseId+'End'+dateOrTime+'_in';
							if (index>=0) { CancelContTd1InnerHTML += '['+index+']'; }
						CancelContTd1InnerHTML += '" name="'+baseId+'End'+dateOrTime+'_in';
							if (index>=0) { CancelContTd1InnerHTML += '['+index+']'; }
						CancelContTd1InnerHTML += '" value="'+initEndVal+'" class="'+widthClass+'" /> <a href="#" onclick="return false;" class="promo_ico_cont clickAllExclude" id="'+baseId+'End'+dateOrTime+'_in_trigger';
							if (index>=0) { CancelContTd1InnerHTML += '['+index+']'; }
						CancelContTd1InnerHTML += '"><img src="'+triggerIcon+'" width="16" height="16" alt="" /></a>';

						CancelContTd2InnerHTML = '<div class="'+widthClassEditDel+' fleft"><img src="/media_stat/crm/images/clear.gif" width="1" height="15" alt="" /><a href="#" onclick="doSave(\''+baseId+'\', '+indexObj.qty+'); editCancelToggle(\''+baseId+'LinkCancel\', '+indexObj.qty+'); return false;" class="greenLink SaveAllInclude">Save</a></div><div class="'+widthClassEditDel+' fleft"><a href="#" onclick="editCancelToggle(this.id, '+indexObj.qty+'); return false;" id="'+baseId+'LinkCancel" class="greenLink CanAllInclude">Cancel</a></div>'
					
					
						$(CancelContTd1Id).innerHTML = CancelContTd1InnerHTML;
						$(CancelContTd2Id).innerHTML = CancelContTd2InnerHTML;
				}

				//increase index count
				indexObj.inc();
			}
		}
	}
	/* dynamically initiate cals */
	function initCal(baseId, ifFormat, showsTime, index) {

		var baseId = baseId || '';
		var index = index;

		//at minimum baseId and baseId_trigger elems should exist
		if (index>=0) {
			if (baseId == '' || !$(baseId+'['+index+']') || !$(baseId+'_trigger'+'['+index+']')) { return false; }
		} else {
			if (baseId == '' || !$(baseId) || !$(baseId+'_trigger')) { return false; }
		}

		var ifFormat = ifFormat || '%m/%d/%Y'; //"%m/%d/%Y %I:%M %p"
		var showsTime = showsTime || false;
		var onUpdateCallback = onUpdateCallback || null;
		
		var triggerId = baseId+"_trigger";
			if (index>=0){ triggerId+='['+index+']'; }
		
		if (index>=0){ baseId+='['+index+']'; }

		Calendar.setup(
			{
				showsTime : false,
				electric : false,
				inputField : baseId,
				ifFormat : ifFormat,
				singleClick: true,
				button : triggerId,
				showsTime: showsTime,
				timeFormat: "12"
			}
		);
	}

	/* take a baseId and populate values across other inputs */
	function doSave(baseId, index) {

		var baseId = baseId || '';
		var index = index;

		var dispId = baseId+'StartDate_disp';
			if (index>=0){ dispId+='['+index+']'; }
		var inId = baseId+'StartDate_in';
			if (index>=0){ inId+='['+index+']'; }

		if ($(dispId) && $(inId)) { $(dispId).innerHTML = $(inId).value; }
		
		var dispId = baseId+'EndDate_disp';
			if (index>=0){ dispId+='['+index+']'; }
		var inId = baseId+'EndDate_in';
			if (index>=0){ inId+='['+index+']'; }

		if ($(dispId) && $(inId)) { $(dispId).innerHTML = $(inId).value; }

		var dispId = baseId+'StartTime_disp';
			if (index>=0){ dispId+='['+index+']'; }
		var inId = baseId+'StartTime_in';
			if (index>=0){ inId+='['+index+']'; }

		if ($(dispId) && $(inId)) { $(dispId).innerHTML = $(inId).value; }
		
		var dispId = baseId+'EndTime_disp';
			if (index>=0){ dispId+='['+index+']'; }
		var inId = baseId+'EndTime_in';
			if (index>=0){ inId+='['+index+']'; }

		if ($(dispId) && $(inId)) { $(dispId).innerHTML = $(inId).value; }

	}

	/* form submits */
	function editPromotionSubmit(promocode){
		document.location.href="/promotion/promo_edit.jsp?promoId="+promocode;
	}
	function activityLogSubmit(promocode){
		document.location.href="/promotion/promo_activity.jsp?promoId="+promocode;
	}
	function editPromotionBasicSubmit(promocode){
		document.location.href="/promotion/promo_edit_basic.jsp?promoId="+promocode;
	}
	function editPromotionOfferSubmit(promocode){
		document.location.href="/promotion/promo_edit_offer.jsp?promoId="+promocode;
	}
	function editPromotionCustReqSubmit(promocode){
		document.location.href="/promotion/promo_edit_customer.jsp?promoId="+promocode;
	}
	function editPromotionCartReqSubmit(promocode){
		document.location.href="/promotion/promo_edit_cart.jsp?promoId="+promocode;
	}
	function promotionDlvReqSubmit(promocode){
		document.location.href="/promotion/promo_edit_delivery.jsp?promoId="+promocode;
	}
	function promotionPaymentReqSubmit(promocode){
		document.location.href="/promotion/promo_edit_payment.jsp?promoId="+promocode;
	}
	function promotionDetailSubmit(promocode){
		document.location.href="/promotion/promo_details.jsp?promoId="+promocode;
	}
	function promotionListView(){
		document.location.href="/main/promo_home.jsp";
	}
	function editPromotionHoldStatus(form,promocode){
		form.action ="/promotion/promo_details.jsp?promoId="+promocode;
	    document.getElementById("actionName").value="holdStatus";
		//document.location.href="/promotion/promo_details.jsp?promoId="+promocode+"&actionName=holdStatus";
		form.submit();
	}
	function editPromotionStatus(form,promocode,status){
		document.getElementById("frmPromoDetails").action ="/promotion/promo_details.jsp?promoId="+promocode;
		document.getElementById("actionName").value="changeStatus";
		document.getElementById("status").value=status;
		document.getElementById("frmPromoDetails").submit();
	}

function doPublish(promocode) {
	var uri = '/api/promo.jsp?action=publish&promoCode='+promocode;
	$C.asyncRequest('GET', uri, {
		success: function(resp) {
			if ('true' == resp.responseText) {
				alert('Promotion successfully published.');
			} else {
				alert('Publish failed.');
			}
			window.location.reload();
		},
		failure: function(resp) {
			alert('An error has occured during publish.');
			window.location.reload();
		}
	});
}
function doCheckCode(promocode) {
	var feedback_ok = $('code-check-ok-div');
	var feedback_err = $('code-check-err-div');
	feedback_ok.style.display = "none";
	feedback_err.style.display = "none";
	
	var uri = '/api/promo.jsp?action=checkPromoCode&promoCode='+promocode;
	$C.asyncRequest('GET', uri, {
		success: function(resp) {
			var feedback_ok = $('code-check-ok-div');
			var feedback_err = $('code-check-err-div');
		
			if ('true' == resp.responseText) {
				feedback_err.style.display = "";
			} else {
				feedback_ok.style.display = "";
			}
		},
		failure: function(resp) {
			alert('An error has occured while code was checked.');
		}
	});
}

function doCheckName(promoname) {
	var feedback_ok = $('name-check-ok-div');
	var feedback_err = $('name-check-err-div');
	feedback_ok.style.display = "none";
	feedback_err.style.display = "none";
	
	var uri = '/api/promo.jsp?action=checkPromoName&promoName='+promoname;
	$C.asyncRequest('GET', uri, {
		success: function(resp) {
			var feedback_ok = $('name-check-ok-div');
			var feedback_err = $('name-check-err-div');
		
			if ('true' == resp.responseText) {
				feedback_err.style.display = "";
			} else {
				feedback_ok.style.display = "";
			}
		},
		failure: function(resp) {
			alert('An error has occured while code was checked.');
		}
	});
}

function doCheckRedemptionCode(rcode) {
	var feedback_ok = $('rcode-check-ok-div');
	var feedback_err = $('rcode-check-err-div');
	feedback_ok.style.display = "none";
	feedback_err.style.display = "none";
	
	var uri = '/api/promo.jsp?action=checkRedemptionCode&redemptionCode='+rcode;
	$C.asyncRequest('GET', uri, {
		success: function(resp) {
			var feedback_ok = $('rcode-check-ok-div');
			var feedback_err = $('rcode-check-err-div');
		
			if ('true' == resp.responseText) {
				feedback_err.style.display = "";
			} else {
				feedback_ok.style.display = "";
			}
		},
		failure: function(resp) {
			alert('An error has occured while code was checked.');
		}
	});
}
