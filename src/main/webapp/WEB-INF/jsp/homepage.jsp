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

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Bootstrap, from Twitter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->

    <style type="text/css">
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
    <script src="/static/js/home.js"></script>
    <link href="/static/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet">
</head>

<body>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#">Project name</a>
            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    Logged in as <a href="#" class="navbar-link">Username</a>
                </p>
                <ul class="nav">
                    <li class="active"><a href="#">Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#contact">Contact</a></li>
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
                        <div><font size="5"><b><a href="/users/${username}">${username}'s</a> Home page</b></font></div>
                        <textarea rows="3" cols="40" style="margin: 0px 0px 10px; width: 363px; height: 101px;"></textarea>
                        <button class="btn btn-primary">Compose new Tweet</button>
                    </div>

                </div>
                <div class="span6">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active"><a data-toggle="tab" href="#tweetfeeds">Tweets</a></li>
                        <li><a data-toggle="tab" onclick="loadFollowers()" href="#userfollowers">Followers</a></li>
                        <li><a data-toggle="tab" onclick="loadFollowing()" href="#userfollowing">Following</a></li>
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

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->


</body>
</html>
