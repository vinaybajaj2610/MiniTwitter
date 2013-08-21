<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String username = request.getParameter( "username" );
%>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>MiniTwitter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="SHORTCUT ICON" href="/static/img/logo.jpg"/>
    <!-- Le styles -->

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

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }
    </style>

    <script src="/static/js/jquery.js"></script>
    <script src="/static/js/bootstrap.js"></script>
    <link rel="stylesheet" href="/static/css/jquery-ui-1.10.3.custom.min.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <link href="/static/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet">
</head>

<body background="/static/img/home-bg.jpg" style="background-repeat: no-repeat; background-attachment: fixed">

<%@include file="header.jsp" %>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="row-fluid">
                <div class="span3">
                    <div class="well">
                        <div><font size="5"><b>
                            ${profile_username}'s Profile Page</b></font>
                        </div>
                        <br>
                        <div><img border="0" src="http://localhost/images/${profile_id}.jpg" onError="this.src = 'http://localhost/images/default.jpg'"
                                  alt="Profile Pic" class="img-polaroid" style="width: 300px;height: 300px;margin-bottom: 10px">
                        </div>
                        <div>
                            <button class="btn btn-primary" style="margin-right: 5px" onclick="editProfile()">Edit Profile</button>
                            <button id="followed" onclick="follow()" class="btn btn-primary">follow</button>
                            <button id="unfollowed" onmouseover="changeBtnNametounfollow()" onmouseout="changeBtnNametofollowing()" onclick="unfollow()" class="btn btn-primary">following</button>
                        </div>
                        <div>
                        <br>
                        <span>Change Profile Picture:</span>
                        <form method="post" action="saveImage" enctype="multipart/form-data">
                            <input type="file" name="file"/>
                            <input class="btn btn-inverse" type="submit" value="Upload Image"/>
                        </form>
                        </div>
                    </div>

                </div>
                <div class="span7">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active"><a data-toggle="tab" onclick="updateTab()" href="#tweetfeeds"><b>Tweets</b></a></li>
                        <li><a data-toggle="tab" onclick="loadFollowers()" href="#userfollowers"><b>Followers</b></a></li>
                        <li><a data-toggle="tab" onclick="loadFollowing()" href="#userfollowing"><b>Following</b></a></li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active"  id="tweetfeeds"></div>
                        <div class="tab-pane" id="userfollowers"></div>
                        <div class="tab-pane" id="userfollowing"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div><!--/.fluid-container-->
<%@include file="JsProfile.jsp" %>

</body>
</html>