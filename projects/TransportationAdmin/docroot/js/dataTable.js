function Table()
{	
	this.panelId;
	this.containerId;
	this.title;
	this.result;
	this.pagination=10;
	this.columns;
}

 
Table.prototype.render=function()
{
	var panel=init(this.panelId,this.title);
	panel.render(document.body);
    panel.show();
    populateTable(this.result,this.columns,this.pagination,this.containerId);
}

                                
	function init(panelId,title) 
     {
       var panel = new YAHOO.widget.Panel(panelId, {
                         fixedcenter: true, 
                         close: true, 
                         draggable: false, 
                         zindex:4,
                         modal: true,
                         visible: false,
                         effect:{effect:YAHOO.widget.ContainerEffect.SLIDE,duration:0.25}});  
        panel.setHeader(title);
        return panel;            
               		 
      }
    
  
  function populateTable(result,columnDef,page,containerId)
  {
  		
  		var myDataSource = new YAHOO.util.LocalDataSource(result.list);
	   
		var myConfigs = { 
	    paginator : new YAHOO.widget.Paginator({ 
	        rowsPerPage    : page
	    }) 
		};
  		var myDataTable = new YAHOO.widget.DataTable(containerId, columnDef, myDataSource,myConfigs);   		  		
  }

 