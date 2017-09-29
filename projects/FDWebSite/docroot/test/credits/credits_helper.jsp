<%@ page import="java.util.*"%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true' />
<%
	Map params = new HashMap();

	Enumeration keys = request.getParameterNames();  
	while (keys.hasMoreElements() ) {  
		String key = (String)keys.nextElement(); 

		//To retrieve a single value  
		String value = request.getParameter(key); 

		params.put(key, value);
	
		// If the same key has multiple values (check boxes)  
		String[] valueArray = request.getParameterValues(key);  
		 
		for(int i = 0; i > valueArray.length; i++){
			params.put(key+valueArray[i], valueArray[i]);
		}  
	}

	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	params.put("baseUrl", baseUrl);
%>
<!doctype html>
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Mass Credits Helper Test Page</title>
	
	<fd:javascript src="/assets/javascript/jquery/1.11.3/jquery.js"/>
	<fd:javascript src="/assets/javascript/jquery/ui/1.11.4/jquery-ui.min.js"/>
	<fd:javascript src="/assets/javascript/SheetJS/xlsx.core.min.js"/>
	<fd:javascript src="/assets/javascript/FileSaver.js/FileSaver.js"/>
	<fd:css href="/assets/css/jquery/ui/1.11.3/themes/smoothness/jquery-ui.css" />
	
	<style>
		div {
			margin: 0;
		}
		.preMono12 {
			font-family: monospace;
			font-size: 12px;
			white-space: pre;
		}
        .preMono14 {
			font-family: monospace;
			font-size: 14px;
			white-space: pre;
        }
        .mono14 {
			font-family: monospace;
			font-size: 14px;
        }
		.bordered { border: 1px solid #333; }
		.inBlock { display: inline-block; }
		textarea.preMono12 {
			height: 300px;
		}
		#in_regex {
		}
		#in_source_cont {
			height: 300px;
			background-color: #eee;
			overflow-y: auto;
		}
		#in_source {
		}
		#dropspot {
			border: 2px dashed #bbb;
			-moz-border-radius: 5px;
			-webkit-border-radius: 5px;
			border-radius: 5px;
			padding: 25px;
			text-align: center;
			font: 20px bold, Verdana;
			color: #bbb;
			width: 100%;
			height: 250px;
			line-height: 250px;
		}
		#save_data_notes {
			height: 175px;
		}
        #save_data_notes_rowalerts {
            height: 235px;
            max-height: 235px;
            overflow-y: auto;
        }
        #in_regex_notes_defaults {
            font-size: 11px;
            font-family: monospace;
            background-color: #ccf;
            border: 1px solid #66f;
            font-weight: bold;
            padding: 10px;
        }
		.clearB { clear: both; }
		.fleft { float: left; }
		.w100p { width: 100%; }
		.w75p { width: 75% }
		.w20p { width: 20% }
		.w23p { width: 23% }
		.h30p { height: 30% }
		.marR10px { margin-right: 10px; }
		.borderlessChild * { border: none; }
		.bold { font-weight: bold; }
		.f15px { font-size: 15px; }
        .center { text-align: center; }
	</style>
</head>
<body>
	<div>
		<div>Source Template:</div>
		<div class="fleft w75p marR10px">
			<textarea id="in_regex_replace" class="preMono12 bordered w100p" spellcheck="false"></textarea>
		</div>
		<div class="fleft w23p">
			<div class="mono14" style="font-weight: bold;">
			    Default data set should have:
				<div id="in_regex_notes_defaults">CustId, OrderId, Amount, CompCode, CompDept, Note</div>
				As headers (exactly as shown, case insensitive). There are SIX values required.
			</div>
		</div>
		<br class="clearB" />
	</div>
	<div>
		<div>Source Data</div>
		<div class="fleft w75p marR10px">
			<div id="in_source_cont" class="bordered inBlock fleft w100p"><div id="in_source" class="preMono12"></div></div>
		</div>
		<div class="fleft w20p">
			<div id="dropspot" class="inBlock">Drop XLS File</div>
		</div>
		<br class="clearB" />
	</div>
	<div>
		<div>Save Data</div>
		<div class="fleft w75p marR10px">
			<textarea id="out_result" class="preMono12 fleft w100p" spellcheck="false"></textarea>
		</div>
		<div class="fleft w23p">
			<div id="save_data_notes">
				<div id="save_data_notes_rowalerts" class="preMono14" style="margin-top: 10px;"></div>
				
				<div class="w100p" style="font-size: 14px;"><input type="checkbox" id="opt_removeDollars" name="opt_removeDollars" checked /><label for="opt_removeDollars" >Remove dollar signs ($) from source data</label></div>
            
                <div class="center">
                    <button id="save_btnPreview">Preview</button>
	    		    <button id="save_btnProcess">Process</button>
    			    <button id="save_btnSaveAs">Save</button>    
                </div>
			</div>
		</div>
		<br class="clearB" />
	</div>


	<script>
		var gWorkbookData = null;
		var gWorkbookData_json = null;
		var regex_replace = '';
			regex_replace += "-- cust_id %%CUSTID%% for %%AMOUNT%% on sale_id %%ORDERID%%\n";
			regex_replace += "INSERT INTO CUST.COMPLAINT (id, create_date, created_by, status, approved_Date, approved_by, amount, note, sale_id, complaint_type) VALUES (cust.SYSTEM_SEQ.NEXTVAL, SYSDATE, 'SYSTEM', 'APP', SYSDATE, 'SYSTEM', '%%AMOUNT%%', 'Mass Credit for %%AMOUNT%% (%%NOTE%%)', '%%ORDERID%%', 'FDC');\n";
			regex_replace += "INSERT INTO CUST.COMPLAINTLINE (id, complaint_id, amount, method, complaint_type, complaint_dept_code_id) VALUES (cust.SYSTEM_SEQ.NEXTVAL, (SELECT id FROM CUST.COMPLAINT where created_by = 'SYSTEM' AND create_date >= (select SYSDATE-1 from dual) AND sale_id = '%%ORDERID%%' and rownum < 2), '%%AMOUNT%%', 'FDC', 'DEPT', (SELECT id FROM CUST.COMPLAINT_DEPT_CODE WHERE comp_code ='%%COMPCODE%%' AND comp_dept = '%%COMPDEPT%%' AND rownum <2));\n";
			regex_replace += "INSERT INTO CUST.CUSTOMERCREDIT (id, amount, original_amount, department, customer_id, complaint_id, create_date, affiliate) VALUES (cust.SYSTEM_SEQ.NEXTVAL, '%%AMOUNT%%', '%%AMOUNT%%', '%%COMPDEPT%%', '%%CUSTID%%', (SELECT id FROM CUST.COMPLAINT where created_by = 'SYSTEM' AND create_date >= (select SYSDATE-1 from dual) AND sale_id = '%%ORDERID%%' and rownum < 2), SYSDATE, 'FD');\n\n";

		/* set up drag-and-drop event */
		function handleDrop(e) {
			e.stopPropagation();
			e.preventDefault();
			$('#in_source').html('Processing...');
			var files = (e.dataTransfer) ? e.dataTransfer.files : e.originalEvent.dataTransfer.files;
			var i,f;
			for (i = 0, f = files[i]; i != files.length; ++i) {
				var reader = new FileReader();
				var name = f.name;
				reader.onload = function(e) {
					var data = e.target.result;

					/* if binary string, read with type 'binary' */
					gWorkbookData = XLSX.read(data, {type: 'binary'});

					/* DO SOMETHING WITH workbook HERE */
					handleWorkbookData();
				};
				reader.readAsBinaryString(f);
			}
		}
		function handleDragover(e) {
			e.stopPropagation();
			e.preventDefault();
			(e.dataTransfer) ? e.dataTransfer.dropEffect : e.originalEvent.dataTransfer.dropEffect = 'copy';
		}
		
		function handleWorkbookData() {
			if (gWorkbookData !== null) {
				$('#in_source').html(to_csv(gWorkbookData, {'FS': '\t'} ));
				gWorkbookData_json = to_json(gWorkbookData);
				displayDataRowCount();
			}
		}

		/* 
			opts can have
			printSheetName (false) for including sheet names in results
			FS for field seperator (",")
			RS for returns ("\n")
		*/
		function to_csv(workbook, opts) {
			var result = [], o = (opts === null) ? {} : opts;
			if (!o.hasOwnProperty('printSheetName')) { o.printSheetName = false; }

			workbook.SheetNames.forEach(function(sheetName) {
				var csv = XLSX.utils.sheet_to_csv(workbook.Sheets[sheetName], opts);
				if(csv.length > 0){
					if (o.printSheetName) {
						result.push("SHEET: " + sheetName);
						result.push("");
					}
					result.push(csv);
				}
			});
			return result.join("\n");
		}

		function to_json(workbook, opts) {
			var result = {};
			workbook.SheetNames.forEach(function(sheetName) {
				var roa = XLSX.utils.sheet_to_row_object_array(workbook.Sheets[sheetName], opts);
				if(roa.length > 0){
					result[sheetName] = roa;
				}
			});
			return result;
		}
		
		function displayDataRowCount() {
			$('#save_data_notes_rowalerts').prepend('<div>'+getDataRowCount()+' rows found</div>');
		}
		function getDataRowCount() {
			var rCount = 0;

			if (gWorkbookData_json !== null) {
				for (var key in gWorkbookData_json) break;
				rCount = gWorkbookData_json[key].length;
			}

			return rCount;
		}
		function doRowReplace(inDataObj, templateStr, lineNumb) {
			var result = templateStr;
			/* safety check */
			var reqVals = 6, valCount = 0;

			for (var key in inDataObj) {
				var regExp = new RegExp('%%'+key.toUpperCase()+'%%','g');
				result = result.replace(regExp, inDataObj[key]);

				/* safety */
				valCount++;
			}
			if ($('#opt_removeDollars:checked').length > 0) {
				result = result.replace(/\$/g, '');
			}

			if (valCount !== reqVals) {
                $('#save_data_notes_rowalerts').append('<div>Line '+lineNumb+' has incorrect number of values. ('+valCount+' =/= '+reqVals+')</div>');
			}

			return result;
		}
		function leadZero(num) {
			if (num < 10) {
				return '0'+num;
			}
			return num;
		}

		$('#dropspot').on('drop', handleDrop);
		$('#dropspot').on('dragover', handleDragover);
		
		$('#in_regex_replace').val(regex_replace);

		$('#save_btnPreview').click(function(e) {
			e.stopPropagation();
			e.preventDefault();

			if (gWorkbookData_json === null) { return; }

			var firstDataRowObj = null;

			for (var key in gWorkbookData_json) break;

			if (gWorkbookData_json[key].length > 0) {
				firstDataRowObj = gWorkbookData_json[key][0];
			}

			$('#out_result').val(doRowReplace(firstDataRowObj, $('#in_regex_replace').val(), 1)); 
		});

		
		$('#save_btnProcess').click(function(e) {
			e.stopPropagation();
			e.preventDefault();

			if (gWorkbookData_json === null) { return; }

			for (var key in gWorkbookData_json) break;

			$('#out_result').val('Processing...');

			setTimeout(function() {
				var result = '';
				for (var r = 0; r < gWorkbookData_json[key].length; r++) {
					result += doRowReplace(gWorkbookData_json[key][r], $('#in_regex_replace').val(), r+1);
				}
				$('#out_result').val(result);
			}, 1);
		});
		$('#save_btnSaveAs').click(function(e) {
			e.stopPropagation();
			e.preventDefault();
			var dateObj = new Date();
			saveTextAs($('#out_result').val(), 'mass_credit-'+dateObj.getFullYear()+leadZero(dateObj.getMonth()+1)+leadZero(dateObj.getDay())+'.sql');
		});
        
        $('button').button();

	</script>

</body>
</html>
