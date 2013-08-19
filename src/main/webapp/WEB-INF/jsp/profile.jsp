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

<body background="/static/img/home-bg.jpg" style="background-repeat: no-repeat; background-attachment: fixed">

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
                        <div><font size="5"><b>
                            ${profile_username}'s Profile Page</b></font></div>
                        </div>
                    <img border="0" src="http://localhost/images/${userid}.jpg" onError="this.src = 'http://localhost/images/default.jpg'" alt="Profile Pic" width="300" height="150">s
                    <button id="followed" onclick="follow()" class="btn btn-primary">follow</button>
                    <button id="unfollowed" onmouseover="changeBtnNametounfollow()" onmouseout="changeBtnNametofollowing()" onclick="unfollow()" class="btn btn-primary">following</button>


                </div>
                <div class="span7">
                    <ul class="nav nav-tabs" id="myTab">
                        <li class="active"><a data-toggle="tab" onclick="updateTab()" href="#tweetfeeds">Tweets</a></li>
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
    var tweetid = 0;
    var tabActive =1;

    function isUrl(s) {
        var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
        return regexp.test(s);
    }

    function formTweet(user){

        var arr = user.details.split(" ");
        var divElement = document.createElement("div");
        finaldiv = $(divElement).addClass('well').css('margin', '10px')
                .append($('<div>').addClass("pull-left").append($('<a>').text(user.username).attr("href", "/" + user.username)))
                .append($('<div>').text(user.timestamp).addClass("pull-right"))
                .append($('</br>'))

        for (var i = 0; i < arr.length; i++){
            if (isUrl(arr[i])){
                finaldiv.append($('<a>').text(arr[i] + " ").attr("href", arr[i]).attr("target", "_blank"));
            }
            else if (arr[i][0]=='@'){
                var tagName = arr[i].substr(1);
                $.ajax({
                    url: "/isFollowing?tagName="+tagName,
                    dataType: 'json',
                    async: false,
                    success: function(data){
                        if (data == 1){
                            finaldiv.append($('<a>').text("@" + tagName + " ").attr("href", "/" + tagName).attr("target", "_blank"));
                        }
                        else
                        {
                            finaldiv.append($('<span>').text(arr[i]+ " "));
                        }
                    }
                });
            }
            else {
                finaldiv.append($('<span>').text(arr[i] + " "));
            }
        }
        return finaldiv;
    }

    /*function loadTweets(){
        $.ajax({
            url: "/tweets/${profile_id}",
            dataType: 'json',
            success: function(data){
                for(var i=0; i < data.length; i++){
                    $('#tweetfeeds').append(
                        formTweet(data[i])
                    );
                }
            }
        });
    }*/

    function loadTweets(){
        $.ajax({
            url: "/tweets/${profile_id}/"+tweetid,
            dataType: 'json',
            success: function(data){
                tweetid = data[data.length-1].tweetid;
                for(var i=0; i < data.length; i++){
                    $('#tweetfeeds').append(
                        formTweet(data[i])
                    );
                }
            }
        });
    }

    function updateTab() {
        tabActive = 1;
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
        tabActive = 2;
        $.ajax({
            url: "/followers/${profile_id}",
            dataType: 'json',
            success: function(data){

                $('#userfollowers').empty();
                for(var i=0; i < data.length; i++){
                    $('#userfollowers').append(
                            $('<div>').addClass('well')
                                    .append($('<div>').addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
                                    .append($('<div>').addClass("pull-right").text(data[i].email))
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
        tabActive = 3;
        $.ajax({
            url: "/following/${profile_id}",
            dataType: 'json',
            success: function(data){
                $('#userfollowing').empty();
                for(var i=0; i < data.length; i++){
                    $('#userfollowing').append(
                            $('<div>').addClass('well')
                                    .append($('<div>').addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
                                    .append($('<div>').addClass("pull-right").text(data[i].email))
                    );
                }
            }
        });
    }

    function bindScroll(){
        if(((window.innerHeight + window.scrollY) >= document.body.offsetHeight && tabActive==1)) {
            loadTweets();
        }
    }

    $(window).scroll(bindScroll);
</script>
</body>
</html>