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
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="css/reset.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/icon_font.css">


    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">

    <script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>

    <script type="text/javascript">

        $(document).ready(function () {
            $('#login_form').on('submit', function (e) {
                e.preventDefault();
                var username = $('#username').val();
                var password = $('#password').val();

                $.ajax({
                    type: 'POST',
                    data: {
                        username: username,
                        password: password
                    },
                    url: '/login',
                    success: function (result) {
                        window.location =
                    },
                    error: function () {
                    }
                })
            })

        });

    </script>

</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="/jsp/header.jsp"/>
    <!-- End Main Header -->

    <div class="zoom-anim-dialog small-dialog login_popup mfp-hide" id="login-popup">
        <form id="login_form" method="POST" action="/login">
            <div class="lf_user_row">
                <span class="lf_header">Login to your Account</span>
            </div>
            <div class="lf_user_row">
                <label for="username">
                    <i class="lf_icon icon-user"></i>
                    <input name="username" id="username" type="text">
                </label>
            </div>
            <div class="lf_user_row">
                <label for="password">
                    <i class="lf_icon icon-lock"></i>
                    <input name="password" id="password" type="password">
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
                    <button name="login" class="btn f_right" id="login">
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
