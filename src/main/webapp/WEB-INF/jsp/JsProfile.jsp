<script>
    var tweetid = 0;
    var tabActive =1;
    var serverAddress ="http://localhost:8080";

    function editProfile(){
        window.location = serverAddress + "/editProfile";
    }

    $(function() {
        $("#search").autocomplete({ // search is the div id
            minLength: 2,
            max : 8,
            source: "http://localhost:8080" + "/loadUsernames?prefix=" + $('#search').text(), // serverAddress is the path to server
            select: function(event, user){
                window.location = serverAddress + user.item.label;
            }
        });
    });

    function isUrl(s) {
        var regexp = /(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/
        return regexp.test(s);
    }

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
                console.log("id=" + data);
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
                            $('<div>').addClass('well').css('margin', '10px')
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
                            $('<div>').addClass('well').css('margin', '10px')
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