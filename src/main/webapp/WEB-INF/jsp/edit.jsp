<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/static/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="/static/css/edit.css" rel="stylesheet" type="text/css">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <title>Edit Profile</title>
</head>
<body>

<div class="container">
<div class="span4 offset8">
    <div class="well">
        <form id="signup" class="form-vertical" method="post" action="updateProfile">
            <legend>Edit your profile</legend>

            <div class="control-group">
                <div class="controls">
                    Old Password:
                    <div class="input-prepend">
                        <input type="Password" class="input-xlarge" id="oldPassword" name="oldPassword"
                               placeholder="Old Password">
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    Full Name:
                    <div class="input-prepend">
                        <input type="text" class="input-xlarge" id="name" name="name"
                               value="${name}">
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    Email:
                    <br>
                    <div class="input-prepend">
                        <input type="text" class="input-xlarge" id="email" name="email" value="${email}">
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    New Password:
                    <div class="input-prepend">
                        <input type="Password" class="input-xlarge" id="newPassword" name="newPassword"
                               placeholder="New Password">
                    </div>
                </div>
            </div>
            <div class="control-group">
                <div class="controls">
                    Re Enter New Password
                    <div class="input-prepend">
                        <input type="Password" class="input-xlarge" id="newPassword1" name="newPassword1"
                               placeholder="New Password">
                    </div>
                </div>
            </div>


            <div class="control-group">
                <label class="control-label"></label>

                <div class="controls offset1">
                    <button type="submit" class="btn btn-warning">Save Changes</button>
                </div>
            </div>
        </form>
        <div id="message">${msg}</div>
    </div>
</div>
</div>
</body>
</html>