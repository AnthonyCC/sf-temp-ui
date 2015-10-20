<!DOCTYPE html>
<html>
<body>

<h4 align="center"><b>Create Account:</b></h4>
<br/><br/>
<h4 align="center">An account with the referenced social network already exists.</h4>
<br/><br/>
<h4 align="center">You are now signed in.</h4>
<br/><br/>

<button onclick="close_window()" style="width: 200px;padding: 10px; margin-top: 0px; background-color: #00B800; color: #ffffff; text-align: center;
	border-radius: 5px; margin-left: 50px;">Continue</button>

<script>
function close_window()
{
	window.top['FreshDirect'].components.ifrPopup.close();
} 
</script>

</body>
</html>