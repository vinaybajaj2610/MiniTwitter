/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 25/7/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */

var tweetpage = 1;


function loadMoreTweets(){
    $.ajax({
        url: "/homepagetweets?page="+tweetpage,
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
    $.ajax({
        url: "/addtweet",
        type:"POST",
        data:JSON.stringify({details:$('#details').val()}),
        contentType:"application/json",
        success:function(){
            console.log("hey");
            alert("New tweet added!!");
            $('#details').val('');
        },
        error: function(){

        }
    });
}


function bindScroll(){
    if((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
        tweetpage++;
        loadMoreTweets();
    }
}

$(window).scroll(bindScroll);