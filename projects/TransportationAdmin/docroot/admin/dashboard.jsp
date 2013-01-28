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
		
		<br/><br/><br/>
		
		<div class="cont_topright">
			<div class="cont_row">
				<div class="cont_Ritem">
				   		<br><br>		   
					<div style="width:50%">
						<div class="grid-header" style="width:50%">
				      		<label>Plant Capacity</label>
    					</div>
						<div id="myGrid" style="width: 50%; height: 425px;"></div>
						<div id="pager" style="width: 50%; height: 100px;"></div>
					</div>
					
					
				</div>
			</div>
		</div>
		<form id="plantCapacityForm" action="" method="POST">
  			<input type="submit" value="Save">
		</form>
  </div> 
  
  <script>
  
  function getCapacityData(){
	  
  }
  function requiredFieldValidator(value) {
	    if (value == null || value == undefined || !value.length) {
	      return {valid: false, msg: "This is a required field"};
	    } else {
	      return {valid: true, msg: null};
	    }
	  }

	  var grid;
	  var data = [];
	  var columns = [
	    {id: "dispatchTime", name: "dispatchTime", field: "dispatchTime", minWidth: 100, editor: Slick.Editors.Text},
	    {id: "capacity", name: "capacity", field: "capacity", width: 100, editor: Slick.Editors.Integer},
	   
	    ];
	  var options = {
	    editable: true,
	    enableAddRow: true,
	    enableCellNavigation: true,
	    asyncEditorLoading: false,
	    autoEdit: false,
	  };


	$(document).ready(function () {
			
		$("#dispatchDate" ).datepicker();
		$.ajax({
			url : "v/1/list/plantcapacity/",
			type : "GET",
			contentType : "application/json",
			dataType : "json",
			async : true,
			success : function(json) {
				
				for(var i=0;i < json.rows.length;i++) {
					var d = (data[i] = {});

				      d["dispatchTime"] = json.rows[i].dispatchTime.timeString;
				      d["capacity"] = json.rows[i].capacity;
				}
				grid = new Slick.Grid("#myGrid", data, columns, options);
				grid.setSelectionModel(new Slick.RowSelectionModel());
				$('#myGrid').show();
				
				grid.onAddNewRow.subscribe(function (e, args) {
					 console.log(args); 
				      var item = args.item;
				      grid.invalidateRow(data.length);
				      data.push(item);
				      grid.updateRowCount();
				      grid.render();
				    });
				    
				  //  grid.onSelectedRowsChanged.subscribe(
				    	//	function() { console.log(grid.getSelectedRows()); });
				  
					//var editedRows = {}
					//	grid.onAddNewRow.subscribe(function(e, args) {
			         //   var item = args.item;
			         //   editedRows[item.id] = item;
			       // });
				    
				    grid.onCellChange.subscribe(function (e,args) { 
			          console.log(args); 
			      });
				    
				
			},
			error : function(msg) {
				var errorText = eval('(' + msg.responseText+ ')');
				alert('Error : \n--------\n'+ errorText.Message);
			}
		});
	  });
	
	 $(function() {
		    $("form").submit(
		    		
		      function(e) {
		    	  e.preventDefault();
		    	  alert(JSON.stringify(data));
		    	  $.ajax({
					url : "v/1/save/plantcapacity/",
					type : "POST",
					data : "data=" + JSON.stringify(data),
					dataType : "html",
					async : false,
					success : function(json) {
						console.log("success");
					},
					error : function(msg) {
						console.log("failure");
					}
				})
				
		        
		        
		      }
		    );
		  });
	 
	
  </script>
  
	
</tmpl:put>
</tmpl:insert>