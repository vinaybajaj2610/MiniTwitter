
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
                            <div class="icon-search icon-white" style="margin-top: 4px;margin-right: 250px;"></div>
                        </form>
                    </li>
                    <li><a href="auth/logout">Logout</a></li>
                </ul>
                <ul class="nav">
                    <li class="active"><a href="homepage">Home</a></li>
                    <li><a href="${username}">Profile</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>