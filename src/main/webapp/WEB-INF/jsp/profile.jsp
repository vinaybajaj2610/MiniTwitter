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
    <%--<script src="/static/js/profile.js"></script>--%>
    <link href="/static/css/bootstrap.css" rel="stylesheet">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet">
</head>

<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span12">
            <div class="row-fluid">
                <div class="span3">
                    <div class="well">
                        <div><font size="5"><b>
                            <%--<a href="/users/${profile_username}">--%>
                            ${profile_username}'s
                                <%--</a> --%>
                                Profile Page</b></font></div>
                        <%--<textarea rows="3" cols="40" style="margin: 0px 0px 10px; width: 363px; height: 101px;"></textarea>--%>
                        <%--<button class="btn btn-primary">Compose new Tweet</button>--%>
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

    $(document).ready(function(){
        loadTweets();
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