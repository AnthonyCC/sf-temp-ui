<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>


<% boolean isGuest = false; %>
	<crm:GetCurrentAgent id="currentAgent">
		<%-- isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); --%>
	</crm:GetCurrentAgent>
	
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Address Scrubbing Tool</tmpl:put>
<tmpl:put name='content' direct='true'>
<form method="post" enctype="multipart/form-data" action="/addressscrubbing.jsp">
<div style="background: #fff; overflow: auto; width: 100%; height: 420px;">
	<div class="scrubbingText">Address Scrubbing Tool:</div>
	<span id="errMsg" class="error1"></span>
	<div class="scrubbingText">Please choose your file:</div>
	<div class="scrubbingText">
		<input type="file" name="fileupload" id="fileupload" style="display: none" onchange="verifyFile()" accept=".csv"/>
		<input type="text" id="filename" readonly="true" disabled="disabled" value="Choose.csv file"/><span></span><span></span>
		<input type="button" value="Choose file" class="choose_file" id="fakeBrowse" onclick="handleBrowseClick();"/><br><br>
		<input type="submit" id = "submit" value="Scrub File" class="scrubb_btn_disable"/>
	</div>
	<%
		session.removeAttribute("future");
	%>
	</div>
</form>
</tmpl:put>

</tmpl:insert>


<script type="text/javascript">
document.getElementById("submit").disabled = true;
document.getElementById("filename").value = "Choose.csv file";
document.getElementById("submit").className = "scrubb_btn_disable";
// document.getElementById('errMsg').removeAttribute('class');
document.getElementById('errMsg').classList.add('errMsg_hidden');

function verifyFile(){
    var fileinput = document.getElementById("fileupload");
       if (fileinput.files.length == 0) {
       	document.getElementById("submit").disabled = true;
       	document.getElementById("filename").value = "Choose.csv file";
       	document.getElementById("submit").className = "scrubb_btn_disable";
    	document.getElementById("submit").disabled = true;
       } else {
            var file = fileinput.files[0];
            if(checkFileType(file.name)){
	            document.getElementById("filename").value = file.name;
	            document.getElementById("submit").disabled = false;
	            document.getElementById('submit').classList.add('scrubb_btn_enable');
// 				document.getElementById('errMsg').removeAttribute('class');
	            document.getElementById('errMsg').classList.add('errMsg_hidden');
	            
            }else{
//             	document.getElementById('errMsg').classList.add('error1');
				document.getElementById("submit").className = "scrubb_btn_disable";
            	document.getElementById("submit").disabled = true;
               	document.getElementById("filename").value = "Choose.csv file";
               	var h1 = document.getElementById("errMsg");
               	var att = document.createAttribute("class");
               	att.value = "error1";
               	h1.setAttributeNode(att);
                document.getElementById('errMsg').innerHTML="Wrong file type. We only support .CSV Files.";
            }
       }
}
function checkFileType(fileName)
{
	if(fileName.length > 0){
		var fileProp = fileName.split(".");
		if(fileProp[1].toLocaleLowerCase() === 'csv'){
			return true;
		}
	}
	return false;    
}

function handleBrowseClick()
{
    var fileinput = document.getElementById("fileupload");
    fileinput.click();
}
</script>