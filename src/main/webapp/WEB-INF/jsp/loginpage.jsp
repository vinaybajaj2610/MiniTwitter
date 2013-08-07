<!DOCTYPE HTML>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="/static/js/login.js"></script>
    <link href="/static/css/login.css" rel="stylesheet" type="text/css">
    <link href="/static/css/bootstrap.css" rel="stylesheet" type="text/css">
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <title>Twitter </title>
</head>

<body background="/static/img/login-bg.jpg">
<%--<div class="offset11">${error}</div>--%>
<div class="container">
    <div class="span4 offset8 " id="top-padding">
        <div class="well">
            <form id="login" class="form-vertical" method="post" action="../../j_spring_security_check">
                <legend>Sign in</legend>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="text" class="input-xlarge" id="j_username" name="j_username"
                                   placeholder="User Name">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="Password" id="j_password" name="j_password" placeholder="Password">
                            <button type="submit" class="btn btn-info">Sign in</button>
                        </div>
                    </div>
                </div>
            </form>
            <div id="error">${error} </div>
        </div>
    </div>

    <%--<div>--%>
    <%--<h2> Welcome To Twitter </h2>--%>
    <%--</div>--%>

    <div class="span4 offset8">
        <div class="well">
            <form id="signup" class="form-vertical" method="post" action="register" onsubmit="return validate(this)">
                <legend>New to Twitter? Sign Up</legend>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="text" class="input-xlarge" id="username" name="username"
                                   placeholder="User Name">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">

                        <div class="input-prepend">
                            <input type="text" class="input-xlarge" id="name" name="name" placeholder="Full Name">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="text" class="input-xlarge" id="email" name="email" placeholder="Email">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="Password" class="input-xlarge" id="password" name="password"
                                   placeholder="Password">
                        </div>
                    </div>
                </div>


                <div class="control-group">
                    <label class="control-label"></label>

                    <div class="controls offset1">
                        <button type="submit" class="btn btn-warning">Create My Account</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>