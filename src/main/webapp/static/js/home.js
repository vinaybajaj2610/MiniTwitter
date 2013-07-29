/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 25/7/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */


function loadTweets(){
    $.ajax({
        url: "/homepagetweets",
        dataType: 'json',
        success: function(data){
            for(var i=0; i < data.length; i++){
                $('#tweetfeeds').append(
                    $('<div>').addClass('well')
                        .append($('<div>').text(data[i].details))
                        .append($('<div>').text("by: ").addClass("pull-left").append($('<a>').text(data[i].username).attr("href","/"+data[i].username)))
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