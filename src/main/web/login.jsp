<%--
  Created by IntelliJ IDEA.
  User: Inmelet
  Date: 8/8/2016
  Time: 10:24 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Event Management</title>
    <link rel="stylesheet" type="text/css" href="./css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="./css/reset.css">
    <link rel="stylesheet" type="text/css" href="./css/main.css">
    <link rel="stylesheet" type="text/css" href="./css/icon_font.css">


    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">

    <script type="text/javascript" src="./js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="./js/jquery-ui.min.js"></script>

</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="/jsp/header.jsp"/>
    <!-- End Main Header -->

    <div class="zoom-anim-dialog small-dialog login_popup mfp-hide" id="login-popup">
        <form id="login_form">
            <div class="lf_user_row">
                <span class="lf_header">Login to your Account</span>
            </div>
            <div class="lf_user_row">
                <label for="login_user_name">
                    <i class="lf_icon icon-user"></i>
                    <input name="login_user_name" id="login_user_name" type="text">
                </label>
            </div>
            <div class="lf_user_row">
                <label for="login_password">
                    <i class="lf_icon icon-lock"></i>
                    <input name="login_password" id="login_password" type="password">
                </label>
            </div>
            <div class="lf_user_row clearfix">
                <div class="form_col_half">
                    <label for="rememberme">
                    <span class="remember-box">
                        <input id="rememberme" name="rememberme" type="checkbox">
                        <span>Remember me</span>
                    </span>
                    </label>
                </div>
                <div class="form_col_half clearfix">
                    <button type="submit" name="login" class="btn f_right">
                        Sign in
                    </button>
                </div>
            </div>
            <a class="lf_forget_pass" href="#">Forgot Your Password?</a>
        </form>
    </div>

    <jsp:include page="/jsp/footer.jsp"/>
</div>
</body>
</html>
