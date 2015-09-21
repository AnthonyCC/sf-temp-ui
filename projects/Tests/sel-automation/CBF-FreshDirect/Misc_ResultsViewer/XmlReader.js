

var XML_READER = (function(){

	var n;
	function readXmlFile(xmlFilePath) {
  try {
	  var Scr  = new ActiveXObject("Scripting.FileSystemObject");
		var CTF = Scr.OpenTextFile(xmlFilePath, 1, false);
		var data = CTF.ReadAll(); 
		n = data.split("</log>");
		CTF .Close();
  } catch(oException) {

	} 
		
	};

  function getSettings(){
    var oSetting = {};
    for(var i=0; i<n.length; i++){			
		  var oXML = new ActiveXObject("Microsoft.XMLDOM"); 
			oXML.loadXML(n[i]+"</log>");
      if(oXML.childNodes[0].attributes[0].nodeValue == "setting"){
        oSetting = eval ("(" + oXML.text.toString().replace(/\\/g,"\\\\") + ")");
        break;
      }
		}
    return oSetting;
  }
  
  function getComponents(){
    //aConsComponents = all modules + their components for the project/application
    //aComponents = Iteration wise modules used
    var aComponents = [];
    var aConsComponents = [];
    for(var ix=0; ix<n.length-2; ix++){			
		  var oXML = new ActiveXObject("Microsoft.XMLDOM"); 
			oXML.loadXML(n[ix]+"</log>");
      if(oXML.childNodes[0].attributes[0].nodeValue == "components")
        aComponents.push(eval ("(" + oXML.text + ")"));
		}
	  var jsonObj =  JSON_UTILS.readJsonFile("Components.json");
	  var cIx=0;
	  if(jsonObj == null || jsonObj==""){
		  aConsComponents.push(aComponents[0]);
		  cIx=1;
	  }else{
		  aConsComponents.push(jsonObj);
	  }
	  var consCompIx = 0;
    for(; cIx<aComponents.length; cIx++){
		for(cnIx=0; cnIx<aConsComponents.length; cnIx++){
			for(var comp in aComponents[cIx]){
				if(aConsComponents[cnIx][comp] == undefined){
					aConsComponents[cnIx][comp] = aComponents[cIx][comp];
					consCompIx = consCompIx + 1;
				}else{
					for(var axn in aComponents[cIx][comp]){
						var axnFound = false;
							for(var axnCon in aConsComponents[cnIx][comp]){
								if(aConsComponents[cnIx][comp][axnCon].componentCode == aComponents[cIx][comp][axn].componentCode){
									axnFound = true;
									break;
								}
							}

						if(!axnFound)
						{
							var axnLen = aConsComponents[cnIx][comp].length;
							aConsComponents[cnIx][comp][axnLen] = aComponents[cIx][comp][axn];
						}
					}
				}}}}
	
    var components = [];
	  for(var evt in aEvents){
		if(aEvents[evt].entity.entityType == "Component" && aEvents[evt].eventType == "FINISH"){
      var event = {};
      event = aEvents[evt];
      event.actual = aEvents[evt-1].eventData.Details.actual;
      event.expected = aEvents[evt-1].eventData.Details.expected;
      event.screenDumpName = aEvents[evt-1].eventData.Details.screenDumpName;
			components.push(aEvents[evt]);
		}
    }
    
    for(var axn in components){
		for(var mod in aConsComponents[0]){
			for(var component in aConsComponents[0][mod])
				if(aConsComponents[0][mod][component].componentCode == components[axn].parent.entity.entityDetails.componentCode && 
           components[axn].parent.entity.entityDetails.moduleCode==mod ){
					  var len = aConsComponents[0][mod][component].occurrences.length;
					  aConsComponents[0][mod][component].occurrences[len] = components[axn];
				}
		  }
	  }
	  return aConsComponents;
  }
  
  var aEvents = [];
  function getTree(){
    for(var i=0; i<n.length-1; i++){			
		  var oXML = new ActiveXObject("Microsoft.XMLDOM"); 
			oXML.loadXML(n[i]+"</log>");
      if(oXML.childNodes[0].attributes[0].nodeValue == "event")
        aEvents.push(eval ("(" + oXML.text + ")"));
		}

    var allEntities = [];
    //Create allEntities array 
	  
    for(var ix=0; ix<aEvents.length; ix++){
      if(aEvents[ix].entity.entityName != undefined){
			  if(aEvents[ix].entity.entityType != "Action"){
				  aEvents[ix].entity.eventData = aEvents[ix].eventData
				  allEntities.push(aEvents[ix].entity);
			  }
			  else{
				  aEvents[ix].entity.eventData = aEvents[ix+1].eventData
			  	allEntities.push(aEvents[ix].entity);
			  }
		  }
    }
      
    //Replace entity id by actual entity object and parent id by parent object
      for(var event in aEvents){
        var entityId = aEvents[event].entity.id;
        for(var ent in allEntities){
          if(allEntities[ent].id==entityId){
            var entity = allEntities[ent];
            break;
          }
        }
        if(entity){
          aEvents[event].entity = entity;
        }
      }
    
      for(var evt in aEvents){
        if(aEvents[evt].entity.parentId)
          aEvents[evt].parent = getParent(aEvents, aEvents[evt].entity.parentId);
      }
		  var aTCEvents = makeTCArray(aEvents);
		  return aTCEvents;
  }
  
  function getChildren(aAllEvents, entId){
    var children = [];
    for(var ent in aAllEvents){
      if(aAllEvents[ent].entity.entityType!= "ACTION" && 
         aAllEvents[ent].entity.parentId == entId && 
         (aAllEvents[ent].eventType == "FINISH") ){ 
		      if(aAllEvents[ent].entity.entityType=="Iteration" && 
             aAllEvents[ent].entity.entityName==0 ){
		      	return getChildren(aAllEvents,aAllEvents[ent].entity.id);
		      }
			  children.push(aAllEvents[ent]);
      }
    }
    for(var child in children){
      children[child].children = getChildren(aAllEvents, children[child].entity.id);
    }
    return children;
  }

  function getParent(allEntities, prntId){
    var parent;
    for(var ent in allEntities){
      if(allEntities[ent].entity.id == prntId){
        parent = allEntities[ent];
        break;
      }
    }
    return parent;
  }
  
  //Create TCEvents array
  function makeTCArray(aAllEvents){
    var aTCEvents = [];
    var tcIndex = 0;
    for(var event in aAllEvents){
      if(aAllEvents[event].entity.entityType == "TestCase"){
        switch(aAllEvents[event].eventType){
          case "FINISH":  
            aTCEvents.push(aAllEvents[event]);
            aTCEvents[tcIndex].children = getChildren(aAllEvents, aAllEvents[event].entity.id);
            tcIndex += 1;
            break;
        }   
     }
    }
    return aTCEvents;
  }
	return {
		readXmlFile: readXmlFile,
		getSettings: getSettings,
		getComponents: getComponents,
		getTree: getTree
	};    
})();



