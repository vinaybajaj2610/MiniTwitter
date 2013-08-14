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


function formTweet(user){
    /*var tweetHtml = "<div class='well' style='margin: 10px'>" +
                        "<div class='pull-left'>" + "<a href=/" + user['username'] + ">" + user['username'] + "</a></div>" +
                        "<div class='pull-right'>"+ user['timestamp'] + "</div>" +
                        "<br/><div>"+ user['details'] +"</div>" +
                     "</div>";
    console.log(tweetHtml);*/

    return $('<div>').addClass('well').css('margin','10px')
        .append($('<div>').addClass("pull-left").append($('<a>').text(user.username).attr("href","/"+user.username)))
        .append($('<div>').text(user.timestamp).addClass("pull-right"))
        .append($('</br>'))
        .append($('<div>').text(user.details));

}

function loadMoreTweets(){

    $.ajax({
        url: "/homepagetweets?tweetid="+tweetid,
        dataType: 'json',
        success: function(data){
            tweetid = data[data.length-1].tweetid;
            console.log(tweetid);

            if (data.length > 0 && data[0].tweetid > latestTweetid)
                latestTweetid = data[0].tweetid;

            for(var i=0; i < data.length; i++){
                $('#tweetfeeds').append(
                    formTweet(data[i])
                );
            }
        }
    });
}

$(document).ready(function(){

    latestTweetid  = 0;
    freshTweetData = [];
    $('#cntfield').html('140');
    loadMoreTweets();
    $('#tweetButton').attr("disabled",true);
    setInterval(checkNewTweets, 15000);
});

function loadFollowers(){
    $.ajax({
        url: "/followers",
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
        url: "/following",
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

function addTweet(){
    if ($('#tweettext').val()!=""){
        $.ajax({
            url: "/addtweet",
            type:"POST",
            data:JSON.stringify({details:$('#tweettext').val()}),
            contentType:"application/json",
            success:function(){
                console.log("hey");
                alert("New tweet added!!");
                $('#tweettext').val('');
                $('#cntfield').html('140');
            },
            error: function(){

            }
        });
        $('#tweetButton').attr("disabled",true);
    }

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
    if((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
        loadMoreTweets();
    }
}

$(window).scroll(bindScroll);


function notifyUserForNewTweets(data) {

    if (data.length > 0){
        freshTweetData = $.merge(data,freshTweetData);
        $('#newTweetCount').remove();
        $('#tweetfeeds').prepend("<div style='text-align: center; font: bold; color: #ffffff' id='newTweetCount' onclick='loadFreshTweets()'>" + freshTweetData.length + " new tweets" + "</div>");
        latestTweetid = freshTweetData[0].tweetid;
    }
}

function loadFreshTweets(){

    $('#newTweetCount').remove();
    for(var i=freshTweetData.length-1; i >= 0; i--){
        $('#tweetfeeds').prepend(
            formTweet(freshTweetData[i])
        );
    }
    freshTweetData = [];

}
function checkNewTweets() {
    $.ajax({
        url: "/checkNewTweets?tweetid="+latestTweetid,
        dataType: 'json',
        success: function(data){
            notifyUserForNewTweets(data);
        }
    });

}

