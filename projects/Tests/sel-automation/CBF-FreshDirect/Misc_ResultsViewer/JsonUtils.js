
  var JSON_UTILS = (function() {
			function readJsonFile(fileName) {
			try{
			var json ="";
			var Scr  = new ActiveXObject("Scripting.FileSystemObject");
			var CTF = Scr.OpenTextFile(fileName, 1, false);
			var data = CTF.ReadAll();
						if(data != "null")      //TODO: Need to check for any Utils available.
							json = eval ("(" + data + ")"); //JSON.parse(data)
						return json;	
				} 
			catch(oException){
					return null;
				}
			}
  return {
    readJsonFile: readJsonFile
  }
})();

 



