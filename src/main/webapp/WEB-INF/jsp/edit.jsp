<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/static/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="/static/css/edit.css" rel="stylesheet" type="text/css">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <title>Edit Profile</title>

    <%--<style type="text/css">--%>
    <%--.bs-docs-sidenav {--%>
    <%--width: 228px;--%>
    <%--margin: 30px 0 0;--%>
    <%--padding: 0;--%>
    <%--background-color: #fff;--%>
    <%---webkit-border-radius: 6px;--%>
    <%---moz-border-radius: 6px;--%>
    <%--border-radius: 6px;--%>
    <%---webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, .065);--%>
    <%---moz-box-shadow: 0 1px 4px rgba(0, 0, 0, .065);--%>
    <%--box-shadow: 0 1px 4px rgba(0, 0, 0, .065);--%>
    <%--}--%>

    <%--&lt;%&ndash;.list-link{padding-top:12px;padding-bottom:12px;border-bottom:0}&ndash;%&gt;--%>
    <%--</style>--%>

</head>
<body background="/static/img/home-bg.jpg" style="background-repeat:no-repeat; background-attachment: fixed">

<div class="row">
    <%--<div class="span3 offset1">--%>
    <%--<ul class="nav nav-tabs bs-docs-sidenav affix">--%>
    <%--<li class="active"><a href="#editInfo"><i class="icon-chevron-right"></i> Edit Details</a></li>--%>
    <%--<li class=""><a href="#uploadImage"><i class="icon-chevron-right"></i> Upload Image</a></li>--%>
    <%--</ul>--%>
    <%--</div>--%>

    <div class="span4 offset5">
        <div class="tab-content">
            <div class="tab-pane active" id="editInfo">
                <div class="well">
                    <form id="edit" class="form-vertical" method="post" action="updateProfile">
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
                                Full Name:<br>
                                <div class="input-prepend">
                                    <input type="text" class="input-xlarge" id="name" name="name"
                                           value="${name}">
                                </div>
                            </div>
                        </div>
                        <div class="control-group">
                            <div class="controls">
                                Email:<br>
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
            <%--<div class="tab-pane" id="uploadImage">--%>
            <%--<div class = "well">--%>
            <%--jhbhj--%>
            <%--</div>--%>
            <%--</div>--%>
        </div>
    </div>
</div>
</body>
</html>
