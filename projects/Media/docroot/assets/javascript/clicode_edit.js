if (typeof FreshDirect == "undefined" || !FreshDirect) {
    var FreshDirect = {};
}

(function() {
	var ClientCodes = {};
	
	FreshDirect.ClientCodes = ClientCodes;

	var numbers = "0123456789";

	var _PREFIX = "clicode_";

	var trs = [];

	var ccs = {};

	var sums = {};

	var noCodes = {};

	var lastIndexes = {};

	var sortedKeys = function(hash) {
		var a = [];
		for (var key in hash)
			if (hash.hasOwnProperty(key))
				a.push(parseInt(key, 10));
		a.sort(function(a,b) { return a - b; });
		return a;
	};

	var parseQuantity = function(value) {
		var val = YAHOO.lang.trim(value);
		var quantity = parseInt(YAHOO.lang.trim(val), 10);
		if (!isNaN(quantity)) {
			for (var i = 0; i < val.length; i++) {
				if (numbers.indexOf(val.charAt(i)) == -1) {
					quantity = Number.NaN;
					break;
				}
			}
		}
		return quantity;
	};
		
    var get = function(id) {
        return document.getElementById(_PREFIX + id);
    };

	ClientCodes.dump = function() {
		alert(YAHOO.lang.JSON.stringify(ccs));
		return true;
	};

	var showBlock = function(id) {
		var item = get(id);
		item.style.display = "block";
		return false;
	};

	var showInline = function(id) {
		var item = get(id);
		item.style.display = "inline";
		return false;
	};

	var showTr = function(id) {
		var item = get(id);
		item.style.display = "";
		return false;
	};

	var hideItem = function(id) {
		var item = get(id);
		item.style.display = "none";
		return false;
	};

	var active = false;

	ClientCodes.show = function() {
		if (!active) {
			hideItem("show");
			showInline("hide");
			showInline("save");
			showInline("save_2");
			for (var item in trs) {
				showTr(trs[item]);
			}
			active = true;
		}
		return false;
	};

	ClientCodes.hide = function() {
		if (ClientCodes.isDirty())
			return true;

		if (active) {
			for (var item in trs) {
				hideItem(trs[item]);
			}
			hideItem("save_2");
			hideItem("save");
			hideItem("hide");
			showInline("show");
			active = false;
		}
		return false;
	};
	
	ClientCodes.collapseMulti = function(id) {
		hideItem("multi_expanded_" + id);
		showBlock("multi_collapsed_" + id);
		return false;
	};

	ClientCodes.expandMulti = function(id) {
		hideItem("multi_collapsed_" + id);
		showBlock("multi_expanded_" + id);
		return false;
	};

	ClientCodes.isDirty = function() {
		return get("dirty").value == "1";
	};

	ClientCodes.setDirty = function() {
		get("dirty").value = "1";
		return true;
	};

	var getIdx = function(node) {
		var node1 = node.parentNode; // span
		node1 = node1.parentNode; // td
		node1 = node1.parentNode; // tr
		var splt = node1.id.split('_'); // clicode_multi_row_<idx>_<i>
		return {
			idx: parseInt(splt[3], 10),
			i: parseInt(splt[4], 10),
			node: node1
		};
	};

	var getFirstI = function(idx) {
		var keys = sortedKeys(ccs[idx]);
		if (keys.length > 0)
			return keys[0];
		else
			return -1;
	};

	var undoQty = {};
	
	var getQty = function(idx) {
		var node = document.getElementById("quantity_" + idx);
		if (node == null)
			return 1;

        var quantity = parseQuantity(node.value);
        if (isNaN(quantity))
            quantity = 0;
        return quantity;
	};

	ClientCodes.saveQty = function(idx, value) {
		undoQty[idx] = value;
	};

	ClientCodes.undoQty = function(idx) {
		var node = document.getElementById("quantity_" + idx);
		if (node != null)
			node.value = undoQty[idx];
	};

	var updateQtyWrapup = function(idx, expand) {
		var newval = getQty(idx);

		var qField = get("quantity_" + idx);
		if (newval == 1) {
			var first = getFirstI(idx);
			var quantity = first < 0 ? "1" : ccs[idx][first].quantity;
			var clientCode = first < 0 ? "" : ccs[idx][first].clientCode;
			qField.disabled = true;
			qField.value = quantity;
			get("clientcode_" + idx).value = clientCode;
		} else {
			if (newval == 0)
				qField.disabled = true;
			else
				qField.disbled = false;
			qField.value = "";
			get("clientcode_" + idx).value = "";
		}

		if (newval == 0) {
			get("clientcode_" + idx).disabled = true;
		} else {
			get("clientcode_" + idx).disabled = false;
		}
		
		if (newval > 1) {
			showTr("multi_body_" + idx);
			showInline("multi_control_" + idx);
			if (expand === true)
				ClientCodes.expandMulti(idx);
			else if (get("multi_expanded_" + idx).style.display == "none")
				ClientCodes.collapseMulti(idx);
			else
				ClientCodes.expandMulti(idx);
		} else /* 0 or 1 */ {
			hideItem("multi_body_" + idx);
			hideItem("multi_control_" + idx);
			hideItem("multi_collapsed_" + idx);
			ClientCodes.expandMulti(idx);
		}
		ClientCodes.saveQty(idx, newval);
	};

	ClientCodes.updateQty = function(idx) {
		var newval = getQty(idx);

		if (newval == undoQty[idx])
			return;

		if (newval >= sums[idx]) {
			updateNoCode(idx, 0);
			updateMultiView(idx, newval == 1);
			updateCollapse(idx);
			updateQtyWrapup(idx);
		} else {
			var confirm = function() {
				FreshDirect.ClientCodes.updateQtyDecrease(idx);
				ClientCodes.hideDialog();
				return false;
			};
			var cancel = function() {
				FreshDirect.ClientCodes.undoQty(idx);
				ClientCodes.hideDialog();
				return false;
			};
			ClientCodes.show();
			ClientCodes.showDialog(
					"Item quantity is now less than the quantity allocated to your clients. " +
					"Items will be removed from the last client in your list.", confirm, cancel);
		}
	};

	ClientCodes.updateQtyDecrease = function(idx) {
		var newval = getQty(idx);

		ClientCodes.setDirty();
		var remaining = sums[idx] - newval;
		var keys = sortedKeys(ccs[idx]);
		for (var j = keys.length - 1; j >= 0; j--) {
			var i = keys[j];
			var item = ccs[idx][i];
			if (item.quantity <= remaining) {
				remaining -= item.quantity;
				ClientCodes.removeMultiCc(idx, i);
				deleteRow(idx, i);
			} else {
				ClientCodes.updateMultiCc(idx, i, item.quantity - remaining, item.clientCode);
				updateRow(idx, i);
				break;
			}
		}
		updateMultiTable(idx, newval == 1);
		updateQtyWrapup(idx, true);
	};

	ClientCodes.fieldsBlur = function(idx, type) {
		if (document.getElementById("salesUnit_" + idx) != null) {
			ClientCodes.setDirty();
			return true;
		}

		if (getQty(idx) == 1) {
			var quantity = parseQuantity(get("quantity_" + idx).value);
			if (isNaN(quantity) || quantity > 1)
				quantity = 0;
			var clientCode = YAHOO.lang.trim(get("clientcode_" + idx).value);

			var i = getFirstI(idx);
			if (quantity == 0) {
				if (i >= 0) {
					ClientCodes.removeMultiCc(idx, i);
					deleteRow(idx, i);
					updateMultiTable(idx, true);
				}
			} else {
				if (i >= 0) {
					ClientCodes.updateMultiCc(idx, i, quantity, clientCode);
					updateRow(idx, i);
					updateMultiTable(idx, true);
				} else {
					ClientCodes.addRow(idx, true);
				}
			}
			ClientCodes.setDirty();
		}
		return true;
	};

	var updateNoCode = function(idx, change, noDirty) {
		var quantity = getQty(idx);
		var newval;
		if (quantity < sums[idx])
			newval = 0;
		else
			newval = getQty(idx) - sums[idx] - change;
		if (newval < 0) {
			if (noDirty !== true)
				ClientCodes.showDialog("Total entered exceeds item quantity being purchased");
			return false;
		}
		noCodes[idx] = newval;
		var node = get("multi_nocode_" + idx);
		if (node != null)
			node.innerHTML = noCodes[idx];
		return true;
	};
	
	ClientCodes.addMultiCc = function(idx, i, quantity, clientCode, noDirty) {
		if (!updateNoCode(idx, quantity, noDirty))
			return -1;

		if (!validateMultiUniqueness(idx, clientCode, -1))
			return -1;
		
		if (i < 0)
			i = ++lastIndexes[idx];
		else {
			lastIndexes[idx] = i;
		}
		ccs[idx][i] = {
			quantity: quantity,
			clientCode: clientCode
		};
		sums[idx] += quantity;
		if (noDirty !== true) {
			ClientCodes.setDirty();
			ClientCodes.addHistory(clientCode);
		}
		return i; // index of the last item added
	};

	ClientCodes.removeMultiCc = function(idx, i) {
        var removed = ccs[idx][i].quantity;
        if (!updateNoCode(idx, -removed))
            return false;
        else {
            delete ccs[idx][i];
            sums[idx] -= removed;
    		ClientCodes.setDirty();
            return true;
        }
	};
	
	ClientCodes.updateMultiCc = function(idx, i, quantity, clientCode) {
		var diff = quantity - ccs[idx][i].quantity;
		if (diff == 0 && clientCode == ccs[idx][i].clientCode)
			return true; // no change
		
		if (diff != 0 && !updateNoCode(idx, diff))
			return false;

		if (!validateMultiUniqueness(idx, clientCode, i))
			return false;

		sums[idx] += diff;
		ccs[idx][i].quantity = quantity;
		ccs[idx][i].clientCode = clientCode;
		ClientCodes.addHistory(clientCode);

		ClientCodes.setDirty();
		return true;
	};

	var validateMultiUniqueness = function(idx, clientCode, exclude) {
		var keys = sortedKeys(ccs[idx]);
		for (var j = 0; j < keys.length; j++) {
			var key = keys[j];
			var item = ccs[idx][key];
			if (key != exclude && item.clientCode == clientCode) {
				ClientCodes.showDialog("Please use unique client codes for the same item in cart");
				return false;
			}
		}
		return true;
	};
	
	var createRow = function(idx) {
		var nocode = get("multi_nocode_row_" + idx);
		var newRow = nocode.parentNode.parentNode.insertRow(nocode.rowIndex);
		newRow.insertCell(0);
		newRow.insertCell(0);
		newRow.insertCell(0);
		newRow.insertCell(0);
		newRow.insertCell(0);
		newRow.cells[0].className = "text10 clicode_quantity";
		newRow.cells[1].className = "text10 clicode_gap";
		newRow.cells[2].className = "text10 clicode_clientcode";
		newRow.cells[3].className = "text10 clicode_gap";
		newRow.cells[4].className = "text10 clicode_actions";
		newRow.cells[4].innerHTML = "<span><a href=\"#\">Edit</a> &nbsp;&nbsp; <a href=\"#\">Delete</a></span>" +
				"<span><a href=\"#\">Ok</a> &nbsp;&nbsp; <a href=\"#\">Cancel</a></span>";
		var spans = newRow.cells[4].getElementsByTagName("span");
		spans[1].style.display = "none";
		var as = newRow.cells[4].getElementsByTagName("a");
		as[0].onclick = function() {
			return ClientCodes.editRow(as[0]);
		};
		as[0].style.color = "#999999";
		as[1].onclick = function() {
			return ClientCodes.deleteRow(as[1]);
		};
		as[1].style.color = "#999999";
		as[2].onclick = function() {
			return ClientCodes.okEditRow(as[2]);
		};
		as[2].style.color = "#999999";
		as[3].onclick = function() {
			return ClientCodes.cancelEditRow(as[3]);
		};
		as[3].style.color = "#999999";
		return newRow;
	};
	
	var init = {};
	
	ClientCodes.initRow = function(idx, quantity, clientCode) {
		if (init[idx] == undefined)
			init[idx] = [];
		if (quantity != undefined)
			init[idx].push({
				quantity: quantity,
				clientCode: clientCode
			});
	};
	
	ClientCodes.onLoad = function() {
		for (var idx in init) {
			if (init.hasOwnProperty(idx)) {
				var item = init[idx];
				trs.push("tr_" + idx);
				addAcField(_PREFIX + "clientcode_" + idx, _PREFIX + "autocomplete_" + idx);
				var qty = getQty(idx);
				noCodes[idx] = qty;
				sums[idx] = 0;
				ccs[idx] = {};
				lastIndexes[idx] = -1;
				for (var i = 0; i < item.length; i++)
					addRow2(idx, item[i].quantity, item[i].clientCode);
				updateMultiTable(idx, qty == 1);				
			}
		}
	};

    var addRow2 = function(idx, quantity, clientCode) {
		var i = ClientCodes.addMultiCc(idx, -1, quantity, clientCode, true);
		if (i < 0)
			return false;
		
		// make clone of template
		var newRow = createRow(idx);
		
		newRow.id = _PREFIX + "multi_row_" + idx + "_" + i;
		
		// initialize clone
		newRow.cells[0].innerHTML = quantity;
		newRow.cells[2].innerHTML = adjustWidth(clientCode);

		return false;
    };

    ClientCodes.addRow = function(idx, noClear) {
		// process new values
		var quantity = parseQuantity(get("quantity_" + idx).value);
		if (isNaN(quantity)) {
			quantity = 0;
			ClientCodes.showDialog("Please use whole number only");
		}
		var clientCode = YAHOO.lang.trim(get("clientcode_" + idx).value);
		
		if (quantity <= 0 || clientCode.length == 0)
			return false; // nothing to do
		
		var i = ClientCodes.addMultiCc(idx, -1, quantity, clientCode);
		if (i < 0)
			return false;
		
		// make clone of template
		var newRow = createRow(idx);
		
		newRow.id = _PREFIX + "multi_row_" + idx + "_" + i;
		
		// initialize clone
		newRow.cells[0].innerHTML = quantity;
		newRow.cells[2].innerHTML = adjustWidth(clientCode);

		if (noClear !== true) {
			get("quantity_" + idx).value = "";
			get("clientcode_" + idx).value = "";
		}

		updateMultiTable(idx, noClear);
		
		return false;
	};

	ClientCodes.deleteRow = function(node) {
		var coord = getIdx(node);
		if (!ClientCodes.removeMultiCc(coord.idx, coord.i))
			return false;
		// find the second parent
		coord.node.parentNode.parentNode.deleteRow(coord.node.rowIndex);
        updateMultiTable(coord.idx);
		return false;
	};

	var deleteRow = function(idx, i) {
		var node = get("multi_row_" + idx + "_" + i);
		node.parentNode.removeChild(node);
	};

	var updateRow = function(idx, i) {
		var node = get("multi_row_" + idx + "_" + i);
		node.cells[0].innerHTML = ccs[idx][i].quantity;
		node.cells[2].innerHTML = adjustWidth(ccs[idx][i].clientCode);
	};

	var updateCollapse = function(idx) {
		var newval = "";
		var notfirst = false;
		var keys = sortedKeys(ccs[idx]);
		for (var j = 0; j < keys.length; j++) {
			var key = keys[j];
			var item = ccs[idx][key];
			if (notfirst)
				newval += "&emsp; |&emsp; ";
			newval += "<span style=\"white-space: nowrap;\">";
			newval += item.clientCode;
			newval += "&nbsp;&ndash;&nbsp;";
			newval += item.quantity;
			newval += "&nbsp;item";
			if (item.quantity > 1)
				newval += "s";
			newval += "</span>";
			notfirst = true;
		}
		var noCode = noCodes[idx];
		if (noCode > 0) {
			if (notfirst)
				newval += "&emsp; |&emsp; ";
			newval += "<span style=\"white-space: nowrap;\">";
			newval += "NO&nbsp;CLIENT&nbsp;CODE&nbsp;&ndash;&nbsp;";
			newval += noCode;
			newval += "&nbsp;item";
			if (noCode > 1)
				newval += "s";
			newval += "</span>";
		}
		var node = get("multi_coll_fillup_" + idx);
		if (node != null)
			node.innerHTML = newval;
	};

	var updateMultiView = function(idx, hideBorder) {
		if (sums[idx] == 0) {
			hideItem("multi_empty_row_" + idx);
			hideItem("multi_head_row_" + idx);
			hideItem("multi_nocode_row_" + idx);
			hideItem("multi_collapser_" + idx);
			get("multi_table_" + idx).style.borderBottom = "0px none";
		} else {
			if (hideBorder === true)
				get("multi_table_" + idx).style.borderBottom = "0px none";
			else
				get("multi_table_" + idx).style.borderBottom = "1px solid #999999";
			showTr("multi_empty_row_" + idx);
			showTr("multi_head_row_" + idx);
			showInline("multi_collapser_" + idx);
			if (noCodes[idx] == 0)
				hideItem("multi_nocode_row_" + idx);
			else
				showTr("multi_nocode_row_" + idx);
		}
	};

	var updateMultiVal = function(idx) {
		get("multi_val_" + idx).value = YAHOO.lang.JSON.stringify(ccs[idx]);
	};

	var updateMultiTable = function(idx, hideBorder) {
		updateMultiView(idx, hideBorder);
		updateCollapse(idx);
		updateMultiVal(idx);
	};

	ClientCodes.editRow = function(node) {
		var coord = getIdx(node);
		coord.node.cells[0].innerHTML = '<input type="text" autocomplete="off" size="5" maxlength="5" class="text10 clicode_quantity" style="text-align: center; border: 1px solid black;">';
		coord.node.cells[0].firstChild.value = ccs[coord.idx][coord.i].quantity;
		coord.node.cells[2].style.overflow = 'visible';
		coord.node.cells[2].innerHTML = '<div style="position: relative; vertical-align: middle; overflow: visible;">' + 
			'<input type="text" autocomplete="off" size="25" maxlength="30" class="text10" style="border: 1px solid black; width: 125px; position: static;">' +
			'<div style="width: 125px; position: absolute; top: 100%; _top: 20px; left: 0px;"></div></div>';
		coord.node.cells[2].firstChild.firstChild.value = ccs[coord.idx][coord.i].clientCode;
		coord.node.cells[2].firstChild.firstChild.id = _PREFIX + "clientcode_" + coord.idx + "_" + coord.i;
		coord.node.cells[2].firstChild.lastChild.id = _PREFIX + "autocomplete_" + coord.idx + "_" + coord.i;
		addAcField(_PREFIX + "clientcode_" + coord.idx + "_" + coord.i, _PREFIX + "autocomplete_" + coord.idx + "_" + coord.i);
		var spans = coord.node.cells[4].getElementsByTagName("span");
		spans[0].style.display = "none";
		spans[1].style.display = "inline";
		return false;
	};

	ClientCodes.okEditRow = function(node) {
		var coord = getIdx(node);

		var quantity = parseQuantity(coord.node.cells[0].firstChild.value);
		var clientCode = YAHOO.lang.trim(coord.node.cells[2].firstChild.firstChild.value);
		
		if (isNaN(quantity)) {
			quantity = 0;
			ClientCodes.showDialog("Please use whole number only");
		}
		if (quantity <= 0 || clientCode.length == 0)
			return false; // nothing to do
		
		if (!ClientCodes.updateMultiCc(coord.idx, coord.i, quantity, clientCode))
			return false;

		coord.node.cells[0].innerHTML = ccs[coord.idx][coord.i].quantity;
		coord.node.cells[2].style.overflow = 'hidden';
		coord.node.cells[2].innerHTML = adjustWidth(ccs[coord.idx][coord.i].clientCode);
		
		var spans = coord.node.cells[4].getElementsByTagName("span");
		spans[1].style.display = "none";
		spans[0].style.display = "inline";

		updateMultiTable(coord.idx);
		return false;
	};

	ClientCodes.cancelEditRow = function(node) {
		var coord = getIdx(node);

		coord.node.cells[0].innerHTML = ccs[coord.idx][coord.i].quantity;
		coord.node.cells[2].style.overflow = 'hidden';
		coord.node.cells[2].innerHTML = adjustWidth(ccs[coord.idx][coord.i].clientCode);

		var spans = coord.node.cells[4].getElementsByTagName("span");
		spans[1].style.display = "none";
		spans[0].style.display = "inline";
		return false;
	};

	var dialog = null;

	ClientCodes.showDialog = function(message, confirmAction, cancelAction) {
		if (dialog == null) {
			dialog = new YAHOO.widget.Panel(_PREFIX + "dialog", {
					width: "530px",  
					fixedcenter: true,  
					close: false,  
					draggable: false,  
					zindex: 4, 
					modal: true, 
					visible: false 
				});
		}
		var isAlert = confirmAction == undefined;
		var alert = isAlert ? "ALERT" : "WARNING";
		dialog.setBody(get("dlgTmpl").innerHTML);
		dialog.render(document.getElementById("i_viewcart"));
		dialog.body.getElementsByTagName("a")[0].onclick = isAlert ? ClientCodes.hideDialog : cancelAction;
		var spans = dialog.body.getElementsByTagName("span");
		spans[0].innerHTML = alert;
		spans[1].innerHTML = message;
		if (isAlert) {
			spans[2].getElementsByTagName("input")[0].onclick = function() {
				return FreshDirect.ClientCodes.hideDialog();
			};
			spans[3].innerHTML = "";
		} else {
			spans[2].innerHTML = "";
			spans[3].getElementsByTagName("input")[0].onclick = cancelAction;
			spans[3].getElementsByTagName("input")[1].onclick = confirmAction;
		}
		dialog.show();
		dialog.focusLast();
		return false;
	};

	ClientCodes.hideDialog = function() {
		if (dialog != null)
			dialog.hide();
		return false;
	};
	
	ClientCodes.clientCodesHistory = [];
	
	var histIndex = function(item) {
		for (var i = 0; i < ClientCodes.clientCodesHistory.length; i++)
			if (ClientCodes.clientCodesHistory[i].toLowerCase() >= item.toLowerCase())
				return i;
		
		return ClientCodes.clientCodesHistory.length;
	};
	
	ClientCodes.addHistory = function(item, nosort) {
		if (nosort === true) {
			ClientCodes.clientCodesHistory.push(item);
		} else {
			var i = histIndex(item);
			if (i == ClientCodes.clientCodesHistory.length)
				ClientCodes.clientCodesHistory.push(item);
			else if (item.toLowerCase() != ClientCodes.clientCodesHistory[i].toLowerCase())
				ClientCodes.clientCodesHistory.splice(i, 0, item);
		}
	};
	
	var addAcField = function(input, container) {
		var ds = new YAHOO.util.LocalDataSource(ClientCodes.clientCodesHistory);
		ds.responseSchema = {
			fields : [ "clientCode" ]
		};

		// Instantiate the AutoComplete 
		var ac = new YAHOO.widget.AutoComplete(input, container, ds);
		ac.prehighlightClassName = "yui-ac-prehighlight";
		ac.useShadow = false;
		ac.animVert = false;
		ac.animHoriz = false;
	};
	
	var adjustWidth = function(content) {
		var node = get("testbox");
		var newval = content;
		node.innerHTML = newval;
		while (node.clientWidth > 242) {
			content = content.substr(0, content.length - 1);
			node.innerHTML = newval = content + "&hellip;";
		}
		return newval;
	};
})();
