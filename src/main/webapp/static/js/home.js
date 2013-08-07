/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 25/7/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */

var tweetid = 0;
var maxlimit = 140;


function loadMoreTweets(){

    $.ajax({
        url: "/homepagetweets?tweetid="+tweetid,
        dataType: 'json',
        success: function(data){
            tweetid = data[data.length-1].tweetid;
            console.log(tweetid);
            for(var i=0; i < data.length; i++){
                $('#tweetfeeds').append(
                    $('<div>').addClass('well')
                        .append($('<div>').text(data[i].details))
                        .append($('<div>').text("by: ").addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
                        .append($('<div>').text(data[i].timestamp).addClass("pull-right"))
                );
                //tweetid = data[i].tweetid;
            }
        }
    });
}

$(document).ready(function(){
    $('#cntfield').html('140');
    loadMoreTweets();
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
    }
}


function textCounter() {
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