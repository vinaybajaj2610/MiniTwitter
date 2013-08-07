<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String username = request.getParameter( "username" );
%>

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
    <%--<script src="/static/js/profile.js"></script>--%>
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
            <a class="brand" href="#">Twitter</a>
            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    Logged in as <a href="#" class="navbar-link">${username}</a>
                </p>
                <ul class="nav">
                    <li class="active"><a href="#">Home</a></li>
                    <li><a href="${username}">Profile</a></li>
                    <li><a href="editProfile" class="navbar-link" style="text-align: center;">Edit Profile</a></li>
                    <li><a href="auth/logout">Logout</a></li>
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
                        <div><font size="5"><b>
                            ${profile_username}'s Profile Page</b></font></div>
                        </div>
                    <button id="followed" onclick="follow()" class="btn btn-primary">follow</button>
                    <button id="unfollowed" onmouseover="changeBtnNametounfollow()" onmouseout="changeBtnNametofollowing()" onclick="unfollow()" class="btn btn-primary">following</button>

                </div>
                <div class="span7">
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


<script>

    function loadTweets(){
        $.ajax({
            url: "/tweets/${profile_id}",
            dataType: 'json',
            success: function(data){
                for(var i=0; i < data.length; i++){
                    $('#tweetfeeds').append(
                            $('<div>').addClass('well')
                                    .append($('<div>').text(data[i].details))
                                    .append($('<div>').text("by: ").addClass("pull-left").append($('<a>').text(data[i].username).attr("href","${profile_username}")))
                                    .append($('<div>').text(data[i].timestamp).addClass("pull-right"))
                    );
                }
            }
        });
    }

    function followUnfollowButton(){
        $.ajax({
            url: "/checkfollow/${profile_id}",
            dataType: 'json',
            success: function(data){
                if(data == 0) {
                    $('#unfollowed').hide();
                }
                else if(data == 1){
                    $('#followed').hide();
                }
                else {
                    $('#followed').hide();
                    $('#unfollowed').hide();
                }
            }
        });
    }

    function followUnfollow(flag) {
        if(flag == "unfollow") {
            $('#unfollowed').hide();
            $('#followed').show();

        }
        else {
            $('#unfollowed').show();
            $('#followed').hide();
        }
    }

    $(document).ready(function(){
        loadTweets();
        followUnfollowButton();
    });


    function loadFollowers(){
        $.ajax({
            url: "/followers/${profile_id}",
            dataType: 'json',
            success: function(data){

                $('#userfollowers').empty();
                for(var i=0; i < data.length; i++){
                    $('#userfollowers').append(
                            $('<div>').addClass('well')
                                    .append($('<div>').addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
                    );
                }
            }
        });
    }

    function follow() {
        $.ajax({
            url: "/follow/${profile_id}",
            success: function(){
                followUnfollow("follow");
            }
        });
    }

    function unfollow() {
        $.ajax({
            url: "/unfollow/${profile_id}",
            success: function(){
                followUnfollow("unfollow");
            }
        });
    }

    function changeBtnNametounfollow(){
        $('#unfollowed').removeClass("btn btn-primary");
        $('#unfollowed').addClass("btn btn-danger");
        $('#unfollowed').html("unfollow");
    }

    function changeBtnNametofollowing(){
        $('#unfollowed').removeClass("btn btn-danger");
        $('#unfollowed').addClass("btn btn-primary");
        $('#unfollowed').html("following");
    }


    function loadFollowing(){
        $.ajax({
            url: "/following/${profile_id}",
            dataType: 'json',
            success: function(data){
                $('#userfollowing').empty();
                for(var i=0; i < data.length; i++){
                    $('#userfollowing').append(
                            $('<div>').addClass('well')
                                    .append($('<div>').addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
                    );
                }
            }
        });
    }
</script>
</body>
</html>