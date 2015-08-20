<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import='com.freshdirect.fdstore.standingorders.FDStandingOrdersManager'%>
<%@page import='com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate'%>
<%@page import='com.freshdirect.fdstore.standingorders.EnumStandingOrderAlternateDeliveryType'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Standing Order Alternative Delivery Dates</tmpl:put>
	<tmpl:put name='content' direct='true'>
<style type="text/css">
div.pq-grid-toolbar-search
{
    text-align:left;
}
div.pq-grid-toolbar-search *
{
    margin:1px 5px 1px 0px;
    vertical-align:middle;      
}
div.pq-grid-toolbar-search .pq-separator
{
   margin-left:10px;  
   margin-right:10px;  
}
div.pq-grid-toolbar-search select
{
    height:18px;   
    position:relative;
}
div.pq-grid-toolbar-search input.pq-filter-txt
{
    width:180px;border:1px solid #b5b8c8;       
    height:16px;
    padding:0px 5px;       
}
div.pq-grid-toolbar-padded
{     height:35px;
}

.pq-grid-toolbar .createButton { float:left;margin-top:0.5em }
.pq-grid-toolbar .deleteButton { margin-top:0.5em }
.pq-grid-toolbar .uploadButton { float:right;margin-top:0.5em }

.crm_standing_orders_alt_dates_col {
float:left;
width:100px;
text-align: center;
}

.crm_standing_orders_alt_dates_col_wide {
float:left;
width:300px;
text-align: center;
}

.crm_standing_orders_alt_dates_row {
float:left;
padding: 4px 0px;
}

.crm_standing_orders_alt_dates_table {
width:1200px;
margin-left:auto;
margin-right:auto;
margin-top: 20px;
}

</style>
    
    
		<crm:GetCurrentAgent id="currentAgent">
			<jsp:include page="/includes/crm_standing_orders_nav.jsp" />
				<table width="100%"><tr><td colspan="3"><div class="promo_page_header_text">Manage Alternative Delivery Dates</div></td></tr>
				<tr><td><div id="resultDiv" width="500"></div></td></tr>
				<!-- tr>
					<td><input type="button" id="create" value="Create"/>&nbsp;<input type="button" id="delete" value="Delete"/></td>
					<td align="right"><input  type="button" id="upload" value="Upload"/></td><td>&nbsp;</td>
				</tr> -->
		
				<tr><td colspan="2"><div id="soAltGrid">&nbsp;&nbsp;</div></td><td>&nbsp;</td></tr></table>
				
				<div id="dialog2" title="Upload Standing Order Alternate Dates">
					<form action="/api/soalternatedate/" enctype="multipart/form-data" method="post" id="uploadForm" name="uploadForm">
					<div id="error" width="500"></div>
					Please specify a xls file and click 'Upload':<br/><br/><input type="file" value="Browse" id="file" name="file"/> <br/>
						<p/><p/><p/> 
						<input type="button" value="Upload" id="fileUpload" name="fileUpload" onclick="javascript:submitUploadForm()"/>
					</form>
					<div id="result" width="500"></div>
				</div>
				<br/>
				
				<div id="dialog1" title="Create/Edit Standing Order Alternate Dates">
				<div id="formResult" width="500"></div><br/>
				<input type="hidden" name="delId" id="delId" value=""/>
				<input type="hidden" name="actionComplete" id="actionComplete" value=""/>
				<form id="form1">
<table width="100%">
	<tr>
		<td class="vTop"><div style="border: 1px solid #D2691E;">
			<table width="100%">
						<% 
						FDStandingOrderAltDeliveryDate altDeliveryDate = new FDStandingOrderAltDeliveryDate();
						%>
				<tr><input type="hidden" name="id" id="id" value=""/>
				</tr>
						
				<tr>
					<td class="promo_detail_label"><span > Description:</span></td>
					<td class="alignL"><input type="text" id="description" name="description" class="w350px" value="<%= altDeliveryDate.getDescription() %>" maxLength="255" /><span id="hidethis2" style="<%=false?"display:;color:green;":"display:none;color:green;" %>"></span></td>
				</tr>		
				<tr>
					<td class="promo_detail_label"><span >* Original Date:</span></td>
					<td class="alignL"><input type="text" id="origDate" name="origDate" class="w100px" value="<%= altDeliveryDate.getOrigDateFormatted() %>" maxlength="10" /></td>
				</tr>
				<tr>
					<td class="promo_detail_label"><span >* Alternate Date:</span></td>
					<td class="alignL"><input type="text" id="altDate" name="altDate" class="w100px" value="<%= altDeliveryDate.getAltDateFormatted() %>" maxlength="10" /></td>
				</tr>
				
				<tr>
					<td class="promo_detail_label">SO Id:</td>
					<td class="alignL"><input type="text" id="soId" name="soId" class="w150px" value="<%= altDeliveryDate.getSoId() %>" maxlength="16" /> <span class="grayIta8pt"></span><span id="hidethis3" style="<%=false?"display:;color:green;":"display:none;color:green;" %>"><br/>(RedeemCode will be append with random String, eg:RedeemCode%random generated string%, RedeemCodeWEF1234)</span></td>
				</tr>
				<tr>
					<td class="promo_detail_label">Actual Dlv Start Time:</td>
					<td class="alignL"><input type="text" id="origStartTimeStr" name="origStartTimeStr" class="w100px" value="<%= altDeliveryDate.getOrigStartTimeStr() %>" maxlength="16" onblur="this.value=time(this.value);"/></td>
				</tr>
				<tr>
					<td class="promo_detail_label">Actual Dlv End Time:</td>
					<td class="alignL"><input type="text" id="origEndTimeStr" name="origEndTimeStr" class="w100px" value="<%= altDeliveryDate.getOrigEndTimeStr() %>" maxlength="16" onblur="this.value=time(this.value);"/> </td>
				</tr>
				<tr>
					<td class="promo_detail_label">Alternate Dlv Start Time:</td>
					<td class="alignL"><input type="text" id="altStartTimeStr" name="altStartTimeStr" class="w100px" value="<%= altDeliveryDate.getAltStartTimeStr() %>" maxlength="16" onblur="this.value=time(this.value);"/> </td>
				</tr>
				<tr>
					<td class="promo_detail_label">Alternate Dlv End Time:</td>
					<td class="alignL"><input type="text" id="altEndTimeStr" name="altEndTimeStr" class="w100px" value="<%= altDeliveryDate.getAltEndTimeStr() %>" maxlength="16" onblur="this.value=time(this.value);"/> </td>
				</tr>
				<tr>
					<td class="promo_detail_label">Action Type:</td>
					<td class="alignL">
								<select name="actionType" id="actionType" >
								<logic:iterate id="type" collection="<%=EnumStandingOrderAlternateDeliveryType.getEnumList()%>" type="com.freshdirect.fdstore.standingorders.EnumStandingOrderAlternateDeliveryType">
									<option value="<%=type.getName()%>" 
										<%if(type.getName().equals(altDeliveryDate.getActionType())) {%> 
											selected="selected"
										<%}%>
										><%=type.getDescription()%></option>
								</logic:iterate>
								</select>
								
					</td>
				</tr>				
				
				<tr class="flatRow">
					<td colspan="2"><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
				</tr>
				
				
				<tr class="flatRow">
					<td colspan="2"><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
				</tr>
					
			</table></div>
			<table width="100%">	
				<tr class="flatRow">
					<td colspan="2"><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
				</tr>
				
				<tr class="flatRow">
					<td colspan="2"><img width="1" height="1" src="/media_stat/crm/images/clear.gif" alt="" class="promo_edit_offer-spacer" /></td>
				</tr>	
				<tr>
					<td align="center"><input type="button" id="save" name="save" value="Save" onclick="javascript:submitForm(this.form);"/> &nbsp;&nbsp;<input type="button" id="cancel" name="cancel" value="Cancel" onclick=""/></td>
				</tr>
				</table>
				<br/>
				<table>
				<tr><td><b>Notes:</b></td></tr>
				<tr><td><ul>
				<li>Alternate delivery date should be (+/-) 7 days from the original delivery date.</li>
				<li>Geo restrictions, delivery fees and variable minimum order total would be applied based on the alternate delivery date.</li>
				<li>Please make sure the time slot and capacity are available for the alternate delivery date.</li>
				<li>Alternate delivery date is not required for 'Skip the standing order next delivery date' type action.</li>
				</ul></td></tr>
				
				
				</table>
		</td>
		
	</tr>
	
</table>
</form>
</div>
			
		</crm:GetCurrentAgent>	
		
	</tmpl:put>
</tmpl:insert>

	<link rel="stylesheet" href="/assets/javascript/paramquery-2.0.3/pqgrid.min.css" />
	<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/humanity/jquery-ui.css" />
    <script src="/assets/javascript/paramquery-2.0.3/pqgrid.min.js"></script>
 <script> 
$jq(function() {
	$jq( "#dialog1" ).dialog({
		 autoOpen: false,
		 height: 600,
		 width: 600,
		 modal: true
	});
});


$jq('div#dialog2').bind('dialogclose', function(event) {
	if($jq('#actionComplete').val()=="true"){
		location.reload(true);
	}	
	$jq('#error').html("");
   	$jq('#result').html("");
	
});
$jq('div#dialog1').bind('dialogclose', function(event) {
	if($jq('#actionComplete').val()=="true"){
		location.reload(true);
	}	
	$jq("#formResult").html("");
	$jq( "#form1" )[0].reset();
});

$jq(function() {
	$jq( "#dialog2" ).dialog({
		 autoOpen: false,
		 height: 500,
		 width: 500,
		 modal: true
	});
});

$jq( "[id^=dialog_]" ).dialog({
	 autoOpen: false,
	 height: 600,
	 width: 600,
	 modal: true
});

$jq( "#create" )
.button()
.click(function() {
	$jq( "#dialog1" ).dialog( "open" );
	$jq( "#form1" )[0].reset();
});

$jq( "#save" )
.button()
.click(function() {
});

$jq( "#delete" )
.button()
.click(function() {
	deleteRow();
});

$jq( "#upload" )
.button()
.click(function() {
	showUploadDialog();
});

$jq( "#cancel" )
.button()
.click(function() {
	$jq( "#form1" )[0].reset();
	$jq("#formResult").html("");
	$jq( "#dialog1" ).dialog( 'close' );;
});

$jq("[id^=edit_]")
.button()
.click(function(event) {
	var altId =event.target.id.substr(5);
	
		$jq( "#dialog_"+altId ).dialog( "open" );
});

$jq("[id^=delete_]")
.button()
.click(function(event) {
	var altId =event.target.id.substr(7);
	var altIdJson =" {id="+altId+"}";
	if(confirm("Are you sure, you want to delete this standing order alternate date setup ?")){
		$jq.ajax({
            type: "DELETE",
            url: "/api/soalternatedate?id="+altId,
            success : function(json){
            	location.reload(true);
            }
        });
	}
		
});


$jq("[id^=cancel_]")
.button()
.click(function(event) {
	var altId =event.target.id.substr(7);	
		$jq( "#form_"+altId )[0].reset();
		$jq( "#dialog_"+altId ).dialog( "close" );
//		location.reload(true);
});
$jq(function() {
	$jq("[id^=origDate_]").datepicker();
	$jq("[id^=altDate_]").datepicker();
	$jq("[id^=startDate_]").datepicker();
	$jq("[id^=endDate_]").datepicker();
	$jq( "#origDate" ).datepicker();
	$jq( "#altDate" ).datepicker();
	$jq( "#startDate" ).datepicker();
	$jq( "#endDate" ).datepicker();
	});
</script>

<script type="text/javascript">

var listData;
var $grid;
var obj = { width: 1200, height: 1000, title: "Standing Order Alternate Delivery Dates", flexHeight: true };
var soGridColModel = 	[	{ title: "<b>Original Date</b>", width: 100, dataType: "date", dataIndx: "origDateFormatted" , align:"center", 
								filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
			                        filter("origDateFormatted", $jq(this).val());
			                    } 
			                    }] }},
                   { title: "<b>Alternate Date</b>", width: 100, dataType: "date", dataIndx: "altDateFormatted" ,align:"center",
									filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
				                        filter("altDateFormatted", $jq(this).val());
				                    } 
				                    }] }},
                   { title: "<b>SO Id</b>", width: 100, dataType: "integer", align: "right", dataIndx: "soId",align:"center",
										filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
					                        filter("soId", $jq(this).val());
					                    } 
					                    }] } },
                   { title: "<b>Description</b>", width: 200, dataType: "string", align: "right", dataIndx: "description", align:"center",
											filter: { type: 'textbox', condition: 'equal',
												listeners: [{ change: function (evt, ui) {
							                        filter("description", $jq(this).val());
							                    } 
							                    }] }},
                   { title: "<b>Orig Dlv Start Time</b>", width: 115, dataType: "string", align: "right", dataIndx: "origStartTimeStr", align:"center",
												filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
							                        filter("origStartTimeStr", $jq(this).val());
							                    } 
							                    }] }},
                   { title: "<b>Orig Dlv End Time</b>", width: 105, dataType: "string", align: "right", dataIndx: "origEndTimeStr", align:"center",
													filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
								                        filter("origEndTimeStr", $jq(this).val());
								                    } 
								                    }] }},
                   { title: "<b>Alt Dlv Start Time</b>", width: 100, dataType: "string", align: "right", dataIndx: "altStartTimeStr", align:"center",
														filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
									                        filter("altStartTimeStr", $jq(this).val());
									                    } 
									                    }] }},
                   { title: "<b>Alt Dlv End Time</b>", width: 100, dataType: "string", align: "right", dataIndx: "altEndTimeStr", align:"center",
															filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
										                        filter("altEndTimeStr", $jq(this).val());
										                    } 
										                    }] }},
                   { title: "<b>Action Type</b>", width: 125, dataType: "string", align: "right", dataIndx: "actionType", align:"center",
																filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
											                        filter("actionType", $jq(this).val());
											                    } 
											                    }] }},
                  
                    {title: "<b>Last Modified By</b>", width: 100, dataType: "date", dataIndx: "modifiedBy", align:"center",
					filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
                        filter("modifiedBy", $jq(this).val());
                    } 
                    }] } },
                    {title: "<b>Last Modified At</b>", width: 100, dataType: "date", dataIndx: "modifiedTimeStr", align:"center",
    					filter: { type: 'textbox', condition: 'equal', listeners: [{ change: function (evt, ui) {
                            filter("modifiedTimeStr", $jq(this).val());
                        } 
                        }] } }
                   
                   ];
                   
                   
var soGridDataModel = {
        location: "local",
        sorting: "local",
        dataType: "JSON",
        method: "GET",
        curPage: 1,
        url: "/api/soalternatedate/",
        getData: function (dataJSON) {
            return { data: dataJSON };
        }
}



$soGrid = $jq("div#soAltGrid").pqGrid({ flexHeight: true,
    width: "100%",
    dataModel: soGridDataModel,
    colModel: soGridColModel,
    pageModel: { type: "local", rPP: 20, rPPOptions: [20, 50, 100, 500, 1000]},
    title: "<b>Standing Order Alternate Delivery Dates</b>",
    filterModel: { on: true, mode: "AND", header:true },
    numberCell: { show: false },
    resizable: true,
    editable: false,
    collapsible:false,
    stripeRows:true,
    wrap: false,
    hwrap: false, 
    columnBorders: true,
    freezeCols: 0,
    selectionModel: { type: 'row' },
    rowDblClick: function(event, ui) {
        if (ui.rowData) {
        	$jq( "#dialog1" ).dialog( "open" );
            var rowData = ui.rowData;
            $jq( "#id" ).val(rowData["id"]);
            //$jq( "#altIdDiv" ).html(rowData["id"]);
            $jq( "#description" ).val(rowData["description"]);
            $jq( "#origDate" ).val(rowData["origDateFormatted"]);
            $jq( "#altDate" ).val(rowData["altDateFormatted"]);
            $jq( "#soId" ).val(rowData["soId"]);
            $jq( "#origStartTimeStr" ).val(rowData["origStartTimeStr"]);
            $jq( "#origEndTimeStr" ).val(rowData["origEndTimeStr"]);
            $jq( "#altStartTimeStr" ).val(rowData["altStartTimeStr"]);
            $jq( "#altEndTimeStr" ).val(rowData["altEndTimeStr"]);
            $jq( "#actionType" ).val(rowData["actionType"]);
            $jq( "#startDate" ).val(rowData["startDateFormatted"]);
            $jq( "#endDate" ).val(rowData["endDateFormatted"]);
            
        }
	},
	rowSelect: function (evt, ui) {
        if (ui.rowData) {  
        	 var rowData = ui.rowData;
        	 if($jq( "#delId" ).val()==""){
        		 $jq( "#delId" ).val(rowData["id"]);
        	 }else{
        		$jq( "#delId" ).val($jq( "#delId" ).val()+"-"+rowData["id"]);
        	}
        	
        }
	},
	render: function(evt, ui){
		var $toolbar = $jq("<div class='pq-grid-toolbar pq-grid-toolbar-padded'></div>").appendTo($jq(".pq-grid-top", this));
		 
        $jq("<span class=\"createButton\"><b>Add</b></span>").appendTo($toolbar).button({ icons: { primary: "ui-icon-plusthick"} }).click(function (evt) {
            addRow();
        });
        $jq("<span class=\"createButton\">&nbsp;</span>").appendTo($toolbar);
        $jq("<span class=\"deleteButton\"><b>Delete</b></span>").appendTo($toolbar).button({ icons: { primary: "ui-icon-circle-minus"} }).click(function (evt) {
            deleteRow();
        });
        $jq("<span class=\"uploadButton\"><b>Export</b></span>").appendTo($toolbar).button({ icons: { primary: "ui-icon-document"} }).click(function () {
        	 $soGrid.pqGrid("exportExcel", { url: "/api/soalternatedate", sheetName: "SO Alternate Delivery Dates" });
        });
        $jq("<span class=\"uploadButton\"><b>Upload</b></span>").appendTo($toolbar).button({ icons: { primary: "ui-button-icon-secondary"} }).click(function () {
        	showUploadDialog();
        });
        $toolbar.disableSelection();
	}
});


$soGrid.pqGrid( "showLoading" );
getLatestData();

function filter(dataIndx, value) {
    $soGrid.pqGrid("filter", {
            data: [{ dataIndx: dataIndx, value: value}]
    });
    $soGrid.pqGrid( "refreshDataAndView" );
}

		function time(time_string) {
			var orgTimeString = time_string;
			if(""!=time_string){
				time_string = time_string.replace(".","");
			}
				var ampm = 'a';
				var hour = -1;
				var minute = 0;
				var temptime = '';
				time_string = time_string.trim();
				var ampmPatEnd =/^([1-9]|0[1-9]|1[0-2]):([0-5]\d)\s(AM|PM)?$/i;// (/am|pm|AM|PM$/);
				if (/* time_string.length == 8 &&  */time_string.match(ampmPatEnd)) {
					return time_string;
				}
				for ( var n = 0; n < time_string.length; n++) {

					var ampmPat = (/a|p|A|P/);
					if (time_string.charAt(n).match(ampmPat)) {
						ampm = time_string.charAt(n);
						break;
					} else {
						ampm = 'a';
					}

					var digPat = (/\d/);
					if (time_string.charAt(n).match(digPat)) {
						temptime += time_string.charAt(n);
					}
				}
				if (temptime.length > 0 && temptime.length <= 2) {
					hour = temptime;
					minute = 0;
				} else if (temptime.length == 3 || temptime.length == 4) {
					if (temptime.length == 3) {
						hour = time_string.charAt(0);
						minute = time_string.charAt(1);
						minute += time_string.charAt(2);
					} else {
						hour = time_string.charAt(0);
						hour += time_string.charAt(1);
						minute = time_string.charAt(2);
						minute += time_string.charAt(3);
					}
				} else {
					if(time_string !=''){
						alert(orgTimeString+ " is invalid time format. Please enter in 'hh:mm am/pm' format only.");
					}
					return '';
				}
				if ((hour <= 12) && (minute <= 59)) {

					if (hour.toString().length == 1) {
						hour = '0' + hour;
					}

					if (minute.toString().length == 1) {
						minute = '0' + minute;
					}
					if (hour == '00') {
						ampm = 'a';
					}
					//if (hour == '12' ) { ampm='p' }
					temptime = hour + ':' + minute + ' ' + ampm.toUpperCase() + 'M';
				} else {
					temptime = '';
				}
				if(time_string !='' && temptime==''){
					alert(orgTimeString+ " is invalid time format. Please enter in 'hh:mm am/pm' format only.");
				}
				return temptime;

			}

			function hour(time) {
				var result = parseFloat(time.trim());
				if (result == null || isNaN(result))
					return "";
				if (result > 24)
					return "";
				var result1 = "" + result;
				if (result1.indexOf(".") == -1)
					result1 += ".00";
				else {
					var index = result1.indexOf(".");
					var le = result1.trim().length;
					if (le - index > 3)
						result1 = result1.substring(0, index + 3);
				}
				if (time.trim().length == 1)
					result1 = "0" + result1;
				return result1;

			}
			function submitForm(form){
				var json = ConvertFormToJSON(form);
		        $jq.ajax({
		            type: "POST",
		            url: "/api/soalternatedate/",
		            data: {data:JSON.stringify(json)},
		            dataType: "json",
		            success : function(data, textStatus, jqXHR){
		            	if(data == '' || data == null){
		            		$jq('#actionComplete').val("true");
		            		setCreateOrUpdateSuccessMsg();
		                	$soGrid.pqGrid( "refresh" );
		            	}else{
		            		$jq('#actionComplete').val("");
		            		$jq('#formResult').html(data);
		            	}
		            },
		            error: function(data,textStatus,jqXHR){
		            	if(data == '' || data == null){
		            		$jq('#actionComplete').val("true");
			            	setCreateOrUpdateSuccessMsg();
		                	$soGrid.pqGrid( "refresh" );
		            	}else{
		            		$jq('#actionComplete').val("");
		            		$jq('#formResult').html(data.responseText);
		            	}
		            }
		        });
			}
			
			function submitUploadForm() {
				 var formUrl =$jq("#uploadForm").attr("action");
      		     var formData = new FormData($jq("#uploadForm")[0]);
      		     $jq.ajax({
   		         url: formUrl,
   		         type: 'POST',
   		         data:  formData,
   		         mimeType:"multipart/form-data",
   		         contentType: false,
   		         cache: false,
   		         processData:false,
   		                success: function(data, textStatus, jqXHR)
   		                {
   		                	$jq('#result').html(data);   		                	
   		                	if(data == '' || data == null){
   		                		$jq('#actionComplete').val("true");
   		                		$jq('#error').css("color","#00FF00"); 
   		                		$jq('#error').html("<b>Upload successful.</b>");
   		                	}else{
   		                		$jq('#actionComplete').val("");
   		                		$jq('#error').css("color","#FF0000"); 
   		                		$jq('#error').html("<b>Upload failed. Please see the errors below.</b>");
   		                	}
   		                },
   		                error: function(jqXHR, textStatus, errorThrown) 
   		                {
   		                	$jq('#actionComplete').val("");
   		                	$jq('#error').css("color","#FF0000");
   		                	$jq('#error').html("<b>Upload failed. Please see the errors below.</b>");
   		                }
   				  });  
	                	
               }
                 
			
			function ConvertFormToJSON(form){
			    var array = $jq(form).serializeArray();
			    var json = {};
			    
			    $jq.each(array, function() {
			        json[this.name] = this.value || '';
			    });
			    return json;
			}
			
			function addRow(){
				$jq( "#dialog1" ).dialog( "open" );
				$jq( "#form1" )[0].reset();
			}
			
			function deleteRow(){
				var altId = $jq("#delId").val();
				if(altId=="" || null == altId){
					alert("Please select a record to delete.");
				}
				else if(confirm("Are you sure, you want to delete this standing order alternate date setup ?")){
					$jq.ajax({
			            type: "DELETE",
			            url: "/api/soalternatedate?id="+altId,
			            success : function(json){	
			            	$jq( "#delId" ).val("");
			            	getLatestData();
			            	setDeleteSuccessMsg();
			            }
			        });
				}
			}
			
			function showUploadDialog(){
				$jq( "#dialog2" ).dialog( "open" );
				$jq( "#form1" )[0].reset();
			}
			function getLatestData(){
				$jq.ajax({
            	    url: "/api/soalternatedate/",
            	    cache: false,
            	    async: true,
            	    dataType: "JSON",
            	    success: function(response){
            	        $soGrid.pqGrid("option", "dataModel.data", response);
            	        $soGrid.pqGrid( "refreshView" );
            	        $soGrid.pqGrid( "hideLoading" );
            	    }
            	});
			}
		
			function setCreateOrUpdateSuccessMsg(){
				$jq('#formResult').css("color","#006400"); 
            	$jq('#formResult').html("<b>Created/Updated successfully.</b>");
			}
			
			function setDeleteSuccessMsg(){
				$jq('#resultDiv').css("color","#006400"); 
            	$jq('#resultDiv').html("<b>Deleted successfully.</b>");
			}
</script>
