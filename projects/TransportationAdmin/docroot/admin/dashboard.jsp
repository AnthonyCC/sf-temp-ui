<%@ taglib uri='template' prefix='tmpl' %>
<%@ page import='com.freshdirect.transadmin.web.ui.*' %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import= 'com.freshdirect.transadmin.util.TransStringUtil' %>


<tmpl:insert template='/common/sitelayout.jsp'>

<tmpl:put name='title' direct='true'> Admin : Dashboard</tmpl:put>
	
	 
  <tmpl:put name='content' direct='true'> 
   		
	<tmpl:put name='slickgrid-lib'>
		<%@ include file='/common/i_slickgrid.jspf'%>
	</tmpl:put>
	
	<tmpl:put name='yui-lib'>
		<%@ include file='/common/i_yui.jspf'%>
	</tmpl:put>
	
	<script type="text/javascript" src="js/dashboard.js"></script>
  <div class="contentroot">
  		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem">
					<div style="float:left;">
						<div style="margin-top:4px;float:left;font-weight:bold;">Day of Week:&nbsp;</div>
						<div style="float:left;">
							<select	class='form-select' id="dayOfWeek" name="dayOfWeek" onchange="javascript:getCapacityData();">
							<option value="Mon">Monday</option>
							<option value="Tue">Tuesday</option>
							<option value="Wed">Wednesday</option>
							<option value="Thu">Thursday</option>
							<option value="Fri">Friday</option>
							<option value="Sat">Saturday</option>
							<option value="Sun">Sunday</option>
						</select>
						</div>
					</div>			
				</div>
			</div>
		</div> 
		
		<br>&nbsp;<br><br>
		
			   	
				<div style="width: 300px;margin-left:10px;float:left;">
						<br><br><div id="pc_message"></div><br>
						<div class="grid-header" style="width: 300px;">
				      		<label>Plant Capacity</label>
    					</div>
						<div id="myGrid" style="width: 300px; height: 300px;"></div>
						<br>
						<form id="plantCapacityForm" action="" method="POST">
  							<input type="submit" value="Save">
						</form>
						
				</div>
				
				<div style="width: 300px;padding:0 0 0 50px;float:left;">
						<br><br><div id="pd_message"></div><br>
						<div class="grid-header" style="width: 300px;">
				      		<label>Plant Dispatch</label>
    					</div>
						<div id="pdGrid" style="width: 300px; height: 300px;"></div>
						<br>
						<form id="plantDispatchForm" action="" method="POST">
  							<input type="submit" value="Save">
						</form>
						
				</div>
				
				<%@include file="dashboard/orderrateexceptions.jspf"%>
			
	</div>
	<style type="text/css">
	
	div#loadingImage {
   position: absolute;
   display: inline-block;
   top: 50%;
   left: 50%;
   margin-top: -150px; /* Half the height */
   margin-left: -150px; /* Half the width */
}
 
  #pdGrid, #edGrid {
  background: white;
  outline: 0;
  border: 1px solid gray;
}

	</style>
  <script>
  
  function requiredFieldValidator(value) {
	    if (value == null || value == undefined || !value.length) {
	      return {valid: false, msg: "This is a required field"};
	    } else {
	      return {valid: true, msg: null};
	    }
	  }

  function removeRow1(current_row) {
	  var dd = dataView.getItems();
	  dd.splice(current_row,1);
	  var r = current_row;
	  while (r<dd.length){
	    grid.invalidateRow(r);
	    r++;
	  }
	  dataView.beginUpdate();
	  dataView.setItems(dd);
	  dataView.endUpdate();
	  dataView.refresh();
	  grid.updateRowCount();
	  grid.invalidate();
	  grid.scrollRowIntoView(current_row-1);
	  }
  function removeRow2(current_row) {
	  var dd = pd_dataView.getItems();
	  dd.splice(current_row,1);
	  var r = current_row;
	  while (r<dd.length){
		  pd_grid.invalidateRow(r);
	    r++;
	  }
	  pd_dataView.beginUpdate();
	  pd_dataView.setItems(dd);
	  pd_dataView.endUpdate();
	  pd_dataView.refresh();
	  pd_grid.updateRowCount();
	  pd_grid.invalidate();
	  pd_grid.scrollRowIntoView(current_row-1);
	  }
  
	  var grid, pd_grid;
	  var data = [];
	  var pd_data = [];
	  var pc_columns = [
	    {id: "dispatchTime", name: "Plant Dispatch", field: "dispatchTime", minWidth: 125,editor: TimeEditor, sortable:true, validator: requiredFieldValidator },
	    {id: "capacity", name: "Cumulative Capacity", field: "capacity", width: 125, editor: Slick.Editors.Integer },
	    {id: 'id', name: '', field: 'dispatchTime',  width: 15, formatter: function (r, c, id, def, datactx) { 
	           return '<a href="#" onclick="removeRow1(' + r + ')">X</a>'; }
	      }
	    ];
	  var pd_columns = [{id: "dispatchTime", name: "TransApp Dispatch", field: "dispatchTime", minWidth: 125,editor: TimeEditor, validator: requiredFieldValidator },
						{id: "plantDispatch", name: "Plant Dispatch", field: "plantDispatch", width: 125,editor: TimeEditor, sortable:true, validator: requiredFieldValidator  },
	            	     {id: 'id', name: '', field: 'dispatchTime',  width: 15, formatter: function (r, c, id, def, datactx) { 
	         	           return '<a href="#" onclick="removeRow2(' + r + ')">X</a>'; }
	         	      }
	            	    ];
	  
	  var options = {
	    editable: true,
	    enableAddRow: true,
	    asyncEditorLoading: false,
	    autoEdit: false,
	  };
	  dataView = new Slick.Data.DataView();
	  pd_dataView = new Slick.Data.DataView();
		
	  function getCapacityData() {
	  $('#pc_message').html("");
	  data = [];
		  
	  $.ajax({
			url : "v/1/list/plantcapacity/"+$("#dayOfWeek").val(),
			type : "GET",
			contentType : "application/json",
			dataType : "json",
			async : true,
			beforeSend: function() {
			    $('#myGrid').html("<div id='loadingImage'><img src='images/loading.gif' /></div>");
			  },
			success : function(json) {
				
				for(var i=0;i < json.rows.length;i++) {
					var d = (data[i] = {});

				      d["dispatchTime"] = json.rows[i].dispatchTime.timeString;
				      d["capacity"] = json.rows[i].capacity;
				}
				
				grid = new Slick.Grid("#myGrid", dataView, pc_columns, options);
				grid.setSelectionModel(new Slick.RowSelectionModel());
				grid.setSortColumn("dispatchTime",true);
				
				// wire up model events to drive the grid
				dataView.onRowCountChanged.subscribe(function (e, args) {
				  grid.updateRowCount();
				  grid.render();
				});

				dataView.onRowsChanged.subscribe(function (e, args) {
				  grid.invalidateRows(args.rows);
				  grid.render();
				});

				dataView.beginUpdate();
			    dataView.setItems(data,"dispatchTime");
			    dataView.endUpdate();
			    dataView.refresh();
			    grid.invalidate();
				$('#myGrid').show();
				
				grid.onSort.subscribe(function (e, args) {
					  sortcol = args.sortCol.field;  // Maybe args.sortcol.field ???
					  dataView.sort(compareTimes, args.sortAsc);
					});


				grid.onAddNewRow.subscribe(function (e, args) {
					  $('#pc_message').html("");
				      var item = args.item;
				      grid.invalidateRow(data.length);
				      dataView.addItem(item);
				      dataView.refresh();
				      grid.updateRowCount();
				      grid.render();
				    });
				
				 grid.onKeyDown.subscribe(activateEdit);
				   
			},
			error : function(msg) {
				$('#pc_message').css('color','#ff0000');
				$('#pc_message').html('Fetch Plant Capacity Failed.');
			}
		});
	  }	
	  
	  function setItems(data, objectIdProperty) {
		     if (objectIdProperty !== undefined) idProperty = objectIdProperty;
		     items = data;
		     refreshIdxById();
		     refresh();
		}  
	  function getDispatchMapData(){
		  $('#pd_message').html("");
		  $.ajax({
				url : "v/1/list/plantdispatch/",
				type : "GET",
				contentType : "application/json",
				dataType : "json",
				async : true,
				success : function(json) {
					
					for(var i=0;i < json.rows.length;i++) {
						var d = (pd_data[i] = {});

					      d["dispatchTime"] = json.rows[i].dispatchTime.timeString;
					      d["plantDispatch"] = json.rows[i].plantDispatch.timeString;
					}
					pd_grid = new Slick.Grid("#pdGrid", pd_dataView, pd_columns, options);
					pd_grid.setSelectionModel(new Slick.RowSelectionModel());
					pd_grid.setSortColumn("plantDispatch",true);
					
					// wire up model events to drive the grid
					pd_dataView.onRowCountChanged.subscribe(function (e, args) {
						pd_grid.updateRowCount();
						pd_grid.render();
					});

					pd_dataView.onRowsChanged.subscribe(function (e, args) {
						pd_grid.invalidateRows(args.rows);
						pd_grid.render();
					});
					
					pd_dataView.beginUpdate();
					pd_dataView.setItems(pd_data,"dispatchTime");
					pd_dataView.endUpdate();
					pd_dataView.refresh();
					pd_grid.invalidate();
					$('#pdGrid').show();
					
					pd_grid.onKeyDown.subscribe(activateEdit);
					
					
					pd_grid.onSort.subscribe(function (e, args) {
						  sortcol = args.sortCol.field;  // Maybe args.sortcol.field ???
						  pd_dataView.sort(compareTimes, args.sortAsc);
						});

						
					pd_grid.onAddNewRow.subscribe(function (e, args) {
						  $('#pd_message').html("");
					      var item = args.item;
					      pd_grid.invalidateRow(pd_data.length);
					      pd_dataView.addItem(item);
					      pd_dataView.refresh();
					      pd_grid.updateRowCount();
					      pd_grid.render();
					    });
					
					
				},
				error : function(msg) {
					$('#pd_message').css('color','#ff0000');
					$('#pd_message').html('Fetch Plant Dispatch Failed.');
				}
			});
	  }
	  
	$(document).ready(function () {
		$("#exceptionDate" ).datepicker();
		getCapacityData();
		getDispatchMapData();
		getExceptions();
		
	  });
	
	 $(function() {
		    $("form#plantDispatchForm").submit(
		    		
		      function(e) {
		    	  e.preventDefault();
		    	  $.ajax({
					url : "v/1/save/plantdispatch/",
					type : "POST",
					data : "data=" + JSON.stringify(pd_dataView.getItems()),
					dataType : "html",
					async : false,
					success : function(json) {
						jsonRsp = JSON.parse(json);
						if(jsonRsp.status=='FAILED')
							$('#pd_message').html('Save Plant Dispatch Failed.').css('color','#ff0000');
						else
							$('#pd_message').html('Save Plant Dispatch Successful.').css('color','#0000ff');
						var gridId = $('#pdGrid').attr("class");
					     gridId = '#'+gridId.replace(" ui-widget","")+'plantDispatch';
					     $(gridId).click();
					      
					},
					error : function(msg) {
						$('#pd_message').html('Save Plant Dispatch Failed.').css('color','#ff0000');
					}
				});
		      }
		    );
		  });
	 
	 $(function() {
		    $("form#plantCapacityForm").submit(
		    		
		      function(e) {
		    	  e.preventDefault();
		    	  $.ajax({
					url : "v/1/save/plantcapacity/"+$("#dayOfWeek").val(),
					type : "POST",
					data : "data=" + JSON.stringify(dataView.getItems()),
					dataType : "html",
					async : false,
					success : function(json) {
						jsonRsp = JSON.parse(json);
						if(jsonRsp.status=='FAILED')
							$('#pc_message').html('Save Plant Capacity Failed.').css('color','#ff0000');
						else	
							$('#pc_message').html('Save Plant Capacity Successful.').css('color','#0000ff');
						var gridId = $('#myGrid').attr("class");
					     gridId = '#'+gridId.replace(" ui-widget","")+'dispatchTime';
					     $(gridId).click();
						
					},
					error : function(msg) {
						$('#pc_message').css('color','#ff0000');
						$('#pc_message').html('Save Plant Capacity Failed.').css('color','#ff0000');
					}
					
				});
		      }
		    );
		  });
	 
	
  </script>
  
	
</tmpl:put>
</tmpl:insert>