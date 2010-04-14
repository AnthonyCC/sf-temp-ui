<%@ page import='java.util.Random' %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Calendar" %>
<%!

	public String getRandomString(int len) {
		int length = 16;
		if (len > 0) { length = len; }
		Random generator = new Random();
		StringBuffer ranString = new StringBuffer();
		char[] buf = new char[length];
		String charset = "abcdefghijklmonpqrstuvwxyz0123456789é";
		for (int i = 0; i < buf.length; i++) {
			ranString.append(charset.charAt(generator.nextInt(charset.length())));
		}

		return ranString.toString();
	}

%>

<html>
<head>
	<style>
		.scrollingDiv {
			height: 100px;
			overflow-y: scroll;
			border: 1px solid #aaa;
		}
	</style>
	<script src="/assets/javascript/prototype.js" type="text/javascript" language="javascript"></script>

	<script type="text/javascript">
		function deptsObj() {
			
			this.debug = true;	// debug mode

			this.log = function () {
				var logMsgs = new Array();
				for (var i = 0; i < arguments.length; i++) {
					logMsgs.push(arguments[i]);
				}
				var logMsg = logMsgs.join(",");

				if (logMsg.constructor == Array) { console.log("logMsg is arr"); logMsg = logMsg.join(","); }
				var time = new Date();
				var timeNow = time.getHours()+":"+time.getMinutes()+":"+time.getSeconds()+"."+time.getMilliseconds();
				if (this.debug) {
					if (window.console) {
						console.log(timeNow+' Log: '+logMsg);
					}
				}
			}

			this.deptList = new Array(0);

			this.addDept = function(deptId, name) {
				if (!this.deptList[deptId]) {
					this.deptList[deptId] = new Array(0, new Array());
				}
				this.deptList[deptId][0] = name;
				this.log("added dept", this.deptList[deptId][0]);
				
				this.log("added dept", deptId, name);
			}

			this.addCatToDept = function (deptId, catId, name) {
				if (!this.deptList[deptId]) {
					this.deptList[deptId] = new Array(0, new Array());
					this.log("addToCat, dept didn't exist", deptId);
				}
				if (this.deptList[deptId][catId]) {
					this.log("addToCat, cat already exists?", catId);
				}else{
					this.deptList[deptId][1][catId] = name;
				}
				this.log("addCatToDept", deptId, catId, name);
			}
			
			this.getDepts = function() {
				var deptList = new Array();
				for (dept in this.deptList) {
					if (typeof(dept) != "string" || typeof(this.deptList[dept][0]) != "string") { break; }
					deptList.push(dept, this.deptList[dept][0]);
				}
				console.log(deptList);

				return deptList;
			}

			this.getCatsFromDept = function(deptId) {
				if (this.deptList[deptId]) {
					var catList = new Array();
					//fetch cats under this id
					for (cat in this.deptList[deptId][1]) {
						if (typeof(cat) != "string" || typeof(this.deptList[deptId][1][cat]) != "string") { break; }
						catList.push(cat, this.deptList[deptId][1][cat]);
					}
					console.log(typeof(this.deptList[deptId][1][cat]));

					this.log("getCatsFromDept", "fetching cats", this.deptList[deptId][1]);
					//this.log("getCatsFromDept", "fetching cats", deptId, catList);}
				}else{
					this.log("getCatsFromDept", "deptId does not exist?", deptId);
				}

				return catList;
			}

			this.populateDeptList = function (deptListId) {
				var deptList = this.getDepts();
				//clean list
				$(deptListId).options.length = 0;

				var optCount = 0;
				var optTexts = new Array();
				var optValues = new Array();

				for (i=0; i < deptList.length; i++) {
					
					if (i%2) {
						optTexts[optTexts.length] = deptList[i];
					}else{
						optValues[optValues.length] = deptList[i];
					}

				}

				for (i=0; i < optTexts.length; i++) {
					$(deptListId).options[$(deptListId).options.length] = new Option(optTexts[i], optValues[i]);
				}
			}

			this.populateCatList = function (catListId, deptListId) {

				var deptId = $(deptListId).value;

				var catList = this.getCatsFromDept(deptId);

				console.log(catList);

				//clean list
				$(catListId).options.length = 0;

				var optCount = 0;
				var optTexts = new Array();
				var optValues = new Array();

				for (i=0; i < catList.length; i++) {
					
					if (i%2) {
						optTexts[optTexts.length] = catList[i];
					}else{
						optValues[optValues.length] = catList[i];
					}

				}

				for (i=0; i < optTexts.length; i++) {
					$(catListId).options[$(catListId).options.length] = new Option(optTexts[i], optValues[i]);
				}
			}

		}
		window['depts'] = new deptsObj();
	</script>

</head>
<body>
<div style="font-size: 11px; font-family: monospace;">
<%
	Calendar cal1 = Calendar.getInstance();
	//out.println(cal1.getTimeInMillis());
	//out.println(new Date());

	Random isObsolete = new Random();
	Random isDupe = new Random();
	int ListsLength = 100;
	HashMap finalMap = new HashMap();

	//use real list
	//List mapList = mapsList;

	//Create a map list
	/**/
		List mapList = new ArrayList();

		for (int i = 0; i < ListsLength; i++) {
			HashMap curMap = new HashMap();
			String countD = Integer.toString(1000+i);
			String countC = Integer.toString(2000+i);
			curMap.put("DEPTID",countD);
			curMap.put("CATID",countC);
			curMap.put("OBSOLETE",(isObsolete.nextBoolean())?"X":"");
			mapList.add(curMap);
		}
		out.println("Map List "+mapList.size() +"<br />");
		%><div class="scrollingDiv"><%
		out.println(mapList);
		%></div><%
		%><hr /><%
	
	
	//use real list
	//List catList = catsList;

	//create a cat list
	/**/
		List catList = new ArrayList();
		String curCatId = getRandomString(8);
		
		for (int i = 0; i < ListsLength; i++) {
			String countC = Integer.toString(2000+i);
			curCatId =(isDupe.nextBoolean())?curCatId:getRandomString(8);

			HashMap curCat = new HashMap();
			curCat.put("ID",countC);
			curCat.put("CATID",curCatId);
			curCat.put("NAME","Category "+countC);
			curCat.put("OBSOLETE",(isObsolete.nextBoolean())?"X":"");
			catList.add(curCat);
		}
	
		out.println("Cat List "+catList.size() +"<br />");
		%><div class="scrollingDiv"><%
		out.println(catList);
		%></div><%
		%><hr /><%
	
	//use real list
	//List deptList = deptsList;

	//create a dept list
	/**/
		List deptList = new ArrayList();
		String curDeptId = getRandomString(8);
		
		for (int i = 0; i < ListsLength; i++) {
			String countD = Integer.toString(1000+i);
			curDeptId =(isDupe.nextBoolean())?curDeptId:getRandomString(8);
			
			HashMap curDept = new HashMap();
			curDept.put("ID",countD);
			curDept.put("DEPTID",curDeptId);
			curDept.put("NAME","Department "+countD);
			curDept.put("OBSOLETE",(isObsolete.nextBoolean())?"X":"");
			deptList.add(curDept);
		}
	
		%><div class="scrollingDiv"><%
		out.println("Dept List "+deptList.size() +"<br />");
		out.println(deptList);
		%></div><%
		%><hr /><%
	

		%><hr /><%

	//loop through map and only keep non-obsolete
		List mapListCopy = new ArrayList();
		Iterator mapListI = mapList.iterator();
		while (mapListI.hasNext()) {
			HashMap curMap = new HashMap();
			curMap = (HashMap)mapListI.next();

			Set mapSet = curMap.entrySet();
			Iterator mapSetI = mapSet.iterator();
			
			while(mapSetI.hasNext()){
				Map.Entry me = (Map.Entry)mapSetI.next();
				if ("OBSOLETE".equals(me.getKey()) && !"X".equals(me.getValue())) {
					mapListCopy.add(curMap);
				}
			}
		}
			//replace original list with fixed list
			mapList = mapListCopy;
			out.println("Map List (clean) "+mapList.size() +"<br />");
			%><div class="scrollingDiv"><%
			out.println(mapList);
			%></div><%
			%><hr /><%


	//loop through non-obsolete list and put values into valid list
		//List validIds = new ArrayList();
		List<String> validMapsList = new ArrayList<String> ();
		//HashMap validMaps = new HashMap();

		Iterator mapListI2 = mapList.iterator();
		while (mapListI2.hasNext()) {
			HashMap curMap = new HashMap();
			curMap = (HashMap)mapListI2.next();

			Set mapSet = curMap.entrySet();
			Iterator mapSetI = mapSet.iterator();

			Object catObj = null;
			Object deptObj = null;
			
			while(mapSetI.hasNext()){
				Map.Entry me = (Map.Entry)mapSetI.next();
				if ("DEPTID".equals(me.getKey())) {
					//validIds.add(me.getValue());
					deptObj = me.getValue();
				}
				if ("CATID".equals(me.getKey())) { 
					//validIds.add(me.getValue());
					catObj = me.getValue();
				}
			}
			if (deptObj != null && catObj != null) {
				//validMaps.put(deptObj, catObj);
				if (!validMapsList.contains(deptObj+"_"+catObj)) {
					validMapsList.add(deptObj+"_"+catObj);
				}
			}
		}
			out.println("Valid Map List Before "+validMapsList.size() +"<br />");
			%><div class="scrollingDiv"><%
			out.println(validMapsList);
			%></div><%
			%><hr /><%

	//loop through cat and only keep non-obsolete
		List catListCopy = new ArrayList();
		Iterator catListI = catList.iterator();
		while (catListI.hasNext()) {
			HashMap curCat = new HashMap();
			curCat = (HashMap)catListI.next();

			Set catSet = curCat.entrySet();
			Iterator catSetI = catSet.iterator();

			//iterate over list and mark non-obsolete, VALID cats
			//boolean isValidCat = false;
			boolean isObsoleteCat = true;

			Object catSeqId = new Object();
			
			while(catSetI.hasNext()){
				Map.Entry me = (Map.Entry)catSetI.next();
				if ("OBSOLETE".equals(me.getKey())) {
					isObsoleteCat = "X".equals(me.getValue());
				}
				if ("ID".equals(me.getKey())) {
					catSeqId = me.getValue();
					//isValidCat = validIds.contains(me.getValue());
					//if (isValidCat) { catSeqId = me.getValue(); }
					catSeqId = me.getValue();
				}
			}
			//add to copy and to final HashMap
			//if (isValidCat && !isObsoleteCat) {
			if (!isObsoleteCat) {
				catListCopy.add(curCat);

				finalMap.put(catSeqId, curCat);
			//}else{
				//remove obsoletes from valid list
			//	if (validIds.indexOf(catSeqId) > -1) { validIds.remove(validIds.indexOf(catSeqId)); }
			}
		}
			//replace original list with fixed list
			catList = catListCopy;
			out.println("Cat List (clean) "+catList.size() +"<br />");
			%><div class="scrollingDiv"><%
			out.println(catList);
			%></div><%
			%><hr /><%

	//loop through dept and only keep non-obsolete
		List deptListCopy = new ArrayList();
		Iterator deptListI = deptList.iterator();
		while (deptListI.hasNext()) {
			HashMap curDept = new HashMap();
			curDept = (HashMap)deptListI.next();

			Set deptSet = curDept.entrySet();
			Iterator deptSetI = deptSet.iterator();
			
			//iterate over list and mark non-obsolete, VALID depts
			//boolean isValidDept = false;
			boolean isObsoleteDept = true;

			Object deptSeqId = new Object();
			
			while(deptSetI.hasNext()){
				Map.Entry me = (Map.Entry)deptSetI.next();
				if ("OBSOLETE".equals(me.getKey())) {
					isObsoleteDept = "X".equals(me.getValue());
				}
				if ("ID".equals(me.getKey())) {
					deptSeqId = me.getValue();
					//isValidDept = validIds.contains(me.getValue());
					//if (isValidDept) { deptSeqId = me.getValue(); }
					deptSeqId = me.getValue();
				}
			}
			//add to copy and to final HashMap
			//if (isValidDept && !isObsoleteDept) {
			if (!isObsoleteDept) {
				deptListCopy.add(curDept);

				finalMap.put(deptSeqId, curDept);
			//}else{
				//remove obsoletes from valid list
				//if (validIds.indexOf(deptSeqId) > -1) { validIds.remove(validIds.indexOf(deptSeqId)); }
				//remove obsoletes from valid map
				//validMaps.remove(deptSeqId);
			}
		}
			//replace original list with fixed list
			deptList = deptListCopy;
			out.println("Dept List (clean) "+deptList.size() +"<br />");
			%><div class="scrollingDiv"><%
			out.println(deptList);
			%></div><%
			%><hr /><%


	//output final map
		out.println("Final Map (clean) "+finalMap.size() +"<br />");
			%><div class="scrollingDiv"><%
		out.println(finalMap);
			%></div><%
		%><hr /><%

	//loop through VALID MAPS, fetching data from FINAL MAP
		out.println("--------- <br />");
			%><div class="scrollingDiv"><%

			Iterator validMapsListI = validMapsList.iterator();
			String[] curItemArr;
			String curItem = "";

			while (validMapsListI.hasNext()) {
				HashMap curDept = new HashMap();
				HashMap curCat = new HashMap();

				curItem = (String)validMapsListI.next();
				curItemArr = curItem.split("_");

				//out.println("curItem is "+curItemArr[0]+" = "+curItemArr[1]+"</br />");
				

				curDept = (HashMap)finalMap.get(curItemArr[0]);
				curCat = (HashMap)finalMap.get(curItemArr[1]);

					String curDeptIdString = "";

					HashMap curMap = new HashMap();
					//if either of these are null, the dept/cat is obsolete
					if (curDept != null && curCat != null) {
						//get the info from Final Map via key here, for depts
						if (curDept != null) {
							//out.println("curDept is "+curDept.getClass()+" "+me.getKey()+"</br />");
							curMap = curDept;
							//no need to iterate, output for javascript
							//out.println(curMap.get("DEPTID"));

							curDeptIdString = curMap.get("DEPTID").toString();

							%><script type="text/javascript">
								depts.addDept("<%=curMap.get("DEPTID")%>", "<%=curMap.get("NAME")%>");
							</script>
							
							<pre>addDept("<%=curMap.get("DEPTID")%>", "<%=curMap.get("NAME")%>");</pre>
							<%
							/*
								Set mapSet = curMap.entrySet();
								Iterator mapSetI = mapSet.iterator();
								while(mapSetI.hasNext()){
									Map.Entry me2 = (Map.Entry)mapSetI.next();
									out.println("["+me2.getKey()+"] <br />");
									out.println("["+me2.getValue()+"] <br />");
								}
							*/
						}
						if (curCat != null) {
							//out.println("curCat is"+curCat.getClass()+" "+me.getValue()+"</br /></br />");
							curMap = curCat;

							%><script type="text/javascript">
								depts.addCatToDept("<%=curDeptIdString%>", "<%=curMap.get("CATID")%>", "<%=curMap.get("NAME")%>");
							</script>
							
							<pre>addCatToDept("<%=curDeptIdString%>", "<%=curMap.get("CATID")%>", "<%=curMap.get("NAME")%>");</pre>
							<%
							/*
								Set mapSet = curMap.entrySet();
								Iterator mapSetI = mapSet.iterator();
								while(mapSetI.hasNext()){
									Map.Entry me2 = (Map.Entry)mapSetI.next();
									//out.println("["+me2.getKey()+"] <br />");
									//out.println("["+me2.getValue()+"] <br />");
								}
							*/
						}
					}
			}
			%></div><%
		out.println("--------- <br />");
	%>
	
	<select id="deptList" onchange="depts.populateCatsList('catList', this.id);"></select>
	<select id="catList" ></select>

	<script type="text/javascript">
	<!--
		depts.populateDeptsList("deptList");
		depts.populateCatsList('catList', 'deptList')
	//-->
	</script>

<%
	//Thread.sleep(4000);
	//out.println(new Date());
	Calendar cal2 = Calendar.getInstance();
	//out.println(cal2.getTimeInMillis());

	out.println("<br />Execution Time: "+(cal2.getTimeInMillis() - cal1.getTimeInMillis())+" MS");

%>
</div>



</body>
</html>