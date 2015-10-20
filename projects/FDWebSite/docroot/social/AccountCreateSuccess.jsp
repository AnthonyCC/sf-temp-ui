<!DOCTYPE html>
<html>
<body>

<h4 align="center">Congratulations!</h4>
<br/><br/>
<h4 align="center">Your account has been created!</h4>
<br/><br/>

<button onclick="close_window()" style="width: 200px;padding: 10px; margin-top: 0px; background-color: #00B800; color: #ffffff; text-align: center;
	border-radius: 5px; margin-left: 20px;">Begin Shopping</button>

<script>
function close_window()
{
	window.top['FreshDirect'].components.ifrPopup.close();
} 
</script>

</body>
</html>