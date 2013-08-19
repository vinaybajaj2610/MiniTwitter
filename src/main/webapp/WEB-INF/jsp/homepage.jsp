<%--
  Created by IntelliJ IDEA.
  User: vinay
  Date: 24/7/13
  Time: 9:15 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Twitter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

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

        .addtweetdetails{
            width: 97%;
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
    <script src="/static/js/home.js"></script>
    <link rel="stylesheet" href="/static/css/jquery-ui-1.10.3.custom.min.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script src="/static/js/bootstrap-typeahead.js"></script>
    <link href="/static/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet">
</head>

<body background="/static/img/home-bg.jpg" style="background-repeat:no-repeat; background-attachment: fixed" >

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#"><b>MiniTwitter</b></a>
            <div class="nav-collapse collapse">

                <ul class="nav pull-right" >
                    <li>
                        <form id="searchBox" style="margin-bottom: 0; margin-right: 10px">

                            <input id="search" type="text" class="search-query" style="height:20px;margin-top:4px;" placeholder="Search Username">
                            <div class="icon-search icon-white" style="margin-top: 4px"></div>
                        </form>
                    </li>
                    <li>
                        <p class="navbar-text">
                            Logged in as <a href="#" class="navbar-link">${username}</a>
                        </p>
                    </li>
                    <li><a href="auth/logout">Logout</a></li>
                </ul>
                <ul class="nav">
                    <li class="active"><a href="homepage">Home</a></li>
                    <li><a href="${username}">Profile</a></li>
                    <li><a href="editProfile" class="navbar-link" style="text-align: center;">Edit Profile</a></li>

                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="row-fluid">
                <div class="span3">
                    <div class="well">
                        <img border="0" src="http://localhost/images/${userid}.jpg" onError="this.src = 'http://localhost/images/default.jpg'"
                             alt="Profile Pic" class="img-rounded" style="height:40px; width:40px; float:left">
                        <div style="display: inline-block;float: left;"><h3 style="padding-left: 5px;display: inline-block;float: left;"><a href=${username}> ${name}</a></h3>
                            <h5 style="display: inline-block;float: right;margin-top: 22px;padding-left: 4px;">@${username}</h5></div>
                        <textarea onkeydown="textCounter()" onkeyup="textCounter()" class="addtweetdetails" name="details" id="tweettext" rows="4" placeholder="Compose new Tweet..."></textarea>
                        <button onclick="addTweet()" id="tweetButton" class="btn btn-primary ">Tweet</button>
                        <p id="cntfield" class="pull-right"></p>
                        <div>${msg}</div>
                    </div>

                    <br>Please upload a file
                    <form method="post" action="saveImage" enctype="multipart/form-data">
                        <input type="file" name="file"/>
                        <input type="submit" value="Upload Image"/>
                    </form>
                </div>
                <div class="span7">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active"><a data-toggle="tab" onclick="updateTab()" href="#tweetfeeds"><b>Tweets</b></a></li>
                        <li><a data-toggle="tab" onclick="loadFollowers()" href="#userfollowers"><b>Followers</b></a></li>
                        <li><a data-toggle="tab" onclick="loadFollowing()" href="#userfollowing"><b>Following</b></a></li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="tweetfeeds"></div>
                        <div class="tab-pane" id="userfollowers"></div>
                        <div class="tab-pane" id="userfollowing"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div><!--/.fluid-container-->

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->


</body>
</html>
