<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='bean' prefix='bean'%>
<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri='crm' prefix='crm'%>

<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='content' direct='true'>
<!--jQuery dependencies-->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
<!--ParamQuery Grid files-->
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.1/themes/hot-sneaks/jquery-ui.css" />
<link rel="stylesheet" href="/assets/javascript/paramquery-2.0.3/pqgrid.min.css" />
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/themes/humanity/jquery-ui.css" />

<script type="text/javascript" language="javascript" src="/assets/javascript/paramquery-2.0.3/pqgrid.min.js"></script>

<script type="text/javascript">
	function displayGrid(response) {
		
		var data = jQuery.parseJSON(response.responseText);
		var obj = {};
		obj.width = 1000;
		obj.height = 400;

		obj.colModel = [  {
			title : "CUSTOMER_ID",
			width : 110,
			dataType : "integer",
			align : "left",
			dataIndx : "customerId"
		},
	                 
		{
			title : "EMAIL_ID",
			width : 150,
			align : "left",
			dataType : "string",
			dataIndx : "customerEmailId"
		}, 
		{
			title : "SO_TEMPLATE_ID",
			width : 110,
			dataType : "string",
			dataIndx : "soTemplateId"
		},
		{
			title : "CUSTOMER_LIST_ID",
			width : 110,
			dataType : "string",
			dataIndx : "listId"
		}, 	
		{
			title : "SKU_CODE",
			width : 110,
			dataType : "string",
			align : "center",
			dataIndx : "skuCode"
		}, 
		{
			title : "QUANTITY",
			width : 80,
			dataType : "integer",
			align : "right",
			dataIndx : "quantity"
		}, 
		{
			title : "SALES_UNIT",
			width : 80,
			dataType : "string",
			align : "right",
			dataIndx : "salesUnit"
		},  
		{
			title : "FREQUENCY",
			width : 80,
			dataType : "integer",
			align : "right",
			dataIndx : "frequency"
		} ];

		obj.dataModel = {
			data : data
		};

		obj.toolbar = {
			cls : 'pq-toolbar-export',
			items : [ {
				type : 'button',
				label : "Export to Excel",
				icon : 'ui-icon-document',
				listeners : [ {
					"click" : function(evt) {
						$("#grid_array").pqGrid("exportExcel", {
							url : "/api/excelExportServlet",
							sheetName : "SKU CODE SHEET"
						});
					}
				} ]
			} ]
		};

		obj.selectionModel = {
			mode : 'single'
		};
		obj.editable = false;
		obj.scrollModel = {
			horizontal : false
		};
		obj.flexHeight = true;
		obj.flexWidth = true;
		obj.showTitle = false;
		obj.showBottom = true;
		
		$("#grid_array").pqGrid(obj);
		$("#grid_array").pqGrid("refreshDataAndView");
	};
	
	function sendData() {
		var existingSKU = $('#existingSKU').val();
		var replacementSKU = $('#replacementSKU').val();
		var buttonVal = $('#buttonId').val();
		$('#loadingmessage').show();
			$.ajax({
			cache : false,
			type : "POST",
			url : "/api/skuReplaceServlet",
			location : "local",
			sorting : "local",
			dataType : "JSON",
			curPage : 1,
			data : {
				existingSKU : existingSKU,
				replacementSKU : replacementSKU,
				buttonVal : buttonVal
			},
			
			 complete : function(response) {
				var res = response.result;
				var data = jQuery.parseJSON(response.responseText);
				$("#validation_message").show();
				 if(buttonVal == 'Check' && data.result != null){
					$("#validation_message").css("color","red");
		       		$("#validation_message").html(data.result);
		       		$('#existingSKU').attr("disabled", false);
	 				$('#replacementSKU').attr("disabled", false);
	 				$('#loadingmessage').hide();
	        	}
				 else if(buttonVal == 'Check' && data.result == null){				 
 				 var y = response.responseText;
 				 if(y != '[]'){
 					$("#validation_message").css("color","blue");
 					 $("#validation_message").html("Recommended to take backup of Grid by clicking on left top corner 'Export to Excel' before replacing the existing SKUs .Click on Replace button to replace SKUs");	
 					 $('#buttonId').val('Replace');
 					 $('#existingSKU').attr("disabled", true);
 	 				 $('#replacementSKU').attr("disabled", true);
 	 				 $('#resetform').show();
 	 				$('#loadingmessage').hide();
 	 				$('#swapId').hide();
 				 }
 				 else{
 					$("#validation_message").css("color","red");
 					 $('#existingSKU').attr("disabled", false);
 	 				 $('#replacementSKU').attr("disabled", false);
 	 				 $('#validation_message').html('Replacement cannot be performed because '+ existingSKU.toUpperCase() + ' is not present in any Standing Order.Click on Clear button to clear fields');	
 	 				 $('#loadingmessage').hide();
 				 }
 				displayGrid(response);
				}
				else if(buttonVal == 'Replace' && data.result != null){
					$("#validation_message").css("color","red");
	        		$("#validation_message").html(data.result); 
	        		$('#existingSKU').attr("disabled", false);
	 				$('#replacementSKU').attr("disabled", false);	 			
	        		$('#buttonId').val('Check');
	        		$('#loadingmessage').hide();
	        	}
	        	else if(buttonVal == 'Replace' && data.result == null){        		
	         		$("#validation_message").css("color","blue");
	        		$("#validation_message").html(existingSKU.toUpperCase() + ' replaced successfully with ' + replacementSKU.toUpperCase() + '.Click on Clear button to Reset fields');		        		
	        		$('#existingSKU').attr("disabled", false);
	 				$('#replacementSKU').attr("disabled", false);	 				
	        		$('#buttonId').val('Check');	
	        		displayGrid(response);
	        		$('#resetform').show();
	        		$('#loadingmessage').hide();
	        		$('#swapId').show();
	        		
	        	} 
			}  

		});
		
	}
	
	$(document).ready(function() {
		
		 $("input").focus(function(){
			    $(this).css("background-color","#cccccc");
		});
		 
	     $("input").blur(function(){
			    $(this).css("background-color","#ffffff");
		});
			  
		$('#buttonId').click(function() {		 
			sendData();		   			
		});
	});
	
	function myFunction() {
		$("#validation_message").hide();
		$("#grid_array").pqGrid("destroy");
		$('#existingSKU').attr("disabled", false);
		$('#replacementSKU').attr("disabled", false);
		$('#buttonId').val('Check');
		$('#swapId').show();
	}
	
	function mySwapFunction() {
		
		var existingSKU1 = $('#existingSKU').val();
		var replacementSKU1 = $('#replacementSKU').val();
		
		$('#replacementSKU').val(existingSKU1);
		$('#existingSKU').val(replacementSKU1);
	
	}
</script>

<crm:GetCurrentAgent id="currentAgent">
	<jsp:include page="/includes/crm_standing_orders_nav.jsp" />
	<table width="100%">
		<tr>
			<td colspan="3"><div class="promo_page_header_text">Manage Standing Order SKU Replacement
					</div></td>
		</tr>
	</table>
			
	<form id="configform">
		<table>
			<tr>
				<td>
					<h4 ><div id="validation_message"></div></h4>
							
				</td>
			</tr>
			
			<tr>
				<td>Existing SKU: <input type="text" id="existingSKU" name="existingSKU">  
										
  					<img src="/media_stat/crm/images/swap_icon.gif" id = 'swapId' title="Swap Input fields" onclick="mySwapFunction();"/>					
									
				Replacement SKU: <input type="text" id="replacementSKU" name="replacementSKU">
					
				<input type="button" id="buttonId" value="Check">
				
				<input type = 'reset' id = 'resetform' value = 'Clear' onclick="myFunction()" title= "Clear Input Fields"/>
				
				
				</td>
			</tr>
			</br>
			</br>
			<tr>
			</tr>
			<tr>
				<td>
					<div id="grid_array"/>
								
				</td>
			</tr>
				
			<tr>
				<td>
					<div align="right" id='loadingmessage' style='display:none'>
     						<img align="center" src="/media_stat/crm/images/ajax-loader.gif"/></div>									
				</td>
			</tr>
		</table>
	</form>		
</crm:GetCurrentAgent>
</tmpl:put>
</tmpl:insert>





