<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>
<%@ page import= 'com.freshdirect.transadmin.util.TransportationAdminProperties' %>
<%@ page import= 'com.freshdirect.transadmin.security.AuthUser.Privilege' %>


<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='title' direct='true'> Operations : Event Log</tmpl:put>
<% 
  boolean hasPrivilege = com.freshdirect.transadmin.security.SecurityManager.hasPrivilege(request, Privilege.SHIFTLOG_ADD );
%>
  <tmpl:put name='content' direct='true'> 
   		
	<tmpl:put name='slickgrid-lib'>
		<%@ include file='/common/i_slickgrid.jspf'%>
	</tmpl:put>
  		
	<div class="MNM002 subsub or_999">
		<div class="subs_left">	
			<div class="sub_tableft sub_tabL_MNM002 <% if(!"M".equalsIgnoreCase(request.getParameter("eventtype")) && !"S".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if(!"M".equalsIgnoreCase(request.getParameter("eventtype")) && !"S".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="eventlog.do" class="<% if(!"M".equalsIgnoreCase(request.getParameter("eventtype")) && !"S".equalsIgnoreCase(request.getParameter("eventtype"))) { %>MNM002<% } %>">Event Log</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if(!"M".equalsIgnoreCase(request.getParameter("eventtype")) && !"S".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeR<% } %>">&nbsp;</div>		
		
			<div class="sub_tableft sub_tabL_MNM002 <% if("M".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("M".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="eventlog.do?eventtype=M" class="<% if("M".equalsIgnoreCase(request.getParameter("eventtype"))) { %>MNM002<% } %>">MOT Log</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if("M".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeR<% } %>">&nbsp;</div>
			
			<div class="sub_tableft sub_tabL_MNM002 <% if("S".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeL<% } %>">&nbsp;</div>
			<div class="subtab <% if("S".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeT<% } %>">
				<div class="minwidth"><!-- --></div>
				<a href="eventlog.do?eventtype=S" class="<% if("M".equalsIgnoreCase(request.getParameter("eventtype"))) { %>MNM002<% } %>">Shift Log</a>
			</div>
			<div class="sub_tabright sub_tabR_MNM002 <% if("S".equalsIgnoreCase(request.getParameter("eventtype"))) { %>activeR<% } %>">&nbsp;</div>
			
		</div>
	</div>
	<div class="cont_row_bottomline"><!--  --></div>

  <div class="contentroot">
  		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_Event Log">
					<div style="float:left;">
						<div style="margin-top:4px;float:left;font-weight:bold;">Date:&nbsp;</div>
						<div style="float:left;">
							<input style="width:85px;" maxlength="40" name="daterange" id="daterange" value='<c:out value="${daterange}"/>' />
								                    			
							<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:viewEvents();" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
							
						</div>
					</div>			
				</div>
			</div>
		</div> 
		
		<br/><br/><br/>
		
		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
				   	<div align="right">
				   	<% if (hasPrivilege) { %>
							<span>&nbsp;<input id="btnAddNewEvent" type="button" style="font-size:11px" type = "button" height="18" value="Add ShiftEventLog"  onclick="javascript:showEventForm();" />&nbsp;</span>											
					<% } %>&nbsp;
					</div>
					<br/>
					<div style="width:99%">
						<div class="grid-header" style="width:100%">
				      		<label>End of Shift Scanner Log</label>
				     	 	<span style="float:right;display:none;" class="ui-icon ui-icon-search" title="Toggle search panel"
				           	 onclick="toggleFilterRow()"></span>
    					</div>
						<div id="myGrid" style="width: 100%; height: 625px;"></div>
						<div id="pager" style="width: 100%; height: 20px;"></div>
					</div>
					<div id="inlineFilterPanel" style="display:none;background:#dddddd;padding:3px;color:black;">
							  Search <input type="text" id="txtSearch2">
							  <div style="width:100px;display:inline-block;" id="pcSlider2"></div>
					</div>
					
				</div>
			</div>
		</div>
  </div> 
<style>
input.btn {   color:#050;   font: bold small 'trebuchet ms',helvetica,sans-serif; } 
</style>

<script>
var hasPrivilege = '<%= hasPrivilege %>';
var aoTypes = [];
var aoSubTypes = [];
var aoRoutes = [];
var aoWindows = [];
var aoGroups = [];

//Grid Variables
var grid;
var columnFilters = {};
var columns = [];
var options = {};

var url = "v/1/list/shiftlogs/";
var crudurl = "v/1/shifteventlog/add/";

$(document).ready(function () {		
	
	$("#daterange" ).datepicker();
	
	$.ajax({
		url : "v/1/list/eventmetadata/",
		type : "GET",
		data : "{}",
		contentType : "application/json",
		dataType : "json",
		async : true,
		success : function(json) {
			
			for(var i=0;i < json.rows.length;i++) {
				for(var j=0;j < json.rows[i].eventType.length;j++) {
					aoTypes.push({"value" : json.rows[i].eventType[j].id, "caption" : json.rows[i].eventType[j].name, "custumerReq" : json.rows[i].eventType[j].custumerReq, "employeeReq" : json.rows[i].eventType[j].employeeReq});
				}
				for(var k=0;k < json.rows[i].eventSubType.length;k++) {
					aoSubTypes.push({"value" : json.rows[i].eventSubType[k].id, "caption" : json.rows[i].eventSubType[k].name, "eventTypeId" : json.rows[i].eventSubType[k].eventTypeId });
				}
				for(var l=0;l < json.rows[i].eventMessageGroup.length;l++) {
					aoGroups.push({"value" : json.rows[i].eventMessageGroup[l].groupName, "caption" : json.rows[i].eventMessageGroup[l].groupName});
				}
			}
			$('#eventType').loadSelect(aoTypes, false, true);
			$("#eventType").val('<%=TransportationAdminProperties.getShiftEventLogType()%>');
			$("#eventType").attr("disabled", "disabled");
						
			var queryResult = $.Enumerable.From(aoSubTypes)
						        	.Where(function (subType) { return subType.eventTypeId == $("#eventType").val() })
						        	.OrderByDescending(function (subType) { return subType.caption })
						    		.ToArray(); 
			$('#eventSubType').loadSelect( queryResult, false, true );
		},
		error : function(msg) {
			var errorText = eval('(' + msg.responseText+ ')');
			alert('Error : \n--------\n'+ errorText.Message);
		}
	});
	
	
					
 	$('#eventDate').change(function() {
		
        lookUpRouteInfo( $(this).val());
    });	

	columns = [ {id : "transactionDate", name : "Created Time",	field : "transactionDate", width: 112, sortable : true, resizable: false, formatter: Slick.Formatters.DateTime}
					, {id : "userId",  name : "Entered By",	field : "userId", width: 65, resizable: false, sortable : true}
					, {id : "eventDate", name : "Event Date",	field : "eventDate", width: 73, sortable : true, resizable: false, formatter: Slick.Formatters.Date}
					, {id : "route",	name : "Route",	field : "route", width: 65, resizable: false, sortable : true}
					, {id : "scannerNumber",	name : "Scanner",	field : "scannerNumber", sortable : true}
					, {id : "eventType",	name : "Type",	field : "eventType", sortable : true}
					, {id : "eventSubType",	name : "Sub-Type",	field : "eventSubType", sortable : true}
					, {id : "description", name : "Description",	field : "description", sortable : true}				
					, {id : "employeeId", name : "Employee Number",	field : "employeeId", sortable : true}
				];

	options = {
			editable : false,
			enableCellNavigation : true,
			asyncEditorLoading : false,
			showHeaderRow : true,
			headerRowHeight : 30,
			forceFitColumns: true,
			multiSelect: false,
			explicitInitialization : true
		};	
					
	showGrid();
});

function showEventForm () {
	
	$('div#editShiftForm').modal({
		closeHTML: "<a href='#' title='Close' class='modal-close'>x</a>",
		position: ["15%",],
		overlayId: 'form-overlay',
		containerId: 'form-container',
		containerCss: {						           
            height: 'auto'
        },
		onOpen: function (dialog) {
				// add padding to the buttons in firefox/mozilla
				if ($.browser.mozilla) {
					$('#form-container .form-button').css({
						'padding-bottom': '2px'
					});
				}
				// input field font size
				if ($.browser.safari) {
					$('#form-container .form-input').css({
						'font-size': '.9em'
					});
				}
				
				$("#eventDate" ).datepicker({ minDate: +0 });
				
				$("#eventType").val('<%=TransportationAdminProperties.getShiftEventLogType()%>');
							
				var id = $('#eventType').val();
			    var queryResult = $.Enumerable.From(aoSubTypes)
			                        	.Where(function (subType) { return subType.eventTypeId == id })
			                        	.OrderByDescending(function (subType) { return subType.caption })
			                    		.ToArray();
			    $('#eventSubType').loadSelect( queryResult, false, true );
			        
			    $('#eventForm').clearForm();
				var formatedDate = $('#eventDate').formatDate(0);
				$('#eventDate').val(formatedDate);
				lookUpRouteInfo(formatedDate);
				
				
			var title = $('#form-container .form-title').html();
			$('#form-container .form-title').html('Loading...');
			dialog.overlay.fadeIn(200, function () {
				dialog.container.fadeIn(200, function () {
					dialog.data.fadeIn(200, function () {
						$('#form-container .form-content').animate({
							height: 'auto'
						}, function () {
							$('#form-container .form-title').html(title);
							$('#form-container form').fadeIn(200, function () {
								$('#form-container #form-name').focus();

								$('#form-container .form-cc').click(function () {
									var cc = $('#form-form #form-cc');
									cc.is(':checked') ? cc.attr('checked', '') : cc.attr('checked', 'checked');
								});

								// fix png's for IE 6
								if ($.browser.msie && $.browser.version < 7) {
									$('#form-container .form-button').each(function () {
										if ($(this).css('backgroundImage').match(/^url[("']+(.*\.png)[)"']+$/i)) {
											var src = RegExp.$1;
											$(this).css({
												backgroundImage: 'none',
												filter: 'progid:DXImageTransform.Microsoft.AlphaImageLoader(src="' +  src + '", sizingMethod="crop")'
											});
										}
									});
								}
							});
						});
					});
				});
			});
		},
		onShow: function (dialog) {
			$('#form-container .form-send').click(function (e) {
				e.preventDefault();
				// validate form
				if (form.validate()) {
					var msg = $('#form-container .form-message');
					msg.fadeOut(function () {
						msg.removeClass('form-error').empty();
					});
					$('#form-container .form-title').html('Sending...');
					$('#form-container form').fadeOut(200);
					$('#form-container .form-content').animate({
						height: '80px'
					}, function () {
						$('#form-container .form-loading').fadeIn(200, function () {
							
							var timeInt = 0;
							timeInt = timeInt + (new Date($('#eventDate').val()).getTimezoneOffset() * 60 * 1000);
							
							var newEventdate = new Date($('#eventDate').val());
							$('#eventDate').val(newEventdate.toJSON());							
							$("#eventType").removeAttr('disabled');
							
							var formData = JSON.stringify(form2js(document.getElementById('shiftForm')));
							$.ajax({
								url : "v/1/shifteventlog/add/",
								type : "POST",
								data : "data=" + $.URLEncode(formData),
								dataType : "html",
								async : false,
								success : function(json) {
									$('#form-container .form-loading').fadeOut(200, function () {
										$('#form-container .form-title').html('Event added sucessfully!');
										//msg.html(json).fadeIn(200);
									});
									showGrid();
									$('#eventForm').clearForm();			
									$('#eventDate').val('');
									$('#form-container .form-message').fadeOut();			
									$('#form-container form').fadeOut(200);
									 $('#form-container .form-content').animate({
										height: 40
									}, function () {
										dialog.data.fadeOut(200, function () {
											dialog.container.fadeOut(200, function () {
												dialog.overlay.fadeOut(200, function () {
													$.modal.close();
												});
											});
										});
									});
								},
								error : function(msg) {
									var errorText = eval('(' + msg.responseText + ')');
									//alert('Error : \n--------\n'+ errorText.Message);
									msg.html(json).fadeIn(200);
								}
							});
						
						});
					});
				}
				else {
					if ($('#form-container .form-message:visible').length > 0) {
						var msg = $('#form-container .form-message div');
						msg.fadeOut(200, function () {
							msg.empty();
							form.showError();
							msg.fadeIn(200);
						});
					}
					else {
						$('#form-container .form-message').animate({
							height: '30px'
						}, form.showError);
					}
					
				}
			});
		},
		onClose: function (dialog) {						
			$('#form-container form').clearForm();			
			$('#form-container .form-message').fadeOut();
			//$('#form-container .form-title').html('Goodbye...');
			$('#form-container form').fadeOut(200);
			$('#form-container .form-content').animate({
				height: 40
			}, function () {
				dialog.data.fadeOut(200, function () {
					dialog.container.fadeOut(200, function () {
						dialog.overlay.fadeOut(200, function () {
							$.modal.close();
						});
					});
				});
			});
			
		}
	});
}



var form = {
		message: null,
		validate: function () {
			form.message = '';
			if (!$('#eventDate').val()) {
				form.message += 'Date is required. ';
			}
			if (!$('#route').val()) {
				form.message += 'Route is required. ';
			}
			if (!$('#eventType').val()) {
				form.message += 'EventType is required. ';
			}
			if (!$('#eventSubType').val()) {
				form.message += 'EventSubType is required. ';
			}			
			if (form.message.length > 0) {
				return false;
			} else {
				return true;
			}
		}, 
		error: function (xhr) {
			alert(xhr.statusText);
		},
		showError: function () {
			$('#form-container .form-message')
				.html($('<div class="form-error"></div>').append(form.message))
				.fadeIn(200);
		}
	};


function lookUpRouteInfo(formatedDate, selRoute) {
	
	var selRouteId = selRoute || '';
	
	var postData = "";
	postData = postData + 'deliveryDate='+ $.URLEncode(formatedDate);
	
	aoRoutes = [];
	aoWindows = [];

	$.ajax({
		url : "v/1/list/routes/",
		type : "POST",
		data : postData,
		//contentType : "application/json",
		dataType : "json",
		async : true,
		success : function(json) {
			for(var i=0;i < json.rows.length;i++) {
				aoRoutes.push({"value" : json.rows[i].routeNo, "caption" : json.rows[i].routeNo });
				for(var k=0;k < json.rows[i].windows.length;k++) {
					aoWindows.push({"value" : json.rows[i].windows[k], "caption" : json.rows[i].windows[k], "route" : json.rows[i].routeNo });
				}
			}								
			$('#route').loadSelect( aoRoutes, false, true );
			$('#route').val(selRouteId);
		},
		error : function(msg) {
			var errorText = eval('(' + msg.responseText+ ')');
			alert('Error : \n--------\n'+ errorText.Message);
		}
	});
}

function viewEvents() {	
	showGrid();
}

function showGrid() {
	var postData = "";
	postData = postData + 'daterange='+ $.URLEncode($('#daterange').val());
	
	$.ajax({
		url : url,
		type : "POST",
		data : postData,
		dataType : "json",
		async : true,
		success : function(json) {								
			restoreGrid(json.rows);
		},
		error : function(msg) {
			var errorText = eval('(' + msg.responseText+ ')');
			alert('Error : \n--------\n'+ errorText.Message);
			restoreGrid(null);
		}
	});

	dataView = new Slick.Data.DataView({ inlineFilters: true });
	dataView.setPagingOptions({	pageSize : 20});
	grid = new Slick.Grid("#myGrid", dataView, columns,	options);
	
	grid.setSelectionModel(new Slick.RowSelectionModel());
	var pager = new Slick.Controls.Pager(dataView, grid, $("#pager"));
	
	// move the filter panel defined in a hidden div into grid top panel
	$("#inlineFilterPanel")
	      .appendTo(grid.getTopPanel())
	      .show();

	$(grid.getHeaderRow()).delegate(
			":input",
			"change keyup",
			function(e) {
				var columnId = $(this).data("columnId");
				if (columnId != null) {
					columnFilters[columnId] = $.trim($(this).val());
					dataView.refresh();
				}
			});

	grid.onHeaderRowCellRendered
			.subscribe(function(e, args) {
				$(args.node).empty();
				$("<input type='text'>").data("columnId", args.column.id).val(columnFilters[args.column.id]).appendTo(args.node);
			});

	grid.onSort.subscribe(function(e, args) {
		sortdir = args.sortAsc ? 1 : -1;
		sortcol = args.sortCol.field;

		dataView.sort(comparer, args.sortAsc);
	});

	// wire up model events to drive the grid
	dataView.onRowCountChanged.subscribe(function(e, args) {
		grid.updateRowCount();
		grid.render();
	});

	dataView.onRowsChanged.subscribe(function(e, args) {
		grid.invalidateRows(args.rows);
		grid.render();
	});
}
			
</script>
				
<div id="editShiftForm" style="display:none;">
	<div class='form-top'></div>
	<div class='form-content'>
		<h1 class='form-title'>Add/Edit Event</h1>
		<div class='form-loading' style='display:none'></div>
		<div class='form-message' style='display:none'></div>
		<form id="shiftForm" name="shiftForm" action='#' style='display:none'>
			<table>
				<tr>
					<td valign="top">
						<label for='form-date'>Event Date:</label>
						<input type='text' id='eventDate' class='form-input' name='eventDate' tabindex='1' />
						<label for='form-route'>Route</label>
						<select class='form-select' id="route" name="route" tabindex='2'></select>												
						
						<label for='form-scannerNumber'>Scanner Number</label>
						<input type='text' id='scannerNumber' class='form-input' name='scannerNumber' tabindex='5' />
						
						<label for='form-employee'>Employee Number</label>
						<input type='text' id='employeeId' class='form-input' name='employeeId' />
										
						<label for='form-type'>Event Type</label>
						<select class='form-select' id="eventType" name="eventType" tabindex='6'></select>
						
						<label for='form-type'>Event SubType</label>
						<select class='form-select' id="eventSubType" name="eventSubType" tabindex='7'>
						   	<option value="" class="eventSubTypes" >--Please select SubType</option>
						</select>
						
						<label for='form-description'>Comment</label>
						<textarea id='description' class='form-input' name='description' cols='40' rows='3' 
						onKeyDown="limitText(this.form.description,this.form.countdown,4000);" 
						onKeyUp="limitText(this.form.description,this.form.countdown,4000);" style="resize:none; width: 206px; height: 96px;"></textarea>						
						<br/><label for='form-desc'>&nbsp;</label><font size="1">You have <input readonly type="text" name="countdown" size="3" value="4000"> characters left.</font>						
					</td>
				</tr>
				<tr>
					<td>
						<label>&nbsp;</label>
						<button type='submit' class='form-send form-button' tabindex='1006'>Update</button>
						<button type='submit' class='form-cancel form-button simplemodal-close' tabindex='1007'>Cancel</button>
						<br/>
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

<style>
#form-container {
    width: 600px;
}

#form-container label {   
    width: 110px;
}
</style>
   
  </tmpl:put>
</tmpl:insert>

