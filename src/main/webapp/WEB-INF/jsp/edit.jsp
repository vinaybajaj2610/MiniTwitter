<!DOCTYPE HTML>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/static/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <link rel="SHORTCUT ICON" href="/static/img/logo.jpg"/>
    <style type="text/css">
        .ui-autocomplete {
            max-height: 205px;
            overflow-y: hidden;
            overflow-x: hidden;
        }

        body {
            padding-top: 60px;
            padding-bottom: 40px;
        }
        .sidebar-nav {
            padding: 9px 0;
        }

        .addtweetdetails{
            width: 97%;
        }

    </style>
    <script src="/static/js/jquery.js"></script>
    <script src="/static/js/bootstrap.js"></script>
    <link href="/static/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/jquery-ui-1.10.3.custom.min.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="/static/js/home.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <title>Edit Profile</title>


</head>
<body background="/static/img/home-bg.jpg" style="background-repeat:no-repeat; background-attachment: fixed">
<%@include file="header.jsp" %>
<div class="container">
    <div class="span4 offset4">
        <div class="well" style="margin-top: 50px">
            <form id="edit" class="form-vertical" method="post" action="updateProfile">
                <legend>Edit your profile</legend>

                <div class="control-group">
                    <div class="controls">
                        <%--Old Password:*--%>
                        <div class="input-prepend">
                            <input type="Password" class="input-xlarge" id="oldPassword" name="oldPassword"
                                   placeholder="Old Password*">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="text" class="input-xlarge" id="name" name="name" placeholder="Full Name"
                                   value="${name}">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <%--Email:<br>--%>
                        <div class="input-prepend">
                            <input type="text" class="input-xlarge" id="email" name="email" value="${email}" placeholder="Email">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <%--New Password:--%>
                        <div class="input-prepend">
                            <input type="Password" class="input-xlarge" id="newPassword" name="newPassword" placeholder="New Password"
                                   placeholder="New Password">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <%--Re Enter New Password--%>
                        <div class="input-prepend">
                            <input type="Password" class="input-xlarge" id="newPassword1" name="newPassword1"
                                   placeholder="Re Enter New Password">
                        </div>
                    </div>
                </div>


                <div class="control-group">
                    <label class="control-label"></label>

                    <div class="controls offset1">
                        <button type="submit" class="btn btn-success" style="margin-left: 39px">Save Changes</button>

                    </div>
                </div>
            </form>
            <div id="message"><font color="red">${msg}</font></div>
        </div>
</div>
</body>
</html>
