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
  body { background-color: #fff;  }
  .lgcontent {
    vertical-align: middle;
    margin-top: 175px;
  }
  .logincont { 
    background: url(./images/login_bg.jpg) no-repeat;
    height: 502px;
    width: 662px;
    margin-left: auto;
    margin-right: auto;
  }
  .lgform {
    vertical-align: middle;
    margin-top: 25%;
    margin-left: auto;
    margin-right: auto;
  }
  .lgtitle {
    font-size: 22px;
    font-weight: bold;
  }
</style>
</head>

<body>
<div class="lgcontent">

  <div class="logincont">

    <form name="login" method="POST" action="j_security_check">

      <table class="lgform">
        <tr><td colspan="2" class="lgtitle">
          <span style="color: #f93;">Fresh</span>
          <span style="color: #693;">Direct</span> - Transportation<br/><br/></td>
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
