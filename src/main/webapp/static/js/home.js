/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 25/7/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */

var tweetid = 0;
var maxlimit = 140;
var latestTweetid;
var freshTweetData;

var tabActive = 1;


function isUrl(s) {
    var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
    return regexp.test(s);
}
/*
<img border="0" src="http://localhost/images/${userid}.jpg" onError="this.src = 'http://localhost/images/default.jpg'"
alt="Profile Pic" class="img-rounded" style="height:40px; width:40px; float:left">*/

function formTweet(user){

    var arr = user.details.split(" ");
    var divElement = document.createElement("div");
    finaldiv = $(divElement).addClass('well').css('margin', '10px')
        .append($('<img>').addClass("img-circle").css("float","left").css("height","50px").css("width","50px").css("margin-right","10px")
            .attr("src","http://localhost/images/" + user.userid +".jpg").attr("onError","this.src = 'http://localhost/images/default.jpg'"))
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

function loadMoreTweets(){

    $.ajax({
        url: "/homepagetweets?tweetid="+tweetid,
        dataType: 'json',
        success: function(data){
            tweetid = data[data.length-1].tweetid;

            if (data.length > 0 && data[0].tweetid > latestTweetid)
                latestTweetid = data[0].tweetid;

            for(var i=0; i < data.length; i++){
                console.log(formTweet(data[i]));
                $('#tweetfeeds').append(

                    formTweet(data[i])
                );
            }
        }
    });
}

function updateTab() {
    if (document.getElementById('newTweetCount')){
        loadFreshTweets();
    }
    tabActive = 1;
}


$(document).ready(function(){

    latestTweetid  = 0;
    freshTweetData = [];
    $('#cntfield').html('140');
    loadMoreTweets();
    $('#tweetButton').attr("disabled",true);
    setInterval(checkNewTweets, 5000);
});

$(function() {
    $("#search").autocomplete({ // search is the div id
        minLength: 2,
        max : 8,
        source: "http://localhost:8080" + "/loadUsernames?prefix=" + $('#search').text(), // serverAddress is the path to server
        select: function(event, user){
            window.location = "http://localhost:8080/"+user.item.label;
        }
    });
});

function loadFollowers(){
    tabActive = 2;
    $.ajax({
        url: "/followers",
        dataType: 'json',
        success: function(data){
            $('#userfollowers').empty();
            for(var i=0; i < data.length; i++){
                $('#userfollowers').append(
                    $('<div>').addClass('well').css('margin', '10px')
                        .append($('<div>').addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
                        .append($('<div>').addClass("pull-right").text(data[i].email))

                );
            }
        }
    });
}


function loadFollowing(){
    tabActive = 3;
    $.ajax({
        url: "/following",
        dataType: 'json',
        success: function(data){
            $('#userfollowing').empty();
            for(var i=0; i < data.length; i++){
                $('#userfollowing').append(
                    $('<div>').addClass('well').css('margin', '10px')
                        .append($('<div>').addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
                        .append($('<div>').addClass("pull-right").text(data[i].email))

                );
            }
        }
    });
}

function addTweet(){
    if ($('#tweettext').val()!=""){
        $.ajax({
            url: "/addtweet",
            type:"POST",
            data:JSON.stringify({details:$('#tweettext').val()}),
            contentType:"application/json",
            success:function(){

                $('#tweettext').val('');
                $('#cntfield').html('140');
                loadFreshTweets();
                updateJedis();
            },
            error: function(){

            }
        });
        $('#tweetButton').attr("disabled",true);
    }
}

function updateJedis() {

    $.ajax({
        url: "/newTweetJedisUpdate",
        dataType: 'json',
        success: function(){
        }
    });
}

function textCounter() {

    if ($('#tweettext').val().length > 0){
        $('#tweetButton').removeAttr("disabled");
    }
    else
        $('#tweetButton').attr("disabled",true);

    if ($('#tweettext').val().length > maxlimit)
        $('#tweettext').val($('#tweettext').val().substring(0,maxlimit));
    else
        $('#cntfield').html(maxlimit-$('#tweettext').val().length);
}

function bindScroll(){
    if(((window.innerHeight + window.scrollY) >= document.body.offsetHeight && tabActive==1)) {
        loadMoreTweets();
    }
}

$(window).scroll(bindScroll);


function notifyUserForNewTweets(data) {

    $('#newTweetCount').remove();
    $('#tweetfeeds').prepend("<div style='text-align: center; font: bold; color: #ffffff' id='newTweetCount' onclick='loadFreshTweets()'>" + data + " new tweets" + "</div>");
}

function loadFreshTweets(){

    $('#newTweetCount').remove();

    $.ajax({
        url: "/fetchNewTweets?tweetid="+latestTweetid,
        dataType: 'json',
        success: function(data){
            for(var i=data.length-1; i >= 0; i--){
                $('#tweetfeeds').prepend(
                    formTweet(data[i])
                );
            }
            latestTweetid = data[0].tweetid;
            jedisUpdateCountZero();
        }
    });
}

function jedisUpdateCountZero(){
    $.ajax({
        url: "/jedisUpdateCountZero",
        dataType: 'json',
        success: function(){
        }
    });
}
function checkNewTweets() {
    $.ajax({
        url: "/checkNewTweets",
        dataType: 'json',
        success: function(data){
            if(data > 0)
                notifyUserForNewTweets(data);
        }
    });

}

