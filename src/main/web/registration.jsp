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
    <link rel="stylesheet" type="text/css" href="css/reset.css">
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="./css/icon_font.css">

    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.structure.min.css">
    <link rel="stylesheet" type="text/css" href="./css/jquery-ui.theme.min.css">

    <link href='https://fonts.googleapis.com/css?family=Oswald:400,300,700' rel='stylesheet' type='text/css'>
    <link href="http://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">

    <script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/event_list.js"></script>
</head>
<body>
<div id="main_wrapper">
    <!-- Main Header -->
    <jsp:include page="/jsp/header.jsp"/>
    <!-- End Main Header -->

    <!-- Content Section -->
    <section class="content_section">
        <div class="content">
            <div class="form_header">
                <div class="main_title upper">
                    <h2>
                        New user?
                        <span class="highlighted"> Register Now.</span>
                    </h2>
                </div>
            </div>
            <form id="registration_form">
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="first-name">
                            <span class="field_name">First Name</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="first-name" id="first-name" type="text">
                    </div>
                    <div class="form_col_half ">
                        <label for="last-name">
                            <span class="field_name">Last Name</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="last-name" id="last-name" type="text">
                    </div>
                </div>
                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="mail">
                            <span class="field_name">Email</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="mail" id="mail" type="text">
                    </div>
                    <div class="form_col_half">
                        <label for="confirm-mail">
                            <span class="field_name">Confirm Email</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="confirm-mail" id="confirm-mail" type="text">
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="password">
                            <span class="field_name">Password</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="password" id="password" type="password">
                    </div>
                    <div class="form_col_half">
                        <label for="confirm-password">
                            <span class="field_name">Confirm Password</span>
                            <span class="required_star">*</span>
                        </label>
                        <input class="input_text" name="confirm-password" id="confirm-password" type="password">
                    </div>
                </div>

                <div class="form_row clearfix">
                    <div class="form_col_half">
                        <label for="phone">
                            <span class="field_name">Phone Number</span>
                        </label>
                        <input class="input_text" name="phone" id="phone" type="text">
                    </div>
                    <div class="form_col_half">
                        <label for="avatar">
                            <span class="field_name">Avatar</span>
                        </label>
                        <div class="file_button_wrapper">
                            <input class="input_file" name="avatar" id="avatar" type="file">
                        </div>
                    </div>
                </div>
                <div class="form_row clearfix">
                    <button type="submit" name="send" class="btn full_button">
                        <i class="ico-check"></i>
                        <span>Register</span>
                    </button>
                </div>

            </form>
        </div>
    </section>
    <!-- End Content Section -->


    <!-- Footer -->
    <jsp:include page="/jsp/footer.jsp"/>
    <!-- End Footer -->
</div>
</body>
</html>
