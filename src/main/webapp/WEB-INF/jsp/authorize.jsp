<!DOCTYPE HTML>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="/static/css/bootstrap.css" rel="stylesheet" type="text/css" >
    <link href="/static/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
    <title>Token Authorization</title>
    <link rel="SHORTCUT ICON" href="/static/img/logo.jpg"/>
</head>

<body>
<div class="container">
    <div class="span4">
        <div class = "well">
            <form id="login" class="form-vertical" method="post" action="authorizeUser">
                <legend>Authorization</legend>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="text" class="input-xlarge" id="username" name="username" placeholder="User Name">
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="text" id="reqtoken" name="requestToken" value=${reqToken}>
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="input-prepend">
                            <input type="Password" id="password" name="password" placeholder="Password">
                            <button type="submit" class="btn btn-info" >Authorize</button>
                        </div>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>
</body>
</html>