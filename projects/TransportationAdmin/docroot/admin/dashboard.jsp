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
	
	
  <div class="contentroot">
  		<div class="cont_topleft">
			<div class="cont_row">
				<div class="cont_Litem" id="page_Event Log">
					<div style="float:left;">
						<div style="margin-top:4px;float:left;font-weight:bold;">Date:&nbsp;</div>
						<div style="float:left;">
							<input style="width:85px;" maxlength="40" name="dispatchDate" id="dispatchDate" value='<c:out value="${dispatchDate}"/>' />
								                    			
							<span><input id="view_button" type="image" alt="View" src="./images/icons/view.gif"  onclick="javascript:getCapacityData();" onmousedown="this.src='./images/icons/view_ON.gif'" /></span>
				
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
				
				<div style="width: 300px;padding:0px 50px;float:left;">
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
	  var dd = grid.getData();
	  dd.splice(current_row,1);
	  var r = current_row;
	  while (r<dd.length){
	    grid.invalidateRow(r);
	    r++;
	  }
	  grid.updateRowCount();
	  grid.render();
	  grid.scrollRowIntoView(current_row-1);
	  }
  function removeRow2(current_row) {
	  var dd = pd_grid.getData();
	  dd.splice(current_row,1);
	  var r = current_row;
	  while (r<dd.length){
		  pd_grid.invalidateRow(r);
	    r++;
	  }
	  pd_grid.updateRowCount();
	  pd_grid.render();
	  pd_grid.scrollRowIntoView(current_row-1);
	  }
  
	  var grid, pd_grid;
	  var data = [];
	  var pd_data = [];
	  var pc_columns = [
	    {id: "dispatchTime", name: "Plant Dispatch", field: "dispatchTime", minWidth: 140,editor: TimeEditor, validator: requiredFieldValidator },
	    {id: "capacity", name: "Cumulative Capacity", field: "capacity", width: 130, editor: Slick.Editors.Integer, validator: requiredFieldValidator },
	    {id: 'id', name: '', field: 'dispatchTime',  width: 15, formatter: function (r, c, id, def, datactx) { 
	           return '<a href="#" onclick="removeRow1(' + r + ')">X</a>'; }
	      }
	    ];
	  var pd_columns = [
	            	    {id: "dispatchTime", name: "TransApp Dispatch", field: "dispatchTime", minWidth: 125,editor: TimeEditor, validator: requiredFieldValidator  },
	            	    {id: "plantDispatch", name: "Plant Dispatch", field: "plantDispatch", width: 125,editor: TimeEditor, validator: requiredFieldValidator },
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
	  
	  function TimeEditor(args) {
		  var $input;
		  var scope = this;
		  var defaultValue;
		  this.init = function () {
			  $input = $("<INPUT type=text class='editor-text' onblur='this.value=time(this.value);' />")
		          .appendTo(args.container)
		          .bind("keydown", scope.handleKeyDown)
				  .focus()
				  .select();
		    };
		    
		    this.handleKeyDown = function (e) {
			      if (e.keyCode === $.ui.keyCode.LEFT || e.keyCode === $.ui.keyCode.RIGHT) {
			        e.stopImmediatePropagation();
			      } if (e.keyCode === $.ui.keyCode.TAB) {
			    	  $input.val(time($input.val()));
			      }
			    };
			    this.destroy = function () {
			        $input.remove();
			      };

			      this.focus = function () {
			        $input.focus();
			      };

			      this.getValue = function () {
			        return $input.val();
			      };

			      this.setValue = function (val) {
			        $input.val(val);
			      };

			
			      this.loadValue = function (item) {
			          defaultValue = item[args.column.field] || "";
			          $input.val(defaultValue);
			          $input[0].defaultValue = defaultValue;
			          $input.select();
			        };

			        this.serializeValue = function () {
			          return $input.val();
			        };

			        this.applyValue = function (item, state) {
			          item[args.column.field] = state;
			        };

			        this.isValueChanged = function () {
			          return (!($input.val() == "" && defaultValue == null)) && ($input.val() != defaultValue);
			        };

			        this.validate = function () {
			            if (args.column.validator) {
			              var validationResults = args.column.validator($input.val());
			              if (!validationResults.valid) {
			                return validationResults;
			              }
			            }

			            return {
			              valid: true,
			              msg: null
			            };
			          };
			          
					this.init();
			    
		  }
	  
		 
	  function getCapacityData() {
	  $.ajax({
			url : "v/1/list/plantcapacity/"+$("#dispatchDate").val(),
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
				grid = new Slick.Grid("#myGrid", data, pc_columns, options);
				grid.setSelectionModel(new Slick.RowSelectionModel());
				grid.setSortColumn("dispatchTime",true);
				
				
				$('#myGrid').show();
				
				grid.onAddNewRow.subscribe(function (e, args) {
					  $('#pc_message').html("");
				      var item = args.item;
				      grid.invalidateRow(data.length);
				      data.push(item);
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
	  
	  
	  function activateEdit(e, args) {
		  var keyCode = $.ui.keyCode,
		      col,
		      activeCell = this.getActiveCell();

		  /////////////////////////////////////////////////////////////////////
		  // Allow instant editing like MS Excel (without presisng enter first
		  // to go into edit mode)
		  if (activeCell) {
		    col = activeCell.cell;

		    // Only for editable fields and not if edit is already in progress
		    if (this.getColumns()[col].editor && !this.getCellEditor()) {
		      // Ignore keys that should not activate edit mode
		      if ($.inArray(e.keyCode, [keyCode.LEFT, keyCode.RIGHT, keyCode.UP,
		                               keyCode.DOWN, keyCode.PAGE_UP, keyCode.PAGE_DOWN,
		                               keyCode.SHIFT, keyCode.CONTROL, keyCode.CAPS_LOCK,
		                               keyCode.HOME, keyCode.END, keyCode.INSERT,
		                               keyCode.ENTER]) === -1) {
		        this.editActiveCell();
		      }
		    }
		  }
		}
	  
	  function getDispatchMapData(){
		  
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
					pd_grid = new Slick.Grid("#pdGrid", pd_data, pd_columns, options);
					pd_grid.setSelectionModel(new Slick.RowSelectionModel());
					pd_grid.setSortColumn("plantDispatch",true);
					
					$('#pdGrid').show();
					pd_grid.onKeyDown.subscribe(activateEdit);
					
					pd_grid.onAddNewRow.subscribe(function (e, args) {
						  $('#pd_message').html("");
					      var item = args.item;
					      pd_grid.invalidateRow(pd_data.length);
					      pd_data.push(item);
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
		$("#dispatchDate" ).datepicker();
		getCapacityData();
		getDispatchMapData();
	  });
	
	 $(function() {
		    $("form#plantDispatchForm").submit(
		    		
		      function(e) {
		    	  e.preventDefault();
		    	  $.ajax({
					url : "v/1/save/plantdispatch/",
					type : "POST",
					data : "data=" + JSON.stringify(pd_data),
					dataType : "html",
					async : false,
					success : function(json) {
						$('#pd_message').html('Save Plant Dispatch Successful.');
					},
					error : function(msg) {
						$('#pd_message').css('color','#ff0000');
						$('#pd_message').html('Save Plant Dispatch Failed.');
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
					url : "v/1/save/plantcapacity/"+$("#dispatchDate").val(),
					type : "POST",
					data : "data=" + JSON.stringify(data),
					dataType : "html",
					async : false,
					success : function(json) {
						$('#pc_message').html('Save Plant Capacity Successful.');
					},
					error : function(msg) {
						$('#pc_message').css('color','#ff0000');
						$('#pc_message').html('Save Plant Capacity Failed.');
					}
					
				});
		      }
		    );
		  });
	 
	
  </script>
  
	
</tmpl:put>
</tmpl:insert>