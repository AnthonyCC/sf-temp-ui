<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>FreshDirect Transportation Admin</title>
<script>
  window.onload = function () {
    if (top.location != location) {
      top.location.href = document.location.href;
    }
    document.login.j_username.focus();
  }
</script>

<style>
.login_container {
	position: absolute;
	top: 25%;
	left: 25%;
	background: url(./images/login_bg.jpg) no-repeat;
	height: 502px;
	width: 662px;
}
.login_content { 
	position: relative;
	top: 37%;
	left: 30%;
}
.login_form {
	border: 0px;
}
.login_form_title {
	font-size: 22px;
	font-weight: bold;
}
.login_fresh { color: #f93; }
.login_direct { color: #693; }
</style>
 
</head>

<body>
<div class="login_container">
	<div class="login_content">
		<form name="login" method="POST" action="j_security_check">
			<table class="login_form">
				<tr>
					<td colspan="2" class="login_form_title">
						<span class="login_fresh">Fresh</span>
						<span class="login_direct">Direct</span> - Transportation<br/><br/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<label for="username">Username</label>
					</td>
					<td>
						<input id="username" type="text" name="j_username"/>
					</td>
				</tr>
				<tr>
					<td class="label">
						<label for="password">Password</label>
					</td>
					<td>
						<input id="password" type="password" name="j_password"/>
					</td>
				</tr>
				<tr>
					<td><br /><br /></td>
					<td>
						<input type="submit" value="Login" class="button" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>

</body>
</html>
